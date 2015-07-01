package org.kuali.ole.deliver.drools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pvsubrah on 3/18/15.
 */
public class DroolsExchange {

    private Map<String, Object> context;
    private List<String> list;

    public void addToContext(String parameter, Object value) {
        getContext().put(parameter, value);
    }

    public Object getFromContext(String parameter){
        return getContext().get(parameter);
    }

    public Map<String,Object> getContext() {
        if(null == context){
            context = new HashMap<>();
        }
        return context;
    }

    public List getList() {
        if (null == list) {
            list.add("overdueDays");
            list.add("recallOverDueDays");
        }
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}
