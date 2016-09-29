package org.kuali.ole.gobi.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileConstantsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileDataMappingOptionsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileMappingOptionsBo;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.gobi.bo.GobiSubAccountDeliveryAddressDocument;
import org.kuali.ole.gobi.dao.BibDAO;
import org.kuali.ole.gobi.datobjects.PurchaseOrder;
import org.kuali.ole.gobi.resolvers.LocalDataMappingValueResolver;
import org.kuali.ole.gobi.resolvers.MiscellaneousNoteValueResolver;
import org.kuali.ole.gobi.resolvers.localdata.*;
import org.kuali.ole.oleng.resolvers.orderimport.*;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.service.impl.OleOrderRecordServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorCustomerNumber;

import java.util.*;

public abstract class OleGobiOrderRecordServiceImpl extends OleOrderRecordServiceImpl {
    private PurchaseOrder order;
    private List<OleOrderRecord> oleOrderRecords;
    private OleTxRecord oleTxRecord;
    private List<TxValueResolver> valueResolvers;
    private List<LocalDataMappingValueResolver> localDataMappingValueResolvers;
    private Map<String, VendorCustomerNumber> vendorSubAccountMap;

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

    @Override
    protected Map<String, String> mapDataFieldsToTxnRecord(OleTxRecord oleTxRecord, BibMarcRecord bibMarcRecord, int recordPosition, OLEBatchProcessJobDetailsBo job) {
        this.oleTxRecord = oleTxRecord;
        processIncomingTxInfoNotHandledByProfileMapping();
        setLocalDataMappings(oleTxRecord, job);
        return new HashMap<>();
    }


    public List<OleOrderRecord> getOleOrderRecords() {
        return oleOrderRecords;
    }

    public void setOleOrderRecords(List<OleOrderRecord> oleOrderRecords) {
        this.oleOrderRecords = oleOrderRecords;
    }

    private void processIncomingTxInfoNotHandledByProfileMapping() {
        preProcess();

        setDefaultLocation();

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

    public void setLocalDataMappings(OleTxRecord oleTxRecord, OLEBatchProcessJobDetailsBo job) {
        List<OLEBatchProcessProfileMappingOptionsBo> oleBatchProcessProfileMappingOptionsList = job.getOrderImportHelperBo().getOleBatchProcessProfileBo().getOleBatchProcessProfileMappingOptionsList();

        if (CollectionUtils.isNotEmpty(oleBatchProcessProfileMappingOptionsList)) {

            for (Iterator<OLEBatchProcessProfileMappingOptionsBo> oleBatchProcessProfileMappingOptionsBoIterator = oleBatchProcessProfileMappingOptionsList.iterator(); oleBatchProcessProfileMappingOptionsBoIterator.hasNext(); ) {
                OLEBatchProcessProfileMappingOptionsBo oleBatchProcessProfileMappingOptionsBo = oleBatchProcessProfileMappingOptionsBoIterator.next();
                List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList = oleBatchProcessProfileMappingOptionsBo.getOleBatchProcessProfileDataMappingOptionsBoList();

                if (CollectionUtils.isNotEmpty(oleBatchProcessProfileDataMappingOptionsBoList)) {

                    for (OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo : oleBatchProcessProfileDataMappingOptionsBoList) {
                        String sourceName = oleBatchProcessProfileDataMappingOptionsBo.getSourceField();
                        String destFieldName = oleBatchProcessProfileDataMappingOptionsBo.getDestinationField();
                        for (Iterator<LocalDataMappingValueResolver> iterator = getLocalDataMappingValueResolvers().iterator(); iterator.hasNext(); ) {
                            LocalDataMappingValueResolver localDataMappingValueResolver = iterator.next();
                            if (localDataMappingValueResolver.isInterested(sourceName)) {
                                localDataMappingValueResolver.setPurchaseOrder(order);
                                localDataMappingValueResolver.setDestFieldName(destFieldName);
                                localDataMappingValueResolver.setAttributeValue(oleTxRecord, null);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Map<String, String> setDefaultAndConstantValuesToTxnRecord(OleTxRecord oleTxRecord, Map<String, String> failureRecords, OLEBatchProcessJobDetailsBo job) {
        if (failureRecords == null) {
            failureRecords = new HashMap<>();
        }

        String attributeName;
        String attributeValue;

        List<OLEBatchProcessProfileConstantsBo> oleBatchProcessProfileConstantsBoList = job.getOrderImportHelperBo().getOleBatchProcessProfileBo().getOleBatchProcessProfileConstantsList();


        for (OLEBatchProcessProfileConstantsBo oleBatchProcessProfileConstantsBo : oleBatchProcessProfileConstantsBoList) {
            attributeName = oleBatchProcessProfileConstantsBo.getAttributeName();
            attributeValue = oleBatchProcessProfileConstantsBo.getAttributeValue();


            for (Iterator<TxValueResolver> iterator = getValueResolvers().iterator(); iterator.hasNext(); ) {
                TxValueResolver txValuResolver = iterator.next();
                if (txValuResolver.isInterested(attributeName)) {
                    if (failureRecords.containsKey(attributeName)) {
                        failureRecords.remove(attributeName);
                    }
                    txValuResolver.setAttributeValue(oleTxRecord, attributeValue);
                }
            }

        }
        return failureRecords;
    }

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
    }

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

    protected abstract void setDefaultLocation();

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
}
