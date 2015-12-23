package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;

/**
 * Created by SheikS on 12/20/2015.
 */
public class CopyNumberHandler extends ItemOverlayHandler {

    private final String TYPE = "Copy Number";

    @Override
    public boolean isInterested(JSONObject jsonObject) {
        return jsonObject.has(TYPE);
    }

    @Override
    public boolean isMatching(ItemRecord itemRecord, JSONObject jsonObject) {
        String copyNumber = getStringValueFromJsonObject(jsonObject, TYPE);
        return StringUtils.equals(itemRecord.getCopyNumber(),copyNumber);
    }

    @Override
    public ItemRecord process(ItemRecord itemRecord, JSONObject jsonObject) {
        String copyNumber = getStringValueFromJsonObject(jsonObject, TYPE);
        itemRecord.setCopyNumber(copyNumber);
        return itemRecord;
    }
}
