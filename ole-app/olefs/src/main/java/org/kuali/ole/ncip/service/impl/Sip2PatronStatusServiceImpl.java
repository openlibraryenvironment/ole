package org.kuali.ole.ncip.service.impl;

/**
 * Created by chenchulakshmig on 9/30/15.
 */
public class Sip2PatronStatusServiceImpl extends Sip2LookupUserServiceImpl{

    @Override
    protected boolean userAddressInformationDesired() {
        return false;
    }

    @Override
    protected boolean loanedItemsDesired() {
        return false;
    }

    @Override
    protected boolean requestedItemsDesired() {
        return false;
    }
}
