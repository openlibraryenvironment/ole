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

<channel:portalChannelTop channelTitle="Import" />
<%@ attribute name="riceUrl" required="false" type="java.lang.String"%>
<div class="body">
   <%-- <portal:olePortalLink rice2="true" green="true" displayTitle="true" title="Order Import" url=""/><portal:olePortalLink displayTitle="false" title="Formerly Staff Upload" url="" hiddenTitle="true"/><br/><br/>--%>
    <portal:portalLink displayTitle="true" title="View Order Reports" url="batchlookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleLoadSumRecords&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
       <portal:portalLink displayTitle="true" title="Invoice Load Details"
                          url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.document.OLEInvoiceIngestLoadReport&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/> <br/>
</div>
<channel:portalChannelBottom />