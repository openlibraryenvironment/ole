package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrDocument;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.common.response.BibResponse;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
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
                            return null;
                        }

                        if (null != results && results.size() == 1) {
                            SolrDocument solrDocument = (SolrDocument) results.get(0);
                            String bibId = (String) solrDocument.getFieldValue("LocalId_display");
                            matchedRecords.put(bibId, marcRecord);

                        } else {
                            unMatchedRecords.add(marcRecord);
                        }
                    }
                }

                OleNGBibImportResponse oleNGBibImportResponse = processBibImport(records, bibImportProfile);

                List<BatchProfileAddOrOverlay> batchProfileAddOrOverlayList = batchProcessProfile.getBatchProfileAddOrOverlayList();
                List<String> options = getOptions(batchProfileAddOrOverlayList);

                for (Iterator<OrderProcessHandler> batchProfileAddOrOverlayIterator = getOrderProcessHandlers().iterator(); batchProfileAddOrOverlayIterator.hasNext(); ) {
                    OrderProcessHandler orderProcessHandler = batchProfileAddOrOverlayIterator.next();
                    if (orderProcessHandler.isInterested(options, matchedRecords.size() > 0, unMatchedRecords.size() > 0)) {
                        orderProcessHandler.setOleNGRequisitionService(oleNGRequisitionService);
                        List<Record> values = new ArrayList<>();
                        if(!matchedRecords.isEmpty()){
                            values.addAll(matchedRecords.values());
                        }
                        unMatchedRecords.addAll(values);


                        Map<String, Record> buildUnMatchedRecordsWithBibId = buildUnMatchedRecordsWithBibId(oleNGBibImportResponse, unMatchedRecords);
                        matchedRecords.putAll(buildUnMatchedRecordsWithBibId);
                        try {
                            orderProcessHandler.processOrder(matchedRecords, batchProcessProfile, orderRequestHandler);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        } else {
            jsonObject.put("status", "failure");
            jsonObject.put("reason", "Invalid record.");
            response = jsonObject.toString();
        }
        return response;
    }

    private Map<String, Record> buildUnMatchedRecordsWithBibId(OleNGBibImportResponse oleNGBibImportResponse, List<Record> unMatchedRecords) {

        Map<String, Record> map = new HashMap<>();

        for (Iterator<Record> iterator = unMatchedRecords.iterator(); iterator.hasNext(); ) {
            Record record =  iterator.next();
            for (Iterator<BibResponse> recordIterator = oleNGBibImportResponse.getBibResponses().iterator(); recordIterator.hasNext(); ) {
                BibResponse bibResponse = recordIterator.next();
                if(bibResponse.getValueOf001().equalsIgnoreCase(getMarcRecordUtil().getControlFieldValue(record, "001"))){
                    map.put(bibResponse.getBibId(), record);
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
