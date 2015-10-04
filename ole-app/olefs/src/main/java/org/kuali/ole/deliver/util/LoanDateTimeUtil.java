package org.kuali.ole.deliver.util;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.Interval;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.calendar.bo.OleCalendar;
import org.kuali.ole.deliver.calendar.bo.OleCalendarWeek;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by pvsubrah on 10/3/15.
 */
public class LoanDateTimeUtil {
    private Map<String, OleCalendar> calendarMap;
    private BusinessObjectService businessObjectService;

    public Date calculateDateTimeByPeriod(String loanPeriod, OleCirculationDesk oleCirculationDesk) {

        Date loanDueDate = null;

        loanDueDate = getLoanDueDate(loanPeriod, loanDueDate);

        if (null != oleCirculationDesk) {
            OleCalendar activeCalendar = getActiveCalendar(loanDueDate, oleCirculationDesk.getCalendarGroupId());

            int day = loanDueDate.getDay();
            Map<String, Map<String, String>> openAndClosingTimeForTheGivenDay = getOpenAndClosingTimeForTheGivenDay(day, activeCalendar);

            boolean dueTimeWithinWorkingHours = compareTimes(openAndClosingTimeForTheGivenDay.get("closeTime"), loanDueDate);
            if (dueTimeWithinWorkingHours) {
                return loanDueDate;
            } else {
                boolean includeNonWorkingHours = includeNonWorkingHours();
                if (includeNonWorkingHours) {
                    //Get open/close times for the following day.
                    Map<String, Map<String, String>> openingAndClosingTimeForTheNextDay = getOpenAndClosingTimeForTheGivenDay(day + 1, activeCalendar);
                    loanDueDate = resolveDateTime(openingAndClosingTimeForTheNextDay.get("openTime"), DateUtils.addDays(loanDueDate, 1)).getTime();
                    loanDueDate = handleGracePeriod(loanDueDate);
                } else {
                    loanDueDate = resolveDateTime(openAndClosingTimeForTheGivenDay.get("closeTime"), loanDueDate).getTime();
                }
            }
        }

        return loanDueDate;
    }

    private Date handleGracePeriod(Date loanDueDate) {
        Date updatedDate = null;
        String gracePeriod = getGracePeriodForIncludingNonWorkingHours();
        StringTokenizer stringTokenizer = new StringTokenizer(gracePeriod, "-");
        String amount = stringTokenizer.nextToken();
        String interval = stringTokenizer.nextToken();
        if (interval.equalsIgnoreCase("m")) {
           updatedDate =  DateUtils.addMinutes(loanDueDate, Integer.valueOf(amount));
        } else if (interval.equalsIgnoreCase("h")) {
            updatedDate = DateUtils.addHours(loanDueDate, Integer.valueOf(amount));
        }

        return updatedDate;
    }

    public Boolean includeNonWorkingHours() {
        return ParameterValueResolver.getInstance().getParameterAsBoolean(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG);
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
        Calendar instance = resolveDateTime(closingTimeForTheGivenDay, loanDueDate);
        //Compares for the givne day if the loan due time falls within the closing time
        return loanDueDate.before(instance.getTime());
    }

    private Calendar resolveDateTime(Map<String, String> closingTimeForTheGivenDay, Date loanDueDate) {
        String time = closingTimeForTheGivenDay.keySet().iterator().next();
        String timeSession = closingTimeForTheGivenDay.get(time);
        StringTokenizer timeTokenizer = new StringTokenizer(time, ":");
        int hour = timeSession.equalsIgnoreCase("am") ? Integer.parseInt(timeTokenizer.nextToken()) : Integer.parseInt(timeTokenizer.nextToken()) + 12;

        Calendar instance = Calendar.getInstance();
        //The date is being set to the loan due date to ensure the comparisons are for the given day.
        instance.setTime(loanDueDate);
        //The hour and minutes are for closing times.
        instance.set(Calendar.HOUR_OF_DAY, hour);
        instance.set(Calendar.MINUTE, Integer.parseInt(timeTokenizer.nextToken()));
        return instance;
    }

    private Map<String, Map<String, String>> getOpenAndClosingTimeForTheGivenDay(int day, OleCalendar oleCalendar) {
        Map<String, Map<String, String>> openingAndClosingTimeMap = new HashMap<>();
        Map<String, String> closingTimeMap = new HashMap<>();
        Map<String, String> openingTimeMap = new HashMap<>();
        List<OleCalendarWeek> oleCalendarWeekList = oleCalendar.getOleCalendarWeekList();
        for (Iterator<OleCalendarWeek> iterator = oleCalendarWeekList.iterator(); iterator.hasNext(); ) {
            OleCalendarWeek oleCalendarWeek = iterator.next();
            if (oleCalendarWeek.getStartDay().equals(String.valueOf(day))) {
                resolveOpenAndCloseTimes(closingTimeMap, openingTimeMap, oleCalendarWeek);

            } else if (oleCalendarWeek.isEachDayWeek()) {
                if (day > Integer.valueOf(oleCalendarWeek.getStartDay()) && day < Integer.valueOf(oleCalendarWeek.getEndDay())) {
                    resolveOpenAndCloseTimes(closingTimeMap, openingTimeMap, oleCalendarWeek);
                }
            }
        }

        openingAndClosingTimeMap.put("openTime", openingTimeMap);
        openingAndClosingTimeMap.put("closeTime", closingTimeMap);

        return openingAndClosingTimeMap;
    }

    private void resolveOpenAndCloseTimes(Map<String, String> closingTimeMap, Map<String, String> openingTimeMap, OleCalendarWeek oleCalendarWeek) {
        String closeTime = oleCalendarWeek.getCloseTime();
        String closeTimeSession = oleCalendarWeek.getCloseTimeSession();
        closingTimeMap.put(closeTime, closeTimeSession);

        String openTime = oleCalendarWeek.getOpenTime();
        String openTimeSession = oleCalendarWeek.getOpenTimeSession();
        openingTimeMap.put(openTime, openTimeSession);
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

    public String getGracePeriodForIncludingNonWorkingHours() {
        return ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.GRACE_PERIOD_FOR_NON_WORKING_HOURS);
    }
}
