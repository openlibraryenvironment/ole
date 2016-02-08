package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.model.HoldingsRecordAndDataMapping;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by pvsubrah on 12/23/15.
 */
public class UpdateHoldingsHandler extends Handler {
    protected List<HoldingsHandler> holdingMetaDataHandlers;

    public List<HoldingsHandler> getHoldingMetaDataHandlers() {
        if (null == holdingMetaDataHandlers) {
            holdingMetaDataHandlers = new ArrayList<HoldingsHandler>();
            holdingMetaDataHandlers.add(new HoldingsLocationHandler());
            holdingMetaDataHandlers.add(new CallNumberHandler());
            holdingMetaDataHandlers.add(new CallNumberTypeHandler());
            holdingMetaDataHandlers.add(new CallNumberPrefixHandler());
            holdingMetaDataHandlers.add(new CopyNumberHandler());
            holdingMetaDataHandlers.add(new AccessStatusHandler());
            holdingMetaDataHandlers.add(new SubscriptionStatusHandler());
            holdingMetaDataHandlers.add(new HoldingsStaffOnlyHandler());
        }
        return holdingMetaDataHandlers;
    }

    public void setHoldingMetaDataHandlers(List<HoldingsHandler> holdingMetaDataHandlers) {
        this.holdingMetaDataHandlers = holdingMetaDataHandlers;
    }

    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getListFromJSONArray(operation);
        for (Iterator iterator = operationsList.iterator(); iterator.hasNext(); ) {
            String op = (String) iterator.next();
            if (op.equals("122") || op.equals("222")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {

        List<HoldingsRecordAndDataMapping> holdingsRecordAndDataMappings = (List<HoldingsRecordAndDataMapping>) exchange.get(OleNGConstants.HOLDINGS_FOR_UPDATE);
        List<HoldingsRecord> holdingsRecords = new ArrayList<HoldingsRecord>();
        if (CollectionUtils.isNotEmpty(holdingsRecordAndDataMappings)) {
            try {
                String updatedBy = requestJsonObject.getString(OleNGConstants.UPDATED_BY);
                String updatedDateString = (String) requestJsonObject.get(OleNGConstants.UPDATED_DATE);
                Timestamp updatedDate = getDateTimeStamp(updatedDateString);

                for (Iterator<HoldingsRecordAndDataMapping> iterator = holdingsRecordAndDataMappings.iterator(); iterator.hasNext(); ) {
                    HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = iterator.next();
                    HoldingsRecord holdingsRecord = holdingsRecordAndDataMapping.getHoldingsRecord();
                    holdingsRecord.setUpdatedDate(updatedDate);
                    holdingsRecord.setUpdatedBy(updatedBy);
                    exchange.add(OleNGConstants.HOLDINGS_RECORD,holdingsRecord);
                    JSONObject dataMappingByValue = holdingsRecordAndDataMapping.getDataMapping();
                    if(null != dataMappingByValue) {
                        processOverlay(exchange, holdingsRecord, dataMappingByValue);
                        holdingsRecords.add(holdingsRecord);

                        processIfDeleteAllExistOpsFound(holdingsRecord,requestJsonObject);

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            exchange.remove(OleNGConstants.HOLDINGS_RECORD);
            getHoldingDAO().saveAll(holdingsRecords);
        }
    }

    private HoldingsRecord processOverlay(Exchange exchange, HoldingsRecord holdingsRecord, JSONObject dataMapping) throws JSONException, IOException {
        Map<String, Object> dataMappingsMap = new ObjectMapper().readValue(dataMapping.toString(), new TypeReference<Map<String, Object>>() {});
        for (Iterator iterator3 = dataMappingsMap.keySet().iterator(); iterator3.hasNext(); ) {
            String key1 = (String) iterator3.next();
            for (Iterator<HoldingsHandler> iterator4 = getHoldingMetaDataHandlers().iterator(); iterator4.hasNext(); ) {
                HoldingsHandler holdingsMetaDataHandlelr1 = iterator4.next();
                if (holdingsMetaDataHandlelr1.isInterested(key1)) {
                    holdingsMetaDataHandlelr1.setBusinessObjectService(getBusinessObjectService());
                    holdingsMetaDataHandlelr1.processDataMappings(dataMapping, exchange);
                }
            }
        }
        exchange.remove(OleNGConstants.MATCHED_HOLDINGS);
        return  holdingsRecord;
    }



    public void processIfDeleteAllExistOpsFound(HoldingsRecord holdingsRecord, JSONObject requestJsonObject) {
        ArrayList<ItemRecord> holdingsListToDelete = getListOfItemsToDelete(holdingsRecord, requestJsonObject);

        if (CollectionUtils.isNotEmpty(holdingsListToDelete)) {

            getBusinessObjectService().delete(holdingsListToDelete);

            StringBuilder itemIdsString = new StringBuilder();
            for (Iterator<ItemRecord> iterator = holdingsListToDelete.iterator(); iterator.hasNext(); ) {
                ItemRecord itemRecord = iterator.next();
                String itemId = itemRecord.getItemId();
                itemIdsString.append(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML + "-" + itemId);
                if(iterator.hasNext()) {
                    itemIdsString.append(" OR ");
                }
            }
            if(StringUtils.isNotBlank(itemIdsString.toString())) {
                String deleteQuery = "id:(" + itemIdsString + ")";
                getSolrRequestReponseHandler().deleteFromSolr(deleteQuery);
            }
        }
    }

    private ArrayList<ItemRecord> getListOfItemsToDelete(HoldingsRecord holdingsRecord, JSONObject requestJsonObject) {
        ArrayList<ItemRecord> itemListToDelete = new ArrayList<ItemRecord>();
        String addedOpsValue = getAddedOpsValue(requestJsonObject, OleNGConstants.ITEM);
        if(StringUtils.isNotBlank(addedOpsValue) && addedOpsValue.equalsIgnoreCase(OleNGConstants.DELETE_ALL_EXISTING_AND_ADD)) {
            List<ItemRecord> itemRecords = holdingsRecord.getItemRecords();
            if (CollectionUtils.isNotEmpty(itemRecords)) {
                itemListToDelete.addAll(itemRecords);
            }
            holdingsRecord.setItemRecords(new ArrayList<ItemRecord>());
        }
        return itemListToDelete;
    }


    private String getAddedOpsValue(JSONObject jsonObject, String docType) {
        JSONObject addedOps = getJSONObjectFromJSONObject(jsonObject, OleNGConstants.ADDED_OPS);
        return getStringValueFromJsonObject(addedOps,docType);
    }
}
