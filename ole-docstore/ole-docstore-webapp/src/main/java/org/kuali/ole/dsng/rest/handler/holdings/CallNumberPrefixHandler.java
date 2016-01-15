package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;

/**
 * Created by SheikS on 12/20/2015.
 */
public class CallNumberPrefixHandler extends HoldingsHandler {

    private final String TYPE = "Call Number Prefix";

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String callNumberPrefix = getStringValueFromJsonObject(requestJsonObject,TYPE);
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        holdingRecord.setCallNumberPrefix(callNumberPrefix);
    }

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String callNumberPrefix = getStringValueFromJsonObject(requestJsonObject, TYPE);
        if(StringUtils.equals(holdingRecord.getCallNumberPrefix(),callNumberPrefix)) {
            exchange.add(OleNGConstants.MATCHED_HOLDINGS, holdingRecord);
        }
    }
}
