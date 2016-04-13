<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp" %>

<channel:portalChannelTop channelTitle="Patron"/>

<div class="body">
    <portal:portalLink displayTitle="true"   title="Patron"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.OlePatronDocument&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <br>
    <portal:portalLink displayTitle="true"   title="Edit Patron Bills"
                       url="${ConfigProperties.application.url}/ole-kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.PatronBillPayment&returnLocation=${ConfigProperties.application.url}/portal.do&hideReturnLink=true&showMaintenanceLinks=true"/><br/>
    <portal:portalLink displayTitle="true" title="Deliver Notices Search"
                       url="${ConfigProperties.application.url}/ole-kr-krad/deliverNoticeSearchController?viewId=OLEDeliverNoticeSearchView&methodToCall=start"/> <br/>
    <%--<portal:portalLink displayTitle="true" title="MyAccount"
                       url="${ConfigProperties.application.url}/ole-kr-krad/myaccountcontroller?viewId=RenewalItemView&methodToCall=start"/> <br/><br/>--%>
    <portal:portalLink displayTitle="true" title="Claimed Returned Items"
                       url="${ConfigProperties.application.url}/ole-kr-krad/claimedReturnedItemsController?viewId=OLEClaimedReturnedItemsView&methodToCall=start"/> <br/>

    <portal:portalLink displayTitle="true" title="Lost & Returned Item Search"
                       url="${ConfigProperties.application.url}/ole-kr-krad/returnedLostItemController?viewId=OLEReturnedItemSearchView&methodToCall=start"/> <br/>

    </br>

</div>
<channel:portalChannelBottom/>
