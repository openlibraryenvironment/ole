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

<%@ include file="/jsp/sys/kfsTldHeader.jsp"%>

<%@ attribute name="orderQueueDocumentAttributes" required="true" type="java.util.Map"
	description="The DataDictionary entry containing attributes for this row's fields."%>

<kul:tabTop tabTitle="Order Queue Search" defaultOpen="true">
    <div class="tab-container" align=center>
        <table cellpadding="0" cellspacing="0" class="datatable" summary="Receiving Queue Search Section">
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${orderQueueDocumentAttributes.selectorUserId}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${orderQueueDocumentAttributes.selectorUserId}" property="document.selectorUserName"/>
                 	<kul:lookup boClassName="org.kuali.rice.kim.impl.identity.PersonImpl" lookupParameters="selectorRoleName:lookupRoleName,selectorRoleNamespace:lookupRoleNamespaceCode"
									fieldConversions="principalId:document.selectorUserId,principalName:document.selectorUserName"/>
                </td>             
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${orderQueueDocumentAttributes.requisitionDocNumber}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${orderQueueDocumentAttributes.requisitionDocNumber}" property="document.requisitionDocNumber"/>
                </td>             
            </tr>
            <tr>
            	<th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${orderQueueDocumentAttributes.requisitionStatus}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${orderQueueDocumentAttributes.requisitionStatus}" property="document.requisitionStatus"/>
                    <!-- kul:lookup boClassName="org.kuali.ole.module.purap.businessobject.RequisitionStatus" 
			            fieldConversions="statusDescription:document.requisitionStatus,statusCode:document.requisitionStatusCode" /-->
                </td>
            </tr>
            <tr>
            	<th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${orderQueueDocumentAttributes.vendorName}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${orderQueueDocumentAttributes.vendorName}" property="document.vendorName"/>
                    <kul:lookup boClassName="org.kuali.ole.vnd.businessobject.VendorDetail" 
			            fieldConversions="vendorName:document.vendorName" />
                </td>
            </tr>
            <tr>
            	<th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${orderQueueDocumentAttributes.requestorName}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${orderQueueDocumentAttributes.requestorName}" property="document.requestorName"/>
                   <select:perreqlookup  boClassName="org.kuali.ole.select.businessobject.OlePersonRequestor"
                       fieldConversions="internalRequestorId:document.internalRequestorId,externalRequestorId:document.externalRequestorId,name:document.requestorName" />
                </td>
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${orderQueueDocumentAttributes.formatTypeId}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${orderQueueDocumentAttributes.formatTypeId}" property="document.formatTypeId" />
                </td>             
            </tr> 
             <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${orderQueueDocumentAttributes.chartOfAccountsCode}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${orderQueueDocumentAttributes.chartOfAccountsCode}" property="document.chartOfAccountsCode" /><%-- 
                 	<kul:lookup boClassName="org.kuali.ole.coa.businessobject.Chart" fieldConversions="chartOfAccountsCode:document.chartOfAccountsCode"
                 	lookupParameters="document.chartOfAccountsCode:chartOfAccountsCode"/> --%>
                </td>  
            </tr> 
            <tr>
            	<th>
                	<div align="right"><kul:htmlAttributeLabel attributeEntry="${orderQueueDocumentAttributes.accountNumber}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${orderQueueDocumentAttributes.accountNumber}" property="document.accountNumber"/>
                 	<kul:lookup boClassName="org.kuali.ole.coa.businessobject.Account" 
                 	fieldConversions="accountNumber:document.accountNumber,chartOfAccountsCode:document.chartOfAccountsCode"
                 	lookupParameters="document.chartOfAccountsCode:chartOfAccountsCode,document.accountNumber:accountNumber"/>
                </td>  
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${orderQueueDocumentAttributes.objectCode}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${orderQueueDocumentAttributes.objectCode}" property="document.objectCode"/>
                 	<kul:lookup boClassName="org.kuali.ole.coa.businessobject.ObjectCode" 
                 	fieldConversions="financialObjectCode:document.objectCode,chartOfAccountsCode:document.chartOfAccountsCode"
                 	lookupParameters="document.chartOfAccountsCode:chartOfAccountsCode,document.objectCode:financialObjectCode"/>
                </td>  
            </tr>           
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${orderQueueDocumentAttributes.workflowStatusChangeDateFrom}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${orderQueueDocumentAttributes.workflowStatusChangeDateFrom}" property="document.workflowStatusChangeDateFrom" />
                </td>             
            </tr> 
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${orderQueueDocumentAttributes.workflowStatusChangeDateTo}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${orderQueueDocumentAttributes.workflowStatusChangeDateTo}" property="document.workflowStatusChangeDateTo" />
                </td>             
            </tr> 
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${orderQueueDocumentAttributes.title}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${orderQueueDocumentAttributes.title}" property="document.title"/>
                </td>             
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${orderQueueDocumentAttributes.author}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${orderQueueDocumentAttributes.author}" property="document.author"/>
                </td>             
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${orderQueueDocumentAttributes.publisher}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${orderQueueDocumentAttributes.publisher}" property="document.publisher"/>
                </td>             
            </tr> 
             <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${orderQueueDocumentAttributes.isbn}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${orderQueueDocumentAttributes.isbn}" property="document.isbn"/>
                </td>             
            </tr> 
        </table>
    </div>
    <div class="tab-container" align=center>
	 <div id="globalbuttons" class="globalbuttons">
		 <html:image src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_search.gif" styleClass="globalbuttons" property="methodToCall.searchRequisitions" title="Search Requisitions" alt="Search"/>
		 <html:image src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_clear.gif" styleClass="globalbuttons" property="methodToCall.clear" title="Clear" alt="Clear"/>
		 <html:image src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_cancel.gif" styleClass="globalbuttons" property="methodToCall.cancel" title="Cancel" alt="Clear Search"/>
	 </div>
	 </div>
</kul:tabTop>
