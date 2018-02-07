/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.core.api.datetime;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * This interface defines methods that a DateTime service must provide
 */
 public interface DateTimeService {
    /**
     * Translates the specified date into a string without a time component, formatted according to "stringDateFormat" that the
     * service is configured with
     *
     * @param date
     * @return formatted string version of the specified date
     */
     String toDateString(Date date);

    /**
     * Translates the specified time into a string without a date component, formatted according to "stringTimeFormat" that the
     * service is configured with
     *
     * @param time
     * @return formatted string version of the specified time
     */
    String toTimeString(Time time);

    /**
     * Translates the specified date into a string with a time component, formatted according to the "stringDateTimeFormat" that the
     * service is configured with
     *
     * @param date
     * @return formatted string version of the specified date
     */
     String toDateTimeString(Date date);

    /**
     * Translates the specified date into a string without a time component, formatted according to the specified pattern
     *
     * @param date
     * @return formatted string version of the specified date
     */
     String toString(Date date, String pattern);

    /**
     * Returns the current date/time as a java.util.Date
     *
     * @return current date/time
     */
     Date getCurrentDate();

    /**
     * Returns the current date/time as a java.sql.Timestamp
     *
     * @return current date/time
     */
     Timestamp getCurrentTimestamp();

    /**
     * Returns the current date/time as a java.sql.Date
     *
     * @return current date/time
     */
     java.sql.Date getCurrentSqlDate();

    /**
     * Returns the current date as a java.sql.Date rounded back to midnight. This is what the JDBC driver is supposed to do with
     * dates on their way to the database, so it can be convenient for comparing to dates from the database or input from the UI.
     *
     * @return current date at the most recent midnight in the JVM's timezone
     */
     java.sql.Date getCurrentSqlDateMidnight();

    /**
     * Returns a Calendar initialized with the current Date
     *
     * @return currennt Calendar
     */
     Calendar getCurrentCalendar();

    /**
     * Returns a Calendar initialized to the given Date
     *
     * @return date-specific Calendar
     * @throws IllegalArgumentException if the given Date is null
     */
     Calendar getCalendar(Date date);

    /**
     * Translates the specified string into a date without a time component, see implementation class for formatting details
     *
     * @param dateString
     * @return the date representation of the specified dateString
     * @throws ParseException
     */
     Date convertToDate(String dateString) throws ParseException;

    /**
     * Translates the specified string into a date with a time component, formatted according to "stringDateTimeFormat" that the
     * service is configured with
     *
     * @param dateTimeString
     * @return the date representation of the specified dateTimeString
     * @throws ParseException
     */
     Date convertToDateTime(String dateTimeString) throws ParseException;

    /**
     * Converts the given String into a java.sql.Timestamp instance according to the "stringDateTimeFormat" that the service is
     * configured with
     *
     * @param timeString
     * @return java.sql.Timestamp
     * @throws IllegalArgumentException if the given string is null or blank
     * @throws ParseException if the string cannot be converted
     */
     java.sql.Timestamp convertToSqlTimestamp(String timeString) throws ParseException;

    /**
     * Converts the given String into a java.sql.Date instance
     *
     * @param dateString
     * @return java.sql.Date
     * @throws IllegalArgumentException if the given string is null or blank
     * @throws ParseException if the string cannot be converted
     */
     java.sql.Date convertToSqlDate(String dateString) throws ParseException;

    /**
     * Converts the given String into a java.sql.Time instance
     *
     * @param timeString
     * @return java.sql.Time
     * @throws IllegalArgumentException if the given string is null or blank
     * @throws ParseException if the string cannot be converted
     */
    java.sql.Time convertToSqlTime(String timeString) throws ParseException;

    /**
     * Converts a Timestamp into a sql Date.
     *
     * @param timestamp
     * @return
     */
     java.sql.Date convertToSqlDate(Timestamp timestamp) throws ParseException;

    /**
     * Returns the number of days between two days - start and end date of some arbitrary period.
     *
     * @param date1 The first date in the period
     * @param date2 The second date in the period
     * @param inclusive Whether the result should include both the start and the end date. Otherwise it only includes one.
     * @return int The number of days in the period
     */
     int dateDiff(Date date1, Date date2, boolean inclusive);

    /**
     * Returns a String representing a date that is suitable for file names, and is preferably chronologically sortable
     *
     * @param date
     * @return
     */
     String toDateStringForFilename(Date date);

    /**
     * Returns a String representing a date/time that is suitable for file names, and is preferably chronologically sortable
     *
     * @param date
     * @return
     */
     String toDateTimeStringForFilename(Date date);
    

}
