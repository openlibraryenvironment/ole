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

<%@ include file="/kr/WEB-INF/jsp/tldHeader.jsp"%>

<script type="text/javascript">

/* window.onload = function(){
	//alert(document.forms[0].elements["document.vendor"].checked);
	if(document.forms[0].elements["document.vendor"].checked)
	    document.getElementById('vendorNameEnable').style.display="";
	else
	    document.getElementById('vendorNameEnable').style.display="none";
	if(document.forms[0].elements["document.purchaseOrderDate"].checked)
		document.getElementById('purchaseOrderDateEnable').style.display="";
	else
	    document.getElementById('purchaseOrderDateEnable').style.display="none";
}
    function enableVendorName(vendor){
    	//alert("hi inside script");
    	//alert(" --> " + vendor.checked);
    	if(vendor.checked){
      		document.getElementById('vendorNameEnable').style.display="";
        }
    	else{
       	    document.getElementById('vendorNameEnable').style.display="none";
        }
    }
    
    function enablePOStartDateEndDate(purchaseOrderDate){
    	if(purchaseOrderDate.checked){
      		document.getElementById('purchaseOrderDateEnable').style.display="";
        }
    	else{
       	    document.getElementById('purchaseOrderDateEnable').style.display="none";
        }
    } */
    
  
</script>

<c:set var="documentAttributes" value="${DataDictionary.OleReceivingQueueSearchDocument.attributes}" />

<kul:tabTop tabTitle="Receiving and Claiming Queue Search" defaultOpen="true">
    <div class="tab-container" align=center>
        <table cellpadding="0" cellspacing="0" class="datatable" summary="Receiving and Claiming Queue Search Section">
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.purchaseOrderNumber}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.purchaseOrderNumber}" property="document.purchaseOrderNumber"/>
                </td>   
                
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.monograph}" /></div>
                </th> 
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${documentAttributes.monograph}" property="document.monograph"/>
                </td>
                
                 <%-- <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.serials}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${documentAttributes.serials}" property="document.serials"/>
                </td>  --%>           
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.standardNumber}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.standardNumber}" property="document.standardNumber"/>
                </td>
                
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right">
                        <kul:htmlAttributeLabel attributeEntry="${documentAttributes.purchaseOrderStatusDescription}" />
                    </div>
                </th>
                
                 <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.purchaseOrderStatusDescription}" property="document.purchaseOrderStatusDescription"/>
                </td>
                <%-- <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.standingOrders}" /></div>
                </th>                                
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${documentAttributes.standingOrders}" property="document.standingOrders"/>
                </td> --%>  
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%" rowspan="3">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.title}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%" rowspan="3">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.title}" property="document.title"/>
                </td>
                 
                
            </tr>
            <%-- <tr>
                
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.status}" /></div>
                </th> 
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${documentAttributes.status}" property="document.status"/>
                </td> 
                 
            </tr> --%>
            <tr>
               
                <%-- <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.vendor}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${documentAttributes.vendor}" property="document.vendor" onclick="enableVendorName(this)"/>
                </td> --%>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right">
                        <kul:htmlAttributeLabel attributeEntry="${documentAttributes.vendorName}" />
                    </div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                   <kul:htmlControlAttribute 
                    	attributeEntry="${documentAttributes.vendorName}" property="document.vendorName"/>
                        <kul:lookup boClassName="org.kuali.ole.vnd.businessobject.VendorDetail" 
			            fieldConversions="vendorName:document.vendorName" />			            
			    </td>  
                
                <%-- <tbody id="vendorNameEnable">
                 
                    <th align=right valign=middle class="bord-l-b" width="25%"/>
                    <th align=right valign=middle class="bord-l-b" width="25%"/>
                    <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right">
                        <kul:htmlAttributeLabel attributeEntry="${documentAttributes.vendorName}" />
                    </div>
                    
                    </th>
                
                    <td align=left valign=middle class="datacell" width="25%">
                   
                    <kul:htmlControlAttribute 
                    	attributeEntry="${documentAttributes.vendorName}" property="document.vendorName"/>
                    <kul:lookup boClassName="org.kuali.ole.vnd.businessobject.VendorDetail" 
			            fieldConversions="vendorName:document.vendorName" />
			            
			        </td>
                </tbody> --%>
                
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="5%">
                        <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.beginDate}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="5%">
                    <kul:htmlControlAttribute 
                	    attributeEntry="${documentAttributes.beginDate}" property="document.beginDate"/>
                </td>
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <kul:htmlAttributeLabel attributeEntry="${documentAttributes.purchaseOrderType}" />
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${documentAttributes.purchaseOrderType}" property="document.purchaseOrderType"/>
                        <kul:lookup boClassName="org.kuali.ole.module.purap.businessobject.PurchaseOrderType" 
			            fieldConversions="purchaseOrderType:document.purchaseOrderType" />
                </td>
                
                <!-- <th align=right valign=middle class="bord-l-b" width="5%"/> 
                <th align=right valign=middle class="bord-l-b" width="5%"/>-->
                <th align=right valign=middle class="bord-l-b" width="5%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.endDate}" /></div>
                </th> 
                <td align=left valign=middle class="datacell" width="5%">
                    <kul:htmlControlAttribute 
                	    attributeEntry="${documentAttributes.endDate}" property="document.endDate"/>
                </td>
                
                <%-- <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.journal}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                 	    attributeEntry="${documentAttributes.journal}" property="document.journal"/>
                </td>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.purchaseOrderDate}" /></div>
                </th> 
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${documentAttributes.purchaseOrderDate}" property="document.purchaseOrderDate" onclick="enablePOStartDateEndDate(this)"/>
                </td> --%>
                <!-- <tbody id="purchaseOrderDateEnable"> -->
                    <!-- <th align=right valign=middle class="bord-l-b" width="5%"/> 
                    <th align=right valign=middle class="bord-l-b" width="5%"/>-->
                    <!-- <tr>
                    
                    </tr> -->
                <!-- </tbody> -->
                
                
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%" rowspan="1">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.claimFilter}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%" rowspan="1">
                    <kul:htmlControlAttribute
                            attributeEntry="${documentAttributes.claimFilter}" property="document.claimFilter"/>
                </td>
            </tr>
        </table>
    </div>
    <div class="tab-container" align=center>
	 <div id="globalbuttons" class="globalbuttons">
		 <html:image src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_search.gif" styleClass="globalbuttons" property="methodToCall.valueSearch" title="Search" alt="Search"/>
		 <html:image src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_clear.gif" styleClass="globalbuttons" property="methodToCall.valueClear" title="Clear" alt="Clear"/>
		 <html:image src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_cancel.gif" styleClass="globalbuttons" property="methodToCall.cancel" title="Cancel" alt="Cancel"/>
	 </div>
	 </div>
</kul:tabTop>
