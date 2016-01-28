package org.kuali.ole.dsng.model;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;

/**
 * Created by SheikS on 1/25/2016.
 */
public class HoldingsRecordAndDataMapping {
    private HoldingsRecord holdingsRecord;
    private JSONObject dataMapping;

    public HoldingsRecord getHoldingsRecord() {
        return holdingsRecord;
    }

    public void setHoldingsRecord(HoldingsRecord holdingsRecord) {
        this.holdingsRecord = holdingsRecord;
    }

    public JSONObject getDataMapping() {
        return dataMapping;
    }

    public void setDataMapping(JSONObject dataMapping) {
        this.dataMapping = dataMapping;
    }

}
