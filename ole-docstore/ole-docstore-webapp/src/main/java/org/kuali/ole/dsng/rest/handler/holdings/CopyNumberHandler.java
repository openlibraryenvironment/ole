package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
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
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String copyNumber = getStringValueFromJsonObject(requestJsonObject, TYPE);
        if (StringUtils.equals(holdingsRecord.getCopyNumber(),copyNumber)) {
            exchange.add(OleNGConstants.MATCHED_HOLDINGS, holdingsRecord);
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String copyNumber = getStringValueFromJsonObject(requestJsonObject, TYPE);
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        holdingsRecord.setCopyNumber(copyNumber);
        exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingsRecord);
    }
}
