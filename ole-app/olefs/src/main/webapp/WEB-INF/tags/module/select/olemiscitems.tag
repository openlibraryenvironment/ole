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

<%@ attribute name="overrideTitle" required="false"
              description="The title to be used for this section." %>
<%@ attribute name="documentAttributes" required="false" type="java.util.Map"
              description="The DataDictionary entry containing attributes for this row's fields." %>
<%@ attribute name="itemAttributes" required="true" type="java.util.Map"
              description="The DataDictionary entry containing attributes for this row's fields."%>
<%@ attribute name="accountingLineAttributes" required="true"
              type="java.util.Map"
              description="The DataDictionary entry containing attributes for this row's fields."%>
<%@ attribute name="showAmount" required="false"
              type="java.lang.Boolean"
              description="show the amount if true else percent" %>
<%@ attribute name="showInvoiced" required="false"
              type="java.lang.Boolean"
              description="post the unitPrice into the extendedPrice field" %>
<%@ attribute name="specialItemTotalType" required="false" %>
<%@ attribute name="specialItemTotalOverride" required="false" fragment="true"
              description="Fragment of code to specify special item total line" %>
<%@ attribute name="descriptionFirst" required="false" type="java.lang.Boolean"
              description="Whether or not to show item description before extended price." %>
<%@ attribute name="mainColumnCount" required="true" %>
<%@ attribute name="colSpanItemType" required="true" %>
<%@ attribute name="colSpanExtendedPrice" required="true" %>
<%@ attribute name="colSpanDescription" required="true" %>
<%@ attribute name="colSpanAmountPaid" required="true" %>
<script type="text/javascript">
    function editableProrateSurcharge(prorateManual){
        var div = document.getElementsByTagName('div');
        document.getElementById('document.prorateManual').checked=true;
        document.getElementById('document.prorateDollar').checked=false;
        document.getElementById('document.prorateQty').checked=false;
        document.getElementById('document.noProrate').checked=false;
        document.getElementsByName("methodToCall.proratedSurchargeRefresh")[0].click();
    }
    function noProration(prorateManual){
        var div = document.getElementsByTagName('div');
        document.getElementById('document.noProrate').checked=true;
        document.getElementById('document.prorateDollar').checked=false;
        document.getElementById('document.prorateQty').checked=false;
        document.getElementById('document.prorateManual').checked=false;
        document.getElementsByName("methodToCall.proratedSurchargeRefresh")[0].click();
    }
    function readOnlyProrateSurchargeForQty(prorateManual){
        var div = document.getElementsByTagName('div');
        document.getElementById('document.prorateQty').checked=true;
        document.getElementById('document.prorateDollar').checked=false;
        document.getElementById('document.prorateManual').checked=false;
        document.getElementById('document.noProrate').checked=false;
        document.getElementsByName('methodToCall.proratedSurchargeRefresh')[0].click();
    }
    function readOnlyProrateSurchargeForDollar(prorateManual){
        var div = document.getElementsByTagName('div');
        document.getElementById('document.prorateDollar').checked=true;
        document.getElementById('document.prorateManual').checked=false;
        document.getElementById('document.prorateQty').checked=false;
        document.getElementById('document.noProrate').checked=false;
        document.getElementsByName("methodToCall.proratedSurchargeRefresh")[0].click();
    }
    function forUsd(additionalChargeUsd,ctr){
        var attribute = document.getElementsByTagName('td');
        if(additionalChargeUsd.checked){
            for(var i=0;i<attribute.length;i++){
                if(attribute[i].getAttribute('id')=="foreignCurrency["+ctr+"]"){
                    attribute[i].style.display="none";
                }
                if(attribute[i].getAttribute('id')=="localCurrency["+ctr+"]"){
                    attribute[i].style.display="";
                }
            }
        }else{
            for(var i=0;i<attribute.length;i++){
                if(attribute[i].getAttribute('id')=="foreignCurrency["+ctr+"]"){
                    attribute[i].style.display="";
                }
                if(attribute[i].getAttribute('id')=="localCurrency["+ctr+"]"){
                    attribute[i].style.display="none";
                }
            }
        }
    }
</script>

