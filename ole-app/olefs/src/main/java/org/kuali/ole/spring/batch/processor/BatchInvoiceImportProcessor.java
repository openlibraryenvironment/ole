package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.InvoiceFailureResponse;
import org.kuali.ole.docstore.common.response.InvoiceResponse;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.docstore.common.response.OleNGInvoiceImportResponse;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.oleng.batch.process.model.ValueByPriority;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataMapping;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileMatchPoint;
import org.kuali.ole.oleng.batch.reports.InvoiceImportLoghandler;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.oleng.resolvers.invoiceimport.*;
import org.kuali.ole.oleng.service.OleNGInvoiceService;
import org.kuali.ole.oleng.util.InvoiceImportHelperUtil;
import org.kuali.ole.oleng.util.OleNGInvoiceRecordBuilderUtil;
import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.pojo.edi.INVOrder;
import org.kuali.ole.pojo.edi.LineItemOrder;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.marc4j.marc.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by SheikS on 1/27/2016.
 */
@Service("batchInvoiceImportProcessor")
public class BatchInvoiceImportProcessor extends BatchFileProcessor {

    @Autowired
    private DescribeDAO describeDAO;

    @Autowired
    private BatchBibFileProcessor batchBibFileProcessor;

    @Autowired
    private OleNGInvoiceService oleNGInvoiceService;

    private List<InvoiceRecordResolver> invoiceRecordResolvers;
    private OleNGInvoiceRecordBuilderUtil oleNGInvoiceRecordBuilderUtil;
    private InvoiceImportHelperUtil invoiceImportHelperUtil;

