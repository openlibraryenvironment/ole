<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
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


<%@ attribute name="itemAttributes" required="true" type="java.util.Map"
              description="The DataDictionary entry containing attributes for this row's fields."%>

<%@ attribute name="count" required="true" description="item number" %>

<%@ attribute name="accountPrefix" required="false" description="an optional prefix to specify a different location for acocunting lines rather than just on the document."%>
<%@ attribute name="itemColSpan" required="true" description="item columns to span"%>

    <c:if test="${!accountingLineScriptsLoaded}">
        <script language="JavaScript" type="text/javascript" src="scripts/sys/objectInfo.js"></script>
        <c:set var="accountingLineScriptsLoaded" value="true" scope="request" />
    </c:if>
    
    <%-- add extra columns count for the "Action" button and/or dual amounts --%>

    <c:set var="currentTabIndex" value="${KualiForm.currentTabIndex}" scope="request" />
    <c:set var="topLevelTabIndex" value="${KualiForm.currentTabIndex}" scope="request" />
    <c:set var="tabTitle" value="AccountingLines-${currentTabIndex}" />
    <c:set var="tabKey" value="${kfunc:generateTabKey(tabTitle)}"/>
    <!--  hit form method to increment tab index -->
    <c:set var="dummyIncrementer" value="${kfunc:incrementTabIndex(KualiForm, tabKey)}" />
    <c:set var="currentTab" value="${kfunc:getTabState(KualiForm, tabKey)}"/>


<c:set var="fullEntryMode"
       value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT] && (empty KualiForm.editingMode['restrictFiscalEntry'])}" />

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
		    	Accounting Lines
		    </div>
		</th>
	</tr>

	<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
	    <tr style="display: none;"  id="tab-${tabKey}-div">
	</c:if>
        <th style="padding:0;">
            <div align=left>
                <c:choose>
                    <c:when test="${fullEntryMode}">
                        <kul:htmlAttributeLabel attributeEntry="${itemAttributes.fundCode}"/>

                        <kul:htmlControlAttribute
                                attributeEntry="${itemAttributes.fundCode}"
                                property="${accountPrefix}fundCode"/>
                        <kul:lookup
                                boClassName="org.kuali.ole.coa.businessobject.OleFundCode"
                                fieldConversions="fundCode:${accountPrefix}fundCode"/>
                        <html:image property="methodToCall.populateAccountingLines.line${count}"
                                    src="${ConfigProperties.kr.externalizable.images.url}tinybutton-add1.gif"
                                    alt="Add" title="Populate Accounting Lines" styleClass="tinybutton"
                                    tabindex="${tabindexOverrideBase + 0}"/></td>


                    </c:when>
                </c:choose>
            </div>
            <sys-java:accountingLines>
			    <sys-java:accountingLineGroup newLinePropertyName="${accountPrefix}newSourceLine" collectionPropertyName="${accountPrefix}sourceAccountingLines" collectionItemPropertyName="${accountPrefix}sourceAccountingLine" attributeGroupName="source" />
		    </sys-java:accountingLines>
        </th>
    
	<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
	    </tr>
	</c:if>

</table>
</td>
</tr>



