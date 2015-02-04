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
<%@ attribute name="itemAttributes" required="true" type="java.util.Map"
    description="The DataDictionary entry containing attributes for this row's fields." %>  

<%@ attribute name="documentAttributes" required="false" type="java.util.Map"
    description="The DataDictionary entry containing attributes for this row's fields." %>  

<%@ attribute name="accountPrefix" required="false" description="an optional prefix to specify a different location for acocunting lines rather than just on the document."%>
<%@ attribute name="itemColSpan" required="true" description="item columns to span"%>
<%@ attribute name="count" required="true" description="item number" %>


   
    
    <%-- add extra columns count for the "Action" button and/or dual amounts --%>

    <c:set var="currentTabIndex" value="${KualiForm.currentTabIndex}" scope="request" />
    <c:set var="topLevelTabIndex" value="${KualiForm.currentTabIndex}" scope="request" />
    <c:set var="vendorCurrencyType" value="${KualiForm.document.vendorDetail.currencyType.currencyType}"/>
    <c:set var="tabTitle" value="AccountingLines-${currentTabIndex}" />
    <c:set var="tabKey" value="${kfunc:generateTabKey(tabTitle)}"/>
    <!--  hit form method to increment tab index -->
    <c:set var="dummyIncrementer" value="${kfunc:incrementTabIndex(KualiForm, tabKey)}" />
    <c:set var="currentTab" value="${kfunc:getTabState(KualiForm, tabKey)}"/>
    <c:set var="isFinal" value="${KualiForm.document.isFinalReqs}"/>
	<%-- default to closed --%>
	<c:choose>
		<c:when test="${empty currentTab}">
			<c:set var="isOpen" value="false" />
			<html:hidden property="tabStates(${tabKey})" value="CLOSE" />		
		</c:when>
		<c:when test="${!empty currentTab}">
			<c:set var="isOpen" value="${currentTab == 'OPEN'}" />
		</c:when>
	</c:choose>

	<tr>
	<td colspan="${itemColSpan}">
	<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<th style="padding: 0px; border-right: none;">
		    <div align=left>
		  	    <c:if test="${isOpen == 'true' || isOpen == 'TRUE'}">
		         <html:image property="methodToCall.toggleTab.tab${tabKey}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-hide.gif" alt="hide" title="toggle" styleClass="tinybutton" styleId="tab-${tabKey}-imageToggle" onclick="javascript: return toggleTab(document, '${tabKey}'); " />
		     </c:if>
		     <c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
		         <html:image property="methodToCall.toggleTab.tab${tabKey}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-show.gif" alt="show" title="toggle" styleClass="tinybutton" styleId="tab-${tabKey}-imageToggle" onclick="javascript: return toggleTab(document, '${tabKey}'); " />
		     </c:if>
		    	Foreign Currency Conversion
		    </div>
		</th>
	</tr>

	<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
	    <tr style="display: none;"  id="tab-${tabKey}-div">
	</c:if>   
        <th>              
            <table>   <tr>
			<td colspan="${itemColSpan}" class="subhead">
			    <span class="subhead-left">Foreign Currency Conversion</span>
			</td>
		</tr> 
          <tr>
		        <th>
                        <kul:htmlAttributeLabel attributeEntry="${itemAttributes.itemCurrencyType}" /></div>
                    </th>
                    <th>
                        <kul:htmlAttributeLabel attributeEntry="${itemAttributes.itemForeignListPrice}" forceRequired="true"/></div>
                    </th>
                    <th>
                        <kul:htmlAttributeLabel attributeEntry="${itemAttributes.itemForeignDiscount}" /></div>
                    </th>
                    <th>
                        <kul:htmlAttributeLabel attributeEntry="${itemAttributes.itemForeignDiscountType}" /></div>
                    </th>
                    <th>
                        <kul:htmlAttributeLabel attributeEntry="${itemAttributes.itemForeignDiscountAmt}" /></div>
                    </th>
                    <th>
                        <kul:htmlAttributeLabel attributeEntry="${itemAttributes.itemForeignUnitCost}" /></div>
                    </th>
                    <th>
                        <kul:htmlAttributeLabel attributeEntry="${itemAttributes.itemExchangeRate}" /></div>
                    </th>
                    <th>
                        <kul:htmlAttributeLabel attributeEntry="${itemAttributes.itemUnitCostUSD}" /></div>
                    </th>
                    <kul:htmlAttributeHeaderCell literalLabel="Action" colspan="${colSpanAction}" />
          </tr>         

             <tr>
		        <td class="infoline">
		            <!--<c:out value="${KualiForm.document.vendorDetail.currencyType.currencyType}"/> 
		            --><kul:htmlControlAttribute
		                property="document.vendorDetail.currencyType.currencyType"
		                attributeEntry="${itemAttributes.itemCurrencyType}"
		                readOnly="${isFinal}"
		                tabindexOverride="${tabindexOverrideBase + 0}"/>
		             </td>
		         <td class="infoline">
		            <kul:htmlControlAttribute
		                property="${accountPrefix}itemForeignListPrice"
		                attributeEntry="${itemAttributes.itemForeignListPrice}"
		                readOnly="${isFinal}"
		                tabindexOverride="${tabindexOverrideBase + 0}"/>  </td>
		         <td class="infoline">
		            <kul:htmlControlAttribute
		                property="${accountPrefix}itemForeignDiscount"
		                attributeEntry="${itemAttributes.itemForeignDiscount}"
		                readOnly="${isFinal}"
		                tabindexOverride="${tabindexOverrideBase + 0}"/>  </td>
		         <td class="infoline">
		            <kul:htmlControlAttribute
		                property="${accountPrefix}itemForeignDiscountType"
		                attributeEntry="${itemAttributes.itemForeignDiscountType}"
		                readOnly="${isFinal}"
		                tabindexOverride="${tabindexOverrideBase + 0}"/>  </td>
		         <td class="infoline">
		            <kul:htmlControlAttribute
		                property="${accountPrefix}itemForeignDiscountAmt"
		                attributeEntry="${itemAttributes.itemForeignDiscountAmt}"
		                readOnly="${isFinal}"
		                tabindexOverride="${tabindexOverrideBase + 0}"/>  </td>
		         <td class="infoline">
		            <kul:htmlControlAttribute
		                property="${accountPrefix}itemForeignUnitCost"
		                attributeEntry="${itemAttributes.itemForeignUnitCost}"
		                readOnly="${isFinal}"
		                tabindexOverride="${tabindexOverrideBase + 0}"/>  </td>
		         <td class="infoline">
		           
		                <kul:htmlControlAttribute
		                property="${accountPrefix}itemExchangeRate"
		                attributeEntry="${itemAttributes.itemExchangeRate}"
		                readOnly="${isFinal}"
		                tabindexOverride="${tabindexOverrideBase + 0}"/>
		          </td>
		         <td class="infoline">
		                <kul:htmlControlAttribute
		                property="${accountPrefix}itemUnitCostUSD"
		                attributeEntry="${itemAttributes.itemUnitCostUSD}"
		                readOnly="${isFinal}"
		                tabindexOverride="${tabindexOverrideBase + 0}"/>  </td>
		        
		        
                    <!--<td>
                        <kul:htmlControlAttribute 
                        	attributeEntry="${documentAttributes.notes}" property="document.notes" 
                        	readOnly="false" tabindexOverride="${tabindexOverrideBase + 4}"/>
                    </td>
		       -->
		       <td class="infoline" rowspan="2" colspan="${colSpanAction}">
				    <div align="center">
				        <html:image src="${ConfigProperties.kr.externalizable.images.url}tinybutton_calculate.gif" styleClass="globalbuttons" property="methodToCall.calculateCurrency" title="Calculate" alt="Calculate"/>
				    </div>
				</td>

               </tr> </table>
        </th>
    
	<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
	    </tr>
	</c:if>

</table>
</td>
</tr>



