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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.ConnectionRepository;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;
import org.apache.ojb.broker.metadata.ObjectReferenceDescriptor;
import org.apache.ojb.broker.metadata.fieldaccess.PersistentField;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.dao.PersistenceDao;
import org.kuali.rice.krad.exception.IntrospectionException;
import org.kuali.rice.krad.exception.ObjectNotABusinessObjectRuntimeException;
import org.kuali.rice.krad.exception.ReferenceAttributeDoesntExistException;
import org.kuali.rice.krad.exception.ReferenceAttributeNotAnOjbReferenceException;
import org.kuali.rice.krad.service.PersistenceService;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * This class is the service implementation for the Persistence structure.
 * OjbRepositoryExplorer provides functions for extracting information from the
 * OJB repository at runtime. This is the default implementation, that is
 * delivered with Kuali.
 */
@Transactional
public class PersistenceServiceOjbImpl extends PersistenceServiceImplBase implements PersistenceService {
    private static Logger LOG = Logger.getLogger(PersistenceServiceOjbImpl.class);
    private static final String CLASSPATH_RESOURCE_PREFIX = "classpath:";
    private PersistenceDao persistenceDao;
    
    public void clearCache() {
        persistenceDao.clearCache();
    }
    
    public Object resolveProxy(Object o) {
        return persistenceDao.resolveProxy(o);
    }

