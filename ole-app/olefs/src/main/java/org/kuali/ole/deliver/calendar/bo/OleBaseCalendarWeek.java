package org.kuali.ole.deliver.calendar.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by pvsubrah on 10/7/15.
 */
public abstract class OleBaseCalendarWeek extends PersistableBusinessObjectBase {
    String openTime;
    String closeTime;
    String startDay;
    String endDay;

    String openTimeSession;
    String closeTimeSession;


    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }


    public String getOpenTimeSession() {
        return openTimeSession;
    }

    public void setOpenTimeSession(String openTimeSession) {
        this.openTimeSession = openTimeSession;
    }

    public String getCloseTimeSession() {
        return closeTimeSession;
    }

    public void setCloseTimeSession(String closeTimeSession) {
        this.closeTimeSession = closeTimeSession;
    }
}
