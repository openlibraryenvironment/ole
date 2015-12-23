package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;

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
        String fullPathLocation = getFullPathLocation(holdingsRecord,jsonObject);
        return StringUtils.equals(holdingsRecord.getLocation(),fullPathLocation);
    }

    @Override
    public HoldingsRecord process(HoldingsRecord holdingsRecord, JSONObject jsonObject) {
        String fullPathLocation = getFullPathLocation(holdingsRecord, jsonObject);
        holdingsRecord.setLocation(fullPathLocation);
        return holdingsRecord;
    }

    private String getFullPathLocation(HoldingsRecord holdingsRecord, JSONObject jsonObject) {
        String location = holdingsRecord.getLocation();
        String locationLevel = holdingsRecord.getLocationLevel();
        return getFullLocationForOverlay(jsonObject, location, locationLevel);
    }


}
