<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp" %>

<channel:portalChannelTop channelTitle="Select"/>

<div class="body">
    <portal:portalLink displayTitle="true"   title="Globally Protected Field"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OleGloballyProtectedField&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
   <%-- <portal:portalLink displayTitle="true" green="true" title="Vendor Account Info"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OleVendorAccountInfo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Call Number"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OleCallNumber&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>

    <portal:portalLink displayTitle="true" green="true" title="Code"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OleCode&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Budget Code"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OleBudgetCode&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>--%>

    <portal:portalLink displayTitle="true"    title="Overlay Action"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.ingest.pojo.OleOverlayAction&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" title="Serials Receiving Record Search"
                       url="${ConfigProperties.application.url}/ole-kr-krad/serialsReceivingRecordController?viewId=SerialsReceivingRecordSearchView&methodToCall=start"/> <br/>

</div>
<channel:portalChannelBottom/>
