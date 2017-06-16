package org.kuali.ole.oleng.gobi.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.gobi.bo.GobiSubAccountDeliveryAddressDocument;
import org.kuali.ole.gobi.dao.BibDAO;
import org.kuali.ole.gobi.datobjects.PurchaseOrder;
import org.kuali.ole.gobi.resolvers.LocalDataMappingValueResolver;
import org.kuali.ole.gobi.resolvers.MiscellaneousNoteValueResolver;
import org.kuali.ole.gobi.resolvers.localdata.*;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileLocalDataMapping;
import org.kuali.ole.oleng.resolvers.orderimport.*;
import org.kuali.ole.oleng.service.impl.OrderImportServiceImpl;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorCustomerNumber;

import java.util.*;

/**
 * Created by SheikS on 8/3/2016.
 */
public abstract class OleNgGobiOrderImportServiceImpl extends OrderImportServiceImpl {
    private PurchaseOrder order;
    private List<OleOrderRecord> oleOrderRecords;
    private OleTxRecord oleTxRecord;
    private List<TxValueResolver> valueResolvers;
    private List<LocalDataMappingValueResolver> localDataMappingValueResolvers;
    private Map<String, VendorCustomerNumber> vendorSubAccountMap;


    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleNgGobiOrderImportServiceImpl.class);

    @Override
    public OleTxRecord processDataMapping(RecordDetails recordDetails, BatchProcessProfile batchProcessProfile, Exchange exchange) {
        this.oleTxRecord = super.processDataMapping(recordDetails, batchProcessProfile, exchange);
        processIncomingTxInfoNotHandledByProfileMapping();
        setLocalDataMappings(this.oleTxRecord, batchProcessProfile);
        return oleTxRecord;
    }

    public List<OleOrderRecord> getOleOrderRecords() {
        return oleOrderRecords;
    }

    public void setOleOrderRecords(List<OleOrderRecord> oleOrderRecords) {
        this.oleOrderRecords = oleOrderRecords;
    }

    private void processIncomingTxInfoNotHandledByProfileMapping() {
        preProcess();

        setListPrice();

        setQuantity();

        setAccountNumber();

        setReceiptNote();

        setVendorItemIdentifier();

        setVendorCustomerInfoNumber();

        setItemChartCode();

        setOrgCode();

        setDeliveryInfo();
    }

    public void setLocalDataMappings(OleTxRecord oleTxRecord, BatchProcessProfile batchProcessProfile) {
        List<BatchProfileLocalDataMapping> batchProfileLocalDataMappings = batchProcessProfile.getBatchProfileLocalDataMappings();
        if(CollectionUtils.isNotEmpty(batchProfileLocalDataMappings)) {
            for (Iterator<BatchProfileLocalDataMapping> iterator = batchProfileLocalDataMappings.iterator(); iterator.hasNext(); ) {
                BatchProfileLocalDataMapping batchProfileLocalDataMapping = iterator.next();
                String source = batchProfileLocalDataMapping.getSource();
                String destination = batchProfileLocalDataMapping.getDestination();
                for (Iterator<LocalDataMappingValueResolver> localDataMappingValueResolverIterator = getLocalDataMappingValueResolvers().iterator(); localDataMappingValueResolverIterator.hasNext(); ) {
                    LocalDataMappingValueResolver localDataMappingValueResolver = localDataMappingValueResolverIterator.next();
                    if (localDataMappingValueResolver.isInterested(source)) {
                        localDataMappingValueResolver.setPurchaseOrder(order);
                        if(destination != null) {
                            localDataMappingValueResolver.setDestFieldName(destination);
                        }
                        localDataMappingValueResolver.setAttributeValue(oleTxRecord, null);
                    }
                }
            }
        }
    }
