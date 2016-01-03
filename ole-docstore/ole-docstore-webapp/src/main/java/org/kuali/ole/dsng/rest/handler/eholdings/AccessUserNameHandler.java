package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;

/**
 * Created by SheikS on 12/31/2015.
 */
public class AccessUserNameHandler extends HoldingsHandler {

    private final String TYPE = "Access Username";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        String accessUserName = getStringValueFromJsonObject(requestJsonObject, TYPE);
        if (StringUtils.equals(holdingRecord.getAccessUserName(), accessUserName)) {
            exchange.add("matchedHoldings", holdingRecord);
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String accessUserName = getStringValueFromJsonObject(requestJsonObject, TYPE);
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        holdingRecord.setAccessUserName(accessUserName);
        exchange.add("holdingsRecord", holdingRecord);
    }
}
