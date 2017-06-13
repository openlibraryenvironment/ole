package org.kuali.ole.oleng.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.docstore.common.document.EHoldings;
import org.kuali.ole.docstore.common.document.PHoldings;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.gobi.resolvers.MiscellaneousNoteValueResolver;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.oleng.batch.process.model.ValueByPriority;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataMapping;
import org.kuali.ole.oleng.resolvers.orderimport.*;
import org.kuali.ole.oleng.service.OleNGMemorizeService;
import org.kuali.ole.oleng.service.OrderImportService;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.document.service.OleDocstoreHelperService;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.ole.spring.batch.processor.BatchBibFileProcessor;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.utility.LocationUtil;
import org.kuali.ole.utility.MarcRecordUtil;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.marc4j.marc.Record;

import java.util.*;

/**
 * Created by SheikS on 1/6/2016.
 */
public class OrderImportServiceImpl implements OrderImportService {

    private List<TxValueResolver> valueResolvers;
    private MarcRecordUtil marcRecordUtil;
    private BusinessObjectService businessObjectService;
    private BatchUtil batchUtil;
    private OleDocstoreHelperService oleDocstoreHelperService;
    private OleNGMemorizeService oleNGMemorizeService;
    private List<String> datamappingTypes;

    @Override
    public OleOrderRecord prepareOleOrderRecord(RecordDetails recordDetails, BatchProcessProfile batchProcessProfile, Exchange exchange) {
        OleTxRecord oleTxRecord = processDataMapping(recordDetails, batchProcessProfile, exchange);

        final OleOrderRecord oleOrderRecord = new OleOrderRecord();
        oleTxRecord.setItemType(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE);
        oleTxRecord.setRequisitionSource(OleSelectConstant.REQUISITON_SRC_TYPE_AUTOINGEST);
        String linkToOption = getLinkToOption(batchProcessProfile);
        oleOrderRecord.setLinkToOrderOption(linkToOption);
        oleOrderRecord.setOleTxRecord(oleTxRecord);

        return oleOrderRecord;

    }

    @Override
    public OleTxRecord processDataMapping(RecordDetails recordDetails, BatchProcessProfile batchProcessProfile, Exchange exchange) {
        OleTxRecord oleTxRecord = new OleTxRecord();
        List<BatchProfileDataMapping> batchProfileDataMappingList = batchProcessProfile.getBatchProfileDataMappingList();

        if (CollectionUtils.isNotEmpty(batchProfileDataMappingList)) {
            Record marcRecord = recordDetails.getRecord();

            List<String> datamappingTypes = getDataMappingTypesToProcess();
            Map<String, List<ValueByPriority>> valueByPriorityMap = getBatchUtil().getvalueByPriorityMapForDataMapping(marcRecord, batchProfileDataMappingList, datamappingTypes);

            for (Iterator<String> iterator = valueByPriorityMap.keySet().iterator(); iterator.hasNext(); ) {
                String destinationField =  iterator.next();
                for (Iterator<TxValueResolver> valueResolverIterator = getValueResolvers().iterator(); valueResolverIterator.hasNext(); ) {
                    TxValueResolver txValueResolver = valueResolverIterator.next();
                    if (txValueResolver.isInterested(destinationField)) {
                        String destinationValue = getBatchUtil().getDestinationValue(valueByPriorityMap,destinationField);
                        txValueResolver.setAttributeValue(oleTxRecord, destinationValue);
                    }
                }
            }

            String orderType = batchProcessProfile.getOrderType();
            String holdingsType = PHoldings.PRINT;
            if(StringUtils.isNotBlank(orderType) && orderType.equalsIgnoreCase(OleNGConstants.BatchProcess.ORDER_TYPE_EHOLDINGS)) {
                holdingsType = EHoldings.ELECTRONIC;
            }

            overlayBibProfile(oleTxRecord, marcRecord,holdingsType, batchProcessProfile.getBibImportProfileForOrderImport(), datamappingTypes, exchange);
        }
        return oleTxRecord;
    }

