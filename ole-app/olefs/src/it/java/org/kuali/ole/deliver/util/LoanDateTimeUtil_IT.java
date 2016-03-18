package org.kuali.ole.deliver.util;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.calendar.bo.*;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.util.*;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;

/**
 * Created by pvsubrah on 10/2/15.
 */
public class LoanDateTimeUtil_IT {
    private OleCalendar mockOleCalendar;

    @Ignore
    @Test
    public void getCal() throws Exception {
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        List<OleCalendar> oleCalendars = (List<OleCalendar>) businessObjectService.findAll(OleCalendar.class);
        assertNotNull(oleCalendars);
        for (Iterator<OleCalendar> iterator = oleCalendars.iterator(); iterator.hasNext(); ) {
            OleCalendar oleCalendar = iterator.next();
            String calendarDescription = oleCalendar.getCalendarDescription();
            System.out.println("Cal Desc: " + calendarDescription);
            Timestamp beginDate = oleCalendar.getBeginDate();
            System.out.println("Begin Date: " + beginDate);
            Timestamp endDate = oleCalendar.getEndDate();
            System.out.println("End Date: " + endDate);
            String calendarGroupId = oleCalendar.getCalendarGroupId();
            System.out.println("Cal group id: " + calendarGroupId);
            List<OleCalendarExceptionDate> oleCalendarExceptionDateList =
                    oleCalendar.getOleCalendarExceptionDateList();
            for (Iterator<OleCalendarExceptionDate> oleCalendarExceptionDateIterator = oleCalendarExceptionDateList.iterator(); oleCalendarExceptionDateIterator.hasNext(); ) {
                OleCalendarExceptionDate oleCalendarExceptionDate = oleCalendarExceptionDateIterator.next();
                String openTime = oleCalendarExceptionDate.getOpenTime();
                System.out.println("Open Time during exception days: " + openTime);
                String closeTime = oleCalendarExceptionDate.getCloseTime();
                System.out.println("Close Time during exception days: " + closeTime);
            }
            List<OleCalendarWeek> oleCalendarWeekList = oleCalendar.getOleCalendarWeekList();
            assertNotNull(oleCalendarWeekList);
            System.out.println("\n***********************\n");
        }
    }

    /*Calculate due date with loan period in minutes*/
    @Test
    public void calculateDueDateForAmountOfMinutes() throws Exception {
        String loanPeriod = "30-M";
        Date dueDate = new LoanDateTimeUtil().calculateDateTimeByPeriod(loanPeriod, null);
        assertNotNull(dueDate);
        System.out.println("Added 30 minutes:" + dueDate + "\n");
    }

    /*Calculate due date with loan period in days*/
    @Test
    public void calculateDueDateForAmountOfDays() throws Exception {
        String loanPeriod = "1-D";
        Date dueDate = new LoanDateTimeUtil().calculateDateTimeByPeriod(loanPeriod, null);
        assertNotNull(dueDate);
        System.out.println("Added 1 day:" +dueDate+ "\n");
    }

    /*Calculate due date with loan period in hours*/
    @Test
    public void calculateDueDateForAmountOfHours() throws Exception {
        String loanPeriod = "2-H";
        Date dueDate = new LoanDateTimeUtil().calculateDateTimeByPeriod(loanPeriod, null);
        assertNotNull(dueDate);
        System.out.println("Added 2 hours:" +dueDate+ "\n");
    }

    /*Calculate due date with loan period in weeks*/
    @Test
    public void calculateDueDateForAmountOfWeeks() throws Exception {
        String loanPeriod = "2-W";
        Date dueDate = new LoanDateTimeUtil().calculateDateTimeByPeriod(loanPeriod, null);
        assertNotNull(dueDate);
        System.out.println("Added 2 Weeks:" +dueDate+ "\n");
    }

    /*Testcase for given date is in between calender begin date and end date date interval */
    @Test
    public void calExists() throws Exception {
        LoanDateTimeUtil loanDateTimeUtil = new LoanDateTimeUtil();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        boolean calendarExists = loanDateTimeUtil.calendarExists(new Timestamp(System.currentTimeMillis()), new Timestamp(calendar.getTimeInMillis()), null);
        assertTrue(calendarExists);
    }

    /*Testcase for given date is not in between calender begin date and end date date interval */
    @Test
    public void calDoesNotExists() throws Exception {
        LoanDateTimeUtil loanDateTimeUtil = new LoanDateTimeUtil();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        boolean calendarExists = loanDateTimeUtil.calendarExists(new Timestamp(calendar.getTimeInMillis()),new Timestamp(System.currentTimeMillis()), null);
        assertFalse(calendarExists);
    }


