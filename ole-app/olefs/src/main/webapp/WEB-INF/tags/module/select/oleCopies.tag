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

<%@ attribute name="displayRequisitionFields" required="false" description="Boolean to indicate if REQ specific fields should be displayed"%>

<%@ attribute name="isPaymentRequest" required="false" description="Boolean to indicate whether that document is PaymentRequest Document" %>
<%@ attribute name="accountPrefix" required="false" description="an optional prefix to specify a different location for acocunting lines rather than just on the document."%>
<%@ attribute name="isATypeOfRCVGDoc" required="false" description="Boolean to indicate whether that document is Receiving Document"%>
<%@ attribute name="isATypeOfCORRDoc" required="false" description="Boolean to indicate whether that document is Receiving Document"%>
<%@ attribute name="itemColSpan" required="true" description="item columns to span"%>
<%@ attribute name="quantity" required="false" description="item quantity"%>
<%@ attribute name="previousItemQuantity" required="false" description="previous Item Quantity"%>
<%@ attribute name="noOfParts" required="false" description="item no of parts"%>
<%@ attribute name="previousItemNoOfParts" required="false" description="previous Item No of Parts"%>
<%@ attribute name="count" required="true" description="item number" %>
<%@ attribute name="isFinal" required="true" description="Is Final" %>
<c:set var="fullEntryMode" value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT] && (empty KualiForm.editingMode['restrictFiscalEntry'])}" />
<c:set var="amendmentEntry"	value="${(not empty KualiForm.editingMode['amendmentEntry'])}" />
<c:set var="lockB2BEntry" value="${(not empty KualiForm.editingMode['lockB2BEntry'])}" />


<%-- add extra columns count for the "Action" button and/or dual amounts --%>

<c:set var="mainColumnCount" value="12"/>
<c:set var="currentTabIndex" value="${KualiForm.currentTabIndex}" scope="request" />
<c:set var="topLevelTabIndex" value="${KualiForm.currentTabIndex}" scope="request" />
<c:set var="tabTitle" value="Copies-${currentTabIndex}" />
<c:set var="tabKey" value="${kfunc:generateTabKey(tabTitle)}"/>
<!--  hit form method to increment tab index -->
<c:set var="dummyIncrementer" value="${kfunc:incrementTabIndex(KualiForm, tabKey)}" />
<c:set var="currentTab" value="${kfunc:getTabState(KualiForm, tabKey)}"/>

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
            Copies
        </div>
    </th>
</tr>

