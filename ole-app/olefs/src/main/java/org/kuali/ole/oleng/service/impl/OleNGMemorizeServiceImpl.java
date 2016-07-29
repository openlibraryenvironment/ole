package org.kuali.ole.oleng.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.cache.Memoize;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.OleFundCode;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatusRecord;
import org.kuali.ole.module.purap.businessobject.ItemType;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderTransmissionMethod;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.common.service.OleNgCommonMemorizeService;
import org.kuali.ole.oleng.common.service.impl.OleNgCommonMemorizeServiceImpl;
import org.kuali.ole.oleng.service.OleNGMemorizeService;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OleVendorAccountInfo;
import org.kuali.ole.select.businessobject.OleRequestSourceType;
import org.kuali.ole.select.document.service.OleDocstoreHelperService;
import org.kuali.ole.sys.businessobject.Building;
import org.kuali.ole.sys.businessobject.Room;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.utility.ItemUtil;
import org.kuali.ole.utility.LocationUtil;
import org.kuali.ole.vnd.businessobject.*;
import org.kuali.ole.vnd.document.service.VendorService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 5/10/2016.
 */
@Component
public class OleNGMemorizeServiceImpl extends BusinessObjectServiceHelperUtil
        implements OleNGMemorizeService {

    @Autowired
    private OleNgCommonMemorizeService oleNgCommonMemorizeService;

    private LocationUtil locationUtil;
    private OleDocstoreHelperService oleDocstoreHelperService;
    private VendorService vendorService;
    private OlePurapService olePurapService;
    private ItemUtil itemUtil;

    @Override
    public OleNgCommonMemorizeService getOleNgCommonMemorizeService() {
        if(null == oleNgCommonMemorizeService) {
            oleNgCommonMemorizeService = new OleNgCommonMemorizeServiceImpl();
        }
        return oleNgCommonMemorizeService;
    }

    @Memoize
    public boolean isValidLocation(String location) {
        return getOleDocstoreHelperService().isValidLocation(location);
    }

    @Memoize
    public Room getRoom(String buildingCode, String campusCode, String buildingRoomNumber) {
        Map<String, String> deliveryMap = new HashMap<>();
        deliveryMap.put(OLEConstants.OLEBatchProcess.BUILDING_CODE, buildingCode);
        deliveryMap.put(OLEConstants.OLEBatchProcess.CAMPUS_CODE, campusCode);
        deliveryMap.put(OLEConstants.BUILDING_ROOM_NUMBER, buildingRoomNumber);
        return getBusinessObjectService().findByPrimaryKey(Room.class, deliveryMap);
    }

    @Memoize
    public Building getBuildingDetails(String campusCode, String buildingCode) {
        return getVendorService().getBuildingDetails(campusCode, buildingCode);
    }

    @Memoize
    public String getParameter(String name) {
        return getOlePurapService().getParameter(name);
    }

    @Memoize
    public List<VendorAlias> getVendorAlias(String aliasName) {
        Map<String, String> vendorAliasMap = new HashMap<>();
        vendorAliasMap.put(OLEConstants.OLEBatchProcess.VENDOR_ALIAS_NAME, aliasName);
        return (List<VendorAlias>) KRADServiceLocatorWeb.getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
    }

    @Memoize
    public VendorDetail getVendorDetail(Integer headerGeneratedIdentifier, Integer detailAssignedIdentifier) {
        Map vendorDetailMap = new HashMap();
        vendorDetailMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_HEADER_IDENTIFIER, headerGeneratedIdentifier);
        vendorDetailMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_DETAIL_IDENTIFIER, detailAssignedIdentifier);
        return getBusinessObjectService().findByPrimaryKey(VendorDetail.class, vendorDetailMap);
    }

    @Memoize
    public VendorAddress getVendorDefaultAddress(Integer vendorHeaderId, Integer vendorDetailId, String addressType, String campus) {
        return getVendorService().getVendorDefaultAddress(vendorHeaderId, vendorDetailId, addressType, campus);
    }
    @Memoize
    public String getTransmissionMethodCode(String transmissionMethodDescription){
        Map<String, String> transmissionCodeMap = new HashMap<>();
        transmissionCodeMap.put(OLEConstants.OLEBatchProcess.PO_TRANSMISSION_METHOD_DESC, transmissionMethodDescription);
        List<PurchaseOrderTransmissionMethod> transmissionMethodList = (List<PurchaseOrderTransmissionMethod>) getBusinessObjectService().findMatching(PurchaseOrderTransmissionMethod.class, transmissionCodeMap);
        if (CollectionUtils.isNotEmpty(transmissionMethodList)) {
            return transmissionMethodList.get(0).getPurchaseOrderTransmissionMethodCode();
        }
        return null;
    }

    @Memoize
    public List<PurchaseOrderType> getOrderType(String key, String value) {
        Map purchaseOrderTypeMap = new HashMap();
        purchaseOrderTypeMap.put(key, value);
        return (List) getBusinessObjectService().findMatching(PurchaseOrderType.class, purchaseOrderTypeMap);
    }

    @Memoize
    public ItemType getPurapItemType(String type) {
        return getBusinessObjectService().findBySinglePrimaryKey(org.kuali.ole.module.purap.businessobject.ItemType.class, type);
    }

    @Memoize
    public Integer getRequestSourceTypeId(String requestSourceType) {
        Map<String,String> requestSourceMap = new HashMap<>();
        requestSourceMap.put(OLEConstants.OLEBatchProcess.REQUEST_SRC,requestSourceType);
        List<OleRequestSourceType> requestSourceList = (List) getBusinessObjectService().findMatching(OleRequestSourceType.class, requestSourceMap);
        if(requestSourceList != null && requestSourceList.size() > 0){
            return requestSourceList.get(0).getRequestSourceTypeId();
        }
        return null;
    }

    public OLEDonor getDonorCode(String donorCode) {
        return getOleNgCommonMemorizeService().getDonorCode(donorCode);
    }

    @Memoize
    public BatchProcessProfile fetchBatchProcessProfile(String profileName, String type) {
        BatchProcessProfile batchProcessProfile = null;
        Map parameterMap = new HashMap();
        parameterMap.put("batchProcessProfileName", profileName);
        List<BatchProcessProfile> matching = (List<BatchProcessProfile>) KRADServiceLocator.getBusinessObjectService().findMatching(BatchProcessProfile.class, parameterMap);
        if (CollectionUtils.isNotEmpty(matching)) {
            try {
                batchProcessProfile = matching.get(0);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setVisibilityChecker(objectMapper.getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                batchProcessProfile = objectMapper.readValue(IOUtils.toString(batchProcessProfile.getContent()), BatchProcessProfile.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return batchProcessProfile;
    }

    @Memoize
    public List<OleExchangeRate> getExchangeRate(String currencyTypeId) {
        Map documentNumberMap = new HashMap();
        documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, currencyTypeId);
        return (List) getBusinessObjectService().findMatchingOrderBy(
                OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
    }

    @Memoize
    public OleCurrencyType getCurrencyType(String currencyTypeId) {
        return getBusinessObjectService().findBySinglePrimaryKey(OleCurrencyType.class,currencyTypeId);
    }

    @Memoize
    public List<OleVendorAccountInfo> getVendorAccountInfo(String code) {
        Map matchBFN = new HashMap();
        matchBFN.put("vendorRefNumber", code);
        return (List<OleVendorAccountInfo>) getBusinessObjectService().findMatching(OleVendorAccountInfo.class, matchBFN);
    }

    @Memoize
    public List<Account> getAccount(String accountNumber) {
        Map matchChartCode = new HashMap();
        matchChartCode.put("accountNumber", accountNumber);
        return (List<Account>) getBusinessObjectService().findMatching(Account.class, matchChartCode);
    }

    @Memoize
    public List<OleFundCode> getFundCode(String fundCode) {
        Map fundCodeMap = new HashMap<>();
        fundCodeMap.put(OLEConstants.OLEEResourceRecord.FUND_CODE, fundCode);
        return (List) getBusinessObjectService().findMatching(OleFundCode.class, fundCodeMap);
    }

    @Memoize
    public ItemStatusRecord fetchItemStatusByName(String itemStatusTypeName) {
        return getItemUtil().fetchItemStatusByName(itemStatusTypeName);
    }

    @Override
    public LocationUtil getLocationUtil() {
        if(null == locationUtil){
            locationUtil = new LocationUtil();
            locationUtil.setOleNgCommonMemorizeService(oleNgCommonMemorizeService);
        }
        return locationUtil;
    }


    public OleDocstoreHelperService getOleDocstoreHelperService() {
        if(null == oleDocstoreHelperService) {
            oleDocstoreHelperService = SpringContext.getBean(OleDocstoreHelperService.class);
        }
        return oleDocstoreHelperService;
    }

    public VendorService getVendorService() {
        if (vendorService == null) {
            vendorService = SpringContext.getBean(VendorService.class);
        }
        return vendorService;
    }


    public OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    public ItemUtil getItemUtil() {
        if(null == itemUtil) {
            itemUtil = new ItemUtil();
        }
        return itemUtil;
    }



}
