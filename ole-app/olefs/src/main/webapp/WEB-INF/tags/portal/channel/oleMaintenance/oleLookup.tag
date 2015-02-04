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

<channel:portalChannelTop channelTitle="Lookup" />
<div class="body">

    <portal:portalLink displayTitle="true" title="Account Delegate" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.AccountDelegate&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:portalLink displayTitle="true" title="Account Delegate Global" url="kr/maintenance.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.AccountDelegateGlobal" /><br/>
    <portal:portalLink displayTitle="true" title="Account Delegate Global From Model" url="kr/lookup.do?businessObjectClassName=org.kuali.ole.coa.businessobject.AccountDelegateModel&conversionFields=chartOfAccountsCode:modelChartOfAccountsCode,organizationCode:modelOrganizationCode,accountDelegateModelName:modelName&returnLocation=portal.do&docFormKey=88888888" /><br/>
    <portal:portalLink displayTitle="true" title="Account Delegate Model" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.businessobject.AccountDelegateModel&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>

</div>
<channel:portalChannelBottom />

