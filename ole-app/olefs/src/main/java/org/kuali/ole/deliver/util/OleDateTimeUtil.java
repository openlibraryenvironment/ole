package org.kuali.ole.deliver.util;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.Interval;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.calendar.bo.OleCalendar;
import org.kuali.ole.deliver.calendar.bo.OleCalendarWeek;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by pvsubrah on 10/3/15.
 */
public class OleDateTimeUtil {
    private Map<String, OleCalendar> calendarMap;
    private BusinessObjectService businessObjectService;

    public Date calculateDateTimeByPeriod(String loanPeriod, OleCirculationDesk oleCirculationDesk) {

        Date loanDueDate = null;

        loanDueDate = getLoanDueDate(loanPeriod, loanDueDate);

        if (null != oleCirculationDesk) {
            OleCalendar activeCalendar = getActiveCalendar(loanDueDate, oleCirculationDesk.getCalendarGroupId());

            int day = loanDueDate.getDay();
            Map<String, String> closingTimeForTheGivenDay = getClosingTimeForTheGivenDay(day, activeCalendar);

            boolean validTime = compareTimes(closingTimeForTheGivenDay, loanDueDate);
            if(validTime){
                return loanDueDate;
            }

            //TODO: System parameter determines include/exclude working hours
            return null;
        }

        return loanDueDate;
    }

    private Date getLoanDueDate(String loanPeriod, Date loanDueDate) {
        StringTokenizer stringTokenizer = new StringTokenizer(loanPeriod, "-");
        String amount = stringTokenizer.nextToken();
        String period = stringTokenizer.nextToken();

        if (period.equalsIgnoreCase("m")) {
            loanDueDate = DateUtils.addMinutes(new Date(), Integer.parseInt(amount));
        } else if (period.equalsIgnoreCase("h")) {
            loanDueDate = DateUtils.addHours(new Date(), Integer.parseInt(amount));
        } else if (period.equalsIgnoreCase("d")) {
            loanDueDate = DateUtils.addDays(new Date(), Integer.parseInt(amount));
        } else if (period.equalsIgnoreCase("w")) {
            loanDueDate = DateUtils.addWeeks(new Date(), Integer.parseInt(amount));
        }
        return loanDueDate;
    }

    private boolean compareTimes(Map<String, String> closingTimeForTheGivenDay, Date loanDueDate) {
        String time = closingTimeForTheGivenDay.keySet().iterator().next();
        String timeSession = closingTimeForTheGivenDay.get(time);

        StringTokenizer timeTokenizer = new StringTokenizer(time, ":");


        int hour = timeSession.equalsIgnoreCase("am") ? Integer.parseInt(timeTokenizer.nextToken()) : Integer.parseInt(timeTokenizer.nextToken()) + 12;

        Calendar instance = Calendar.getInstance();
        //The date is being set to the loan due date to ensure the comparisons are for the given day.
        instance.setTime(loanDueDate);
        //The hour and minutes are for closing times.
        instance.set(Calendar.HOUR, hour);
        instance.set(Calendar.MINUTE, Integer.parseInt(timeTokenizer.nextToken()));

        //Compares for the givne day if the loan due time falls within the closing time
        return instance.getTime().before(loanDueDate);
    }

    private Map<String, String> getClosingTimeForTheGivenDay(int day, OleCalendar oleCalendar) {
        Map<String, String> closingTimeMap = new HashMap<>();
        List<OleCalendarWeek> oleCalendarWeekList = oleCalendar.getOleCalendarWeekList();
        for (Iterator<OleCalendarWeek> iterator = oleCalendarWeekList.iterator(); iterator.hasNext(); ) {
            OleCalendarWeek oleCalendarWeek = iterator.next();

            if (oleCalendarWeek.getStartDay().equals(String.valueOf(day))) {
                String closeTime = oleCalendarWeek.getCloseTime();
                String closeTimeSession = oleCalendarWeek.getCloseTimeSession();
                closingTimeMap.put(closeTime, closeTimeSession);
            } else if (oleCalendarWeek.isEachDayWeek()) {
                if (day > Integer.valueOf(oleCalendarWeek.getStartDay()) && day < Integer.valueOf(oleCalendarWeek.getEndDay())) {
                    String closeTime = oleCalendarWeek.getCloseTime();
                    String closeTimeSession = oleCalendarWeek.getCloseTimeSession();
                    closingTimeMap.put(closeTime, closeTimeSession);
                }
            }
        }

        return closingTimeMap;
    }

    public OleCalendar getActiveCalendar(Date date, String groupId) {
        if (!getCalendarMap().containsKey(groupId)) {
            List<OleCalendar> oleCalendarList = getOleCalendars(groupId);
            for (OleCalendar calendar : oleCalendarList) {
                if (calendarExists(new Timestamp(date.getTime()), calendar.getBeginDate(), calendar.getEndDate())) {
                    getCalendarMap().put(groupId, calendar);
                }
            }
        }
        return getCalendarMap().get(groupId);
    }

    protected List<OleCalendar> getOleCalendars(String groupId) {
        HashMap criteriaMap = new HashMap();
        criteriaMap.put(OLEConstants.CALENDER_ID, groupId);
        return (List<OleCalendar>) getBusinessObjectService().findMatching(OleCalendar.class, criteriaMap);
    }

    public boolean calendarExists(Timestamp date, Timestamp fromDate, Timestamp toDate) {
        Interval interval;
        if (null != fromDate) {
            if (null != toDate) {
                interval = new Interval(fromDate.getTime(), toDate.getTime());
                return interval.contains(date.getTime());
            } else {
                return date.compareTo(fromDate) > 0 ? true : false;
            }
        }

        return false;
    }

    public Map<String, OleCalendar> getCalendarMap() {
        if (null == calendarMap) {
            calendarMap = new HashMap<>();
        }
        return calendarMap;
    }

    public void setCalendarMap(Map<String, OleCalendar> calendarMap) {
        this.calendarMap = calendarMap;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
}
