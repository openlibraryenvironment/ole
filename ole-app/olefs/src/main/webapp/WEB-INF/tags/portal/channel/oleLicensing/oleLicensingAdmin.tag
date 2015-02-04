<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp"%>

<channel:portalChannelTop channelTitle="Licensing Admin" />

<div class="body">
   <portal:portalLink displayTitle="true"   title="Manage CheckList"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OleCheckListBo&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>

    <portal:portalLink displayTitle="true"   title="License Request Status"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OleLicenseRequestStatus&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="License Request Current Location"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OleLicenseRequestLocation&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="License Request Type"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OleLicenseRequestType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="Agreement"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OleAgreementSearch&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>

</div>
<channel:portalChannelBottom />
