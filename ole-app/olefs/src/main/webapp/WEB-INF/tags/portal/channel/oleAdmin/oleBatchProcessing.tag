<%--
 Copyright 2007-2009 The Kuali Foundation

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

<channel:portalChannelTop channelTitle="Batch Processes"/>
<div class="body">
    <portal:portalLink displayTitle="true" title="Batch File Type" url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.batch.bo.OLEBatchProcessFileTypeBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true" /><br/>
    <portal:portalLink displayTitle="true" title="Batch Process" url="${ConfigProperties.application.url}/ole-kr-krad/oleBatchProcessDefinitionController?viewId=OLEBatchProcessDefinitionView&methodToCall=startBatch"/><br/>
    <portal:portalLink displayTitle="true" title="Batch Process Filter Criteria" url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.batch.bo.OLEBatchProcessFilterCriteriaBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true" /><br/>
    <portal:portalLink displayTitle="true" title="Batch Process Job Details" url="${ConfigProperties.application.url}/ole-kr-krad/oleBatchProcessJobController?viewId=OLEBatchProcessJobDetailsView&methodToCall=jobDocHandler&command=initiate&documentClass=org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo"/><br/>
    <portal:portalLink displayTitle="true" title="Batch Process Profile" url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.batch.bo.OLEBatchProcessProfileBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true" /><br/>
    <portal:portalLink displayTitle="true" title="Batch Process Schedule" url="${ConfigProperties.application.url}/ole-kr-krad/oleBatchProcessJobController?viewId=OLEBatchScheduleJob&methodToCall=jobDocHandler&command=initiate&documentClass=org.kuali.ole.batch.bo.OLEBatchProcessScheduleBo"/><br/>

    <portal:portalLink displayTitle="true" title="Batch Process Type" url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.batch.bo.OLEBatchProcessTypeBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true" /><br/>
<%--    <portal:portalLink displayTitle="true" title="Location Import"
                       url="${ConfigProperties.application.url}/ole-kr-krad/locationcontroller?viewId=OleLocationView&methodToCall=start"/> <br/>
    <portal:portalLink displayTitle="true" title="Location Load Reports"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleLocationIngestSummaryRecord&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/> <br/>
    <portal:portalLink displayTitle="true" title="Patron Import"
                       url="${ConfigProperties.application.url}/ole-kr-krad/patronrecordcontroller?viewId=OlePatronRecordView&methodToCall=start"/>
    <br/>
    <portal:portalLink displayTitle="true"   title="Patron Import Records"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.OlePatronIngestSummaryRecord&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/>
    </br>--%>

    <br/>

    <strong>OLE FS Batch</strong><br/><br/>

    <portal:portalLink displayTitle="true" title="Batch File" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.sys.batch.BatchFile&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
    <portal:portalLink displayTitle="true" title="Batch/Scheduled Jobs"
                       url="${ConfigProperties.application.url}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.OleBatchJobBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" title="Batch Semaphore File Upload" url="batchUpload.do?methodToCall=start&batchUpload.batchInputTypeName=semaphoreInputFileTypeError" /><br/>

    <portal:portalLink displayTitle="true" title="Schedule" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.sys.batch.BatchJobStatus&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&conversionFields=name:name,group:group" /><br/>
    <portal:portalLink displayTitle="true" title="Special Batch File Upload" url="batchFileUpload" /><br/>
    <portal:portalLink displayTitle="true" title="Compute Loan Overdue Notice Dates"
                       url="${ConfigProperties.application.url}/ole-kr-krad/oleNoticeController?viewId=OleNoticeView&methodToCall=start"/> <br/>







</div>
<channel:portalChannelBottom/>
