package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrDocument;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.docstore.common.response.*;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.batch.process.model.ValueByPriority;
import org.kuali.ole.oleng.batch.profile.model.*;
import org.kuali.ole.oleng.batch.reports.BibImportReportLogHandler;
import org.kuali.ole.oleng.exception.ValidationException;
import org.kuali.ole.utility.OleDsNgRestClient;
import org.kuali.ole.utility.callnumber.CallNumberFactory;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.util.ObjectUtils;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.VariableField;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by SheikS on 12/9/2015.
 */
@Service("batchBibFileProcessor")
public class BatchBibFileProcessor extends BatchFileProcessor {
    private Map<String, String> operationIndMap;
    private Map<String, String> matchOptionIndMap;
    private Map<String, String> dataTypeIndMap;
    private static final Logger LOG = Logger.getLogger(BatchBibFileProcessor.class);

    @Override
    public OleNgBatchResponse processRecords(Map<Integer, RecordDetails> recordsMap, BatchProcessTxObject batchProcessTxObject,
                                             BatchProcessProfile batchProcessProfile) throws JSONException {
        BatchJobDetails batchJobDetails = batchProcessTxObject.getBatchJobDetails();
        String reportDirectoryName = batchProcessTxObject.getReportDirectoryName();
        JSONArray jsonArray = new JSONArray();
        String response = "";
        OleNGBibImportResponse oleNGBibImportResponse = null;
        List<Record> matchedRecords = new ArrayList<>();
        List<Record> unmatchedRecords = new ArrayList<>();
        List<Record> multipleMatchedRecords = new ArrayList<>();
        Exchange exchange = batchProcessTxObject.getExchangeObjectForBibImport();
        for (Iterator<Integer> iterator = recordsMap.keySet().iterator(); iterator.hasNext(); ) {
            Integer index = iterator.next();
            RecordDetails recordDetails = recordsMap.get(index);
            Record marcRecord = recordDetails.getRecord();
            if(null == marcRecord) {
                addBibFaiureResponseToExchange(new ValidationException(recordDetails.getMessage()), index, exchange);
                continue;
            }
            try {
                JSONObject jsonObject = null;

                if (!batchProcessProfile.getBatchProfileMatchPointList().isEmpty()) {
                    String query = getMatchPointProcessor().prepareSolrQueryMapForMatchPoint(marcRecord, batchProcessProfile.getBatchProfileMatchPointList());

                    if (StringUtils.isNotBlank(query)) {
                        query = query.replace("\\", "\\\\");
                        List results = getSolrRequestReponseHandler().getSolrDocumentList(query);
                        if (null == results || results.size() > 1) {
                            System.out.println("**** More than one record found for query : " + query);
                            multipleMatchedRecords.add(marcRecord);
                            continue;
                        }

                        if (null != results && results.size() == 1) {
                            SolrDocument solrDocument = (SolrDocument) results.get(0);
                            String bibId = (String) solrDocument.getFieldValue(DocstoreConstants.LOCALID_DISPLAY);
                            jsonObject = prepareRequest(index, bibId, marcRecord, batchProcessProfile);
                            matchedRecords.add(marcRecord);
                        } else {
                            jsonObject = prepareRequest(index, null, marcRecord, batchProcessProfile);
                            unmatchedRecords.add(marcRecord);
                        }
                    } else {
                        jsonObject = prepareRequest(index, null, marcRecord, batchProcessProfile);
                        unmatchedRecords.add(marcRecord);
                    }
                } else {
                    jsonObject = prepareRequest(index, null, marcRecord, batchProcessProfile);
                    unmatchedRecords.add(marcRecord);
                }
                jsonArray.put(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
                addBibFaiureResponseToExchange(e, index, exchange);
            }
        }

        if (jsonArray.length() > 0) {
            response = getOleDsNgRestClient().postData(OleDsNgRestClient.Service.PROCESS_BIB_HOLDING_ITEM, jsonArray, OleDsNgRestClient.Format.JSON);
        }

        try {
            if(StringUtils.isNotBlank(response)) {
                oleNGBibImportResponse = getObjectMapper().readValue(response, OleNGBibImportResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            addBibFaiureResponseToExchange(e, null, exchange);
        }

        if(null  == oleNGBibImportResponse) {
            oleNGBibImportResponse = new OleNGBibImportResponse();
        }

        List<BibFailureResponse> bibFailureResponses = (List<BibFailureResponse>) exchange.get(OleNGConstants.FAILURE_RESPONSE);
        if(CollectionUtils.isNotEmpty(bibFailureResponses)) {
            oleNGBibImportResponse.getFailureResponses().addAll(bibFailureResponses);
        }
        oleNGBibImportResponse.setMatchedBibsCount(matchedRecords.size());
        oleNGBibImportResponse.setUnmatchedBibsCount(unmatchedRecords.size());
        oleNGBibImportResponse.setMultipleMatchedBibsCount(multipleMatchedRecords.size());
        oleNGBibImportResponse.setBibImportProfileName(batchProcessProfile.getBatchProcessProfileName());
        oleNGBibImportResponse.setJobDetailId(String.valueOf(batchJobDetails.getJobDetailId()));
        oleNGBibImportResponse.setJobName(batchJobDetails.getJobName());
        oleNGBibImportResponse.setBibImportProfileName(batchProcessProfile.getBatchProcessProfileName());
        oleNGBibImportResponse.setMatchedRecords(matchedRecords);
        oleNGBibImportResponse.setUnmatchedRecords(unmatchedRecords);
        oleNGBibImportResponse.setMultipleMatchedRecords(multipleMatchedRecords);
        oleNGBibImportResponse.setMultipleMatchedRecords(multipleMatchedRecords);
        oleNGBibImportResponse.getRecordsMap().putAll(recordsMap);
        oleNGBibImportResponse = mergeResponse(oleNGBibImportResponse, exchange);
        generateBatchReport(oleNGBibImportResponse,reportDirectoryName, batchProcessProfile.getBatchProcessProfileName());
        clearMarcRecordObjects(oleNGBibImportResponse);
        exchange.add(OleNGConstants.BIB_RESPONSE, oleNGBibImportResponse);
        exchange.remove(OleNGConstants.FAILURE_RESPONSE);

        OleNgBatchResponse oleNgBatchResponse = new OleNgBatchResponse();
        oleNgBatchResponse.setResponse(response);

        oleNgBatchResponse.setNoOfFailureRecord(getFailureRecordsCount(bibFailureResponses));

        return oleNgBatchResponse;
    }

    public void generateBatchReport(OleNGBibImportResponse oleNGBibImportResponse, String reportDirectoryName, String profileName) {
        oleNGBibImportResponse.setDirectoryName(reportDirectoryName);
        BibImportReportLogHandler bibImportReportLogHandler = BibImportReportLogHandler.getInstance();
        bibImportReportLogHandler.logMessage(oleNGBibImportResponse,reportDirectoryName);
    }

    private String getOperationInd(String operation) {
        return getOperationIndMap().get(operation);
    }

    private String getAddOperationInd(String operation) {
        return getAddOperationIndMap().get(operation);
    }

    private String getMatchOptionInd(String matchOption) {
        return getMatchOptionIndMap().get(matchOption);
    }

    private String getDataTypeInd(String dataType) {
        return getDataTypeIndMap().get(dataType);
    }

    /**
     * @param bibId
     * @param marcRecord
     * @param batchProcessProfile
     * @return The method accepts a bibId, MarcRecord for the bib and the profile. It sets up the
     * main bibData json object that contains the respective holdings, items json objects.
     * @throws JSONException
     */
    private JSONObject prepareRequest(Integer index, String bibId, Record marcRecord, BatchProcessProfile batchProcessProfile) throws JSONException {
        LOG.info("Preparing JSON Request for Bib/Holdings/Items");

        JSONObject bibData = new JSONObject();
        String unmodifiedRecord = getMarcXMLConverter().generateMARCXMLContent(marcRecord);
        String updatedUserName = getUpdatedUserName();
        String updatedDate = DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date());

        if (null != bibId) {
            bibData.put("id", bibId);
        }

        bibData.put(OleNGConstants.RECORD_INDEX , index);
        bibData.put(OleNGConstants.UPDATED_BY, updatedUserName);
        bibData.put(OleNGConstants.UPDATED_DATE, updatedDate);
        bibData.put(OleNGConstants.UNMODIFIED_CONTENT, unmodifiedRecord);
        bibData.put(OleNGConstants.OPS, getOverlayOps(batchProcessProfile));
        bibData.put(OleNGConstants.ADDED_OPS, getAddedOps(batchProcessProfile));
        bibData.put(OleNGConstants.ADDITIONAL_OVERLAY_OPS, getAdditionalOverlayOps(batchProcessProfile));
        bibData.put(OleNGConstants.ACTION_OPS, getActionOps(batchProcessProfile));
        bibData.put(OleNGConstants.FIELD_OPS, getFieldOps(batchProcessProfile));

        // Prepare data mapping before MARC Transformation
        Map<String, List<JSONObject>> dataMappingsMapPreTransformation = prepareDataMapping(marcRecord, batchProcessProfile, OleNGConstants.PRE_MARC_TRANSFORMATION);

        JSONObject holdingsData = getMatchPointProcessor().prepareMatchPointsForHoldings(marcRecord, batchProcessProfile);
        JSONObject eholdingsData = getMatchPointProcessor().prepareMatchPointsForEHoldings(marcRecord, batchProcessProfile);
        JSONObject itemData = getMatchPointProcessor().prepareMatchPointsForItem(marcRecord, batchProcessProfile);

        //Transformations pertaining to MARC record (001,003,035$a etc..)
        handleBatchProfileTransformations(marcRecord, batchProcessProfile);
        String modifiedRecord = getMarcXMLConverter().generateMARCXMLContent(marcRecord);
        bibData.put(OleNGConstants.MODIFIED_CONTENT, modifiedRecord);

        // Prepare data mapping after MARC Transformation
        Map<String, List<JSONObject>> dataMappingsMapPostTransformations = prepareDataMapping(marcRecord, batchProcessProfile, OleNGConstants.POST_MARC_TRANSFORMATION);


        List<JSONObject> bibDataMappingsPreTrans = dataMappingsMapPreTransformation.get(OleNGConstants.BIB_DATAMAPPINGS);
        List<JSONObject> bibDataMappingsPostTrans = dataMappingsMapPostTransformations.get(OleNGConstants.BIB_DATAMAPPINGS);
        bibData.put(OleNGConstants.DATAMAPPING, buildOneObjectForList(bibDataMappingsPreTrans, bibDataMappingsPostTrans));

        List<JSONObject> holdingsDataMappingsPreTrans = dataMappingsMapPreTransformation.get(OleNGConstants.HOLDINGS_DATAMAPPINGS);
        List<JSONObject> holdingsDataMappingsPostTrans = dataMappingsMapPostTransformations.get(OleNGConstants.HOLDINGS_DATAMAPPINGS);
        holdingsData.put(OleNGConstants.DATAMAPPING, buildOneObjectForList(holdingsDataMappingsPreTrans, holdingsDataMappingsPostTrans));
        bibData.put(OleNGConstants.HOLDINGS, holdingsData);

        List<JSONObject> eholdingsDataMappingsPreTrans = dataMappingsMapPreTransformation.get(OleNGConstants.EHOLDINGS_DATAMAPPINGS);
        List<JSONObject> eholdingsDataMappingsPostTrans = dataMappingsMapPostTransformations.get(OleNGConstants.EHOLDINGS_DATAMAPPINGS);
        eholdingsData.put(OleNGConstants.DATAMAPPING, buildOneObjectForList(eholdingsDataMappingsPreTrans, eholdingsDataMappingsPostTrans));
        bibData.put(OleNGConstants.EHOLDINGS, eholdingsData);

        List<JSONObject> itemsDataMappingsPreTrans = dataMappingsMapPreTransformation.get(OleNGConstants.ITEM_DATAMAPPINGS);
        List<JSONObject> itemsDataMappingsPostTrans = dataMappingsMapPostTransformations.get(OleNGConstants.ITEM_DATAMAPPINGS);
        itemData.put(OleNGConstants.DATAMAPPING, buildOneObjectForList(itemsDataMappingsPreTrans, itemsDataMappingsPostTrans));
        bibData.put(OleNGConstants.ITEM, itemData);

        return bibData;
    }

    private JSONArray getFieldOps(BatchProcessProfile batchProcessProfile) {
        JSONArray fieldOps = new JSONArray();
        List<BatchProfileFieldOperation> batchProfileFieldOperationList = batchProcessProfile.getBatchProfileFieldOperationList();
        if (CollectionUtils.isNotEmpty(batchProfileFieldOperationList)) {
            for (Iterator<BatchProfileFieldOperation> iterator = batchProfileFieldOperationList.iterator(); iterator.hasNext(); ) {
                BatchProfileFieldOperation batchProfileFieldOperation = iterator.next();
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(OleNGConstants.DATA_FIELD, batchProfileFieldOperation.getDataField());
                    jsonObject.put(OleNGConstants.IND1, batchProfileFieldOperation.getInd1());
                    jsonObject.put(OleNGConstants.IND2, batchProfileFieldOperation.getInd2());
                    jsonObject.put(OleNGConstants.SUBFIELD, batchProfileFieldOperation.getSubField());
                    jsonObject.put(OleNGConstants.VALUE, batchProfileFieldOperation.getValue());
                    jsonObject.put(OleNGConstants.IGNORE_GPF, batchProfileFieldOperation.getIgnoreGPF());
                    fieldOps.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return fieldOps;
    }

    private JSONObject buildOneObject(JSONObject bibDataMappingsPreTrans, JSONObject bibDataMappingsPostTrans) {
        JSONObject finalObject = new JSONObject();

        for (Iterator iterator = bibDataMappingsPreTrans.keys(); iterator.hasNext(); ) {
            String key = (String) iterator.next();
            try {
                finalObject.put(key, bibDataMappingsPreTrans.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (Iterator iterator = bibDataMappingsPostTrans.keys(); iterator.hasNext(); ) {
            String key = (String) iterator.next();
            try {
                finalObject.put(key, bibDataMappingsPostTrans.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return finalObject;
    }

    public List<JSONObject> buildOneObjectForList(List<JSONObject> dataMappingsPreTrans, List<JSONObject> dataMappingsPostTrans) {
        List<JSONObject> finalObjects = new ArrayList<>();
        Map<String,String> shelvingOrderCode = new HashMap<>();
        for (int index = 0; index < dataMappingsPreTrans.size(); index++) {
            JSONObject preTransformObject =  dataMappingsPreTrans.size() > index ? dataMappingsPreTrans.get(index) : new JSONObject();
            JSONObject postTransformObject = dataMappingsPostTrans.size() > index ? dataMappingsPostTrans.get(index) : new JSONObject();
            finalObjects.add(buildOneObject(preTransformObject, postTransformObject));
        }
        Iterator<?> keys = dataMappingsPreTrans.get(0).keys();
        while(keys.hasNext()){
            String key = (String)keys.next();
            if(key.equalsIgnoreCase("Call Number") || key.equalsIgnoreCase("Call Number Type"))
                try {
                    if ( dataMappingsPreTrans.get(0).get(key) instanceof JSONArray ) {
                        shelvingOrderCode.put(key, dataMappingsPreTrans.get(0).get(key).toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }if(!shelvingOrderCode.isEmpty()){
            List<String> preTransformObject =  buildSortableCallNumber(shelvingOrderCode);
            if(preTransformObject.size()!=0){
                try {
                    finalObjects.get(0).put("Shelving Order", preTransformObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return finalObjects;
    }

    private Map<String, List<JSONObject>> prepareDataMapping(Record marcRecord, BatchProcessProfile batchProcessProfile, String transformationOption) throws JSONException {
        Map<String, List<JSONObject>> dataMappings = new HashMap<>();

        List<JSONObject> bibDataMappings = prepareDataMappings(Collections.singletonList(marcRecord), batchProcessProfile, OleNGConstants.BIBLIOGRAPHIC, transformationOption, false);
        dataMappings.put(OleNGConstants.BIB_DATAMAPPINGS, bibDataMappings);

        List<Record> recordListForHoldings = getRecordList(marcRecord, batchProcessProfile, OleNGConstants.HOLDINGS);
        List<JSONObject> holdingsDataMappings = prepareDataMappings(recordListForHoldings, batchProcessProfile, OleNGConstants.HOLDINGS, transformationOption, true);
        dataMappings.put(OleNGConstants.HOLDINGS_DATAMAPPINGS, holdingsDataMappings);

        List<Record> recordListForItem = getRecordList(marcRecord, batchProcessProfile, OleNGConstants.ITEM);
        List<JSONObject> itemDataMappings = prepareDataMappings(recordListForItem, batchProcessProfile, OleNGConstants.ITEM, transformationOption, true);
        dataMappings.put(OleNGConstants.ITEM_DATAMAPPINGS, itemDataMappings);

        List<Record> recordListForEHoldings = getRecordList(marcRecord, batchProcessProfile, OleNGConstants.EHOLDINGS);
        List<JSONObject> eholdingsDataMappings = prepareDataMappings(recordListForEHoldings, batchProcessProfile, OleNGConstants.EHOLDINGS, transformationOption, true);
        dataMappings.put(OleNGConstants.EHOLDINGS_DATAMAPPINGS, eholdingsDataMappings);

        return dataMappings;
    }

    private List<Record> getRecordList(Record marcRecord, BatchProcessProfile batchProcessProfile, String docType) {
        MarcDataField multiTagField = getMultiTagField(batchProcessProfile, docType);
        List<Record> records = new ArrayList<>();
        if (null != multiTagField) {
            records = splitRecordByMultiValue(marcRecord, multiTagField);
        }
        return CollectionUtils.isNotEmpty(records) ? records : Collections.singletonList(marcRecord);
    }

    private MarcDataField getMultiTagField(BatchProcessProfile batchProcessProfile, String docType) {
        List<BatchProfileAddOrOverlay> batchProfileAddOrOverlayList = batchProcessProfile.getBatchProfileAddOrOverlayList();
        if (CollectionUtils.isNotEmpty(batchProfileAddOrOverlayList)) {
            for (Iterator<BatchProfileAddOrOverlay> iterator = batchProfileAddOrOverlayList.iterator(); iterator.hasNext(); ) {
                BatchProfileAddOrOverlay batchProfileAddOrOverlay = iterator.next();
                if (batchProfileAddOrOverlay.getDataType().equalsIgnoreCase(docType)) {
                    String addOperation = batchProfileAddOrOverlay.getAddOperation();
                    if (StringUtils.isNotBlank(addOperation) && (addOperation.equalsIgnoreCase(OleNGConstants.CREATE_MULTIPLE)
                            || addOperation.equalsIgnoreCase(OleNGConstants.OVERLAY_MULTIPLE) || addOperation.equalsIgnoreCase(OleNGConstants.CREATE_MULTIPLE_DELETE_ALL_EXISTING)
                            || addOperation.equalsIgnoreCase(OleNGConstants.CREATE_MULTIPLE_KEEP_ALL_EXISTING))) {
                        String dataField = batchProfileAddOrOverlay.getDataField();
                        String ind1 = batchProfileAddOrOverlay.getInd1();
                        String ind2 = batchProfileAddOrOverlay.getInd2();
                        String subField = batchProfileAddOrOverlay.getSubField();
                        MarcDataField marcDataField = new MarcDataField();
                        marcDataField.setDataField(dataField);
                        marcDataField.setInd1(ind1);
                        marcDataField.setInd2(ind2);
                        marcDataField.setSubField(subField);
                        return marcDataField;
                    }
                }
            }
        }
        return null;
    }

    public List getOverlayOps(BatchProcessProfile batchProcessProfile) {
        List addOverlayOps = new ArrayList();

        List<BatchProfileAddOrOverlay> batchProfileAddOrOverlayList = batchProcessProfile.getBatchProfileAddOrOverlayList();
        for (Iterator<BatchProfileAddOrOverlay> iterator = batchProfileAddOrOverlayList.iterator(); iterator.hasNext(); ) {
            BatchProfileAddOrOverlay batchProfileAddOrOverlay = iterator.next();
            String dataType = batchProfileAddOrOverlay.getDataType();
            String matchOption = batchProfileAddOrOverlay.getMatchOption();
            String operation = batchProfileAddOrOverlay.getOperation();

            String matchOptionInd = getMatchOptionInd(matchOption);
            String dataTypeInd = getDataTypeInd(dataType);
            String operationInd = getOperationInd(operation);
            addOverlayOps.add(matchOptionInd + dataTypeInd + operationInd);
        }
        return addOverlayOps;
    }

    public List getActionOps(BatchProcessProfile batchProcessProfile) {
        List actionOps = new ArrayList();

        List<BatchProfileAddOrOverlay> batchProfileAddOrOverlayList = batchProcessProfile.getBatchProfileAddOrOverlayList();
        for (Iterator<BatchProfileAddOrOverlay> iterator = batchProfileAddOrOverlayList.iterator(); iterator.hasNext(); ) {
            BatchProfileAddOrOverlay batchProfileAddOrOverlay = iterator.next();
            String addOperation = batchProfileAddOrOverlay.getAddOperation();

            if (StringUtils.isNotBlank(addOperation)) {
                String operationInd = getAddOperationInd(addOperation);
                try {
                    JSONObject action = new JSONObject();
                    action.put(OleNGConstants.ACTION, operationInd);
                    action.put(OleNGConstants.DOC_TYPE, batchProfileAddOrOverlay.getDataType());
                    action.put(OleNGConstants.DATA_FIELD, batchProfileAddOrOverlay.getDataField());
                    action.put(OleNGConstants.IND1, batchProfileAddOrOverlay.getInd1());
                    action.put(OleNGConstants.IND2, batchProfileAddOrOverlay.getInd2());
                    action.put(OleNGConstants.SUBFIELD, batchProfileAddOrOverlay.getSubField());
                    action.put(OleNGConstants.LINKFIELD, batchProfileAddOrOverlay.getLinkField());
                    actionOps.add(action);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return actionOps;
    }

    public JSONObject getAddedOps(BatchProcessProfile batchProcessProfile) {
        JSONObject addedOps = new JSONObject();

        List<BatchProfileAddOrOverlay> batchProfileAddOrOverlayList = batchProcessProfile.getBatchProfileAddOrOverlayList();

        try {
            String holdingsAddedOps = getAddedOps(batchProfileAddOrOverlayList, OleNGConstants.HOLDINGS);
            addedOps.put(OleNGConstants.HOLDINGS, holdingsAddedOps);

            String itemAddedOps = getAddedOps(batchProfileAddOrOverlayList, OleNGConstants.ITEM);
            addedOps.put(OleNGConstants.ITEM, itemAddedOps);

            String eholdingsAddedOps = getAddedOps(batchProfileAddOrOverlayList, OleNGConstants.EHOLDINGS);
            addedOps.put(OleNGConstants.EHOLDINGS, eholdingsAddedOps);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return addedOps;
    }

    public JSONObject getAdditionalOverlayOps(BatchProcessProfile batchProcessProfile) {
        JSONObject additionalOverlayOps = new JSONObject();
        List<BatchProfileAddOrOverlay> batchProfileAddOrOverlayList = batchProcessProfile.getBatchProfileAddOrOverlayList();
        try {
            JSONObject bibAdditionalOverayOps = getAdditionalOverlayOps(batchProfileAddOrOverlayList, OleNGConstants.BIBLIOGRAPHIC);
            additionalOverlayOps.put(OleNGConstants.BIB, bibAdditionalOverayOps);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return additionalOverlayOps;
    }

    public String getAddedOps(List<BatchProfileAddOrOverlay> batchProfileAddOrOverlayList, String docType) throws JSONException {
        String addedOpsValue = null;

        for (Iterator<BatchProfileAddOrOverlay> iterator = batchProfileAddOrOverlayList.iterator(); iterator.hasNext(); ) {
            BatchProfileAddOrOverlay batchProfileAddOrOverlay = iterator.next();
            String matchOption = batchProfileAddOrOverlay.getMatchOption();
            if (matchOption.equalsIgnoreCase(OleNGConstants.IF_MATCH_FOUND) && batchProfileAddOrOverlay.getDataType().equalsIgnoreCase(docType)) {
                String operation = batchProfileAddOrOverlay.getOperation();
                if (operation.equalsIgnoreCase(OleNGConstants.ADD)) {
                    addedOpsValue = OleNGConstants.DELETE_ALL_EXISTING_AND_ADD;
                    String addOperation = batchProfileAddOrOverlay.getAddOperation();
                    if (StringUtils.isNotBlank(addOperation)) {
                        switch (addOperation) {
                            case OleNGConstants.DELETE_ALL_EXISTING_AND_ADD: {
                                addedOpsValue = OleNGConstants.DELETE_ALL_EXISTING_AND_ADD;
                                break;
                            }

                            case OleNGConstants.KEEP_ALL_EXISTING_AND_ADD: {
                                addedOpsValue = OleNGConstants.KEEP_ALL_EXISTING_AND_ADD;
                                break;
                            }

                            case OleNGConstants.CREATE_MULTIPLE_DELETE_ALL_EXISTING: {
                                addedOpsValue = OleNGConstants.DELETE_ALL_EXISTING_AND_ADD;
                                break;
                            }

                            case OleNGConstants.CREATE_MULTIPLE_KEEP_ALL_EXISTING: {
                                addedOpsValue = OleNGConstants.KEEP_ALL_EXISTING_AND_ADD;
                                break;
                            }

                            case OleNGConstants.CREATE_MULTIPLE: {
                                addedOpsValue = OleNGConstants.KEEP_ALL_EXISTING_AND_ADD;
                                break;
                            }
                        }
                    }
                } else if (operation.equalsIgnoreCase(OleNGConstants.OVERLAY)) {
                    addedOpsValue = OleNGConstants.OVERLAY;
                } else if (operation.equalsIgnoreCase(OleNGConstants.DELETE_ALL)) {
                    addedOpsValue = OleNGConstants.DELETE_ALL;
                } else {
                    addedOpsValue = OleNGConstants.DISCARD;
                }
            }
        }
        return addedOpsValue;
    }

    public JSONObject getAdditionalOverlayOps(List<BatchProfileAddOrOverlay> batchProfileAddOrOverlayList, String docType) throws JSONException {
        for (Iterator<BatchProfileAddOrOverlay> iterator = batchProfileAddOrOverlayList.iterator(); iterator.hasNext(); ) {
            BatchProfileAddOrOverlay batchProfileAddOrOverlay = iterator.next();
            String matchOption = batchProfileAddOrOverlay.getMatchOption();
            if (matchOption.equalsIgnoreCase(OleNGConstants.IF_MATCH_FOUND) && batchProfileAddOrOverlay.getDataType().equalsIgnoreCase(docType)) {
                String operation = batchProfileAddOrOverlay.getOperation();
                String addOrOverlayField = batchProfileAddOrOverlay.getAddOrOverlayField();
                String addOrOverlayFieldOperation = batchProfileAddOrOverlay.getAddOrOverlayFieldOperation();
                List<String> addOrOverlayFieldValue = batchProfileAddOrOverlay.getAddOrOverlayFieldValue();
                if (operation.equalsIgnoreCase(OleNGConstants.OVERLAY) && StringUtils.isNotBlank(addOrOverlayField) && StringUtils.isNotBlank(addOrOverlayFieldOperation)
                        && CollectionUtils.isNotEmpty(addOrOverlayFieldValue)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(OleNGConstants.WHERE, addOrOverlayField);
                    jsonObject.put(OleNGConstants.CONDITION, addOrOverlayFieldOperation);
                    jsonObject.put(OleNGConstants.VALUE, addOrOverlayFieldValue);
                    return jsonObject;
                }
            }
        }
        return null;
    }

    /**
     * @param marcRecords
     * @param batchProcessProfile
     * @param docType
     * @param transformationOption
     * @return The method returns a list of key/value pairs where key is the destination field and value if the value determined from the profile's data maping section.
     * It handles the following usecases:
     * 1. Dest field mapping from multiple tags (For example: CallNumber determined from 050$b and 050$b - Here it will take the values and concatenate with a space in between)
     * 2. Dest field mapping for multi-valued fields (For example: Public Note determined from 856$z and 856$3 - Here it will form a list of values)
     * 3. Handling priority when determining values for the destination field.
     * @throws JSONException
     */
    public List<JSONObject> prepareDataMappings(List<Record> marcRecords, BatchProcessProfile batchProcessProfile,
                                                String docType, String transformationOption, boolean addMatchPoint) throws JSONException {
        List<JSONObject> dataMappings = new ArrayList<>();
        List<BatchProfileDataMapping> filteredDataMappings = filterDataMappingsByTransformationOption(batchProcessProfile.getBatchProfileDataMappingList(), transformationOption);

        sortDataMappings(filteredDataMappings);
        for (Iterator<Record> recordIterator = marcRecords.iterator(); recordIterator.hasNext(); ) {
            Map<String, List<ValueByPriority>> valueByPriorityMap = new HashedMap();
            Record marcRecord = recordIterator.next();
            for (Iterator<BatchProfileDataMapping> iterator = filteredDataMappings.iterator(); iterator.hasNext(); ) {
                BatchProfileDataMapping batchProfileDataMapping = iterator.next();
                String destination = batchProfileDataMapping.getDestination();
                if (destination.equalsIgnoreCase(docType)) {
                    String destinationField = batchProfileDataMapping.getField();


                    boolean multiValue = batchProfileDataMapping.isMultiValue();
                    List<String> fieldValues = getFieldValues(marcRecord, batchProfileDataMapping, multiValue);

                    if (CollectionUtils.isNotEmpty(fieldValues)) {
                        int priority = batchProfileDataMapping.getPriority();

                        buildingValuesForDestinationBasedOnPriority(valueByPriorityMap, destinationField, multiValue, fieldValues, priority);
                    }
                }
            }
            JSONObject dataMapping = buildDataMappingsJSONObject(valueByPriorityMap);
            if (addMatchPoint) {
                addMatchPointToDataMapping(marcRecord, dataMapping, batchProcessProfile, docType);
            }
            dataMappings.add(dataMapping);
        }
        return dataMappings;
    }

    private void addMatchPointToDataMapping(Record marcRecord, JSONObject dataMapping, BatchProcessProfile batchProcessProfile, String docType) {
        try {
            JSONObject matchpointForDataMapping = new JSONObject();
            JSONObject matchPoints = getMatchPointProcessor().prepareMatchPointsForDocType(marcRecord, batchProcessProfile.getBatchProfileMatchPointList(), docType);
            for (Iterator iterator = matchPoints.keys(); iterator.hasNext(); ) {
                String key = (String) iterator.next();
                String matchPointsString = matchPoints.getString(key);
                StringTokenizer matchPointValueTockenize = new StringTokenizer(matchPointsString, ",");
                JSONArray valueArray = new JSONArray();
                while (matchPointValueTockenize.hasMoreTokens()) {
                    String value = matchPointValueTockenize.nextToken();
                    valueArray.put(value);
                }
                matchpointForDataMapping.put(key, valueArray);
            }
            if(matchpointForDataMapping.length() > 0) {
                dataMapping.put(OleNGConstants.MATCHPOINT_FOR_DATAMAPPING, matchpointForDataMapping);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject buildDataMappingsJSONObject(Map<String, List<ValueByPriority>> valueByPriorityMap) throws JSONException {
        JSONObject dataMappings = new JSONObject();

        for (Iterator<String> iterator = valueByPriorityMap.keySet().iterator(); iterator.hasNext(); ) {
            String destField = iterator.next();
            List<ValueByPriority> vals = valueByPriorityMap.get(destField);
            for (Iterator<ValueByPriority> iterator1 = vals.iterator(); iterator1.hasNext(); ) {
                ValueByPriority valueByPriority = iterator1.next();
                List<String> values = valueByPriority.getValues();
                if (CollectionUtils.isNotEmpty(values)) {
                    dataMappings.put(destField, values);
                    break;
                }
            }
        }

        return dataMappings;
    }

    private List<BatchProfileDataMapping> filterDataMappingsByTransformationOption(List<BatchProfileDataMapping> batchProfileDataMappingList, String transformationOption) {
        List<BatchProfileDataMapping> batchProfileDataMappings = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(batchProfileDataMappingList)) {
            for (Iterator<BatchProfileDataMapping> iterator = batchProfileDataMappingList.iterator(); iterator.hasNext(); ) {
                BatchProfileDataMapping batchProfileDataMapping = iterator.next();
                if (batchProfileDataMapping.getTransferOption().equalsIgnoreCase(transformationOption)) {
                    batchProfileDataMappings.add(batchProfileDataMapping);
                }
            }
        }
        return batchProfileDataMappings;
    }
    public Map<String, String> getDataMappingMap(List<BatchProfileDataMapping> batchProfileDataMappingList) {
        Map<String, String> dataMappingMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(batchProfileDataMappingList)) {
            for (Iterator<BatchProfileDataMapping> iterator = batchProfileDataMappingList.iterator(); iterator.hasNext(); ) {
                BatchProfileDataMapping batchProfileDataMapping = iterator.next();
                String mapKey = batchProfileDataMapping.getDestination() + "-"
                        + batchProfileDataMapping.getField();
                String value = batchProfileDataMapping.getDataField() + " $" + batchProfileDataMapping.getSubField();
                if (dataMappingMap.containsKey(mapKey)) {
                    value = dataMappingMap.get(mapKey);
                    value = value + "$" + batchProfileDataMapping.getSubField();
                }
                dataMappingMap.put(mapKey, value);
            }
        }
        return dataMappingMap;
    }


    private void handleBatchProfileTransformations(Record record, BatchProcessProfile batchProcessProfile) {
        new StepsProcessor().processSteps(record, batchProcessProfile);
    }

    public Map<String, String> getOperationIndMap() {
        if (null == operationIndMap) {
            operationIndMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
            operationIndMap.put(OleNGConstants.ADD, OleNGConstants.ONE);
            operationIndMap.put(OleNGConstants.OVERLAY, OleNGConstants.TWO);
            operationIndMap.put(OleNGConstants.DISCARD, OleNGConstants.THREE);
        }
        return operationIndMap;
    }

    public Map<String, String> getAddOperationIndMap() {
        if (null == operationIndMap) {
            operationIndMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
            operationIndMap.put(OleNGConstants.CREATE_MULTIPLE, OleNGConstants.ONE);
            operationIndMap.put(OleNGConstants.CREATE_MULTIPLE_DELETE_ALL_EXISTING, OleNGConstants.TWO);
            operationIndMap.put(OleNGConstants.CREATE_MULTIPLE_KEEP_ALL_EXISTING, OleNGConstants.THREE);
            operationIndMap.put(OleNGConstants.DELETE_ALL_EXISTING_AND_ADD, OleNGConstants.FOUR);
            operationIndMap.put(OleNGConstants.KEEP_ALL_EXISTING_AND_ADD, OleNGConstants.FIVE);

        }
        return operationIndMap;
    }

    public void setOperationIndMap(Map<String, String> operationIndMap) {
        this.operationIndMap = operationIndMap;
    }

    public Map<String, String> getMatchOptionIndMap() {
        if (null == matchOptionIndMap) {
            matchOptionIndMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
            matchOptionIndMap.put(OleNGConstants.IF_MATCH_FOUND, OleNGConstants.ONE);
            matchOptionIndMap.put(OleNGConstants.IF_NOT_MATCH_FOUND, OleNGConstants.TWO);
        }
        return matchOptionIndMap;
    }

    public void setMatchOptionIndMap(Map<String, String> matchOptionIndMap) {
        this.matchOptionIndMap = matchOptionIndMap;
    }

    public Map<String, String> getDataTypeIndMap() {
        if (null == dataTypeIndMap) {
            dataTypeIndMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
            dataTypeIndMap.put(OleNGConstants.BIBLIOGRAPHIC, OleNGConstants.ONE);
            dataTypeIndMap.put(OleNGConstants.HOLDINGS, OleNGConstants.TWO);
            dataTypeIndMap.put(OleNGConstants.ITEM, OleNGConstants.THREE);
            dataTypeIndMap.put(OleNGConstants.EHOLDINGS, OleNGConstants.FOUR);
        }
        return dataTypeIndMap;
    }

    public void setDataTypeIndMap(Map<String, String> dataTypeIndMap) {
        this.dataTypeIndMap = dataTypeIndMap;
    }

    @Override
    public String getReportingFilePath() {
        return ConfigContext.getCurrentContextConfig().getProperty("batch.bibImport.directory");
    }


    public List<Record> splitRecordByMultiValue(Record record, MarcDataField marcDataField) {
        String dataFieldString = marcDataField.getDataField();
        String ind1 = marcDataField.getInd1();
        String ind2 = marcDataField.getInd2();
        String subField = marcDataField.getSubField();


        List<Record> records = new ArrayList<>();
        List<VariableField> dataFields = record.getVariableFields(dataFieldString);

        List<VariableField> filteredDataFields = getMarcRecordUtil().getMatchedDataFields(ind1, ind2, subField, null, dataFields);


        for (Iterator<VariableField> variableFieldIterator = filteredDataFields.iterator(); variableFieldIterator.hasNext(); ) {
            DataField dataField = (DataField) variableFieldIterator.next();
            Record clonedRecord = (Record) ObjectUtils.deepCopy(record);
            getMarcRecordUtil().removeFieldFromRecord(clonedRecord, dataFieldString);
            getMarcRecordUtil().addVariableFieldToRecord(clonedRecord, dataField);
            records.add(clonedRecord);
        }

        return CollectionUtils.isNotEmpty(records) ? records : Collections.singletonList(record);

    }

    private BibFailureResponse getBibFailureResponse(Integer index, Exception e) {
        BibFailureResponse failureResponse = new BibFailureResponse();
        failureResponse.setFailureMessage(e.toString());
        failureResponse.setIndex(index);
        return failureResponse;
    }


    public OleNGBibImportResponse processBibImportForOrderOrInvoiceImport(Map<Integer, RecordDetails> recordsMap,
                                                                          BatchProcessTxObject batchProcessTxObject, BatchProcessProfile bibImportProfile, Exchange exchange) {
        OleNGBibImportResponse oleNGBibImportResponse = new OleNGBibImportResponse();
        try {
            OleNgBatchResponse oleNgBatchResponse = processRecords(recordsMap, batchProcessTxObject, bibImportProfile);
            String response = oleNgBatchResponse.getResponse();
            if(StringUtils.isNotBlank(response)) {
                oleNGBibImportResponse = getObjectMapper().readValue(response, OleNGBibImportResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            addOrderFaiureResponseToExchange(e, null, exchange);
        }
        return oleNGBibImportResponse;
    }

    private OleNGBibImportResponse mergeResponse(OleNGBibImportResponse newResponseObject, org.kuali.ole.Exchange exchange) {
        OleNGBibImportResponse oldResponseObject = (OleNGBibImportResponse) exchange.get(OleNGConstants.BIB_RESPONSE);
        if(null == oldResponseObject) {
            return newResponseObject;
        }
        return mergeBibImportResponse(oldResponseObject, newResponseObject);
    }

    private OleNGBibImportResponse mergeBibImportResponse(OleNGBibImportResponse oldOleNGBibImportResponse, OleNGBibImportResponse newOleNGBibImportResponse) {
        oldOleNGBibImportResponse.getBibResponses().addAll(newOleNGBibImportResponse.getBibResponses());
        oldOleNGBibImportResponse.getFailureResponses().addAll(newOleNGBibImportResponse.getFailureResponses());

        oldOleNGBibImportResponse.setMatchedRecords(newOleNGBibImportResponse.getMatchedRecords());
        oldOleNGBibImportResponse.setUnmatchedRecords(newOleNGBibImportResponse.getUnmatchedRecords());
        oldOleNGBibImportResponse.setMultipleMatchedRecords(newOleNGBibImportResponse.getMultipleMatchedRecords());
        oldOleNGBibImportResponse.setRecordsMap(newOleNGBibImportResponse.getRecordsMap());

        oldOleNGBibImportResponse.setMatchedBibsCount(oldOleNGBibImportResponse.getMatchedBibsCount() + newOleNGBibImportResponse.getMatchedBibsCount());
        oldOleNGBibImportResponse.setUnmatchedBibsCount(oldOleNGBibImportResponse.getUnmatchedBibsCount() + newOleNGBibImportResponse.getUnmatchedBibsCount());
        oldOleNGBibImportResponse.setMultipleMatchedBibsCount(oldOleNGBibImportResponse.getMultipleMatchedBibsCount() + newOleNGBibImportResponse.getMultipleMatchedBibsCount());

        oldOleNGBibImportResponse.setMatchedHoldingsCount(oldOleNGBibImportResponse.getMatchedHoldingsCount() + newOleNGBibImportResponse.getMatchedHoldingsCount());
        oldOleNGBibImportResponse.setUnmatchedHoldingsCount(oldOleNGBibImportResponse.getUnmatchedHoldingsCount() + newOleNGBibImportResponse.getUnmatchedHoldingsCount());
        oldOleNGBibImportResponse.setMultipleMatchedHoldingsCount(oldOleNGBibImportResponse.getMultipleMatchedHoldingsCount() + newOleNGBibImportResponse.getMultipleMatchedHoldingsCount());

        oldOleNGBibImportResponse.setMatchedItemsCount(oldOleNGBibImportResponse.getMatchedItemsCount() + newOleNGBibImportResponse.getMatchedItemsCount());
        oldOleNGBibImportResponse.setUnmatchedItemsCount(oldOleNGBibImportResponse.getUnmatchedItemsCount() + newOleNGBibImportResponse.getUnmatchedItemsCount());
        oldOleNGBibImportResponse.setMultipleMatchedItemsCount(oldOleNGBibImportResponse.getMultipleMatchedItemsCount() + newOleNGBibImportResponse.getMultipleMatchedItemsCount());

        oldOleNGBibImportResponse.setMatchedEHoldingsCount(oldOleNGBibImportResponse.getMatchedEHoldingsCount() + newOleNGBibImportResponse.getMatchedEHoldingsCount());
        oldOleNGBibImportResponse.setUnmatchedEHoldingsCount(oldOleNGBibImportResponse.getUnmatchedEHoldingsCount() + newOleNGBibImportResponse.getUnmatchedEHoldingsCount());
        oldOleNGBibImportResponse.setMultipleMatchedEHoldingsCount(oldOleNGBibImportResponse.getMultipleMatchedEHoldingsCount() + newOleNGBibImportResponse.getMultipleMatchedEHoldingsCount());

        return  oldOleNGBibImportResponse;
    }

    private void clearMarcRecordObjects(OleNGBibImportResponse oleNGBibImportResponse) {
        oleNGBibImportResponse.getMatchedRecords().clear();
        oleNGBibImportResponse.getUnmatchedRecords().clear();
        oleNGBibImportResponse.getMultipleMatchedRecords().clear();
        oleNGBibImportResponse.getRecordsMap().clear();
    }

    protected List<String> buildSortableCallNumber(Map<String, String> codeValues) {
        List<String> shelvingOrder = new ArrayList<>();
        if(org.apache.commons.lang.StringUtils.isNotEmpty(codeValues.get("Call Number"))){
            org.solrmarc.callnum.CallNumber callNumberObj= CallNumberFactory.getInstance().getCallNumber("LCC");
            if (callNumberObj != null) {
                callNumberObj.parse(codeValues.get("Call Number"));
                shelvingOrder.add(callNumberObj.getShelfKey());
            }
        }
        return shelvingOrder;
    }


}
