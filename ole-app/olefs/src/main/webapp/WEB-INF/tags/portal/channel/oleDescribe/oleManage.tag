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

<channel:portalChannelTop channelTitle="Manage Titles, Holdings or Items"/>
<div class="body">
    <portal:portalLink displayTitle="true" title="Global Changes To Holdings Or Items"
                           url="${ConfigProperties.application.url}/ole-kr-krad/globaleditController?viewId=GlobalEditView&methodToCall=start"/> <br/>
    <portal:portalLink displayTitle="true" title="Global Edit Fields"
                       url="${ConfigProperties.application.url}/ole-kr-krad/globaleditController?viewId=GlobalEditFieldsView&methodToCall=test"/> <br/>
    <portal:portalLink displayTitle="true" title="Transfer titles, Holdings or Items"
                       url="${ConfigProperties.application.url}/ole-kr-krad/transferController?viewId=TransferView&methodToCall=start"/> <br/>


</div>
<channel:portalChannelBottom/>
