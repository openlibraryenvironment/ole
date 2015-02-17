
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

<%@ attribute name="itemAttributes" required="true" type="java.util.Map" description="The DataDictionary entry containing attributes for this row's fields."%>
<%@ attribute name="accountingLineAttributes" required="true" type="java.util.Map" description="The DataDictionary entry containing attributes for this row's fields."%>
<%@ attribute name="mainColumnCount" required="true" %>

<c:set var="fullEntryMode" value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT]}" />
<c:set var="lockTaxAmountEntry" value="${(not empty KualiForm.editingMode['lockTaxAmountEntry'])}" />
<c:set var="clearAllTaxes" value="${(not empty KualiForm.editingMode['clearAllTaxes'])}" />
<c:set var="purapTaxEnabled" value="${(not empty KualiForm.editingMode['purapTaxEnabled'])}" />
<c:set var="fullDocEntryCompleted" value="${(not empty KualiForm.editingMode['fullDocumentEntryCompleted'])}" />
<c:set var="colSpanDescription" value="2"/>
<c:set var="colSpanAction" value="2" />
<c:set var="colSpanForeignTab" value="17" />
<c:set var="isSaved" value="${KualiForm.document.isSaved}"/>
<c:if test="${purapTaxEnabled}">
    <c:set var="colSpanDescription" value="1"/>
</c:if>
<c:set var="dublinEditorEditURL"
       value="${KualiForm.document.dublinEditorEditURL}" />
<c:set var="dublinEditorViewURL"
       value="${KualiForm.document.dublinEditorViewURL}" />
<c:set var="bibeditorCreateURL" value="${KualiForm.document.bibeditorCreateURL}" />
<c:set var="bibeditorEditURL" value="${KualiForm.document.bibeditorEditURL}" />
<c:set var="bibeditorViewURL" value="${KualiForm.document.bibeditorViewURL}" />
<c:set var="docNumber" value="${KualiForm.document.documentNumber }" />
<c:set var="marcXMLFileDirLocation" value="${KualiForm.document.marcXMLFileDirLocation}" />
<c:set var="usePO" value="true" />
<!--  replace literal with PurapConstants once exported -->
<c:if test="${KualiForm.document.creditMemoType eq 'PREQ'}" >
    <c:set var="usePO" value="false" />
</c:if>
<c:set var="tabindexOverrideBase" value="50" />
<c:set var="itemNo" value="0" />

<tr>
    <td colspan="${mainColumnCount+7}" class="subhead">
        <span class="subhead-left">Process Titles</span>
    </td>
</tr>
<c:if test="${usePO}" >
<c:if test="${not(fullDocEntryCompleted)}" >
<tr>
    <td colspan="${mainColumnCount+7}" class="subhead">
        <span class="subhead-left">Add Item</span>
    </td>
</tr>
<tr>

    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemLineNumber}" width="2%"/>
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.poInvoicedTotalQuantity}" width="12%"/>
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.poUnitPrice}" width="12%"/>
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.poTotalAmount}" width="20%"/>


    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemQuantity}" width="12%"/>
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemUnitPrice}" width="20%"/>
    <%--<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemSurcharge}" width="12%"/>--%>
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.extendedPrice}" width="12%"/>


    <c:if test="${purapTaxEnabled}">
        <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemTaxAmount}" width="12%"/>
        <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.totalAmount}" width="12%"/>
    </c:if>

    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemCatalogNumber}" width="12%"/>
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemDescription}" width="25%" colspan="${colSpanDescription}"/>
    <kul:htmlAttributeHeaderCell literalLabel="Action" 	colspan="${colSpanAction}" />
</tr>
<c:if
        test="${KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator }">
    <c:set var="rowSpanLineNum" value="4" />
</c:if>
<c:if
        test="${not KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator }">
    <c:set var="rowSpanLineNum" value="1" />
</c:if>

