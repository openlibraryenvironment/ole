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


<%@ attribute name="displayRequisitionFields" required="false"
              description="Boolean to indicate if REQ specific fields should be displayed"%>
<%@ attribute name="itemAttributes" required="true" type="java.util.Map"
              description="The DataDictionary entry containing attributes for this row's fields."%>
<%@ attribute name="bibInfoAttributes" required="false"
              type="java.util.Map"
              description="The DataDictionary entry containing attributes for this row's fields."%>
<%@ attribute name="accountingLineAttributes" required="true"
              type="java.util.Map"
              description="The DataDictionary entry containing attributes for this row's accounting line fields."%>
<%@ attribute name="extraHiddenItemFields" required="false"
              description="A comma seperated list of names to be added to the list of normally hidden fields
              for the existing misc items."%>
<%@ attribute name="count" required="false" description="item number"%>
<%@ attribute name="intialOpen" required="false"
              description="Boolean to indicate whether the tag to be opened when initiating the document" %>


<script language="JavaScript" type="text/javascript" src="dwr/interface/PurapCommodityCodeService.js"></script>
<script language="JavaScript" type="text/javascript" src="scripts/vnd/objectInfo.js"></script>
<script language="JavaScript" type="text/javascript" src="dwr/interface/ItemUnitOfMeasureService.js"></script>
<script language="JavaScript" type="text/javascript" src="scripts/module/purap/objectInfo.js"></script>

<c:if test="${empty intialOpen}">
    <c:set var="intialOpen" value="true" />
</c:if>

<c:set var="baseCurrency" value="${ConfigProperties.config.base.currency}" />
<c:set var="fullEntryMode"
       value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT] && (empty KualiForm.editingMode['restrictFiscalEntry'])}" />
<c:set var="amendmentEntry"
       value="${(not empty KualiForm.editingMode['amendmentEntry'])}" />
<c:set var="isReOpenPO" value="${KualiForm.document.isReOpenPO }" />
<c:set var="isSplitPO" value="${KualiForm.document.isSplitPO }" />
<c:set var="lockB2BEntry"
       value="${(not empty KualiForm.editingMode['lockB2BEntry'])}" />
<c:set var="unorderedItemAccountEntry"
       value="${(not empty KualiForm.editingMode['unorderedItemAccountEntry'])}" />
<c:set var="amendmentEntryWithUnpaidPreqOrCM"
       value="${(amendmentEntry && (KualiForm.document.containsUnpaidPaymentRequestsOrCreditMemos))}" />
<c:set var="lockTaxAmountEntry"
       value="${(not empty KualiForm.editingMode['lockTaxAmountEntry']) || !fullEntryMode}" />
<c:set var="purapTaxEnabled"
       value="${(not empty KualiForm.editingMode['purapTaxEnabled'])}" />
<c:set var="displayCommodityCodeFields"
       value="${KualiForm.editingMode['enableCommodityCode']}" />

<c:set var="documentType"
       value="${KualiForm.document.documentHeader.workflowDocument.documentTypeName}" />
<c:set var="isATypeOfPODoc" value="${KualiForm.document.isATypeOfPODoc}" />
<c:set var="isATypeOfRCVGDoc" value="${KualiForm.document.isATypeOfRCVGDoc}" />
<c:set var="isATypeOfCORRDoc" value="${KualiForm.document.isATypeOfCORRDoc}" />
<c:set var="isPurchaseOrder" value="${KualiForm.document.isPODoc}" />
<c:set var="isFinal" value="${KualiForm.document.isFinalReqs}" />

<c:set var="hasItems" value="${fn:length(KualiForm.document.items) > 0}" />
<c:set var="hasLineItems"
       value="${fn:length(KualiForm.document.items) > fn:length(KualiForm.document.belowTheLineTypes)}" />
<c:set var="isRequestSourceCodeWeb" value="${KualiForm.document.requisitionSourceCode == 'WEB'}"/>
<c:set var="isRequestSourceCodeMan" value="${KualiForm.document.requisitionSourceCode == 'MAN'}"/>

<c:set var="tabindexOverrideBase" value="50" />
<c:set var="isSaved" value="${KualiForm.document.isSaved}"/>
<c:set var="mainColumnCount" value="20" />
<c:set var="colSpanItemType" value="6" />
<c:set var="colSpanDescription" value="2" />
<c:set var="colSpanExtendedPrice" value="1" />
<c:set var="bibeditorCreateURL"
       value="${KualiForm.document.bibeditorCreateURL}" />
<c:set var="bibSearchURL"
       value="${KualiForm.document.bibSearchURL}" />
<c:set var="bibeditorEditURL"
       value="${KualiForm.document.bibeditorEditURL}" />
<c:set var="dublinEditorEditURL"
       value="${KualiForm.document.dublinEditorEditURL}" />
<c:set var="dublinEditorViewURL"
       value="${KualiForm.document.dublinEditorViewURL}" />
<c:set var="bibeditorViewURL"
       value="${KualiForm.document.bibeditorViewURL}" />
<c:set var="instanceEditorURL" value="${KualiForm.document.instanceEditorURL}"/>
<c:set var="docNumber" value="${KualiForm.document.documentNumber }" />
<c:set var="marcXMLFileDirLocation"
       value="${KualiForm.document.marcXMLFileDirLocation}" />
<c:set var="itemNo" value="0" />

<c:choose>
    <c:when test="${displayRequisitionFields}">
        <c:set var="colSpanAmountPaid" value="0" />
    </c:when>
    <c:otherwise>
        <c:set var="colSpanAmountPaid" value="1" />
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${displayCommodityCodeFields}">
        <c:set var="colSpanCatlogNumber" value="2" />
    </c:when>
    <c:otherwise>
        <c:set var="colSpanCatlogNumber" value="3" />
    </c:otherwise>
</c:choose>

<kul:tab tabTitle="Titles" defaultOpen="${intialOpen}"
         tabErrorKey="${PurapConstants.ITEM_TAB_ERRORS}">
<div class="tab-container" align=center><c:if
        test="${!KualiForm.document.inquiryRendered}">
    <div align="left">Object Code and Sub-Object Code inquiries and
        descriptions have been removed because this is a prior year document.
    </div>
    <br>
</c:if>

<table cellpadding="0" cellspacing="0" class="datatable"
       summary="Items Section">

<!--  if (fullEntryMode or amendmentEntry) and not lockB2BEntry, then display the addLine -->
<c:if test="${(fullEntryMode or amendmentEntry) and !lockB2BEntry}">
<tr>
    <td colspan="9" class="subhead"><span class="subhead-left">Add
				Item <a href="${KualiForm.lineItemImportInstructionsUrl}"
                        target="helpWindow"><img
                src="${ConfigProperties.kr.externalizable.images.url}my_cp_inf.gif"
                title="Line Item Import Help" src="Line Item Import Help"
                hspace="5" border="0" align="middle" /></a></td>
    <td colspan="8" class="subhead" align="right" nowrap="nowrap"
        style="border-left: none;"><SCRIPT type="text/javascript">
        function hideImport() {
            document.getElementById("showLink").style.display="inline";
            document.getElementById("uploadDiv").style.display="none";
        }
        function showImport() {
            document.getElementById("showLink").style.display="none";
            document.getElementById("uploadDiv").style.display="inline";
        }
        function hideLinkToBibImage() {
            document.getElementById("bibSelectExistingItemButton").hidden=true;
            document.getElementById("bibCreateCurrentItemButton").hidden=false;
        }
        function hideCreateNewImage() {
            document.getElementById("bibCreateCurrentItemButton").hidden=true;
            document.getElementById("bibSelectExistingItemButton").hidden=false;
        }
       /* document.write(
                '<a id="showLink" href="#" onclick="showImport();return false;">' +
                        '<img src="${ConfigProperties.externalizable.images.url}tinybutton-importlines.gif" title="import items from file" alt="import items from file"' +
                        '     width=72 height=15 border=0 align="right" class="det-button">' +
                        '<\/a>' +
                        '<div id="uploadDiv" style="display:none;" >' +
                        '<html:file size="30" property="itemImportFile" />' +
                        '<html:image property="methodToCall.importItems" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-add1.gif"
                                    styleClass="tinybutton" alt="add imported items" title="add imported items" />' +
                        '<html:image property="methodToCall.cancel" src="${ConfigProperties.externalizable.images.url}tinybutton-cancelimport.gif"
                                    styleClass="tinybutton" alt="cancel import" title="cancel import" onclick="hideImport();return false;" />' +
                        '<\/div>');*/
    </SCRIPT>
       <%-- <NOSCRIPT>Import lines <html:file size="30"
                                          property="itemImportFile" style="font:10px;height:16px;" /> <html:image
                property="methodToCall.importItems"
                src="${ConfigProperties.kr.externalizable.images.url}tinybutton-add1.gif"
                alt="add imported items" title="add imported items" /></NOSCRIPT>--%>
    </td>
</tr>

