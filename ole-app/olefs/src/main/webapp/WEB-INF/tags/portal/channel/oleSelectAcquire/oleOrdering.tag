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

<channel:portalChannelTop channelTitle="Ordering" />
<div class="body">

    <strong>REQUISITIONS</strong><br/><br/>
    <portal:portalLink displayTitle="true" title="Create" url="purapOleRequisition.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_REQS" /><br/>
    <portal:portalLink displayTitle="true" title='Search' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_REQS'/>
    <br/>
    <br/>

    <strong>PURCHASE ORDERS</strong><br/>
    <br/>

    <portal:portalLink displayTitle="true" title='Search' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_PO'/><br/><br/>

    <br/>
    <portal:portalLink displayTitle="true" title='Order Holding Queue' url="oleOrderQueue.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ORDQU"/><br/><br/>
</div>
<channel:portalChannelBottom />