<tr>
    <td class="infoline" rowspan="${rowSpanLineNum}">
        <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemLineNumber}"
                                  property="newPurchasingItemLine.itemLineNumber" readOnly="true"/>
    </td>
    <c:if test="${usePO}">
        <td class="infoline">
            <div align="right">
                <kul:htmlControlAttribute attributeEntry="${itemAttributes.poInvoicedTotalQuantity}"
                                          property="newPurchasingItemLine.poInvoicedTotalQuantity" />
            </div>
        </td>
        <td class="infoline">
            <div align="right">
                <kul:htmlControlAttribute attributeEntry="${itemAttributes.poUnitPrice}"
                                          property="newPurchasingItemLine.poUnitPrice"/>
            </div>
        </td>
        <td class="infoline" rowspan="1" colspan="1">
            <div align="right">
                <kul:htmlControlAttribute attributeEntry="${itemAttributes.poTotalAmount}"
                                          property="newPurchasingItemLine.poTotalAmount" readOnly="true"/>
            </div>
        </td>
    </c:if>
    <c:if test="${!usePO}">
        <td class="infoline">
            <div align="right">
                <kul:htmlControlAttribute attributeEntry="${itemAttributes.preqInvoicedTotalQuantity}"
                                          property="newPurchasingItemLine.preqInvoicedTotalQuantity"/>
            </div>
        </td>
        <td class="infoline">
            <div align="right">
                <kul:htmlControlAttribute attributeEntry="${itemAttributes.preqUnitPrice}"
                                          property="newPurchasingItemLine.preqUnitPrice"/>
            </div>
        </td>
        <td class="infoline" rowspan="1" colspan="1">
            <div align="right">
                <kul:htmlControlAttribute attributeEntry="${itemAttributes.preqTotalAmount}"
                                          property="newPurchasingItemLine.preqTotalAmount" readOnly="true"/>
            </div>
        </td>
    </c:if>
    <td class="infoline">
        <div align="right">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemQuantity}"
                                      property="newPurchasingItemLine.itemQuantity"/>
        </div>
    </td>
    <c:if
            test="${KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator }">
        <td class="infoline">
            <div align="right">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.itemUnitPrice}"
                        property="newPurchasingItemLine.itemUnitPrice"
                        tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true" />
            </div>
        </td>
    </c:if>
    <c:if
            test="${not KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator }">
        <td class="infoline">
            <div align="right">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.itemUnitPrice}"
                        property="newPurchasingItemLine.itemUnitPrice"
                        tabindexOverride="${tabindexOverrideBase + 0}" /></div></td>
    </c:if>
    <%--<td class="infoline">
        <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemSurcharge}"
                                  property="newPurchasingItemLine.itemSurcharge"
                                  readOnly="true"/>
    </td>--%>
    <td class="infoline">
        <div align="right">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.extendedPrice}"
                                      property="newPurchasingItemLine.extendedPrice"/>
        </div>
    </td>
    <c:if test="${purapTaxEnabled}">
        <td class="infoline">
            <div align="right">
                <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemTaxAmount}"
                                          property="newPurchasingItemLine.itemTaxAmount"
                                          readOnly="true"/>
            </div>
        </td>
        <td class="infoline">
            <div align="right">
                <kul:htmlControlAttribute attributeEntry="${itemAttributes.totalAmount}"
                                          property="newPurchasingItemLine.totalAmount"
                                          readOnly="true"/>
            </div>
        </td>
    </c:if>
    <td class="infoline">
        <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemCatalogNumber}"
                                  property="newPurchasingItemLine.itemCatalogNumber" readOnly="true"/>

    </td>
    <td class="infoline" rowspan="1" colspan="2">
        <div align="right">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemDescription}"
                                      property="newPurchasingItemLine.itemDescription" />
        </div>
    </td>
    <c:if
            test="${KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator }">
        <td class="infoline" rowspan="1" colspan="1">
            <div align="right"><html:image
                    property="methodToCall.addItem"
                    src="${ConfigProperties.kr.externalizable.images.url}tinybutton-add1.gif"
                    alt="Insert an Item" title="Add an Item" styleClass="tinybutton"
                    tabindex="${tabindexOverrideBase + 0}" /></div>
        </td>
    </c:if>
    <c:if
            test="${not KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator }">
        <td class="infoline" rowspan="1" colspan="1">
            <div align="right"><html:image
                    property="methodToCall.addItem"
                    src="${ConfigProperties.kr.externalizable.images.url}tinybutton-add1.gif"
                    alt="Insert an Item" title="Add an Item" styleClass="tinybutton"
                    tabindex="${tabindexOverrideBase + 0}" /></div>
        </td>
    </c:if>
</tr>

