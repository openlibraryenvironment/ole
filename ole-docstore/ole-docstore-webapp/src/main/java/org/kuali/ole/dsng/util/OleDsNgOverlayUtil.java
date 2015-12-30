package org.kuali.ole.dsng.util;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jettison.json.JSONObject;
import org.marc4j.marc.Record;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by SheikS on 12/20/2015.
 */
public class OleDsNgOverlayUtil extends OleDsHelperUtil {
    
    public final static String LOCATION_LEVEL_1 = "Location Level1";
    public final static String LOCATION_LEVEL_2 = "Location Level2";
    public final static String LOCATION_LEVEL_3 = "Location Level3";
    public final static String LOCATION_LEVEL_4 = "Location Level4";
    public final static String LOCATION_LEVEL_5 = "Location Level5";

    public String getFullLocationForOverlay(JSONObject jsonObject, String itemLocation, String itemLocationLevel) {
        String locationLevel1 = null;
        String locationLevel2 = null;
        String locationLevel3 = null;
        String locationLevel4 = null;
        String locationLevel5 = null;

        StringTokenizer locationTokenizer = new StringTokenizer(itemLocation, "/");
        StringTokenizer locationLevelTokenizer = new StringTokenizer(itemLocationLevel, "/");
        while (locationLevelTokenizer.hasMoreTokens()) {
            String locationLevelName = locationLevelTokenizer.nextToken();
            String locationCode = locationTokenizer.nextToken();
            if (locationLevelName.equalsIgnoreCase(OleDsHelperUtil.LEVEL_NAMES.INSTITUTION.getName())) {
                locationLevel1 = locationCode;
            } else if (locationLevelName.equalsIgnoreCase(OleDsHelperUtil.LEVEL_NAMES.CAMPUS.getName())) {
                locationLevel2 = locationCode;
            }
            if (locationLevelName.equalsIgnoreCase(OleDsHelperUtil.LEVEL_NAMES.LIBRARY.getName())) {
                locationLevel3 = locationCode;
            }
            if (locationLevelName.equalsIgnoreCase(OleDsHelperUtil.LEVEL_NAMES.COLLECTION.getName())) {
                locationLevel4 = locationCode;
            }
            if (locationLevelName.equalsIgnoreCase(OleDsHelperUtil.LEVEL_NAMES.SHELVING.getName())) {
                locationLevel5 = locationCode;
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
        return formLocation(locationLevel1, locationLevel2, locationLevel3,
                locationLevel4, locationLevel5);
    }

    public String replaceBibIdTo001Tag(String marcContent,String bibId) {
        List<Record> records = getMarcRecordUtil().convertMarcXmlContentToMarcRecord(marcContent);
        if(CollectionUtils.isNotEmpty(records)) {
            Record record = records.get(0);
            getMarcRecordUtil().updateControlFieldValue(record,"001",bibId);
            return getMarcRecordUtil().convertMarcRecordToMarcContent(record);
        }
        return null;
    }
}
