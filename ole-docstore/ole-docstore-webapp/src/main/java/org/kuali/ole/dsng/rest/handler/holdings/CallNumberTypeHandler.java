package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.CallNumberTypeRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;

/**
 * Created by SheikS on 12/20/2015.
 */
public class CallNumberTypeHandler extends HoldingsOverlayHandler {

    private final String TYPE = "Call Number Type";

    @Override
    public boolean isInterested(JSONObject jsonObject) {
        return jsonObject.has(TYPE);
    }

    @Override
    public boolean isMatching(HoldingsRecord holdingsRecord, JSONObject jsonObject) {
        String callNumber = getStringValueFromJsonObject(jsonObject, TYPE);
        return (null != holdingsRecord.getCallNumberTypeRecord() &&
                StringUtils.equals(holdingsRecord.getCallNumberTypeRecord().getCode(),callNumber));
    }

    @Override
    public HoldingsRecord process(HoldingsRecord holdingsRecord, JSONObject jsonObject) {
        String callNumber = getStringValueFromJsonObject(jsonObject, TYPE);
        CallNumberTypeRecord callNumberTypeRecord = fetchCallNumberTypeRecordById(callNumber);
        if (null != callNumberTypeRecord) {
            holdingsRecord.setCallNumberTypeId(callNumberTypeRecord.getCallNumberTypeId());
            holdingsRecord.setCallNumberTypeRecord(callNumberTypeRecord);
        }
        return holdingsRecord;
    }
}
