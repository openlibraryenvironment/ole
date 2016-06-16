package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.docstore.common.response.*;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.batch.process.model.ValueByPriority;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataMapping;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileMatchPoint;
import org.kuali.ole.oleng.batch.reports.InvoiceImportLoghandler;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.oleng.exception.ValidationException;
import org.kuali.ole.oleng.resolvers.invoiceimport.*;
import org.kuali.ole.oleng.service.OleNGInvoiceService;
import org.kuali.ole.oleng.util.InvoiceImportHelperUtil;
import org.kuali.ole.oleng.util.OleNGInvoiceRecordBuilderUtil;
import org.kuali.ole.oleng.util.OleNGInvoiceValidationUtil;
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

import java.io.File;
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
    private OleNGInvoiceValidationUtil oleNGInvoiceValidationUtil;

    @Override
    public OleNgBatchResponse processRecords(Map<Integer, RecordDetails> recordsMap, BatchProcessTxObject batchProcessTxObject, BatchProcessProfile batchProcessProfile) throws JSONException {
        JSONObject response = new JSONObject();
        BatchJobDetails batchJobDetails = batchProcessTxObject.getBatchJobDetails();
        String reportDirectoryName = batchProcessTxObject.getReportDirectoryName();
        String fileType = batchProcessTxObject.getFileExtension();
        OleNGInvoiceImportResponse oleNGInvoiceImportResponse = new OleNGInvoiceImportResponse();
        MatchedDetails matchedDetails = new MatchedDetails();
        Exchange exchange = batchProcessTxObject.getExchangeForOrderOrInvoiceImport();

        Map<String, List<OleInvoiceRecord>> oleinvoiceRecordMap = new HashMap<>();
        if (fileType.equalsIgnoreCase(OleNGConstants.MARC)) {
            if (recordsMap.size() > 0) {
                BatchProcessProfile bibImportProfile = getBibImportProfile(batchProcessProfile.getBibImportProfileForOrderImport());
                if (null != bibImportProfile) {
                    OleNGBibImportResponse oleNGBibImportResponse = batchBibFileProcessor.processBibImportForOrderOrInvoiceImport(recordsMap, batchProcessTxObject, bibImportProfile, exchange);
                    List<RecordDetails> recordDetailses = new ArrayList<RecordDetails>(recordsMap.values());
                    oleinvoiceRecordMap = prepareInvoiceRecordsForMarc(recordDetailses, batchProcessProfile, matchedDetails, exchange);
                }
            }
        } else if (fileType.equalsIgnoreCase(OleNGConstants.INV) || fileType.equalsIgnoreCase(OleNGConstants.EDI)) {
            oleinvoiceRecordMap = prepareInvoiceRecordsForEdifact(batchProcessTxObject, batchProcessProfile, matchedDetails, batchJobDetails, exchange);
        }


        for (Iterator<String> iterator = oleinvoiceRecordMap.keySet().iterator(); iterator.hasNext(); ) {
            String invoiceNumber = iterator.next();
            List<OleInvoiceRecord> oleInvoiceRecords = oleinvoiceRecordMap.get(invoiceNumber);

            try {
                OleInvoiceDocument oleInvoiceDocument = oleNGInvoiceService.createNewInvoiceDocument();
                InvoiceResponse invoiceResponse = new InvoiceResponse();
                if (CollectionUtils.isNotEmpty(oleInvoiceRecords)) {
                    oleInvoiceDocument = oleNGInvoiceService.populateInvoiceDocWithOrderInformation(oleInvoiceDocument, oleInvoiceRecords, exchange);
                    if(oleInvoiceDocument != null) {
                        oleNGInvoiceService.saveInvoiceDocument(oleInvoiceDocument);
                        String documentNumber = oleInvoiceDocument.getDocumentNumber();
                        if (null != documentNumber) {
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
                        }
                    }
                }
                oleNGInvoiceImportResponse.getInvoiceResponses().add(invoiceResponse);
            } catch (Exception e) {
                e.printStackTrace();
                addInvoiceFaiureResponseToExchange(e, null, exchange);
            }
        }

        oleNGInvoiceImportResponse.setJobName(batchJobDetails.getJobName());
        oleNGInvoiceImportResponse.setJobDetailId(String.valueOf(batchJobDetails.getJobDetailId()));
        oleNGInvoiceImportResponse.setMatchedCount(matchedDetails.getMatchedCount());
        oleNGInvoiceImportResponse.setUnmatchedCount(matchedDetails.getUnmatchedCount());
        oleNGInvoiceImportResponse.setMultiMatchedCount(matchedDetails.getMultiMatchedCount());
        OleNgBatchResponse oleNgBatchResponse = new OleNgBatchResponse();

        List<InvoiceFailureResponse> invoiceFailureResponseList = (List<InvoiceFailureResponse>) exchange.get(OleNGConstants.FAILURE_RESPONSE);
        oleNGInvoiceImportResponse.setInvoiceFailureResponses(invoiceFailureResponseList);
        oleNGInvoiceImportResponse.setRecordsMap(recordsMap);
        oleNgBatchResponse.setNoOfFailureRecord(getFailureRecordsCount(invoiceFailureResponseList));
        try {
            String successResponse = getObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(oleNGInvoiceImportResponse);
            oleNGInvoiceImportResponse = mergeResponse(oleNGInvoiceImportResponse, exchange);
            oleNGInvoiceImportResponse.setFileExtension(fileType);
            InvoiceImportLoghandler invoiceImportLoghandler = InvoiceImportLoghandler.getInstance();
            invoiceImportLoghandler.logMessage(oleNGInvoiceImportResponse, reportDirectoryName);
            clearMarcRecordObjects(oleNGInvoiceImportResponse);
            exchange.add(OleNGConstants.INVOICE_RESPONSE, oleNGInvoiceImportResponse);
            exchange.remove(OleNGConstants.FAILURE_RESPONSE);
            oleNgBatchResponse.setResponse(successResponse);
            return oleNgBatchResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.put(OleNGConstants.STATUS, OleNGConstants.FAILURE);
        oleNgBatchResponse.setResponse(response.toString());

        return oleNgBatchResponse;
    }

    private Map<String, List<OleInvoiceRecord>> prepareInvoiceRecordsForMarc(List<RecordDetails> recordDetailses , BatchProcessProfile batchProcessProfile,
                                                                             MatchedDetails matchedDetails, Exchange exchange) {
        Map<String, List<OleInvoiceRecord>> oleInvoiceRecordMap = new TreeMap<>();
        ArrayList<String> datamappingTypes = new ArrayList<>();
        datamappingTypes.add(OleNGConstants.CONSTANT);
        datamappingTypes.add(OleNGConstants.BIB_MARC);
        for (int index=0 ; index < recordDetailses.size() ; index++) {
            RecordDetails recordDetails = recordDetailses.get(index);
            Integer recordIndex = recordDetails.getIndex();
            try {
                Record record = recordDetails.getRecord();
                if(null == record) {
                    addInvoiceFaiureResponseToExchange(new ValidationException(recordDetails.getMessage()), recordIndex, exchange);
                    continue;
                }
                OleInvoiceRecord oleInvoiceRecord = prepareInvoiceOrderRercordFromProfile(record, batchProcessProfile, null, datamappingTypes);
                oleInvoiceRecord.setRecordIndex(recordIndex);

                //Match Point process start
                if (CollectionUtils.isNotEmpty(batchProcessProfile.getBatchProfileMatchPointList())) {
                    List<OlePurchaseOrderItem> olePurchaseOrderItems = processMatchpoint(record, batchProcessProfile, oleInvoiceRecord, exchange);
                    setLinkedOrUnlinked(oleInvoiceRecord, olePurchaseOrderItems, matchedDetails);
                }


                addInvoiceRecordToMap(oleInvoiceRecordMap, oleInvoiceRecord, exchange);
            } catch(Exception e) {
                e.printStackTrace();
                addInvoiceFaiureResponseToExchange(e, recordIndex, exchange);
            }
        }
        return oleInvoiceRecordMap;
    }


    private Map<String, List<OleInvoiceRecord>> prepareInvoiceRecordsForEdifact(BatchProcessTxObject batchProcessTxObject, BatchProcessProfile batchProcessProfile,
                                                                                MatchedDetails matchedDetails, BatchJobDetails batchJobDetails, Exchange exchange) {
        Map<String, List<OleInvoiceRecord>> oleInvoiceRecordMap = new TreeMap<>();

        File file = new File(batchProcessTxObject.getIncomingFileDirectoryPath(), batchProcessTxObject.getBatchJobDetails().getFileName());
        String rawContent = null;
        try {
            rawContent = FileUtils.readFileToString(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<INVOrder> invOrders = getInvoiceImportHelperUtil().readEDIContent(rawContent);
        int totalNoOfRecords = 0;

        ArrayList<String> datamappingTypes = new ArrayList<>();
        datamappingTypes.add(OleNGConstants.CONSTANT);

        if (CollectionUtils.isNotEmpty(invOrders)) {
            for (Iterator<INVOrder> iterator = invOrders.iterator(); iterator.hasNext(); ) {
                INVOrder invOrder = iterator.next();
                List<LineItemOrder> lineItemOrders = invOrder.getLineItemOrder();
                if (CollectionUtils.isNotEmpty(lineItemOrders)) {
                    for(int index = 0; index < lineItemOrders.size() ; index++) {
                        LineItemOrder lineItemOrder = lineItemOrders.get(index);
                        totalNoOfRecords++;
                        OleInvoiceRecord oleInvoiceRecord = null;
                        try {
                            oleInvoiceRecord = getOleNGInvoiceRecordBuilderUtil().build(lineItemOrder, invOrder);
                            oleInvoiceRecord = prepareInvoiceOrderRercordFromProfile(null, batchProcessProfile, oleInvoiceRecord, datamappingTypes);
                            String[] vendorIds = oleInvoiceRecord.getVendorNumber() != null ? oleInvoiceRecord.getVendorNumber().split("-") : new String[0];
                            List<OlePurchaseOrderItem> olePurchaseOrderItems = new ArrayList<>();
                            if (oleInvoiceRecord.getVendorNumber() != null && vendorIds != null && vendorIds.length == 2 && StringUtils.isNotBlank(vendorIds[0]) && StringUtils.isNotBlank(vendorIds[1])) {
                                Map dbCriteria = new HashMap();
                                olePurchaseOrderItems = new ArrayList<>();
                                if (StringUtils.isNotBlank(oleInvoiceRecord.getVendorItemIdentifier())) {
                                    dbCriteria.put("vendorItemPoNumber", oleInvoiceRecord.getVendorItemIdentifier());
                                    dbCriteria.put("purchaseOrder.vendorHeaderGeneratedIdentifier", vendorIds.length > 0 ? vendorIds[0] : "");
                                    dbCriteria.put("purchaseOrder.vendorDetailAssignedIdentifier", vendorIds.length > 1 ? vendorIds[1] : "");
                                    oleInvoiceRecord.setMatchPointType(OleNGConstants.BatchProcess.VENDOR_ITEM_IDENTIFIER);
                                    olePurchaseOrderItems = getPurchaseOrderItemsByCriteria(dbCriteria);
                                }

                                if (CollectionUtils.isEmpty(olePurchaseOrderItems) && null != oleInvoiceRecord.getPurchaseOrderNumber()
                                        &&oleInvoiceRecord.getPurchaseOrderNumber() != 0) {
                                    dbCriteria.clear();
                                    oleInvoiceRecord.setMatchPointType(OleNGConstants.BatchProcess.PO_NUMBER);
                                    dbCriteria.put("purchaseOrder.purapDocumentIdentifier", oleInvoiceRecord.getPurchaseOrderNumber());
                                    dbCriteria.put("purchaseOrder.vendorHeaderGeneratedIdentifier", vendorIds.length > 0 ? vendorIds[0] : "");
                                    dbCriteria.put("purchaseOrder.vendorDetailAssignedIdentifier", vendorIds.length > 1 ? vendorIds[1] : "");
                                    olePurchaseOrderItems = getPurchaseOrderItemsByCriteria(dbCriteria);
                                }

                                else if (CollectionUtils.isEmpty(olePurchaseOrderItems) &&
                                        (StringUtils.isNotBlank(oleInvoiceRecord.getISSN()) || StringUtils.isNotBlank(oleInvoiceRecord.getISBN()))) {
                                    List<String> values = new ArrayList<>();
                                    values.add(oleInvoiceRecord.getISSN());
                                    values.add(oleInvoiceRecord.getISBN());
                                    Set<String> itemTitleIds = getItemTitleId(values);
                                    if(CollectionUtils.isNotEmpty(itemTitleIds)) {
                                        for (Iterator<String> stringIterator = itemTitleIds.iterator(); stringIterator.hasNext(); ) {
                                            String itemTitleId = stringIterator.next();
                                            if(StringUtils.isNotBlank(itemTitleId)) {
                                                dbCriteria.clear();
                                                dbCriteria.put("itemTitleId", itemTitleId);
                                                dbCriteria.put("purchaseOrder.vendorHeaderGeneratedIdentifier", vendorIds.length > 0 ? vendorIds[0] : "");
                                                dbCriteria.put("purchaseOrder.vendorDetailAssignedIdentifier", vendorIds.length > 1 ? vendorIds[1] : "");
                                                olePurchaseOrderItems.addAll(getPurchaseOrderItemsByCriteria(dbCriteria));
                                            }
                                        }
                                    }

                                    Set<String> poDocumentNumbers = new HashSet<>();
                                    if(CollectionUtils.isNotEmpty(olePurchaseOrderItems)) {
                                        for (Iterator<OlePurchaseOrderItem> iterator1 = olePurchaseOrderItems.iterator(); iterator1.hasNext(); ) {
                                            OlePurchaseOrderItem olePurchaseOrderItem = iterator1.next();
                                            String documentNumber = olePurchaseOrderItem.getPurchaseOrder().getDocumentNumber();
                                            poDocumentNumbers.add(documentNumber);
                                        }
                                    }

                                    if(poDocumentNumbers.size() > 1) {
                                        olePurchaseOrderItems.clear();
                                    }
                                }
                            }
                            oleInvoiceRecord.setRecordIndex(totalNoOfRecords);
                            setLinkedOrUnlinked(oleInvoiceRecord, olePurchaseOrderItems, matchedDetails);

                            addInvoiceRecordToMap(oleInvoiceRecordMap, oleInvoiceRecord, exchange);
                        } catch (Exception e) {
                            e.printStackTrace();
                            addInvoiceFaiureResponseToExchange(e, totalNoOfRecords, exchange);
                        }
                    }
                }
            }
        }

        if (batchJobDetails.getJobId() != 0 && batchJobDetails.getJobDetailId() != 0) {
            batchJobDetails.setTotalRecords(String.valueOf(totalNoOfRecords));
            getBusinessObjectService().save(batchJobDetails);
        }

        return oleInvoiceRecordMap;
    }

    private void setLinkedOrUnlinked(OleInvoiceRecord oleInvoiceRecord, List<OlePurchaseOrderItem> olePurchaseOrderItems,
                                     MatchedDetails matchedDetails) {
        if (CollectionUtils.isNotEmpty(olePurchaseOrderItems)) {
            oleInvoiceRecord.setOlePurchaseOrderItems(olePurchaseOrderItems);
            if (oleInvoiceRecord.getMatchPointType().equalsIgnoreCase(OleNGConstants.BatchProcess.VENDOR_ITEM_IDENTIFIER)) {
                if(olePurchaseOrderItems.size() == 1) {
                    oleInvoiceRecord.setLink(true);
                    matchedDetails.setMatchedCount(matchedDetails.getMatchedCount() + 1);
                } else {
                    matchedDetails.setMultiMatchedCount(matchedDetails.getMultiMatchedCount() + 1);
                }
            } else {
                oleInvoiceRecord.setLink(true);
                matchedDetails.setMatchedCount(matchedDetails.getMatchedCount() + 1);
            }
        } else {
            matchedDetails.setUnmatchedCount(matchedDetails.getUnmatchedCount() + 1);
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

    private void addInvoiceRecordToMap(Map<String, List<OleInvoiceRecord>> oleInvoiceRecordMap, OleInvoiceRecord oleInvoiceRecord, Exchange exchange) {
        oleInvoiceRecord.setUnitPrice(oleInvoiceRecord.getListPrice());

        boolean valid = getOleNGInvoiceValidationUtil().validateOleInvoiceRecord(oleInvoiceRecord, exchange, oleInvoiceRecord.getRecordIndex());
        if(valid) {
            String invoiceNumber = oleInvoiceRecord.getInvoiceNumber();

            List oleInvoiceRecords = oleInvoiceRecordMap.get(invoiceNumber);
            if (oleInvoiceRecords == null) {
                oleInvoiceRecords = new ArrayList<OleInvoiceRecord>();
            }
            oleInvoiceRecords.add(oleInvoiceRecord);
            oleInvoiceRecordMap.put(invoiceNumber, oleInvoiceRecords);
        }
    }

    private List<OlePurchaseOrderItem> processMatchpoint(Record record, BatchProcessProfile batchProcessProfile, OleInvoiceRecord oleInvoiceRecord, Exchange exchange) {
        Map<String, List<String>> criteriaMapForMatchPoint = getCriteriaMapForMatchPoint(record, batchProcessProfile.getBatchProfileMatchPointList());

        if(StringUtils.isNotBlank(oleInvoiceRecord.getVendorNumber())) {
            String[] vendorIds = oleInvoiceRecord.getVendorNumber() != null ? oleInvoiceRecord.getVendorNumber().split("-") : new String[0];
            if (vendorIds != null && vendorIds.length == 2 && StringUtils.isNotBlank(vendorIds[0]) && StringUtils.isNotBlank(vendorIds[1])) {
                for (Iterator<String> iterator = criteriaMapForMatchPoint.keySet().iterator(); iterator.hasNext(); ) {
                    String key = iterator.next();
                    Map dbCriteria = new HashMap();
                    dbCriteria.put("purchaseOrder.vendorHeaderGeneratedIdentifier", vendorIds.length > 0 ? vendorIds[0] : "");
                    dbCriteria.put("purchaseOrder.vendorDetailAssignedIdentifier", vendorIds.length > 1 ? vendorIds[1] : "");
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


    private Set<String> getItemTitleId(List<String> values) {
        StringBuilder queryBuilder = new StringBuilder();
        for (Iterator<String> iterator = values.iterator(); iterator.hasNext(); ) {
            String value = iterator.next();
            if(StringUtils.isNotBlank(value)) {
                value = OleNGConstants.COMMON_IDENTIFIER_SEARCH + ":\"" + value + "\"";
                appendQuery(queryBuilder, value);
            }
        }
        Set<String> itemTitleIds = new TreeSet<>();
        if(StringUtils.isNotBlank(queryBuilder.toString())) {
            String query = OleNGConstants.BIB_QUERY_BEGIN + queryBuilder.toString() + "))";
            List results = getSolrRequestReponseHandler().getSolrDocumentList(query);
            if (CollectionUtils.isNotEmpty(results)) {
                SolrDocument solrDocument = (SolrDocument) results.get(0);
                String bibId = (String) solrDocument.getFieldValue(OleNGConstants.ID);
                itemTitleIds.add(bibId);
            }
        }
        return itemTitleIds;
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
            invoiceRecordResolvers.add(new LineItemNotesValueResolver());
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

    public OleNGInvoiceValidationUtil getOleNGInvoiceValidationUtil() {
        if(null == oleNGInvoiceValidationUtil) {
            this.oleNGInvoiceValidationUtil = new OleNGInvoiceValidationUtil();
        }
        return oleNGInvoiceValidationUtil;
    }

    public void setOleNGInvoiceValidationUtil(OleNGInvoiceValidationUtil oleNGInvoiceValidationUtil) {
        this.oleNGInvoiceValidationUtil = oleNGInvoiceValidationUtil;
    }

    private OleNGInvoiceImportResponse mergeResponse(OleNGInvoiceImportResponse newOleNGInvoiceImportResponse, org.kuali.ole.Exchange exchange) {
        OleNGInvoiceImportResponse oldResponseObject = (OleNGInvoiceImportResponse) exchange.get(OleNGConstants.INVOICE_RESPONSE);
        if(null == oldResponseObject) {
            return newOleNGInvoiceImportResponse;
        }
        return mergeBibImportResponse(oldResponseObject, newOleNGInvoiceImportResponse);
    }

    private OleNGInvoiceImportResponse mergeBibImportResponse(OleNGInvoiceImportResponse oldOleNGInvoiceImportResponse, OleNGInvoiceImportResponse newOleNGInvoiceImportResponse) {
        oldOleNGInvoiceImportResponse.getInvoiceResponses().addAll(newOleNGInvoiceImportResponse.getInvoiceResponses());
        oldOleNGInvoiceImportResponse.getInvoiceFailureResponses().addAll(newOleNGInvoiceImportResponse.getInvoiceFailureResponses());

        oldOleNGInvoiceImportResponse.setRecordsMap(newOleNGInvoiceImportResponse.getRecordsMap());

        oldOleNGInvoiceImportResponse.setMatchedCount(oldOleNGInvoiceImportResponse.getMatchedCount() + newOleNGInvoiceImportResponse.getMatchedCount());
        oldOleNGInvoiceImportResponse.setUnmatchedCount(oldOleNGInvoiceImportResponse.getUnmatchedCount() + newOleNGInvoiceImportResponse.getUnmatchedCount());
        oldOleNGInvoiceImportResponse.setMultiMatchedCount(oldOleNGInvoiceImportResponse.getMultiMatchedCount() + newOleNGInvoiceImportResponse.getMultiMatchedCount());

        return  oldOleNGInvoiceImportResponse;
    }

    private void clearMarcRecordObjects(OleNGInvoiceImportResponse oleNGInvoiceImportResponse) {
        oleNGInvoiceImportResponse.getRecordsMap().clear();
    }

    class MatchedDetails {
        private int matchedCount;
        private int unmatchedCount;
        private int multiMatchedCount;

        public int getMatchedCount() {
            return matchedCount;
        }

        public void setMatchedCount(int matchedCount) {
            this.matchedCount = matchedCount;
        }

        public int getUnmatchedCount() {
            return unmatchedCount;
        }

        public void setUnmatchedCount(int unmatchedCount) {
            this.unmatchedCount = unmatchedCount;
        }

        public int getMultiMatchedCount() {
            return multiMatchedCount;
        }

        public void setMultiMatchedCount(int multiMatchedCount) {
            this.multiMatchedCount = multiMatchedCount;
        }
    }
}
