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

<%@ attribute name="itemAttributes" required="true" type="java.util.Map" description="The DataDictionary entry containing attributes for this row's fields."%>

<%@ attribute name="intialOpen" required="false"
              description="Boolean to indicate whether the tag to be opened when initiating the document" %>


<c:if test="${empty intialOpen}">
    <c:set var="intialOpen" value="true" />
</c:if>
<script language="JavaScript" type="text/javascript" src="dwr/interface/ItemUnitOfMeasureService.js"></script>
<script language="JavaScript" type="text/javascript" src="scripts/purap/objectInfo.js"></script>
<c:set var="isSaved" value="${KualiForm.document.isSaved}"/>
<c:set var="isATypeOfRCVGDoc" value="${KualiForm.document.isATypeOfRCVGDoc}" />
<c:set var="isATypeOfCORRDoc" value="${KualiForm.document.isATypeOfCORRDoc}" />
<c:set var="fullEntryMode" value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT]}" />
<c:set var="documentType" value="${KualiForm.document.documentHeader.workflowDocument.documentTypeName}" />
<c:set var="colCount" value="20"/>
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
<c:set var="docNumber" value="${KualiForm.document.documentNumber }" />
<c:set var="marcXMLFileDirLocation"
       value="${KualiForm.document.marcXMLFileDirLocation}" />
<c:if test="${KualiForm.stateFinal}">
    <c:set var="colCount" value="16"/>
</c:if>
<c:set var="tabindexOverrideBase" value="20" />
<c:set var="isFinal" value="${KualiForm.document.isFinalReqs}" />

<kul:tab tabTitle="Titles" defaultOpen="${intialOpen}" tabErrorKey="${PurapConstants.LINEITEM_TAB_ERRORS}">
<div class="tab-container" align=center>
<table cellpadding="0" cellspacing="0" class="datatable" summary="Items Section">
<tr>
    <td colspan="${colCount}" class="subhead">
        <span class="subhead-left">Receiving Line Items</span>
    </td>
</tr>

<c:if test="${fullEntryMode}">
    <tr>
        <td colspan="${colCount}" class="datacell" align="right" nowrap="nowrap">
            <div align="right">
                <c:if test="${KualiForm.ableToShowClearAndLoadQtyButtons}">
                    <html:image property="methodToCall.loadQty" src="${ConfigProperties.externalizable.images.url}tinybutton-loadqtyreceived.gif" alt="load qty received" title="load qty received" styleClass="tinybutton" />
                    <html:image property="methodToCall.clearQty" src="${ConfigProperties.externalizable.images.url}tinybutton-clearqtyreceived.gif" alt="clear qty received" title="clear qty received" styleClass="tinybutton" />
                </c:if>
            </div>
        </td>
    </tr>
</c:if>

<!--  Column Names -->
<tr>
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemLineNumber}" />
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemCatalogNumber}" />
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemDescription}" />
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleFormatId}" />
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemOrderedQuantity}" />
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemOrderedParts}" />
        <%-- <kul:htmlAttributeHeaderCell><kul:htmlAttributeLabel attributeEntry="${itemAttributes.itemUnitOfMeasureCode}" useShortLabel="true"/></kul:htmlAttributeHeaderCell> --%>

    <c:if test="${KualiForm.stateFinal == false}">
        <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemReceivedPriorQuantity}" />
        <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemReceivedPriorParts}" />
        <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemReceivedToBeQuantity}" />
        <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemReceivedToBeParts}" />
    </c:if>
<%--
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemReceivedTotalQuantity}" />
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemReceivedTotalParts}" />--%>
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemReturnedTotalQuantity}" />
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemReturnedTotalParts}" />
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemDamagedTotalQuantity}" />
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemDamagedTotalParts}" />
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemReasonAddedCode}" />
    <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.availableToPublic}" />

    <kul:htmlAttributeHeaderCell literalLabel="Actions"/>
</tr>

