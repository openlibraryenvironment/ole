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
package org.kuali.rice.krad.service.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.framework.persistence.jpa.metadata.EntityDescriptor;
import org.kuali.rice.core.framework.persistence.jpa.metadata.MetadataManager;
import org.kuali.rice.core.framework.persistence.jpa.metadata.ObjectDescriptor;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.dao.PersistenceDao;
import org.kuali.rice.krad.exception.IntrospectionException;
import org.kuali.rice.krad.exception.ObjectNotABusinessObjectRuntimeException;
import org.kuali.rice.krad.exception.ReferenceAttributeDoesntExistException;
import org.kuali.rice.krad.exception.ReferenceAttributeNotAnOjbReferenceException;
import org.kuali.rice.krad.service.PersistenceService;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;


/**
 * This class is the service implementation for the Persistence structure.
 */
@Transactional
public class PersistenceServiceJpaImpl extends PersistenceServiceImplBase implements PersistenceService {

	private static Logger LOG = Logger.getLogger(PersistenceServiceJpaImpl.class);
    private PersistenceDao persistenceDao;
        
    public void setPersistenceDao(PersistenceDao persistenceDao) {
        this.persistenceDao = persistenceDao;
    }

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#allForeignKeyValuesPopulatedForReference(org.kuali.rice.krad.bo.PersistableBusinessObject, java.lang.String)
	 */
	public boolean allForeignKeyValuesPopulatedForReference(PersistableBusinessObject bo, String referenceName) {
        boolean allFkeysHaveValues = true;

        // yelp if nulls were passed in
        if (bo == null) {
            throw new IllegalArgumentException("The Class passed in for the BusinessObject argument was null.");
        }
        if (StringUtils.isBlank(referenceName)) {
            throw new IllegalArgumentException("The String passed in for the referenceName argument was null or empty.");
        }

        PropertyDescriptor propertyDescriptor = null;

        // make sure the attribute exists at all, throw exception if not
        try {
            propertyDescriptor = PropertyUtils.getPropertyDescriptor(bo, referenceName);
		} catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (propertyDescriptor == null) {
            throw new ReferenceAttributeDoesntExistException("Requested attribute: '" + referenceName + "' does not exist " + "on class: '" + bo.getClass().getName() + "'.");
        }

        // get the class of the attribute name
        Class referenceClass = getBusinessObjectAttributeClass( bo.getClass(), referenceName );
        if ( referenceClass == null ) {
        	referenceClass = propertyDescriptor.getPropertyType();
        }

        // make sure the class of the attribute descends from BusinessObject,
        // otherwise throw an exception
        if (!PersistableBusinessObject.class.isAssignableFrom(referenceClass)) {
			throw new ObjectNotABusinessObjectRuntimeException("Attribute requested (" + referenceName + ") is of class: " + "'" + referenceClass.getName() + "' and is not a " + "descendent of BusinessObject.  Only descendents of BusinessObject " + "can be used.");
        }

        EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(bo.getClass());
    	ObjectDescriptor objectDescriptor = descriptor.getObjectDescriptorByName(referenceName);
        if (objectDescriptor == null) {
            throw new ReferenceAttributeNotAnOjbReferenceException("Attribute requested (" + referenceName + ") is not listed " + "in OJB as a reference-descriptor for class: '" + bo.getClass().getName() + "'");
        }

		// get the list of the foreign-keys for this reference-descriptor
        List fkFields = objectDescriptor.getForeignKeyFields();
        Iterator fkIterator = fkFields.iterator();

        // walk through the list of the foreign keys, get their types
        while (fkIterator.hasNext()) {

            // get the field name of the fk & pk field
            String fkFieldName = (String) fkIterator.next();

            // get the value for the fk field
            Object fkFieldValue = null;
            try {
                fkFieldValue = PropertyUtils.getSimpleProperty(bo, fkFieldName);
            }

            // if we cant retrieve the field value, then
            // it doesnt have a value
            catch (IllegalAccessException e) {
                return false;
			} catch (InvocationTargetException e) {
                return false;
			} catch (NoSuchMethodException e) {
                return false;
            }

            // test the value
            if (fkFieldValue == null) {
                return false;
			} else if (String.class.isAssignableFrom(fkFieldValue.getClass())) {
                if (StringUtils.isBlank((String) fkFieldValue)) {
                    return false;
                }
            }
        }
        
        return allFkeysHaveValues;
    }

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#clearCache()
	 */
	public void clearCache() {
		persistenceDao.clearCache();
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#getFlattenedPrimaryKeyFieldValues(java.lang.Object)
	 */
	public String getFlattenedPrimaryKeyFieldValues(Object persistableObject) {
        if (persistableObject == null) {
            throw new IllegalArgumentException("invalid (null) persistableObject");
        }
        
        Map primaryKeyValues = getPrimaryKeyFieldValues(persistableObject, true);

        StringBuffer flattened = new StringBuffer(persistableObject.getClass().getName());
        flattened.append("(");
        for (Iterator i = primaryKeyValues.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();

            String fieldName = (String) e.getKey();
            Object fieldValue = e.getValue();

            flattened.append(fieldName + "=" + fieldValue);
            if (i.hasNext()) {
                flattened.append(",");
            }
        }

        flattened.append(")");

        return flattened.toString();
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#linkObjects(java.lang.Object)
	 */
	public void linkObjects(Object persistableObject) {
		linkObjectsWithCircularReferenceCheck(persistableObject, new HashSet());
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#loadRepositoryDescriptor(java.lang.String)
	 */
	public void loadRepositoryDescriptor(String ojbRepositoryFilePath) {}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#refreshAllNonUpdatingReferences(org.kuali.rice.krad.bo.PersistableBusinessObject)
	 */
	public void refreshAllNonUpdatingReferences(PersistableBusinessObject bo) {
		EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(bo.getClass());
		List<ObjectDescriptor> objectDescriptors = descriptor.getObjectRelationships();
		
		for (ObjectDescriptor od : objectDescriptors) {
            if (od.getCascade().length == 0) {           	
                retrieveReferenceObject(bo, od.getAttributeName());
            }			
		}
    }

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#resolveProxy(java.lang.Object)
	 */
	public Object resolveProxy(Object o) {
		return persistenceDao.resolveProxy(o);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#retrieveNonKeyFields(java.lang.Object)
	 */
	public void retrieveNonKeyFields(Object persistableObject) {
        if (persistableObject == null) {
            throw new IllegalArgumentException("invalid (null) persistableObject");
        }
        LOG.debug("retrieving non-key fields for " + persistableObject);

        persistenceDao.retrieveAllReferences(persistableObject);
    }

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#retrieveReferenceObject(java.lang.Object, java.lang.String)
	 */
	public void retrieveReferenceObject(Object persistableObject, String referenceObjectName) {
        if (persistableObject == null) {
            throw new IllegalArgumentException("invalid (null) persistableObject");
        }
        LOG.debug("retrieving reference object " + referenceObjectName + " for " + persistableObject);

        persistenceDao.retrieveReference(persistableObject, referenceObjectName);
    }

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#retrieveReferenceObjects(java.lang.Object, java.util.List)
	 */
	public void retrieveReferenceObjects(Object persistableObject, List referenceObjectNames) {
        if (persistableObject == null) {
            throw new IllegalArgumentException("invalid (null) persistableObject");
        }
        if (referenceObjectNames == null) {
            throw new IllegalArgumentException("invalid (null) referenceObjectNames");
        }
        if (referenceObjectNames.isEmpty()) {
            throw new IllegalArgumentException("invalid (empty) referenceObjectNames");
        }

        int index = 0;
        for (Iterator i = referenceObjectNames.iterator(); i.hasNext(); index++) {
            String referenceObjectName = (String) i.next();
            if (StringUtils.isBlank(referenceObjectName)) {
                throw new IllegalArgumentException("invalid (blank) name at position " + index);
            }

            retrieveReferenceObject(persistableObject, referenceObjectName);
        }
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#retrieveReferenceObjects(java.util.List, java.util.List)
	 */
	public void retrieveReferenceObjects(List persistableObjects, List referenceObjectNames) {
        if (persistableObjects == null) {
            throw new IllegalArgumentException("invalid (null) persistableObjects");
        }
        if (persistableObjects.isEmpty()) {
            throw new IllegalArgumentException("invalid (empty) persistableObjects");
        }
        if (referenceObjectNames == null) {
            throw new IllegalArgumentException("invalid (null) referenceObjectNames");
        }
        if (referenceObjectNames.isEmpty()) {
            throw new IllegalArgumentException("invalid (empty) referenceObjectNames");
        }

        for (Iterator i = persistableObjects.iterator(); i.hasNext();) {
            Object persistableObject = i.next();
            retrieveReferenceObjects(persistableObject, referenceObjectNames);
        }
	}
    
    private void linkObjectsWithCircularReferenceCheck(Object persistableObject, Set referenceSet) {
        if (ObjectUtils.isNull(persistableObject) || referenceSet.contains(persistableObject)) {
            return;
        }
        
        referenceSet.add(persistableObject);
        
        EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(persistableObject.getClass());

        String className = null;
        String fieldName = null;
        try {
            // iterate through all object references for the persistableObject
        	List<ObjectDescriptor> objectDescriptors = descriptor.getObjectRelationships();
        	for (ObjectDescriptor od : objectDescriptors) {
                // get the actual reference object
                className = persistableObject.getClass().getName();
                fieldName = od.getAttributeName();
                Object referenceObject = PropertyUtils.getProperty(persistableObject, fieldName);
                if (ObjectUtils.isNull(referenceObject) || referenceSet.contains(referenceObject)) {
                    continue;
                }

                // recursively link object
                linkObjectsWithCircularReferenceCheck(referenceObject, referenceSet);

				// iterate through the keys for the reference object and set value
                List<String> refFkNames = od.getForeignKeyFields();
                EntityDescriptor refCld = MetadataManager.getEntityDescriptor(od.getTargetEntity());
                Set<org.kuali.rice.core.framework.persistence.jpa.metadata.FieldDescriptor> refPkNames = refCld.getPrimaryKeys();

                try {
	                for (String fk : refFkNames) {
						Field f = referenceObject.getClass().getDeclaredField(fk);
						f.setAccessible(true);
	                	if (ObjectUtils.isNull(f.get(referenceObject))) {
							Field f2 = persistableObject.getClass().getDeclaredField(fk);
							f2.setAccessible(true);
							f.set(referenceObject, f2.get(persistableObject));
						}	                	
					}
                } catch (Exception e) {
                	LOG.error(e.getMessage(), e);
                }
            }
		} catch (NoSuchMethodException e) {
            throw new IntrospectionException("no setter for property '" + className + "." + fieldName + "'", e);
		} catch (IllegalAccessException e) {
            throw new IntrospectionException("problem accessing property '" + className + "." + fieldName + "'", e);
		} catch (InvocationTargetException e) {
            throw new IntrospectionException("problem invoking getter for property '" + className + "." + fieldName + "'", e);
        }
    }
    
    /**
	 * Asks persistenceDao if this represents a proxy
	 * 
	 * @see org.kuali.rice.krad.service.PersistenceService#isProxied(java.lang.Object)
	 */
	public boolean isProxied(Object object) {
		return persistenceDao.isProxied(object);
	}
}
