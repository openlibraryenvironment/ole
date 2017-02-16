package org.kuali.ole.oleng.dao.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.*;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatusRecord;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.oleng.dao.SelectDAO;
import org.kuali.ole.oleng.util.OrderImportFieldValuesUtil;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.businessobject.OLERequestorPatronDocument;
import org.kuali.ole.select.businessobject.OleFormatType;
import org.kuali.ole.select.businessobject.OleRequestSourceType;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.select.document.OLEPlatformRecordDocument;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.sys.businessobject.Building;
import org.kuali.ole.sys.businessobject.Room;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.*;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.krad.service.*;
import org.kuali.rice.krad.service.KeyValuesService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rajeshbabuk on 12/29/15.
 */
@Repository("SelectDAO")
@Scope("prototype")
public class SelectDAOImpl extends BusinessObjectServiceHelperUtil implements SelectDAO {

    private KeyValuesService keyValuesService;
    private OleSelectDocumentService oleSelectDocumentService;
    private OrderImportFieldValuesUtil orderImportFieldValuesUtil;

    @Override
    public Map<String, String> fetchOrderImportFieldValues(String fieldName) {
        return getOrderImportFieldValuesUtil().getMaintenanceDataByField(fieldName);
    }

    @Override
    public List<Organization> fetchAllOrganization() {
        return (List<Organization>) getBusinessObjectService().findAll(Organization.class);
    }

    @Override
    public List<Chart> fetchAllItemChartCode() {
        return (List<Chart>) getBusinessObjectService().findAll(Chart.class);
    }

    @Override
    public List<ContractManager> fetchAllContractManager() {
        return (List<ContractManager>) getBusinessObjectService().findAll(ContractManager.class);
    }

    @Override
    public List<PurchaseOrderType> fetchAllOrderType() {
        return (List<PurchaseOrderType>) getBusinessObjectService().findAll(PurchaseOrderType.class);
    }

    @Override
    public List<FundingSource> fetchAllFundingSource() {
        return (List<FundingSource>) getBusinessObjectService().findAll(FundingSource.class);
    }

    @Override
    public List<Building> fetchAllBuilding() {
        return (List<Building>) getBusinessObjectService().findAll(Building.class);
    }

    @Override
    public List<Room> fetchAllRoom() {
        return (List<Room>) getBusinessObjectService().findAll(Room.class);
    }

    @Override
    public List<PurchaseOrderVendorChoice> fetchAllVendorChoice() {
        return (List<PurchaseOrderVendorChoice>) getBusinessObjectService().findAll(PurchaseOrderVendorChoice.class);
    }

    @Override
    public List<PurchaseOrderCostSource> fetchAllCostSource() {
        return (List<PurchaseOrderCostSource>) getBusinessObjectService().findAll(PurchaseOrderCostSource.class);
    }

    @Override
    public List<String> fetchAllItemLocation() {
        return getOrderImportFieldValuesUtil().getItemLocation();
    }

    @Override
    public List<VendorDetail> fetchAllVendorDetail() {
        return (List<VendorDetail>) getBusinessObjectService().findAll(VendorDetail.class);
    }

    @Override
    public List<VendorAlias> fetchAllVendorAlias() {
        return (List<VendorAlias>) getBusinessObjectService().findAll(VendorAlias.class);
    }

    @Override
    public List<VendorCustomerNumber> fetchAllVendorCustomerNumber() {
        return (List<VendorCustomerNumber>) getBusinessObjectService().findAll(VendorCustomerNumber.class);
    }

    @Override
    public List<OLERequestorPatronDocument> fetchAllRequestor() {
        return getOleSelectDocumentService().getPatronDocumentListFromWebService();
    }

    @Override
    public List<OleItemAvailableStatus> fetchAllItemStatus() {
        return (List<OleItemAvailableStatus>) getBusinessObjectService().findAll(OleItemAvailableStatus.class);
    }

    @Override
    public List<Account> fetchAllAccount() {
        return (List<Account>) getBusinessObjectService().findAll(Account.class);
    }