<!--  New Receiving Line Item -->
<%--<c:if test="${fullEntryMode}">
    <tr>
        <td class="infoline" rowspan="2">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemLineNumber}" property="newLineItemReceivingItemLine.itemLineNumber" readOnly="${true}"/>
        </td>
        <td class="infoline">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemCatalogNumber}" property="newLineItemReceivingItemLine.itemCatalogNumber" tabindexOverride="${tabindexOverrideBase + 0}"/>
        </td>
        <td class="infoline">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemDescription}" property="newLineItemReceivingItemLine.itemDescription" tabindexOverride="${tabindexOverrideBase + 0}"
                                      readOnly="${true}"/>
        </td>
        <td class="infoline">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.oleFormatId}" property="newLineItemReceivingItemLine.oleFormatId" tabindexOverride="${tabindexOverrideBase + 0}"/>
        </td>
        <td class="infoline">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.oleItemOrderedQuantity}" property="newLineItemReceivingItemLine.oleItemOrderedQuantity" readOnly="${true}"/>
        </td>
        <td class="infoline">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.oleItemOrderedParts}" property="newLineItemReceivingItemLine.oleItemOrderedParts" readOnly="${true}"/>
        </td>
            &lt;%&ndash; <td class="infoline" nowrap="nowrap">
                <c:set var="itemUnitOfMeasureCodeField"  value="newLineItemReceivingItemLine.itemUnitOfMeasureCode" />
                <c:set var="itemUnitOfMeasureDescriptionField"  value="newLineItemReceivingItemLine.itemUnitOfMeasure.itemUnitOfMeasureDescription" />
                <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemUnitOfMeasureCode}" 
                    property="${itemUnitOfMeasureCodeField}" 
                    onblur="loadItemUnitOfMeasureInfo( '${itemUnitOfMeasureCodeField}', '${itemUnitOfMeasureDescriptionField}' );${onblur}" tabindexOverride="${tabindexOverrideBase + 0}"/>
                <kul:lookup boClassName="org.kuali.ole.sys.businessobject.UnitOfMeasure" 
                    fieldConversions="itemUnitOfMeasureCode:newLineItemReceivingItemLine.itemUnitOfMeasureCode"
                    lookupParameters="'Y':active"/>     
                <div id="newLineItemReceivingItemLine.itemUnitOfMeasure.itemUnitOfMeasureDescription.div" class="fineprint">
                    <html:hidden write="true" property="${itemUnitOfMeasureDescriptionField}"/>&nbsp;        
                </div>     
            </td>  &ndash;%&gt;

        <c:if test="${KualiForm.stateFinal == false}">

            <td class="infoline">
                <kul:htmlControlAttribute attributeEntry="${itemAttributes.oleItemReceivedPriorQuantity}" property="newLineItemReceivingItemLine.oleItemReceivedPriorQuantity" readOnly="${true}"/>
            </td>
            <td class="infoline">
                <kul:htmlControlAttribute attributeEntry="${itemAttributes.oleItemReceivedPriorParts}" property="newLineItemReceivingItemLine.oleItemReceivedPriorParts" readOnly="${true}"/>
            </td>
            <td class="infoline">
                <kul:htmlControlAttribute attributeEntry="${itemAttributes.oleItemReceivedToBeQuantity}" property="newLineItemReceivingItemLine.oleItemReceivedToBeQuantity" readOnly="${true}"/>
            </td>
            <td class="infoline">
                <kul:htmlControlAttribute attributeEntry="${itemAttributes.oleItemReceivedToBeParts}" property="newLineItemReceivingItemLine.oleItemReceivedToBeParts" readOnly="${true}"/>
            </td>
        </c:if>

        <td class="infoline">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.oleItemReceivedTotalQuantity}" property="newLineItemReceivingItemLine.oleItemReceivedTotalQuantity" tabindexOverride="${tabindexOverrideBase + 0}" readOnly="${true}"/>
        </td>
        <td class="infoline">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.oleItemReceivedTotalParts}" property="newLineItemReceivingItemLine.oleItemReceivedTotalParts" tabindexOverride="${tabindexOverrideBase + 0}" readOnly="${true}"/>
        </td>
        <td class="infoline">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.oleItemReturnedTotalQuantity}" property="newLineItemReceivingItemLine.oleItemReturnedTotalQuantity" tabindexOverride="${tabindexOverrideBase + 0}" readOnly="${true}"/>
        </td>
        <td class="infoline">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.oleItemReturnedTotalParts}" property="newLineItemReceivingItemLine.oleItemReturnedTotalParts" tabindexOverride="${tabindexOverrideBase + 0}" readOnly="${true}"/>
        </td>
        <td class="infoline">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.oleItemDamagedTotalQuantity}" property="newLineItemReceivingItemLine.oleItemDamagedTotalQuantity" tabindexOverride="${tabindexOverrideBase + 0}" readOnly="${true}"/>
        </td>
        <td class="infoline">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.oleItemDamagedTotalParts}" property="newLineItemReceivingItemLine.oleItemDamagedTotalParts" tabindexOverride="${tabindexOverrideBase + 0}" readOnly="${true}"/>
        </td>
        <td class="infoline">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.itemReasonAddedCode}" property="newLineItemReceivingItemLine.itemReasonAddedCode" tabindexOverride="${tabindexOverrideBase + 0}"/>
        </td>
        <td class="infoline">
            <kul:htmlControlAttribute attributeEntry="${itemAttributes.availableToPublic}" property="newLineItemReceivingItemLine.availableToPublic" tabindexOverride="${tabindexOverrideBase + 0}"/>
        </td>

        <td class="infoline" rowspan="2">
            <div align="center">
                <html:image property="methodToCall.addItem" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-add1.gif" alt="Insert an Item" title="Add an Item" styleClass="tinybutton" />
            </div>
        </td>
    </tr>
    <tr>

        <td class="subhead">
            <div>Bib Info:</div>
        </td>
        <th colspan="1" rowspan="1">&nbsp; Bib Editor</th>
        <td class="infoline" colspan="2">
            <div align="center">
                <input type="radio" name="AttachBib" onclick="hideLinkToBibImage();"/>&nbsp;new bib&nbsp;
                <input type="radio" name="AttachBib" onclick="hideCreateNewImage();"/>&nbsp;existing bib&nbsp;
                <!--<input type="button" id="bibCreateCurrentItemButton" value="create"
						onclick="javascript:addXMLContent('${bibeditorCreateURL}','','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')" />-->
                <input type="image" hidden="true" id="bibCreateCurrentItemButton" name="Create New Bib" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-createnew.gif" onclick="javascript:addXMLContent('${bibeditorCreateURL}','','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')" />
                <input type="image" hidden="true" id="bibSelectExistingItemButton" name="existing bib" src="${ConfigProperties.externalizable.images.url}existing bib.gif" onclick="javascript:addXMLContent('${bibSearchURL}','','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')">
            </div>
        </td>
            &lt;%&ndash; <kul:htmlAttributeHeaderCell
                attributeEntry="${itemAttributes.receiptStatusId}" colspan="1" width="10%"/>

            <td class="infoline" colspan="1">
            <div align="center" />
            <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.receiptStatusId}"
                property="newLineItemReceivingItemLine.receiptStatusId"
                extraReadOnlyProperty="newLineItemReceivingItemLine.oleReceiptStatus.receiptStatusId"
                tabindexOverride="${tabindexOverrideBase + 0}" /></td> &ndash;%&gt;

    </tr>

