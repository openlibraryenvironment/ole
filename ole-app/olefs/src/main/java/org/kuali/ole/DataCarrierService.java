package org.kuali.ole;

import java.util.HashMap;
import java.util.Map;

/**
 *  DataCarrierService supports to add and get data though map.
 */
public class DataCarrierService {
    private Map<String, Object> dataValuesMap = new HashMap<String, Object>();

    /**
     * This method adds key and Object in dataValuesMap.
     * @param key
     * @param value
     */
    public void addData(String key, Object value) {
        this.dataValuesMap.put(key, value);
    }

    /**
     *   This method returns Object using key.
     * @param key
     * @return Object
     */
    public Object getData(String key) {
        return dataValuesMap.get(key);
    }

    /**
     * This method removes key and Object in dataValuesMap.
     * @param key
     */
    public void removeData(String key) {
        this.dataValuesMap.remove(key);
    }
}
