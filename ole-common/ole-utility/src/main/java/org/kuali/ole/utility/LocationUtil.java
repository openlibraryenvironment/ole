package org.kuali.ole.utility;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.oleng.common.service.OleNgCommonMemorizeService;
import org.kuali.ole.oleng.common.service.impl.OleNgCommonMemorizeServiceImpl;

import java.util.*;

/**
 * Created by SheikS on 3/2/2016.
 */
public class LocationUtil extends BusinessObjectServiceHelperUtil{

    public static final String FORWARD_SLASH = "/";

    private JSONHelperUtil jsonHelperUtil;

    private OleNgCommonMemorizeService oleNgCommonMemorizeService;

    public String getLevelIdByLocationCode(String locationCode) {
        OleLocation levelIdForLocationCode = getLocationByCode(locationCode);
        if (null != levelIdForLocationCode) {
            return levelIdForLocationCode.getLevelId();
        }
        return null;
    }

    public OleLocation getLocationByCode(String locationCode) {
        return getOleNgCommonMemorizeService().getLocationByCode(locationCode);
    }

    public String buildLocationName(String oldLocationName, String locationCode, Exchange exchange) {
        Map levelMap = new TreeMap();
        StringTokenizer stringTokenizer = new StringTokenizer(oldLocationName, "/");
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            levelMap.put(getLevelIdByLocationCode(token), token);
        }

        String levelIdByLocationCode = getLevelIdByLocationCode(locationCode);
        if (StringUtils.isNotBlank(levelIdByLocationCode)){
            levelMap.put(levelIdByLocationCode, locationCode);
        } else {
            new OleNgUtil().addValidationErrorMessageToExchange(exchange, "Invalid Location Level" + " : " + locationCode);
        }
        return buildLocationName(levelMap);
    }

    public Map<String,String> buildLocationMap(JSONObject requestJsonObject, Exchange exchange) {
        Map<String,String> locationMap = new TreeMap<String, String>();

        if(requestJsonObject.has(OleNGConstants.BatchProcess.LOCATION_LEVEL_1)) {
            JSONArray jsonArrayeFromJsonObject = getJsonHelperUtil().getJSONArrayeFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.LOCATION_LEVEL_1);
            List<String> listFromJSONArray = getJsonHelperUtil().getListFromJSONArray(jsonArrayeFromJsonObject.toString());
            if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
                String value = listFromJSONArray.get(0);
                addLocationToMap(value,locationMap, exchange, 1);
            }
        }
        if(requestJsonObject.has(OleNGConstants.BatchProcess.LOCATION_LEVEL_2)) {
            JSONArray jsonArrayeFromJsonObject = getJsonHelperUtil().getJSONArrayeFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.LOCATION_LEVEL_2);
            List<String> listFromJSONArray = getJsonHelperUtil().getListFromJSONArray(jsonArrayeFromJsonObject.toString());
            if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
                String value = listFromJSONArray.get(0);
                addLocationToMap(value,locationMap, exchange, 2);
            }
        }
        if(requestJsonObject.has(OleNGConstants.BatchProcess.LOCATION_LEVEL_3)) {
            JSONArray jsonArrayeFromJsonObject = getJsonHelperUtil().getJSONArrayeFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.LOCATION_LEVEL_3);
            List<String> listFromJSONArray = getJsonHelperUtil().getListFromJSONArray(jsonArrayeFromJsonObject.toString());
            if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
                String value = listFromJSONArray.get(0);
                addLocationToMap(value,locationMap, exchange, 3);
            }
        }
        if(requestJsonObject.has(OleNGConstants.BatchProcess.LOCATION_LEVEL_4)) {
            JSONArray jsonArrayeFromJsonObject = getJsonHelperUtil().getJSONArrayeFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.LOCATION_LEVEL_4);
            List<String> listFromJSONArray = getJsonHelperUtil().getListFromJSONArray(jsonArrayeFromJsonObject.toString());
            if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
                String value = listFromJSONArray.get(0);
                addLocationToMap(value,locationMap, exchange, 4);
            }
        }
        if(requestJsonObject.has(OleNGConstants.BatchProcess.LOCATION_LEVEL_5)) {
            JSONArray jsonArrayeFromJsonObject = getJsonHelperUtil().getJSONArrayeFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.LOCATION_LEVEL_5);
            List<String> listFromJSONArray = getJsonHelperUtil().getListFromJSONArray(jsonArrayeFromJsonObject.toString());
            if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
                String value = listFromJSONArray.get(0);
                addLocationToMap(value,locationMap, exchange, 5);
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

    public void addLocationToMap(String locationCode, Map<String,String> locationMap, Exchange exchange, int locationLevel) {
        OleLocation oleLocation = getLocationByCode(locationCode);
        if(null != oleLocation) {
            locationMap.put(oleLocation.getOleLocationLevel().getLevelCode(),oleLocation.getLocationCode());
        } else {
            new OleNgUtil().addValidationErrorMessageToExchange(exchange, "Invalid Location Level" + locationLevel + " : " + locationCode);
        }
    }


    public JSONHelperUtil getJsonHelperUtil() {
        if(null == jsonHelperUtil) {
            jsonHelperUtil = new JSONHelperUtil();
        }
        return jsonHelperUtil;
    }

    public void setJsonHelperUtil(JSONHelperUtil jsonHelperUtil) {
        this.jsonHelperUtil = jsonHelperUtil;
    }

    public OleNgCommonMemorizeService getOleNgCommonMemorizeService() {
        if(null == oleNgCommonMemorizeService) {
            oleNgCommonMemorizeService = new OleNgCommonMemorizeServiceImpl();
        }
        return oleNgCommonMemorizeService;
    }

    public void setOleNgCommonMemorizeService(OleNgCommonMemorizeService oleNgCommonMemorizeService) {
        this.oleNgCommonMemorizeService = oleNgCommonMemorizeService;
    }
}
