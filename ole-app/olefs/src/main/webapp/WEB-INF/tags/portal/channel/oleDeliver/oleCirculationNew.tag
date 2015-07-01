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
<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp" %>

<channel:portalChannelTop channelTitle="Circulation New"/>
<div class="body">


    <%-- <portal:portalLink displayTitle="true" title="Loan/Return" green="true"
                        url="${ConfigProperties.application.url}/kr-krad/loancontroller?viewId=PatronItemView&methodToCall=start"/> <br/>--%>
    <portal:portalLink displayTitle="true" title="Checkout Item(s)"
                       url="${ConfigProperties.application.url}/ole-kr-krad/circcontroller?viewId=circView&methodToCall=start"/> <br/>
    <portal:portalLink displayTitle="true" title="Circulation Policy Ingester"
                       url="${ConfigProperties.application.url}/ole-kr-krad/droolUploadController?viewId=droolUploadView&methodToCall=start"/> <br/>

</div>
<channel:portalChannelBottom/>
