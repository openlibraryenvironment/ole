package org.kuali.ole.deliver.util;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.calendar.bo.OleCalendar;
import org.kuali.ole.deliver.calendar.bo.OleCalendarExceptionDate;
import org.kuali.ole.deliver.calendar.bo.OleCalendarGroup;
import org.kuali.ole.deliver.calendar.bo.OleCalendarWeek;
import org.kuali.ole.utility.DateTimeUtil;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;

/**
 * Created by pvsubrah on 10/2/15.
 */
public class OleDateTimeUtil_IT {
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

    @Test
    public void calculateDueDateForAmountOfMinutes() throws Exception {
        String loanPeriod = "30-M";
        Date dueDate = new OleDateTimeUtil().calculateDateTimeByPeriod(loanPeriod, null);
        assertNotNull(dueDate);
        System.out.println("Added 30 minutes:" + dueDate + "\n");
    }


    @Test
    public void calculateDueDateForAmountOfDays() throws Exception {
        String loanPeriod = "1-D";
        Date dueDate = new OleDateTimeUtil().calculateDateTimeByPeriod(loanPeriod, null);
        assertNotNull(dueDate);
        System.out.println("Added 1 day:" +dueDate+ "\n");
    }


    @Test
    public void calculateDueDateForAmountOfHours() throws Exception {
        String loanPeriod = "2-H";
        Date dueDate = new OleDateTimeUtil().calculateDateTimeByPeriod(loanPeriod, null);
        assertNotNull(dueDate);
        System.out.println("Added 2 hours:" +dueDate+ "\n");
    }

    @Test
    public void calculateDueDateForAmountOfWeeks() throws Exception {
        String loanPeriod = "2-W";
        Date dueDate = new OleDateTimeUtil().calculateDateTimeByPeriod(loanPeriod, null);
        assertNotNull(dueDate);
        System.out.println("Added 2 Weeks:" +dueDate+ "\n");
    }

    @Test
    public void calExists() throws Exception {
        OleDateTimeUtil oleDateTimeUtil = new OleDateTimeUtil();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        boolean calendarExists = oleDateTimeUtil.calendarExists(new Timestamp(System.currentTimeMillis()), new Timestamp(calendar.getTimeInMillis()), null);
        assertTrue(calendarExists);
    }

    @Test
    public void calDoesNotExists() throws Exception {
        OleDateTimeUtil oleDateTimeUtil = new OleDateTimeUtil();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        boolean calendarExists = oleDateTimeUtil.calendarExists(new Timestamp(calendar.getTimeInMillis()),new Timestamp(System.currentTimeMillis()), null);
        assertFalse(calendarExists);
    }


    @Test
    public void compareTime() throws Exception {
        String time = "09:45";
        StringTokenizer timeTokenizer = new StringTokenizer(time, ":");
        String timeSession = "AM";

        Calendar instance = Calendar.getInstance();
        int hour = timeSession.equalsIgnoreCase("am") ? Integer.parseInt(timeTokenizer.nextToken()) : Integer.parseInt(timeTokenizer.nextToken()) + 12;
        instance.set(Calendar.HOUR, hour);
        instance.set(Calendar.MINUTE, Integer.parseInt(timeTokenizer.nextToken()));

        Date date = new Date(System.currentTimeMillis());

        boolean before = instance.getTime().before(date);
        assertTrue(before);
    }

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
        oleCalendarWeek.setCloseTime("08:00");
        oleCalendarWeek.setCloseTimeSession("AM");
        oleCalendarWeek.setStartDay("0");
        oleCalendarWeek.setEndDay("6");
        oleCalendarWeekList.add(oleCalendarWeek);

        mockOleCalendar.setOleCalendarWeekList(oleCalendarWeekList);
        mockOleCalendar.setOleCalendarGroup(oleCalendarGroup);


        OleDateTimeUtil oleDateTimeUtil = new MockOleDateTimeUtil();
        Date date = oleDateTimeUtil.calculateDateTimeByPeriod("1-D", oleCirculationDesk);
        assertNotNull(date);
        System.out.println(date);
    }



    class MockOleDateTimeUtil extends OleDateTimeUtil {
        @Override
        protected List<OleCalendar> getOleCalendars(String groupId) {
            if(groupId.equalsIgnoreCase("1")){
                ArrayList<OleCalendar> oleCalendars = new ArrayList<>();
                oleCalendars.add(mockOleCalendar);
                return oleCalendars;
            }
            return  null;
        }
    }
}
