package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
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
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String callNumber = getStringValueFromJsonObject(requestJsonObject, TYPE);
        List<String> parsedValues = parseCommaSeperatedValues(callNumber);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String callNumberValue = iterator.next();
            if (StringUtils.equals(holdingRecord.getCallNumber(), callNumberValue)) {
                exchange.add(OleNGConstants.MATCHED_HOLDINGS, Boolean.TRUE);
                exchange.add(OleNGConstants.MATCHED_VALUE, callNumberValue);
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, TYPE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            String callNumberValue = listFromJSONArray.get(0);
            HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
            holdingRecord.setCallNumber(callNumberValue);
        }
    }
}
