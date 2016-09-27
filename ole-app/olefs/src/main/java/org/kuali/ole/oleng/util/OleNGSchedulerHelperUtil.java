package org.kuali.ole.oleng.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.oleng.batch.process.model.BatchScheduleJob;
import org.quartz.CronExpression;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by SheikS on 4/21/2016.
 */
public class OleNGSchedulerHelperUtil extends BusinessObjectServiceHelperUtil {


    public String getCronExpression(BatchScheduleJob batchScheduleJob) {
        String scheduleOption = batchScheduleJob.getScheduleOption();
        if(StringUtils.isNotBlank(scheduleOption)) {
            if(scheduleOption.equalsIgnoreCase(OleNGConstants.PROVIDE_CRON_EXPRESSION)) {
                return batchScheduleJob.getCronExpression();
            } else if(scheduleOption.equalsIgnoreCase(OleNGConstants.SCHEDULE)) {
                String scheduleType = batchScheduleJob.getScheduleType();
                String scheduleTime = batchScheduleJob.getScheduleTime();
                if(OleNGConstants.ONCE.equalsIgnoreCase(scheduleType)) {
                    return createCronExpressionForOneTime(batchScheduleJob.getScheduleDate(), scheduleTime);
                } else if(OleNGConstants.DAILY.equalsIgnoreCase(scheduleType)) {
                    return createCronExpressionForDaily(scheduleTime);
                } else if(OleNGConstants.WEEKLY.equalsIgnoreCase(scheduleType)) {
                   return createCronExpressionForWeekly(batchScheduleJob.getWeekDay(), scheduleTime);
                } else if(OleNGConstants.MONTHLY.equalsIgnoreCase(scheduleType)) {
                    return createCronExpressionForMonthly(batchScheduleJob.getMonthDay(), batchScheduleJob.getMonthFrequency(), scheduleTime);
                }
            }
        }
        return null;
    }

    public String createCronExpressionForOneTime(Timestamp scheduleDate, String scheduleTimeString) {
        String cronExpression = null;
        try {
            if (null != scheduleDate) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(scheduleDate.getTime()));
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                ;
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                String[] timeArray = scheduleTimeString.split(":");
                int hour = 0;
                int minutes = 0 ;
                if(timeArray.length > 0 && NumberUtils.isDigits(timeArray[0])) {
                    hour = Integer.valueOf(timeArray[0]);
                    if(hour > 0 && hour > 24) {
                        hour = 0;
                    }
                }
                if(timeArray.length > 1 && NumberUtils.isDigits(timeArray[1])) {
                    minutes = Integer.valueOf(timeArray[1]);
                    if(minutes > 0 && minutes > 59) {
                        minutes = 0;
                    }
                }
                cronExpression = "0 " + minutes + " " + hour + " " + day + " " + month
                        + " ? " + year;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cronExpression;
    }

    public String createCronExpressionForDaily(String scheduleTime) {
        String cron = null;
        if (StringUtils.isNotBlank(scheduleTime)) {
            String[] timeArray = scheduleTime.split(":");
            int hour = 0;
            int minutes = 0 ;
            if(timeArray.length > 0 && NumberUtils.isDigits(timeArray[0])) {
                hour = Integer.valueOf(timeArray[0]);
                if(hour > 0 && hour > 24) {
                    hour = 0;
                }
            }
            if(timeArray.length > 1 && NumberUtils.isDigits(timeArray[1])) {
                minutes = Integer.valueOf(timeArray[1]);
                if(minutes > 0 && minutes > 59) {
                    minutes = 0;
                }
            }
            cron = "0 " + minutes + " " + hour + " " + "1/1 " + "* " + "? " + "*";
        }
        return cron;
    }

    public String createCronExpressionForWeekly(String weekDay, String scheduleTime) {
        String cron = null;
        if (StringUtils.isNotBlank(scheduleTime) && StringUtils.isNotBlank(weekDay)) {
            String[] timeArray = scheduleTime.split(":");
            int hour = 0;
            int minutes = 0 ;
            if(timeArray.length > 0 && NumberUtils.isDigits(timeArray[0])) {
                hour = Integer.valueOf(timeArray[0]);
                if(hour > 0 && hour > 24) {
                    hour = 0;
                }
            }
            if(timeArray.length > 1 && NumberUtils.isDigits(timeArray[1])) {
                minutes = Integer.valueOf(timeArray[1]);
                if(minutes > 0 && minutes > 59) {
                    minutes = 0;
                }
            }
            cron = "0 " + minutes + " " + hour + " " + "? " + "* " + weekDay + " *";
        }
        return cron;
    }

    public String createCronExpressionForMonthly(String day, String frequency, String scheduleTime) {
        String cron = null;
        if (StringUtils.isNotBlank(scheduleTime) && NumberUtils.isDigits(day)
                && NumberUtils.isDigits(frequency) && (Integer.parseInt(day) > 0 && Integer.parseInt(day) <= 31) &&
                (Integer.parseInt(frequency) > 0 && Integer.parseInt(frequency) <= 12)) {
            String[] timeArray = scheduleTime.split(":");
            int hour = 0;
            int minutes = 0 ;
            if(timeArray.length > 0 && NumberUtils.isDigits(timeArray[0])) {
                hour = Integer.valueOf(timeArray[0]);
                if(hour > 0 && hour > 24) {
                    hour = 0;
                }
            }
            if(timeArray.length > 1 && NumberUtils.isDigits(timeArray[1])) {
                minutes = Integer.valueOf(timeArray[1]);
                if(minutes > 0 && minutes > 59) {
                    minutes = 0;
                }
            }
            cron = "0 " + minutes + " " + hour + " " + day + " " + "1/" + frequency + " " + "?" + " " + "*";;
        }
        return cron;
    }


    public Date getNextValidTimeAfter(String cron) {
        try {
            CronExpression exp = new CronExpression(cron);
            return exp.getNextValidTimeAfter(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
