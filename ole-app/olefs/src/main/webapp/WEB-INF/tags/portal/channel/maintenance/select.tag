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

<channel:portalChannelTop channelTitle="OLE-Pre-Order Request" />
<div class="body">
    <ul class="chan">
         <li><portal:portalLink displayTitle="true" title="Category" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleCategory&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
         <li><portal:portalLink displayTitle="true" title="Load Error" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleLoadError&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
         <li><portal:portalLink displayTitle="true" title="Request Source Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleRequestSourceType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
         <li><portal:portalLink displayTitle="true" title="Format Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleFormatType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
         <li><portal:portalLink displayTitle="true" title="Item Price Source" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleItemPriceSource&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
         <li><portal:portalLink displayTitle="true" title="Requestor" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleRequestor&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
         <li><portal:portalLink displayTitle="true" title="Requestor Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleRequestorType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
         <li><portal:portalLink displayTitle="true" title="Invoice Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleInvoiceType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
		 <li><portal:portalLink displayTitle="true" title="Invoice Sub Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleInvoiceSubType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
		 <li><portal:portalLink displayTitle="true" title="Payment Method" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OlePaymentMethod&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
                
         <li><portal:portalLink 
         displayTitle="true" title="Lookup from Doc Store" url="${ConfigProperties.application.url}/doclookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.lookup.DocData&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
         <li><portal:portalLink 
         displayTitle="true" title="Lookup from Doc Store and DB" url="${ConfigProperties.application.url}/doclookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.Book&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
         <li><portal:portalLink 
         displayTitle="true" title="Bibliography" url="${ConfigProperties.application.url}/doclookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleRequisitionItem&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
         
         <li><portal:portalLink displayTitle="true" title="Document Store Search" url="discovery.do?searchType=newSearch" /></li>
         <li><portal:portalLink displayTitle="true" title="Default Table Column" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleDefaultTableColumn&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
         <li><portal:portalLink displayTitle="true" title="Default Value" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleDefaultValue&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
         <li><portal:portalLink 
         displayTitle="true" title="Default Table Column Value" url="oledefaulttablecolumnValue.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_DFTTABCL" /></li>
         <li><portal:portalLink 
         displayTitle="true" title="Order Holding Queue" url="oleOrderQueue.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ORDQU" /></li>
         <li><portal:portalLink displayTitle="true" title="Exception Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleExceptionType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
		 <li><portal:portalLink displayTitle="true" title="Note Type" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.select.businessobject.OleNoteType&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
		 <li><portal:portalLink 
         displayTitle="true" title="Receiving Queue Search" url="oleReceivingQueueSearch.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_QUEUESEARCH" /></li>
         <br/><br/>
        <portal:portalLink displayTitle="true"  title="Globally Protected Field"
                           url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OleGloballyProtectedField&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
        <%-- <portal:portalLink displayTitle="true"  title="Vendor Account Info"
                            url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OleVendorAccountInfo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
         <portal:portalLink displayTitle="true"  title="Call Number"
                            url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OleCallNumber&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
     
         <portal:portalLink displayTitle="true"  title="Code"
                            url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OleCode&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
         <portal:portalLink displayTitle="true"  title="Budget Code"
                            url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OleBudgetCode&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>--%>

        <portal:portalLink displayTitle="true"  title="Overlay Action"
                           url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.ingest.pojo.OleOverlayAction&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
        <portal:portalLink displayTitle="true" title="Serials Receiving Record Search" 
                           url="${ConfigProperties.application.url}/ole-kr-krad/serialsReceivingRecordController?viewId=SerialsReceivingRecordSearchView&methodToCall=start"/> <br/>


    </ul>
</div>
<channel:portalChannelBottom />
