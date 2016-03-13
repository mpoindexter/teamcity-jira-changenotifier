<%@ include file="/include-internal.jsp" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%--
  ~ Copyright 2016 Michael Poindexter
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<jsp:useBean id="keys" class="com.github.mpoindexter.teamcityjiranotifier.feature.JiraNotifierSettingsConstants"/>

<tr>
  <td colspan="2">Specify Jira issue update settings</td>
</tr>
<l:settingsGroup title="Main">

  <props:selectSectionProperty name="${keys.settingActivationPolicy}" title="Update Jira when build status is:">
    <props:selectSectionPropertyContent value="${keys.activationPolicyAlways}" caption="Any">
    </props:selectSectionPropertyContent>

    <props:selectSectionPropertyContent value="${keys.activationPolicySuccess}" caption="Successful">
    </props:selectSectionPropertyContent>

    <props:selectSectionPropertyContent value="${keys.activationPolicyFailure}" caption="Failed">
    </props:selectSectionPropertyContent>
  </props:selectSectionProperty>

  <props:selectSectionProperty name="${keys.settingIssueSelectionPolicy}" title="Issues to update:">
    <props:selectSectionPropertyContent value="${keys.issueSelectionPolicyLastBuild}" caption="In this build">
    </props:selectSectionPropertyContent>

    <props:selectSectionPropertyContent value="${keys.issueSelectionPolicySinceLastSuccess}" caption="Since last successful build">
    </props:selectSectionPropertyContent>
  </props:selectSectionProperty>

  <tr>
    <th><label for="${keys.settingComment}">Comment:</label></th>
    <td>
      <props:checkboxProperty name="${keys.settingComment}"/>
      <label for="${keys.settingComment}">Comment issue with build details</label>
      <span class="error" id="error_${keys.settingComment}"></span>
    </td>
  </tr>

  <tr>
    <th><label for="${keys.settingTransition}">Transition:</label></th>
    <td>
      <props:checkboxProperty name="${keys.settingTransition}"/>
      <label for="${keys.settingTransition}">Transition issue through workflow</label>
      <span class="error" id="error_${keys.settingTransition}"></span>
    </td>
  </tr>

  <tr>
    <th>Transition Map (Required if using transitions):</th>
    <td>
      <props:textProperty name="${keys.settingTransitionMap}" className="longField"/>
      <span class="error" id="error_${keys.settingTransitionMap}"></span>
      <span class="smallNote">Specify transition map in the format &lt;STATE|*&gt;-&gt&lt;TRANSITION&gt;[;&lt;STATE&gt;-&gt&lt;TRANSITION&gt;]*</span>
    </td>
  </tr>
</l:settingsGroup>
