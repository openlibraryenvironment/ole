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
package org.kuali.rice.krad.dao.impl;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.search.SearchOperator;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.TypeUtils;
import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.core.framework.persistence.jpa.metadata.EntityDescriptor;
import org.kuali.rice.core.framework.persistence.jpa.metadata.FieldDescriptor;
import org.kuali.rice.core.framework.persistence.jpa.metadata.MetadataManager;
import org.kuali.rice.core.framework.persistence.ojb.conversion.OjbCharBooleanConversion;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.krad.bo.InactivatableFromTo;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectExtension;
import org.kuali.rice.krad.dao.LookupDao;
import org.kuali.rice.krad.lookup.CollectionIncomplete;
import org.kuali.rice.krad.lookup.LookupUtils;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.PersistenceStructureService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * JPA implementation of the LookupDao interface
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LookupDaoJpa implements LookupDao {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LookupDao.class);

	private DateTimeService dateTimeService;
	private PersistenceStructureService persistenceStructureService;
	private DataDictionaryService dataDictionaryService;

	@PersistenceContext
	private EntityManager entityManager;

    public Long findCountByMap(Object example, Map formProps) {
		Criteria criteria = new Criteria(example.getClass().getName());

		// iterate through the parameter map for key values search criteria
		Iterator propsIter = formProps.keySet().iterator();
		while (propsIter.hasNext()) {
			String propertyName = (String) propsIter.next();
			String searchValue = (String) formProps.get(propertyName);

			// if searchValue is empty and the key is not a valid property ignore
			if (StringUtils.isBlank(searchValue) || !(PropertyUtils.isWriteable(example, propertyName))) {
				continue;
			}

			// get property type which is used to determine type of criteria
			Class propertyType = ObjectUtils.getPropertyType(example, propertyName, persistenceStructureService);
			if (propertyType == null) {
				continue;
			}
			Boolean caseInsensitive = Boolean.TRUE;
			if (KRADServiceLocatorWeb.getDataDictionaryService().isAttributeDefined(example.getClass(), propertyName)) {
        		// If forceUppercase is true, both the database value and the user entry should be converted to Uppercase -- so change the caseInsensitive to false since we don't need to 
        		// worry about the values not matching.  However, if forceUppercase is false, make sure to do a caseInsensitive search because the database value and user entry 
        		// could be mixed case.  Thus, caseInsensitive will be the opposite of forceUppercase. 
				caseInsensitive = !KRADServiceLocatorWeb.getDataDictionaryService().getAttributeForceUppercase(example.getClass(), propertyName);
			}
			if (caseInsensitive == null) {
				caseInsensitive = Boolean.TRUE;
			}

			boolean treatWildcardsAndOperatorsAsLiteral = KNSServiceLocator.
					getBusinessObjectDictionaryService().isLookupFieldTreatWildcardsAndOperatorsAsLiteral(example.getClass(), propertyName); 
			// build criteria
    		if (!caseInsensitive) { 
    			// Verify that the searchValue is uppercased if caseInsensitive is false 
    			searchValue = searchValue.toUpperCase(); 
    		}
			addCriteria(propertyName, searchValue, propertyType, caseInsensitive, treatWildcardsAndOperatorsAsLiteral, criteria);
		}

		// execute query and return result list
		return (Long) new QueryByCriteria(entityManager, criteria).toCountQuery().getSingleResult();
	}

    /**
     * Since 2.3
     * This version of findCollectionBySearchHelper is needed for version compatibility.   It allows executeSearch
     * to behave the same way as it did prior to 2.3. The value for searchResultsLimit will be retrieved from the
     * KNS version of LookupUtils.
     *
     * @see org.kuali.rice.krad.dao.LookupDao#findCollectionBySearchHelper(java.lang.Class, java.util.Map, boolean,
     *      boolean)
     */
    public Collection findCollectionBySearchHelper(Class businessObjectClass, Map formProps, boolean unbounded,
            boolean usePrimaryKeyValuesOnly) {
        Integer searchResultsLimit = org.kuali.rice.kns.lookup.LookupUtils.getSearchResultsLimit(businessObjectClass);
        return findCollectionBySearchHelper(businessObjectClass, formProps, unbounded, usePrimaryKeyValuesOnly,
                searchResultsLimit);
    }

    /**
     * @see org.kuali.rice.krad.dao.LookupDao#findCollectionBySearchHelper(java.lang.Class, java.util.Map, boolean,
     *      boolean, Integer)
     *
     * If searchResultsLimit is null, the search results will not be limited by any other means.
     */
    public Collection findCollectionBySearchHelper(Class businessObjectClass, Map formProps, boolean unbounded,
            boolean usePrimaryKeyValuesOnly, Integer searchResultsLimit) {
        PersistableBusinessObject businessObject = checkBusinessObjectClass(businessObjectClass);
        if (usePrimaryKeyValuesOnly) {
            return executeSearch(businessObjectClass, getCollectionCriteriaFromMapUsingPrimaryKeysOnly(
                    businessObjectClass, formProps), unbounded, searchResultsLimit);
        } else {
            Criteria crit = getCollectionCriteriaFromMap(businessObject, formProps);
            return executeSearch(businessObjectClass, crit, unbounded, searchResultsLimit);
        }
    }

	public Criteria getCollectionCriteriaFromMap(PersistableBusinessObject example, Map formProps) {
		Criteria criteria = new Criteria(example.getClass().getName());
		Iterator propsIter = formProps.keySet().iterator();
		while (propsIter.hasNext()) {
			String propertyName = (String) propsIter.next();
			Boolean caseInsensitive = Boolean.TRUE;
			if (KRADServiceLocatorWeb.getDataDictionaryService().isAttributeDefined(example.getClass(), propertyName)) {
				caseInsensitive = !KRADServiceLocatorWeb.getDataDictionaryService().getAttributeForceUppercase(example.getClass(), propertyName);
			}
			if (caseInsensitive == null) {
				caseInsensitive = Boolean.TRUE;
			}
            boolean treatWildcardsAndOperatorsAsLiteral = KNSServiceLocator.
    				getBusinessObjectDictionaryService().isLookupFieldTreatWildcardsAndOperatorsAsLiteral(example.getClass(), propertyName);
			if (formProps.get(propertyName) instanceof Collection) {
				Iterator iter = ((Collection) formProps.get(propertyName)).iterator();
				while (iter.hasNext()) {
                    String searchValue = (String) iter.next();
            		if (!caseInsensitive) { 
            			// Verify that the searchValue is uppercased if caseInsensitive is false 
            			searchValue = searchValue.toUpperCase(); 
            		}
					if (!createCriteria(example, searchValue, propertyName, caseInsensitive, treatWildcardsAndOperatorsAsLiteral, criteria, formProps)) {
						throw new RuntimeException("Invalid value in Collection");
					}
				}
			} else {
                String searchValue = (String) formProps.get(propertyName);
        		if (!caseInsensitive) { 
        			// Verify that the searchValue is uppercased if caseInsensitive is false 
        			searchValue = searchValue.toUpperCase(); 
        		}
				if (!createCriteria(example, searchValue, propertyName, caseInsensitive, treatWildcardsAndOperatorsAsLiteral, criteria, formProps)) {
					continue;
				}
			}
		}
		return criteria;
	}

	public Criteria getCollectionCriteriaFromMapUsingPrimaryKeysOnly(Class businessObjectClass, Map formProps) {
		PersistableBusinessObject businessObject = checkBusinessObjectClass(businessObjectClass);
		Criteria criteria = new Criteria(businessObjectClass.getName());
		List pkFields = persistenceStructureService.listPrimaryKeyFieldNames(businessObjectClass);
		Iterator pkIter = pkFields.iterator();
		while (pkIter.hasNext()) {
			String pkFieldName = (String) pkIter.next();
			String pkValue = (String) formProps.get(pkFieldName);

			if (StringUtils.isBlank(pkValue)) {
				throw new RuntimeException("Missing pk value for field " + pkFieldName + " when a search based on PK values only is performed.");
			} else {
				for (SearchOperator op :  SearchOperator.QUERY_CHARACTERS) {
                    if (pkValue.contains(op.op())) {
                        throw new RuntimeException("Value \"" + pkValue + "\" for PK field " + pkFieldName + " contains wildcard/operator characters.");
                    }
                }
			}
            boolean treatWildcardsAndOperatorsAsLiteral = KNSServiceLocator.
    				getBusinessObjectDictionaryService().isLookupFieldTreatWildcardsAndOperatorsAsLiteral(businessObjectClass, pkFieldName);
			createCriteria(businessObject, pkValue, pkFieldName, false, treatWildcardsAndOperatorsAsLiteral, criteria);
		}
		return criteria;
	}

	private PersistableBusinessObject checkBusinessObjectClass(Class businessObjectClass) {
		if (businessObjectClass == null) {
			throw new IllegalArgumentException("BusinessObject class passed to LookupDao findCollectionBySearchHelper... method was null");
		}
		PersistableBusinessObject businessObject = null;
		try {
			businessObject = (PersistableBusinessObject) businessObjectClass.newInstance();
		} catch (IllegalAccessException e) {
			throw new RuntimeException("LookupDao could not get instance of " + businessObjectClass.getName(), e);
		} catch (InstantiationException e) {
			throw new RuntimeException("LookupDao could not get instance of " + businessObjectClass.getName(), e);
		}
		return businessObject;
	}

	private Collection executeSearch(Class businessObjectClass, Criteria criteria, boolean unbounded, Integer searchResultsLimit) {
		Collection<PersistableBusinessObject> searchResults = new ArrayList<PersistableBusinessObject>();
		Long matchingResultsCount = null;
		try {
			if (!unbounded && (searchResultsLimit != null)) {
				matchingResultsCount = (Long) new QueryByCriteria(entityManager, criteria).toCountQuery().getSingleResult();
				searchResults = new QueryByCriteria(entityManager, criteria).toQuery().setMaxResults(searchResultsLimit).getResultList();
			} else {
				searchResults = new QueryByCriteria(entityManager, criteria).toQuery().getResultList();
			}
			if ((matchingResultsCount == null) || (matchingResultsCount.intValue() <= searchResultsLimit.intValue())) {
				matchingResultsCount = new Long(0);
			}
			// Temp solution for loading extension objects - need to find a
			// better way
			// Should look for a JOIN query, for the above query, that will grab
			// the PBOEs as well (1+n query problem)
			for (PersistableBusinessObject bo : searchResults) {
				if (bo.getExtension() != null) {
					PersistableBusinessObjectExtension boe = bo.getExtension();
					EntityDescriptor entity = MetadataManager.getEntityDescriptor(bo.getExtension().getClass());
					Criteria extensionCriteria = new Criteria(boe.getClass().getName());
					for (FieldDescriptor fieldDescriptor : entity.getPrimaryKeys()) {
						try {
							Field field = bo.getClass().getDeclaredField(fieldDescriptor.getName());
							field.setAccessible(true);
							extensionCriteria.eq(fieldDescriptor.getName(), field.get(bo));
						} catch (Exception e) {
							LOG.error(e.getMessage(), e);
						}
					}
					try {
						boe = (PersistableBusinessObjectExtension) new QueryByCriteria(entityManager, extensionCriteria).toQuery().getSingleResult();
					} catch (PersistenceException e) {}
					bo.setExtension(boe);
				}
			}
			// populate Person objects in business objects
			List bos = new ArrayList();
			bos.addAll(searchResults);
			searchResults = bos;
		} catch (DataIntegrityViolationException e) {
			throw new RuntimeException("LookupDao encountered exception during executeSearch", e);
		}
		return new CollectionIncomplete(searchResults, matchingResultsCount);
	}

	/**
	 * Return whether or not an attribute is writeable. This method is aware
	 * that that Collections may be involved and handles them consistently with
	 * the way in which OJB handles specifying the attributes of elements of a
	 * Collection.
	 * 
	 * @param o
	 * @param p
	 * @return
	 * @throws IllegalArgumentException
	 */
	private boolean isWriteable(Object o, String p) throws IllegalArgumentException {
		if (null == o || null == p) {
			throw new IllegalArgumentException("Cannot check writeable status with null arguments.");
		}

		boolean b = false;

		// Try the easy way.
		if (!(PropertyUtils.isWriteable(o, p))) {

			// If that fails lets try to be a bit smarter, understanding that
			// Collections may be involved.
			if (-1 != p.indexOf('.')) {

				String[] parts = p.split("\\.");

				// Get the type of the attribute.
				Class c = ObjectUtils.getPropertyType(o, parts[0], persistenceStructureService);

				Object i = null;

				// If the next level is a Collection, look into the collection,
				// to find out what type its elements are.
				if (Collection.class.isAssignableFrom(c)) {
					Map<String, Class> m = persistenceStructureService.listCollectionObjectTypes(o.getClass());
					c = m.get(parts[0]);
				}

				// Look into the attribute class to see if it is writeable.
				try {
					i = c.newInstance();
					StringBuffer sb = new StringBuffer();
					for (int x = 1; x < parts.length; x++) {
						sb.append(1 == x ? "" : ".").append(parts[x]);
					}
					b = isWriteable(i, sb.toString());
				} catch (InstantiationException ie) {
					LOG.info(ie);
				} catch (IllegalAccessException iae) {
					LOG.info(iae);
				}
			}
		} else {
			b = true;
		}

		return b;
	}

	public boolean createCriteria(Object example, String searchValue, String propertyName, Object criteria) {
		return createCriteria(example, searchValue, propertyName, false, false, criteria);
	}
	
    public boolean createCriteria(Object example, String searchValue, String propertyName, boolean caseInsensitive, boolean treatWildcardsAndOperatorsAsLiteral, Object criteria) {
    	return createCriteria( example, searchValue, propertyName, caseInsensitive, treatWildcardsAndOperatorsAsLiteral, criteria, null );
    }

	public boolean createCriteria(Object example, String searchValue, String propertyName, boolean caseInsensitive, boolean treatWildcardsAndOperatorsAsLiteral, Object criteria, Map searchValues) {
		// if searchValue is empty and the key is not a valid property ignore
		if (!(criteria instanceof Criteria) || StringUtils.isBlank(searchValue) || !isWriteable(example, propertyName)) {
			return false;
		}

		// get property type which is used to determine type of criteria
		Class propertyType = ObjectUtils.getPropertyType(example, propertyName, persistenceStructureService);
		if (propertyType == null) {
			return false;
		}

		// build criteria
		if (example instanceof InactivatableFromTo) {
			if (KRADPropertyConstants.ACTIVE.equals(propertyName)) {
				addInactivateableFromToActiveCriteria(example, searchValue, (Criteria) criteria, searchValues);
			} else if (KRADPropertyConstants.CURRENT.equals(propertyName)) {
				addInactivateableFromToCurrentCriteria(example, searchValue, (Criteria) criteria, searchValues);
			} else if (!KRADPropertyConstants.ACTIVE_AS_OF_DATE.equals(propertyName)) {
				addCriteria(propertyName, searchValue, propertyType, caseInsensitive,
						treatWildcardsAndOperatorsAsLiteral, (Criteria) criteria);
			}
		} else {
			addCriteria(propertyName, searchValue, propertyType, caseInsensitive, treatWildcardsAndOperatorsAsLiteral,
					(Criteria) criteria);
		}
		
		return true;
	}

	/**
	 * @see org.kuali.rice.krad.dao.LookupDao#findObjectByMap(java.lang.Object,
	 *      java.util.Map)
	 */
	public Object findObjectByMap(Object example, Map formProps) {
		Criteria jpaCriteria = new Criteria(example.getClass().getName());

		Iterator propsIter = formProps.keySet().iterator();
		while (propsIter.hasNext()) {
			String propertyName = (String) propsIter.next();
			String searchValue = "";
			if (formProps.get(propertyName) != null) {
				searchValue = (formProps.get(propertyName)).toString();
			}

			if (StringUtils.isNotBlank(searchValue) & PropertyUtils.isWriteable(example, propertyName)) {
				Class propertyType = ObjectUtils.getPropertyType(example, propertyName, persistenceStructureService);
				if (TypeUtils.isIntegralClass(propertyType) || TypeUtils.isDecimalClass(propertyType)) {
					if (propertyType.equals(Long.class)) {
						jpaCriteria.eq(propertyName, new Long(searchValue));
					} else {
						jpaCriteria.eq(propertyName, new Integer(searchValue));
					}
				} else if (TypeUtils.isTemporalClass(propertyType)) {
					jpaCriteria.eq(propertyName, parseDate(ObjectUtils.clean(searchValue)));
				} else {
					jpaCriteria.eq(propertyName, searchValue);
				}
			}
		}

		return new QueryByCriteria(entityManager, jpaCriteria).toQuery().getSingleResult();
	}

	private void addCriteria(String propertyName, String propertyValue, Class propertyType, boolean caseInsensitive, boolean treatWildcardsAndOperatorsAsLiteral, Criteria criteria) {
		String alias = "";
		String[] keySplit = propertyName.split("\\.");
		if (keySplit.length > 1) {
			alias = keySplit[keySplit.length-2];
			String variableKey = keySplit[keySplit.length-1];
			for (int j = 0; j < keySplit.length - 1; j++)  {
				if (StringUtils.contains(keySplit[j], Criteria.JPA_ALIAS_PREFIX)) {
					String tempKey = keySplit[j].substring(keySplit[j].indexOf('\'', keySplit[j].indexOf(Criteria.JPA_ALIAS_PREFIX)) + 1,
							keySplit[j].lastIndexOf('\'', keySplit[j].indexOf(Criteria.JPA_ALIAS_SUFFIX)));
					if (criteria.getAliasIndex(tempKey) == -1) {
						criteria.join(tempKey, tempKey, false, true);
					}
				} else {
					if (criteria.getAliasIndex(keySplit[j]) == -1) {
						criteria.join(keySplit[j], keySplit[j], false, true);
					}
				}
			}
			if (!StringUtils.contains(propertyName, "__JPA_ALIAS[[")) {
				propertyName = "__JPA_ALIAS[['" + alias + "']]__." + variableKey;
			}
		}
		if (!treatWildcardsAndOperatorsAsLiteral && StringUtils.contains(propertyValue, SearchOperator.OR.op())) {
			addOrCriteria(propertyName, propertyValue, propertyType, caseInsensitive, criteria);
			return;
		}

		if (!treatWildcardsAndOperatorsAsLiteral && StringUtils.contains(propertyValue, SearchOperator.AND.op())) {
			addAndCriteria(propertyName, propertyValue, propertyType, caseInsensitive, criteria);
			return;
		}

        if (StringUtils.containsIgnoreCase(propertyValue, SearchOperator.NULL.op())) {
        	if (StringUtils.contains(propertyValue, SearchOperator.NOT.op())) {
        		criteria.notNull(propertyName);
        	}
        	else {
        		criteria.isNull(propertyName);
        	}
        }
        else if (TypeUtils.isStringClass(propertyType)) {
			// KULRICE-85 : made string searches case insensitive - used new
			// DBPlatform function to force strings to upper case
			if (caseInsensitive) {
				// TODO: What to do here now that the JPA version does not extend platform aware?
				//propertyName = getDbPlatform().getUpperCaseFunction() + "(__JPA_ALIAS[[0]]__." + propertyName + ")";
				if (StringUtils.contains(propertyName, "__JPA_ALIAS[[")) {
					propertyName = "UPPER(" + propertyName + ")";
				} else {
					propertyName = "UPPER(__JPA_ALIAS[[0]]__." + propertyName + ")";
				}
				propertyValue = propertyValue.toUpperCase();
			}
			if (!treatWildcardsAndOperatorsAsLiteral && StringUtils.contains(propertyValue,
					SearchOperator.NOT.op())) {
				addNotCriteria(propertyName, propertyValue, propertyType,
						caseInsensitive, criteria);
            } else if (
            		!treatWildcardsAndOperatorsAsLiteral && propertyValue != null && (
            				StringUtils.contains(propertyValue, SearchOperator.BETWEEN.op())
            				|| propertyValue.startsWith(">")
            				|| propertyValue.startsWith("<") ) ) {
				addStringRangeCriteria(propertyName, propertyValue, criteria);
			} else {
				if (treatWildcardsAndOperatorsAsLiteral) {
					propertyValue = StringUtils.replace(propertyValue, "*", "\\*");
				}
				criteria.like(propertyName, propertyValue);
			}
		} else if (TypeUtils.isIntegralClass(propertyType) || TypeUtils.isDecimalClass(propertyType)) {
			addNumericRangeCriteria(propertyName, propertyValue, criteria);
		} else if (TypeUtils.isTemporalClass(propertyType)) {
			addDateRangeCriteria(propertyName, propertyValue, criteria);
		} else if (TypeUtils.isBooleanClass(propertyType)) {
			String temp = ObjectUtils.clean(propertyValue);
			criteria.eq(propertyName, ("Y".equalsIgnoreCase(temp) || "T".equalsIgnoreCase(temp) || "1".equalsIgnoreCase(temp) || "true".equalsIgnoreCase(temp)) ? true : false);
		} else {
			LOG.error("not adding criterion for: " + propertyName + "," + propertyType + "," + propertyValue);
		}
	}
	
    
    /**
     * Translates criteria for active status to criteria on the active from and to fields
     * 
     * @param example - business object being queried on
     * @param activeSearchValue - value for the active search field, should convert to boolean
     * @param criteria - Criteria object being built
     * @param searchValues - Map containing all search keys and values
     */
    protected void addInactivateableFromToActiveCriteria(Object example, String activeSearchValue, Criteria criteria, Map searchValues) {
    	Timestamp activeTimestamp = LookupUtils.getActiveDateTimestampForCriteria(searchValues);
		
    	String activeBooleanStr = (String) (new OjbCharBooleanConversion()).javaToSql(activeSearchValue);
    	if (OjbCharBooleanConversion.DATABASE_BOOLEAN_TRUE_STRING_REPRESENTATION.equals(activeBooleanStr)) {
    		// (active from date <= date or active from date is null) and (date < active to date or active to date is null)
    		Criteria criteriaBeginDate = new Criteria(example.getClass().getName());
    		criteriaBeginDate.lte(KRADPropertyConstants.ACTIVE_FROM_DATE, activeTimestamp);
    		
    		Criteria criteriaBeginDateNull = new Criteria(example.getClass().getName());
    		criteriaBeginDateNull.isNull(KRADPropertyConstants.ACTIVE_FROM_DATE);
    		criteriaBeginDate.or(criteriaBeginDateNull);
    		
    		criteria.and(criteriaBeginDate);
    		
    		Criteria criteriaEndDate = new Criteria(example.getClass().getName());
    		criteriaEndDate.gt(KRADPropertyConstants.ACTIVE_TO_DATE, activeTimestamp);
    	
    		Criteria criteriaEndDateNull = new Criteria(example.getClass().getName());
    		criteriaEndDateNull.isNull(KRADPropertyConstants.ACTIVE_TO_DATE);
    		criteriaEndDate.or(criteriaEndDateNull);
    		
    		criteria.and(criteriaEndDate);
    	}
    	else if (OjbCharBooleanConversion.DATABASE_BOOLEAN_FALSE_STRING_REPRESENTATION.equals(activeBooleanStr)) {
    		// (date < active from date) or (active from date is null) or (date >= active to date) 
    		Criteria criteriaNonActive = new Criteria(example.getClass().getName());
    		criteriaNonActive.gt(KRADPropertyConstants.ACTIVE_FROM_DATE, activeTimestamp);
    		
    		Criteria criteriaBeginDateNull = new Criteria(example.getClass().getName());
    		criteriaBeginDateNull.isNull(KRADPropertyConstants.ACTIVE_FROM_DATE);
    		criteriaNonActive.or(criteriaBeginDateNull);
    		
    		Criteria criteriaEndDate = new Criteria(example.getClass().getName());
    		criteriaEndDate.lte(KRADPropertyConstants.ACTIVE_TO_DATE, activeTimestamp);
    		criteriaNonActive.or(criteriaEndDate);
    		
    		criteria.and(criteriaNonActive);
    	}
    }
    
    /**
     * Translates criteria for current status to a sub-query on active begin date
     * 
     * @param example - business object being queried on
     * @param currentSearchValue - value for the current search field, should convert to boolean
     * @param criteria - Criteria object being built
     */
	protected void addInactivateableFromToCurrentCriteria(Object example, String currentSearchValue, Criteria criteria, Map searchValues) {
		Timestamp activeTimestamp = LookupUtils.getActiveDateTimestampForCriteria(searchValues);
		
		List<String> groupByFieldList = dataDictionaryService.getGroupByAttributesForEffectiveDating(example
				.getClass());
		if (groupByFieldList == null) {
			return;
		}

		String alias = "c";

		String jpql = " (select max(" + alias + "." + KRADPropertyConstants.ACTIVE_FROM_DATE + ") from "
				+ example.getClass().getName() + " as " + alias + " where ";
		String activeDateDBStr = KRADServiceLocator.getDatabasePlatform().getDateSQL(dateTimeService.toDateTimeString(activeTimestamp), null);
		jpql += alias + "." + KRADPropertyConstants.ACTIVE_FROM_DATE + " <= '" + activeDateDBStr + "'";

		// join back to main query with the group by fields
		boolean firstGroupBy = true;
		String groupByJpql = "";
		for (String groupByField : groupByFieldList) {
			if (!firstGroupBy) {
				groupByJpql += ", ";
			}

			jpql += " AND " + alias + "." + groupByField + " = " + criteria.getAlias() + "." + groupByField + " ";
			groupByJpql += alias + "." + groupByField;
			firstGroupBy = false;
		}

		jpql += " group by " + groupByJpql + " )";

		String currentBooleanStr = (String) (new OjbCharBooleanConversion()).javaToSql(currentSearchValue);
		if (OjbCharBooleanConversion.DATABASE_BOOLEAN_TRUE_STRING_REPRESENTATION.equals(currentBooleanStr)) {
			jpql = criteria.getAlias() + "." + KRADPropertyConstants.ACTIVE_FROM_DATE + " in " + jpql;
		} else if (OjbCharBooleanConversion.DATABASE_BOOLEAN_FALSE_STRING_REPRESENTATION.equals(currentBooleanStr)) {
			jpql = criteria.getAlias() + "." + KRADPropertyConstants.ACTIVE_FROM_DATE + " not in " + jpql;
		}

		criteria.rawJpql(jpql);
	}

	private void addOrCriteria(String propertyName, String propertyValue, Class propertyType, boolean caseInsensitive, Criteria criteria) {
		addLogicalOperatorCriteria(propertyName, propertyValue, propertyType, caseInsensitive, criteria, SearchOperator.OR.op());
	}

	private void addAndCriteria(String propertyName, String propertyValue, Class propertyType, boolean caseInsensitive, Criteria criteria) {
		addLogicalOperatorCriteria(propertyName, propertyValue, propertyType, caseInsensitive, criteria, SearchOperator.AND.op());
	}

	private void addNotCriteria(String propertyName, String propertyValue, Class propertyType, boolean caseInsensitive, Criteria criteria) {
		String[] splitPropVal = StringUtils.split(propertyValue, SearchOperator.NOT.op());

		int strLength = splitPropVal.length;
		// if more than one NOT operator assume an implicit and (i.e. !a!b = !a&!b)
		if (strLength > 1) {
			String expandedNot = "!" + StringUtils.join(splitPropVal, SearchOperator.AND.op() + SearchOperator.NOT.op());
			// we know that since this method is called, treatWildcardsAndOperatorsAsLiteral is false
			addCriteria(propertyName, expandedNot, propertyType, caseInsensitive, false, criteria);
		} else {
			// only one so add a not like
			criteria.notLike(propertyName, splitPropVal[0]);
		}
	}

	private void addLogicalOperatorCriteria(String propertyName, String propertyValue, Class propertyType, boolean caseInsensitive, Criteria criteria, String splitValue) {
		String[] splitPropVal = StringUtils.split(propertyValue, splitValue);

		Criteria subCriteria = new Criteria("N/A");
		for (int i = 0; i < splitPropVal.length; i++) {
			Criteria predicate = new Criteria("N/A");
			// we know that since this method is called, treatWildcardsAndOperatorsAsLiteral is false
			addCriteria(propertyName, splitPropVal[i], propertyType, caseInsensitive, false, predicate);
			if (splitValue == SearchOperator.OR.op()) {
				subCriteria.or(predicate);
			}
			if (splitValue == SearchOperator.AND.op()) {
				subCriteria.and(predicate);
			}
		}

		criteria.and(subCriteria);
	}

	private java.sql.Date parseDate(String dateString) {
		dateString = dateString.trim();
		try {
			return dateTimeService.convertToSqlDate(dateString);
		} catch (ParseException ex) {
			return null;
		}
	}

	private void addDateRangeCriteria(String propertyName, String propertyValue, Criteria criteria) {

		if (StringUtils.contains(propertyValue, SearchOperator.BETWEEN.op())) {
			String[] rangeValues = StringUtils.split(propertyValue, SearchOperator.BETWEEN.op());
			criteria.between(propertyName, parseDate(ObjectUtils.clean(rangeValues[0])), parseDate(ObjectUtils.clean(rangeValues[1])));
		} else if (propertyValue.startsWith(">=")) {
			criteria.gte(propertyName, parseDate(ObjectUtils.clean(propertyValue)));
		} else if (propertyValue.startsWith("<=")) {
			criteria.lte(propertyName, parseDate(ObjectUtils.clean(propertyValue)));
		} else if (propertyValue.startsWith(">")) {
			criteria.gt(propertyName, parseDate(ObjectUtils.clean(propertyValue)));
		} else if (propertyValue.startsWith("<")) {
			criteria.lt(propertyName, parseDate(ObjectUtils.clean(propertyValue)));
		} else {
			criteria.eq(propertyName, parseDate(ObjectUtils.clean(propertyValue)));
		}
	}

	private BigDecimal cleanNumeric(String value) {
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
		try {
			return new BigDecimal(cleanedValue);
		} catch (NumberFormatException ex) {
			GlobalVariables.getMessageMap().putError(KRADConstants.DOCUMENT_ERRORS, RiceKeyConstants.ERROR_CUSTOM, new String[] { "Invalid Numeric Input: " + value });
			return null;
		}
	}

	private void addNumericRangeCriteria(String propertyName, String propertyValue, Criteria criteria) {

		if (StringUtils.contains(propertyValue, SearchOperator.BETWEEN.op())) {
			String[] rangeValues = StringUtils.split(propertyValue, SearchOperator.BETWEEN.op());
			criteria.between(propertyName, cleanNumeric(rangeValues[0]), cleanNumeric(rangeValues[1]));
		} else if (propertyValue.startsWith(">=")) {
			criteria.gte(propertyName, cleanNumeric(propertyValue));
		} else if (propertyValue.startsWith("<=")) {
			criteria.lte(propertyName, cleanNumeric(propertyValue));
		} else if (propertyValue.startsWith(">")) {
			criteria.gt(propertyName, cleanNumeric(propertyValue));
		} else if (propertyValue.startsWith("<")) {
			criteria.lt(propertyName, cleanNumeric(propertyValue));
		} else {
			criteria.eq(propertyName, cleanNumeric(propertyValue));
		}
	}

	private void addStringRangeCriteria(String propertyName, String propertyValue, Criteria criteria) {

		if (StringUtils.contains(propertyValue, SearchOperator.BETWEEN.op())) {
			String[] rangeValues = StringUtils.split(propertyValue, SearchOperator.BETWEEN.op());
			criteria.between(propertyName, rangeValues[0], rangeValues[1]);
		} else if (propertyValue.startsWith(">=")) {
			criteria.gte(propertyName, ObjectUtils.clean(propertyValue));
		} else if (propertyValue.startsWith("<=")) {
			criteria.lte(propertyName, ObjectUtils.clean(propertyValue));
		} else if (propertyValue.startsWith(">")) {
			criteria.gt(propertyName, ObjectUtils.clean(propertyValue));
		} else if (propertyValue.startsWith("<")) {
			criteria.lt(propertyName, ObjectUtils.clean(propertyValue));
		}
	}

	/**
	 * @param dateTimeService
	 *            the dateTimeService to set
	 */
	public void setDateTimeService(DateTimeService dateTimeService) {
		this.dateTimeService = dateTimeService;
	}

    /**
     * @return the entityManager
     */
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * @param entityManager the entityManager to set
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
	public void setPersistenceStructureService(PersistenceStructureService persistenceStructureService) {
		this.persistenceStructureService = persistenceStructureService;
	}

	public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
        this.dataDictionaryService = dataDictionaryService;
    }
	
}
