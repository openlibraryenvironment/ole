package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.content.bib.marc.ControlField;
import org.kuali.ole.utility.OleDsNgRestClient;
import org.marc4j.marc.*;
import org.marc4j.marc.impl.ControlFieldImpl;

import java.util.*;

/**
 * Created by SheikS on 12/9/2015.
 */
public class BatchBibFileProcessor extends BatchFileProcessor {

    private static final String FORWARD_SLASH = "/";
    private static final String DASH = "-";

    @Override
    public String processRecords(List<Record> records,String profileName) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        SolrRequestReponseHandler solrRequestReponseHandler = new SolrRequestReponseHandler();
        for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
            Record marcRecord = iterator.next();
            List<VariableField> dataFields = marcRecord.getVariableFields("980");
            for (Iterator<VariableField> variableFieldIterator = dataFields.iterator(); variableFieldIterator.hasNext(); ) {
                DataField dataField = (DataField) variableFieldIterator.next();
                List<Subfield> subFields = dataField.getSubfields("a");
                for (Iterator<Subfield> subfieldIterator = subFields.iterator(); subfieldIterator.hasNext(); ) {
                    Subfield subfield = subfieldIterator.next();
                    String matchPoint1 = subfield.getData();
                    List results = solrRequestReponseHandler.getSolrDocumentList("mdf_980a:" + "\"" + matchPoint1 + "\"");
                    if (null != results && results.size() == 1) {
                        JSONObject bib = new JSONObject();
                        SolrDocument solrDocument = (SolrDocument) results.get(0);
                        String updatedUserName = getUpdatedUserName();
                        String updatedDate = DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date());

                        //Bib data

                        JSONObject bibData = new JSONObject();

                        marcRecord = processMarcRecordToBibHoldingsAndItem(marcRecord);

                        // Processing custom process based on profile (Casalini/YBP)
                        doCustomProcessForProfile(marcRecord,profileName);

                        bibData.put("id", solrDocument.getFieldValue("LocalId_display"));
                        bibData.put("content", getMarcXMLConverter().generateMARCXMLContent(marcRecord));
                        bibData.put("bibStatus", "Cataloging complete");
                        bibData.put("updatedBy", updatedUserName);
                        bibData.put("updatedDate", updatedDate);


                        //Holdings data
                        String locationLevel1 = "UC";
                        String locationLevel2 = null;
                        String locationLevel3 = "UCX";
                        String locationLevel4 = null;
                        String locationLevel5 = "InProc";

                        String locationForHolding = formLocation(locationLevel1, locationLevel2, locationLevel3,
                                locationLevel4, locationLevel5);

                        String callNumberForHolding = getMarcRecordUtil().getContentFromMarcRecord(marcRecord, "050", "$a$b");

                        JSONObject holdingsData = new JSONObject();
                        holdingsData.put("location", locationForHolding);
                        holdingsData.put("callNumberType", "LCC - Library Of Congress classification");
                        holdingsData.put("callNumber", callNumberForHolding);
                        if (profileName.equalsIgnoreCase("BibForInvoiceYBP")) {
                            String callNumberPrefix = getMarcRecordUtil().getContentFromMarcRecord(marcRecord, "090", "$p");
                            locationLevel5 = getMarcRecordUtil().getContentFromMarcRecord(marcRecord, "980", "$c");
                            locationLevel3 = getMarcRecordUtil().getContentFromMarcRecord(marcRecord, "980", "$d");
                            locationForHolding = formLocation(locationLevel1, locationLevel2, locationLevel3,
                                    locationLevel4, locationLevel5);
                            holdingsData.put("callNumberPrefix", callNumberPrefix);
                            holdingsData.put("location", locationForHolding);
                        }
                        bibData.put("holdings", holdingsData);

                        //Item data
                        JSONObject itemData = new JSONObject();
                        itemData.put("itemType", "stks - Regular loan");
                        itemData.put("itemStatus", "In Process");
                        if (profileName.equalsIgnoreCase("BibForInvoiceYBP")) {
                            itemData.put("copyNumber", "c.1");
                            itemData.put("callNumberType", "LCC - Library Of Congress classification");
                        }
                        bibData.put("items", itemData);

                        jsonArray.put(bibData);
                    }
                }

            }
        }
        return getOleDsNgRestClient().postData(OleDsNgRestClient.Service.OVERLAY_BIB_HOLDING, jsonArray, OleDsNgRestClient.Format.JSON);
    }

    private Record processMarcRecordToBibHoldingsAndItem(Record marcRecord) {
        String value = getMarcRecordUtil().getContentFromMarcRecord(marcRecord, "035", "$a");
        getMarcRecordUtil().updateControlField(marcRecord,"001",value);
        return marcRecord;
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

    private void doCustomProcessForProfile(Record record,String profileName) {
        if(StringUtils.isNotBlank(profileName) && profileName.equalsIgnoreCase("BibForInvoiceCasalini")) {
            // TODO : process For Casalini
            processCasaliniProfile(record);
        } else if(StringUtils.isNotBlank(profileName) && profileName.equalsIgnoreCase("BibForInvoiceYBP")) {
            // TODO : process For YBP
            processYBPProfile(record);
        }
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