<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
<tr style="display: none;"  id="tab-${tabKey}-div">
    </c:if>

    <th style="padding:0;">
        <table  cellpadding="${mainColumnCount}" cellspacing="0" class="datatable">
            <c:choose>
            <c:when test="${fullEntryMode && !isATypeOfRCVGDoc && !isATypeOfCORRDoc && !amendmentEntry}">
            <tr>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.locationCopies}" /></div>
                </th>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.itemCopies}" /></div>
                </th>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.parts}" /></div>
                </th>

                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.startingCopyNumber}" /></div>
                </th>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.caption}" /></div>
                </th>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.volumeNumber}" /></div>
                </th>
                <kul:htmlAttributeHeaderCell literalLabel="Action" colspan="${colSpanAction}" />
            </tr>
            <tr>
                <td class="infoline" width="25%">

                    <div align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.locationCopies}"
                            property="${accountPrefix}locationCopies"
                            tabindexOverride="${tabindexOverrideBase + 0}" /></div>
                </td>
                <td class="infoline" width="25%">
                    <div id="${accountPrefix}itemCopies" align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.itemCopies}"
                            property="${accountPrefix}itemCopies"
                            tabindexOverride="${tabindexOverrideBase + 0}" /></div>
                </td>
                <td class="infoline" width="25%">

                    <div id="${accountPrefix}parts" align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.parts}"
                            property="${accountPrefix}parts"
                            tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/></div>
                </td>

                <td class="infoline" width="25%">

                    <div align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.startingCopyNumber}"
                            property="${accountPrefix}startingCopyNumber"
                            tabindexOverride="${tabindexOverrideBase + 0}"/></div>
                </td>
                <td class="infoline" width="25%">

                    <div align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.caption}"
                            property="${accountPrefix}caption"
                            tabindexOverride="${tabindexOverrideBase + 0}"/></div>
                </td>
                <td class="infoline" width="25%">

                    <div align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.volumeNumber}"
                            property="${accountPrefix}volumeNumber"
                            tabindexOverride="${tabindexOverrideBase + 0}"/></div>
                </td>
                <td class="infoline" rowspan="2" colspan="${colSpanAction}">
                    <div align="center">
                        <html:image property="methodToCall.addCopy.line${count}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-add1.gif" alt="Add" title="Add a copies" styleClass="tinybutton" tabindex="${tabindexOverrideBase + 0}"/>
                    </div>
                </td>
            </tr>
            </c:when>
            <c:otherwise>
            <c:choose>
            <c:when test="${(amendmentEntry && (quantity != previousItemQuantity && noOfParts != previousItemNoOfParts))}">
            <tr>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.locationCopies}" /></div>
                </th>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.itemCopies}" /></div>
                </th>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.parts}" /></div>
                </th>

                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.startingCopyNumber}" /></div>
                </th>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.caption}" /></div>
                </th>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.volumeNumber}" /></div>
                </th>
                <kul:htmlAttributeHeaderCell literalLabel="Action" colspan="${colSpanAction}" />
            </tr>
            <tr>
                <td class="infoline" width="25%">

                    <div align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.locationCopies}"
                            property="${accountPrefix}locationCopies"
                            tabindexOverride="${tabindexOverrideBase + 0}" /></div>
                </td>
                <td class="infoline" width="25%">
                    <div id="${accountPrefix}itemCopies" align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.itemCopies}"
                            property="${accountPrefix}itemCopies"
                            tabindexOverride="${tabindexOverrideBase + 0}" /></div>
                </td>
                <td class="infoline" width="25%">

                    <div id="${accountPrefix}parts" align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.parts}"
                            property="${accountPrefix}parts"
                            tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/></div>
                </td>

                <td class="infoline" width="25%">

                    <div align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.startingCopyNumber}"
                            property="${accountPrefix}startingCopyNumber"
                            tabindexOverride="${tabindexOverrideBase + 0}"/></div>
                </td>
                <td class="infoline" width="25%">

                    <div align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.caption}"
                            property="${accountPrefix}caption"
                            tabindexOverride="${tabindexOverrideBase + 0}"/></div>
                </td>
                <td class="infoline" width="25%">

                    <div align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.volumeNumber}"
                            property="${accountPrefix}volumeNumber"
                            tabindexOverride="${tabindexOverrideBase + 0}"/></div>
                </td>
                <td class="infoline" rowspan="2" colspan="${colSpanAction}">
                    <div align="center">
                        <html:image property="methodToCall.addCopy.line${count}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-add1.gif" alt="Add" title="Add a copies" styleClass="tinybutton" tabindex="${tabindexOverrideBase + 0}"/>
                    </div>
                </td>
            </tr>
            </c:when>
            <c:when test="${(amendmentEntry && (quantity != previousItemQuantity && noOfParts == previousItemNoOfParts))}">
            <tr>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.locationCopies}" /></div>
                </th>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.itemCopies}" /></div>
                </th>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.parts}" /></div>
                </th>

                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.startingCopyNumber}" /></div>
                </th>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.caption}" /></div>
                </th>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.volumeNumber}" /></div>
                </th>
                <kul:htmlAttributeHeaderCell literalLabel="Action" colspan="${colSpanAction}" />
            </tr>
            <tr>
                <td class="infoline" width="25%">

                    <div align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.locationCopies}"
                            property="${accountPrefix}locationCopies"
                            tabindexOverride="${tabindexOverrideBase + 0}" /></div>
                </td>
                <td class="infoline" width="25%">
                    <div id="${accountPrefix}itemCopies" align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.itemCopies}"
                            property="${accountPrefix}itemCopies"
                            tabindexOverride="${tabindexOverrideBase + 0}" /></div>
                </td>
                <td class="infoline" width="25%">

                    <div id="${accountPrefix}parts" align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.parts}"
                            property="${accountPrefix}parts"
                            tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/></div>
                </td>

                <td class="infoline" width="25%">

                    <div align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.startingCopyNumber}"
                            property="${accountPrefix}startingCopyNumber"
                            tabindexOverride="${tabindexOverrideBase + 0}"/></div>
                </td>
                <td class="infoline" width="25%">

                    <div align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.caption}"
                            property="${accountPrefix}caption"
                            tabindexOverride="${tabindexOverrideBase + 0}"/></div>
                </td>
                <td class="infoline" width="25%">

                    <div align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.volumeNumber}"
                            property="${accountPrefix}volumeNumber"
                            tabindexOverride="${tabindexOverrideBase + 0}"/></div>
                </td>
                <td class="infoline" rowspan="2" colspan="${colSpanAction}">
                    <div align="center">
                        <html:image property="methodToCall.addCopy.line${count}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-add1.gif" alt="Add" title="Add a copies" styleClass="tinybutton" tabindex="${tabindexOverrideBase + 0}"/>
                    </div>
                </td>
            </tr>
            </c:when>
            <c:when test="${(amendmentEntry && (quantity == previousItemQuantity && noOfParts != previousItemNoOfParts))}">
            <tr>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.locationCopies}" /></div>
                </th>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.itemCopies}" /></div>
                </th>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.parts}" /></div>
                </th>

                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.startingCopyNumber}" /></div>
                </th>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.caption}" /></div>
                </th>
                <th>
                    <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.volumeNumber}" /></div>
                </th>
                <kul:htmlAttributeHeaderCell literalLabel="Action" colspan="${colSpanAction}" />
            </tr>
            <tr>
                <td class="infoline" width="25%">

                    <div align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.locationCopies}"
                            property="${accountPrefix}locationCopies"
                            tabindexOverride="${tabindexOverrideBase + 0}" /></div>
                </td>
                <td class="infoline" width="25%">
                    <div id="${accountPrefix}itemCopies" align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.itemCopies}"
                            property="${accountPrefix}itemCopies"
                            tabindexOverride="${tabindexOverrideBase + 0}" /></div>
                </td>
                <td class="infoline" width="25%">

                    <div id="${accountPrefix}parts" align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.parts}"
                            property="${accountPrefix}parts"
                            tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/></div>
                </td>

                <td class="infoline" width="25%">

                    <div align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.startingCopyNumber}"
                            property="${accountPrefix}startingCopyNumber"
                            tabindexOverride="${tabindexOverrideBase + 0}"/></div>
                </td>
                <td class="infoline" width="25%">

                    <div align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.caption}"
                            property="${accountPrefix}caption"
                            tabindexOverride="${tabindexOverrideBase + 0}"/></div>
                </td>
                <td class="infoline" width="25%">

                    <div align="center"><kul:htmlControlAttribute
                            attributeEntry="${itemAttributes.volumeNumber}"
                            property="${accountPrefix}volumeNumber"
                            tabindexOverride="${tabindexOverrideBase + 0}"/></div>
                </td>
                <td class="infoline" rowspan="2" colspan="${colSpanAction}">
                    <div align="center">
                        <html:image property="methodToCall.addCopy.line${count}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-add1.gif" alt="Add" title="Add a copies" styleClass="tinybutton" tabindex="${tabindexOverrideBase + 0}"/>
                    </div>
                </td>
            </tr>
            </c:when>
            </c:choose>
            </c:otherwise>
            </c:choose>
            <c:choose>
            <c:when test="${isFinal || isATypeOfRCVGDoc || isATypeOfCORRDoc}">
            <tr>
                <logic:notEmpty name="KualiForm" property="${accountPrefix}copyList" >
            <tr>
            <th>
                <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.enumeration}" /></div>
            </th>
            <th>
                <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.location}" /></div>
            </th>
            <th>
                <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.copyNumber}" /></div>
            </th>

            <th>
                <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.receiptStatus}" /></div>
            </th>
            <c:if test="${!isFinal  && (isATypeOfRCVGDoc || isATypeOfCORRDoc)}">
    </th>
    <kul:htmlAttributeHeaderCell literalLabel="Action" colspan="${colSpanAction}" />