<tr>
    <c:if
            test="${not (KualiForm.document.vendorDetail.currencyType.currencyType eq baseCurrency) or (not KualiForm.document.vendorDetail.currencyType.currencyType eq null)}">
        <c:set var="rowSpanLineNum" value="6" />
    </c:if>
    <c:if
            test="${KualiForm.document.vendorDetail.currencyType.currencyType eq baseCurrency or (KualiForm.document.vendorDetail.currencyType.currencyType eq  null) }">
        <c:set var="rowSpanLineNum" value="5" />
    </c:if>

    <td class="infoline" rowspan="${rowSpanLineNum}"><kul:htmlControlAttribute
            attributeEntry="${itemAttributes.itemLineNumber}"
            property="newPurchasingItemLine.itemLineNumber" readOnly="true" />
    </td>

    <th colspan="1" rowspan="1">&nbsp;Bib Info:</th>
    <td colspan="2" class="infoline">
        <input id="attachNewBibId" type="radio" name="AttachBib" onclick="hideLinkToBibImage();"/>&nbsp;new bib&nbsp;
        <input id="attachExistingBibId" type="radio" name="AttachBib" onclick="hideCreateNewImage();"/>&nbsp;existing bib&nbsp;
    </td>
    <td colspan="2" class="infoline" id="holdingOptions">
        <input type="radio" id="printOption" name="linkToOrderOption" checked="true"/>&nbsp;print holdings&nbsp;
        <input type="radio" name="linkToOrderOption"/>&nbsp;e-holdings&nbsp;
    </td>
    <td class="infoline" colspan="1">
        <div align="center">
            <!--<input type="button" id="bibCreateCurrentItemButton" value="create"
						onclick="javascript:addXMLContent('${bibeditorCreateURL}','','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')" />-->
            <input type="image" hidden="true" id="bibCreateCurrentItemButton" name="Create New Bib" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-createnew.gif" onclick="javascript:addXMLContent('${bibeditorCreateURL}','','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc','NB_'+getHoldingValue())" />
            <input type="image" hidden="true" id="bibSelectExistingItemButton" name="existing bib" src="${ConfigProperties.externalizable.images.url}existing bib.gif" onclick="javascript:addXMLContent('${bibSearchURL}','','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc','EB_'+getHoldingValue())">
        </div>
    </td>
        <%-- <kul:htmlAttributeHeaderCell
        attributeEntry="${itemAttributes.itemLocation}" width="10%"/>
        <td class="infoline" colspan="3">
        <div align="center" />
        <kul:htmlControlAttribute
            attributeEntry="${itemAttributes.itemLocation}"
            property="newPurchasingItemLine.itemLocation"
            extraReadOnlyProperty="newPurchasingItemLine.itemLocation"
            tabindexOverride="${tabindexOverrideBase + 0}" /></td> --%>
    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.itemLocation}" width="10%"/>
    <td class="infoline" colspan="3">
        <div align="center" />
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemLocation}"
                property="newPurchasingItemLine.itemLocation"
                extraReadOnlyProperty="newPurchasingItemLine.itemType.itemLocation"
                tabindexOverride="${tabindexOverrideBase + 0}" /></td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.singleCopyNumber}" width="10%"/>
    <td class="infoline">
        <div align="center"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.singleCopyNumber}"
                property="newPurchasingItemLine.singleCopyNumber"
                tabindexOverride="${tabindexOverrideBase + 0}"/></div>
    </td>

        <%-- <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.itemPublicViewIndicator}" />
        <td class="infoline">
            <div align="center"><kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.itemPublicViewIndicator}"
                    property="newPurchasingItemLine.itemPublicViewIndicator"
                    tabindexOverride="${tabindexOverrideBase + 0}" />
        </td> --%>
</tr>

<tr>
    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.oleItemQuantity}" width="10%" forceRequired="true"/>
    <td class="infoline">
        <div align="center"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.oleItemQuantity}"
                property="newPurchasingItemLine.oleItemQuantity"
                tabindexOverride="${tabindexOverrideBase + 0}" /></div>
    </td>
    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.itemNoOfParts}" forceRequired="true"/>
    <td class="infoline">
        <div align="center"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemNoOfParts}"
                property="newPurchasingItemLine.itemNoOfParts"
                tabindexOverride="${tabindexOverrideBase + 0}" /></div>
    </td>
    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.itemListPrice}"
            forceRequired="true" width="10%"/>
    <c:if
            test="${not (KualiForm.document.vendorDetail.currencyType.currencyType eq baseCurrency) and (not (KualiForm.document.vendorDetail.currencyType.currencyType eq null))}">
        <td class="infoline">
            <div align="center"><kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.itemListPrice}"
                    property="newPurchasingItemLine.itemListPrice"
                    tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true" /></div>
        </td>
    </c:if>
    <c:if
            test="${KualiForm.document.vendorDetail.currencyType.currencyType eq baseCurrency or ( KualiForm.document.vendorDetail.currencyType.currencyType eq  null)}">
        <td class="infoline">
            <div align="center"><kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.itemListPrice}"
                    property="newPurchasingItemLine.itemListPrice"
                    tabindexOverride="${tabindexOverrideBase + 0}" /></div>
        </td>
        <%-- <td class="infoline">
        <div align="center"><kul:htmlControlAttribute
            attributeEntry="${itemAttributes.itemDiscount}"
            property="newPurchasingItemLine.itemDiscount"
            tabindexOverride="${tabindexOverrideBase + 0}" /></div>
        </td>
        <td class="infoline">
        <div align="center"><kul:htmlControlAttribute
            attributeEntry="${itemAttributes.itemDiscountType}"
            property="newPurchasingItemLine.itemDiscountType"
            tabindexOverride="${tabindexOverrideBase + 0}" /></div>
        </td> --%>
    </c:if>
    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.extendedPrice}" width="10%"/>
    <td class="infoline">
        <div align="center"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.extendedPrice}"
                property="newPurchasingItemLine.extendedPrice" readOnly="true" />
        </div>
    </td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.itemPublicViewIndicator}" />
    <td class="infoline">
        <div align="center"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemPublicViewIndicator}"
                property="newPurchasingItemLine.itemPublicViewIndicator"
                tabindexOverride="${tabindexOverrideBase + 0}" />
    </td>


    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.doNotClaim}" />
    <td class="infoline">
        <div align="center"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.doNotClaim}"
                property="newPurchasingItemLine.doNotClaim"
                tabindexOverride="${tabindexOverrideBase + 0}" />
    </td>


        <%-- <th align=right width='75%' colspan="${colSpanTotalLabel}"
                    scope="row">
                <div align="right"><kul:htmlAttributeLabel
                    attributeEntry="${DataDictionary.RequisitionDocument.attributes.totalDollarAmount}" />
                </div>
                </th>
                <td valign=middle class="datacell" colspan="2">
                <div align="right"><b> <kul:htmlControlAttribute
                    attributeEntry="${DataDictionary.RequisitionDocument.attributes.totalDollarAmount}"
                    property="document.totalDollarAmount" readOnly="true" />&nbsp; </b></div>
                </td> --%>
        <%-- <td class="datacell" colspan="${colSpanTotalBlank}">&nbsp;</td> --%>
    <!-- </tr> -->

</tr>

<c:set var="colSpanForeignTab" value="14" />

<c:if
        test="${not (KualiForm.document.vendorDetail.currencyType.currencyType eq baseCurrency) and (not (KualiForm.document.vendorDetail.currencyType.currencyType eq null))}">
    <%-- <tr>
        <td colspan="${colSpanForeignTab}" class="subhead"><span
            class="subhead-left">Foreign Currency Conversion</span></td>
    </tr> --%>
    <tr>

        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemCurrencyType}" colspan="1" />
        <td class="infoline" colspan="3">
            <div align="center"><kul:htmlControlAttribute
                    property="document.vendorDetail.currencyType.currencyType"
                    attributeEntry="${itemAttributes.itemCurrencyType}"
                    readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" /></div>
        </td>
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemExchangeRate}" colspan="1" />
        <td class="infoline" colspan="3">
            <div align="center"><kul:htmlControlAttribute
                    property="newPurchasingItemLine.itemExchangeRate"
                    attributeEntry="${itemAttributes.itemExchangeRate}"
                    readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" /></div>
        </td>
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignListPrice}"
                forceRequired="true" colspan="1" />
        <td class="infoline" colspan="1">
            <div align="center"><kul:htmlControlAttribute
                    property="newPurchasingItemLine.itemForeignListPrice"
                    attributeEntry="${itemAttributes.itemForeignListPrice}"
                    readOnly="false" tabindexOverride="${tabindexOverrideBase + 0}" /></div>
        </td>
            <%-- <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignUnitCost}" colspan="1" />
                <td class="infoline" colspan="1">
            <div align="center"><kul:htmlControlAttribute
                property="newPurchasingItemLine.itemForeignUnitCost"
                attributeEntry="${itemAttributes.itemForeignUnitCost}"
                readOnly="true"/></div>
            </td> --%>
            <%-- <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.itemForiegnExtendedPrice}" width="10%"/>
            <td class="infoline">
            <div align="center"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemForiegnExtendedPrice}"
                property="newPurchasingItemLine.extendedPrice" readOnly="true" />
            </div>
            </td>
            <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.itemForiegnExtendedPrice}" width="10%"/>
            <td class="infoline" colspan="2">
            <div align="center"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemForiegnExtendedPrice}"
                property="newPurchasingItemLine.extendedPrice" readOnly="true" />
            </div>
            </td> --%>
            <%-- <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignListPrice}"
                forceRequired="true" colspan="2" />
            <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignDiscount}" />
            <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignDiscountType}" />
            <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignDiscountAmt}"
                colspan="2" />
            <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignUnitCost}" colspan="2" />
            <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemExchangeRate}" colspan="2" />
            <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemUnitCostUSD}" colspan="2" /> --%>
    </tr>

</c:if>

<tr>
    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.receiptStatusId}" colspan="1" width="10%"/>

    <td class="infoline" colspan="1">
        <div align="center" />
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.receiptStatusId}"
                property="newPurchasingItemLine.receiptStatusId"
                extraReadOnlyProperty="newPurchasingItemLine.oleReceiptStatus.receiptStatusId"
                tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/></td>
    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.noOfCopiesReceived}" width="10%"/>
    <td class="infoline">
        <div align="center"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.noOfCopiesReceived}"
                property="newPurchasingItemLine.noOfCopiesReceived"
                tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/></div>
    </td>
    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.noOfPartsReceived}" />
    <td class="infoline">
        <div align="center"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.noOfPartsReceived}"
                property="newPurchasingItemLine.noOfPartsReceived"
                tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/></div>
    </td>
    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.itemPriceSourceId}" colspan="1" width="15%"/>

    <td class="infoline">
        <div align="center" />
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemPriceSourceId}"
                property="newPurchasingItemLine.itemPriceSourceId"
                extraReadOnlyProperty="newPurchasingItemLine.itemPriceSource.itemPriceSource"
                tabindexOverride="${tabindexOverrideBase + 0}" /></td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.requestSourceTypeId}" colspan="1" width="15%"/>

    <td class="infoline" colspan="1">
        <div align="center" />
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.requestSourceTypeId}"
                property="newPurchasingItemLine.requestSourceTypeId"
                extraReadOnlyProperty="newPurchasingItemLine.oleRequestSourceType.requestSourceType"
                tabindexOverride="${tabindexOverrideBase + 0}" /></td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.claimDate}" rowspan="2"/>
    <td class="infoline" rowspan="2">
        <div align="center" />
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.claimDate}"
                property="newPurchasingItemLine.claimDate"
                datePicker="true"
                extraReadOnlyProperty="newPurchasingItemLine.itemType.claimDate"
                tabindexOverride="${tabindexOverrideBase + 0}" /></td>

