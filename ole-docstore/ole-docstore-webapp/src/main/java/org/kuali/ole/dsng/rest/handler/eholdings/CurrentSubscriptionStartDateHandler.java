package org.kuali.ole.dsng.rest.handler.eholdings;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by SheikS on 12/31/2015.
 */
public class CurrentSubscriptionStartDateHandler extends HoldingsHandler {

    private final String TYPE = "Current Subscription Start Date";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String currentSubscriptionStartDate = getStringValueFromJsonObject(requestJsonObject, TYPE);
        try{
            Date parsedDate = DOCSTORE_DATE_FORMAT.parse(currentSubscriptionStartDate);
            if (holdingRecord.getCurrentSubscriptionStartDate().compareTo(new Timestamp(parsedDate.getTime())) == 0) {
                exchange.add(OleNGConstants.MATCHED_HOLDINGS, holdingRecord);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String currentSubscriptionStartDate = getStringValueFromJsonObject(requestJsonObject, TYPE);
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        try{
            Date parsedDate = DOCSTORE_DATE_FORMAT.parse(currentSubscriptionStartDate);
            holdingRecord.setCurrentSubscriptionStartDate(new Timestamp(parsedDate.getTime()));
        } catch(Exception e) {
            e.printStackTrace();
        }
        exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingRecord);
    }
}
