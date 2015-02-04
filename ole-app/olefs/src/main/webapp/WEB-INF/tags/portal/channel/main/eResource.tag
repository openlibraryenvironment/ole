<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp" %>

<channel:portalChannelTop channelTitle="E-Resource"/>

<div class="body">
    <strong>E-Resource</strong><br/><br/>
    <portal:portalLink displayTitle="true" title="E-Resource"
                          url="${ConfigProperties.application.url}/ole-kr-krad/oleERSController?viewId=OLEEResourceRecordView&methodToCall=docHandler&command=initiate&documentClass=org.kuali.ole.select.document.OLEEResourceRecordDocument"/><br/>
    <portal:portalLink displayTitle="true" title="E-Resource Search"
                       url="${ConfigProperties.application.url}/ole-kr-krad/searchEResourceController?viewId=OLEEResourceSearchView&methodToCall=start"/><br/><br/>
    <strong>Maintenance</strong><br/><br/>
    <portal:portalLink displayTitle="true"   title="Authentication Type"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OLEAuthenticationType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="Access Location"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OLEAccessLocation&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="E-Resource Status"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OLEEResourceStatus&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="Package Type"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OLEPackageType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="Package Scope"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OLEPackageScope&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="Material Type"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OLEMaterialType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="Access Type"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OLEAccessType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"  title="Content Type"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OLEContentType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"  title="Payment Type"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OLEPaymentType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="Request Priority"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.select.bo.OLERequestPriority&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>

</div>
<channel:portalChannelBottom/>