<c:set var="fullEntryMode" value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT] && (empty KualiForm.editingMode['restrictFiscalEntry'])}" />
<c:set var="lockTaxAmountEntry" value="${(not empty KualiForm.editingMode['lockTaxAmountEntry']) || !fullEntryMode}" />
<c:set var="tabindexOverrideBase" value="50" />
<c:set var="isATypeOfPODoc" value="${KualiForm.document.isATypeOfPODoc}" />
<c:set var="colSpanUSD" value="0"/>
<c:if test="${empty overrideTitle}">
    <c:set var="overrideTitle" value="Additional Charges"/>
</c:if>

<c:set var="amendmentEntry"
       value="${(!empty KualiForm.editingMode['amendmentEntry'])}" />

<c:set var="documentType" value="${KualiForm.document.documentHeader.workflowDocument.documentTypeName}" />
<c:set var="isATypeOfPODoc" value="${KualiForm.document.isATypeOfPODoc}" />

<c:set var="currentTabIndex" value="${KualiForm.currentTabIndex}" scope="request" />
<c:set var="topLevelTabIndex" value="${KualiForm.currentTabIndex}" scope="request" />
<c:set var="tabKey" value="${kfunc:generateTabKey(overrideTitle)}" />
<!--  hit form method to increment tab index -->
<c:set var="dummyIncrementer" value="${kfunc:incrementTabIndex(KualiForm, tabKey)}" />
<c:set var="currentTab" value="${kfunc:getTabState(KualiForm, tabKey)}" />
<c:set var="purapTaxEnabled" value="${(not empty KualiForm.editingMode['purapTaxEnabled'])}" />

<%-- default to close --%>
<c:choose>
    <c:when test="${empty currentTab}">
        <c:set var="isOpen" value="false" />
    </c:when>
    <c:when test="${!empty currentTab}">
        <c:set var="isOpen" value="${currentTab == 'OPEN'}" />
    </c:when>
</c:choose>
<html:hidden property="tabStates(${tabKey})" value="${(isOpen ? 'OPEN' : 'CLOSE')}" />
<tr>
    <td colspan="${mainColumnCount+4}" class="subhead">
        <span class="subhead-left"><c:out value="${overrideTitle}" /> &nbsp;</span>
        <c:if test="${isOpen == 'true' || isOpen == 'TRUE'}">
            <html:image property="methodToCall.toggleTab.tab${tabKey}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-hide.gif" alt="hide" title="toggle" styleClass="tinybutton" styleId="tab-${tabKey}-imageToggle"
                        onclick="javascript: return toggleTab(document, '${tabKey}'); " />
        </c:if>
        <c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
            <html:image property="methodToCall.toggleTab.tab${tabKey}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-show.gif" alt="show" title="toggle" styleClass="tinybutton" styleId="tab-${tabKey}-imageToggle"
                        onclick="javascript: return toggleTab(document, '${tabKey}'); " />
        </c:if>
    </td>
</tr>

<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
    <tbody style="display: none;" id="tab-${tabKey}-div">
</c:if>

