package org.kuali.ole.deliver.calendar.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/20/13
 * Time: 4:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCalendarWeek extends PersistableBusinessObjectBase {
    private String calendarWeekId;
    private String calendarId;
    private String openTime;
    private String closeTime;
    private String startDay;
    private String endDay;
    private String openTimeSession;
    private String closeTimeSession;
    private OleCalendar oleCalendar;

    private String exceptionPeriodType;

    public String getExceptionPeriodType() {
        return exceptionPeriodType;
    }

    public void setExceptionPeriodType(String exceptionPeriodType) {
        this.exceptionPeriodType = exceptionPeriodType;
    }

    private boolean eachDayWeek;

    public boolean isEachDayWeek() {
        return eachDayWeek;
    }

    public void setEachDayWeek(boolean eachDayWeek) {
        this.eachDayWeek = eachDayWeek;
    }

    public OleCalendar getOleCalendar() {
        return oleCalendar;
    }

    public void setOleCalendar(OleCalendar oleCalendar) {
        this.oleCalendar = oleCalendar;
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

    public String getCalendarWeekId() {
        return calendarWeekId;
    }

    public void setCalendarWeekId(String calendarWeekId) {
        this.calendarWeekId = calendarWeekId;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

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
}
