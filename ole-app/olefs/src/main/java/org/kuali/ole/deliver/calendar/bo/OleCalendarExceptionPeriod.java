package org.kuali.ole.deliver.calendar.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/20/13
 * Time: 4:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCalendarExceptionPeriod extends PersistableBusinessObjectBase {
    private String calendarExceptionPeriodId;
    private String calendarId;
    private Timestamp beginDate;
    private Timestamp endDate;
    private String exceptionPeriodType;
    private String calendarExceptionPeriodDesc;
    private OleCalendar oleCalendar;
    private List<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodWeekList = new ArrayList<>();


    public String getExceptionPeriodType() {
        return exceptionPeriodType;
    }

    public void setExceptionPeriodType(String exceptionPeriodType) {
        this.exceptionPeriodType = exceptionPeriodType;
    }

    public String getCalendarExceptionPeriodDesc() {
        return calendarExceptionPeriodDesc;
    }

    public void setCalendarExceptionPeriodDesc(String calendarExceptionPeriodDesc) {
        this.calendarExceptionPeriodDesc = calendarExceptionPeriodDesc;
    }

    public OleCalendar getOleCalendar() {
        return oleCalendar;
    }

    public void setOleCalendar(OleCalendar oleCalendar) {
        this.oleCalendar = oleCalendar;
    }

    public String getCalendarExceptionPeriodId() {
        return calendarExceptionPeriodId;
    }

    public void setCalendarExceptionPeriodId(String calendarExceptionPeriodId) {
        this.calendarExceptionPeriodId = calendarExceptionPeriodId;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public Timestamp getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Timestamp beginDate) {
        this.beginDate = beginDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public List<OleCalendarExceptionPeriodWeek> getOleCalendarExceptionPeriodWeekList() {
        return oleCalendarExceptionPeriodWeekList;
    }

    public void setOleCalendarExceptionPeriodWeekList(List<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodWeekList) {
        this.oleCalendarExceptionPeriodWeekList = oleCalendarExceptionPeriodWeekList;
    }
}
