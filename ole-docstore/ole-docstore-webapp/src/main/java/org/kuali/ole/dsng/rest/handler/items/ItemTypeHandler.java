
package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemTypeRecord;

/**
 * Created by SheikS on 12/20/2015.
 */
public class ItemTypeHandler extends ItemOverlayHandler {
    private final String TYPE = "Item Type";

    @Override
    public boolean isInterested(JSONObject jsonObject) {
        return jsonObject.has(TYPE);
    }

    @Override
    public boolean isMatching(ItemRecord itemRecord, JSONObject jsonObject) {
        String itemTypeName = getStringValueFromJsonObject(jsonObject,TYPE);
        return (null != itemRecord.getItemTypeRecord() &&
                StringUtils.equals(itemRecord.getItemTypeRecord().getName(),itemTypeName));
    }

    @Override
    public ItemRecord process(ItemRecord itemRecord, JSONObject jsonObject) {
        String itemTypeName = getStringValueFromJsonObject(jsonObject,TYPE);
        ItemTypeRecord itemTypeRecord = fetchItemTypeByName(itemTypeName);
        if(null != itemTypeRecord) {
            itemRecord.setItemTypeId(itemTypeRecord.getItemTypeId());
            itemRecord.setItemTypeRecord(itemTypeRecord);
        }
        return itemRecord;
    }
}
