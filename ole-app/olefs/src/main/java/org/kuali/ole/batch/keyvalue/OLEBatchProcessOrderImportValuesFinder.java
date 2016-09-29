package org.kuali.ole.batch.keyvalue;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileConstantsBo;
import org.kuali.ole.coa.businessobject.*;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.select.businessobject.OLERequestorPatronDocument;
import org.kuali.ole.select.businessobject.OleItemPriceSource;
import org.kuali.ole.select.businessobject.OleRequestSourceType;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
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
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: syedk
 * Date: 6/6/14
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessOrderImportValuesFinder extends UifKeyValuesFinderBase {

    private BusinessObjectService businessObjectService;
    private OleSelectDocumentService oleSelectDocumentService;

    public BusinessObjectService getBusinessObjectService() {
        if(businessObjectService==null)
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        return businessObjectService;
    }

    public OleSelectDocumentService getOleSelectDocumentService() {
        if (oleSelectDocumentService == null) {
            oleSelectDocumentService = SpringContext.getBean(OleSelectDocumentService.class);
        }
        return oleSelectDocumentService;
    }

    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) model;
        OLEBatchProcessProfileConstantsBo oleBatchProcessProfileConstantsBo = null;
        if (!CollectionUtils.isEmpty(maintenanceForm.getNewCollectionLines())) {
            oleBatchProcessProfileConstantsBo = (OLEBatchProcessProfileConstantsBo) maintenanceForm.getNewCollectionLines().get("document.newMaintainableObject.dataObject.oleBatchProcessProfileConstantsList");
        }
        if (oleBatchProcessProfileConstantsBo != null) {
            String dataType = oleBatchProcessProfileConstantsBo.getDataType();
            if (StringUtils.isNotBlank(dataType)) {
                String attributeName = oleBatchProcessProfileConstantsBo.getAttributeName();
                String oldAttributeName = oleBatchProcessProfileConstantsBo.getOldAttributeName();
                if(!StringUtils.isBlank(attributeName)){
                    if(oldAttributeName == null || !attributeName.equalsIgnoreCase(oldAttributeName)){
                        oleBatchProcessProfileConstantsBo.setOldAttributeName(attributeName);
                        if (dataType.equalsIgnoreCase(OLEConstants.OLEBatchProcess.ORDER_IMPORT)) {
                            if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.CHART_CODE)){
                                List<Organization> chartCodeList = (List<Organization>) KRADServiceLocator.getBusinessObjectService().findAll(Organization.class);
                                if(chartCodeList != null && chartCodeList.size()>0){
                                    Set<KeyValue> chartCodes  = new HashSet<>();
                                    for(Organization chartCode : chartCodeList) {
                                        chartCodes.add(new ConcreteKeyValue(chartCode.getChartOfAccountsCode(), chartCode.getChartOfAccountsCode()));
                                    }
                                    keyValues.addAll(chartCodes);
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.ITEM_CHART_CODE)){
                                List<Chart> itemChartCodeList = (List<Chart>) KRADServiceLocator.getBusinessObjectService().findAll(Chart.class);
                                for (Chart itemChartCode : itemChartCodeList) {
                                    keyValues.add(new ConcreteKeyValue(itemChartCode.getChartOfAccountsCode(), itemChartCode.getChartOfAccountsCode()));
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.ORG_CODE)){
                                List<Organization> organizationCodeList = (List<Organization>) KRADServiceLocator.getBusinessObjectService().findAll(Organization.class);
                                if(organizationCodeList != null && organizationCodeList.size() >0){
                                    Set<KeyValue> orgCodes  = new HashSet<>();
                                    for (Organization organizationCode : organizationCodeList) {
                                        orgCodes.add(new ConcreteKeyValue(organizationCode.getOrganizationCode(), organizationCode.getOrganizationCode()));
                                    }
                                    keyValues.addAll(orgCodes);
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.CONTRACT_MANAGER)){
                                List<ContractManager> contractManagerList = (List<ContractManager>) KRADServiceLocator.getBusinessObjectService().findAll(ContractManager.class);
                                for (ContractManager contractManager : contractManagerList) {
                                    keyValues.add(new ConcreteKeyValue(contractManager.getContractManagerName(), contractManager.getContractManagerName()));
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.ORDER_TYPE)){
                                List<PurchaseOrderType> purchaseOrderTypeList = (List<PurchaseOrderType>) KRADServiceLocator.getBusinessObjectService().findAll(PurchaseOrderType.class);
                                for (PurchaseOrderType purchaseOrderType : purchaseOrderTypeList) {
                                    keyValues.add(new ConcreteKeyValue(purchaseOrderType.getPurchaseOrderType(), purchaseOrderType.getPurchaseOrderType()));
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.FUNDING_SOURCE)){
                                List<FundingSource> fundingSourceList = (List<FundingSource>) KRADServiceLocator.getBusinessObjectService().findAll(FundingSource.class);
                                for (FundingSource fundingSource : fundingSourceList) {
                                    keyValues.add(new ConcreteKeyValue(fundingSource.getFundingSourceCode(), fundingSource.getFundingSourceCode()));
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.DELIVERY_CAMPUS_CODE)){
                                List<Building> campusList = (List<Building>) KRADServiceLocator.getBusinessObjectService().findAll(Building.class);
                                if(campusList != null && campusList.size() >0){
                                    Set<KeyValue> chartCodes  = new HashSet<>();
                                    for (Building campus : campusList) {
                                        chartCodes.add(new ConcreteKeyValue(campus.getCampusCode(), campus.getCampusCode()));
                                    }
                                    keyValues.addAll(chartCodes);
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.BUILDING_CODE)){
                                List<Building> buildingList = (List<Building>) KRADServiceLocator.getBusinessObjectService().findAll(Building.class);
                                for (Building building : buildingList) {
                                    keyValues.add(new ConcreteKeyValue(building.getBuildingCode(), building.getBuildingCode()));
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.DELIVERY_BUILDING_ROOM_NUMBER)){
                                List<Room> roomList = (List<Room>) KRADServiceLocator.getBusinessObjectService().findAll(Room.class);
                                if (roomList!=null && roomList.size()>0) {
                                    Set<KeyValue> roomNos = new HashSet<>();
                                    for (Room room : roomList) {
                                        roomNos.add(new ConcreteKeyValue(room.getBuildingRoomNumber(),room.getBuildingRoomNumber()));
                                    }
                                    keyValues.addAll(roomNos);
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.VENDOR_CHOICE)){
                                List<PurchaseOrderVendorChoice> purchaseOrderVendorChoiceList = (List<PurchaseOrderVendorChoice>) KRADServiceLocator.getBusinessObjectService().findAll(PurchaseOrderVendorChoice.class);
                                for (PurchaseOrderVendorChoice purchaseOrderVendorChoice : purchaseOrderVendorChoiceList) {
                                    keyValues.add(new ConcreteKeyValue(purchaseOrderVendorChoice.getPurchaseOrderVendorChoiceCode(), purchaseOrderVendorChoice.getPurchaseOrderVendorChoiceCode()));
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.COST_SOURCE)){
                                List<PurchaseOrderCostSource> purchaseOrderCostSourceList = (List<PurchaseOrderCostSource>) KRADServiceLocator.getBusinessObjectService().findAll(PurchaseOrderCostSource.class);
                                for (PurchaseOrderCostSource purchaseOrderCostSource : purchaseOrderCostSourceList) {
                                    keyValues.add(new ConcreteKeyValue(purchaseOrderCostSource.getPurchaseOrderCostSourceCode(), purchaseOrderCostSource.getPurchaseOrderCostSourceCode()));
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION)){
                                List<String> locationList = getItemLocation();
                                for(int locationCount = 0;locationCount < locationList.size();locationCount++) {
                                    keyValues.add(new ConcreteKeyValue(locationList.get(locationCount),locationList.get(locationCount)));
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.VENDOR_NUMBER)){
                                List<VendorDetail> vendorDetailList = (List<VendorDetail>) KRADServiceLocator.getBusinessObjectService().findAll(VendorDetail.class);
                                for (VendorDetail vendorDetail : vendorDetailList) {
                                    keyValues.add(new ConcreteKeyValue(vendorDetail.getVendorHeaderGeneratedIdentifier() + "-" + vendorDetail.getVendorDetailAssignedIdentifier(), vendorDetail.getVendorHeaderGeneratedIdentifier() + "-" + vendorDetail.getVendorDetailAssignedIdentifier()));
                                }
                            }
                            else if (attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.VENDOR_ALIAS_NAME)) {
                                List<VendorAlias> vendorAliasList = (List<VendorAlias>) KRADServiceLocator.getBusinessObjectService().findAll(VendorAlias.class);
                                if (vendorAliasList != null && vendorAliasList.size() > 0) {
                                    Set<KeyValue> vendorAliases = new HashSet<>();
                                    for (VendorAlias vendorAlias : vendorAliasList) {
                                        vendorAliases.add(new ConcreteKeyValue(vendorAlias.getVendorAliasName(), vendorAlias.getVendorAliasName()));
                                    }
                                    keyValues.addAll(vendorAliases);
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.VENDOR_CUST_NBR)){
                                List<VendorCustomerNumber> vendorCustomerList = (List<VendorCustomerNumber>) KRADServiceLocator.getBusinessObjectService().findAll(VendorCustomerNumber.class);
                                for (VendorCustomerNumber vendorCustomer : vendorCustomerList) {
                                    keyValues.add(new ConcreteKeyValue(vendorCustomer.getVendorCustomerNumber(), vendorCustomer.getVendorCustomerNumber()));
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.REQUESTOR_NAME)){
                                List<OLERequestorPatronDocument> olePatronDocumentList = getOleSelectDocumentService().getPatronDocumentListFromWebService();
                                if(olePatronDocumentList != null && olePatronDocumentList.size()>0){
                                    for(int recCount = 0;recCount < olePatronDocumentList.size();recCount++) {
                                        keyValues.add(new ConcreteKeyValue(olePatronDocumentList.get(recCount).getLastName() + ", " +  olePatronDocumentList.get(recCount).getFirstName(),olePatronDocumentList.get(recCount).getLastName() + ", " +  olePatronDocumentList.get(recCount).getFirstName()));
                                    }
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.ITEM_STATUS)){
                                List<KeyValue> itemStatusList = getItemStatus();
                                for(int itemStatusCount = 0; itemStatusCount < itemStatusList.size(); itemStatusCount++){
                                    keyValues.add(new ConcreteKeyValue(itemStatusList.get(itemStatusCount).getKey(),itemStatusList.get(itemStatusCount).getKey()));
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.DISCOUNT_TYPE)){
                                keyValues.add(new ConcreteKeyValue("%", "%"));
                                keyValues.add(new ConcreteKeyValue("#", "#"));
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER)){
                                List<Account> accountNumberList = (List<Account>) KRADServiceLocator.getBusinessObjectService().findAll(Account.class);
                                for (Account account : accountNumberList) {
                                    keyValues.add(new ConcreteKeyValue(account.getAccountNumber(), account.getAccountNumber()));
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.OBJECT_CODE)){
                                List<ObjectCode> objectCodeList = (List<ObjectCode>) KRADServiceLocator.getBusinessObjectService().findAll(ObjectCode.class);
                                if(objectCodeList != null && objectCodeList.size() >0){
                                    Set<KeyValue> objectCodes  = new HashSet<>();
                                    for (ObjectCode objectCode : objectCodeList) {
                                        objectCodes.add(new ConcreteKeyValue(objectCode.getFinancialObjectCode(), objectCode.getFinancialObjectCode()));
                                    }
                                    keyValues.addAll(objectCodes);
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.REQUEST_SRC)){
                                List<OleRequestSourceType> requestSourceTypeList = (List<OleRequestSourceType>) KRADServiceLocator.getBusinessObjectService().findAll(OleRequestSourceType.class);
                                for (OleRequestSourceType requestSourceType : requestSourceTypeList) {
                                    keyValues.add(new ConcreteKeyValue(requestSourceType.getRequestSourceType(),requestSourceType.getRequestSourceType()));
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.ASSIGN_TO_USER)){
                                List<PrincipalBo> assignedUserList = (List<PrincipalBo>) KRADServiceLocator.getBusinessObjectService().findAll(PrincipalBo.class);
                                for (PrincipalBo assignedUser : assignedUserList) {
                                    keyValues.add(new ConcreteKeyValue(assignedUser.getPrincipalName(), assignedUser.getPrincipalName()));
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.METHOD_OF_PO_TRANSMISSION)){
                                KeyValuesService boService = SpringContext.getBean(KeyValuesService.class);
                                List<PurchaseOrderTransmissionMethod> transmissionMethodList = (List<PurchaseOrderTransmissionMethod>) boService.findAll(PurchaseOrderTransmissionMethod.class);
                                for (PurchaseOrderTransmissionMethod transmissionMethod : transmissionMethodList) {
                                    if(transmissionMethod.isDisplayToUser()){
                                        keyValues.add(new ConcreteKeyValue(transmissionMethod.getPurchaseOrderTransmissionMethodDescription(), transmissionMethod.getPurchaseOrderTransmissionMethodDescription()));
                                    }
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_TYP)){
                                KeyValuesService boService = SpringContext.getBean(KeyValuesService.class);
                                List<RecurringPaymentType> recurringPaymentTypeList = (List<RecurringPaymentType>) boService.findAll(RecurringPaymentType.class);
                                for (RecurringPaymentType recurringPaymentTypeMethod : recurringPaymentTypeList) {
                                    if(recurringPaymentTypeMethod.isActive()){
                                        keyValues.add(new ConcreteKeyValue(recurringPaymentTypeMethod.getRecurringPaymentTypeCode(), recurringPaymentTypeMethod.getRecurringPaymentTypeDescription()));
                                    }
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.RECEIVING_REQUIRED) ||
                                    attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.USE_TAX_INDICATOR) ||
                                    attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.PREQ_POSITIVE_APPROVAL_REQ) ||
                                    attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.PO_CONFIRMATION_INDICATOR) ||
                                    attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.ROUTE_TO_REQUESTOR)){
                                keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.TRUE,OLEConstants.OLEBatchProcess.TRUE));
                                keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.FALSE,OLEConstants.OLEBatchProcess.FALSE));
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.ITEM_PRICE_SOURCE)){
                                List<OleItemPriceSource> itemPriceSourceList = (List<OleItemPriceSource>) getBusinessObjectService().findAll(OleItemPriceSource.class);
                                for (OleItemPriceSource itemPriceSource : itemPriceSourceList) {
                                    keyValues.add(new ConcreteKeyValue(itemPriceSource.getItemPriceSource(), itemPriceSource.getItemPriceSource()));
                                }
                            } else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.REQUISITION_SOURCE)){
                                List<RequisitionSource> requisitionSourceList = (List<RequisitionSource>) getBusinessObjectService().findAll(RequisitionSource.class);
                                for (RequisitionSource requisitionSource : requisitionSourceList) {
                                    keyValues.add(new ConcreteKeyValue(requisitionSource.getRequisitionSourceCode(), requisitionSource.getRequisitionSourceCode()));
                                }
                            }
                            else if (attributeName.equalsIgnoreCase(OLEConstants.OLEEResourceRecord.FUND_CODE)) {
                                List<OleFundCode> fundCodeList = (List<OleFundCode>) KRADServiceLocator.getBusinessObjectService().findAll(OleFundCode.class);
                                if (fundCodeList != null && fundCodeList.size() > 0) {
                                    Set<KeyValue> fundCodes = new HashSet<>();
                                    for (OleFundCode fundCode : fundCodeList) {
                                        fundCodes.add(new ConcreteKeyValue(fundCode.getFundCode(), fundCode.getFundCode()));
                                    }
                                    keyValues.addAll(fundCodes);
                                }
                            }
                        }
                        else if (dataType.equalsIgnoreCase(OLEConstants.OLEBatchProcess.INVOICE_IMPORT)) {
                            if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER)){
                                List<Account> accountNumberList = (List<Account>) KRADServiceLocator.getBusinessObjectService().findAll(Account.class);
                                for (Account account : accountNumberList) {
                                    keyValues.add(new ConcreteKeyValue(account.getAccountNumber(), account.getAccountNumber()));
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.OBJECT_CODE)){
                                List<ObjectCode> objectCodeList = (List<ObjectCode>) KRADServiceLocator.getBusinessObjectService().findAll(ObjectCode.class);
                                if(objectCodeList != null && objectCodeList.size() >0){
                                    Set<KeyValue> objectCodes  = new HashSet<>();
                                    for (ObjectCode objectCode : objectCodeList) {
                                        objectCodes.add(new ConcreteKeyValue(objectCode.getFinancialObjectCode(), objectCode.getFinancialObjectCode()));
                                    }
                                    keyValues.addAll(objectCodes);
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.VENDOR_NUMBER)){
                                List<VendorDetail> vendorDetailList = (List<VendorDetail>) KRADServiceLocator.getBusinessObjectService().findAll(VendorDetail.class);
                                for (VendorDetail vendorDetail : vendorDetailList) {
                                    keyValues.add(new ConcreteKeyValue(vendorDetail.getVendorHeaderGeneratedIdentifier() + "-" + vendorDetail.getVendorDetailAssignedIdentifier(), vendorDetail.getVendorHeaderGeneratedIdentifier() + "-" + vendorDetail.getVendorDetailAssignedIdentifier()));
                                }
                            }
                            else if(attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.CURRENCY_TYPE)){
                                List<OleCurrencyType> currencyTypeList = (List<OleCurrencyType>) KRADServiceLocator.getBusinessObjectService().findAll(OleCurrencyType.class);
                                for (OleCurrencyType currencyType : currencyTypeList) {
                                    keyValues.add(new ConcreteKeyValue(currencyType.getCurrencyType(), currencyType.getCurrencyType()));
                                }
                            }
                            else if (attributeName.equalsIgnoreCase(OLEConstants.OLEEResourceRecord.FUND_CODE)) {
                                List<OleFundCode> fundCodeList = (List<OleFundCode>) KRADServiceLocator.getBusinessObjectService().findAll(OleFundCode.class);
                                if (fundCodeList != null && fundCodeList.size() > 0) {
                                    for (OleFundCode fundCode : fundCodeList) {
                                        keyValues.add(new ConcreteKeyValue(fundCode.getFundCode(), fundCode.getFundCode()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        Collections.sort(keyValues, new Comparator<KeyValue>() {
            public int compare(KeyValue keyValue1, KeyValue keyValue2) {
                return keyValue1.getValue().compareTo(keyValue2.getValue());

            }
        });
    }
        return keyValues;
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
        Collection<OleItemAvailableStatus> oleItemAvailableStatuses = KRADServiceLocator.getBusinessObjectService().findAll(OleItemAvailableStatus.class);
        String excludeItemStatus = getParameter(OLEConstants.EXCLUDE_ITEM_STATUS);
        Map<String,String> map = new HashMap<>();
        if(excludeItemStatus!=null && !excludeItemStatus.isEmpty()){
            String[] itemStatusList = excludeItemStatus.split(",");
            for(String itemStatus : itemStatusList){
                map.put(itemStatus,itemStatus);
            }
        }
        for (OleItemAvailableStatus type : oleItemAvailableStatuses) {
            if (type.isActive() && !map.containsKey(type.getItemAvailableStatusCode())) {
                options.add(new ConcreteKeyValue(type.getItemAvailableStatusCode(), type.getItemAvailableStatusName()));
            }
        }
        return options;
    }

    public String getParameter(String name){
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.SELECT_NMSPC, OLEConstants.SELECT_CMPNT,name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter!=null?parameter.getValue():null;
    }
}