</c:if>--%>

<!-- Existing Receiving Line Items -->
<logic:iterate indexId="ctr" name="KualiForm" property="document.items" id="itemLine">
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
        <c:set var="isOpen" value="true" />
    </c:when>
    <c:when test="${!empty currentTab}">
        <c:set var="isOpen" value="${currentTab == 'OPEN'}" />
    </c:when>
</c:choose>

<tr>
    <td colspan="${colCount}" class="tab-subhead" style="border-right: none;">
        <c:if test="${isOpen == 'true' || isOpen == 'TRUE'}">
            <html:image
                    property="methodToCall.toggleTab.tab${tabKey}"
                    src="${ConfigProperties.kr.externalizable.images.url}tinybutton-hide.gif"
                    alt="hide" title="toggle" styleClass="tinybutton"
                    styleId="tab-${tabKey}-imageToggle"
                    onclick="javascript: return toggleTab(document, '${tabKey}'); " />
        </c:if>
        <c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
            <html:image
                    property="methodToCall.toggleTab.tab${tabKey}"
                    src="${ConfigProperties.kr.externalizable.images.url}tinybutton-show.gif"
                    alt="show" title="toggle" styleClass="tinybutton"
                    styleId="tab-${tabKey}-imageToggle"
                    onclick="javascript: return toggleTab(document, '${tabKey}'); " />
        </c:if>
    </td>
</tr>

<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
    <tbody style="display: none;" id="tab-${tabKey}-div">
</c:if>

