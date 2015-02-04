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
<channel:portalChannelTop channelTitle="Acquisitions" />
<div class="body">
<strong>Purchasing/Accounts Payable</strong><br /><br/>
  
        <portal:olePortalLink grey="true" displayTitle="false" title="Bulk Receiving" url="purapBulkReceiving.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_RCVB" hiddenTitle="true"/><br/> 
        <portal:olePortalLink grey="true" displayTitle="false" title="Claiming" url="" hiddenTitle="true"/><br/>
        <%-- <portal:olePortalLink green="true" displayTitle="true" title="Contract Manager Assignment" url="purapContractManagerAssignment.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ACM" /><br/> --%>
 		<portal:olePortalLink green="true" displayTitle="true" title="Payment Request" url="selectOlePaymentRequest.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_PREQ" /><br/>
		<%--<portal:olePortalLink grey="true" displayTitle="false" title="Invoice" url="selectOleInvoice.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_PPRQ" hiddenTitle="true" /><br/>--%>
        <portal:olePortalLink green="true" displayTitle="true" title="Invoice" url="${ConfigProperties.application.url}/ole-kr-krad/OLEInvoice?viewId=OLEInvoiceDocumentView&methodToCall=docHandler&command=initiate&documentClass=org.kuali.ole.krad.transaction.documents.OLEInvoiceDocument"/><br/>
		<portal:olePortalLink green="true" displayTitle="true" title="Receiving" url="selectOleLineItemReceiving.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_RCVL" /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Requisition" url="purapOleRequisition.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_REQS" /><br/>
        <portal:olePortalLink grey="true" displayTitle="false" title="Shop Catalogs" url="b2b.do?methodToCall=shopCatalogs" hiddenTitle="true"/><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Vendor Credit Memo" url="selectOleVendorCreditMemo.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_CM"/><br/><br/>


</div>
<channel:portalChannelBottom />