</tr>
</c:if>
</tr>
<logic:iterate indexId="ctr" name="KualiForm" property="${accountPrefix}copyList" id="itemCopies">
    <c:choose>
        <c:when test="${itemNotes.objectId == null}">
            <c:set var="newObjectId" value="<%= (new org.kuali.rice.kns.util.Guid()).toString()%>" />
            <c:set var="tabKey" value="Copy-${newObjectId}" />
        </c:when>
        <c:when test="${itemLine.objectId != null}">
            <c:set var="tabKey" value="Copy-${itemNotes.objectId}" />
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
        <td colspan="${mainColumnCount}" class="subhead" style="border-right: none;">

            Copies ${ctr+1}

        </td>
    </tr>
    <c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
        <tbody style="display: none;" id="tab-${tabKey}-div">
    </c:if>
    <!-- table class="datatable" style="width: 100%;" -->
    <tr>
    <td class="infoline">
        <div align="center" >
            <kul:htmlControlAttribute
                    property="${accountPrefix}copyList[${ctr}].enumeration"
                    attributeEntry="${itemAttributes.enumeration}"
                    readOnly="true"
                    tabindexOverride="${tabindexOverrideBase + 0}"/> </div>
    </td>
    <td class="infoline">
        <div align="center">
            <kul:htmlControlAttribute
                    property="${accountPrefix}copyList[${ctr}].location"
                    attributeEntry="${itemAttributes.location}"
                    readOnly="true"
                    tabindexOverride="${tabindexOverrideBase + 0}"/> </div>
    </td>

    <td class="infoline">
        <div align="center">
            <kul:htmlControlAttribute
                    property="${accountPrefix}copyList[${ctr}].copyNumber"
                    attributeEntry="${itemAttributes.copyNumber}"
                    tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/></div>
    </td>

    <td class="infoline">
        <div align="center" >
            <kul:htmlControlAttribute
                    property="${accountPrefix}copyList[${ctr}].receiptStatus"
                    attributeEntry="${itemAttributes.receiptStatus}"
                    tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/>  </div>
    </td>
    <c:if test="${!isFinal && isATypeOfRCVGDoc && itemCopies.receiptStatus ne 'Received'}">
        <td class="infoline">
            <div  align="center">
                <html:image
                        property="methodToCall.receiveCopy.line${count}:${ctr}"
                        src="${ConfigProperties.externalizable.images.url}receive.gif"
                        alt="Receive" title="Receive"
                        styleClass="tinybutton"  />
            </div>
        </td>
    </c:if>
    <c:if test="${!isFinal && isATypeOfRCVGDoc && itemCopies.receiptStatus eq 'Received'}">
        <td class="infoline">
        </td>
    </c:if>
    <c:if test="${!isFinal && isATypeOfCORRDoc  && itemCopies.receiptStatus eq 'Received'}">
        <td class="infoline">
            <div align="center">
                <html:image
                        property="methodToCall.unReceiveCopy.line${count}:${ctr}"
                        src="${ConfigProperties.externalizable.images.url}unreceive.gif"
                        alt="Un Receive" title="Un Receive"
                        styleClass="tinybutton" />
            </div>
        </td>
    </c:if>
    <c:if test="${!isFinal && isATypeOfCORRDoc && itemCopies.receiptStatus ne 'Received'}">
        <td class="infoline">
        </td>
    </c:if>
    <c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
        </tbody>
    </c:if>
