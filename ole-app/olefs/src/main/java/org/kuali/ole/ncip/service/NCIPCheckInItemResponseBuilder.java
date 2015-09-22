package org.kuali.ole.ncip.service;

import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.bo.OLECheckInItem;
import org.kuali.ole.ncip.bo.OLENCIPConstants;

/**
 * Created by chenchulakshmig on 8/21/15.
 */
public class NCIPCheckInItemResponseBuilder {

    public void setItemId(CheckInItemResponseData checkInItemResponseData, String itemBarcode, AgencyId agencyId, String identifierType) {
        ItemId itemId = new ItemId();
        itemId.setAgencyId(agencyId);
        ItemIdentifierType itemIdentifierType = new ItemIdentifierType(OLENCIPConstants.SCHEME, identifierType);
        itemId.setItemIdentifierType(itemIdentifierType);
        itemId.setItemIdentifierValue(itemBarcode);
        checkInItemResponseData.setItemId(itemId);
    }

    public void setUserId(CheckInItemResponseData checkInItemResponseData, AgencyId agencyId, OLECheckInItem checkedInItem) {
        UserId userId = new UserId();
        userId.setAgencyId(agencyId);
        UserIdentifierType userIdentifierType = new UserIdentifierType(checkedInItem.getUserType(), checkedInItem.getUserType());
        userId.setUserIdentifierValue(checkedInItem.getPatronBarcode());
        userId.setUserIdentifierType(userIdentifierType);
        checkInItemResponseData.setUserId(userId);
    }

    public void setItemOptionalFields(CheckInItemResponseData checkInItemResponseData, OLECheckInItem checkedInItem) {
        ItemOptionalFields itemOptionalFields = new ItemOptionalFields();
        BibliographicDescription bibliographicDescription = new BibliographicDescription();
        bibliographicDescription.setAuthor(checkedInItem.getTitle());
        bibliographicDescription.setTitle(checkedInItem.getAuthor());
        itemOptionalFields.setBibliographicDescription(bibliographicDescription);
        ItemDescription itemDescription = new ItemDescription();
        itemDescription.setCallNumber(checkedInItem.getCallNumber());
        itemOptionalFields.setItemDescription(itemDescription);
        checkInItemResponseData.setItemOptionalFields(itemOptionalFields);
    }


}