package org.kuali.ole.dsng.rest.handler.overlay.holdings;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;

/**
 * Created by SheikS on 12/20/2015.
 */
public class CallNumberPrefixHandler extends HoldingsOverlayHandler {

    private final String TYPE = "Call Number Prefix";
    @Override
    public boolean isInterested(JSONObject jsonObject) {
        return jsonObject.has(TYPE);
    }

    @Override
    public boolean isMatching(HoldingsRecord holdingsRecord, JSONObject jsonObject) {
        String callNumberPrefix = getStringValueFromJsonObject(jsonObject,TYPE);
        return StringUtils.equals(holdingsRecord.getCallNumberPrefix(),callNumberPrefix);
    }

    @Override
    public HoldingsRecord process(HoldingsRecord holdingsRecord, JSONObject jsonObject) {
        String callNumberPrefix = getStringValueFromJsonObject(jsonObject,TYPE);
        holdingsRecord.setCallNumberPrefix(callNumberPrefix);
        return holdingsRecord;
    }
}
