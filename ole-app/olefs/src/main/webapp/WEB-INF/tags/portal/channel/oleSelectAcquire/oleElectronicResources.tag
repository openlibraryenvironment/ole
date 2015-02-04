<%@ include file="/jsp/sys/kfsTldHeader.jsp"%>

<channel:portalChannelTop channelTitle="Electronic Resources" />

<div class="body">
    <strong>E-Resources</strong><br/>
    <portal:portalLink displayTitle="true" title="Create" url="${ConfigProperties.application.url}/ole-kr-krad/oleERSController?viewId=OLEEResourceRecordView&methodToCall=docHandler&command=initiate&documentClass=org.kuali.ole.select.document.OLEEResourceRecordDocument"/><br/>
    <portal:portalLink displayTitle="true" title="Search" url="${ConfigProperties.application.url}/ole-kr-krad/searchEResourceController?viewId=OLEEResourceSearchView&methodToCall=start"/><br/>
    <portal:portalLink displayTitle="true" title="Review DashBoard" url="${ConfigProperties.application.url}/ole-kr-krad/oleEResourceChangeDashBoardController?viewId=EResourceChangesDashBoardView&methodToCall=start"/><br/>
    <portal:portalLink displayTitle="true" title="Local GOKb" url="${ConfigProperties.application.url}/ole-kr-krad/localGokbController?viewId=LocalGokbView&methodToCall=start"/><br/>
    <br/>

    <strong>Platform</strong><br/>
    <portal:portalLink displayTitle="true" title="Create" url="${ConfigProperties.application.url}/ole-kr-krad/platformRecordController?viewId=OLEPlatformRecordView&methodToCall=docHandler&command=initiate&documentClass=org.kuali.ole.select.document.OLEPlatformRecordDocument"/><br/>
    <portal:portalLink displayTitle="true" title="Search" url="${ConfigProperties.application.url}/ole-kr-krad/platformSearchController?viewId=OLEPlatformSearchView&methodToCall=start"/><br/>
    <br/>
</div>
<channel:portalChannelBottom />