    //This test will fail depending on what time of the day its run. So, adjust the input to validate the test.
    //TODO: Will need to be refactored
    /*Testcase for comparing two dates */
    @Test
    public void compareTime() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,-5);
        String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        StringTokenizer timeTokenizer = new StringTokenizer(time, ":");

        Calendar instance = Calendar.getInstance();
        int hour = Integer.parseInt(timeTokenizer.nextToken());
        instance.set(Calendar.HOUR_OF_DAY, hour);
        instance.set(Calendar.MINUTE, Integer.parseInt(timeTokenizer.nextToken()));

        Date date = new Date(System.currentTimeMillis());

        boolean before = instance.getTime().before(date);
        assertTrue(before);
    }

    /*Testcase for due date, due time will be default time for due date parameter value*/
    @Test
    public void loanDueDateWithDefaultDueTimeParameterValueAsTime() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        mockOleCalendar = new OleCalendar();

        ArrayList<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setOleCalendar(mockOleCalendar);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        mockOleCalendar.setBeginDate(new Timestamp(calendar.getTimeInMillis()));
        oleCalendarWeek.setEachDayWeek(true);
        oleCalendarWeek.setOpenTime("02:00");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTime("20:00");
        oleCalendarWeek.setCloseTimeSession("PM");
        oleCalendarWeek.setStartDay("0");
        oleCalendarWeek.setEndDay("6");
        oleCalendarWeekList.add(oleCalendarWeek);

        mockOleCalendar.setOleCalendarWeekList(oleCalendarWeekList);
        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);


        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtil();
        Date date = loanDateTimeUtil.calculateDateTimeByPeriod("3-d", oleCirculationDesk);
        assertNotNull(date);
        System.out.println(date);
    }

    /*Testcase for due date, due time will be set to 23:59:59(because default time for due date parameter value is blank)*/
    @Test
    public void loanDueDateWithDefaultTime() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        mockOleCalendar = new OleCalendar();

        ArrayList<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setOleCalendar(mockOleCalendar);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        mockOleCalendar.setBeginDate(new Timestamp(calendar.getTimeInMillis()));
        oleCalendarWeek.setEachDayWeek(true);
        oleCalendarWeek.setOpenTime("02:00");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTime("20:00");
        oleCalendarWeek.setCloseTimeSession("PM");
        oleCalendarWeek.setStartDay("0");
        oleCalendarWeek.setEndDay("6");
        oleCalendarWeekList.add(oleCalendarWeek);

        mockOleCalendar.setOleCalendarWeekList(oleCalendarWeekList);
        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);


        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtilDefaultDueTimeBlank();
        Date date = loanDateTimeUtil.calculateDateTimeByPeriod("3-d", oleCirculationDesk);
        assertNotNull(date);
        System.out.println(date);
    }

    /*Testcase for due date time within library working hours*/
    @Test
    public void loanPeriodFallsWithinWorkingHours() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        mockOleCalendar = new OleCalendar();

        ArrayList<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setOleCalendar(mockOleCalendar);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        mockOleCalendar.setBeginDate(new Timestamp(calendar.getTimeInMillis()));
        oleCalendarWeek.setEachDayWeek(true);
        oleCalendarWeek.setOpenTime("02:00");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTime("20:00");
        oleCalendarWeek.setCloseTimeSession("PM");
        oleCalendarWeek.setStartDay("0");
        oleCalendarWeek.setEndDay("6");
        oleCalendarWeekList.add(oleCalendarWeek);

        mockOleCalendar.setOleCalendarWeekList(oleCalendarWeekList);
        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);


        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtil();
        Date date = loanDateTimeUtil.calculateDateTimeByPeriod("24-h", oleCirculationDesk);
        assertNotNull(date);
        System.out.println(date);
    }

    /*Testcase for due date exceed the library working hours and includeNonWorkingHours is false*/
    @Test
    public void loanPeriodExceedWorkingHours() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        mockOleCalendar = new OleCalendar();

        ArrayList<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setOleCalendar(mockOleCalendar);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        mockOleCalendar.setBeginDate(new Timestamp(calendar.getTimeInMillis()));
        oleCalendarWeek.setEachDayWeek(true);
        oleCalendarWeek.setOpenTime("02:00");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTime("06:00");
        oleCalendarWeek.setCloseTimeSession("AM");
        oleCalendarWeek.setStartDay("0");
        oleCalendarWeek.setEndDay("6");
        oleCalendarWeekList.add(oleCalendarWeek);

        mockOleCalendar.setOleCalendarWeekList(oleCalendarWeekList);
        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);


        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtil();
        Date date = loanDateTimeUtil.calculateDateTimeByPeriod("24-h", oleCirculationDesk);
        assertNotNull(date);
        System.out.println(date);
    }

    /*Testcase for due date in library holiday date and includeNonWorkingHours is false*/
    @Test
    public void loanPeriodInExceptionDate() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        mockOleCalendar = new OleCalendar();

        ArrayList<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setOleCalendar(mockOleCalendar);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        mockOleCalendar.setBeginDate(new Timestamp(calendar.getTimeInMillis()));
        oleCalendarWeek.setEachDayWeek(true);
        oleCalendarWeek.setOpenTime("02:00");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTime("20:00");
        oleCalendarWeek.setCloseTimeSession("PM");
        oleCalendarWeek.setStartDay("0");
        oleCalendarWeek.setEndDay("6");
        oleCalendarWeekList.add(oleCalendarWeek);

        OleCalendarExceptionDate oleCalendarExceptionDate = new OleCalendarExceptionDate();
        oleCalendarExceptionDate.setExceptionType(OLEConstants.CALENDAR_EXCEPTION_TYPE);
        oleCalendarExceptionDate.setDate(DateUtils.addDays(new Date(), 1));

        List<OleCalendarExceptionDate> oleCalendarExceptionDates = new ArrayList<>();
        oleCalendarExceptionDates.add(oleCalendarExceptionDate);


        mockOleCalendar.setOleCalendarExceptionDateList(oleCalendarExceptionDates);

        mockOleCalendar.setOleCalendarWeekList(oleCalendarWeekList);
        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);


        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtil();
        Date date = loanDateTimeUtil.calculateDateTimeByPeriod("24-h", oleCirculationDesk);
        assertNotNull(date);
        System.out.println(date);
    }

    /*Testcase for due date in library holiday date and includeNonWorkingHours is false*/
    @Test
    public void loanPeriodInExceptionDateWithExcludingNonWorkingHours() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        mockOleCalendar = new OleCalendar();

        ArrayList<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setOleCalendar(mockOleCalendar);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        mockOleCalendar.setBeginDate(new Timestamp(calendar.getTimeInMillis()));
        oleCalendarWeek.setEachDayWeek(true);
        oleCalendarWeek.setOpenTime("02:00");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTime("15:00");
        oleCalendarWeek.setCloseTimeSession("PM");
        oleCalendarWeek.setStartDay("0");
        oleCalendarWeek.setEndDay("6");
        oleCalendarWeekList.add(oleCalendarWeek);

        OleCalendarExceptionDate oleCalendarExceptionDate = new OleCalendarExceptionDate();
        oleCalendarExceptionDate.setExceptionType(OLEConstants.CALENDAR_EXCEPTION_TYPE);
        oleCalendarExceptionDate.setDate(DateUtils.addDays(new Date(), 1));

        List<OleCalendarExceptionDate> oleCalendarExceptionDates = new ArrayList<>();
        oleCalendarExceptionDates.add(oleCalendarExceptionDate);


        mockOleCalendar.setOleCalendarExceptionDateList(oleCalendarExceptionDates);

        mockOleCalendar.setOleCalendarWeekList(oleCalendarWeekList);
        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);


        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtil();
        Date date = loanDateTimeUtil.calculateDateTimeByPeriod("24-h", oleCirculationDesk);
        assertNotNull(date);
        System.out.println(date);
    }

    /*Testcase for due date in library holiday date and includeNonWorkingHours is true*/
    @Test
    public void loanPeriodInExceptionDateWithIncludeNonWorkingHours() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        mockOleCalendar = new OleCalendar();

        ArrayList<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setOleCalendar(mockOleCalendar);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        mockOleCalendar.setBeginDate(new Timestamp(calendar.getTimeInMillis()));
        oleCalendarWeek.setEachDayWeek(true);
        oleCalendarWeek.setOpenTime("02:00");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTime("15:00");
        oleCalendarWeek.setCloseTimeSession("PM");
        oleCalendarWeek.setStartDay("0");
        oleCalendarWeek.setEndDay("6");
        oleCalendarWeekList.add(oleCalendarWeek);

        OleCalendarExceptionDate oleCalendarExceptionDate = new OleCalendarExceptionDate();
        oleCalendarExceptionDate.setExceptionType(OLEConstants.CALENDAR_EXCEPTION_TYPE);
        oleCalendarExceptionDate.setDate(DateUtils.addDays(new Date(), 1));

        List<OleCalendarExceptionDate> oleCalendarExceptionDates = new ArrayList<>();
        oleCalendarExceptionDates.add(oleCalendarExceptionDate);


        mockOleCalendar.setOleCalendarExceptionDateList(oleCalendarExceptionDates);

        mockOleCalendar.setOleCalendarWeekList(oleCalendarWeekList);
        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);


        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtilIncludeNonWorkingHours();
        Date date = loanDateTimeUtil.calculateDateTimeByPeriod("24-h", oleCirculationDesk);
        assertNotNull(date);
        System.out.println(date);
    }

    /*Testcase for due date in library holiday date and includeNonWorkingHours is false*/
    @Test
    public void loanPeriodFallsOutsideWorkingHoursAndDoNotIncludeNonWorkingHours() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        mockOleCalendar = new OleCalendar();

        ArrayList<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setOleCalendar(mockOleCalendar);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        mockOleCalendar.setBeginDate(new Timestamp(calendar.getTimeInMillis()));
        oleCalendarWeek.setEachDayWeek(true);
        oleCalendarWeek.setOpenTime("02:00");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTime("08:00");
        oleCalendarWeek.setCloseTimeSession("AM");
        oleCalendarWeek.setStartDay("0");
        oleCalendarWeek.setEndDay("6");
        oleCalendarWeekList.add(oleCalendarWeek);

        mockOleCalendar.setOleCalendarWeekList(oleCalendarWeekList);
        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);


        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtil();
        Date date = loanDateTimeUtil.calculateDateTimeByPeriod("24-h", oleCirculationDesk);
        assertNotNull(date);
        System.out.println(date);
    }


    /*Testcase for due date exceed in library working hours and includeNonWorkingHours is true*/
    @Test
    public void loanPeriodFallsOutsideWorkingHoursAndIncludeNonWorkingHours() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        mockOleCalendar = new OleCalendar();

        ArrayList<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setOleCalendar(mockOleCalendar);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        mockOleCalendar.setBeginDate(new Timestamp(calendar.getTimeInMillis()));
        oleCalendarWeek.setEachDayWeek(true);
        oleCalendarWeek.setOpenTime("02:00");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTime("08:00");
        oleCalendarWeek.setCloseTimeSession("AM");
        oleCalendarWeek.setStartDay("0");
        oleCalendarWeek.setEndDay("6");
        oleCalendarWeekList.add(oleCalendarWeek);

        mockOleCalendar.setOleCalendarWeekList(oleCalendarWeekList);
        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);


        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtilIncludeNonWorkingHours();
        Date date = loanDateTimeUtil.calculateDateTimeByPeriod("24-h", oleCirculationDesk);
        assertNotNull(date);
        System.out.println(date);
    }

    /*Testcase for due date in library exception period and includeNonWorkingHours is true*/
    @Test
    public void loanPeriodFallsInExceptionDays() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        mockOleCalendar = new OleCalendar();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        mockOleCalendar.setBeginDate(new Timestamp(calendar.getTimeInMillis()));

        OleCalendarExceptionDate oleCalendarExceptionDate = new OleCalendarExceptionDate();
        oleCalendarExceptionDate.setExceptionType("Partial");
        oleCalendarExceptionDate.setOpenTime("09:00");
        oleCalendarExceptionDate.setOpenTimeSession("AM");
        oleCalendarExceptionDate.setCloseTime("23:59");
        oleCalendarExceptionDate.setCloseTimeSession("PM");
        oleCalendarExceptionDate.setDate(DateUtils.addDays(new Date(), 1));

        OleCalendarExceptionDate oleCalendarExceptionDate2 = new OleCalendarExceptionDate();
        oleCalendarExceptionDate2.setExceptionType("Partial");
        oleCalendarExceptionDate2.setOpenTime("10:00");
        oleCalendarExceptionDate2.setOpenTimeSession("AM");
        oleCalendarExceptionDate2.setCloseTime("20:00");
        oleCalendarExceptionDate2.setCloseTimeSession("PM");
        oleCalendarExceptionDate2.setDate(DateUtils.addDays(new Date(), 2));

        OleCalendarExceptionDate oleCalendarExceptionDate3 = new OleCalendarExceptionDate();
        oleCalendarExceptionDate3.setExceptionType("Partial");
        oleCalendarExceptionDate3.setOpenTime("02:00");
        oleCalendarExceptionDate3.setOpenTimeSession("AM");
        oleCalendarExceptionDate3.setCloseTime("12:00");
        oleCalendarExceptionDate3.setCloseTimeSession("PM");
        oleCalendarExceptionDate3.setDate(DateUtils.addDays(new Date(), 3));

        List<OleCalendarExceptionDate> oleCalendarExceptionDates = new ArrayList<>();
        oleCalendarExceptionDates.add(oleCalendarExceptionDate);
        oleCalendarExceptionDates.add(oleCalendarExceptionDate2);
        oleCalendarExceptionDates.add(oleCalendarExceptionDate3);


        mockOleCalendar.setOleCalendarExceptionDateList(oleCalendarExceptionDates);
        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);


        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtilIncludeNonWorkingHours();
        Date date = loanDateTimeUtil.calculateDateTimeByPeriod("24-h", oleCirculationDesk);
        assertNotNull(date);
        System.out.println(date);
    }

    /* This tests the following usecase;
    1. Due date falls within the exception period (week list is not-empty - partial working hours period)
    2. Due date falls on the partial day
    3. Due time is exceed the working hours of the partial day
    4. Check for non-working hours
    5. Include non-working hours
    6. Following day is falls on the Exception day which is partial day

    Expect the due date to be the open time + grace time of the following day.
     * */
    @Test
    public void loanPeriodFallsInExceptionDaysAndExceededWorkingHoursWithFollowinDayIsAnExceptionDay() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar with Exception Period having partial work days");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList = new ArrayList<>();
        OleCalendarExceptionPeriod oleCalendarExceptionPeriod = new OleCalendarExceptionPeriod();

        Calendar beginDate = Calendar.getInstance();
        beginDate.add(Calendar.DAY_OF_MONTH, -5);
        oleCalendarExceptionPeriod.setBeginDate(new Timestamp(beginDate.getTime().getTime()));

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 1);
        oleCalendarExceptionPeriod.setEndDate(new Timestamp(endDate.getTime().getTime()));

        oleCalendarExceptionPeriod.setExceptionPeriodType("Partial");

        ArrayList<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodWeekList = new ArrayList<>();

        OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek = new OleCalendarExceptionPeriodWeek();
        oleCalendarExceptionPeriodWeek.setOpenTime("09:00");
        oleCalendarExceptionPeriodWeek.setOpenTimeSession("AM");
        oleCalendarExceptionPeriodWeek.setCloseTime("10:00");
        oleCalendarExceptionPeriodWeek.setCloseTimeSession("AM");
        oleCalendarExceptionPeriodWeek.setStartDay("0");
        oleCalendarExceptionPeriodWeek.setEndDay("5");
        oleCalendarExceptionPeriodWeek.setEachDayOfExceptionWeek(true);

        oleCalendarExceptionPeriodWeekList.add(oleCalendarExceptionPeriodWeek);

        oleCalendarExceptionPeriod.setOleCalendarExceptionPeriodWeekList(oleCalendarExceptionPeriodWeekList);
        oleCalendarExceptionPeriodList.add(oleCalendarExceptionPeriod);

        mockOleCalendar = new OleCalendar();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(calendar1.DATE, -1);
        mockOleCalendar.setBeginDate(new Timestamp(calendar1.getTimeInMillis()));
        mockOleCalendar.setOleCalendarExceptionPeriodList(oleCalendarExceptionPeriodList);

        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);

        OleCalendarExceptionDate oleCalendarExceptionDate = new OleCalendarExceptionDate();
        oleCalendarExceptionDate.setDate(DateUtils.addDays(new Date(), 2));
        oleCalendarExceptionDate.setOpenTime("10:00");
        oleCalendarExceptionDate.setOpenTimeSession("AM");
        oleCalendarExceptionDate.setCloseTime("14:00");
        oleCalendarExceptionDate.setCloseTimeSession("PM");

        mockOleCalendar.setOleCalendarExceptionDateList(Collections.singletonList(oleCalendarExceptionDate));

        ArrayList<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setOleCalendar(mockOleCalendar);

        oleCalendarWeek.setEachDayWeek(true);
        oleCalendarWeek.setOpenTime("02:00");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTime("20:00");
        oleCalendarWeek.setCloseTimeSession("PM");
        oleCalendarWeek.setStartDay("0");
        oleCalendarWeek.setEndDay("6");
        oleCalendarWeekList.add(oleCalendarWeek);

        mockOleCalendar.setOleCalendarWeekList(oleCalendarWeekList);


        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtilIncludeNonWorkingHours();
        Date loanDueDate = loanDateTimeUtil.calculateDateTimeByPeriod("24-h", oleCirculationDesk);
        assertNotNull(loanDueDate);
        System.out.println(loanDueDate);
    }




    /* This tests the following usecase;
    1. Due date falls within the exception period (week list is not-empty - partial working hours period)
    2. Due date falls on the partial day
    3. Check for non-working hours
    4. Exclude non-working hours
     5. Due date is the close time of the given day which is a partial day as well

    Expect the due date to be the close time of the given day.
     * */
    @Test
    public void loanPeriodFallsInExceptionDaysAndExceededWorkingHoursWithFollowinDayBeingInARegularWeek() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar with Exception Period having partial work days");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList = new ArrayList<>();
        OleCalendarExceptionPeriod oleCalendarExceptionPeriod = new OleCalendarExceptionPeriod();

        Calendar beginDate = Calendar.getInstance();
        beginDate.add(Calendar.DAY_OF_MONTH, -5);
        oleCalendarExceptionPeriod.setBeginDate(new Timestamp(beginDate.getTime().getTime()));

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 1);
        oleCalendarExceptionPeriod.setEndDate(new Timestamp(endDate.getTime().getTime()));

        oleCalendarExceptionPeriod.setExceptionPeriodType("Partial");

        ArrayList<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodWeekList = new ArrayList<>();

        OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek = new OleCalendarExceptionPeriodWeek();
        oleCalendarExceptionPeriodWeek.setOpenTime("09:00");
        oleCalendarExceptionPeriodWeek.setOpenTimeSession("AM");
        oleCalendarExceptionPeriodWeek.setCloseTime("14:00");
        oleCalendarExceptionPeriodWeek.setCloseTimeSession("PM");
        oleCalendarExceptionPeriodWeek.setStartDay("0");
        oleCalendarExceptionPeriodWeek.setEndDay("5");
        oleCalendarExceptionPeriodWeek.setEachDayOfExceptionWeek(true);

        oleCalendarExceptionPeriodWeekList.add(oleCalendarExceptionPeriodWeek);

        oleCalendarExceptionPeriod.setOleCalendarExceptionPeriodWeekList(oleCalendarExceptionPeriodWeekList);
        oleCalendarExceptionPeriodList.add(oleCalendarExceptionPeriod);

        mockOleCalendar = new OleCalendar();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(calendar1.DATE, -1);
        mockOleCalendar.setBeginDate(new Timestamp(calendar1.getTimeInMillis()));
        mockOleCalendar.setOleCalendarExceptionPeriodList(oleCalendarExceptionPeriodList);

        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);

        ArrayList<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setOleCalendar(mockOleCalendar);
        oleCalendarWeek.setEachDayWeek(true);
        oleCalendarWeek.setOpenTime("02:00");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTime("08:00");
        oleCalendarWeek.setCloseTimeSession("PM");
        oleCalendarWeek.setStartDay("0");
        oleCalendarWeek.setEndDay("6");
        oleCalendarWeekList.add(oleCalendarWeek);

        mockOleCalendar.setOleCalendarWeekList(oleCalendarWeekList);


        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtilIncludeNonWorkingHours();
        Date loanDueDate = loanDateTimeUtil.calculateDateTimeByPeriod("24-h", oleCirculationDesk);
        assertNotNull(loanDueDate);
        System.out.println(loanDueDate);
    }

    /* This tests the following usecase;
    1. Due date falls within the exception period (week list is not-empty - partial working hours period)
    2. Due date falls on the partial day
    3. Check for non-working hours
    4. Exclude non-working hours
    5. Due date is the close time of the given day which is a partial day as well

   Expect the due date to be the close time of the given day.
    * */
    @Test
    public void loanPeriodFallsInExceptionDaysAndExceededWorkingHoursWithExcludeNonWorkingHours() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar with Exception Period having partial work days");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList = new ArrayList<>();
        OleCalendarExceptionPeriod oleCalendarExceptionPeriod = new OleCalendarExceptionPeriod();

        Calendar beginDate = Calendar.getInstance();
        beginDate.add(Calendar.DAY_OF_MONTH, -5);
        oleCalendarExceptionPeriod.setBeginDate(new Timestamp(beginDate.getTime().getTime()));

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 5);
        oleCalendarExceptionPeriod.setEndDate(new Timestamp(endDate.getTime().getTime()));

        oleCalendarExceptionPeriod.setExceptionPeriodType("Partial");

        ArrayList<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodWeekList = new ArrayList<>();

        OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek = new OleCalendarExceptionPeriodWeek();
        oleCalendarExceptionPeriodWeek.setOpenTime("09:00");
        oleCalendarExceptionPeriodWeek.setOpenTimeSession("AM");
        oleCalendarExceptionPeriodWeek.setCloseTime("14:00");
        oleCalendarExceptionPeriodWeek.setCloseTimeSession("PM");
        oleCalendarExceptionPeriodWeek.setStartDay("0");
        oleCalendarExceptionPeriodWeek.setEndDay("5");
        oleCalendarExceptionPeriodWeek.setEachDayOfExceptionWeek(true);

        oleCalendarExceptionPeriodWeekList.add(oleCalendarExceptionPeriodWeek);

        oleCalendarExceptionPeriod.setOleCalendarExceptionPeriodWeekList(oleCalendarExceptionPeriodWeekList);
        oleCalendarExceptionPeriodList.add(oleCalendarExceptionPeriod);

        mockOleCalendar = new OleCalendar();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(calendar1.DATE, -1);
        mockOleCalendar.setBeginDate(new Timestamp(calendar1.getTimeInMillis()));
        mockOleCalendar.setOleCalendarExceptionPeriodList(oleCalendarExceptionPeriodList);

        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);

        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtil();
        Date loanDueDate = loanDateTimeUtil.calculateDateTimeByPeriod("24-h", oleCirculationDesk);
        assertNotNull(loanDueDate);
        System.out.println(loanDueDate);
    }


    /* This tests the following usecase;
     1. Due date falls within the exception period (week list is not-empty - partial working hours period)
    2. Due date falls on the partial day
    3. Check for non-working hours
    4. Include non-working hours
    5. Due date is the open time of the following day which is a partial day plus grace period.

    Expect the due date to be the day after partial date on which the due date falls.
    * */
    @Test
    public void getOpenAndCloseTimeForGivenDayFromExceptionPeriodWithPartialHours() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar with Exception Period having partial work days");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList = new ArrayList<>();
        OleCalendarExceptionPeriod oleCalendarExceptionPeriod = new OleCalendarExceptionPeriod();

        Calendar beginDate = Calendar.getInstance();
        beginDate.add(Calendar.DAY_OF_MONTH, -5);
        oleCalendarExceptionPeriod.setBeginDate(new Timestamp(beginDate.getTime().getTime()));

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 5);
        oleCalendarExceptionPeriod.setEndDate(new Timestamp(endDate.getTime().getTime()));

        oleCalendarExceptionPeriod.setExceptionPeriodType("Partial");

        ArrayList<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodWeekList = new ArrayList<>();

        OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek = new OleCalendarExceptionPeriodWeek();
        oleCalendarExceptionPeriodWeek.setOpenTime("09:00");
        oleCalendarExceptionPeriodWeek.setOpenTimeSession("AM");
        oleCalendarExceptionPeriodWeek.setCloseTime("14:00");
        oleCalendarExceptionPeriodWeek.setCloseTimeSession("PM");
        oleCalendarExceptionPeriodWeek.setStartDay("0");
        oleCalendarExceptionPeriodWeek.setEndDay("5");
        oleCalendarExceptionPeriodWeek.setEachDayOfExceptionWeek(true);

        oleCalendarExceptionPeriodWeekList.add(oleCalendarExceptionPeriodWeek);

        oleCalendarExceptionPeriod.setOleCalendarExceptionPeriodWeekList(oleCalendarExceptionPeriodWeekList);
        oleCalendarExceptionPeriodList.add(oleCalendarExceptionPeriod);

        mockOleCalendar = new OleCalendar();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(calendar1.DATE, -1);
        mockOleCalendar.setBeginDate(new Timestamp(calendar1.getTimeInMillis()));
        mockOleCalendar.setOleCalendarExceptionPeriodList(oleCalendarExceptionPeriodList);

        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);


        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtilIncludeNonWorkingHours();
        Date loanDueDate = loanDateTimeUtil.calculateDateTimeByPeriod("24-h", oleCirculationDesk);
        assertNotNull(loanDueDate);
        System.out.println(loanDueDate);

    }

    /* This tests the following usecase;
    1. Due date falls within the exception period (weeklist is empty - holiday period)
    2. Due date is the following day post exception period
    3. Post exception period is a regular working day

    Expect the due date to be the day after the exception period end date.
    * */
    @Test
    public void loanPeriodFallsInExceptionPeriodWithHolidays() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList = new ArrayList<>();
        OleCalendarExceptionPeriod oleCalendarExceptionPeriod = new OleCalendarExceptionPeriod();

        Calendar beginDate = Calendar.getInstance();
        beginDate.add(Calendar.DAY_OF_MONTH, -5);
        oleCalendarExceptionPeriod.setBeginDate(new Timestamp(beginDate.getTime().getTime()));

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 5);
        oleCalendarExceptionPeriod.setEndDate(new Timestamp(endDate.getTime().getTime()));

        oleCalendarExceptionPeriod.setExceptionPeriodType("Holidays");

        mockOleCalendar = new OleCalendar();

        ArrayList<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setOleCalendar(mockOleCalendar);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(calendar1.DATE, -1);
        mockOleCalendar.setBeginDate(new Timestamp(calendar1.getTimeInMillis()));
        oleCalendarWeek.setEachDayWeek(true);
        oleCalendarWeek.setOpenTime("02:00");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTime("20:00");
        oleCalendarWeek.setCloseTimeSession("PM");
        oleCalendarWeek.setStartDay("0");
        oleCalendarWeek.setEndDay("6");
        oleCalendarWeekList.add(oleCalendarWeek);

        mockOleCalendar.setOleCalendarWeekList(oleCalendarWeekList);

        oleCalendarExceptionPeriodList.add(oleCalendarExceptionPeriod);



        mockOleCalendar.setOleCalendarExceptionPeriodList(oleCalendarExceptionPeriodList);
        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);


        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtilIncludeNonWorkingHours();
        Date date = loanDateTimeUtil.calculateDateTimeByPeriod("24-h", oleCirculationDesk);
        assertNotNull(date);
        System.out.println(date);
    }


    /*Testcase for comparing date in between two dates */
    @Test
    public void compareDateInBetweenTwoDates() throws Exception {
        Calendar beginDateCalendar = Calendar.getInstance();
        beginDateCalendar.add(Calendar.DATE, -5);
        Date beginDate = beginDateCalendar.getTime();

        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.add(Calendar.DATE, 5);
        Date endDate = endDateCalendar.getTime();

        Date date = new Date(System.currentTimeMillis());

        boolean isWithinPeriod = (beginDate.compareTo(date) <=0 && endDate.compareTo(date) >= 0);

        assertTrue(isWithinPeriod);
    }

    /*Testcase for comparing date not inbetween two dates */
    @Test
    public void compareDateNotInBetweenTwoDates() throws Exception {
        Calendar beginDateCalendar = Calendar.getInstance();
        beginDateCalendar.add(Calendar.DATE, 5);
        Date beginDate = beginDateCalendar.getTime();

        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.add(Calendar.DATE, 5);
        Date endDate = endDateCalendar.getTime();

        Date date = new Date(System.currentTimeMillis());

        boolean isWithinPeriod = (beginDate.compareTo(date) <=0 && endDate.compareTo(date) >= 0);

        assertFalse(isWithinPeriod);
    }



    /* This tests the following usecase;
    1. Due date falls within the exception period (week list is not-empty - partial working hours period)
    2. Due date falls on the partial day
    3. Due time is exceed the working hours of the partial day
    4. Check for non-working hours
    5. Include non-working hours
    6. Following day is falls on the Exception day which is holiday
    7. Next following day is falls on the regular week list

    Expect the due date to be the open time + grace time of the following day.
     * */
    @Test
    public void loanPeriodFallsInExceptionDaysAndExceededWorkingHoursWithFollowinDayIsHolidayAndNextFollowingDayIsRegularWeek() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar with Exception Period having partial work days");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList = new ArrayList<>();
        OleCalendarExceptionPeriod oleCalendarExceptionPeriod = new OleCalendarExceptionPeriod();

        Calendar beginDate = Calendar.getInstance();
        beginDate.add(Calendar.DAY_OF_MONTH, -5);
        oleCalendarExceptionPeriod.setBeginDate(new Timestamp(beginDate.getTime().getTime()));

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 1);
        oleCalendarExceptionPeriod.setEndDate(new Timestamp(endDate.getTime().getTime()));

        oleCalendarExceptionPeriod.setExceptionPeriodType("Partial");

        ArrayList<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodWeekList = new ArrayList<>();

        OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek = new OleCalendarExceptionPeriodWeek();
        oleCalendarExceptionPeriodWeek.setOpenTime("09:00");
        oleCalendarExceptionPeriodWeek.setOpenTimeSession("AM");
        oleCalendarExceptionPeriodWeek.setCloseTime("10:00");
        oleCalendarExceptionPeriodWeek.setCloseTimeSession("AM");
        oleCalendarExceptionPeriodWeek.setStartDay("0");
        oleCalendarExceptionPeriodWeek.setEndDay("5");
        oleCalendarExceptionPeriodWeek.setEachDayOfExceptionWeek(true);

        oleCalendarExceptionPeriodWeekList.add(oleCalendarExceptionPeriodWeek);

        oleCalendarExceptionPeriod.setOleCalendarExceptionPeriodWeekList(oleCalendarExceptionPeriodWeekList);
        oleCalendarExceptionPeriodList.add(oleCalendarExceptionPeriod);

        mockOleCalendar = new OleCalendar();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(calendar1.DATE, -1);
        mockOleCalendar.setBeginDate(new Timestamp(calendar1.getTimeInMillis()));
        mockOleCalendar.setOleCalendarExceptionPeriodList(oleCalendarExceptionPeriodList);

        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);

        OleCalendarExceptionDate oleCalendarExceptionDate = new OleCalendarExceptionDate();
        oleCalendarExceptionDate.setDate(DateUtils.addDays(new Date(), 2));

        mockOleCalendar.setOleCalendarExceptionDateList(Collections.singletonList(oleCalendarExceptionDate));

        ArrayList<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setOleCalendar(mockOleCalendar);

        oleCalendarWeek.setEachDayWeek(true);
        oleCalendarWeek.setOpenTime("02:00");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTime("20:00");
        oleCalendarWeek.setCloseTimeSession("PM");
        oleCalendarWeek.setStartDay("0");
        oleCalendarWeek.setEndDay("6");
        oleCalendarWeekList.add(oleCalendarWeek);

        mockOleCalendar.setOleCalendarWeekList(oleCalendarWeekList);


        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtilIncludeNonWorkingHours();
        Date loanDueDate = loanDateTimeUtil.calculateDateTimeByPeriod("24-h", oleCirculationDesk);
        assertNotNull(loanDueDate);
        System.out.println(loanDueDate);
    }


    /* This tests the following usecase;
        1. Due date falls the regular week list (6 to 0) --> (Saturday to Sunday)
        2. Due time is falls in the working hours

        Expect the due date to be the due date time.
         * */
    @Test
    public void loanPeriodFallsWithinWorkingHoursAndDifferentDayOrder() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());

        mockOleCalendar = new OleCalendar();

        ArrayList<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setOleCalendar(mockOleCalendar);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        mockOleCalendar.setBeginDate(new Timestamp(calendar.getTimeInMillis()));
        oleCalendarWeek.setEachDayWeek(true);
        oleCalendarWeek.setOpenTime("02:00");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTime("23:00");
        oleCalendarWeek.setCloseTimeSession("PM");
        oleCalendarWeek.setStartDay("6");
        oleCalendarWeek.setEndDay("0");
        oleCalendarWeekList.add(oleCalendarWeek);

        mockOleCalendar.setOleCalendarWeekList(oleCalendarWeekList);
        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);


        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtil();
        Date date = loanDateTimeUtil.calculateDateTimeByPeriod("24-h", oleCirculationDesk);
        assertNotNull(date);
        System.out.println(date);
    }

    class MockLoanDateTimeUtil extends LoanDateTimeUtil {
        @Override
        protected List<OleCalendar> getOleCalendars(String groupId) {
            if(groupId.equalsIgnoreCase("1")){
                ArrayList<OleCalendar> oleCalendars = new ArrayList<>();
                oleCalendars.add(mockOleCalendar);
                return oleCalendars;
            }
            return  null;
        }

        @Override
        public Boolean includeNonWorkingHours() {
            return false;
        }

        @Override
        public String getGracePeriodForIncludingNonWorkingHours() {
            return "25-m";
        }

        @Override
        public String getDefaultTimeForDueDate() {
            return "22:22:22";
        }
    }

    class MockLoanDateTimeUtilDefaultDueTimeBlank extends LoanDateTimeUtil {
        @Override
        protected List<OleCalendar> getOleCalendars(String groupId) {
            if(groupId.equalsIgnoreCase("1")){
                ArrayList<OleCalendar> oleCalendars = new ArrayList<>();
                oleCalendars.add(mockOleCalendar);
                return oleCalendars;
            }
            return  null;
        }

        @Override
        public Boolean includeNonWorkingHours() {
            return false;
        }

        @Override
        public String getGracePeriodForIncludingNonWorkingHours() {
            return "25-m";
        }

        @Override
        public String getDefaultTimeForDueDate() {
            return "";
        }
    }

    class MockLoanDateTimeUtilIncludeNonWorkingHours extends LoanDateTimeUtil {
        @Override
        protected List<OleCalendar> getOleCalendars(String groupId) {
            if(groupId.equalsIgnoreCase("1")){
                ArrayList<OleCalendar> oleCalendars = new ArrayList<>();
                oleCalendars.add(mockOleCalendar);
                return oleCalendars;
            }
            return  null;
        }

        @Override
        public Boolean includeNonWorkingHours() {
            return true;
        }

        @Override
        public String getGracePeriodForIncludingNonWorkingHours() {
            return "25-m";
        }

        @Override
        public String getDefaultTimeForDueDate() {
            return "22:22:22";
        }
    }



    @Test
    public void dueDateCheck() throws Exception {
        OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setCalendarGroupId("1");
        oleCalendarGroup.setCalendarGroupName("Mock Fall Calendar with Exception Period having partial work days");

        oleCirculationDesk.setOleCalendarGroup(oleCalendarGroup);
        oleCirculationDesk.setCalendarGroupId(oleCalendarGroup.getCalendarGroupId());



        mockOleCalendar = new OleCalendar();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(calendar1.DATE, -1);
        mockOleCalendar.setBeginDate(null);


        ArrayList<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();

        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setOleCalendar(mockOleCalendar);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        mockOleCalendar.setBeginDate(new Timestamp(calendar.getTimeInMillis()));
        oleCalendarWeek.setEachDayWeek(true);
        oleCalendarWeek.setOpenTime("12:00");
        oleCalendarWeek.setOpenTimeSession("PM");
        oleCalendarWeek.setCloseTime("22:00");
        oleCalendarWeek.setCloseTimeSession("PM");
        oleCalendarWeek.setStartDay("0");
        oleCalendarWeek.setEndDay("0");
        oleCalendarWeekList.add(oleCalendarWeek);

        OleCalendarWeek oleCalendarWeek2 = new OleCalendarWeek();
        oleCalendarWeek2.setOleCalendar(mockOleCalendar);
        oleCalendarWeek2.setEachDayWeek(true);
        oleCalendarWeek2.setOpenTime("09:00");
        oleCalendarWeek2.setOpenTimeSession("AM");
        oleCalendarWeek2.setCloseTime("17:00");
        oleCalendarWeek2.setCloseTimeSession("PM");
        oleCalendarWeek2.setStartDay("1");
        oleCalendarWeek2.setEndDay("4");
        oleCalendarWeekList.add(oleCalendarWeek2);

        OleCalendarWeek oleCalendarWeek3 = new OleCalendarWeek();
        oleCalendarWeek3.setOleCalendar(mockOleCalendar);
        oleCalendarWeek3.setEachDayWeek(true);
        oleCalendarWeek3.setOpenTime("09:00");
        oleCalendarWeek3.setOpenTimeSession("AM");
        oleCalendarWeek3.setCloseTime("19:00");
        oleCalendarWeek3.setCloseTimeSession("PM");
        oleCalendarWeek3.setStartDay("5");
        oleCalendarWeek3.setEndDay("5");
        oleCalendarWeekList.add(oleCalendarWeek3);

        OleCalendarWeek oleCalendarWeek4 = new OleCalendarWeek();
        oleCalendarWeek4.setOleCalendar(mockOleCalendar);
        oleCalendarWeek4.setEachDayWeek(true);
        oleCalendarWeek4.setOpenTime("10:00");
        oleCalendarWeek4.setOpenTimeSession("AM");
        oleCalendarWeek4.setCloseTime("17:00");
        oleCalendarWeek4.setCloseTimeSession("PM");
        oleCalendarWeek4.setStartDay("6");
        oleCalendarWeek4.setEndDay("6");
        oleCalendarWeekList.add(oleCalendarWeek4);

        mockOleCalendar.setOleCalendarWeekList(oleCalendarWeekList);

        OleCalendarExceptionPeriod oleCalendarExceptionPeriod1 = new OleCalendarExceptionPeriod();

        Calendar beginDate = Calendar.getInstance();
        beginDate.add(Calendar.DAY_OF_MONTH, 4);
        oleCalendarExceptionPeriod1.setBeginDate(new Timestamp(beginDate.getTime().getTime()));

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 13);
        oleCalendarExceptionPeriod1.setEndDate(new Timestamp(endDate.getTime().getTime()));

        oleCalendarExceptionPeriod1.setExceptionPeriodType("Holidays");

        List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList = new ArrayList<>();
        OleCalendarExceptionPeriod oleCalendarExceptionPeriod = new OleCalendarExceptionPeriod();

        Calendar beginDate2 = Calendar.getInstance();
        Date beginDate2Time = beginDate2.getTime();
        oleCalendarExceptionPeriod.setBeginDate(new Timestamp(beginDate2Time.getTime()));

        Calendar endDate2 = Calendar.getInstance();
        endDate2.add(Calendar.DAY_OF_MONTH, 3);
        oleCalendarExceptionPeriod.setEndDate(new Timestamp(endDate2.getTime().getTime()));

        oleCalendarExceptionPeriod.setExceptionPeriodType("Partial");

        ArrayList<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodWeekList = new ArrayList<>();

        OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek4 = new OleCalendarExceptionPeriodWeek();
        oleCalendarExceptionPeriodWeek4.setOpenTime("09:00");
        oleCalendarExceptionPeriodWeek4.setOpenTimeSession("AM");
        oleCalendarExceptionPeriodWeek4.setCloseTime("17:00");
        oleCalendarExceptionPeriodWeek4.setCloseTimeSession("PM");
        oleCalendarExceptionPeriodWeek4.setStartDay("1");
        oleCalendarExceptionPeriodWeek4.setEndDay("6");
        oleCalendarExceptionPeriodWeek4.setEachDayOfExceptionWeek(true);

        oleCalendarExceptionPeriodWeekList.add(oleCalendarExceptionPeriodWeek4);

        oleCalendarExceptionPeriod.setOleCalendarExceptionPeriodWeekList(oleCalendarExceptionPeriodWeekList);
        oleCalendarExceptionPeriodList.add(oleCalendarExceptionPeriod);
        oleCalendarExceptionPeriodList.add(oleCalendarExceptionPeriod1);
        mockOleCalendar.setOleCalendarExceptionPeriodList(oleCalendarExceptionPeriodList);

        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);

        LoanDateTimeUtil loanDateTimeUtil = new MockLoanDateTimeUtilIncludeNonWorkingHours();
        Date loanDueDate = loanDateTimeUtil.calculateDateTimeByPeriod("3-D", oleCirculationDesk);
        assertNotNull(loanDueDate);
        System.out.println(loanDueDate);
    }

    @Test
    public void getOpenAndClosingTimeForTheGivenDayFromWeekList() throws Exception {
        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setOpenTime("09:30");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTime("09:25");
        oleCalendarWeek.setCloseTimeSession("AM");
        oleCalendarWeek.setStartDay("6");
        oleCalendarWeek.setEndDay("5");

        List<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
        oleCalendarWeekList.add(oleCalendarWeek);
        LoanDateTimeUtil loanDateTimeUtil = new LoanDateTimeUtil();
        Map<String, Map<String, String>> openingAndClosingTimeMap = loanDateTimeUtil.getOpenAndClosingTimeForTheGivenDayFromWeekList(new Date(), oleCalendarWeekList);
        Map<String, String> openTimeMap = openingAndClosingTimeMap.get("openTime");
        Map<String, String> closeTimeMap = openingAndClosingTimeMap.get("closeTime");

        String openTime = openTimeMap.keySet().iterator().hasNext() ? openTimeMap.keySet().iterator().next() : "";
        String closeTime = closeTimeMap.keySet().iterator().hasNext() ? closeTimeMap.keySet().iterator().next() : "";
        System.out.println("Open Time : " + openTime);
        System.out.println("Close Time : " + closeTime);
    }
}