</tr>
<tr>
    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.formatTypeId}" />

    <td class="infoline">
        <div align="center" />
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.formatTypeId}"
                property="newPurchasingItemLine.formatTypeId"
                extraReadOnlyProperty="newPurchasingItemLine.formatTypeName.formatTypeName"
                tabindexOverride="${tabindexOverrideBase + 0}" /></td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.categoryId}" width="15%"/>

    <td class="infoline" >
        <div align="center" />
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.categoryId}"
                property="newPurchasingItemLine.categoryId"
                extraReadOnlyProperty="newPurchasingItemLine.category.category"
                tabindexOverride="${tabindexOverrideBase + 0}" /></td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.vendorItemPoNumber}" colspan="1" width="15%"/>

    <td class="infoline" >
        <div align="center" />
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.vendorItemPoNumber}"
                property="newPurchasingItemLine.vendorItemPoNumber"
                tabindexOverride="${tabindexOverrideBase + 0}" /></td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.requestorFirstName}" colspan="1"/>
    <td class="infoline" colspan="1">
        <div align="center"><c:if
                test="${not empty KualiForm.newPurchasingItemLine.requestorId}">
            <kul:inquiry
                    boClassName="org.kuali.ole.deliver.bo.OLEPatronEntityViewBo"
                    keyValues="patronId=${KualiForm.newPurchasingItemLine.requestorId}"
                    render="true">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.requestorFirstName}"
                        property="newPurchasingItemLine.requestorFirstName"
                        readOnly="${true}" tabindexOverride="${tabindexOverrideBase + 0}" />
            </kul:inquiry>
            <select:ptrnlookup
                    boClassName="org.kuali.ole.deliver.bo.OLEPatronEntityViewBo"
                    fieldConversions="patronId:newPurchasingItemLine.requestorId,name:newPurchasingItemLine.requestorFirstName" />
        </c:if> <c:if test="${empty KualiForm.newPurchasingItemLine.requestorId}">
            <c:if test="${not empty KualiForm.newPurchasingItemLine.requestorFirstName}">
                <kul:inquiry boClassName="org.kuali.ole.deliver.bo.OLEPatronEntityViewBo"
                             keyValues="patronId=${KualiForm.newPurchasingItemLine.requestorId}"
                             render="true">
                    <kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.requestorFirstName}"
                            property="newPurchasingItemLine.requestorFirstName"
                            readOnly="${true}" tabindexOverride="${tabindexOverrideBase + 0}" />
                </kul:inquiry>
            </c:if>
            <select:ptrnlookup
                    boClassName="org.kuali.ole.deliver.bo.OLEPatronEntityViewBo"
                    fieldConversions="patronId:newPurchasingItemLine.requestorId,name:newPurchasingItemLine.requestorFirstName" />
        </c:if></div>


    </td>
    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.itemRouteToRequestorIndicator}" />
    <td class="infoline">
        <div align="center"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemRouteToRequestorIndicator}"
                property="newPurchasingItemLine.itemRouteToRequestorIndicator"
                tabindexOverride="${tabindexOverrideBase + 0}" />
    </td>
</tr>

<tr>

    <c:if
            test="${not (KualiForm.document.vendorDetail.currencyType.currencyType eq baseCurrency) and (not (KualiForm.document.vendorDetail.currencyType.currencyType eq null))}">
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignUnitCost}" colspan="1" />
        <td class="infoline" colspan="1">
            <div align="center"><kul:htmlControlAttribute
                    property="newPurchasingItemLine.itemForeignUnitCost"
                    attributeEntry="${itemAttributes.itemForeignUnitCost}"
                    readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}" /></div>
        </td>
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignDiscount}" />
        <td class="infoline">
            <div align="center"><kul:htmlControlAttribute
                    property="newPurchasingItemLine.itemForeignDiscount"
                    attributeEntry="${itemAttributes.itemForeignDiscount}"
                    readOnly="false" tabindexOverride="${tabindexOverrideBase + 0}" /></div>
        </td>
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignDiscountType}" />
        <td class="infoline">
            <div align="center"><kul:htmlControlAttribute
                    property="newPurchasingItemLine.itemForeignDiscountType"
                    attributeEntry="${itemAttributes.itemForeignDiscountType}"
                    readOnly="false" tabindexOverride="${tabindexOverrideBase + 0}" /></div>
        </td>

    </c:if>

    <c:if
            test="${KualiForm.document.vendorDetail.currencyType.currencyType eq baseCurrency or ( KualiForm.document.vendorDetail.currencyType.currencyType eq  null)}">
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemUnitPrice}"
                forceRequired="true" width="10%"/>
        <td class="infoline">
            <div align="center"><kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.itemUnitPrice}"
                    property="newPurchasingItemLine.itemUnitPrice"
                    tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true" /></div>
        </td>
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemDiscount}" width="10%"/>
        <td class="infoline">
            <div align="center"><kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.itemDiscount}"
                    property="newPurchasingItemLine.itemDiscount"
                    tabindexOverride="${tabindexOverrideBase + 0}" /></div>
        </td>
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemDiscountType}" />
        <td class="infoline">
            <div align="center"><kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.itemDiscountType}"
                    property="newPurchasingItemLine.itemDiscountType"
                    tabindexOverride="${tabindexOverrideBase + 0}" /></div>
        </td>
    </c:if>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.itemStatus}" />
    <td class="infoline" rowspan="1" colspan="1">
        <div align="center"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemStatus}"
                property="newPurchasingItemLine.itemStatus"
                tabindexOverride="${tabindexOverrideBase + 0}" />
    </td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.purposeId}" />
    <td class="infoline" rowspan="1" colspan="1">
        <div align="center"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.purposeId}"
                property="newPurchasingItemLine.purposeId"
                tabindexOverride="${tabindexOverrideBase + 0}" />
    </td>

    <c:if
            test="${not (KualiForm.document.vendorDetail.currencyType.currencyType  eq baseCurrency) }">
        <td class="infoline" rowspan="1" colspan="2">
            <div align="center"><html:image
                    property="methodToCall.addItem"
                    src="${ConfigProperties.kr.externalizable.images.url}tinybutton-add1.gif"
                    alt="Insert an Item" title="Add an Item" styleClass="tinybutton"
                    tabindex="${tabindexOverrideBase + 0}" /></div>
        </td>
    </c:if>
    <c:if
            test="${KualiForm.document.vendorDetail.currencyType.currencyType  eq baseCurrency}">
        <td class="infoline" rowspan="1" colspan="2">
            <div align="center"><html:image
                    property="methodToCall.addItem"
                    src="${ConfigProperties.kr.externalizable.images.url}tinybutton-add1.gif"
                    alt="Insert an Item" title="Add an Item" styleClass="tinybutton"
                    tabindex="${tabindexOverrideBase + 0}" /></div>
        </td>
    </c:if>

        <%--<select:oleUnitCost itemAttributes="${itemAttributes}"
                    accountPrefix="document.item[${ctr}]."
                    itemColSpan="${accountColumnCount}" count="${ctr}" /> --%>

</tr>


</c:if>

<!-- End of if (fullEntryMode or amendmentEntry), then display the addLine -->

<tr>
    <th height=30 colspan="${mainColumnCount+1}"><purap:oleAccountdistribution
            accountingLineAttributes="${accountingLineAttributes}"
            itemAttributes="${itemAttributes}"
            displayCommodityCodeFields="${displayCommodityCodeFields}" /></th>
</tr>

<tr>
    <td colspan="${mainColumnCount}" class="subhead"><span
            class="subhead-left">Current Items</span></td>
</tr>

<c:if
        test="${not (KualiForm.document.vendorDetail.currencyType.currencyType eq baseCurrency) or (not KualiForm.document.vendorDetail.currencyType.currencyType eq null)}">
    <c:set var="currentItemRowspan" value="9" />
</c:if>
<c:if
        test="${KualiForm.document.vendorDetail.currencyType.currencyType eq baseCurrency or ( KualiForm.document.vendorDetail.currencyType.currencyType eq  null)}">
    <c:set var="currentItemRowspan" value="6" />
</c:if>

    <c:choose>
        <c:when test="${KualiForm.document.orderType.purchaseOrderType eq PurapConstants.ORDER_TYPE_FIRM}">
            <c:set var="readOnlyFlag" value="true" />
        </c:when>
        <c:otherwise>
            <c:set var="readOnlyFlag" value="false" />
        </c:otherwise>
    </c:choose>

<c:choose>
    <c:when test="${itemLine.itemInvoicedTotalAmount != null}">
        <c:set var="invoiceFlag" value="false" />
    </c:when>
    <c:otherwise>
        <c:set var="invoiceFlag" value="true" />
    </c:otherwise>
</c:choose>

<c:if
        test="${(!lockB2BEntry and !hasLineItems and isRequestSourceCode and !isRequestSourceCodeMan) or (lockB2BEntry and !hasItems and isRequestSourceCode and isRequestSourceCodeMan)}">
    <tr>
        <th height=30 colspan="${mainColumnCount+5}">No items added to
            document</th>
    </tr>
</c:if>

<logic:iterate indexId="ctr" name="KualiForm"
               property="document.items" id="itemLine">
<c:if test="${itemLine.itemType.lineItemIndicator == true}">
<c:set var="currentTabIndex" value="${KualiForm.currentTabIndex}"
       scope="request" />
<c:set var="topLevelTabIndex" value="${KualiForm.currentTabIndex}"
       scope="request" />

<c:choose>
    <c:when test="${itemLine.objectId == null}">
        <c:set var="newObjectId"
               value="<%= (new org.kuali.rice.kns.util.Guid()).toString()%>" />
        <c:set var="tabKey" value="Item-${newObjectId}" />
    </c:when>
    <c:when test="${itemLine.objectId != null}">
        <c:set var="tabKey" value="Item-${itemLine.objectId}" />
    </c:when>
</c:choose>

<!--  hit form method to increment tab index -->
<c:set var="dummyIncrementer"
       value="${kfunc:incrementTabIndex(KualiForm, tabKey)}" />
<c:set var="currentTab"
       value="${kfunc:getTabState(KualiForm, tabKey)}" />

<%-- default to closed --%>
<c:choose>
    <c:when test="${empty currentTab}">
        <c:set var="isOpen" value="true" />
    </c:when>
    <c:when test="${!empty currentTab}">
        <c:set var="isOpen" value="${currentTab == 'OPEN'}" />
    </c:when>
</c:choose>

<tr>
    <td colspan="${mainColumnCount+5}" class="tab-subhead"
        style="border-right: none;">Item ${ctr+1}</td>
</tr>

<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
    <tbody style="display: none;" id="tab-${tabKey}-div">
</c:if>
<!-- table class="datatable" style="width: 100%;" -->

