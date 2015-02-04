package org.kuali.ole.select.bo;


import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 9/21/12
 * Time: 7:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleEventType extends PersistableBusinessObjectBase {

    private String eventTypeId;
    private String eventTypeName;

    public String getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(String eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("eventTypeId", eventTypeId);
        toStringMap.put("eventTypeName", eventTypeName);
        return toStringMap;
    }
}
