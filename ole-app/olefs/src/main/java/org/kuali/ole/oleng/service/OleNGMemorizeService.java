package org.kuali.ole.oleng.service;

import org.kuali.ole.module.purap.businessobject.ItemType;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.common.service.OleNgCommonMemorizeService;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.sys.businessobject.Building;
import org.kuali.ole.sys.businessobject.Room;
import org.kuali.ole.utility.LocationUtil;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.ole.vnd.document.service.VendorService;

import java.util.List;

/**
 * Created by SheikS on 5/10/2016.
 */
public interface OleNGMemorizeService {
    public OleNgCommonMemorizeService getOleNgCommonMemorizeService();
    public VendorService getVendorService();
    public OlePurapService getOlePurapService();
    public LocationUtil getLocationUtil();
    public boolean isValidLocation(String location);
    public Room getRoom(String buildingCode, String campusCode, String buildingRoomNumber);
    public Building getBuildingDetails(String campusCode, String buildingCode);
    public String getParameter(String name);
    public List<VendorAlias> getVendorAlias(String aliasName);
    public VendorDetail getVendorDetail(Integer headerGeneratedIdentifier, Integer detailAssignedIdentifier);
    public VendorAddress getVendorDefaultAddress(Integer vendorHeaderId, Integer vendorDetailId, String addressType, String campus);
    public String getTransmissionMethodCode(String transmissionMethodDescription);
    public List<PurchaseOrderType> getOrderType(String key, String value);
    public ItemType getPurapItemType(String type);
    public Integer getRequestSourceTypeId(String requestSourceType);
    public OLEDonor getDonorCode(String donorCode);
    public BatchProcessProfile fetchBatchProcessProfile(String profileName, String type);
}