<tr>
    <td class="infoline" nowrap="nowrap"
        rowspan="${currentItemRowspan}">&nbsp;<b><bean:write
            name="KualiForm" property="document.item[${ctr}].itemLineNumber" /></b>&nbsp;
        <c:if
                test="${(fullEntryMode and !amendmentEntry) and !lockB2BEntry}">
            <html:image property="methodToCall.upItem.line${ctr}"
                        src="${ConfigProperties.externalizable.images.url}purap-up.gif"
                        alt="Move Item Up" title="Move Item Up" styleClass="tinybutton" />
            <html:image property="methodToCall.downItem.line${ctr}"
                        src="${ConfigProperties.externalizable.images.url}purap-down.gif"
                        alt="Move Item Down" title="Move Item Down"
                        styleClass="tinybutton" />
        </c:if></td>
        <%-- <td class="infoline">
        <div align="center" />
        <kul:htmlControlAttribute
            attributeEntry="${itemAttributes.itemTypeCode}"
            property="document.item[${ctr}].itemTypeCode"
            extraReadOnlyProperty="document.item[${ctr}].itemType.itemTypeDescription"
            readOnly="${not ( (fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.versionNumber == null)) or itemLine.itemTypeCode == 'UNOR' or lockB2BEntry}"
            tabindexOverride="${tabindexOverrideBase + 0}" /></td> --%>
    <th colspan="1" rowspan="1">&nbsp;Bib Info:</th>

    <c:if test="${not isPurchaseOrder }">
        <c:if test="${KualiForm.document.items[ctr].bibUUID ne null}">
            <td class="infoline" colspan="5">
                <div align="left">
                        <%-- <c:out value="${bibeditorEditURL }"/>
                        <c:set var="uuid" value="${document.item['${ctr}'].bibUUID}"/>
                        <c:out value="uuid --> '${uuid }'"/> --%>
                    <c:set var="docformat" value="${KualiForm.document.items[ctr].docFormat}"/>
                    <c:if test="${isSaved and docformat eq 'dublinunq'}">
                        <a href="#" onclick="javascript:editXMLContent('${dublinEditorEditURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','dublinunq')">
                            <c:out value="${KualiForm.document.items[ctr].itemDescription}"/>
                        </a>
                    </c:if>
                    <c:if test="${isSaved and docformat eq 'marc'}">
                        <a href="#" onclick="javascript:editXMLContent('${bibeditorEditURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')">
                            <c:out value="${KualiForm.document.items[ctr].itemDescription}"/>
                        </a>
                    </c:if>
                    <c:if test="${! isSaved and docformat eq 'dublinunq'}">
                        <a href="#" onclick="javascript:viewXMLContent('${dublinEditorViewURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','dublinunq')">
                            <c:out value="${KualiForm.document.items[ctr].itemDescription}"/>
                        </a>
                    </c:if>
                    <c:if test="${!isSaved and docformat eq 'marc'}">
                        <a href="#" onclick="javascript:viewXMLContent('${bibeditorViewURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')" >
                            <c:out value="${KualiForm.document.items[ctr].itemDescription}"/>
                        </a>
                    </c:if>
                </div>
            </td>
        </c:if>
        <c:if test="${KualiForm.document.items[ctr].bibUUID eq null and KualiForm.document.items[ctr].oleERSIdentifier ne null}">
            <td class="infoline" colspan="5">
                <div align="left">
                    <a href="${KualiForm.document.items[ctr].eResLink}" target="_blank">
                        <c:out value="${KualiForm.document.items[ctr].itemDescription}"/>
                    </a>
                </div>
            </td>
        </c:if>
        <%-- <kul:htmlAttributeHeaderCell
attributeEntry="${itemAttributes.itemLocation}" width="10%"/>
<td class="infoline" colspan="1">
    <div align="right">
        <a href="${bibeditorEditURL}" >
            <c:out value="${KualiForm.document.items[ctr].itemLocation}"/>
        </a>
    </div>
</td>

<td class="infoline" colspan="1">
<div align="center" />
<kul:htmlControlAttribute
    attributeEntry="${itemAttributes.itemLocation}"
    property="document.item[${ctr}].itemLocation"
    readOnly="${not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment))))}"
    tabindexOverride="${tabindexOverrideBase + 0}" />

</td> --%>

        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemLocation}" width="10%"/>
        <td class="infoline" colspan="3">
            <div align="center" />
            <kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.itemLocation}"
                    property="document.item[${ctr}].itemLocation"
                    readOnly="${not (amendmentEntry or isReOpenPO)}"
                    tabindexOverride="${tabindexOverrideBase + 0}" />

        </td>
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.singleCopyNumber}" width="10%"/>
        <td class="infoline">
            <div align="center"/>
            <kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.singleCopyNumber}"
                    property="document.item[${ctr}].singleCopyNumber"
                    readOnly="${(readOnlyFlag and invoiceFlag) and (not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment)))))}"
                    tabindexOverride="${tabindexOverrideBase + 0}"/>

        </td>

        <%-- <kul:htmlAttributeHeaderCell
             attributeEntry="${itemAttributes.itemPublicViewIndicator}" />
        <td class="infoline">
                     <div align="center"><kul:htmlControlAttribute
                             attributeEntry="${itemAttributes.itemPublicViewIndicator}"
                             property="newPurchasingItemLine.itemPublicViewIndicator"
                             tabindexOverride="${tabindexOverrideBase + 0}" />
        </td> --%>
    </c:if>
        <%-- <c:out value="-------------->${isPurchaseOrder }"/> --%>

    <c:if test="${isPurchaseOrder }">
        <c:if test="${KualiForm.document.items[ctr].bibUUID ne null}">
            <c:set var="docformat" value="${KualiForm.document.items[ctr].docFormat}"/>
            <c:if test="${! isSaved and docformat eq 'dublinunq'}">
                <%-- <c:out value=" @@@@@@@@@@@@@@@@@@ inside ispurchaseorder of ! isSaved"/>  dublin view --%>
                <td class="infoline" colspan="5">
                    <div align="left">
                        <a href="#"
                           onclick="javascript:viewXMLContent('${dublinEditorViewURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','dublinunq')">
                            <c:out value="${KualiForm.document.items[ctr].itemDescription}"/>
                        </a>
                    </div>
                </td>
            </c:if>

            <c:if test="${! isSaved and docformat eq 'marc'}">

                <%-- <c:out value=" @@@@@@@@@@@@@@@@@@ inside ispurchaseorder of ! isSaved"/> marc view --%>
                <td class="infoline" colspan="5">
                    <div align="left">
                        <a href="#"
                           onclick="javascript:viewXMLContent('${bibeditorViewURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')">
                            <c:out value="${KualiForm.document.items[ctr].itemDescription}"/>
                        </a>
                    </div>
                </td>
            </c:if>
            <c:if test="${isSaved and docformat eq 'marc'}">
                <%-- <c:out value=" ############## inside ispurchaseorder of  isSaved"/> marc edit --%>
                <td class="infoline" colspan="5">
                    <div align="left">
                        <a href="#"
                           onclick="javascript:editXMLContent('${bibeditorEditURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')">
                            <c:out value="${KualiForm.document.items[ctr].itemDescription}"/>
                        </a>
                    </div>
                </td>
            </c:if>

            <c:if test="${isSaved and docformat eq 'dublinunq'}">
                <%-- <c:out value=" ############## inside ispurchaseorder of  isSaved"/> dublin edit--%>
                <td class="infoline" colspan="5">
                    <div align="left">
                        <a href="#"
                           onclick="javascript:editXMLContent('${dublinEditorEditURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','dublinunq')">
                            <c:out value="${KualiForm.document.items[ctr].itemDescription}"/>
                        </a>
                    </div>
                </td>
            </c:if>
        </c:if>
        <c:if test="${KualiForm.document.items[ctr].bibUUID eq null and KualiForm.document.items[ctr].oleERSIdentifier ne null}">
            <td class="infoline" colspan="5">
                <div align="left">
                    <a href="${KualiForm.document.items[ctr].eResLink}" target="_blank">
                        <c:out value="${KualiForm.document.items[ctr].itemDescription}"/>
                    </a>
                </div>
            </td>
        </c:if>

        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemLocation}" width="10%"/>
        <td class="infoline" colspan="3">
            <div align="center"/>
            <kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.itemLocation}"
                    property="document.item[${ctr}].itemLocation"
                    readOnly="${not amendmentEntry}"
                    tabindexOverride="${tabindexOverrideBase + 0}"/></td>

        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.singleCopyNumber}" width="10%"/>
        <td class="infoline">
            <div align="center"/>
            <kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.singleCopyNumber}"
                    property="document.item[${ctr}].singleCopyNumber"
                    readOnly="${not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment))))}"
                    tabindexOverride="${tabindexOverrideBase + 0}"/>
        </td>

    </c:if>
        <%-- <c:if test="${not isPurchaseOrder }">

        </c:if> --%>

</tr>

<tr>
    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.oleItemQuantity}" width="10%" forceRequired="true"/>

<td class="infoline">
    <div align="center" />
    <c:if test="${!amendmentEntry}">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.oleItemQuantity}"
                property="document.item[${ctr}].oleItemQuantity"
                readOnly="${not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment))))}"
                tabindexOverride="${tabindexOverrideBase + 0}" />
    </c:if>
    <c:if test="${amendmentEntry}">
        <c:choose>
            <c:when test="${itemLine.oleItemQuantity == 1 && itemLine.itemNoOfParts == 1}">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.oleItemQuantity}"
                        property="document.item[${ctr}].oleItemQuantity"
                        readOnly="${not amendmentEntry}"
                        tabindexOverride="${tabindexOverrideBase + 0}" />
            </c:when>
            <c:when test="${itemLine.oleItemQuantity > 1 && (itemLine.previousItemQuantity == itemLine.itemQuantity)}">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.oleItemQuantity}"
                        property="document.item[${ctr}].oleItemQuantity"
                        readOnly="${amendmentEntry}"
                        tabindexOverride="${tabindexOverrideBase + 0}" />
            </c:when>
            <c:when test="${itemLine.oleItemQuantity > 1 && !(itemLine.previousItemQuantity == itemLine.itemQuantity)}">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.oleItemQuantity}"
                        property="document.item[${ctr}].oleItemQuantity"
                        readOnly="${amendmentEntry}"
                        tabindexOverride="${tabindexOverrideBase + 0}" />
            </c:when>
            <c:when test="${itemLine.oleItemQuantity == 1 && !(itemLine.previousItemNoOfParts == itemLine.itemNoOfParts)}">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.oleItemQuantity}"
                        property="document.item[${ctr}].oleItemQuantity"
                        readOnly="${not amendmentEntry}"
                        tabindexOverride="${tabindexOverrideBase + 0}" />
            </c:when>
            <c:when test="${itemLine.oleItemQuantity > 1}">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.oleItemQuantity}"
                        property="document.item[${ctr}].oleItemQuantity"
                        readOnly="${amendmentEntry}"
                        tabindexOverride="${tabindexOverrideBase + 0}" />
            </c:when>
            <c:otherwise>
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.oleItemQuantity}"
                        property="document.item[${ctr}].oleItemQuantity"
                        readOnly="${not amendmentEntry}"
                        tabindexOverride="${tabindexOverrideBase + 0}" />
            </c:otherwise>
        </c:choose>
    </c:if>
