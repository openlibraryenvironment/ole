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
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.DataObjectRelationship;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.dao.BusinessObjectDao;
import org.kuali.rice.krad.exception.ObjectNotABusinessObjectRuntimeException;
import org.kuali.rice.krad.exception.ReferenceAttributeDoesntExistException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DataObjectMetaDataService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.service.PersistenceService;
import org.kuali.rice.krad.service.PersistenceStructureService;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is the service implementation for the BusinessObjectService structure. This is the default implementation, that is
 * delivered with Kuali.
 */

public class BusinessObjectServiceImpl implements BusinessObjectService {

    private PersistenceService persistenceService;
    private PersistenceStructureService persistenceStructureService;
    private BusinessObjectDao businessObjectDao;
    private PersonService personService;
    private DataObjectMetaDataService dataObjectMetaDataService;

    private boolean illegalBusinessObjectsForSaveInitialized;
    private final Set<String> illegalBusinessObjectsForSave = new HashSet<String>();
    
    @Override
    @Transactional
    public <T extends PersistableBusinessObject> T save(T bo) {
    	validateBusinessObjectForSave(bo);
        return (T) businessObjectDao.save(bo);
    }

    @Override
    @Transactional
    public List<? extends PersistableBusinessObject> save(List<? extends PersistableBusinessObject> businessObjects) {
        validateBusinessObjectForSave(businessObjects);
        return businessObjectDao.save(businessObjects);
    }

    @Override
    @Transactional
    public PersistableBusinessObject linkAndSave(PersistableBusinessObject bo) {
    	validateBusinessObjectForSave(bo);
        persistenceService.linkObjects(bo);
        return businessObjectDao.save(bo);
    }

    @Override
    @Transactional
    public List<? extends PersistableBusinessObject> linkAndSave(List<? extends PersistableBusinessObject> businessObjects) {
        validateBusinessObjectForSave(businessObjects);
        return businessObjectDao.save(businessObjects);
    }

    protected void validateBusinessObjectForSave(PersistableBusinessObject bo) {
    	if (bo == null) {
            throw new IllegalArgumentException("Object passed in is null");
        }
        if (!isBusinessObjectAllowedForSave(bo)) {
        	throw new IllegalArgumentException("Object passed in is a BusinessObject but has been restricted from save operations according to configuration parameter '" + KRADConstants.Config.ILLEGAL_BUSINESS_OBJECTS_FOR_SAVE);
        }
    }
    
    protected void validateBusinessObjectForSave(List<? extends PersistableBusinessObject> businessObjects) {
    	for (PersistableBusinessObject bo : businessObjects) {
    		 if (bo == null) {
                 throw new IllegalArgumentException("One of the objects in the List is null.");
             }
    		 if (!isBusinessObjectAllowedForSave(bo)) {
    			 throw new IllegalArgumentException("One of the objects in the List is a BusinessObject but has been restricted from save operations according to configuration parameter '" + KRADConstants.Config.ILLEGAL_BUSINESS_OBJECTS_FOR_SAVE
    					 + "  Passed in type was '" + bo.getClass().getName() + "'.");
    		 }
    	}
    }
    
    
    /**
     * Returns true if the BusinessObjectService should be permitted to save instances of the given PersistableBusinessObject.
     * Implementation checks a configuration parameter for class names of PersistableBusinessObjects that shouldn't be allowed
     * to be saved.
     */
    protected boolean isBusinessObjectAllowedForSave(PersistableBusinessObject bo) {
    	if (!illegalBusinessObjectsForSaveInitialized) {
    		synchronized (this) {
    			boolean applyCheck = true;
    			String applyCheckValue = ConfigContext.getCurrentContextConfig().getProperty(KRADConstants.Config.APPLY_ILLEGAL_BUSINESS_OBJECT_FOR_SAVE_CHECK);
    			if (!StringUtils.isEmpty(applyCheckValue)) {
    				applyCheck = Boolean.valueOf(applyCheckValue);
    			}
    			if (applyCheck) {
    				String illegalBos = ConfigContext.getCurrentContextConfig().getProperty(KRADConstants.Config.ILLEGAL_BUSINESS_OBJECTS_FOR_SAVE);
    				if (!StringUtils.isEmpty(illegalBos)) {
    					String[] illegalBosSplit = illegalBos.split(",");
    					for (String illegalBo : illegalBosSplit) {
    						illegalBusinessObjectsForSave.add(illegalBo.trim());
    					}
    				}
    			}
    		}
    		illegalBusinessObjectsForSaveInitialized = true;
    	}
    	return !illegalBusinessObjectsForSave.contains(bo.getClass().getName());
    }
    

