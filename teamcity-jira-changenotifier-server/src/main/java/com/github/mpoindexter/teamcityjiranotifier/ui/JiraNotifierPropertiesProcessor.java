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

package com.github.mpoindexter.teamcityjiranotifier.ui;

import com.github.mpoindexter.teamcityjiranotifier.feature.JiraNotifierSettingsConstants;
import com.github.mpoindexter.teamcityjiranotifier.feature.JiraNotifierTransitionMap;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JiraNotifierPropertiesProcessor implements PropertiesProcessor {

    @Override
    public Collection<InvalidProperty> process(@Nullable Map<String, String> props) {
        List<InvalidProperty> result = new ArrayList<InvalidProperty>();

        String activationPolicy = props.get(JiraNotifierSettingsConstants.SETTING_ACTIVATION_POLICY);
        if (StringUtil.isEmptyOrSpaces(activationPolicy)) {
            result.add(new InvalidProperty(JiraNotifierSettingsConstants.SETTING_ACTIVATION_POLICY, "Activation policy is required"));
        } else if (!JiraNotifierSettingsConstants.ACTIVATION_POLICY_ALWAYS.equals(activationPolicy)
                && !JiraNotifierSettingsConstants.ACTIVATION_POLICY_FAILURE.equals(activationPolicy)
                && !JiraNotifierSettingsConstants.ACTIVATION_POLICY_SUCCESS.equals(activationPolicy)) {
            result.add(new InvalidProperty(JiraNotifierSettingsConstants.SETTING_ACTIVATION_POLICY, "Invalid activation policy"));
        }

        String selectionPolicy = props.get(JiraNotifierSettingsConstants.SETTING_ISSUE_SELECTION_POLICY);
        if (StringUtil.isEmptyOrSpaces(selectionPolicy)) {
            result.add(new InvalidProperty(JiraNotifierSettingsConstants.SETTING_ISSUE_SELECTION_POLICY, "Issue selection policy is required"));
        } else if (!JiraNotifierSettingsConstants.ISSUE_SELECTION_POLICY_SINCE_LAST_SUCCESS.equals(selectionPolicy)
                && !JiraNotifierSettingsConstants.ISSUE_SELECTION_POLICY_LAST_BUILD.equals(selectionPolicy)) {
            result.add(new InvalidProperty(JiraNotifierSettingsConstants.SETTING_ISSUE_SELECTION_POLICY, "Invalid issue selection policy"));
        }

        if (!StringUtil.isEmptyOrSpaces(props.get(JiraNotifierSettingsConstants.SETTING_TRANSITION))) {
            String transitionMap = props.get(JiraNotifierSettingsConstants.SETTING_TRANSITION_MAP);
            if (StringUtil.isEmptyOrSpaces(transitionMap)) {
                result.add(new InvalidProperty(JiraNotifierSettingsConstants.SETTING_TRANSITION_MAP, "Transition map is required"));
            } else if (JiraNotifierTransitionMap.parse(transitionMap) == null) {
                result.add(new InvalidProperty(JiraNotifierSettingsConstants.SETTING_TRANSITION_MAP, "Invalid transition map"));
            }
        }
        return result;
    }
}
