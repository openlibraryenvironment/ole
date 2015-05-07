package org.kuali.ole.deliver.service;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.calendar.bo.*;
import org.kuali.ole.deliver.calendar.service.impl.OleCalendarServiceImpl;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by aurojyotit on 4/13/15.
 */
public class OleCalendarServiceImpl_UT {

    private static String CIR_ID = "100";
    private static String TIME_PERIOD = "10-D";
    private static String FINE_AMOUNT = "10/H";
    private static String CURR_DATE = "16-04-2015 16:14:00";
    private static String DUE_DATE = "13-04-2015 00:00:00";
    private static String CALENDAR_BEGIN_DATE = "17-03-2014 00:00:00";
    private static String CALENDAR_END_DATE = null;
    private static String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    private static String CALENDAR_FLAG = "true";//By default its true only

    @Mock
    private BusinessObjectService businessObjectService;

    @Mock
    private ParameterValueResolver parameterValueResolver;

    @Mock
    private ParameterValueResolver mockParameterResolverInstance;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void calculateDueDateScenario1() {
        // This test is used to calculate the dueDate based on the OleCalendar input as configured by getOleCalendarsWithHolidays and getCalendarGroup method .
        // Info : In this method the Calendar is defined as MonDay-Friday working hour (9:00 AM to 11 AM) with each day of week is true (day is bounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-05-01 09:00:00.0'
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");

        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidays());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDate(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-05-01 09:00:00.0", dateTimestamp);
    }

    @Test
    public void calculateDueDateScenario2() {
        // This test is used to calculate the dueDate based on the OleCalendar input as configured by getOleCalendarsWithoutHolidays and getCalendarGroup method .
        // Info :In this method the Calendar is defined as MonDay-Friday working hour (9:00 AM to 11 AM) with each day of week is false (day is unbounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-04-30 23:59:00.0'
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");

        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithoutHolidays());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDate(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-04-30 23:59:00.0",dateTimestamp);
    }

    @Test
    public void calculateDueDateScenario3() {
        // This test is used to calculate the dueDate based on the OleCalendar input as configured by getOleCalendarsWithHolidays1 and getCalendarGroup method .
        // Info : In this method the Calendar is defined as MonDay,Tuesday,ThursDay, Friday working hour (9:00 AM to 11 AM) with each day of week is false (day is unbounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-05-04 23:59:00.0'

        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();

        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");

        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidays1());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDate(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-05-04 23:59:00.0",dateTimestamp);
    }

    @Test
    public void calculateDueDateScenario4() {
        // This test is used to calculate the dueDate based on the OleCalendar input as configured by getOleCalendarsWithHolidaysExceptionDays and getCalendarGroup method .
        // In this method the Calendar is defined as MonDay,Tuesday,ThursDay, Friday working hour (9:00 AM to 11 AM) with each day of week is false (day is unbounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-05-04 23:59:00.0' and "21-04-2015" is holiday

        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");

        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidaysExceptionDays());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDate(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-05-04 23:59:00.0",dateTimestamp);
    }

    @Test
    public void calculateDueDateScenario5() {
        // This test is used to calculate the dueDate based on the OleCalendar input as configured by getOleCalendarsWithHolidaysExceptionDaysAndPeriods and getCalendarGroup method .
        // Info : In this method the Calendar is defined as MonDay,Tuesday,ThursDay,Friday have working hour (9:00 AM to 11 AM) with each day of week is true (day is bounded),
        // having exceptional working hour "6:00AM" to "10:00AM" form "25-04-2015" to "28-04-2015"
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-05-11 09:00:00.0' and "21-04-2015" is holiday
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");

        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidaysExceptionDaysAndPeriods());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDate(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-05-11 09:00:00.0",dateTimestamp);
    }

    @Test
    public void calculateDueDateScenario6() {
        // This test is used to calculate the dueDate based on the OleCalendar input as configured by getOleCalendarsWithHolidaysEachExceptionDaysAndPeriodsTrue and getCalendarGroup method .
        // In this method the Calendar is defined as MonDay,Tuesday,ThursDay, Friday working hour (9:00 AM to 11 AM) with each day of week is false (day is unbounded)
        // having exceptional working hour "6:00AM" to "10:00AM" form "25-04-2015" to "28-04-2015" with  EachDayOfExceptionWeek is false (day is unbounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-05-11 09:00:00.0' and "21-04-2015" is holiday
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");

        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidaysEachExceptionDaysAndPeriodsTrue());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDate(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-05-11 09:00:00.0",dateTimestamp);
    }

