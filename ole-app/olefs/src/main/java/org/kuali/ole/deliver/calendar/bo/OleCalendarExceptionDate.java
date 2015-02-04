package org.kuali.ole.deliver.calendar.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/20/13
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCalendarExceptionDate extends PersistableBusinessObjectBase {
    private String calendarExceptionDateId;
    private String calendarId;
    private String exceptionDateDesc;
    private Date date;
    private String exceptionType;
    private String openTime;
    private String closeTime;
    private String openTimeSession;
    private String closeTimeSession;
    private OleCalendar oleCalendar;

    public String getExceptionDateDesc() {
        return exceptionDateDesc;
    }

    public void setExceptionDateDesc(String exceptionDateDesc) {
        this.exceptionDateDesc = exceptionDateDesc;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
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

    public String getCalendarExceptionDateId() {
        return calendarExceptionDateId;
    }

    public void setCalendarExceptionDateId(String calendarExceptionDateId) {
        this.calendarExceptionDateId = calendarExceptionDateId;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
}
