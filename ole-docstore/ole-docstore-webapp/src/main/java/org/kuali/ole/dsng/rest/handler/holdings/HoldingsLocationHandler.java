package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.docstore.common.document.Holdings;
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
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
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
                String levelId = (String) map.get(matchPointLevelId);
                if (StringUtils.isNotBlank(levelId) && levelId.equals(value)) {
                    exchange.add(OleNGConstants.MATCHED_HOLDINGS, holdingRecord);
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
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String holdingsLocation = holdingsRecord.getLocation();
        if (StringUtils.isNotBlank(holdingsLocation)) {
            for (Iterator iterator = requestJsonObject.keys(); iterator.hasNext(); ) {
                String key = (String) iterator.next();
                if(key.contains("Location Level")) {
                    JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, key);
                    List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
                    if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
                        String value = listFromJSONArray.get(0);
                        holdingsLocation = getLocationUtil().buildLocationName(holdingsLocation, value);
                    }
                }
            }
            holdingsRecord.setLocation(holdingsLocation);
        } else {
            StringBuilder locationName = new StringBuilder();
            StringBuilder locationLevelName = new StringBuilder();
            Map<String, String> locationMap = getLocationUtil().buildLocationMap(requestJsonObject);
            for (Iterator<String> iterator = locationMap.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                String locationCode = locationMap.get(key);
                getLocationUtil().appendLocationToStringBuilder(locationLevelName,key);
                getLocationUtil().appendLocationToStringBuilder(locationName, locationCode);
            }
            holdingsRecord.setLocation(locationName.toString());
            holdingsRecord.setLocationLevel(locationLevelName.toString());
        }
        exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingsRecord);
    }
}
