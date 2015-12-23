package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;

/**
 * Created by SheikS on 12/20/2015.
 */
public class ChronologyHandler extends ItemOverlayHandler {
    private final String TYPE = "Chronology";

    @Override
    public boolean isInterested(JSONObject jsonObject) {
        return jsonObject.has(TYPE);
    }

    @Override
    public boolean isMatching(ItemRecord itemRecord, JSONObject jsonObject) {
        String chronology = getStringValueFromJsonObject(jsonObject,TYPE);
        return StringUtils.equals(itemRecord.getChronology(),chronology);
    }

    @Override
    public ItemRecord process(ItemRecord itemRecord, JSONObject jsonObject) {
        String chronology = getStringValueFromJsonObject(jsonObject,TYPE);
        itemRecord.setChronology(chronology);
        return itemRecord;
    }
}