    public List<String> getDataMappingTypesToProcess() {
        if (CollectionUtils.isEmpty(datamappingTypes)) {
            datamappingTypes = new ArrayList<>();
            datamappingTypes.add(OleNGConstants.CONSTANT);
            datamappingTypes.add(OleNGConstants.BIB_MARC);
        }
        return datamappingTypes;
    }

    private void overlayBibProfile(OleTxRecord oleTxRecord, Record marcRecord, String holdingsType, String bibImportProfileForOrderImport, List<String> datamappingTypes, Exchange exchange) {
        BatchProcessProfile bibProfile = getBatchUtil().getProfileByNameAndType(bibImportProfileForOrderImport, OleNGConstants.BIB_IMPORT);
        if(null != bibProfile) {
            try {
                BatchBibFileProcessor batchBibFileProcessor = new BatchBibFileProcessor();
                List<Record> marcRecords = Collections.singletonList(marcRecord);
                if (holdingsType.equalsIgnoreCase(PHoldings.PRINT)) {
                    List<JSONObject> preTransformForHoldings = batchBibFileProcessor.prepareDataMappings(marcRecords, bibProfile,
                            OleNGConstants.HOLDINGS, OleNGConstants.PRE_MARC_TRANSFORMATION, false);
                    List<JSONObject> postTransformForHoldings = batchBibFileProcessor.prepareDataMappings(marcRecords, bibProfile,
                            OleNGConstants.HOLDINGS, OleNGConstants.POST_MARC_TRANSFORMATION, false);

                    List<JSONObject> dataMappingForHoldings = batchBibFileProcessor.buildOneObjectForList(preTransformForHoldings, postTransformForHoldings);

                    if(CollectionUtils.isNotEmpty(dataMappingForHoldings)) {
                        overlayLocation(oleTxRecord, dataMappingForHoldings.get(0), exchange);
                    }

                    List<JSONObject> preTransformForItem = batchBibFileProcessor.prepareDataMappings(marcRecords, bibProfile,
                            OleNGConstants.ITEM, OleNGConstants.PRE_MARC_TRANSFORMATION, false);
                    List<JSONObject> postTransformForItem = batchBibFileProcessor.prepareDataMappings(marcRecords, bibProfile,
                            OleNGConstants.ITEM, OleNGConstants.POST_MARC_TRANSFORMATION, false);

                    List<JSONObject> dataMappingForItem = batchBibFileProcessor.buildOneObjectForList(preTransformForItem, postTransformForItem);
                    if(CollectionUtils.isNotEmpty(dataMappingForItem)) {
                        JSONObject dataMapping = dataMappingForItem.get(0);
                        overlayDonor(oleTxRecord, dataMapping);
                        overlayItemType(oleTxRecord, dataMapping);
                        overlayItemStatus(oleTxRecord, dataMapping);
                        overlayEnumeration(oleTxRecord, dataMapping);
                    }
                } else {
                    List<JSONObject> preTransformForEHoldings = batchBibFileProcessor.prepareDataMappings(marcRecords, bibProfile,
                            OleNGConstants.EHOLDINGS, OleNGConstants.PRE_MARC_TRANSFORMATION, false);
                    List<JSONObject> postTransformForHoldings = batchBibFileProcessor.prepareDataMappings(marcRecords, bibProfile,
                            OleNGConstants.EHOLDINGS, OleNGConstants.POST_MARC_TRANSFORMATION, false);

                    List<JSONObject> dataMappingForHoldings = batchBibFileProcessor.buildOneObjectForList(preTransformForEHoldings, postTransformForHoldings);

                    if(CollectionUtils.isNotEmpty(dataMappingForHoldings)) {
                        JSONObject dataMapping = dataMappingForHoldings.get(0);
                        overlayLocation(oleTxRecord, dataMapping, exchange);
                        overlayDonor(oleTxRecord, dataMapping);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private void overlayEnumeration(OleTxRecord oleTxRecord, JSONObject dataMapping) {
        JSONArray jsonArrayeFromJsonObject = getBatchUtil().getJSONArrayeFromJsonObject(dataMapping, OleNGConstants.BatchProcess.ENUMERATION);
        if (null != jsonArrayeFromJsonObject) {
            List<String> listFromJSONArray = getBatchUtil().getListFromJSONArray(jsonArrayeFromJsonObject.toString());
            if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
                String chronology = listFromJSONArray.get(0);
                oleTxRecord.setCaption(chronology);
            }
        }
    }

    private void overlayLocation(OleTxRecord oleTxRecord, JSONObject dataMapping, Exchange exchange) {
        StringBuilder locationName = new StringBuilder();
        LocationUtil locationUtil = getOleNGMemorizeService().getLocationUtil();
        Map<String, String> locationMap = locationUtil.buildLocationMap(dataMapping, exchange);
        for (Iterator<String> iterator = locationMap.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            String locationCode = locationMap.get(key);
            locationUtil.appendLocationToStringBuilder(locationName, locationCode);
        }
        boolean validLocation = getOleNGMemorizeService().isValidLocation(locationName.toString());
        if(validLocation) {
            oleTxRecord.setDefaultLocation(locationName.toString());
        }

        if(StringUtils.isBlank(oleTxRecord.getDefaultLocation())) {
            ParameterValueResolver instance = ParameterValueResolver.getInstance();
            String defaultLocation = instance.getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.SELECT_NMSPC,
                    OLEConstants.SELECT_CMPNT, org.kuali.ole.sys.OLEConstants.ITEM_LOCATION_FIRM_FIXD);
            validLocation = getOleNGMemorizeService().isValidLocation(defaultLocation);
            if(validLocation) {
                oleTxRecord.setDefaultLocation(defaultLocation);
            }
        }
    }

    private void overlayDonor(OleTxRecord oleTxRecord, JSONObject dataMapping) {
        JSONArray jsonArrayeFromJsonObject = getBatchUtil().getJSONArrayeFromJsonObject(dataMapping, OleNGConstants.BatchProcess.DONOR_CODE);
        if (null != jsonArrayeFromJsonObject) {
            List<String> listFromJSONArray = getBatchUtil().getListFromJSONArray(jsonArrayeFromJsonObject.toString());
            if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
                List<String> donorCodes = new ArrayList<>();
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String donorCode = iterator.next();
                    donorCodes.add(donorCode);
                }
                oleTxRecord.setOleDonors(donorCodes);
            }
        }
    }

    private void overlayItemType(OleTxRecord oleTxRecord, JSONObject dataMapping) {
        JSONArray jsonArrayeFromJsonObject = getBatchUtil().getJSONArrayeFromJsonObject(dataMapping, OleNGConstants.BatchProcess.ITEM_TYPE);
        if (null != jsonArrayeFromJsonObject) {
            List<String> listFromJSONArray = getBatchUtil().getListFromJSONArray(jsonArrayeFromJsonObject.toString());
            if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
                String itemTypeName = listFromJSONArray.get(0);
                oleTxRecord.setItemType(itemTypeName);
            }
        }
    }

