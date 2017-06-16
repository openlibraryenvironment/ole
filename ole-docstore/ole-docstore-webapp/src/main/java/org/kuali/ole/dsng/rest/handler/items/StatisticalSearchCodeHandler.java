package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatisticalSearchRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.StatisticalSearchRecord;
import org.kuali.ole.dsng.util.StatisticalSearchCodeUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/20/2015.
 */
public class StatisticalSearchCodeHandler extends ItemHandler {

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(OleNGConstants.BatchProcess.STATISTICAL_CODE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        String statisticalSearchCode = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.STATISTICAL_CODE);
        List<String> parsedValues = parseCommaSeperatedValues(statisticalSearchCode);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String statisticalSearchCodeValue = iterator.next();
            List<ItemStatisticalSearchRecord> itemStatisticalSearchRecords = itemRecord.getItemStatisticalSearchRecords();
            if(CollectionUtils.isNotEmpty(itemStatisticalSearchRecords)) {
                for (Iterator<ItemStatisticalSearchRecord> itemStatisticalSearchRecordIterator = itemStatisticalSearchRecords.iterator(); itemStatisticalSearchRecordIterator.hasNext(); ) {
                    ItemStatisticalSearchRecord itemStatisticalSearchRecord = itemStatisticalSearchRecordIterator.next();
                    if(null != itemStatisticalSearchRecord.getStatisticalSearchRecord() &&
                            StringUtils.equals(itemStatisticalSearchRecord.getStatisticalSearchRecord().getCode(),statisticalSearchCodeValue)) {
                        exchange.add(OleNGConstants.MATCHED_ITEM, Boolean.TRUE);
                        exchange.add(OleNGConstants.MATCHED_VALUE, statisticalSearchCodeValue);
                    }
                }
            }
        }

    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.STATISTICAL_CODE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            List<ItemStatisticalSearchRecord> itemStatisticalSearchRecords = itemRecord.getItemStatisticalSearchRecords();
            if (CollectionUtils.isNotEmpty(itemStatisticalSearchRecords)) {
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String statisticalSearchCode = iterator.next();
                    StatisticalSearchRecord statisticalSearchRecord = getOleDsNGMemorizeService().fetchStatisticalSearchRecordByCode(statisticalSearchCode);
                    for (Iterator<ItemStatisticalSearchRecord> itemStatisticalSearchRecordIterator = itemStatisticalSearchRecords.iterator(); itemStatisticalSearchRecordIterator.hasNext(); ) {
                        ItemStatisticalSearchRecord itemStatisticalSearchRecord = itemStatisticalSearchRecordIterator.next();
                        itemStatisticalSearchRecord.setStatisticalSearchId(statisticalSearchRecord.getStatisticalSearchId());
                        itemStatisticalSearchRecord.setStatisticalSearchRecord(statisticalSearchRecord);
                        itemStatisticalSearchRecord.setItemId(itemRecord.getItemId());
                        itemStatisticalSearchRecord.setItemRecord(itemRecord);
                    }
                }
                itemRecord.setItemStatisticalSearchRecords(itemStatisticalSearchRecords);
            } else {
                itemStatisticalSearchRecords = new ArrayList<ItemStatisticalSearchRecord>();
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String statisticalSearchCode = iterator.next();
                    StatisticalSearchRecord statisticalSearchRecord = getOleDsNGMemorizeService().fetchStatisticalSearchRecordByCode(statisticalSearchCode);
                    ItemStatisticalSearchRecord holdingsStatisticalSearchRecord = new ItemStatisticalSearchRecord();
                    holdingsStatisticalSearchRecord.setStatisticalSearchId(statisticalSearchRecord.getStatisticalSearchId());
                    holdingsStatisticalSearchRecord.setStatisticalSearchRecord(statisticalSearchRecord);
                    holdingsStatisticalSearchRecord.setItemId(itemRecord.getItemId());
                    holdingsStatisticalSearchRecord.setItemRecord(itemRecord);
                    itemStatisticalSearchRecords.add(holdingsStatisticalSearchRecord);

                }
                itemRecord.setItemStatisticalSearchRecords(itemStatisticalSearchRecords);
            }
        }
        exchange.add(OleNGConstants.ITEM_RECORD, itemRecord);

    }
}
