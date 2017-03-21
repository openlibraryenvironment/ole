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

<channel:portalChannelTop channelTitle="Circulation"/>
<div class="body">


    <%-- <portal:portalLink displayTitle="true" title="Loan/Return" green="true"
                        url="${ConfigProperties.application.url}/kr-krad/loancontroller?viewId=PatronItemView&methodToCall=start"/> <br/>--%>
    <portal:portalLink displayTitle="true" title="Loan"
                       url="${ConfigProperties.application.url}/ole-kr-krad/circcontroller?viewId=circView&methodToCall=start"/> <br/>
        <portal:portalLink displayTitle="true" title="Return"
                           url="${ConfigProperties.application.url}/ole-kr-krad/checkincontroller?viewId=checkinView&methodToCall=start"/> <br/>
        <portal:portalLink displayTitle="true" title="Item Search"
                           url="${ConfigProperties.application.url}/ole-kr-krad/deliverItemSearchController?viewId=OLEDeliverItemSearchView&methodToCall=start"/> <br/>
        <br/>
        <portal:portalLink displayTitle="true" title="Create New Request"
                       url="${ConfigProperties.application.url}/ole-kr-krad/deliverRequestMaintenance?viewTypeName=MAINTENANCE&methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.OleDeliverRequestBo"/> <br/>
    <portal:portalLink displayTitle="true"   title="Request Search"
                       url="${ConfigProperties.application.url}/ole-kr-krad/requestLookup?methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.OleDeliverRequestBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" title="Request Re-order"
                       url="${ConfigProperties.application.url}/ole-kr-krad/deliverRequestController?viewId=DeliverRequestSearch&methodToCall=start"/> <br/>

        <br/>
    <portal:portalLink displayTitle="true" title="Item Fast Add"
                           url="${ConfigProperties.application.url}/ole-kr-krad/instantFastAddItemController?viewId=fastAddView&methodToCall=start&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/> <br/>
 <br/>
</div>
<channel:portalChannelBottom/>
