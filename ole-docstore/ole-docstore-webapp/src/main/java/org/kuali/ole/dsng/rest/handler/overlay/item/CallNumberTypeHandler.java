package org.kuali.ole.dsng.rest.handler.overlay.item;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.CallNumberTypeRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;

/**
 * Created by SheikS on 12/20/2015.
 */
public class CallNumberTypeHandler extends ItemOverlayHandler {

    private final String TYPE = "Call Number Type";

    @Override
    public boolean isInterested(JSONObject jsonObject) {
        return jsonObject.has(TYPE);
    }

    @Override
    public boolean isMatching(ItemRecord itemRecord, JSONObject jsonObject) {
        String callNumberType = getStringValueFromJsonObject(jsonObject, TYPE);
        return (null != itemRecord.getCallNumberTypeRecord() &&
                StringUtils.equals(itemRecord.getCallNumberTypeRecord().getCallNumberTypeId(),itemRecord.getCallNumberTypeId()));
    }

    @Override
    public ItemRecord process(ItemRecord itemRecord, JSONObject jsonObject) {
        String callNumberType = getStringValueFromJsonObject(jsonObject, TYPE);
        CallNumberTypeRecord callNumberTypeRecord = fetchCallNumberTypeRecordById(callNumberType);
        if (null != callNumberTypeRecord) {
            itemRecord.setCallNumberTypeId(callNumberTypeRecord.getCallNumberTypeId());
            itemRecord.setCallNumberTypeRecord(callNumberTypeRecord);
        }
        return itemRecord;
    }
}
