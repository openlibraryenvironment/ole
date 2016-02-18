package org.kuali.ole.dsng.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.dsng.rest.handler.Handler;

import java.util.*;

/**
 * Created by pvsubrah on 1/4/16.
 */
public class LocationUtil extends OleDsHelperUtil {

    public String getLevelIdByLocationCode(String locationCode) {
        OleLocation levelIdForLocationCode = getLocationByCode(locationCode);
        if (null != levelIdForLocationCode) {
            return levelIdForLocationCode.getLevelId();
        }
        return null;
    }

    public OleLocation getLocationByCode(String locationCode) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("locationCode", locationCode);
        List<OleLocation> locations = (List<OleLocation>) getBusinessObjectService().findMatching(OleLocation.class, map);
        if (locations.size() > 0) {
            return locations.get(0);
        }
        return null;
    }

    public String buildLocationName(String oldLocationName, String locationCode) {
        Map levelMap = new TreeMap();
        StringTokenizer stringTokenizer = new StringTokenizer(oldLocationName, "/");
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            levelMap.put(getLevelIdByLocationCode(token), token);
        }

        levelMap.put(getLevelIdByLocationCode(locationCode), locationCode);

        return buildLocationName(levelMap);
    }

    public Map<String,String> buildLocationMap(JSONObject requestJsonObject) {
        Map<String,String> locationMap = new TreeMap<String, String>();

        if(requestJsonObject.has(Handler.LOCATION_LEVEL_1)) {
            JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, Handler.LOCATION_LEVEL_1);
            List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
            if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
                String value = listFromJSONArray.get(0);
                addLocationToMap(value,locationMap);
            }
        }
        if(requestJsonObject.has(Handler.LOCATION_LEVEL_2)) {
            JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, Handler.LOCATION_LEVEL_2);
            List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
            if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
                String value = listFromJSONArray.get(0);
                addLocationToMap(value,locationMap);
            }
        }
        if(requestJsonObject.has(Handler.LOCATION_LEVEL_3)) {
            JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, Handler.LOCATION_LEVEL_3);
            List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
            if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
                String value = listFromJSONArray.get(0);
                addLocationToMap(value,locationMap);
            }
        }
        if(requestJsonObject.has(Handler.LOCATION_LEVEL_4)) {
            JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, Handler.LOCATION_LEVEL_4);
            List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
            if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
                String value = listFromJSONArray.get(0);
                addLocationToMap(value,locationMap);
            }
        }
        if(requestJsonObject.has(Handler.LOCATION_LEVEL_5)) {
            JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, Handler.LOCATION_LEVEL_5);
            List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
            if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
                String value = listFromJSONArray.get(0);
                addLocationToMap(value,locationMap);
            }
        }
        return locationMap;
    }

    private String buildLocationName(Map map) {
        StringBuffer strBuffer = new StringBuffer();
        for (Iterator iterator = map.keySet().iterator(); iterator.hasNext(); ) {
            String key = (String) iterator.next();
            strBuffer.append(map.get(key));
            if(iterator.hasNext()){
                strBuffer.append("/");
            }
        }
        return strBuffer.toString();
    }

    public void appendLocationToStringBuilder(StringBuilder stringBuilder, String location) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append(FORWARD_SLASH).append(location);
        } else {
            stringBuilder.append(location);
        }
    }

    public void addLocationToMap(String locationCode, Map<String,String> locationMap) {
        OleLocation oleLocation = getLocationByCode(locationCode);
        if(null != oleLocation) {
            locationMap.put(oleLocation.getOleLocationLevel().getLevelCode(),oleLocation.getLocationCode());
        }
    }
}
