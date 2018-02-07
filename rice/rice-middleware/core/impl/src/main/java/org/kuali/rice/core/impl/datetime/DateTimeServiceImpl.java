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
package org.kuali.rice.core.impl.datetime;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.springframework.beans.factory.InitializingBean;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * This class is the service implementation for a DateTime structure. This is
 * the default, Kuali delivered implementation.
 */
//@Transactional
public class DateTimeServiceImpl implements DateTimeService, InitializingBean {
    /**
     * Default date/time formats
     */
    private static final String STRING_TO_DATE_FORMATS = "MM/dd/yyyy hh:mm a;MM/dd/yy;MM/dd/yyyy;MM-dd-yy;MM-dd-yyyy;MMddyy;MMMM dd;yyyy;MM/dd/yy HH:mm:ss;MM/dd/yyyy HH:mm:ss;MM-dd-yy HH:mm:ss;MMddyy HH:mm:ss;MMMM dd HH:mm:ss;yyyy HH:mm:ss";
    private static final String STRING_TO_TIME_FORMATS = "hh:mm aa";
    private static final String STRING_TO_TIMESTAMP_FORMATS = "MM/dd/yyyy hh:mm a;MM/dd/yy;MM/dd/yyyy;MM-dd-yy;MMddyy;MMMM dd;yyyy;MM/dd/yy HH:mm:ss;MM/dd/yyyy HH:mm:ss;MM-dd-yy HH:mm:ss;MMddyy HH:mm:ss;MMMM dd HH:mm:ss;yyyy HH:mm:ss";
    private static final String DATE_TO_STRING_FORMAT_FOR_USER_INTERFACE = "MM/dd/yyyy";
    private static final String TIME_TO_STRING_FORMAT_FOR_USER_INTERFACE = "hh:mm aa";
    private static final String TIMESTAMP_TO_STRING_FORMAT_FOR_USER_INTERFACE = "MM/dd/yyyy hh:mm a";
    private static final String DATE_TO_STRING_FORMAT_FOR_FILE_NAME = "yyyyMMdd";
    private static final String TIMESTAMP_TO_STRING_FORMAT_FOR_FILE_NAME = "yyyyMMdd-HH-mm-ss-S";

	protected String[] stringToDateFormats;
    protected String[] stringToTimeFormats;
	protected String[] stringToTimestampFormats;
	protected String dateToStringFormatForUserInterface;
    protected String timeToStringFormatForUserInterface;
	protected String timestampToStringFormatForUserInterface;
	protected String dateToStringFormatForFileName;
	protected String timestampToStringFormatForFileName;

		
	/**
	 * @see org.kuali.rice.core.api.datetime.DateTimeService#toDateString(java.util.Date)
	 */
	public String toDateString(Date date) {
		return toString(date, dateToStringFormatForUserInterface);
	}

    /**
     * @see org.kuali.rice.core.api.datetime.DateTimeService#toTimeString(java.sql.Time)
     */
    public String toTimeString(Time time) {
        return toString(time, timeToStringFormatForUserInterface);
    }

	/**
	 * @see org.kuali.rice.core.api.datetime.DateTimeService#toDateTimeString(java.util.Date)
	 */
	public String toDateTimeString(Date date) {
		return toString(date, timestampToStringFormatForUserInterface);
	}

	/**
	 * @see org.kuali.rice.core.api.datetime.DateTimeService#toString(java.util.Date,
	 *      java.lang.String)
	 */
	public String toString(Date date, String pattern) {
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		dateFormat.setLenient(false);
		return dateFormat.format(date);
	}

