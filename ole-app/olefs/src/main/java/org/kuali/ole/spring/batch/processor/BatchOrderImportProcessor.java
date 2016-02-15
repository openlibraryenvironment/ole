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
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.BibResponse;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileAddOrOverlay;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.oleng.handler.CreateReqAndPOServiceHandler;
import org.kuali.ole.oleng.resolvers.CreateRequisitionAndPurchaseOrderHander;
import org.kuali.ole.oleng.resolvers.CreateRequisitionOnlyHander;
import org.kuali.ole.oleng.resolvers.OrderProcessHandler;
import org.kuali.ole.oleng.service.OleNGRequisitionService;
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
    private CreateReqAndPOServiceHandler orderRequestHandler;

    @Autowired
    private DescribeDAO describeDAO;


    @Autowired
    OleNGRequisitionService oleNGRequisitionService;

    @Autowired
    private BatchBibFileProcessor batchBibFileProcessor;
    private List<OrderProcessHandler> orderProcessHandlers;
    private Map matchOptionIndMap;
    private HashMap operationIndMap;

    @Override
    public String processRecords(List<Record> records, BatchProcessProfile batchProcessProfile) throws JSONException {
        String response = "";
        JSONObject jsonObject = new JSONObject();

        Map<String, Record> matchedRecords = new HashMap();
        List<Record> unMatchedRecords = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(records)) {

            BatchProcessProfile bibImportProfile = getBibImportProfile(batchProcessProfile.getBibImportProfileForOrderImport());
            if (null != bibImportProfile) {

                for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
                    Record marcRecord = iterator.next();
                    String query = getMatchPointProcessor().prepareSolrQueryMapForMatchPoint(marcRecord, batchProcessProfile.getBatchProfileMatchPointList());
                    if (StringUtils.isNotBlank(query)) {
                        List results = getSolrRequestReponseHandler().getSolrDocumentList(query);
                        if (null == results || results.size() > 1) {
                            System.out.println("**** More than one record found for query : " + query);
                            continue;
                        }

                        if (null != results && results.size() == 1) {
                            SolrDocument solrDocument = (SolrDocument) results.get(0);
                            String bibId = (String) solrDocument.getFieldValue("id");
                            matchedRecords.put(bibId, marcRecord);

                        } else {
                            unMatchedRecords.add(marcRecord);
                        }
                    }
                }

                OleNGBibImportResponse oleNGBibImportResponse = null;
                if (CollectionUtils.isNotEmpty(unMatchedRecords)) {
                    oleNGBibImportResponse = processBibImport(unMatchedRecords, bibImportProfile);
                }

                List<BatchProfileAddOrOverlay> batchProfileAddOrOverlayList = batchProcessProfile.getBatchProfileAddOrOverlayList();

                List<Integer> purapIds = new ArrayList<>();
                for (Iterator<BatchProfileAddOrOverlay> iterator = batchProfileAddOrOverlayList.iterator(); iterator.hasNext(); ) {
                    BatchProfileAddOrOverlay batchProfileAddOrOverlay = iterator.next();
                    for (Iterator<OrderProcessHandler> batchProfileAddOrOverlayIterator = getOrderProcessHandlers().iterator(); batchProfileAddOrOverlayIterator.hasNext(); ) {
                        OrderProcessHandler orderProcessHandler = batchProfileAddOrOverlayIterator.next();
                        if (orderProcessHandler.isInterested(batchProfileAddOrOverlay.getAddOperation())) {
                            orderProcessHandler.setOleNGRequisitionService(oleNGRequisitionService);
                            Map<String, Record> recordsToProcess = new HashMap<>();
                            if(batchProfileAddOrOverlay.getMatchOption().equalsIgnoreCase(OleNGConstants.IF_MATCH_FOUND)) {
                                recordsToProcess = matchedRecords;
                            } else if(batchProfileAddOrOverlay.getMatchOption().equalsIgnoreCase(OleNGConstants.IF_NOT_MATCH_FOUND)) {
                                recordsToProcess = buildUnMatchedRecordsWithBibId(oleNGBibImportResponse, matchedRecords);
                            }
                            try {
                                if (recordsToProcess.size() > 0) {
                                    purapIds.addAll(orderProcessHandler.processOrder(recordsToProcess, batchProcessProfile, orderRequestHandler));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }

                jsonObject.put("status", "Success");
                jsonObject.put("requisitionIds", purapIds);
                System.out.println("Order Import Response : " + jsonObject.toString());
                return jsonObject.toString();

            }
        } else {
            jsonObject.put("status", "failure");
            jsonObject.put("reason", "Invalid record.");
            response = jsonObject.toString();
        }
        return response;
    }

    private Map<String, Record> buildUnMatchedRecordsWithBibId(OleNGBibImportResponse oleNGBibImportResponse, Map<String, Record> matchedRecords) {

        Map<String, Record> map = new HashMap<>();

        if (null != oleNGBibImportResponse) {
            List<BibResponse> bibResponses = oleNGBibImportResponse.getBibResponses();
            if(CollectionUtils.isNotEmpty(bibResponses)) {
                for (Iterator<BibResponse> iterator = bibResponses.iterator(); iterator.hasNext(); ) {
                    BibResponse bibResponse = iterator.next();
                    String bibId = bibResponse.getBibId();
                    if (!matchedRecords.containsKey(bibId)) {
                        String bibIdWithoutPrefix = DocumentUniqueIDPrefix.getDocumentId(bibId);
                        BibRecord matchedBibRecord = getBusinessObjectService().findBySinglePrimaryKey(BibRecord.class, bibIdWithoutPrefix);
                        if(null != matchedBibRecord) {
                            String content = matchedBibRecord.getContent();
                            List<Record> records = getMarcRecordUtil().convertMarcXmlContentToMarcRecord(content);
                            if(CollectionUtils.isNotEmpty(records)) {
                                map.put(bibId, records.get(0));
                            }
                        }
                    }
                }
            }
        }
        return map;
    }

    private List<OrderProcessHandler> getOrderProcessHandlers() {
        if (null == orderProcessHandlers) {
            orderProcessHandlers = new ArrayList<>();
            orderProcessHandlers.add(new CreateRequisitionAndPurchaseOrderHander());
            orderProcessHandlers.add(new CreateRequisitionOnlyHander());
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

    private OleNGBibImportResponse processBibImport(List<Record> records, BatchProcessProfile bibImportProfile) {
        OleNGBibImportResponse oleNGBibImportResponse = new OleNGBibImportResponse();
        try {
            String response = batchBibFileProcessor.processRecords(records, bibImportProfile);
            oleNGBibImportResponse = getObjectMapper().readValue(response, OleNGBibImportResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oleNGBibImportResponse;
    }


    @Override
    public String getReportingFilePath() {
        return ConfigContext.getCurrentContextConfig().getProperty("batch.orderRecord.directory");
    }

    private List<String> getOptions(List<BatchProfileAddOrOverlay> optionsList) {
        List<String> options = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(optionsList)) {
            for (Iterator<BatchProfileAddOrOverlay> iterator = optionsList.iterator(); iterator.hasNext(); ) {
                BatchProfileAddOrOverlay batchProfileAddOrOverlay = iterator.next();
                String matchOption = batchProfileAddOrOverlay.getMatchOption();
                String operation = batchProfileAddOrOverlay.getAddOperation();
                String option = getMatchOptionInd(matchOption) + getActionInd(operation);
                options.add(option);
            }
        }
        return options;
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

}
