package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;

/**
 * Created by SheikS on 12/20/2015.
 */
public class CallNumberPrefixHandler extends ItemOverlayHandler {

    private final String TYPE = "Call Number Prefix";
    @Override
    public boolean isInterested(JSONObject jsonObject) {
        return jsonObject.has(TYPE);
    }

    @Override
    public boolean isMatching(ItemRecord itemRecord, JSONObject jsonObject) {
        String callNumberPrefix = getStringValueFromJsonObject(jsonObject,TYPE);
        return StringUtils.equals(itemRecord.getCallNumberPrefix(),callNumberPrefix);
    }

    @Override
    public ItemRecord process(ItemRecord itemRecord, JSONObject jsonObject) {
        String callNumberPrefix = getStringValueFromJsonObject(jsonObject,TYPE);
        itemRecord.setCallNumberPrefix(callNumberPrefix);
        return itemRecord;
    }
}