    @Override
    public String processRecords(String rawContent, Map<Integer, RecordDetails> recordsMap, String fileType, BatchProcessProfile batchProcessProfile, String reportDirectoryName) throws JSONException {
        JSONObject response = new JSONObject();
        OleNGInvoiceImportResponse oleNGInvoiceImportResponse = new OleNGInvoiceImportResponse();
        List<InvoiceFailureResponse> invoiceFailureResponses = new ArrayList<>();
        Exchange exchange = new Exchange();

        Map<String, List<OleInvoiceRecord>> oleinvoiceRecordMap = null;
        if (fileType.equalsIgnoreCase(OleNGConstants.MARC)) {
            if (recordsMap.size() > 0) {
                BatchProcessProfile bibImportProfile = getBibImportProfile(batchProcessProfile.getBibImportProfileForOrderImport());
                if (null != bibImportProfile) {
                    OleNGBibImportResponse oleNGBibImportResponse = processBibImport(rawContent, recordsMap, fileType, bibImportProfile, reportDirectoryName);
                    List<RecordDetails> recordDetailses = new ArrayList<RecordDetails>(recordsMap.values());
                    List<Record> records = new ArrayList<>();
                    if(CollectionUtils.isNotEmpty(recordDetailses)) {
                        for (Iterator<RecordDetails> iterator = recordDetailses.iterator(); iterator.hasNext(); ) {
                            RecordDetails recordDetails = iterator.next();
                            records.add(recordDetails.getRecord());
                        }
                    }
                    oleinvoiceRecordMap = prepareInvoiceRecordsForMarc(records, batchProcessProfile, exchange);
                }
            }
        } else if (fileType.equalsIgnoreCase(OleNGConstants.INV) || fileType.equalsIgnoreCase(OleNGConstants.EDI)) {
            oleinvoiceRecordMap = prepareInvoiceRecordsForEdifact(rawContent, batchProcessProfile, exchange);
        }


        for (Iterator<String> iterator = oleinvoiceRecordMap.keySet().iterator(); iterator.hasNext(); ) {
            String invoiceNumber = iterator.next();
            List<OleInvoiceRecord> oleInvoiceRecords = oleinvoiceRecordMap.get(invoiceNumber);

            try {
                OleInvoiceDocument oleInvoiceDocument = oleNGInvoiceService.createNewInvoiceDocument();
                oleNGInvoiceService.populateInvoiceDocWithOrderInformation(oleInvoiceDocument, oleInvoiceRecords);
                oleNGInvoiceService.saveInvoiceDocument(oleInvoiceDocument);
                String documentNumber = oleInvoiceDocument.getDocumentNumber();
                if (null != documentNumber) {
                    InvoiceResponse invoiceResponse = new InvoiceResponse();
                    invoiceResponse.setDocumentNumber(documentNumber);
                    for (Iterator<OleInvoiceRecord> oleInvoiceRecordIterator = oleInvoiceRecords.iterator(); oleInvoiceRecordIterator.hasNext(); ) {
                        OleInvoiceRecord invoiceRecord = oleInvoiceRecordIterator.next();
                        int lineItemsCount = invoiceRecord.getOlePurchaseOrderItems().size();
                        if (invoiceRecord.isLink()) {
                            invoiceResponse.addLinkCount(lineItemsCount);
                        } else {
                            invoiceResponse.addUnLinkCount();
                        }
                    }
                    oleNGInvoiceImportResponse.getInvoiceResponses().add(invoiceResponse);
                }
            } catch (Exception e) {
                e.printStackTrace();
                InvoiceFailureResponse invoiceFailureResponse = new InvoiceFailureResponse();
                invoiceFailureResponse.setInvoiceNumber(invoiceNumber);
                invoiceFailureResponse.setFailureMessage(e.getMessage());
                invoiceFailureResponses.add(invoiceFailureResponse);
            }
        }

        try {
            List<InvoiceFailureResponse> invoiceFailureResponseList = (List<InvoiceFailureResponse>) exchange.get(OleNGConstants.FAILURE_RESPONSE);
            if(CollectionUtils.isNotEmpty(invoiceFailureResponseList)) {
                invoiceFailureResponses.addAll(invoiceFailureResponseList);
            }
            oleNGInvoiceImportResponse.getInvoiceFailureResponses().addAll(invoiceFailureResponses);
            String successResponse = getObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(oleNGInvoiceImportResponse);
            //System.out.println("Invoice Import Response : " + successResponse);
            InvoiceImportLoghandler invoiceImportLoghandler = InvoiceImportLoghandler.getInstance();
            invoiceImportLoghandler.logMessage(oleNGInvoiceImportResponse, reportDirectoryName);
            return successResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.put(OleNGConstants.STATUS, OleNGConstants.FAILURE);
        return response.toString();
    }

    private Map<String, List<OleInvoiceRecord>> prepareInvoiceRecordsForMarc(List<Record> records, BatchProcessProfile batchProcessProfile, Exchange exchange) {
        Map<String, List<OleInvoiceRecord>> oleInvoiceRecordMap = new TreeMap<>();
        ArrayList<String> datamappingTypes = new ArrayList<>();
        List<InvoiceFailureResponse> invoiceFailureResponses = new ArrayList<>();
        datamappingTypes.add(OleNGConstants.CONSTANT);
        datamappingTypes.add(OleNGConstants.BIB_MARC);
        for (int index=0 ; index < records.size() ; index++) {
            try {
                Record record = records.get(index);
                OleInvoiceRecord oleInvoiceRecord = prepareInvoiceOrderRercordFromProfile(record, batchProcessProfile, null, datamappingTypes);

                //Match Point process start
                if (CollectionUtils.isNotEmpty(batchProcessProfile.getBatchProfileMatchPointList())) {
                    List<OlePurchaseOrderItem> olePurchaseOrderItems = processMatchpoint(record, batchProcessProfile, oleInvoiceRecord);
                    setLinkedOrUnlinked(oleInvoiceRecord, olePurchaseOrderItems);
                }

                addInvoiceRecordToMap(oleInvoiceRecordMap, oleInvoiceRecord);
            } catch(Exception e) {
                e.printStackTrace();
                InvoiceFailureResponse invoiceFailureResponse = new InvoiceFailureResponse();
                invoiceFailureResponse.setIndex(index);
                invoiceFailureResponse.setFailureMessage(e.getMessage());
                invoiceFailureResponses.add(invoiceFailureResponse);
            }
        }
        exchange.add(OleNGConstants.FAILURE_RESPONSE, invoiceFailureResponses);
        return oleInvoiceRecordMap;
    }


    private Map<String, List<OleInvoiceRecord>> prepareInvoiceRecordsForEdifact(String rawContent, BatchProcessProfile batchProcessProfile, Exchange exchange) {
        Map<String, List<OleInvoiceRecord>> oleInvoiceRecordMap = new TreeMap<>();
        List<InvoiceFailureResponse> invoiceFailureResponses = new ArrayList<>();

        List<INVOrder> invOrders = getInvoiceImportHelperUtil().readEDIContent(rawContent);

        ArrayList<String> datamappingTypes = new ArrayList<>();
        datamappingTypes.add(OleNGConstants.CONSTANT);

        for (Iterator<INVOrder> iterator = invOrders.iterator(); iterator.hasNext(); ) {
            INVOrder invOrder = iterator.next();
            if (CollectionUtils.isNotEmpty(invOrder.getLineItemOrder())) {
                for(int index=0 ; index < invOrder.getLineItemOrder().size() ; index++) {
                    LineItemOrder lineItemOrder = invOrder.getLineItemOrder().get(index);
                    OleInvoiceRecord oleInvoiceRecord = null;
                    try {
                        oleInvoiceRecord = getOleNGInvoiceRecordBuilderUtil().build(lineItemOrder, invOrder);
                        oleInvoiceRecord = prepareInvoiceOrderRercordFromProfile(null, batchProcessProfile, oleInvoiceRecord, datamappingTypes);
                        Map dbCriteria = new HashMap();
                        List<OlePurchaseOrderItem> olePurchaseOrderItems = new ArrayList<>();
                        if (StringUtils.isNotBlank(oleInvoiceRecord.getVendorItemIdentifier())) {
                            dbCriteria.put("vendorItemPoNumber", oleInvoiceRecord.getVendorItemIdentifier());
                            oleInvoiceRecord.setMatchPointType(OleNGConstants.BatchProcess.VENDOR_ITEM_IDENTIFIER);
                            olePurchaseOrderItems = getPurchaseOrderItemsByCriteria(dbCriteria);
                        }

                        if (CollectionUtils.isEmpty(olePurchaseOrderItems) && null != oleInvoiceRecord.getPurchaseOrderNumber()
                                &&oleInvoiceRecord.getPurchaseOrderNumber() != 0) {
                            dbCriteria.clear();
                            oleInvoiceRecord.setMatchPointType(OleNGConstants.BatchProcess.PO_NUMBER);
                            dbCriteria.put("purchaseOrder.purapDocumentIdentifier", oleInvoiceRecord.getPurchaseOrderNumber());
                            olePurchaseOrderItems = getPurchaseOrderItemsByCriteria(dbCriteria);
                        }
                        setLinkedOrUnlinked(oleInvoiceRecord, olePurchaseOrderItems);

                        addInvoiceRecordToMap(oleInvoiceRecordMap, oleInvoiceRecord);
                    } catch (Exception e) {
                        e.printStackTrace();
                        InvoiceFailureResponse invoiceFailureResponse = new InvoiceFailureResponse();
                        invoiceFailureResponse.setIndex(index);
                        invoiceFailureResponse.setDetailedMessage(e.getMessage());
                        invoiceFailureResponses.add(invoiceFailureResponse);
                    }
                }
            }
        }
        exchange.add(OleNGConstants.FAILURE_RESPONSE, invoiceFailureResponses);

        return oleInvoiceRecordMap;
    }

    private void setLinkedOrUnlinked(OleInvoiceRecord oleInvoiceRecord, List<OlePurchaseOrderItem> olePurchaseOrderItems) {
        if (CollectionUtils.isNotEmpty(olePurchaseOrderItems)) {
            oleInvoiceRecord.setOlePurchaseOrderItems(olePurchaseOrderItems);
            if (oleInvoiceRecord.getMatchPointType().equalsIgnoreCase(OleNGConstants.BatchProcess.VENDOR_ITEM_IDENTIFIER)) {
                oleInvoiceRecord.setLink((olePurchaseOrderItems.size() == 1 ? true : false));
            } else {
                oleInvoiceRecord.setLink(true);
            }
        }
    }

    private List<OlePurchaseOrderItem> filterOpenDocuments(List<OlePurchaseOrderItem> olePurchaseOrderItems) {
        List<OlePurchaseOrderItem> filteredItems = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(olePurchaseOrderItems)) {
            Map<String, Boolean> poStatus = new HashMap<>();
            for (Iterator<OlePurchaseOrderItem> iterator = olePurchaseOrderItems.iterator(); iterator.hasNext(); ) {
                OlePurchaseOrderItem olePurchaseOrderItem = iterator.next();
                if(olePurchaseOrderItem.getItemTypeCode().equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
                    String documentNumber = olePurchaseOrderItem.getPurchaseOrder().getDocumentNumber();
                    if(!poStatus.containsKey(documentNumber)) {
                        boolean documentOpen = isDocumentOpen(olePurchaseOrderItem.getPurapDocument().getDocumentNumber());
                        poStatus.put(documentNumber,documentOpen);
                    }
                    Boolean status = poStatus.get(documentNumber);
                    if(null != status && status.equals(Boolean.TRUE)) {
                        filteredItems.add(olePurchaseOrderItem);
                    }
                }
            }
        }
        return filteredItems;
    }

