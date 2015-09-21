<%--
 Copyright 2006 The Kuali Foundation
 
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
	documentTypeName="RequisitionDocument"
	htmlFormAction="purapOleRequisition" renderMultipart="true"
	showTabButtons="true">
    <c:set var="fullEntryMode" value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT] && (empty KualiForm.editingMode['restrictFiscalEntry'])}" />
 
	<select:olesysDocumentOverview editingMode="${KualiForm.editingMode}"
		includePostingYear="true"
        fiscalYearReadOnly="${not KualiForm.editingMode['allowPostingYearEntry']}"
        postingYearAttributes="${DataDictionary.RequisitionDocument.attributes}" >

    	<purap:purapDocumentDetail
	    	documentAttributes="${DataDictionary.RequisitionDocument.attributes}"
	    	detailSectionLabel="Requisition Detail"
	    	editableFundingSource="true" />
    </select:olesysDocumentOverview>

    <purap:delivery
        documentAttributes="${DataDictionary.RequisitionDocument.attributes}" 
        showDefaultBuildingOption="true"
        intialOpen="${KualiForm.document.deliveryFlag}"/>

    <select:olevendor
        documentAttributes="${DataDictionary.RequisitionDocument.attributes}"
        displayRequisitionFields="true" intialOpen="${KualiForm.document.vendorFlag}"/>
 
    <select:oleitems itemAttributes="${DataDictionary.RequisitionItem.attributes}"
    	accountingLineAttributes="${DataDictionary.RequisitionAccount.attributes}" 
    	bibInfoAttributes="${DataDictionary.BibInfoBean.attributes}"
    	displayRequisitionFields="true" intialOpen="${KualiForm.document.titlesFlag}"/>
 	<purap:purCams documentAttributes="${DataDictionary.RequisitionDocument.attributes}"
		itemAttributes="${DataDictionary.RequisitionItem.attributes}" 
		camsItemAttributes="${DataDictionary.RequisitionCapitalAssetItem.attributes}" 
		camsSystemAttributes="${DataDictionary.RequisitionCapitalAssetSystem.attributes}"
		camsAssetAttributes="${DataDictionary.RequisitionItemCapitalAsset.attributes}"
		camsLocationAttributes="${DataDictionary.RequisitionCapitalAssetLocation.attributes}" 
		isRequisition="true" />

    <select:olePaymentInfo
        documentAttributes="${DataDictionary.RequisitionDocument.attributes}" intialOpen="${KualiForm.document.paymentInfoFlag}"/>

    <select:oleAdditional
        documentAttributes="${DataDictionary.RequisitionDocument.attributes}"
        displayRequisitionFields="true" intialOpen="${KualiForm.document.additionalInstitutionalInfoFlag}"/>
         
    <select:oleSummaryAccounts
        itemAttributes="${DataDictionary.RequisitionItem.attributes}"
    	documentAttributes="${DataDictionary.SourceAccountingLine.attributes}"  intialOpen="${KualiForm.document.accountSummaryFlag}"/>

    <select:oleRelatedDocuments
            documentAttributes="${DataDictionary.RelatedDocuments.attributes}" intialOpen="${KualiForm.document.relatedDocumentsFlag}"/>
    
    <%--<purap:paymentHistory
            documentAttributes="${DataDictionary.RelatedDocuments.attributes}" intialOpen="${KualiForm.document.paymentHistoryFlag}"/>--%>
	            
	<select:oleNotesAndAttachment intialOpen="${KualiForm.document.notesAndAttachmentFlag}"/>

	<select:oleAdHocRecipients intialOpen="${KualiForm.document.adHocRecipientsFlag}"/>

	<select:oleRouteLog intialOpen="${KualiForm.document.routeLogFlag}"/>

	<kul:panelFooter />
	
	<c:set var="extraButtons" value="${KualiForm.extraButtons}"/>  	

	<sys:documentControls transactionalDocument="true" extraButtons="${extraButtons}" />

</kul:documentPage>
<script type="text/javascript">
    window.onload = function () {
        document.getElementById('document.vendorAliasName').focus();
        if (${KualiForm.document.vendorEnterKeyEvent}) {
            document.getElementById('attachExistingBibId').checked = true;
            document.getElementById('attachExistingBibId').click();
            if (document.getElementsByName('methodToCall.toggleTab.tabVendor')[0].title == 'close Vendor') {
                document.getElementsByName('methodToCall.toggleTab.tabVendor')[0].click();
            }
        }
        var itemCount = getItemCount() - 4;
        document.getElementById('document.item[' + itemCount + '].newSourceLine.accountNumber').focus();
    }

</script>
