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

<%@ attribute name="documentAttributes" required="true" type="java.util.Map"
              description="The DataDictionary entry containing attributes for this row's fields." %>
              
<c:set var="fullEntryMode" value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT]}" />
<c:set var="fullDocumentEntryCompleted" value="${not empty KualiForm.editingMode['fullDocumentEntryCompleted']}" />
<c:set var="purchaseOrderAttributes" value="${DataDictionary.PurchaseOrderDocument.attributes}" />

<kul:tab tabTitle="Credit Memo Info" defaultOpen="true">
   
    <div class="tab-container" align=center>
            <h3>Credit Memo Info</h3>

        <table cellpadding="0" cellspacing="0" class="datatable" summary="Credit Memo Info Section">

            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                   <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.creditMemoNumber}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                   <kul:htmlControlAttribute attributeEntry="${documentAttributes.creditMemoNumber}" property="document.creditMemoNumber" readOnly="true" /> 
                </td>
                <th align=right valign=middle class="bord-l-b" width="25%">
                   <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.creditMemoType}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                   <bean:write name="KualiForm" property="document.creditMemoType" />
                </td>
            </tr>
            
            <tr>
                <th align=right valign=middle class="bord-l-b">
                   <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.creditMemoDate}" /></div>
                </th>
                <td align=left valign=middle class="datacell">
                   <kul:htmlControlAttribute attributeEntry="${documentAttributes.creditMemoDate}" property="document.creditMemoDate" readOnly="true" />
                </td>
                <th align=right valign=middle class="bord-l-b">
                   <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.vendorNumber}" /></div>
                </th>
                <td align=left valign=middle class="datacell">
                   <kul:htmlControlAttribute attributeEntry="${documentAttributes.vendorNumber}" property="document.vendorNumber" readOnly="true" />
                </td>
             </tr>
             
             <c:if test="${not fullDocumentEntryCompleted}">
                  <tr>
                     <th align=right valign=middle class="bord-l-b">                   
        	    		<div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.creditMemoAmount}" useShortLabel="true" /></div>
                     </th>
                     <td align=left valign=middle class="datacell">                   
                     	<kul:htmlControlAttribute attributeEntry="${documentAttributes.creditMemoAmount}" property="document.creditMemoAmount" readOnly="true" />
                     </td>
                     <th align=right valign=middle class="bord-l-b">&nbsp;</th>               
                     <td align=left valign=middle class="datacell">&nbsp;</td>                
                  <tr>   
             </c:if>

             <tr>   
                <th align=right valign=middle class="bord-l-b">
                   <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.purchaseOrderEndDate}" /></div>
                </th>
                <td align=left valign=middle class="datacell">
                   <kul:htmlControlAttribute  attributeEntry="${documentAttributes.purchaseOrderEndDate}" property="document.purchaseOrder.purchaseOrderEndDate" readOnly="true" />
                </td>
                <th align=right valign=middle class="bord-l-b">
                   <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.purchaseOrderIdentifier}" /></div>
                </th>
                <td align=left valign=middle class="datacell">
                   <kul:htmlControlAttribute attributeEntry="${documentAttributes.purchaseOrderIdentifier}" property="document.purchaseOrderIdentifier" readOnly="true" />
                </td>
             </tr>
             
             <tr>   
                <th align=right valign=middle class="bord-l-b">
                   <div align="right"><kul:htmlAttributeLabel  attributeEntry="${documentAttributes.purchaseOrderNotes}" /></div>
                </th>
                <td align=left valign=middle class="datacell">
                   <bean:write name="KualiForm" property="document.purchaseOrderNotes" />
                </td>
                <th align=right valign=middle class="bord-l-b">
                   <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.paymentRequestIdentifier}" /></div>
                </th>
                <td align=left valign=middle class="datacell">
                   <kul:htmlControlAttribute attributeEntry="${documentAttributes.paymentRequestIdentifier}" property="document.paymentRequestIdentifier" readOnly="true" />
                </td>
            </tr>
			<tr>
                <th align=right valign=middle class="bord-l-b">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.extractedTimestamp}" /></div>
                </th>
                <td align=left valign=middle class="datacell">
                    <kul:htmlControlAttribute attributeEntry="${documentAttributes.extractedTimestamp}" property="document.extractedTimestamp" readOnly="${true}" />
                    <c:if test="${not empty KualiForm.document.extractedTimestamp}">
                        <purap:disbursementInfo sourceDocumentNumber="${KualiForm.document.documentNumber}" sourceDocumentType="${KualiForm.document.documentType}" />          
					</c:if>
                </td>
                <th align=right valign=middle class="bord-l-b">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.accountsPayableApprovalTimestamp}" /></div>
                </th>
                <td align=left valign=middle class="datacell">
                    <kul:htmlControlAttribute attributeEntry="${documentAttributes.accountsPayableApprovalTimestamp}" property="document.accountsPayableApprovalTimestamp" readOnly="${not displayInitTab}" />
                </td>          
            </tr>

				 <tr>
                    <th align=right valign=middle class="bord-l-b">
	                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.invoiceType}" /></div>
	                </th>
                    <td align=left valign=middle class="datacell">
                    
                        <kul:htmlControlAttribute attributeEntry="${DataDictionary.VendorCreditMemoDocument.attributes.invoiceType}" property="document.creditMemoInvoiceType.invoiceType"  extraReadOnlyProperty = "document.creditMemoInvoiceType.invoiceType"  readOnly="${not (fullEntryMode or editPreExtract)}"/>
                        
                        <c:if test="${ (fullEntryMode or editPreExtract) }">                        
                        <kul:lookup boClassName="org.kuali.ole.select.businessobject.OleInvoiceType"  
									fieldConversions="invoiceTypeId:document.invoiceTypeId,invoiceType:document.invoiceType,invoiceType:document.creditMemoInvoiceType.invoiceType"/>
						</c:if>
                    
                    </td>
                    <th align=right valign=middle class="bord-l-b">
                        <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.invoiceSubType}" /></div>
                    </th>
                    <td align=left valign=middle class="datacell">
                        <kul:htmlControlAttribute attributeEntry="${DataDictionary.VendorCreditMemoDocument.attributes.invoiceSubType}" property="document.creditMemoInvoiceSubType.invoiceSubType" extraReadOnlyProperty = "document.creditMemoInvoiceSubType.invoiceSubType"  readOnly="${not (fullEntryMode or editPreExtract)}" />
                        
                        <c:if test="${ (fullEntryMode or editPreExtract) }">
                        <kul:lookup boClassName="org.kuali.ole.select.businessobject.OleInvoiceSubType"  
									fieldConversions="invoiceSubTypeId:document.invoiceSubTypeId,invoiceSubType:document.invoiceSubType,invoiceSubType:document.creditMemoInvoiceSubType.invoiceSubType"/>          </c:if>          
                    </td>
                
            </tr>


			<tr>
	            <sys:bankLabel align="right"/>
                <sys:bankControl property="document.bankCode" objectProperty="document.bank" readOnly="${not fullEntryMode}"/>
                <th align=right valign=middle class="bord-l-b">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.paymentMethodId}" /></div>
                </th>
                <td align=left valign=middle class="datacell">
                     <kul:htmlControlAttribute attributeEntry="${documentAttributes.paymentMethodId}" property="document.paymentMethodId"  onchange="paymentMethodMessages(this.value);" readOnly="${false}" />
                      <kul:lookup boClassName="org.kuali.ole.select.businessobject.OlePaymentMethod"  
									fieldConversions="paymentMethodId:document.paymentMethodId,paymentMethod:document.paymentMethod,paymentMethod:document.olePaymentMethod.paymentMethod"
									lookupParameters="document.paymentMethodId:paymentMethodId"/>
                    
                </td>
                
            </tr>	
            
		</table> 
    </div>
    
</kul:tab>
