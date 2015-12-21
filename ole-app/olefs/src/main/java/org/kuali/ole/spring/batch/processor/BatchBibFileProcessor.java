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
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataMapping;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataTransformer;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileMatchPoint;
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
    private static final Logger LOG = Logger.getLogger(BatchBibFileProcessor.class);

    private static final String FORWARD_SLASH = "/";

    @Autowired
    private MatchPointProcessor matchPointProcessor;

    @Override
    public String processRecords(List<Record> records, BatchProcessProfile batchProcessProfile) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        Map<Record, String> queryMap = new HashedMap();
        for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
            Record marcRecord = iterator.next();
            String query = matchPointProcessor.prepareSolrQueryMapForMatchPoint(marcRecord, batchProcessProfile.getBatchProfileMatchPointList());
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

    private JSONObject prepareRequest(Record marcRecord, BatchProcessProfile batchProcessProfile, String query) throws JSONException {
        LOG.info("Preparing JSON Request for Bib/Holdings/Items");
        List results = getSolrRequestReponseHandler().getSolrDocumentList(query);
        if (null != results && results.size() == 1) {
            JSONObject bib = new JSONObject();
            SolrDocument solrDocument = (SolrDocument) results.get(0);
            String updatedUserName = getUpdatedUserName();
            String updatedDate = DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date());

            //Bib data

            JSONObject bibData = new JSONObject();

            String profileName = batchProcessProfile.getBatchProcessProfileName();
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
                String value = getMarcRecordUtil().getContentFromMarcRecord(marcRecord, batchProfileDataMapping.getDataField(), batchProfileDataMapping.getSubField());
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
        List<BatchProfileDataTransformer> batchProfileDataTransformerList =
                batchProcessProfile.getBatchProfileDataTransformerList();

        Map steps = new TreeMap();

        for (Iterator<BatchProfileDataTransformer> iterator = batchProfileDataTransformerList.iterator(); iterator.hasNext(); ) {
            BatchProfileDataTransformer transformer = iterator.next();
            steps.put(transformer.getStep(), transformer);
        }




    }

    private void processYBPProfile(Record record) {
        String controlFieldValue = getMarcRecordUtil().getControlFieldValue(record, "001");
        //Removeing ocn,ocm prefix from controlFieldValue
        controlFieldValue = controlFieldValue.replace("ocm", "");
        controlFieldValue = controlFieldValue.replace("ocn", "");

        String valueOf003 = getMarcRecordUtil().getControlFieldValue(record, "003");
        String valueToUpdate030 = "(" + valueOf003 + ")" + controlFieldValue;

        MarcFactory factory = MarcFactory.newInstance();

        // Adding new field 035
        DataField dataField = factory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');
        Subfield subfield = factory.newSubfield();
        subfield.setCode('a');
        subfield.setData(valueToUpdate030);
        dataField.addSubfield(subfield);
        getMarcRecordUtil().addVariableFieldToRecord(record, dataField);

        //update 001 value by bibId

    }

    private void processCasaliniProfile(Record record) {
        String controlFieldValue = getMarcRecordUtil().getControlFieldValue(record, "001");
        String valueOf003 = getMarcRecordUtil().getControlFieldValue(record, "003");
        String valueToUpdate030 = "(" + valueOf003 + ")" + controlFieldValue;

        MarcFactory factory = MarcFactory.newInstance();

        // Adding new field 035
        DataField dataField = factory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');
        Subfield subfield = factory.newSubfield();
        subfield.setCode('a');
        subfield.setData(valueToUpdate030);
        dataField.addSubfield(subfield);
        getMarcRecordUtil().addVariableFieldToRecord(record, dataField);
    }
}
