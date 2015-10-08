package org.kuali.ole.deliver.calendar.bo;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/20/13
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCalendarExceptionPeriodWeek extends OleBaseCalendarWeek{

    private String calendarWeekId;
    private String calendarExceptionPeriodId;

    private OleCalendarExceptionPeriod oleCalendarExceptionPeriod;


    private boolean eachDayOfExceptionWeek;

    public boolean isEachDayOfExceptionWeek() {
        return eachDayOfExceptionWeek;
    }

    public void setEachDayOfExceptionWeek(boolean eachDayOfExceptionWeek) {
        this.eachDayOfExceptionWeek = eachDayOfExceptionWeek;
    }


    public OleCalendarExceptionPeriod getOleCalendarExceptionPeriod() {
        return oleCalendarExceptionPeriod;
    }

    public void setOleCalendarExceptionPeriod(OleCalendarExceptionPeriod oleCalendarExceptionPeriod) {
        this.oleCalendarExceptionPeriod = oleCalendarExceptionPeriod;
    }


    public String getCalendarWeekId() {
        return calendarWeekId;
    }

    public void setCalendarWeekId(String calendarWeekId) {
        this.calendarWeekId = calendarWeekId;
    }

    public String getCalendarExceptionPeriodId() {
        return calendarExceptionPeriodId;
    }

    public void setCalendarExceptionPeriodId(String calendarExceptionPeriodId) {
        this.calendarExceptionPeriodId = calendarExceptionPeriodId;
    }


}
