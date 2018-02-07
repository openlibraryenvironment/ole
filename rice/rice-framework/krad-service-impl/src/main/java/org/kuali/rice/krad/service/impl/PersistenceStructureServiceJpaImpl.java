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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.framework.persistence.jpa.metadata.EntityDescriptor;
import org.kuali.rice.core.framework.persistence.jpa.metadata.JoinColumnDescriptor;
import org.kuali.rice.core.framework.persistence.jpa.metadata.MetadataManager;
import org.kuali.rice.core.framework.persistence.jpa.metadata.ObjectDescriptor;
import org.kuali.rice.krad.bo.DataObjectRelationship;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.exception.ObjectNotABusinessObjectRuntimeException;
import org.kuali.rice.krad.exception.ReferenceAttributeDoesntExistException;
import org.kuali.rice.krad.exception.ReferenceAttributeNotAnOjbReferenceException;
import org.kuali.rice.krad.service.PersistenceStructureService;
import org.kuali.rice.krad.util.ForeignKeyFieldsPopulationState;

public class PersistenceStructureServiceJpaImpl extends PersistenceServiceImplBase implements PersistenceStructureService {

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

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#isPersistable(java.lang.Class)
	 */
	
	public boolean isPersistable(Class clazz) {
		boolean isPersistable = false;

		if (MetadataManager.getEntityDescriptor(clazz) != null) {
			isPersistable = true;
		}

		return isPersistable;
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#getPrimaryKeys(java.lang.Class)
	 */
	
	public List getPrimaryKeys(Class clazz) {
		List pkList = new ArrayList();

		EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(clazz);
		for (org.kuali.rice.core.framework.persistence.jpa.metadata.FieldDescriptor field : descriptor.getPrimaryKeys()) {
			pkList.add(field.getName());
		}

		return pkList;
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceMetadataExplorerService#listFieldNames(java.lang.Class)
	 */
	
	public List listFieldNames(Class clazz) {
		List fieldNames = new ArrayList();

		EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(clazz);
		for (org.kuali.rice.core.framework.persistence.jpa.metadata.FieldDescriptor field : descriptor.getFields()) {
			fieldNames.add(field.getName());
		}

		return fieldNames;
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceMetadataService#clearPrimaryKeyFields(java.lang.Object)
	 */
	// Unit tests only
	// TODO: Write JPA Version
	public Object clearPrimaryKeyFields(Object persistableObject) {
		/*
		 * if (persistableObject == null) { throw new
		 * IllegalArgumentException("invalid (null) persistableObject"); }
		 * 
		 * String className = null; String fieldName = null; try { className =
		 * persistableObject.getClass().getName(); List fields =
		 * listPrimaryKeyFieldNames(persistableObject.getClass()); for (Iterator
		 * i = fields.iterator(); i.hasNext();) { fieldName = (String) i.next();
		 * 
		 * PropertyUtils.setProperty(persistableObject, fieldName, null); }
		 * 
		 * if (persistableObject instanceof PersistableBusinessObject) {
		 * ((PersistableBusinessObject) persistableObject).setObjectId(null); } }
		 * catch (NoSuchMethodException e) { throw new
		 * IntrospectionException("no setter for property '" + className + "." +
		 * fieldName + "'", e); } catch (IllegalAccessException e) { throw new
		 * IntrospectionException("problem accessing property '" + className +
		 * "." + fieldName + "'", e); } catch (InvocationTargetException e) {
		 * throw new IntrospectionException("problem invoking getter for
		 * property '" + className + "." + fieldName + "'", e); }
		 */
		return persistableObject;
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceMetadataExplorerService#listPersistableSubclasses(java.lang.Class)
	 */
	
	// Unit tests only
	// TODO: Write JPA Version
	public List listPersistableSubclasses(Class superclazz) {
		List persistableSubclasses = new ArrayList();
		/*
		 * if (superclazz == null) { throw new IllegalArgumentException("invalid
		 * (null) uberclass"); }
		 * 
		 * Map allDescriptors = getDescriptorRepository().getDescriptorTable();
		 * for (Iterator i = allDescriptors.entrySet().iterator(); i.hasNext();) {
		 * Map.Entry e = (Map.Entry) i.next();
		 * 
		 * Class persistableClass = ((ClassDescriptor)
		 * e.getValue()).getClassOfObject(); if
		 * (!superclazz.equals(persistableClass) &&
		 * superclazz.isAssignableFrom(persistableClass)) {
		 * persistableSubclasses.add(persistableClass); } }
		 */
		return persistableSubclasses;
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#getRelationshipMetadata(java.lang.Class,
	 *      java.lang.String)
	 */
	
	public Map<String, DataObjectRelationship> getRelationshipMetadata(Class persistableClass, String attributeName, String attributePrefix) {
		if (persistableClass == null) {
			throw new IllegalArgumentException("invalid (null) persistableClass");
		}
		if (StringUtils.isBlank(attributeName)) {
			throw new IllegalArgumentException("invalid (blank) attributeName");
		}

		Map<String, DataObjectRelationship> relationships = new HashMap<String, DataObjectRelationship>();

		EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(persistableClass);
		for (ObjectDescriptor objectDescriptor : descriptor.getObjectRelationships()) {
			List<String> fks = objectDescriptor.getForeignKeyFields();
			if (fks.contains(attributeName) || objectDescriptor.getAttributeName().equals(attributeName)) {
				Map<String, String> fkToPkRefs = getForeignKeysForReference(persistableClass, objectDescriptor.getAttributeName());
				DataObjectRelationship
                        rel = new DataObjectRelationship(persistableClass, objectDescriptor.getAttributeName(), objectDescriptor.getTargetEntity());
				for (Map.Entry<String, String> ref : fkToPkRefs.entrySet()) {
					if (StringUtils.isBlank(attributePrefix)) {
						rel.getParentToChildReferences().put(ref.getKey(), ref.getValue());
					} else {
						rel.getParentToChildReferences().put(attributePrefix + "." + ref.getKey(), ref.getValue());
					}
				}
				relationships.put(objectDescriptor.getAttributeName(), rel);
			}
		}

		return relationships;
	}

	
	// Unit tests only
	public Map<String, DataObjectRelationship> getRelationshipMetadata(Class persistableClass, String attributeName) {
		return getRelationshipMetadata(persistableClass, attributeName, null);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#getForeignKeyFieldName(java.lang.Object,
	 *      java.lang.String, java.lang.String)
	 */
	
	public String getForeignKeyFieldName(Class persistableObjectClass, String attributeName, String pkName) {
		String fkName = null;

		EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(persistableObjectClass);
		ObjectDescriptor objectDescriptor = descriptor.getObjectDescriptorByName(attributeName);
		if (objectDescriptor == null) {
			throw new RuntimeException("Attribute name " + attributeName + " is not a valid reference to class " + persistableObjectClass.getName());
		}
		List<org.kuali.rice.core.framework.persistence.jpa.metadata.FieldDescriptor> matches = new ArrayList<org.kuali.rice.core.framework.persistence.jpa.metadata.FieldDescriptor>();
		for (org.kuali.rice.core.framework.persistence.jpa.metadata.FieldDescriptor field : descriptor.getFields()) {
			String column = field.getColumn();
			for (JoinColumnDescriptor join : objectDescriptor.getJoinColumnDescriptors()) {
				if (column != null && column.equals(join.getName())) {
					matches.add(field);
				}
			}
		}

		if (matches.size() == 1) {
			fkName = matches.get(0).getName();
		} else {
			throw new RuntimeException("Implement me!");
		}

		return fkName;
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#getReferencesForForeignKey(java.lang.Class,
	 *      java.lang.String)
	 */
	
	public Map getReferencesForForeignKey(Class persistableObjectClass, String attributeName) {
		Map referenceClasses = new HashMap();

		if (PersistableBusinessObject.class.isAssignableFrom(persistableObjectClass)) {
			EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(persistableObjectClass);
			for (ObjectDescriptor objectDescriptor : descriptor.getObjectRelationships()) {
				List<String> refFkNames = objectDescriptor.getForeignKeyFields();
				for (String fk : refFkNames) {
					if (fk.equals(attributeName)) {
						referenceClasses.put(objectDescriptor.getAttributeName(), objectDescriptor.getTargetEntity());
					}
				}
			}
		}

		return referenceClasses;
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#getForeignKeysForReference(java.lang.Class,
	 *      java.lang.String) The Map structure is: Key(String fkFieldName) =>
	 *      Value(String pkFieldName) NOTE that this implementation depends on
	 *      the ordering of foreign-key elements in the ojb-repository matching
	 *      the ordering of primary-key declarations of the class on the other
	 *      side of the relationship. This is done because: 1. The current
	 *      version of OJB requires you to declare all of these things in the
	 *      correct (and matching) order in the ojb-repository file for it to
	 *      work at all. 2. There is no other way to match a given foreign-key
	 *      reference to its corresponding primary-key on the opposing side of
	 *      the relationship. Yes, this is a crummy way to do it, but OJB doesnt
	 *      provide explicit matches of foreign-keys to primary keys, and always
	 *      assumes that foreign-keys map to primary keys on the other object,
	 *      and never to a set of candidate keys, or any other column.
	 */
	
	public Map getForeignKeysForReference(Class clazz, String attributeName) {

		// yelp if nulls were passed in
		if (clazz == null) {
			throw new IllegalArgumentException("The Class passed in for the clazz argument was null.");
		}
		if (attributeName == null) {
			throw new IllegalArgumentException("The String passed in for the attributeName argument was null.");
		}

		// get the class of the attribute name
		Class attributeClass = getBusinessObjectAttributeClass(clazz, attributeName);
		if (attributeClass == null) {
			throw new ReferenceAttributeDoesntExistException("Requested attribute: '" + attributeName + "' does not exist on class: '" + clazz.getName() + "'.");
		}

		// make sure the class of the attribute descends from BusinessObject,
		// otherwise throw an exception
		if (!PersistableBusinessObject.class.isAssignableFrom(attributeClass)) {
			throw new ObjectNotABusinessObjectRuntimeException("Attribute requested (" + attributeName + ") is of class: '" + attributeClass.getName() + "' and is not a descendent of BusinessObject.  Only descendents of BusinessObject can be used.");
		}

		return determineFkMap(clazz, attributeName, attributeClass);
	}

	private Map determineFkMap(Class clazz, String attributeName, Class attributeClass) {
		Map fkMap = new HashMap();
		EntityDescriptor entityDescriptor = MetadataManager.getEntityDescriptor(clazz);
		ObjectDescriptor objectDescriptor = entityDescriptor.getObjectDescriptorByName(attributeName);
		if (objectDescriptor == null) {
			throw new ReferenceAttributeNotAnOjbReferenceException("Attribute requested (" + attributeName + ") is not defined in JPA annotations for class: '" + clazz.getName() + "'");
		}

		// special case when the attributeClass passed in doesnt match the
		// class of the reference-descriptor as defined in ojb-repository.
		// Currently
		// the only case of this happening is ObjectCode vs.
		// ObjectCodeCurrent.
		if (!attributeClass.equals(objectDescriptor.getTargetEntity())) {
			if (referenceConversionMap.containsKey(attributeClass)) {
				attributeClass = referenceConversionMap.get(attributeClass);
			} else {
				throw new RuntimeException("The Class of the Java member [" + attributeClass.getName() + "] '" + attributeName + "' does not match the class of the reference [" + objectDescriptor.getTargetEntity().getName() + "]. " + "This is an unhandled special case for which special code needs to be written in this class.");
			}
		}

		// get the list of the foreign-keys for this reference-descriptor
		// (OJB)
		List<String> fkFields = objectDescriptor.getForeignKeyFields();
		Iterator<String> fkIterator = fkFields.iterator();

		// get the list of the corresponding pk fields on the other side of
		// the relationship
		List pkFields = getPrimaryKeys(attributeClass);
		Iterator pkIterator = pkFields.iterator();

		// make sure the size of the pkIterator is the same as the
		// size of the fkIterator, otherwise this whole thing is borked
		if (pkFields.size() != fkFields.size()) {
			throw new RuntimeException("KualiPersistenceStructureService Error: The number of " + "foreign keys doesnt match the number of primary keys.");
		}

		// walk through the list of the foreign keys, get their types
		while (fkIterator.hasNext()) {
			// if there is a next FK but not a next PK, then we've got a big
			// problem,
			// and cannot continue
			if (!pkIterator.hasNext()) {
				throw new RuntimeException("The number of foriegn keys dont match the number of primary keys for the reference '" + attributeName + "', on BO of type '" + clazz.getName() + "'.  " + "This should never happen under normal circumstances.");
			}

			// get the field name of the fk & pk field
			String fkFieldName = (String) fkIterator.next();
			String pkFieldName = (String) pkIterator.next();

			// add the fieldName and fieldType to the map
			fkMap.put(fkFieldName, pkFieldName);
		}
		return fkMap;
	}

	
	public Map<String, String> getInverseForeignKeysForCollection(Class boClass, String collectionName) {
		// yelp if nulls were passed in
		if (boClass == null) {
			throw new IllegalArgumentException("The Class passed in for the boClass argument was null.");
		}
		if (collectionName == null) {
			throw new IllegalArgumentException("The String passed in for the attributeName argument was null.");
		}

		PropertyDescriptor propertyDescriptor = null;

		// make an instance of the class passed
		Object classInstance;
		try {
			classInstance = boClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// make sure the attribute exists at all, throw exception if not
		try {
			propertyDescriptor = PropertyUtils.getPropertyDescriptor(classInstance, collectionName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (propertyDescriptor == null) {
			throw new ReferenceAttributeDoesntExistException("Requested attribute: '" + collectionName + "' does not exist " + "on class: '" + boClass.getName() + "'. GFK");
		}

		// get the class of the attribute name
		Class attributeClass = propertyDescriptor.getPropertyType();

		// make sure the class of the attribute descends from BusinessObject,
		// otherwise throw an exception
		if (!Collection.class.isAssignableFrom(attributeClass)) {
			throw new ObjectNotABusinessObjectRuntimeException("Attribute requested (" + collectionName + ") is of class: " + "'" + attributeClass.getName() + "' and is not a " + "descendent of Collection");
		}

		EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(boClass);
		org.kuali.rice.core.framework.persistence.jpa.metadata.CollectionDescriptor cd = descriptor.getCollectionDescriptorByName(collectionName);
		List<String> childPrimaryKeys = cd.getForeignKeyFields();

		List parentForeignKeys = getPrimaryKeys(boClass);

		if (parentForeignKeys.size() != childPrimaryKeys.size()) {
			throw new RuntimeException("The number of keys in the class descriptor and the inverse foreign key mapping for the collection descriptors do not match.");
		}

		Map<String, String> fkToPkMap = new HashMap<String, String>();
		Iterator pFKIter = parentForeignKeys.iterator();
		Iterator cPKIterator = childPrimaryKeys.iterator();

		while (pFKIter.hasNext()) {
			String parentForeignKey = (String) pFKIter.next();
			String childPrimaryKey = (String) cPKIterator.next();

			fkToPkMap.put(parentForeignKey, childPrimaryKey);
		}
		return fkToPkMap;
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#getNestedForeignKeyMap(java.lang.Class)
	 */
	
	public Map getNestedForeignKeyMap(Class persistableObjectClass) {
		Map fkMap = new HashMap();

		// Rice JPA MetadataManager
		// Note: Not sure how to test this method, and JPA and OJB ordering of
		// PK/FK is not the same as well. This should be tested more thoroughly.
		EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(persistableObjectClass);
		for (ObjectDescriptor objectReferenceDescriptor : descriptor.getObjectRelationships()) {
			EntityDescriptor referenceDescriptor = MetadataManager.getEntityDescriptor(objectReferenceDescriptor.getTargetEntity());
			List<String> fkFields = objectReferenceDescriptor.getForeignKeyFields();
			Set<org.kuali.rice.core.framework.persistence.jpa.metadata.FieldDescriptor> pkFields = referenceDescriptor.getPrimaryKeys();
			int i = 0;
			for (org.kuali.rice.core.framework.persistence.jpa.metadata.FieldDescriptor fd : pkFields) {
				fkMap.put(objectReferenceDescriptor.getAttributeName() + "." + fd.getName(), fkFields.get(i));
				i++;
			}
		}

		return fkMap;
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceMetadataService#hasPrimaryKeyFieldValues(java.lang.Object)
	 */
	public boolean hasPrimaryKeyFieldValues(Object persistableObject) {
		Map keyFields = getPrimaryKeyFieldValues(persistableObject);

		boolean emptyField = false;
		for (Iterator i = keyFields.entrySet().iterator(); !emptyField && i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();

			Object fieldValue = e.getValue();
			if (fieldValue == null) {
				emptyField = true;
			} else if (fieldValue instanceof String) {
				if (StringUtils.isEmpty((String) fieldValue)) {
					emptyField = true;
				} else {
					emptyField = false;
				}
			}
		}

		return !emptyField;
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceService#getForeignKeyFieldsPopulationState(org.kuali.rice.krad.bo.BusinessObject,
	 *      java.lang.String)
	 */
	public ForeignKeyFieldsPopulationState getForeignKeyFieldsPopulationState(PersistableBusinessObject bo, String referenceName) {

		boolean allFieldsPopulated = true;
		boolean anyFieldsPopulated = false;
		List<String> unpopulatedFields = new ArrayList<String>();

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
		Class referenceClass = propertyDescriptor.getPropertyType();

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

		List<String> fkFields = objectDescriptor.getForeignKeyFields();
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

			// abort if the value is not retrievable
			catch (Exception e) {
				throw new RuntimeException(e);
			}

			// test the value
			if (fkFieldValue == null) {
				allFieldsPopulated = false;
				unpopulatedFields.add(fkFieldName);
			} else if (fkFieldValue instanceof String) {
				if (StringUtils.isBlank((String) fkFieldValue)) {
					allFieldsPopulated = false;
					unpopulatedFields.add(fkFieldName);
				} else {
					anyFieldsPopulated = true;
				}
			} else {
				anyFieldsPopulated = true;
			}
		}

		// sanity check. if the flag for all fields populated is set, then
		// there should be nothing in the unpopulatedFields list
		if (allFieldsPopulated) {
			if (!unpopulatedFields.isEmpty()) {
				throw new RuntimeException("The flag is set that indicates all fields are populated, but there " + "are fields present in the unpopulatedFields list.  This should never happen, and indicates " + "that the logic in this method is broken.");
			}
		}

		return new ForeignKeyFieldsPopulationState(allFieldsPopulated, anyFieldsPopulated, unpopulatedFields);
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceStructureService#listReferenceObjectFieldNames(java.lang.Class)
	 */
	
	public Map<String, Class> listReferenceObjectFields(Class boClass) {
		// validate parameter
		if (boClass == null) {
			throw new IllegalArgumentException("Class specified in the parameter was null.");
		}
		if (!PersistableBusinessObject.class.isAssignableFrom(boClass)) {
			throw new IllegalArgumentException("Class specified [" + boClass.getName() + "] must be a class that " + "inherits from BusinessObject.");
		}

		EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(boClass);
		Map<String, Class> references = new HashMap();
		for (org.kuali.rice.core.framework.persistence.jpa.metadata.ObjectDescriptor od : descriptor.getObjectRelationships()) {
			references.put(od.getAttributeName(), od.getTargetEntity());
		}

		return references;
	}

	
	public Map<String, Class> listCollectionObjectTypes(Class boClass) {
		if (boClass == null) {
			throw new IllegalArgumentException("Class specified in the parameter was null.");
		}

		Map<String, Class> references = new HashMap();

		EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(boClass);
		if (descriptor == null) {
			return references;
		}

		for (org.kuali.rice.core.framework.persistence.jpa.metadata.CollectionDescriptor cd : descriptor.getCollectionRelationships()) {
			references.put(cd.getAttributeName(), cd.getTargetEntity());
		}

		return references;
	}

	public Map<String, Class> listCollectionObjectTypes(PersistableBusinessObject bo) {
		// validate parameter
		if (bo == null) {
			throw new IllegalArgumentException("BO specified in the parameter was null.");
		}
		if (!(bo instanceof PersistableBusinessObject)) {
			throw new IllegalArgumentException("BO specified [" + bo.getClass().getName() + "] must be a class that " + "inherits from BusinessObject.");
		}

		return listCollectionObjectTypes(bo.getClass());
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceStructureService#listReferenceObjectFieldNames(org.kuali.rice.krad.bo.BusinessObject)
	 */
	public Map<String, Class> listReferenceObjectFields(PersistableBusinessObject bo) {
		// validate parameter
		if (bo == null) {
			throw new IllegalArgumentException("BO specified in the parameter was null.");
		}
		if (!(bo instanceof PersistableBusinessObject)) {
			throw new IllegalArgumentException("BO specified [" + bo.getClass().getName() + "] must be a class that " + "inherits from BusinessObject.");
		}

		return listReferenceObjectFields(bo.getClass());
	}

	
	public boolean isReferenceUpdatable(Class boClass, String referenceName) {
		EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(boClass);
		return descriptor.getObjectDescriptorByName(referenceName).isUpdateable();
//		for (CascadeType ct : descriptor.getObjectDescriptorByName(referenceName).getCascade()) {
//			if (ct.equals(CascadeType.ALL) || ct.equals(CascadeType.MERGE) || ct.equals(CascadeType.PERSIST)) {
//				return true;
//			}
//		}
//		return false;
	}

	
	public boolean isCollectionUpdatable(Class boClass, String collectionName) {
		EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(boClass);
		return descriptor.getCollectionDescriptorByName(collectionName).isUpdateable();
//		for (CascadeType ct : descriptor.getCollectionDescriptorByName(collectionName).getCascade()) {
//			if (ct.equals(CascadeType.ALL) || ct.equals(CascadeType.MERGE) || ct.equals(CascadeType.PERSIST)) {
//				return true;
//			}
//		}
//		return false;
	}

	
	public boolean hasCollection(Class boClass, String collectionName) {
		EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(boClass);
		return descriptor.getCollectionDescriptorByName(collectionName) != null;
	}

	
	public boolean hasReference(Class boClass, String referenceName) {
		EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(boClass);
		return descriptor.getObjectDescriptorByName(referenceName) != null;
	}

	/**
	 * @see org.kuali.rice.krad.service.PersistenceStructureService#getTableName(java.lang.Class)
	 */
	
	public String getTableName(Class<? extends PersistableBusinessObject> boClass) {
		EntityDescriptor descriptor = MetadataManager.getEntityDescriptor(boClass);
		return descriptor.getTable();
	}
}