<tr>
    <td class="infoline" nowrap="nowrap" rowspan="6">
        &nbsp;<b><bean:write name="KualiForm" property="document.item[${ctr}].itemLineNumber"/></b>&nbsp;
    </td>
    <td class="infoline">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemCatalogNumber}"
                property="document.item[${ctr}].itemCatalogNumber"
                extraReadOnlyProperty="document.item[${ctr}].itemCatalogNumber"
                readOnly="${((itemLine.itemTypeCode eq 'ITEM') or not (fullEntryMode))}" />
    </td>
    <td class="infoline">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemDescription}"
                property="document.item[${ctr}].itemDescription"
                readOnly="${true}" />
    </td>
    <td class="infoline">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.oleFormatId}"
                property="document.item[${ctr}].oleFormatId"
                readOnly="${((itemLine.itemTypeCode eq 'ITEM') or not (fullEntryMode))}" />
    </td>
    <td class="infoline">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.oleItemOrderedQuantity}"
                property="document.item[${ctr}].oleItemOrderedQuantity"
                readOnly="${true}" />
    </td>
    <td class="infoline">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.oleItemOrderedParts}"
                property="document.item[${ctr}].oleItemOrderedParts"
                readOnly="${true}" />
    </td>
        <%-- <td class="infoline">
            <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemUnitOfMeasureCode}"
                property="document.item[${ctr}].itemUnitOfMeasureCode"
                onblur="loadItemUnitOfMeasureInfo( 'document.item[${ctr}].itemUnitOfMeasureCode', 'document.item[${ctr}].itemUnitOfMeasure.itemUnitOfMeasureDescription' );${onblur}"
                readOnly="${((itemLine.itemTypeCode eq 'ITEM') or not (fullEntryMode))}"
                tabindexOverride="${tabindexOverrideBase + 0}"/>
                <c:if test="${((!itemLine.itemTypeCode eq 'ITEM') && fullEntryMode)}">
                    <kul:lookup boClassName="org.kuali.ole.sys.businessobject.UnitOfMeasure"
                            fieldConversions="itemUnitOfMeasureCode:document.item[${ctr}].itemUnitOfMeasureCode"
                            lookupParameters="'Y':active"/>
                </c:if>
                <div id="document.item[${ctr}].itemUnitOfMeasure.itemUnitOfMeasureDescription.div" class="fineprint">
                    <html:hidden write="true" property="document.item[${ctr}].itemUnitOfMeasure.itemUnitOfMeasureDescription"/>&nbsp;
                </div>

        </td> --%>

    <c:if test="${KualiForm.stateFinal == false}">
        <td class="infoline">
            <kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.oleItemReceivedPriorQuantity}"
                    property="document.item[${ctr}].oleItemReceivedPriorQuantity"
                    readOnly="${true}" />
        </td>
        <td class="infoline">
            <kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.oleItemReceivedPriorParts}"
                    property="document.item[${ctr}].oleItemReceivedPriorParts"
                    readOnly="${true}" />
        </td>
        <td class="infoline">
            <kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.oleItemReceivedToBeQuantity}"
                    property="document.item[${ctr}].oleItemReceivedToBeQuantity"
                    readOnly="${true}" />
        </td>
        <td class="infoline">
            <kul:htmlControlAttribute
                    attributeEntry="${itemAttributes.oleItemReceivedToBeParts}"
                    property="document.item[${ctr}].oleItemReceivedToBeParts"
                    readOnly="${true}" />
        </td>
    </c:if>

   <%-- <td class="infoline">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.oleItemReceivedTotalQuantity}"
                property="document.item[${ctr}].oleItemReceivedTotalQuantity"
                readOnly="${true}" tabindexOverride="${tabindexOverrideBase + 0}"/>
    </td>
    <td class="infoline">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.oleItemReceivedTotalParts}"
                property="document.item[${ctr}].oleItemReceivedTotalParts"
                readOnly="${true}" tabindexOverride="${tabindexOverrideBase + 0}"/>
    </td>--%>
    <td class="infoline">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.oleItemReturnedTotalQuantity}"
                property="document.item[${ctr}].oleItemReturnedTotalQuantity"
                readOnly="${true}" tabindexOverride="${tabindexOverrideBase + 0}"/>
    </td>
    <td class="infoline">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.oleItemReturnedTotalParts}"
                property="document.item[${ctr}].oleItemReturnedTotalParts"
                readOnly="${true}" tabindexOverride="${tabindexOverrideBase + 0}"/>
    </td>
    <td class="infoline">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.oleItemDamagedTotalQuantity}"
                property="document.item[${ctr}].oleItemDamagedTotalQuantity"
                readOnly="${true}" tabindexOverride="${tabindexOverrideBase + 0}"/>
    </td>
    <td class="infoline">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.oleItemDamagedTotalParts}"
                property="document.item[${ctr}].oleItemDamagedTotalParts"
                readOnly="${true}" tabindexOverride="${tabindexOverrideBase + 0}"/>
    </td>
    <td class="infoline">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.itemReasonAddedCode}"
                property="document.item[${ctr}].itemReasonAddedCode"
                extraReadOnlyProperty="document.item[${ctr}].itemReasonAdded.itemReasonAddedDescription"
                readOnly="${not (fullEntryMode) or itemLine.itemLineNumber != null}" tabindexOverride="${tabindexOverrideBase + 0}"/>
    </td>
    <td class="infoline">
        <kul:htmlControlAttribute
                attributeEntry="${itemAttributes.availableToPublic}"
                property="document.item[${ctr}].availableToPublic"
                readOnly="${not (fullEntryMode)}" tabindexOverride="${tabindexOverrideBase + 0}"/>
    </td>

