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

<channel:portalChannelTop channelTitle="Paying" />
<div class="body">

    <strong>INVOICES</strong><br/><br/>
    <portal:portalLink displayTitle="true" title="Create" url='${ConfigProperties.application.url}/ole-kr-krad/OLEInvoice?viewId=OLEInvoiceDocumentView&methodToCall=docHandler&command=initiate&documentClass=org.kuali.ole.krad.transaction.documents.OLEInvoiceDocument&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true' /><br/>
    <portal:portalLink displayTitle="true" title='Search' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_PRQS'/><br/>

    <br/>

    <strong>CREDIT MEMOS</strong><br/><br/>
    <portal:portalLink displayTitle="true" title='Create' url='selectOleVendorCreditMemo.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_CM'/><br/>
    <portal:portalLink displayTitle="true" title='Search' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_CM'/>
    <br/>
    <br/>

    <strong>PAYMENT REQUESTS</strong><br/><br/>
    <portal:portalLink displayTitle="true" title='Search' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_PREQ'/><br/>

</div>
<channel:portalChannelBottom />
