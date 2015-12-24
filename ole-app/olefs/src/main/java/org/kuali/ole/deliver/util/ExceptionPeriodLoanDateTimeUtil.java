package org.kuali.ole.deliver.util;

import org.kuali.ole.deliver.calendar.bo.OleCalendar;
import org.kuali.ole.deliver.calendar.bo.OleCalendarExceptionPeriod;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by pvsubrah on 10/7/15.
 */
public class ExceptionPeriodLoanDateTimeUtil {

    public OleCalendarExceptionPeriod doesDateFallInExceptionPeriod(OleCalendar oleCalendar, Date loanDueDate) {

        List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList =
                oleCalendar.getOleCalendarExceptionPeriodList();

        for (Iterator<OleCalendarExceptionPeriod> iterator = oleCalendarExceptionPeriodList.iterator(); iterator.hasNext(); ) {
            OleCalendarExceptionPeriod oleCalendarExceptionPeriod = iterator.next();
            Timestamp beginDate = oleCalendarExceptionPeriod.getBeginDate();
            int beginDayAndLoadDayMatch = datesMatch(beginDate, loanDueDate);
            Timestamp endDate = oleCalendarExceptionPeriod.getEndDate();
            int endDayAndLoadDayMatch = datesMatch(endDate, loanDueDate);
            if (beginDayAndLoadDayMatch <= 0 && endDayAndLoadDayMatch >= 0) {
                return oleCalendarExceptionPeriod;
            }
        }
        return null;
    }

    public int datesMatch(Date date1, Date date2) {
        int isDateMatch = 0;
        List<Calendar> calendars = prepareCalendars(date1, date2);

        isDateMatch = doDatesMatch(calendars.get(0), calendars.get(1));

        return isDateMatch;
    }

    private int doDatesMatch(Calendar date1Calendar, Calendar date2Calendar) {
        return date1Calendar.compareTo(date2Calendar);
    }

    private List<Calendar> prepareCalendars(Date date1, Date date2) {
        Calendar date1Calendar = Calendar.getInstance();
        date1Calendar.setTime(date1);


        Calendar date2Calendar = Calendar.getInstance();
        date2Calendar.setTime(date2);

        ArrayList<Calendar> calendars = new ArrayList<>();
        calendars.add(date1Calendar);
        calendars.add(date2Calendar);
        return calendars;
    }
}
