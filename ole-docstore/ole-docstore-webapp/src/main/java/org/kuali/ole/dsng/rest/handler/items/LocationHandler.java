package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.rest.Exchange;

import java.util.*;

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

        for (Iterator iterator = requestJsonObject.keys(); iterator.hasNext(); ) {
            String key = (String) iterator.next();
            String value = getStringValueFromJsonObject(requestJsonObject, key);

            OleLocation locationBasedOnCode = getLevelIdForLocationCode(value);

            OleLocationLevel oleLocationLevel = locationBasedOnCode.getOleLocationLevel();
            String matchPointLevelId = oleLocationLevel.getLevelId();

            String holdingsId = itemRecord.getHoldingsId();
            HoldingsRecord holdingsRecord  = getBusinessObjectService().findBySinglePrimaryKey(HoldingsRecord.class, holdingsId);
            Map map = new TreeMap();
            String location = holdingsRecord.getLocation();
            StringTokenizer stringTokenizer = new StringTokenizer(location, "/");


            while (stringTokenizer.hasMoreTokens()) {
                String token = stringTokenizer.nextToken();
                map.put(getLevelId(token), token);
            }
            if (map.get(matchPointLevelId).equals(value)) {
                exchange.add("matchedItem", itemRecord);
                break;
            }

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

    private String getLevelId(String token) {
        OleLocation levelIdForLocationCode = getLevelIdForLocationCode(token);
        if(null != levelIdForLocationCode){
            return levelIdForLocationCode.getLevelId();
        }
        return null;
    }


    private OleLocation getLevelIdForLocationCode(String locationCode) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("locationCode", locationCode);
        List<OleLocation> locations = (List<OleLocation>) getBusinessObjectService().findMatching(OleLocation.class, map);
        if (locations.size() > 0) {
            return locations.get(0);
        }
        return null;

    }
}
