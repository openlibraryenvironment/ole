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

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.type.TypeUtils;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria.QueryByCriteriaType;
import org.kuali.rice.core.framework.persistence.platform.DatabasePlatform;
import org.kuali.rice.core.web.format.BooleanFormatter;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



/**
 * A criteria builder for JDBC Query strings.
 *
 * TODO: Rewrite this class with a better criteria building algorithm.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@SuppressWarnings("unchecked")
public class Criteria {
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(Criteria.class);

	private Integer searchLimit;

	private String entityName;

	private String alias;

	private boolean distinct = false;
	
	private transient DateTimeService dateTimeService;

	protected List tokens = new ArrayList();

	private List orderByTokens = new ArrayList();

	protected Map<String, Object> params = new LinkedHashMap<String, Object>();

	DatabasePlatform dbPlatform = null;

	public Criteria(String entityName) {
		this(entityName, "a");
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

	public Criteria(String entityName, String alias) {
		this.entityName = entityName;
		this.alias = alias;
	}

	public void between(String attribute, Object value1, Object value2, Class propertyType) {

		String fixedValue1 = this.fixValue(value1, propertyType);
		String fixedValue2= this.fixValue(value2, propertyType);
		if (attribute.contains("__JPA_ALIAS__")) {
			tokens.add(" (" + fix(attribute) + " BETWEEN " + fixedValue1 + " AND " + fixedValue2 + ") ");
		} else {
			tokens.add(" (" + alias + "." + attribute + " BETWEEN " + fixedValue1 + " AND " + fixedValue2 + ") ");
		}

	}

	private String fixValue(Object value, Class propertyType){
		
		if (value == null) {
			return "";
		}

		if(SqlBuilder.isJoinClass(propertyType)){
			return value.toString();
		}

		if(TypeUtils.isIntegralClass(propertyType) || TypeUtils.isDecimalClass(propertyType)){
			new BigDecimal(value.toString()); // This should throw an exception if the number is invalid.
			return value.toString();
		}
		if(TypeUtils.isTemporalClass(propertyType)){
			try {
				if (value instanceof String) {
					value = getDateTimeService().convertToSqlTimestamp(value.toString());
				}
				return getFixedTemporalValue(value);
			} catch (ParseException pe) {
				LOG.warn("Could not parse "+value.toString()+" as date");
				throw new RuntimeException("Could not parse "+value.toString()+" as date", pe);
			}
		}
		if (TypeUtils.isStringClass(propertyType)) {
			return " '" + getDbPlatform().escapeString(value.toString().trim()) + "' ";
		}
		if (TypeUtils.isBooleanClass(propertyType)) {
			if (value instanceof String) {
				value = new BooleanFormatter().convertFromPresentationFormat(value.toString());
			}
			boolean bVal = ((Boolean)value).booleanValue();
			if(bVal){return "1";}
			else { return "0";}
		}

		return value.toString();
	}
	
	/**
	 * Prepares a temporally classed value for inclusion in criteria
	 * @param value the Timestamp value to convert
	 * @return the fixed SQL version of that value
	 */
	private String getFixedTemporalValue(Object value) {
		Timestamp ts = (Timestamp)value;
		java.sql.Date dt = new java.sql.Date(ts.getTime());
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

		String sql = getDbPlatform().getDateSQL(sdfDate.format(dt),sdfTime.format(dt)) ;
		return sql;
	}


	/**
	 * This method ...
	 *
	 * @param string
	 * @return
	 */
