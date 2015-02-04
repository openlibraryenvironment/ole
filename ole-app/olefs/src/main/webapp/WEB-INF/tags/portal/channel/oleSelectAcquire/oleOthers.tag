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

<channel:portalChannelTop channelTitle="Others"/>
<div class="body">
    <portal:olePortalLink  displayTitle="true" title="Disbursement Voucher" url="oleFinancialDisbursementVoucher.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_DV" /><br/>
    <portal:olePortalLink  displayTitle="true" title="Distribution of Income and Expense" url="oleFinancialDistributionOfIncomeAndExpense.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_DI" /><br/>
    <portal:olePortalLink displayTitle="true" title="Donor Annual Stewardship Report" url="${ConfigProperties.application.url}/ole-kr-krad/searchAnnualStewardshipReportController?viewId=OLEAnnualStewardshipReportView&methodToCall=start"/><br/>
    <portal:olePortalLink displayTitle="true" title="Donor Encumbered Report" url="${ConfigProperties.application.url}/ole-kr-krad/searchDonorEncumberedReportController?viewId=OLEEncumberedReportView&methodToCall=start"/><br/>
    <portal:olePortalLink  displayTitle="true" title="General Error Correction" url="oleFinancialGeneralErrorCorrection.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_GEC" /><br/>
    <portal:portalLink displayTitle="true" title="General Ledger Correction Process" url="generalLedgerCorrection.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_GLCP" /><br/>
    <portal:portalLink displayTitle="true" title="General Ledger Entry" url="${OLEConstants.GL_MODIFIED_INQUIRY_ACTION}?methodToCall=start&businessObjectClassName=org.kuali.ole.gl.businessobject.Entry&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /><br/>
    <portal:olePortalLink  displayTitle="true" title="Pre-Encumbrance" url="financialPreEncumbrance.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_PE" /><br/>
    <portal:olePortalLink  displayTitle="true" title="Year End Budget Adjustment" url="financialYearEndBudgetAdjustment.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_YEBA" /><br/>
    <portal:olePortalLink  displayTitle="true" title="Year End Distribution of Income and Expense" url="financialYearEndDistributionOfIncomeAndExpense.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_YEDI" /><br/>
    <portal:olePortalLink  displayTitle="true" title="Year End General Error Correction" url="financialYearEndGeneralErrorCorrection.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_YEGE" /><br/>

</div>
<channel:portalChannelBottom/>
