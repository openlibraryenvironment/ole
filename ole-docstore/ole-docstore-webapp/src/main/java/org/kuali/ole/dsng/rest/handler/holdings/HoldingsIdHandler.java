package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 5/27/2016.
 */
public class HoldingsIdHandler extends HoldingsHandler {


    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(OleNGConstants.BatchProcess.LOCAL_IDENTIFIER);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String localIdentifier = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.LOCAL_IDENTIFIER);
        List<String> parsedValues = parseCommaSeperatedValues(localIdentifier);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String holdingId = iterator.next();
            if (StringUtils.equals(holdingRecord.getHoldingsId(), holdingId)) {
                exchange.add(OleNGConstants.MATCHED_HOLDINGS, Boolean.TRUE);
                exchange.add(OleNGConstants.MATCHED_VALUE, holdingId);
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {

    }
}