package org.kuali.ole.oleng.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.EHoldings;
import org.kuali.ole.docstore.common.document.PHoldings;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatusRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemTypeRecord;
import org.kuali.ole.oleng.batch.process.model.ValueByPriority;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataMapping;
import org.kuali.ole.oleng.resolvers.orderimport.*;
import org.kuali.ole.oleng.service.OrderImportService;
import org.kuali.ole.pojo.OleTxRecord;
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
    private LocationUtil locationUtil;
    private OleDocstoreHelperService oleDocstoreHelperService;

    @Override
    public OleTxRecord processDataMapping(RecordDetails recordDetails, BatchProcessProfile batchProcessProfile) {
        OleTxRecord oleTxRecord = new OleTxRecord();
        List<BatchProfileDataMapping> batchProfileDataMappingList = batchProcessProfile.getBatchProfileDataMappingList();

        if (CollectionUtils.isNotEmpty(batchProfileDataMappingList)) {
            Record marcRecord = recordDetails.getRecord();

            ArrayList<String> datamappingTypes = new ArrayList<>();
            datamappingTypes.add(OleNGConstants.CONSTANT);
            datamappingTypes.add(OleNGConstants.BIB_MARC);
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

            overlayBibProfile(oleTxRecord, marcRecord,holdingsType, batchProcessProfile.getBibImportProfileForOrderImport(), datamappingTypes);
        }
        return oleTxRecord;
    }

    private void overlayBibProfile(OleTxRecord oleTxRecord, Record marcRecord, String holdingsType, String bibImportProfileForOrderImport, ArrayList<String> datamappingTypes) {
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
                        overlayLocation(oleTxRecord, dataMappingForHoldings.get(0));
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
                        overlayCopyNumber(oleTxRecord, dataMapping);
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
                        overlayLocation(oleTxRecord, dataMapping);
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

    private void overlayCopyNumber(OleTxRecord oleTxRecord, JSONObject dataMapping) {
        JSONArray jsonArrayeFromJsonObject = getBatchUtil().getJSONArrayeFromJsonObject(dataMapping, OleNGConstants.BatchProcess.COPY_NUMBER);
        if (null != jsonArrayeFromJsonObject) {
            List<String> listFromJSONArray = getBatchUtil().getListFromJSONArray(jsonArrayeFromJsonObject.toString());
            if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
                String copyNumber = listFromJSONArray.get(0);
                oleTxRecord.setSingleCopyNumber(copyNumber);
            }
        }
    }

    private void overlayLocation(OleTxRecord oleTxRecord, JSONObject dataMapping) {
        StringBuilder locationName = new StringBuilder();
        Map<String, String> locationMap = getLocationUtil().buildLocationMap(dataMapping);
        for (Iterator<String> iterator = locationMap.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            String locationCode = locationMap.get(key);
            getLocationUtil().appendLocationToStringBuilder(locationName, locationCode);
        }
        boolean validLocation = getOleDocstoreHelperService().isValidLocation(locationName.toString());
        if(validLocation) {
            oleTxRecord.setDefaultLocation(locationName.toString());
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
            valueResolvers.add(new VendorItemIdentifierValueResolver());
            valueResolvers.add(new VendorCustomerNumberValueResolver());
            valueResolvers.add(new VendorInstructionsNoteValueResolver());
            valueResolvers.add(new DeliveryBuildingRoomNumberValueResolver());
            valueResolvers.add(new SpecialProcessingInstructionNoteValueResolver());
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

    public LocationUtil getLocationUtil() {
        if(null == locationUtil) {
            locationUtil = new LocationUtil();
        }
        return locationUtil;
    }

    public void setLocationUtil(LocationUtil locationUtil) {
        this.locationUtil = locationUtil;
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
}
