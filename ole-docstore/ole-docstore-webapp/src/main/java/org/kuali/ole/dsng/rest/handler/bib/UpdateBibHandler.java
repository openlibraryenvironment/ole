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
                bibRecord.setUpdatedBy(updatedBy);
                bibRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC);

                Timestamp updatedDate = getDateTimeStamp(updatedDateString);

                bibRecord.setDateEntered(updatedDate);

                String newContent = process001And003(newBibContent, bibId);

                newContent = processFieldOptions(bibRecord.getContent(),newContent,requestJsonObject);

                bibRecord.setContent(newContent);
                exchange.add(OleNGConstants.BIB, bibRecord);
                bibRecord = setDataMappingValues(bibRecord,requestJsonObject,exchange);

                Boolean statusUpdated = (Boolean) exchange.get(OleNGConstants.BIB_STATUS_UPDATED);
                if(null != statusUpdated && statusUpdated == Boolean.TRUE) {
                    bibRecord.setStatusUpdatedBy(updatedBy);
                    bibRecord.setStatusUpdatedDate(updatedDate);
                }

                processIfDeleteAllExistOpsFound(bibRecord, requestJsonObject);

                getBibDAO().save(bibRecord);

                saveBibInfoRecord(bibRecord,false);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
            Boolean ignoreGPF = getBooleanValueFromJsonObject(fieldOption,OleNGConstants.IGNORE_GPF);

            if (null != ignoreGPF && ignoreGPF == Boolean.FALSE) {
                List<VariableField> dataFields = record.getVariableFields(dataField);
                return getMarcRecordUtil().getMatchedDataFields(ind1, ind2, subfield, value, dataFields);
            }
        }
        return null;
    }
}
