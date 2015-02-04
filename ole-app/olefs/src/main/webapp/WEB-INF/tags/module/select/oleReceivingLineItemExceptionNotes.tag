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
		    	Exception Notes
		    </div>
		</th>
	</tr>
	
	<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
	    <tr style="display: none;"  id="tab-${tabKey}-div">
	</c:if>  
	
          <td style="padding:0;">       
          <table>       
          <c:if test="${fullEntryMode}">
          		<tr>
	                <th>
			            <kul:htmlAttributeLabel attributeEntry="${itemAttributes.exceptionTypeId}" />
			        </th>
		        	<th>
                        <kul:htmlAttributeLabel attributeEntry="${itemAttributes.exceptionNotes}" />
                    </th>
                    <c:if test="${fullEntryMode}"> 
                    <kul:htmlAttributeHeaderCell literalLabel="Action" colspan="${colSpanAction}" />
                    </c:if>
                </tr>      
		        <tr>
			        <td class="infoline">
					    <kul:htmlControlAttribute attributeEntry="${itemAttributes.exceptionTypeId}" property="${accountPrefix}exceptionTypeId" tabindexOverride="${tabindexOverrideBase + 0}"/>
				    </td>
			        <td class="infoline">
					    <kul:htmlControlAttribute attributeEntry="${itemAttributes.exceptionNotes}" property="${accountPrefix}exceptionNotes" tabindexOverride="${tabindexOverrideBase + 0}"/>
				    </td>
				    <td class="infoline" colspan="${colSpanAction}">
					    <div align="center">
					        <html:image property="methodToCall.addExceptionNote.line${count}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-add1.gif" alt="Add" title="Add an Exception Note" styleClass="tinybutton" tabindex="${tabindexOverrideBase + 0}"/>
					    </div>
					</td>	
		        </tr>	     
			</c:if>
			 <logic:notEmpty name="KualiForm" property="${accountPrefix}exceptionNoteList">
			 	<tr>
		         <th>
		            <kul:htmlAttributeLabel attributeEntry="${itemAttributes.exceptionTypeId}" /></div>
		         </th>
		         <th>
                    <kul:htmlAttributeLabel attributeEntry="${itemAttributes.exceptionNotes}" /></div>
                 </th>
                 <c:choose>		    	
					 <c:when test="${fullEntryMode and !amendmentEntry}">			
                          <kul:htmlAttributeHeaderCell literalLabel="Action" colspan="${colSpanAction}" />
                     </c:when>
					 <c:otherwise>							    	
						<div align="center">&nbsp;</div>
					 </c:otherwise>
				 </c:choose>		
                 </tr> 
		     <logic:iterate indexId="ctr" name="KualiForm" property="${accountPrefix}exceptionNoteList" id="itemNotes">
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
					<c:choose>		    	
						<c:when test="${fullEntryMode}">
							<td class="infoline">
							 <kul:htmlControlAttribute
				                property="${accountPrefix}exceptionNoteList[${ctr}].exceptionTypeId"
				                attributeEntry="${itemAttributes.exceptionTypeId}" 
				                tabindexOverride="${tabindexOverrideBase + 0}"/>  </td>				   
							</td>
							
							<td class="infoline">
							    <kul:htmlControlAttribute 
		                        	attributeEntry="${itemAttributes.exceptionNotes}" property="${accountPrefix}exceptionNoteList[${ctr}].exceptionNotes" 
		                        	tabindexOverride="${tabindexOverrideBase + 0}"/>
							</td>
						</c:when>
						<c:otherwise>
							<td class="infoline">
							 <kul:htmlControlAttribute
				                property="${accountPrefix}exceptionNoteList[${ctr}].exceptionTypeId"
				                attributeEntry="${itemAttributes.exceptionTypeId}"  readOnly="true" 
				                tabindexOverride="${tabindexOverrideBase + 0}"/>  </td>				   
							</td>
							
							<td class="infoline">
							    <kul:htmlControlAttribute 
		                        	attributeEntry="${itemAttributes.exceptionNotes}" property="${accountPrefix}exceptionNoteList[${ctr}].exceptionNotes" 
		                        	readOnly="true" tabindexOverride="${tabindexOverrideBase + 0}"/>
							</td>
						</c:otherwise>
					</c:choose>
					<c:if test="${fullEntryMode}"> 
					<td class="infoline">
					    <div align="center">	
					    <c:choose>		    	
					    	<c:when test="${fullEntryMode}">				    
					        	<html:image
						        	property="methodToCall.deleteExceptionNote.line${count}:${ctr}"
						        	src="${ConfigProperties.kr.externalizable.images.url}tinybutton-delete1.gif"
						        	alt="Delete Note ${ctr+1}" title="Delete Note ${ctr+1}"
						        	styleClass="tinybutton" />		
						    </c:when>
						      <c:otherwise>							    	
					    		<div align="center">&nbsp;</div>
						    </c:otherwise>
						    </c:choose>						    			    						    					    
						</div>
					</td>
					</c:if>	
				</tr>
				<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
					</tbody>
				</c:if>
			</c:if>
		</logic:iterate>
		</logic:notEmpty>
    </table>
    </td>
  
	<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
	    </tr>
	</c:if>

</table>
</td>
</tr>