</td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.itemNoOfParts}" forceRequired="true"/>

<td class="infoline">
    <div align="center" />
    <c:if test="${!amendmentEntry}">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemNoOfParts}"
                property="document.item[${ctr}].itemNoOfParts"
                readOnly="${not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment))))}"
                tabindexOverride="${tabindexOverrideBase + 0}" />
    </c:if>
    <c:if test="${amendmentEntry}">
        <c:choose>
            <c:when test="${itemLine.oleItemQuantity == 1 && itemLine.itemNoOfParts == 1}">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.itemNoOfParts}"
                        property="document.item[${ctr}].itemNoOfParts"
                        readOnly="${not amendmentEntry}"
                        tabindexOverride="${tabindexOverrideBase + 0}" />
            </c:when>
            <c:when test="${itemLine.itemNoOfParts > 1 && (itemLine.previousItemNoOfParts == itemLine.itemNoOfParts)}">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.itemNoOfParts}"
                        property="document.item[${ctr}].itemNoOfParts"
                        readOnly="${amendmentEntry}"
                        tabindexOverride="${tabindexOverrideBase + 0}" />
            </c:when>
            <c:when test="${itemLine.itemNoOfParts > 1 && !(itemLine.previousItemNoOfParts == itemLine.itemNoOfParts)}">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.itemNoOfParts}"
                        property="document.item[${ctr}].itemNoOfParts"
                        readOnly="${amendmentEntry}"
                        tabindexOverride="${tabindexOverrideBase + 0}" />
            </c:when>
            <c:when test="${itemLine.itemNoOfParts == 1 && !(itemLine.previousItemQuantity == itemLine.itemQuantity)}">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.itemNoOfParts}"
                        property="document.item[${ctr}].itemNoOfParts"
                        readOnly="${not amendmentEntry}"
                        tabindexOverride="${tabindexOverrideBase + 0}" />
            </c:when>
            <c:when test="${itemLine.itemNoOfParts > 1}">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.itemNoOfParts}"
                        property="document.item[${ctr}].itemNoOfParts"
                        readOnly="${amendmentEntry}"
                        tabindexOverride="${tabindexOverrideBase + 0}" />
            </c:when>
            <c:otherwise>
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.itemNoOfParts}"
                        property="document.item[${ctr}].itemNoOfParts"
                        readOnly="${not amendmentEntry}"
                        tabindexOverride="${tabindexOverrideBase + 0}" />
            </c:otherwise>
        </c:choose>
    </c:if>
</td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.itemListPrice}"
            forceRequired="true" width="10%"/>
    <c:if
            test="${not (KualiForm.document.vendorDetail.currencyType.currencyType eq baseCurrency) and (not (KualiForm.document.vendorDetail.currencyType.currencyType eq null))}">
        <td class="infoline" colspan="1">
            <div align="center" />
            <kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.itemListPrice}"
                    property="document.item[${ctr}].itemListPrice" readOnly="true"
                    tabindexOverride="${tabindexOverrideBase + 0}" /></td>
        <!-- <td class="infoline">&nbsp;</td>
        <td class="infoline">&nbsp;</td> -->
    </c:if>
    <c:if
            test="${KualiForm.document.vendorDetail.currencyType.currencyType eq baseCurrency or (KualiForm.document.vendorDetail.currencyType.currencyType eq  null)}">

    <td class="infoline" colspan="1">
        <div align="center" />
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemListPrice}"
                property="document.item[${ctr}].itemListPrice"
                readOnly="${((not amendmentEntry or (readOnlyFlag and invoiceFlag)) and (not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment))))))}"
                tabindexOverride="${tabindexOverrideBase + 0}" /></td>
    <%-- <td class="infoline">
    <div align="center" />
    <kul:htmlControlAttribute
            attributeEntry="${itemAttributes.itemDiscount}"
            property="document.item[${ctr}].itemDiscount"
            readOnly="${not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment))))}"
            tabindexOverride="${tabindexOverrideBase + 0}" /></td>
    <td class="infoline">
    <div align="center" />
    <kul:htmlControlAttribute
            attributeEntry="${itemAttributes.itemDiscountType}"
            property="document.item[${ctr}].itemDiscountType"
            extraReadOnlyProperty="document.item[${ctr}].itemDiscountType"
            readOnly="${not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment))))}" /></td>
--%>
    </c:if>

        <%-- <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.itemUnitPrice}" width="10%"/>

<td class="infoline">
<div align="right"><kul:htmlControlAttribute
    attributeEntry="${itemAttributes.itemUnitPrice}"
    property="document.item[${ctr}].itemUnitPrice" readOnly="${true}" />
</div>
</td> --%>
<kul:htmlAttributeHeaderCell
        attributeEntry="${itemAttributes.extendedPrice}" width="10%"/>
<td class="infoline">
    <div align="center"><kul:htmlControlAttribute
            attributeEntry="${itemAttributes.extendedPrice}"
            property="document.item[${ctr}].extendedPrice" readOnly="${(not amendmentEntry or (readOnlyFlag and invoiceFlag))}" />
    </div>
</td>
<kul:htmlAttributeHeaderCell
        attributeEntry="${itemAttributes.itemPublicViewIndicator}" />
<td class="infoline">
    <div align="center"><kul:htmlControlAttribute
            attributeEntry="${itemAttributes.itemPublicViewIndicator}"
            property="document.item[${ctr}].itemPublicViewIndicator"
            readOnly="${(not amendmentEntry)}"
            tabindexOverride="${tabindexOverrideBase + 0}" />
</td>

<kul:htmlAttributeHeaderCell
        attributeEntry="${itemAttributes.doNotClaim}" />
<td class="infoline">
    <div align="center"><kul:htmlControlAttribute
            attributeEntry="${itemAttributes.doNotClaim}"
            property="document.item[${ctr}].doNotClaim"
            readOnly="${(not amendmentEntry)}"
            tabindexOverride="${tabindexOverrideBase + 0}" />
</td>

        <%-- <th align=right width='75%' colspan="${colSpanTotalLabel}"
    scope="row">
<div align="right"><kul:htmlAttributeLabel
    attributeEntry="${DataDictionary.RequisitionDocument.attributes.totalDollarAmount}" />
</div>
</th>
<td valign=middle class="datacell" colspan="2">
<div align="right"><b> <kul:htmlControlAttribute
    attributeEntry="${DataDictionary.RequisitionDocument.attributes.totalDollarAmount}"
    property="document.totalDollarAmount" readOnly="true" />&nbsp; </b></div>
</td> --%>
</tr>

<c:if
        test="${not (KualiForm.document.vendorDetail.currencyType.currencyType eq baseCurrency) and (not (KualiForm.document.vendorDetail.currencyType.currencyType eq null))}">
    <!-- <tr>
    <td colspan="14" class="subhead"><span class="subhead-left">Foreign
    Currency Conversion</span></td>
    </tr> -->
    <tr>

        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemCurrencyType}" colspan="1" />
        <td class="infoline" colspan="3">
            <div align="center"><kul:htmlControlAttribute
                    property="document.vendorDetail.currencyType.currencyType"
                    attributeEntry="${itemAttributes.itemCurrencyType}"
                    readOnly="${(not amendmentEntry or (readOnlyFlag and invoiceFlag))}" tabindexOverride="${tabindexOverrideBase + 0}" /></div>
        </td>
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemExchangeRate}" colspan="1" />
        <td class="infoline" colspan="3">
            <div align="center"><kul:htmlControlAttribute
                    property="document.item[${ctr}].itemExchangeRate"
                    attributeEntry="${itemAttributes.itemExchangeRate}"
                    readOnly="${(not amendmentEntry or (readOnlyFlag and invoiceFlag))}" tabindexOverride="${tabindexOverrideBase + 0}" /></div>
        </td>
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignListPrice}"
                forceRequired="true" />
        <td class="infoline" colspan="1">
            <div align="center" />
            <kul:htmlControlAttribute
                    property="document.item[${ctr}].itemForeignListPrice"
                    attributeEntry="${itemAttributes.itemForeignListPrice}"
                    readOnly="${(not amendmentEntry or (readOnlyFlag and invoiceFlag)) and (not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment)))))}"
                    tabindexOverride="${tabindexOverrideBase + 0}" /></td>
            <%-- <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignUnitCost}" colspan="1" />
                <td class="infoline" colspan="1">
            <div align="center"><kul:htmlControlAttribute
                property="document.item[${ctr}].itemForeignUnitCost"
                attributeEntry="${itemAttributes.itemForeignUnitCost}"
                readOnly="true"/></div>
            </td> --%>
            <%-- <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.extendedPrice}" width="10%"/>
            <td class="infoline">
            <div align="right"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.extendedPrice}"
                property="newPurchasingItemLine.extendedPrice" readOnly="true" />
            </div>
            </td>
            <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.extendedPrice}" width="10%"/>
            <td class="infoline" colspan="2">
            <div align="right"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.extendedPrice}"
                property="newPurchasingItemLine.extendedPrice" readOnly="true" />
            </div>
            </td> --%>
    </tr>


</c:if>

<tr>
    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.receiptStatusId}" colspan="1" width="10%"/>

    <td class="infoline" colspan="1">
        <div align="center" />
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.receiptStatusId}"
                property="document.item[${ctr}].receiptStatusId"
                tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/>

    </td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.noOfCopiesReceived}" width="10%"/>

    <td class="infoline">
        <div align="center" />
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.noOfCopiesReceived}"
                property="document.item[${ctr}].noOfCopiesReceived"
                readOnly="true"
                tabindexOverride="${tabindexOverrideBase + 0}" /></td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.noOfPartsReceived}" />

    <td class="infoline">
        <div align="center" />
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.noOfPartsReceived}"
                property="document.item[${ctr}].noOfPartsReceived"
                readOnly="true"
                tabindexOverride="${tabindexOverrideBase + 0}" /></td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.itemPriceSourceId}" colspan="1" width="15%"/>

    <td class="infoline">
        <div align="center" /><kul:htmlControlAttribute
            attributeEntry="${itemAttributes.itemPriceSourceId}"
            property="document.item[${ctr}].itemPriceSourceId"
            readOnly="${not (amendmentEntry or isReOpenPO)}"
            tabindexOverride="${tabindexOverrideBase + 0}" /></td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.requestSourceTypeId}" colspan="1" width="15%"/>

    <td class="infoline" colspan="1">
        <div align="center" /><kul:htmlControlAttribute
            attributeEntry="${itemAttributes.requestSourceTypeId}"
            property="document.item[${ctr}].requestSourceTypeId"
            readOnly="${not (amendmentEntry or isReOpenPO)}"
            tabindexOverride="${tabindexOverrideBase + 0}" /></td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.claimDate}" rowspan="2" />
    <td class="infoline" rowspan="2">
        <div align="center" />
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.claimDate}"
                property="document.item[${ctr}].claimDate"
                datePicker="true"
                readOnly="${not (amendmentEntry or isReOpenPO)}"
                tabindexOverride="${tabindexOverrideBase + 0}" />

    </td>
