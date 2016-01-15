
package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemTypeRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.util.ItemUtil;

/**
 * Created by SheikS on 12/20/2015.
 */
public class ItemTypeHandler extends ItemHandler {
    private final String TYPE = "Item Type";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        String itemTypeName = getStringValueFromJsonObject(requestJsonObject, TYPE);
        if (null != itemRecord.getItemTypeRecord() &&
                StringUtils.equals(itemRecord.getItemTypeRecord().getName(),itemTypeName)) {
            exchange.add(OleNGConstants.MATCHED_ITEM, itemRecord);
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String itemTypeName = getStringValueFromJsonObject(requestJsonObject, TYPE);
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        ItemTypeRecord itemTypeRecord = new ItemUtil().fetchItemTypeByName(itemTypeName);
        if(null != itemTypeRecord) {
            itemRecord.setItemTypeId(itemTypeRecord.getItemTypeId());
            itemRecord.setItemTypeRecord(itemTypeRecord);
        }
        exchange.add(OleNGConstants.ITEM_RECORD, itemRecord);
    }
}
