package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.util.LocationUtil;

import java.util.*;

/**
 * Created by SheikS on 12/20/2015.
 */
public class HoldingsLocationHandler extends HoldingsHandler {

    private LocationUtil locationUtil;

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

            OleLocation locationBasedOnCode = getLocationUtil().getLocationByCode(value);

            OleLocationLevel oleLocationLevel = locationBasedOnCode.getOleLocationLevel();
            String matchPointLevelId = oleLocationLevel.getLevelId();

            String location = holdingRecord.getLocation();
            if (StringUtils.isNotBlank(location)) {
                Map map = new TreeMap();
                StringTokenizer stringTokenizer = new StringTokenizer(location, "/");


                while (stringTokenizer.hasMoreTokens()) {
                    String token = stringTokenizer.nextToken();
                    map.put(getLocationUtil().getLevelIdByLocationCode(token), token);
                }
                if (map.get(matchPointLevelId).equals(value)) {
                    exchange.add("matchedHoldings", holdingRecord);
                    break;
                }
            }

        }
    }


    public LocationUtil getLocationUtil() {
        if(null == locationUtil){
            locationUtil = new LocationUtil();
        }
        return locationUtil;
    }


    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        // Todo : Set Location.
        String holdingsLocation = holdingsRecord.getLocation();
        if (StringUtils.isNotBlank(holdingsLocation)) {
            for (Iterator iterator = requestJsonObject.keys(); iterator.hasNext(); ) {
                String key = (String) iterator.next();
                if(key.contains("Location Level")) {
                    String value = getStringValueFromJsonObject(requestJsonObject, key);
                    holdingsLocation = getLocationUtil().updateLocation(holdingsLocation, value);
                }
            }

            holdingsRecord.setLocation(holdingsLocation);
        }
        exchange.add("holdingsRecord", holdingsRecord);
    }
}
