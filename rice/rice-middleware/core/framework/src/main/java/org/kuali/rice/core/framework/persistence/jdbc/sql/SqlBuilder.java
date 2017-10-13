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
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.search.SearchOperator;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.type.TypeUtils;
import org.kuali.rice.core.framework.persistence.platform.DatabasePlatform;
import org.kuali.rice.core.web.format.BooleanFormatter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * This is a description of what this class does - Garey don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SqlBuilder {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SqlBuilder.class);

	private DateTimeService dateTimeService;
	private DatabasePlatform dbPlatform;
	
	public static final  String EMPTY_STRING = "";

    public static final class JoinType {

	}

	/**
	 * @param clazz
	 * @return true if the given Class is an join type
	 * @throws IllegalArgumentException
	 *             if the given Class is null
	 */
	public static boolean isJoinClass(Class clazz) {
		return clazz.isAssignableFrom(JoinType.class);
	}

	public Criteria createCriteria(String columnName, String searchValue, String tableName, String tableAlias, Class propertyType) {
		return createCriteria(columnName, searchValue, tableName, tableAlias, propertyType, false, true);
	}

    public Criteria createCriteria(String columnName, String searchValue, String tableName, String tableAlias, Class propertyType, boolean caseInsensitive) {
        return createCriteria(columnName, searchValue, tableName, tableAlias, propertyType, caseInsensitive, true);
    }

	public Criteria createCriteria(String columnName, String searchValue, String tableName, String tableAlias, Class propertyType, boolean caseInsensitive, boolean allowWildcards) {

		if (propertyType == null) {
			return null;
		}

		Criteria criteria = new Criteria(tableName, tableAlias);
		criteria.setDbPlatform(this.getDbPlatform());

		// build criteria
		addCriteria(columnName, searchValue, propertyType, caseInsensitive, allowWildcards, criteria);
		return criteria;
	}

	public void andCriteria(String columnName, String searchValue, String tableName, String tableAlias, Class propertyType, boolean caseInsensitive, boolean allowWildcards, Criteria addToThisCriteria) {
		Criteria crit = createCriteria(columnName,searchValue, tableName, tableAlias, propertyType, caseInsensitive, allowWildcards);

		addToThisCriteria.and(crit);
	}
	public void andCriteria(Criteria addToThisCriteria, Criteria newCriteria) {
		addToThisCriteria.and(newCriteria);
	}
	public void orCriteria(String columnName, String searchValue, String tableName, String tableAlias, Class propertyType, boolean caseInsensitive, boolean allowWildcards, Criteria addToThisCriteria) {
		Criteria crit = createCriteria(columnName, searchValue,tableName, tableAlias, propertyType, caseInsensitive, allowWildcards);

		addToThisCriteria.or(crit);
	}

	public void addCriteria(String propertyName, String propertyValue, Class propertyType, boolean caseInsensitive, boolean allowWildcards, Criteria criteria) {

		if(SqlBuilder.isJoinClass(propertyType)){ // treat this as a join table.
			String temp = SQLUtils.cleanString(propertyValue);
			criteria.eq(propertyName, temp, propertyType);
			return;
		}

		if (StringUtils.contains(propertyValue, SearchOperator.OR.op())) {
			addOrCriteria(propertyName, propertyValue, propertyType, caseInsensitive, criteria, allowWildcards);
			return;
		}

		if ( StringUtils.contains(propertyValue, SearchOperator.AND.op())) {
			addAndCriteria(propertyName, propertyValue, propertyType, caseInsensitive, criteria, allowWildcards);
			return;
		}

        if (StringUtils.contains(propertyValue,	SearchOperator.NOT.op())) {
				addNotCriteria(propertyName, propertyValue, propertyType, caseInsensitive, criteria, allowWildcards);
            return;
        }

		if (TypeUtils.isStringClass(propertyType)) {
			if (propertyValue != null && (
            				StringUtils.contains(propertyValue, SearchOperator.BETWEEN.op())
            				|| propertyValue.startsWith(">")
            				|| propertyValue.startsWith("<") ) ) {
				addStringRangeCriteria(propertyName, propertyValue, criteria, propertyType, caseInsensitive, allowWildcards);
			} else {
				//if (!allowWildcards) {
				//	propertyValue = StringUtils.replace(propertyValue, "*", "\\*");
				//}
				// KULRICE-85 : made string searches case insensitive - used new
				// DBPlatform function to force strings to upper case
				if (caseInsensitive) {
					// TODO: What to do here now that the JPA version does not extend platform aware?
					propertyName = getDbPlatform().getUpperCaseFunction() + "(__JPA_ALIAS__." + propertyName + ")";
					//propertyName = "UPPER("+ tableAlias + "." + propertyName + ")";
					propertyValue = propertyValue.toUpperCase();
				}
				criteria.like(propertyName, propertyValue,propertyType, allowWildcards);
			}
		} else if (TypeUtils.isIntegralClass(propertyType) || TypeUtils.isDecimalClass(propertyType)) {
			addNumericRangeCriteria(propertyName, propertyValue, criteria, propertyType);
		} else if (TypeUtils.isTemporalClass(propertyType)) {
			addDateRangeCriteria(propertyName, propertyValue, criteria, propertyType);
		} else if (TypeUtils.isBooleanClass(propertyType)) {
			String temp = SQLUtils.cleanString(propertyValue);
			criteria.eq(propertyName, new BooleanFormatter().convertFromPresentationFormat(temp), propertyType);
		} else {
			LOG.error("not adding criterion for: " + propertyName + "," + propertyType + "," + propertyValue);
		}
	}

	private void addOrCriteria(String propertyName, String propertyValue, Class propertyType, boolean caseInsensitive, Criteria criteria, boolean allowWildcards) {
		addLogicalOperatorCriteria(propertyName, propertyValue, propertyType, caseInsensitive, criteria, SearchOperator.OR.op(), allowWildcards);
	}

	private void addAndCriteria(String propertyName, String propertyValue, Class propertyType, boolean caseInsensitive, Criteria criteria, boolean allowWildcards) {
		addLogicalOperatorCriteria(propertyName, propertyValue, propertyType, caseInsensitive, criteria, SearchOperator.AND.op(), allowWildcards);
	}

	private void addNotCriteria(String propertyName, String propertyValue, Class propertyType, boolean caseInsensitive, Criteria criteria, boolean allowWildcards) {
		String[] splitPropVal = StringUtils.split(propertyValue, SearchOperator.NOT.op());

		int strLength = splitPropVal.length;
		// if more than one NOT operator assume an implicit and (i.e. !a!b = !a&!b)
		if (strLength > 1) {
			String expandedNot = SearchOperator.NOT + StringUtils.join(splitPropVal, SearchOperator.AND.op() + SearchOperator.NOT.op());
			// we know that since this method is called, treatWildcardsAndOperatorsAsLiteral is false
			addCriteria(propertyName, expandedNot, propertyType, caseInsensitive, allowWildcards, criteria);
		} else {
			// only one so add a not like (all the rest) or not equal (decimal types)
            if (TypeUtils.isDecimalClass(propertyType)) {
                criteria.notEqual(propertyName, splitPropVal[0], propertyType, allowWildcards);
            }  else {
			    criteria.notLike(propertyName, splitPropVal[0], propertyType, allowWildcards);
            }
		}
	}

	private void addLogicalOperatorCriteria(String propertyName, String propertyValue, Class propertyType, boolean caseInsensitive, Criteria criteria, String splitValue, boolean allowWildcards) {
		String[] splitPropVal = StringUtils.split(propertyValue, splitValue);

		Criteria subCriteria = new Criteria("N/A");
		for (String element : splitPropVal) {
			Criteria predicate = new Criteria("N/A", criteria.getAlias());
			// we know that since this method is called, treatWildcardsAndOperatorsAsLiteral is false
			addCriteria(propertyName, element, propertyType, caseInsensitive, allowWildcards, predicate);
			if (splitValue == SearchOperator.OR.op()) {
				subCriteria.or(predicate);
			}
			if (splitValue == SearchOperator.AND.op()) {
				subCriteria.and(predicate);
			}
		}

		criteria.and(subCriteria);
	}

	private Timestamp parseDate(String dateString) {
		dateString = dateString.trim();
		try {
			Timestamp dt =  this.getDateTimeService().convertToSqlTimestamp(dateString);
			return dt;
		} catch (ParseException ex) {
			return null;
		}
	}
	public boolean isValidDate(String dateString){
		//FIXME: wtf - weird!
		try {
			this.createCriteria("date", dateString.trim(), "validation", "test", Date.class);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean containsRangeCharacters(String string){
		boolean bRet = false;
		for (SearchOperator op : SearchOperator.RANGE_CHARACTERS) {
            if(StringUtils.contains(string, op.op())){
            	bRet = true;
            }
        }
		return bRet;
	}

	private void addDateRangeCriteria(String propertyName, String propertyValue, Criteria criteria, Class propertyType) {

		if (StringUtils.contains(propertyValue, SearchOperator.BETWEEN.op())) {
			String[] rangeValues = propertyValue.split("\\.\\."); // this translate to the .. operator
			criteria.between(propertyName, parseDate(SQLUtils.cleanDate(rangeValues[0])), parseDate(cleanUpperBound(SQLUtils.cleanDate(rangeValues[1]))), propertyType);
		} else if (propertyValue.startsWith(SearchOperator.GREATER_THAN_EQUAL.op())) {
			criteria.gte(propertyName, parseDate(SQLUtils.cleanDate(propertyValue)), propertyType);
		} else if (propertyValue.startsWith(SearchOperator.LESS_THAN_EQUAL.op())) {
			criteria.lte(propertyName, parseDate(cleanUpperBound(SQLUtils.cleanDate(propertyValue))),propertyType);
		} else if (propertyValue.startsWith(SearchOperator.GREATER_THAN.op())) {
			// we clean the upper bound here because if you say >12/22/09, it translates greater than
			// the date... as in whole date. ie. the next day on.
			criteria.gt(propertyName, parseDate(cleanUpperBound(SQLUtils.cleanDate(propertyValue))), propertyType);
		} else if (propertyValue.startsWith(SearchOperator.LESS_THAN.op())) {
			criteria.lt(propertyName, parseDate(SQLUtils.cleanDate(propertyValue)), propertyType);
		} else {
			String sDate = convertSimpleDateToDateRange(SQLUtils.cleanDate(propertyValue));
			if(sDate.contains(SearchOperator.BETWEEN.op())){
				addDateRangeCriteria(propertyName, sDate, criteria, propertyType);
			}else{
				criteria.eq(propertyName, parseDate(sDate), propertyType);
			}
		}
	}

	public static boolean isValidNumber(String value){
		try{
		stringToBigDecimal(value);
			return true;
		}catch(Exception ex){
			return false;
		}
	}

	public static String cleanNumeric(String value){
		String cleanedValue = value.replaceAll("[^-0-9.]", "");
		// ensure only one "minus" at the beginning, if any
		if (cleanedValue.lastIndexOf('-') > 0) {
			if (cleanedValue.charAt(0) == '-') {
				cleanedValue = "-" + cleanedValue.replaceAll("-", "");
			} else {
				cleanedValue = cleanedValue.replaceAll("-", "");
			}
		}
		// ensure only one decimal in the string
		int decimalLoc = cleanedValue.lastIndexOf('.');
		if (cleanedValue.indexOf('.') != decimalLoc) {
			cleanedValue = cleanedValue.substring(0, decimalLoc).replaceAll("\\.", "") + cleanedValue.substring(decimalLoc);
		}
		return cleanedValue;
	}

	public static BigDecimal stringToBigDecimal(String value) {

		//try {
			return new BigDecimal(cleanNumeric(value));
		/*
		} catch (NumberFormatException ex) {
			GlobalVariables.getMessageMap().putError(KRADConstants.DOCUMENT_ERRORS, RiceKeyConstants.ERROR_CUSTOM, new String[] { "Invalid Numeric Input: " + value });
			return null;
		}*/
	}

	private void addNumericRangeCriteria(String propertyName, String propertyValue, Criteria criteria, Class propertyType) {

		if (StringUtils.contains(propertyValue, SearchOperator.BETWEEN.op())) {
			String[] rangeValues = propertyValue.split("\\.\\."); // this translate to the .. operator
			criteria.between(propertyName, stringToBigDecimal(rangeValues[0]), stringToBigDecimal(rangeValues[1]), propertyType);
		} else if (propertyValue.startsWith(SearchOperator.GREATER_THAN_EQUAL.op())) {
			criteria.gte(propertyName, stringToBigDecimal(propertyValue), propertyType);
		} else if (propertyValue.startsWith(SearchOperator.LESS_THAN_EQUAL.op())) {
			criteria.lte(propertyName, stringToBigDecimal(propertyValue), propertyType);
		} else if (propertyValue.startsWith(SearchOperator.GREATER_THAN.op())) {
			criteria.gt(propertyName, stringToBigDecimal(propertyValue), propertyType);
		} else if (propertyValue.startsWith(SearchOperator.LESS_THAN.op())) {
			criteria.lt(propertyName, stringToBigDecimal(propertyValue), propertyType);
		} else {
			criteria.eq(propertyName, stringToBigDecimal(propertyValue), propertyType);
		}
	}

	private void addStringRangeCriteria(String propertyName, String propertyValue, Criteria criteria, Class propertyType, boolean caseInsensitive, boolean allowWildcards) {

		if (StringUtils.contains(propertyValue, SearchOperator.BETWEEN.op())) {
			String[] rangeValues = propertyValue.split("\\.\\."); // this translate to the .. operator
			propertyName = this.getCaseAndLiteralPropertyName(propertyName, caseInsensitive);
			String val1 = this.getCaseAndLiteralPropertyValue(rangeValues[0], caseInsensitive, allowWildcards);
			String val2 = this.getCaseAndLiteralPropertyValue(rangeValues[1], caseInsensitive, allowWildcards);
			criteria.between(propertyName, val1, val2, propertyType);
		} else{
			propertyName = this.getCaseAndLiteralPropertyName(propertyName, caseInsensitive);
			String value = this.getCaseAndLiteralPropertyValue(SQLUtils.cleanString(propertyValue), caseInsensitive, allowWildcards);

			if (propertyValue.startsWith(SearchOperator.GREATER_THAN_EQUAL.op())) {
				criteria.gte(propertyName, value, propertyType);
			} else if (propertyValue.startsWith(SearchOperator.LESS_THAN_EQUAL.op())) {
				criteria.lte(propertyName, value, propertyType);
			} else if (propertyValue.startsWith(SearchOperator.GREATER_THAN.op())) {
				criteria.gt(propertyName, value, propertyType);
			} else if (propertyValue.startsWith(SearchOperator.LESS_THAN.op())) {
				criteria.lt(propertyName, value, propertyType);
			}
		}
	}

	private String getCaseAndLiteralPropertyName(String propertyName, boolean caseInsensitive){
		// KULRICE-85 : made string searches case insensitive - used new
		// DBPlatform function to force strings to upper case
		if (caseInsensitive) {
			// TODO: What to do here now that the JPA version does not extend platform aware?
			propertyName = getDbPlatform().getUpperCaseFunction() + "(__JPA_ALIAS__." + propertyName + ")";

		}
		return propertyName;
	}
	private String getCaseAndLiteralPropertyValue(String propertyValue, boolean caseInsensitive, boolean allowWildcards){
		//if (!allowWildcards) {
		//	propertyValue = StringUtils.replace(propertyValue, "*", "\\*");
		//}
		// KULRICE-85 : made string searches case insensitive - used new
		// DBPlatform function to force strings to upper case
		if (caseInsensitive) {
			//propertyName = "UPPER("+ tableAlias + "." + propertyName + ")";
			propertyValue = propertyValue.toUpperCase();
		}
		return propertyValue;
	}


	protected DateTimeService getDateTimeService(){
		if (dateTimeService == null) {
			dateTimeService = GlobalResourceLoader.getService(CoreConstants.Services.DATETIME_SERVICE);
    	}
    	return dateTimeService;
	}

	/**
	 * @param dateTimeService
	 *            the dateTimeService to set
	 */
	public void setDateTimeService(DateTimeService dateTimeService) {
		this.dateTimeService = dateTimeService;
	}

	public DatabasePlatform getDbPlatform() {
    	if (dbPlatform == null) {
    		dbPlatform = (DatabasePlatform) GlobalResourceLoader.getService(RiceConstants.DB_PLATFORM);
    	}
    	return dbPlatform;
    }

	public void setDbPlatform(DatabasePlatform dbPlatform){
		this.dbPlatform = dbPlatform;
	}

	 /**
     * When dealing with upperbound dates, it is a business requirement that if a timestamp isn't already
     * stated append 23:59:59 to the end of the date.  This ensures that you are searching for the entire
     * day.
     */
    private String cleanUpperBound(String stringDate){
    	final java.sql.Timestamp dt;
    	try {
			dt = getDateTimeService().convertToSqlTimestamp(stringDate);
		} catch (ParseException e) {
			throw new SQLBuilderException(e);
		}
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

		if("00:00:00".equals(sdfTime.format(dt)) && !StringUtils.contains(stringDate, "00:00:00") && !StringUtils.contains(stringDate, "12:00 AM")){
			stringDate = stringDate + " 23:59:59";
		}
		return stringDate;
    }

    /**
    *
    * This method will take a whole date like 03/02/2009 and convert it into
    * 03/02/2009 .. 03/02/20009 00:00:00
    *
    * This is used for non-range searchable attributes
    *
    * @param stringDate
    * @return
    */
   private String convertSimpleDateToDateRange(String stringDate){
	   final java.sql.Timestamp dt;
	   try {
   			dt = getDateTimeService().convertToSqlTimestamp(stringDate);
	   } catch (ParseException e) {
		   throw new SQLBuilderException(e);
	   }
	   SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

	   if("00:00:00".equals(sdfTime.format(dt)) && !StringUtils.contains(stringDate, "00:00:00") && !StringUtils.contains(stringDate, "12:00 AM")){
		   stringDate = stringDate + " .. " + stringDate + " 23:59:59";
	   }

		return stringDate;
   }

	public static final class SQLBuilderException extends RiceRuntimeException {
		public SQLBuilderException(Throwable t) {
			super(t);
		}
	}
}