</tr>
<tr>
    <c:set var="itemNo" value="${itemNo+1}" />
    <td class="subhead">
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

   <%-- <c:if test="${fn:length(KualiForm.document.items[ctr].copyList) == 1 }">--%>
        <c:if test="${!isFinal && isATypeOfRCVGDoc }">
            <c:if test="${KualiForm.document.items[ctr].copyList[0].receiptStatus ne 'Received'}">
                <th>Action:</th>
                <td class="infoline">
                    <html:image
                        property="methodToCall.receiveCopy.line${ctr}"
                        src="${ConfigProperties.externalizable.images.url}receive.gif"
                        alt="Receive" title="Receive"
                        styleClass="tinybutton"  />
                </td>
            </c:if>
        </c:if>
        <c:if test="${KualiForm.document.items[ctr].copyList[0].receiptStatus eq 'Received'}">
            <th>Receipt Status:</th>
            <td class="infoline">
                <kul:htmlControlAttribute
                    property="document.item[${ctr}].copyList[0].receiptStatus"
                    attributeEntry="${itemAttributes.receiptStatus}"
                    tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/>
            </td>
        </c:if>
        <c:if test="${isFinal && isATypeOfRCVGDoc && KualiForm.document.items[ctr].copyList[0].receiptStatus eq 'Not Received'}">
            <th>Receipt Status:</th>
            <td class="infoline">
                <kul:htmlControlAttribute
                        property="document.item[${ctr}].copyList[0].receiptStatus"
                        attributeEntry="${itemAttributes.receiptStatus}"
                        tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/>
            </td>
        </c:if>
    <%--</c:if>--%>

        <%-- <kul:htmlAttributeHeaderCell
            attributeEntry="${itemAttributes.receiptStatusId}" colspan="1" width="15%"/>
        <td class="infoline" colspan="2">
        <div align="center" />
                            <kul:htmlControlAttribute
            attributeEntry="${itemAttributes.receiptStatusId}"
            property="document.item[${ctr}].receiptStatusId"
            readOnly="${not ((fullEntryMode and !amendmentEntry) or (amendmentEntry and itemLine.itemActiveIndicator and (not (amendmentEntryWithUnpaidPreqOrCM and itemLine.itemInvoicedTotalAmount != null and !itemLine.newItemForAmendment))))}"
            tabindexOverride="${tabindexOverrideBase + 0}" />

            </td> --%>

</tr>
<select:oleReceivingLineItemExceptionNotes itemAttributes="${itemAttributes}" accountPrefix="document.item[${ctr}]." itemColSpan="${colCount}" count="${ctr}"/>
<select:oleReceivingLineItemReceiptNotes itemAttributes="${itemAttributes}" accountPrefix="document.item[${ctr}]." itemColSpan="${colCount}" count="${ctr}"/>
<select:oleReceivingLineItemSpecialHandlingNotes itemAttributes="${itemAttributes}" accountPrefix="document.item[${ctr}]." itemColSpan="${colCount}" count="${ctr}"/>
<c:if test="${fn:length(KualiForm.document.items[ctr].copyList) > 1 }">
    <select:oleCopies itemAttributes="${itemAttributes}" accountPrefix="document.item[${ctr}]." isATypeOfRCVGDoc="${isATypeOfRCVGDoc}"  isATypeOfCORRDoc="${isATypeOfCORRDoc}" itemColSpan="${colCount}" count="${ctr}" isFinal="${isFinal}"/>
</c:if>
<select:oleDonor itemAttributes="${itemAttributes}" accountPrefix="document.item[${ctr}]." itemColSpan="${colCount}" count="${ctr}" isFinal="${isFinal}"/>
<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
    </tbody>
</c:if>

</logic:iterate>

</table>
</div>
</kul:tab>
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
    function hideLinkToBibImage() {
        document.getElementById("bibCreateCurrentItemButton").style.display="inline";
        document.getElementById("bibSelectExistingItemButton").style.display="none";
    }
    function hideCreateNewImage() {
        document.getElementById("bibSelectExistingItemButton").style.display="inline";
        document.getElementById("bibCreateCurrentItemButton").style.display="none";
    }

</script>
