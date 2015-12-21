package org.kuali.ole.dsng.rest.handler.overlay.item;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;

/**
 * Created by SheikS on 12/20/2015.
 */
public class CallNumberHandler extends ItemOverlayHandler {
    private final String TYPE = "Call Number";

    @Override
    public boolean isInterested(JSONObject jsonObject) {
        return jsonObject.has(TYPE);
    }

    @Override
    public boolean isMatching(ItemRecord itemRecord, JSONObject jsonObject) {
        String callNumber = getStringValueFromJsonObject(jsonObject,TYPE);
        return StringUtils.equals(itemRecord.getCallNumber(),callNumber);
    }
}
