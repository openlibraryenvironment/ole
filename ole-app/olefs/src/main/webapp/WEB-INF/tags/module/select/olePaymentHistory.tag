<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

<%--<%@ attribute name="displayRequisitionFields" required="false" description="Boolean to indicate if REQ specific fields should be displayed"%>--%>

<%--<%@ attribute name="isPaymentRequest" required="false" description="Boolean to indicate whether that document is PaymentRequest Document" %>--%>
<%@ attribute name="accountPrefix" required="false" description="an optional prefix to specify a different location for acocunting lines rather than just on the document."%>
<%@ attribute name="itemColSpan" required="true" description="item columns to span"%>
<%@ attribute name="count" required="true" description="item number" %>
<%--<c:set var="isATypeOfPODoc" value="${KualiForm.document.isATypeOfPODoc}" />--%>
<%--<c:set var="isRequisition" value="${KualiForm.document.isReqsDoc}" />--%>
<c:set var="fullEntryMode" value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT] && (empty KualiForm.editingMode['restrictFiscalEntry'])}" />
<c:set var="amendmentEntry"	value="${(not empty KualiForm.editingMode['amendmentEntry'])}" />
<c:set var="lockB2BEntry" value="${(not empty KualiForm.editingMode['lockB2BEntry'])}" />
<c:set var="itemIdentifier" value="${KualiForm.document.items[count].itemIdentifier}"/>
<c:set var="requisition" value="false"/>
<c:set var="itemTitleId" value="${KualiForm.document.items[count].itemTitleId}"/>

<%--<c:choose>--%>
   <%-- <c:when test="${isATypeOfPODoc}">--%>
        <c:set var="limitByPoId" value="${KualiForm.document.purapDocumentIdentifier}" />
    <%--</c:when>--%>
    <%--<c:when test="${isRequisition}">
        <c:set var="requisition" value="true"/>
    </c:when>--%>
   <%-- <c:otherwise>--%>
       <%-- <c:set var="limitByPoId" value="${KualiForm.document.purchaseOrderIdentifier}" />
    </c:otherwise>
</c:choose>--%>
<%--<c:choose>
    <c:when test="${isRequisition}">
    </c:when>
