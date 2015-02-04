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
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<kul:documentPage showDocumentInfo="true"
	documentTypeName="VendorCreditMemoDocument"
	htmlFormAction="selectOleVendorCreditMemo" renderMultipart="true"
	showTabButtons="true">

	<c:set var="fullEntryMode"
		value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT]}" />

	<c:set var="displayInitTab"
		value="${KualiForm.editingMode['displayInitTab']}" scope="request" />

	<c:if test="${displayInitTab}">
		<purap:creditMemoInit
			documentAttributes="${DataDictionary.VendorCreditMemoDocument.attributes}" />

		<kul:panelFooter />

		<div align="right">
			<br>
			<bean:message key="message.creditMemo.initMessage" />
		</div>
		<br>
	</c:if>

	<c:if test="${not displayInitTab}">
		<!--  Display hold message if payment is on hold -->
		<c:if test="${KualiForm.document.holdIndicator}">
			<h4>
				This Credit Memo has been Held by
				<c:out value="${KualiForm.document.lastActionPerformedByPersonName}" />
			</h4>
		</c:if>

		<select:olesysDocumentOverview editingMode="${KualiForm.editingMode}"
			includePostingYear="true" fiscalYearReadOnly="true"
			postingYearAttributes="${DataDictionary.VendorCreditMemoDocument.attributes}" >
		<select:oleVendorCreditMemoDocumentDetail
	    	documentAttributes="${DataDictionary.VendorCreditMemoDocument.attributes}"
	    	detailSectionLabel="Vendor Credit Memo Document Detail"
	    	editableFundingSource="true" />
	    </select:olesysDocumentOverview>

		<select:olevendor
			documentAttributes="${DataDictionary.VendorCreditMemoDocument.attributes}"
			displayPurchaseOrderFields="false" displayCreditMemoFields="true" intialOpen="${KualiForm.document.vendorFlag}"/>

		<select:oleCreditMemoInfo
			documentAttributes="${DataDictionary.VendorCreditMemoDocument.attributes}"
            intialOpen="${KualiForm.document.creditMemoInfoFlag}"/>

		<select:olepaymentRequestProcessItems
			documentAttributes="${DataDictionary.VendorCreditMemoDocument.attributes}"
			itemAttributes="${DataDictionary.CreditMemoItem.attributes}"
			accountingLineAttributes="${DataDictionary.CreditMemoAccount.attributes}"
			isCreditMemo="true"
            intialOpen="${KualiForm.document.processItemsFlag}"/>

		<select:oleSummaryAccounts
			itemAttributes="${DataDictionary.CreditMemoItem.attributes}"
			documentAttributes="${DataDictionary.SourceAccountingLine.attributes}"
            intialOpen="${KualiForm.document.accountSummaryFlag}"/>

		<select:oleRelatedDocuments
			documentAttributes="${DataDictionary.RelatedDocuments.attributes}"
            intialOpen="${KualiForm.document.relatedDocumentsFlag}"/>

		<purap:paymentHistory
			documentAttributes="${DataDictionary.RelatedDocuments.attributes}"
            intialOpen="${KualiForm.document.paymentHistoryFlag}"/>

		<select:oleGeneralLedgerPendingEntries intialOpen="${KualiForm.document.notesAndAttachmentFlag}"/>

		<select:oleNotesAndAttachment
			attachmentTypesValuesFinderClass="${DataDictionary.VendorCreditMemoDocument.attachmentTypesValuesFinderClass}" />

		<select:oleAdHocRecipients intialOpen="${KualiForm.document.adHocRecipientsFlag}"/>

		<select:oleRouteLog intialOpen="${KualiForm.document.routeLogFlag}"/>

		<kul:panelFooter />
	</c:if>

	<c:set var="extraButtons" value="${KualiForm.extraButtons}"
		scope="request" />

	<sys:documentControls transactionalDocument="true"
		extraButtons="${extraButtons}"
		suppressRoutingControls="${displayInitTab}" />

</kul:documentPage>
<script type="text/javascript">
window.onload = function(){
	var div = document.getElementsByTagName('div');
	var count=0;
	var prorateCondn = false;

	for(var i=count;i<count+4;i++){
		var attribute = document.getElementsByTagName('td');
		var usdCondn = false;
		if(document.getElementById('document.item['+i+'].additionalChargeUsd')!=null)
			usdCondn = document.getElementById('document.item['+i+'].additionalChargeUsd').checked
		if(usdCondn){
			for(var j=0;j<attribute.length;j++){
				if(attribute[j].getAttribute('id')=="foreignCurrency["+i+"]"){
					attribute[j].style.display="none";
				}
				if(attribute[j].getAttribute('id')=="localCurrency["+i+"]"){
					attribute[j].style.display="";
				}
			}
		}else{
			for(var j=0;j<attribute.length;j++){
				if(attribute[j].getAttribute('id')=="foreignCurrency["+i+"]"){
					attribute[j].style.display="";
				}
				if(attribute[j].getAttribute('id')=="localCurrency["+i+"]"){
					attribute[j].style.display="none";
				}
			}
		}
	}
}
</script>
