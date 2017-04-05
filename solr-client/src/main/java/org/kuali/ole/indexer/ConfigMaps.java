package org.kuali.ole.indexer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sheiks on 01/11/16.
 */
public class ConfigMaps {
    Map<String, String> FIELDS_TO_TAGS_2_INCLUDE_MAP = new HashMap<String, String>();
    Map<String, String> FIELDS_TO_TAGS_2_EXCLUDE_MAP = new HashMap<String, String>();

    public Map<String, String> getFIELDS_TO_TAGS_2_INCLUDE_MAP() {
        return FIELDS_TO_TAGS_2_INCLUDE_MAP;
    }

    public void setFIELDS_TO_TAGS_2_INCLUDE_MAP(Map<String, String> FIELDS_TO_TAGS_2_INCLUDE_MAP) {
        this.FIELDS_TO_TAGS_2_INCLUDE_MAP = FIELDS_TO_TAGS_2_INCLUDE_MAP;
    }

    public Map<String, String> getFIELDS_TO_TAGS_2_EXCLUDE_MAP() {
        return FIELDS_TO_TAGS_2_EXCLUDE_MAP;
    }

    public void setFIELDS_TO_TAGS_2_EXCLUDE_MAP(Map<String, String> FIELDS_TO_TAGS_2_EXCLUDE_MAP) {
        this.FIELDS_TO_TAGS_2_EXCLUDE_MAP = FIELDS_TO_TAGS_2_EXCLUDE_MAP;
    }
}
