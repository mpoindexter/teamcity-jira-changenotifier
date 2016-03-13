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

import com.github.mpoindexter.teamcityjiranotifier.ui.JiraNotifierPaths;
import com.github.mpoindexter.teamcityjiranotifier.ui.JiraNotifierPropertiesProcessor;
import jetbrains.buildServer.serverSide.BuildFeature;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class JiraNotifierFeature extends BuildFeature {
    public static final String FEATURE_TYPE = "com.github.mpoindexter.jiranotifier";
    private static final JiraNotifierPropertiesProcessor PROPERTIES_PROCESSOR = new JiraNotifierPropertiesProcessor();

    private final JiraNotifierPaths paths;

    public JiraNotifierFeature(JiraNotifierPaths paths) {
        this.paths = paths;
    }

    @NotNull
    @Override
    public String getType() {
        return FEATURE_TYPE;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Jira issue notifier";
    }

    @Nullable
    @Override
    public String getEditParametersUrl() {
        return paths.getSettingsControllerPath();
    }

    @NotNull
    @Override
    public String describeParameters(@NotNull Map<String, String> params) {
        return "Jira Notifier Settings";
    }

    @Nullable
    @Override
    public PropertiesProcessor getParametersProcessor() {
        return PROPERTIES_PROCESSOR;
    }

    @Nullable
    @Override
    public Map<String, String> getDefaultParameters() {
        Map<String, String> r = new HashMap<String, String>();
        r.put(JiraNotifierSettingsConstants.SETTING_ACTIVATION_POLICY, JiraNotifierSettingsConstants.ACTIVATION_POLICY_ALWAYS);
        r.put(JiraNotifierSettingsConstants.SETTING_ISSUE_SELECTION_POLICY, JiraNotifierSettingsConstants.ISSUE_SELECTION_POLICY_SINCE_LAST_SUCCESS);
        return r;
    }

    @Override
    public boolean isMultipleFeaturesPerBuildTypeAllowed() {
        return true;
    }
}