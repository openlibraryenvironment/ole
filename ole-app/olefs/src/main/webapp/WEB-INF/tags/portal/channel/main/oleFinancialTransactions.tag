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
<channel:portalChannelTop channelTitle="Financial Transactions" />
<div class="body">
	<strong>Accounts Receivable</strong><br /><br/>
   
      	<portal:olePortalLink grey="true" displayTitle="false" title="Cash Control" url="arCashControlDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_CTRL" hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Customer Credit Memo" url="arCustomerCreditMemoDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_CRM" hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Customer Invoice" url="arCustomerInvoiceDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_INV" hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Customer Invoice Writeoff" url="arCustomerInvoiceWriteoffDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_INVW" hiddenTitle="true"/><br/>
		<portal:olePortalLink grey="true" displayTitle="false" title="Customer Invoice Writeoff Lookup" url="arCustomerInvoiceWriteoffLookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.module.ar.businessobject.CustomerInvoiceWriteoffLookupResult&docFormKey=88888888&returnLocation=portal.do&hideReturnLink=true" hiddenTitle="true"/><br/>        
        <portal:olePortalLink grey="true" displayTitle="false" title="Payment Application" url="arPaymentApplicationDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_APP" hiddenTitle="true"/><br/><br/>

    <strong>Budget Construction</strong><br /><br/>
 
      	<portal:olePortalLink grey="true" displayTitle="false" title="Budget Construction Selection" url="budgetBudgetConstructionSelection.do?methodToCall=loadExpansionScreen" hiddenTitle="true"/><br/><br/> 
    
    <!--<strong>Circulation</strong><br /><br/>
  
    	<portal:olePortalLink grey="true" displayTitle="false" title="Coming soon!" url="" hiddenTitle="true"/><br/><br/> 

	--><strong>Financial Processing</strong><br /><br/>
    
        <portal:olePortalLink yellow="true" displayTitle="true" title="Advance Deposit" url="financialAdvanceDeposit.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_AD" /><br/>
		<portal:olePortalLink yellow="true" displayTitle="true" title="Auxiliary Voucher" url="financialAuxiliaryVoucher.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_AV" /><br/>
        <portal:olePortalLink yellow="true" displayTitle="true" title="Budget Adjustment" url="financialBudgetAdjustment.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_BA" /><br/>
        <portal:olePortalLink yellow="true" displayTitle="true" title="Cash Receipt" url="financialCashReceipt.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_CR" /><br/>
        <portal:olePortalLink yellow="true" displayTitle="true" title="Credit Card Receipt" url="financialCreditCardReceipt.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_CCR" /><br/>
        <portal:olePortalLink yellow="true" displayTitle="true" title="Disbursement Voucher" url="oleFinancialDisbursementVoucher.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_DV" /><br/>
        <portal:olePortalLink yellow="true" displayTitle="true" title="Distribution of Income and Expense" url="oleFinancialDistributionOfIncomeAndExpense.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_DI" /><br/>
		<portal:olePortalLink yellow="true" displayTitle="true" title="General Error Correction" url="oleFinancialGeneralErrorCorrection.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_GEC" /><br/>
		<portal:olePortalLink yellow="true" displayTitle="true" title="Indirect Cost Adjustment" url="financialIndirectCostAdjustment.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ICA" /><br/>
		<portal:olePortalLink yellow="true" displayTitle="true" title="Internal Billing" url="financialInternalBilling.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_IB" /><br/>
		<portal:olePortalLink yellow="true" displayTitle="true" title="Pre-Encumbrance" url="financialPreEncumbrance.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_PE" /><br/>
		<portal:olePortalLink yellow="true" displayTitle="true" title="Transfer of Funds" url="financialTransferOfFunds.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_TF" /><br/><br/>
    
    
   <%--<strong><strike>Labor Distribution</strike></strong><br />
    <ul class="chan">
        <portal:olePortalLink displayTitle="true" title="Benefit Expense Transfer" url="laborBenefitExpenseTransfer.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_BT" /><br/>	            
		<portal:olePortalLink displayTitle="true" title="Salary Expense Transfer" url="laborSalaryExpenseTransfer.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ST" /><br/>	
    </ul> --%>

	<!--<strong>Purchasing/Accounts Payable</strong><br /><br/>
  
        <portal:olePortalLink grey="true" displayTitle="false" title="Bulk Receiving" url="purapBulkReceiving.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_RCVB" hiddenTitle="true"/><br/> 
        <portal:olePortalLink grey="true" displayTitle="false" title="Claiming" url="" hiddenTitle="true"/><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Contract Manager Assignment" url="purapContractManagerAssignment.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ACM" /><br/>
		<portal:olePortalLink green="true" displayTitle="true" title="Payment Request" url="selectOlePaymentRequest.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_PREQ" /><br/>
		<portal:olePortalLink green="true" displayTitle="true" title="Receiving" url="selectOleLineItemReceiving.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_RCVL" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Requisition" url="purapOleRequisition.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_REQS" /><br/>
       <portal:olePortalLink grey="true" displayTitle="false" title="Shop Catalogs" url="b2b.do?methodToCall=shopCatalogs" hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Vendor Credit Memo" url="purapVendorCreditMemo.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_CM" hiddenTitle="true"/><br/><br/>
        --><%-- <portal:olePortalLink displayTitle="true" title="Purchase Order" url="purapOlePurchaseOrder.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_PO" /><br/>--%>
    
    
    <strong>Endowment</strong><br /><br/>

        <portal:olePortalLink grey="true" displayTitle="false" title="Asset Decrease" url="endowAssetDecreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_EAD" hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Asset Increase" url="endowAssetIncreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_EAI" hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Cash Decrease" url="endowCashDecreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ECDD" hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Cash Increase" url="endowCashIncreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ECI" hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Cash Transfer" url="endowCashTransferDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ECT" hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Endowment To GL Transfer Of Funds" url="endowEndowmentToGLTransferOfFundsDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_EGLT" hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="GL To Endowment Transfer Of Funds" url="endowGLToEndowmentTransferOfFundsDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_GLET" hiddenTitle="true"/><br/>        
  		<portal:olePortalLink grey="true" displayTitle="false" title="Liability Decrease" url="endowLiabilityDecreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ELD" hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Liability Increase" url="endowLiabilityIncreaseDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ELI" hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Security Transfer" url="endowSecurityTransferDocument.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_EST" hiddenTitle="true"/><br/><br/> 
    
</div>
<channel:portalChannelBottom />

