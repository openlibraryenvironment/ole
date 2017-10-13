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
package org.kuali.rice.core.framework.persistence.jpa.metadata;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.framework.persistence.jpa.annotations.Sequence;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MetadataManager {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MetadataManager.class);
	
	private static Map<Class, EntityDescriptor> entitesByClass = Collections.synchronizedMap( new HashMap<Class, EntityDescriptor>() );
	private static Map<String, EntityDescriptor> entitesByName = Collections.synchronizedMap( new HashMap<String, EntityDescriptor>() );
	
	private MetadataManager() {}

	public static EntityDescriptor getEntityDescriptor(Class clazz) {
		if (clazz != null && clazz.getName().contains("$$EnhancerByCGLIB")) {
			try {
				clazz = Class.forName(clazz.getName().substring(0, clazz.getName().indexOf("$$EnhancerByCGLIB")));
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}

		return addEntity(clazz);
	}
	
	public static Map<String, Object> getEntityPrimaryKeyValuePairs(Object object) {
		Map<String, Object> pks = new HashMap<String, Object>();
		EntityDescriptor descriptor = getEntityDescriptor(object.getClass());
		for (FieldDescriptor fieldDescriptor : descriptor.getPrimaryKeys()) {
			try {
				Field field = getField(object.getClass(), fieldDescriptor.getName());
				field.setAccessible(true);
				if (field.get(object) != null) {
					pks.put(fieldDescriptor.getName(), field.get(object));
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return pks;
	}
	
	/**
	 * Retrieves the primary key as an object for the given Object (which is assumed to be a JPA entity).  If the entity has a single field
	 * primary key, the value of that field is returned.  If a composite key is needed, it will be constructed and populated with the correct
	 * values.  If a problem occurs, a null will be returned
	 * 
	 * @param object the object to get a primary key value from
	 * @return a primary key value
	 */
	public static Object getEntityPrimaryKeyObject(Object object) {
		final EntityDescriptor descriptor = getEntityDescriptor(object.getClass());
		final Class idClass = descriptor.getIdClass();
		if (idClass != null) {
			try {
				Object pkObject = idClass.newInstance();
				
				for (FieldDescriptor fieldDescriptor : descriptor.getPrimaryKeys()) {
					Field field = getField(object.getClass(), fieldDescriptor.getName());
					field.setAccessible(true);
					final Object value = field.get(object);
					if (value != null) {
						final Field fieldToSet = getField(pkObject.getClass(), fieldDescriptor.getName());
						fieldToSet.setAccessible(true);
						fieldToSet.set(pkObject, value);
					}
				}
				
				return pkObject;
			} catch (SecurityException se) {
				LOG.error(se.getMessage(), se);
			} catch (InstantiationException ie) {
				LOG.error(ie.getMessage(), ie);
			} catch (IllegalAccessException iae) {
				LOG.error(iae.getMessage(), iae);
			} catch (NoSuchFieldException nsfe) {
				LOG.error(nsfe.getMessage(), nsfe);
			}
		} else {
			for (FieldDescriptor fieldDescriptor : descriptor.getPrimaryKeys()) {
				try {
					Field field = getField(object.getClass(), fieldDescriptor.getName());
					field.setAccessible(true);
					return field.get(object);  // there's only one value, let's kick out
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Retrieves the primary key as an object for the given Object (which is assumed to be a JPA entity), filling it with values from the extension.
	 * If the entity has a single field primary key, the value of that field from the extension is returned.  If a composite key is needed, it will be 
	 * constructed (based on the id class for the extension object) and populated with the correct values from the extension.  If a problem occurs, 
	 * a null will be returned.
	 * 
	 * @param owner the object to get values from
	 * @param extension the object to build a key for
	 * @return a primary key value
	 */
	public static Object getPersistableBusinessObjectPrimaryKeyObjectWithValuesForExtension(Object owner, Object extension) {
		final EntityDescriptor descriptor = getEntityDescriptor(extension.getClass());
		final Class idClass = descriptor.getIdClass();
		if (idClass != null) {
			try {
				Object pkObject = idClass.newInstance();
				
				for (FieldDescriptor fieldDescriptor : descriptor.getPrimaryKeys()) {
					Field field = getField(owner.getClass(), fieldDescriptor.getName());
					field.setAccessible(true);
					final Object value = field.get(owner);
					if (value != null) {
						final Field fieldToSet = getField(pkObject.getClass(), fieldDescriptor.getName());
						fieldToSet.setAccessible(true);
						fieldToSet.set(pkObject, value);
					}
				}
				
				return pkObject;
			} catch (SecurityException se) {
				LOG.error(se.getMessage(), se);
			} catch (InstantiationException ie) {
				LOG.error(ie.getMessage(), ie);
			} catch (IllegalAccessException iae) {
				LOG.error(iae.getMessage(), iae);
			} catch (NoSuchFieldException nsfe) {
				LOG.error(nsfe.getMessage(), nsfe);
			}
		} else {
			for (FieldDescriptor fieldDescriptor : descriptor.getPrimaryKeys()) {
				try {
					Field field = getField(owner.getClass(), fieldDescriptor.getName());
					field.setAccessible(true);
					final Object value = field.get(owner);
					return value;  // there's only one value, let's kick out
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
		
		return null;
	}
	
	/**
	 * This converts a map of primary keys into an object: in the case of a single key, just the value object iself; in the case of a composite key,
	 * the correct composite key object populated with the values from the map
	 * @param entityClazz the class of the entity the pkMap was for
	 * @param pkMap the map of primary key fields and values
	 * @return the correct primary key object
	 */
	public static Object convertPrimaryKeyMapToObject(Class entityClazz, Map<String, Object> pkMap) {
		if (pkMap.isEmpty()) {
			return null;
		}
		
		final EntityDescriptor descriptor = getEntityDescriptor(entityClazz);
		final Class idClass = descriptor.getIdClass();
		
		if (idClass == null) {
			if (pkMap.size() != 1) {
				throw new IllegalArgumentException("pkMap has a size of "+pkMap.size()+"; but since entityClazz does not have a composite primary key, the size should be 1");
			}
			for (String key : pkMap.keySet()) {
				return pkMap.get(key);
			}
		} else {
			try {
				Object pkObject = idClass.newInstance();
				for (String key : pkMap.keySet()) {
					Field field = getField(idClass, key);
					field.setAccessible(true);
					field.set(pkObject, pkMap.get(key));
				}
				return pkObject;
			} catch (InstantiationException ie) {
				throw new RuntimeException("Could not convert primary key map to composite key object", ie);
			} catch (IllegalAccessException iae) {
				throw new RuntimeException("Could not convert primary key map to composite key object", iae);
			} catch (NoSuchFieldException nsfe) {
				throw new RuntimeException("Could not convert primary key map to composite key object", nsfe);
			}
		}
		return null;// I don't believe this code is reachable, but...you never know
	}
	
	private static Field getField(Class clazz, String name) throws NoSuchFieldException {
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
	
	private static EntityDescriptor addEntity(Class clazz) {
		EntityDescriptor entity = entitesByClass.get(clazz); 
		if (entity == null) {
			entity = construct(clazz);
			if (entity != null) {
				entitesByClass.put(entity.getClazz(), entity);
				entitesByName.put(entity.getName(), entity);
			}
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	private static EntityDescriptor construct(Class clazz) {
		if (!clazz.isAnnotationPresent(Entity.class)) {
			return null;
		}
		
		// Determine the base entity metadata
		EntityDescriptor entityDescriptor = new EntityDescriptor();
		entityDescriptor.setClazz(clazz);
		String defaultName = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
		Entity entity = (Entity) clazz.getAnnotation(Entity.class);
		if (StringUtils.isBlank(entity.name())) {
			entityDescriptor.setName(defaultName);
		} else {
			entityDescriptor.setName(entity.name());
		}
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = (Table) clazz.getAnnotation(Table.class);
			entityDescriptor.setTable(table.name());
		} else {
			entityDescriptor.setTable(defaultName);
		}
		if (clazz.isAnnotationPresent(IdClass.class)) {
			entityDescriptor.setIdClass(((IdClass)clazz.getAnnotation(IdClass.class)).value());
		}
		if (clazz.isAnnotationPresent(Sequence.class)) {
			entityDescriptor.setSequence((Sequence)clazz.getAnnotation(Sequence.class));
		}		
		
		// Check for an "extension"
		try {
			Class extensionClass = Class.forName(clazz.getName() + "Extension");
			OneToOneDescriptor descriptor = new OneToOneDescriptor();
			descriptor.setCascade(new CascadeType[] { CascadeType.PERSIST });
			descriptor.setAttributeName("extension");
			descriptor.setTargetEntity(extensionClass);
			descriptor.setMappedBy("extension");
			EntityDescriptor extensionDescriptor = MetadataManager.getEntityDescriptor(extensionClass);
			for (FieldDescriptor fd : extensionDescriptor.getPrimaryKeys()) {
				descriptor.addFkField(fd.getName());
			}
			entityDescriptor.add(descriptor);
			FieldDescriptor extension = new FieldDescriptor();
			extension.setName("extension");
			extension.setClazz(extensionClass);
			entityDescriptor.add(extension);
		} catch (Exception e) {}
		
		
		List<Class> classes = new ArrayList<Class>();
		classes.add(clazz);
		Class c = clazz;
		while (!c.getSuperclass().equals(Object.class)) {
			c = c.getSuperclass();
			classes.add(c);
		}
		Collections.reverse(classes);
		
		// Determine the field/relationship metadata for all classes in the clazz hierarchy
		for (Class temp : classes) {
			extractFieldMetadata(temp, entityDescriptor);
			if (temp.isAnnotationPresent(AttributeOverrides.class)) {
				for (AttributeOverride override : ((AttributeOverrides)temp.getAnnotation(AttributeOverrides.class)).value()) {
					entityDescriptor.getFieldByName(override.name()).setColumn(override.column().name());
				}
			}
			if (temp.isAnnotationPresent(AttributeOverride.class)) {
				AttributeOverride override = (AttributeOverride) temp.getAnnotation(AttributeOverride.class);
				entityDescriptor.getFieldByName(override.name()).setColumn(override.column().name());					
			}
			//if (temp.isAnnotationPresent(AssociationOverrides.class)) {
			//	for (AssociationOverride override : ((AssociationOverrides)temp.getAnnotation(AssociationOverride.class)).value()) {
			//		entityDescriptor.getFieldByName(override.name()).;
			//	}
			//}
			//if (temp.isAnnotationPresent(AttributeOverride.class)) {
			//	AttributeOverride override = (AttributeOverride) temp.getAnnotation(AttributeOverride.class);
			//	entityDescriptor.getFieldByName(override.name()).setColumn(override.column().name());					
			//}
			
		}
				
		return entityDescriptor;
	}

	
	private static void extractFieldMetadata(Class clazz, EntityDescriptor entityDescriptor) {
    	// Don't want to get parent fields if overridden in children since we are walking the tree from child to parent
		Set<String> cachedFields = new HashSet<String>(); 
		do {
			for (Field field : clazz.getDeclaredFields()) {
				if (cachedFields.contains(field.getName())) {
					continue;
				}
				cachedFields.add(field.getName());
				
				int mods = field.getModifiers();
				if (Modifier.isFinal(mods) || Modifier.isStatic(mods) || Modifier.isTransient(mods) || field.isAnnotationPresent(Transient.class)) {
					continue;
				}

				// Basic Fields
				FieldDescriptor fieldDescriptor = new FieldDescriptor();
				fieldDescriptor.setClazz(field.getType());
				fieldDescriptor.setTargetClazz(field.getType());
				fieldDescriptor.setName(field.getName());
				
				
				if (field.isAnnotationPresent(Id.class)) {
					fieldDescriptor.setId(true);
					
					if (entityDescriptor.getIdClass() != null) {
						// pull the column from IdClass
						try {
							Field idClassField = entityDescriptor.getIdClass().getDeclaredField(field.getName());
							idClassField.setAccessible(true);
							addColumnInformationToFieldDescriptor(fieldDescriptor, idClassField);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if (field.isAnnotationPresent(Column.class)) {
					
					if (!field.isAnnotationPresent(Id.class) || entityDescriptor.getIdClass() == null) {
						// only populate if we haven't populated already
						addColumnInformationToFieldDescriptor(fieldDescriptor, field);
					}
				} else if (!field.isAnnotationPresent(Id.class) || entityDescriptor.getIdClass() == null) {
					fieldDescriptor.setColumn(field.getName());
				}
				if (field.isAnnotationPresent(Version.class)) {
					fieldDescriptor.setVersion(true);
				}
				if (field.isAnnotationPresent(Lob.class)) {
					fieldDescriptor.setLob(true);
				}
				if (field.isAnnotationPresent(Temporal.class)) {
					fieldDescriptor.setTemporal(true);
					fieldDescriptor.setTemporalType(field.getAnnotation(Temporal.class).value());
				}				

				
				// Relationships
				if (field.isAnnotationPresent(OneToOne.class)) {
					OneToOneDescriptor descriptor = new OneToOneDescriptor();
					OneToOne relation = field.getAnnotation(OneToOne.class);
					descriptor.setAttributeName(field.getName());
					if (relation.targetEntity().equals(void.class)) {
						descriptor.setTargetEntity(field.getType());
					} else {
						descriptor.setTargetEntity(relation.targetEntity());
						fieldDescriptor.setTargetClazz(relation.targetEntity());
					}
					
					descriptor.setCascade(relation.cascade());
					descriptor.setFetch(relation.fetch());
					descriptor.setMappedBy(relation.mappedBy());
					descriptor.setOptional(relation.optional());
					if (field.isAnnotationPresent(JoinColumn.class)) {
						JoinColumn jc = field.getAnnotation(JoinColumn.class);
						descriptor.addJoinColumnDescriptor(constructJoinDescriptor(jc));
						FieldDescriptor jcFkField = entityDescriptor.getFieldByColumnName(jc.name());
						if (jcFkField != null) {
							descriptor.addFkField(jcFkField.getName());
						} else {
							//check to see if foreign key is in an AttributeOverride annotation
							if (clazz.isAnnotationPresent(AttributeOverrides.class)) {
								for (AttributeOverride override : ((AttributeOverrides)clazz.getAnnotation(AttributeOverrides.class)).value()) {
									if (jc.name().equals(override.column().name())) {
										entityDescriptor.getFieldByName(override.name()).setColumn(override.column().name());
										jcFkField = entityDescriptor.getFieldByName(override.name());
										if (jcFkField != null) {
											descriptor.addFkField(jcFkField.getName());
										}
									}
								}
							}
							if (clazz.isAnnotationPresent(AttributeOverride.class)) {
								AttributeOverride override = (AttributeOverride) clazz.getAnnotation(AttributeOverride.class);
								if (jc.name().equals(override.column().name())) {
									entityDescriptor.getFieldByName(override.name()).setColumn(override.column().name());
									jcFkField = entityDescriptor.getFieldByName(override.name());
									if (jcFkField != null) {
										descriptor.addFkField(jcFkField.getName());
									}
								}					
							}
						}
						//descriptor.addFkField(entityDescriptor.getFieldByColumnName(jc.name()).getName());
						descriptor.setInsertable(jc.insertable());
						descriptor.setUpdateable(jc.updatable());					
					}
					if (field.isAnnotationPresent(JoinColumns.class)) {
						JoinColumns jcs = field.getAnnotation(JoinColumns.class);
						for (JoinColumn jc : jcs.value()) {
							descriptor.addJoinColumnDescriptor(constructJoinDescriptor(jc));
							descriptor.addFkField(entityDescriptor.getFieldByColumnName(jc.name()).getName());
							descriptor.setInsertable(jc.insertable());
							descriptor.setUpdateable(jc.updatable());
						} 
					}
					entityDescriptor.add(descriptor);
				}

				if (field.isAnnotationPresent(OneToMany.class)) {
					OneToManyDescriptor descriptor = new OneToManyDescriptor();
					OneToMany relation = field.getAnnotation(OneToMany.class);
					descriptor.setAttributeName(field.getName());
					if (relation.targetEntity().equals(void.class)) {
						descriptor.setTargetEntity((Class)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]);
					} else {
						descriptor.setTargetEntity(relation.targetEntity());
						fieldDescriptor.setTargetClazz(relation.targetEntity());
					}
					descriptor.setCascade(relation.cascade());
					descriptor.setFetch(relation.fetch());
					descriptor.setMappedBy(relation.mappedBy());
					EntityDescriptor mappedBy = (entityDescriptor.getClazz().equals(descriptor.getTargetEntity())) ?
							entityDescriptor : MetadataManager.getEntityDescriptor(descriptor.getTargetEntity());
					ObjectDescriptor od = mappedBy.getObjectDescriptorByName(descriptor.getMappedBy());
					if (od != null) {
						for (String fk : od.getForeignKeyFields()) {				
							descriptor.addFkField(fk);
						}
					}
					if (field.isAnnotationPresent(JoinTable.class)) {
						JoinTable jt = field.getAnnotation(JoinTable.class);
						for (JoinColumn jc : jt.joinColumns()) {
							descriptor.addFkField(entityDescriptor.getFieldByColumnName(jc.name()).getName());
							descriptor.setInsertable(jc.insertable());
							descriptor.setUpdateable(jc.updatable());
							descriptor.addJoinColumnDescriptor(constructJoinDescriptor(jc));
						} 
						for (JoinColumn jc : jt.inverseJoinColumns()) {
							descriptor.setInsertable(jc.insertable());
							descriptor.setUpdateable(jc.updatable());
							descriptor.addInverseJoinColumnDescriptor(constructJoinDescriptor(jc));
						} 
					} else {
						if (field.isAnnotationPresent(JoinColumn.class)) {
							JoinColumn jc = field.getAnnotation(JoinColumn.class);
							FieldDescriptor jcFkField = entityDescriptor.getFieldByColumnName(jc.name());
							if (jcFkField != null) {
								descriptor.addFkField(jcFkField.getName());
							}
							descriptor.setInsertable(jc.insertable());
							descriptor.setUpdateable(jc.updatable());
							descriptor.addJoinColumnDescriptor(constructJoinDescriptor(jc));
						}
						if (field.isAnnotationPresent(JoinColumns.class)) {
							JoinColumns jcs = field.getAnnotation(JoinColumns.class);
							for (JoinColumn jc : jcs.value()) {
								descriptor.addFkField(entityDescriptor.getFieldByColumnName(jc.name()).getName());
								descriptor.setInsertable(jc.insertable());
								descriptor.setUpdateable(jc.updatable());
								descriptor.addJoinColumnDescriptor(constructJoinDescriptor(jc));
							} 
						}
					}
					entityDescriptor.add(descriptor);
				}

				if (field.isAnnotationPresent(ManyToOne.class)) {
					ManyToOne relation = field.getAnnotation(ManyToOne.class);
					ManyToOneDescriptor descriptor = new ManyToOneDescriptor();
					descriptor.setAttributeName(field.getName());
					if (relation.targetEntity().equals(void.class)) {
						descriptor.setTargetEntity(field.getType());
					} else {
						descriptor.setTargetEntity(relation.targetEntity());
						fieldDescriptor.setTargetClazz(relation.targetEntity());
					}
					descriptor.setCascade(relation.cascade());
					descriptor.setFetch(relation.fetch());
					descriptor.setOptional(relation.optional());
					if (field.isAnnotationPresent(JoinColumn.class)) {
						JoinColumn jc = field.getAnnotation(JoinColumn.class);
						descriptor.addJoinColumnDescriptor(constructJoinDescriptor(jc));
						FieldDescriptor jcFkField = entityDescriptor.getFieldByColumnName(jc.name());
						if (jcFkField != null) {
							descriptor.addFkField(jcFkField.getName());
						}
						//descriptor.addFkField(entityDescriptor.getFieldByColumnName(jc.name()).getName());
						//descriptor.addFkField(entitesByClass.get(field.getType()).getFieldByColumnName(jc.name()).getName());
						descriptor.setInsertable(jc.insertable());
						descriptor.setUpdateable(jc.updatable());
					}
					if (field.isAnnotationPresent(JoinColumns.class)) {
						JoinColumns jcs = field.getAnnotation(JoinColumns.class);
						for (JoinColumn jc : jcs.value()) {
							descriptor.addJoinColumnDescriptor(constructJoinDescriptor(jc));
							descriptor.addFkField(entityDescriptor.getFieldByColumnName(jc.name()).getName());
							descriptor.setInsertable(jc.insertable());
							descriptor.setUpdateable(jc.updatable());
						} 
					}
					entityDescriptor.add(descriptor);
				}

				if (field.isAnnotationPresent(ManyToMany.class)) {
					ManyToManyDescriptor descriptor = new ManyToManyDescriptor();
					ManyToMany relation = field.getAnnotation(ManyToMany.class);
					descriptor.setAttributeName(field.getName());
					if (relation.targetEntity().equals(void.class)) {
						descriptor.setTargetEntity((Class)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]);
					} else {
						descriptor.setTargetEntity(relation.targetEntity());
						fieldDescriptor.setTargetClazz(relation.targetEntity());
					}
					descriptor.setCascade(relation.cascade());
					descriptor.setFetch(relation.fetch());
					descriptor.setMappedBy(relation.mappedBy());
					if (field.isAnnotationPresent(JoinTable.class)) {
						JoinTable jt = field.getAnnotation(JoinTable.class);
						descriptor.setJoinTableName(jt.name());
						for (JoinColumn jc : jt.joinColumns()) {
							descriptor.addJoinColumnDescriptor(constructJoinDescriptor(jc));
							descriptor.addFkField(entityDescriptor.getFieldByColumnName(jc.name()).getName());
							descriptor.setInsertable(jc.insertable());
							descriptor.setUpdateable(jc.updatable());
						} 
						for (JoinColumn jc : jt.inverseJoinColumns()) {
							descriptor.addInverseJoinColumnDescriptor(constructJoinDescriptor(jc));
							descriptor.setInsertable(jc.insertable());
							descriptor.setUpdateable(jc.updatable());
							// TODO: Should we add inverse join columns?
						} 
					} else {
						if (field.isAnnotationPresent(JoinColumn.class)) {
							JoinColumn jc = field.getAnnotation(JoinColumn.class);
							FieldDescriptor jcFkField = entityDescriptor.getFieldByColumnName(jc.name());
							if (jcFkField != null) {
								descriptor.addFkField(jcFkField.getName());
							}
							descriptor.addJoinColumnDescriptor(constructJoinDescriptor(jc));
							descriptor.setInsertable(jc.insertable());
							descriptor.setUpdateable(jc.updatable());
						}
						if (field.isAnnotationPresent(JoinColumns.class)) {
							JoinColumns jcs = field.getAnnotation(JoinColumns.class);
							for (JoinColumn jc : jcs.value()) {
								descriptor.addJoinColumnDescriptor(constructJoinDescriptor(jc));
								descriptor.addFkField(entityDescriptor.getFieldByColumnName(jc.name()).getName());
								descriptor.setInsertable(jc.insertable());
								descriptor.setUpdateable(jc.updatable());
							} 
						}
					}
					entityDescriptor.add(descriptor);						
				}

				// Add the field to the entity
				entityDescriptor.add(fieldDescriptor);
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null && !(clazz.equals(Object.class)));
	}
	
	/**
	 * Populate a FieldDescriptor with Column annotation information
	 * 
	 * @param fieldDescriptor the FieldDescriptor to populate
	 * @param field the field which has the annotation
	 */
	private static void addColumnInformationToFieldDescriptor(FieldDescriptor fieldDescriptor, Field field) {
		Column column = field.getAnnotation(Column.class);
		fieldDescriptor.setColumn(column.name());
		fieldDescriptor.setInsertable(column.insertable());
		fieldDescriptor.setLength(column.length());
		fieldDescriptor.setNullable(column.nullable());
		fieldDescriptor.setPrecision(column.precision());
		fieldDescriptor.setScale(column.scale());
		fieldDescriptor.setUnique(column.unique());
		fieldDescriptor.setUpdateable(column.updatable());
	}

	private static JoinColumnDescriptor constructJoinDescriptor(JoinColumn jc) {
		JoinColumnDescriptor join = new JoinColumnDescriptor();
		if (StringUtils.isBlank(jc.name())) {
			// TODO: Implement default name
			// See: http://www.oracle.com/technology/products/ias/toplink/jpa/resources/toplink-jpa-annotations.html#JoinColumn
			throw new RuntimeException("Default name for Join Column not yet implemented!");
		} else {
			join.setName(jc.name());
		}
		join.setInsertable(jc.insertable());
		join.setNullable(jc.nullable());
		join.setUnique(jc.unique());
		join.setUpdateable(jc.updatable());
		join.setReferencedColumName(jc.referencedColumnName());
		return join;
	}
	
}
