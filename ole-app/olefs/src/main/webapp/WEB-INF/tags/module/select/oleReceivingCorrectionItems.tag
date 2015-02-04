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

<c:set var="isSaved" value="${KualiForm.document.isSaved}"/>
<c:set var="isATypeOfRCVGDoc" value="${KualiForm.document.isATypeOfRCVGDoc}" />
<c:set var="isATypeOfCORRDoc" value="${KualiForm.document.isATypeOfCORRDoc}" />
<c:set var="fullEntryMode" value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT]}" />
<c:set var="documentType" value="${KualiForm.document.documentHeader.workflowDocument.documentTypeName}" />
<c:set var="tabindexOverrideBase" value="10" />
<c:set var="colCount" value="17"/>
<c:set var="bibeditorEditURL"
	value="${KualiForm.document.bibeditorEditURL}" />
	<c:set var="bibeditorViewURL"
	value="${KualiForm.document.bibeditorViewURL}" />
<c:set var="dublinEditorEditURL"
       value="${KualiForm.document.dublinEditorEditURL}" />
<c:set var="dublinEditorViewURL"
       value="${KualiForm.document.dublinEditorViewURL}" />
<c:set var="docNumber" value="${KualiForm.document.documentNumber }" />
<c:set var="marcXMLFileDirLocation"
	value="${KualiForm.document.marcXMLFileDirLocation}" />
<c:set var="itemNo" value="0" />
<c:set var="isFinal" value="${KualiForm.document.isFinalReqs}" />
<kul:tab tabTitle="Items" defaultOpen="${intialOpen}" tabErrorKey="${PurapConstants.ITEM_TAB_ERRORS}">
	<div class="tab-container" align=center>
	<table cellpadding="0" cellspacing="0" class="datatable" summary="Items Section">
		<tr>
			<td colspan="${colCount}" class="subhead">
			    <span class="subhead-left">Receiving Correction Title</span>
			</td>
		</tr>

		<tr>
			<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemLineNumber}" />
			<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemCatalogNumber}" />
			<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemDescription}" />	
			<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemFormatId}" />			
			<%-- <kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.itemUnitOfMeasureCode}" /> --%>
			<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemOriginalReceivedTotalQuantity}" />
			<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemOriginalReceivedTotalParts}" />
			<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemOriginalReturnedTotalQuantity}" />
			<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemOriginalReturnedTotalParts}" />
			<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemOriginalDamagedTotalQuantity}" />
			<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemOriginalDamagedTotalParts}" />
<%--			<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemReceivedTotalQuantity}" />
			<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemReceivedTotalParts}" />--%>
			<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemReturnedTotalQuantity}" />
			<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemReturnedTotalParts}" />
			<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemDamagedTotalQuantity}" />
			<kul:htmlAttributeHeaderCell attributeEntry="${itemAttributes.oleItemDamagedTotalParts}" />		
		</tr>
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
			<td class="infoline" nowrap="nowrap">
			    <kul:htmlControlAttribute
				    attributeEntry="${itemAttributes.itemLineNumber}"
				    property="document.item[${ctr}].itemLineNumber"
				    extraReadOnlyProperty="document.item[${ctr}].itemLineNumber"
				    readOnly="${true}" />			    
			</td>					
			<td class="infoline">
			    <kul:htmlControlAttribute
				    attributeEntry="${itemAttributes.itemCatalogNumber}"
				    property="document.item[${ctr}].itemCatalogNumber"
				    extraReadOnlyProperty="document.item[${ctr}].itemCatalogNumber"
				    readOnly="${true}" />
			</td>
			<td class="infoline">
				<kul:htmlControlAttribute
				    attributeEntry="${itemAttributes.itemDescription}"
				    property="document.item[${ctr}].itemDescription"
				    readOnly="${true}" />
			</td>
			<td class="infoline">
			    <kul:htmlControlAttribute
				    attributeEntry="${itemAttributes.itemFormatId}"
				    property="document.item[${ctr}].itemFormatId"
				    readOnly="${true}"/>
			</td>
			<%-- <td class="infoline">
			    <kul:htmlControlAttribute
				    attributeEntry="${itemAttributes.itemUnitOfMeasureCode}"
				    property="document.item[${ctr}].itemUnitOfMeasureCode"
				    readOnly="${true}" />
		    </td> --%>
			<td class="infoline">
			    <kul:htmlControlAttribute
				    attributeEntry="${itemAttributes.oleItemOriginalReceivedTotalQuantity}"
				    property="document.item[${ctr}].oleItemOriginalReceivedTotalQuantity"
				    readOnly="${true}" />
			</td>
			<td class="infoline">
			    <kul:htmlControlAttribute
				    attributeEntry="${itemAttributes.oleItemOriginalReceivedTotalParts}"
				    property="document.item[${ctr}].oleItemOriginalReceivedTotalParts"
				    readOnly="${true}"/>
			</td>
			<td class="infoline">
			    <kul:htmlControlAttribute
				    attributeEntry="${itemAttributes.oleItemOriginalReturnedTotalQuantity}"
				    property="document.item[${ctr}].oleItemOriginalReturnedTotalQuantity"
				    readOnly="${true}" />
			</td>
			<td class="infoline">
			    <kul:htmlControlAttribute
				    attributeEntry="${itemAttributes.oleItemOriginalReturnedTotalParts}"
				    property="document.item[${ctr}].oleItemOriginalReturnedTotalParts"
				    readOnly="${true}"/>
			</td>
			<td class="infoline">
			    <kul:htmlControlAttribute
				    attributeEntry="${itemAttributes.oleItemOriginalDamagedTotalQuantity}"
				    property="document.item[${ctr}].oleItemOriginalDamagedTotalQuantity"
				    readOnly="${true}" />
			</td>
			<td class="infoline">
			    <kul:htmlControlAttribute
				    attributeEntry="${itemAttributes.oleItemOriginalDamagedTotalParts}"
				    property="document.item[${ctr}].oleItemOriginalDamagedTotalParts"
				    readOnly="${true}"/>
			</td>
