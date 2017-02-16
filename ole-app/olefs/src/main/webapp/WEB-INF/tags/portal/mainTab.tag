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
<%@ attribute name="environment" required="false"%>

<td class="content" valign="top">
      <mainChannel:transactions />
      <mainChannel:administrativeTransactions />
</td>
<td class="content" valign="top">
      <mainChannel:customDocumentSearches />
      <mainChannel:lookupAndMaintenance />
</td>
<td class="content" valign="top">
      <mainChannel:balanceInquiries />
      <mainChannel:reports />
      <mainChannel:yearEndTransactions />
</td> 

<td class="content" valign="top">
<a href="${environment}"> NEW PORTAL </a><br/>
<!--<a href="http://dev.oleproject.org/ole-dev/portal.jsp"> OLE PORTAL DEV</a><br/>
<a href="http://tst.oleproject.org/ole-tst/portal.jsp"> OLE PORTAL TEST</a><br/>
<a href="http://localhost:8080/ole-local/portal.jsp"> OLE PORTAL LOC</a><br/>-->
</td>