package org.kuali.ole.deliver.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pvsubrah on 8/25/15.
 */
public class AlterDueDateAndRenewResponse {
    Map<String, String[]> validItemIdsMap = new HashMap();
    Map<String, String[]> invalidItemIdsMap = new HashMap();

    public Map<String, String[]> getValidItemIdsMap() {
        return validItemIdsMap;
    }

    public void setValidItemIdsMap(Map<String, String[]> validItemIdsMap) {
        this.validItemIdsMap = validItemIdsMap;
    }

    public Map<String, String[]> getInvalidItemIdsMap() {
        return invalidItemIdsMap;
    }

    public void setInvalidItemIdsMap(Map<String, String[]> invalidItemIdsMap) {
        this.invalidItemIdsMap = invalidItemIdsMap;
    }
}
