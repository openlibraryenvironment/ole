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
<%@ include file="/jsp/sys/kfsTldHeader.jsp"%>
<channel:portalChannelTop channelTitle="KRMS Rules" />
<%@ attribute name="riceUrl" required="false" type="java.lang.String"%>
<div class="body">

  <strong>Lookups</strong><br/><br/>
  
   	<portal:olePortalLink green="true" rice2="true" riceUrl="${riceUrl}" displayTitle="true" title="Agenda Lookup" url="${riceUrl}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krms.impl.repository.AgendaBo&returnLocation=${riceUrl}/portal.do&hideReturnLink=true&__login_user=admin" /><br/>
    <portal:olePortalLink green="true" rice2="true" riceUrl="${riceUrl}" displayTitle="true" title="Context Lookup" url="${riceUrl}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krms.impl.repository.ContextBo&returnLocation=${riceUrl}/portal.do&hideReturnLink=true&__login_user=admin" /><br/>
    <portal:olePortalLink green="true" rice2="true" riceUrl="${riceUrl}" displayTitle="true" title="Attribute Definition Lookup" url="${riceUrl}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krms.impl.repository.KrmsAttributeDefinitionBo&returnLocation=${riceUrl}/portal.do&hideReturnLink=true&__login_user=admin" /><br/>
    <portal:olePortalLink green="true" rice2="true" riceUrl="${riceUrl}" displayTitle="true" title="Term Lookup" url="${riceUrl}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krms.impl.repository.TermBo&returnLocation=${riceUrl}/portal.do&hideReturnLink=true&__login_user=admin" /><br/>
   	<portal:olePortalLink green="true" rice2="true" riceUrl="${riceUrl}" displayTitle="true" title="Term Specification Lookup" url="${riceUrl}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krms.impl.repository.TermSpecificationBo&returnLocation=${riceUrl}/portal.do&hideReturnLink=true&__login_user=admin" /><br>
    <portal:olePortalLink green="true" rice2="true" riceUrl="${riceUrl}" displayTitle="true" title="Category Lookup" url="${riceUrl}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krms.impl.repository.CategoryBo&returnLocation=${riceUrl}/portal.do&hideReturnLink=true&__login_user=admin" /><br/>
    <portal:olePortalLink green="true" rice2="true" riceUrl="${riceUrl}" displayTitle="true" title="Marc Editor" url="${riceUrl}/kr-krad/marceditorcomponents?viewId=MarcEditorView&methodToCall=start"/> <br/>
    <portal:olePortalLink green="true" rice2="true" riceUrl="${riceUrl}" displayTitle="true" title="Dublin Editor" url="${riceUrl}/kr-krad/dublincomponents?viewId=DublinEditorView&methodToCall=start"/> <br/><br/>
</div>
<channel:portalChannelBottom />
