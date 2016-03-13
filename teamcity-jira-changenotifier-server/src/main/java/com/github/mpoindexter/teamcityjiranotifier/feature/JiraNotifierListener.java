/*
 * Copyright 2016 Michael Poindexter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.mpoindexter.teamcityjiranotifier.feature;

import jetbrains.buildServer.issueTracker.Issue;
import jetbrains.buildServer.issueTracker.IssueProvider;
import jetbrains.buildServer.messages.Status;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.serverSide.executors.ExecutorServices;
import jetbrains.buildServer.util.EventDispatcher;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.vcs.SVcsModification;
import jetbrains.buildServer.vcs.SelectPrevBuildPolicy;
import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.JiraClient;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class JiraNotifierListener {
    private final WebLinks web;
    private final ExecutorService executor;

    public JiraNotifierListener(@NotNull final EventDispatcher<BuildServerListener> listener,
                                @NotNull final ExecutorServices services,
                                @NotNull final WebLinks web) {
        this.web = web;
        this.executor = services.getLowPriorityExecutorService();
        listener.addListener(new BuildServerAdapter() {
            @Override
            public void buildFinished(@NotNull SRunningBuild build) {
                updateJira(build);
            }
        });
    }

    private void updateJira(SRunningBuild build) {
        SBuildType bt = build.getBuildType();
        if (bt == null) {
            return;
        }

        for (SBuildFeatureDescriptor feature : bt.getResolvedSettings().getBuildFeatures()) {
            if (!feature.getType().equals(JiraNotifierFeature.FEATURE_TYPE)) {
                continue;
            }

            Map<String, String> params = feature.getParameters();

            boolean active = false;
            if (JiraNotifierSettingsConstants.ACTIVATION_POLICY_ALWAYS.equals(params.get(JiraNotifierSettingsConstants.SETTING_ACTIVATION_POLICY))) {
                active = true;
            } else if (JiraNotifierSettingsConstants.ACTIVATION_POLICY_SUCCESS.equals(params.get(JiraNotifierSettingsConstants.SETTING_ACTIVATION_POLICY))) {
                active = build.getBuildStatus().isSuccessful();
            } else if (JiraNotifierSettingsConstants.ACTIVATION_POLICY_FAILURE.equals(params.get(JiraNotifierSettingsConstants.SETTING_ACTIVATION_POLICY))) {
                active = build.getBuildStatus().isFailed();
            }

            if (!active) {
                continue;
            }

            List<SVcsModification> changes;
            if (JiraNotifierSettingsConstants.ISSUE_SELECTION_POLICY_SINCE_LAST_SUCCESS.equals(params.get(JiraNotifierSettingsConstants.SETTING_ISSUE_SELECTION_POLICY))) {
                changes = build.getChanges(SelectPrevBuildPolicy.SINCE_LAST_SUCCESSFULLY_FINISHED_BUILD, true);
            } else if (JiraNotifierSettingsConstants.ISSUE_SELECTION_POLICY_LAST_BUILD.equals(params.get(JiraNotifierSettingsConstants.SETTING_ISSUE_SELECTION_POLICY))) {
                changes = build.getChanges(SelectPrevBuildPolicy.SINCE_LAST_BUILD, true);
            } else {
                continue;
            }

            for (SVcsModification change : changes) {
                for (Issue issue : change.getRelatedIssues()) {
                    IssueProvider provider = issue.getProvider();
                    if (!"jira".equals(provider.getType())) {
                        continue;
                    }

                    String url = provider.getProperties().get("host");
                    String user = provider.getProperties().get("username");
                    String password = provider.getProperties().get("secure:password");
                    String issueKey = issue.getId();

                    boolean comment = !StringUtil.isEmptyOrSpaces(params.get(JiraNotifierSettingsConstants.SETTING_COMMENT));
                    boolean transition = !StringUtil.isEmptyOrSpaces(params.get(JiraNotifierSettingsConstants.SETTING_TRANSITION));
                    JiraNotifierTransitionMap transitionMap = JiraNotifierTransitionMap.parse(params.get(JiraNotifierSettingsConstants.SETTING_TRANSITION_MAP));

                    executor.submit(new UpdateIssueTask(url, user, password, issueKey, build, comment, transition, transitionMap));
                }
            }
        }
    }

    private class UpdateIssueTask implements Callable<Void> {
        private final String baseUrl;
        private final String user;
        private final String password;
        private final String issueKey;
        private final SRunningBuild build;
        private final boolean comment;
        private final boolean transition;
        private final JiraNotifierTransitionMap transitionMap;

        public UpdateIssueTask(String baseUrl, String user, String password, String issueKey, SRunningBuild build, boolean comment, boolean transition, JiraNotifierTransitionMap transitionMap) {
            this.baseUrl = baseUrl;
            this.user = user;
            this.password = password;
            this.issueKey = issueKey;
            this.build = build;
            this.comment = comment;
            this.transition = transition;
            this.transitionMap = transitionMap;
        }

        @Override
        public Void call() throws Exception {
            try {
                BasicCredentials creds = new BasicCredentials(user, password);
                JiraClient jira = new JiraClient(baseUrl, creds);
                net.rcarz.jiraclient.Issue issue = jira.getIssue(issueKey);

                if (transition) {
                    String status = issue.getStatus().getName();
                    if (status != null) {
                        String transition = transitionMap.getTransition(status);
                        if (transition != null) {
                            issue.transition().execute(transition);
                        }
                    }
                }

                if (comment) {
                    final StringBuilder comment = new StringBuilder();
                    comment.append("TeamCity ");
                    final SBuildType bt = build.getBuildType();
                    if (bt != null) {
                        comment.append(bt.getFullName());
                    }
                    comment.append(" [Build ");
                    comment.append(build.getBuildNumber());
                    comment.append("](");
                    comment.append(web.getViewResultsUrl(build));
                    comment.append(") ");
                    comment.append("outcome was **").append(build.getStatusDescriptor().getStatus().getText()).append("**");
                    issue.addComment(comment.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
