package org.kuali.ole.deliver.calendar.service.impl;

import org.apache.log4j.Logger;
import org.joda.time.Interval;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.calendar.bo.*;
import org.kuali.ole.deliver.calendar.controller.OleCalendarController;
import org.kuali.ole.deliver.calendar.service.DateUtil;
import org.kuali.ole.deliver.calendar.service.OleCalendarService;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: arjuns
 * Date: 7/27/13
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */


public class OleCalendarServiceImpl implements OleCalendarService {

    private static final Logger LOG = Logger.getLogger(OleCalendarController.class);

    private BusinessObjectService businessObjectService;

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    private int totalWorkingDays = 0;
    private int totalDays = 0;
    private ParameterValueResolver parameterValueResolver;

    public ParameterValueResolver getParameterValueResolver() {
        if(parameterValueResolver==null){
            parameterValueResolver=ParameterValueResolver.getInstance();
        }
        return parameterValueResolver;
    }

    public void setParameterValueResolver(ParameterValueResolver parameterValueResolver) {
        this.parameterValueResolver = parameterValueResolver;
    }

    public OleCalendarGroup getCalendarGroup(String deskId) {
        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, deskId);
        List<OleCalendarGroup> oleCalendarGroupList = (List<OleCalendarGroup>) getBusinessObjectService().findMatching(OleCalendarGroup.class, calendarGroup);
        if (oleCalendarGroupList != null && oleCalendarGroupList.size() > 0) {
            return oleCalendarGroupList.get(0);
        }
        return null;
    }


    @Override
    public OleCalendar getActiveCalendar(Timestamp date, String groupId) {
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, groupId);
        List<OleCalendar> oleCalendarList = (List<OleCalendar>) getBusinessObjectService().findMatching(OleCalendar.class, cg);
        for (OleCalendar calendar : oleCalendarList) {
            if (calendar.getBeginDate() != null && date != null && calendar.getEndDate() != null && isCalendarExists(calendar.getBeginDate(), calendar.getEndDate())) {
                calendar=continuousWeek(calendar);
                return calendar;
            } else if (calendar.getBeginDate() != null && calendar.getEndDate() == null) {
                calendar=continuousWeek(calendar);
                return calendar;
            }
        }
        return null;
    }

    public OleCalendar continuousWeek(OleCalendar oleCalendar){     //each day of a week
        int week=-1;
        List<OleCalendarWeek> oleCalendarWeekList=new ArrayList<OleCalendarWeek>();
        for(OleCalendarWeek oleCalendarWeek:oleCalendar.getOleCalendarWeekList()){
            if(!oleCalendarWeek.isEachDayWeek() && !oleCalendarWeek.getEndDay().equals(oleCalendarWeek.getStartDay())){
                if(Integer.parseInt(oleCalendarWeek.getStartDay())<Integer.parseInt(oleCalendarWeek.getEndDay())){
                    for(int strt=new Integer(oleCalendarWeek.getStartDay());strt<=new Integer(oleCalendarWeek.getEndDay());strt++){
                        OleCalendarWeek oleCalendarWeek1=new OleCalendarWeek();
                        oleCalendarWeek1.setCalendarId(oleCalendarWeek.getCalendarId());
                        oleCalendarWeek1.setStartDay(String.valueOf(strt));
                        oleCalendarWeek1.setEndDay(String.valueOf(strt));
                        oleCalendarWeek1.setOpenTimeSession(oleCalendarWeek.getOpenTimeSession());
                        oleCalendarWeek1.setCloseTimeSession(oleCalendarWeek.getCloseTimeSession());
                        oleCalendarWeek1.setOpenTime("00:00");
                        oleCalendarWeek1.setCloseTime("23:59");
                        if(strt==new Integer(oleCalendarWeek.getStartDay())){
                            oleCalendarWeek1.setOpenTime(oleCalendarWeek.getOpenTime());
                        }
                        else if(strt==new Integer(oleCalendarWeek.getEndDay())){
                            oleCalendarWeek1.setCloseTime(oleCalendarWeek.getCloseTime());
                        }
                        oleCalendarWeekList.add(week+1,oleCalendarWeek1);
                    }
                }
                else{
                    for(int strt=new Integer(oleCalendarWeek.getStartDay());strt<7;strt++){
                        OleCalendarWeek oleCalendarWeek1=new OleCalendarWeek();
                        oleCalendarWeek1.setCalendarId(oleCalendarWeek.getCalendarId());
                        oleCalendarWeek1.setStartDay(String.valueOf(strt));
                        oleCalendarWeek1.setEndDay(String.valueOf(strt));
                        oleCalendarWeek1.setOpenTimeSession(oleCalendarWeek.getOpenTimeSession());
                        oleCalendarWeek1.setCloseTimeSession(oleCalendarWeek.getCloseTimeSession());
                        oleCalendarWeek1.setOpenTime("00:00");
                        oleCalendarWeek1.setCloseTime("23:59");
                        if(strt==new Integer(oleCalendarWeek.getStartDay())){
                            oleCalendarWeek1.setOpenTime(oleCalendarWeek.getOpenTime());
                        }
                        else if(strt==new Integer(oleCalendarWeek.getEndDay())){
                            oleCalendarWeek1.setCloseTime(oleCalendarWeek.getCloseTime());
                        }
                        oleCalendarWeekList.add(week+1,oleCalendarWeek1);
                        if(strt>=6){
                            for(int end=0;end<=new Integer(oleCalendarWeek.getEndDay());end++){
                                oleCalendarWeek1=new OleCalendarWeek();
                                oleCalendarWeek1.setCalendarId(oleCalendarWeek.getCalendarId());
                                oleCalendarWeek1.setStartDay(String.valueOf(end));
                                oleCalendarWeek1.setEndDay(String.valueOf(end));
                                oleCalendarWeek1.setOpenTimeSession(oleCalendarWeek.getOpenTimeSession());
                                oleCalendarWeek1.setCloseTimeSession(oleCalendarWeek.getCloseTimeSession());
                                oleCalendarWeek1.setOpenTime("00:00");
                                oleCalendarWeek1.setCloseTime("23:59");
                                if(end==new Integer(oleCalendarWeek.getStartDay())){
                                    oleCalendarWeek1.setOpenTime(oleCalendarWeek.getOpenTime());
                                }
                                else if(end==new Integer(oleCalendarWeek.getEndDay())){
                                    oleCalendarWeek1.setCloseTime(oleCalendarWeek.getCloseTime());
                                }
                                oleCalendarWeekList.add(week+1,oleCalendarWeek1);
                            }
                        }
                    }

                }
            }
            else{
                oleCalendarWeekList.add(oleCalendarWeek);
            }
        }
        oleCalendar.setOleCalendarWeekList(oleCalendarWeekList);
        for(OleCalendarExceptionPeriod oleCalendarExceptionPeriod:oleCalendar.getOleCalendarExceptionPeriodList()){
            int period=-1;
            List<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodWeekList = new ArrayList<OleCalendarExceptionPeriodWeek>();
            for(OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek:oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList()){
                if(!oleCalendarExceptionPeriodWeek.isEachDayOfExceptionWeek() && !oleCalendarExceptionPeriodWeek.getEndDay().equals(oleCalendarExceptionPeriodWeek.getStartDay())){
                    if(Integer.parseInt(oleCalendarExceptionPeriodWeek.getStartDay())<Integer.parseInt(oleCalendarExceptionPeriodWeek.getEndDay())){
                        for(int strt=new Integer(oleCalendarExceptionPeriodWeek.getStartDay());strt<=new Integer(oleCalendarExceptionPeriodWeek.getEndDay());strt++){
                            OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek1=new OleCalendarExceptionPeriodWeek();
                            oleCalendarExceptionPeriodWeek1.setStartDay(String.valueOf(strt));
                            oleCalendarExceptionPeriodWeek1.setEndDay(String.valueOf(strt));
                            oleCalendarExceptionPeriodWeek1.setOpenTimeSession("AM");
                            oleCalendarExceptionPeriodWeek1.setCloseTimeSession("PM");
                            oleCalendarExceptionPeriodWeek1.setOpenTime("00:00");
                            oleCalendarExceptionPeriodWeek1.setCloseTime("23:59");
                            oleCalendarExceptionPeriodWeek1.setEachDayOfExceptionWeek(true);
                            if(strt==new Integer(oleCalendarExceptionPeriodWeek.getStartDay())){
                                oleCalendarExceptionPeriodWeek1.setOpenTime(oleCalendarExceptionPeriodWeek.getOpenTime());
                            }
                            else if(strt==new Integer(oleCalendarExceptionPeriodWeek.getEndDay())){
                                oleCalendarExceptionPeriodWeek1.setCloseTime(oleCalendarExceptionPeriodWeek.getCloseTime());
                            }
                            oleCalendarExceptionPeriodWeekList.add(period+1,oleCalendarExceptionPeriodWeek1);
                        }
                    }
                    else{
                        for(int strt=new Integer(oleCalendarExceptionPeriodWeek.getStartDay());strt<7;strt++){
                            OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek1=new OleCalendarExceptionPeriodWeek();
                            oleCalendarExceptionPeriodWeek1.setStartDay(String.valueOf(strt));
                            oleCalendarExceptionPeriodWeek1.setEndDay(String.valueOf(strt));
                            oleCalendarExceptionPeriodWeek1.setOpenTimeSession("AM");
                            oleCalendarExceptionPeriodWeek1.setCloseTimeSession("PM");
                            oleCalendarExceptionPeriodWeek1.setOpenTime("00:00");
                            oleCalendarExceptionPeriodWeek1.setCloseTime("23:59");
                            oleCalendarExceptionPeriodWeek1.setEachDayOfExceptionWeek(true);
                            if(strt==new Integer(oleCalendarExceptionPeriodWeek.getStartDay())){
                                oleCalendarExceptionPeriodWeek1.setOpenTime(oleCalendarExceptionPeriodWeek.getOpenTime());
                            }
                            else if(strt==new Integer(oleCalendarExceptionPeriodWeek.getEndDay())){
                                oleCalendarExceptionPeriodWeek1.setCloseTime(oleCalendarExceptionPeriodWeek.getCloseTime());
                            }
                            oleCalendarExceptionPeriodWeekList.add(period+1,oleCalendarExceptionPeriodWeek1);
                            if(strt>=6){
                                for(int end=0;end<=new Integer(oleCalendarExceptionPeriodWeek.getEndDay());end++){
                                    oleCalendarExceptionPeriodWeek1=new OleCalendarExceptionPeriodWeek();
                                    oleCalendarExceptionPeriodWeek1.setStartDay(String.valueOf(strt));
                                    oleCalendarExceptionPeriodWeek1.setEndDay(String.valueOf(strt));
                                    oleCalendarExceptionPeriodWeek1.setOpenTimeSession("AM");
                                    oleCalendarExceptionPeriodWeek1.setCloseTimeSession("PM");
                                    oleCalendarExceptionPeriodWeek1.setOpenTime("00:00");
                                    oleCalendarExceptionPeriodWeek1.setCloseTime("23:59");
                                    oleCalendarExceptionPeriodWeek1.setEachDayOfExceptionWeek(true);
                                    if(strt==new Integer(oleCalendarExceptionPeriodWeek.getStartDay())){
                                        oleCalendarExceptionPeriodWeek1.setOpenTime(oleCalendarExceptionPeriodWeek.getOpenTime());
                                    }
                                    else if(strt==new Integer(oleCalendarExceptionPeriodWeek.getEndDay())){
                                        oleCalendarExceptionPeriodWeek1.setCloseTime(oleCalendarExceptionPeriodWeek.getCloseTime());
                                    }
                                    oleCalendarExceptionPeriodWeekList.add(period+1,oleCalendarExceptionPeriodWeek1);
                                }
                            }
                        }

                    }
                }
                else{
                    oleCalendarExceptionPeriodWeekList.add(oleCalendarExceptionPeriodWeek);
                }
            }
            oleCalendarExceptionPeriod.setOleCalendarExceptionPeriodWeekList(oleCalendarExceptionPeriodWeekList);
        }
    return oleCalendar;
    }

    //
    public boolean isCalendarExists(Timestamp fromDate, Timestamp toDate) {
        Interval interval=new Interval(fromDate.getTime(),toDate.getTime());
        return interval.contains(System.currentTimeMillis());
    }


    private boolean isHoliday(String deskId, Timestamp currentDate) {
        OleCalendarGroup ocg = getCalendarGroup(deskId);
        if(ocg == null)
            return false;
        OleCalendar oc = getActiveCalendar(currentDate, ocg.getCalendarGroupId());
        boolean holidayExceptionType = false;

        List<OleCalendarExceptionDate> oleCalendarExceptionDates = oc!=null && oc.getOleCalendarExceptionDateList()!=null ? oc.getOleCalendarExceptionDateList() : new ArrayList<OleCalendarExceptionDate>();
        for (OleCalendarExceptionDate exceptionDate : oleCalendarExceptionDates) {
            if (exceptionDate.getExceptionType().equals(OLEConstants.CALENDAR_EXCEPTION_TYPE) && currentDate != null && currentDate.toString().split(" ")[0].equals(exceptionDate.getDate().toString().split(" ")[0])) {

                holidayExceptionType = true;
                break;

            }
        }
        if (!holidayExceptionType) {
            HashMap<String, String> timeMap = new HashMap<String, String>();
            timeMap = getOpenAndCloseTime(deskId, currentDate);
            if (timeMap.size() != 2) {
                holidayExceptionType = true;
            }
        }
        return holidayExceptionType;
    }


    //Due date calculation - days input
    public Timestamp calculateDueDate(String deskId, String timePeriod, Timestamp currentDate) {

        Integer count = Integer.parseInt(timePeriod != null ? timePeriod.split("-")[0] : "0");
        while (count > 0) {
            boolean temp = false;
            if (isHoliday(deskId, currentDate)) {
                temp = true;
            }
            if (temp) {
                currentDate = DateUtil.addDays(currentDate, 1);
            } else {
                count = count - 1;
                currentDate = DateUtil.addDays(currentDate, 1);
            }
        }
        if (isHoliday(deskId, currentDate)) {
            currentDate = nextWorkingDay(deskId, currentDate);

        }
        HashMap<String, String> timeMap = getOpenAndCloseTime(deskId, currentDate);
        float openTime = 0;
        float closeTime = 0;
        float currentTime = 0;

        if (timeMap.size() > 0) {
            String[] openTimes = timeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME) != null ? timeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME).split(":") : new String[0];
            String openTimeHrs = openTimes.length > 0 ? openTimes[0] : null;
            String openTimeMin = openTimes.length > 1 ? openTimes[1] : null;
            if (openTimeHrs != null && openTimeMin != null) {
                openTime = Float.parseFloat(openTimeHrs + "." + openTimeMin);
            }
            String[] closeTimes = timeMap.get(OLEConstants.CALENDAR_GET_CLOSE_TIME) != null ? timeMap.get(OLEConstants.CALENDAR_GET_CLOSE_TIME).split(":") : new String[0];
            String closeTimeHrs = closeTimes.length > 0 ? closeTimes[0] : null;
            String closeTimeMin = closeTimes.length > 1 ? closeTimes[1] : null;
            if (closeTimeHrs != null && closeTimeMin != null) {
                closeTime = Float.parseFloat(closeTimeHrs + "." + closeTimeMin);
            }
            String[] currentTimeFromDate = currentDate.toString().split(" ");
            String[] currentTimes = currentTimeFromDate.length > 1 && currentTimeFromDate[1] != null ? currentTimeFromDate[1].split(":") : new String[0];
            String currentTimeHrs = currentTimes.length > 0 ? currentTimes[0] : null;
            String currentTimeMin = currentTimes.length > 1 ? currentTimes[1] : null;
            if (currentTimeHrs != null && currentTimeMin != null) {
                currentTime = Float.parseFloat(currentTimeHrs + "." + currentTimeMin);
            }
            if (currentTime < openTime) {
                currentDate = Timestamp.valueOf(new StringBuilder().append(currentDate.toString().split(" ")[0]).append(" ").append(timeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME)).append(":00").toString());
            }
            else if (currentTime > closeTime) {
                boolean holidayExceptionType = true;
                Timestamp temp;

                int count1 = 1;
                while (holidayExceptionType) {

                    temp = DateUtil.addDays(currentDate, count1);
                    timeMap = getOpenAndCloseTime(deskId, temp);
                    holidayExceptionType = isHoliday(deskId, temp);
                    if (holidayExceptionType == false) {
                        timeMap = getOpenAndCloseTime(deskId, temp);
                        currentDate = Timestamp.valueOf(new StringBuilder().append(temp.toString().split(" ")[0]).append(" ").append(timeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME)).append(":00").toString());
                    }
                    count1++;
                }//while ends
            }else {
                currentDate = Timestamp.valueOf(new StringBuilder().append(currentDate.toString().split(" ")[0]).append(" ").append(timeMap.get(OLEConstants.CALENDAR_GET_CLOSE_TIME)).append(":00").toString());
            }

        }
        return currentDate;

    }//end calculateDueDate method..

    public Timestamp nextWorkingDay(String deskId, Timestamp currentDate) {
        boolean holidayExceptionType = true;
        Timestamp temp;
        int count = 1;
        while (holidayExceptionType) {

            temp = DateUtil.addDays(currentDate, count);
            holidayExceptionType = isHoliday(deskId, temp);
            if (holidayExceptionType == false) {
                return temp;
            }

            count++;

        }//while ends
        return null;
    }

    //Due date calculation - hours input
    public Timestamp calculateDueDateHrs(String deskId, String timePeriod, Timestamp currentDate) {
        boolean isDay = false;
        String[] timePeriods = timePeriod.split("-");
        String timePeriodType = timePeriods.length > 1 ? timePeriods[1] : "";
        Timestamp destinationTimestamp = null;
        Timestamp destinationTemp = null;
        String sysFlag =getParameterValueResolver().getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.CALENDER_FLAG);
        if (timePeriodType != null && timePeriodType.equalsIgnoreCase("H")) {
            destinationTemp = DateUtil.addHours(currentDate, new Integer(timePeriods.length > 0 ? timePeriods[0] : "0"));
        }
        if (timePeriodType != null && timePeriodType.equalsIgnoreCase("D")) {
            isDay =  true;
            destinationTemp = DateUtil.addDays(currentDate, Integer.parseInt(timePeriods.length > 0 ? timePeriods[0] : "0"));
        }
        if (sysFlag.equalsIgnoreCase("true")) {
            HashMap<String, String> timeMap = new HashMap<String, String>();
            if (isHoliday(deskId, destinationTemp)) { //holiday
                destinationTemp = nextWorkingDay(deskId, destinationTemp);
                timeMap = getOpenAndCloseTime(deskId, destinationTemp);
                destinationTimestamp = Timestamp.valueOf(new StringBuilder().append(destinationTemp.toString().split(" ")[0]).append(" ").append(timeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME)).append(":00").toString());
            } else {
                timeMap = getOpenAndCloseTime(deskId, destinationTemp);
                long openTime = Timestamp.valueOf(new StringBuilder().append(destinationTemp.toString().split(" ")[0]).append(" ").append(timeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME)).append(":00").toString()).getTime();
                long closeTime = Timestamp.valueOf(new StringBuilder().append(destinationTemp.toString().split(" ")[0]).append(" ").append(timeMap.get(OLEConstants.CALENDAR_GET_CLOSE_TIME)).append(":00").toString()).getTime();
                long currentTime = destinationTemp.getTime();
                if (currentTime < openTime) {
                    destinationTimestamp = Timestamp.valueOf(new StringBuilder().append(destinationTemp.toString().split(" ")[0]).append(" ").append(timeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME)).append(":00").toString());
                } else if (currentTime >= openTime && currentTime <= closeTime) {
                    if(isDay){
                        destinationTimestamp = Timestamp.valueOf(new StringBuilder().append(destinationTemp.toString().split(" ")[0]).append(" ").append(timeMap.get(OLEConstants.CALENDAR_GET_CLOSE_TIME)).append(":00").toString());
                    }else{
                        destinationTimestamp = destinationTemp;
                    }

                } else {
                    destinationTemp = nextWorkingDay(deskId, destinationTemp);
                    timeMap = getOpenAndCloseTime(deskId, destinationTemp);
                    destinationTimestamp = Timestamp.valueOf(new StringBuilder().append(destinationTemp.toString().split(" ")[0]).append(" ").append(timeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME)).append(":00").toString());
                }

            }
        } else {
            Long currentTimeMillis = currentDate.getTime();
            Long loanHoursMillis = (Long.parseLong(timePeriod.split("-")[0])) * 60 * 60 * 1000;
            Long currentDayStartTime;
            Long currentDayEndTime;
            Long workingMillisLeft;
            Long tempLoanHoursMillis = loanHoursMillis;
            if (!isHoliday(deskId, currentDate)) {
                HashMap<String, String> startEndTimeMap = getOpenAndCloseTime(deskId, currentDate);
                // currentDayStartTime = Timestamp.valueOf(new StringBuilder().append(currentDate.toString().split(" ")[0]).append(" ").append(startEndTimeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME)).append(":00").toString()).getTime();
                currentDayEndTime = Timestamp.valueOf(new StringBuilder().append(currentDate.toString().split(" ")[0]).append(" ").append(startEndTimeMap.get(OLEConstants.CALENDAR_GET_CLOSE_TIME)).append(":00").toString()).getTime();
                workingMillisLeft = currentDayEndTime - currentTimeMillis;
                loanHoursMillis = loanHoursMillis - workingMillisLeft;
                Timestamp nextDay = currentDate;
                if (loanHoursMillis <= 0) {
                    nextDay = new Timestamp(currentTimeMillis + tempLoanHoursMillis);
                }
                //while ends
                while (loanHoursMillis > 0) {
                    boolean holidayExceptionType = true;
                    //   int count = 1;
                    while (holidayExceptionType) {
                        nextDay = DateUtil.addDays(nextDay, 1);
                        holidayExceptionType = isHoliday(deskId, nextDay);
                        //      count++;
                    }
                    startEndTimeMap = getOpenAndCloseTime(deskId, nextDay);
                    currentDayStartTime = Timestamp.valueOf(new StringBuilder().append(nextDay.toString().split(" ")[0]).append(" ").append(startEndTimeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME)).append(":00").toString()).getTime();
                    currentDayEndTime = Timestamp.valueOf(new StringBuilder().append(nextDay.toString().split(" ")[0]).append(" ").append(startEndTimeMap.get(OLEConstants.CALENDAR_GET_CLOSE_TIME)).append(":00").toString()).getTime();
                    workingMillisLeft = currentDayEndTime - currentDayStartTime;
                    tempLoanHoursMillis = loanHoursMillis;
                    loanHoursMillis = loanHoursMillis - workingMillisLeft;
                    if (loanHoursMillis <= 0) {
                        nextDay = new Timestamp(currentDayStartTime + tempLoanHoursMillis);
                    }
                }
                if (nextDay != null) {
                    destinationTimestamp = nextDay;
                }
            }
        }
        return destinationTimestamp;
    }


    public Float calculateFine(String deskId, Timestamp dueDate, Timestamp currentDate, String fineAmount) {

        String difference = String.valueOf(currentDate.getTime() - dueDate.getTime());
        Long diffInt = new Long(difference);
        Float fineRate = 0.0f;

        if (diffInt > 0) {

            if (fineAmount != null && fineAmount.contains("/")) {
                String[] fineCal = fineAmount.split("/");
                String fineAmt = fineCal[0];
                String fineMode = fineCal[1];
                String sysFlag = getParameterValueResolver().getParameter(OLEConstants
                        .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.FINE_FLAG);
                if (fineMode != null && fineMode.equalsIgnoreCase("H")) {
                    Integer numberOfHours = 0;
                    if (sysFlag.equalsIgnoreCase("true")) {
                        numberOfHours = getHoursDiff(dueDate, currentDate);
                    } else {
                        numberOfHours = workingHours(deskId, dueDate, currentDate);
                    }
                    fineRate = numberOfHours * Float.parseFloat(fineAmt);
                } else if (fineMode != null && fineMode.equalsIgnoreCase("D")) {
                    workingDays(deskId, dueDate, currentDate);
                    if (sysFlag.equalsIgnoreCase("true")) {
                        fineRate = totalDays * Float.parseFloat(fineAmt);
                    } else {
                        fineRate = totalWorkingDays * Float.parseFloat(fineAmt);
                    }
                }
            }
        }
        return fineRate;

    }

    public Integer getHoursDiff(java.util.Date dateOne, java.util.Date dateTwo) {
        if (dateOne != null && dateTwo != null && dateOne.compareTo(dateTwo) <= 0) {
            String diff = "";
            long timeDiff = Math.abs(dateOne.getTime() - dateTwo.getTime());
            diff = String.format("%d", TimeUnit.MILLISECONDS.toHours(timeDiff),
                    -TimeUnit.HOURS.toMinutes(timeDiff));
            return new Integer(diff);
        }
        return 0;
    }

    public void workingDays(String deskId, Timestamp dueDate, Timestamp currentDate) {
        totalWorkingDays = 0;
        totalDays = 0;
        while (dueDate.getTime() < currentDate.getTime()) {
            if (currentDate.toString().split(" ")[0].equalsIgnoreCase(dueDate.toString().split(" ")[0])) {

                break;
            } else if (!currentDate.toString().split(" ")[0].equalsIgnoreCase(dueDate.toString().split(" ")[0])) {
                boolean isHoliday = isHoliday(deskId, dueDate);
                if (!isHoliday) {
                    totalWorkingDays++;

                }
                totalDays++;
                dueDate = DateUtil.addDays(dueDate, 1);
            }
        }
    }

    public Integer workingHours(String deskId, Timestamp dueDateTime, Timestamp currentDate) {
        float totalHours = 0;
        float openTime;
        float closeTime;
        int openTimeHrs;
        int closeTimeHrs;
        int totalHrs = 0;
        Timestamp dueDate=dueDateTime;
        HashMap<String, String> timeMap = new HashMap<String, String>();
        while (dueDate.getTime() <= currentDate.getTime() || currentDate.toString().split(" ")[0].equalsIgnoreCase(dueDate.toString().split(" ")[0])) {
            if (currentDate.toString().split(" ")[0].equalsIgnoreCase(dueDate.toString().split(" ")[0])) {

                timeMap = getOpenAndCloseTime(deskId, currentDate);
                openTime = Float.parseFloat(timeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME).split(":")[0] + "." + timeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME).split(":")[1]);
                openTimeHrs=Integer.parseInt(timeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME).split(":")[0]);

                if(currentDate.toString().split(" ")[0].equalsIgnoreCase(dueDateTime.toString().split(" ")[0])){
                    openTime =Float.parseFloat(dueDateTime.toString().split(" ")[1].split(":")[0]+"."+dueDateTime.toString().split(" ")[1].split(":")[1]);
                    openTimeHrs=Integer.parseInt(dueDateTime.toString().split(" ")[1].split(":")[0]);
                }
                float openTimeTemp=Float.parseFloat(currentDate.toString().split(" ")[1].split(":")[0]+"."+currentDate.toString().split(" ")[1].split(":")[1]);
                int openTimeHrsTemp=Integer.parseInt(currentDate.toString().split(" ")[1].split(":")[0]);

                totalHours = totalHours + (openTimeTemp-openTime);
                totalHrs = totalHrs + (openTimeHrsTemp-openTimeHrs);
                break;
            } else if (!currentDate.toString().split(" ")[0].equalsIgnoreCase(dueDate.toString().split(" ")[0])) {
                boolean isHoliday = isHoliday(deskId, dueDate);
                if (!isHoliday) {
                    timeMap = getOpenAndCloseTime(deskId, dueDate);
                    openTime = Float.parseFloat(timeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME).split(":")[0] + "." + timeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME).split(":")[1]);
                    closeTime = Float.parseFloat(timeMap.get(OLEConstants.CALENDAR_GET_CLOSE_TIME).split(":")[0] + "." + timeMap.get(OLEConstants.CALENDAR_GET_CLOSE_TIME).split(":")[1]);
                    closeTimeHrs=Integer.parseInt(timeMap.get(OLEConstants.CALENDAR_GET_CLOSE_TIME).split(":")[0]);
                    openTimeHrs=Integer.parseInt(timeMap.get(OLEConstants.CALENDAR_GET_OPEN_TIME).split(":")[0]);
                    if(dueDate.equals(dueDateTime)){
                        Float currentTime=Float.parseFloat(dueDateTime.toString().split(" ")[1].split(":")[0]+"."+dueDateTime.toString().split(" ")[1].split(":")[1]);
                        if(currentTime>openTime){
                            openTime=currentTime;
                        }
                        int currentTimeHrs=Integer.parseInt(dueDateTime.toString().split(" ")[1].split(":")[0]);
                        if(currentTimeHrs>openTimeHrs){
                            openTimeHrs=currentTimeHrs;
                        }
                    }
                    totalHours = totalHours + (closeTime - openTime);
                    totalHrs = totalHrs + (closeTimeHrs - openTimeHrs );
                }
                dueDate = DateUtil.addDays(dueDate, 1);
            }
        }


        return totalHrs;

    }


    //Open and Close Time of a Date
    public HashMap getOpenAndCloseTime(String deskId, Timestamp currentDate) {
        HashMap<String, String> timeMap = new HashMap<String, String>();
        OleCalendarGroup ocg = getCalendarGroup(deskId);
        OleCalendar calendar = getActiveCalendar(currentDate, ocg.getCalendarGroupId());


        List<OleCalendarWeek> oleCalendarWeekList = calendar.getOleCalendarWeekList();
        List<OleCalendarExceptionDate> oleCalendarExceptionDates = calendar.getOleCalendarExceptionDateList();
        List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList = calendar.getOleCalendarExceptionPeriodList();

        boolean temp = true;
        if (timeMap.size() == 0) {
            for (OleCalendarExceptionDate exceptionDate : oleCalendarExceptionDates) {
                if (currentDate.toString().split(" ")[0].equals(exceptionDate.getDate().toString().split(" ")[0])) {
                    timeMap.put(OLEConstants.CALENDAR_GET_OPEN_TIME, exceptionDate.getOpenTime());
                    timeMap.put(OLEConstants.CALENDAR_GET_CLOSE_TIME, convertTimeInto24HrFormat(exceptionDate.getCloseTime()));
                    temp = false;
                    break;
                }
            }
        }
        boolean exceptionPeriodFlag=true;
        if (timeMap.size() == 0) {
            for (OleCalendarExceptionPeriod exceptionPeriod : oleCalendarExceptionPeriodList) {
                if (currentDate.getTime() >= exceptionPeriod.getBeginDate().getTime() && currentDate.getTime() <= exceptionPeriod.getEndDate().getTime()) {
                    exceptionPeriodFlag=false;
                    List<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodWeekList = exceptionPeriod.getOleCalendarExceptionPeriodWeekList();
                    for (OleCalendarExceptionPeriodWeek exceptionPeriodWeek : oleCalendarExceptionPeriodWeekList) {
                        int startDay = new Integer(exceptionPeriodWeek.getStartDay());
                        int endDay = new Integer(exceptionPeriodWeek.getEndDay());
                        //if(!exceptionPeriodWeek.isEachDayOfExceptionWeek()){
                            if (startDay > endDay) {
                                if ((currentDate.getDay() >= startDay && currentDate.getDay() <= 6) || (currentDate.getDay() >= 0 && currentDate.getDay() <= endDay)) {
                                    timeMap.put(OLEConstants.CALENDAR_GET_OPEN_TIME, exceptionPeriodWeek.getOpenTime());
                                    timeMap.put(OLEConstants.CALENDAR_GET_CLOSE_TIME, convertTimeInto24HrFormat(exceptionPeriodWeek.getCloseTime()));
                                    temp = false;
                                    break;
                                }
                            } else {
                                if ((currentDate.getDay() >= startDay && currentDate.getDay() <= endDay)) {
                                    timeMap.put(OLEConstants.CALENDAR_GET_OPEN_TIME, exceptionPeriodWeek.getOpenTime());
                                    timeMap.put(OLEConstants.CALENDAR_GET_CLOSE_TIME, convertTimeInto24HrFormat(exceptionPeriodWeek.getCloseTime()));
                                    temp = false;
                                    break;
                                }
                            }  //end of else
                    }
                }


            }
        }
        if (timeMap.size() == 0 && exceptionPeriodFlag) {
            for (OleCalendarWeek week : oleCalendarWeekList) {
                int startDay = new Integer(week.getStartDay());
                int endDay = new Integer(week.getEndDay());
                if (startDay > endDay) {
                    if ((currentDate.getDay() >= startDay && currentDate.getDay() <= 6) || (currentDate.getDay() >= 0 && currentDate.getDay() <= endDay)) {
                        timeMap.put(OLEConstants.CALENDAR_GET_OPEN_TIME, week.getOpenTime());
                        timeMap.put(OLEConstants.CALENDAR_GET_CLOSE_TIME, convertTimeInto24HrFormat(week.getCloseTime()));
                        temp = false;
                        break;
                    }
                } else {
                    if ((currentDate.getDay() >= startDay && currentDate.getDay() <= endDay)) {
                        timeMap.put(OLEConstants.CALENDAR_GET_OPEN_TIME, week.getOpenTime());
                        timeMap.put(OLEConstants.CALENDAR_GET_CLOSE_TIME, convertTimeInto24HrFormat(week.getCloseTime()));
                        temp = false;
                        break;
                    }
                }  //end of else
            }
        }


        return timeMap;
    }
    //Open and Close Time of a Date

    private String convertTimeInto24HrFormat(String time){
        StringBuffer newTime = new StringBuffer();
        String[] times = time.split(":");
        if(times!=null && times.length>1 && Integer.parseInt(times[0])==0){
                newTime.append("24:"+times[1]);
        } else{
               newTime.append(time);
        }
        return newTime.toString();
    }
    public void generalInfoValidation(OleCalendar oleCalendar, boolean isNew) {
        List<OleCalendarWeek> oleCalendarWeekList = oleCalendar.getOleCalendarWeekList();

        //millitary format conversion

        DateUtil dateUtil = new DateUtil();

        if (oleCalendar.getCalendarGroupId().equals("")) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "temp.group.name");//"oleCalendarGroupId"
        }


        if (oleCalendar.getOleCalendarWeekList().size() > 0) {

            for (OleCalendarWeek oleCalendarWeek : oleCalendarWeekList) {
                if (oleCalendarWeek.getOpenTime().equals("")) {
                    GlobalVariables.getMessageMap().putErrorForSectionId("GeneralInfo", "temp.week.open.time");
                }
                if (oleCalendarWeek.getCloseTime().equals("")) {
                    GlobalVariables.getMessageMap().putErrorForSectionId("GeneralInfo", "temp.week.close.time");
                }
            }

            boolean repeat = false;

            List<Integer> dest = new ArrayList<Integer>();
            for (int i = 0; i < oleCalendarWeekList.size(); i++) {
                for (int x = new Integer(oleCalendarWeekList.get(i).getStartDay()); x <= new Integer(oleCalendarWeekList.get(i).getEndDay()); x++) {
                    if (dest.size() > 0) {
                        if (dest.contains(x)) {
                            repeat = true;
                            break;
                        } else {
                            dest.add(x);
                        }
                    } else {
                        dest.add(x);
                    }

                }


            }
            if (repeat) {
                GlobalVariables.getMessageMap().putErrorForSectionId("GeneralInfo", "temp.weak.days.overlap");

            }

//            To set exception period end date (concat with 23:59:59)
            String excptEndDateSplit = null;
            for (OleCalendarExceptionPeriod oleCalendarExceptionPeriod : oleCalendar.getOleCalendarExceptionPeriodList()) {
                if (oleCalendarExceptionPeriod.getEndDate()!=null && oleCalendarExceptionPeriod.getEndDate().toString().contains(" ")) {
                    excptEndDateSplit = oleCalendarExceptionPeriod.getEndDate().toString().split(" ")[0];
                    oleCalendarExceptionPeriod.setEndDate(Timestamp.valueOf(excptEndDateSplit + " " + "23:59:59"));
                }
            }
//            To set exception period end date (concat with 23:59:59)


            String openTimePeriodWeek = "";
            String closeTimePeriodWeek = "";
            String openTimeConvExceptionDate = "";
            String closeTimeConvExceptionDate = "";

            for (OleCalendarWeek oleCalendarWeek : oleCalendar.getOleCalendarWeekList()) {
                if (oleCalendarWeek.getOpenTime().equals("") || oleCalendarWeek.getCloseTime().equals("")) {
                    break;
                }
                String openTimeWeek = oleCalendarWeek.getOpenTime() + oleCalendarWeek.getOpenTimeSession();
                String closeTimeWeek = oleCalendarWeek.getCloseTime() + oleCalendarWeek.getCloseTimeSession();
                String openTime = "";
                String closeTime = "";

                try {
                    openTime = dateUtil.convertTo24HoursFormat(openTimeWeek);
                    closeTime = dateUtil.convertTo24HoursFormat(closeTimeWeek);
                } catch (java.text.ParseException e) {
                    LOG.error("Exception while converting to 12Hours format", e);
                }

                oleCalendarWeek.setOpenTime(openTime);
                oleCalendarWeek.setCloseTime(closeTime);
            }

            for (OleCalendarExceptionPeriod oleCalendarExceptionPeriod : oleCalendar.getOleCalendarExceptionPeriodList()) {
                for (OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek : oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList()) {
                    if(!oleCalendarExceptionPeriodWeek.getOpenTime().equals("")&& oleCalendarExceptionPeriodWeek.getOpenTime()!=null && !oleCalendarExceptionPeriodWeek.getCloseTime().equals("") && oleCalendarExceptionPeriodWeek.getCloseTime()!=null){
                        String openTimeExceptionPeriodWeek = oleCalendarExceptionPeriodWeek.getOpenTime() + oleCalendarExceptionPeriodWeek.getOpenTimeSession();
                        String closeTimeExceptionPeriodWeek = oleCalendarExceptionPeriodWeek.getCloseTime() + oleCalendarExceptionPeriodWeek.getCloseTimeSession();
                        try {
                            openTimePeriodWeek = dateUtil.convertTo24HoursFormat(openTimeExceptionPeriodWeek);
                            closeTimePeriodWeek = dateUtil.convertTo24HoursFormat(closeTimeExceptionPeriodWeek);
                        } catch (java.text.ParseException e) {
                            LOG.error("Exception while converting to 12Hours format", e);
                        }
                        oleCalendarExceptionPeriodWeek.setOpenTime(openTimePeriodWeek);
                        oleCalendarExceptionPeriodWeek.setCloseTime(closeTimePeriodWeek);
                    }

                }
            }

            for (OleCalendarExceptionDate oleCalendarExceptionDate : oleCalendar.getOleCalendarExceptionDateList()) {

                if ((!oleCalendarExceptionDate.getOpenTime().equals("")) && (!oleCalendarExceptionDate.getCloseTime().equals(""))) {
                    String openTimeExceptionDate = oleCalendarExceptionDate.getOpenTime() + oleCalendarExceptionDate.getOpenTimeSession();
                    String closeTimeExceptionDate = oleCalendarExceptionDate.getCloseTime() + oleCalendarExceptionDate.getCloseTimeSession();
                    try {
                        openTimeConvExceptionDate = dateUtil.convertTo24HoursFormat(openTimeExceptionDate);
                        closeTimeConvExceptionDate = dateUtil.convertTo24HoursFormat(closeTimeExceptionDate);
                    } catch (java.text.ParseException e) {
                        LOG.error("Exception while converting to 12Hours format", e);
                    }
                    if (!oleCalendarExceptionDate.getExceptionType().equals("Holiday")) {  //changed
                        oleCalendarExceptionDate.setOpenTime(openTimeConvExceptionDate);
                        oleCalendarExceptionDate.setCloseTime(closeTimeConvExceptionDate);

                    }
                }

            }

            if (isNew) {
                String groupId1 = oleCalendar.getCalendarGroupId();
                Map map1 = new HashMap();
                map1.put("calendarGroupId", groupId1);
                Collection<OleCalendar> calendars1 = getBusinessObjectService().findMatching(OleCalendar.class, map1);
                Integer latestCalChrSeqNo = 0;
                Integer maxChrSeqNo = 0;
                for (OleCalendar calendar1 : calendars1) {
                    if (calendar1.getChronologicalSequence() != null) {
                        latestCalChrSeqNo = Integer.parseInt(calendar1.getChronologicalSequence());
                        maxChrSeqNo = Math.max(maxChrSeqNo, latestCalChrSeqNo);
                    }
                }

                map1.put("chronologicalSequence", maxChrSeqNo.toString());
                List<OleCalendar> calendars2 = (List<OleCalendar>) getBusinessObjectService().findMatching(OleCalendar.class, map1);
                OleCalendar lastCalendar = calendars2 != null && calendars2.size() > 0 ? calendars2.get(0) : null;
                if (oleCalendar != null && oleCalendar.getBeginDate() != null && lastCalendar != null && lastCalendar.getBeginDate() != null &&
                        oleCalendar.getBeginDate().compareTo(lastCalendar.getBeginDate()) <= 0) {
                    GlobalVariables.getMessageMap().putErrorForSectionId("GeneralInfo", "temp.strt.end.date.new.cal");
                }
            }
        }
        /*chronologicalSequence strt*/
        Map<String, String> criteria = new HashMap<String, String>();
        int seq = 0;

        criteria.put("calendarGroupId", oleCalendar.getCalendarGroupId());
        List<OleCalendar> oleCalendarGroups = (List<OleCalendar>) getBusinessObjectService().findMatching(OleCalendar.class, criteria);
        if (oleCalendarGroups.size() > 0) {
            //int high=0;
            for (int i = 0; i < oleCalendarGroups.size(); i++) {
                if (seq == 0) {
                    seq = new Integer(oleCalendarGroups.get(i).getChronologicalSequence());
                } else {
                    if (seq < new Integer(oleCalendarGroups.get(i).getChronologicalSequence())) {
                        seq = new Integer(oleCalendarGroups.get(i).getChronologicalSequence());
                    }
                }
            }
            if(oleCalendar.getChronologicalSequence()== null || oleCalendar.getChronologicalSequence().equals("")){
                oleCalendar.setChronologicalSequence(new StringBuilder().append(seq + 1).toString());
            }

        } else {

            oleCalendar.setChronologicalSequence(new StringBuilder().append(seq + 1).toString());
        }
        /*chronologicalSequence end*/
    }

    public BusinessObjectService getBusinessObjectService() {
        if (this.businessObjectService == null) {
            this.businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return this.businessObjectService;
    }

    public void assignEndDate(OleCalendar oleCalendar) {

        if (GlobalVariables.getMessageMap().getErrorMessages().isEmpty()) {

            //*-*-*-Setting End-Date of a calendar while adding a new Calendar*-*-*-
            Date endDate = DateUtil.addDays(oleCalendar.getBeginDate(), -1);
            String groupId = oleCalendar.getCalendarGroupId();
            Map map = new HashMap();
            map.put("calendarGroupId", groupId);
            Collection<OleCalendar> calendars = getBusinessObjectService().findMatching(OleCalendar.class, map);
            Integer latestCalChrSeqNo = 0;
            Integer maxChrSeqNo = 0;

            for (OleCalendar calendar1 : calendars) {
                if (calendar1.getChronologicalSequence() != null) {
                    latestCalChrSeqNo = Integer.parseInt(calendar1.getChronologicalSequence());
                    maxChrSeqNo = Math.max(maxChrSeqNo, latestCalChrSeqNo);
                }
            }

            for (OleCalendar calendar : calendars) {
                if (oleCalendar.getCalendarId() != null && oleCalendar.getCalendarId().equals(calendar.getCalendarId())) {
                    break;
                }
                if ((oleCalendar.getCalendarId() == null || !oleCalendar.getCalendarId().equals(calendar.getCalendarId()))
                        && calendar.getCalendarGroupId().equalsIgnoreCase(groupId) && (maxChrSeqNo==0 || maxChrSeqNo==Integer.parseInt(calendar.getChronologicalSequence()))) {
                    String endDateSplit = null;
                    if (endDate != null) {
                        if (endDate.toString().contains(" ")) {

                            endDateSplit = endDate.toString().split(" ")[0];
                        }


                    }
                    calendar.setEndDate(Timestamp.valueOf(endDateSplit +" "+ "23:59:59"));
                    getBusinessObjectService().save(calendar);
                }
            }
        }
    }

    public void convert12HrsFormat(OleCalendar oldCalendar) {
        DateUtil dateUtil = new DateUtil();
        for (OleCalendarWeek oldCalendarWeek : oldCalendar.getOleCalendarWeekList()) {

            if (oldCalendarWeek.getOpenTime().equals("") || oldCalendarWeek.getCloseTime().equals("")) {
                break;
            }
            String convertedOpenTime = "";
            String convertCloseTime = "";
            String oleOpenTime = oldCalendarWeek.getOpenTime();
            String oleCloseTime = oldCalendarWeek.getCloseTime();
            try {
                convertedOpenTime = dateUtil.convertTo12HoursFormat(oleOpenTime);
                convertCloseTime = dateUtil.convertTo12HoursFormat(oleCloseTime);
            } catch (java.text.ParseException e) {
                LOG.error("Exception while converting to 12Hours format", e);
            }

            if (convertedOpenTime != null) {
                oldCalendarWeek.setOpenTime(convertedOpenTime.substring(0, convertedOpenTime.length() - 2));
                //oldCalendarWeek.setOpenTimeSession(convertedOpenTime.substring(convertedOpenTime.length() - 2, convertedOpenTime.length()));
            }
            if (convertCloseTime != null) {
                // oldCalendarWeek.setCloseTimeSession(convertCloseTime.substring(convertCloseTime.length() - 2, convertCloseTime.length()));
                oldCalendarWeek.setCloseTime(convertCloseTime.substring(0, convertCloseTime.length() - 2));
            }
        }


        for (OleCalendarExceptionPeriod oleCalendarExceptionPeriod : oldCalendar.getOleCalendarExceptionPeriodList()) {
            //changed
            for (OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek : oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList()) {
                if(!oleCalendarExceptionPeriodWeek.getOpenTime().equals("")&& oleCalendarExceptionPeriodWeek.getOpenTime()!=null && !oleCalendarExceptionPeriodWeek.getCloseTime().equals("") && oleCalendarExceptionPeriodWeek.getCloseTime()!=null){
                    String convOpenTimeExcptPrdWk = "";
                    String convCloseTimeExcptPrdWk = "";
                    String oleOpenTimeExcptPrdWk = oleCalendarExceptionPeriodWeek.getOpenTime();
                    String oleCloseTimeExcptPrdWk = oleCalendarExceptionPeriodWeek.getCloseTime();
                    try {
                        convOpenTimeExcptPrdWk = dateUtil.convertTo12HoursFormat(oleOpenTimeExcptPrdWk);
                        convCloseTimeExcptPrdWk = dateUtil.convertTo12HoursFormat(oleCloseTimeExcptPrdWk);
                    } catch (java.text.ParseException e) {
                        LOG.error("Exception while converting to 12Hours format", e);
                    }

                    if (convOpenTimeExcptPrdWk != null) {
                        oleCalendarExceptionPeriodWeek.setOpenTime(convOpenTimeExcptPrdWk.substring(0, convOpenTimeExcptPrdWk.length() - 2));
                        //oleCalendarExceptionPeriodWeek.setOpenTimeSession(convOpenTimeExcptPrdWk.substring(convOpenTimeExcptPrdWk.length() - 2, convOpenTimeExcptPrdWk.length()));
                    }
                    if (convCloseTimeExcptPrdWk != null) {
                        // oleCalendarExceptionPeriodWeek.setCloseTimeSession(convCloseTimeExcptPrdWk.substring(convCloseTimeExcptPrdWk.length() - 2, convCloseTimeExcptPrdWk.length()));
                        oleCalendarExceptionPeriodWeek.setCloseTime(convCloseTimeExcptPrdWk.substring(0, convCloseTimeExcptPrdWk.length() - 2));
                    }
                }

            }
        }


        for (OleCalendarExceptionDate oleCalendarExceptionDate : oldCalendar.getOleCalendarExceptionDateList()) {
            String convertedOpenTimeExcptDate = "";
            String convertedCloseTimeExcptDate = "";
            String oleOpenTimeExcptDate = oleCalendarExceptionDate.getOpenTime();
            String oleCloseTimeExcptDate = oleCalendarExceptionDate.getCloseTime();
            if ((!oleCalendarExceptionDate.getOpenTime().equals("")) && (!oleCalendarExceptionDate.getCloseTime().equals(""))) { //changed
                try {
                    convertedOpenTimeExcptDate = dateUtil.convertTo12HoursFormat(oleOpenTimeExcptDate);
                    convertedCloseTimeExcptDate = dateUtil.convertTo12HoursFormat(oleCloseTimeExcptDate);
                } catch (java.text.ParseException e) {
                    LOG.error("Exception while converting to 12Hours format", e);  //To change body of catch statement use File | Settings | File Templates.
                }

                if (convertedOpenTimeExcptDate != null) {
                    oleCalendarExceptionDate.setOpenTime(convertedOpenTimeExcptDate.substring(0, convertedOpenTimeExcptDate.length() - 2));
                    //  oleCalendarExceptionDate.setOpenTimeSession(convertedOpenTimeExcptDate.substring(convertedOpenTimeExcptDate.length() - 2, convertedOpenTimeExcptDate.length()));
                }
                if (convertedCloseTimeExcptDate != null) {
                    //  oleCalendarExceptionDate.setCloseTimeSession(convertedCloseTimeExcptDate.substring(convertedCloseTimeExcptDate.length() - 2, convertedCloseTimeExcptDate.length()));
                    oleCalendarExceptionDate.setCloseTime(convertedCloseTimeExcptDate.substring(0, convertedCloseTimeExcptDate.length() - 2));
                }
            }
        }
    }

    public void validateCalendarDocument(OleCalendar oleCalendar){
        oleCalendar.setEndDateNoMessage(null);
        oleCalendar.setEndDateYesMessage(null);
        oleCalendar.setEndDateYesFlag(false);
        oleCalendar.setEndDateNoFlag(false);
        oleCalendar.setMessage("");
        OleCalendar calendar = null;
        String groupId = oleCalendar.getCalendarGroupId();
        Map map = new HashMap();
        map.put("calendarGroupId", groupId);
        Collection<OleCalendar> calendars = getBusinessObjectService().findMatching(OleCalendar.class, map);
        int cseq = 0;
        for (OleCalendar oleCalendar1 : calendars) {
            if (cseq < Integer.parseInt(oleCalendar1.getChronologicalSequence())) {
                cseq = Integer.parseInt(oleCalendar1.getChronologicalSequence());
            }
        }
        if (oleCalendar.getCalendarId() != null && !oleCalendar.getCalendarId().equals("") && oleCalendar.getChronologicalSequence() != null && !oleCalendar.getChronologicalSequence().equals("")) { //for edit
            Map calMap = new HashMap();
            calMap.put("calendarId", oleCalendar.getCalendarId());
            calendar = getBusinessObjectService().findByPrimaryKey(OleCalendar.class, calMap);
            String beginDate = null;
            String beginDateDataBase = null;
            String endDate = null;
            String endDateDataBase = null;
            if (cseq != Integer.parseInt(oleCalendar.getChronologicalSequence())) {
                if (oleCalendar.getEndDate() != null && oleCalendar.getEndDate().toString().contains(" ")) {
                    endDate = oleCalendar.getEndDate().toString().split(" ")[0];
                }
                if (oleCalendar.getBeginDate() != null && oleCalendar.getBeginDate().toString().contains(" ")) {
                    beginDate = oleCalendar.getBeginDate().toString().split(" ")[0];
                }
                if (calendar.getEndDate() != null && calendar.getEndDate().toString().contains(" ")) {
                    endDateDataBase = calendar.getEndDate().toString().split(" ")[0];
                }
                if (calendar.getBeginDate() != null && calendar.getBeginDate().toString().contains(" ")) {
                    beginDateDataBase = calendar.getBeginDate().toString().split(" ")[0];
                }
                if (beginDate != null && beginDateDataBase != null && !beginDate.equals(beginDateDataBase)) {
                    oleCalendar.setMessage("This Calendar End-Date cannot be edited");
                }
                if (endDate != null && endDateDataBase != null && !endDate.equals(endDateDataBase)) {
                    oleCalendar.setMessage("This Calendar End-Date cannot be edited");
                }
            }
            if (oleCalendar.getMessage().equals("This Calendar End-Date cannot be edited")) {

                oleCalendar.setBeginDate(calendar.getBeginDate());
                oleCalendar.setEndDate(calendar.getEndDate());

            }
        }

        if (oleCalendar.getChronologicalSequence() != null && !oleCalendar.getChronologicalSequence().equals("") && oleCalendar.getEndDate()!=null) {
            if (cseq == 0 || cseq == Integer.parseInt(oleCalendar.getChronologicalSequence())) {
                oleCalendar.setEndDateYesMessage("By entering an End Date this calendar will become inactive and there is no future calendar.");
                oleCalendar.setEndDateYesFlag(true);
            }
        }
        if (oleCalendar.getChronologicalSequence().equals("")&& oleCalendar.getEndDate()!=null) {
            oleCalendar.setEndDateYesMessage("By entering an End Date this calendar will become inactive and there is no future calendar.");
            oleCalendar.setEndDateYesFlag(true);
        }

        if (calendars.size() >= 1 && (oleCalendar.getCalendarId() == null || oleCalendar.getCalendarId().equals(""))) {
            oleCalendar.setEndDateNoMessage("The Previous calendars End-Date will be changed to ensure no gaps are created.");
            oleCalendar.setEndDateNoFlag(true);
        }
    }

    public void deleteCalendarDocument(OleCalendar oleCalendar){
        OleCalendar prevCalendar = null;
        OleCalendar nextCalendar = null;
        try {
            if (oleCalendar != null && oleCalendar.getCalendarId() != null) {
                Map map = new HashMap<>();
                map.put("calendarGroupId", oleCalendar.getCalendarGroupId());
                List<OleCalendar> calendars = (List<OleCalendar>) getBusinessObjectService().findMatchingOrderBy(OleCalendar.class, map, "CL_SEQ", true);
                for (int i = 0; i < calendars.size(); i++) {
                    OleCalendar calendar = calendars.get(i);
                    if (calendar.getChronologicalSequence().equalsIgnoreCase(oleCalendar.getChronologicalSequence())) {

                        prevCalendar = (i - 1) >= 0 ? calendars.get(i - 1) : null;
                        nextCalendar = (i + 1) < calendars.size() ? calendars.get(i + 1) : null;
                        if (nextCalendar == null && prevCalendar != null) {
                            prevCalendar.setEndDate(null);
                            getBusinessObjectService().save(prevCalendar);
                        } else if (nextCalendar != null && prevCalendar != null) {
                            Timestamp prevCalNewEndDate = DateUtil.addDays(nextCalendar.getBeginDate(),-1);
                            prevCalendar.setEndDate(prevCalNewEndDate);
                            getBusinessObjectService().save(prevCalendar);
                        }

                    }
                }
                getBusinessObjectService().delete(oleCalendar);
            } else {
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_MESSAGES, OLEConstants.OlePatron.ERROR_PATRON_NOT_FOUND);
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new RuntimeException();
        }
    }
}
