package org.kuali.ole.dsng.service;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.*;
import org.kuali.ole.dsng.dao.BibDAO;
import org.kuali.ole.dsng.dao.HoldingDAO;
import org.kuali.ole.dsng.dao.ItemDAO;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.utility.LocationUtil;

/**
 * Created by SheikS on 5/9/2016.
 */
public interface OleDsNGMemorizeService {
    public BibDAO getBibDAO();
    public HoldingDAO getHoldingDAO();
    public ItemDAO getItemDAO();
    public LocationUtil getLocationUtil();

    public AccessLocation fetchAccessLocationByCode(String accessLocationCode);
    public AuthenticationTypeRecord fetchAuthenticationTypeRecordByCode(String authenticationType);
    public OLEDonor getDonorCode(String donorCode);
    public StatisticalSearchRecord fetchStatisticalSearchRecordByCode(String statisticalSearchCode);
    public CallNumberTypeRecord fetchCallNumberTypeRecordById(String callNumberTypeId);
    public ItemStatusRecord fetchItemStatusByName(String itemStatusTypeName);
    public ItemTypeRecord fetchItemTypeByName(String itemTypeName);
    public ItemTypeRecord fetchItemTypeByCode(String itemTypeCode);


}
