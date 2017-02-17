package org.kuali.ole.oleng.dao;

import org.kuali.ole.coa.businessobject.*;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatusRecord;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.businessobject.OLERequestorPatronDocument;
import org.kuali.ole.select.businessobject.OleFormatType;
import org.kuali.ole.select.businessobject.OleRequestSourceType;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.select.document.OLEPlatformRecordDocument;
import org.kuali.ole.sys.businessobject.Building;
import org.kuali.ole.sys.businessobject.Room;
import org.kuali.ole.vnd.businessobject.*;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;

import java.util.List;
import java.util.Map;

/**
 * Created by rajeshbabuk on 12/29/15.
 */
public interface SelectDAO {
    public Map<String,String> fetchOrderImportFieldValues(String fieldName);
    public List<Organization> fetchAllOrganization();
    public List<Chart> fetchAllItemChartCode();
    public List<ContractManager> fetchAllContractManager();
    public List<PurchaseOrderType> fetchAllOrderType();
    public List<FundingSource> fetchAllFundingSource();
    public List<Building> fetchAllBuilding();
    public List<Room> fetchAllRoom();
    public List<PurchaseOrderVendorChoice> fetchAllVendorChoice();
    public List<PurchaseOrderCostSource> fetchAllCostSource();
    public List<String> fetchAllItemLocation();
    public List<VendorDetail> fetchAllVendorDetail();
    public List<VendorAlias> fetchAllVendorAlias();
    public List<VendorCustomerNumber> fetchAllVendorCustomerNumber();
    public List<OLERequestorPatronDocument> fetchAllRequestor();
    public List<OleItemAvailableStatus> fetchAllItemStatus();
    public List<Account> fetchAllAccount();
    public List<ObjectCode> fetchAllObjectCode();
    public List<OleRequestSourceType> fetchAllRequestSourceType();
    public List<PrincipalBo> fetchAllPrincipalBo();
    public List<PurchaseOrderTransmissionMethod> fetchAllPurchaseOrderTransmissionMethod();
    public List<RecurringPaymentType> fetchAllRecurringPaymentType();
    public List<OleFundCode> fetchAllFundCode();
    public List<OleCurrencyType> fetchAllCurrencyType();
    public List<OLEEResourceRecordDocument> fetchAllEResourceDocuments();
    public List<OLEPlatformRecordDocument> fetchAllPlatformRecordDocuments();
    public List<OleFormatType> fetchAllFormatType();
    public Organization getOrganizationByChartCode(String chartCode);
    public Organization getOrganizationByOrgCode(String orgCode);
    public Organization getOrganizationByChartAndOrgCode(String chartCode, String orgCode);
    public VendorDetail getVendorDetailByVendorNumber(String vendorNumber);
    public VendorAlias getVendorAliasByName(String vendorAliasName);
    public Account getAccountByAccountNumber(String accountNumber);
    public Building getBuildingByBuildingCode(String buildingCode);
    public Room getRoomByBuildingRoomNumber(String buildingRoomNumber);
    public PurchaseOrderType getPurchaseOrderTypeByOrderType(String orderType);
    public Chart getChartByChartCode(String chartOfAccountsCode);
    public ObjectCode getObjectCodeByCode(String financialObjectCode);
    public PurchaseOrderCostSource getPurchaseOrderCostSourceByCode(String purchaseOrderCostSourceCode);
    public PurchaseOrderTransmissionMethod getPurchaseOrderTransmissionMethodByDesc(String purchaseOrderTransmissionMethodCode);
    public Building getBuildingByCampusCode(String campusCode);
    public FundingSource getFundingSourceByCode(String fundingSourceCode);
    public ContractManager getContractManagerByName(String contractManager);
    public PurchaseOrderVendorChoice getPurchaseOrderVendorChoiceByCode(String purchaseOrderVendorChoiceCode);
    public OLERequestorPatronDocument getRequestorPatronDocumentByName(String requestorName);
    public OleRequestSourceType getRequestSourceTypeByType(String requestSourceType);
    public Room getRoom(String buildingCode, String campusCode, String buildingRoomNumber);
    public Account getAccountByAccountNumberAndChartCode(String accountNumber, String itemChartCode);
    public ObjectCode getObjectCodeByChartCodeAndObjectCode(String itemChartCode, String financialObjectCode);
    public RecurringPaymentType getRecurringPaymentTypeByCode(String recurringPaymentType);
    public OleFundCode getOleFundCodeByCode(String fundCode);
    public ItemStatusRecord fetchItemStatusByName(String itemStatusCode);
    public VendorCustomerNumber getVendorCustomerNumberByNumber(String vendorCustomerNumber);
    public OLEDonor getOLEDonorByCode(String donorCode);
    public OleCurrencyType getCurrencyType(String currencyType);
    public OleFormatType getFormatTypeByName(String formatTypeName);

}
