package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
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
        ItemRecord itemRecord = (ItemRecord) exchange.get("itemRecord");
        List<ItemStatisticalSearchRecord> itemStatisticalSearchRecords = itemRecord.getItemStatisticalSearchRecords();
        if(CollectionUtils.isNotEmpty(itemStatisticalSearchRecords)) {
            for (Iterator<ItemStatisticalSearchRecord> iterator = itemStatisticalSearchRecords.iterator(); iterator.hasNext(); ) {
                ItemStatisticalSearchRecord itemStatisticalSearchRecord = iterator.next();
                if(null != itemStatisticalSearchRecord.getStatisticalSearchRecord() &&
                        StringUtils.equals(itemStatisticalSearchRecord.getStatisticalSearchRecord().getCode(),statisticalSearchCode)) {
                    exchange.add("matchedItem", itemRecord);
                }
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String statisticalSearchCode = getStringValueFromJsonObject(requestJsonObject,TYPE);
        ItemRecord itemRecord = (ItemRecord) exchange.get("itemRecord");

        StatisticalSearchRecord statisticalSearchRecord = new StatisticalSearchCodeUtil().fetchStatisticalSearchRecordByCode(statisticalSearchCode);
        if (null != statisticalSearchRecord) {
            List<ItemStatisticalSearchRecord> itemStatisticalSearchRecords = itemRecord.getItemStatisticalSearchRecords();
            if(CollectionUtils.isNotEmpty(itemStatisticalSearchRecords)) {
                for (Iterator<ItemStatisticalSearchRecord> iterator = itemStatisticalSearchRecords.iterator(); iterator.hasNext(); ) {
                    ItemStatisticalSearchRecord itemStatisticalSearchRecord = iterator.next();
                    itemStatisticalSearchRecord.setStatisticalSearchRecord(statisticalSearchRecord);
                }
            } else {
                itemStatisticalSearchRecords = new ArrayList<ItemStatisticalSearchRecord>();
                ItemStatisticalSearchRecord holdingsStatisticalSearchRecord = new ItemStatisticalSearchRecord();
                holdingsStatisticalSearchRecord.setStatisticalSearchId(statisticalSearchRecord.getStatisticalSearchId());
                holdingsStatisticalSearchRecord.setStatisticalSearchRecord(statisticalSearchRecord);
                holdingsStatisticalSearchRecord.setItemId(itemRecord.getItemId());
                holdingsStatisticalSearchRecord.setItemRecord(itemRecord);
                itemStatisticalSearchRecords.add(holdingsStatisticalSearchRecord);
            }
        }
        exchange.add("itemRecord", itemRecord);

    }
}
