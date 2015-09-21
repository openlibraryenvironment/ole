package org.kuali.ole.ncip.service;

import java.util.Map;

/**
 * Created by sheiksalahudeenm on 25/6/15.
 */
public interface CirculationRestService {
    public String renewItems(Map renewParameters);
    public String renewItemsSIP2(Map renewParameters);
    public String lookupUserSIP2(Map lookupUserParameters);
    public String acceptItemSIP2(Map acceptItemParameters);
    public String checkoutItem(Map checoutParameters);
    public String checkoutItemSIP2(Map checoutParameters);
    public String checkinItem(Map checkinParameters);
    public String checkinItemSIP2(Map checkinParameters);
    public String placeRequestSIP2(Map placeReqeustParameters);
    public String cancelRequestVuFind(Map cancelRequestParameters);
}
