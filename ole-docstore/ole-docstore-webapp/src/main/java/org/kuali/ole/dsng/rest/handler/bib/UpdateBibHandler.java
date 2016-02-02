package org.kuali.ole.dsng.rest.handler.bib;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.EHoldings;
import org.kuali.ole.docstore.common.document.PHoldings;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;

import java.sql.Timestamp;
import java.util.ArrayList;
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
}
