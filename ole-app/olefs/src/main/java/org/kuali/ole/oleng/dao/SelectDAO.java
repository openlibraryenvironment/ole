package org.kuali.ole.oleng.dao;

import org.kuali.ole.coa.businessobject.*;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.select.businessobject.OLERequestorPatronDocument;
import org.kuali.ole.select.businessobject.OleRequestSourceType;
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

}
