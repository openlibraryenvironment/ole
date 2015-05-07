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

<channel:portalChannelTop channelTitle="Reports"/>
<div class="body">

    <portal:olePortalLink displayTitle="true" title="Available Balances" url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.gl.businessobject.AccountBalance&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink displayTitle="true" title="Cash Balances" url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.gl.businessobject.CashBalance&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /><br/>
    <portal:portalLink displayTitle="true" title="Open Encumbrances" url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.gl.businessobject.Encumbrance&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink yellow="true" displayTitle="true" title="Organization Review" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.coa.identity.OrgReviewRole&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/><br/>

</div>
<channel:portalChannelBottom/>