/*
    @Override
    public List<OleTxRecord> getQuantityItemPartsLocation(List<BibMarcRecord> bibMarcRecords, OLEBatchProcessJobDetailsBo job) {
        //TODO: Need to figure out;

        ArrayList<OleTxRecord> oleTxRecords = new ArrayList<>();
        for (Iterator<BibMarcRecord> iterator = bibMarcRecords.iterator(); iterator.hasNext(); ) {
            BibMarcRecord next = iterator.next();
            OleTxRecord oleTxRecord = new OleTxRecord();
            oleTxRecords.add(oleTxRecord);
        }
        return oleTxRecords;
    }*/

    public void setValueResolvers(List<TxValueResolver> valueResolvers) {
        this.valueResolvers = valueResolvers;
    }

    protected GobiSubAccountDeliveryAddressDocument getBuildingInfoBySubAcccount(String subAccount) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("subAccount", subAccount);
        List<GobiSubAccountDeliveryAddressDocument> subAccountDeliveryAddressDocuments =
                (List<GobiSubAccountDeliveryAddressDocument>) getBusinessObjectService().findMatching(GobiSubAccountDeliveryAddressDocument.class, map);

        if (CollectionUtils.isNotEmpty(subAccountDeliveryAddressDocuments)) {
            GobiSubAccountDeliveryAddressDocument gobiSubAccountDeliveryAddressDocument = subAccountDeliveryAddressDocuments.get(0);
            return gobiSubAccountDeliveryAddressDocument;
        }
        LOG.error("No mapping found for gobi sub-account and building information.");
        return null;
    }

    protected void setDeliveryInfo() {
        GobiSubAccountDeliveryAddressDocument buildingInfoBySubAcccount = getBuildingInfoBySubAcccount(getSubAccount());
        if (null != buildingInfoBySubAcccount) {
            oleTxRecord.setBuildingCode(buildingInfoBySubAcccount.getBuildingCode());
            oleTxRecord.setDeliveryBuildingRoomNumber(buildingInfoBySubAcccount.getRoomNumber());
        }
    }

    protected void setItemChartCode() {
        String itemChartCode = resolveItemChartCode(getSubAccount());
        oleTxRecord.setItemChartCode(itemChartCode);
        oleTxRecord.setChartCode(itemChartCode);
    }

    protected void setOrgCode() {
        String orgCode = resolveOrgCode(getSubAccount());
        oleTxRecord.setOrgCode(orgCode);

    }

    protected void setVendorCustomerInfoNumber() {
        String subAccount = getSubAccount();
        oleTxRecord.setVendorInfoCustomer(subAccount);
    }

    protected void setVendorItemIdentifier(String vendorItemIdentifier) {
        oleTxRecord.setVendorItemIdentifier(vendorItemIdentifier);
    }

    protected void setReceiptNote(String orderNotes) {
        oleTxRecord.setReceiptNote(orderNotes);
    }

    protected void setAccountNumber(String fundCode) {
        oleTxRecord.setAccountNumber(fundCode);
    }

    protected void setQuantity(String quantity) {
        oleTxRecord.setQuantity(quantity);
    }

    protected void setListPrice(String listPrice) {
        oleTxRecord.setListPrice(listPrice);
    }

    protected void setDefaultLocation(String location) {
        oleTxRecord.setDefaultLocation(location);
    }

    protected String resolveItemChartCode(String baseAccount) {

        if(getVendorSubAccountMap().containsKey(baseAccount)) {
            return getVendorSubAccountMap().get(baseAccount).getChartOfAccounts().getCode();
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("vendorCustomerNumber", baseAccount);
        List<VendorCustomerNumber> vendorCustomerNumbers =
                (List<VendorCustomerNumber>) getBusinessObjectService().findMatching(VendorCustomerNumber.class, map);
        if (CollectionUtils.isNotEmpty(vendorCustomerNumbers)) {
            VendorCustomerNumber vendorCustomerNumber = vendorCustomerNumbers.get(0);
            getVendorSubAccountMap().put(baseAccount, vendorCustomerNumber);
            return vendorCustomerNumber.getChartOfAccounts().getCode();
        }
        return null;
    }

    protected String resolveOrgCode(String baseAccount) {

        if(getVendorSubAccountMap().containsKey(baseAccount)) {
            return getVendorSubAccountMap().get(baseAccount).getVendorOrganizationCode();
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("vendorCustomerNumber", baseAccount);
        List<VendorCustomerNumber> vendorCustomerNumbers =
                (List<VendorCustomerNumber>) getBusinessObjectService().findMatching(VendorCustomerNumber.class, map);
        if (CollectionUtils.isNotEmpty(vendorCustomerNumbers)) {
            VendorCustomerNumber vendorCustomerNumber = vendorCustomerNumbers.get(0);
            getVendorSubAccountMap().put(baseAccount, vendorCustomerNumber);
            return vendorCustomerNumber.getVendorOrganizationCode();
        }
        return null;
    }

    public void set980SubFielda(String ybpOrderKey) {
        BibDAO bibDAO = (BibDAO) SpringContext.getService("bibDAO");
        String bibId = oleTxRecord.getBibId();
        if (StringUtils.isNotBlank(bibId)) {
            bibDAO.updateYPBOrderKeyContent(bibId, ybpOrderKey);
        }
    }

    protected abstract void setListPrice();

    protected abstract void setQuantity();

    protected abstract void setAccountNumber();

    protected abstract void setReceiptNote();

    protected abstract void setVendorItemIdentifier();

    protected abstract String getSubAccount();

    protected abstract void preProcess();

    public List<TxValueResolver> getValueResolvers() {
        if (null == valueResolvers) {
            valueResolvers = new ArrayList<>();
            valueResolvers.add(new AssignToUserValueResolver());
            valueResolvers.add(new CaptionValueResolver());
            valueResolvers.add(new ContractManagerValueResolver());
            valueResolvers.add(new CostSourceValueResolver());
            valueResolvers.add(new DefaultLocationValueResolver());
            valueResolvers.add(new DeliveryCampusCodeValueResolver());
            valueResolvers.add(new DiscountValueResolver());
            valueResolvers.add(new DonorCodeValueResolver());
            valueResolvers.add(new FundingSourceValueResolver());
            valueResolvers.add(new ItemNumPartsValueResolver());
            valueResolvers.add(new ItemPriceSourceValueResolver());
            valueResolvers.add(new ItemStatusValueResolver());
            valueResolvers.add(new ItemListPriceValueResolver());
            valueResolvers.add(new MethodOfPOTransmissionValueResolver());
            valueResolvers.add(new MiscellaneousNoteValueResolver());
            valueResolvers.add(new ObjectCodeValueResolver());
            valueResolvers.add(new OrderTypeValueResolver());
            valueResolvers.add(new OrgCodeCodeValueResolver());
            valueResolvers.add(new PercentValueResolver());
            valueResolvers.add(new POConfirmationValueResolver());
            valueResolvers.add(new PreqPosstiveApprovalReqValueResolver());
            valueResolvers.add(new ReceivingRequiredValueResolver());
            valueResolvers.add(new ReceiptNoteValueResolver());
            valueResolvers.add(new RecurringPaymentTypeValueResolver());
            valueResolvers.add(new RecurringPaymentBeginDateValueResolver());
            valueResolvers.add(new RecurringPaymentEndDateTypeValueResolver());
            valueResolvers.add(new RequestSourceValueResolver());
            valueResolvers.add(new RequisitionSourceValueResolver());
            valueResolvers.add(new RequestorNoteValueResolver());
            valueResolvers.add(new RouteToRequestorValueResolver());
            valueResolvers.add(new SelectorNoteValueResolver());
            valueResolvers.add(new SingleCopyNumberValueResolver());
            valueResolvers.add(new TaxIndicatorValueResolver());
            valueResolvers.add(new VendorChoiceValueResolver());
            valueResolvers.add(new VendorNumberValueResolver());
            valueResolvers.add(new VendorInstructionsNoteValueResolver());
        }
        return valueResolvers;
    }

    public List<LocalDataMappingValueResolver> getLocalDataMappingValueResolvers() {
        if (CollectionUtils.isEmpty(localDataMappingValueResolvers)) {
            localDataMappingValueResolvers = new ArrayList<>();
            localDataMappingValueResolvers.add(new UnlistedElectronicMonographLocalDataValueResolver());
            localDataMappingValueResolvers.add(new UnlistedPrintMonographLocalDataValueResolver());
            localDataMappingValueResolvers.add(new ListedPrintMonographLocalDataValueResolver());
            localDataMappingValueResolvers.add(new ListedElectronicMonographLocalDataValueResolver());
            localDataMappingValueResolvers.add(new ListedPrintSerialLocalDataValueResolver());
            localDataMappingValueResolvers.add(new ListedElectronicSerialLocalDataValueResolver());
            localDataMappingValueResolvers.add(new UnListedPrintSerialLocalDataValueResolver());
        }
        return localDataMappingValueResolvers;
    }

    @Override
    public List<String> getDataMappingTypesToProcess() {
        List<String> datamappingTypes = new ArrayList<>();
        datamappingTypes.add(OleNGConstants.CONSTANT);
        return datamappingTypes;
    }


    public void setOrder(PurchaseOrder order) {
        this.order = order;
    }

    public PurchaseOrder getOrder() {
        return order;
    }

    public Map<String, VendorCustomerNumber> getVendorSubAccountMap() {
        if (null == vendorSubAccountMap) {
            vendorSubAccountMap = new HashMap<>();
        }
        return vendorSubAccountMap;
    }

    public void setVendorSubAccountMap(Map<String, VendorCustomerNumber> vendorSubAccountMap) {
        this.vendorSubAccountMap = vendorSubAccountMap;
    }

}
