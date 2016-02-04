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
    private String locationLevel;

    @Override
    public Boolean isInterested(String operation) {
        boolean locationLevel1Match = operation.equals(LOCATION_LEVEL_1);
        boolean locationLevel2Match = operation.equals(LOCATION_LEVEL_2);
        boolean locationLevel3Match = operation.equals(LOCATION_LEVEL_3);
        boolean locationLevel4Match = operation.equals(LOCATION_LEVEL_4);
        boolean locationLevel5Match = operation.equals(LOCATION_LEVEL_5);
        if(locationLevel1Match) {
            locationLevel = LOCATION_LEVEL_1;
        } else if(locationLevel2Match) {
            locationLevel = LOCATION_LEVEL_2;
        } else if(locationLevel3Match) {
            locationLevel = LOCATION_LEVEL_3;
        } else if(locationLevel4Match) {
            locationLevel = LOCATION_LEVEL_4;
        } else if(locationLevel5Match) {
            locationLevel = LOCATION_LEVEL_5;
        }
        return  (locationLevel1Match || locationLevel2Match || locationLevel3Match ||
                locationLevel4Match || locationLevel5Match);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String locationLevel = getStringValueFromJsonObject(requestJsonObject, this.locationLevel);
        List<String> parsedValues = parseCommaSeperatedValues(locationLevel);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String locationLevelValue = iterator.next();

            OleLocation locationBasedOnCode = getLocationUtil().getLocationByCode(locationLevelValue);

            OleLocationLevel oleLocationLevel = locationBasedOnCode.getOleLocationLevel();
            String matchPointLevelId = oleLocationLevel.getLevelId();

            Map map = new TreeMap();
            String location = holdingRecord.getLocation();
            if (StringUtils.isNotBlank(location)) {
                StringTokenizer stringTokenizer = new StringTokenizer(location, "/");


                while (stringTokenizer.hasMoreTokens()) {
                    String token = stringTokenizer.nextToken();
                    map.put(getLocationUtil().getLevelIdByLocationCode(token), token);
                }
                String levelId = (String) map.get(matchPointLevelId);
                if (StringUtils.isNotBlank(levelId) && levelId.equals(locationLevelValue)) {
                    exchange.add(OleNGConstants.MATCHED_HOLDINGS, Boolean.TRUE);
                    exchange.add(OleNGConstants.MATCHED_VALUE, locationLevelValue);
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
