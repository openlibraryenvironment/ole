<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp" %>

<channel:portalChannelTop channelTitle="Cataloging"/>

<div class="body">

    <strong>Bibliographic</strong><br><br/>

    <portal:portalLink displayTitle="true"  title="Bibliographic Record Status"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleBibliographicRecordStatus&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>


    <br/>
    <strong>Instance</strong><br/><br/>

    <portal:portalLink displayTitle="true"   title="Item Type"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleInstanceItemType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="Item Availability Status"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleItemAvailableStatus&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="Receipt Status"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleReceiptStatus&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="Call Number Type"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleShelvingScheme&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="Statistical Searching Codes"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleStatisticalSearchingCodes&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true"   title="Type Of Ownership (extent of ownership type)"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleTypeOfOwnership&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>

<%--
    <portal:portalLink displayTitle="true" green="true" title="Access Method"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleAccessMethod&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Acquisition Method"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleAcquisitionMethod&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Action"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleAction&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Completeness"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleCompleteness&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Country Codes"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleCountryCodes&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="ELA Relationship"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleElectronicLocationAndAccessRelationship&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Encoding Level"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleEncodingLevel&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Field Encoding Level"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleFieldEncodingLevel&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="General Retention Policy"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleGeneralRetentionPolicy&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Lending Policy"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleLendingPolicy&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Notation Type"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleNotationType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Privacy"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OlePrivacy&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Record Type"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleRecordType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Reproduction Policy"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleReproductionPolicy&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Seperate Or Composite Report"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleSeperateOrCompositeReport&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Shelving Order"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleShelvingOrder&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Source Of Term"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleSourceOfTerm&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Specific Retention Policy Type"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleSpecificRetentionPolicyType&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" green="true" title="Specific Retention Policy Type Unit"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleSpecificRetentionPolicyTypeUnit&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
--%>


</div>
<channel:portalChannelBottom/>
