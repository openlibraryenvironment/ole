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
<%@ attribute name="environment" required="false" type="java.lang.String"%>
<%@ attribute name="riceUrl" required="false" type="java.lang.String"%>
<td class="content" valign="top">
	  <mainChannel:oleAcquisitions />
	  <mainChannel:oleIngest riceUrl="${riceUrl}"/>
      <mainChannel:oleFinancialTransactions />
      <mainChannel:oleAdministrativeTransactions />
</td>
<td class="content" valign="top">
	  <mainChannel:oleAcquisitionsSearch />
	  <mainChannel:oleSearch />
	  <mainChannel:oleLookupAndMaintenance />      
</td>
<td class="content" valign="top">
	  <mainChannel:oleBetaLegend/>
      <mainChannel:oleBalanceInquiries />
      <mainChannel:oleReports />
      <mainChannel:oleYearEndTransactions />
</td>


<td class="content" valign="top">
    <mainChannel:deliver/>
    <mainChannel:licensing />
    <mainChannel:eResource />
</td>
<td class="content" valign="top">
<a href="${environment}"> OLD PORTAL </a><br/>
<!-- <a href="http://dev.oleproject.org/ole-dev/olePortal.jsp"> OLD PORTAL </a><br/>
<a href="http://tst.oleproject.org/ole-tst/olePortal.jsp"> OLD PORTAL TEST</a><br/>
<a href="http://localhost:8080/ole-local/olePortal.jsp"> OLD PORTAL LOC</a><br/>-->
</td>

<!--<td class="content" valign="top">
<a href="">LINK</a>
</td>
-->