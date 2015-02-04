package org.kuali.ole.deliver.calendar.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/20/13
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCalendarExceptionPeriodWeek extends PersistableBusinessObjectBase {

    private String calendarWeekId;
    private String calendarExceptionPeriodId;
    private String openTime;
    private String closeTime;
    private String startDay;
    private String endDay;

    private String openTimeSession;
    private String closeTimeSession;
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

    public String getCalendarExceptionPeriodId() {
        return calendarExceptionPeriodId;
    }

    public void setCalendarExceptionPeriodId(String calendarExceptionPeriodId) {
        this.calendarExceptionPeriodId = calendarExceptionPeriodId;
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