/*
	private String fixAttr(String attr) {
		return "?";
		//return fixAttr(attr,0);
	}
	private String fixAttr(String attr, int cnt) {
		String sRet = attr.replace(".", "_");

		if(params.containsKey(sRet)){
			sRet = fixAttr(attr, ++cnt);
		}
		return sRet;
	}
*/
	public void eq(String attribute, Object value, Class propertyType) {

		tokens.add(alias + "." + attribute + " = " + fixValue(value, propertyType) + " ");

	}

	public void gt(String attribute, Object value, Class propertyType) {
		if (attribute.contains("__JPA_ALIAS__")) {
			tokens.add(fix(attribute) + " > " + fixValue(value, propertyType) + " ");
		} else {
			tokens.add(alias + "." + attribute + " > " + fixValue(value, propertyType) + " ");
		}

	}

	public void gte(String attribute, Object value, Class propertyType) {
		if (attribute.contains("__JPA_ALIAS__")) {
			tokens.add(fix(attribute) + " >= " + fixValue(value, propertyType) + " ");
		} else {
			tokens.add(alias + "." + attribute + " >= " + fixValue(value, propertyType) + " ");
		}
	}

	public void like(String attribute, Object value, Class propertyType, boolean allowWildcards) {
		String fixedValue = fixValue(value, propertyType);

		if(allowWildcards){
            fixedValue = fixWildcards(fixedValue, getAttributeValueStartIndex(fixedValue), getAttributeValueEndIndex(
                    fixedValue));
		}

		if (attribute.contains("__JPA_ALIAS__")) {
			tokens.add(fix(attribute) + " LIKE " +  fixedValue + " ");
		} else {
			tokens.add(alias + "." + attribute + " LIKE " + fixedValue + " ");
		}
	}

	public void notLike(String attribute, Object value, Class propertyType, boolean allowWildcards) {
		String fixedValue = fixValue(value, propertyType);

		if(allowWildcards){
			fixedValue = fixWildcards(fixedValue, getAttributeValueStartIndex(fixedValue), getAttributeValueEndIndex(fixedValue));
		}

		if (attribute.contains("__JPA_ALIAS__")) {
			tokens.add(fix(attribute) + " NOT LIKE " + fixedValue + " ");
		} else {
			tokens.add(alias + "." + attribute + " NOT LIKE " + fixedValue + " ");
		}
		//tokens.add(alias + "." + attribute + " NOT LIKE " + stripFunctions(fixedValue).replaceAll("\\*", "%") + " ");
	}

    public void notEqual(String attribute, Object value, Class propertyType, boolean allowWildcards) {
		String fixedValue = fixValue(value, propertyType);

		if(allowWildcards){
            fixedValue = fixWildcards(fixedValue, getAttributeValueStartIndex(fixedValue), getAttributeValueEndIndex(
                    fixedValue));
		}

		if (attribute.contains("__JPA_ALIAS__")) {
			tokens.add(fix(attribute) + " <> " + fixedValue + " ");
		} else {
			tokens.add(alias + "." + attribute + " <> " + fixedValue + " ");
		}
		//tokens.add(alias + "." + attribute + " NOT LIKE " + stripFunctions(fixedValue).replaceAll("\\*", "%") + " ");
	}

	private static String fixWildcards(String sIn, int startIndex, int endIndex){
        String attribute = startIndex == -1 ? sIn : sIn.substring(startIndex+1, endIndex);
        attribute = attribute.replaceAll("\\*", "%");
		attribute = attribute.replaceAll("\\?", "_");
        
        return startIndex == -1 ? attribute :
                (new StringBuilder(sIn.substring(0, startIndex+1)).append(attribute).append(sIn.substring(endIndex)).toString());
	}

	public void lt(String attribute, Object value, Class propertyType) {
		if (attribute.contains("__JPA_ALIAS__")) {
			tokens.add(fix(attribute) + " < " + fixValue(value, propertyType) + " ");
		} else {
			tokens.add(alias + "." + attribute + " < " + fixValue(value, propertyType) + " ");
		}
	}

	public void lte(String attribute, Object value, Class propertyType) {
		if (attribute.contains("__JPA_ALIAS__")) {
			tokens.add(fix(attribute) + " <= " + fixValue(value, propertyType) + " ");
		} else {
			tokens.add(alias + "." + attribute + " <= " + fixValue(value, propertyType) + " ");
		}
	}

	public void ne(String attribute, Object value, Class propertyType) {
		tokens.add(alias + "." + attribute + " != " + fixValue(value, propertyType) + " ");
	}

	public void isNull(String attribute) {
		tokens.add(alias + "." + attribute + " IS NULL ");
	}

	public void rawJpql(String jpql) {
		tokens.add(" " + jpql + " ");
	}

	public void in(String attribute, List values, Class propertyType) {
		String in = "";
		for (Object object : values) {
			in += fixValue(object, propertyType) + ",";
		}
		if (!"".equals(in)) {
			in = in.substring(0, in.length()-1);
		}
		tokens.add(alias + "." + attribute + " IN (" + in + ") ");
	}

	public void notIn(String attribute, List values, Class propertyType) {
		String in = "";
		for (Object object : values) {
			in += fixValue(object, propertyType) + ",";
		}
		if (!"".equals(in)) {
			in = in.substring(in.length()-1);
		}
		tokens.add(alias + "." + attribute + " NOT IN (" + in + ") ");
	}

	public void orderBy(String attribute, boolean sortAscending) {
		String sort = (sortAscending ? "ASC" : "DESC");
		orderByTokens.add(alias + "." + attribute + " " + sort + " ");
	}

	public void and(Criteria and) {
		tokens.add(new AndCriteria(and));
	}

	public void or(Criteria or) {
		tokens.add(new OrCriteria(or));
	}

	public void exists(Criteria exists) {
        tokens.add(new ExistsCriteria(exists));
    }
	
	public String toQuery(QueryByCriteriaType type) {
		String queryType = type.toString();
		if (type.equals(QueryByCriteriaType.SELECT)) {
			if(distinct){
				queryType += " " + "DISTINCT";
			}

			queryType += " " + alias;
		}
		String queryString = queryType + " FROM " + entityName + " AS " + alias;
		if (!tokens.isEmpty()) {
			queryString += " WHERE " + buildWhere();
		}
		if (!orderByTokens.isEmpty()) {
			queryString += " ORDER BY ";
			int count = 0;
			for (Iterator iterator = orderByTokens.iterator(); iterator.hasNext();) {
				Object token = (Object) iterator.next();
				if (count == 0) {
					count++;
				} else {
					queryString += ", ";
				}
				queryString += (String) token;
			}
		}
		return fix(queryString);
	}

	public String toCountQuery() {
		String queryString = "SELECT COUNT(*) FROM " + entityName + " AS " + alias;
		if (!tokens.isEmpty()) {
			queryString += " WHERE " + buildWhere();
		}
		return fix(queryString);
	}

	private String fix(String queryString) {
		queryString = queryString.replaceAll("__JPA_ALIAS__", alias);
		return queryString;
	}

	public String buildWhere() {
		return fix(buildWhere(null));
	}

	private String buildWhere(Criteria parentCriteria) {
		String queryString = "";
		int i = 0;
		for (Iterator iterator = tokens.iterator(); iterator.hasNext();) {
			Object token = (Object) iterator.next();
			if (token instanceof Criteria) {
				String logic = "";
				if (i>0 && token instanceof AndCriteria) {
					logic = " AND ";
				} else if (i>0 && token instanceof OrCriteria) {
					logic = " OR ";
				} else if (i>0 && token instanceof ExistsCriteria) {
                    logic = " EXISTS ";
                }
			queryString += logic + " (" + ((Criteria) token).buildWhere(((Criteria) token)) + ") ";
			} else {
				if(i>0){
					queryString += " AND " + (String) token;
				}else{
					queryString += (String) token;
				}
			}
			i++;
		}
		return queryString;
	}

	// Keep this package access so the QueryByCriteria can call it from this package.
	void prepareParameters(Query query) {
		prepareParameters(query, tokens, params);
	}

	public List<Object> getParameteres() {
		return getParameteres(tokens, params);
	}

	public List<Object> getParameteres(List tokens, Map<String, Object> params) {

		List<Object> mRet = new ArrayList<Object>();

		for (Map.Entry<String, Object> param : params.entrySet()) {
			Object value = param.getValue();
			if (value instanceof BigDecimal) {
				value = new Long(((BigDecimal)value).longValue());
			}
			if (value instanceof String) {
				value = ((String)value).replaceAll("\\*", "%");
			}
			mRet.add(value);
		}
		for (Iterator iterator = tokens.iterator(); iterator.hasNext();) {
			Object token = (Object) iterator.next();
			if (token instanceof Criteria) {
				mRet.addAll(getParameteres(((Criteria)token).tokens, ((Criteria)token).params));
			}
		}
		return mRet;
	}

	void prepareParameters(Query query, List tokens, Map<String, Object> params) {
		for (Map.Entry<String, Object> param : params.entrySet()) {
			Object value = param.getValue();
			if (value instanceof BigDecimal) {
				value = new Long(((BigDecimal)value).longValue());
			}
			if (value instanceof String) {
				value = ((String)value).replaceAll("\\*", "%");
			}
			query.setParameter(param.getKey(), value);
		}
		for (Iterator iterator = tokens.iterator(); iterator.hasNext();) {
			Object token = (Object) iterator.next();
			if (token instanceof Criteria) {
				prepareParameters(query, ((Criteria)token).tokens, ((Criteria)token).params);
			}
		}
	}

	private class AndCriteria extends Criteria {
		public AndCriteria(Criteria and) {
			super(and.entityName, and.alias);
			this.tokens = new ArrayList(and.tokens);
			this.params = new HashMap(and.params);
		}
	}

	private class OrCriteria extends Criteria {
		public OrCriteria(Criteria or) {
			super(or.entityName, or.alias);
			this.tokens = new ArrayList(or.tokens);
			this.params = new HashMap(or.params);
		}
	}
	
	private class ExistsCriteria extends Criteria {
        public ExistsCriteria(Criteria exists) {
            super(exists.entityName, exists.alias);
            this.tokens = new ArrayList(exists.tokens);
            this.params = new HashMap(exists.params);
        }       
    }

	public Integer getSearchLimit() {
		return this.searchLimit;
	}

	public void setSearchLimit(Integer searchLimit) {
		this.searchLimit = searchLimit;
	}


	public void notNull(String attribute) {
		tokens.add(alias + "." + attribute + " IS NOT NULL ");
	}

	public void distinct(boolean distinct){
		this.distinct = distinct;
	}

	/**
	 * This method ...
	 *
	 * @param string
	 * @param timestamp
	 * @param timestamp2
	 */
	public void notBetween(String attribute, Object value1,
			Object value2, Class propertyType) {
		String fixedValue1 = fixValue(value1, propertyType);
		String fixedValue2 = fixValue(value1, propertyType);
		if (attribute.contains("__JPA_ALIAS__")) {
			tokens.add(" (" + fix(attribute) + " NOT BETWEEN " + fixedValue1 + " AND " + fixedValue2 + ") ");
		} else {
			tokens.add(" (" + alias + "." + attribute + " NOT BETWEEN " + fixedValue1 + " AND " + fixedValue2 + ") ");
		}

	}

	/**
	 * This method ...
	 *
	 * @param string
	 * @param responsibilitySubQuery
	 */
	public void in(String match, Criteria subQuery, String attribute, Class propertyType) {
		if("a".equals(subQuery.alias)){
			subQuery.alias="b";
		}
		String whereClause = "";
		if(subQuery.tokens.isEmpty()){
			whereClause = "WHERE ";
		}else{
			whereClause = "AND ";
		}
		whereClause += subQuery.alias+"."+attribute + " = " + alias+"."+match;

		tokens.add("EXISTS (" + subQuery.toQuery(QueryByCriteriaType.SELECT) + whereClause + " ) ");

	}

	private String stripFunctions(String attribute) {
	    int index = attribute.lastIndexOf('(');
	    if(index != -1) {
	        return attribute.substring(index+1, attribute.indexOf(')'));
	    }

	    return attribute;
	}

    //indexes to strip off sql functions
    private int getAttributeValueStartIndex(String attribute) {
        return attribute.lastIndexOf('(');
    }

    private int getAttributeValueEndIndex(String attribute) {
        return attribute.lastIndexOf(')');
    }
    
    
	public String getAlias(){
		return this.alias;
	}

	public String establishDateString(String fromDate, String toDate, String columnDbName, String whereStatementClause) {
    	DatabasePlatform platform = getDbPlatform();
    	StringBuffer dateSqlString = new StringBuffer(whereStatementClause).append(" " + platform.escapeString(columnDbName) + " ");
        if (fromDate != null && SQLUtils.getSqlFormattedDate(fromDate) != null && toDate != null && SQLUtils.getSqlFormattedDate(toDate) != null) {
            return dateSqlString.append(" >= " + platform.getDateSQL(platform.escapeString(SQLUtils.getSqlFormattedDate(fromDate.trim())), null) + " and " + platform.escapeString(columnDbName) + " <= " + platform.getDateSQL(platform.escapeString(SQLUtils.getSqlFormattedDate(toDate.trim())), "23:59:59")).toString();
        } else {
            if (fromDate != null && SQLUtils.getSqlFormattedDate(fromDate) != null) {
                return dateSqlString.append(" >= " + platform.getDateSQL(platform.escapeString(SQLUtils.getSqlFormattedDate(fromDate.trim())), null)).toString();
            } else if (toDate != null && SQLUtils.getSqlFormattedDate(toDate) != null) {
                return dateSqlString.append(" <= " + platform.getDateSQL(platform.escapeString(SQLUtils.getSqlFormattedDate(toDate.trim())), "23:59:59")).toString();
            } else {
                return "";
            }
        }
    }
	
	private DateTimeService getDateTimeService() {
    	if (this.dateTimeService == null) {
    		this.dateTimeService = GlobalResourceLoader.getService(CoreConstants.Services.DATETIME_SERVICE);
    	}
    	return this.dateTimeService;
    }
}
