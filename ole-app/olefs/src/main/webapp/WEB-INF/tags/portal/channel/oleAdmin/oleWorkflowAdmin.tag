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
<%
    /*
    * User Preferences
    * Quicklinks
    * Routing Rules
    * Routing Rule Delegations
    * Routing and Identity Management Document Type Hierarchy
    * Document Type
    * eDoc Lite
    * People Flow
    */
%>
<channel:portalChannelTop channelTitle="Workflow Admin" />
<div class="body">


        <portal:portalLink displayTitle="true" title="Document Type"
                               url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.kew.doctype.bo.DocumentType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:portalLink displayTitle="true" title="People Flow" url="${ConfigProperties.application.url}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.kew.impl.peopleflow.PeopleFlowBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
        <portal:portalLink displayTitle="true" title="Routing and IDM Doc Type" url="${ConfigProperties.kew.url}/RuleQuickLinks.do" /><br/>
        <portal:portalLink displayTitle="true"
                               title="Rule Attribute"
                               url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.kew.rule.bo.RuleAttribute&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:portalLink displayTitle="true" title="Statistics" url="${ConfigProperties.kew.url}/Stats.do?returnLocation=${ConfigProperties.application.url}/portal.do"  /><br/>
        <portal:portalLink displayTitle="true"
                               title="Workflow Attribute"
                               url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.kew.rule.bo.RuleAttribute&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        <portal:portalLink displayTitle="true" title="XML Import" url="${ConfigProperties.core.url}/Ingester.do" /><br/>











</div>
<channel:portalChannelBottom />