</logic:iterate>
</logic:notEmpty>
</c:when>
<c:otherwise>
<c:if test="${amendmentEntry && (quantity == previousItemQuantity && noOfParts == previousItemNoOfParts)}">
    <tr>
    <logic:notEmpty name="KualiForm" property="${accountPrefix}copyList" >
        <tr>
        <th>
            <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.enumeration}" /></div>
        </th>
        <th>
            <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.location}" /></div>
        </th>
        <th>
            <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.copyNumber}" /></div>
        </th>

        <th>
            <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.receiptStatus}" /></div>
        </th>
        <c:if test="${!isFinal  && (isATypeOfRCVGDoc || isATypeOfCORRDoc)}">
            </th>
            <kul:htmlAttributeHeaderCell literalLabel="Action" colspan="${colSpanAction}" />
            </tr>
        </c:if>
        </tr>
        <logic:iterate indexId="ctr" name="KualiForm" property="${accountPrefix}copyList" id="itemCopies">
            <c:choose>
                <c:when test="${itemNotes.objectId == null}">
                    <c:set var="newObjectId" value="<%= (new org.kuali.rice.kns.util.Guid()).toString()%>" />
                    <c:set var="tabKey" value="Copy-${newObjectId}" />
                </c:when>
                <c:when test="${itemLine.objectId != null}">
                    <c:set var="tabKey" value="Copy-${itemNotes.objectId}" />
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
                <td colspan="${mainColumnCount}" class="subhead" style="border-right: none;">

                    Copies ${ctr+1}

                </td>
            </tr>
            <c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
                <tbody style="display: none;" id="tab-${tabKey}-div">
            </c:if>
            <!-- table class="datatable" style="width: 100%;" -->
            <tr>
            <td class="infoline">
                <div align="center" >
                    <kul:htmlControlAttribute
                            property="${accountPrefix}copyList[${ctr}].enumeration"
                            attributeEntry="${itemAttributes.enumeration}"
                            readOnly="true"
                            tabindexOverride="${tabindexOverrideBase + 0}"/> </div>
            </td>
            <td class="infoline">
                <div align="center">
                    <kul:htmlControlAttribute
                            property="${accountPrefix}copyList[${ctr}].location"
                            attributeEntry="${itemAttributes.location}"
                            readOnly="true"
                            tabindexOverride="${tabindexOverrideBase + 0}"/> </div>
            </td>

            <td class="infoline">
                <div align="center">
                    <kul:htmlControlAttribute
                            property="${accountPrefix}copyList[${ctr}].copyNumber"
                            attributeEntry="${itemAttributes.copyNumber}"
                            tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/></div>
            </td>

            <td class="infoline">
                <div align="center" >
                    <kul:htmlControlAttribute
                            property="${accountPrefix}copyList[${ctr}].receiptStatus"
                            attributeEntry="${itemAttributes.receiptStatus}"
                            tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/>  </div>
            </td>
            <c:if test="${!isFinal && isATypeOfRCVGDoc && itemCopies.receiptStatus ne 'Received'}">
                <td class="infoline">
                    <div  align="center">
                        <html:image
                                property="methodToCall.receiveCopy.line${count}:${ctr}"
                                src="${ConfigProperties.externalizable.images.url}receive.gif"
                                alt="Receive" title="Receive"
                                styleClass="tinybutton"  />
                    </div>
                </td>
            </c:if>
            <c:if test="${!isFinal && isATypeOfRCVGDoc && itemCopies.receiptStatus eq 'Received'}">
                <td class="infoline">
                </td>
            </c:if>
            <c:if test="${!isFinal && isATypeOfCORRDoc  && itemCopies.receiptStatus eq 'Received'}">
                <td class="infoline">
                    <div align="center">
                        <html:image
                                property="methodToCall.unReceiveCopy.line${count}:${ctr}"
                                src="${ConfigProperties.externalizable.images.url}unreceive.gif"
                                alt="Un Receive" title="Un Receive"
                                styleClass="tinybutton" />
                    </div>
                </td>
            </c:if>
            <c:if test="${!isFinal && isATypeOfCORRDoc && itemCopies.receiptStatus ne 'Received'}">
                <td class="infoline">
                </td>
            </c:if>
            <c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
                </tbody>
            </c:if>
        </logic:iterate>
    </logic:notEmpty>
