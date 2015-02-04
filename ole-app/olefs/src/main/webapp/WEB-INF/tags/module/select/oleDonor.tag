<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
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
<%@ include file="/jsp/sys/kfsTldHeader.jsp" %>

<%@ attribute name="itemAttributes" required="true" type="java.util.Map"
              description="The DataDictionary entry containing attributes for this row's fields." %>

<%@ attribute name="displayRequisitionFields" required="false"
              description="Boolean to indicate if REQ specific fields should be displayed" %>

<%@ attribute name="isPaymentRequest" required="false"
              description="Boolean to indicate whether that document is PaymentRequest Document" %>
<%@ attribute name="accountPrefix" required="false"
              description="an optional prefix to specify a different location for acocunting lines rather than just on the document." %>
<%@ attribute name="itemColSpan" required="true" description="item columns to span" %>
<%@ attribute name="count" required="true" description="item number" %>
<%@ attribute name="isFinal" required="false" description="Is Final" %>
<c:set var="isATypeOfPODoc" value="${KualiForm.document.isATypeOfPODoc}"/>
<c:set var="isRequisition" value="${KualiForm.document.isReqsDoc}"/>
<c:set var="fullEntryMode"
       value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT] && (empty KualiForm.editingMode['restrictFiscalEntry'])}"/>
<c:set var="amendmentEntry" value="${(not empty KualiForm.editingMode['amendmentEntry'])}"/>
<c:set var="lockB2BEntry" value="${(not empty KualiForm.editingMode['lockB2BEntry'])}"/>
<c:choose>
    <c:when test="${isRequisition}">
    </c:when>
</c:choose>
<%@ attribute name="isATypeOfRCVGDoc" required="false"
              description="Boolean to indicate whether that document is Receiving Document" %>
<%@ attribute name="isATypeOfCORRDoc" required="false"
              description="Boolean to indicate whether that document is Receiving Document" %>
<c:set var="mainColumnCount" value="12"/>
<c:set var="currentTabIndex" value="${KualiForm.currentTabIndex}" scope="request"/>
<c:set var="topLevelTabIndex" value="${KualiForm.currentTabIndex}" scope="request"/>
<c:set var="tabTitle" value="PaymentHistory-${currentTabIndex}"/>
<c:set var="tabKey" value="${kfunc:generateTabKey(tabTitle)}"/>
<!-- hit form method to increment tab index -->
<c:set var="dummyIncrementer" value="${kfunc:incrementTabIndex(KualiForm, tabKey)}"/>
<c:set var="currentTab" value="${kfunc:getTabState(KualiForm, tabKey)}"/>

<%-- default to closed --%>
<c:choose>
    <c:when test="${empty currentTab}">
        <c:set var="isOpen" value="false"/>
        <html:hidden property="tabStates(${tabKey})" value="CLOSE"/>
    </c:when>
    <c:when test="${!empty currentTab}">
        <c:set var="isOpen" value="${currentTab == 'OPEN'}"/>
    </c:when>
</c:choose>

