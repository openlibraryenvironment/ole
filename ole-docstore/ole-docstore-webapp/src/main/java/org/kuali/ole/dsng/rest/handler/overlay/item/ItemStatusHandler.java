
package org.kuali.ole.dsng.rest.handler.overlay.item;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatusRecord;

/**
 * Created by SheikS on 12/20/2015.
 */
public class ItemStatusHandler extends ItemOverlayHandler {
    private final String TYPE = "Item Status";

    @Override
    public boolean isInterested(JSONObject jsonObject) {
        return jsonObject.has(TYPE);
    }

    @Override
    public boolean isMatching(ItemRecord itemRecord, JSONObject jsonObject) {
        String itemStatusName = getStringValueFromJsonObject(jsonObject, "itemStatus");
        return StringUtils.equals(itemRecord.getItemStatusRecord().getCode(),itemStatusName);
    }
}
