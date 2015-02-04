<%--
   - Copyright 2011 The Kuali Foundation.
   - 
   - Licensed under the Educational Community License, Version 2.0 (the "License");
   - you may not use this file except in compliance with the License.
   - You may obtain a copy of the License at
   - 
   - http://www.opensource.org/licenses/ecl2.php
   - 
   - Unless required by applicable law or agreed to in writing, software
   - distributed under the License is distributed on an "AS IS" BASIS,
   - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   - See the License for the specific language governing permissions and
   - limitations under the License.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/jsp/sys/kfsTldHeader.jsp"%>

<kul:documentPage showDocumentInfo="true"
    documentTypeName="LineItemReceivingDocument"
    htmlFormAction="selectOleLineItemReceiving" renderMultipart="true"
    showTabButtons="true">
    		     		
    <c:set var="fullEntryMode" value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT]}" />
    
	<c:if test="${KualiForm.editingMode['displayInitTab']}" > 
    	<purap:receivingLineInit documentAttributes="${DataDictionary.LineItemReceivingDocument.attributes}"/>
	</c:if>
    
    <c:if test="${not KualiForm.editingMode['displayInitTab']}" >
	
	    <select:olesysDocumentOverview editingMode="${KualiForm.editingMode}" />
	
		<select:oleReceivingVendor documentAttributes="${DataDictionary.LineItemReceivingDocument.attributes}"
                                   intialOpen="${KualiForm.document.vendorFlag}"/>
	
		<select:oleReceivingLineItems itemAttributes="${DataDictionary.LineItemReceivingItem.attributes}"
                                      intialOpen="${KualiForm.document.titlesFlag}"/>
		
	    <purap:delivery
			documentAttributes="${DataDictionary.LineItemReceivingDocument.attributes}"
			deliveryReadOnly="true"
            intialOpen="${KualiForm.document.deliveryFlag}"/>
		          	
	    <select:oleRelatedDocuments documentAttributes="${DataDictionary.RelatedDocuments.attributes}"
                                    intialOpen="${KualiForm.document.relatedDocumentsFlag}"/>

		<select:oleNotesAndAttachment  intialOpen="${KualiForm.document.notesAndAttachmentFlag}"/>
	
	    <select:oleRouteLog intialOpen="${KualiForm.document.routeLogFlag}"/>
	</c:if>
	    		
    <kul:panelFooter />
	
    <c:set var="extraButtons" value="${KualiForm.extraButtons}"/>  	
  	
    <sys:documentControls 
        transactionalDocument="true" 
        extraButtons="${extraButtons}"
        suppressRoutingControls="${KualiForm.editingMode['displayInitTab']}" />
      
</kul:documentPage>