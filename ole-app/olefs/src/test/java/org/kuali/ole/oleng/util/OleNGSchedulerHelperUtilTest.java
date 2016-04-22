package org.kuali.ole.oleng.util;

import org.junit.Test;
import org.kuali.ole.constants.OleNGConstants;
import org.quartz.CronExpression;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 4/21/2016.
 */
public class OleNGSchedulerHelperUtilTest {

    @Test
    public void getCronExpressionForOneTime() throws ParseException {
        String date = "2016-JUN-5";
        String time = "10:20";
        Date parse = OleNGConstants.DATE_FORMAT_WITHOUT_TIME.parse(date);
        OleNGSchedulerHelperUtil oleNGSchedulerHelperUtil = new OleNGSchedulerHelperUtil();
        String cronExpressionForOneTime = oleNGSchedulerHelperUtil.createCronExpressionForOneTime(new Timestamp(parse.getTime()), time);
        assertNotNull(cronExpressionForOneTime);
        System.out.println(cronExpressionForOneTime);
        CronExpression cronExpression = new CronExpression(cronExpressionForOneTime);
        assertTrue(CronExpression.isValidExpression(cronExpressionForOneTime));
        System.out.println(cronExpression.getNextValidTimeAfter(new Date()));
    }

    @Test
    public void getCronExpressionForOneTime_WithoutTime() throws ParseException {
        String date = "2016-JUN-5";
        String time = "";
        Date parse = OleNGConstants.DATE_FORMAT_WITHOUT_TIME.parse(date);
        OleNGSchedulerHelperUtil oleNGSchedulerHelperUtil = new OleNGSchedulerHelperUtil();
        String cronExpressionForOneTime = oleNGSchedulerHelperUtil.createCronExpressionForOneTime(new Timestamp(parse.getTime()), time);
        assertNotNull(cronExpressionForOneTime);
        System.out.println(cronExpressionForOneTime);
        CronExpression cronExpression = new CronExpression(cronExpressionForOneTime);
        assertTrue(CronExpression.isValidExpression(cronExpressionForOneTime));
        System.out.println(cronExpression.getNextValidTimeAfter(new Date()));
    }

    @Test
    public void getCronExpressionForOneTime_WithoutDate() throws ParseException {
        String date = "";
        String time = "";
        OleNGSchedulerHelperUtil oleNGSchedulerHelperUtil = new OleNGSchedulerHelperUtil();
        String cronExpressionForOneTime = oleNGSchedulerHelperUtil.createCronExpressionForOneTime(null, time);
        assertNull(cronExpressionForOneTime);
    }


    @Test
    public void testCreateCronExpressionForDaily() throws ParseException {
        String time = "10:20";
        OleNGSchedulerHelperUtil oleNGSchedulerHelperUtil = new OleNGSchedulerHelperUtil();
        String cronExpressionForOneTime = oleNGSchedulerHelperUtil.createCronExpressionForDaily(time);
        assertNotNull(cronExpressionForOneTime);
        System.out.println(cronExpressionForOneTime);
        CronExpression cronExpression = new CronExpression(cronExpressionForOneTime);
        assertTrue(CronExpression.isValidExpression(cronExpressionForOneTime));
        System.out.println(cronExpression.getNextValidTimeAfter(new Date()));
    }

    @Test
    public void testCreateCronExpressionForDaily_WithoutHour() throws ParseException {
        String time = "10:20";
        OleNGSchedulerHelperUtil oleNGSchedulerHelperUtil = new OleNGSchedulerHelperUtil();
        String cronExpressionForOneTime = oleNGSchedulerHelperUtil.createCronExpressionForDaily(time);
        assertNotNull(cronExpressionForOneTime);
        System.out.println(cronExpressionForOneTime);
        CronExpression cronExpression = new CronExpression(cronExpressionForOneTime);
        assertTrue(CronExpression.isValidExpression(cronExpressionForOneTime));
        System.out.println(cronExpression.getNextValidTimeAfter(new Date()));
    }

    @Test
    public void testCreateCronExpressionForDaily_WithoutMinutes() throws ParseException {
        String time = "10";
        OleNGSchedulerHelperUtil oleNGSchedulerHelperUtil = new OleNGSchedulerHelperUtil();
        String cronExpressionForOneTime = oleNGSchedulerHelperUtil.createCronExpressionForDaily(time);
        assertNotNull(cronExpressionForOneTime);
        System.out.println(cronExpressionForOneTime);
        CronExpression cronExpression = new CronExpression(cronExpressionForOneTime);
        assertTrue(CronExpression.isValidExpression(cronExpressionForOneTime));
        System.out.println(cronExpression.getNextValidTimeAfter(new Date()));
    }

    @Test
    public void testCreateCronExpressionForDaily_WithoutHourAndMinutes() throws ParseException {
        String time = "";
        OleNGSchedulerHelperUtil oleNGSchedulerHelperUtil = new OleNGSchedulerHelperUtil();
        String cronExpressionForOneTime = oleNGSchedulerHelperUtil.createCronExpressionForDaily(time);
        assertNull(cronExpressionForOneTime);
    }

    @Test
    public void testCreateCronExpressionForWeekly() throws ParseException {
        String weekDay = "MON";
        String time = "10:20";
        OleNGSchedulerHelperUtil oleNGSchedulerHelperUtil = new OleNGSchedulerHelperUtil();
        String cronExpressionForOneTime = oleNGSchedulerHelperUtil.createCronExpressionForWeekly(weekDay, time);
        assertNotNull(cronExpressionForOneTime);
        System.out.println(cronExpressionForOneTime);
        CronExpression cronExpression = new CronExpression(cronExpressionForOneTime);
        assertTrue(CronExpression.isValidExpression(cronExpressionForOneTime));
        System.out.println(cronExpression.getNextValidTimeAfter(new Date()));
    }

    @Test
    public void testCreateCronExpressionForMonthly() throws ParseException {
        String day = "31";
        String frequency = "5";
        String time = "12:00";
        OleNGSchedulerHelperUtil oleNGSchedulerHelperUtil = new OleNGSchedulerHelperUtil();
        String cronExpressionForOneTime = oleNGSchedulerHelperUtil.createCronExpressionForMonthly(day, frequency, time);
        assertNotNull(cronExpressionForOneTime);
        System.out.println(cronExpressionForOneTime);
        CronExpression cronExpression = new CronExpression(cronExpressionForOneTime);
        assertTrue(CronExpression.isValidExpression(cronExpressionForOneTime));
        System.out.println(cronExpression.getNextValidTimeAfter(new Date()));
    }

}