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
	<channel:portalChannelTop channelTitle="OLE Demo Help" />
	
		<div class="body">
			<b>OLE Help</b><br/><br/>
			
				<!--<portal:olePortalLink green="true" displayTitle="true" title="OLE Drivers Manual" url="https://wiki.kuali.org/display/OLE/OLE+0.3+Milestone+Release+Documentation"/><br/>-->
				<!--<portal:olePortalLink green="true" displayTitle="true" title="OLE Demo FTP Site" url="" /><br/>
				--><!--<portal:olePortalLink yellow="true" displayTitle="true" title="KFS User Documentation 4.x Help" url="" /><br/>
				-->
				<portal:olePortalLink green="true" displayTitle="true" title="OLE Drivers Manual 0.3" url="https://wiki.kuali.org/display/OLE/OLE+0.3+Milestone+Release+Documentation"/><br/>
				<portal:olePortalLink green="true" displayTitle="true" title="OLE Drivers Manual 0.6" url="https://wiki.kuali.org/display/OLE/OLE+0.6+Milestone+User+Documentation"/><br/>
				<portal:olePortalLink green="true" displayTitle="true" title="OLE Release Documentation" url="https://wiki.kuali.org/display/OLE/OLE+Release+Documentation"/><br/>

		</div>
	<channel:portalChannelBottom />
</c:if>