<c:if
        test="${KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator }">
    <tr>
        <td colspan="${colSpanForeignTab}" class="subhead"><span
                class="subhead-left">Foreign Currency Conversion</span>
        </td>
    </tr>
    <tr>

        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemCurrencyType}" colspan="2" width="20%"/>
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignListPrice}"
                forceRequired="true" colspan="1" />
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignDiscount}"
                colspan="1" />
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignDiscountType}" colspan="2" />
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignDiscountAmt}"
                />
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignUnitCost}"
                colspan="1" />
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemExchangeRate}" colspan="2" />
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemUnitCostUSD}" colspan="2" />
    </tr>

    <tr>
        <td class="infoline" colspan="2">
            <div align="center">
                <kul:htmlControlAttribute
                        property="document.vendorDetail.currencyType.currencyType"
                        attributeEntry="${itemAttributes.itemCurrencyType}"
                        readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" />
        </td>
        <td class="infoline" colspan="1">
            <div align="center">
                <kul:htmlControlAttribute
                        property="newPurchasingItemLine.itemForeignListPrice"
                        attributeEntry="${itemAttributes.itemForeignListPrice}"
                        readOnly="false" tabindexOverride="${tabindexOverrideBase + 0}" />
        </td>
        <td class="infoline" colspan="1">
            <div align="center">
                <kul:htmlControlAttribute
                        property="newPurchasingItemLine.itemForeignDiscount"
                        attributeEntry="${itemAttributes.itemForeignDiscount}"
                        readOnly="false" tabindexOverride="${tabindexOverrideBase + 0}" />
        </td>
        <td class="infoline" colspan="2">
            <div align="center">
                <kul:htmlControlAttribute
                        property="newPurchasingItemLine.itemForeignDiscountType"
                        attributeEntry="${itemAttributes.itemForeignDiscountType}"
                        readOnly="false" tabindexOverride="${tabindexOverrideBase + 0}" />
        </td>
        <td class="infoline">
            <div align="center">
                <kul:htmlControlAttribute
                        property="newPurchasingItemLine.itemForeignDiscountAmt"
                        attributeEntry="${itemAttributes.itemForeignDiscountAmt}"
                        readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" />
        </td>
        <td class="infoline" colspan="1">
            <div align="center">
                <kul:htmlControlAttribute
                        property="newPurchasingItemLine.itemForeignUnitCost"
                        attributeEntry="${itemAttributes.itemForeignUnitCost}"
                        readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" />
        </td>
        <td class="infoline" colspan="2">
            <div align="center">
                <kul:htmlControlAttribute
                        property="newPurchasingItemLine.itemExchangeRate"
                        attributeEntry="${itemAttributes.itemExchangeRate}"
                        readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" />
        </td>
        <td class="infoline" colspan="2">
            <div align="center">
                <kul:htmlControlAttribute
                        property="newPurchasingItemLine.itemUnitCostUSD"
                        attributeEntry="${itemAttributes.itemUnitCostUSD}"
                        readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" />
        </td>
    </tr>
</c:if>
<tr>

    <td class="subhead">
        <div>Bib Info:</div>
    </td>
    <th colspan="1" rowspan="1">&nbsp; Bib Editor</th>
    <td class="infoline" colspan="1">
        <div align="right">
            <!--<input type="button" id="bibCreateCurrentItemButton" value="create"
					onclick="javascript:addXMLContent('${bibeditorCreateURL}','','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')" /> -->
            <input type="image" id="bibCreateCurrentItemButton${ctr}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-createnew.gif" onclick="javascript:addXMLContent('${bibeditorCreateURL}','','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')" />
        </div>

    </td>
</tr>
</c:if>
</c:if>

<c:if test="${KualiForm.countOfAboveTheLine>=1}">
    <tr>
        <td colspan="${mainColumnCount+7}" class="subhead">
            <c:if test="${usePO}" >
                <span class="subhead-left">Current Items</span>
            </c:if>
            <c:if test="${!usePO}" >
                <span class="subhead-left">Items</span>
            </c:if>
        </td>
    </tr>
    <c:if
            test="${KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator}">
        <c:set var="currentItemRowspan" value="7" />
    </c:if>
    <c:if
            test="${!KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator}">
        <c:set var="currentItemRowspan" value="4" />

    </c:if>
    <tr>
        <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemLineNumber}" width="2%"/>

        <c:if test="${usePO}" >
            <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.poInvoicedTotalQuantity}" width="12%"/>
            <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.poUnitPrice}" width="12%"/>
            <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.poTotalAmount}" width="12%"/>
        </c:if>

        <c:if test="${!usePO}" >
            <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.preqInvoicedTotalQuantity}" width="12%"/>
            <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.preqUnitPrice}" width="12%"/>
            <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.preqTotalAmount}" width="12%"/>
        </c:if>

        <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemQuantity}" width="12%"/>
        <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemUnitPrice}" width="12%"/>
        <%--<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemSurcharge}" width="12%"/>--%>
        <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.extendedPrice}" width="12%"/>

        <c:if test="${purapTaxEnabled}">
            <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemTaxAmount}" width="12%"/>
            <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.totalAmount}" width="12%"/>
        </c:if>

        <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemCatalogNumber}" width="12%"/>
        <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemDescription}" width="25%" colspan="${colSpanDescription}"/>
        <kul:htmlAttributeHeaderCell literalLabel="Action" 	colspan="${colSpanAction}" />
    </tr>
