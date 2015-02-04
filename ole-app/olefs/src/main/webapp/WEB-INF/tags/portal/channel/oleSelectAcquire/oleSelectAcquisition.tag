
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

<channel:portalChannelTop channelTitle="Acquisition" />
<div class="body">
    <portal:olePortalLink green="true" displayTitle="true" title='Search Payment Requests' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_PREQ'/><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Payment Request" url="selectOlePaymentRequest.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_PREQ" /><br/>
        <%--<portal:olePortalLink green="true" displayTitle="true" title='Search Receiving' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_RCV'/><br/>--%>
<%--        <portal:olePortalLink green="true" displayTitle="true" title="Invoice" url="${ConfigProperties.application.url}/ole-kr-krad/OLEInvoice?viewId=OLEInvoiceDocumentView&methodToCall=docHandler&command=initiate&documentClass=org.kuali.ole.krad.transaction.documents.OLEInvoiceDocument"/><br/>--%>
       <%-- <portal:olePortalLink green="true" displayTitle="true" title="Receiving" url="selectOleLineItemReceiving.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_RCVL" /><br/>--%>
        <%--<portal:olePortalLink green="true" displayTitle="true" title="Requisition" url="purapOleRequisition.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_REQS" /><br/>--%>
        <%--<portal:olePortalLink green="true" displayTitle="true" title='Order Holding Queue' url="oleOrderQueue.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ORDQU"/><br/>--%>
        <%--<portal:olePortalLink green="true" displayTitle="true" title='Purchase Orders' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_PO'/><br/>--%>
        <%--<portal:olePortalLink green="true" displayTitle="true" title='Receiving Queue' url="oleReceivingQueueSearch.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_QUEUESEARCH"/><br/>--%>
        <%--<portal:olePortalLink green="true" displayTitle="true" title='Requisitions' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_REQS'/><br/>--%>
        <%--<portal:olePortalLink green="true" displayTitle="true" title='Search Vendor Credit Memos' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_CM' /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title="Create Vendor Credit Memo" url="selectOleVendorCreditMemo.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_CM"/><br/><br/>--%>
        <portal:olePortalLink yellow="true" displayTitle="true" title="General Error Correction" url="oleFinancialGeneralErrorCorrection.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_GEC" /><br/>
        <portal:olePortalLink yellow="true" displayTitle="true" title="Disbursement Voucher" url="oleFinancialDisbursementVoucher.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_DV" /><br/>
        <portal:olePortalLink displayTitle="true" green="true" title="E-Resource" url="${ConfigProperties.application.url}/ole-kr-krad/oleERSController?viewId=OLEEResourceRecordView&methodToCall=docHandler&command=initiate&documentClass=org.kuali.ole.select.document.OLEEResourceRecordDocument"/><br/>
        <portal:olePortalLink displayTitle="true" green="true" title="E-Resource Search" url="${ConfigProperties.application.url}/ole-kr-krad/searchEResourceController?viewId=OLEEResourceSearchView&methodToCall=start"/><br/><br/>
        <%--<portal:portalLink displayTitle="true" title="Serials Receiving Record Search"
                           url="${ConfigProperties.application.url}/ole-kr-krad/serialsReceivingRecordController?viewId=SerialsReceivingRecordSearchView&methodToCall=start"/> <br/>--%>

</div>
<channel:portalChannelBottom />

