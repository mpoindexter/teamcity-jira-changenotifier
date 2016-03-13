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

public class JiraNotifierSettingsConstants {
    public static final String SETTING_ACTIVATION_POLICY = "jira_activation_policy";
    public static final String SETTING_ISSUE_SELECTION_POLICY = "jira_issue_selection";
    public static final String SETTING_COMMENT= "jira_comment";
    public static final String SETTING_TRANSITION = "jira_transition";
    public static final String SETTING_TRANSITION_MAP = "jira_transition_map";

    public static final String ACTIVATION_POLICY_SUCCESS = "success";
    public static final String ACTIVATION_POLICY_FAILURE = "failure";
    public static final String ACTIVATION_POLICY_ALWAYS = "always";

    public static final String ISSUE_SELECTION_POLICY_SINCE_LAST_SUCCESS = "since-last-success";
    public static final String ISSUE_SELECTION_POLICY_LAST_BUILD = "last-build";

    public String getSettingActivationPolicy() {
        return SETTING_ACTIVATION_POLICY;
    }

    public String getSettingComment() {
        return SETTING_COMMENT;
    }

    public String getSettingIssueSelectionPolicy() {
        return SETTING_ISSUE_SELECTION_POLICY;
    }

    public String getSettingTransition() {
        return SETTING_TRANSITION;
    }

    public String getSettingTransitionMap() {
        return SETTING_TRANSITION_MAP;
    }

    public String getActivationPolicyAlways() {
        return ACTIVATION_POLICY_ALWAYS;
    }

    public String getActivationPolicyFailure() {
        return ACTIVATION_POLICY_FAILURE;
    }

    public String getActivationPolicySuccess() {
        return ACTIVATION_POLICY_SUCCESS;
    }

    public String getIssueSelectionPolicyLastBuild() {
        return ISSUE_SELECTION_POLICY_LAST_BUILD;
    }

    public String getIssueSelectionPolicySinceLastSuccess() {
        return ISSUE_SELECTION_POLICY_SINCE_LAST_SUCCESS;
    }
}
