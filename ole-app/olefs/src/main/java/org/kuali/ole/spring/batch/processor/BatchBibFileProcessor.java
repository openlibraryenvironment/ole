package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrDocument;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.oleng.batch.profile.model.*;
import org.kuali.ole.oleng.batch.reports.BatchReportLogHandler;
import org.kuali.ole.utility.OleDsNgRestClient;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.util.ObjectUtils;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    public String processRecords(List<Record> records, BatchProcessProfile batchProcessProfile) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        String response = "";
        int matchedBibsCount = 0;
        int unmatchedBibsCount = 0;
        int multipleMatchedBibsCount = 0;
        for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
            JSONObject jsonObject = null;

            Record marcRecord = iterator.next();
            if (!batchProcessProfile.getBatchProfileMatchPointList().isEmpty()) {
                String query = getMatchPointProcessor().prepareSolrQueryMapForMatchPoint(marcRecord, batchProcessProfile.getBatchProfileMatchPointList());

                if (StringUtils.isNotBlank(query)) {
                    List results = getSolrRequestReponseHandler().getSolrDocumentList(query);
                    if (null == results || results.size() > 1) {
                        System.out.println("**** More than one record found for query : " + query);
                        multipleMatchedBibsCount = multipleMatchedBibsCount + results.size();
                        continue;
                    }

                    if (null != results && results.size() == 1) {
                        SolrDocument solrDocument = (SolrDocument) results.get(0);
                        String bibId = (String) solrDocument.getFieldValue(DocstoreConstants.LOCALID_DISPLAY);
                        jsonObject = prepareRequest(bibId, marcRecord, batchProcessProfile);
                        matchedBibsCount = matchedBibsCount + 1;
                    } else {
                        jsonObject = prepareRequest(null, marcRecord, batchProcessProfile);
                        unmatchedBibsCount = unmatchedBibsCount + 1;
                    }
                }
            } else {
                jsonObject = prepareRequest(null, marcRecord, batchProcessProfile);
                unmatchedBibsCount = unmatchedBibsCount + 1;
            }
            jsonArray.put(jsonObject);
        }

        if (jsonArray.length() > 0) {
            response = getOleDsNgRestClient().postData(OleDsNgRestClient.Service.PROCESS_BIB_HOLDING_ITEM, jsonArray, OleDsNgRestClient.Format.JSON);
            try {
                OleNGBibImportResponse oleNGBibImportResponse = getObjectMapper().readValue(response, OleNGBibImportResponse.class);
                oleNGBibImportResponse.setMatchedBibsCount(matchedBibsCount);
                oleNGBibImportResponse.setUnmatchedBibsCount(unmatchedBibsCount);
                oleNGBibImportResponse.setMultipleMatchedBibsCount(multipleMatchedBibsCount);
                generateBatchReport(oleNGBibImportResponse);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    public void generateBatchReport(OleNGBibImportResponse oleNGBibImportResponse) throws Exception {
        BatchReportLogHandler batchReportLogHandler = BatchReportLogHandler.getInstance();
        batchReportLogHandler.logMessage(oleNGBibImportResponse);
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
    private JSONObject prepareRequest(String bibId, Record marcRecord, BatchProcessProfile batchProcessProfile) throws JSONException {
        LOG.info("Preparing JSON Request for Bib/Holdings/Items");

        JSONObject bibData = new JSONObject();
        String unmodifiedRecord = getMarcXMLConverter().generateMARCXMLContent(marcRecord);
        String updatedUserName = getUpdatedUserName();
        String updatedDate = DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date());

        if (null != bibId) {
            bibData.put("id", bibId);
        }

        bibData.put(OleNGConstants.UPDATED_BY, updatedUserName);
        bibData.put(OleNGConstants.UPDATED_DATE, updatedDate);
        bibData.put(OleNGConstants.UNMODIFIED_CONTENT, unmodifiedRecord);
        bibData.put(OleNGConstants.OPS, getOverlayOps(batchProcessProfile));
        bibData.put(OleNGConstants.ADDED_OPS, getAddedOps(batchProcessProfile));
        bibData.put(OleNGConstants.ACTION_OPS, getActionOps(batchProcessProfile));
        bibData.put(OleNGConstants.FIELD_OPS, getFieldOps(batchProcessProfile));

        // Prepare data mapping before MARC Transformation
        Map<String, List<JSONObject>> dataMappingsMapPreTransformation = prepareDataMapping(marcRecord, batchProcessProfile, OleNGConstants.PRE_MARC_TRANSFORMATION);


        //Transformations pertaining to MARC record (001,003,035$a etc..)
        handleBatchProfileTransformations(marcRecord, batchProcessProfile);
        String modifiedRecord = getMarcXMLConverter().generateMARCXMLContent(marcRecord);
        bibData.put(OleNGConstants.MODIFIED_CONTENT, modifiedRecord);

        // Prepare data mapping after MARC Transformation
        Map<String, List<JSONObject>> dataMappingsMapPostTransformations = prepareDataMapping(marcRecord, batchProcessProfile, OleNGConstants.POST_MARC_TRANSFORMATION);


        List<JSONObject> bibDataMappingsPreTrans = dataMappingsMapPreTransformation.get(OleNGConstants.BIB_DATAMAPPINGS);
        List<JSONObject> bibDataMappingsPostTrans = dataMappingsMapPostTransformations.get(OleNGConstants.BIB_DATAMAPPINGS);
        bibData.put(OleNGConstants.DATAMAPPING, buildOneObjectForList(bibDataMappingsPreTrans, bibDataMappingsPostTrans));

        JSONObject holdingsData = getMatchPointProcessor().prepareMatchPointsForHoldings(marcRecord, batchProcessProfile);
        List<JSONObject> holdingsDataMappingsPreTrans = dataMappingsMapPreTransformation.get(OleNGConstants.HOLDINGS_DATAMAPPINGS);
        List<JSONObject> holdingsDataMappingsPostTrans = dataMappingsMapPostTransformations.get(OleNGConstants.HOLDINGS_DATAMAPPINGS);
        holdingsData.put(OleNGConstants.DATAMAPPING, buildOneObjectForList(holdingsDataMappingsPreTrans, holdingsDataMappingsPostTrans));
        bibData.put(OleNGConstants.HOLDINGS, holdingsData);

        JSONObject eholdingsData = getMatchPointProcessor().prepareMatchPointsForEHoldings(marcRecord, batchProcessProfile);
        List<JSONObject> eholdingsDataMappingsPreTrans = dataMappingsMapPreTransformation.get(OleNGConstants.EHOLDINGS_DATAMAPPINGS);
        List<JSONObject> eholdingsDataMappingsPostTrans = dataMappingsMapPostTransformations.get(OleNGConstants.EHOLDINGS_DATAMAPPINGS);
        eholdingsData.put(OleNGConstants.DATAMAPPING, buildOneObjectForList(eholdingsDataMappingsPreTrans, eholdingsDataMappingsPostTrans));
        bibData.put(OleNGConstants.EHOLDINGS, eholdingsData);

        JSONObject itemData = getMatchPointProcessor().prepareMatchPointsForItem(marcRecord, batchProcessProfile);
        List<JSONObject> itemsDataMappingsPreTrans = dataMappingsMapPreTransformation.get(OleNGConstants.ITEM_DATAMAPPINGS);
        List<JSONObject> itemsDataMappingsPostTrans = dataMappingsMapPostTransformations.get(OleNGConstants.ITEM_DATAMAPPINGS);
        itemData.put(OleNGConstants.DATAMAPPING, buildOneObjectForList(itemsDataMappingsPreTrans, itemsDataMappingsPostTrans));
        bibData.put(OleNGConstants.ITEM, itemData);

        return bibData;
    }

    private JSONArray getFieldOps(BatchProcessProfile batchProcessProfile) {
        JSONArray fieldOps = new JSONArray();
        List<BatchProfileFieldOperation> batchProfileFieldOperationList = batchProcessProfile.getBatchProfileFieldOperationList();
        if(CollectionUtils.isNotEmpty(batchProfileFieldOperationList)) {
            for (Iterator<BatchProfileFieldOperation> iterator = batchProfileFieldOperationList.iterator(); iterator.hasNext(); ) {
                BatchProfileFieldOperation batchProfileFieldOperation = iterator.next();
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(OleNGConstants.DATA_FIELD,batchProfileFieldOperation.getDataField());
                    jsonObject.put(OleNGConstants.IND1,batchProfileFieldOperation.getInd1());
                    jsonObject.put(OleNGConstants.IND2,batchProfileFieldOperation.getInd2());
                    jsonObject.put(OleNGConstants.SUBFIELD,batchProfileFieldOperation.getSubField());
                    jsonObject.put(OleNGConstants.VALUE,batchProfileFieldOperation.getValue());
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

    private List<JSONObject> buildOneObjectForList(List<JSONObject> dataMappingsPreTrans, List<JSONObject> dataMappingsPostTrans) {
        List<JSONObject> finalObjects = new ArrayList<>();

        for (int index = 0; index < dataMappingsPreTrans.size(); index++) {
            JSONObject preTransformObject = dataMappingsPreTrans.get(index);
            JSONObject postTransformObject = dataMappingsPostTrans.get(index);
            finalObjects.add(buildOneObject(preTransformObject, postTransformObject));
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
                            || addOperation.equalsIgnoreCase(OleNGConstants.OVERLAY_MULTIPLE))) {
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
            addedOps.put(OleNGConstants.HOLDINGS,holdingsAddedOps);

            String itemAddedOps = getAddedOps(batchProfileAddOrOverlayList, OleNGConstants.ITEM);
            addedOps.put(OleNGConstants.ITEM,itemAddedOps);

            String eholdingsAddedOps = getAddedOps(batchProfileAddOrOverlayList, OleNGConstants.EHOLDINGS);
            addedOps.put(OleNGConstants.EHOLDINGS,eholdingsAddedOps);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return addedOps;
    }

    public String getAddedOps(List<BatchProfileAddOrOverlay> batchProfileAddOrOverlayList, String docType) throws JSONException {
        String addedOpsValue = null;

        for (Iterator<BatchProfileAddOrOverlay> iterator = batchProfileAddOrOverlayList.iterator(); iterator.hasNext(); ) {
            BatchProfileAddOrOverlay batchProfileAddOrOverlay = iterator.next();
            String matchOption = batchProfileAddOrOverlay.getMatchOption();
            if(matchOption.equalsIgnoreCase(OleNGConstants.IF_MATCH_FOUND) && batchProfileAddOrOverlay.getDataType().equalsIgnoreCase(docType)) {
                String operation = batchProfileAddOrOverlay.getOperation();
                if(operation.equalsIgnoreCase(OleNGConstants.ADD)) {
                    addedOpsValue = OleNGConstants.DELETE_ALL_EXISTING_AND_ADD;
                    String addOperation = batchProfileAddOrOverlay.getAddOperation();
                    if(StringUtils.isNotBlank(addOperation)){
                        switch (addOperation){
                            case OleNGConstants.DELETE_ALL_EXISTING_AND_ADD : {
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
                } else if(operation.equalsIgnoreCase(OleNGConstants.OVERLAY)) {
                    addedOpsValue = OleNGConstants.OVERLAY;
                } else {
                    addedOpsValue = OleNGConstants.DISCARD;
                }
            }
        }
        return addedOpsValue;
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
                dataMapping.put(key, valueArray);
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

    /**
     * @param valueByPriorityMap
     * @param destinationField
     * @param multiValue
     * @param fieldValues
     * @param priority
     * @return The valueByPriorityMap contains valueByPriority pojo for each destination field.
     */
    private Map<String, List<ValueByPriority>> buildingValuesForDestinationBasedOnPriority(Map<String, List<ValueByPriority>> valueByPriorityMap, String destinationField, boolean multiValue, List<String> fieldValues, int priority) {
        List<ValueByPriority> valueByPriorities;

        ValueByPriority valueByPriority = new ValueByPriority();
        valueByPriority.setField(destinationField);
        valueByPriority.setPriority(priority);
        valueByPriority.setMultiValue(multiValue);

        valueByPriority.setValues(fieldValues);

        if (valueByPriorityMap.containsKey(destinationField)) {
            valueByPriorities = valueByPriorityMap.get(destinationField);

            if (valueByPriorities.contains(valueByPriority)) {
                ValueByPriority existingValuePriority = valueByPriorities.get(valueByPriorities.indexOf(valueByPriority));
                List<String> values = existingValuePriority.getValues();
                values.addAll(fieldValues);
                StringBuilder stringBuilder = new StringBuilder();
                if (!multiValue) {
                    for (Iterator<String> iterator = values.iterator(); iterator.hasNext(); ) {
                        String value = iterator.next();
                        stringBuilder.append(value);
                        if (iterator.hasNext()) {
                            stringBuilder.append(" ");
                        }
                    }
                    values.clear();
                    values.add(stringBuilder.toString());
                }
            } else {
                valueByPriorities.add(valueByPriority);
            }
        } else {
            valueByPriorities = new ArrayList<>();
            valueByPriorities.add(valueByPriority);
        }
        valueByPriorityMap.put(destinationField, valueByPriorities);

        return valueByPriorityMap;
    }

    private List<String> getFieldValues(Record marcRecord, BatchProfileDataMapping batchProfileDataMapping, boolean multiValue) {
        List<String> marcValues = new ArrayList<>();
        if (batchProfileDataMapping.getDataType().equalsIgnoreCase(OleNGConstants.BIB_MARC)) {
            String marcValue;
            String dataField = batchProfileDataMapping.getDataField();
            if (StringUtils.isNotBlank(dataField)) {
                if (getMarcRecordUtil().isControlField(dataField)) {
                    marcValue = getMarcRecordUtil().getControlFieldValue(marcRecord, dataField);
                    marcValues.add(marcValue);
                } else {
                    String subField = batchProfileDataMapping.getSubField();
                    if (multiValue) {
                        marcValues = getMarcRecordUtil().getMultiDataFieldValues(marcRecord, dataField, batchProfileDataMapping.getInd1(), batchProfileDataMapping.getInd2(), subField);
                    } else {
                        marcValue = getMarcRecordUtil().getDataFieldValueWithIndicators(marcRecord, dataField, batchProfileDataMapping.getInd1(), batchProfileDataMapping.getInd2(), subField);
                        if (StringUtils.isNotBlank(marcValue)) {
                            marcValues.add(marcValue);
                        }
                    }
                }
            }
        } else {
            String constantValue = batchProfileDataMapping.getConstant();
            if (StringUtils.isNotBlank(constantValue)) {
                marcValues.add(constantValue);
            }
        }
        return marcValues;
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

    private void sortDataMappings(List<BatchProfileDataMapping> filteredDataMappings) {
        Collections.sort(filteredDataMappings, new Comparator<BatchProfileDataMapping>() {
            public int compare(BatchProfileDataMapping dataMapping1, BatchProfileDataMapping dataMapping2) {
                int priorityForDataMapping1 = dataMapping1.getPriority();
                int priorityForDataMapping2 = dataMapping2.getPriority();
                return new Integer(priorityForDataMapping1).compareTo(new Integer(priorityForDataMapping2));
            }
        });
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


    private class ValueByPriority {
        private int priority;
        private String field;
        private boolean multiValue;
        private List<String> values;

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public boolean isMultiValue() {
            return multiValue;
        }

        public void setMultiValue(boolean multiValue) {
            this.multiValue = multiValue;
        }

        public List<String> getValues() {
            if (null == values) {
                values = new ArrayList<>();
            }
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }

        public void addValues(String value) {
            if (null != value) {
                getValues().add(value);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ValueByPriority that = (ValueByPriority) o;

            if (priority != that.priority) return false;
            return field != null ? field.equals(that.field) : that.field == null;

        }

        @Override
        public int hashCode() {
            int result = priority;
            result = 31 * result + (field != null ? field.hashCode() : 0);
            return result;
        }
    }

    public List<Record> splitRecordByMultiValue(Record record, MarcDataField marcDataField) {
        String dataFieldString = marcDataField.getDataField();
        String ind1 = marcDataField.getInd1();
        String ind2 = marcDataField.getInd2();
        String subField = marcDataField.getSubField();


        List<Record> records = new ArrayList<>();
        List<VariableField> dataFields = record.getVariableFields(dataFieldString);

        List<VariableField> filteredDataFields = new ArrayList<>();
        boolean matchedDataField = true;

        for (Iterator<VariableField> iterator = dataFields.iterator(); iterator.hasNext(); ) {
            DataField field = (DataField) iterator.next();
            if (StringUtils.isNotBlank(ind1)) {
                matchedDataField &= ind1.charAt(0) == field.getIndicator1();
            }
            if (StringUtils.isNotBlank(ind2)) {
                matchedDataField &= ind2.charAt(0) == field.getIndicator2();
            }

            if (null != subField) {
                for (Iterator<Subfield> variableFieldIterator = field.getSubfields().iterator(); variableFieldIterator.hasNext(); ) {
                    Subfield sf = variableFieldIterator.next();
                    char subFieldChar = (StringUtils.isNotBlank(subField) ? subField.charAt(0) : ' ');
                    matchedDataField&= subFieldChar == sf.getCode();
                }
            }
            if (matchedDataField) {
                filteredDataFields.add(field);
            }

        }

        for (Iterator<VariableField> variableFieldIterator = filteredDataFields.iterator(); variableFieldIterator.hasNext(); ) {
            DataField dataField = (DataField) variableFieldIterator.next();
            Record clonedRecord = (Record) ObjectUtils.deepCopy(record);
            getMarcRecordUtil().removeFieldFromRecord(clonedRecord, dataFieldString);
            getMarcRecordUtil().addVariableFieldToRecord(clonedRecord, dataField);
            records.add(clonedRecord);
        }

        return CollectionUtils.isNotEmpty(records) ? records : Collections.singletonList(record);

    }

}
