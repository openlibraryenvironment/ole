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

<channel:portalChannelTop channelTitle="Chart of Accounts" />
<div class="body">

    <portal:olePortalLink green="true" displayTitle="true" title="Constraint Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleConstraintType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Fund Group" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.FundGroup&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Object Consolidation" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.ObjectConsolidation&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Object Level" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.ObjectLevel&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Object Sub-Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.ObjectSubType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Object Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.ObjectType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Restricted Status" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.RestrictedStatus&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Stewardship Requirement" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.OleStewardship&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Sub-Fund Group" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.SubFundGroup&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Sub-Fund Group Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.SubFundGroupType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Sufficient Funds Code" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.SufficientFundsCode&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink green="true" displayTitle="true" title="Sufficient Funds Check Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleSufficientFundsCheckType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>

</div>
<channel:portalChannelBottom />