<tr>
    <kul:htmlAttributeHeaderCell colspan="${colSpanItemType-3}"
                                 attributeEntry="${itemAttributes.itemTypeCode}" />

    <c:if test="${showInvoiced}">
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.originalAmountfromPO}" />
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.poOutstandingAmount}" />
    </c:if>

    <c:choose>
        <c:when test="${descriptionFirst}">
            <kul:htmlAttributeHeaderCell colspan="${colSpanDescription-1}"
                                         attributeEntry="${itemAttributes.itemDescription}" />
            <kul:htmlAttributeHeaderCell colspan="${colSpanExtendedPrice-2}"
                                         attributeEntry="${itemAttributes.extendedPrice}" />
            <c:set var="colSpanBlank" value="${mainColumnCount - (colSpanItemType + colSpanDescription + colSpanExtendedPrice + colSpanAmountPaid)-6}" />
            <c:if test="${purapTaxEnabled}">
                <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemTaxAmount}" />
                <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.totalAmount}" />
                <c:set var="colSpanBlank" value="${colSpanBlank - 2}" />
            </c:if>
            <c:if test="${colSpanBlank > 0}">
                <th colspan="${colSpanBlank}">&nbsp;</th>
            </c:if>
            <c:if test="${isATypeOfPODoc}">
                <kul:htmlAttributeHeaderCell colspan="${colSpanAmountPaid}" literalLabel="Amount Paid" />
            </c:if>
        </c:when>
        <c:otherwise>
            <%--<c:if test="${KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator}">
                <c:set var="colSpanUSD" value="1"/>
                <kul:htmlAttributeHeaderCell colspan="1"
                                             attributeEntry="${itemAttributes.foreignCurrencyExtendedPrice}" />
                <kul:htmlAttributeHeaderCell
                        attributeEntry="${itemAttributes.itemCurrencyType}" colspan="1" />
                <kul:htmlAttributeHeaderCell
                        attributeEntry="${itemAttributes.itemExchangeRate}" colspan="1" />
            </c:if>--%>
            <kul:htmlAttributeHeaderCell colspan="${colSpanExtendedPrice-1}"
                                         attributeEntry="${itemAttributes.extendedPrice}" />
            <c:if test="${purapTaxEnabled}">
                <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemTaxAmount}" />
                <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.totalAmount}" />
            </c:if>
            <kul:htmlAttributeHeaderCell colspan="${colSpanDescription-1}"
                                         attributeEntry="${itemAttributes.itemDescription}" />
            <c:if test="${KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator}">
                <kul:htmlAttributeHeaderCell colspan="1"
                                             attributeEntry="${itemAttributes.additionalChargeUsd}" />
            </c:if>
            <td class="infoline" rowspan="20" colspan="3-colSpanUSD">
                <table>
                    <tr><td>
                        <kul:htmlControlAttribute
                                attributeEntry="${documentAttributes.prorateQty}"
                                property="document.prorateQty"
                                readOnly="false" onclick="readOnlyProrateSurchargeForQty(this)"/>Prorate By Quantity
                    </td></tr>

                    <tr><td>
                        <kul:htmlControlAttribute
                                attributeEntry="${documentAttributes.prorateDollar}"
                                property="document.prorateDollar"
                                readOnly="false" onclick="readOnlyProrateSurchargeForDollar(this)"/>Prorate By Dollar
                    </td></tr>

                    <tr><td>
                        <kul:htmlControlAttribute
                                attributeEntry="${documentAttributes.prorateManual}"
                                property="document.prorateManual"
                                readOnly="false" onclick = "editableProrateSurcharge(this)"/>Manual Prorate
                    </td></tr>
                    <tr><td>
                        <kul:htmlControlAttribute
                                attributeEntry="${documentAttributes.noProrate}"
                                property="document.noProrate"
                                readOnly="false" onclick = "noProration(this)"/>No Proration
                    </td></tr>
                    <tr><td>
                        <div align="center" style="display:none;">
                                <html:image property="methodToCall.proratedSurchargeRefresh"
                                            src="${ConfigProperties.externalizable.images.url}tinybutton-refaccsum.gif" alt="refresh account summary"
                                            />
                        </div>

                    </td></tr>
                </table>
            </td>

        </c:otherwise>
    </c:choose>
</tr>

<logic:iterate indexId="ctr" name="KualiForm" property="document.items" id="itemLine">
<%-- to ensure order this should pull out items from APC instead of this--%>
<c:if test="${itemLine.itemType.additionalChargeIndicator && !itemLine.itemType.isTaxCharge}">
<c:if test="${not empty specialItemTotalType and itemLine.itemTypeCode == specialItemTotalType }">
    <c:if test="${!empty specialItemTotalOverride}">
        <jsp:invoke fragment="specialItemTotalOverride"/>
    </c:if>
</c:if>
<tr>
    <td colspan="${mainColumnCount+colSpanUSD-6}" class="tab-subhead" style="border-right: none;">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemTypeCode}"
                property="document.item[${ctr}].itemType.itemTypeDescription"
                readOnly="${true}" /> <!-- TODO need the show/hide? --></td>
</tr>

