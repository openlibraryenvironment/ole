package org.kuali.ole.ncip.service;


import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.ncip.bo.OLENCIPConstants;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

/**
 * Created by chenchulakshmig on 8/18/15.
 */
public class NCIPCheckOutItemResponseBuilder {

    public void setItemId(CheckOutItemResponseData checkOutItemResponseData, CheckOutItemInitiationData checkOutItemInitiationData, AgencyId agencyId, String identifierType) {
        ItemId itemId = new ItemId();
        itemId.setAgencyId(agencyId);
        ItemIdentifierType itemIdentifierType = new ItemIdentifierType(OLENCIPConstants.SCHEME, identifierType);
        itemId.setItemIdentifierType(itemIdentifierType);
        itemId.setItemIdentifierValue(checkOutItemInitiationData.getItemId().getItemIdentifierValue());
        checkOutItemResponseData.setItemId(itemId);
    }

    public void setUserId(CheckOutItemResponseData checkOutItemResponseData, CheckOutItemInitiationData initData, AgencyId agencyId, String borrowerType) {
        UserId userId = new UserId();
        userId.setAgencyId(agencyId);
        UserIdentifierType userIdentifierType = new UserIdentifierType(borrowerType, borrowerType);
        userId.setUserIdentifierValue(initData.getUserId().getUserIdentifierValue());
        userId.setUserIdentifierType(userIdentifierType);
        checkOutItemResponseData.setUserId(userId);
    }

    public void setDateDue(CheckOutItemResponseData checkOutItemResponseData, GregorianCalendar dueDate) {
        checkOutItemResponseData.setDateDue(dueDate);
    }

    public void setRenewalCount(CheckOutItemResponseData checkOutItemResponseData, BigDecimal renewalCount) {
        checkOutItemResponseData.setRenewalCount(renewalCount);
    }

}
