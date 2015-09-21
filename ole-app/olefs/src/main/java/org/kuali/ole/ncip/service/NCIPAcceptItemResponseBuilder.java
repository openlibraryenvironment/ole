package org.kuali.ole.ncip.service;

import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.ncip.bo.OLENCIPConstants;


/**
 * Created by chenchulakshmig on 8/11/15.
 */
public class NCIPAcceptItemResponseBuilder {

    public void setRequestId(AcceptItemResponseData acceptItemResponseData, AgencyId agencyId, String requestIdentifierValue) {
        RequestId requestId = new RequestId();
        requestId.setAgencyId(agencyId);
        RequestIdentifierType requestIdentifierType = new RequestIdentifierType(OLENCIPConstants.SCHEME, OLENCIPConstants.REQUEST_IDS);
        requestId.setRequestIdentifierType(requestIdentifierType);
        requestId.setRequestIdentifierValue(requestIdentifierValue);
        acceptItemResponseData.setRequestId(requestId);

    }

    public void setItemId(AcceptItemResponseData acceptItemResponseData, AgencyId agencyId, String itemIdentifierValue) {
        ItemId itemId = new ItemId();
        itemId.setAgencyId(agencyId);
        ItemIdentifierType itemIdentifierType = new ItemIdentifierType(OLENCIPConstants.SCHEME, OLENCIPConstants.ITEM_BARCODES);
        itemId.setItemIdentifierType(itemIdentifierType);
        itemId.setItemIdentifierValue(itemIdentifierValue);
        acceptItemResponseData.setItemId(itemId);
    }

}
