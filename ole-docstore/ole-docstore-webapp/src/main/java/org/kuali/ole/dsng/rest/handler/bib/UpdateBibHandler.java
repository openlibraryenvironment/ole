package org.kuali.ole.dsng.rest.handler.bib;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.EHoldings;
import org.kuali.ole.docstore.common.document.PHoldings;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 12/23/15.
 */
public class UpdateBibHandler extends BibHandler {
    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getListFromJSONArray(operation);
        for (Iterator iterator = operationsList.iterator(); iterator.hasNext(); ) {
            String op = (String) iterator.next();
            if(op.equals("112") || op.equals("212")){
                return true;
            }
        }
        return false;
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        try {
            String newBibContent = requestJsonObject.getString(OleNGConstants.MODIFIED_CONTENT);
            String updatedBy = requestJsonObject.getString(OleNGConstants.UPDATED_BY);
            String updatedDateString = (String) requestJsonObject.get(OleNGConstants.UPDATED_DATE);

            if (requestJsonObject.has(OleNGConstants.ID)) {
                String bibId = requestJsonObject.getString(OleNGConstants.ID);
                BibRecord bibRecord = getBibDAO().retrieveBibById(bibId);
                bibRecord.setStatusUpdatedBy(updatedBy);
                bibRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC);

                Timestamp updatedDate = getDateTimeStamp(updatedDateString);

                bibRecord.setStatusUpdatedDate(updatedDate);

                String newContent = process001And003(newBibContent, bibId);

                newContent = processFieldOptions(bibRecord.getContent(),newContent,requestJsonObject);

                bibRecord.setContent(newContent);
                exchange.add(OleNGConstants.BIB, bibRecord);
                bibRecord = setDataMappingValues(bibRecord,requestJsonObject,exchange);

                processIfDeleteAllExistOpsFound(bibRecord, requestJsonObject);

                getBibDAO().save(bibRecord);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void processIfDeleteAllExistOpsFound(BibRecord bibRecord, JSONObject requestJsonObject) {
        ArrayList<HoldingsRecord> holdingsListToDelete = new ArrayList<HoldingsRecord>();

        ArrayList<HoldingsRecord> listOfHoldingsToDelete = getListOfHoldingsToDelete(bibRecord, requestJsonObject);
        holdingsListToDelete.addAll(listOfHoldingsToDelete);

        ArrayList<HoldingsRecord> listOfEHoldingsToDelete = getListOfEHoldingsToDelete(bibRecord, requestJsonObject);
        holdingsListToDelete.addAll(listOfEHoldingsToDelete);

        if (CollectionUtils.isNotEmpty(holdingsListToDelete)) {
            getBusinessObjectService().delete(holdingsListToDelete);
            StringBuilder holdingIdsString = new StringBuilder();
            for (Iterator<HoldingsRecord> iterator = holdingsListToDelete.iterator(); iterator.hasNext(); ) {
                HoldingsRecord holdingsRecord = iterator.next();
                String holdingsId = holdingsRecord.getHoldingsId();
                holdingIdsString.append(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML + "-" + holdingsId);
                if(iterator.hasNext()) {
                    holdingIdsString.append(" OR ");
                }
            }
            if(StringUtils.isNotBlank(holdingIdsString.toString())) {
                String deleteQuery = "id:(" + holdingIdsString + ")";
                getSolrRequestReponseHandler().deleteFromSolr(deleteQuery);
            }
        }
    }

    private ArrayList<HoldingsRecord> getListOfHoldingsToDelete(BibRecord bibRecord, JSONObject requestJsonObject) {
        String addedOpsValue = getAddedOpsValue(requestJsonObject, OleNGConstants.HOLDINGS);
        return filterHoldingsOrEholdingsRecordsToDelete(bibRecord,addedOpsValue,PHoldings.PRINT);
    }


    private ArrayList<HoldingsRecord> getListOfEHoldingsToDelete(BibRecord bibRecord, JSONObject requestJsonObject) {
        String addedOpsValue = getAddedOpsValue(requestJsonObject, OleNGConstants.EHOLDINGS);
        return filterHoldingsOrEholdingsRecordsToDelete(bibRecord,addedOpsValue,EHoldings.ELECTRONIC);
    }

    private ArrayList<HoldingsRecord> filterHoldingsOrEholdingsRecordsToDelete(BibRecord bibRecord, String addedOpsValue, String type) {
        ArrayList<HoldingsRecord> holdingsListToDelete = new ArrayList<HoldingsRecord>();
        if(StringUtils.isNotBlank(addedOpsValue) && addedOpsValue.equalsIgnoreCase(OleNGConstants.DELETE_ALL_EXISTING_AND_ADD)) {
            ArrayList<HoldingsRecord> finalHoldingsListForRetain = new ArrayList<HoldingsRecord>();
            List<HoldingsRecord> holdingsRecords = bibRecord.getHoldingsRecords();
            for (Iterator<HoldingsRecord> iterator = holdingsRecords.iterator(); iterator.hasNext(); ) {
                HoldingsRecord holdingsRecord = iterator.next();
                if(holdingsRecord.getHoldingsType().equalsIgnoreCase(type)) {
                    holdingsListToDelete.add(holdingsRecord);
                } else {
                    finalHoldingsListForRetain.add(holdingsRecord);
                }
            }
            bibRecord.setHoldingsRecords(finalHoldingsListForRetain);
        }
        return holdingsListToDelete;
    }


    private String getAddedOpsValue(JSONObject jsonObject, String docType) {
        JSONObject addedOps = getJSONObjectFromJSONObject(jsonObject, OleNGConstants.ADDED_OPS);
        return getStringValueFromJsonObject(addedOps,docType);
    }

    private String processFieldOptions(String oldMarcContent,String newMarcContent, JSONObject requestJSON) {
        List<Record> records = getMarcRecordUtil().convertMarcXmlContentToMarcRecord(newMarcContent);
        if(CollectionUtils.isNotEmpty(records)) {
            Record record = records.get(0);
            JSONArray fieldOps = getJSONArrayeFromJsonObject(requestJSON, OleNGConstants.FIELD_OPS);
            if(null != fieldOps) {
                for(int index = 0; index < fieldOps.length(); index++) {
                    JSONObject field = getJSONObjectFromJsonArray(fieldOps, index);
                    List<VariableField> matchedDataFields = getDataFieldBasedOnFieldOps(oldMarcContent, field);
                    if (CollectionUtils.isNotEmpty(matchedDataFields)) {
                        for (Iterator<VariableField> iterator = matchedDataFields.iterator(); iterator.hasNext(); ) {
                            VariableField variableField = iterator.next();
                            getMarcRecordUtil().addVariableFieldToRecord(record,variableField);
                        }
                    }

                }
            }
            return getMarcRecordUtil().convertMarcRecordToMarcContent(record);
        }
        return newMarcContent;

    }

    public List<VariableField> getDataFieldBasedOnFieldOps(String oldMarcContent, JSONObject fieldOption) {
        List<Record> records = getMarcRecordUtil().convertMarcXmlContentToMarcRecord(oldMarcContent);
        if(CollectionUtils.isNotEmpty(records)) {
            Record record = records.get(0);
            String dataField = getStringValueFromJsonObject(fieldOption,OleNGConstants.DATA_FIELD);
            String ind1 = getStringValueFromJsonObject(fieldOption,OleNGConstants.IND1);
            String ind2 = getStringValueFromJsonObject(fieldOption,OleNGConstants.IND2);
            String subfield = getStringValueFromJsonObject(fieldOption,OleNGConstants.SUBFIELD);
            String value = getStringValueFromJsonObject(fieldOption,OleNGConstants.VALUE);

            List<VariableField> dataFields = record.getVariableFields(dataField);

            if (CollectionUtils.isNotEmpty(dataFields)) {
                if(StringUtils.isBlank(ind1) && StringUtils.isBlank(ind2) && StringUtils.isBlank(subfield)){
                    return dataFields;
                }

                List<VariableField> fieldsToReturn = new ArrayList<VariableField>();

                for (Iterator<VariableField> iterator = dataFields.iterator(); iterator.hasNext(); ) {
                    DataField field = (DataField) iterator.next();
                    boolean matched = isMatched(field, ind1, ind2, subfield, value);
                    if(matched) {
                        fieldsToReturn.add(field);
                    }
                }

                if(CollectionUtils.isNotEmpty(fieldsToReturn)){
                    return fieldsToReturn;
                }
            }
        }
        return null;
    }

    private boolean isMatched( DataField field, String ind1, String ind2, String subfield, String value) {
        boolean matchedDataField = true;
        if (StringUtils.isNotBlank(ind1)) {
            matchedDataField &= ind1.charAt(0) == field.getIndicator1();
        }
        if (matchedDataField && StringUtils.isNotBlank(ind2)) {
            matchedDataField &= ind2.charAt(0) == field.getIndicator2();
        }

        if (matchedDataField && StringUtils.isNotBlank(subfield)) {
            for (Iterator<Subfield> variableFieldIterator = field.getSubfields().iterator(); variableFieldIterator.hasNext(); ) {
                Subfield sf = variableFieldIterator.next();
                char subFieldChar = (StringUtils.isNotBlank(subfield) ? subfield.charAt(0) : ' ');
                if(subFieldChar == sf.getCode()) {
                    String data = sf.getData();
                    if (StringUtils.isNotBlank(value)) {
                        if(StringUtils.equals(data,value)){
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        } else if(matchedDataField && StringUtils.isBlank(subfield)) {
            return true;
        }
        return false;
    }
}
