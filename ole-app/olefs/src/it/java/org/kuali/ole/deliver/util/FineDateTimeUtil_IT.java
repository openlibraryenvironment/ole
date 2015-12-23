package org.kuali.ole.deliver.util;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.calendar.bo.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenchulakshmig on 12/11/15.
 */
public class FineDateTimeUtil_IT {

    @Test
    public void calculateOverdueFineAmountInDays() {
        MockFineDateTimeUtil mockFineDateTimeUtil = new MockFineDateTimeUtil();

        ItemFineRate itemFineRate = new ItemFineRate();
        itemFineRate.setInterval("d");
        itemFineRate.setFineRate(new Double("20.00"));

        Date dueDate = DateUtils.addDays(new Date(), -1);
        dueDate.setHours(13);
        dueDate.setMinutes(0);
        Date checkInDate = DateUtils.addDays(new Date(), -1);
        checkInDate.setHours(16);
        checkInDate.setMinutes(0);
        Double fineAmt = mockFineDateTimeUtil.calculateOverdueFine("1", dueDate, checkInDate, itemFineRate);
        System.out.println("Fine Amount " + fineAmt);
    }

    @Test
    public void calculateOverdueFineAmountInHrs() {
        MockFineDateTimeUtil mockFineDateTimeUtil = new MockFineDateTimeUtil();

        ItemFineRate itemFineRate = new ItemFineRate();
        itemFineRate.setInterval("h");
        itemFineRate.setFineRate(new Double("10.00"));

        Date dueDate = DateUtils.addDays(new Date(), -3);
        dueDate.setHours(8);
        dueDate.setMinutes(0);
        Date checkInDate = DateUtils.addDays(new Date(), -1);
        checkInDate.setHours(17);
        checkInDate.setMinutes(0);
        Double fineAmt = mockFineDateTimeUtil.calculateOverdueFine("1", dueDate, checkInDate, itemFineRate);
        System.out.println("Fine Amount " + fineAmt);
    }

    class MockFineDateTimeUtil extends FineDateTimeUtil {

        @Override
        public Boolean isIncludeNonWorkingHoursForFineCalc() {
            return false;
        }

        @Override
        public List<OleCalendar> getOleCalendars(String deskId) {
            OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();
            oleCirculationDesk.setCalendarGroupId("100");

            List<OleCalendar> oleCalendarList = new ArrayList<>();

            OleCalendar oleCalendar = new OleCalendar();
            oleCalendar.setBeginDate(new Timestamp(DateUtils.addDays(new Date(), -10).getTime()));

            List<OleCalendarWeek> oleCalendarWeekList = getCalendarWeeks();
            oleCalendar.setOleCalendarWeekList(oleCalendarWeekList);

            List<OleCalendarExceptionDate> oleCalendarExceptionDateList = getHolidayCalendarExceptionDates();
            oleCalendar.setOleCalendarExceptionDateList(oleCalendarExceptionDateList);

            /*List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList = getPartialCalendarExceptionPeriods();
            oleCalendar.setOleCalendarExceptionPeriodList(oleCalendarExceptionPeriodList);*/

            OleCalendarGroup oleCalendarGroup = new OleCalendarGroup();
            oleCalendarGroup.setCalendarGroupId("100");
            oleCalendar.setOleCalendarGroup(oleCalendarGroup);

            oleCalendarList.add(oleCalendar);
            return oleCalendarList;
        }

        private List<OleCalendarWeek> getCalendarWeeks(){
            List<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
            OleCalendarWeek oleCalendarWeek1 = new OleCalendarWeek();
            oleCalendarWeek1.setEachDayWeek(true);
            oleCalendarWeek1.setOpenTime("09:00");
            oleCalendarWeek1.setOpenTimeSession("AM");
            oleCalendarWeek1.setCloseTime("05:00");
            oleCalendarWeek1.setCloseTimeSession("PM");
            oleCalendarWeek1.setStartDay("0");
            oleCalendarWeek1.setEndDay("6");
            oleCalendarWeekList.add(oleCalendarWeek1);

            return oleCalendarWeekList;
        }

        private List<OleCalendarExceptionDate> getHolidayCalendarExceptionDates() {
            List<OleCalendarExceptionDate> oleCalendarExceptionDateList = new ArrayList<>();
            OleCalendarExceptionDate oleCalendarExceptionDate1 = new OleCalendarExceptionDate();
            oleCalendarExceptionDate1.setExceptionType("Holiday");
            oleCalendarExceptionDate1.setDate(DateUtils.addDays(new Date(), -8));
            oleCalendarExceptionDateList.add(oleCalendarExceptionDate1);

            OleCalendarExceptionDate oleCalendarExceptionDate2 = new OleCalendarExceptionDate();
            oleCalendarExceptionDate2.setExceptionType("Holiday");
            oleCalendarExceptionDate2.setDate(DateUtils.addDays(new Date(), -5));
            oleCalendarExceptionDateList.add(oleCalendarExceptionDate2);
            return oleCalendarExceptionDateList;
        }