</tr>

<tr>
    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.formatTypeId}" />

    <td class="infoline">
        <div align="center" /><kul:htmlControlAttribute
            attributeEntry="${itemAttributes.formatTypeId}"
            property="document.item[${ctr}].formatTypeId"
            readOnly="${not (amendmentEntry or isReOpenPO)}"
            tabindexOverride="${tabindexOverrideBase + 0}" /></td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.categoryId}" width="15%"/>

    <td class="infoline" >
        <div align="center" /><kul:htmlControlAttribute
            attributeEntry="${itemAttributes.categoryId}"
            property="document.item[${ctr}].categoryId"
            readOnly="${not (amendmentEntry or isReOpenPO)}"
            tabindexOverride="${tabindexOverrideBase + 0}" /></td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.vendorItemPoNumber}" colspan="1" width="15%"/>
    <td class="infoline" >
        <div align="center" /><kul:htmlControlAttribute
            attributeEntry="${itemAttributes.vendorItemPoNumber}"
            property="document.item[${ctr}].vendorItemPoNumber"
            readOnly="${not (amendmentEntry or isReOpenPO)}"
            tabindexOverride="${tabindexOverrideBase + 0}" /></td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.requestorFirstName}"  colspan="1"/>
    <td class="infoline" colspan="1">
        <div align="center"><c:if
                test="${not empty KualiForm.document.items[ctr].requestorId}">

            <kul:inquiry
                    boClassName="org.kuali.ole.deliver.bo.OLEPatronEntityViewBo"
                    keyValues="patronId=${KualiForm.document.items[ctr].requestorId}"
                    render="true">
                <kul:htmlControlAttribute
                        attributeEntry="${itemAttributes.requestorFirstName}"
                        property="document.item[${ctr}].requestorFirstName"
                        readOnly="${(not amendmentEntry or (readOnlyFlag and invoiceFlag))}"
                        tabindexOverride="${tabindexOverrideBase + 0}" />
            </kul:inquiry>
            <c:if test="${fullEntryMode}">
                <select:ptrnlookup
                        boClassName="org.kuali.ole.deliver.bo.OLEPatronEntityViewBo"
                        fieldConversions="patronId:document.item[${ctr}].requestorId,name:document.item[${ctr}].requestorFirstName" />

            </c:if>
        </c:if> <c:if test="${empty KualiForm.document.items[ctr].requestorId}">
            <c:if test="${not empty KualiForm.document.items[ctr].requestorFirstName}">
                <kul:inquiry boClassName="org.kuali.ole.deliver.bo.OLEPatronEntityViewBo"
                             keyValues="patronId=${KualiForm.document.items[ctr].requestorId}"
                             render="true">
                    <kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.requestorFirstName}"
                            property="document.item[${ctr}].requestorFirstName"
                            readOnly="${(not amendmentEntry or (readOnlyFlag and invoiceFlag))}"
                            tabindexOverride="${tabindexOverrideBase + 0}" />
                </kul:inquiry>
            </c:if>
            <c:if test="${fullEntryMode}">
                <select:ptrnlookup
                        boClassName="org.kuali.ole.deliver.bo.OLEPatronEntityViewBo"
                        fieldConversions="patronId:document.item[${ctr}].requestorId,name:document.item[${ctr}].requestorFirstName" />
            </c:if>
        </c:if></div>
    </td>
    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.itemRouteToRequestorIndicator}" />

    <td class="infoline">
        <div align="center"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemRouteToRequestorIndicator}"
                property="document.item[${ctr}].itemRouteToRequestorIndicator"
                readOnly="${(not amendmentEntry or (readOnlyFlag and invoiceFlag)) and (not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment)))))}"
                tabindexOverride="${tabindexOverrideBase + 0}" /></div>
    </td>

</tr>


<tr>
    <c:if
            test="${not (KualiForm.document.vendorDetail.currencyType.currencyType eq baseCurrency) and (not (KualiForm.document.vendorDetail.currencyType.currencyType eq null))}">
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignUnitCost}"
                colspan="1" />
        <td class="infoline" colspan="1">
            <div align="center" />
            <kul:htmlControlAttribute
                    property="document.item[${ctr}].itemForeignUnitCost"
                    attributeEntry="${itemAttributes.itemForeignUnitCost}"
                    readOnly="${(not amendmentEntry or (readOnlyFlag and invoiceFlag))} " tabindexOverride="${tabindexOverrideBase + 0}" />
        </td>
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignDiscount}"
                colspan="1" />
        <td class="infoline" >
            <div align="center" />
            <kul:htmlControlAttribute
                    property="document.item[${ctr}].itemForeignDiscount"
                    attributeEntry="${itemAttributes.itemForeignDiscount}"
                    readOnly="${(not amendmentEntry or (readOnlyFlag and invoiceFlag)) and (not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment)))))}"
                    tabindexOverride="${tabindexOverrideBase + 0}" /></td>
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemForeignDiscountType}" />
        <td class="infoline">
            <div align="center" />
            <kul:htmlControlAttribute
                    property="document.item[${ctr}].itemForeignDiscountType"
                    attributeEntry="${itemAttributes.itemForeignDiscountType}"
                    readOnly="${(not amendmentEntry or (readOnlyFlag and invoiceFlag)) and (not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment)))))}"
                    tabindexOverride="${tabindexOverrideBase + 0}" /></td>

    </c:if>
    <c:if
            test="${KualiForm.document.vendorDetail.currencyType.currencyType eq baseCurrency or ( KualiForm.document.vendorDetail.currencyType.currencyType eq  null)}">
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemUnitPrice}" width="10%"/>

        <td class="infoline">
            <div align="center"><kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.itemUnitPrice}"                                                                                                          property="document.item[${ctr}].itemUnitPrice" readOnly="${true}" />
            </div>
        </td>
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemDiscount}" width="10%"/>
        <td class="infoline">
            <div align="center" />
            <kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.itemDiscount}"
                    property="document.item[${ctr}].itemDiscount"
                    readOnly="${(not amendmentEntry or (readOnlyFlag and invoiceFlag)) and (not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment)))))}"
                    tabindexOverride="${tabindexOverrideBase + 0}" /></td>
        <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.itemDiscountType}" width="10%"/>
        <td class="infoline">
            <div align="center" />
            <kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.itemDiscountType}"
                    property="document.item[${ctr}].itemDiscountType"
                    extraReadOnlyProperty="document.item[${ctr}].itemDiscountType"
                    readOnly="${(not amendmentEntry or (readOnlyFlag and invoiceFlag)) and (not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment)))))}" /></td>
    </c:if>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.itemStatus}" />

    <td class="infoline" rowspan="1" colspan="1">
        <div align="center"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemStatus}"
                property="document.item[${ctr}].itemStatus"
                readOnly="${(not amendmentEntry or (readOnlyFlag and invoiceFlag)) and (not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment)))))}"
                tabindexOverride="${tabindexOverrideBase + 0}" /></div>
    </td>

    <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.purposeId}" />

    <td class="infoline" rowspan="1" colspan="1">
        <div align="center"><kul:htmlControlAttribute
                attributeEntry="${itemAttributes.purposeId}"
                property="document.item[${ctr}].purposeId"
                readOnly="${not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment))))}"
                tabindexOverride="${tabindexOverrideBase + 0}" /></div>
    </td>

    <c:if
            test="${ not (KualiForm.document.vendorDetail.currencyType.currencyType eq baseCurrency) and (not (KualiForm.document.vendorDetail.currencyType.currencyType eq null))}">
        <td class="infoline" rowspan="1"
            colspan="2">
            <div align="center"><c:choose>
                <c:when
                        test="${fullEntryMode and !amendmentEntry or amendmentEntry and (itemLine.itemInvoicedTotalAmount == null or itemLine.newItemForAmendment)}">
                    <html:image property="methodToCall.deleteItem.line${ctr}"
                                src="${ConfigProperties.kr.externalizable.images.url}tinybutton-delete1.gif"
                                alt="Delete Item ${ctr+1}" title="Delete Item ${ctr+1}"
                                styleClass="tinybutton" />
                </c:when>
                <c:when
                        test="${amendmentEntry and itemLine.canInactivateItem and itemLine.itemInvoicedTotalAmount == 0 and itemLine.copyList[0].receiptStatus ne 'Received'}">
                    <html:image property="methodToCall.inactivateItem.line${ctr}"
                                src="${ConfigProperties.externalizable.images.url}tinybutton-inactivate.gif"
                                alt="Inactivate Item ${ctr+1}" title="Inactivate Item ${ctr+1}"
                                styleClass="tinybutton" />
                    <!-- <br>
                    <br> -->
                </c:when>
                <c:when test="${isATypeOfPODoc and !itemLine.itemActiveIndicator}">
                    <div align="center">Inactive</div>
                    <!-- <br>
                    <br> -->
                </c:when>
                <c:otherwise>
                    <div align="center">&nbsp;</div>
                </c:otherwise>
            </c:choose> <c:if test="${isATypeOfPODoc}">
                <%-- <td class="infoline" rowspan="${currentItemRowspan}"
                    colspan="${colSpanAction}">
                <div align="center"><kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.itemInvoicedTotalAmount}"
                    property="document.item[${ctr}].itemInvoicedTotalAmount"
                    readOnly="${true}" /></div>
                </td> --%>
            </c:if></div>
        </td>
    </c:if>
    <c:if
            test="${KualiForm.document.vendorDetail.currencyType.currencyType eq baseCurrency or ( KualiForm.document.vendorDetail.currencyType.currencyType eq  null)}">
        <td class="infoline" rowspan="1"
            colspan="2">
            <div align="center"><c:choose>
                <c:when
                        test="${fullEntryMode and !amendmentEntry or amendmentEntry and (itemLine.itemInvoicedTotalAmount == null or itemLine.newItemForAmendment)}">
                    <html:image property="methodToCall.deleteItem.line${ctr}"
                                src="${ConfigProperties.kr.externalizable.images.url}tinybutton-delete1.gif"
                                alt="Delete Item ${ctr+1}" title="Delete Item ${ctr+1}"
                                styleClass="tinybutton" />
                    <!-- <br>
                    <br> -->
                </c:when>
                <%--<c:when
                        test="${amendmentEntry}">--%>
                <c:when
                        test="${amendmentEntry and itemLine.canInactivateItem and itemLine.itemInvoicedTotalAmount == 0 and itemLine.copyList[0].receiptStatus ne 'Received'}">
                    <html:image property="methodToCall.inactivateItem.line${ctr}"
                                src="${ConfigProperties.externalizable.images.url}tinybutton-inactivate.gif"
                                alt="Inactivate Item ${ctr+1}" title="Inactivate Item ${ctr+1}"
                                styleClass="tinybutton" />
                    <!-- <br>
                    <br> -->
                </c:when>
                <c:when test="${isATypeOfPODoc and !itemLine.itemActiveIndicator}">
                    <div align="center">Inactive</div>
                    <!-- <br>
                    <br> -->
                </c:when>
                <c:otherwise>
                    <div align="center">&nbsp;</div>
                </c:otherwise>
            </c:choose> <c:if test="${isATypeOfPODoc}">
                <%-- <td class="infoline" rowspan="${currentItemRowspan}"
                    colspan="${colSpanAction}">
                <div align="center"><kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.itemInvoicedTotalAmount}"
                    property="document.item[${ctr}].itemInvoicedTotalAmount"
                    readOnly="${true}" /></div>
                </td> --%>
            </c:if></div>
        </td>
    </c:if>

