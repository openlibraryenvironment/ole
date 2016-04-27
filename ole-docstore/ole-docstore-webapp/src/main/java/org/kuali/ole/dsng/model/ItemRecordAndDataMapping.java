package org.kuali.ole.dsng.model;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;

/**
 * Created by SheikS on 1/25/2016.
 */
public class ItemRecordAndDataMapping {
    private ItemRecord itemRecord;
    private JSONObject dataMapping;

    public ItemRecord getItemRecord() {
        return itemRecord;
    }

    public void setItemRecord(ItemRecord itemRecord) {
        this.itemRecord = itemRecord;
    }

    public JSONObject getDataMapping() {
        return dataMapping;
    }

    public void setDataMapping(JSONObject dataMapping) {
        this.dataMapping = dataMapping;
    }
}
