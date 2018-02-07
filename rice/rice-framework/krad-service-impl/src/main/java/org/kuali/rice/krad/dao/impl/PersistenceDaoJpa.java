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

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.hibernate.proxy.HibernateProxy;
import org.kuali.rice.core.framework.persistence.jpa.metadata.CollectionDescriptor;
import org.kuali.rice.core.framework.persistence.jpa.metadata.EntityDescriptor;
import org.kuali.rice.core.framework.persistence.jpa.metadata.JoinColumnDescriptor;
import org.kuali.rice.core.framework.persistence.jpa.metadata.MetadataManager;
import org.kuali.rice.core.framework.persistence.jpa.metadata.ObjectDescriptor;
import org.kuali.rice.krad.dao.PersistenceDao;
import org.kuali.rice.krad.service.KRADServiceLocator;

public class PersistenceDaoJpa implements PersistenceDao {
	static org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(PersistenceDaoJpa.class);
	
	@PersistenceContext
	private EntityManager entityManager;
    
	/**
	 * @see org.kuali.rice.krad.dao.PersistenceDao#clearCache()
	 */
	public void clearCache() {}

	/**
	 * @see org.kuali.rice.krad.dao.PersistenceDao#resolveProxy(java.lang.Object)
	 */
	public Object resolveProxy(Object o) {
		if (o instanceof HibernateProxy) {
        	try {
	        	final Object realObject = ((HibernateProxy) o).getHibernateLazyInitializer().getImplementation();
	        	return realObject;
        	} catch (EntityNotFoundException enfe) {
        		return null;
        	}
        }
		return o;
	}

	/**
	 * @see org.kuali.rice.krad.dao.PersistenceDao#retrieveAllReferences(java.lang.Object)
	 */
	public void retrieveAllReferences(Object o) {
		EntityDescriptor ed = MetadataManager.getEntityDescriptor(o.getClass());
		for (ObjectDescriptor od : ed.getObjectRelationships()) {
			retrieveReference(o, od.getAttributeName());
		}
		for (CollectionDescriptor cd : ed.getCollectionRelationships()) {
			retrieveReference(o, cd.getAttributeName());
		}
	}

