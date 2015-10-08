package org.kuali.ole.deliver.util;

import org.kuali.ole.deliver.calendar.bo.OleCalendar;
import org.kuali.ole.deliver.calendar.bo.OleCalendarExceptionDate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 10/7/15.
 */
public class ExceptionDateLoanDateTimeUtil extends ExceptionPeriodLoanDateTimeUtil {
    public OleCalendarExceptionDate isDateAnExceptionDate(OleCalendar oleCalendar, Date loanDueDate) {

        List<OleCalendarExceptionDate> oleCalendarExceptionDateList = oleCalendar.getOleCalendarExceptionDateList();

        for (Iterator<OleCalendarExceptionDate> iterator = oleCalendarExceptionDateList.iterator(); iterator.hasNext(); ) {
            OleCalendarExceptionDate oleCalendarExceptionDate = iterator.next();
            if (datesMatch(oleCalendarExceptionDate.getDate(), loanDueDate) == 0) {
                return oleCalendarExceptionDate;
            }
        }
        return null;
    }
}
