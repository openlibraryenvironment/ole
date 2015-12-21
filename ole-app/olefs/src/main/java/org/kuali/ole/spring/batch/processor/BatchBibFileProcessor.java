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
    private static final String DASH = "-";

    @Autowired
    private MatchPointProcessor matchPointProcessor;

    @Override
    public String processRecords(List<Record> records, BatchProcessProfile batchProcessProfile) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        String profileName = batchProcessProfile.getBatchProcessProfileName();
        Map<Record, String> queryMap = new HashedMap();
        for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
            Record marcRecord = iterator.next();
            String query = matchPointProcessor.prepareSolrQueryMapForMatchPoint(marcRecord, batchProcessProfile.getBatchProfileMatchPointList());
            if (StringUtils.isNotBlank(query)) {
                queryMap.put(marcRecord, query);
            }
        }
        if(queryMap.size() > 0) {
            for (Iterator<Record> iterator = queryMap.keySet().iterator(); iterator.hasNext(); ) {
                Record key = iterator.next();
                String query = queryMap.get(key);
                JSONObject jsonObject = processOverlay(key, batchProcessProfile, query);
                if(null != jsonObject) {
                    jsonArray.put(jsonObject);
                }
            }
        }
        if (jsonArray.length() > 0) {
            return getOleDsNgRestClient().postData(OleDsNgRestClient.Service.OVERLAY_BIB_HOLDING, jsonArray, OleDsNgRestClient.Format.JSON);
        }
        return null;
    }

    private JSONObject processOverlay(Record marcRecord, BatchProcessProfile batchProcessProfile,String query) throws JSONException {
        LOG.info("Overlay processing started");
        List results = getSolrRequestReponseHandler().getSolrDocumentList(query);
        if (null != results && results.size() == 1) {
            JSONObject bib = new JSONObject();
            SolrDocument solrDocument = (SolrDocument) results.get(0);
            String updatedUserName = getUpdatedUserName();
            String updatedDate = DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date());

            //Bib data

            JSONObject bibData = new JSONObject();

            // Processing custom process based on profile (Casalini/YBP)
            String profileName = batchProcessProfile.getBatchProcessProfileName();
            doCustomProcessForProfile(marcRecord, profileName);

            bibData.put("id", solrDocument.getFieldValue("LocalId_display"));
            bibData.put("content", getMarcXMLConverter().generateMARCXMLContent(marcRecord));
            bibData.put("bibStatus", "Cataloging complete");
            bibData.put("updatedBy", updatedUserName);
            bibData.put("updatedDate", updatedDate);


            JSONObject holdingsData = new JSONObject();
            JSONObject holdingsMatchPoints = prepareMatchPointForHoldingsOrItem(batchProcessProfile.getBatchProfileMatchPointList(), "holdings");
            if (holdingsMatchPoints.length() > 0) {
                holdingsData.put("matchPoints", holdingsMatchPoints);
            }
            bibData.put("holdings", holdingsData);


            JSONObject itemData = new JSONObject();
            JSONObject itemMatchPoints = prepareMatchPointForHoldingsOrItem(batchProcessProfile.getBatchProfileMatchPointList(), "item");
            if (holdingsMatchPoints.length() > 0) {
                itemData.put("matchPoints", itemMatchPoints);
            }
            bibData.put("items", itemData);

            return bibData;
        }
        return null;
    }
    private JSONObject prepareMatchPointForHoldingsOrItem(List<BatchProfileMatchPoint> batchProfileMatchPoints, String matchPointType) throws JSONException {
        JSONObject matchPoints = new JSONObject();
        if(CollectionUtils.isNotEmpty(batchProfileMatchPoints)) {
            for (Iterator<BatchProfileMatchPoint> iterator = batchProfileMatchPoints.iterator(); iterator.hasNext(); ) {
                BatchProfileMatchPoint batchProfileMatchPoint =  iterator.next();
                if(batchProfileMatchPoint.getDataType().equalsIgnoreCase(matchPointType)) {
                    String matchPoint = batchProfileMatchPoint.getMatchPointType();
                    if (StringUtils.isNotBlank(batchProfileMatchPoint.getMatchPointValue())) {
                        matchPoints.put(matchPoint, batchProfileMatchPoint.getMatchPointValue());
                    } else {
                        matchPoints.put(matchPoint, batchProfileMatchPoint.getConstant());
                    }
                }
            }
        }
        return matchPoints;
    }

    private void appendLocationToStrinBuilder(StringBuilder stringBuilder, String location) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append(FORWARD_SLASH).append(location);
        } else {
            stringBuilder.append(location);
        }
    }

    private void doCustomProcessForProfile(Record record,String profileName) {
        if(StringUtils.isNotBlank(profileName) && profileName.equalsIgnoreCase("BibForInvoiceCasalini")) {
            // TODO : process For Casalini
            processCasaliniProfile(record);
        } else if(StringUtils.isNotBlank(profileName) && profileName.equalsIgnoreCase("BibForInvoiceYBP")) {
            // TODO : process For YBP
            processYBPProfile(record);
        }
        getMarcRecordUtil().deleteFieldInRecord(record,"003");
    }

    private void processYBPProfile(Record record) {
        String controlFieldValue = getMarcRecordUtil().getControlFieldValue(record, "001");
        //Removeing ocn,ocm prefix from controlFieldValue
        controlFieldValue = controlFieldValue.replace("ocm","");
        controlFieldValue = controlFieldValue.replace("ocn","");

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
        getMarcRecordUtil().addVariableFieldToRecord(record,dataField);

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
        getMarcRecordUtil().addVariableFieldToRecord(record,dataField);
    }
}
