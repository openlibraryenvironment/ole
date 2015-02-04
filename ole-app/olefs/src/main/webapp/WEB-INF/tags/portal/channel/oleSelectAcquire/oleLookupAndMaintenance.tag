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

<channel:portalChannelTop channelTitle="Lookup and Maintenance" />
<div class="body">

    <strong>CHART OF ACCOUNTS :</strong><br /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Account" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.Account&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Account Global" url="kr/maintenance.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.AccountGlobal" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Object Code" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.ObjectCode&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Object Code Global" url="kr/maintenance.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.ObjectCodeGlobal" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Organization" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.Organization&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <%--<portal:olePortalLink yellow="true" displayTitle="true" title="Organization Review" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.identity.OrgReviewRole&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>--%>
    <portal:olePortalLink green="true" displayTitle="true" title="Sub-Account" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.SubAccount&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Sub-Object Code" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.SubObjectCode&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Sub-Object Code Global" url="kr/maintenance.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.SubObjectCodeGlobal" /><br/><br/>

  <%--  <strong>VENDOR :</strong><br /><br/>
		
        <portal:olePortalLink green="true" displayTitle="true" title="Vendor" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.VendorDetail&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
        &lt;%&ndash;<portal:olePortalLink grey="true" displayTitle="false" title="Vendor Contracts" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.vnd.businessobject.VendorContract&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" hiddenTitle="true"/><br/><br/>&ndash;%&gt;
	--%>
    </div>
<channel:portalChannelBottom />
