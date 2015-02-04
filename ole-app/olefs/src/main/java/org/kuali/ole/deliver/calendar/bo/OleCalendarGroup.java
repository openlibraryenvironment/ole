package org.kuali.ole.deliver.calendar.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * OleCalendarGroup is business object class for OleCalendarGroup Maintenance Document
 */
public class OleCalendarGroup extends PersistableBusinessObjectBase {

    private String calendarGroupId;
    private String calendarGroupCode;
    private String calendarGroupName;
    private boolean active;

    public String getCalendarGroupId() {
        return calendarGroupId;
    }

    public void setCalendarGroupId(String calendarGroupId) {
        this.calendarGroupId = calendarGroupId;
    }

    public String getCalendarGroupCode() {
        return calendarGroupCode;
    }

    public void setCalendarGroupCode(String calendarGroupCode) {
        this.calendarGroupCode = calendarGroupCode;
    }

    public String getCalendarGroupName() {
        return calendarGroupName;
    }

    public void setCalendarGroupName(String calendarGroupName) {
        this.calendarGroupName = calendarGroupName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("calendarGroupId", calendarGroupId);
        toStringMap.put("active", active);
        return toStringMap;
    }
}
