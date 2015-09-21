package org.kuali.ole.common;


import org.kuali.ole.constants.OLESIP2Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class offers methods that are needed for creating SIP2 messages.
 *
 * @author Gayathri A
 */
public class MessageUtil {

    /**
     * Computes the checksum of the given string.
     *
     * @param str transmission string
     * @return checksum of the given string
     */
    public static String computeChecksum(String str) {
        char[] message = str.toCharArray();
        int checksum = 0;
        int i = 0;
        // Count the binary sum of the characters
        while (i < str.length()) {
            checksum += (int) message[i];
            i++;
        }
        // Take the lower 16 bits of the total and
        // perform 2's complement
        checksum = -(checksum & 0xFFFF);
        // Return the result represented by four hex digits
        return Integer.toHexString(checksum).substring(4, 8).toUpperCase();
    }

    /**
     * Returns the current date and time in the format used in the
     * SIP2 messages. The SIP2 format is "yyyyMMdd    HHmmss".
     *
     * @return current date and time in SIP2 format
     */
    public static String getSipDateTime() {
        SimpleDateFormat simpleDf = new SimpleDateFormat(OLESIP2Constants.SIP2_DATE_FORMAT);
        return simpleDf.format(new Date());
    }

    /**
     * Parses the date and time from the given SIP2 formatted string. The SIP2
     * format is "yyyyMMdd    HHmmss".
     *
     * @param dateStr date and time in SIP2 format
     * @return Date object
     */
    public static Date parseSipDateTime(String dateStr) {
        SimpleDateFormat simpleDf = new SimpleDateFormat(OLESIP2Constants.SIP2_DATE_FORMAT);
        try {
            return simpleDf.parse(dateStr);
        } catch (ParseException pe) {
            return null;
        }
    }

    /**
     * Converts the given date to a SIP2 formatted date/time string. The SIP2
     * format is "yyyyMMdd    HHmmss".
     *
     * @param date date object to be converted
     * @return SIP2 formatted date/time string
     */
    public static String toSipDateTime(Date date) {
        SimpleDateFormat simpleDf = new SimpleDateFormat(OLESIP2Constants.SIP2_DATE_FORMAT);
        return simpleDf.format(date);
    }

    /**
     * Creates a new date/time string presented in the SIP2 format
     * "yyyyMMdd    HHmmss" by adding the given number of days and months
     * to the current date. This method is usefull when setting an
     * expiration date to a hold.
     *
     * @param days   number of days to be added to the current date
     * @param months number of months to be added to the current date
     * @return current date plus the given number of days and months in the
     * SIP2 format "yyyyMMdd    HHmmss"
     */
    public static String createFutureDate(int days, int months) {
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        date.add(Calendar.DATE, days);
        date.add(Calendar.MONTH, months);
        return toSipDateTime(date.getTime());
    }
}
