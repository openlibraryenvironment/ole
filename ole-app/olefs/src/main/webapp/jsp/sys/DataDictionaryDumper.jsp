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
<kul:page headerTitle="Data Dictionary Entries"
	transactionalDocument="false" showDocumentInfo="false"
	htmlFormAction="dataDictionaryDumper"
	docTitle="Data Dictionary Entries">
	
	<kul:tabTop tabTitle="Maintenance Document Entries" defaultOpen="true">
		<div class="tab-container" style="width: auto;">
			<table>
				<c:forEach var="documentEntry" items="${KualiForm.maintenanceDocumentEntries}">
					<tr>
						<td><a href="dataDictionaryDumperDetail.do?type=document&jstlKey=${documentEntry.jstlKey}&entryClass=${documentEntry.entryClass.name}&docClass=${documentEntry.documentClass.name}&title=${documentEntry.entryClass.simpleName}+Maintenance+Document">${documentEntry.entryClass.simpleName} Maintenance Document</a></td>
						<td>${documentEntry.entryClass.name}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</kul:tabTop>
	<kul:tab tabTitle="Transactional Document Entries" defaultOpen="true">
		<div class="tab-container" style="width: auto;">
			<table>
				<c:forEach var="documentEntry" items="${KualiForm.transactionalDocumentEntries}">
					<tr>
						<td><a href="dataDictionaryDumperDetail.do?type=document&jstlKey=${documentEntry.jstlKey}&entryClass=${documentEntry.entryClass.name}&docClass=${documentEntry.documentClass.name}&title=${documentEntry.documentClass.simpleName}">${documentEntry.documentClass.simpleName} (${documentEntry.documentTypeName})</a></td>
						<td>${documentEntry.entryClass.name}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</kul:tab>
	<kul:panelFooter />


</kul:page>
