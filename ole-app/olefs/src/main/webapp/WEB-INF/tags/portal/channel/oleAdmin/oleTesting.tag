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

<c:if test="${fn:trim(ConfigProperties.environment) != fn:trim(ConfigProperties.production.environment.code)}" >
	<channel:portalChannelTop channelTitle="Testing" />
		<div class="body">
			<b>Data Dictionary</b><br/><br/>
			
			<portal:portalLink displayTitle="true" title="View Data Dictionary" url="dataDictionaryDumper.do" /><br/><br/>

				
			<b>Webservices</b><br/><br/>
			    
			<portal:portalLink displayTitle="true" title="PreOrder Service" url="wsclient.jsp" /><br/>
			<%--<portal:olePortalLink green="true" displayTitle="true" title="Default Table Column" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleDefaultTableColumn&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
			<portal:olePortalLink green="true" displayTitle="true" title="Default Value" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleDefaultValue&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
			<portal:olePortalLink green="true" displayTitle="true" title="Default Table Column Value" url="oledefaulttablecolumnValue.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_DFTTABCL" /><br/><br/>--%>
				
		</div>
	<channel:portalChannelBottom />
</c:if>