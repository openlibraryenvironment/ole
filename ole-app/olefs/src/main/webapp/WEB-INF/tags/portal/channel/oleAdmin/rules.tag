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

<channel:portalChannelTop channelTitle="KRMS Rules" />
<div class="body">
  <%--<strong>Maintenance Docs</strong><br/><br/>

   <portal:portalLink  grey="true" hiddenTitle="true" title="Create New Agenda" url="${ConfigProperties.application.url}/kr-krad/krmsAgendaEditor?methodToCall=start&dataObjectClassName=org.kuali.rice.krms.impl.ui.AgendaEditor&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/> <br/><br/>
--%>
   <portal:portalLink  displayTitle="true"  title="Agenda Lookup" url="${ConfigProperties.application.url}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krms.impl.repository.AgendaBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true" /><br/>
   <portal:portalLink displayTitle="true"   title="Context Lookup" url="${ConfigProperties.application.url}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krms.impl.repository.ContextBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true" /><br/>
   <portal:portalLink displayTitle="true"   title="Attribute Definition Lookup" url="${ConfigProperties.application.url}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krms.impl.repository.KrmsAttributeDefinitionBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true" /><br/>
   <portal:portalLink displayTitle="true"   title="Term Lookup" url="${ConfigProperties.application.url}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krms.impl.repository.TermBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true" /><br/>
   <portal:portalLink displayTitle="true"   title="Term Specification Lookup" url="${ConfigProperties.application.url}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krms.impl.repository.TermSpecificationBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true" /><br/>
   <portal:portalLink displayTitle="true"   title="Category Lookup" url="${ConfigProperties.application.url}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krms.impl.repository.CategoryBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true" /><br/>


    <%--<portal:portalLink  hiddenTitle="true" green="true" title="TEST Category Lookup" url="${ConfigProperties.application.url}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krms.impl.repository.CategoryBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" />--%>

</div>
<channel:portalChannelBottom />