    private void overlayItemStatus(OleTxRecord oleTxRecord, JSONObject dataMapping) {
        JSONArray jsonArrayeFromJsonObject = getBatchUtil().getJSONArrayeFromJsonObject(dataMapping, OleNGConstants.BatchProcess.ITEM_STATUS);
        if (null != jsonArrayeFromJsonObject) {
            List<String> listFromJSONArray = getBatchUtil().getListFromJSONArray(jsonArrayeFromJsonObject.toString());
            if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
                String itemStatusName = listFromJSONArray.get(0);
                oleTxRecord.setItemStatus(itemStatusName);
            }
        }
    }

    public String getLinkToOption(BatchProcessProfile batchProcessProfile) {
        String orderType = batchProcessProfile.getOrderType();
        String linkToOption = "";
        if(StringUtils.isNotBlank(orderType) && orderType.equalsIgnoreCase(OleNGConstants.BatchProcess.ORDER_TYPE_EHOLDINGS)) {
            linkToOption = OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_ELECTRONIC;
        }

        if(StringUtils.isBlank(linkToOption)) {
            linkToOption = OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_PRINT;
        }
        return linkToOption;
    }


    public MarcRecordUtil getMarcRecordUtil() {
        if (null == marcRecordUtil) {
            marcRecordUtil = new MarcRecordUtil();
        }
        return marcRecordUtil;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public List<TxValueResolver> getValueResolvers() {
        if (null == valueResolvers) {
            valueResolvers = new ArrayList<>();
            valueResolvers.add(new AccountNumberValueResolver());
            valueResolvers.add(new AssignToUserValueResolver());
            valueResolvers.add(new BuildingCodeValueResolver());
            valueResolvers.add(new CaptionValueResolver());
            valueResolvers.add(new ChartCodeValueResolver());
            valueResolvers.add(new ItemChartCodeValueResolver());
            valueResolvers.add(new QuantityValueResolver());
            valueResolvers.add(new ContractManagerValueResolver());
            valueResolvers.add(new CostSourceValueResolver());
            valueResolvers.add(new DefaultLocationValueResolver());
            valueResolvers.add(new DeliveryCampusCodeValueResolver());
            valueResolvers.add(new DiscountValueResolver());
            valueResolvers.add(new DonorCodeValueResolver());
            valueResolvers.add(new FundingSourceValueResolver());
            valueResolvers.add(new ItemNumPartsValueResolver());
            valueResolvers.add(new ItemPriceSourceValueResolver());
            valueResolvers.add(new ItemListPriceValueResolver());
            valueResolvers.add(new ItemStatusValueResolver());
            valueResolvers.add(new MiscellaneousNoteValueResolver());
            valueResolvers.add(new MethodOfPOTransmissionValueResolver());
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
            valueResolvers.add(new VendorAliasNameValueResolver());
            valueResolvers.add(new RequestorNameValueResolver());
            valueResolvers.add(new FundCodeValueResolver());
            valueResolvers.add(new VendorItemIdentifierValueResolver());
            valueResolvers.add(new VendorCustomerNumberValueResolver());
            valueResolvers.add(new VendorInstructionsNoteValueResolver());
            valueResolvers.add(new DeliveryBuildingRoomNumberValueResolver());
            valueResolvers.add(new SpecialProcessingInstructionNoteValueResolver());
            valueResolvers.add(new FormatTypeValueResolver());
        }
        return valueResolvers;
    }

    public BatchUtil getBatchUtil() {
        if(null == batchUtil) {
            batchUtil = new BatchUtil();
        }
        return batchUtil;
    }

    public void setBatchUtil(BatchUtil batchUtil) {
        this.batchUtil = batchUtil;
    }

    public OleDocstoreHelperService getOleDocstoreHelperService() {
        if(null == oleDocstoreHelperService) {
            oleDocstoreHelperService = SpringContext.getBean(OleDocstoreHelperService.class);
        }
        return oleDocstoreHelperService;
    }

    public void setOleDocstoreHelperService(OleDocstoreHelperService oleDocstoreHelperService) {
        this.oleDocstoreHelperService = oleDocstoreHelperService;
    }

    public OleNGMemorizeService getOleNGMemorizeService() {
        if(null == oleNGMemorizeService) {
            oleNGMemorizeService = new OleNGMemorizeServiceImpl();
        }
        return oleNGMemorizeService;
    }

    public void setOleNGMemorizeService(OleNGMemorizeService oleNGMemorizeService) {
        this.oleNGMemorizeService = oleNGMemorizeService;
    }
}
