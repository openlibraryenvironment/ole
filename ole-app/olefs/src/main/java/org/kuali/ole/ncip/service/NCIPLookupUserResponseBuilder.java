package org.kuali.ole.ncip.service;

import org.extensiblecatalog.ncip.v2.service.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pvsubrah on 8/10/15.
 */
public class NCIPLookupUserResponseBuilder {

    public void addOptionalFields(LookupUserResponseData lookupUserResponseData, UserOptionalFields userOptionalFields) {
        lookupUserResponseData.setUserOptionalFields(userOptionalFields);
    }

    public void setUserId(LookupUserResponseData lookupUserResponseData, UserId userId) {
        lookupUserResponseData.setUserId(userId);
    }

    public void setLoanedItems(LookupUserResponseData lookupUserResponseData, List<LoanedItem> loanedItems) {
        lookupUserResponseData.setLoanedItems(loanedItems);
    }

    public void setRequestedItems(LookupUserResponseData lookupUserResponseData, List<RequestedItem> requestedItems) {
        lookupUserResponseData.setRequestedItems(requestedItems);
    }

    public void setUserFiscalAccounts(LookupUserResponseData lookupUserResponseData, List<UserFiscalAccount> userFiscalAccounts) {
        lookupUserResponseData.setUserFiscalAccounts(userFiscalAccounts);
    }
}