<%--			<td class="infoline">
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
		</tr>

			<tr>
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
                    <c:if test="${docformat eq 'marc'}">
				<input type="button" id="editXMLButton"
					value="Edit"
					onclick="javascript:editXMLContent('${bibeditorEditURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','marc')" />
                    </c:if>
                    <c:if test="${docformat eq 'dublinunq'}">
                        <input type="button" id="editXMLButton"
                               value="Edit"
                               onclick="javascript:editXMLContent('${dublinEditorEditURL}','document.item[${ctr}].bibUUID','${docNumber}','${marcXMLFileDirLocation}','${ctr}','dublinunq')" />
                        </c:if>
                    </c:if>
			<c:if test="${! isSaved}">
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
				</script></td>

                <c:if test="${fn:length(KualiForm.document.items[ctr].copyList) == 1 }">
                    <c:if test="${!isFinal && isATypeOfCORRDoc}">
                        <c:if test="${KualiForm.document.items[ctr].copyList[0].receiptStatus eq 'Received'}">
                            <th>Action:</th>
                            <td class="infoline">
                                <html:image
                                    property="methodToCall.unReceiveCopy.line${ctr}"
                                    src="${ConfigProperties.externalizable.images.url}unreceive.gif"
                                    alt="Un Receive" title="Un Receive"
                                    styleClass="tinybutton" />
                            </td>
                        </c:if>
                    </c:if>
                    <c:if test="${KualiForm.document.items[ctr].copyList[0].receiptStatus eq 'Not Received'}">
                        <th>Receipt Status:</th>
                        <td class="infoline">
                                <kul:htmlControlAttribute
                                    property="document.item[${ctr}].copyList[0].receiptStatus"
                                    attributeEntry="${itemAttributes.receiptStatus}"
                                    tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/>
                        </td>
                    </c:if>
                    <c:if test="${isFinal && isATypeOfCORRDoc && KualiForm.document.items[ctr].copyList[0].receiptStatus eq 'Received'}">
                        <th>Receipt Status:</th>
                        <td class="infoline">
                            <kul:htmlControlAttribute
                                    property="document.item[${ctr}].copyList[0].receiptStatus"
                                    attributeEntry="${itemAttributes.receiptStatus}"
                                    tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/>
                        </td>
                    </c:if>
                </c:if>
			</tr>

		<select:oleReceivingCorrectionItemExceptionNotes itemAttributes="${itemAttributes}" accountPrefix="document.item[${ctr}]." itemColSpan="${colCount}" count="${ctr}"/>
		<select:oleReceivingCorrectionItemReceiptNotes itemAttributes="${itemAttributes}" accountPrefix="document.item[${ctr}]." itemColSpan="${colCount}" count="${ctr}"/>
		<select:oleReceivingCorrectionItemSpecialHandlingNotes itemAttributes="${itemAttributes}" accountPrefix="document.item[${ctr}]." itemColSpan="${colCount}" count="${ctr}"/>
            <c:if test="${fn:length(KualiForm.document.items[ctr].copyList) > 1 }">
            <select:oleCopies itemAttributes="${itemAttributes}" isATypeOfRCVGDoc="${isATypeOfRCVGDoc}" isATypeOfCORRDoc="${isATypeOfCORRDoc}"
											accountPrefix="document.item[${ctr}]."
											itemColSpan="${colCount}" count="${ctr}"
                                            isFinal = "${isFinal}" />
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

</script>