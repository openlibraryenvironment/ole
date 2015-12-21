package org.kuali.ole.dsng.rest.handler.overlay.holdings;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;

import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by SheikS on 12/20/2015.
 */
public class LocationHandler extends HoldingsOverlayHandler {

    private final String LOCATION_LEVEL_1 = "Location Level1";
    private final String LOCATION_LEVEL_2 = "Location Level2";
    private final String LOCATION_LEVEL_3 = "Location Level3";
    private final String LOCATION_LEVEL_4 = "Location Level4";
    private final String LOCATION_LEVEL_5 = "Location Level5";

    @Override
    public boolean isInterested(JSONObject jsonObject) {
        return (jsonObject.has(LOCATION_LEVEL_1) || jsonObject.has(LOCATION_LEVEL_2) ||jsonObject.has(LOCATION_LEVEL_3) ||
                jsonObject.has(LOCATION_LEVEL_4) || jsonObject.has(LOCATION_LEVEL_5));
    }

    @Override
    public boolean isMatching(HoldingsRecord holdingsRecord, JSONObject jsonObject) {

        String locationLevel1 = null;
        String locationLevel2 = null;
        String locationLevel3 = null;
        String locationLevel4 = null;
        String locationLevel5 = null;

        StringTokenizer locationTokenizer = new StringTokenizer(holdingsRecord.getLocation(), "/");
        StringTokenizer locationLevelTokenizer = new StringTokenizer(holdingsRecord.getLocationLevel(), "/");
        while (locationLevelTokenizer.hasMoreTokens()) {
            String locationLevelName = locationLevelTokenizer.nextToken();
            String location = locationTokenizer.nextToken();
            if (locationLevelName.equalsIgnoreCase(LEVEL_NAMES.INSTITUTION.getName())) {
                locationLevel1 = location;
            } else if (locationLevelName.equalsIgnoreCase(LEVEL_NAMES.CAMPUS.getName())) {
                locationLevel2 = location;
            }
            if (locationLevelName.equalsIgnoreCase(LEVEL_NAMES.LIBRARY.getName())) {
                locationLevel3 = location;
            }
            if (locationLevelName.equalsIgnoreCase(LEVEL_NAMES.COLLECTION.getName())) {
                locationLevel4 = location;
            }
            if (locationLevelName.equalsIgnoreCase(LEVEL_NAMES.SHELVING.getName())) {
                locationLevel5 = location;
            }
        }

        if(jsonObject.has(LOCATION_LEVEL_1)){
            locationLevel1 = getStringValueFromJsonObject(jsonObject,LOCATION_LEVEL_1);
        }
        if(jsonObject.has(LOCATION_LEVEL_2)){
            locationLevel2 = getStringValueFromJsonObject(jsonObject,LOCATION_LEVEL_2);
        }
        if(jsonObject.has(LOCATION_LEVEL_3)){
            locationLevel3 = getStringValueFromJsonObject(jsonObject,LOCATION_LEVEL_3);
        }
        if(jsonObject.has(LOCATION_LEVEL_4)){
            locationLevel4 = getStringValueFromJsonObject(jsonObject,LOCATION_LEVEL_4);
        }
        if(jsonObject.has(LOCATION_LEVEL_5)){
            locationLevel5 = getStringValueFromJsonObject(jsonObject,LOCATION_LEVEL_5);
        }

        String fullPathLocation = formLocation(locationLevel1, locationLevel2, locationLevel3,
                locationLevel4, locationLevel5);

        return StringUtils.equals(holdingsRecord.getLocation(),fullPathLocation);
    }


}
