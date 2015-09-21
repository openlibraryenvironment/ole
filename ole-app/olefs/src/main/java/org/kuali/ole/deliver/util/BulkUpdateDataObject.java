package org.kuali.ole.deliver.util;

import java.util.Map;

/**
 * Created by sheiksalahudeenm on 8/12/15.
 */
public class BulkUpdateDataObject {
    private Map setClauseMap;
    private Map whereClauseMap;

    public Map getSetClauseMap() {
        return setClauseMap;
    }

    public void setSetClauseMap(Map setClauseMap) {
        this.setClauseMap = setClauseMap;
    }

    public Map getWhereClauseMap() {
        return whereClauseMap;
    }

    public void setWhereClauseMap(Map whereClauseMap) {
        this.whereClauseMap = whereClauseMap;
    }
}
