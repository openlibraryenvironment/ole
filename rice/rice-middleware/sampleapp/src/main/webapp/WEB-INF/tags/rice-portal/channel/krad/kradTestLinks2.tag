<%--
 Copyright 2007-2009 The Kuali Foundation
 
 Licensed under the Educational Community License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.opensource.org/licenses/ecl2.php
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp"%>

<channel:portalChannelTop channelTitle="KRAD Testing" />
<div class="body">

  <!-- NOTE: Do not add new KRAD views here but in the KRAD sampleapp. All KRAD functionality will be removed from the Rice
   sampleapp once the functionality is reproduced -->


  <strong>Screen Element Testing</strong>
  <ul class="chan">
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/uicomponents?viewId=UifCompView&methodToCall=start&readOnlyFields=field91" />Uif Components (Kitchen Sink)</a></li>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/uicomponents?viewId=Demo-StandardLayout&methodToCall=start" />Standard Layout Demo</a></li>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/uicomponents?viewId=Demo-ValidationLayout&methodToCall=start" />Validation Framework Demo</a></li>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/uicomponents?viewId=Demo-ValidationServerSide&methodToCall=start" />ServerSide Constraint Validation Demo</a></li>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/uilayouttest?viewId=UifLayoutView&methodToCall=start" />Uif Layout Test</a></li>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/uicomponents?viewId=Demo-RowDetails&methodToCall=start">Row Details Demo</a></li>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/uitest?viewId=Travel-testView2&methodToCall=foo" />Incident Report</a></li>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/configuration-test-view-uif-controller?viewId=ConfigurationTestView&methodToCall=start" />Configuration Test View</a></li>
    <%--<li><portal:portalLink displayTitle="true" title="Test View 1 (old)" url="${ConfigProperties.application.url}/kr-krad/uitest?viewId=Travel-testView1&methodToCall=start" /></li>--%>
    <%--<li><portal:portalLink displayTitle="true" title="Test View 2 (old)" url="${ConfigProperties.application.url}/kr-krad/uitest?viewId=Travel-testView2&methodToCall=start" /></li>--%>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/dialog-configuration-test?viewId=DialogTestView&methodToCall=start" />Dialog Test View</a></li>
    <%--<li><portal:portalLink displayTitle="true" title="Generated Fields Test" url="${ConfigProperties.application.url}/kr-krad/uicomponents?viewId=UifGeneratedFields&methodToCall=start" /></li>--%>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/uicomponents?viewId=ConfigurationTestView-Collections&methodToCall=start"/>Collections Configuration Test View</a></li>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/uicomponents?viewId=Demo-StateBasedValidation-Server&methodToCall=start" />State-based Validation (Server test)</a></li>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/uicomponents?viewId=Demo-StateBasedValidation-Client&methodToCall=start" />State-based Validation (Client test)</a></li>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/uicomponents?viewId=Demo-StateBasedValidation-ClientCustom&methodToCall=start" />State-based Validation (Client customized test)</a></li>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/uicomponents?viewId=RichMessagesView&methodToCall=start"/>Rich Messages</a></li>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/uicomponents?viewId=ClientDisableView&methodToCall=start"/>Client-side disable</a></li>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/uicomponents?viewId=Demo-ReadOnlyTestView&methodToCall=start">ReadOnly fields Demo</a></li>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/uicomponents?viewId=Demo-CollectionTotaling&methodToCall=start"/>Collection Totaling</a></li>
    <li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/uicomponents?viewId=Demo-CollectionGrouping&methodToCall=start"/>Collection Grouping and Totaling</a></li>
    <%--<li><a class="portal_link" target="_blank" href="${ConfigProperties.application.url}/kr-krad/collegeapp?viewId=Training-CollegeApplicationView&methodToCall=start"/>Training - Student College Application</a></li>--%>
  </ul>
   <%--<br/>--%>

  <%--<strong>Demo Views</strong>--%>
  <%--<ul class="chan">--%>
    <%--<li><a class="portal_link" href="${ConfigProperties.application.url}/kr-krad/courseOffering?viewId=CourseOfferingView&methodToCall=start" title="Course Offering" target="_blank">Course Offering</a></li>--%>
    <%--<li><a class="portal_link" href="${ConfigProperties.application.url}/kr-krad/registration?viewId=RegistrationView&methodToCall=start" title="Registration" target="_blank">Registration</a></li>--%>
  <%--</ul>--%>
</div>
<channel:portalChannelBottom />
