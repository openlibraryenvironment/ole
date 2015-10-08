package org.kuali.ole.deliver.calendar.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/20/13
 * Time: 4:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCalendarWeek extends OleBaseCalendarWeek {
    private String calendarWeekId;
    private String calendarId;

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

}
