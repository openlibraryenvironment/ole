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
        if(document.getElementById('document.vendorName').value == '') {
            document.getElementById('document.vendorAliasName').focus();
        }
        if (${KualiForm.document.vendorEnterKeyEvent}) {
            document.getElementById('attachExistingBibId').checked = true;
            document.getElementById('attachExistingBibId').click();
            document.getElementById('tab-Titles-div').scrollIntoView();
        }
        var itemCount = getItemCount() - 4;
        document.getElementById('document.item[' + itemCount + '].newSourceLine.accountNumber').focus();
    }

    setParameterizedValues(); //To load the parameterized value at the time of page load

    function setParameterizedValues() {
        document.getElementById("newPurchasingItemLine.singleCopyNumber").value = "${KualiForm.document.copyNumber}"
        var orderTypeKeyValuePair=[];
        var purchasetypeList="${KualiForm.document.orderTypes}".split(";");
        for(var orderTypeCount=0;orderTypeCount<purchasetypeList.length;orderTypeCount++){
            if(purchasetypeList[orderTypeCount]!=null&&purchasetypeList[orderTypeCount]!=''){
                var tempOrderType=purchasetypeList[orderTypeCount].split(":");
                orderTypeKeyValuePair.push({id:tempOrderType[0],orderType:tempOrderType[1]});
            }
        }
        var inputOrderype='';
        for(var getIPOrdTypCnt=0;getIPOrdTypCnt<orderTypeKeyValuePair.length;getIPOrdTypCnt++){
            if(document.getElementById('document.purchaseOrderTypeId').value==orderTypeKeyValuePair[getIPOrdTypCnt].id) {
                inputOrderype = orderTypeKeyValuePair[getIPOrdTypCnt].orderType;
            }
        }
        if (inputOrderype == "Firm, Fixed") {
            document.getElementById("newPurchasingItemLine.itemLocation").value = "${KualiForm.document.itemLocationForFixed}";
            document.getElementById("newPurchasingItemLine.itemStatus").value = "${KualiForm.document.itemStatusForFixed}";
            if(document.getElementById('document.recurringPaymentTypeCode').value == null) {
                document.getElementById('document.recurringPaymentTypeCode').value = '';
                document.getElementById('document.purchaseOrderBeginDate').value = '';
                document.getElementById('document.purchaseOrderEndDate').value = '';
            }
        } else if (inputOrderype == "Approval") {
            document.getElementById("newPurchasingItemLine.itemLocation").value = "${KualiForm.document.itemLocationForApproval}";
            document.getElementById("newPurchasingItemLine.itemStatus").value = "${KualiForm.document.itemStatusForApproval}";
            if(document.getElementById('document.recurringPaymentTypeCode').value == null) {
                document.getElementById('document.recurringPaymentTypeCode').value = '';
                document.getElementById('document.purchaseOrderBeginDate').value = '';
                document.getElementById('document.purchaseOrderEndDate').value = '';
            }
        } else if (inputOrderype != "") {
            var poCreateDate = new Date();
            var dd = poCreateDate.getDate();
            var mm = poCreateDate.getMonth()+1;
            var yyyy = poCreateDate.getFullYear();
            poCreateDate = mm+'/'+dd+'/'+yyyy;
            document.getElementById("newPurchasingItemLine.itemLocation").value = '';
            document.getElementById("newPurchasingItemLine.itemStatus").value = '';
            document.getElementById('document.recurringPaymentTypeCode').value =  "${KualiForm.document.paymentTypeCode}"
            document.getElementById('document.purchaseOrderBeginDate').value = poCreateDate;
            document.getElementById('document.purchaseOrderEndDate').value = '12/31/9999'
        }
    }
</script>
