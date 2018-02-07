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
package org.kuali.rice.core.framework.persistence.jdbc.sql;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.search.SearchOperator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for working with SQL.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class SQLUtils {
	
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SQLUtils.class);

	private static final String DATE_REGEX_SMALL_TWO_DIGIT_YEAR = "^\\d{1,2}/\\d{1,2}/\\d{2}$|^\\d{1,2}-\\d{1,2}-\\d{2}$"; // matches M/d/yy or MM/dd/yy or M-d-yy or MM-dd-yy
	private static final String DATE_REGEX_SMALL_TWO_DIGIT_YEAR_SPLIT = "(\\d{1,2})[/,-](\\d{1,2})[/,-](\\d{2})";
	private static final String DATE_REGEX_SMALL_FOUR_DIGIT_YEAR = "^\\d{1,2}/\\d{1,2}/\\d{4}$|^\\d{1,2}-\\d{1,2}-\\d{4}$"; // matches M/d/yyyy or MM/dd/yyyy or M-d-yyyy or MM-dd-yyyy
	private static final String DATE_REGEX_SMALL_FOUR_DIGIT_YEAR_SPLIT = "(\\d{1,2})[/,-](\\d{1,2})[/,-](\\d{4})";
	
	private static final String DATE_REGEX_SMALL_FOUR_DIGIT_YEAR_FIRST = "^\\d{4}/\\d{1,2}/\\d{1,2}$|^\\d{4}-\\d{1,2}-\\d{1,2}$"; // matches yyyy/M/d or yyyy/MM/dd or yyyy-M-d or yyyy-MM-dd
	private static final String DATE_REGEX_SMALL_FOUR_DIGIT_YEAR_FIRST_SPLIT = "(\\d{4})[/,-](\\d{1,2})[/,-](\\d{1,2})";
	
	private static final String DATE_REGEX_WHOLENUM_SMALL = "^\\d{6}$"; // matches MMddyy
	private static final String DATE_REGEX_WHOLENUM_SMALL_SPLIT = "(\\d{2})(\\d{2})(\\d{2})";
	private static final String DATE_REGEX_WHOLENUM_LARGE = "^\\d{8}$"; // matches MMddyyyy
	private static final String DATE_REGEX_WHOLENUM_LARGE_SPLIT = "(\\d{2})(\\d{2})(\\d{4})";

	private static final String TIME_REGEX = "([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])";
	
	
	private static final Collection<String> DOCUMENT_SEARCH_DATE_VALIDATION_REGEX_EXPRESSIONS = Collections.unmodifiableCollection(Arrays.asList(DATE_REGEX_SMALL_FOUR_DIGIT_YEAR, DATE_REGEX_SMALL_FOUR_DIGIT_YEAR_FIRST));
	
	
	private static final Map<String, String> REGEX_EXPRESSION_MAP_TO_REGEX_SPLIT_EXPRESSION;
	static {
		final Map<String, String> map = new HashMap<String, String>();
		
		map.put(DATE_REGEX_SMALL_TWO_DIGIT_YEAR, DATE_REGEX_SMALL_TWO_DIGIT_YEAR_SPLIT);
		map.put(DATE_REGEX_SMALL_FOUR_DIGIT_YEAR, DATE_REGEX_SMALL_FOUR_DIGIT_YEAR_SPLIT);
		map.put(DATE_REGEX_SMALL_FOUR_DIGIT_YEAR_FIRST, DATE_REGEX_SMALL_FOUR_DIGIT_YEAR_FIRST_SPLIT);
		map.put(DATE_REGEX_WHOLENUM_SMALL, DATE_REGEX_WHOLENUM_SMALL_SPLIT);
		map.put(DATE_REGEX_WHOLENUM_LARGE,DATE_REGEX_WHOLENUM_LARGE_SPLIT);
		REGEX_EXPRESSION_MAP_TO_REGEX_SPLIT_EXPRESSION = Collections.unmodifiableMap(map);
	}
	
	private SQLUtils() {
		throw new UnsupportedOperationException("do not call");
	}
	
    /**
     * A method to format any variety of date strings into a common format
     *
     * @param date
     *            A string date in one of a few different formats
     * @return A string representing a date in the format yyyy/MM/dd or null if date is invalid
     */
    public static String getSqlFormattedDate(String date) {
        DateComponent dc = formatDateToDateComponent(date, REGEX_EXPRESSION_MAP_TO_REGEX_SPLIT_EXPRESSION.keySet());
        if (dc == null) {
            return null;
        }
        return dc.getYear() + "/" + dc.getMonth() + "/" + dc.getDate();
    }
    
    public static Timestamp convertStringDateToTimestamp(String dateWithoutTime) {
        Pattern p = Pattern.compile(TIME_REGEX);
        Matcher util = p.matcher(dateWithoutTime);
        if (util.find()) {
            dateWithoutTime = StringUtils.substringBeforeLast(dateWithoutTime, " ");
        }
        DateComponent formattedDate = formatDateToDateComponent(dateWithoutTime, REGEX_EXPRESSION_MAP_TO_REGEX_SPLIT_EXPRESSION.keySet());
        if (formattedDate == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.MONTH, Integer.valueOf(formattedDate.getMonth()).intValue() - 1);
        c.set(Calendar.DATE, Integer.valueOf(formattedDate.getDate()).intValue());
        c.set(Calendar.YEAR, Integer.valueOf(formattedDate.getYear()).intValue());
        return convertCalendar(c);
    }

    private static class DateComponent {
        protected String month;
        protected String date;
        protected String year;

        public DateComponent(String year, String month, String date) {
            this.month = month;
            this.date = date;
            this.year = year;
        }

        public String getDate() {
            return date;
        }

        public String getMonth() {
            return month;
        }

        public String getYear() {
            return year;
        }
    }
    
    /**
     * A method to format any variety of date strings into a common format
     *
     * @param date
     *            A string date in one of a few different formats
     * @return A string representing a date in the format MM/dd/yyyy or null if date is invalid
     */
    public static String getEntryFormattedDate(String date) {
        Pattern p = Pattern.compile(TIME_REGEX);
        Matcher util = p.matcher(date);
        if (util.find()) {
            date = StringUtils.substringBeforeLast(date, " ");
        }
        DateComponent dc = formatDateToDateComponent(date, DOCUMENT_SEARCH_DATE_VALIDATION_REGEX_EXPRESSIONS);
        if (dc == null) {
            return null;
        }
        return dc.getMonth() + "/" + dc.getDate() + "/" + dc.getYear();
    }

    private static DateComponent formatDateToDateComponent(String date, Collection<String> regularExpressionList) {
        String matchingRegexExpression = null;
        for (String string : regularExpressionList) {
            String matchRegex = string;
            if (!REGEX_EXPRESSION_MAP_TO_REGEX_SPLIT_EXPRESSION.containsKey(matchRegex)) {
                String errorMsg = "";
                LOG.error("formatDateToDateComponent(String,List) " + errorMsg);

            }
            Pattern p = Pattern.compile(matchRegex);
            if ((p.matcher(date)).matches()) {
                matchingRegexExpression = matchRegex;
                break;
            }
        }

        if (matchingRegexExpression == null) {
            String errorMsg = "formatDate(String,List) Date string given '" + date + "' is not valid according to Workflow defaults.  Returning null value.";
            if (StringUtils.isNotBlank(date)) {
                LOG.warn(errorMsg);
            } else {
                LOG.debug(errorMsg);
            }
            return null;
        }
        String regexSplitExpression = REGEX_EXPRESSION_MAP_TO_REGEX_SPLIT_EXPRESSION.get(matchingRegexExpression);

        // Check date formats and reformat to yyyy/MM/dd
        // well formed MM/dd/yyyy
        Pattern p = Pattern.compile(regexSplitExpression);
        Matcher util = p.matcher(date);
        util.matches();
        if (regexSplitExpression.equals(DATE_REGEX_SMALL_TWO_DIGIT_YEAR_SPLIT)) {
            StringBuffer yearBuf = new StringBuffer();
            StringBuffer monthBuf = new StringBuffer();
            StringBuffer dateBuf = new StringBuffer();
            Integer year = Integer.valueOf(util.group(3));

            if (year.intValue() <= 50) {
                yearBuf.append("20").append(util.group(3));
            } else if (util.group(3).length() < 3) {
                yearBuf.append("19").append(util.group(3));
            } else {
                yearBuf.append(util.group(3));
            }

            if (util.group(1).length() < 2) {
                monthBuf.append("0").append(util.group(1));
            } else {
                monthBuf.append(util.group(1));
            }

            if (util.group(2).length() < 2) {
                dateBuf.append("0").append(util.group(2));
            } else {
                dateBuf.append(util.group(2));
            }

            return new DateComponent(yearBuf.toString(), monthBuf.toString(), dateBuf.toString());

            // small date format M/d/yyyy | MM/dd/yyyy | M-d-yyyy | MM-dd-yyyy
        } else if (regexSplitExpression.equals(DATE_REGEX_SMALL_FOUR_DIGIT_YEAR_SPLIT)) {
            StringBuffer yearBuf = new StringBuffer(util.group(3));
            StringBuffer monthBuf = new StringBuffer();
            StringBuffer dateBuf = new StringBuffer();

            if (util.group(1).length() < 2) {
                monthBuf.append("0").append(util.group(1));
            } else {
                monthBuf.append(util.group(1));
            }

            if (util.group(2).length() < 2) {
                dateBuf.append("0").append(util.group(2));
            } else {
                dateBuf.append(util.group(2));
            }

            return new DateComponent(yearBuf.toString(), monthBuf.toString(), dateBuf.toString());

            // small date format yyyy/M/d | yyyy/MM/dd | yyyy-M-d | yyyy-MM-dd
        } else if (regexSplitExpression.equals(DATE_REGEX_SMALL_FOUR_DIGIT_YEAR_FIRST_SPLIT)) {
            StringBuffer yearBuf = new StringBuffer(util.group(1));
            StringBuffer monthBuf = new StringBuffer();
            StringBuffer dateBuf = new StringBuffer();

            if (util.group(2).length() < 2) {
                monthBuf.append("0").append(util.group(2));
            } else {
                monthBuf.append(util.group(2));
            }

            if (util.group(3).length() < 2) {
                dateBuf.append("0").append(util.group(3));
            } else {
                dateBuf.append(util.group(3));
            }

            return new DateComponent(yearBuf.toString(), monthBuf.toString(), dateBuf.toString());

            // large number MMddyyyy
        } else if (regexSplitExpression.equals(DATE_REGEX_WHOLENUM_LARGE_SPLIT)) {
            return new DateComponent(util.group(3), util.group(1), util.group(2));

            // small number MMddyy
        } else if (regexSplitExpression.equals(DATE_REGEX_WHOLENUM_SMALL_SPLIT)) {
            StringBuffer yearBuf = new StringBuffer();
            Integer year = Integer.valueOf(util.group(3));

            if (year.intValue() < 50) {
                yearBuf.append("20");
            } else {
                yearBuf.append("19");
            }
            yearBuf.append(util.group(3));
            return new DateComponent(yearBuf.toString(), util.group(1), util.group(2));
        } else {
            LOG.warn("formatDate(String,List) Date string given '" + date + "' is not valid according to Workflow defaults.  Returning null value.");
            return null;
        }
    }
    
    public static Calendar convertTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        return calendar;
    }

    public static Timestamp convertCalendar(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        return new Timestamp(calendar.getTimeInMillis());
    }
   
	public static String cleanDate(String string) {		
        for (SearchOperator op : SearchOperator.RANGE_CHARACTERS) {
            string = StringUtils.replace(string, op.op(), "");
        }
        return string;
    }
   
	public static String cleanNumericOfValidOperators(String string){
		for (SearchOperator op : SearchOperator.RANGE_CHARACTERS) {
            string = StringUtils.replace(string, op.op(), "");
        }
		string = StringUtils.replace(string, SearchOperator.OR.op(), "");
		string = StringUtils.replace(string, SearchOperator.AND.op(), "");
		string = StringUtils.replace(string, SearchOperator.NOT.op(), "");

		return string;
	}
	
    /**
     * Removes all query characters from a string.
     *
     * @param string
     * @return Cleaned string
     */
    public static String cleanString(String string) {
        for (SearchOperator op : SearchOperator.QUERY_CHARACTERS) {
            string = StringUtils.replace(string, op.op(), "");
        }
        return string;
    }
    
    /**
     * Splits the values then cleans them of any other query characters like *?!><...
     *
     * @param valueEntered
     * @param propertyDataType
     * @return
     */
    public static List<String> getCleanedSearchableValues(String valueEntered, String propertyDataType) {
  	   List<String> lRet = null;
  	   List<String> lTemp = getSearchableValues(valueEntered);
  	   if(lTemp != null && !lTemp.isEmpty()){
  		   lRet = new ArrayList<String>();
  		   for(String val: lTemp){
  			   // Clean the wildcards appropriately, depending on the field's data type.
  			   if (CoreConstants.DATA_TYPE_STRING.equals(propertyDataType)) {
  				   lRet.add(clean(val));
  			   } else if (CoreConstants.DATA_TYPE_FLOAT.equals(propertyDataType) || CoreConstants.DATA_TYPE_LONG.equals(propertyDataType)) {
  				   lRet.add(SQLUtils.cleanNumericOfValidOperators(val));
  			   } else if (CoreConstants.DATA_TYPE_DATE.equals(propertyDataType)) {
  				   lRet.add(SQLUtils.cleanDate(val));
  			   } else {
  				   lRet.add(clean(val));
  			   }
  		   }
  	   }
  	   return lRet;
    }
    
    /**
    * Splits the valueEntered on locical operators and, or, and between
    *
    * @param valueEntered
    * @return
    */
 	private static List<String> getSearchableValues(String valueEntered) {
 		List<String> lRet = new ArrayList<String>();
 		getSearchableValueRecursive(valueEntered, lRet);
 		return lRet;
 	}

 	private static void getSearchableValueRecursive(String valueEntered, List lRet) {
 		if(valueEntered == null) {
 			return;
 		}

 		valueEntered = valueEntered.trim();

 		if(lRet == null){
 			throw new NullPointerException("The list passed in is by reference and should never be null.");
 		}

 		if (StringUtils.contains(valueEntered, SearchOperator.BETWEEN.op())) {
 			List<String> l = Arrays.asList(valueEntered.split("\\.\\."));
 			for(String value : l){
 				getSearchableValueRecursive(value,lRet);
 			}
 			return;
 		}
 		if (StringUtils.contains(valueEntered, SearchOperator.OR.op())) {
 			List<String> l = Arrays.asList(StringUtils.split(valueEntered, SearchOperator.OR.op()));
 			for(String value : l){
 				getSearchableValueRecursive(value,lRet);
 			}
 			return;
 		}
 		if (StringUtils.contains(valueEntered, SearchOperator.AND.op())) {
 			//splitValueList.addAll(Arrays.asList(StringUtils.split(valueEntered, KRADConstants.AND.op())));
 			List<String> l = Arrays.asList(StringUtils.split(valueEntered, SearchOperator.AND.op()));
 			for(String value : l){
 				getSearchableValueRecursive(value,lRet);
 			}
 			return;
 		}

 		// lRet is pass by ref and should NEVER be null
 		lRet.add(valueEntered);
   }
 	
    /**
     * Removes all query characters from a string.
     *
     * @param string
     * @return Cleaned string
     */
    private static String clean(String string) {
        for (SearchOperator op : SearchOperator.QUERY_CHARACTERS) {
            string = StringUtils.replace(string, op.op(), CoreConstants.EMPTY_STRING);
        }
        return string;
    }
}
