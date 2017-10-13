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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.rice.krad.bo.DataObjectRelationship;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.service.PersistenceStructureService;
import org.kuali.rice.krad.util.ForeignKeyFieldsPopulationState;

/**
 * This class is now a proxy, which uses the @Entity annotation to decide whether to use the JPA or OJB underlying services to perform an action.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class PersistenceStructureServiceImpl extends PersistenceServiceImplBase implements PersistenceStructureService {

	/**
	 * 
	 * special case when the attributeClass passed in doesnt match the class of
	 * the reference-descriptor as defined in ojb-repository. Currently the only
	 * case of this happening is ObjectCode vs. ObjectCodeCurrent.
	 * 
	 * NOTE: This method makes no real sense and is a product of a hack
	 * introduced by KFS for an unknown reason. If you find yourself using this
	 * map stop and go do something else.
	 * 
	 * @param from
	 *            the class in the code
	 * @param to
	 *            the class in the repository
	 */
	public static Map<Class, Class> referenceConversionMap = new HashMap<Class, Class>();
	
	private PersistenceStructureService persistenceStructureServiceJpa;
	private PersistenceStructureService persistenceStructureServiceOjb;

	public void setPersistenceStructureServiceJpa(PersistenceStructureService persistenceStructureServiceJpa) {
		this.persistenceStructureServiceJpa = persistenceStructureServiceJpa;
	}

	public void setPersistenceStructureServiceOjb(PersistenceStructureService persistenceStructureServiceOjb) {
		this.persistenceStructureServiceOjb = persistenceStructureServiceOjb;
	}
	
	private PersistenceStructureService getService(Class clazz) {
		return (isJpaEnabledForKradClass(clazz)) ?
						persistenceStructureServiceJpa : persistenceStructureServiceOjb;
	}
	
	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#isPersistable(java.lang.Class)
	 */
	
	public boolean isPersistable(Class clazz) {
		return getService(clazz).isPersistable(clazz);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#getPrimaryKeys(java.lang.Class)
	 */
	
	public List getPrimaryKeys(Class clazz) {
		return getService(clazz).getPrimaryKeys(clazz);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceMetadataExplorerService#listFieldNames(java.lang.Class)
	 */
	
	public List listFieldNames(Class clazz) {
		return getService(clazz).listFieldNames(clazz);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceMetadataService#clearPrimaryKeyFields(java.lang.Object)
	 */
	// Unit tests only
	public Object clearPrimaryKeyFields(Object persistableObject) {
		return getService(persistableObject.getClass()).clearPrimaryKeyFields(persistableObject);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceMetadataExplorerService#listPersistableSubclasses(java.lang.Class)
	 */
	
	// Unit tests only
	public List listPersistableSubclasses(Class superclazz) {
		return getService(superclazz).listPersistableSubclasses(superclazz);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#getRelationshipMetadata(java.lang.Class,
	 *      java.lang.String)
	 */
	
	public Map<String, DataObjectRelationship> getRelationshipMetadata(Class persistableClass, String attributeName, String attributePrefix) {
		return getService(persistableClass).getRelationshipMetadata(persistableClass, attributeName, attributePrefix);
	}

	
	// Unit tests only
	public Map<String, DataObjectRelationship> getRelationshipMetadata(Class persistableClass, String attributeName) {
		return getService(persistableClass).getRelationshipMetadata(persistableClass, attributeName);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#getForeignKeyFieldName(java.lang.Object,
	 *      java.lang.String, java.lang.String)
	 */
	
	public String getForeignKeyFieldName(Class persistableObjectClass, String attributeName, String pkName) {
		return getService(persistableObjectClass).getForeignKeyFieldName(persistableObjectClass, attributeName, pkName);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#getReferencesForForeignKey(java.lang.Class,
	 *      java.lang.String)
	 */
	
	public Map getReferencesForForeignKey(Class persistableObjectClass, String attributeName) {
		return getService(persistableObjectClass).getReferencesForForeignKey(persistableObjectClass, attributeName);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#getForeignKeysForReference(java.lang.Class, java.lang.String)
	 */
	
	public Map getForeignKeysForReference(Class clazz, String attributeName) {
		return getService(clazz).getForeignKeysForReference(clazz, attributeName);
	}

	
	public Map<String, String> getInverseForeignKeysForCollection(Class boClass, String collectionName) {
		return getService(boClass).getInverseForeignKeysForCollection(boClass, collectionName);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#getNestedForeignKeyMap(java.lang.Class)
	 */
	
	public Map getNestedForeignKeyMap(Class persistableObjectClass) {
		return getService(persistableObjectClass).getNestedForeignKeyMap(persistableObjectClass);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceMetadataService#hasPrimaryKeyFieldValues(java.lang.Object)
	 */
	public boolean hasPrimaryKeyFieldValues(Object persistableObject) {
		return getService(persistableObject.getClass()).hasPrimaryKeyFieldValues(persistableObject);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#getForeignKeyFieldsPopulationState(org.kuali.rice.krad.bo.BusinessObject,
	 *      java.lang.String)
	 */
	public ForeignKeyFieldsPopulationState getForeignKeyFieldsPopulationState(PersistableBusinessObject bo, String referenceName) {
		return getService(bo.getClass()).getForeignKeyFieldsPopulationState(bo, referenceName);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceStructureService#listReferenceObjectFieldNames(java.lang.Class)
	 */
	
	public Map<String, Class> listReferenceObjectFields(Class boClass) {
		return getService(boClass).listReferenceObjectFields(boClass);
	}

	
	public Map<String, Class> listCollectionObjectTypes(Class boClass) {
		return getService(boClass).listCollectionObjectTypes(boClass);
	}

	public Map<String, Class> listCollectionObjectTypes(PersistableBusinessObject bo) {
		return getService(bo.getClass()).listCollectionObjectTypes(bo);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceStructureService#listReferenceObjectFieldNames(org.kuali.rice.krad.bo.BusinessObject)
	 */
	public Map<String, Class> listReferenceObjectFields(PersistableBusinessObject bo) {
		return getService(bo.getClass()).listReferenceObjectFields(bo);
	}

	
	public boolean isReferenceUpdatable(Class boClass, String referenceName) {
		return getService(boClass).isReferenceUpdatable(boClass, referenceName);
	}

	
	public boolean isCollectionUpdatable(Class boClass, String collectionName) {
		return getService(boClass).isCollectionUpdatable(boClass, collectionName);
	}

	
	public boolean hasCollection(Class boClass, String collectionName) {
		return getService(boClass).hasCollection(boClass, collectionName);
	}

	
	public boolean hasReference(Class boClass, String referenceName) {
		return getService(boClass).hasReference(boClass, referenceName);
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.krad.service.PersistenceStructureService#getTableName(java.lang.Class)
	 */
	public String getTableName(
			Class<? extends PersistableBusinessObject> boClass) {
		return getService(boClass).getTableName(boClass);
	}
	
	
}
