package org.kuali.ole.dsng.rest.handler.bib;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.dsng.rest.handler.AdditionalOverlayOpsHandler;
import org.marc4j.marc.Record;
import org.marc4j.marc.VariableField;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by pvsubrah on 12/23/15.
 */
public class UpdateBibHandler extends BibHandler {

    public List<AdditionalOverlayOpsHandler> additionalOverlayOpsHandlers;

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
            Record record = isValidLeader(newBibContent);
            if(record!=null) {
                String updatedBy = requestJsonObject.getString(OleNGConstants.UPDATED_BY);
                String updatedDateString = (String) requestJsonObject.get(OleNGConstants.UPDATED_DATE);

                if (requestJsonObject.has(OleNGConstants.ID)) {
                    String bibId = requestJsonObject.getString(OleNGConstants.ID);
                    BibRecord bibRecord = getOleDsNGMemorizeService().getBibDAO().retrieveBibById(bibId);

                    boolean validForOverlay = isValidForOverlay(bibRecord, requestJsonObject);

                    if (validForOverlay) {
                        bibRecord.setUpdatedBy(updatedBy);
                        bibRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC);

                        Timestamp updatedDate = getDateTimeStamp(updatedDateString);

                        bibRecord.setDateEntered(updatedDate);
                        if (bibRecord.getStaffOnlyFlag() == null) {
                            bibRecord.setStaffOnlyFlag(false);
                        }
                        String newContent = process001And003(record, bibId);

                        newContent = processFieldOptions(bibRecord.getContent(), newContent, requestJsonObject);

                        bibRecord.setContent(newContent);
                        exchange.add(OleNGConstants.BIB, bibRecord);
                        bibRecord = setDataMappingValues(bibRecord, requestJsonObject, exchange);

                        Boolean statusUpdated = (Boolean) exchange.get(OleNGConstants.BIB_STATUS_UPDATED);
                        if (null != statusUpdated && statusUpdated == Boolean.TRUE) {
                            bibRecord.setStatusUpdatedBy(updatedBy);
                            bibRecord.setStatusUpdatedDate(updatedDate);
                        }

                        processIfDeleteAllExistOpsFound(bibRecord, requestJsonObject, exchange);

                        getOleDsNGMemorizeService().getBibDAO().save(bibRecord);
                        bibRecord.setOperationType(OleNGConstants.UPDATED);

                        saveBibInfoRecord(bibRecord, false);
                    }else {
                        Set<String> discardedBibIdsForAdditionalOps = (Set<String>) exchange.get(OleNGConstants.DISCARDED_BIB_FOR_ADDITIONAL_OVERLAY_OPS);
                        if (null == discardedBibIdsForAdditionalOps) {
                            discardedBibIdsForAdditionalOps = new HashSet<String>();
                        }
                        discardedBibIdsForAdditionalOps.add(bibRecord.getBibId());
                        exchange.add(OleNGConstants.DISCARDED_BIB_FOR_ADDITIONAL_OVERLAY_OPS, discardedBibIdsForAdditionalOps);
                    }
                }
            }else{
                try{
                    isValidLeaderCheck(newBibContent);
                }
                catch(Exception e){
                    e.printStackTrace();
                    addFailureReportToExchange(requestJsonObject, exchange, "bib", e, null);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            addFailureReportToExchange(requestJsonObject, exchange, "bib", e, null);
        }

    }

    private boolean isValidForOverlay(BibRecord bibRecord, JSONObject requestJsonObject) {
        boolean isValid = true;
        if(null != bibRecord) {
            JSONObject additionalOverlayOps = getJSONObjectFromJSONObject(requestJsonObject, OleNGConstants.ADDITIONAL_OVERLAY_OPS);
            if(null !=  additionalOverlayOps && additionalOverlayOps.has(OleNGConstants.BIB)) {
                JSONObject bibAdditionalOverlayOps = getJSONObjectFromJSONObject(additionalOverlayOps, OleNGConstants.BIB);
                String where = getStringValueFromJsonObject(bibAdditionalOverlayOps, OleNGConstants.WHERE);
                String condition = getStringValueFromJsonObject(bibAdditionalOverlayOps, OleNGConstants.CONDITION);
                String value = getStringValueFromJsonObject(bibAdditionalOverlayOps, OleNGConstants.VALUE);
                for (Iterator<AdditionalOverlayOpsHandler> iterator = getAdditionalOverlayOpsHandlers().iterator(); iterator.hasNext(); ) {
                    AdditionalOverlayOpsHandler additionalOverlayOpsHandler = iterator.next();
                    List<String> values = getListFromJSONArray(value);
                    if(additionalOverlayOpsHandler.isInterested(where) && CollectionUtils.isNotEmpty(values)) {
                        isValid = isValid & additionalOverlayOpsHandler.isValid(condition, values, bibRecord);
                    }
                }
            }
        }
        return isValid;
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
            String dataField = getStringValueFromJsonObject(fieldOption, OleNGConstants.DATA_FIELD);
            String ind1 = getStringValueFromJsonObject(fieldOption, OleNGConstants.IND1);
            String ind2 = getStringValueFromJsonObject(fieldOption, OleNGConstants.IND2);
            String subfield = getStringValueFromJsonObject(fieldOption, OleNGConstants.SUBFIELD);
            String value = getStringValueFromJsonObject(fieldOption, OleNGConstants.VALUE);
            Boolean ignoreGPF = getBooleanValueFromJsonObject(fieldOption, OleNGConstants.IGNORE_GPF);

            if (null != ignoreGPF && ignoreGPF == Boolean.FALSE) {
                List<VariableField> dataFields = record.getVariableFields(dataField);
                return getMarcRecordUtil().getMatchedDataFields(ind1, ind2, subfield, value, dataFields);
            }
        }
        return null;
    }

    public List<AdditionalOverlayOpsHandler> getAdditionalOverlayOpsHandlers() {
        if(null == additionalOverlayOpsHandlers) {
            additionalOverlayOpsHandlers = new ArrayList<AdditionalOverlayOpsHandler>();
            additionalOverlayOpsHandlers.add(new BibStatusOverlayOpsHandler());
        }
        return additionalOverlayOpsHandlers;
    }

    public void setAdditionalOverlayOpsHandlers(List<AdditionalOverlayOpsHandler> additionalOverlayOpsHandlers) {
        this.additionalOverlayOpsHandlers = additionalOverlayOpsHandlers;
    }
}