	/**
	 * @see org.kuali.rice.core.api.datetime.DateTimeService#getCurrentDate()
	 */
	public Date getCurrentDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.getTime();
	}

	/**
	 * @see org.kuali.rice.core.api.datetime.DateTimeService#getCurrentTimestamp()
	 */
	public Timestamp getCurrentTimestamp() {
		return new java.sql.Timestamp(getCurrentDate().getTime());
	}

	/**
	 * @see org.kuali.rice.core.api.datetime.DateTimeService#getCurrentSqlDate()
	 */
	public java.sql.Date getCurrentSqlDate() {
		return new java.sql.Date(getCurrentDate().getTime());
	}

	/**
	 * @see org.kuali.rice.core.api.datetime.DateTimeService#getCurrentSqlDateMidnight()
	 */
	public java.sql.Date getCurrentSqlDateMidnight() {
		// simple and not unduely inefficient way to truncate the time component
		return java.sql.Date.valueOf(getCurrentSqlDate().toString());
	}

	/**
	 * @see org.kuali.rice.core.api.datetime.DateTimeService#getCurrentCalendar()
	 */
	public Calendar getCurrentCalendar() {
		return getCalendar(getCurrentDate());
	}

	/**
	 * @see org.kuali.rice.core.api.datetime.DateTimeService#getCalendar
	 */
	public Calendar getCalendar(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("invalid (null) date");
		}

		Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.setTime(date);

		return currentCalendar;
	}

	/**
	 * Formats strings into dates using the format string in the KR-NS/All/STRING_TO_DATE_FORMATS parameter
	 *
	 * @see org.kuali.rice.core.api.datetime.DateTimeService#convertToDate(java.lang.String)
	 */
	public Date convertToDate(String dateString) throws ParseException {
		return parseAgainstFormatArray(dateString, stringToDateFormats);
	}

	/**
	 * @see org.kuali.rice.core.api.datetime.DateTimeService#convertToDateTime(java.lang.String)
	 */
	public Date convertToDateTime(String dateTimeString) throws ParseException {
		if (StringUtils.isBlank(dateTimeString)) {
			throw new IllegalArgumentException("invalid (blank) date/time string");
		}
		return parseAgainstFormatArray(dateTimeString, stringToTimestampFormats);
	}

	/**
	 * @see org.kuali.rice.core.api.datetime.DateTimeService#convertToSqlTimestamp(java.lang.String)
	 */
	public java.sql.Timestamp convertToSqlTimestamp(String timeString)
			throws ParseException {
		if (!StringUtils.isBlank(timeString)) {
			return new java.sql.Timestamp(convertToDateTime(timeString).getTime());
		}
        return null;
	}

	/**
	 * @see org.kuali.rice.core.api.datetime.DateTimeService#convertToSqlDate(java.lang.String)
	 */
	public java.sql.Date convertToSqlDate(String dateString)
			throws ParseException {
		if (StringUtils.isBlank(dateString)) {
			throw new IllegalArgumentException("invalid (blank) dateString");
		}
		Date date = parseAgainstFormatArray(dateString, stringToDateFormats);
		return new java.sql.Date(date.getTime());
	}

    /**
     * @see org.kuali.rice.core.api.datetime.DateTimeService#convertToSqlTime(java.lang.String)
     */
    public java.sql.Time convertToSqlTime(String timeString)
            throws ParseException {
        if (StringUtils.isBlank(timeString)) {
            throw new IllegalArgumentException("invalid (blank) dateString");
        }
        Date date = parseAgainstFormatArray(timeString, stringToTimeFormats);
        return new java.sql.Time(date.getTime());
    }

	protected Date parseAgainstFormatArray(String dateString, String[] formats) throws ParseException {
		dateString = dateString.trim();
		StringBuffer exceptionMessage = new StringBuffer("Date or date/time string '")
				.append(dateString)
				.append("' could not be converted using any of the accepted formats: ");
		for (String dateFormatString : formats) {
			try {
				return parse(dateString, dateFormatString);
			} catch (ParseException e) {
				exceptionMessage.append(dateFormatString).append(
						" (error offset=").append(e.getErrorOffset()).append(
						"),");
			}
		}
		throw new ParseException(exceptionMessage.toString().substring(0,
				exceptionMessage.length() - 1), 0);
	}

	/**
	 * @throws ParseException
	 * @see org.kuali.rice.core.api.datetime.DateTimeService#convertToSqlDate(java.sql.Timestamp)
	 */
	public java.sql.Date convertToSqlDate(Timestamp timestamp)
			throws ParseException {
		return new java.sql.Date(timestamp.getTime());
	}

	public int dateDiff(Date startDate, Date endDate, boolean inclusive) {
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(startDate);

		Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.setTime(endDate);

		int startDateOffset = -(startDateCalendar.get(Calendar.ZONE_OFFSET) + startDateCalendar
				.get(Calendar.DST_OFFSET))
				/ (60 * 1000);

		int endDateOffset = -(endDateCalendar.get(Calendar.ZONE_OFFSET) + endDateCalendar
				.get(Calendar.DST_OFFSET))
				/ (60 * 1000);

		if (startDateOffset > endDateOffset) {
			startDateCalendar.add(Calendar.MINUTE, endDateOffset
					- startDateOffset);
		}

		if (inclusive) {
			startDateCalendar.add(Calendar.DATE, -1);
		}

		int dateDiff = Integer.parseInt(DurationFormatUtils.formatDuration(
				endDateCalendar.getTimeInMillis()
						- startDateCalendar.getTimeInMillis(), "d", true));

		return dateDiff;
	}

	protected Date parse(String dateString, String pattern) throws ParseException {
		if (!StringUtils.isBlank(dateString)) {
			DateFormat dateFormat = new SimpleDateFormat(pattern);
			dateFormat.setLenient(false);
			ParsePosition parsePosition = new ParsePosition(0);
			Date testDate = dateFormat.parse(dateString, parsePosition);
			
			// Ensure that the entire date String can be parsed by the current format.
			if (testDate == null) {
				throw new ParseException("The date that you provided is invalid.",parsePosition.getErrorIndex());
			} else if (parsePosition.getIndex() != dateString.length()) {
				throw new ParseException("The date that you provided is invalid.",parsePosition.getIndex());
			}

			// Ensure that the date's year lies between 1000 and 9999, to help prevent database-related date errors.
			Calendar testCalendar = Calendar.getInstance();
			testCalendar.setLenient(false);
			testCalendar.setTime(testDate);
			if (testCalendar.get(Calendar.YEAR) < 1000 || testCalendar.get(Calendar.YEAR) > 9999) {
				throw new ParseException("The date that you provided is not between the years 1000 and 9999.",-1);
			}
			
			if(testCalendar.get(Calendar.YEAR) == 1970 && !pattern.contains("y".toLowerCase())){		    	
		    	Calendar curCalendar = Calendar.getInstance();
		    	curCalendar.setTime(new java.util.Date());
		    	testCalendar.set(Calendar.YEAR, curCalendar.get(Calendar.YEAR));
				testDate = testCalendar.getTime();
			}
			
			return testDate;
		}
		return null;
	}

	/**
	 * @see org.kuali.rice.core.api.datetime.DateTimeService#toDateStringForFilename(java.util.Date)
	 */
	public String toDateStringForFilename(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateToStringFormatForFileName);
		return dateFormat.format(date);
	}

	/**
	 * @see org.kuali.rice.core.api.datetime.DateTimeService#toDateTimeStringForFilename(java.util.Date)
	 */
	public String toDateTimeStringForFilename(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(timestampToStringFormatForFileName);
		return dateFormat.format(date);
	}
	

	/**
	 * This overridden method ...
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if (stringToDateFormats == null) {
            stringToDateFormats = loadAndValidateFormats(CoreConstants.STRING_TO_DATE_FORMATS, STRING_TO_DATE_FORMATS);
		}

        if (stringToTimeFormats == null) {
            stringToTimeFormats = loadAndValidateFormats(CoreConstants.STRING_TO_TIME_FORMATS, STRING_TO_TIME_FORMATS);
        }

		if (stringToTimestampFormats == null) {
            stringToTimestampFormats = loadAndValidateFormats(CoreConstants.STRING_TO_TIMESTAMP_FORMATS, STRING_TO_TIMESTAMP_FORMATS);
		}

		if (dateToStringFormatForUserInterface == null) {
			dateToStringFormatForUserInterface = loadAndValidateFormat(CoreConstants.DATE_TO_STRING_FORMAT_FOR_USER_INTERFACE, DATE_TO_STRING_FORMAT_FOR_USER_INTERFACE);
		}

        if (timeToStringFormatForUserInterface == null) {
            timeToStringFormatForUserInterface = loadAndValidateFormat(CoreConstants.TIME_TO_STRING_FORMAT_FOR_USER_INTERFACE, TIME_TO_STRING_FORMAT_FOR_USER_INTERFACE);
        }

		if (timestampToStringFormatForUserInterface == null) {
			timestampToStringFormatForUserInterface = loadAndValidateFormat(CoreConstants.TIMESTAMP_TO_STRING_FORMAT_FOR_USER_INTERFACE, TIMESTAMP_TO_STRING_FORMAT_FOR_USER_INTERFACE);
		}

		if (dateToStringFormatForFileName == null) {
			dateToStringFormatForFileName = loadAndValidateFormat(CoreConstants.DATE_TO_STRING_FORMAT_FOR_FILE_NAME, DATE_TO_STRING_FORMAT_FOR_FILE_NAME);
		}

		if (timestampToStringFormatForFileName == null) {
			timestampToStringFormatForFileName = loadAndValidateFormat(CoreConstants.TIMESTAMP_TO_STRING_FORMAT_FOR_FILE_NAME, TIMESTAMP_TO_STRING_FORMAT_FOR_FILE_NAME);
		}
	}

    	/**
	 *
	 * The dateTime config vars are ';' seperated. This method should probably
	 * be on the config interface.
	 *
	 * @param configValue
	 * @return
	 */
	private List<String> parseConfigValues(String configValue) {
	    if (configValue == null || "".equals(configValue)) {
	        return Collections.emptyList();
	    }
	    return Arrays.asList(configValue.split(";"));
	}

    /**
     * Loads a format string list from config or default
     * @param property the config property
     * @param deflt the default value
     * @return the config or default value
     */
    private List<String> loadFormats(String property, String deflt) {
        return parseConfigValues(loadFormat(property, deflt));
    }

    /**
     * Loads a format string list from the config or default and validates each entry
     * @param property the config property
     * @param deflt the default value
     * @return string array of valid date/time formats
     */
    private String[] loadAndValidateFormats(String property, String deflt) {
        List<String> dateFormatParams = loadFormats(property, deflt);

        String[] validFormats = new String[dateFormatParams.size()];

        for (int i = 0; i < dateFormatParams.size(); i++) {
            String dateFormatParam = dateFormatParams.get(i);
            if (StringUtils.isBlank(dateFormatParam)) {
                throw new IllegalArgumentException("Core/All/" + property + " parameter contains a blank semi-colon delimited substring");
            }
            else {
                // try to create a new SimpleDateFormat to try to detect illegal patterns
                new SimpleDateFormat(dateFormatParam);
                validFormats[i] = dateFormatParam;
            }
        }

        return validFormats;
    }

    /**
     * Loads a particular date format from the config, using a default for fallback
     * @param property the config property
     * @param deflt the default value
     * @return the config value or default value
     */
    private String loadFormat(String property, String deflt) {
        String format = ConfigContext.getCurrentContextConfig().getProperty(property);
        if (StringUtils.isBlank(format)) {
            format = deflt;
        }
        return format;
    }

    /**
     * Loads a particular date format from the config, using a default for fallback,
     * and validates the format.
     * @param property the config property
     * @param deflt the default value
     * @return the validated config value or default value
     */
    private String loadAndValidateFormat(String property, String deflt) {
        String format = loadFormat(property, deflt);
        // construct new SDF to make sure it's properly formatted
        new SimpleDateFormat(format);
        return format;
    }
}
