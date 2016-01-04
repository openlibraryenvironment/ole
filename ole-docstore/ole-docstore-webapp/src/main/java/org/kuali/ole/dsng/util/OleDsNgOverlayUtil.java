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
