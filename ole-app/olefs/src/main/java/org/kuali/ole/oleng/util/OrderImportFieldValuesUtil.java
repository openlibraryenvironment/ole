package org.kuali.ole.oleng.util;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.coa.businessobject.*;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.oleng.dao.SelectDAO;
import org.kuali.ole.select.businessobject.OLERequestorPatronDocument;
import org.kuali.ole.select.businessobject.OleFormatType;
import org.kuali.ole.select.businessobject.OleRequestSourceType;
import org.kuali.ole.sys.businessobject.Building;
import org.kuali.ole.sys.businessobject.Room;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.*;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KeyValuesService;

import java.util.*;

/**
 * Created by rajeshbabuk on 12/28/15.
 */
public class OrderImportFieldValuesUtil {

    private BusinessObjectService businessObjectService;
    private SelectDAO selectDAO;

    public OrderImportFieldValuesUtil() {}

    public OrderImportFieldValuesUtil(SelectDAO selectDAO) {
        this.selectDAO = selectDAO;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null)
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        return businessObjectService;
    }

    public Map<String, String> getMaintenanceDataByField(String fieldName) {
        Map<String, String> fieldValues = new HashMap<>();
        if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.ORG_CODE)) {
            List<Organization> chartCodeList = selectDAO.fetchAllOrganization();
            if (CollectionUtils.isNotEmpty(chartCodeList)) {
                for (Organization chartCode : chartCodeList) {
                    fieldValues.put(chartCode.getOrganizationCode(), chartCode.getOrganizationCode());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.CHART_CODE) || fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.ITEM_CHART_CODE)) {
            List<Chart> itemChartCodeList = selectDAO.fetchAllItemChartCode();
            if (CollectionUtils.isNotEmpty(itemChartCodeList)) {
                for (Chart itemChartCode : itemChartCodeList) {
                    fieldValues.put(itemChartCode.getChartOfAccountsCode(), itemChartCode.getChartOfAccountsCode());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.CONTRACT_MANAGER)) {
            List<ContractManager> contractManagerList = selectDAO.fetchAllContractManager();
            if (CollectionUtils.isNotEmpty(contractManagerList)) {
                for (ContractManager contractManager : contractManagerList) {
                    fieldValues.put(contractManager.getContractManagerName(), contractManager.getContractManagerName());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.ORDER_TYPE)) {
            List<PurchaseOrderType> purchaseOrderTypeList = selectDAO.fetchAllOrderType();
            if (CollectionUtils.isNotEmpty(purchaseOrderTypeList)) {
                for (PurchaseOrderType purchaseOrderType : purchaseOrderTypeList) {
                    fieldValues.put(purchaseOrderType.getPurchaseOrderType(), purchaseOrderType.getPurchaseOrderType());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.FUNDING_SOURCE)) {
            List<FundingSource> fundingSourceList = selectDAO.fetchAllFundingSource();
            if (CollectionUtils.isNotEmpty(fundingSourceList)) {
                for (FundingSource fundingSource : fundingSourceList) {
                    fieldValues.put(fundingSource.getFundingSourceCode(), fundingSource.getFundingSourceCode());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.DELIVERY_CAMPUS_CODE)) {
            List<Building> campusList = selectDAO.fetchAllBuilding();
            if (CollectionUtils.isNotEmpty(campusList)) {
                for (Building campus : campusList) {
                    fieldValues.put(campus.getCampusCode(), campus.getCampusCode());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.BUILDING_CODE)) {
            List<Building> buildingList = selectDAO.fetchAllBuilding();
            if (CollectionUtils.isNotEmpty(buildingList)) {
                for (Building building : buildingList) {
                    fieldValues.put(building.getBuildingCode(), building.getBuildingCode());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.DELIVERY_BUILDING_ROOM_NUMBER)) {
            List<Room> roomList = selectDAO.fetchAllRoom();
            if (CollectionUtils.isNotEmpty(roomList)) {
                for (Room room : roomList) {
                    fieldValues.put(room.getBuildingRoomNumber(), room.getBuildingRoomNumber());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.VENDOR_CHOICE)) {
            List<PurchaseOrderVendorChoice> purchaseOrderVendorChoiceList = selectDAO.fetchAllVendorChoice();
            if (CollectionUtils.isNotEmpty(purchaseOrderVendorChoiceList)) {
                for (PurchaseOrderVendorChoice purchaseOrderVendorChoice : purchaseOrderVendorChoiceList) {
                    fieldValues.put(purchaseOrderVendorChoice.getPurchaseOrderVendorChoiceCode(), purchaseOrderVendorChoice.getPurchaseOrderVendorChoiceCode());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.COST_SOURCE)) {
            List<PurchaseOrderCostSource> purchaseOrderCostSourceList = selectDAO.fetchAllCostSource();
            if (CollectionUtils.isNotEmpty(purchaseOrderCostSourceList)) {
                for (PurchaseOrderCostSource purchaseOrderCostSource : purchaseOrderCostSourceList) {
                    fieldValues.put(purchaseOrderCostSource.getPurchaseOrderCostSourceCode(), purchaseOrderCostSource.getPurchaseOrderCostSourceCode());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.DEFAULT_LOCATION)) {
            List<String> locationList = getItemLocation();
            if (CollectionUtils.isNotEmpty(locationList)) {
                for (String location : locationList) {
                    fieldValues.put(location, location);
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.VENDOR_NUMBER)) {
            List<VendorDetail> vendorDetailList = selectDAO.fetchAllVendorDetail();
            if (CollectionUtils.isNotEmpty(vendorDetailList)) {
                for (VendorDetail vendorDetail : vendorDetailList) {
                    fieldValues.put(vendorDetail.getVendorHeaderGeneratedIdentifier() + "-" + vendorDetail.getVendorDetailAssignedIdentifier(), vendorDetail.getVendorHeaderGeneratedIdentifier() + "-" + vendorDetail.getVendorDetailAssignedIdentifier());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.VENDOR_ALIAS_NAME)) {
            List<VendorAlias> vendorAliasList = selectDAO.fetchAllVendorAlias();
            if (CollectionUtils.isNotEmpty(vendorAliasList)) {
                for (VendorAlias vendorAlias : vendorAliasList) {
                    fieldValues.put(vendorAlias.getVendorAliasName(), vendorAlias.getVendorAliasName());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.VENDOR_INFO_CUSOTMER_NUMBER)) {
            List<VendorCustomerNumber> vendorCustomerList = selectDAO.fetchAllVendorCustomerNumber();
            if (CollectionUtils.isNotEmpty(vendorCustomerList)) {
                for (VendorCustomerNumber vendorCustomer : vendorCustomerList) {
                    fieldValues.put(vendorCustomer.getVendorCustomerNumber(), vendorCustomer.getVendorCustomerNumber());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.REQUESTOR_NAME)) {
            List<OLERequestorPatronDocument> olePatronDocumentList = selectDAO.fetchAllRequestor();
            if (CollectionUtils.isNotEmpty(olePatronDocumentList)) {
                for (OLERequestorPatronDocument oleRequestorPatronDocument : olePatronDocumentList) {
                    fieldValues.put(oleRequestorPatronDocument.getLastName() + ", " + oleRequestorPatronDocument.getFirstName(), oleRequestorPatronDocument.getLastName() + ", " + oleRequestorPatronDocument.getFirstName());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.ITEM_STATUS)) {
            List<KeyValue> itemStatusList = getItemStatus();
            if (CollectionUtils.isNotEmpty(itemStatusList)) {
                for (int itemStatusCount = 0; itemStatusCount < itemStatusList.size(); itemStatusCount++) {
                    fieldValues.put(itemStatusList.get(itemStatusCount).getKey(), itemStatusList.get(itemStatusCount).getKey());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.ACCOUNT_NUMBER)) {
            List<Account> accountNumberList = selectDAO.fetchAllAccount();
            if (CollectionUtils.isNotEmpty(accountNumberList)) {
                for (Account account : accountNumberList) {
                    fieldValues.put(account.getAccountNumber(), account.getAccountNumber());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.OBJECT_CODE)) {
            List<ObjectCode> objectCodeList = selectDAO.fetchAllObjectCode();
            if (CollectionUtils.isNotEmpty(objectCodeList)) {
                for (ObjectCode objectCode : objectCodeList) {
                    fieldValues.put(objectCode.getFinancialObjectCode(), objectCode.getFinancialObjectCode());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.REQUEST_SRC)) {
            List<OleRequestSourceType> requestSourceTypeList = selectDAO.fetchAllRequestSourceType();
            if (CollectionUtils.isNotEmpty(requestSourceTypeList)) {
                for (OleRequestSourceType requestSourceType : requestSourceTypeList) {
                    fieldValues.put(requestSourceType.getRequestSourceType(), requestSourceType.getRequestSourceType());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.ASSIGN_TO_USER)) {
            List<PrincipalBo> assignedUserList = selectDAO.fetchAllPrincipalBo();
            if (CollectionUtils.isNotEmpty(assignedUserList)) {
                for (PrincipalBo assignedUser : assignedUserList) {
                    fieldValues.put(assignedUser.getPrincipalName(), assignedUser.getPrincipalName());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.METHOD_OF_PO_TRANSMISSION)) {
            List<PurchaseOrderTransmissionMethod> transmissionMethodList = selectDAO.fetchAllPurchaseOrderTransmissionMethod();
            if (CollectionUtils.isNotEmpty(transmissionMethodList)) {
                for (PurchaseOrderTransmissionMethod transmissionMethod : transmissionMethodList) {
                    if (transmissionMethod.isDisplayToUser()) {
                        fieldValues.put(transmissionMethod.getPurchaseOrderTransmissionMethodDescription(), transmissionMethod.getPurchaseOrderTransmissionMethodDescription());
                    }
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.RECURRING_PAYMENT_TYP)) {
            KeyValuesService boService = SpringContext.getBean(KeyValuesService.class);
            List<RecurringPaymentType> recurringPaymentTypeList = selectDAO.fetchAllRecurringPaymentType();
            if (CollectionUtils.isNotEmpty(recurringPaymentTypeList)) {
                for (RecurringPaymentType recurringPaymentTypeMethod : recurringPaymentTypeList) {
                    if (recurringPaymentTypeMethod.isActive()) {
                        fieldValues.put(recurringPaymentTypeMethod.getRecurringPaymentTypeCode(), recurringPaymentTypeMethod.getRecurringPaymentTypeCode());
                    }
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.FUNDING_CODE)) {
            List<OleFundCode> fundCodeList = selectDAO.fetchAllFundCode();
            if (CollectionUtils.isNotEmpty(fundCodeList)) {
                for (OleFundCode fundCode : fundCodeList) {
                    fieldValues.put(fundCode.getFundCode(), fundCode.getFundCode());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.CURRENCY_TYPE)) {
            List<OleCurrencyType> currencyTypeList = selectDAO.fetchAllCurrencyType();
            if (CollectionUtils.isNotEmpty(currencyTypeList)) {
                for (OleCurrencyType currencyType : currencyTypeList) {
                    fieldValues.put(currencyType.getCurrencyType(), currencyType.getCurrencyType());
                }
            }
        } else if (fieldName.equalsIgnoreCase(OleNGConstants.BatchProcess.FORMAT)){
            List<OleFormatType> formatTypeList=selectDAO.fetchAllFormatType();
            if(CollectionUtils.isNotEmpty(formatTypeList)){
                for(OleFormatType formatType:formatTypeList){
                    fieldValues.put(formatType.getFormatTypeName(),formatType.getFormatTypeName());
                }
            }
        }
        return fieldValues;
    }

    public List<String> getItemLocation() {
        List<String> locationList = new ArrayList<String>();
        List<KeyValue> options = new ArrayList<KeyValue>();
        Map parentCriteria1 = new HashMap();
        parentCriteria1.put(OLEConstants.OLEBatchProcess.LEVEL_CODE, OLEConstants.LOC_LEVEL_SHELVING);
        List<OleLocationLevel> oleLocationLevel = (List<OleLocationLevel>) getBusinessObjectService().findMatching(OleLocationLevel.class, parentCriteria1);
        String shelvingId = oleLocationLevel.get(0).getLevelId();
        Map parentCriteria = new HashMap();
        parentCriteria.put(OLEConstants.LEVEL_ID, shelvingId);
        Collection<OleLocation> oleLocationCollection = getBusinessObjectService().findMatching(OleLocation.class, parentCriteria);
        for (OleLocation oleLocation : oleLocationCollection) {
            String locationName = oleLocation.getLocationName();
            String levelCode = oleLocation.getLocationCode();
            boolean parentId = oleLocation.getParentLocationId() != null ? true : false;
            while (parentId) {
                Map criteriaMap = new HashMap();
                criteriaMap.put(OLEConstants.LOCATION_ID, oleLocation.getParentLocationId());
                OleLocation location = getBusinessObjectService().findByPrimaryKey(OleLocation.class,
                        criteriaMap);
                if (locationName != null) {
                    locationName = location.getLocationName() + OLEConstants.SLASH + locationName;
                }
                if (levelCode != null) {
                    levelCode = location.getLocationCode() + OLEConstants.SLASH + levelCode;
                }
                parentId = location.getParentLocationId() != null ? true : false;
                oleLocation = location;
            }
            options.add(new ConcreteKeyValue(levelCode, levelCode));
        }
        Map<String, String> map = new HashMap<String, String>();
        for (KeyValue option : options) {
            map.put(option.getKey(), option.getValue());
        }
        Map<String, Object> map1 = sortByLocation(map);
        for (Map.Entry<String, Object> entry : map1.entrySet()) {
            locationList.add((String) entry.getValue());
        }
        return locationList;
    }

    private Map<String, Object> sortByLocation(Map<String, String> parentCriteria) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<String> keyList = new ArrayList<String>(parentCriteria.keySet());
        List<String> valueList = new ArrayList<String>(parentCriteria.values());
        Set<String> sortedSet = new TreeSet<String>(valueList);
        Object[] sortedArray = sortedSet.toArray();
        int size = sortedArray.length;
        for (int i = 0; i < size; i++) {
            map.put(keyList.get(valueList.indexOf(sortedArray[i])), sortedArray[i]);
        }
        return map;
    }

    public List<KeyValue> getItemStatus() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleItemAvailableStatus> oleItemAvailableStatuses = selectDAO.fetchAllItemStatus();
        String excludeItemStatus = getParameter(OLEConstants.EXCLUDE_ITEM_STATUS);
        Map<String, String> map = new HashMap<>();
        if (excludeItemStatus != null && !excludeItemStatus.isEmpty()) {
            String[] itemStatusList = excludeItemStatus.split(",");
            for (String itemStatus : itemStatusList) {
                map.put(itemStatus, itemStatus);
            }
        }
        for (OleItemAvailableStatus type : oleItemAvailableStatuses) {
            if (type.isActive() && !map.containsKey(type.getItemAvailableStatusCode())) {
                options.add(new ConcreteKeyValue(type.getItemAvailableStatusCode(), type.getItemAvailableStatusName()));
            }
        }
        return options;
    }

    public String getParameter(String name) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.SELECT_NMSPC, OLEConstants.SELECT_CMPNT, name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter != null ? parameter.getValue() : null;
    }

}
