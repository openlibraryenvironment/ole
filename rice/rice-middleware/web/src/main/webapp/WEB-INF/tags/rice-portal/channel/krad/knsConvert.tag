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
<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp" %>

<channel:portalChannelTop channelTitle="KNS Conversion"/>
<div class="body">
  <strong>KIM Inquiries</strong>
  <ul class="chan">
    <li><portal:portalLink displayTitle="true" title="Component"
                           url="${ConfigProperties.application.url}/kr-krad/inquiry?methodToCall=start&namespaceCode=KR-WKFLW&code=ActionList&dataObjectClassName=org.kuali.rice.core.impl.component.ComponentBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
    <li><portal:portalLink displayTitle="true" title="Person"
                           url="${ConfigProperties.application.url}/kr-krad/inquiry?methodToCall=start&principalId=admin&dataObjectClassName=org.kuali.rice.kim.api.identity.Person&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
    <li><portal:portalLink displayTitle="true" title="Group"
                           url="${ConfigProperties.application.url}/kr-krad/inquiry?methodToCall=start&id=1&dataObjectClassName=org.kuali.rice.kim.impl.group.GroupBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
    <li><portal:portalLink displayTitle="true" title="Role"
                           url="${ConfigProperties.application.url}/kr-krad/inquiry?methodToCall=start&id=1&dataObjectClassName=org.kuali.rice.kim.impl.role.RoleBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
    <li><portal:portalLink displayTitle="true" title="Permission"
                           url="${ConfigProperties.application.url}/kr-krad/inquiry?methodToCall=start&permissionId=140&dataObjectClassName=org.kuali.rice.kim.bo.impl.PermissionImpl&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
    <li><portal:portalLink displayTitle="true" title="Responsibility"
                           url="${ConfigProperties.application.url}/kr-krad/inquiry?methodToCall=start&responsibilityId=1&dataObjectClassName=org.kuali.rice.kim.impl.responsibility.UberResponsibilityBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
    <!-- <li><portal:portalLink displayTitle="true" title="Role" url="${ConfigProperties.application.url}/spring/inquiry?methodToCall=start&roleId=1&dataObjectClassName=org.kuali.rice.kim.impl.role.RoleBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li> -->
    <li><portal:portalLink displayTitle="true" title="Rule"
                           url="${ConfigProperties.application.url}/kr-krad/inquiry?methodToCall=start&ruleBaseValuesId=1103&dataObjectClassName=org.kuali.rice.kew.rule.RuleBaseValues&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
    <li><portal:portalLink displayTitle="true" title="Rule Delegation"
                           url="${ConfigProperties.application.url}/kr-krad/inquiry?methodToCall=start&ruleDelegationId=1641&dataObjectClassName=org.kuali.rice.kew.rule.RuleDelegationBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
  </ul>
  <br/>
  <strong>KIM Lookups</strong>
  <ul class="chan">
    <li><portal:portalLink displayTitle="true" title="Person"
                           url="${ConfigProperties.application.url}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.kim.api.identity.Person&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
    <li><portal:portalLink displayTitle="true" title="Group"
                           url="${ConfigProperties.application.url}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.kim.impl.group.GroupBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
    <li><portal:portalLink displayTitle="true" title="Role"
                           url="${ConfigProperties.application.url}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.kim.impl.role.RoleBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
  </ul>
  <br/>
  <strong>KEW Screens</strong>
  <ul class="chan">
    <li><portal:portalLink displayTitle="true" title="Rule Attribute"
                           url="${ConfigProperties.application.url}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.kew.rule.bo.RuleAttribute&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
    <li><portal:portalLink displayTitle="true" title="Rule Template"
                           url="${ConfigProperties.application.url}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.kew.rule.bo.RuleTemplate&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
    <li><portal:portalLink displayTitle="true" title="XML Stylesheets"
                           url="${ConfigProperties.application.url}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.coreservice.impl.style.StyleBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
    <li><portal:portalLink displayTitle="true" title="XML Ingester"
                           url="${ConfigProperties.application.url}/kr-krad/ingester?viewId=KEW-IngesterView&methodToCall=start"/></li>
    <li><portal:portalLink displayTitle="true" title="Statistics"
                           url="${ConfigProperties.application.url}/kr-krad/stats?viewId=KEW-StatView&methodToCall=start"/></li>
    <li><portal:portalLink displayTitle="true" title="Document Type Maintenance"
                           url="${ConfigProperties.application.url}/kr-krad/maintenance?methodToCall=start&dataObjectClassName=org.kuali.rice.kew.doctype.bo.DocumentType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
  </ul>
</div>
<channel:portalChannelBottom/>