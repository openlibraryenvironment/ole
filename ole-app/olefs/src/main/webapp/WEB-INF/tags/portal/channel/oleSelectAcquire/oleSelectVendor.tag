<%--
 Copyright 2007 The Kuali Foundation

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

<channel:portalChannelTop channelTitle="Vendor" />
<div class="body">

    <portal:portalLink displayTitle="true" title='Create' url='kr/maintenance.do?businessObjectClassName=org.kuali.ole.vnd.businessobject.VendorDetail&methodToCall=start#topOfForm'/><br/>
    <portal:portalLink displayTitle="true" title='Search' url='kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.VendorDetail&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true'/>

    <br/><br/>
<%--
    <strong>Bibliographic records</strong><br/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <portal:olePortalLink green="true" displayTitle="true" title='Document Store Search' url='${ConfigProperties.ole.docstore.url.base}/discovery.do?searchType=newSearch'/><br/><br/> --%>


</div>
<channel:portalChannelBottom />
