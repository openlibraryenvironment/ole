package org.kuali.ole.ncip.service;

import java.util.Map;

/**
 * Created by sheiksalahudeenm on 25/6/15.
 */
public interface CirculationRestService {
    public String renewItems(Map renewParameters);
    public String renewItemsSIP2(Map renewParameters);
    public String acceptItemSIP2(Map acceptItemParameters);
    public String checkoutItem(Map checkoutParameters);
    public String checkoutItemSIP2(Map checkoutParameters);
    public String checkinItem(Map checkinParameters);
    public String checkinItemSIP2(Map checkinParameters);
    public String lookupUser(Map lookupUserParameters);
    public String lookupUserSIP2(Map lookupUserParameters);
    public String patronStatusSIP2(Map lookupUserParameters);
    public String patronBlockSIP2(Map patronBlockParameters);
    public String patronEnableSIP2(Map patronBlockParameters);
    public String loginSIP2(Map lookupUserParameters);
    public String placeRequestSIP2(Map placeReqeustParameters);
    public String cancelRequestVuFind(Map cancelRequestParameters);
    public String itemInfoSIP2(Map itemInfoParameters);
}