</c:if>

<c:if test="${KualiForm.countOfAboveTheLine<1}">
    <tr>
        <th height=30 colspan="${mainColumnCount}">No items added to document</th>
    </tr>
</c:if>

<logic:iterate indexId="ctr" name="KualiForm" property="document.items" id="itemLine">

<c:if test="${itemLine.itemType.lineItemIndicator == true}">
<c:set var="currentTabIndex" value="${KualiForm.currentTabIndex}" scope="request" />
<c:set var="topLevelTabIndex" value="${KualiForm.currentTabIndex}" scope="request" />

<c:choose>
    <c:when test="${itemLine.objectId == null}">
        <c:set var="newObjectId" value="<%= (new org.kuali.rice.kns.util.Guid()).toString()%>" />
        <c:set var="tabKey" value="Item-${newObjectId}" />
    </c:when>
    <c:when test="${itemLine.objectId != null}">
        <c:set var="tabKey" value="Item-${itemLine.objectId}" />
    </c:when>
</c:choose>

<!--  hit form method to increment tab index -->
<c:set var="dummyIncrementer" value="${kfunc:incrementTabIndex(KualiForm, tabKey)}" />
<c:set var="currentTab" value="${kfunc:getTabState(KualiForm, tabKey)}"/>

<%-- default to closed --%>
<c:choose>
    <c:when test="${empty currentTab}">
        <c:set var="isOpen" value="false" />
    </c:when>
    <c:when test="${!empty currentTab}">
        <c:set var="isOpen" value="${(isOpen ? 'OPEN' : 'CLOSE')}" />
    </c:when>
</c:choose>

