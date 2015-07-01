package org.kuali.ole.deliver.calendar.service;

import freemarker.template.SimpleDate;
import org.kuali.rice.core.api.util.RiceConstants;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 7/25/13
 * Time: 5:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateUtil {

    private SimpleDateFormat simpleDateFormat;

    public static Timestamp addDays(Timestamp date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return new Timestamp(cal.getTime().getTime());


    }

    public static Timestamp addHours(Timestamp date, int numOfHours) {          //changed

        Long milliSecInAnHour = new Long(60 * 60 * 1000);
        Timestamp newTS = new Timestamp(date.getTime());
        long milliSecToAdd = milliSecInAnHour * numOfHours;
        long newTimeMilliSec = newTS.getTime();
        newTS.setTime(newTimeMilliSec + milliSecToAdd);
        return newTS;


    }//end addHours method..

    // Replace with KK:mma if you want 0-11 interval
    private static final DateFormat TWELVE_TF = new SimpleDateFormat("hh:mma");
    // Replace with kk:mm if you want 1-24 interval
    private static final DateFormat TWENTY_FOUR_TF = new SimpleDateFormat("HH:mm");

    public static String convertTo24HoursFormat(String twelveHourTime)
            throws ParseException {
        return TWENTY_FOUR_TF.format(
                TWELVE_TF.parse(twelveHourTime));
    }

    public static String convertTo12HoursFormat(String twentyFourHourTime)
            throws ParseException {
        return TWELVE_TF.format(
                TWENTY_FOUR_TF.parse(twentyFourHourTime));
    }


    public Date getDate(String dateString) {
        SimpleDateFormat dateFormat = getDateFormat();
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  null;
    }

    private SimpleDateFormat getDateFormat() {
        if(simpleDateFormat == null){
            simpleDateFormat = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE);
        }
        return simpleDateFormat;
    }

    public void setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
        this.simpleDateFormat = simpleDateFormat;
    }
}
