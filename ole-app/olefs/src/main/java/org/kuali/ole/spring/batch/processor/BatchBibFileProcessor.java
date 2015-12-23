package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrDocument;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.oleng.batch.profile.model.*;
import org.kuali.ole.oleng.describe.processor.bibimport.MatchPointProcessor;
import org.kuali.ole.utility.OleDsNgRestClient;
import org.marc4j.marc.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MatchPointProcessor matchPointProcessor;

    @Override
    public String processRecords(List<Record> records, BatchProcessProfile batchProcessProfile) throws JSONException {

        List addOverlayOps = new ArrayList();

        List<BatchProfileAddOrOverlay> batchProfileAddOrOverlayList = batchProcessProfile.getBatchProfileAddOrOverlayList();
        for (Iterator<BatchProfileAddOrOverlay> iterator = batchProfileAddOrOverlayList.iterator(); iterator.hasNext(); ) {
            BatchProfileAddOrOverlay batchProfileAddOrOverlay = iterator.next();
            String dataType = batchProfileAddOrOverlay.getDataType();
            String matchOption = batchProfileAddOrOverlay.getMatchOption();
            String operation = batchProfileAddOrOverlay.getOperation();

            addOverlayOps.add(getDataTypeInd(dataType) + getMatchOptionInd(matchOption) + getOperationInd(operation));
        }


        JSONArray jsonArray = new JSONArray();
        Map<Record, String> queryMap = new HashedMap();
        for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
            Record marcRecord = iterator.next();
            String query = getMatchPointProcessor().prepareSolrQueryMapForMatchPoint(marcRecord, batchProcessProfile.getBatchProfileMatchPointList());
            if (StringUtils.isNotBlank(query)) {
                queryMap.put(marcRecord, query);
            }
        }

        if (queryMap.size() > 0) {
            for (Iterator<Record> iterator = queryMap.keySet().iterator(); iterator.hasNext(); ) {
                Record key = iterator.next();
                String query = queryMap.get(key);
                JSONObject jsonObject = prepareRequest(key, batchProcessProfile, query);
                if (null != jsonObject) {
                    jsonArray.put(jsonObject);
                }
            }
        }


        if (jsonArray.length() > 0) {
            return getOleDsNgRestClient().postData(OleDsNgRestClient.Service.OVERLAY_BIB_HOLDING, jsonArray, OleDsNgRestClient.Format.JSON);
        }
        return null;
    }

    private String getOperationInd(String operation) {
         return getOperationIndMap().get(operation);
    }

    private String getMatchOptionInd(String matchOption) {
       return getMatchOptionIndMap().get(matchOption);
    }

    private String getDataTypeInd(String dataType) {
        return getDataTypeIndMap().get(dataType);
    }

    private JSONObject prepareRequest(Record marcRecord, BatchProcessProfile batchProcessProfile, String query) throws JSONException {
        LOG.info("Preparing JSON Request for Bib/Holdings/Items");
        List results = getSolrRequestReponseHandler().getSolrDocumentList(query);
        if (null != results && results.size() == 1) {
            SolrDocument solrDocument = (SolrDocument) results.get(0);
            String updatedUserName = getUpdatedUserName();
            String updatedDate = DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date());

            //Bib data
            JSONObject bibData = new JSONObject();

            handleBatchProfileTransformations(marcRecord, batchProcessProfile);

            bibData.put("id", solrDocument.getFieldValue("LocalId_display"));
            bibData.put("content", getMarcXMLConverter().generateMARCXMLContent(marcRecord));
            bibData.put("bibStatus", "Cataloging complete");
            bibData.put("updatedBy", updatedUserName);
            bibData.put("updatedDate", updatedDate);


            JSONObject holdingsData = prepareMatchPointsForHoldings(batchProcessProfile);
            prepareDataMappings(marcRecord, batchProcessProfile, holdingsData, "holdings");
            bibData.put("holdings", holdingsData);


            JSONObject itemData = prepareMatchPointsForItem(batchProcessProfile);
            prepareDataMappings(marcRecord, batchProcessProfile, itemData, "item");
            bibData.put("items", itemData);

            return bibData;
        }
        return null;
    }

    private JSONObject prepareMatchPointsForItem(BatchProcessProfile batchProcessProfile) throws JSONException {
        JSONObject itemData = new JSONObject();
        JSONObject itemMatchPoints = prepareMatchPointsForDocType(batchProcessProfile.getBatchProfileMatchPointList(), "item");
        if (itemMatchPoints.length() > 0) {
            itemData.put("matchPoints", itemMatchPoints);
        }

        return itemData;
    }


    private JSONObject prepareMatchPointsForHoldings(BatchProcessProfile batchProcessProfile) throws JSONException {
        JSONObject holdingsData = new JSONObject();
        JSONObject holdingsMatchPoints = prepareMatchPointsForDocType(batchProcessProfile.getBatchProfileMatchPointList(), "holdings");
        if (holdingsMatchPoints.length() > 0) {
            holdingsData.put("matchPoints", holdingsMatchPoints);
        }
        return holdingsData;
    }

    private void prepareDataMappings(Record marcRecord, BatchProcessProfile batchProcessProfile, JSONObject jsonObject, String docType) throws JSONException {

        JSONObject dataMappings = new JSONObject();

        List<BatchProfileDataMapping> batchProfileDataMappingList = batchProcessProfile.getBatchProfileDataMappingList();
        for (Iterator<BatchProfileDataMapping> iterator = batchProfileDataMappingList.iterator(); iterator.hasNext(); ) {
            BatchProfileDataMapping batchProfileDataMapping = iterator.next();
            String destination = batchProfileDataMapping.getDestination();
            if(destination.equalsIgnoreCase(docType)){
                String newValue;
                String destinationField = batchProfileDataMapping.getField();
                String value = getMarcRecordUtil().getDataFieldValue(marcRecord, batchProfileDataMapping.getDataField(), batchProfileDataMapping.getSubField());
                if(dataMappings.has(destinationField)){
                    newValue = dataMappings.get(destinationField) + " " + value;
                } else {
                    newValue = value;
                }

                dataMappings.put(destinationField, newValue);
            }
        }

        jsonObject.put("dataMapping", dataMappings);
    }

    private JSONObject prepareMatchPointsForDocType(List<BatchProfileMatchPoint> batchProfileMatchPoints, String docType) throws JSONException {
        JSONObject matchPoints = new JSONObject();
        if (CollectionUtils.isNotEmpty(batchProfileMatchPoints)) {
            for (Iterator<BatchProfileMatchPoint> iterator = batchProfileMatchPoints.iterator(); iterator.hasNext(); ) {
                BatchProfileMatchPoint batchProfileMatchPoint = iterator.next();
                if (batchProfileMatchPoint.getDataType().equalsIgnoreCase(docType)) {
                    String matchPoint = batchProfileMatchPoint.getMatchPointType();
                    String newValue;
                    if (matchPoints.has(matchPoint)) {
                        newValue = matchPoints.getString(matchPoint) + "," + getMatchPointValue(batchProfileMatchPoint);
                    } else {
                        newValue = getMatchPointValue(batchProfileMatchPoint);
                    }
                    matchPoints.put(matchPoint, newValue);
                }
            }
        }
        return matchPoints;
    }

    private String getMatchPointValue(BatchProfileMatchPoint batchProfileMatchPoint) {
        String matchPointValue = batchProfileMatchPoint.getMatchPointValue();
        if (StringUtils.isNotBlank(matchPointValue)) {
            return matchPointValue;
        }

        return batchProfileMatchPoint.getConstant();
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
            operationIndMap = new HashedMap();
            operationIndMap.put("add", "1");
            operationIndMap.put("overlay", "2");
            operationIndMap.put("discard", "3");
        }
        return operationIndMap;
    }

    public void setOperationIndMap(Map<String, String> operationIndMap) {
        this.operationIndMap = operationIndMap;
    }

    public Map<String, String> getMatchOptionIndMap() {
        if (null == matchOptionIndMap) {
            matchOptionIndMap = new HashedMap();
            matchOptionIndMap.put("do match", "1");
            matchOptionIndMap.put("do not match", "2");
        }
        return matchOptionIndMap;
    }

    public void setMatchOptionIndMap(Map<String, String> matchOptionIndMap) {
        this.matchOptionIndMap = matchOptionIndMap;
    }

    public Map<String, String> getDataTypeIndMap() {
        if (null == dataTypeIndMap) {
            dataTypeIndMap = new HashedMap();
            dataTypeIndMap.put("bibliographic", "1");
            dataTypeIndMap.put("holdings", "2");
            dataTypeIndMap.put("items", "3");
            dataTypeIndMap.put("eholdings", "4");
        }
        return dataTypeIndMap;
    }

    public void setDataTypeIndMap(Map<String, String> dataTypeIndMap) {
        this.dataTypeIndMap = dataTypeIndMap;
    }

    public MatchPointProcessor getMatchPointProcessor() {
        return matchPointProcessor;
    }

    public void setMatchPointProcessor(MatchPointProcessor matchPointProcessor) {
        this.matchPointProcessor = matchPointProcessor;
    }
}
