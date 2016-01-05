package org.kuali.ole.dsng.util;

import org.kuali.ole.describe.bo.OleLocation;

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

    public String updateLocation(String oldLocationName, String locationCode) {
        Map levelMap = new TreeMap();
        StringTokenizer stringTokenizer = new StringTokenizer(oldLocationName, "/");
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            levelMap.put(getLevelIdByLocationCode(token), token);
        }

        levelMap.put(getLevelIdByLocationCode(locationCode), locationCode);

        return buildLocationName(levelMap);

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
}
