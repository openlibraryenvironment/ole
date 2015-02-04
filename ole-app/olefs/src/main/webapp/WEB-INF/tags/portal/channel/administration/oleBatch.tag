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
	<strong>Financial Processing</strong><br/><br/>
    
		<portal:olePortalLink grey="true"  displayTitle="false" title="Procurement Card Upload" url="batchUpload.do?methodToCall=start&batchUpload.batchInputTypeName=procurementCardInputFileType" hiddenTitle="true"/><br/><br/>
		<%--<portal:olePortalLink yellow="true"  displayTitle="true" title="Marc File Upload" url="batchUpload.do?methodToCall=start&batchUpload.batchInputTypeName=marcInputFileType" /><br/>
		<portal:olePortalLink yellow="true"  displayTitle="true" title="Acquisition File Upload" url="acqBatchUpload.do?methodToCall=start&acquisitionBatchUpload.batchInputTypeName=ordInputFileType" /><br/>  --%>
	
	<strong>General Ledger</strong><br/><br/>
    
	    <portal:olePortalLink yellow="true"  displayTitle="true" title="Collector Flat File Upload" url="batchUpload.do?methodToCall=start&batchUpload.batchInputTypeName=collectorFlatFileInputFileType" /><br/>
		<portal:olePortalLink yellow="true"  displayTitle="true" title="Collector XML Upload" url="batchUpload.do?methodToCall=start&batchUpload.batchInputTypeName=collectorXmlInputFileType" /><br/>				
		<portal:olePortalLink yellow="true"  displayTitle="true" title="Enterprise Feed Upload" url="batchUploadFileSet.do?methodToCall=start&batchUpload.batchInputTypeName=enterpriseFeederFileSetType" /><br/><br/>
	
	<strong>System</strong><br/><br/>
    
    	<portal:olePortalLink yellow="true"  displayTitle="true" title="Batch File" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.sys.batch.BatchFile&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/>
		<portal:olePortalLink yellow="true"  displayTitle="true" title="Schedule" url="kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.sys.batch.BatchJobStatus&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&conversionFields=name:name,group:group" /><br/>
		<portal:olePortalLink yellow="true"  displayTitle="true" title="Special Batch File Upload" url="batchFileUpload" /><br/><br/>
	
	<c:if test="${ConfigProperties.module.access.security.enabled == 'true'}">
		<strong>Security</strong><br/><br/>
	    
	    	<portal:olePortalLink yellow="true"  displayTitle="true" title="Access Security Simulation" url="${ConfigProperties.kr.url}/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.ole.sec.businessobject.AccessSecuritySimulation&docFormKey=88888888&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true" /><br/><br/>
		
	</c:if>
</div>
<channel:portalChannelBottom />
                
