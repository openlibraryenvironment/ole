package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrDocument;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.docstore.common.response.*;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileAddOrOverlay;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileMatchPoint;
import org.kuali.ole.oleng.batch.reports.OrderImportReportLogHandler;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.oleng.exception.ValidationException;
import org.kuali.ole.oleng.resolvers.CreateNeitherReqNorPOHandler;
import org.kuali.ole.oleng.resolvers.CreateRequisitionAndPurchaseOrderHander;
import org.kuali.ole.oleng.resolvers.CreateRequisitionOnlyHander;
import org.kuali.ole.oleng.resolvers.OrderProcessHandler;
import org.kuali.ole.oleng.service.OleNGMemorizeService;
import org.kuali.ole.oleng.service.OleNGRequisitionService;
import org.kuali.ole.oleng.util.OleNGPOHelperUtil;
import org.kuali.ole.utility.BibUtil;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.marc4j.marc.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by SheikS on 1/6/2016.
 */
@Service("batchOrderImportProcessor")
public class BatchOrderImportProcessor extends BatchFileProcessor {

    private static final Logger LOG = Logger.getLogger(BatchOrderImportProcessor.class);

    @Autowired
    private DescribeDAO describeDAO;

    @Autowired
    OleNGRequisitionService oleNGRequisitionService;

    @Autowired
    OleNGMemorizeService oleNGMemorizeService;

    @Autowired
    private BatchBibFileProcessor batchBibFileProcessor;
    private List<OrderProcessHandler> orderProcessHandlers;
    private Map matchOptionIndMap;
    private HashMap operationIndMap;
    private OleNGPOHelperUtil oleNGPOHelperUtil;