	public void loadRepositoryDescriptor(String ojbRepositoryFilePath) {
		if ( LOG.isInfoEnabled() ) {
			LOG.info("Begin loading OJB Metadata for: " + ojbRepositoryFilePath);
		}
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader(ClassLoaderUtils.getDefaultClassLoader());
		InputStream is = null;
		try {
			is = resourceLoader.getResource(CLASSPATH_RESOURCE_PREFIX + ojbRepositoryFilePath).getInputStream();
			ConnectionRepository cr = MetadataManager.getInstance().readConnectionRepository(is);
			MetadataManager.getInstance().mergeConnectionRepository(cr);

			is = resourceLoader.getResource(CLASSPATH_RESOURCE_PREFIX + ojbRepositoryFilePath).getInputStream();
			DescriptorRepository dr = MetadataManager.getInstance().readDescriptorRepository(is);
			MetadataManager.getInstance().mergeDescriptorRepository(dr);

			if (LOG.isDebugEnabled()) {
				LOG.debug("--------------------------------------------------------------------------");
				LOG.debug("Merging repository descriptor: " + ojbRepositoryFilePath);
				LOG.debug("--------------------------------------------------------------------------");
			}
		} catch (IOException ioe) {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.warn("Failed to close InputStream on OJB repository path " + ojbRepositoryFilePath, e);
				}
			}
			throw new RiceRuntimeException(ioe);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.warn("Failed to close InputStream on OJB repository path " + ojbRepositoryFilePath, e);
				}
			}
		}
		if ( LOG.isInfoEnabled() ) {
			LOG.info("Finished loading OJB Metadata for: " + ojbRepositoryFilePath);
		}
	}

    /**
	 * @see org.kuali.rice.krad.service.PersistenceService#retrieveNonKeyFields(java.lang.Object)
	 */
    public void retrieveNonKeyFields(Object persistableObject) {
        if (persistableObject == null) {
            throw new IllegalArgumentException("invalid (null) persistableObject");
        }
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("retrieving non-key fields for " + persistableObject);
        }

        persistenceDao.retrieveAllReferences(persistableObject);
    }

    /**
	 * @see org.kuali.rice.krad.service.PersistenceService#retrieveReferenceObject(java.lang.Object,
	 *      String referenceObjectName)
     */
    public void retrieveReferenceObject(Object persistableObject, String referenceObjectName) {
        if (persistableObject == null) {
            throw new IllegalArgumentException("invalid (null) persistableObject");
        }
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("retrieving reference object " + referenceObjectName + " for " + persistableObject);
        }
        persistenceDao.retrieveReference(persistableObject, referenceObjectName);
    }

    /**
	 * @see org.kuali.rice.krad.service.PersistenceService#retrieveReferenceObject(java.lang.Object,
	 *      String referenceObjectName)
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
	 * @see org.kuali.rice.krad.service.PersistenceService#retrieveReferenceObject(java.lang.Object,
	 *      String referenceObjectName)
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

    private void linkObjectsWithCircularReferenceCheck(Object persistableObject, Set referenceSet) {
        if (ObjectUtils.isNull(persistableObject) || referenceSet.contains(persistableObject)) {
            return;
        }
        referenceSet.add(persistableObject);
        ClassDescriptor classDescriptor = getClassDescriptor(persistableObject.getClass());

        String className = null;
        String fieldName = null;
        try {
            // iterate through all object references for the persistableObject
            Vector objectReferences = classDescriptor.getObjectReferenceDescriptors();
            for (Iterator iter = objectReferences.iterator(); iter.hasNext();) {
                ObjectReferenceDescriptor referenceDescriptor = (ObjectReferenceDescriptor) iter.next();

                // get the actual reference object
                className = persistableObject.getClass().getName();
                fieldName = referenceDescriptor.getAttributeName();
                Object referenceObject = PropertyUtils.getProperty(persistableObject, fieldName);
                if (ObjectUtils.isNull(referenceObject) || referenceSet.contains(referenceObject)) {
                    continue;
                }

                // recursively link object
                linkObjectsWithCircularReferenceCheck(referenceObject, referenceSet);

				// iterate through the keys for the reference object and set
				// value
                FieldDescriptor[] refFkNames = referenceDescriptor.getForeignKeyFieldDescriptors(classDescriptor);
                ClassDescriptor refCld = getClassDescriptor(referenceDescriptor.getItemClass());
                FieldDescriptor[] refPkNames = refCld.getPkFields();

                Map objFkValues = new HashMap();
                for (int i = 0; i < refPkNames.length; i++) {
                    objFkValues.put(refFkNames[i].getAttributeName(), ObjectUtils.getPropertyValue(referenceObject, refPkNames[i].getAttributeName()));
                }

                for (int i = 0; i < refFkNames.length; i++) {
                    FieldDescriptor fkField = refFkNames[i];
                    String fkName = fkField.getAttributeName();

					// if the fk from object and use if main object does not
					// have value
                    Object fkValue = null;
                    if (objFkValues.containsKey(fkName)) {
                        fkValue = objFkValues.get(fkName);
                    }

                    // if fk is set in main object, take value from there
                    Object mainFkValue = ObjectUtils.getPropertyValue(persistableObject, fkName);
                    if (ObjectUtils.isNotNull(mainFkValue) && StringUtils.isNotBlank(mainFkValue.toString())) {
                        fkValue = mainFkValue;
					} else if (ObjectUtils.isNull(fkValue) || StringUtils.isBlank(fkValue.toString())) {
						// find the value from one of the other reference
						// objects
                        for (Iterator iter2 = objectReferences.iterator(); iter2.hasNext();) {
                            ObjectReferenceDescriptor checkDescriptor = (ObjectReferenceDescriptor) iter2.next();

                            fkValue = getReferenceFKValue(persistableObject, checkDescriptor, fkName);
                            if (ObjectUtils.isNotNull(fkValue) && StringUtils.isNotBlank(fkValue.toString())) {
                                break;
                            }
                        }
                    }

                    // set the fk value
                    if (ObjectUtils.isNotNull(fkValue)) {
                        fieldName = refPkNames[i].getAttributeName();
                        ObjectUtils.setObjectProperty(referenceObject, fieldName, fkValue.getClass(), fkValue);

                        // set fk in main object
                        if (ObjectUtils.isNull(mainFkValue)) {
                            ObjectUtils.setObjectProperty(persistableObject, fkName, fkValue.getClass(), fkValue);
                        }
                    }
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
	 * For each reference object to the parent persistableObject, sets the key
	 * values for that object. First, if the reference object already has a
	 * value for the key, the value is left unchanged. Otherwise, for
	 * non-anonymous keys, the value is taken from the parent object. For
	 * anonymous keys, all other persistableObjects are checked until a value
	 * for the key is found.
     * 
     * @see org.kuali.rice.krad.service.PersistenceService#getReferencedObject(java.lang.Object,
     *      org.apache.ojb.broker.metadata.ObjectReferenceDescriptor)
     */
    public void linkObjects(Object persistableObject) {
        linkObjectsWithCircularReferenceCheck(persistableObject, new HashSet());
    }

    /**
     * 
     * @see org.kuali.rice.krad.service.PersistenceService#allForeignKeyValuesPopulatedForReference(org.kuali.rice.krad.bo.BusinessObject,
     *      java.lang.String)
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
			throw new ObjectNotABusinessObjectRuntimeException("Attribute requested (" + referenceName + ") is of class: " + "'" + referenceClass.getName() + "' and is not a " + "descendent of BusinessObject.  Only descendents of BusinessObject "
					+ "can be used.");
        }

		// make sure the attribute designated is listed as a
		// reference-descriptor
		// on the clazz specified, otherwise throw an exception (OJB);
        ClassDescriptor classDescriptor = getClassDescriptor(bo.getClass());
        ObjectReferenceDescriptor referenceDescriptor = classDescriptor.getObjectReferenceDescriptorByName(referenceName);
        if (referenceDescriptor == null) {
            throw new ReferenceAttributeNotAnOjbReferenceException("Attribute requested (" + referenceName + ") is not listed " + "in OJB as a reference-descriptor for class: '" + bo.getClass().getName() + "'");
        }

		// get the list of the foreign-keys for this reference-descriptor
		// (OJB)
        Vector fkFields = referenceDescriptor.getForeignKeyFields();
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
     * 
     * @see org.kuali.rice.krad.service.PersistenceService#refreshAllNonUpdatingReferences(org.kuali.rice.krad.bo.BusinessObject)
     */
    public void refreshAllNonUpdatingReferences(PersistableBusinessObject bo) {

        // get the OJB class-descriptor for the bo class
        ClassDescriptor classDescriptor = getClassDescriptor(bo.getClass());

        // get a list of all reference-descriptors for that class
        Vector references = classDescriptor.getObjectReferenceDescriptors();

        // walk through all of the reference-descriptors
        for (Iterator iter = references.iterator(); iter.hasNext();) {
            ObjectReferenceDescriptor reference = (ObjectReferenceDescriptor) iter.next();

            // if its NOT an updateable reference, then lets refresh it
            if (reference.getCascadingStore() == ObjectReferenceDescriptor.CASCADE_NONE) {
                PersistentField persistentField = reference.getPersistentField();
                String referenceName = persistentField.getName();
                retrieveReferenceObject(bo, referenceName);
            }
        }
    }

    private Object getReferenceFKValue(Object persistableObject, ObjectReferenceDescriptor chkRefCld, String fkName) {
        ClassDescriptor classDescriptor = getClassDescriptor(persistableObject.getClass());
        Object referenceObject = ObjectUtils.getPropertyValue(persistableObject, chkRefCld.getAttributeName());

        if (referenceObject == null) {
            return null;
        }

        FieldDescriptor[] refFkNames = chkRefCld.getForeignKeyFieldDescriptors(classDescriptor);
        ClassDescriptor refCld = getClassDescriptor(chkRefCld.getItemClass());
        FieldDescriptor[] refPkNames = refCld.getPkFields();


        Object fkValue = null;
        for (int i = 0; i < refFkNames.length; i++) {
            FieldDescriptor fkField = refFkNames[i];

            if (fkField.getAttributeName().equals(fkName)) {
                fkValue = ObjectUtils.getPropertyValue(referenceObject, refPkNames[i].getAttributeName());
                break;
            }
        }

        return fkValue;
    }
    
    /**
	 * Asks persistenceDao if this represents a proxy
	 * 
	 * @see org.kuali.rice.krad.service.PersistenceService#isProxied(java.lang.Object)
	 */
	public boolean isProxied(Object object) {
		return persistenceDao.isProxied(object);
	}

	/**
     * Sets the persistenceDao attribute value.
	 * 
	 * @param persistenceDao
	 *            The persistenceDao to set.
     */
    public void setPersistenceDao(PersistenceDao persistenceDao) {
        this.persistenceDao = persistenceDao;
    }
}
