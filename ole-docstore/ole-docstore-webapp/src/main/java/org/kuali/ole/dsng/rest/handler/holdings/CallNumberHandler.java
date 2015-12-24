package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/20/2015.
 */
public class CallNumberHandler extends HoldingsHandler {

    private final String TYPE = "Call Number";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        List<HoldingsRecord> matchedHoldingRecords = new ArrayList<HoldingsRecord>();
        List<HoldingsRecord> holdingRecords = (List<HoldingsRecord>) exchange.get("holdingRecords");
        for (Iterator<HoldingsRecord> iterator = holdingRecords.iterator(); iterator.hasNext(); ) {
            HoldingsRecord holdingsRecord = iterator.next();
            JSONObject matchPoints = null;
            try {
                matchPoints = requestJsonObject.getJSONObject("matchPoints");
                String callNumber = getStringValueFromJsonObject(matchPoints, TYPE);
                if (StringUtils.equals(holdingsRecord.getCallNumber(), callNumber)) {
                    matchedHoldingRecords.add(holdingsRecord);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            exchange.add("matchedHoldingRecords", matchedHoldingRecords);
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String callNumberValue = getStringValueFromJsonObject(requestJsonObject, TYPE);
        List<HoldingsRecord> matchedHoldingRecords = (List<HoldingsRecord>) exchange.get("matchedHoldingRecords");
        for (Iterator<HoldingsRecord> iterator = matchedHoldingRecords.iterator(); iterator.hasNext(); ) {
            HoldingsRecord holdingsRecord = iterator.next();
            holdingsRecord.setCallNumber(callNumberValue);
        }
    }
}
