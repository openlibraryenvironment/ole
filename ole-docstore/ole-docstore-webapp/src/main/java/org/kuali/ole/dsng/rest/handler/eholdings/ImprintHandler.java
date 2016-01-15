package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;

/**
 * Created by SheikS on 1/7/2016.
 */
public class ImprintHandler extends HoldingsHandler {

    private final String TYPE = "Imprint";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String imprint = getStringValueFromJsonObject(requestJsonObject, TYPE);
        if (StringUtils.equals(holdingRecord.getImprint(), imprint)) {
            exchange.add(OleNGConstants.MATCHED_HOLDINGS, holdingRecord);
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String imprint = getStringValueFromJsonObject(requestJsonObject, TYPE);
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        holdingRecord.setImprint(imprint);
        exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingRecord);
    }
}