<tr>
    <td class="infoline" nowrap="nowrap" rowspan="${rowSpanLineNum}">
        &nbsp;<b><bean:write name="KualiForm" property="document.item[${ctr}].itemLineNumber"/></b>
    </td>

    <c:if test="${usePO}" >
        <td class="infoline">
            <c:if test="${itemLine.itemType.quantityBasedGeneralLedgerIndicator}" >
                <div align="right">
                    <kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.poInvoicedTotalQuantity}"
                            property="document.item[${ctr}].poInvoicedTotalQuantity"
                            readOnly="true" styleClass="infoline" />
                </div>
            </c:if>
            <c:if test="${!itemLine.itemType.quantityBasedGeneralLedgerIndicator}" >
                &nbsp;
            </c:if>
        </td>
        <td class="infoline">
            <div align="right">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.poUnitPrice}"
                        property="document.item[${ctr}].poUnitPrice"
                        readOnly="true" styleClass="infoline" />
            </div>
        </td>
        <td class="infoline">
            <div align="right">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.poTotalAmount}"
                        property="document.item[${ctr}].poTotalAmount"
                        readOnly="true" styleClass="infoline" />
            </div>
        </td>
    </c:if>

    <c:if test="${!usePO}" >
        <td class="infoline">
            <c:if test="${itemLine.itemType.quantityBasedGeneralLedgerIndicator}" >
                <div align="right">
                    <kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.preqInvoicedTotalQuantity}"
                            property="document.item[${ctr}].preqInvoicedTotalQuantity"
                            readOnly="true" styleClass="infoline" />
                </div>
            </c:if>
            <c:if test="${!itemLine.itemType.quantityBasedGeneralLedgerIndicator}" >
                &nbsp;
            </c:if>
        </td>
        <td class="infoline">
            <div align="right">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.preqUnitPrice}"
                        property="document.item[${ctr}].preqUnitPrice"
                        readOnly="true" styleClass="infoline" />
            </div>
        </td>
        <td class="infoline">
            <div align="right">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.preqTotalAmount}"
                        property="document.item[${ctr}].preqTotalAmount"
                        readOnly="true" styleClass="infoline" />
            </div>
        </td>
    </c:if>

    <td class="infoline">
        <div align="right">
            <c:if test="${itemLine.itemType.quantityBasedGeneralLedgerIndicator}" >
                <div align="right">
                    <kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.itemQuantity}"
                            property="document.item[${ctr}].itemQuantity"
                            readOnly="${not (fullEntryMode)}" styleClass="amount"
                            tabindexOverride="${tabindexOverrideBase + 0}"/>
                </div>
            </c:if>
            <c:if test="${!itemLine.itemType.quantityBasedGeneralLedgerIndicator}" >
                &nbsp;
            </c:if>
        </div>
    </td>
    <c:if test="${KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator}">
        <td class="infoline">
            <div align="right">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.itemUnitPrice}"
                        property="document.item[${ctr}].itemUnitPrice"
                        readOnly="${ (itemLine.itemTypeCode eq 'ITEM') or fullDocEntryCompleted}" />
            </div>
        </td>
    </c:if>
    <c:if test="${not KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator}">
        <td class="infoline">
            <div align="right">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.itemUnitPrice}"
                        property="document.item[${ctr}].itemUnitPrice"
                        readOnly="${fullDocEntryCompleted}" /></div>
        </td>
    </c:if>
    <%--<td class="infoline">
        <div name="systemDiv" style="display:block" align="right">
            <kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.itemSurcharge}"
                    property="document.item[${ctr}].itemSurcharge"
                    readOnly="true"/>
        </div>
        <div name="manualDiv" style="display:none" align="right">
            <kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.itemSurcharge}"
                    property="document.item[${ctr}].itemSurcharge"
                    readOnly="false"/>
        </div>
    </td>--%>
    <td class="infoline">
        <div align="right">
            <kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.extendedPrice}"
                    property="document.item[${ctr}].extendedPrice"
                    readOnly="${not fullEntryMode}" styleClass="amount"
                    tabindexOverride="${tabindexOverrideBase + 0}"/>
        </div>
    </td>

    <c:if test="${purapTaxEnabled}">
        <td class="infoline">
            <div align="right">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.itemTaxAmount}"
                        property="document.item[${ctr}].itemTaxAmount"
                        readOnly="${not fullEntryMode or lockTaxAmountEntry}" styleClass="amount"
                        tabindexOverride="${tabindexOverrideBase + 0}"/>
            </div>
        </td>
        <td class="infoline">
            <div align="right">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.totalAmount}"
                        property="document.item[${ctr}].totalAmount"
                        readOnly="true" styleClass="amount"/>
            </div>
        </td>
    </c:if>

    <td class="infoline">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemCatalogNumber}"
                property="document.item[${ctr}].itemCatalogNumber"
                readOnly="true" />
    </td>
    <td class="infoline" colspan="${colSpanDescription}"/>
    <kul:htmlControlAttribute
            attributeEntry="${itemAttributes.itemDescription}"
            property="document.item[${ctr}].itemDescription"
            readOnly="true" />
    </td>
    <td class="infoline" rowspan="6">
        <div align="center">
            <c:choose>
                <c:when test="${not((itemLine.itemTypeCode eq 'ITEM') or fullDocEntryCompleted )}">
                    <html:image property="methodToCall.deleteItem.line${ctr}"
                                src="${ConfigProperties.kr.externalizable.images.url}tinybutton-delete1.gif"
                                alt="Delete Item ${ctr+1}" title="Delete Item ${ctr+1}"
                                styleClass="tinybutton" />
                    <br>
                    <br>
                </c:when>
                <c:otherwise>
                    <div align="center">&nbsp;</div>
                </c:otherwise>
            </c:choose>
        </div>
    </td>
</tr>
<c:set var="accountColumnCount" value="${mainColumnCount - colSpanAction+7}" />
<c:set var="hideFields" value="amount" />
<c:if test="${showAmount}">
    <c:set var="hideFields" value="" />
</c:if>