<tr>
    <td class="infoline" colspan="${colSpanItemType-3}">
        <div align="right">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemTypeCode}" property="document.item[${ctr}].itemType.itemTypeDescription" readOnly="${true}" />:&nbsp;
        </div>
    </td>

    <c:if test="${showInvoiced}">

        <td class="infoline">
            <kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.purchaseOrderItemUnitPrice}"
                    property="document.item[${ctr}].purchaseOrderItemUnitPrice"
                    readOnly="true" />
        </td>
        <td class="infoline">
            <kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.poOutstandingAmount}"
                    property="document.item[${ctr}].poOutstandingAmount"
                    readOnly="true" />
        </td>
        <%--<c:if test="${KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator}">
            <td class="infoline" colspan="2"><kul:htmlControlAttribute
                    property="document.item[${ctr}].foreignCurrencyExtendedPrice"
                    attributeEntry="${itemAttributes.foreignCurrencyExtendedPrice}"
                    readOnly="false" tabindexOverride="${tabindexOverrideBase + 0}" />
            </td>
            <td class="infoline" colspan="1" style="" id="foreignCurrency[${ctr}]"><kul:htmlControlAttribute
                    property="document.vendorDetail.currencyType.currencyType"
                    attributeEntry="${itemAttributes.itemCurrencyType}"
                    readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" />
            </td>
            <td class="infoline" colspan="1" style="display:none" id="localCurrency[${ctr}]"><kul:htmlControlAttribute
                    property="document.item[${ctr}].itemCurrencyType"
                    attributeEntry="${itemAttributes.itemCurrencyType}"
                    readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" />
            </td>
            <td class="infoline" colspan="1"  style="" id="foreignCurrency[${ctr}]"><kul:htmlControlAttribute
                    property="document.item[${ctr}].itemExchangeRate"
                    attributeEntry="${itemAttributes.itemExchangeRate}"
                    readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" />
            </td>
            <td class="infoline" colspan="1"  style="display:none" id="localCurrency[${ctr}]">
            </td>
        </c:if>--%>
    </c:if>

    <c:if test="${!showInvoiced}">

        <c:if test="${KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator}">
            <td class="infoline" colspan="2"><kul:htmlControlAttribute
                    property="document.item[${ctr}].foreignCurrencyExtendedPrice"
                    attributeEntry="${itemAttributes.foreignCurrencyExtendedPrice}"
                    readOnly="false" tabindexOverride="${tabindexOverrideBase + 0}" />
            </td>
            <td class="infoline" colspan="1" style="" id="foreignCurrency[${ctr}]"><kul:htmlControlAttribute
                    property="document.vendorDetail.currencyType.currencyType"
                    attributeEntry="${itemAttributes.itemCurrencyType}"
                    readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" />
            </td>
            <td class="infoline" colspan="1" style="display:none" id="localCurrency[${ctr}]"><kul:htmlControlAttribute
                    property="document.item[${ctr}].itemCurrencyType"
                    attributeEntry="${itemAttributes.itemCurrencyType}"
                    readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" />
            </td>
            <td class="infoline" colspan="1"  style="" id="foreignCurrency[${ctr}]"><kul:htmlControlAttribute
                    property="document.item[${ctr}].itemExchangeRate"
                    attributeEntry="${itemAttributes.itemExchangeRate}"
                    readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" />
            </td>
            <td class="infoline" colspan="1"  style="display:none" id="localCurrency[${ctr}]">
            </td>
        </c:if>
    </c:if>


    <c:choose>
        <c:when test="${descriptionFirst}">
            <td class="infoline" colspan="${colSpanDescription-3}">
                <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemDescription}" property="document.item[${ctr}].itemDescription" readOnly="${not (fullEntryMode or amendmentEntry)}" tabindexOverride="${tabindexOverrideBase + 0}"/>
            </td>
            <c:if test="${not KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator}">
                <td class="infoline" colspan="${colSpanExtendedPrice-2}">
                    <div align="right">
                        <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemUnitPrice}" property="document.item[${ctr}].itemUnitPrice" readOnly="${not (fullEntryMode or amendmentEntry)}" styleClass="amount" tabindexOverride="${tabindexOverrideBase + 0}"/>
                    </div>
                </td>
            </c:if>
            <c:if test="${KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator}">
                <td class="infoline" colspan="${colSpanExtendedPrice-1}">
                    <div align="right">
                        <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemUnitPrice}" property="document.item[${ctr}].itemUnitPrice" readOnly="true" styleClass="amount" tabindexOverride="${tabindexOverrideBase + 0}"/>
                    </div>
                </td>
            </c:if>
            <c:if test="${purapTaxEnabled and itemLine.itemType.taxableIndicator}">
                <td class="infoline">
                    <div align="right">
                        <kul:htmlControlAttribute
                                attributeEntry="${itemAttributes.itemTaxAmount}"
                                property="document.item[${ctr}].itemTaxAmount" readOnly="${lockTaxAmountEntry}"
                                tabindexOverride="${tabindexOverrideBase + 0}"/>
                    </div>
                </td>
                <td class="infoline">
                    <div align="right">
                        <kul:htmlControlAttribute
                                attributeEntry="${itemAttributes.totalAmount}"
                                property="document.item[${ctr}].totalAmount" readOnly="true"
                                tabindexOverride="${tabindexOverrideBase + 0}"/>
                    </div>
                </td>
            </c:if>

            <c:if test="${colSpanBlank > 0}">
                <td class="infoline" colspan="${colSpanBlank}">
                    &nbsp;
                </td>
            </c:if>

            <c:if test="${isATypeOfPODoc}">
                <td class="infoline">
                    <div align="right">
                        <kul:htmlControlAttribute
                                attributeEntry="${itemAttributes.itemInvoicedTotalAmount}"
                                property="document.item[${ctr}].itemInvoicedTotalAmount" readOnly="${true}"/>
                    </div>
                </td>
            </c:if>
        </c:when>

        <c:otherwise>
            <c:if test="${not KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator}">
                <td class="infoline" colspan="${colSpanExtendedPrice-1}">
                    <div align="right">
                        <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemUnitPrice}" property="document.item[${ctr}].itemUnitPrice" readOnly="${not (fullEntryMode or amendmentEntry)}" styleClass="amount" tabindexOverride="${tabindexOverrideBase + 0}"/>
                    </div>
                </td>
            </c:if>
            <c:if test="${KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator}">
                <td class="infoline" colspan="${colSpanExtendedPrice-1}">
                    <div align="right">
                        <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemUnitPrice}" property="document.item[${ctr}].itemUnitPrice" readOnly="true" styleClass="amount" tabindexOverride="${tabindexOverrideBase + 0}"/>
                    </div>
                </td>
            </c:if>

            <c:if test="${purapTaxEnabled}">
                <c:choose>
                    <c:when test="${itemLine.itemType.taxableIndicator}">
                        <td class="infoline">
                            <div align="right">
                                <kul:htmlControlAttribute
                                        attributeEntry="${itemAttributes.itemTaxAmount}"
                                        property="document.item[${ctr}].itemTaxAmount" readOnly="${lockTaxAmountEntry}"
                                        tabindexOverride="${tabindexOverrideBase + 0}"/>
                            </div>
                        </td>
                        <td class="infoline">
                            <div align="right">
                                <kul:htmlControlAttribute
                                        attributeEntry="${itemAttributes.totalAmount}"
                                        property="document.item[${ctr}].totalAmount" readOnly="true"
                                        tabindexOverride="${tabindexOverrideBase + 0}"/>
                            </div>
                        </td>
                    </c:when>
                    <c:otherwise>
                        <td class="infoline">
                            &nbsp;
                        </td>
                        <td class="infoline">
                            &nbsp;
                        </td>
                    </c:otherwise>
                </c:choose>
            </c:if>

            <td class="infoline" colspan="${colSpanDescription-1}">
                <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemDescription}" property="document.item[${ctr}].itemDescription" readOnly="${not (fullEntryMode or amendmentEntry)}" tabindexOverride="${tabindexOverrideBase + 0}"/>
            </td>
            <%--<c:if test="${KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator}">
                <td class="infoline" colspan="1">
                    <kul:htmlControlAttribute attributeEntry="${itemAttributes.additionalChargeUsd}" property="document.item[${ctr}].additionalChargeUsd" readOnly="false" tabindexOverride="${tabindexOverrideBase + 0}" onclick="forUsd(this,${ctr})"/>
                </td>
            </c:if>--%>
        </c:otherwise>
    </c:choose>
</tr>

<c:if test="${amendmentEntry}">
    <purap:purapGeneralAccounting
            accountPrefix="document.item[${ctr}]."
            itemColSpan="${mainColumnCount+colSpanUSD-6}"/>
</c:if>

<c:if test="${!empty KualiForm.editingMode['allowItemEntry'] && !empty itemLine.itemUnitPrice || empty KualiForm.editingMode['allowItemEntry']}">
    <c:if test="${!amendmentEntry && KualiForm.document.statusCode!='AFOA' || KualiForm.document.statusCode=='AFOA' && !empty KualiForm.document.items[ctr].itemUnitPrice}">
        <purap:purapGeneralAccounting
                accountPrefix="document.item[${ctr}]."
                itemColSpan="${mainColumnCount+colSpanUSD-6}" />
    </c:if>
</c:if>
</c:if>
</logic:iterate>

<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
    </tbody>
</c:if>
