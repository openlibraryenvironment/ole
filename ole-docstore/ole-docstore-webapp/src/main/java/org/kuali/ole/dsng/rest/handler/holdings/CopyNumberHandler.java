package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;

/**
 * Created by SheikS on 12/20/2015.
 */
public class CopyNumberHandler extends HoldingsHandler {

    private final String TYPE = "Copy Number";


    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        String copyNumber = getStringValueFromJsonObject(requestJsonObject, TYPE);
        if (StringUtils.equals(holdingsRecord.getCopyNumber(),copyNumber)) {
            exchange.add("matchedHoldings", holdingsRecord);
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String copyNumber = getStringValueFromJsonObject(requestJsonObject, TYPE);
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        holdingsRecord.setCopyNumber(copyNumber);
        exchange.add("holdingsRecord", holdingsRecord);
    }
}
