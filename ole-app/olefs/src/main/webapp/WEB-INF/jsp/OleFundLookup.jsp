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
	value="${DataDictionary.OleFundLookupDocument.attributes}" />
<c:set var="routeHeaderLabel" value="routeHeaderId"/>
<c:set var="routeLog" value="routeLog"/>
<kul:documentPage showDocumentInfo="true"
	htmlFormAction="oleFundLookup" documentTypeName="OLE_FLU"
	renderMultipart="true" showTabButtons="false">
	<select:oleFundLookup
		documentAttributes="${documentAttributes}" />
	
	<logic:notEmpty name="KualiForm" property="document.finalResults">
	<kul:tab tabTitle="Search Results" defaultOpen="true">
	<div class="tab-container" align=center>
		
		<display:table class="datatable-100" cellspacing="2"
			requestURI="oleFundLookup.do" cellpadding="0"
			name="${KualiForm.document.finalResults}" id="row"  pagesize="100" sort="list" defaultsort="1"> 
			<display:column
							value="${row.chartOfAccountsCode}"
							sortable="true" title="Chart Code"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
			<display:column
							value="${row.accountNumber}"
							sortable="true" title="Account Number"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
			<display:column
							value="${row.accountName}"
							sortable="true" title="Account Name"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
			<display:column
							value="${row.organizationCode}"
							sortable="true" title="Organization Code"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
			<display:column
							value="${row.objectCode}"
							sortable="true" title="Object Code"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
			<%-- <display-el:column
							value="${row.universityFiscalYear}"
							sortable="true" title="Fiscal Year"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" /> --%>
		 <%-- 	<display-el:column
							value="${row.accountName}"
							sortable="true" title="Account Name"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" /> --%>
			<display:column
							value="${row.cashBalance}"
							sortable="true" title="Cash balance"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
			<display:column
							value="${row.freeBalance}"
							sortable="true" title="Free Balance"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" /> 
			<display:column
							value="${row.intialBudgetAllocation}"
							sortable="true" title="Intial Budget Allocation"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" /> 
		<%-- 	<display-el:column
							value="${row.carryOverBalance}"
							sortable="true" title="Carry Over Balance"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" /> 
			<display-el:column
							value="${row.transfered}"
							sortable="true" title="Transfered"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />  --%>
			<display:column
							value="${row.netAllocation}"
							sortable="true" title="Net Allocation"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
			<display:column
							value="${row.encumbrances}"
							sortable="true" title="Encumbrances"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
			<display:column
							value="${row.sumPaidInvoice}"
							sortable="true" title="Sum Paid Invoices"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
			<display:column
							value="${row.sumUnpaidInvoice}"
							sortable="true" title="Sum Unpaid Invoices"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
			<display:column
							value="${row.expendedPercentage}"
							sortable="true" title="% Expended"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
		   <display:column
							value="${row.expenEncumPercentage}"
							sortable="true" title="% Expended &  Encumbered"
							decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
 		</display:table>
 		</div>
		</kul:tab>				
		</logic:notEmpty> 
	<kul:panelFooter /> 
</kul:documentPage>
