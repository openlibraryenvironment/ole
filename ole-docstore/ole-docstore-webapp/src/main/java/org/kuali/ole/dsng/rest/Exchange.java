package org.kuali.ole.dsng.rest;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Created by pvsubrah on 12/23/15.
 */
public class Exchange {
    private Map context;

    public void add(Object key, Object value){
        getContext().put(key, value);
    }

    public Object get(Object key){
        return getContext().get(key);
    }

    private Map getContext() {
        if (null == context) {
            context = new HashedMap();
        }
        return context;
    }
}