    @Test
    public void calculateDueDateScenario7() {
        // This test is used to calculate the dueDate based on the OleCalendar input as configured by getOleCalendarsWithHolidays and getCalendarGroup method .
        // In this method the Calendar is defined as MonDay-Friday working hour (9:00 AM to 11 AM) with each day of week is true (day is bounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-05-01 09:00:00.0'
        // CALENDAR_FLAG is false (it refers to the system parameter constant INCLUDE_NON_WORKING_HRS)
        CALENDAR_FLAG="false";

        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");

        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidays());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDate(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-05-01 09:00:00.0",dateTimestamp);
    }

    @Test
    public void calculateDueDateScenario8() {
        // This test is used to calculate the dueDate based on the OleCalendar input as configured by getOleCalendarsWithoutHolidays and getCalendarGroup method .
        // In this method the Calendar is defined as MonDay-Friday working hour (9:00 AM to 11 AM) with each day of week is false (day is unbounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-04-30 23:59:00.0'
        // CALENDAR_FLAG is false (it refers to the system parameter constant INCLUDE_NON_WORKING_HRS)
        CALENDAR_FLAG="false";
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");

        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithoutHolidays());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDate(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-04-30 23:59:00.0",dateTimestamp);
    }

    @Test
    public void calculateDueDateScenario9() {
        // This test is used to calculate the dueDate based on the OleCalendar input as configured by getOleCalendarsWithHolidays1 and getCalendarGroup method .
        // Info : In this method the Calendar is defined as MonDay,Tuesday,ThursDay, Friday working hour (9:00 AM to 11 AM) with each day of week is false (day is unbounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-05-04 23:59:00.0'
        // CALENDAR_FLAG is false (it refers to the system parameter constant INCLUDE_NON_WORKING_HRS)
        CALENDAR_FLAG="false";
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");

        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidays1());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDate(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-05-04 23:59:00.0",dateTimestamp);
    }

    @Test
    public void calculateDueDateScenario10() {
        // This test is used to calculate the dueDate based on the OleCalendar input as configured by getOleCalendarsWithHolidaysExceptionDays and getCalendarGroup method .
        // In this method the Calendar is defined as MonDay,Tuesday,ThursDay, Friday working hour (9:00 AM to 11 AM) with each day of week is false (day is unbounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-05-04 23:59:00.0' and "21-04-2015" is holiday
        // CALENDAR_FLAG is false (it refers to the system parameter constant INCLUDE_NON_WORKING_HRS)
        CALENDAR_FLAG="false";
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");

        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidaysExceptionDays());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDate(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-05-04 23:59:00.0",dateTimestamp);
    }

    @Test
    public void calculateDueDateScenario11() {
        // This test is used to calculate the dueDate based on the OleCalendar input as configured by getOleCalendarsWithHolidaysExceptionDaysAndPeriods and getCalendarGroup method .
        // Info : In this method the Calendar is defined as MonDay,Tuesday,ThursDay,Friday have working hour (9:00 AM to 11 AM) with each day of week is true (day is bounded),
        // having exceptional working hour "6:00AM" to "10:00AM" form "25-04-2015" to "28-04-2015"
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-05-11 09:00:00.0' and "21-04-2015" is holiday
        // CALENDAR_FLAG is false (it refers to the system parameter constant INCLUDE_NON_WORKING_HRS)
        CALENDAR_FLAG="false";
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();

        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");

        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidaysExceptionDaysAndPeriods());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDate(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-05-11 09:00:00.0",dateTimestamp);
    }

    @Test
    public void calculateDueDateScenario12() {
        // This test is used to calculate the dueDate based on the OleCalendar input as configured by getOleCalendarsWithHolidaysEachExceptionDaysAndPeriodsTrue and getCalendarGroup method .
        // In this method the Calendar is defined as MonDay,Tuesday,ThursDay, Friday working hour (9:00 AM to 11 AM) with each day of week is false (day is unbounded)
        // having exceptional working hour "6:00AM" to "10:00AM" form "25-04-2015" to "28-04-2015" with  EachDayOfExceptionWeek is false (day is unbounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-05-11 09:00:00.0' and "21-04-2015" is holiday
        // INCLUDE_NON_WORKING_HRS=false in system parameter
        CALENDAR_FLAG="false";
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");

        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidaysEachExceptionDaysAndPeriodsTrue());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDate(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-05-11 09:00:00.0",dateTimestamp);
    }

