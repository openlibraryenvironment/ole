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

<c:set var="documentAttributes"
	value="${DataDictionary.OleAcquisitionsSearchDocument.attributes}" />
<c:set var="routeHeaderLabel" value="routeHeaderId"/>
<c:set var="routeLog" value="routeLog"/>
<kul:documentPage showDocumentInfo="true"
	htmlFormAction="oleAcquisitionsSearch" documentTypeName="OLE_ACQS"
	renderMultipart="true" showTabButtons="false">
	<select:oleAcquisitionsSearch
		documentAttributes="${documentAttributes}" />
	
	<logic:notEmpty name="KualiForm" property="document.acqSearchResults">
	<kul:tab tabTitle="Search Results" defaultOpen="true">
	<div class="tab-container" align=center>
		<%-- <display:table class="datatable-100" cellpadding="2"
						cellspacing="0" name="${KualiForm.document.requisitionItems}"
						pagesize="100" id="result" excludedParams="*"
						requestURI="oleOrderQueue.do" sort="list" defaultsort="2"> --%>
		<display:table class="datatable-100" cellspacing="2"
			requestURI="oleAcquisitionsSearch.do" cellpadding="0" excludedParams="*"
			name="${KualiForm.document.acqSearchResults}" id="row"  pagesize="100" sort="list" >
			<c:set var="routeHeaderId" value=""/>
					<display:column sortable="true" title="Document Number">
					<a href="<c:url value="${ConfigProperties.application.url}${KualiForm.url}=${row.organizationDocumentNumber}" ></c:url>"
								target="_blank" class="showvisit"> <c:out
									value="${row.organizationDocumentNumber}" />
							</a>
					</display:column>	
					<c:if test="${KualiForm.document.searchType}">
						<display:column sortable="true" title="PO#" property="purapDocumentIdentifier"/>
					</c:if>											
					<display:column sortable="true" title="Document Type" property="financialDocumentTypeCode" />
					<c:if test="${!KualiForm.document.searchType}">
						<display:column sortable="true" title="Document Title" property="documentDescription"/>
						<display:column sortable="true" title="Route Status" property="applicationDocumentStatus"/>
						<display:column sortable="true" title="Initiator" property="initiator"/>
						<display:column sortable="true" title="Date Created" property="dateCreated"/>
						<display:column title="Route Log">
							<a href="${ConfigProperties.application.url}/kew/RouteLog.do?documentId=${row.organizationDocumentNumber}" target="_self" >
							<img alt="Route Log for Document" src="${ConfigProperties.application.url}/kr/images/my_route_log.gif"></a>
						</display:column>					
					</c:if>
					<c:if test="${KualiForm.document.searchType}">
						<display:column sortable="true" title="Title Id" property="itemTitleId"/>
						<display:column sortable="true" title="(Bib)Title" property="title"/>
						<display:column sortable="true" title="Author" property="author"/>
						<display:column sortable="true" title="Publisher" property="publisher"/>
						<display:column sortable="true" title="Isbn" property="isbn"/>
						<display:column sortable="true" title="Local ID" property="localIdentifier"/>
					</c:if>
		</display:table>
		</div>
		</kul:tab>
		</logic:notEmpty>
	<kul:panelFooter /> 
</kul:documentPage>