    @Override
	public <T extends BusinessObject> T findBySinglePrimaryKey(Class<T> clazz, Object primaryKey) {
		return businessObjectDao.findBySinglePrimaryKey(clazz, primaryKey);
	}
    @Override
    public <T extends BusinessObject> T findByPrimaryKey(Class<T> clazz, Map<String, ?> primaryKeys) {
        return businessObjectDao.findByPrimaryKey(clazz, primaryKeys);
    }

    @Override
    public PersistableBusinessObject retrieve(PersistableBusinessObject object) {
        return businessObjectDao.retrieve(object);
    }

    @Override
    public <T extends BusinessObject> Collection<T> findAll(Class<T> clazz) {
        return businessObjectDao.findAll(clazz);
    }
    @Override
    public <T extends BusinessObject> Collection<T> findAllOrderBy( Class<T> clazz, String sortField, boolean sortAscending ) {
    	final Map<String, ?> emptyParameters = Collections.emptyMap();
    	return businessObjectDao.findMatchingOrderBy(clazz, emptyParameters, sortField, sortAscending );
    }
    
    @Override
    public <T extends BusinessObject> Collection<T> findMatching(Class<T> clazz, Map<String, ?> fieldValues) {
        return businessObjectDao.findMatching(clazz, fieldValues);
    }

    @Override
    public int countMatching(Class clazz, Map<String, ?> fieldValues) {
        return businessObjectDao.countMatching(clazz, fieldValues);
    }

    @Override
    public int countMatching(Class clazz, Map<String, ?> positiveFieldValues, Map<String, ?> negativeFieldValues) {
        return businessObjectDao.countMatching(clazz, positiveFieldValues, negativeFieldValues);
    }
    @Override
    public <T extends BusinessObject> Collection<T> findMatchingOrderBy(Class<T> clazz, Map<String, ?> fieldValues, String sortField, boolean sortAscending) {
        return businessObjectDao.findMatchingOrderBy(clazz, fieldValues, sortField, sortAscending);
    }
    @Override
    @Transactional
    public void delete(PersistableBusinessObject bo) {
        businessObjectDao.delete(bo);
    }

    @Override
    @Transactional
    public void delete(List<? extends PersistableBusinessObject> boList) {
        businessObjectDao.delete(boList);
    }

    @Override
    @Transactional
    public void deleteMatching(Class clazz, Map<String, ?> fieldValues) {
        businessObjectDao.deleteMatching(clazz, fieldValues);
    }

