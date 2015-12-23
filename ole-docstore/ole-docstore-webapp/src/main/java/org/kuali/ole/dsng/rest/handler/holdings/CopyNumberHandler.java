package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;

/**
 * Created by SheikS on 12/20/2015.
 */
public class CopyNumberHandler extends HoldingsOverlayHandler {

    private final String TYPE = "Copy Number";

    @Override
    public boolean isInterested(JSONObject jsonObject) {
        return jsonObject.has(TYPE);
    }

    @Override
    public boolean isMatching(HoldingsRecord holdingsRecord, JSONObject jsonObject) {
        String copyNumber = getStringValueFromJsonObject(jsonObject, TYPE);
        return StringUtils.equals(holdingsRecord.getCopyNumber(),copyNumber);
    }

    @Override
    public HoldingsRecord process(HoldingsRecord holdingsRecord, JSONObject jsonObject) {
        String copyNumber = getStringValueFromJsonObject(jsonObject, TYPE);
        holdingsRecord.setCopyNumber(copyNumber);
        return holdingsRecord;
    }
}