    @Override
    public List<ObjectCode> fetchAllObjectCode() {
        return (List<ObjectCode>) getBusinessObjectService().findAll(ObjectCode.class);
    }

    @Override
    public List<OleRequestSourceType> fetchAllRequestSourceType() {
        return (List<OleRequestSourceType>) getBusinessObjectService().findAll(OleRequestSourceType.class);
    }

    @Override
    public List<PrincipalBo> fetchAllPrincipalBo() {
        return (List<PrincipalBo>) getBusinessObjectService().findAll(PrincipalBo.class);
    }

    @Override
    public List<PurchaseOrderTransmissionMethod> fetchAllPurchaseOrderTransmissionMethod() {
        return (List<PurchaseOrderTransmissionMethod>) getKeyValuesService().findAll(PurchaseOrderTransmissionMethod.class);
    }

    @Override
    public List<RecurringPaymentType> fetchAllRecurringPaymentType() {
        return (List<RecurringPaymentType>) getKeyValuesService().findAll(RecurringPaymentType.class);
    }

    @Override
    public List<OleFundCode> fetchAllFundCode() {
        return (List<OleFundCode>) getBusinessObjectService().findAll(OleFundCode.class);
    }

    @Override
    public List<OleCurrencyType> fetchAllCurrencyType() {
        return (List<OleCurrencyType>) getBusinessObjectService().findAll(OleCurrencyType.class);
    }

    @Override
    public List<OLEEResourceRecordDocument> fetchAllEResourceDocuments() {
        return (List<OLEEResourceRecordDocument>) getBusinessObjectService().findAll(OLEEResourceRecordDocument.class);
    }

    @Override
    public List<OLEPlatformRecordDocument> fetchAllPlatformRecordDocuments() {
        return (List<OLEPlatformRecordDocument>) getBusinessObjectService().findAll(OLEPlatformRecordDocument.class);
    }

    @Override
    public List<OleFormatType> fetchAllFormatType(){
        return (List<OleFormatType>) getBusinessObjectService().findAll(OleFormatType.class);
    }

