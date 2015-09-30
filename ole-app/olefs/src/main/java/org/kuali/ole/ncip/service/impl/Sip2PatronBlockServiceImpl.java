package org.kuali.ole.ncip.service.impl;

import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.Map;

/**
 * Created by chenchulakshmig on 9/30/15.
 */
public class Sip2PatronBlockServiceImpl extends Sip2LookupUserServiceImpl{

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

    @Override
    protected void preProcess(Map patronBlockParameters){
        String blockedCardMessage = ((String) patronBlockParameters.get("blockedCardMessage"));
        getOlePatronDocument().setGeneralBlock(true);
        getOlePatronDocument().setGeneralBlockNotes(blockedCardMessage);
        KRADServiceLocator.getBusinessObjectService().save(getOlePatronDocument());
    }

}