</tr>


<!-- Code added for display of Title and Author as part of the requirement for the April 2011 Demo -->
<tr>

</tr>
<c:if test="${amendmentEntry or isSplitPO or isReOpenPO}">
    <c:set var="colspanCurrentRate" value="1" />
</c:if>
<c:if test="${not (amendmentEntry or isSplitPO or isReOpenPO)}">
    <c:set var="colspanCurrentRate" value="2" />
</c:if>

<tr>
    <c:set var="itemNo" value="${itemNo+1}" />
    <div align="right"><kul:htmlControlAttribute
            attributeEntry="${itemAttributes.itemTitleId}"
            property="document.item[${ctr}].itemTitleId" readOnly="${false}"
            tabindexOverride="${tabindexOverrideBase + 0}" /></div>
    <div align="right"><kul:htmlControlAttribute
            attributeEntry="${itemAttributes.bibUUID}"
            property="document.item[${ctr}].bibUUID" readOnly="${false}"
            tabindexOverride="${tabindexOverrideBase + 0}" /></div>
    <!-- <th colspan="1" rowspan="1">&nbsp;Bib</th> -->
    <td class="infoline" colspan="1">

        <div align="right">
            <c:if test="${isSaved}">
                <!--<input type="button" id="bibEditAddedItemButton_${ctr}" value="edit"
                onclick="javascript:editXMLContent('${bibeditorEditURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')" />
                <input type="button" id="bibCreateAddedItemButton_${ctr}" value="create"
                onclick="javascript:addXMLContent('${bibeditorCreateURL}','','${docNumber}','${marcXMLFileDirLocation}','${itemNo-1}','marc')" />-->
                <%-- <input type="image" id="bibEditAddedItemButton_${ctr}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-edit1.gif" onclick="javascript:editXMLContent('${bibeditorEditURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')" />
                <input type="image" id="bibCreateAddedItemButton_${ctr}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-createnew.gif" onclick="javascript:addXMLContent('${bibeditorCreateURL}','','${docNumber}','${marcXMLFileDirLocation}','${itemNo-1}','marc')" /> --%>
            </c:if>
            <c:if test="${! isSaved}">
                <%-- <input type="image" id="bibViewAddedItemButton_${ctr}" src="${ConfigProperties.externalizable.images.url}tinybutton-view.gif" onclick="javascript:viewXMLContent('${bibeditorViewURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')"/> --%>
            </c:if>
        </div>

        <script>
            //document.getElementById('document.item['+${ctr}+'].itemTitleId').style.display="none";
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

<!-- End Code for display of bib info for April 2011 Demo -->
</tr>

<c:set var="accountColumnCount"
       value="${mainColumnCount - colSpanAction - 4}" />
<c:if test="${isATypeOfPODoc}">
    <c:set var="accountColumnCount" value="${accountColumnCount - 1}" />
</c:if>

<c:choose>
    <c:when test="${amendmentEntry}">
        <c:choose>
            <c:when
                    test="${itemLine.itemActiveIndicator and (!amendmentEntryWithUnpaidPreqOrCM or itemLine.itemInvoicedTotalAmount == null or itemLine.newItemForAmendment)}">
                <c:set target="${KualiForm.accountingLineEditingMode}"
                       property="fullEntry" value="true" />
                <!-- Start of Notes Tag  -->
                <select:olenotes itemAttributes="${itemAttributes}"
                                 accountPrefix="document.item[${ctr}]."
                                 itemColSpan="${accountColumnCount}" count="${ctr}" />
                <!-- End of Notes Tag  -->
                <purap:purapGeneralAccounting itemAttributes="${itemAttributes}"
                                              accountPrefix="document.item[${ctr}]."
                                              itemColSpan="${accountColumnCount}" count="${ctr}" />
                <c:if test="${(KualiForm.document.items[ctr].linkToOrderOption eq 'NB_PRINT' || KualiForm.document.items[ctr].linkToOrderOption eq 'ORDER_RECORD_IMPORT_MARC_ONLY_PRINT' || KualiForm.document.items[ctr].linkToOrderOption eq 'ORDER_RECORD_IMPORT_MARC_EDI' || KualiForm.document.items[ctr].linkToOrderOption eq 'EB_PRINT') &&( KualiForm.document.items[ctr].itemQuantity >1 || KualiForm.document.items[ctr].itemNoOfParts >1)}">
                    <select:oleCopies itemAttributes="${itemAttributes}"
                                      accountPrefix="document.item[${ctr}]."
                                      isATypeOfRCVGDoc="${isATypeOfRCVGDoc}"
                                      isATypeOfCORRDoc="${isATypeOfCORRDoc}"
                                      quantity="${itemLine.itemQuantity}"
                                      previousItemQuantity="${itemLine.previousItemQuantity}"
                                      noOfParts="${itemLine.itemNoOfParts}"
                                      previousItemNoOfParts="${itemLine.previousItemNoOfParts}"
                                      itemColSpan="${accountColumnCount}"
                                      count="${ctr}"
                                      isFinal="${isFinal}"/>
                </c:if>
                <select:olePaymentHistory itemAttributes="${itemAttributes}"
                                          accountPrefix="document.item[${ctr}]."
                                          itemColSpan="${accountColumnCount}" count="${ctr}" />
                <select:oleClaimHistory itemAttributes="${itemAttributes}"
                                        accountPrefix="document.item[${ctr}]."
                                        itemColSpan="${accountColumnCount}" count="${ctr}" />
                <select:oleDonor itemAttributes="${itemAttributes}"
                                 accountPrefix="document.item[${ctr}]."
                                 itemColSpan="${accountColumnCount}" count="${ctr}"
                                 isFinal="${isFinal}"/>

            </c:when>
            <c:otherwise>
                <c:set target="${KualiForm.editingMode}" property="viewOnly"
                       value="true" />
                <!-- Start of Notes Tag  -->
                <select:olenotes itemAttributes="${itemAttributes}"
                                 accountPrefix="document.item[${ctr}]."
                                 itemColSpan="${accountColumnCount}" count="${ctr}" />
                <!-- End of Notes Tag  -->
                <purap:purapGeneralAccounting itemAttributes="${itemAttributes}"
                accountPrefix="document.item[${ctr}]."
                itemColSpan="${accountColumnCount}" count="${ctr}" />
                <c:if test="${(KualiForm.document.items[ctr].linkToOrderOption eq 'NB_PRINT' || KualiForm.document.items[ctr].linkToOrderOption eq 'ORDER_RECORD_IMPORT_MARC_ONLY_PRINT' || KualiForm.document.items[ctr].linkToOrderOption eq 'ORDER_RECORD_IMPORT_MARC_EDI' || KualiForm.document.items[ctr].linkToOrderOption eq 'EB_PRINT') && (KualiForm.document.items[ctr].itemQuantity >1 || KualiForm.document.items[ctr].itemNoOfParts >1)}">
                    <select:oleCopies itemAttributes="${itemAttributes}"
                                      accountPrefix="document.item[${ctr}]."
                                      isATypeOfRCVGDoc="${isATypeOfRCVGDoc}"
                                      isATypeOfCORRDoc="${isATypeOfCORRDoc}"
                                      itemColSpan="${accountColumnCount}"
                                      count="${ctr}"
                                      isFinal="${isFinal}"/>
                </c:if>
                <select:olePaymentHistory itemAttributes="${itemAttributes}"
                                          accountPrefix="document.item[${ctr}]."
                                          itemColSpan="${accountColumnCount}" count="${ctr}" />
                <select:oleClaimHistory itemAttributes="${itemAttributes}"
                                        accountPrefix="document.item[${ctr}]."
                                        itemColSpan="${accountColumnCount}" count="${ctr}" />
                <select:oleDonor itemAttributes="${itemAttributes}"
                                 accountPrefix="document.item[${ctr}]."
                                 itemColSpan="${accountColumnCount}" count="${ctr}"
                                 isFinal="${isFinal}"/>


            </c:otherwise>
        </c:choose>
    </c:when>
    <c:when
            test="${unorderedItemAccountEntry and itemLine.newUnorderedItem}">
        <c:set target="${KualiForm.accountingLineEditingMode}"
               property="fullEntry" value="true" />
        <!-- Start of Notes Tag  -->
        <select:olenotes itemAttributes="${itemAttributes}"
                         accountPrefix="document.item[${ctr}]."
                         itemColSpan="${accountColumnCount}" count="${ctr}" />
        <!-- End of Notes Tag  -->
        <purap:purapGeneralAccounting itemAttributes="${itemAttributes}"
                                      accountPrefix="document.item[${ctr}]."
                                      itemColSpan="${accountColumnCount}" count="${ctr}" />
        <c:if test="${(KualiForm.document.items[ctr].linkToOrderOption eq 'NB_PRINT' || KualiForm.document.items[ctr].linkToOrderOption eq 'ORDER_RECORD_IMPORT_MARC_ONLY_PRINT' || KualiForm.document.items[ctr].linkToOrderOption eq 'ORDER_RECORD_IMPORT_MARC_EDI' || KualiForm.document.items[ctr].linkToOrderOption eq 'EB_PRINT') && (KualiForm.document.items[ctr].itemQuantity >1 || KualiForm.document.items[ctr].itemNoOfParts >1)}">
            <select:oleCopies itemAttributes="${itemAttributes}"
                              accountPrefix="document.item[${ctr}]."
                              isATypeOfRCVGDoc="${isATypeOfRCVGDoc}"
                              isATypeOfCORRDoc="${isATypeOfCORRDoc}"
                              itemColSpan="${accountColumnCount}"
                              count="${ctr}"
                              isFinal="${isFinal}" />
        </c:if>
        <select:olePaymentHistory itemAttributes="${itemAttributes}"
                                  accountPrefix="document.item[${ctr}]."
                                  itemColSpan="${accountColumnCount}" count="${ctr}" />
        <select:oleClaimHistory itemAttributes="${itemAttributes}"
                                accountPrefix="document.item[${ctr}]."
                                itemColSpan="${accountColumnCount}" count="${ctr}" />
        <select:oleDonor itemAttributes="${itemAttributes}"
                         accountPrefix="document.item[${ctr}]."
                         itemColSpan="${accountColumnCount}" count="${ctr}"
                         isFinal="${isFinal}"/>


    </c:when>

    <c:when test="${(!amendmentEntry)}">
        <c:if
                test="${!empty KualiForm.editingMode['allowItemEntry'] && (KualiForm.editingMode['allowItemEntry'] == itemLine.itemIdentifier)}">
            <c:set target="${KualiForm.editingMode}" property="expenseEntry"
                   value="true" />
        </c:if>
        <!-- Start of Notes Tag  -->
        <select:olenotes itemAttributes="${itemAttributes}"
                         accountPrefix="document.item[${ctr}]."
                         itemColSpan="${accountColumnCount}" count="${ctr}" />
        <!-- End of Notes Tag  -->
        <purap:purapGeneralAccounting itemAttributes="${itemAttributes}"
                                      accountPrefix="document.item[${ctr}]."
                                      itemColSpan="${accountColumnCount}" count="${ctr}" />
        <c:if test="${(KualiForm.document.items[ctr].linkToOrderOption eq 'NB_PRINT' || KualiForm.document.items[ctr].linkToOrderOption eq 'ORDER_RECORD_IMPORT_MARC_ONLY_PRINT' || KualiForm.document.items[ctr].linkToOrderOption eq 'ORDER_RECORD_IMPORT_MARC_EDI' || KualiForm.document.items[ctr].linkToOrderOption eq 'EB_PRINT') && (KualiForm.document.items[ctr].itemQuantity >1 || KualiForm.document.items[ctr].itemNoOfParts >1)}">
            <select:oleCopies itemAttributes="${itemAttributes}"
                              accountPrefix="document.item[${ctr}]."
                              isATypeOfRCVGDoc="${isATypeOfRCVGDoc}"
                              isATypeOfCORRDoc="${isATypeOfCORRDoc}"
                              itemColSpan="${accountColumnCount}"
                              count="${ctr}"
                              isFinal="${isFinal}" />
        </c:if>
        <select:olePaymentHistory itemAttributes="${itemAttributes}"
                                  accountPrefix="document.item[${ctr}]."
                                  itemColSpan="${accountColumnCount}" count="${ctr}" />
        <select:oleClaimHistory itemAttributes="${itemAttributes}"
                                accountPrefix="document.item[${ctr}]."
                                itemColSpan="${accountColumnCount}" count="${ctr}" />
        <select:oleDonor itemAttributes="${itemAttributes}"
                         accountPrefix="document.item[${ctr}]."
                         itemColSpan="${accountColumnCount}" count="${ctr}"
                         isFinal="${isFinal}"/>


    </c:when>
