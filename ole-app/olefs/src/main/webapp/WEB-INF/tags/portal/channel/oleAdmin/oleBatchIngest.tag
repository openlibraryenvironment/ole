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
<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp" %>

<channel:portalChannelTop channelTitle="Batch"/>
<div class="body">


    <portal:portalLink displayTitle="true" title="Batch Job"
                       url="${ConfigProperties.application.url}/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.OleBatchJobBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/><br/>
    <portal:portalLink displayTitle="true" title="Deliver Notices"
                       url="${ConfigProperties.application.url}/ole-kr-krad/oleDeliverNoticeController?viewId=OLEDeliverNoticeView&methodToCall=start"/> <br/>
    <strong>Batch/Scheduled Jobs</strong><br/>
    <ul class="chan">
        <li><portal:portalLink displayTitle="true" title="Batch Semaphore File Upload" url="batchUpload.do?methodToCall=start&batchUpload.batchInputTypeName=semaphoreInputFileTypeError" /></li>
        <li><portal:portalLink displayTitle="true" title="Batch File" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.sys.batch.BatchFile&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
        <c:if test="${ConfigProperties.use.quartz.scheduling == 'true'}">
            <li><portal:portalLink displayTitle="true" title="Schedule" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.sys.batch.BatchJobStatus&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&conversionFields=name:name,group:group" /></li>
        </c:if>
        <li><portal:portalLink displayTitle="true" title="Special Batch File Upload" url="batchFileUpload" /></li>
    </ul>
    <br/>
</div>
<channel:portalChannelBottom/>
