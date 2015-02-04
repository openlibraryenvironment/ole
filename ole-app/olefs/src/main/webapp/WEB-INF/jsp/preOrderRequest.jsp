<%--
 Copyright 2006-2008 The Kuali Foundation
 
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
<%@ include file="/kr/WEB-INF/jsp/tldHeader.jsp"%>

<c:set var="preOrderRequestAttributes"
	value="${DataDictionary.PreOrderRequestDocument.attributes}" />

<kul:documentPage showDocumentInfo="true"
	htmlFormAction="preOrderRequest"
	documentTypeName="preOrderRequstDocumentType" renderMultipart="true"
	showTabButtons="true" auditCount="0">

	<kul:hiddenDocumentFields />

	<kul:documentOverview editingMode="${KualiForm.editingMode}" />
	<kul:tab tabTitle="PreOrder Request" defaultOpen="true">
		<div class="tab-container" align="center">
		<div class="h2-container">
		<h2>PreOrder Request</h2>
		</div>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="datatable">
			<tr>
				<td colspan="4">
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="datatable">
					<tr>
						<kul:htmlAttributeHeaderCell labelFor="document.title"
							attributeEntry="${preOrderRequestAttributes.title}" align="left" />
						<td><kul:htmlControlAttribute property="document.title"
							attributeEntry="${preOrderRequestAttributes.title}"
							readOnly="${readOnly}" /></td>
					</tr>
					<tr>
						<kul:htmlAttributeHeaderCell labelFor="document.author"
							attributeEntry="${preOrderRequestAttributes.author}" align="left" />
						<td><kul:htmlControlAttribute property="document.author"
							attributeEntry="${preOrderRequestAttributes.author}"
							readOnly="${readOnly}" /></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</div>
	</kul:tab>


	<kul:tab tabTitle="PreOrder Request - Citation Parser"
		defaultOpen="true">
		<div class="tab-container" align="center">
		<div class="h2-container">
		<h2>PreOrder Request</h2>
		</div>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="datatable">
			<tr>
				<td colspan="4">
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="datatable">
					<tr>
						<kul:htmlAttributeHeaderCell labelFor="document.citationString"
							attributeEntry="${preOrderRequestAttributes.citationString}"
							align="left" />
						<td><kul:htmlControlAttribute
							property="document.citationString"
							attributeEntry="${preOrderRequestAttributes.citationString}"
							readOnly="${readOnly}" /></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</div>
	</kul:tab>


	<kul:tab tabTitle="PreOrder Request - Open Url" defaultOpen="true">
		<div class="tab-container" align="center">
		<div class="h2-container">
		<h2>PreOrder Request</h2>
		</div>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="datatable">
			<tr>
				<td colspan="4">
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="datatable">
					<tr>
						<kul:htmlAttributeHeaderCell labelFor="document.openUrl"
							attributeEntry="${preOrderRequestAttributes.openUrl}"
							align="left" />
						<td><kul:htmlControlAttribute property="document.openUrl"
							attributeEntry="${preOrderRequestAttributes.openUrl}"
							readOnly="${readOnly}" /></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</div>
	</kul:tab>

	<kul:notes />
	<kul:adHocRecipients />
	<kul:routeLog />
	<kul:panelFooter />
	<kul:documentControls transactionalDocument="false" />
</kul:documentPage>
