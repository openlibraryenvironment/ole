package org.kuali.ole.deliver.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.Interval;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.calendar.bo.*;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.drools.FixedDateUtil;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pvsubrah on 10/3/15.
 */
public class LoanDateTimeUtil extends ExceptionDateLoanDateTimeUtil {
    private String policyId;
    private Map<String, OleCalendar> calendarMap;
    private OleCalendar activeCalendar;
    private BusinessObjectService businessObjectService;
    private Boolean nonWorkingHoursCheck = false;
    private String period;
    private ParameterValueResolver parameterValueResolver;
    private Date timeToCalculateFrom;

    public Date calculateDateTimeByPeriod(String loanPeriod, OleCirculationDesk oleCirculationDesk) {
        Date loanDueDate;

        loanDueDate = getLoanDueDate(loanPeriod);

        if (null != loanDueDate && null != oleCirculationDesk) {
            OleCalendar activeCalendar = getActiveCalendar(loanDueDate, oleCirculationDesk.getCalendarGroupId());
            setActiveCalendar(activeCalendar);

            if (null != activeCalendar) {
                loanDueDate = calculateDueDate(loanDueDate);
            }
        }

        return loanDueDate;
    }

    private Date getFollowingDay(Date loanDueDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(loanDueDate);
        calendar.add(Calendar.DATE,1);
        calendar.set(Calendar.HOUR_OF_DAY,00);
        calendar.set(Calendar.MINUTE,00);
        calendar.set(Calendar.SECOND,00);
        calendar.set(Calendar.MILLISECOND,01);
        return calendar.getTime();
    }

    private Date calculateDueDate(Date loanDueDate) {
        OleCalendarExceptionPeriod oleCalendarExceptionPeriod = doesDateFallInExceptionPeriod(getActiveCalendar(), loanDueDate);

        if (null == oleCalendarExceptionPeriod) {
            OleCalendarExceptionDate exceptionDate = isDateAnExceptionDate(getActiveCalendar(), loanDueDate);
            if (null == exceptionDate) {
                loanDueDate = handleNonWorkingHoursWorkflow(loanDueDate, getActiveCalendar().getOleCalendarWeekList());
            } else {
                if (StringUtils.isEmpty(exceptionDate.getOpenTime()) && StringUtils.isEmpty(exceptionDate.getCloseTime())) {
                    //Holiday workflow;
                    Date followingDay = getFollowingDay(loanDueDate);
                    loanDueDate = calculateDueDate(followingDay);
                } else {
                    // Partial hours workflow
                    loanDueDate = handleExceptionDayWithPartialHours(loanDueDate, exceptionDate);
                }
            }
        } else {
            List<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodWeekList = oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList();
            //If the week list is empty i.e its a holiday period;
            if (CollectionUtils.isEmpty(oleCalendarExceptionPeriodWeekList)) {
                Timestamp endDate = oleCalendarExceptionPeriod.getEndDate();
                Date followingDay = getFollowingDay(endDate);
                loanDueDate = calculateDueDate(followingDay);
            } else {
                loanDueDate = handleNonWorkingHoursWorkflow(loanDueDate, oleCalendarExceptionPeriodWeekList);
            }
        }
        if(isDateAnExceptionDate(getActiveCalendar(), loanDueDate) != null) {
            loanDueDate = calculateDueDate(loanDueDate);
        }
        else if(doesDateFallInExceptionPeriod(getActiveCalendar(), loanDueDate) != null){
            loanDueDate = calculateDueDate(loanDueDate);
        }
        return loanDueDate;
    }

    private Date handleExceptionDayWithPartialHours(Date loanDueDate, OleCalendarExceptionDate exceptionDate) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(exceptionDate.getDate());
        exceptionDate.setStartDay(String.valueOf(instance.get(Calendar.DAY_OF_WEEK)-1));
        exceptionDate.setEndDay(String.valueOf(instance.get(Calendar.DAY_OF_WEEK)-1));

        List oleBaseCalendarWeekList = new ArrayList<>();
        oleBaseCalendarWeekList.add(exceptionDate);
        loanDueDate = handleNonWorkingHoursWorkflow(loanDueDate, oleBaseCalendarWeekList);

