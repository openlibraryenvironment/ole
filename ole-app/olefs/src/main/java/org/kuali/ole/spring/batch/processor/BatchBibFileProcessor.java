package org.kuali.ole.spring.batch.processor;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.content.bib.marc.ControlField;
import org.kuali.ole.utility.OleDsNgRestClient;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;
import org.marc4j.marc.impl.ControlFieldImpl;

import java.util.*;

/**
 * Created by SheikS on 12/9/2015.
 */
public class BatchBibFileProcessor extends BatchFileProcessor {

    private static final String FORWARD_SLASH = "/";
    private static final String DASH = "-";

    @Override
    public String processRecords(List<Record> records) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
            Record marcRecord = iterator.next();
            List<VariableField> dataFields = marcRecord.getVariableFields("980");
            for (Iterator<VariableField> variableFieldIterator = dataFields.iterator(); variableFieldIterator.hasNext(); ) {
                DataField dataField = (DataField) variableFieldIterator.next();
                List<Subfield> subFields = dataField.getSubfields("a");
                for (Iterator<Subfield> subfieldIterator = subFields.iterator(); subfieldIterator.hasNext(); ) {
                    Subfield subfield = subfieldIterator.next();
                    String matchPoint1 = subfield.getData();
                    SolrRequestReponseHandler solrRequestReponseHandler = new SolrRequestReponseHandler();
                    List results = solrRequestReponseHandler.getSolrDocumentList("mdf_980a:" + "\"" + matchPoint1 + "\"");
                    if (null != results && results.size() == 1) {
                        JSONObject bib = new JSONObject();
                        SolrDocument solrDocument = (SolrDocument) results.get(0);
                        String updatedUserName = getUpdatedUserName();
                        String updatedDate = DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date());

                        //Bib data

                        JSONObject bibData = new JSONObject();

                        marcRecord = processMarcRecordToBibHoldingsAndItem(marcRecord);

                        bibData.put("id", solrDocument.getFieldValue("LocalId_display"));
                        bibData.put("content", generateMARCXMLContent(marcRecord));
                        bibData.put("bibStatus", "Cataloging complete");
                        bibData.put("updatedBy", updatedUserName);
                        bibData.put("updatedDate", updatedDate);


                        //Holdings data
                        String locationLevel1 = "UC";
                        String locationLevel2 = null;
                        String locationLevel3 = "UCX";
                        String locationLevel4 = null;
                        String locationLevel5 = "InProc";

                        String location = formLocation(locationLevel1, locationLevel2, locationLevel3,
                                locationLevel4, locationLevel5);

                        String callNumberForHolding = getContentFromMarcRecord(marcRecord, "050", "$a$b");

                        JSONObject holdingsData = new JSONObject();
                        holdingsData.put("location", location);
                        holdingsData.put("callNumberType", "LCC - Library Of Congress classification");
                        holdingsData.put("callNumber", callNumberForHolding);
                        bibData.put("holdings", holdingsData);

                        //Item data
                        JSONObject itemData = new JSONObject();
                        itemData.put("itemType", "stks - Regular loan");
                        itemData.put("itemStatus", "In Process");
                        bibData.put("items", itemData);

                        jsonArray.put(bibData);
                    }
                }

            }
        }
        return getOleDsNgRestClient().postData(OleDsNgRestClient.Service.OVERLAY_BIB_HOLDING, jsonArray, OleDsNgRestClient.Format.JSON);
    }

    private Record processMarcRecordToBibHoldingsAndItem(Record marcRecord) {
        String value = getContentFromMarcRecord(marcRecord, "035", "$a");
        updateControlField(marcRecord,"001",value);
        return marcRecord;
    }

    private void updateControlField(Record marcRecord, String field,String value) {
        List<VariableField> dataFields = marcRecord.getVariableFields(field);
        for (Iterator<VariableField> variableFieldIterator = dataFields.iterator(); variableFieldIterator.hasNext(); ) {
            ControlFieldImpl controlField = (ControlFieldImpl) variableFieldIterator.next();
            controlField.setData(value);
        }
    }

    /*This method will get the field and tags and will return return the concadinated value
    * Eg:
    *   field : 050
    *   tags  : $a$b*/
    private String getContentFromMarcRecord(Record marcRecord, String field, String tags) {
        List<VariableField> dataFields = marcRecord.getVariableFields(field);
        StringBuilder stringBuilder = new StringBuilder();
        for (Iterator<VariableField> variableFieldIterator = dataFields.iterator(); variableFieldIterator.hasNext(); ) {
            DataField dataField = (DataField) variableFieldIterator.next();
            StringTokenizer stringTokenizer = new StringTokenizer(tags, "$");
            while(stringTokenizer.hasMoreTokens()) {
                String tag = stringTokenizer.nextToken();
                List < Subfield > subFields = dataField.getSubfields(tag);
                for (Iterator<Subfield> subfieldIterator = subFields.iterator(); subfieldIterator.hasNext(); ) {
                    Subfield subfield = subfieldIterator.next();
                    String data = subfield.getData();
                    appendMarcRecordValuesToStrinBuilder(stringBuilder,data);
                }
            }
        }
        return stringBuilder.toString();
    }

    public String formLocation(String locationLevel1, String locationLevel2, String locationLevel3,
                               String locationLevel4, String locationLevel5) {
        StringBuilder location = new StringBuilder();

        if (StringUtils.isNotBlank(locationLevel1)) {
            appendLocationToStrinBuilder(location, locationLevel1);
        }
        if (StringUtils.isNotBlank(locationLevel2)) {
            appendLocationToStrinBuilder(location, locationLevel2);
        }
        if (StringUtils.isNotBlank(locationLevel3)) {
            appendLocationToStrinBuilder(location, locationLevel3);
        }
        if (StringUtils.isNotBlank(locationLevel4)) {
            appendLocationToStrinBuilder(location, locationLevel4);
        }
        if (StringUtils.isNotBlank(locationLevel5)) {
            appendLocationToStrinBuilder(location, locationLevel5);
        }

        return location.toString();
    }

    private void appendLocationToStrinBuilder(StringBuilder stringBuilder, String location) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append(FORWARD_SLASH).append(location);
        } else {
            stringBuilder.append(location);
        }
    }

    private void appendMarcRecordValuesToStrinBuilder(StringBuilder stringBuilder, String location) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append(DASH).append(location);
        } else {
            stringBuilder.append(location);
        }
    }
}
