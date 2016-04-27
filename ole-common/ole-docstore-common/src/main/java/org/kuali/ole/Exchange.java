package org.kuali.ole;

import java.util.HashMap;
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

    public Map getContext() {
        if (null == context) {
            context = new HashMap();
        }
        return context;
    }

    public void remove(String key) {
        getContext().remove(key);
    }
}