        private List<OleCalendarExceptionDate> getPartialCalendarExceptionDates() {
            List<OleCalendarExceptionDate> oleCalendarExceptionDateList = new ArrayList<>();
            OleCalendarExceptionDate oleCalendarExceptionDate1 = new OleCalendarExceptionDate();
            oleCalendarExceptionDate1.setExceptionType("Partial");
            oleCalendarExceptionDate1.setDate(new Date());
            oleCalendarExceptionDate1.setOpenTime("08:00");
            oleCalendarExceptionDate1.setOpenTimeSession("AM");
            oleCalendarExceptionDate1.setCloseTime("05:00");
            oleCalendarExceptionDate1.setCloseTimeSession("PM");
            oleCalendarExceptionDateList.add(oleCalendarExceptionDate1);

            OleCalendarExceptionDate oleCalendarExceptionDate2 = new OleCalendarExceptionDate();
            oleCalendarExceptionDate2.setExceptionType("Partial");
            oleCalendarExceptionDate2.setDate(DateUtils.addDays(new Date(), -2));
            oleCalendarExceptionDate2.setOpenTime("09:00");
            oleCalendarExceptionDate2.setOpenTimeSession("AM");
            oleCalendarExceptionDate2.setCloseTime("06:00");
            oleCalendarExceptionDate2.setCloseTimeSession("PM");
            oleCalendarExceptionDateList.add(oleCalendarExceptionDate2);
            return oleCalendarExceptionDateList;
        }

        private List<OleCalendarExceptionPeriod> getHolidayCalendarExceptionPeriods() {
            List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList = new ArrayList<>();
            OleCalendarExceptionPeriod oleCalendarExceptionPeriod1 = new OleCalendarExceptionPeriod();
            oleCalendarExceptionPeriod1.setExceptionPeriodType("Holiday");
            oleCalendarExceptionPeriod1.setBeginDate(new Timestamp(DateUtils.addDays(new Date(), -10).getTime()));
            oleCalendarExceptionPeriod1.setEndDate(new Timestamp(DateUtils.addDays(new Date(), -8).getTime()));
            oleCalendarExceptionPeriodList.add(oleCalendarExceptionPeriod1);

            OleCalendarExceptionPeriod oleCalendarExceptionPeriod2 = new OleCalendarExceptionPeriod();
            oleCalendarExceptionPeriod2.setExceptionPeriodType("Holiday");
            oleCalendarExceptionPeriod2.setBeginDate(new Timestamp(DateUtils.addDays(new Date(), -7).getTime()));
            oleCalendarExceptionPeriod2.setEndDate(new Timestamp(DateUtils.addDays(new Date(), -6).getTime()));
            oleCalendarExceptionPeriodList.add(oleCalendarExceptionPeriod2);
            return oleCalendarExceptionPeriodList;
        }

        private List<OleCalendarExceptionPeriod> getPartialCalendarExceptionPeriods() {
            List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList = new ArrayList<>();
            OleCalendarExceptionPeriod oleCalendarExceptionPeriod1 = new OleCalendarExceptionPeriod();
            oleCalendarExceptionPeriod1.setExceptionPeriodType("Partial");
            oleCalendarExceptionPeriod1.setBeginDate(new Timestamp(DateUtils.addDays(new Date(), -5).getTime()));
            oleCalendarExceptionPeriod1.setEndDate(new Timestamp(DateUtils.addDays(new Date(), -2).getTime()));

            List<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodWeekList = new ArrayList<>();
            OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek1 = new OleCalendarExceptionPeriodWeek();
            oleCalendarExceptionPeriodWeek1.setOpenTime("09:00");
            oleCalendarExceptionPeriodWeek1.setOpenTimeSession("AM");
            oleCalendarExceptionPeriodWeek1.setCloseTime("08:45");
            oleCalendarExceptionPeriodWeek1.setCloseTimeSession("PM");
            oleCalendarExceptionPeriodWeek1.setStartDay("0");
            oleCalendarExceptionPeriodWeek1.setEndDay("3");
            oleCalendarExceptionPeriodWeek1.setEachDayOfExceptionWeek(false);
            oleCalendarExceptionPeriodWeekList.add(oleCalendarExceptionPeriodWeek1);

            oleCalendarExceptionPeriod1.setOleCalendarExceptionPeriodWeekList(oleCalendarExceptionPeriodWeekList);

            oleCalendarExceptionPeriodList.add(oleCalendarExceptionPeriod1);
            return oleCalendarExceptionPeriodList;
        }
    }
}
