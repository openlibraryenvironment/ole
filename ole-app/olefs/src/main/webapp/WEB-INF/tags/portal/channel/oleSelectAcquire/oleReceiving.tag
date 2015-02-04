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

<channel:portalChannelTop channelTitle="Receiving" />
<div class="body">

    <strong>MONOGRAPH RECEIVING</strong><br/><br/>
    <portal:portalLink displayTitle="true" title="Create" url='selectOleLineItemReceiving.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_RCVL' /><br/>
    <portal:portalLink displayTitle="true" title='Search' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_RCV'/>
    <br/>
    <br/>

    <portal:portalLink displayTitle="true" title="Claim Notices"
                       url="${ConfigProperties.application.url}/ole-kr-krad/oleClaimNoticeController?viewId=OLEClaimNoticeView&methodToCall=start"/> <br/>
    <portal:portalLink displayTitle="true" title='Claim Response Update' url='${ConfigProperties.application.url}/ole-kr-krad/oleClaimingSearchController?viewId=OLEClaimingSearchView&methodToCall=start'/><br/>
    <portal:portalLink displayTitle="true" title='Receiving and Claiming Queue' url='oleReceivingQueueSearch.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_QUEUESEARCH'/><br/><br/>


    <strong>SERIALS RECEIVING</strong><br/><br/>
    <portal:olePortalLink yellow="true" displayTitle="false" title="Claim" url='' hiddenTitle="true"/><br/>
    <portal:portalLink displayTitle="true" title='Search & Receive' url='${ConfigProperties.application.url}/ole-kr-krad/serialsReceivingRecordController?viewId=SerialsReceivingRecordSearchView&methodToCall=start'/><br/><br/>



</div>
<channel:portalChannelBottom />
