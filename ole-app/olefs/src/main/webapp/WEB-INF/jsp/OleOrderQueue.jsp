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
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript">
function setAllOrderHoldQueueResults(checked) {
	for (i = 0; i < KualiForm.elements.length; i++) {
		if (KualiForm.elements[i].type == 'checkbox') {
			KualiForm.elements[i].checked = checked;
		}
	}
}
</script>

<c:set var="orderQueueDocumentAttributes"
	value="${DataDictionary.OleOrderQueueDocument.attributes}" />
  	
<kul:documentPage showDocumentInfo="true"
	htmlFormAction="oleOrderQueue" documentTypeName="OLE_ORDQU"
	renderMultipart="true" showTabButtons="false">
	
	<select:oleOrderQueueSearch
		orderQueueDocumentAttributes="${orderQueueDocumentAttributes}" />
	 
	<c:set var="counter" value="0" />
	<logic:notEmpty name="KualiForm" property="document.requisitionItems">
	<kul:tab tabTitle="Search Results" defaultOpen="true">
			<c:if test="${KualiForm.document.requisitionAdded}">
			
				<div class="tab-container" align=center>
								
					<display:table class="datatable-100" cellpadding="2"
						cellspacing="0" name="${KualiForm.document.requisitionItems}"
						pagesize="100" id="result" excludedParams="*"
						requestURI="oleOrderQueue.do" sort="list" >
						        <display:column title="Select?">
						        	<html:checkbox property="document.requisitionItems[${result_rowNum-1}].itemAdded" />
					            </display:column>
					            <display:column sortable="true" title="Document Number">
							 		<a href="<c:url value="${ConfigProperties.application.url}${KualiForm.url}=${result.requisition.documentNumber}" ></c:url>"
								target="_blank" class="showvisit"> <c:out value="${result.requisition.documentNumber}" /></a>
							    </display:column>
							    <display:column property="requisition.applicationDocumentStatus"
												sortable="true" title="Requisition Status" />
							    <display:column property="dateModified" sortable="true" title="Workflow Status Change Date" />
										    <c:set var="bibEditorUrl" value="${result.requisition.bibeditorViewURL}?docAction=checkOut" />
										    <c:set var="bibUUID" value="${KualiForm.document.requisitionItems[counter].bibUUID}" />
										    <c:set var="publisher" value="${KualiForm.document.requisitionItems[counter].docData.publisher}" />
											<%-- <c:set var="place" value="${ KualiForm.document.requisitionItems[counter].docData.placeOfPublication}"/> --%>
											<!--<c:out value="${ itemTitleId}ee"/>--> 		
											<c:set var="title" value="${result.requisition.items[0].itemDescription}" />
											<c:set var="bibDetails" value="${fn:split(title,',')}"/>
                                            <c:set var="docId" value="${fn:split(bibUUID,'-')}"/>
											<c:set var="listSize"  value="${fn:length(bibDetails)}"/>
											<%-- <display-el:column sortable="true" title="Title" decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator">
													<a href="<c:url value="${bibEditorUrl}&Title_display=${bibDetails[0]}&uuid=${bibUUID}"></c:url>"
														target="_blank" class="showvisit"> <c:out value="${bibDetails[0]}" /> 
														<c:set var="counter" value="${counter+1 }" /></a>
												 </display-el:column> --%>
							    <display:column comparator="org.kuali.ole.select.businessobject.options.OLEOHQComparator" sortable="true" title="Title" decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" >
							    		 <a href="<c:url value="${bibEditorUrl}&Title_display=${bibDetails[0]}&docId=${docId[1]}&uuid=${bibUUID} "></c:url>" target="_blank" class="showvisit">
							    		  <c:out value="${KualiForm.document.requisitionItems[counter].docData.title}"/>
							    		  <c:set var="author" value="${ KualiForm.document.requisitionItems[counter].docData.author}"/>
							    		  <c:set var="counter" value="${counter+1 }"/></a>
							    </display:column>	
							    <%-- <c:if test="${listSize==2 && publisher == null }">
									  <c:set var="author" value="${bibDetails[1]}"/>
									</c:if>
									<c:if test = "${listSize==3}">
									<c:set var="author" value = "${bibDetails[1]}"/>
								</c:if> --%>
								<display:column sortable="true" title="Author" decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator">
									<a href="<c:url value="${bibEditorUrl}&Author_display=${author}&uuid=${bibUUID}"></c:url>"target="_blank" class="showvisit">
										  <c:out value="${author}"/></a>
							    </display:column>
							    <display:column property="docData.publisher" sortable="true"	title="Publisher" 
							    					decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
							    <display:column property="docData.publicationDate" sortable="true" title="Publication Date"
							    					decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />	
							    <display:column property="formatTypeName.formatTypeName"	sortable="true" title="Format"
									    					decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
											<c:set var="accountingLineSize" value="${fn:length(result.sourceAccountingLines)}" />
											<c:if test="${accountingLineSize==0}">
												<c:set var="price" value="${result.itemListPrice}" />
											</c:if>
											<c:if test="${accountingLineSize>0}">
												<c:set var="price" value="${result.sourceAccountingLines[0].amount}" />
											</c:if>
								<display:column sortable="true" title="Price"
									decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator">
									<c:out value="${price}" />
								</display:column>
								<%-- # jira OLE-2363 validation for order holding queue.--%>
								<display:column value="${result.sourceAccountingLines[0].chartOfAccountsCode}"
									sortable="true" title="Chart Code" decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
								<display:column
									value="${result.sourceAccountingLines[0].accountNumber}"
									sortable="true" title="Account"
									decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
								<display:column
									value="${result.sourceAccountingLines[0].financialObjectCode}"
									sortable="true" title="Object Code"
									decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
								<%-- # end of jira OLE-2363 validation for order holding queue.--%>
					</display:table>
				<logic:present name="TOTAL_PRICE">
						<table cellpadding=0 class="datatable"
							summary="Select Requisitions">
						<tr>
								<th colspan="9" align="right">Total Price: $<c:out
										value="${TOTAL_PRICE}" /></th>
						</tr>
					</table>
				</logic:present>
                
                <p>
						<html:image
							src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_selectallfromallpages.png"
							alt="Select all rows from all pages"
							title="Select all rows from all pages" styleClass="tinybutton"
							property="methodToCall.selectAll" value="Select All Rows" />
						<html:image
							src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_deselectallfromallpages.png"
							alt="Deselect all rows from all pages"
							title="Unselect all rows from all pages" styleClass="tinybutton"
							property="methodToCall.unselectAll" value="Unselect All Rows" />
					<script>
						document.write('\n');
							document
									.write('<a href="javascript:void(0)" onclick="setAllOrderHoldQueueResults(true);"><img src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_selectallfromthispage.png" alt="Select all rows from this page" title="Select all rows from this page" class="tinybutton"/></a>');
						document.write('\n');
							document
									.write('<a href="javascript:void(0)" onclick="setAllOrderHoldQueueResults(false);"><img src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_deselectallfromthispage.png" alt="Deselect all rows from this page" title="Deselect all rows from this page" class="tinybutton" onclick="setAllOrderHoldQueueResults(false)"/></a>');
					</script>						
				</p>
				
				</div>
			</c:if>
	</kul:tab>
	<kul:tab tabTitle="Assign To" defaultOpen="true">
					<div class="tab-container" align=center>
					<h3>Assign To</h3>
					<table cellpadding=0 class="datatable" summary="Assign To">
						<tr>
							<kul:htmlAttributeHeaderCell width="50%" literalLabel="User Id" />
							<td>
								<kul:htmlControlAttribute attributeEntry="${orderQueueDocumentAttributes.selectedUserId}" 
									property="document.principalName" readOnly="${true}" />
								<kul:lookup boClassName="org.kuali.rice.kim.impl.identity.PersonImpl" lookupParameters="selectorRoleName:lookupRoleName,selectorRoleNamespace:lookupRoleNamespaceCode"
									fieldConversions="principalId:document.selectedUserId,principalName:document.principalName"/>
							</td>
						</tr>
							
					</table>
					</div>
	</kul:tab>
	<kul:tab tabTitle="Actions" defaultOpen="true">
	<div class="tab-container" align=center>
		<c:set var="extraButtons" value="${KualiForm.extraButtons}"/> 
		<div id="globalbuttons" > 	
			<c:if test="${!empty extraButtons}">
				<c:forEach items="${extraButtons}" var="extraButton">
							<html:image src="${extraButton.extraButtonSource}"
								styleClass="globalbuttons"
								property="${extraButton.extraButtonProperty}"
								title="${extraButton.extraButtonAltText}"
								alt="${extraButton.extraButtonAltText}"
								onclick="${extraButton.extraButtonOnclick}"
								tabindex="${tabindex}" />
				</c:forEach>
			</c:if>
		</div>
	</div>
	</kul:tab>
	</logic:notEmpty>
	 
	
	<kul:panelFooter /> 
</kul:documentPage>