<c:if
        test="${KualiForm.document.vendorDetail.vendorHeader.vendorForeignIndicator }">
    <tr>
        <td colspan="${colSpanForeignTab}" class="subhead"><span class="subhead-left">Foreign
						Currency Conversion</span></td>
    </tr>
    <tr>

        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemCurrencyType}" colspan="2" />
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignListPrice}"
                forceRequired="true" colspan="1"/>
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignDiscount}"
                colspan="1" />
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignDiscountType}" colspan="2"/>
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignDiscountAmt}"
                colspan="1" />
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignUnitCost}"
                colspan="1" />
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemExchangeRate}" colspan="2" />
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemUnitCostUSD}"
                colspan="2" />

    </tr>

    <tr>
        <td class="infoline" colspan="2">
            <div align="center" />
            <kul:htmlControlAttribute
                    property="document.vendorDetail.currencyType.currencyType"
                    attributeEntry="${itemAttributes.itemCurrencyType}"
                    readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" />
        </td>
        <td class="infoline" colspan="1">
            <div align="center" />
            <kul:htmlControlAttribute
                    property="document.item[${ctr}].itemForeignListPrice"
                    attributeEntry="${itemAttributes.itemForeignListPrice}"
                    readOnly="${not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment))))}"
                    tabindexOverride="${tabindexOverrideBase + 0}" /></td>
        <td class="infoline" colspan="1">
            <div align="center" />
            <kul:htmlControlAttribute
                    property="document.item[${ctr}].itemForeignDiscount"
                    attributeEntry="${itemAttributes.itemForeignDiscount}"
                    readOnly="${not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment))))}"
                    tabindexOverride="${tabindexOverrideBase + 0}" /></td>
        <td class="infoline" colspan="2">
            <div align="center" />
            <kul:htmlControlAttribute
                    property="document.item[${ctr}].itemForeignDiscountType"
                    attributeEntry="${itemAttributes.itemForeignDiscountType}"
                    readOnly="${not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment))))}"
                    tabindexOverride="${tabindexOverrideBase + 0}" /></td>
        <td class="infoline" colspan="1">
            <div align="center" />
            <kul:htmlControlAttribute
                    property="document.item[${ctr}].itemForeignDiscountAmt"
                    attributeEntry="${itemAttributes.itemForeignDiscountAmt}"
                    readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" />
        </td>
        <td class="infoline" colspan="1">
            <div align="center" />
            <kul:htmlControlAttribute
                    property="document.item[${ctr}].itemForeignUnitCost"
                    attributeEntry="${itemAttributes.itemForeignUnitCost}"
                    readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" />
        </td>
        <td class="infoline" colspan="2">
            <div align="center" />
            <kul:htmlControlAttribute
                    property="document.item[${ctr}].itemExchangeRate"
                    attributeEntry="${itemAttributes.itemExchangeRate}"
                    readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" />
        </td>
        <td class="infoline" colspan="2">
            <div align="center" />
            <kul:htmlControlAttribute
                    property="document.item[${ctr}].itemUnitCostUSD"
                    attributeEntry="${itemAttributes.itemUnitCostUSD}"
                    readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" />
        </td>
    </tr>