    @Override
    public BusinessObject getReferenceIfExists(BusinessObject bo, String referenceName) {
        // if either argument is null, then we have nothing to do, complain and abort
        if (ObjectUtils.isNull(bo)) {
            throw new IllegalArgumentException("Passed in BusinessObject was null.  No processing can be done.");
        }
        if (StringUtils.isEmpty(referenceName)) {
            throw new IllegalArgumentException("Passed in referenceName was empty or null.  No processing can be done.");
        }

        // make sure the attribute exists at all, throw exception if not
        PropertyDescriptor propertyDescriptor;
        try {
            propertyDescriptor = PropertyUtils.getPropertyDescriptor(bo, referenceName);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (propertyDescriptor == null) {
            throw new ReferenceAttributeDoesntExistException("Requested attribute: '" + referenceName + "' does not exist " + "on class: '" + bo.getClass().getName() + "'. GFK");
        }

        // get the class of the attribute name
        Class referenceClass = null;
        if(bo instanceof PersistableBusinessObject) {
            referenceClass = persistenceStructureService.getBusinessObjectAttributeClass(((PersistableBusinessObject)bo).getClass(), referenceName);
        }
        if(referenceClass == null) {
            referenceClass = ObjectUtils.getPropertyType( bo, referenceName, persistenceStructureService );
        }
        if ( referenceClass == null ) {
        	referenceClass = propertyDescriptor.getPropertyType();
        }

        /*
         * check for Person or EBO references in which case we can just get the reference through propertyutils
         */
        if (ExternalizableBusinessObject.class.isAssignableFrom(referenceClass)) {
            try {
            	BusinessObject referenceBoExternalizable = (BusinessObject) PropertyUtils.getProperty(bo, referenceName);
            	if (referenceBoExternalizable!=null) {
            		return referenceBoExternalizable;
                }
            } catch (Exception ex) {
                //throw new RuntimeException("Unable to get property " + referenceName + " from a BO of class: " + bo.getClass().getName(),ex);
            	//Proceed further - get the BO relationship using responsible module service and proceed further
            }
        }

        // make sure the class of the attribute descends from BusinessObject,
        // otherwise throw an exception
        if (!ExternalizableBusinessObject.class.isAssignableFrom(referenceClass) && !PersistableBusinessObject.class.isAssignableFrom(referenceClass)) {
            throw new ObjectNotABusinessObjectRuntimeException("Attribute requested (" + referenceName + ") is of class: " + "'" + referenceClass.getName() + "' and is not a " + "descendent of PersistableBusinessObject.  Only descendents of PersistableBusinessObject " + "can be used.");
        }

        // get the list of foreign-keys for this reference. if the reference
        // does not exist, or is not a reference-descriptor, an exception will
        // be thrown here.
        //DataObjectRelationship boRel = dataObjectMetaDataService.getBusinessObjectRelationship( bo, referenceName );
        DataObjectRelationship boRel = dataObjectMetaDataService.getDataObjectRelationship(bo, bo.getClass(),
                referenceName, "", true, false, false);
        final Map<String,String> fkMap = boRel != null ? boRel.getParentToChildReferences() : Collections.<String, String>emptyMap();

        boolean allFkeysHaveValues = true;
        // walk through the foreign keys, testing each one to see if it has a value
        Map<String,Object> pkMap = new HashMap<String,Object>();
        for (Map.Entry<String, String> entry : fkMap.entrySet()) {
            String fkFieldName = entry.getKey();
            String pkFieldName = entry.getValue();

            // attempt to retrieve the value for the given field
            Object fkFieldValue;
            try {
                fkFieldValue = PropertyUtils.getProperty(bo, fkFieldName);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

            // determine if there is a value for the field
            if (ObjectUtils.isNull(fkFieldValue)) {
                allFkeysHaveValues = false;
                break; // no reason to continue processing the fkeys
            }
            else if (String.class.isAssignableFrom(fkFieldValue.getClass())) {
                if (StringUtils.isEmpty((String) fkFieldValue)) {
                    allFkeysHaveValues = false;
                    break;
                }
                else {
                    pkMap.put(pkFieldName, fkFieldValue);
                }
            }

            // if there is a value, grab it
            else {
                pkMap.put(pkFieldName, fkFieldValue);
            }
        }

        BusinessObject referenceBo = null;
        // only do the retrieval if all Foreign Keys have values
        if (allFkeysHaveValues) {
        	if (ExternalizableBusinessObject.class.isAssignableFrom(referenceClass)) {
        		ModuleService responsibleModuleService = KRADServiceLocatorWeb.getKualiModuleService().getResponsibleModuleService(referenceClass);
				if(responsibleModuleService!=null) {
					return responsibleModuleService.<ExternalizableBusinessObject>getExternalizableBusinessObject(referenceClass, pkMap);
				}
        	} else
        		referenceBo = this.<BusinessObject>findByPrimaryKey(referenceClass, pkMap);
        }

        // return what we have, it'll be null if it was never retrieved
        return referenceBo;
    }
    @Override
    public void linkUserFields(PersistableBusinessObject bo) {
        if (bo == null) {
            throw new IllegalArgumentException("bo passed in was null");
        }

        bo.linkEditableUserFields();
       
        linkUserFields( Collections.singletonList( bo ) );
    }

    @Override
    public void linkUserFields(List<PersistableBusinessObject> bos) {

        // do nothing if there's nothing to process
        if (bos == null) {
            throw new IllegalArgumentException("List of bos passed in was null");
        }
        else if (bos.isEmpty()) {
            return;
        }


        Person person;
        for (PersistableBusinessObject bo : bos) {
            // get a list of the reference objects on the BO
            List<DataObjectRelationship> relationships = dataObjectMetaDataService.getDataObjectRelationships(
                    bo.getClass());
            for ( DataObjectRelationship rel : relationships ) {
                if ( Person.class.isAssignableFrom( rel.getRelatedClass() ) ) {
                    person = (Person) ObjectUtils.getPropertyValue(bo, rel.getParentAttributeName() );
                    if (person != null) {
                        // find the universal user ID relationship and link the field
                        for ( Map.Entry<String,String> entry : rel.getParentToChildReferences().entrySet() ) {
                            if ( "principalId".equals(entry.getValue())) {
                                linkUserReference(bo, person, rel.getParentAttributeName(), entry.getKey() );
                                break;
                            }
                        }
                    }                    
                }
            }
            if ( persistenceStructureService.isPersistable(bo.getClass())) {
	            Map<String, Class> references = persistenceStructureService.listReferenceObjectFields(bo);

	            // walk through the ref objects, only doing work if they are Person objects
	            for ( Map.Entry<String, Class> entry : references.entrySet() ) {
	                if (Person.class.isAssignableFrom(entry.getValue())) {
	                    person = (Person) ObjectUtils.getPropertyValue(bo, entry.getKey());
	                    if (person != null) {
	                        String fkFieldName = persistenceStructureService.getForeignKeyFieldName(bo.getClass(), entry.getKey(), "principalId");
	                        linkUserReference(bo, person, entry.getKey(), fkFieldName);
	                    }
	                }
	            }
            }
        }
    }

    /**
     * 
     * This method links a single UniveralUser back to the parent BO based on the authoritative principalName.
     * 
     * @param bo
     * @param refFieldName
     */
    private void linkUserReference(PersistableBusinessObject bo, Person user, String refFieldName, String fkFieldName) {

        // if the UserId field is blank, there's nothing we can do, so quit
        if (StringUtils.isBlank(user.getPrincipalName())) {
            return;
        }

        // attempt to load the user from the user-name, exit quietly if the user isnt found
        Person userFromService = getPersonService().getPersonByPrincipalName(user.getPrincipalName());
        if (userFromService == null) {
            return;
        }

        // attempt to set the universalId on the parent BO
        setBoField(bo, fkFieldName, userFromService.getPrincipalId());
    }

    private void setBoField(PersistableBusinessObject bo, String fieldName, Object fieldValue) {
        try {
            ObjectUtils.setObjectProperty(bo, fieldName, fieldValue.getClass(), fieldValue);
        }
        catch (Exception e) {
            throw new RuntimeException("Could not set field [" + fieldName + "] on BO to value: " + fieldValue.toString() + " (see nested exception for details).", e);
        }
    }

    @Override
	public PersistableBusinessObject manageReadOnly(PersistableBusinessObject bo) {
		return getBusinessObjectDao().manageReadOnly(bo);
	}

	/**
     * Gets the businessObjectDao attribute.
     * 
     * @return Returns the businessObjectDao.
     */
    protected BusinessObjectDao getBusinessObjectDao() {
        return businessObjectDao;
    }

    /**
     * Sets the businessObjectDao attribute value.
     * 
     * @param businessObjectDao The businessObjectDao to set.
     */
    public void setBusinessObjectDao(BusinessObjectDao businessObjectDao) {
        this.businessObjectDao = businessObjectDao;
    }

    /**
     * Sets the persistenceStructureService attribute value.
     * 
     * @param persistenceStructureService The persistenceStructureService to set.
     */
    public void setPersistenceStructureService(PersistenceStructureService persistenceStructureService) {
        this.persistenceStructureService = persistenceStructureService;
    }

    /**
     * Sets the kualiUserService attribute value.
     */
	public final void setPersonService(PersonService personService) {
        this.personService = personService;
    }

	protected PersonService getPersonService() {
        return personService != null ? personService : (personService = KimApiServiceLocator.getPersonService());
    }

    /**
     * Sets the persistenceService attribute value.
     * 
     * @param persistenceService The persistenceService to set.
     */
    public final void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    protected DataObjectMetaDataService getDataObjectMetaDataService() {
        return dataObjectMetaDataService;
    }

    public void setDataObjectMetaDataService(DataObjectMetaDataService dataObjectMetadataService) {
        this.dataObjectMetaDataService = dataObjectMetadataService;
    }

}