    @Override
    public Organization getOrganizationByChartCode(String chartCode) {
        Map<String, String> chartCodeMap = new HashMap<>();
        chartCodeMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, chartCode);
        List<Organization> organizations = (List) getBusinessObjectService().findMatching(Organization.class, chartCodeMap);
        if (CollectionUtils.isNotEmpty(organizations)) {
            return organizations.get(0);
        }
        return null;
    }

    @Override
    public Organization getOrganizationByOrgCode(String orgCode) {
        Map<String,String> orgCodeMap = new HashMap<>();
        orgCodeMap.put(OLEConstants.OLEBatchProcess.ORGANIZATION_CODE, orgCode);
        List<Organization> organizations = (List) getBusinessObjectService().findMatching(Organization.class, orgCodeMap);
        if (CollectionUtils.isNotEmpty(organizations)) {
            return organizations.get(0);
        }
        return null;
    }

    @Override
    public Organization getOrganizationByChartAndOrgCode(String chartCode, String orgCode) {
        Map<String,String> orgCodeMap = new HashMap<>();
        orgCodeMap.put(OLEConstants.OLEBatchProcess.ORGANIZATION_CODE, orgCode);
        orgCodeMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, chartCode);
        List<Organization> organizations = (List) getBusinessObjectService().findMatching(Organization.class, orgCodeMap);
        if (CollectionUtils.isNotEmpty(organizations)) {
            return organizations.get(0);
        }
        return null;
    }

    @Override
    public VendorDetail getVendorDetailByVendorNumber(String vendorNumber){
        String[] vendorDetail = vendorNumber.split("-");
        if(vendorDetail.length == 2 && StringUtils.isNotBlank(vendorDetail[0]) && org.apache.commons.lang.StringUtils.isNotBlank(vendorDetail[1])) {
            String vendorHeaderGeneratedIdentifier = vendorDetail[0];
            String vendorDetailAssignedIdentifier = vendorDetail[1];
            if (NumberUtils.isDigits(vendorHeaderGeneratedIdentifier) && NumberUtils.isDigits(vendorDetailAssignedIdentifier)) {
                Map<String, Integer> vendorMap = new HashMap<>();
                vendorMap.put(OLEConstants.VENDOR_HEADER_GENERATED_ID, Integer.parseInt(vendorHeaderGeneratedIdentifier));
                vendorMap.put(OLEConstants.VENDOR_DETAILED_ASSIGNED_ID, Integer.parseInt(vendorDetailAssignedIdentifier));
                List<VendorDetail> vendorDetailList = (List) getBusinessObjectService().findMatching(VendorDetail.class, vendorMap);
                if (CollectionUtils.isNotEmpty(vendorDetailList)) {
                    return vendorDetailList.get(0);
                }
            }
        }
        return null;
    }

    @Override
    public VendorAlias getVendorAliasByName(String vendorAliasName){
        Map<String, String> vendorAliasMap = new HashMap<>();
        vendorAliasMap.put(OLEConstants.OLEBatchProcess.VENDOR_ALIAS_NAME, vendorAliasName);
        List<VendorAlias> vendorAliasList = (List) getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
        if (CollectionUtils.isNotEmpty(vendorAliasList)){
            return vendorAliasList.get(0);
        }
        return null;
    }

    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        Map<String, String> accountNumberMap = new HashMap<>();
        accountNumberMap.put(OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER, accountNumber);
        List<Account> accountNumberList = (List) getLookupService().findCollectionBySearchHelper(Account.class, accountNumberMap, true);
        if (CollectionUtils.isNotEmpty(accountNumberList)) {
            return accountNumberList.get(0);
        }
        return null;
    }

    @Override
    public Building getBuildingByBuildingCode(String buildingCode) {
        Map<String, String> buildingCodeMap = new HashMap<>();
        buildingCodeMap.put(OLEConstants.OLEBatchProcess.BUILDING_CODE, buildingCode);
        List<Building> buildingList = (List) getBusinessObjectService().findMatching(Building.class, buildingCodeMap);
        if (CollectionUtils.isNotEmpty(buildingList)) {
            return buildingList.get(0);
        }
        return null;
    }

    @Override
    public Room getRoomByBuildingRoomNumber(String buildingRoomNumber) {
        Map<String, String> deliveryBuildingRoomNumberMap = new HashMap<>();
        deliveryBuildingRoomNumberMap.put(OLEConstants.BUILDING_ROOM_NUMBER, buildingRoomNumber);
        List<Room> roomList = (List) getBusinessObjectService().findMatching(Room.class, deliveryBuildingRoomNumberMap);
        if (CollectionUtils.isNotEmpty(roomList)) {
            return roomList.get(0);
        }
        return null;
    }

    @Override
    public PurchaseOrderType getPurchaseOrderTypeByOrderType(String orderType) {
        Map<String,String> orderTypeMap = new HashMap<>();
        orderTypeMap.put(OLEConstants.OLEBatchProcess.PURCHASE_ORDER_TYPE, orderType);
        List<PurchaseOrderType> purchaseOrderTypeList = (List) getBusinessObjectService().findMatching(PurchaseOrderType.class, orderTypeMap);
        if (CollectionUtils.isNotEmpty(purchaseOrderTypeList)){
            return purchaseOrderTypeList.get(0);
        }
        return null;
    }

    @Override
    public Chart getChartByChartCode(String chartOfAccountsCode) {
        Map<String,String> itemChartCodeMap = new HashMap<>();
        itemChartCodeMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, chartOfAccountsCode);
        List<Chart> chartList = (List) getBusinessObjectService().findMatching(Chart.class, itemChartCodeMap);
        if (CollectionUtils.isNotEmpty(chartList)){
            return chartList.get(0);
        }
        return null;
    }

    @Override
    public ObjectCode getObjectCodeByCode(String financialObjectCode) {
        Map<String,String> objectCodeMap = new HashMap<>();
        objectCodeMap.put(OLEConstants.OLEBatchProcess.OBJECT_CODE, financialObjectCode);
        List<ObjectCode> objectCodeList = (List) getBusinessObjectService().findMatching(ObjectCode.class, objectCodeMap);
        if (CollectionUtils.isNotEmpty(objectCodeList)){
            return objectCodeList.get(0);
        }
        return null;
    }

    @Override
    public PurchaseOrderCostSource getPurchaseOrderCostSourceByCode(String purchaseOrderCostSourceCode) {
        Map<String,String> costSourceMap = new HashMap<>();
        costSourceMap.put(OLEConstants.OLEBatchProcess.PO_COST_SOURCE_CODE, purchaseOrderCostSourceCode);
        List<PurchaseOrderCostSource> purchaseOrderCostSourceList= (List) getBusinessObjectService().findMatching(PurchaseOrderCostSource.class, costSourceMap);
        if (CollectionUtils.isNotEmpty(purchaseOrderCostSourceList)){
            return purchaseOrderCostSourceList.get(0);
        }
        return null;
    }

    @Override
    public PurchaseOrderTransmissionMethod getPurchaseOrderTransmissionMethodByDesc(String purchaseOrderTransmissionMethodDesc) {
        Map<String,String> methodOfPOTransmissionMap = new HashMap<>();
        methodOfPOTransmissionMap.put(OLEConstants.OLEBatchProcess.PO_TRANSMISSION_METHOD_DESC, purchaseOrderTransmissionMethodDesc);
        List<PurchaseOrderTransmissionMethod> purchaseOrderTransmissionMethodList = (List) getBusinessObjectService().findMatching(PurchaseOrderTransmissionMethod.class, methodOfPOTransmissionMap);
        if (CollectionUtils.isNotEmpty(purchaseOrderTransmissionMethodList)){
            return purchaseOrderTransmissionMethodList.get(0);
        }
        return null;
    }

    @Override
    public Building getBuildingByCampusCode(String campusCode) {
        Map<String,String> campusCodeMap = new HashMap<>();
        campusCodeMap.put(OLEConstants.OLEBatchProcess.CAMPUS_CODE, campusCode);
        List<Building> campusList= (List) getBusinessObjectService().findMatching(Building.class, campusCodeMap);
        if (CollectionUtils.isNotEmpty(campusList)){
            return campusList.get(0);
        }
        return null;
    }

    @Override
    public FundingSource getFundingSourceByCode(String fundingSourceCode) {
        Map<String,String> fundingSourceMap = new HashMap<>();
        fundingSourceMap.put(OLEConstants.OLEBatchProcess.FUNDING_SOURCE_CODE, fundingSourceCode);
        List<FundingSource> fundingSourceList = (List) getBusinessObjectService().findMatching(FundingSource.class, fundingSourceMap);
        if (CollectionUtils.isNotEmpty(fundingSourceList)){
            return fundingSourceList.get(0);
        }
        return null;
    }

    @Override
    public ContractManager getContractManagerByName(String contractManager) {
        Map<String,String> contractManagerMap = new HashMap<>();
        contractManagerMap.put(OLEConstants.OLEBatchProcess.CONTRACT_MANAGER_NAME, contractManager);
        List<ContractManager> contractManagerList = (List) getBusinessObjectService().findMatching(ContractManager.class, contractManagerMap);
        if (CollectionUtils.isNotEmpty(contractManagerList)){
            return contractManagerList.get(0);
        }
        return null;
    }

    @Override
    public PurchaseOrderVendorChoice getPurchaseOrderVendorChoiceByCode(String purchaseOrderVendorChoiceCode) {
        Map<String,String> vendorChoiceMap = new HashMap<>();
        vendorChoiceMap.put(OLEConstants.OLEBatchProcess.PO_VENDOR_CHOICE_CODE, purchaseOrderVendorChoiceCode);
        List<PurchaseOrderVendorChoice> purchaseOrderVendorChoiceList = (List) getBusinessObjectService().findMatching(PurchaseOrderVendorChoice.class, vendorChoiceMap);
        if (CollectionUtils.isNotEmpty(purchaseOrderVendorChoiceList)){
            return purchaseOrderVendorChoiceList.get(0);
        }
        return null;
    }

    @Override
    public OLERequestorPatronDocument getRequestorPatronDocumentByName(String requestorName) {
        String[] requestorNames = requestorName.split(", ");
        if (requestorNames.length == 2) {
            String lastName = requestorNames[0];
            String firstName = requestorNames[1];
            Map<String, String> requestorNameMap = new HashMap<>();
            requestorNameMap.put(org.kuali.ole.sys.OLEConstants.OlePersonRequestorLookupable.FIRST_NAME, firstName);
            requestorNameMap.put(org.kuali.ole.sys.OLEConstants.OlePersonRequestorLookupable.LAST_NAME, lastName);
            List<OLERequestorPatronDocument> oleRequestorPatronDocumentList = getOleSelectDocumentService().getPatronDocumentListFromWebService();
            if (CollectionUtils.isNotEmpty(oleRequestorPatronDocumentList)) {
                for (OLERequestorPatronDocument oleRequestorPatronDocument : oleRequestorPatronDocumentList) {
                    if (oleRequestorPatronDocument.getFirstName().equalsIgnoreCase(firstName) && oleRequestorPatronDocument.getLastName().equalsIgnoreCase(lastName)) {
                        return oleRequestorPatronDocument;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public OleRequestSourceType getRequestSourceTypeByType(String requestSourceType) {
        Map<String,String> requestSourceMap = new HashMap<>();
        requestSourceMap.put(OLEConstants.OLEBatchProcess.REQUEST_SRC,requestSourceType);
        List<OleRequestSourceType> requestSourceList = (List) getBusinessObjectService().findMatching(OleRequestSourceType.class, requestSourceMap);
        if (CollectionUtils.isNotEmpty(requestSourceList)){
            return requestSourceList.get(0);
        }
        return null;
    }

    @Override
    public Room getRoom(String buildingCode, String campusCode, String buildingRoomNumber) {
        Map<String,String> deliveryMap = new HashMap<>();
        deliveryMap.put(OLEConstants.OLEBatchProcess.BUILDING_CODE, buildingCode);
        deliveryMap.put(OLEConstants.OLEBatchProcess.CAMPUS_CODE, campusCode);
        deliveryMap.put(OLEConstants.BUILDING_ROOM_NUMBER, buildingRoomNumber);
        List<Room> roomList = (List) getBusinessObjectService().findMatching(Room.class, deliveryMap);
        if (CollectionUtils.isNotEmpty(roomList)){
            return roomList.get(0);
        }
        return null;
    }

    @Override
    public Account getAccountByAccountNumberAndChartCode(String accountNumber, String itemChartCode) {
        Map<String,String> accLinesMap = new HashMap<>();
        accLinesMap.put(OLEConstants.ACCOUNT_NUMBER,accountNumber);
        accLinesMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE,itemChartCode);
        List<Account> accountList = (List) getBusinessObjectService().findMatching(Account.class, accLinesMap);
        if (CollectionUtils.isNotEmpty(accountList)){
            return accountList.get(0);
        }
        return null;
    }

    @Override
    public ObjectCode getObjectCodeByChartCodeAndObjectCode(String itemChartCode, String financialObjectCode) {
        Map<String,String> accLinesMap = new HashMap<>();
        accLinesMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE,itemChartCode);
        accLinesMap.put(OLEConstants.OLEBatchProcess.OBJECT_CODE,financialObjectCode);
        List<ObjectCode> objectCodeList = (List) getBusinessObjectService().findMatching(ObjectCode.class, accLinesMap);
        if (CollectionUtils.isNotEmpty(objectCodeList)){
            return objectCodeList.get(0);
        }
        return null;
    }

    @Override
    public RecurringPaymentType getRecurringPaymentTypeByCode(String recurringPaymentType) {
        Map<String,String> recurringPaymentTypeMap = new HashMap<>();
        recurringPaymentTypeMap.put(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_TYP_CODE, recurringPaymentType);
        List<RecurringPaymentType> recurringPaymentTypeList = (List) getBusinessObjectService().findMatching(RecurringPaymentType.class, recurringPaymentTypeMap);
        if (CollectionUtils.isNotEmpty(recurringPaymentTypeList)){
            return recurringPaymentTypeList.get(0);
        }
        return null;
    }

    @Override
    public OleFundCode getOleFundCodeByCode(String fundCode) {
        Map<String,String> fundCodeMap = new HashMap<>();
        fundCodeMap.put(OLEConstants.OLEEResourceRecord.FUND_CODE, fundCode);
        List<OleFundCode> fundCodeList = (List) getBusinessObjectService().findMatching(OleFundCode.class, fundCodeMap);
        if (CollectionUtils.isNotEmpty(fundCodeList)){
            return fundCodeList.get(0);
        }
        return null;
    }

    @Override
    public ItemStatusRecord fetchItemStatusByName(String itemStatusCode) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(OLEConstants.NAME, itemStatusCode);
        List<ItemStatusRecord> matching = (List<ItemStatusRecord>) getBusinessObjectService().findMatching(ItemStatusRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }

    @Override
    public VendorCustomerNumber getVendorCustomerNumberByNumber(String vendorCustomerNumber) {
        Map<String,String> vendorCustomerMap = new HashMap<>();
        vendorCustomerMap.put(OLEConstants.OLEBatchProcess.VENDOR_CUST_NBR, vendorCustomerNumber);
        List<VendorCustomerNumber> vendorCustomerList = (List) getBusinessObjectService().findMatching(VendorCustomerNumber.class, vendorCustomerMap);
        if (CollectionUtils.isNotEmpty(vendorCustomerList)){
            return vendorCustomerList.get(0);
        }
        return null;
    }

    @Override
    public OLEDonor getOLEDonorByCode(String donorCode) {
        Map<String, String> donorCodeMap = new HashMap<>();
        donorCodeMap.put(OLEConstants.DONOR_CODE, donorCode);
        List<OLEDonor> donorCodeList = (List) getBusinessObjectService().findMatching(OLEDonor.class, donorCodeMap);
        if (CollectionUtils.isNotEmpty(donorCodeList)){
            return donorCodeList.get(0);
        }
        return null;
    }

    @Override
    public OleCurrencyType getCurrencyType(String currencyType) {
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("currencyType", currencyType);
        List<OleCurrencyType> oleCurrencyTypes = (List) getBusinessObjectService().findMatching(OleCurrencyType.class, parameterMap);
        if (CollectionUtils.isNotEmpty(oleCurrencyTypes)){
            return oleCurrencyTypes.get(0);
        }
        return null;
    }

    @Override
    public OleFormatType getFormatTypeByName(String formatTypeName){

        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("formatTypeName", formatTypeName);
        List<OleFormatType> formatTypes = (List) getBusinessObjectService().findMatching(OleFormatType.class,parameterMap);
        if(CollectionUtils.isNotEmpty(formatTypes)){
            return  formatTypes.get(0);
        }
        return null;
    }

    public KeyValuesService getKeyValuesService() {
        if(null == keyValuesService){
            keyValuesService = SpringContext.getBean(KeyValuesService.class);
        }
        return keyValuesService;
    }

    public OleSelectDocumentService getOleSelectDocumentService() {
        if (oleSelectDocumentService == null) {
            oleSelectDocumentService = SpringContext.getBean(OleSelectDocumentService.class);
        }
        return oleSelectDocumentService;
    }

    public OrderImportFieldValuesUtil getOrderImportFieldValuesUtil() {
        if(null == orderImportFieldValuesUtil){
            orderImportFieldValuesUtil = new OrderImportFieldValuesUtil(this);
        }
        return orderImportFieldValuesUtil;
    }

    public LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }
}
