package org.kuali.ole.deliver.calendar.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 7/25/13
 * Time: 5:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateUtil {
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


}