    private boolean isDocumentOpen(String documentNumber) {
        Map purchaseOrderDocNumberMap = new HashMap();
        purchaseOrderDocNumberMap.put("documentNumber", documentNumber);
        List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList = (List<OlePurchaseOrderDocument>) getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, purchaseOrderDocNumberMap);
        if(CollectionUtils.isNotEmpty(olePurchaseOrderDocumentList)) {
            String applicationDocumentStatus = olePurchaseOrderDocumentList.get(0).getApplicationDocumentStatus();
            if(StringUtils.isNotBlank(applicationDocumentStatus) &&
                    applicationDocumentStatus.equals(PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN)){
                return true;
            }
        }
        return false;
    }

    private void addInvoiceRecordToMap(Map<String, List<OleInvoiceRecord>> oleInvoiceRecordMap, OleInvoiceRecord oleInvoiceRecord) {
        oleInvoiceRecord.setUnitPrice(oleInvoiceRecord.getListPrice());

        String invoiceNumber = (oleInvoiceRecord.getInvoiceNumber() != null && !oleInvoiceRecord.getInvoiceNumber().isEmpty())
                ? oleInvoiceRecord.getInvoiceNumber() : "0";

        List oleInvoiceRecords = oleInvoiceRecordMap.get(invoiceNumber);
        if (oleInvoiceRecords == null) {
            oleInvoiceRecords = new ArrayList<OleInvoiceRecord>();
        }
        oleInvoiceRecords.add(oleInvoiceRecord);
        oleInvoiceRecordMap.put(invoiceNumber, oleInvoiceRecords);
    }

    private List<OlePurchaseOrderItem> processMatchpoint(Record record, BatchProcessProfile batchProcessProfile, OleInvoiceRecord oleInvoiceRecord) {
        Map<String, List<String>> criteriaMapForMatchPoint = getCriteriaMapForMatchPoint(record, batchProcessProfile.getBatchProfileMatchPointList());
        for (Iterator<String> iterator = criteriaMapForMatchPoint.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            Map dbCriteria = new HashMap();
            String dbCriteriaKey = "";
            if (key.equalsIgnoreCase(OleNGConstants.BatchProcess.VENDOR_ITEM_IDENTIFIER)) {
                dbCriteriaKey = "vendorItemPoNumber";
            } else if (key.equalsIgnoreCase(OleNGConstants.BatchProcess.PO_NUMBER)) {
                dbCriteriaKey = "purchaseOrder.purapDocumentIdentifier";
            }

            List<String> values = criteriaMapForMatchPoint.get(key);
            for (Iterator<String> matchPointIterator = values.iterator(); matchPointIterator.hasNext(); ) {
                String value = matchPointIterator.next();
                if (StringUtils.isNotBlank(value)) {
                    dbCriteria.put(dbCriteriaKey, value);
                    List<OlePurchaseOrderItem> matching = getPurchaseOrderItemsByCriteria(dbCriteria);
                    if (CollectionUtils.isNotEmpty(matching)) {
                        oleInvoiceRecord.setMatchPointType(key);
                        return matching;
                    }
                }
            }

        }
        return null;
    }

    private List<OlePurchaseOrderItem> getPurchaseOrderItemsByCriteria(Map dbCriteria) {
        List<OlePurchaseOrderItem> matching = (List<OlePurchaseOrderItem>) getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, dbCriteria);
        return filterOpenDocuments(matching);
    }

    private Map<String, List<String>> getCriteriaMapForMatchPoint(Record marcRecord, List<BatchProfileMatchPoint> batchProfileMatchPointList) {
        Map criteriaMap = new HashMap();
        for (Iterator<BatchProfileMatchPoint> iterator = batchProfileMatchPointList.iterator(); iterator.hasNext(); ) {
            BatchProfileMatchPoint batchProfileMatchPoint = iterator.next();
            criteriaMap.put(batchProfileMatchPoint.getMatchPointType(), getMatchPointValue(marcRecord, batchProfileMatchPoint));
        }
        return criteriaMap;
    }

    private List<String> getMatchPointValue(Record marcRecord, BatchProfileMatchPoint batchProfileMatchPoint) {
        List<String> values = new ArrayList<>();
        if (batchProfileMatchPoint.getDataType().equalsIgnoreCase(OleNGConstants.BIB_MARC)) {
            List<String> multiDataFieldValues = getMarcRecordUtil().getMultiDataFieldValues(marcRecord, batchProfileMatchPoint.getDataField(), batchProfileMatchPoint.getInd1(),
                    batchProfileMatchPoint.getInd2(), batchProfileMatchPoint.getSubField());
            if (!batchProfileMatchPoint.isMultiValue()) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Iterator<String> iterator = multiDataFieldValues.iterator(); iterator.hasNext(); ) {
                    String next = iterator.next();
                    stringBuilder.append(next);
                    if (iterator.hasNext()) {
                        stringBuilder.append(",");
                    }
                }
                values.add(stringBuilder.toString());
            } else {
                values.addAll(multiDataFieldValues);
            }
        } else {
            values.add(batchProfileMatchPoint.getMatchPointValue());
        }
        return values;
    }

    @Override
    public String getReportingFilePath() {
        return ConfigContext.getCurrentContextConfig().getProperty("batch.invoice.directory");
    }

    private BatchProcessProfile getBibImportProfile(String profileName) {
        BatchProcessProfile batchProcessProfile = null;
        try {
            List<BatchProcessProfile> batchProcessProfiles = describeDAO.fetchProfileByNameAndType(profileName, "Bib Import");
            if (CollectionUtils.isNotEmpty(batchProcessProfiles)) {
                batchProcessProfile = batchProcessProfiles.get(0);
                getObjectMapper().setVisibilityChecker(getObjectMapper().getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                batchProcessProfile = getObjectMapper().readValue(IOUtils.toString(batchProcessProfile.getContent()), BatchProcessProfile.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return batchProcessProfile;
    }

    private OleNGBibImportResponse processBibImport(String rawContent, Map<Integer, RecordDetails> recordsMap, String fileType, BatchProcessProfile bibImportProfile, String reportDirectoryName) {
        OleNGBibImportResponse oleNGBibImportResponse = new OleNGBibImportResponse();
        try {
            String response = batchBibFileProcessor.processRecords(rawContent, recordsMap, fileType, bibImportProfile, reportDirectoryName);
            if(StringUtils.isNotBlank(response)) {
                oleNGBibImportResponse = getObjectMapper().readValue(response, OleNGBibImportResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oleNGBibImportResponse;
    }

    private OleInvoiceRecord prepareInvoiceOrderRercordFromProfile(Record marcRecord, BatchProcessProfile batchProcessProfile, OleInvoiceRecord oleInvoiceRecord, List<String> datamappingTypes) {
        if(oleInvoiceRecord == null) {
            oleInvoiceRecord = new OleInvoiceRecord();
        }

        List<BatchProfileDataMapping> batchProfileDataMappingList = batchProcessProfile.getBatchProfileDataMappingList();
        Map<String, List<ValueByPriority>> valueByPriorityMap = getvalueByPriorityMapForDataMapping(marcRecord, batchProfileDataMappingList, datamappingTypes);

        for (Iterator<String> iterator = valueByPriorityMap.keySet().iterator(); iterator.hasNext(); ) {
            String destinationField = iterator.next();
            for (Iterator<InvoiceRecordResolver> invoiceRecordResolverIterator = getInvoiceRecordResolvers().iterator(); invoiceRecordResolverIterator.hasNext(); ) {
                InvoiceRecordResolver invoiceRecordResolver = invoiceRecordResolverIterator.next();
                if (invoiceRecordResolver.isInterested(destinationField)) {
                    String value = getDestinationValue(valueByPriorityMap, destinationField);
                    invoiceRecordResolver.setAttributeValue(oleInvoiceRecord, value);
                }
            }
        }

        checkForForeignCurrency(oleInvoiceRecord);
        oleInvoiceRecord.setUnitPrice(oleInvoiceRecord.getListPrice());

        return oleInvoiceRecord;

    }

    private void checkForForeignCurrency(OleInvoiceRecord oleInvoiceRecord) {
        if (!StringUtils.isBlank(oleInvoiceRecord.getCurrencyType())) {
            if (!oleInvoiceRecord.getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                if (oleInvoiceRecord.getForeignListPrice() != null && !oleInvoiceRecord.getForeignListPrice().isEmpty() &&
                        oleInvoiceRecord.getInvoiceCurrencyExchangeRate() != null && !oleInvoiceRecord.getInvoiceCurrencyExchangeRate().isEmpty()) {
                    oleInvoiceRecord.setListPrice((new BigDecimal(oleInvoiceRecord.getForeignListPrice()).
                            divide(new BigDecimal(oleInvoiceRecord.getInvoiceCurrencyExchangeRate()), 4, RoundingMode.HALF_UP)).toString());
                }
            }
        }
    }

    public List<InvoiceRecordResolver> getInvoiceRecordResolvers() {
        if (CollectionUtils.isEmpty(invoiceRecordResolvers)) {
            invoiceRecordResolvers = new ArrayList<>();
            invoiceRecordResolvers.add(new AccountNumberValueResolver());
            invoiceRecordResolvers.add(new CurrencyTypeValueResolver());
            invoiceRecordResolvers.add(new ExchangeRateValueResolver());
            invoiceRecordResolvers.add(new FundCodeValueResolver());
            invoiceRecordResolvers.add(new InvoiceDateValueResolver());
            invoiceRecordResolvers.add(new InvoicedForeignPriceValueResolver());
            invoiceRecordResolvers.add(new InvoicedPriceValueResolver());
            invoiceRecordResolvers.add(new InvoiceNumberValueResolver());
            invoiceRecordResolvers.add(new ItemDescriptionValueResolver());
            invoiceRecordResolvers.add(new ObjectCodeValueResolver());
            invoiceRecordResolvers.add(new QuantityValueResolver());
            invoiceRecordResolvers.add(new VendorItemIdentifierValueResolver());
            invoiceRecordResolvers.add(new VendorNumberValueResolver());
        }
        return invoiceRecordResolvers;
    }

    public void setInvoiceRecordResolvers(List<InvoiceRecordResolver> invoiceRecordResolvers) {
        this.invoiceRecordResolvers = invoiceRecordResolvers;
    }

    public InvoiceImportHelperUtil getInvoiceImportHelperUtil() {
        if(null == invoiceImportHelperUtil) {
            invoiceImportHelperUtil = new InvoiceImportHelperUtil();
        }
        return invoiceImportHelperUtil;
    }

    public void setInvoiceImportHelperUtil(InvoiceImportHelperUtil invoiceImportHelperUtil) {
        this.invoiceImportHelperUtil = invoiceImportHelperUtil;
    }

    public OleNGInvoiceRecordBuilderUtil getOleNGInvoiceRecordBuilderUtil() {
        if(null == oleNGInvoiceRecordBuilderUtil) {
            oleNGInvoiceRecordBuilderUtil = OleNGInvoiceRecordBuilderUtil.getInstance();
        }
        return oleNGInvoiceRecordBuilderUtil;
    }

    public void setOleNGInvoiceRecordBuilderUtil(OleNGInvoiceRecordBuilderUtil oleNGInvoiceRecordBuilderUtil) {
        this.oleNGInvoiceRecordBuilderUtil = oleNGInvoiceRecordBuilderUtil;
    }
}
