package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;

/**
 * Created by SheikS on 12/20/2015.
 */
public class LocationHandler extends ItemOverlayHandler {

    @Override
    public boolean isInterested(JSONObject jsonObject) {
        return (jsonObject.has(LOCATION_LEVEL_1) || jsonObject.has(LOCATION_LEVEL_2) ||jsonObject.has(LOCATION_LEVEL_3) ||
                jsonObject.has(LOCATION_LEVEL_4) || jsonObject.has(LOCATION_LEVEL_5));
    }

    @Override
    public boolean isMatching(ItemRecord itemRecord, JSONObject jsonObject) {
        String itemLocation = itemRecord.getLocation();
        String itemLocationLevel = itemRecord.getLocationLevel();
        String fullPathLocation = getFullLocationForOverlay(jsonObject, itemLocation, itemLocationLevel);
        return StringUtils.equals(itemRecord.getLocation(),fullPathLocation);
    }

    @Override
    public ItemRecord process(ItemRecord itemRecord, JSONObject jsonObject) {
        String fullPathLocation = getFullPathLocation(itemRecord, jsonObject);
        itemRecord.setLocation(fullPathLocation);
        return itemRecord;
    }

    private String getFullPathLocation(ItemRecord itemRecord, JSONObject jsonObject) {
        String location = itemRecord.getLocation();
        String locationLevel = itemRecord.getLocationLevel();
        return getFullLocationForOverlay(jsonObject, location, locationLevel);
    }
}
