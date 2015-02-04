<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp" %>

<channel:portalChannelTop channelTitle="Patron Admin"/>

<div class="body">
    <%--<portal:portalLink displayTitle="true" title="Patron Import"
                       url="${ConfigProperties.application.url}/ole-kr-krad/patronrecordcontroller?viewId=OlePatronRecordView&methodToCall=start"/>
    <br/>--%>
    <portal:portalLink displayTitle="true"   title="Patron Load Reports"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.OlePatronIngestSummaryRecord&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" title="Patron Merge"
                       url="${ConfigProperties.application.url}/ole-kr-krad/patronMergeController?viewId=PatronMergeView&methodToCall=start"/> <br/>
    <portal:portalLink displayTitle="true"  title="Patron Type"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.OleBorrowerType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" title="Patron Source"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.OleSourceBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" title="Statistical Category"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.OleStatisticalCategoryBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" title="Address Source"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.OleAddressSourceBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="Fee Type"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.OleFeeType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="Payment Status"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.OlePaymentStatus&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="Fixed Due Date"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.OleFixedDueDate&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>


</div>
<channel:portalChannelBottom/>
