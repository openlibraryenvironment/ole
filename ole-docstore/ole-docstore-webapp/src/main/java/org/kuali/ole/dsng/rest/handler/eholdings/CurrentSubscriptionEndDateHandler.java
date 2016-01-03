package org.kuali.ole.dsng.rest.handler.eholdings;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by SheikS on 12/31/2015.
 */
public class CurrentSubscriptionEndDateHandler extends HoldingsHandler {

    private final String TYPE = "Current Subscription End Date";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        String currentSubscriptionEndDate = getStringValueFromJsonObject(requestJsonObject, TYPE);
        try{
            Date parsedDate = DOCSTORE_DATE_FORMAT.parse(currentSubscriptionEndDate);
            if (holdingRecord.getCurrentSubscriptionEndDate().compareTo(new Timestamp(parsedDate.getTime())) == 0) {
                exchange.add("matchedHoldings", holdingRecord);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String currentSubscriptionEndDate = getStringValueFromJsonObject(requestJsonObject, TYPE);
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        try{
            Date parsedDate = DOCSTORE_DATE_FORMAT.parse(currentSubscriptionEndDate);
            holdingRecord.setCurrentSubscriptionEndDate(new Timestamp(parsedDate.getTime()));
        } catch(Exception e) {
            e.printStackTrace();
        }
        exchange.add("holdingsRecord", holdingRecord);
    }
}
