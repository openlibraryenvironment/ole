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

<kul:tabTop tabTitle="Fund Lookup" defaultOpen="true">
    <div class="tab-container" align=center>
        <table cellpadding="0" cellspacing="0" class="datatable" summary="Fund Lookup Section">
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.keyword}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.keyword}" property="document.keyword"/>
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
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.fundCode}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute
                            attributeEntry="${documentAttributes.fundCode}" property="document.fundCode"/>
                    <kul:lookup boClassName="org.kuali.ole.coa.businessobject.OleFundCode" fieldConversions="fundCode:document.fundCode,fundCodeId:document.fundCodeId"
                                lookupParameters="document.chartOfAccountsCode:chartOfAccountsCode,document.accountNumber:accountNumber,document.fundCode:fundCode"/>
                </td>
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.organizationCode}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.organizationCode}" property="document.organizationCode"/>
                 	<kul:lookup boClassName="org.kuali.ole.coa.businessobject.Organization" 
                 	fieldConversions="organizationCode:document.organizationCode,chartOfAccountsCode:document.chartOfAccountsCode"
                 	lookupParameters="document.chartOfAccountsCode:chartOfAccountsCode,document.organizationCode:organizationCode"/>
                </td>  
            </tr>
           
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.accountNumber}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.accountNumber}" property="document.accountNumber"/>
                 	<kul:lookup boClassName="org.kuali.ole.coa.businessobject.Account" 
                 	fieldConversions="accountNumber:document.accountNumber,chartOfAccountsCode:document.chartOfAccountsCode"
                 	lookupParameters="document.chartOfAccountsCode:chartOfAccountsCode,document.accountNumber:accountNumber"/>

                 	<%-- <kul:lookup boClassName="org.kuali.ole.coa.businessobject.Account" 
                 	fieldConversions="accountNumber:document.accountNumber,organizationCode:document.organizationCode"
                 	lookupParameters="document.organizationCode:organizationCode,document.accountNumber:accountNumber"/> --%>
                </td>  
            </tr>     
            
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.objectCode}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.objectCode}" property="document.objectCode"/>
                 	<kul:lookup boClassName="org.kuali.ole.coa.businessobject.ObjectCode" 
                 	fieldConversions="financialObjectCode:document.objectCode,chartOfAccountsCode:document.chartOfAccountsCode"
                 	lookupParameters="document.chartOfAccountsCode:chartOfAccountsCode,document.objectCode:financialObjectCode"/>
                </td>  
            </tr>                
            
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.universityFiscalYear}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                     <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.universityFiscalYear}" property="document.universityFiscalYear"/>                  	
                 	<kul:lookup boClassName="org.kuali.ole.fp.businessobject.FiscalYearFunctionControl" fieldConversions="universityFiscalYear:document.universityFiscalYear"/>
                </td>  
            </tr>                      
             <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.active}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.active}" property="document.active"/>
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