    @Test
    public void calculateDueDateHrsScenario1() {
        // This test is used to calculate the dueDateHrs based on the OleCalendar input as configured by getOleCalendarsWithHolidays and getCalendarGroup method .
        // Info : In this method the Calendar is defined as MonDay,Tuesday,ThursDay, Friday working hour (9:00 AM to 11 AM) with each day of week is false (day is unbounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-04-28 10:14:00.0'
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();

        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");
        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidays());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDateHrs(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-04-28 10:14:00.0",dateTimestamp);

    }
    @Test
    public void calculateDueDateHrsScenario2() {
        // This test is used to calculate the dueDateHrs based on the OleCalendar input as configured by getOleCalendarsWithoutHolidays and getCalendarGroup method .
        // Info :In this method the Calendar is defined as MonDay-Friday working hour (9:00 AM to 11 AM) with each day of week is false (day is unbounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-04-17 02:15:00.0'
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");
        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithoutHolidays());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDateHrs(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-04-17 02:15:00.0",dateTimestamp);

    }
    @Test
    public void calculateDueDateHrsScenario3() {
        // This test is used to calculate the dueDateHrs based on the OleCalendar input as configured by getOleCalendarsWithHolidays1 and getCalendarGroup method .
        // In this method the Calendar is defined as MonDay,Tuesday,ThursDay, Friday working hour (9:00 AM to 11 AM) with each day of week is false (day is unbounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-04-17 02:15:00.0'
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");
        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidays1());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDateHrs(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-04-17 02:15:00.0",dateTimestamp);

    }
    @Test
    public void calculateDueDateHrsScenario4() {
        // This test is used to calculate the dueDateHrs based on the OleCalendar input as configured by getOleCalendarsWithHolidaysExceptionDaysAndPeriods and getCalendarGroup method .
        // Info : In this method the Calendar is defined as MonDay,Tuesday,ThursDay,Friday have working hour (9:00 AM to 11 AM) with each day of week is true (day is bounded),
        // having exceptional working hour "6:00AM" to "10:00AM" form "25-04-2015" to "28-04-2015"
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-04-17 02:15:00.0' and "21-04-2015" is holiday
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");
        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidaysExceptionDaysAndPeriods());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDateHrs(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-04-17 02:15:00.0",dateTimestamp);

    }
    @Test
    public void calculateDueDateHrsScenario5() {
        // This test is used to calculate the dueDateHrs based on the OleCalendar input as configured by getOleCalendarsWithHolidaysEachExceptionDaysAndPeriodsTrue and getCalendarGroup method .
        // In this method the Calendar is defined as MonDay,Tuesday,ThursDay, Friday working hour (9:00 AM to 11 AM) with each day of week is false (day is unbounded)
        // having exceptional working hour "6:00AM" to "10:00AM" form "25-04-2015" to "28-04-2015" with  EachDayOfExceptionWeek is false (day is unbounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-D' the expected output will be '2015-04-17 02:15:00.0' and "21-04-2015" is holiday
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();

        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");
        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidaysEachExceptionDaysAndPeriodsTrue());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDateHrs(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-04-17 02:15:00.0",dateTimestamp);

    }
    @Test
    public void calculateDueDateHrsScenario6() {
        // This test is used to calculate the dueDateHrs based on the OleCalendar input as configured by getOleCalendarsWithHolidays and getCalendarGroup method .
        // In this method the Calendar is defined as MonDay-Friday working hour (9:00 AM to 11 AM) with each day of week is true (day is bounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-H' the expected output will be '2015-04-28 10:14:00.0'
        // CALENDAR_FLAG is false (it refers to the system parameter constant INCLUDE_NON_WORKING_HRS)
        CALENDAR_FLAG="false";
        TIME_PERIOD="10-H";
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");
        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidays());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDateHrs(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-04-28 10:14:00.0",dateTimestamp);

    }
    @Test
    public void calculateDueDateHrsScenario7() {
        // This test is used to calculate the dueDatehrs based on the OleCalendar input as configured by getOleCalendarsWithoutHolidays and getCalendarGroup method .
        // In this method the Calendar is defined as MonDay-Friday working hour (9:00 AM to 11 AM) with each day of week is false (day is unbounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-H' the expected output will be '2015-04-17 02:15:00.0'
        // CALENDAR_FLAG is false (it refers to the system parameter constant INCLUDE_NON_WORKING_HRS)
        CALENDAR_FLAG="false";
        TIME_PERIOD="10-H";
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");
        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithoutHolidays());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDateHrs(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-04-17 02:15:00.0",dateTimestamp);

    }
    @Test
    public void calculateDueDateHrsScenario8() {
        // This test is used to calculate the dueDateHrs based on the OleCalendar input as configured by getOleCalendarsWithHolidays1 and getCalendarGroup method .
        // Info : In this method the Calendar is defined as MonDay,Tuesday,ThursDay, Friday working hour (9:00 AM to 11 AM) with each day of week is false (day is unbounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-H' the expected output will be '2015-04-17 02:15:00.0'
        // CALENDAR_FLAG is false (it refers to the system parameter constant INCLUDE_NON_WORKING_HRS)
        CALENDAR_FLAG="false";
        TIME_PERIOD="10-H";
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();

        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");
        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidays1());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDateHrs(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-04-17 02:15:00.0",dateTimestamp);

    }
    @Test
    public void calculateDueDateHrsScenario9() {
        // This test is used to calculate the dueDateHrs based on the OleCalendar input as configured by getOleCalendarsWithHolidaysExceptionDays and getCalendarGroup method .
        // In this method the Calendar is defined as MonDay,Tuesday,ThursDay, Friday working hour (9:00 AM to 11 AM) with each day of week is false (day is unbounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-H' the expected output will be '2015-04-17 02:15:00.0' and "21-04-2015" is holiday
        TIME_PERIOD="10-H";
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");
        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidaysExceptionDays());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDateHrs(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-04-17 02:15:00.0",dateTimestamp);

    }
    @Test
    public void calculateDueDateHrsScenario10() {
        // This test is used to calculate the dueDateHrs based on the OleCalendar input as configured by getOleCalendarsWithHolidaysExceptionDaysAndPeriods and getCalendarGroup method .
        // Info : In this method the Calendar is defined as MonDay,Tuesday,ThursDay,Friday have working hour (9:00 AM to 11 AM) with each day of week is true (day is bounded),
        // having exceptional working hour "6:00AM" to "10:00AM" form "25-04-2015" to "28-04-2015"
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-H' the expected output will be '2015-04-17 02:15:00.0' and "21-04-2015" is holiday
        // CALENDAR_FLAG is false (it refers to the system parameter constant INCLUDE_NON_WORKING_HRS)
        CALENDAR_FLAG="false";
        TIME_PERIOD="10-H";
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");
        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidaysExceptionDaysAndPeriods());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDateHrs(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-04-17 02:15:00.0",dateTimestamp);

    }
    @Test
    public void calculateDueDateHrsScenario11() {
        // This test is used to calculate the dueDateHrs based on the OleCalendar input as configured by getOleCalendarsWithHolidaysEachExceptionDaysAndPeriodsTrue and getCalendarGroup method .
        // In this method the Calendar is defined as MonDay,Tuesday,ThursDay, Friday working hour (9:00 AM to 11 AM) with each day of week is false (day is unbounded)
        // having exceptional working hour "6:00AM" to "10:00AM" form "25-04-2015" to "28-04-2015" with  EachDayOfExceptionWeek is false (day is unbounded)
        // if user inputs the date as '16-04-2015 16:14:00' and the tme period as '10-H' the expected output will be '2015-04-17 02:15:00.0' and "21-04-2015" is holiday
        // INCLUDE_NON_WORKING_HRS=false in system parameter
        CALENDAR_FLAG="false";
        TIME_PERIOD="10-H";
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");

        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");
        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidaysEachExceptionDaysAndPeriodsTrue());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());

        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CALENDER_FLAG)).thenReturn(CALENDAR_FLAG);
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Timestamp timestamp = oleCalendarService.calculateDueDateHrs(CIR_ID, TIME_PERIOD, getTimeStamp(CURR_DATE));
        assertNotNull(timestamp);
        String dateTimestamp=timestamp.toString();
        assertEquals("2015-04-17 02:15:00.0",dateTimestamp);

    }

    @Test
    public void calculateFine() {
        // This is to calculate fine based on the dueDate passed i.e if current date exceeds the dueDate and fine is calculated based hour or day basis as defined in FINE_AMOUNT
        // as  this is based on the hour wise fine calculation
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        Mockito.when(mockParameterResolverInstance.getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.FINE_FLAG)).thenReturn(CALENDAR_FLAG);
        HashMap cg = new HashMap();
        cg.put(OLEConstants.CALENDER_ID, "100");
        HashMap calendarGroup = new HashMap();
        calendarGroup.put(OLEConstants.CALENDER_ID, "100");
        oleCalendarService.setParameterValueResolver(mockParameterResolverInstance);
        oleCalendarService.setBusinessObjectService(businessObjectService);
        Mockito.when(businessObjectService.findMatching(OleCalendar.class, cg)).thenReturn(getOleCalendarsWithHolidaysEachExceptionDaysAndPeriodsTrue());
        Mockito.when(businessObjectService.findMatching(OleCalendarGroup.class, calendarGroup)).thenReturn(getCalendarGroup());
        Float fine = oleCalendarService.calculateFine(CIR_ID, getTimeStamp(DUE_DATE), getTimeStamp(CURR_DATE), FINE_AMOUNT);
        assertNotNull(fine);
        assertEquals(320.0f,fine,0.0f);

    }

    private List<OleCalendar> getOleCalendarsWithHolidays() {
        List<OleCalendar> oleCalendars = new ArrayList<OleCalendar>();
        List<OleCalendarWeek> oleCalendarWeeks = new ArrayList<>();

        OleCalendar oleCalendar = new OleCalendar();
        oleCalendar.setCalendarId("100");
        oleCalendar.setCalendarDescription("Calendar1");
        oleCalendar.setBeginDate(getTimeStamp(CALENDAR_BEGIN_DATE));
        oleCalendar.setEndDate(getTimeStamp(CALENDAR_END_DATE));
        oleCalendar.setCalendarGroupId("100");

        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setCalendarWeekId("100");
        oleCalendarWeek.setCalendarId("100");
        oleCalendarWeek.setOpenTime("09:00");
        oleCalendarWeek.setCloseTime("11:00");
        oleCalendarWeek.setStartDay("1");
        oleCalendarWeek.setEndDay("5");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTimeSession("AM");
        oleCalendarWeek.setEachDayWeek(true);
        oleCalendarWeeks.add(oleCalendarWeek);

        oleCalendar.setOleCalendarWeekList(oleCalendarWeeks);
        oleCalendars.add(oleCalendar);
        return oleCalendars;
    }

    private List<OleCalendar> getOleCalendarsWithoutHolidays() {
        List<OleCalendar> oleCalendars = new ArrayList<OleCalendar>();
        List<OleCalendarWeek> oleCalendarWeeks = new ArrayList<>();

        OleCalendar oleCalendar = new OleCalendar();
        oleCalendar.setCalendarId("100");
        oleCalendar.setCalendarDescription("Calendar1");
        oleCalendar.setBeginDate(getTimeStamp(CALENDAR_BEGIN_DATE));
        oleCalendar.setEndDate(getTimeStamp(CALENDAR_END_DATE));
        oleCalendar.setCalendarGroupId("100");

        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setCalendarWeekId("100");
        oleCalendarWeek.setCalendarId("100");
        oleCalendarWeek.setOpenTime("09:00");
        oleCalendarWeek.setCloseTime("11:00");
        oleCalendarWeek.setStartDay("1");
        oleCalendarWeek.setEndDay("5");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTimeSession("AM");
        oleCalendarWeeks.add(oleCalendarWeek);

        oleCalendar.setOleCalendarWeekList(oleCalendarWeeks);
        oleCalendars.add(oleCalendar);
        return oleCalendars;
    }

    private List<OleCalendar> getOleCalendarsWithHolidays1() {
        List<OleCalendar> oleCalendars = new ArrayList<OleCalendar>();
        List<OleCalendarWeek> oleCalendarWeeks = new ArrayList<>();

        OleCalendar oleCalendar = new OleCalendar();
        oleCalendar.setCalendarId("100");
        oleCalendar.setCalendarDescription("Calendar1");
        oleCalendar.setBeginDate(getTimeStamp(CALENDAR_BEGIN_DATE));
        oleCalendar.setEndDate(getTimeStamp(CALENDAR_END_DATE));
        oleCalendar.setCalendarGroupId("100");

        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setCalendarWeekId("100");
        oleCalendarWeek.setCalendarId("100");
        oleCalendarWeek.setOpenTime("09:00");
        oleCalendarWeek.setCloseTime("11:00");
        oleCalendarWeek.setStartDay("1");
        oleCalendarWeek.setEndDay("2");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTimeSession("AM");
        oleCalendarWeeks.add(oleCalendarWeek);

        OleCalendarWeek oleCalendarWeek1 = new OleCalendarWeek();
        oleCalendarWeek1.setCalendarWeekId("100");
        oleCalendarWeek1.setCalendarId("100");
        oleCalendarWeek1.setOpenTime("09:00");
        oleCalendarWeek1.setCloseTime("11:00");
        oleCalendarWeek1.setStartDay("4");
        oleCalendarWeek1.setEndDay("5");
        oleCalendarWeek1.setOpenTimeSession("AM");
        oleCalendarWeek1.setCloseTimeSession("AM");
        oleCalendarWeeks.add(oleCalendarWeek1);

        oleCalendar.setOleCalendarWeekList(oleCalendarWeeks);
        oleCalendars.add(oleCalendar);
        return oleCalendars;
    }

    private List<OleCalendar> getOleCalendarsWithHolidaysExceptionDays() {
        List<OleCalendar> oleCalendars = new ArrayList<OleCalendar>();
        List<OleCalendarWeek> oleCalendarWeeks = new ArrayList<>();
        List<OleCalendarExceptionDate> oleCalendarExceptionDates = new ArrayList<>();

        OleCalendar oleCalendar = new OleCalendar();
        oleCalendar.setCalendarId("100");
        oleCalendar.setCalendarDescription("Calendar1");
        oleCalendar.setBeginDate(getTimeStamp(CALENDAR_BEGIN_DATE));
        oleCalendar.setEndDate(getTimeStamp(CALENDAR_END_DATE));
        oleCalendar.setCalendarGroupId("100");

        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setCalendarWeekId("100");
        oleCalendarWeek.setCalendarId("100");
        oleCalendarWeek.setOpenTime("09:00");
        oleCalendarWeek.setCloseTime("11:00");
        oleCalendarWeek.setStartDay("1");
        oleCalendarWeek.setEndDay("2");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTimeSession("AM");
        oleCalendarWeeks.add(oleCalendarWeek);

        OleCalendarWeek oleCalendarWeek1 = new OleCalendarWeek();
        oleCalendarWeek1.setCalendarWeekId("100");
        oleCalendarWeek1.setCalendarId("100");
        oleCalendarWeek1.setOpenTime("09:00");
        oleCalendarWeek1.setCloseTime("11:00");
        oleCalendarWeek1.setStartDay("4");
        oleCalendarWeek1.setEndDay("5");
        oleCalendarWeek1.setOpenTimeSession("AM");
        oleCalendarWeek1.setCloseTimeSession("AM");
        oleCalendarWeeks.add(oleCalendarWeek1);

        OleCalendarExceptionDate oleCalendarExceptionDate = new OleCalendarExceptionDate();
        oleCalendarExceptionDate.setCalendarId("100");
        oleCalendarExceptionDate.setExceptionDateDesc("Holiday");
        oleCalendarExceptionDate.setDate(getDate("21-04-2015"));
        oleCalendarExceptionDate.setExceptionType("Holiday");
        oleCalendarExceptionDates.add(oleCalendarExceptionDate);
        oleCalendar.setOleCalendarExceptionDateDeleteList(oleCalendarExceptionDates);
        oleCalendar.setOleCalendarWeekList(oleCalendarWeeks);
        oleCalendars.add(oleCalendar);
        return oleCalendars;
    }

    private List<OleCalendarGroup> getCalendarGroup(){
        ArrayList<OleCalendarGroup> oleCalendarGroups = new ArrayList<OleCalendarGroup>();
        OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
        oleCalendarGroup.setActive(true);
        oleCalendarGroup.setCalendarGroupId(CIR_ID);
        oleCalendarGroup.setCalendarGroupCode("GRP_001");
        oleCalendarGroup.setCalendarGroupName("Group 1");
        oleCalendarGroups.add(oleCalendarGroup);
        return  oleCalendarGroups;
    }

    private List<OleCalendar> getOleCalendarsWithHolidaysExceptionDaysAndPeriods() {
        List<OleCalendar> oleCalendars = new ArrayList<OleCalendar>();
        List<OleCalendarWeek> oleCalendarWeeks = new ArrayList<>();
        List<OleCalendarExceptionDate> oleCalendarExceptionDates = new ArrayList<>();
        List<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodWeeks = new ArrayList<>();
        List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriods = new ArrayList<>();

        OleCalendar oleCalendar = new OleCalendar();
        oleCalendar.setCalendarId("100");
        oleCalendar.setCalendarDescription("Calendar1");
        oleCalendar.setBeginDate(getTimeStamp(CALENDAR_BEGIN_DATE));
        oleCalendar.setEndDate(getTimeStamp(CALENDAR_END_DATE));
        oleCalendar.setCalendarGroupId("100");

        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setCalendarWeekId("100");
        oleCalendarWeek.setCalendarId("100");
        oleCalendarWeek.setOpenTime("09:00");
        oleCalendarWeek.setCloseTime("11:00");
        oleCalendarWeek.setStartDay("1");
        oleCalendarWeek.setEndDay("2");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTimeSession("AM");
        oleCalendarWeeks.add(oleCalendarWeek);

        OleCalendarWeek oleCalendarWeek1 = new OleCalendarWeek();
        oleCalendarWeek1.setCalendarWeekId("100");
        oleCalendarWeek1.setCalendarId("100");
        oleCalendarWeek1.setOpenTime("09:00");
        oleCalendarWeek1.setCloseTime("11:00");
        oleCalendarWeek1.setStartDay("4");
        oleCalendarWeek1.setEndDay("5");
        oleCalendarWeek1.setOpenTimeSession("AM");
        oleCalendarWeek1.setCloseTimeSession("AM");
        oleCalendarWeeks.add(oleCalendarWeek1);

        OleCalendarExceptionDate oleCalendarExceptionDate = new OleCalendarExceptionDate();
        oleCalendarExceptionDate.setCalendarId("100");
        oleCalendarExceptionDate.setExceptionDateDesc("Holiday");
        oleCalendarExceptionDate.setDate(getDate("04-05-2015 00:00:00"));
        oleCalendarExceptionDate.setExceptionType("Holiday");
        oleCalendarExceptionDates.add(oleCalendarExceptionDate);


        OleCalendarExceptionDate oleCalendarExceptionDate1 = new OleCalendarExceptionDate();
        oleCalendarExceptionDate1.setCalendarId("100");
        oleCalendarExceptionDate1.setExceptionDateDesc("Partial");
        oleCalendarExceptionDate1.setDate(getDate("21-04-2015 00:00:00"));
        oleCalendarExceptionDate1.setExceptionType("Partial");
        oleCalendarExceptionDate1.setOpenTime("06:00");
        oleCalendarExceptionDate1.setCloseTime("10:00");
        oleCalendarExceptionDate1.setOpenTimeSession("AM");
        oleCalendarExceptionDate1.setCloseTimeSession("AM");
        oleCalendarExceptionDates.add(oleCalendarExceptionDate1);


        OleCalendarExceptionPeriod oleCalendarExceptionPeriod1 = new OleCalendarExceptionPeriod();
        oleCalendarExceptionPeriod1.setCalendarId("100");
        oleCalendarExceptionPeriod1.setBeginDate(getTimeStamp("21-04-2015 00:00:00"));
        oleCalendarExceptionPeriod1.setEndDate(getTimeStamp("24-04-2015 00:00:00"));
        oleCalendarExceptionPeriod1.setCalendarExceptionPeriodDesc("Holiday");
        oleCalendarExceptionPeriod1.setExceptionPeriodType("Holiday");

        OleCalendarExceptionPeriod oleCalendarExceptionPeriod2 = new OleCalendarExceptionPeriod();
        oleCalendarExceptionPeriod2.setCalendarId("100");
        oleCalendarExceptionPeriod2.setBeginDate(getTimeStamp("25-04-2015 00:00:00"));
        oleCalendarExceptionPeriod2.setEndDate(getTimeStamp("28-04-2015 00:00:00"));
        oleCalendarExceptionPeriod2.setCalendarExceptionPeriodDesc("Partial");
        oleCalendarExceptionPeriod2.setExceptionPeriodType("Partial");

        OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek = new OleCalendarExceptionPeriodWeek();
        oleCalendarExceptionPeriodWeek.setOpenTime("06:00");
        oleCalendarExceptionPeriodWeek.setCloseTime("10:00");
        oleCalendarExceptionPeriodWeek.setOpenTimeSession("AM");
        oleCalendarExceptionPeriodWeek.setCloseTimeSession("AM");
        oleCalendarExceptionPeriodWeek.setStartDay("4");
        oleCalendarExceptionPeriodWeek.setEndDay("5");


        oleCalendarExceptionPeriodWeeks.add(oleCalendarExceptionPeriodWeek);
        oleCalendarExceptionPeriod2.setOleCalendarExceptionPeriodWeekList(oleCalendarExceptionPeriodWeeks);
        oleCalendarExceptionPeriods.add(oleCalendarExceptionPeriod1);
        oleCalendarExceptionPeriods.add(oleCalendarExceptionPeriod2);
        oleCalendar.setOleCalendarExceptionPeriodList(oleCalendarExceptionPeriods);
        oleCalendar.setOleCalendarExceptionDateDeleteList(oleCalendarExceptionDates);
        oleCalendar.setOleCalendarWeekList(oleCalendarWeeks);
        oleCalendars.add(oleCalendar);

        return oleCalendars;
    }


    private List<OleCalendar> getOleCalendarsWithHolidaysEachExceptionDaysAndPeriodsTrue() {
        List<OleCalendar> oleCalendars = new ArrayList<OleCalendar>();
        List<OleCalendarWeek> oleCalendarWeeks = new ArrayList<>();
        List<OleCalendarExceptionDate> oleCalendarExceptionDates = new ArrayList<>();
        List<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodWeeks = new ArrayList<>();
        List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriods = new ArrayList<>();

        OleCalendar oleCalendar = new OleCalendar();
        oleCalendar.setCalendarId("100");
        oleCalendar.setCalendarDescription("Calendar1");
        oleCalendar.setBeginDate(getTimeStamp(CALENDAR_BEGIN_DATE));
        oleCalendar.setEndDate(getTimeStamp(CALENDAR_END_DATE));
        oleCalendar.setCalendarGroupId("100");

        OleCalendarWeek oleCalendarWeek = new OleCalendarWeek();
        oleCalendarWeek.setCalendarWeekId("100");
        oleCalendarWeek.setCalendarId("100");
        oleCalendarWeek.setOpenTime("09:00");
        oleCalendarWeek.setCloseTime("11:00");
        oleCalendarWeek.setStartDay("1");
        oleCalendarWeek.setEndDay("2");
        oleCalendarWeek.setOpenTimeSession("AM");
        oleCalendarWeek.setCloseTimeSession("AM");
        oleCalendarWeeks.add(oleCalendarWeek);

        OleCalendarWeek oleCalendarWeek1 = new OleCalendarWeek();
        oleCalendarWeek1.setCalendarWeekId("100");
        oleCalendarWeek1.setCalendarId("100");
        oleCalendarWeek1.setOpenTime("09:00");
        oleCalendarWeek1.setCloseTime("11:00");
        oleCalendarWeek1.setStartDay("4");
        oleCalendarWeek1.setEndDay("5");
        oleCalendarWeek1.setOpenTimeSession("AM");
        oleCalendarWeek1.setCloseTimeSession("AM");
        oleCalendarWeeks.add(oleCalendarWeek1);

        OleCalendarExceptionDate oleCalendarExceptionDate = new OleCalendarExceptionDate();
        oleCalendarExceptionDate.setCalendarId("100");
        oleCalendarExceptionDate.setExceptionDateDesc("Holiday");
        oleCalendarExceptionDate.setDate(getDate("04-05-2015 00:00:00"));
        oleCalendarExceptionDate.setExceptionType("Holiday");
        oleCalendarExceptionDates.add(oleCalendarExceptionDate);


        OleCalendarExceptionDate oleCalendarExceptionDate1 = new OleCalendarExceptionDate();
        oleCalendarExceptionDate1.setCalendarId("100");
        oleCalendarExceptionDate1.setExceptionDateDesc("Partial");
        oleCalendarExceptionDate1.setDate(getDate("21-04-2015 00:00:00"));
        oleCalendarExceptionDate1.setExceptionType("Partial");
        oleCalendarExceptionDate1.setOpenTime("06:00");
        oleCalendarExceptionDate1.setCloseTime("10:00");
        oleCalendarExceptionDate1.setOpenTimeSession("AM");
        oleCalendarExceptionDate1.setCloseTimeSession("AM");
        oleCalendarExceptionDates.add(oleCalendarExceptionDate1);


        OleCalendarExceptionPeriod oleCalendarExceptionPeriod1 = new OleCalendarExceptionPeriod();
        oleCalendarExceptionPeriod1.setCalendarId("100");
        oleCalendarExceptionPeriod1.setBeginDate(getTimeStamp("21-04-2015 00:00:00"));
        oleCalendarExceptionPeriod1.setEndDate(getTimeStamp("24-04-2015 00:00:00"));
        oleCalendarExceptionPeriod1.setCalendarExceptionPeriodDesc("Holiday");
        oleCalendarExceptionPeriod1.setExceptionPeriodType("Holiday");

        OleCalendarExceptionPeriod oleCalendarExceptionPeriod2 = new OleCalendarExceptionPeriod();
        oleCalendarExceptionPeriod2.setCalendarId("100");
        oleCalendarExceptionPeriod2.setBeginDate(getTimeStamp("25-04-2015 00:00:00"));
        oleCalendarExceptionPeriod2.setEndDate(getTimeStamp("28-04-2015 00:00:00"));
        oleCalendarExceptionPeriod2.setCalendarExceptionPeriodDesc("Partial");
        oleCalendarExceptionPeriod2.setExceptionPeriodType("Partial");

        OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek = new OleCalendarExceptionPeriodWeek();
        oleCalendarExceptionPeriodWeek.setOpenTime("06:00");
        oleCalendarExceptionPeriodWeek.setCloseTime("10:00");
        oleCalendarExceptionPeriodWeek.setOpenTimeSession("AM");
        oleCalendarExceptionPeriodWeek.setCloseTimeSession("AM");
        oleCalendarExceptionPeriodWeek.setStartDay("4");
        oleCalendarExceptionPeriodWeek.setEndDay("5");
        oleCalendarExceptionPeriodWeek.setEachDayOfExceptionWeek(true);


        oleCalendarExceptionPeriodWeeks.add(oleCalendarExceptionPeriodWeek);
        oleCalendarExceptionPeriod2.setOleCalendarExceptionPeriodWeekList(oleCalendarExceptionPeriodWeeks);
        oleCalendarExceptionPeriods.add(oleCalendarExceptionPeriod1);
        oleCalendarExceptionPeriods.add(oleCalendarExceptionPeriod2);
        oleCalendar.setOleCalendarExceptionPeriodList(oleCalendarExceptionPeriods);
        oleCalendar.setOleCalendarExceptionDateDeleteList(oleCalendarExceptionDates);
        oleCalendar.setOleCalendarWeekList(oleCalendarWeeks);
        oleCalendars.add(oleCalendar);

        return oleCalendars;
    }

    private Date getDate(String dateString) {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    private Timestamp getTimeStamp(String dateString) {
        if (dateString == null) {
            return null;
        }
        Date date = getDate(dateString);
        if (date != null) {
            return new Timestamp(date.getTime());
        } else {
            return new Timestamp(System.currentTimeMillis());
        }
    }
}