<tr>
    <td colspan="${itemColSpan}">
        <table border="0" cellspacing="0" cellpadding="0" width="100%">
            <tr>
                <th style="padding: 0px; border-right: none;">
                    <div align=left>
                        <c:if test="${isOpen == 'true' || isOpen == 'TRUE'}">
                            <html:image property="methodToCall.toggleTab.tab${tabKey}"
                                        src="${ConfigProperties.kr.externalizable.images.url}tinybutton-hide.gif"
                                        alt="hide" title="toggle" styleClass="tinybutton"
                                        styleId="tab-${tabKey}-imageToggle"
                                        onclick="javascript: return toggleTab(document, '${tabKey}'); "/>
                        </c:if>
                        <c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
                            <html:image property="methodToCall.toggleTab.tab${tabKey}"
                                        src="${ConfigProperties.kr.externalizable.images.url}tinybutton-show.gif"
                                        alt="show" title="toggle" styleClass="tinybutton"
                                        styleId="tab-${tabKey}-imageToggle"
                                        onclick="javascript: return toggleTab(document, '${tabKey}'); "/>
                        </c:if>
                        Donor
                    </div>
                </th>
            </tr>

            <c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
                <tr style="display: none;" id="tab-${tabKey}-div">
            </c:if>
            <th style="padding:0;">
                <table cellpadding="${mainColumnCount}" cellspacing="0" class="datatable">
                    <c:choose>
                        <c:when test="${isFinal || isPaymentRequest}">
                            <tr>
                                <logic:notEmpty name="KualiForm" property="${accountPrefix}oleDonors">
                                    <tr>
                                        <th>
                                            <div align="center"><kul:htmlAttributeLabel
                                                    attributeEntry="${itemAttributes.donorCode}"/></div>
                                        </th>
                                    </tr>
                                    <logic:iterate indexId="ctr" name="KualiForm" property="${accountPrefix}oleDonors"
                                                   id="donors">
                                        <tr>
                                            <td class="infoline">
                                                <div align="center">
                                                    <kul:inquiry
                                                            boClassName="org.kuali.ole.select.bo.OLEDonorLookup"
                                                            keyValues="donorId=${donors.donorId}"
                                                            render="true">
                                                        <kul:htmlControlAttribute
                                                                property="${accountPrefix}oleDonors[${ctr}].donorCode"
                                                                attributeEntry="${itemAttributes.donorCode}"
                                                                readOnly="true"
                                                                tabindexOverride="${tabindexOverrideBase + 0}"/>
                                                    </kul:inquiry>
                                                </div>
                                            </td>
                                        </tr>
                            </tr>
                            </logic:iterate>
                            </logic:notEmpty>
                        </c:when>
                        <c:when test="${fullEntryMode}">
                            <tr>
                                <kul:htmlAttributeLabel attributeEntry="${itemAttributes.donorCode}"/>
                            </tr>
                            <tr>
                                <td>
                                    <kul:htmlControlAttribute
                                            attributeEntry="${itemAttributes.donorCode}"
                                            property="${accountPrefix}donorCode"/>
                                    <kul:lookup
                                            boClassName="org.kuali.ole.select.bo.OLEDonorLookup"
                                            fieldConversions="donorCode:${accountPrefix}donorCode"/>
                                    <html:image property="methodToCall.addDonor.line${count}"
                                                src="${ConfigProperties.kr.externalizable.images.url}tinybutton-add1.gif"
                                                alt="Add" title="Add Donor" styleClass="tinybutton"
                                                tabindex="${tabindexOverrideBase + 0}"/></td>
                                </td>
                            </tr>
                            <tr>
                                <logic:notEmpty name="KualiForm" property="${accountPrefix}oleDonors">
                                    <tr>
                                        <th>
                                            <div align="center"><kul:htmlAttributeLabel
                                                    attributeEntry="${itemAttributes.donorCode}"/></div>
                                        </th>
                                    </tr>
                                    <logic:iterate indexId="ctr" name="KualiForm"
                                                   property="${accountPrefix}oleDonors"
                                                   id="donors">
                                        <tr>
                                            <td class="infoline">
                                                <div align="center">
                                                    <kul:htmlControlAttribute
                                                            property="${accountPrefix}oleDonors[${ctr}].donorCode"
                                                            attributeEntry="${itemAttributes.donorCode}"
                                                            readOnly="${not(fullEntryMode)}"
                                                            tabindexOverride="${tabindexOverrideBase + 0}"/>
                                                    <kul:lookup
                                                            boClassName="org.kuali.ole.select.bo.OLEDonorLookup"
                                                            fieldConversions="donorCode:${accountPrefix}oleDonors[${ctr}].donorCode"/>
                                                    <html:image
                                                            property="methodToCall.deleteDonor.line${count}:${ctr}"
                                                            src="${ConfigProperties.kr.externalizable.images.url}tinybutton-delete1.gif"
                                                            alt="Delete Donor ${ctr+1}"
                                                            title="Delete Donor ${ctr+1}"
                                                            styleClass="tinybutton"/>
                                                </div>
                                            </td>
                                        </tr>
                                    </logic:iterate>
                                </logic:notEmpty>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <logic:notEmpty name="KualiForm" property="${accountPrefix}oleDonors">
                                    <tr>
                                        <th>
                                            <div align="center"><kul:htmlAttributeLabel
                                                    attributeEntry="${itemAttributes.donorCode}"/></div>
                                        </th>
                                    </tr>
                                    <logic:iterate indexId="ctr" name="KualiForm" property="${accountPrefix}oleDonors"
                                                   id="donors">
                                        <tr>
                                            <td class="infoline">
                                                <div align="center">
                                                        <kul:inquiry
                                                                boClassName="org.kuali.ole.select.bo.OLEDonorLookup"
                                                                keyValues="donorId=${donors.donorId}"
                                                                render="true">
                                                                <kul:htmlControlAttribute
                                                                        property="${accountPrefix}oleDonors[${ctr}].donorCode"
                                                                        attributeEntry="${itemAttributes.donorCode}"
                                                                        readOnly="${not(fullEntryMode)}"
                                                                        tabindexOverride="${tabindexOverrideBase + 0}"/>
                                                        </kul:inquiry>
                                            </td>
                                        </tr>
                                    </logic:iterate>
                                </logic:notEmpty>
                        </c:otherwise>
                    </c:choose>
                </table>
            </th>
            </tr>
        </table>
    </td>
</tr>
