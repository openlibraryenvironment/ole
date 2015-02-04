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

<channel:portalChannelTop channelTitle="Transactions" />
<div class="body">
	<strong>Accounts Receivable</strong><br />
    <ul class="chan">
        <li><portal:portalLink displayTitle="true" title="Cash Control" url="arCashControlDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_CTRL" /></li>
        <li><portal:portalLink displayTitle="true" title="Customer Credit Memo" url="arCustomerCreditMemoDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_CRM" /></li>
        <li><portal:portalLink displayTitle="true" title="Customer Invoice" url="arCustomerInvoiceDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_INV" /></li>
        <li><portal:portalLink displayTitle="true" title="Customer Invoice Writeoff" url="arCustomerInvoiceWriteoffDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_INVW" /></li>
		<li><portal:portalLink displayTitle="true" title="Customer Invoice Writeoff Lookup" url="arCustomerInvoiceWriteoffLookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.ar.businessobject.CustomerInvoiceWriteoffLookupResult&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" /></li>        
        <li><portal:portalLink displayTitle="true" title="Payment Application" url="arPaymentApplicationDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_APP" /></li>
    </ul>

    <strong>Budget Construction</strong><br />
    <ul class="chan">
        <li><portal:portalLink displayTitle="true" title="Budget Construction Selection" url="budgetBudgetConstructionSelection.do?methodToCall=loadExpansionScreen" /></li>
    </ul>

	<strong>Financial Processing</strong><br />
    <ul class="chan">
        <li><portal:portalLink displayTitle="true" title="Advance Deposit" url="financialAdvanceDeposit.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_AD" /></li>
		<li><portal:portalLink displayTitle="true" title="Auxiliary Voucher" url="financialAuxiliaryVoucher.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_AV" /></li>
        <li><portal:portalLink displayTitle="true" title="Budget Adjustment" url="financialBudgetAdjustment.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_BA" /></li>
        <li><portal:portalLink displayTitle="true" title="Cash Receipt" url="financialCashReceipt.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_CR" /></li>
        <li><portal:portalLink displayTitle="true" title="Credit Card Receipt" url="financialCreditCardReceipt.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_CCR" /></li>
        <li><portal:portalLink displayTitle="true" title="Disbursement Voucher" url="financialDisbursementVoucher.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_DV" /></li>
        <li><portal:portalLink displayTitle="true" title="Distribution of Income and Expense" url="financialDistributionOfIncomeAndExpense.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_DI" /></li>
		<li><portal:portalLink displayTitle="true" title="General Error Correction" url="financialGeneralErrorCorrection.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_GEC" /></li>
		<li><portal:portalLink displayTitle="true" title="Indirect Cost Adjustment" url="financialIndirectCostAdjustment.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ICA" /></li>
		<li><portal:portalLink displayTitle="true" title="Internal Billing" url="financialInternalBilling.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_IB" /></li>
		<li><portal:portalLink displayTitle="true" title="Pre-Encumbrance" url="financialPreEncumbrance.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_PE" /></li>
		<li><portal:portalLink displayTitle="true" title="Transfer of Funds" url="financialTransferOfFunds.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_TF" /></li>
    </ul>
    
   <!--<strong>Labor Distribution</strong><br />
    <ul class="chan">
        <li><portal:portalLink displayTitle="true" title="Benefit Expense Transfer" url="laborBenefitExpenseTransfer.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_BT" /></li>	            
		<li><portal:portalLink displayTitle="true" title="Salary Expense Transfer" url="laborSalaryExpenseTransfer.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ST" /></li>	
    </ul>-->

	<strong>Purchasing/Accounts Payable</strong><br />
    <ul class="chan">
        <li><portal:portalLink displayTitle="true" title="Bulk Receiving" url="purapBulkReceiving.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_RCVB" /></li>
        <li><portal:portalLink displayTitle="true" title="Contract Manager Assignment" url="purapContractManagerAssignment.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ACM" /></li>
		<li><portal:portalLink displayTitle="true" title="Payment Request" url="selectOlePaymentRequest.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_PREQ" /></li>
		<li><portal:portalLink displayTitle="false" title="Invoice" url="selectOleInvoice.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_PPRQ" /></li>
		<li><portal:portalLink displayTitle="true" title="Receiving" url="selectOleLineItemReceiving.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_RCVL" /></li>
        <li><portal:portalLink displayTitle="true" title="Requisition" url="purapOleRequisition.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_REQS" /></li>
        <li><portal:portalLink displayTitle="true" title="Shop Catalogs" url="b2b.do?methodToCall=shopCatalogs" /></li>
        <li><portal:portalLink displayTitle="true" title="Vendor Credit Memo" url="selectOleVendorCreditMemo.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_CM" /></li>
         <li><portal:portalLink displayTitle="true" title="Purchase Order" url="purapOlePurchaseOrder.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_PO" /></li>
    </ul>
    
    <strong>Endowment</strong><br />
    <ul class="chan">
        <li><portal:portalLink displayTitle="true" title="Asset Decrease" url="endowAssetDecreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_EAD" /></li>
        <li><portal:portalLink displayTitle="true" title="Asset Increase" url="endowAssetIncreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_EAI" /></li>
        <li><portal:portalLink displayTitle="true" title="Cash Decrease" url="endowCashDecreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ECDD" /></li>
        <li><portal:portalLink displayTitle="true" title="Cash Increase" url="endowCashIncreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ECI" /></li>
        <li><portal:portalLink displayTitle="true" title="Cash Transfer" url="endowCashTransferDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ECT" /></li>
        <li><portal:portalLink displayTitle="true" title="Endowment To GL Transfer Of Funds" url="endowEndowmentToGLTransferOfFundsDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_EGLT" /></li>
        <li><portal:portalLink displayTitle="true" title="GL To Endowment Transfer Of Funds" url="endowGLToEndowmentTransferOfFundsDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_GLET" /></li>        
  		<li><portal:portalLink displayTitle="true" title="Liability Decrease" url="endowLiabilityDecreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ELD" /></li>
        <li><portal:portalLink displayTitle="true" title="Liability Increase" url="endowLiabilityIncreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ELI" /></li>
        <li><portal:portalLink displayTitle="true" title="Security Transfer" url="endowSecurityTransferDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_EST" /></li>
     </ul>
</div>
<channel:portalChannelBottom />

