package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatusRecord;
import org.kuali.ole.utility.OleNgUtil;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/20/2015.
 */
public class ItemStatusHandler extends ItemHandler {

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(OleNGConstants.BatchProcess.ITEM_STATUS);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        String itemStatusName = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.ITEM_STATUS);
        List<String> parsedValues = parseCommaSeperatedValues(itemStatusName);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String itemStatusNameValue = iterator.next();
            if (null != itemRecord.getItemStatusRecord() &&
                    StringUtils.equals(itemRecord.getItemStatusRecord().getName(),itemStatusNameValue)) {
                exchange.add(OleNGConstants.MATCHED_ITEM, Boolean.TRUE);
                exchange.add(OleNGConstants.MATCHED_VALUE, itemStatusName);
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.ITEM_STATUS);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            String itemStatusCode = listFromJSONArray.get(0);
            ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
            ItemStatusRecord itemStatusRecord = getOleDsNGMemorizeService().fetchItemStatusByName(itemStatusCode);
            if(null != itemStatusRecord) {
                itemRecord.setItemStatusId(itemStatusRecord.getItemStatusId());
                itemRecord.setItemStatusRecord(itemStatusRecord);
            } else {
                new OleNgUtil().addValidationErrorMessageToExchange(exchange, "Invalid Item Status : " + itemStatusCode);
            }
            exchange.add(OleNGConstants.ITEM_RECORD, itemRecord);
        }
    }
}
