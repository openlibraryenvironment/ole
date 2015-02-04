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

<channel:portalChannelTop channelTitle="Acquisitions Search" />
<div class="body">

    <strong>Cataloging</strong><br/><br/>
    
		<!--<portal:olePortalLink green="true" displayTitle="true" title='Document Store Search' url='docStoredirect.do' /><br/><br/>
	    --><portal:olePortalLink green="true" displayTitle="true" title='Document Store Search' url='${ConfigProperties.ole.docstore.url.base}/discovery.do?searchType=newSearch'/><br/><br/>
	<strong>Load Order Records</strong><br/><br/>
	
		<portal:olePortalLink green="true" displayTitle="true" title="Load Reports" url="batchlookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleLoadSumRecords&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/><br/>
	
	<strong>Load Invoices</strong><br/><br/>
	
		<portal:olePortalLink grey="true" displayTitle="false" title="Load Reports" url="" hiddenTitle="true"/><br/> <br/>
	
	<strong>Load Bibliographic Records</strong><br/><br/>
	
		 <portal:olePortalLink grey="true" displayTitle="false" title="Load Reports" url="" hiddenTitle="true"/><br/><br/>
	
	<strong>Purchasing/Accounts Payable</strong><br/><br/>
    
        <portal:olePortalLink yellow="true" displayTitle="false" title='Electronic Invoice Rejects' url="${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_EIRT" hiddenTitle="true"/><br/>
        <portal:olePortalLink green="true" displayTitle="true" title='Order Holding Queue' url="oleOrderQueue.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ORDQU"/><br/>
        <portal:olePortalLink green="true" displayTitle="true" title='Payment Requests' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_PREQ'/><br/>
        <portal:olePortalLink green="true" displayTitle="true" title='Purchase Orders' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_PO'/><br/>
        <portal:olePortalLink green="true" displayTitle="true" title='Receiving' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_RCV'/><br/>
        <portal:olePortalLink green="true" displayTitle="true" title='Receiving Queue' url="oleReceivingQueueSearch.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_QUEUESEARCH"/><br/>
        <portal:olePortalLink green="true" displayTitle="true" title='Requisitions' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_REQS'/><br/>
        <portal:olePortalLink green="true" displayTitle="true" title='Vendor Credit Memos' url='${ConfigProperties.workflow.documentsearch.base.url}&documentTypeName=OLE_CM' /><br/>
        <portal:olePortalLink green="true" displayTitle="true" title='Acquisitions Search' url="oleAcquisitionsSearch.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ACQS"/></<br/></br>
    
</div>
<channel:portalChannelBottom />
