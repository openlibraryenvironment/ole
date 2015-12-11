package org.kuali.ole.dsng.rest.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.*;
import org.kuali.ole.dsng.dao.BibDAO;
import org.kuali.ole.dsng.dao.HoldingDAO;
import org.kuali.ole.dsng.dao.ItemDAO;
import org.kuali.ole.dsng.util.OleDsHelperUtil;
import org.marc4j.marc.*;
import org.marc4j.marc.impl.ControlFieldImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * Created by SheikS on 12/8/2015.
 */
public class OleDsNgOverlayProcessor extends OleDsHelperUtil implements DocstoreConstants {


    @Autowired
    BibDAO bibDAO;

    @Autowired
    HoldingDAO holdingDAO;

    @Autowired
    ItemDAO itemDAO;

    public String processOverlayForBib(String jsonBody) {
        JSONArray responseJsonArray = null;
        try {
            JSONArray requestJsonArray = new JSONArray(jsonBody);
            responseJsonArray = new JSONArray();
            for (int index = 0; index < requestJsonArray.length(); index++) {
                JSONObject jsonObject = requestJsonArray.getJSONObject(index);

                String bibId = jsonObject.getString("id");

                String updatedContent = jsonObject.getString("content");
                String updatedBy = jsonObject.getString("updatedBy");
                String updatedDateString = (String) jsonObject.get("updatedDate");

                BibRecord bibRecord = bibDAO.retrieveBibById(bibId);
                if (null != bibRecord) {
                    //TODO : process bib record with overlay
                    bibRecord.setContent(updatedContent);
                    bibRecord.setUpdatedBy(updatedBy);
                    if (StringUtils.isNotBlank(updatedDateString)) {
                        bibRecord.setDateEntered(getDateTimeStamp(updatedDateString));
                    }
                    BibRecord updatedBibRecord = bibDAO.save(bibRecord);

                    getBibIndexer().updateDocument(updatedBibRecord);

                    JSONObject responseObject = new JSONObject();
                    responseObject.put("bibId", updatedBibRecord.getBibId());
                    responseJsonArray.put(responseObject);
                } else {
                    // TODO : need to handle if bib record is not found
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseJsonArray.toString();
    }


    public String processOverlayForHolding(String jsonBody) {
        JSONArray responseJsonArray = null;
        try {
            JSONArray requestJsonArray = new JSONArray(jsonBody);
            responseJsonArray = new JSONArray();
            for (int index = 0; index < requestJsonArray.length(); index++) {
                JSONObject jsonObject = requestJsonArray.getJSONObject(index);

                String holdingId = jsonObject.getString("id");

                //String updatedContent = jsonObject.getString("content");
                String updatedBy = jsonObject.getString("updatedBy");
                String updatedDateString = (String) jsonObject.get("updatedDate");

                HoldingsRecord holdingsRecord = holdingDAO.retrieveHoldingById(holdingId);
                if (null != holdingsRecord) {
                    //TODO : process holding record with overlay
                    holdingsRecord.setUpdatedBy(updatedBy);
                    if (StringUtils.isNotBlank(updatedDateString)) {
                        holdingsRecord.setUpdatedDate(getDateTimeStamp(updatedDateString));
                    }
                    HoldingsRecord updatedHoldingRecord = holdingDAO.save(holdingsRecord);

                    getHoldingIndexer().updateDocument(updatedHoldingRecord);

                    JSONObject responseObject = new JSONObject();
                    responseObject.put("holdingId", updatedHoldingRecord.getHoldingsId());
                    responseJsonArray.put(responseObject);
                } else {
                    // TODO : need to handle if bib record is not found
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseJsonArray.toString();
    }


    public String processOverlayForBibAndHoldingsAndItems(String jsonBody) {
        JSONArray responseJsonArray = null;
        Map<String,SolrInputDocument> solrInputDocumentMap = new HashedMap();
        try {
            JSONArray requestJsonArray = new JSONArray(jsonBody);
            responseJsonArray = new JSONArray();
            for (int index = 0; index < requestJsonArray.length(); index++) {
                JSONObject jsonObject = requestJsonArray.getJSONObject(index);

                String bibId = jsonObject.getString("id");

                String updatedContent = jsonObject.getString("content");
                String updatedBy = jsonObject.getString("updatedBy");
                String updatedDateString = (String) jsonObject.get("updatedDate");
                String bibStatus = getStringValueFromJsonObject(jsonObject,"bibStatus");
                String profileName = getStringValueFromJsonObject(jsonObject,"profileName");


                BibRecord bibRecord = bibDAO.retrieveBibById(bibId);
                if (null != bibRecord) {
                    //TODO : process bib record with overlay
                    updatedContent = doCustomProcessForProfile(updatedContent, profileName,bibId);
                    bibRecord.setContent(updatedContent);
                    bibRecord.setUpdatedBy(updatedBy);
                    Timestamp updatedDate = null;
                    if (StringUtils.isNotBlank(updatedDateString)) {
                        updatedDate =  getDateTimeStamp(updatedDateString);
                    }
                    bibRecord.setDateEntered(updatedDate);
                    bibRecord.setStatus(bibStatus);
                    bibRecord.setStatusUpdatedBy(updatedBy);
                    bibRecord.setStatusUpdatedDate(updatedDate);
                    BibRecord updatedBibRecord = bibDAO.save(bibRecord);

                    solrInputDocumentMap = getBibIndexer().getInputDocumentForBib(bibRecord, solrInputDocumentMap);

                    JSONObject responseObject = new JSONObject();
                    responseObject.put("bibId", updatedBibRecord.getBibId());

                    // Processing Holdings
                    List<HoldingsRecord> holdingsRecords = bibRecord.getHoldingsRecords();
                    if (CollectionUtils.isNotEmpty(holdingsRecords) && jsonObject.has("holdings")) {
                        for (Iterator<HoldingsRecord> iterator = holdingsRecords.iterator(); iterator.hasNext(); ) {
                            HoldingsRecord holdingsRecord = iterator.next();
                            JSONObject holdingJsonObject = jsonObject.getJSONObject("holdings");
                            String location = getStringValueFromJsonObject(holdingJsonObject, "location");
                            String callNumberTypeName = getStringValueFromJsonObject(holdingJsonObject, "callNumberType");
                            String callNumber = getStringValueFromJsonObject(holdingJsonObject, "callNumber");
                            holdingsRecord.setLocation(location);
                            CallNumberTypeRecord callNumberTypeRecord = fetchCallNumberTypeRecordByName(callNumberTypeName);
                            if (null != callNumberTypeRecord) {
                                holdingsRecord.setCallNumberTypeId(callNumberTypeRecord.getCallNumberTypeId());
                                holdingsRecord.setCallNumberTypeRecord(callNumberTypeRecord);
                            }
                            holdingsRecord.setUpdatedBy(updatedBy);
                            holdingsRecord.setUpdatedDate(updatedDate);
                            holdingsRecord.setCallNumber(callNumber);
                            if (profileName.equalsIgnoreCase("BibForInvoiceYBP")) {
                                String callNumberPrefix = getStringValueFromJsonObject(holdingJsonObject, "callNumberPrefix");
                                holdingsRecord.setCallNumberPrefix(callNumberPrefix);
                            }
                            HoldingsRecord updatedHoldignsRecord = holdingDAO.save(holdingsRecord);
                            solrInputDocumentMap = getHoldingIndexer().getInputDocumentForHoldings(holdingsRecord, solrInputDocumentMap);

                            // Processing Items
                            List<ItemRecord> itemRecords = holdingsRecord.getItemRecords();
                            if(CollectionUtils.isNotEmpty(itemRecords) && jsonObject.has("items")) {
                                for (Iterator<ItemRecord> itemRecordIterator = itemRecords.iterator(); itemRecordIterator.hasNext(); ) {
                                    ItemRecord itemRecord = itemRecordIterator.next();
                                    JSONObject itemJsonObject = jsonObject.getJSONObject("items");
                                    String itemStatusName = getStringValueFromJsonObject(itemJsonObject, "itemStatus");
                                    String itemTypeName = getStringValueFromJsonObject(itemJsonObject, "itemType");
                                    ItemStatusRecord itemStatusRecord = fetchItemStatusByName(itemStatusName);
                                    if(null != itemStatusRecord) {
                                        itemRecord.setItemStatusId(itemStatusRecord.getItemStatusId());
                                        itemRecord.setItemStatusRecord(itemStatusRecord);
                                    }

                                    ItemTypeRecord itemTypeRecord = fetchItemTypeByName(itemTypeName);
                                    if(null != itemTypeRecord) {
                                        itemRecord.setItemTypeId(itemTypeRecord.getItemTypeId());
                                        itemRecord.setItemTypeRecord(itemTypeRecord);
                                    }

                                    if (profileName.equalsIgnoreCase("BibForInvoiceYBP")) {
                                        String callNumberTypeForItem = getStringValueFromJsonObject(itemJsonObject, "callNumberType");
                                        String copyNumber = getStringValueFromJsonObject(itemJsonObject, "copyNumber");
                                        CallNumberTypeRecord callNumberTypeRecordForItem = fetchCallNumberTypeRecordByName(callNumberTypeForItem);
                                        if (null != callNumberTypeRecordForItem) {
                                            itemRecord.setCallNumberTypeId(callNumberTypeRecordForItem.getCallNumberTypeId());
                                            itemRecord.setCallNumberTypeRecord(callNumberTypeRecordForItem);
                                        }

                                        itemRecord.setCopyNumber(copyNumber);
                                    }

                                    itemRecord.setUpdatedBy(updatedBy);
                                    itemRecord.setUpdatedDate(updatedDate);
                                    ItemRecord updatedItemRecord = itemDAO.save(itemRecord);
                                    solrInputDocumentMap = getItemIndexer().getInputDocumentForItem(itemRecord, solrInputDocumentMap);
                                }
                            }
                        }
                    }
                    responseJsonArray.put(responseObject);
                } else {
                    // TODO : need to handle if bib record is not found
                }
            }
            List<SolrInputDocument> solrInputDocuments = getBibIndexer().getSolrInputDocumentListFromMap(solrInputDocumentMap);
            getBibIndexer().commitDocumentToSolr(solrInputDocuments);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return responseJsonArray.toString();
    }

    private String doCustomProcessForProfile(String marcContent,String profileName,String bibId) {
        MarcXMLConverter marcXMLConverter = new MarcXMLConverter();
        List<Record> records = marcXMLConverter.convertRawMarchToMarc(marcContent);
        if(CollectionUtils.isNotEmpty(records)) {
            for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
                Record record = iterator.next();
                if(StringUtils.isNotBlank(profileName) && profileName.equalsIgnoreCase("BibForInvoiceCasalini")) {
                    // TODO : process For Casalini
                    processCasaliniProfile(record,bibId);
                } else if(StringUtils.isNotBlank(profileName) && profileName.equalsIgnoreCase("BibForInvoiceYBP")) {
                    // TODO : process For YBP
                    processYBPProfile(record,bibId);
                }
                return marcXMLConverter.generateMARCXMLContent(record);
            }
        }
        return marcContent;
    }

    private void processYBPProfile(Record record, String bibId) {
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
        getMarcRecordUtil().updateControlField(record,"001", bibId);

    }

    private void processCasaliniProfile(Record record, String bibId) {
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

        //update 001 value by bibId
        getMarcRecordUtil().updateControlField(record,"001", bibId);
    }

    private Timestamp getDateTimeStamp(String updatedDateString) {
        Timestamp timeStamp = null;
        try {
            Date parse = DocstoreConstants.DOCSTORE_DATE_FORMAT.parse(updatedDateString);
            if (null != parse) {
                timeStamp = new Timestamp(parse.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    public String getStringValueFromJsonObject(JSONObject jsonObject, String key) {
        String returnValue = null;
        try {
            returnValue = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}
