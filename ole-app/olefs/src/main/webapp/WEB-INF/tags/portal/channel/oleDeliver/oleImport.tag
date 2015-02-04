<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp" %>

<channel:portalChannelTop channelTitle="Import"/>

<div class="body">
    <portal:portalLink displayTitle="true" title="Location Import"
                       url="${ConfigProperties.application.url}/ole-kr-krad/locationcontroller?viewId=OleLocationView&methodToCall=start"/> <br/>
    <%--<portal:portalLink displayTitle="true" title="Patron Import"
                       url="${ConfigProperties.application.url}/ole-kr-krad/patronrecordcontroller?viewId=OlePatronRecordView&methodToCall=start"/>
    <br/>--%>
    <portal:portalLink displayTitle="true" title="View Location Reports"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.describe.bo.OleLocationIngestSummaryRecord&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/> <br/>
    <portal:portalLink displayTitle="true"   title="View Patron Reports"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.OlePatronIngestSummaryRecord&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/>
    </br>

</div>
<channel:portalChannelBottom/>
