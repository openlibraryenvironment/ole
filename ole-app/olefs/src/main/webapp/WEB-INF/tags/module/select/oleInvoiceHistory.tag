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

<%@ attribute name="isInvoice" required="false" description="Boolean to indicate whether that document is Invoice Document" %>
<%@ attribute name="accountPrefix" required="false" description="an optional prefix to specify a different location for acocunting lines rather than just on the document."%>
<%@ attribute name="itemColSpan" required="true" description="item columns to span"%>
<%@ attribute name="count" required="true" description="item number" %>
<c:set var="fullEntryMode" value="${KualiForm.documentActions[Constants.KUALI_ACTION_CAN_EDIT] && (empty KualiForm.editingMode['restrictFiscalEntry'])}" />
<c:set var="amendmentEntry"	value="${(not empty KualiForm.editingMode['amendmentEntry'])}" />
<c:set var="lockB2BEntry" value="${(not empty KualiForm.editingMode['lockB2BEntry'])}" />
    
    <%-- add extra columns count for the "Action" button and/or dual amounts --%>

	<c:set var="mainColumnCount" value="12"/>
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
		         Payment History
		    </div>
		</th>
	</tr>
	
	<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
	    <tr style="display: none;"  id="tab-${tabKey}-div">
	</c:if>  
	
          <th style="padding:0;">       
          <table  cellpadding="${mainColumnCount}" cellspacing="0" class="datatable"> 
          <tr>   
        	<th>
			            <kul:htmlAttributeLabel attributeEntry="${itemAttributes.paymentHistory}" />
			         </th>
			    
		         <th>
            <%--         <kul:htmlAttributeLabel attributeEntry="${itemAttributes.partEnumeration}" />
                 </th>
                 <th>
                    <kul:htmlAttributeLabel attributeEntry="${itemAttributes.locationCopies}" />
                 </th>
                  <th>
                    <kul:htmlAttributeLabel attributeEntry="${itemAttributes.startingCopyNumber}" />
                 </th>
                 <kul:htmlAttributeHeaderCell literalLabel="Action" colspan="${colSpanAction}" />
                 </tr>
                 <tr>
					<td class="infoline" width="25%">
				<div align="right">
				<div align="center"><kul:htmlControlAttribute
					attributeEntry="${itemAttributes.parts}"
					property="${accountPrefix}parts"
					tabindexOverride="${tabindexOverrideBase + 0}" /></div>
				</td>
				
					<td class="infoline" width="25%">
				<div align="right">
				<div align="center"><kul:htmlControlAttribute
					attributeEntry="${itemAttributes.partEnumeration}"
					property="${accountPrefix}partEnumeration"
					tabindexOverride="${tabindexOverrideBase + 0}" /></div>
				</td>
				
					<td class="infoline" width="25%">
				<div align="right">
				<div align="center"><kul:htmlControlAttribute
					attributeEntry="${itemAttributes.locationCopies}"
					property="${accountPrefix}locationCopies"
					tabindexOverride="${tabindexOverrideBase + 0}" /></div>
				</td>
				
				<td class="infoline" width="25%">
				<div align="right">
				<div align="center"><kul:htmlControlAttribute
					attributeEntry="${itemAttributes.startingCopyNumber}"
					property="${accountPrefix}startingCopyNumber"
					tabindexOverride="${tabindexOverrideBase + 0}" /></div>
				</td> --%>
				 <td class="infoline" rowspan="2" colspan="${colSpanAction}">
				    <div align="center">
				        <html:image property="methodToCall.addPaymentHistory.line${count}" src="${ConfigProperties.kr.externalizable.images.url}tinybutton-add1.gif" alt="Add" title="Add a Payment History" styleClass="tinybutton" tabindex="${tabindexOverrideBase + 0}"/>
				    </div>
				  </td>	
				</tr>
				  <tr>
		       <logic:notEmpty name="KualiForm" property="${accountPrefix}requisitionPaymentHistory" >
		         <tr>
		        
		         	<th>
			            <kul:htmlAttributeLabel attributeEntry="${itemAttributes.paymentHistory}" />
			         </th>
			    
		        <%--  <th>
                    <kul:htmlAttributeLabel attributeEntry="${itemAttributes.partEnumeration}" />
                 </th>
                 <th>
                    <kul:htmlAttributeLabel attributeEntry="${itemAttributes.locationCopies}" />
                 </th>
                  <th>
                    <kul:htmlAttributeLabel attributeEntry="${itemAttributes.startingCopyNumber}" />
                 </th> --%>
                 <c:choose>		    	
					 <c:when test="${fullEntryMode and !amendmentEntry}">			
                          <kul:htmlAttributeHeaderCell literalLabel="Action" colspan="${colSpanAction}" />
                     </c:when>
					 <c:otherwise>							    	
						<div align="center">&nbsp;</div>
					 </c:otherwise>
				 </c:choose>		
                 </tr> 
                <logic:iterate indexId="ctr" name="KualiForm" property="${accountPrefix}requisitionPaymentHistory" id="itemCopies">
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
					 
					      Payment History ${ctr+1}
					 
					</td>
				</tr>
				<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
					<tbody style="display: none;" id="tab-${tabKey}-div">
				</c:if>
				<!-- table class="datatable" style="width: 100%;" -->
				<tr>
				   
				    	<%-- <td class="infoline">
						    <kul:htmlControlAttribute
			               		 property="${accountPrefix}copies[${ctr}].parts"
	    	                     attributeEntry="${itemAttributes.parts}"		              
		    	 	             readOnly="${not(fullEntryMode)}" 
		        	             tabindexOverride="${tabindexOverrideBase + 0}"/>  </td>				   
						 </td>
				   
					 <td class="infoline">
					    <kul:htmlControlAttribute 
                        	attributeEntry="${itemAttributes.partEnumeration}" property="${accountPrefix}copies[${ctr}].partEnumeration" 
                        	readOnly="${not(fullEntryMode)}" tabindexOverride="${tabindexOverrideBase + 0}"/>
					 </td> 
					 <td class="infoline">
						    <kul:htmlControlAttribute
			               		 property="${accountPrefix}copies[${ctr}].locationCopies"
	    	                     attributeEntry="${itemAttributes.locationCopies}"		              
		    	 	             readOnly="${not(fullEntryMode)}" 
		        	             tabindexOverride="${tabindexOverrideBase + 0}"/>  </td>				   
						 </td>
						 <td class="infoline">
						    <kul:htmlControlAttribute
			               		 property="${accountPrefix}copies[${ctr}].startingCopyNumber"
	    	                     attributeEntry="${itemAttributes.parts}"		              
		    	 	             readOnly="${not(fullEntryMode)}" 
		        	             tabindexOverride="${tabindexOverrideBase + 0}"/>  </td>				   
						 </td> --%>
				  <c:choose>		    	
				    	<c:when test="${fullEntryMode}">	   
				   			<td class="infoline">
							    <div align="center">						    			    
					        		<html:image
						        		property="methodToCall.deletePaymentHistory.line${count}:${ctr}"
						        		src="${ConfigProperties.kr.externalizable.images.url}tinybutton-delete1.gif"
							        	alt="Delete Note ${ctr+1}" title="Delete Copy ${ctr+1}"
							        	styleClass="tinybutton" />	
							 </td>
						 </c:when>
						 <c:otherwise>						    	
					    		<div align="center">&nbsp;</div>
						 </c:otherwise>
					</c:choose>	
						<c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
					</tbody>
				</c:if>
		        </logic:iterate>
		        </logic:notEmpty>	
				</tr>	
          </table>
          </th>
  
	     <c:if test="${isOpen != 'true' && isOpen != 'TRUE'}">
	         </tr>
	     </c:if>

     </table>
     </td>
     </tr>