</c:if>
<c:if test="${!amendmentEntry || (quantity != previousItemQuantity) || (noOfParts != previousItemNoOfParts)}">
    <tr>
    <logic:notEmpty name="KualiForm" property="${accountPrefix}copies" >
        <tr>
            <th>
                <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.locationCopies}" /></div>
            </th>
            <th>
                <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.itemCopies}" /></div>
            </th>
            <th>
                <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.parts}" /></div>
            </th>

            <th>
                <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.startingCopyNumber}" /></div>
            </th>
            <th>
                <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.caption}" /></div>
            </th>
            <c:choose>
                <c:when test="${fullEntryMode and !amendmentEntry}">
                    <th>
                        <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.volumeNumber}" /></div>
                    </th>
                    <kul:htmlAttributeHeaderCell literalLabel="Action" colspan="${colSpanAction}" />
                </c:when>
                <c:otherwise>
                    <th>

                        <div align="center"><kul:htmlAttributeLabel attributeEntry="${itemAttributes.volumeNumber}" /></div>
                    </th>
                </c:otherwise>
            </c:choose>
        </tr>
        <logic:iterate indexId="ctr" name="KualiForm" property="${accountPrefix}copies" id="itemCopies">
            <c:choose>
                <c:when test="${itemNotes.objectId == null}">
                    <c:set var="newObjectId" value="<%= (new org.kuali.rice.kns.util.Guid()).toString()%>" />
                    <c:set var="tabKey" value="Copy-${newObjectId}" />
                </c:when>
                <c:when test="${itemLine.objectId != null}">
                    <c:set var="tabKey" value="Copy-${itemNotes.objectId}" />
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
                <td colspan="${mainColumnCount}" class="subhead" style="border-right: none;">

                    Copies ${ctr+1}

                </td>
            </tr>
            <c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
                <tbody style="display: none;" id="tab-${tabKey}-div">
            </c:if>
            <!-- table class="datatable" style="width: 100%;" -->
            <tr>
            <td class="infoline">
                <div align="center" >
                    <kul:htmlControlAttribute
                            property="${accountPrefix}copies[${ctr}].locationCopies"
                            attributeEntry="${itemAttributes.locationCopies}"
                            readOnly="${not(fullEntryMode)}"
                            tabindexOverride="${tabindexOverrideBase + 0}"/> </div>
            </td>
            <td class="infoline">
                <div id="${accountPrefix}copies[${ctr}].itemCopies" align="center">
                    <kul:htmlControlAttribute
                            property="${accountPrefix}copies[${ctr}].itemCopies"
                            attributeEntry="${itemAttributes.itemCopies}"
                            readOnly="true"
                            tabindexOverride="${tabindexOverrideBase + 0}"/> </div>
            </td>

            <td class="infoline">
                <div id="${accountPrefix}copies[${ctr}].parts" align="center">
                    <kul:htmlControlAttribute
                            property="${accountPrefix}copies[${ctr}].parts"
                            attributeEntry="${itemAttributes.parts}"
                            tabindexOverride="${tabindexOverrideBase + 0}" readOnly="true"/></div>
            </td>

            <td class="infoline">
                <div align="center" />
                <kul:htmlControlAttribute
                        property="${accountPrefix}copies[${ctr}].startingCopyNumber"
                        attributeEntry="${itemAttributes.startingCopyNumber}"
                        tabindexOverride="${tabindexOverrideBase + 0}" />  </div>
            </td>
            <td class="infoline">
                <div align="center">
                    <kul:htmlControlAttribute
                            property="${accountPrefix}copies[${ctr}].caption"
                            attributeEntry="${itemAttributes.caption}"
                            tabindexOverride="${tabindexOverrideBase + 0}" />  </div>
            </td>

            <c:choose>
                <c:when test="${fullEntryMode}">
                    <td class="infoline">
                        <div align="center">
                            <kul:htmlControlAttribute
                                    property="${accountPrefix}copies[${ctr}].volumeNumber"
                                    attributeEntry="${itemAttributes.volumeNumber}"
                                    tabindexOverride="${tabindexOverrideBase + 0}" />  </div>
                    </td>
                    <c:if test="${not isATypeOfRCVGDoc && not isATypeOfCORRDoc}">
                        <td class="infoline">
                            <div align="center">
                                    <html:image
                                            property="methodToCall.deleteCopy.line${count}:${ctr}"
                                            src="${ConfigProperties.kr.externalizable.images.url}tinybutton-delete1.gif"
                                            alt="Delete Note ${ctr+1}" title="Delete Copy ${ctr+1}"
                                            styleClass="tinybutton" />
                        </td>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <td class="infoline">
                        <div align="center">
                            <kul:htmlControlAttribute
                                    property="${accountPrefix}copies[${ctr}].volumeNumber"
                                    attributeEntry="${itemAttributes.volumeNumber}"
                                    tabindexOverride="${tabindexOverrideBase + 0}" />  </div>
                    </td>
                </c:otherwise>
            </c:choose>
            <c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
                </tbody>
            </c:if>
        </logic:iterate>
    </logic:notEmpty>
</c:if>
</c:otherwise>
</c:choose>


</tr>
</table>
</td>
</th>

<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
    </tr>
</c:if>

</table>
</td>
</tr>


