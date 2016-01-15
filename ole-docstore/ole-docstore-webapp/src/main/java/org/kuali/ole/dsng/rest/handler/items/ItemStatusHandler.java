
package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatusRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.util.ItemUtil;

/**
 * Created by SheikS on 12/20/2015.
 */
public class ItemStatusHandler extends ItemHandler {
    private final String TYPE = "Item Status";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        String itemStatusName = getStringValueFromJsonObject(requestJsonObject, TYPE);
        if (null != itemRecord.getItemStatusRecord() &&
                StringUtils.equals(itemRecord.getItemStatusRecord().getName(),itemStatusName)) {
            exchange.add(OleNGConstants.MATCHED_ITEM, itemRecord);
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String itemStatusName = getStringValueFromJsonObject(requestJsonObject, TYPE);
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        ItemStatusRecord itemStatusRecord = new ItemUtil().fetchItemStatusByName(itemStatusName);
        if(null != itemStatusRecord) {
            itemRecord.setItemStatusId(itemStatusRecord.getItemStatusId());
            itemRecord.setItemStatusRecord(itemStatusRecord);
        }
        exchange.add(OleNGConstants.ITEM_RECORD, itemRecord);
    }
}
