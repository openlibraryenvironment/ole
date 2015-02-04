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
<%@ attribute name="itemColSpan" required="true" description="item columns to span"%>
<%@ attribute name="count" required="true" description="item number" %>
<c:set var="isATypeOfPODoc" value="${KualiForm.document.isATypeOfPODoc}" />
<c:set var="isRequisition" value="${KualiForm.document.isReqsDoc}" />
<c:set var="fullEntryMode" value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT] && (empty KualiForm.editingMode['restrictFiscalEntry'])}" />
<c:set var="amendmentEntry"	value="${(not empty KualiForm.editingMode['amendmentEntry'])}" />
<c:set var="lockB2BEntry" value="${(not empty KualiForm.editingMode['lockB2BEntry'])}" />
<c:choose>
    <c:when test="${isRequisition}">
    </c:when>
</c:choose>
<%-- add extra columns count for the "Action" button and/or dual amounts --%>

<c:set var="mainColumnCount" value="12"/>
<c:set var="currentTabIndex" value="${KualiForm.currentTabIndex}" scope="request" />
<c:set var="topLevelTabIndex" value="${KualiForm.currentTabIndex}" scope="request" />
<c:set var="tabTitle" value="CliamHistory-${currentTabIndex}" />
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

<logic:notEmpty name="KualiForm" property="${accountPrefix}claimHistories" >

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
                        Claim History
                    </div>
                </th>
            </tr>

            <c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
                <tr style="display: none;"  id="tab-${tabKey}-div">
            </c:if>

            <th style="padding:0;">
                <table  cellpadding="${mainColumnCount}" cellspacing="0" class="datatable">
                    <tr>
                        <kul:htmlAttributeHeaderCell scope="col">Claim Date</kul:htmlAttributeHeaderCell>
                        <kul:htmlAttributeHeaderCell scope="col">Operator</kul:htmlAttributeHeaderCell>
                        <kul:htmlAttributeHeaderCell scope="col">Claim Count</kul:htmlAttributeHeaderCell>
                        <kul:htmlAttributeHeaderCell scope="col">Claim Response Information</kul:htmlAttributeHeaderCell>
                    </tr>
                    <logic:iterate id="claimHistory" name="KualiForm" property="document.item[${count}].claimHistories" indexId="ctr">
                       <%-- <c:if test="${(empty limitByPoId) or (limitByPoId eq invoiceHistory.purchaseOrderIdentifier)}">--%>
                            <tr>
                                <td align="left" valign="middle" class="datacell">
                                    <c:out value="${claimHistory.claimDate}" />
                                </td>
                                <td align="left" valign="middle" class="datacell">
                                    <c:out value="${claimHistory.operator}" />
                                </td>
                                <td align="left" valign="middle" class="datacell">
                                    <c:out value="${claimHistory.claimCount}" />
                                </td>
                                <td align="left" valign="middle" class="datacell">
                                    <c:out value="${claimHistory.claimResponseInformation}" />
                                </td>
                            </tr>
                      <%--  </c:if>--%>
                    </logic:iterate>
                </table>
            </th>

            <c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
                </tr>
            </c:if>

        </table>
    </td>
</tr>
</logic:notEmpty>