</c:choose>

<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
    </tbody>
</c:if>
</c:if>
</logic:iterate>

<c:if test="${!lockB2BEntry}">
    <tr>
        <th height=30 colspan="${mainColumnCount}">&nbsp;</th>
    </tr>
    <purap:miscitems itemAttributes="${itemAttributes}"
                     accountingLineAttributes="${accountingLineAttributes}"
                     descriptionFirst="${KualiForm.document.isATypeOfPurDoc}"
                     mainColumnCount="${mainColumnCount}"
                     colSpanItemType="${colSpanItemType}"
                     colSpanDescription="${colSpanDescription}"
                     colSpanExtendedPrice="${colSpanExtendedPrice}"
                     colSpanAmountPaid="${colSpanAmountPaid}" />
</c:if>

<!-- BEGIN TOTAL SECTION -->
<tr>
    <th height=30 colspan="${mainColumnCount}">&nbsp;</th>
</tr>

<tr>
    <td colspan="${mainColumnCount}" class="subhead"><span
            class="subhead-left">Totals</span> <span class="subhead-right">&nbsp;</span>
    </td>
</tr>

<c:set var="colSpanTotalLabel"
       value="${colSpanItemType+colSpanDescription}" />
<c:set var="colSpanTotalAmount" value="${colSpanExtendedPrice}" />
<c:set var="colSpanTotalBlank"
       value="${mainColumnCount-colSpanTotalLabel-colSpanTotalAmount}" />

<c:if test="${purapTaxEnabled}">
    <c:set var="colSpanTotalBlank" value="${colSpanTotalBlank-2}" />
    <c:set var="colSpanTotalAmount" value="1" />
    <c:set var="colSpanTotalLabel"
           value="${mainColumnCount-colSpanTotalBlank-colSpanTotalAmount}" />

    <tr>
        <th align=right width='75%' colspan="${colSpanTotalLabel}"
            scope="row">
            <div align="right"><kul:htmlAttributeLabel
                    attributeEntry="${DataDictionary.RequisitionDocument.attributes.totalPreTaxDollarAmount}" />
            </div>
        </th>
        <td valign=middle class="datacell" colspan="${colSpanTotalAmount}">
            <div align="right"><b> <kul:htmlControlAttribute
                    attributeEntry="${DataDictionary.RequisitionDocument.totalPreTaxDollarAmount}"
                    property="document.totalPreTaxDollarAmount" readOnly="true" />&nbsp;
            </b></div>
        </td>
        <td class="datacell" colspan="${colSpanTotalBlank}">&nbsp;</td>
    </tr>

    <tr>
        <th align=right width='75%' colspan="${colSpanTotalLabel}"
            scope="row">
            <div align="right"><kul:htmlAttributeLabel
                    attributeEntry="${DataDictionary.RequisitionDocument.attributes.totalTaxAmount}" />
            </div>
        </th>
        <td valign=middle class="datacell" colspan="${colSpanTotalAmount}">
            <div align="right"><b> <kul:htmlControlAttribute
                    attributeEntry="${DataDictionary.RequisitionDocument.totalTaxAmount}"
                    property="document.totalTaxAmount" readOnly="true" />&nbsp; </b></div>
        </td>
        <td class="datacell" colspan="${colSpanTotalBlank}">&nbsp;</td>
    </tr>
</c:if>

<tr>
    <th align=right width='75%' colspan="${colSpanTotalLabel}"
        scope="row">
        <div align="right"><kul:htmlAttributeLabel
                attributeEntry="${DataDictionary.RequisitionDocument.attributes.totalDollarAmount}" />
        </div>
    </th>
    <td valign=middle class="datacell" colspan="${colSpanTotalAmount}">
        <div align="right"><b> <kul:htmlControlAttribute
                attributeEntry="${DataDictionary.RequisitionDocument.attributes.totalDollarAmount}"
                property="document.totalDollarAmount" readOnly="true" />&nbsp; </b></div>
    </td>
    <td class="datacell" colspan="${colSpanTotalBlank}">&nbsp;</td>
</tr>

<tr>
    <th align=right width='75%' colspan="${colSpanTotalLabel}"
        scope="row"><c:if test="${displayRequisitionFields}">
        <div align="right"><kul:htmlAttributeLabel
                attributeEntry="${DataDictionary.RequisitionDocument.attributes.organizationAutomaticPurchaseOrderLimit}" />
        </div>
    </c:if> <c:if test="${!displayRequisitionFields}">
        <div align="right"><kul:htmlAttributeLabel
                attributeEntry="${DataDictionary.PurchaseOrderDocument.attributes.internalPurchasingLimit}" />
        </div>
    </c:if></th>
    <td align=right valign=middle class="datacell"
        colspan="${colSpanTotalAmount}"><c:if
            test="${displayRequisitionFields}">
        <div align="right"><kul:htmlControlAttribute
                attributeEntry="${DataDictionary.RequisitionDocument.attributes.organizationAutomaticPurchaseOrderLimit}"
                property="document.organizationAutomaticPurchaseOrderLimit"
                readOnly="true" />&nbsp;</div>
    </c:if> <c:if test="${!displayRequisitionFields}">
        <div align="right"><kul:htmlControlAttribute
                attributeEntry="${DataDictionary.PurchaseOrderDocument.attributes.internalPurchasingLimit}"
                property="document.internalPurchasingLimit" readOnly="true" />&nbsp;
        </div>
    </c:if></td>
    <td class="datacell" colspan="${colSpanTotalBlank}">&nbsp;</td>
</tr>
<!-- END TOTAL SECTION -->

</table>

</div>
</kul:tab>
<script>
    function addXMLContent(url,elementId,docNumber,dirPath,itemNumber,docType,linkToOrderOption) {
        var controlId = elementId;
        var tokenId = docNumber;
        if(itemNumber.length ==0){//used to set file name with item no for current item create
            itemNumber = ${itemNo};
        }
        tokenId = docNumber+'_'+itemNumber;
        url = url+"&action=create&tokenId="+tokenId+"&__login_user=admin"+"&linkToOrderOption="+linkToOrderOption;
        //url = url + "?fromOLE=true&action=create&fileName="+fileName+"&dirPath="+dirPath;
        window.open(url);
    }

    function editXMLContent(url,elementId,docNumber,dirPath,itemNumber,docType) {
        var controlId = elementId;
        var tokenId = docNumber;
        tokenId = docNumber+'_'+itemNumber;
        uuid = document.getElementById(controlId).value;
        document.getElementById(controlId).style.display="none";
        //url = url+"&action=edit&docType="+docType+"&uuid="+uuid+"&tokenId="+tokenId+"&__login_user=admin";
        //url=url+"?docType="+docType+"&uuid="+uuid+"&action=edit&fromOLE=true";
        url = url + "&docId=" + uuid;
        window.open(url);
    }

    function viewXMLContent(url,elementId,docNumber,dirPath,itemNumber,docType) {
        var controlId = elementId;
        uuid = document.getElementById(controlId).value;
        url =url+"docId="+uuid;
        window.open(url);
    }
    function getHoldingValue() {
        if (document.getElementById('printOption').checked) {
            return 'PRINT';
        }
        else {
            return 'ELECTRONIC';
        }
    }

    function getItemCount() {
        return ${ctr};
    }

</script>