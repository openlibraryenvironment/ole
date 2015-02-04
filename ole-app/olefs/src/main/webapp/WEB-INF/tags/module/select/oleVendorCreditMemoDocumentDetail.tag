<%--
 Copyright 2006-2009 The Kuali Foundation
 
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
              description="The DataDictionary entry containing attributes for this row's fields."%>
         
<%@ attribute name="detailSectionLabel" required="true"
			  description="The label of the detail section."%>
<%@ attribute name="editableFundingSource" required="false"
			  description="Is fundingsourcecode editable?."%>
<%@ attribute name="tabErrorKey" required="false"
			  description="error map to display"%>
		  
			
<c:set var="fullEntryMode" value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT] && (empty KualiForm.editingMode['restrictFiscalEntry'])}" />
<c:if test="${empty editableFundingSource}">
	<c:set var="editableFundingSource" value="false" />
</c:if>

<c:if test="${amendmentEntry}">
	<c:if test="${KualiForm.readOnlyReceivingRequired eq 'true'}">	
		<c:set var="readOnlyReceivingRequired" value="true" />
	</c:if>
</c:if>

<c:set var="useTaxIndicatorButton" value="changeusetax" scope="request" />
<c:if test="${KualiForm.document.useTaxIndicator}">
	<c:set var="useTaxIndicatorButton" value="changesalestax" scope="request" />
</c:if>

<%--<c:set var="enableLicenseIndicator" value="${(not empty KualiForm.editingMode['enableLicenseIndicator'])}" />--%>
<c:set var="enableRouteButtons" value="${(not empty KualiForm.editingMode['enableRouteButtons'])}" />
<c:set var="purapTaxEnabled" value="${(not empty KualiForm.editingMode['purapTaxEnabled'])}" />
<c:set var="contentReadOnly" value="${(not empty KualiForm.editingMode['lockContentEntry'])}" />
<c:set var="internalPurchasingReadOnly" value="${(not empty KualiForm.editingMode['lockInternalPurchasingEntry'])}" />
<c:set var="tabindexOverrideBase" value="10" />
<c:set var="poOutForQuote" value="${KualiForm.document.statusCode eq 'QUOT'}" />

<h3><c:out value="${detailSectionLabel}"/> </h3>
<div class="tab-container-error"><div class="left-errmsg-tab"><kul:errors keyMatch="document.assignedUserPrincipalName"/></div></div>		        



<table cellpadding="0" cellspacing="0" class="datatable" summary="Detail Section">
    <tr>
	   <th align=left valign=middle >
		            <div align="left"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.purchaseOrderTypeId}" /></div>
	   </th>
		        <td align=left valign=middle class="datacell">
		            <kul:htmlControlAttribute
		                property="document.purchaseOrderTypeId"
		                attributeEntry="${documentAttributes.purchaseOrderTypeId}"
		                extraReadOnlyProperty="document.orderType.purchaseOrderType" 
		                readOnly="${not(fullEntryMode or amendmentEntry)}"/>
		              
		         
		        </td>
		        </tr>

    
</table>
