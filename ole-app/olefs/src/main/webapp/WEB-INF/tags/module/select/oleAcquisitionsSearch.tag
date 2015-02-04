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

<%@ attribute name="documentAttributes" required="true" type="java.util.Map"
	description="The DataDictionary entry containing attributes for this row's fields."%>

<kul:tabTop tabTitle="Acquisitions Search" defaultOpen="true">
    <div class="tab-container" align=center>
        <table cellpadding="0" cellspacing="0" class="datatable" summary="Acquisition Search Section">
             <tr>
               <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.documentType}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${documentAttributes.documentType}" property="document.documentType"/>
                    <kul:lookup boClassName="org.kuali.rice.kew.doctype.bo.DocumentType" 
			            fieldConversions="name:document.documentType,docTypeParentId:document.documentParent" />
                </td>             
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.docNumber}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.docNumber}" property="document.docNumber"/>
                </td>             
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.purapDocumentIdentifier}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.purapDocumentIdentifier}" property="document.purapDocumentIdentifier"/>
                </td>             
            </tr> 
            <tr>
            	<th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.vendorName}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${documentAttributes.vendorName}" property="document.vendorName"/>
                    <kul:lookup boClassName="org.kuali.ole.vnd.businessobject.VendorDetail" 
			            fieldConversions="vendorName:document.vendorName" />
                </td>
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.dateFrom}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.dateFrom}" property="document.dateFrom" />
                </td>             
            </tr> 
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.dateTo}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.dateTo}" property="document.dateTo" />
                </td>             
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.initiator}" /></div>
                </th>
                 
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.initiator}" property="document.initiator"/>
                 	<kul:lookup boClassName="org.kuali.rice.kim.impl.identity.PersonImpl" fieldConversions="principalName:document.initiator"/>
                </td>             
            </tr>                             
            <tr>
            	<th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.requestorName}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${documentAttributes.requestorName}" property="document.requestorName"/>
                    <select:perreqlookup
						boClassName="org.kuali.ole.select.businessobject.OlePersonRequestor"
						fieldConversions="internalRequestorId:document.internalRequestorId,externalRequestorId:document.externalRequestorId,name:document.requestorName" />
                </td>
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.accountNumber}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.accountNumber}" property="document.accountNumber"/>
                 	<kul:lookup boClassName="org.kuali.ole.coa.businessobject.Account" fieldConversions="accountNumber:document.accountNumber"/>
                </td>  
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.organizationCode}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.organizationCode}" property="document.organizationCode"/>
                 	<kul:lookup boClassName="org.kuali.ole.coa.businessobject.Organization" fieldConversions="organizationCode:document.organizationCode"/>
                </td>  
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.chartOfAccountsCode}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.chartOfAccountsCode}" property="document.chartOfAccountsCode"/>
                 	<kul:lookup boClassName="org.kuali.ole.coa.businessobject.Chart" fieldConversions="chartOfAccountsCode:document.chartOfAccountsCode"/>
                </td>  
            </tr>        
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.title}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.title}" property="document.title"/>
                </td>             
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.author}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.author}" property="document.author"/>
                </td>             
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.publisher}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.publisher}" property="document.publisher"/>
                </td>             
            </tr> 
             <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.isbn}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.isbn}" property="document.isbn"/>
                </td>             
            </tr>   <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.localIdentifier}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute
                 	    attributeEntry="${documentAttributes.localIdentifier}" property="document.localIdentifier"/>
                </td>
            </tr>
             <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.searchType}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.searchType}" property="document.searchType"/>
                </td>      
               
            </tr>         
        </table>
    </div>
    <div class="tab-container" align=center>
	 <div id="globalbuttons" class="globalbuttons">
		 <html:image src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_search.gif" styleClass="globalbuttons" property="methodToCall.search" title="Search " alt="Search"/>
		 <html:image src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_clear.gif" styleClass="globalbuttons" property="methodToCall.clear" title="Clear" alt="Clear"/>
		 <html:image src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_cancel.gif" styleClass="globalbuttons" property="methodToCall.cancel" title="Cancel" alt="Clear Search"/>
	 </div>
	 </div>
</kul:tabTop>
