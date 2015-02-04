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

<channel:portalChannelTop channelTitle="Batch" />
<div class="body">
	<strong>Financial Processing</strong><br/>
    <ul class="chan">
		<li><portal:portalLink displayTitle="true" title="Procurement Card Upload" url="batchUpload.do?methodToCall=start&batchUpload.batchInputTypeName=procurementCardInputFileType" /></li>
		<li><portal:portalLink displayTitle="true" title="Marc File Upload" url="batchUpload.do?methodToCall=start&batchUpload.batchInputTypeName=marcInputFileType" /></li>
		<li><portal:portalLink displayTitle="true" title="Acquisition File Upload" url="acqBatchUpload.do?methodToCall=start&acquisitionBatchUpload.batchInputTypeName=ordInputFileType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/></li>
	</ul>
	<strong>General Ledger</strong><br/>
    <ul class="chan">
	    <li><portal:portalLink displayTitle="true" title="Collector Flat File Upload" url="batchUpload.do?methodToCall=start&batchUpload.batchInputTypeName=collectorFlatFileInputFileType" /></li>
		<li><portal:portalLink displayTitle="true" title="Collector XML Upload" url="batchUpload.do?methodToCall=start&batchUpload.batchInputTypeName=collectorXmlInputFileType" /></li>				
		<li><portal:portalLink displayTitle="true" title="Enterprise Feed Upload" url="batchUploadFileSet.do?methodToCall=start&batchUpload.batchInputTypeName=enterpriseFeederFileSetType" /></li>
	</ul>
	<strong>Purchasing/Accounts Payable</strong><br/>
    <ul class="chan">
	    <li><portal:portalLink displayTitle="true" title="Electronic Invoice Upload" url="batchUpload.do?methodToCall=start&batchUpload.batchInputTypeName=electronicInvoiceInputFileType" /></li>
    	<c:if test="${fn:trim(ConfigProperties.environment) != fn:trim(ConfigProperties.production.environment.code)}" >
			<li><portal:portalLink displayTitle="true" title="Electronic Invoice Test File Generation" url="purapElectronicInvoiceTestFileGeneration.do" /></li>				
	    </c:if>
	</ul>
	<strong>Vendor</strong><br/>
	<ul class="chan">
	    <li><portal:portalLink displayTitle="true" title="Vendor Exclusion Upload" url="batchUpload.do?methodToCall=start&batchUpload.batchInputTypeName=vendorExcludeInputFileType" /></li>
	</ul>
	<strong>Batch/Scheduled Jobs</strong><br/>
    <ul class="chan">
        <li><portal:portalLink displayTitle="true" title="Batch Semaphore File Upload" url="batchUpload.do?methodToCall=start&batchUpload.batchInputTypeName=semaphoreInputFileTypeError" /></li>
    	<li><portal:portalLink displayTitle="true" title="Batch File" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.sys.batch.BatchFile&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
    	<c:if test="${ConfigProperties.use.quartz.scheduling == 'true'}">
		<li><portal:portalLink displayTitle="true" title="Schedule" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.sys.batch.BatchJobStatus&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&conversionFields=name:name,group:group" /></li>
		</c:if>
		<li><portal:portalLink displayTitle="true" title="Special Batch File Upload" url="batchFileUpload" /></li>
	</ul>
	<c:if test="${ConfigProperties.module.access.security.enabled == 'true'}">
		<strong>Security</strong><br/>
	    <ul class="chan">
	    	<li><portal:portalLink displayTitle="true" title="Access Security Simulation" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.sec.businessobject.AccessSecuritySimulation&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /></li>
		</ul>	
	</c:if>
</div>
<channel:portalChannelBottom />
                
