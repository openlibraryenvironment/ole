package org.kuali.ole.deliver.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.joda.time.Interval;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.calendar.bo.*;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenchulakshmig on 12/11/15.
 */
public class FineDateTimeUtil {

    private static final Logger LOG = Logger.getLogger(FineDateTimeUtil.class);

    private ParameterValueResolver parameterValueResolver;
    private BusinessObjectService businessObjectService;

    public ParameterValueResolver getParameterValueResolver() {
        if (null == parameterValueResolver) {
            parameterValueResolver = ParameterValueResolver.getInstance();
        }
        return parameterValueResolver;
    }

    public void setParameterValueResolver(ParameterValueResolver parameterValueResolver) {
        this.parameterValueResolver = parameterValueResolver;
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

    public Double calculateOverdueFine(String deskId, Date dueDate, Date checkInDate, ItemFineRate itemFineRate) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        Double fineAmt = 0.0;
        Long difference = checkInDate.getTime() - dueDate.getTime();

        if (difference > 0) {
            String fineMode = itemFineRate.getInterval();
            if (StringUtils.isNotBlank(fineMode)) {
                Double fineRate = itemFineRate.getFineRate();

                if (fineMode.equalsIgnoreCase("h")) {
                    if (isIncludeNonWorkingHoursForFineCalc()) {
                        long numberOfHours = calculateTotalHours(difference);
                        fineAmt = numberOfHours * fineRate;
                    } else {
                        long numberOfWorkingHours = calculateWorkingHours(deskId, dueDate, checkInDate);
                        fineAmt = numberOfWorkingHours * fineRate;
                    }
                } else if (fineMode.equalsIgnoreCase("d")) {
                    if (isIncludeNonWorkingHoursForFineCalc()) {
                        long numberOfDays = calculateTotalDays(dueDate, checkInDate);
                        fineAmt = numberOfDays * fineRate;
                    } else {
                        long numberOfWorkingDays = calculateWorkingDays(deskId, dueDate, checkInDate);
                        fineAmt = numberOfWorkingDays * fineRate;
                    }
                }
            }
        }
        oleStopWatch.end();
        LOG.info("Time taken to calculate overdue fine " + oleStopWatch.getTotalTime());
        return fineAmt;
    }

    private long calculateTotalHours(Long difference) {
        long numberOfWorkingHrs = TimeUnit.MILLISECONDS.toHours(difference);
        long numberOfWorkingMin = TimeUnit.MILLISECONDS.toMinutes(difference);
        if ((numberOfWorkingMin / 60.0 - numberOfWorkingHrs) != 0) {
            numberOfWorkingHrs++;
        }
        return numberOfWorkingHrs;
    }

    private long calculateTotalDays(Date dueDate, Date checkInDate) {
        long numberOfDays = 0;
        while (checkInDate.getTime() > dueDate.getTime()) {
            if (DateUtils.isSameDay(dueDate, checkInDate)) {
                break;
            } else {
                numberOfDays++;
                dueDate = DateUtils.addDays(dueDate, 1);
            }
        }
        return numberOfDays;
    }

    private long calculateWorkingDays(String deskId, Date dueDate, Date checkInDate) {
        long numberOfWorkingDays = 0;
        List<OleCalendar> oleCalendarList = getOleCalendars(deskId);
        if (CollectionUtils.isNotEmpty(oleCalendarList)) {
            while (checkInDate.getTime() > dueDate.getTime()) {
                if (DateUtils.isSameDay(dueDate, checkInDate)) {
                    break;
                } else {
                    OleCalendar activeCalendar = getActiveCalendar(oleCalendarList, dueDate);
                    if (activeCalendar != null) {
                        HashMap timeMap = getOpenAndCloseTime(activeCalendar, dueDate);
                        if (timeMap != null && timeMap.size() == 2) {
                            numberOfWorkingDays++;
                        }
                    }
                    dueDate = DateUtils.addDays(dueDate, 1);
                }
            }
        }
        return numberOfWorkingDays;
    }

