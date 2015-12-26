package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.rest.Exchange;

/**
 * Created by SheikS on 12/20/2015.
 */
public class LocationHandler extends ItemHandler {

    @Override
    public Boolean isInterested(String operation) {
        return (operation.equals(LOCATION_LEVEL_1) || operation.equals(LOCATION_LEVEL_2) || operation.equals(LOCATION_LEVEL_3) ||
                operation.equals(LOCATION_LEVEL_4) || operation.equals(LOCATION_LEVEL_5));
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {

        ItemRecord itemRecord = (ItemRecord) exchange.get("itemRecord");
        String fullPathLocation = getFullPathLocation(itemRecord, requestJsonObject);
        if (StringUtils.isNotBlank(fullPathLocation) && StringUtils.equals(itemRecord.getLocation(), fullPathLocation)) {
            exchange.add("matchedItem", itemRecord);
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get("itemRecord");
        String fullPathLocation = getFullPathLocation(itemRecord, requestJsonObject);
        itemRecord.setLocation(fullPathLocation);
        exchange.add("itemRecord", itemRecord);
    }

    private String getFullPathLocation(ItemRecord itemRecord, JSONObject jsonObject) {
        String location = itemRecord.getLocation();
        String locationLevel = itemRecord.getLocationLevel();
        if(StringUtils.isNotBlank(location) && StringUtils.isNotBlank(locationLevel)) {
            return getFullLocationForOverlay(jsonObject, location, locationLevel);
        }
        return null;
    }
}