	/**
	 * @see org.kuali.rice.krad.dao.PersistenceDao#retrieveReference(java.lang.Object, java.lang.String)
	 */
	public void retrieveReference(Object o, String referenceName) {
		try {
			if (getEntityManager().contains(o)) {
				LOG.debug("the entity manager contains the object");
			}
			
			Field field = getField(o.getClass(), referenceName);
			field.setAccessible(true);
			
			String fk = null;
			String foreignPK = null;
			
			if (isReferenceCollection(o, referenceName)) {
				Collection reference = retrieveCollectionReference(o, referenceName);
				field.set(o, reference);
			} else {
				Object reference = retrieveObjectReference(o, referenceName);
				field.set(o, reference);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private Field getField(Class clazz, String name) throws NoSuchFieldException {
		if (clazz.equals(Object.class)) {
			throw new NoSuchFieldException(name);
		}
		Field field = null;
		try {
			field = clazz.getDeclaredField(name);
		} catch (Exception e) {}
		if (field == null) {
			field = getField(clazz.getSuperclass(), name);
		}
		return field;
	}
	
	/**
	 * Determines if the reference on the given object represents a collection or not
	 * 
	 * @param o the object which is to be refreshed
	 * @param referenceName the name of the reference to refresh
	 * @return true if the reference is a collection, false otherwise
	 */
	protected boolean isReferenceCollection(Object o, String referenceName) {
		EntityDescriptor ed = MetadataManager.getEntityDescriptor(o.getClass());
		return ed.getCollectionDescriptorByName(referenceName) != null;
	}
	
	/**
	 * This method fetches a collection to refresh a reference
	 * 
	 * @param o the object to refresh
	 * @param referenceName the name of the reference to refresh
	 * @return the retrieved object to refresh the 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	protected Collection retrieveCollectionReference(Object o, String referenceName) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		final EntityDescriptor ed = MetadataManager.getEntityDescriptor(o.getClass());
		final CollectionDescriptor cd = ed.getCollectionDescriptorByName(referenceName);
		final EntityDescriptor foreignEntityDescriptor = MetadataManager.getEntityDescriptor(cd.getTargetEntity());
		
		Map<String, Object> searchKey = new HashMap<String, Object>();
		for (String foreignKey : cd.getForeignKeyFields()) {
			Field localField = getField(o.getClass(), foreignKey);
			localField.setAccessible(true);
			final Object localValue = localField.get(o);
			
			final String foreignKeyProperty = getForeignKeyPropertyForKeyWithPossibleInverse(foreignKey, ed, foreignEntityDescriptor, cd);

			searchKey.put(foreignKeyProperty, localValue);
		}
		return KRADServiceLocator.getBusinessObjectService().findMatching(cd.getTargetEntity(), searchKey);
	}
	
	/**
	 * Finds the correct foreign key property which corresponds to the given key
	 * 
	 * @param foreignKey the name of the key we want to find the proper foreign name for
	 * @param localEntityDescriptor the descriptor of the entity that key is on
	 * @param foreignEntityDescriptor the descriptor of the entity we're trying to retrieve
	 * @param joinColumnDescriptors a Map of the JoinColumnDescriptors that describe the relationship from the local entity's point of view, keyed by the column name of the JoinColumnDescriptor 
	 * @return the name of the property on the related object
	 */
	protected String getForeignKeyPropertyForKeyWithPossibleInverse(String foreignKey, EntityDescriptor localEntityDescriptor, EntityDescriptor foreignEntityDescriptor, CollectionDescriptor collectionDescriptor) {
		final String foreignKeyColumn = localEntityDescriptor.getFieldByName(foreignKey).getColumn();
		
		int count = 0;
		JoinColumnDescriptor joinColumnDescriptor = null;
		JoinColumnDescriptor inverseColumnDescriptor = null;
		while (count < collectionDescriptor.getJoinColumnDescriptors().size() && joinColumnDescriptor == null) {
			if (collectionDescriptor.getJoinColumnDescriptors().get(count).getName().equalsIgnoreCase(foreignKeyColumn)) {
				joinColumnDescriptor = collectionDescriptor.getJoinColumnDescriptors().get(count);
				if (count < collectionDescriptor.getInverseJoinColumnDescriptors().size()) {
					inverseColumnDescriptor = collectionDescriptor.getInverseJoinColumnDescriptors().get(count);
				}
			}
			count += 1;
		}
		
		if (inverseColumnDescriptor != null) {
			return foreignEntityDescriptor.getFieldByColumnName(inverseColumnDescriptor.getName()).getName();
		}
		
		if (!StringUtils.isBlank(joinColumnDescriptor.getReferencedColumName())) {
			return foreignEntityDescriptor.getFieldByColumnName(joinColumnDescriptor.getReferencedColumName()).getName();
		}

		return foreignEntityDescriptor.getFieldByColumnName(joinColumnDescriptor.getName()).getName();

	}
	
	/**
	 * Fetches an object reference
	 * 
	 * @param o the object to refresh
	 * @param referenceName the name of the reference to fetch
	 * @return the fetched referred to object
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchFieldException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 */
	protected Object retrieveObjectReference(Object o, String referenceName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InstantiationException, ClassNotFoundException {
		final EntityDescriptor ed = MetadataManager.getEntityDescriptor(o.getClass());
		final ObjectDescriptor od = ed.getObjectDescriptorByName(referenceName);
		
		final Object foreignKeyObject = buildForeignKeyObject(o, ed, od);
		return getEntityManager().find(od.getTargetEntity(), foreignKeyObject);
	}
	
	/**
	 * Builds a foreign key object for the relationship given by the foreignKeyClass and the objectDescriptor
	 * 
	 * @param o the object to refresh
	 * @param localEntityDescriptor the entity descriptor for that object
	 * @param objectDescriptor the object descriptor for the relationship to refresh
	 * @return the foreign key to fetch that object
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws InstantiationException 
	 */
	protected Object buildForeignKeyObject(Object o, EntityDescriptor localEntityDescriptor, ObjectDescriptor objectDescriptor) throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, InstantiationException {
		return (objectDescriptor.getForeignKeyFields().size() == 1) ? 
					buildSingleKeyForeignKeyObject(o, objectDescriptor.getForeignKeyFields().get(0)) : 
					buildCompositeForeignKeyObject(o, localEntityDescriptor, objectDescriptor);
	}

	/**
	 * Builds a composite key to fetch a reference
	 * 
	 * @param o the object to refresh
	 * @param localEntityDescriptor the entity descriptor for that object
	 * @param objectDescriptor the object descriptor for the relationship to refresh
	 * @return the foreign key to fetch that object
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	private Object buildCompositeForeignKeyObject(Object o, EntityDescriptor localEntityDescriptor, ObjectDescriptor objectDescriptor) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
		final Map<String, JoinColumnDescriptor> joinColumnDescriptors = buildJoinColumnDescriptorMap(objectDescriptor.getJoinColumnDescriptors());
		
		final EntityDescriptor foreignEntityDescriptor = MetadataManager.getEntityDescriptor(objectDescriptor.getTargetEntity());
		final Class foreignEntityIdClass = foreignEntityDescriptor.getIdClass();
		
		Object foreignEntityId = foreignEntityIdClass.newInstance();
		for (String foreignKey : objectDescriptor.getForeignKeyFields()) {
			// get the value from the current object
			Field localField = getField(o.getClass(), foreignKey);
			localField.setAccessible(true);
			final Object localValue = localField.get(o); 
			
			final String foreignKeyProperty = getForeignKeyPropertyForKey(foreignKey, localEntityDescriptor, foreignEntityDescriptor, joinColumnDescriptors);
						
			Field foreignField = getField(foreignEntityId.getClass(), foreignKeyProperty);
			foreignField.setAccessible(true);
			foreignField.set(foreignEntityId, localValue);
		}
		return foreignEntityId;
	}
	
	/**
	 * Finds the correct foreign key property which corresponds to the given key
	 * 
	 * @param foreignKey the name of the key we want to find the proper foreign name for
	 * @param localEntityDescriptor the descriptor of the entity that key is on
	 * @param foreignEntityDescriptor the descriptor of the entity we're trying to retrieve
	 * @param joinColumnDescriptors a Map of the JoinColumnDescriptors that describe the relationship from the local entity's point of view, keyed by the column name of the JoinColumnDescriptor 
	 * @return the name of the property on the related object
	 */
	protected String getForeignKeyPropertyForKey(String foreignKey, EntityDescriptor localEntityDescriptor, EntityDescriptor foreignEntityDescriptor, Map<String, JoinColumnDescriptor> joinColumnDescriptors) {
		final String foreignKeyColumn = localEntityDescriptor.getFieldByName(foreignKey).getColumn();
		final JoinColumnDescriptor joinColumnDescriptor = joinColumnDescriptors.get(foreignKeyColumn);
		
		return (!StringUtils.isBlank(joinColumnDescriptor.getReferencedColumName())) ? 
				foreignEntityDescriptor.getFieldByColumnName(joinColumnDescriptor.getReferencedColumName()).getName() : 
				foreignEntityDescriptor.getFieldByColumnName(joinColumnDescriptor.getName()).getName();

	}
	
	/**
	 * Turns a List of JoinColumnDescriptors and maps them by their name
	 * 
	 * @param joinColumnDescriptors a List of JoinColumnDescriptors
	 * @return a Map the List as a Map, keyed by the name of the JoinColumn
	 */
	protected Map<String, JoinColumnDescriptor> buildJoinColumnDescriptorMap(List<JoinColumnDescriptor> joinColumnDescriptors) {
		Map<String, JoinColumnDescriptor> descriptorMap = new HashMap<String, JoinColumnDescriptor>();
		for (JoinColumnDescriptor joinColumnDescriptor : joinColumnDescriptors) {
			descriptorMap.put(joinColumnDescriptor.getName(), joinColumnDescriptor);
		}
		return descriptorMap;
	}
	
	/**
	 * Builds a foreign key, where that foreign key has a single field
	 * 
	 * @param o the object to get the foreign key value from
	 * @param singleForeignKeyFieldName the name of the foreign key field
	 * @return a value for the foreign key
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	protected Object buildSingleKeyForeignKeyObject(Object o, String singleForeignKeyFieldName) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field singleFKField = getField(o.getClass(), singleForeignKeyFieldName);
		singleFKField.setAccessible(true);
		return singleFKField.get(o);
	}

	/**
	 * True if object is an instance of HibernateProxy, false otherwise
	 * 
	 * @see org.kuali.rice.krad.dao.PersistenceDao#isProxied(java.lang.Object)
	 */
	public boolean isProxied(Object object) {
		return (object instanceof HibernateProxy);
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
}
