package org.kuali.ole.deliver.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Interval;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.calendar.bo.OleCalendar;
import org.kuali.ole.deliver.calendar.bo.OleCalendarExceptionPeriod;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 10/7/15.
 */
public class ExceptionPeriodLoanDateTimeUtil {

    public OleCalendarExceptionPeriod doesDateFallInExceptionPeriod(OleCalendar oleCalendar, Date loanDueDate) {

        List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList = oleCalendar.getOleCalendarExceptionPeriodList();

        for (Iterator<OleCalendarExceptionPeriod> iterator = oleCalendarExceptionPeriodList.iterator(); iterator.hasNext(); ) {
            OleCalendarExceptionPeriod oleCalendarExceptionPeriod = iterator.next();
            if (oleCalendarExceptionPeriod != null && oleCalendarExceptionPeriod.getBeginDate() != null && oleCalendarExceptionPeriod.getEndDate() != null) {
                Interval calendarExceptionPeriodInterval = new Interval(oleCalendarExceptionPeriod.getBeginDate().getTime(), oleCalendarExceptionPeriod.getEndDate().getTime());
                if (calendarExceptionPeriodInterval.contains(loanDueDate.getTime())) {
                    return oleCalendarExceptionPeriod;
                }
            }
        }
        return null;
    }

    public OleCalendarExceptionPeriod doesDateFallInPartialExceptionPeriod(OleCalendar oleCalendar, Date loanDueDate) {

        List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList = oleCalendar.getOleCalendarExceptionPeriodList();

        for (Iterator<OleCalendarExceptionPeriod> iterator = oleCalendarExceptionPeriodList.iterator(); iterator.hasNext(); ) {
            OleCalendarExceptionPeriod oleCalendarExceptionPeriod = iterator.next();
            if (oleCalendarExceptionPeriod != null && oleCalendarExceptionPeriod.getBeginDate() != null && oleCalendarExceptionPeriod.getEndDate() != null) {
                if(StringUtils.isNotBlank(oleCalendarExceptionPeriod.getExceptionPeriodType()) && oleCalendarExceptionPeriod.getExceptionPeriodType().equalsIgnoreCase(OLEConstants.HOLIDAY)) {
                    Interval calendarExceptionPeriodInterval = new Interval(oleCalendarExceptionPeriod.getBeginDate().getTime(), oleCalendarExceptionPeriod.getEndDate().getTime());
                    if (calendarExceptionPeriodInterval.contains(loanDueDate.getTime())) {
                        return oleCalendarExceptionPeriod;
                    }
                }
            }
        }
        return null;
    }
}