</c:if>
<c:if test="${usePO}" >
    <tr>
        <c:set var="itemNo" value="${itemNo+1}" />
        <td colspan="1" class="subhead">
            <div>Bib Info:</div>
        </td>
        <div align="right"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemTitleId}"
                property="document.item[${ctr}].itemTitleId" readOnly="${false}"
                tabindexOverride="${tabindexOverrideBase + 0}" /></div>
        <div align="right"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.bibUUID}"
                property="document.item[${ctr}].bibUUID" readOnly="${false}"
                tabindexOverride="${tabindexOverrideBase + 0}" /></div>
        <th colspan="1" rowspan="1">&nbsp; Bib Editor</th>
        <td class="infoline" colspan="1">
            <div align="right">
                <c:set var="docformat" value="${KualiForm.document.items[ctr].docFormat}"/>
                <c:if test="${isSaved}">
                    <!--<input type="button" id="bibEditAddedItemButton_${ctr}" value="edit"
                    onclick="javascript:editXMLContent('${bibeditorEditURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')" />
                    <input type="button" id="bibCreateAddedItemButton_${ctr}" value="create"
                    onclick="javascript:addXMLContent('${bibeditorCreateURL}','','${docNumber}','${marcXMLFileDirLocation}','${itemNo-1}','marc')" /> -->

                    <c:if test="${docformat eq 'marc'}">

                        <input type="image" id="bibEditAddedItemButton_${ctr}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-edit1.gif" onclick="javascript:editXMLContent('${bibeditorEditURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')" />
                    </c:if>
                    <c:if test="${docformat eq 'dublinunq'}">

                        <input type="image" id="bibEditAddedItemButton_${ctr}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-edit1.gif" onclick="javascript:editXMLContent('${dublinEditorEditURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','dublinunq')" />
                    </c:if>


                    <input type="image" id="bibCreateAddedItemButton_${ctr}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-createnew.gif" onclick="javascript:addXMLContent('${bibeditorCreateURL}','','${docNumber}','${marcXMLFileDirLocation}','${itemNo-1}','marc')" />
                </c:if>

                <c:if test="${!isSaved}">
                    <c:if test="${docformat eq 'marc'}">
                        <input type="image" id="bibViewAddedItemButton_${ctr}" src="${ConfigProperties.externalizable.images.url}tinybutton-view.gif" onclick="javascript:viewXMLContent('${bibeditorViewURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')"/>
                    </c:if>
                    <c:if test="${docformat eq 'dublinunq'}">
                        <input type="image" id="bibViewAddedItemButton_${ctr}" src="${ConfigProperties.externalizable.images.url}tinybutton-view.gif" onclick="javascript:viewXMLContent('${dublinEditorViewURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','dublinunq')"/>
                    </c:if>
                </c:if>


            </div>
            <script>
                document.getElementById('document.item['+${ctr}+'].itemTitleId').style.display="none";
                document.getElementById('document.item['+${ctr}+'].bibUUID').style.display="none";
                var itemTitleUUID = document.getElementById('document.item['+${ctr}+'].bibUUID').value;
                itemTitleUUID = itemTitleUUID.toString();
                if(itemTitleUUID != null && itemTitleUUID!="" ){
                    document.getElementById('bibCreateAddedItemButton_'+${ctr}).style.display="none";
                }
                else{
                    document.getElementById('bibEditAddedItemButton_'+${ctr}).style.display="none";
                }
            </script></td>
    </tr>
</c:if>
<c:if test="${not fullDocEntryCompleted }">
    <c:set target="${KualiForm.editingMode}" property="viewOnly" value="true" />

</c:if>
<purap:purapGeneralAccounting itemAttributes="${itemAttributes}"
        accountPrefix="document.item[${ctr}]."
        itemColSpan="${accountColumnCount}" count="${ctr}"/>
<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
    </tbody>
</c:if>
</c:if>
</logic:iterate>

<c:if test="${(fullEntryMode) and (clearAllTaxes) and (purapTaxEnabled)}">
    <tr>
        <th height=30 colspan="${mainColumnCount}">
            <html:image
                    property="methodToCall.clearAllTaxes"
                    src="${ConfigProperties.externalizable.images.url}tinybutton-clearalltax.gif"
                    alt="Clear all tax"
                    title="Clear all tax" styleClass="tinybutton" />
            </div>
        </th>
    </tr>
</c:if>
<tr>
    <th height=30 colspan="${mainColumnCount}">&nbsp;</th>
</tr>

<script>
    function addXMLContent(url,elementId,docNumber,dirPath,itemNumber,docType) {
        var controlId = elementId;
        var tokenId = docNumber;
        if(itemNumber.length ==0){//used to set file name with item no for current item create
            itemNumber = ${itemNo};
        }
        tokenId = docNumber+'_'+itemNumber;
        url = url+"&action=create&tokenId="+tokenId+"&__login_user=admin";
        //url = url + "?fromOLE=true&action=create&fileName="+fileName+"&dirPath="+dirPath;
        window.open(url);
    }

    function editXMLContent(url,elementId,docNumber,dirPath,itemNumber,docType) {
        var controlId = elementId;
        var tokenId = docNumber;
        tokenId = docNumber+'_'+itemNumber;
        uuid = document.getElementById(controlId).value;
        document.getElementById(controlId).style.display="none";
        url = url+"&action=edit&docType="+docType+"&docId="+uuid+"&tokenId="+tokenId+"&__login_user=admin";
        //url=url+"?docType="+docType+"&uuid="+uuid+"&action=edit&fromOLE=true";
        window.open(url);
    }

    function viewXMLContent(url,elementId,docNumber,dirPath,itemNumber,docType) {
        var controlId = elementId;
        uuid = document.getElementById(controlId).value;
        url =url+"?docAction=checkOut&docId="+uuid;
        window.open(url);
    }

</script>