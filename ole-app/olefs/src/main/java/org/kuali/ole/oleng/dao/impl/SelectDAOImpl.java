package org.kuali.ole.oleng.dao.impl;

import org.kuali.ole.coa.businessobject.*;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.oleng.dao.SelectDAO;
import org.kuali.ole.oleng.util.OrderImportFieldValuesUtil;
import org.kuali.ole.select.businessobject.OLERequestorPatronDocument;
import org.kuali.ole.select.businessobject.OleRequestSourceType;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.sys.businessobject.Building;
import org.kuali.ole.sys.businessobject.Room;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.*;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KeyValuesService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by rajeshbabuk on 12/29/15.
 */
@Repository("SelectDAO")
@Scope("prototype")
public class SelectDAOImpl implements SelectDAO {

    private BusinessObjectService businessObjectService;
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
        return (List<OleCurrencyType>) KRADServiceLocator.getBusinessObjectService().findAll(OleCurrencyType.class);
    }

    public BusinessObjectService getBusinessObjectService() {
        if(null == businessObjectService){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
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
}
