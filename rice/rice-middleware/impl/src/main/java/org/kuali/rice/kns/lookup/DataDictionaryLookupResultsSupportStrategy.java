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
package org.kuali.rice.kns.lookup;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.search.SearchOperator;
import org.kuali.rice.core.web.format.Formatter;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.kns.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * LookupResults support strategy which uses the primary keys and lookupable defined in a business object's data dictionary file to support the multivalue lookup 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DataDictionaryLookupResultsSupportStrategy implements
		LookupResultsSupportStrategyService {
	
	private DataDictionaryService dataDictionaryService;

	/**
	 * Builds a lookup id for the given business object
	 * @see LookupResultsSupportStrategyService#getLookupIdForBusinessObject(org.kuali.rice.krad.bo.BusinessObject)
	 */
	public String getLookupIdForBusinessObject(BusinessObject businessObject) {
		final List<String> pkFieldNames = getPrimaryKeyFieldsForBusinessObject(businessObject.getClass());
		return convertPKFieldMapToLookupId(pkFieldNames, businessObject);
	}

	/**
	 * Determines if both the primary keys and Lookupable are present in the data dictionary definition; if so, the BO qualifies, but if either are missing, it does not
	 * @see LookupResultsSupportStrategyService#qualifiesForStrategy(java.lang.Class)
	 */
	public boolean qualifiesForStrategy(Class<? extends BusinessObject> boClass) {
		if (getLookupableForBusinessObject(boClass) == null) {
			return false; // this probably isn't going to happen, but still...
		}
		final List<String> pkFields = getPrimaryKeyFieldsForBusinessObject(boClass);
		return pkFields != null && pkFields.size() > 0;
	}

	/**
	 * Uses the Lookupable associated with the given BusinessObject to search for business objects
	 * @see LookupResultsSupportStrategyService#retrieveSelectedResultBOs(java.lang.String, java.lang.Class, java.lang.String, org.kuali.rice.krad.lookup.LookupResultsService)
	 */
	public <T extends BusinessObject> Collection<T> retrieveSelectedResultBOs(Class<T> boClass, Set<String> lookupIds) throws Exception {
        
        List<T> retrievedBusinessObjects = new ArrayList<T>();
        final org.kuali.rice.kns.lookup.Lookupable lookupable = getLookupableForBusinessObject(boClass);
        for (String lookupId : lookupIds) {
        	final Map<String, String> lookupKeys = convertLookupIdToPKFieldMap(lookupId, boClass);
        	List<? extends BusinessObject> bos = lookupable.getSearchResults(lookupKeys);
        	
        	// we should only get one business object...but let's put them all in...
        	for (BusinessObject bo : bos) {
        		retrievedBusinessObjects.add((T)bo);
        	}
        }
		
		return retrievedBusinessObjects;
	}
	
	/**
	 * Retrieves the Lookupable for the given business object class
	 * 
	 * @param businessObjectClass the class to find the Lookupable for
	 * @return the Lookupable, or null if nothing could be found
	 */
	protected org.kuali.rice.kns.lookup.Lookupable getLookupableForBusinessObject(Class<? extends BusinessObject> businessObjectClass) {
		final BusinessObjectEntry boEntry = getBusinessObjectEntry(businessObjectClass);
		if (boEntry == null) {
			return null;
		}
		
		final String lookupableId = boEntry.getLookupDefinition().getLookupableID();
		return KNSServiceLocator.getLookupable(lookupableId);
	}
	
	/**
	 * Returns the data dictionary defined primary keys for the given BusinessObject
	 * 
	 * @param businessObjectClass the business object to get DataDictionary defined primary keys for
	 * @return the List of primary key property names, or null if nothing could be found
	 */
	protected List<String> getPrimaryKeyFieldsForBusinessObject(Class<? extends BusinessObject> businessObjectClass) {
		final BusinessObjectEntry boEntry = getBusinessObjectEntry(businessObjectClass);
		if (boEntry == null) {
			return null;
		}
		return boEntry.getPrimaryKeys();
	}
	
	/**
	 * Converts a lookup id into a PK field map to use to search in a lookupable
	 * 
	 * @param lookupId the id returned by the lookup
	 * @param businessObjectClass the class of the business object getting the primary key
	 * @return a Map of field names and values which can be profitably used to search for matching business objects
	 */
	protected Map<String, String> convertLookupIdToPKFieldMap(String lookupId, Class<? extends BusinessObject> businessObjectClass) {
		Map<String, String> pkFields = new HashMap<String, String>();
		if (!StringUtils.isBlank(lookupId)) {
			final String[] pkValues = lookupId.split("\\|");
			for (String pkValue : pkValues) {
				if (!StringUtils.isBlank(pkValue)) {
					final String[] pkPieces = pkValue.split("-");
					if (!StringUtils.isBlank(pkPieces[0]) && !StringUtils.isBlank(pkPieces[1])) {
						pkFields.put(pkPieces[0], pkPieces[1]);
					}
				}
			}
		}
		return pkFields;
	}
	
	/**
	 * Converts a Map of PKFields into a String lookup ID
	 * @param pkFieldNames the name of the PK fields, which should be converted to the given lookupId
	 * @param businessObjectClass the class of the business object getting the primary key
	 * @return the String lookup id
	 */
	protected String convertPKFieldMapToLookupId(List<String> pkFieldNames, BusinessObject businessObject) {
		StringBuilder lookupId = new StringBuilder();
		for (String pkFieldName : pkFieldNames) {
			try {
				final Object value = PropertyUtils.getProperty(businessObject, pkFieldName);
				
				if (value != null) {
					lookupId.append(pkFieldName);
					lookupId.append("-");
					final Formatter formatter = retrieveBestFormatter(pkFieldName, businessObject.getClass());
					final String formattedValue = (formatter != null) ? formatter.format(value).toString() : value.toString();
					
					lookupId.append(formattedValue);
				}
				lookupId.append(SearchOperator.OR.op());
			} catch (IllegalAccessException iae) {
				throw new RuntimeException("Could not retrieve pk field value "+pkFieldName+" from business object "+businessObject.getClass().getName(), iae);
			} catch (InvocationTargetException ite) {
				throw new RuntimeException("Could not retrieve pk field value "+pkFieldName+" from business object "+businessObject.getClass().getName(), ite);
			} catch (NoSuchMethodException nsme) {
				throw new RuntimeException("Could not retrieve pk field value "+pkFieldName+" from business object "+businessObject.getClass().getName(), nsme);
			}
		}
		return lookupId.substring(0, lookupId.length() - 1); // kill the last "|"
	}
	
	/**
	 * Like when you're digging through your stuff drawer, you know the one in the kitchen with all the batteries and lint in it, this method
	 * goes through the stuff drawer of KNS formatters and attempts to return you a good one
	 * 
	 * @param propertyName the name of the property to retrieve
	 * @param boClass the class of the BusinessObject the property is on
	 * @return a Formatter, or null if we were unsuccessful in finding
	 */
	protected Formatter retrieveBestFormatter(String propertyName, Class<? extends BusinessObject> boClass) {
		Formatter formatter = null;
		
		try {
			Class<? extends Formatter> formatterClass = null;
			
			final BusinessObjectEntry boEntry = getBusinessObjectEntry(boClass);
			if (boEntry != null) {
				final AttributeDefinition attributeDefinition = boEntry.getAttributeDefinition(propertyName);
				if (attributeDefinition != null && attributeDefinition.hasFormatterClass()) {
					formatterClass = (Class<? extends Formatter>)Class.forName(attributeDefinition.getFormatterClass());
				}
			}
			if (formatterClass == null) {
				final java.lang.reflect.Field propertyField = boClass.getDeclaredField(propertyName);
				if (propertyField != null) {
					formatterClass = Formatter.findFormatter(propertyField.getType());
				}
			}
			
			if (formatterClass != null) {
				formatter = formatterClass.newInstance();
			}
		} catch (SecurityException se) {
			throw new RuntimeException("Could not retrieve good formatter", se);
		} catch (ClassNotFoundException cnfe) {
			throw new RuntimeException("Could not retrieve good formatter", cnfe);
		} catch (NoSuchFieldException nsfe) {
			throw new RuntimeException("Could not retrieve good formatter", nsfe);
		} catch (IllegalAccessException iae) {
			throw new RuntimeException("Could not retrieve good formatter", iae);
		} catch (InstantiationException ie) {
			throw new RuntimeException("Could not retrieve good formatter", ie);
		}
		
		return formatter;
	}
	
	/**
	 * Looks up the DataDictionary BusinessObjectEntry for the given class
	 * 
	 * @param boClass the class of the BusinessObject to find a BusinessObjectEntry for
	 * @return the entry from the data dictionary, or null if nothing was found
	 */
	protected BusinessObjectEntry getBusinessObjectEntry(Class<? extends BusinessObject> boClass) {
		return (BusinessObjectEntry) getDataDictionaryService().getDataDictionary().getBusinessObjectEntry(boClass.getName());
	}

	/**
	 * @return the dataDictionaryService
	 */
	public DataDictionaryService getDataDictionaryService() {
		return this.dataDictionaryService;
	}

	/**
	 * @param dataDictionaryService the dataDictionaryService to set
	 */
	public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
		this.dataDictionaryService = dataDictionaryService;
	}
}
