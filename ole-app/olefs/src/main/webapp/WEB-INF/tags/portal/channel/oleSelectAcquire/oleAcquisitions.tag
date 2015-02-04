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

<div class="body">

    <td>
        <oleSelectAcquireChannel:oleOrdering/>
        <oleSelectAcquireChannel:oleFunds/>
        <oleSelectAcquireChannel:oleLicensing/>
        <%--<oleSelectAcquireChannel:oleSelectAcquisition/>
        <oleSelectAcquireChannel:oleChartOfAccounts/>
        <oleSelectAcquireChannel:oleLookupAndMaintenance/>--%>


    </td>
    <td>
         <oleSelectAcquireChannel:olePaying/>
         <%--<oleSelectAcquireChannel:oleMonograph/>--%>
         <oleSelectAcquireChannel:oleSelectVendor/>
         <oleSelectAcquireChannel:oleElectronicResources/>

    </td>
    <td>
         <oleSelectAcquireChannel:oleReceiving/>
         <oleSelectAcquireChannel:oleImport/>
         <oleSelectAcquireChannel:oleAcquisitionRecords/>
    </td>
    <td>
        <oleSelectAcquireChannel:oleLegend/>
        <oleSelectAcquireChannel:oleReports/>
        <oleSelectAcquireChannel:oleOthers/>
    </td>

 </div>
<channel:portalChannelBottom />