    private long calculateWorkingHours(String deskId, Date dueDate, Date checkInDate) {
        long numberOfWorkingMin = 0;
        Date dueDateTime = dueDate;
        List<OleCalendar> oleCalendarList = getOleCalendars(deskId);
        if (CollectionUtils.isNotEmpty(oleCalendarList)) {
            while (checkInDate.getTime() > dueDateTime.getTime() || DateUtils.isSameDay(dueDateTime, checkInDate)) {
                OleCalendar activeCalendar = getActiveCalendar(oleCalendarList, dueDateTime);
                if (activeCalendar != null) {
                    HashMap timeMap = getOpenAndCloseTime(activeCalendar, dueDateTime);
                    if (timeMap != null && timeMap.size() == 2) {

                        String openTime = (String) timeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME);
                        String closeTime = (String) timeMap.get(OLEConstants.CALENDAR_GET_CLOSE_TIME);

                        String[] openTimes = openTime.split(":");
                        int openTimeInMin = Integer.parseInt(openTimes[0]) * 60 + Integer.parseInt(openTimes[1]);

                        String[] closeTimes = closeTime.split(":");
                        int closeTimeInMin = Integer.parseInt(closeTimes[0]) * 60 + Integer.parseInt(closeTimes[1]);

                        if (openTimeInMin > closeTimeInMin) {
                            closeTimeInMin = closeTimeInMin + (24 * 60);
                        }

                        int checkInDateInMin = checkInDate.getHours() * 60 + checkInDate.getMinutes();
                        int dueDateInMin = dueDateTime.getHours() * 60 + dueDateTime.getMinutes();

                        if (DateUtils.isSameDay(dueDateTime, checkInDate)) {
                            if (DateUtils.isSameDay(dueDate, checkInDate)) {
                                if (checkInDateInMin < closeTimeInMin) {
                                    closeTimeInMin = checkInDateInMin;
                                }
                                if (dueDateInMin > openTimeInMin) {
                                    openTimeInMin = dueDateInMin;
                                }
                                if (closeTimeInMin > openTimeInMin) {
                                    numberOfWorkingMin = numberOfWorkingMin + (closeTimeInMin - openTimeInMin);
                                }
                            } else {
                                if (checkInDateInMin > openTimeInMin && checkInDateInMin < closeTimeInMin) {
                                    numberOfWorkingMin = numberOfWorkingMin + (checkInDateInMin - openTimeInMin);
                                } else if (checkInDateInMin >= closeTimeInMin) {
                                    numberOfWorkingMin = numberOfWorkingMin + (closeTimeInMin - openTimeInMin);
                                }
                            }
                        } else {
                            if (DateUtils.isSameDay(dueDate, dueDateTime)) {
                                if (dueDateInMin > openTimeInMin && dueDateInMin < closeTimeInMin) {
                                    numberOfWorkingMin = numberOfWorkingMin + (closeTimeInMin - dueDateInMin);
                                } else if (dueDateInMin <= openTimeInMin) {
                                    numberOfWorkingMin = numberOfWorkingMin + (closeTimeInMin - openTimeInMin);
                                }
                            } else {
                                numberOfWorkingMin = numberOfWorkingMin + (closeTimeInMin - openTimeInMin);
                            }
                        }
                    }
                }
                dueDateTime = DateUtils.addDays(dueDateTime, 1);
            }
        }
        long numberOfWorkingHrs = TimeUnit.MINUTES.toHours(numberOfWorkingMin);
        if ((numberOfWorkingMin / 60.0 - numberOfWorkingHrs) != 0) {
            numberOfWorkingHrs++;
        }
        return numberOfWorkingHrs;
    }

    private OleCalendar getActiveCalendar(List<OleCalendar> oleCalendarList, Date date) {
        OleCalendar activeCalendar = null;
        for (OleCalendar oleCalendar : oleCalendarList) {
            if (oleCalendar != null && oleCalendar.getBeginDate() != null) {
                if (oleCalendar.getEndDate() != null) {
                    Interval interval = new Interval(oleCalendar.getBeginDate().getTime(), oleCalendar.getEndDate().getTime());
                    if (date != null && interval.contains(date.getTime())) {
                        activeCalendar = oleCalendar;
                    }
                } else {
                    activeCalendar = oleCalendar;
                }
            }
        }
        return activeCalendar;
    }

    public List<OleCalendar> getOleCalendars(String deskId) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        List<OleCalendar> oleCalendarList = null;
        if (StringUtils.isNotBlank(deskId)) {
            OleCirculationDesk oleCirculationDesk = getBusinessObjectService().findBySinglePrimaryKey(OleCirculationDesk.class, deskId);
            if (oleCirculationDesk != null) {
                String calendarGroupId = oleCirculationDesk.getCalendarGroupId();
                if (StringUtils.isNotBlank(calendarGroupId)) {
                    Map calMap = new HashMap();
                    calMap.put("calendarGroupId", calendarGroupId);
                    oleCalendarList = (List<OleCalendar>) getBusinessObjectService().findMatching(OleCalendar.class, calMap);
                }
            }
        }
        oleStopWatch.end();
        LOG.error("Time taken to get calendar List" + oleStopWatch.getTotalTime());
        return oleCalendarList;
    }

    private HashMap getOpenAndCloseTime(OleCalendar oleCalendar, Date date) {
        List<OleCalendarExceptionDate> oleCalendarExceptionDateList = oleCalendar.getOleCalendarExceptionDateList();
        List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList = oleCalendar.getOleCalendarExceptionPeriodList();
        List<OleCalendarWeek> oleCalendarWeekList = oleCalendar.getOleCalendarWeekList();

        boolean isHoliday = false;
        isHoliday = isGivenDateInExceptionDateHolidayList(date, oleCalendarExceptionDateList);
        if (isHoliday) {
            return null;
        }

        isHoliday = isGivenDateInExceptionPeriodHolidayList(date, oleCalendarExceptionPeriodList);
        if (isHoliday) {
            return null;
        }

        HashMap<String, String> timeMap = new HashMap<>();
        timeMap = getOpenAndCloseTimeFromExceptionDateList(date, oleCalendarExceptionDateList);
        if (timeMap != null) {
            return timeMap;
        }

        timeMap = getOpenAndCloseTimeFromExceptionPeriodList(date, oleCalendarExceptionPeriodList);
        if (timeMap != null) {
            return timeMap;
        }

        timeMap = getOpenAndCloseTimeFromWeekList(date, oleCalendarWeekList);
        if (timeMap != null) {
            return timeMap;
        }

        return null;
    }

    private boolean isGivenDateInExceptionDateHolidayList(Date date, List<OleCalendarExceptionDate> oleCalendarExceptionDateList) {
        if (CollectionUtils.isNotEmpty(oleCalendarExceptionDateList)) {
            for (OleCalendarExceptionDate oleCalendarExceptionDate : oleCalendarExceptionDateList) {
                if (oleCalendarExceptionDate != null && oleCalendarExceptionDate.getExceptionType().equalsIgnoreCase(OLEConstants.CALENDAR_EXCEPTION_TYPE)) {
                    if (oleCalendarExceptionDate.getDate() != null && DateUtils.isSameDay(oleCalendarExceptionDate.getDate(), date)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isGivenDateInExceptionPeriodHolidayList(Date date, List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList) {
        if (CollectionUtils.isNotEmpty(oleCalendarExceptionPeriodList)) {
            for (OleCalendarExceptionPeriod oleCalendarExceptionPeriod : oleCalendarExceptionPeriodList) {
                if (oleCalendarExceptionPeriod != null && oleCalendarExceptionPeriod.getExceptionPeriodType().equalsIgnoreCase(OLEConstants.CALENDAR_EXCEPTION_TYPE)
                        && oleCalendarExceptionPeriod.getBeginDate() != null && oleCalendarExceptionPeriod.getEndDate() != null) {
                    Interval calendarExceptionPeriodInterval = new Interval(oleCalendarExceptionPeriod.getBeginDate().getTime(), oleCalendarExceptionPeriod.getEndDate().getTime());
                    if (calendarExceptionPeriodInterval.contains(date.getTime())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private HashMap getOpenAndCloseTimeFromExceptionDateList(Date date, List<OleCalendarExceptionDate> oleCalendarExceptionDateList) {
        if (CollectionUtils.isNotEmpty(oleCalendarExceptionDateList)) {
            for (OleCalendarExceptionDate oleCalendarExceptionDate : oleCalendarExceptionDateList) {
                if (oleCalendarExceptionDate != null && oleCalendarExceptionDate.getExceptionType().equalsIgnoreCase("Partial")) {
                    if (oleCalendarExceptionDate.getDate() != null && DateUtils.isSameDay(oleCalendarExceptionDate.getDate(), date)) {
                        HashMap<String, String> timeMap = new HashMap<>();
                        timeMap.put(OLEConstants.CALENDAR_GET_OPEN_TIME, oleCalendarExceptionDate.getOpenTime());
                        timeMap.put(OLEConstants.CALENDAR_GET_CLOSE_TIME, oleCalendarExceptionDate.getCloseTime());
                        return timeMap;
                    }
                }
            }
        }
        return null;
    }

    private HashMap getOpenAndCloseTimeFromExceptionPeriodList(Date date, List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList) {
        if (CollectionUtils.isNotEmpty(oleCalendarExceptionPeriodList)) {
            for (OleCalendarExceptionPeriod oleCalendarExceptionPeriod : oleCalendarExceptionPeriodList) {
                if (oleCalendarExceptionPeriod != null && oleCalendarExceptionPeriod.getExceptionPeriodType().equalsIgnoreCase("Partial")
                        && oleCalendarExceptionPeriod.getBeginDate() != null && oleCalendarExceptionPeriod.getEndDate() != null) {
                    Interval calendarExceptionPeriodInterval = new Interval(oleCalendarExceptionPeriod.getBeginDate().getTime(), oleCalendarExceptionPeriod.getEndDate().getTime());
                    if (calendarExceptionPeriodInterval.contains(date.getTime())) {
                        List<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodWeekList = oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList();
                        if (CollectionUtils.isNotEmpty(oleCalendarExceptionPeriodWeekList)) {
                            for (OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek : oleCalendarExceptionPeriodWeekList) {

                                int startDay = Integer.parseInt(oleCalendarExceptionPeriodWeek.getStartDay());
                                int endDay = Integer.parseInt(oleCalendarExceptionPeriodWeek.getEndDay());
                                String openTime = oleCalendarExceptionPeriodWeek.getOpenTime();
                                String closeTime = oleCalendarExceptionPeriodWeek.getCloseTime();
                                boolean eachDayOfExceptionWeek = oleCalendarExceptionPeriodWeek.isEachDayOfExceptionWeek();

                                HashMap<String, String> timeMap = getOpenAndCloseTimeForTheGivenDay(startDay, endDay, date.getDay(), openTime, closeTime, eachDayOfExceptionWeek);
                                if (timeMap != null) {
                                    return timeMap;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private HashMap getOpenAndCloseTimeFromWeekList(Date date, List<OleCalendarWeek> oleCalendarWeekList) {
        if (CollectionUtils.isNotEmpty(oleCalendarWeekList)) {
            for (OleCalendarWeek oleCalendarWeek : oleCalendarWeekList) {
                int startDay = Integer.parseInt(oleCalendarWeek.getStartDay());
                int endDay = Integer.parseInt(oleCalendarWeek.getEndDay());
                String openTime = oleCalendarWeek.getOpenTime();
                String closeTime = oleCalendarWeek.getCloseTime();
                boolean eachDayOfWeek = oleCalendarWeek.isEachDayWeek();
                HashMap<String, String> timeMap = getOpenAndCloseTimeForTheGivenDay(startDay, endDay, date.getDay(), openTime, closeTime, eachDayOfWeek);
                if (timeMap != null) {
                    return timeMap;
                }
            }
        }
        return null;
    }

    private HashMap getOpenAndCloseTimeForTheGivenDay(int startDay, int endDay, int dateDay, String openTime, String closeTime, boolean eachDayOfWeek) {
        HashMap<String, String> timeMap = new HashMap<>();
        if ((startDay == dateDay) && (dateDay == endDay)) {
            timeMap.put(OLEConstants.CALENDAR_GET_OPEN_TIME, openTime);
            timeMap.put(OLEConstants.CALENDAR_GET_CLOSE_TIME, closeTime);
            return timeMap;
        } else if ((startDay > endDay && (dateDay >= startDay || dateDay <= endDay))
                || (startDay < endDay && dateDay >= startDay && dateDay <= endDay)) {
            if (eachDayOfWeek) {
                timeMap.put(OLEConstants.CALENDAR_GET_OPEN_TIME, openTime);
                timeMap.put(OLEConstants.CALENDAR_GET_CLOSE_TIME, closeTime);
                return timeMap;
            } else {
                if (dateDay == startDay) {
                    timeMap.put(OLEConstants.CALENDAR_GET_OPEN_TIME, openTime);
                    timeMap.put(OLEConstants.CALENDAR_GET_CLOSE_TIME, "23:59");
                } else if (dateDay == endDay) {
                    timeMap.put(OLEConstants.CALENDAR_GET_OPEN_TIME, "00:00");
                    timeMap.put(OLEConstants.CALENDAR_GET_CLOSE_TIME, closeTime);
                } else {
                    timeMap.put(OLEConstants.CALENDAR_GET_OPEN_TIME, "00:00");
                    timeMap.put(OLEConstants.CALENDAR_GET_CLOSE_TIME, "23:59");
                }
                return timeMap;
            }
        }
        return null;
    }

    public Boolean isIncludeNonWorkingHoursForFineCalc() {
        return getParameterValueResolver().getParameterAsBoolean(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.FINE_FLAG);
    }
}