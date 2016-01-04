package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.HadoopIllegalArgumentException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;

import java.util.*;

/**
 * Created by SheikS on 12/20/2015.
 */
public class HoldingsLocationHandler extends HoldingsHandler {

    @Override
    public Boolean isInterested(String operation) {
        return (operation.equals(LOCATION_LEVEL_1) || operation.equals(LOCATION_LEVEL_2) || operation.equals(LOCATION_LEVEL_3) ||
                operation.equals(LOCATION_LEVEL_4) || operation.equals(LOCATION_LEVEL_5));
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        for (Iterator iterator = requestJsonObject.keys(); iterator.hasNext(); ) {
            String key = (String) iterator.next();
            String value = getStringValueFromJsonObject(requestJsonObject, key);

            OleLocation locationBasedOnCode = getLevelIdForLocationCode(value);

            OleLocationLevel oleLocationLevel = locationBasedOnCode.getOleLocationLevel();
            String matchPointLevelId = oleLocationLevel.getLevelId();

            String location = holdingRecord.getLocation();
            Map map = new TreeMap();
            StringTokenizer stringTokenizer = new StringTokenizer(location, "/");


            while (stringTokenizer.hasMoreTokens()) {
                String token = stringTokenizer.nextToken();
                map.put(getLevelId(token), token);
            }
            if (map.get(matchPointLevelId).equals(value)) {
                exchange.add("matchedHoldings", holdingRecord);
                break;
            }

        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        // Todo : Set Location.
        exchange.add("holdingsRecord", holdingsRecord);
    }
}