    @Override
    public OleNgBatchResponse processRecords(Map<Integer, RecordDetails> recordsMap, BatchProcessTxObject batchProcessTxObject,
                                             BatchProcessProfile batchProcessProfile) throws JSONException {
        String response = "";
        BatchJobDetails batchJobDetails = batchProcessTxObject.getBatchJobDetails();
        String reportDirectoryName = batchProcessTxObject.getReportDirectoryName();
        JSONObject jsonObject = new JSONObject();
        OleNGOrderImportResponse oleNGOrderImportResponse = new OleNGOrderImportResponse();
        Map<Integer, Set<Integer>> poIdsMap = new HashMap<>();
        List<Integer> requisitionIds = new ArrayList<>();

        Map<Integer,RecordDetails> unMatchedRecordMap = new HashMap<>();
        Map<Integer,RecordDetails> matchedRecordMap = new HashMap<>();
        Map<Integer,RecordDetails> multipleMatchedRecordMap = new HashMap<>();

        List<OrderData> matchedOrderDatas = new ArrayList<OrderData>();
        List<OrderData> unmatchedOrderDatas = new ArrayList<OrderData>();

        Exchange exchange = batchProcessTxObject.getExchangeForOrderOrInvoiceImport();

        BibUtil bibUtil = new BibUtil();
        if (recordsMap.size() > 0) {

            BatchProcessProfile bibImportProfile = getBibImportProfile(batchProcessProfile.getBibImportProfileForOrderImport());
            if (null != bibImportProfile) {
                Map<Integer,RecordDetails> recordForBibImportsMap = new HashMap<>();
                for (Iterator<Integer> iterator = recordsMap.keySet().iterator(); iterator.hasNext(); ) {
                    Integer index = iterator.next();
                    RecordDetails recordDetails = recordsMap.get(index);
                    Record marcRecord = recordDetails.getRecord();
                    if(null == marcRecord) {
                        addOrderFaiureResponseToExchange(new ValidationException(recordDetails.getMessage()), index, exchange);
                        continue;
                    }

                    try {
                        Map<String, String> bibInfoMap = bibUtil.buildDataValuesForBibInfo(marcRecord);
                        OrderData orderData = new OrderData();
                        orderData.setRecordNumber(index);
                        orderData.setTitle(bibInfoMap.get(DocstoreConstants.TITLE_DISPLAY));

                        List<BatchProfileMatchPoint> batchProfileMatchPointList = getMatchPointToUse(batchProcessProfile, bibImportProfile);
                        String query = getMatchPointProcessor().prepareSolrQueryMapForMatchPoint(marcRecord, batchProfileMatchPointList);

                        if (StringUtils.isNotBlank(query)) {
                            query = query.replace("\\", "\\\\");
                            List results = getSolrRequestReponseHandler().getSolrDocumentList(query);
                            if (null == results || results.size() > 1) {
                                System.out.println("**** More than one record found for query : " + query);
                                multipleMatchedRecordMap.put(index, recordDetails);
                                continue;
                            }

                            if (null != results && results.size() == 1) {
                                SolrDocument solrDocument = (SolrDocument) results.get(0);
                                String bibId = (String) solrDocument.getFieldValue("id");
                                recordDetails.setBibUUID(bibId);
                                matchedRecordMap.put(index, recordDetails);
                                orderData.setSuccessfulMatchPoints(query);
                                matchedOrderDatas.add(orderData);
                            } else {
                                unMatchedRecordMap.put(index, recordDetails);
                                unmatchedOrderDatas.add(orderData);
                            }
                        } else {
                            unMatchedRecordMap.put(index, recordDetails);
                            unmatchedOrderDatas.add(orderData);
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                        addOrderFaiureResponseToExchange(e, index, exchange);
                    }
                }
                recordForBibImportsMap.putAll(matchedRecordMap);
                recordForBibImportsMap.putAll(unMatchedRecordMap);
//                recordForBibImportsMap.putAll(multipleMatchedRecordMap);

                OleNGBibImportResponse oleNGBibImportResponse = null;
                if (recordForBibImportsMap.size() > 0) {
                    oleNGBibImportResponse = batchBibFileProcessor.processBibImportForOrderOrInvoiceImport(recordForBibImportsMap, batchProcessTxObject,bibImportProfile, exchange);
                }

                List<BatchProfileAddOrOverlay> batchProfileAddOrOverlayList = batchProcessProfile.getBatchProfileAddOrOverlayList();

                for (Iterator<BatchProfileAddOrOverlay> iterator = batchProfileAddOrOverlayList.iterator(); iterator.hasNext(); ) {
                    BatchProfileAddOrOverlay batchProfileAddOrOverlay = iterator.next();
                    for (Iterator<OrderProcessHandler> batchProfileAddOrOverlayIterator = getOrderProcessHandlers().iterator(); batchProfileAddOrOverlayIterator.hasNext(); ) {
                        OrderProcessHandler orderProcessHandler = batchProfileAddOrOverlayIterator.next();
                        try {
                            if (orderProcessHandler.isInterested(batchProfileAddOrOverlay.getAddOperation())) {
                                orderProcessHandler.setOleNGRequisitionService(oleNGRequisitionService);
                                orderProcessHandler.setOleNGPOHelperUtil(getOleNGPOHelperUtil());
                                List<RecordDetails> recordsToProcess = new ArrayList<>();
                                List<OrderData> orderDatasToReport = new ArrayList<OrderData>();
                                if (batchProfileAddOrOverlay.getMatchOption().equalsIgnoreCase(OleNGConstants.IF_MATCH_FOUND)) {
                                    recordsToProcess = new ArrayList<RecordDetails>(matchedRecordMap.values());
                                    orderDatasToReport = matchedOrderDatas;
                                } else if (batchProfileAddOrOverlay.getMatchOption().equalsIgnoreCase(OleNGConstants.IF_NOT_MATCH_FOUND)) {
                                    recordsToProcess = buildUnMatchedRecordsWithBibId(oleNGBibImportResponse,unMatchedRecordMap);
                                    orderDatasToReport = unmatchedOrderDatas;
                                }
                                if (recordsToProcess.size() > 0) {
                                    poIdsMap.putAll(orderProcessHandler.processOrder(recordsToProcess, batchProcessProfile,oleNGMemorizeService, exchange));
                                    prepareResponse(batchProfileAddOrOverlay.getAddOperation(), orderDatasToReport, oleNGOrderImportResponse, poIdsMap);
                                }
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            addOrderFaiureResponseToExchange(e, null, exchange);
                        }
                    }
                }


                requisitionIds = new ArrayList<>(poIdsMap.keySet());
                jsonObject.put("status", "Success");
                jsonObject.put("requisitionIds", requisitionIds);
                System.out.println("Order Import Response : " + jsonObject.toString());
                response = jsonObject.toString();

            }
        } else {
            jsonObject.put("status", "failure");
            jsonObject.put("reason", "Invalid record.");
            response = jsonObject.toString();
        }
        List<OrderFailureResponse> orderFailureResponseList = (List<OrderFailureResponse>) exchange.get(OleNGConstants.FAILURE_RESPONSE);
        oleNGOrderImportResponse.setRequisitionIds(requisitionIds);
        oleNGOrderImportResponse.setJobName(batchJobDetails.getJobName());
        oleNGOrderImportResponse.setJobDetailId(String.valueOf(batchJobDetails.getJobDetailId()));
        oleNGOrderImportResponse.setMatchedCount(matchedRecordMap.size());
        oleNGOrderImportResponse.setUnmatchedCount(unMatchedRecordMap.size());
        oleNGOrderImportResponse.setMultiMatchedCount(multipleMatchedRecordMap.size());
        oleNGOrderImportResponse.setOrderFailureResponses(orderFailureResponseList);
        oleNGOrderImportResponse.setRecordsMap(recordsMap);
        OrderImportReportLogHandler orderImportReportLogHandler = OrderImportReportLogHandler.getInstance();

        oleNGOrderImportResponse = mergeResponse(oleNGOrderImportResponse, exchange);
        orderImportReportLogHandler.logMessage(oleNGOrderImportResponse,reportDirectoryName);
        clearMarcRecordObjects(oleNGOrderImportResponse);
        exchange.add(OleNGConstants.ORDER_RESPONSE, oleNGOrderImportResponse);
        exchange.remove(OleNGConstants.FAILURE_RESPONSE);

        OleNgBatchResponse oleNgBatchResponse = new OleNgBatchResponse();
        oleNgBatchResponse.setResponse(response);
        oleNgBatchResponse.setNoOfFailureRecord(getFailureRecordsCount(orderFailureResponseList));

        return oleNgBatchResponse;
    }

    private List<BatchProfileMatchPoint> getMatchPointToUse(BatchProcessProfile orderImportProfile, BatchProcessProfile bibImportProfile) {
        String matchPointToUse = orderImportProfile.getMatchPointToUse();
        if(StringUtils.isNotBlank(matchPointToUse)) {
            if(matchPointToUse.equalsIgnoreCase(OleNGConstants.BIB_IMPORT)) {
                return bibImportProfile.getBatchProfileMatchPointList();
            }
        }
        return orderImportProfile.getBatchProfileMatchPointList();
    }

    private void prepareResponse(String processType, List<OrderData> orderDatas, OleNGOrderImportResponse oleNGOrderImportResponse, Map<Integer, Set<Integer>> poIdsMap) {
        if (CollectionUtils.isNotEmpty(orderDatas)) {
            if (processType.equalsIgnoreCase(OleNGConstants.BatchProcess.CREATE_NEITHER_REQ_NOR_PO)) {
                OrderResponse orderResponse = new OrderResponse();
                orderResponse.setProcessType(processType);
                orderResponse.setOrderDatas(orderDatas);
                oleNGOrderImportResponse.addNoReqNorPOResponse(orderResponse);
            } else {
                if(poIdsMap.size() > 0) {
                    for (Iterator<Integer> iterator = poIdsMap.keySet().iterator(); iterator.hasNext(); ) {
                        Integer reqId = iterator.next();
                        Set<Integer> recordIndexSet = poIdsMap.get(reqId);
                        List<OrderData> createdOrderDatas = new ArrayList<>();
                        for (Iterator<OrderData> integerIterator = orderDatas.iterator(); integerIterator.hasNext(); ) {
                            OrderData orderData = integerIterator.next();
                            if(recordIndexSet.contains(orderData.getRecordNumber())) {
                                orderData.setReqDocumentNumber(reqId);
                                createdOrderDatas.add(orderData);
                            }
                        }
                        if(CollectionUtils.isNotEmpty(createdOrderDatas)) {
                            OrderResponse orderResponse = new OrderResponse();
                            orderResponse.setProcessType(processType);
                            orderResponse.setOrderDatas(createdOrderDatas);
                            if (processType.equalsIgnoreCase(OleNGConstants.BatchProcess.CREATE_REQ_PO)) {
                                oleNGOrderImportResponse.addReqAndPOResponse(orderResponse);
                            } else if (processType.equalsIgnoreCase(OleNGConstants.BatchProcess.CREATE_REQ_ONLY)) {
                                oleNGOrderImportResponse.addReqOnlyResponse(orderResponse);
                            }
                        }
                    }
                }
            }


        }
    }

    private List<RecordDetails> buildUnMatchedRecordsWithBibId(OleNGBibImportResponse oleNGBibImportResponse, Map<Integer, RecordDetails> recordDetailsMap) {
        List<RecordDetails> recordDetailsList = new ArrayList<>();

        if (null != oleNGBibImportResponse) {
            List<BibResponse> bibResponses = oleNGBibImportResponse.getBibResponses();
            if (CollectionUtils.isNotEmpty(bibResponses)) {
                for (Iterator<BibResponse> iterator = bibResponses.iterator(); iterator.hasNext(); ) {
                    BibResponse bibResponse = iterator.next();
                    Integer recordIndex = bibResponse.getRecordIndex();
                    if (recordDetailsMap.containsKey(recordIndex)) {
                        RecordDetails recordDetails = recordDetailsMap.get(recordIndex);
                        if(null != recordDetails) {
                            recordDetails.setBibUUID(bibResponse.getBibId().startsWith(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC)? bibResponse.getBibId() : DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC, bibResponse.getBibId()));
                            recordDetailsList.add(recordDetails);
                        }
                    }
                }
            }
        }
        return recordDetailsList;
    }

    private List<OrderProcessHandler> getOrderProcessHandlers() {
        if (null == orderProcessHandlers) {
            orderProcessHandlers = new ArrayList<>();
            orderProcessHandlers.add(new CreateRequisitionAndPurchaseOrderHander());
            orderProcessHandlers.add(new CreateRequisitionOnlyHander());
            orderProcessHandlers.add(new CreateNeitherReqNorPOHandler());
        }

        return orderProcessHandlers;

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

    @Override
    public String getReportingFilePath() {
        return ConfigContext.getCurrentContextConfig().getProperty("batch.orderRecord.directory");
    }

    private String getMatchOptionInd(String matchOption) {
        return getMatchOptionIndMap().get(matchOption);
    }

    private String getActionInd(String operation) {
        return getOperationIndMap().get(operation);
    }

    public Map<String, String> getMatchOptionIndMap() {
        if (null == matchOptionIndMap) {
            matchOptionIndMap = new HashMap();
            matchOptionIndMap.put("If Match Found", "1");
            matchOptionIndMap.put("If Match Not Found", "2");
        }
        return matchOptionIndMap;
    }

    public Map<String, String> getOperationIndMap() {
        if (null == operationIndMap) {
            operationIndMap = new HashMap();
            operationIndMap.put("Create Requisition Only", "1");
            operationIndMap.put("Create Requisition and PO", "2");
            operationIndMap.put("Create neither a Requisition nor a PO", "3");
        }
        return operationIndMap;
    }

    private OleNGOrderImportResponse mergeResponse(OleNGOrderImportResponse newResponseObject, org.kuali.ole.Exchange exchange) {
        OleNGOrderImportResponse oldResponseObject = (OleNGOrderImportResponse) exchange.get(OleNGConstants.ORDER_RESPONSE);
        if(null == oldResponseObject) {
            return newResponseObject;
        }
        return mergeBibImportResponse(oldResponseObject, newResponseObject);
    }

    private OleNGOrderImportResponse mergeBibImportResponse(OleNGOrderImportResponse oldOleNGOrderImportResponse, OleNGOrderImportResponse newOleNGOrderImportResponse) {
        oldOleNGOrderImportResponse.getRequisitionIds().addAll(newOleNGOrderImportResponse.getRequisitionIds());
        oldOleNGOrderImportResponse.getOrderFailureResponses().addAll(newOleNGOrderImportResponse.getOrderFailureResponses());
        oldOleNGOrderImportResponse.getReqOnlyResponses().addAll(newOleNGOrderImportResponse.getReqOnlyResponses());
        oldOleNGOrderImportResponse.getReqAndPOResponses().addAll(newOleNGOrderImportResponse.getReqAndPOResponses());
        oldOleNGOrderImportResponse.getNoReqNorPOResponses().addAll(newOleNGOrderImportResponse.getNoReqNorPOResponses());

        oldOleNGOrderImportResponse.setRecordsMap(newOleNGOrderImportResponse.getRecordsMap());

        oldOleNGOrderImportResponse.setMatchedCount(oldOleNGOrderImportResponse.getMatchedCount() + newOleNGOrderImportResponse.getMatchedCount());
        oldOleNGOrderImportResponse.setUnmatchedCount(oldOleNGOrderImportResponse.getUnmatchedCount() + newOleNGOrderImportResponse.getUnmatchedCount());
        oldOleNGOrderImportResponse.setMultiMatchedCount(oldOleNGOrderImportResponse.getMultiMatchedCount() + newOleNGOrderImportResponse.getMultiMatchedCount());

        return  oldOleNGOrderImportResponse;
    }

    private void clearMarcRecordObjects(OleNGOrderImportResponse oleNGOrderImportResponse) {
        oleNGOrderImportResponse.getRecordsMap().clear();
    }

    public OleNGPOHelperUtil getOleNGPOHelperUtil() {
        if(null == oleNGPOHelperUtil) {
            oleNGPOHelperUtil = new OleNGPOHelperUtil();
        }
        return oleNGPOHelperUtil;
    }

    public void setOleNGPOHelperUtil(OleNGPOHelperUtil oleNGPOHelperUtil) {
        this.oleNGPOHelperUtil = oleNGPOHelperUtil;
    }
}
