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

<channel:portalChannelTop channelTitle="All Acquisitions Records" />
<%@ attribute name="riceUrl" required="false" type="java.lang.String"%>
<div class="body">
   <%-- <strong>All acquisitions records</strong><br/>--%>
    <portal:portalLink displayTitle="true" title='Acquisitions Search' url="oleAcquisitionsSearch.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ACQS"/><br/><br/>
</div>
<channel:portalChannelBottom />


