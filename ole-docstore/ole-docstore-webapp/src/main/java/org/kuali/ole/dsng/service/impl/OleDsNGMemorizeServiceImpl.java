package org.kuali.ole.dsng.service.impl;

import org.kuali.ole.cache.Memoize;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.*;
import org.kuali.ole.dsng.dao.BibDAO;
import org.kuali.ole.dsng.dao.HoldingDAO;
import org.kuali.ole.dsng.dao.ItemDAO;
import org.kuali.ole.dsng.service.OleDsNGMemorizeService;
import org.kuali.ole.dsng.util.*;
import org.kuali.ole.oleng.common.service.OleNgCommonMemorizeService;
import org.kuali.ole.oleng.common.service.impl.OleNgCommonMemorizeServiceImpl;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.utility.ItemUtil;
import org.kuali.ole.utility.LocationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by SheikS on 5/9/2016.
 */
@Component
public class OleDsNGMemorizeServiceImpl extends BusinessObjectServiceHelperUtil implements OleDsNGMemorizeService {

    @Autowired
    BibDAO bibDAO;

    @Autowired
    HoldingDAO holdingDAO;

    @Autowired
    ItemDAO itemDAO;

    @Autowired
    OleNgCommonMemorizeService oleNgCommonMemorizeService;

    private AccessLocationUtil accessLocationUtil;
    private AuthenticationTypeUtil authenticationTypeUtil;
    private StatisticalSearchCodeUtil statisticalSearchCodeUtil;
    private CallNumberUtil callNumberUtil;
    private LocationUtil locationUtil;
    private ItemUtil itemUtil;

    @Memoize
    public AccessLocation fetchAccessLocationByCode(String accessLocationCode){
        return getAccessLocationUtil().fetchAccessLocationByCode(accessLocationCode);
    }

    @Memoize
    public AuthenticationTypeRecord fetchAuthenticationTypeRecordByCode(String authenticationType) {
        return getAuthenticationTypeUtil().fetchAuthenticationTypeRecordByCode(authenticationType);
    }

    public OLEDonor getDonorCode(String donorCode) {
        return getOleNgCommonMemorizeService().getDonorCode(donorCode);
    }

    @Memoize
    public StatisticalSearchRecord fetchStatisticalSearchRecordByCode(String statisticalSearchCode) {
        return getStatisticalSearchCodeUtil().fetchStatisticalSearchRecordByCode(statisticalSearchCode);
    }

    @Memoize
    public CallNumberTypeRecord fetchCallNumberTypeRecordById(String callNumberTypeId) {
        return getCallNumberUtil().fetchCallNumberTypeRecordById(callNumberTypeId);
    }

    @Memoize
    public ItemStatusRecord fetchItemStatusByName(String itemStatusTypeName) {
        return getItemUtil().fetchItemStatusByName(itemStatusTypeName);
    }

    @Memoize
    public ItemTypeRecord fetchItemTypeByName(String itemTypeName) {
        return getItemUtil().fetchItemTypeByName(itemTypeName);
    }

    @Memoize
    public ItemTypeRecord fetchItemTypeByCode(String itemTypeCode) {
        return getItemUtil().fetchItemTypeByCode(itemTypeCode);
    }

    public BibDAO getBibDAO() {
        return bibDAO;
    }

    public HoldingDAO getHoldingDAO() {
        return holdingDAO;
    }

    public ItemDAO getItemDAO() {
        return itemDAO;
    }

    public AccessLocationUtil getAccessLocationUtil() {
        if(null == accessLocationUtil) {
            accessLocationUtil = new AccessLocationUtil();
        }
        return accessLocationUtil;
    }

    public AuthenticationTypeUtil getAuthenticationTypeUtil() {
        if(null == authenticationTypeUtil) {
            authenticationTypeUtil = new AuthenticationTypeUtil();
        }
        return authenticationTypeUtil;
    }

    public StatisticalSearchCodeUtil getStatisticalSearchCodeUtil() {
        if(null == statisticalSearchCodeUtil) {
            statisticalSearchCodeUtil = new StatisticalSearchCodeUtil();
        }
        return statisticalSearchCodeUtil;
    }

    public CallNumberUtil getCallNumberUtil() {
        if(null == callNumberUtil) {
            callNumberUtil = new CallNumberUtil();
        }
        return callNumberUtil;
    }

    @Override
    public LocationUtil getLocationUtil() {
        if(null == locationUtil){
            locationUtil = new LocationUtil();
            locationUtil.setOleNgCommonMemorizeService(getOleNgCommonMemorizeService());
        }
        return locationUtil;
    }

    public ItemUtil getItemUtil() {
        if(null == itemUtil) {
            itemUtil = new ItemUtil();
        }
        return itemUtil;
    }

    public OleNgCommonMemorizeService getOleNgCommonMemorizeService() {
        if(null == oleNgCommonMemorizeService) {
            oleNgCommonMemorizeService = new OleNgCommonMemorizeServiceImpl();
        }
        return oleNgCommonMemorizeService;
    }
}
