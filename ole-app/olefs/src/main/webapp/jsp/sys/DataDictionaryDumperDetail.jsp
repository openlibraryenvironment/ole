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
<kul:page headerTitle="${KualiForm.title}" transactionalDocument="false"
	showDocumentInfo="false" htmlFormAction="dataDictionaryDumper"
	docTitle="${KualiForm.title}">
	
	
<style type="text/css">
   .highlightrow {}
   tr.highlightrow:hover, tr.over td { background-color: #66FFFF; }
</style>
	<%--
<style>
TABLE {
	border: 1px solid black;
	padding: 0; margin: 0;
	border-collapse: collapse;
	empty-cells: show; font-size: 10pt;
	font-family: sans-serif;
	background: white;
} 
BODY {
	background: white;
} 
TD, TH { 
	border: 1px solid black;
	padding: 0;
	margin: 0;
}
</style>
</head>
<body>
 --%>
	<%--
${KualiForm.sections}
<c:forEach var="detailSectionTemp" items="${KualiForm.sections}" >
<c:set var="detailSection" value="${detailSectionTemp}" />
${detailSectionTemp["docName"]}
</c:forEach>
--%>
	<c:forEach var="detailSection" items="${KualiForm.sections}">

		<table align="center" cellpadding="0" cellspacing="0"
			class="datatable-100">
			<tbody>
				<tr>
					<th style="text-align: right;" class="infocell">Doc Name:&nbsp;</th>
					<td class="infocell">${detailSection.docName}</td>
				</tr>
				<tr>
					<th style="text-align: right;" class="infocell">Document ID:&nbsp;</th>
					<td class="infocell"><table width="100%" style="border: none;">
							<td>${detailSection.docId }</td>
							<th style="text-align: right;">DB Table:&nbsp;</th>
							<td>${detailSection.table}</td>
						</table>
					</td>
				</tr>
				<tr>
					<th class="infocell" style="text-align: right;">Object Class:&nbsp;</th>
					<td class="infocell">${detailSection.businessObject }</td>
				</tr>
				<c:if test="${not empty detailSection.businessObjectRulesClass }">
					<tr>
						<th class="infocell" colspan="2">&nbsp;</th>
					</tr>
					<tr>
						<th class="infocell" style="text-align: right;">Business Rules Class:&nbsp;</th>
						<td class="infocell">${detailSection.businessObjectRulesClass }</td>
					</tr>
					<tr>
						<th class="infocell" style="text-align: right;">Document Authorizer
							Class:&nbsp;</th>
						<td class="infocell">${detailSection.documentAuthorizerClass }</td>
					</tr>
				</c:if>
			</tbody>
		</table>
		<br />
		<br />
		<c:set var="rows" value="${detailSection.documentRows}" scope="request" />
		  <display:table excludedParams="*" class="datatable-100" cellspacing="0" cellpadding="0" name="rows" id="result" 
		  	decorator="org.kuali.rice.kns.web.format.KualiTableDecorator">
		  	<display:setProperty name="basic.msg.empty_list">No Properties</display:setProperty>
		  	
		  	<display:column class="infocell" sortable="false" title="Name" ><c:out value="${result.name}"/>&nbsp;</display:column>
		  	<display:column class="infocell" sortable="false" title="DB Column" ><c:out value="${result.column}"/>&nbsp;</display:column>
		  	<display:column class="infocell" sortable="false" title="Required?" ><c:out value="${result.required}"/>&nbsp;</display:column>
		  	<display:column class="infocell" sortable="false" title="Field Type" ><c:out value="${result.fieldType}"/>&nbsp;</display:column>
		  	<c:if test="${!detailSection.collection}">
			  	<display:column class="infocell" sortable="false" title="Def. Value" ><c:out value="${result.defaultValue}"/>&nbsp;</display:column>
			  	<display:column class="infocell" sortable="false" title="Read Only?" ><c:out value="${result.readOnly}"/>&nbsp;</display:column>
		  	</c:if>
		  	<display:column class="infocell" sortable="false" title="Valid. Rules" ><c:out value="${result.validationRules}"/>&nbsp;</display:column>
		  	<display:column class="infocell" sortable="false" title="Max Length" ><c:out value="${result.maxLength}"/>&nbsp;</display:column>
		  	<display:column class="infocell" sortable="false" title="Exist. Check" ><c:out value="${result.existenceCheck}"/>&nbsp;</display:column>
		  	<display:column class="infocell" sortable="false" title="Lookup Param?" ><c:out value="${result.lookupParam}"/>&nbsp;</display:column>
		  	<display:column class="infocell" sortable="false" title="Lookup Result?" ><c:out value="${result.lookupParam}"/>&nbsp;</display:column>
		  	<display:column class="infocell" sortable="false" title="On Inquiry?" ><c:out value="${result.lookupParam}"/>&nbsp;</display:column>
		  	<display:column class="infocell" sortable="false" title="Control Def." ><c:out value="${result.lookupParam}"/>&nbsp;</display:column>
		  </display:table>
	</c:forEach>
	
</kul:page>