package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatisticalSearchRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.StatisticalSearchRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.util.StatisticalSearchCodeUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/20/2015.
 */
public class StatisticalSearchCodeHandler extends ItemHandler {
    private final String TYPE = "Statistical Code";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        String statisticalSearchCode = getStringValueFromJsonObject(requestJsonObject,TYPE);
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        List<ItemStatisticalSearchRecord> itemStatisticalSearchRecords = itemRecord.getItemStatisticalSearchRecords();
        if(CollectionUtils.isNotEmpty(itemStatisticalSearchRecords)) {
            for (Iterator<ItemStatisticalSearchRecord> iterator = itemStatisticalSearchRecords.iterator(); iterator.hasNext(); ) {
                ItemStatisticalSearchRecord itemStatisticalSearchRecord = iterator.next();
                if(null != itemStatisticalSearchRecord.getStatisticalSearchRecord() &&
                        StringUtils.equals(itemStatisticalSearchRecord.getStatisticalSearchRecord().getCode(),statisticalSearchCode)) {
                    exchange.add(OleNGConstants.MATCHED_ITEM, Boolean.TRUE);
                }
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, TYPE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            List<ItemStatisticalSearchRecord> itemStatisticalSearchRecords = itemRecord.getItemStatisticalSearchRecords();
            if (CollectionUtils.isNotEmpty(itemStatisticalSearchRecords)) {
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String statisticalSearchCode = iterator.next();
                    StatisticalSearchRecord statisticalSearchRecord = new StatisticalSearchCodeUtil().fetchStatisticalSearchRecordByCode(statisticalSearchCode);
                    for (Iterator<ItemStatisticalSearchRecord> itemStatisticalSearchRecordIterator = itemStatisticalSearchRecords.iterator(); itemStatisticalSearchRecordIterator.hasNext(); ) {
                        ItemStatisticalSearchRecord itemStatisticalSearchRecord = itemStatisticalSearchRecordIterator.next();
                        itemStatisticalSearchRecord.setStatisticalSearchRecord(statisticalSearchRecord);

                    }

                }
            } else {
                itemStatisticalSearchRecords = new ArrayList<ItemStatisticalSearchRecord>();
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String statisticalSearchCode = iterator.next();
                    StatisticalSearchRecord statisticalSearchRecord = new StatisticalSearchCodeUtil().fetchStatisticalSearchRecordByCode(statisticalSearchCode);
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
