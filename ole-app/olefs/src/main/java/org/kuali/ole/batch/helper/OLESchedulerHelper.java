package org.kuali.ole.batch.helper;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessScheduleBo;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 7/20/13
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESchedulerHelper {

    private static volatile OLESchedulerHelper helper = new OLESchedulerHelper();

    private OLESchedulerHelper(){

    }

    public static synchronized OLESchedulerHelper getInstance(){
        return helper;
    }

    public String getCronExpression(OLEBatchProcessScheduleBo oleBatchProcessScheduleBo){
        String cronExpression=null;
        String oneTimeOrRecur = oleBatchProcessScheduleBo.getOneTimeOrRecurring();
        if(oneTimeOrRecur.equalsIgnoreCase(OLEConstants.OLEBatchProcess.SCHEDULE_ONETIME)){
            Date oneDate = oleBatchProcessScheduleBo.getOneTimeDate();
            String oneTime = oleBatchProcessScheduleBo.getOneTimeStartTime();
            cronExpression = getCronExpressionForOneTime(oneDate, oneTime);
        }

        if(oneTimeOrRecur.equalsIgnoreCase(OLEConstants.OLEBatchProcess.SCHEDULE_RECURRING)){
            String schduleType = oleBatchProcessScheduleBo.getScheduleType();
            if(schduleType.equalsIgnoreCase(OLEConstants.OLEBatchProcess.SCHEDULE_TYPE_DAILY)){
                String startTimeDaily = oleBatchProcessScheduleBo.getStartTime();
                cronExpression = getCronExpressionForDaily(startTimeDaily);
            }
            if(schduleType.equalsIgnoreCase(OLEConstants.OLEBatchProcess.SCHEDULE_TYPE_WEEKLY)){
                String startTimeWeekly = oleBatchProcessScheduleBo.getStartTime();
                String weekDays = oleBatchProcessScheduleBo.getWeekDays();
                cronExpression = getCronExpressionForWeekly(startTimeWeekly, weekDays);
            }
            if(schduleType.equalsIgnoreCase(OLEConstants.OLEBatchProcess.SCHEDULE_TYPE_MONTHLY)){
                String startTimeMonthly = oleBatchProcessScheduleBo.getStartTime();
                String dayNumberMonthly = oleBatchProcessScheduleBo.getDayNumber();
                String monthNumberMonthly = oleBatchProcessScheduleBo.getMonthNumber();
                cronExpression = getCronExpressionForMonthly(startTimeMonthly, dayNumberMonthly, monthNumberMonthly);
            }
        }

        boolean validCronExpression = org.quartz.CronExpression.isValidExpression(cronExpression);
            if(validCronExpression){
               return cronExpression;
            }else{
                return null;
            }
    }

    public String getCronExpressionForOneTime(Date oneTimeDate, String oneTime) {
        String[] oneTimeArray = oneTime.split(":");
        String hour = oneTimeArray[0];
        String minutes = oneTimeArray[1];
        String startDate = oneTimeDate.toString();
        String[] dateArray = startDate.split("\\-");
        String year = dateArray[0];
        String month = dateArray[1];
        if (month.charAt(0) == '0') {
            month = month.substring(1);
        }
        String day = dateArray[2];
        if (day.charAt(0) == '0') {
            day = day.substring(1);
        }
        String cronExp = "0 " + minutes + " " + hour + " " + day + " " + month
                + " ? " + year;
        return cronExp;
    }

    public String getCronExpressionForDaily(String startTime) {
        String[] startTimeArray = startTime.split(":");
        String hour = startTimeArray[0];
        String minutes = startTimeArray[1];
        String cronExp = "0 " + minutes + " " + hour + " " + "1/1 " + "* " + "? " + "*";
        return cronExp;
    }

    public String getCronExpressionForWeekly(String startTime, String weekdays) {
        String[] startTimeArray = startTime.split(":");
        String hour = startTimeArray[0];
        String minutes = startTimeArray[1];
        String cronExp = "0 " + minutes + " " + hour + " " + "? " + "* " + weekdays + " *";
        return cronExp;
    }

    public String getCronExpressionForMonthly(String startTime, String dayNumber, String monthNumber) {
        String[] startTimeArray = startTime.split(":");
        String hour = startTimeArray[0];
        String minutes = startTimeArray[1];
        String cronExp = "0 " + minutes + " " + hour + " " + dayNumber + " " + "1/" + monthNumber + " " + "?" + " " + "*";
        return cronExp;
    }

    /*public String getCronExpressionForDaily24Hr(String startTime) {
        String[] time = startTime.split(":");
        String minutes = time[1];
        String hour = time[0];
        String cronExp = "0 " + minutes + " " + hour + " " + "* * ?";
        boolean validCronExpression = org.quartz.CronExpression.isValidExpression(cronExp);
        if(validCronExpression){
            return cronExp;
        }else{
            return null;
        }
    }*/


    /*private String[] splitTime(String time){
        String[] minsAndHour = new String[2];
        String[] time1 = time.split(":");
        String[] time2 = new String[2];
        Matcher spaceMatch = Pattern.compile("\\s").matcher(time1[1]);
        if(spaceMatch.matches()){
        time2 = time1[1].split("\\ ");
        }else{
            time2[0] = time1[1].substring(0,2);
            time2[1] = time1[1].substring(2);
        }
        String hour;
        String minutes;
        if (time2[1].equalsIgnoreCase(OLEConstants.OLEBatchProcess.TIME_PM)) {
            Integer hourInt = Integer.parseInt(time1[0]) + 12;
            if (hourInt == 24) {
                hourInt = 0;
            }
            hour = hourInt.toString();
        } else {
            hour = time1[0];
        }
        minutes = time2[0];
        minsAndHour[0] = hour;
        minsAndHour[1] = minutes;
        return minsAndHour;
    }*/

}
