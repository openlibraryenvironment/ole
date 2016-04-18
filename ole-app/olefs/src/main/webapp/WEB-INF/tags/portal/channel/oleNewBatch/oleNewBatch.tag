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

<channel:portalChannelTop channelTitle="New Batch Process"/>
<div class="body">
    <portal:portalLink displayTitle="true" title="Quick Import" url="${ConfigProperties.application.url}/batchProcess.html"/><br/>
    <portal:portalLink displayTitle="true" title="Batch Process Profile Create" url="${ConfigProperties.application.url}/batchProcessProfile.html"/><br/>
    <portal:portalLink displayTitle="true" title="Batch Process Profile Search" url="${ConfigProperties.application.url}/oleng/view/batchProfileSearch.html"/><br/>
    <portal:portalLink displayTitle="true" title="Batch Process Profile Import" url="${ConfigProperties.application.url}/oleng/view/batchProfileImport.html"/><br/>
    <portal:portalLink displayTitle="true" title="Batch Process Jobs" url="${ConfigProperties.application.url}/batchProcessJobs.html"/><br/>
    <portal:portalLink displayTitle="true" title="Batch Reports" url="${ConfigProperties.application.url}/reportViewer.html"/><br/>
    <portal:portalLink displayTitle="true" title="Batch Display Reports" url="${ConfigProperties.application.url}/showBatchReport.html"/><br/>
</div>
<channel:portalChannelBottom/>
