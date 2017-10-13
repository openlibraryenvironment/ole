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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kuali.rice.core.framework.persistence.jpa.annotations.Sequence;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EntityDescriptor implements Serializable {

	private static final long serialVersionUID = -4209120979389982233L;
	
	private String name;
	private String table;
	private Class clazz;
	private Class idClass;
	private Sequence sequence;
	
	private Map<String, FieldDescriptor> fieldsByName = new HashMap<String, FieldDescriptor>();
	private Map<String, FieldDescriptor> fieldsByColumnName = new HashMap<String, FieldDescriptor>();

	private Set<FieldDescriptor> fields = new LinkedHashSet<FieldDescriptor>();
	private Set<FieldDescriptor> nonKeyFields = new LinkedHashSet<FieldDescriptor>();
	private Set<FieldDescriptor> primaryKeys = new LinkedHashSet<FieldDescriptor>();
	
	// Type these after creating classes
	private Set<OneToOneDescriptor> oneToOneRelationships = new LinkedHashSet<OneToOneDescriptor>();
	private Set<OneToManyDescriptor> oneToManyRelationships = new LinkedHashSet<OneToManyDescriptor>();
	private Set<ManyToOneDescriptor> manyToOneRelationships = new LinkedHashSet<ManyToOneDescriptor>();
	private Set<ManyToManyDescriptor> manyToManyRelationships = new LinkedHashSet<ManyToManyDescriptor>();

	// The "one" side relations (one-to-one and many-to-one)
	private List<ObjectDescriptor> objectRelationships = new ArrayList<ObjectDescriptor>();

	// The "collection" relations (one-to-many and many-to-many)
	private List<CollectionDescriptor> collectionRelationships = new ArrayList<CollectionDescriptor>();

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;	
	}
	
	public String getTable() {
		return table;
	}
	
	public void setTable(String table) {
		this.table = table;
	}
	
	public Class getClazz() {
		return clazz;
	}
	
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	
	public Class getIdClass() {
		return idClass;
	}
	
	public void setIdClass(Class idClass) {
		this.idClass = idClass;
	}
	
	public Sequence getSequence() {
		return sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}
	
	public FieldDescriptor getFieldByName(String name) {
		return fieldsByName.get(name.toUpperCase());
	}
	
	public FieldDescriptor getFieldByColumnName(String name) {
		return fieldsByColumnName.get(name.toUpperCase());
	}
	
	public void add(FieldDescriptor fieldDescriptor) {
		if (fieldsByName.containsKey(fieldDescriptor.getName().toUpperCase())) {
			return;
		}
		fieldsByName.put(fieldDescriptor.getName().toUpperCase(), fieldDescriptor);
		fieldsByColumnName.put(fieldDescriptor.getColumn().toUpperCase(), fieldDescriptor);
		fields.add(fieldDescriptor);
		if (fieldDescriptor.isId()) {
			primaryKeys.add(fieldDescriptor);
		} else {
			nonKeyFields.add(fieldDescriptor);
		}
	}

	public void add(OneToOneDescriptor relation) {
		oneToOneRelationships.add(relation);
		objectRelationships.add(relation);
	}

	public void add(OneToManyDescriptor relation) {
		oneToManyRelationships.add(relation);
		collectionRelationships.add(relation);
	}

	public void add(ManyToOneDescriptor relation) {
		manyToOneRelationships.add(relation);
		objectRelationships.add(relation);
	}

	public void add(ManyToManyDescriptor relation) {
		manyToManyRelationships.add(relation);
		collectionRelationships.add(relation);
	}

	// TODO: Cache by name and probably by class
	public ObjectDescriptor getObjectDescriptorByName(String attributeName) {
		if (attributeName == null)  {
			return null;
		}
		for (ObjectDescriptor od : objectRelationships) {
			if (od.getAttributeName().equals(attributeName)) {
				return od;
			}
		}
		return null;
	}

	// TODO: Cache by name and probably by class
	public CollectionDescriptor getCollectionDescriptorByName(String attributeName) {
		if (attributeName == null)  {
			return null;
		}
		for (CollectionDescriptor cd : collectionRelationships) {
			if (cd.getAttributeName().equals(attributeName)) {
				return cd;
			}
		}
		return null;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("EntityDescriptor = [");
		sb.append("\n   name:     ").append(name);
		sb.append("\n   table:    ").append(table);
		sb.append("\n   class:    ").append(clazz.getName());
		if (idClass != null) {
			sb.append("\n   id class: ").append(idClass.getName());
		}
		if (sequence != null) {
			sb.append("\n   sequence: ").append(sequence.property()).append(" -> ").append(sequence.name());
		}
		if (!primaryKeys.isEmpty()) {
			sb.append("\n   primary keys = {");
			for (FieldDescriptor pk : primaryKeys) {
				sb.append("\n                     ");
				sb.append(pk.toString());
			}
			sb.append("\n                  }");
		}
		if (!fields.isEmpty()) {
			sb.append("\n   fields =       {");
			for (FieldDescriptor field : fields) {
				sb.append("\n                     ");
				sb.append(field.toString());
			}
			sb.append("\n                  }");
		}
		if (!oneToOneRelationships.isEmpty()) {
			sb.append("\n   one-to-one =   {");
			for (OneToOneDescriptor relation : oneToOneRelationships) {
				sb.append("\n                     ");
				sb.append(relation.toString());
			}
			sb.append("\n                  }");
		}
		if (!manyToOneRelationships.isEmpty()) {
			sb.append("\n   many-to-one =  {");
			for (ManyToOneDescriptor relation : manyToOneRelationships) {
				sb.append("\n                     ");
				sb.append(relation.toString());
			}
			sb.append("\n                  }");
		}
		if (!oneToManyRelationships.isEmpty()) {
			sb.append("\n   one-to-many =  {");
			for (OneToManyDescriptor relation : oneToManyRelationships) {
				sb.append("\n                     ");
				sb.append(relation.toString());
			}
			sb.append("\n                  }");
		}
		if (!manyToManyRelationships.isEmpty()) {
			sb.append("\n   many-to-many = {");
			for (ManyToManyDescriptor relation : manyToManyRelationships) {
				sb.append("\n                     ");
				sb.append(relation.toString());
			}
			sb.append("\n                  }");
		}
		sb.append("\n]");
		return sb.toString();
	}

	public Set<FieldDescriptor> getFields() {
		return this.fields;
	}

	public Set<FieldDescriptor> getNonKeyFields() {
		return this.nonKeyFields;
	}

	public Set<FieldDescriptor> getPrimaryKeys() {
		return this.primaryKeys;
	}

	public Set<OneToOneDescriptor> getOneToOneRelationships() {
		return this.oneToOneRelationships;
	}

	public Set<OneToManyDescriptor> getOneToManyRelationships() {
		return this.oneToManyRelationships;
	}

	public Set<ManyToOneDescriptor> getManyToOneRelationships() {
		return this.manyToOneRelationships;
	}

	public Set<ManyToManyDescriptor> getManyToManyRelationships() {
		return this.manyToManyRelationships;
	}

	public List<ObjectDescriptor> getObjectRelationships() {
		return this.objectRelationships;
	}

	public List<CollectionDescriptor> getCollectionRelationships() {
		return this.collectionRelationships;
	}

}
