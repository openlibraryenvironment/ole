package org.kuali.ole.ncip.service.impl;

import org.kuali.ole.ncip.service.CirculationRestService;

import java.util.Map;

/**
 * Created by sheiksalahudeenm on 25/6/15.
 */
public class CirculationRestServiceImpl implements CirculationRestService {

    @Override
    public String renewItems(Map renewParameters) {
        return new NonSip2RenewItemService().renewItems(renewParameters);
    }

    @Override
    public String renewItemsSIP2(Map renewParameters) {
        return new Sip2RenewItemService().renewItems(renewParameters);

    }

    @Override
    public String lookupUserSIP2(Map lookupUserParameters) {
        return null;
    }

    @Override
    public String acceptItemSIP2(Map acceptItemParameters) {
        return null;
    }

    @Override
    public String checkoutItem(Map checoutParameters) {
        return new NonSip2CheckoutItemService().checkoutItem(checoutParameters);
    }

    @Override
    public String checkoutItemSIP2(Map checoutParameters) {
        return new Sip2CheckoutItemService().checkoutItem(checoutParameters);
    }

    @Override
    public String checkinItem(Map checkinParameters) {
        return new NonSip2CheckinItemService().checkinItem(checkinParameters);
    }

    @Override
    public String checkinItemSIP2(Map checkinParameters) {
        return new Sip2CheckinItemService().checkinItem(checkinParameters);
    }

    @Override
    public String placeRequestSIP2(Map placeReqeustParameters) {
        return null;
    }

    @Override
    public String cancelRequestVuFind(Map cancelRequestParameters) {
        return null;
    }

}