        return loanDueDate;
    }

    private Date handleNonWorkingHoursWorkflow(Date loanDueDate, List<? extends OleBaseCalendarWeek> oleBaseCalendarWeekList) {
        if (StringUtils.isNotBlank(period) && (period.equalsIgnoreCase("h") || period.equalsIgnoreCase("m"))) {
            Map<String, Map<String, String>> openAndClosingTimeForTheGivenDayFromWeekList = getOpenAndClosingTimeForTheGivenDayFromWeekList(loanDueDate, oleBaseCalendarWeekList);
            while(!(openAndClosingTimeForTheGivenDayFromWeekList.get("openTime").size() > 0 && openAndClosingTimeForTheGivenDayFromWeekList.get("closeTime").size() > 0)) {
                Date followingDay = getFollowingDay(loanDueDate);
                loanDueDate = followingDay;
                openAndClosingTimeForTheGivenDayFromWeekList = getOpenAndClosingTimeForTheGivenDayFromWeekList(loanDueDate, oleBaseCalendarWeekList);
            }
            if (nonWorkingHoursCheck) {
                loanDueDate = processDueDateAndGracePeriod(loanDueDate, openAndClosingTimeForTheGivenDayFromWeekList);
            } else {
                boolean loanDueTimeWithinWorkingHours = isLoanDueTimeWithinWorkingHours(loanDueDate, oleBaseCalendarWeekList);
                if (!loanDueTimeWithinWorkingHours) {
                    if (includeNonWorkingHours()) {
                    Date followingDay =getFollowingDay(loanDueDate);
                        nonWorkingHoursCheck = true;
                        loanDueDate = calculateDueDate(followingDay);
                    } else {
                        Map<String, String> closeTime = openAndClosingTimeForTheGivenDayFromWeekList.get("closeTime");
                        Map<String, String> openTime = openAndClosingTimeForTheGivenDayFromWeekList.get("openTime");
                        Calendar calendar;
                        if (isOpenTimeGreaterThanCloseTime(openTime, closeTime)) {
                            calendar = resolveDateTime(closeTime, DateUtils.addDays(loanDueDate, 1));
                        } else {
                            calendar = resolveDateTime(closeTime, loanDueDate);
                        }
                        loanDueDate = calendar.getTime();
                    }
            } else if(inDueDateFallsBeforeOpeningTimeOfLibrary(openAndClosingTimeForTheGivenDayFromWeekList.get("openTime"), loanDueDate)) {
                loanDueDate = processDueDateAndGracePeriod(loanDueDate,openAndClosingTimeForTheGivenDayFromWeekList);
                }
            }
        } else {
            loanDueDate = processDueTimeForRegularLoan(loanDueDate);
        }
        return loanDueDate;
    }

    private Date processDueTimeForRegularLoan(Date loanDueDate) {
        boolean validTime = false;
        int hours = 23;
        int minutes = 59;
        int seconds = 59;
        String defaultDueTime = getDefaultTimeForDueDate();
        if (StringUtils.isNotBlank(defaultDueTime)) {
            validTime = new CircUtilController().validateTime(defaultDueTime);
        }
        if (validTime) {
            StringTokenizer timeTokenizer = new StringTokenizer(defaultDueTime, ":");
            hours = timeTokenizer.hasMoreTokens() ? Integer.parseInt(timeTokenizer.nextToken()) : 0;
            minutes = timeTokenizer.hasMoreTokens() ? Integer.parseInt(timeTokenizer.nextToken()) : 0;
            seconds = timeTokenizer.hasMoreTokens() ? Integer.parseInt(timeTokenizer.nextToken()) : 0;
        }
        loanDueDate.setHours(hours);
        loanDueDate.setMinutes(minutes);
        loanDueDate.setSeconds(seconds);
        return loanDueDate;
    }

    public String getDefaultTimeForDueDate() {
        return getParameterValueResolver().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.DEFAULT_TIME_FOR_DUE_DATE);
    }

    private Date processDueDateAndGracePeriod(Date loanDueDate, Map<String, Map<String, String>> openAndClosingTimeForTheGivenDayFromWeekList) {
        Map<String, String> openTime = openAndClosingTimeForTheGivenDayFromWeekList.get("openTime");
        Calendar calendar = resolveDateTime(openTime, loanDueDate);
        loanDueDate = calendar.getTime();
        loanDueDate = handleGracePeriod(loanDueDate);
        return loanDueDate;
    }


    private boolean inDueDateFallsBeforeOpeningTimeOfLibrary(Map<String, String> openTime, Date loanDueDate) {
        Calendar calendar = resolveDateTime(openTime, loanDueDate);
        Calendar loanDueDateCalendar = Calendar.getInstance();
        loanDueDateCalendar.setTime(loanDueDate);
        return calendar.after(loanDueDateCalendar);
    }

    private Date getLoanDueDate(String loanPeriod) {
        Date loanDueDate = null;
        if (StringUtils.isNotBlank(loanPeriod)) {
            if (loanPeriod.equalsIgnoreCase(OLEConstants.FIXED_DUE_DATE)) {
                loanDueDate = new FixedDateUtil().getFixedDateByPolicyId(getPolicyId());
            } else {
                StringTokenizer stringTokenizer = new StringTokenizer(loanPeriod, "-");
                String amount = stringTokenizer.nextToken();
                period = stringTokenizer.nextToken();

                if (period.equalsIgnoreCase("m")) {
                    loanDueDate = DateUtils.addMinutes(getTimeToCalculateFrom(), Integer.parseInt(amount));
                } else if (period.equalsIgnoreCase("h")) {
                    loanDueDate = DateUtils.addHours(getTimeToCalculateFrom(), Integer.parseInt(amount));
                } else if (period.equalsIgnoreCase("d")) {
                    loanDueDate = DateUtils.addDays(getTimeToCalculateFrom(), Integer.parseInt(amount));
                } else if (period.equalsIgnoreCase("w")) {
                    loanDueDate = DateUtils.addWeeks(getTimeToCalculateFrom(), Integer.parseInt(amount));
                }
            }
        }
        return loanDueDate;
    }

    private Date handleGracePeriod(Date loanDueDate) {
        Date updatedDate = null;
        String gracePeriod = getGracePeriodForIncludingNonWorkingHours();
        if (StringUtils.isNotBlank(gracePeriod)) {
            StringTokenizer stringTokenizer = new StringTokenizer(gracePeriod, "-");
            String amount = stringTokenizer.nextToken();
            String interval = stringTokenizer.nextToken();
            if (interval.equalsIgnoreCase("m")) {
                updatedDate = DateUtils.addMinutes(loanDueDate, Integer.valueOf(amount));
            } else if (interval.equalsIgnoreCase("h")) {
                updatedDate = DateUtils.addHours(loanDueDate, Integer.valueOf(amount));
            }
        } else {
            updatedDate = loanDueDate;
        }
        return updatedDate;
    }

    public Boolean includeNonWorkingHours() {
        return getParameterValueResolver().getParameterAsBoolean(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG);
    }


    private boolean compareTimes(Map<String, String> openTimeForTheGivenDay, Map<String, String> closingTimeForTheGivenDay, Date loanDueDate) {
        Calendar closeTimeCalendar;
        if (isOpenTimeGreaterThanCloseTime(openTimeForTheGivenDay, closingTimeForTheGivenDay)) {
            closeTimeCalendar = resolveDateTime(closingTimeForTheGivenDay, DateUtils.addDays(loanDueDate, 1));
        }else {
            closeTimeCalendar = resolveDateTime(closingTimeForTheGivenDay, loanDueDate);
        }
        Calendar openTimeCalendar = resolveDateTime(openTimeForTheGivenDay, loanDueDate);
        //Compares for the givne day if the loan due time falls within the closing time
        return (openTimeCalendar.getTime().after(loanDueDate) || openTimeCalendar.getTime().compareTo(loanDueDate) <= 0 && closeTimeCalendar.getTime().compareTo(loanDueDate) >= 0);
    }

    public boolean isOpenTimeGreaterThanCloseTime(Map<String, String> openTimeForTheGivenDay, Map<String, String> closingTimeForTheGivenDay) {
        String openTime = openTimeForTheGivenDay.keySet().iterator().next();
        String closeTime = closingTimeForTheGivenDay.keySet().iterator().next();
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        try {
            Date openDateTime = parser.parse(openTime);
            Date closeDateTime = parser.parse(closeTime);
            if (openDateTime.after(closeDateTime)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Calendar resolveDateTime(Map<String, String> closingTimeForTheGivenDay, Date loanDueDate) {
        String time = closingTimeForTheGivenDay.keySet().iterator().next();
        StringTokenizer timeTokenizer = new StringTokenizer(time, ":");
        int hour = Integer.parseInt(timeTokenizer.nextToken());

        Calendar instance = Calendar.getInstance();
        //The date is being set to the loan due date to ensure the comparisons are for the given day.
        instance.setTime(loanDueDate);
        //The hour and minutes are for closing times.
        instance.set(Calendar.HOUR_OF_DAY, hour);
        instance.set(Calendar.MINUTE, Integer.parseInt(timeTokenizer.nextToken()));
        return instance;
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
        return getParameterValueResolver().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.GRACE_PERIOD_FOR_NON_WORKING_HOURS);
    }


    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public boolean isLoanDueTimeWithinWorkingHours(Date loanDueDate, List<? extends OleBaseCalendarWeek> oleBaseCalendarWeekList) {
        Map<String, Map<String, String>> openAndClosingTimeForTheGivenDay = getOpenAndClosingTimeForTheGivenDay(loanDueDate, oleBaseCalendarWeekList);
        return compareTimes(openAndClosingTimeForTheGivenDay.get("openTime"), openAndClosingTimeForTheGivenDay.get("closeTime"), loanDueDate);
    }

    private Map<String, Map<String, String>> getOpenAndClosingTimeForTheGivenDay(Date loanDueDate, List<? extends OleBaseCalendarWeek> oleBaseCalendarWeekList) {
        Map<String, Map<String, String>> openingAndClosingTimeMap;

        openingAndClosingTimeMap = getOpenAndClosingTimeForTheGivenDayFromWeekList(loanDueDate, oleBaseCalendarWeekList);

        return openingAndClosingTimeMap;
    }


    public Map<String, Map<String, String>> getOpenAndClosingTimeForTheGivenDayFromWeekList(Date loanDueDate, List<? extends OleBaseCalendarWeek> oleCalendarWeekList) {
        int day = loanDueDate.getDay();
        Map<String, Map<String, String>> openingAndClosingTimeMap = new HashMap<>();
        Map<String, String> closingTimeMap = new HashMap<>();
        Map<String, String> openingTimeMap = new HashMap<>();

        for (Iterator<? extends OleBaseCalendarWeek> iterator = oleCalendarWeekList.iterator(); iterator.hasNext(); ) {
            OleBaseCalendarWeek OleBaseCalendarWeek = iterator.next();
            String startDay = OleBaseCalendarWeek.getStartDay();
            String endDay = OleBaseCalendarWeek.getEndDay();
            if(startDay.equals(endDay)) {
                if (startDay.equals(String.valueOf(day))) {
                    resolveOpenAndCloseTimesForCalendarWeek(closingTimeMap, openingTimeMap, OleBaseCalendarWeek);
                    break;
                }
            }
            else if ((day >= Integer.valueOf(startDay)) && day <= Integer.valueOf(endDay)) {
                resolveOpenAndCloseTimesForCalendarWeek(closingTimeMap, openingTimeMap, OleBaseCalendarWeek);
                break;
            }
           /* //The start day may not always be Sunday (0); Hence the check.
            if ((Integer.valueOf(startDay) < Integer.valueOf(endDay)) && (day > Integer.valueOf(startDay) && day < Integer.valueOf(endDay))) {
              //  if (day > Integer.valueOf(startDay) && day <= Integer.valueOf(endDay)) {
                    resolveOpenAndCloseTimesForCalendarWeek(closingTimeMap, openingTimeMap, OleBaseCalendarWeek);
                    break;
              //  }
            }
            else {
                if (day > Integer.valueOf(startDay) || day <= Integer.valueOf(endDay)) {
                    resolveOpenAndCloseTimesForCalendarWeek(closingTimeMap, openingTimeMap, OleBaseCalendarWeek);
                    break;
                }
            }*/
        }
        openingAndClosingTimeMap.put("openTime", openingTimeMap);
        openingAndClosingTimeMap.put("closeTime", closingTimeMap);
        return openingAndClosingTimeMap;
    }

    private void resolveOpenAndCloseTimesForCalendarWeek(Map<String, String> closingTimeMap, Map<String, String> openingTimeMap, OleBaseCalendarWeek oleBaseCalendarWeek) {
        String closeTime = oleBaseCalendarWeek.getCloseTime();
        String closeTimeSession = oleBaseCalendarWeek.getCloseTimeSession();
        closingTimeMap.put(closeTime, closeTimeSession);

        String openTime = oleBaseCalendarWeek.getOpenTime();
        String openTimeSession = oleBaseCalendarWeek.getOpenTimeSession();
        openingTimeMap.put(openTime, openTimeSession);
    }

    public OleCalendar getActiveCalendar() {
        return activeCalendar;
    }

    public void setActiveCalendar(OleCalendar activeCalendar) {
        this.activeCalendar = activeCalendar;
    }

    public ParameterValueResolver getParameterValueResolver() {
        if (null == parameterValueResolver) {
            parameterValueResolver = ParameterValueResolver.getInstance();
        }
        return parameterValueResolver;
    }

    public void setParameterValueResolver(ParameterValueResolver parameterValueResolver) {
        this.parameterValueResolver = parameterValueResolver;
    }

    public Date getTimeToCalculateFrom() {
        if (timeToCalculateFrom == null) {
            timeToCalculateFrom = new Date();
        }
        return timeToCalculateFrom;
    }

    public void setTimeToCalculateFrom(Date timeToCalculateFrom) {
        this.timeToCalculateFrom = timeToCalculateFrom;
    }
}
