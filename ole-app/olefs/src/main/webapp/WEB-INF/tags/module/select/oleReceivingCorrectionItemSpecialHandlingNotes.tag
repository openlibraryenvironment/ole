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

<%@ attribute name="itemAttributes" required="true" type="java.util.Map" description="The DataDictionary entry containing attributes for this row's fields." %>  
<%@ attribute name="accountPrefix" required="false" description="an optional prefix to specify a different location for acocunting lines rather than just on the document."%>
<%@ attribute name="itemColSpan" required="true" description="item columns to span"%>
<%@ attribute name="count" required="true" description="item number" %>
<c:set var="fullEntryMode" value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT] && (empty KualiForm.editingMode['restrictFiscalEntry'])}" />
<c:set var="amendmentEntry"	value="${(not empty KualiForm.editingMode['amendmentEntry'])}" />
<c:set var="lockB2BEntry" value="${(not empty KualiForm.editingMode['lockB2BEntry'])}" />
    
    
    <%-- add extra columns count for the "Action" button and/or dual amounts --%>

    <c:set var="currentTabIndex" value="${KualiForm.currentTabIndex}" scope="request" />
    <c:set var="topLevelTabIndex" value="${KualiForm.currentTabIndex}" scope="request" />
    <c:set var="tabTitle" value="Notes-${currentTabIndex}" />
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
		    	Special Processing Instructions
		    </div>
		</th>
	</tr>
	
	<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
	    <tr style="display: none;"  id="tab-${tabKey}-div">
	</c:if>  
	
          <td style="padding:0;">       
          <table>    
          		<tr>
                    <kul:htmlAttributeHeaderCell literalLabel="Special Processing Instructions" />
                    <c:if test="${fullEntryMode}"> 
                    <kul:htmlAttributeHeaderCell literalLabel="Acknowledge" colspan="${colSpanAction}" />
                    </c:if>
                </tr>  
        
			 <logic:present name="KualiForm" property="${accountPrefix}correctionSpecialHandlingNoteList">
		     <logic:iterate indexId="ctr" name="KualiForm" property="${accountPrefix}correctionSpecialHandlingNoteList" id="itemNotes">
				<c:if test="true">				
				<c:choose>	
 				    <c:when test="${itemNotes.objectId == null}">
 				        <c:set var="newObjectId" value="<%= (new org.kuali.rice.kns.util.Guid()).toString()%>" />
                        <c:set var="tabKey" value="Note-${newObjectId}" />
				    </c:when>
				    <c:when test="${itemLine.objectId != null}">
				        <c:set var="tabKey" value="Note-${itemNotes.objectId}" />
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
					<td colspan="${itemColSpan}" class="subhead" style="border-right: none;">
					    Note ${ctr+1}
					</td>
				</tr>
				<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
					<tbody style="display: none;" id="tab-${tabKey}-div">
				</c:if>
				<tr>
					
					<td class="infoline">
					    <kul:htmlControlAttribute 
                        	attributeEntry="${itemAttributes.notes}" property="${accountPrefix}correctionSpecialHandlingNoteList[${ctr}].notes" 
                        	readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}"/>
					</td>
				<c:if test="${fullEntryMode}"> 
				<td align=left valign=middle class="datacell">
          				  <kul:htmlControlAttribute
               			 property="${accountPrefix}correctionSpecialHandlingNoteList[${ctr}].notesAck"
               			 attributeEntry="${itemAttributes.notesAck}"
               			 readOnly="${false}"
                tabindexOverride="${tabindexOverrideBase + 0}"/>
        		</td>
        		</c:if>					
			</tr>
				<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
					</tbody>
				</c:if>
			</c:if>
		</logic:iterate>
        </logic:present>
    </table>
    </td>
  
	<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
	    </tr>
	</c:if>

</table>
</td>
</tr>