</c:choose>--%>
    <%-- add extra columns count for the "Action" button and/or dual amounts --%>

	<c:set var="mainColumnCount" value="12"/>
    <c:set var="currentTabIndex" value="${KualiForm.currentTabIndex}" scope="request" />
    <c:set var="topLevelTabIndex" value="${KualiForm.currentTabIndex}" scope="request" />
    <c:set var="tabTitle" value="PaymentHistory-${currentTabIndex}" />
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
<logic:notEmpty name="KualiForm" property="${accountPrefix}invoiceDocuments" >
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
		         Invoice History
		    </div>
		</th>
	</tr>
	
	<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
	    <tr style="display: none;"  id="tab-${tabKey}-div">
	</c:if>

          <th style="padding:0;">
          <table  cellpadding="${mainColumnCount}" cellspacing="0" class="datatable">
              <tr>
                  <kul:htmlAttributeHeaderCell scope="col">INV #</kul:htmlAttributeHeaderCell>
                  <kul:htmlAttributeHeaderCell scope="col">Invoice Date</kul:htmlAttributeHeaderCell>
                  <kul:htmlAttributeHeaderCell scope="col">INV Status</kul:htmlAttributeHeaderCell>
                  <kul:htmlAttributeHeaderCell scope="col">Amount</kul:htmlAttributeHeaderCell>
                  <kul:htmlAttributeHeaderCell scope="col">Start Date</kul:htmlAttributeHeaderCell>
                  <kul:htmlAttributeHeaderCell scope="col">End Date</kul:htmlAttributeHeaderCell>
                  <kul:htmlAttributeHeaderCell scope="col">PREQ /CM #</kul:htmlAttributeHeaderCell>
              </tr>
              <logic:iterate id="invoiceHistory" name="KualiForm" property="document.item[${count}].invoiceDocuments" indexId="ctr">
                  <c:if test="${(not empty limitByPoId) or (limitByPoId eq invoiceHistory.purchaseOrderIdentifier)}">
                      <tr>
                          <td align="left" valign="middle" class="datacell">
                              <a href="<c:url value="${ConfigProperties.workflow.url}/DocHandler.do?docId=${invoiceHistory.documentNumber}&command=displayDocSearchView" ></c:url>"
                                 target="_blank" class="showvisit">
                                  <c:if test="${not empty invoiceHistory.invoiceNumber}">
                                  <c:out value="${invoiceHistory.invoiceNumber}" />
                                  </c:if>
                                  <c:if test="${empty invoiceHistory.invoiceNumber}">
                                      <c:out value="${invoiceHistory.documentNumber}" />
                                  </c:if>
                              </a>
                          </td>
                          <td align="left" valign="middle" class="datacell">
                              <%--<c:out value="${invoiceHistory.invoiceDate}" />--%>
                              <fmt:formatDate pattern="${RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE}" value="${invoiceHistory.invoiceDate}" />
                          </td>
                          <td align="left" valign="middle" class="datacell">
                              <c:out value="${invoiceHistory.applicationDocumentStatus}" />
                          </td>
                          <td align="left" valign="middle" class="datacell">
                             <%-- <c:if test="${requisition eq 'true'}">
                                  <logic:iterate id="invoiceItem" name="invoiceHistory" property="items" indexId="itemctr">
                                      <c:if test="${((not empty itemIdentifier) and (not empty item.requisitionItemIdentifier) and item.itemType.itemTypeCode eq 'ITEM') and (itemIdentifier eq item.requisitionItemIdentifier)}">
                                      <c:if test="${invoiceItem.debitItem}">
                                          <c:out value="${invoiceItem.extendedPrice}" />
                                      </c:if>
                                      <c:if test="${!invoiceItem.debitItem}">
                                      <c:out value="${invoiceItem.extendedPrice * -1}" />
                                  </logic:iterate>
                              </c:if>
                              <c:if test="${requisition eq 'false'}">--%>
                                  <logic:iterate id="invoiceItem" name="invoiceHistory" property="items" indexId="itemctr">
                                      <c:if test="${((not empty itemIdentifier) and (not empty invoiceItem.poItemIdentifier) and invoiceItem.itemType.itemTypeCode eq 'ITEM') and itemTitleId eq invoiceItem.itemTitleId}">
                                          <c:if test="${invoiceItem.debitItem}">
                                          <c:out value="${invoiceItem.extendedPrice}" />
                                          </c:if>
										  <c:if test="${!invoiceItem.debitItem}">
                                          <c:out value="(${invoiceItem.extendedPrice})" />
                                          </c:if>

                                      </c:if>
                                  </logic:iterate>
                              <%--</c:if>--%>
                          <td align="left" valign="middle" class="datacell">
                          <logic:iterate id="invoiceItem" name="invoiceHistory" property="items" indexId="itemctr">
                              <c:if test="${((not empty itemIdentifier) and (not empty invoiceItem.poItemIdentifier) and invoiceItem.itemType.itemTypeCode eq 'ITEM') and itemTitleId eq invoiceItem.itemTitleId}">

                              <c:out value="${invoiceItem.subscriptionFromDate}" />
                                  </td>
                          <td>
                              <c:out value="${invoiceItem.subscriptionToDate}" />
                          </td>
                              </c:if>
                          </logic:iterate>
                          </td>

                          </td>
                            <td align="left" valign="middle" class="datacell">
                               <%-- <c:if test="${invoiceItem.debitItem}">--%>
                                    <logic:iterate id="paymentHistory" name="invoiceHistory" property="paymentRequestDocuments" indexId="paymentctr">
                                        <a href="<c:url value="${ConfigProperties.workflow.url}/DocHandler.do?docId=${paymentHistory.documentNumber}&command=displayDocSearchView" ></c:url>"
                                        target="_blank" class="showvisit"> <c:out value="${paymentHistory.documentNumber}" />
                                        </a>
                                    </logic:iterate>
                               <%-- </c:if>
                                <c:if test="${!invoiceItem.debitItem}">
                                    <logic:iterate id="creditMemoHistory" name="invoiceHistory" property="creditMemoDocuments" indexId="creditmemoctr">
                                        <a href="<c:url value="${ConfigProperties.workflow.url}/DocHandler.do?docId=${creditMemoHistory.documentNumber}&command=displayDocSearchView" ></c:url>"
                                        target="_blank" class="showvisit"> <c:out value="${creditMemoHistory.documentNumber}" />
                                        </a>
                                    </logic:iterate>
                                </c:if>--%>
                            </td> <%--
                            <td align="left" valign="middle" class="datacell">
                                <c:out value="${invoiceHistory.vendorCustomerNumber}" />
                            </td>
                            <td align="left" valign="middle" class="datacell">
                                <c:out value="${invoiceHistory.totalAmount}" />
                            </td>--%>
                      </tr>
                  </c:if>
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



<%--
<logic:empty name="KualiForm" property="document.relatedViews.paymentHistoryInvoiceViews">
    <h4>No Invoices</h4>
</logic:empty>
--%>

