<%--
 Copyright 2007 The Kuali Foundation
 
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

<channel:portalChannelTop channelTitle="Search" />
<div class="body">

	<portal:olePortalLink grey="true" displayTitle="false" title="Financial Transactions" url="${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OpenLibraryEnvironmentTransactionalDocument" hiddenTitle="true"/><br/><br/>
	
	<strong>Accounts Receivable</strong><br/><br/>
	
        <portal:olePortalLink grey="true" displayTitle="false" title='Customer Invoices' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_INV' hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title='Customer Credit Memos' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_CRM' hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title='Customer Invoice Writeoffs' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_INVW' hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title='Cash Controls' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_CTRL' hiddenTitle="true"/><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title='Payment Applications' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_APP' hiddenTitle="true"/><br/><br/>
    
   	<strong>Contracts & Grants</strong><br/><br/>
	
		<portal:olePortalLink grey="true" displayTitle="false"
                              title='Proposals' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_PRPL' hiddenTitle="true"/><br/><br/>
	
	<strong>Financial Processing</strong><br/><br/>
	
		<portal:olePortalLink grey="true" displayTitle="false" title="Disbursement Vouchers" url="${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_DV" hiddenTitle="true"/><br/><br/>
	
</div>
<channel:portalChannelBottom />
