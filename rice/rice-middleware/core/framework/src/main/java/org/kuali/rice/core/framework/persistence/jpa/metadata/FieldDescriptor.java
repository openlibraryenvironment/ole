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

import javax.persistence.TemporalType;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FieldDescriptor implements Comparable, Serializable {

	private static final long serialVersionUID = -4231007918447422886L;
	
	private String name;
	private Class clazz;
	private Class targetClazz;
	private String column;
	private int length = 255;
	private int scale = 0;
	private int precision = 0;
	private boolean id = false;
	private boolean lob = false;
	private boolean temporal = false;
	private boolean version = false;
	private boolean unique = false;
	private boolean nullable = true;
	private boolean insertable = true;
	private boolean updateable = true;
	private TemporalType temporalType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Class getClazz() {
		return clazz;
	}
	
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	
	public Class getTargetClazz() {
		return targetClazz;
	}
	
	public void setTargetClazz(Class targetClazz) {
		this.targetClazz = targetClazz;
	}
	
	public String getColumn() {
		return column;
	}
	
	public void setColumn(String column) {
		this.column = column;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public int getScale() {
		return scale;
	}
	
	public void setScale(int scale) {
		this.scale = scale;
	}
	
	public int getPrecision() {
		return precision;
	}
	
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	
	public boolean isId() {
		return id;
	}
	
	public void setId(boolean id) {
		this.id = id;
	}
	
	public boolean isLob() {
		return lob;
	}
	
	public void setLob(boolean lob) {
		this.lob = lob;
	}
	
	public boolean isTemporal() {
		return temporal;
	}
	
	public void setTemporal(boolean temporal) {
		this.temporal = temporal;
	}
	
	public boolean isVersion() {
		return version;
	}
	
	public void setVersion(boolean version) {
		this.version = version;
	}
	
	public boolean isUnique() {
		return unique;
	}
	
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	
	public boolean isNullable() {
		return nullable;
	}
	
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	
	public boolean isInsertable() {
		return insertable;
	}
	
	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}
	
	public boolean isUpdateable() {
		return updateable;
	}
	
	public void setUpdateable(boolean updateable) {
		this.updateable = updateable;
	}
	
	public TemporalType getTemporalType() {
		return temporalType;
	}
	
	public void setTemporalType(TemporalType temporalType) {
		this.temporalType = temporalType;
	}
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("FieldDescriptor = [ ");
		sb.append("name:").append(name).append(", ");
		sb.append("column:").append(column).append(", ");
		sb.append("class:").append(clazz).append(", ");
		sb.append("targetClass:").append(targetClazz).append(", ");
		sb.append("length:").append(length).append(", ");
		sb.append("scale:").append(scale).append(", ");
		sb.append("precision:").append(precision).append(", ");
		sb.append("unique:").append(unique).append(", ");
		sb.append("nullable:").append(nullable).append(", ");
		sb.append("insertable:").append(insertable).append(", ");
		sb.append("updateable:").append(updateable).append(", ");
		sb.append("lob:").append(lob).append(", ");
		sb.append("version:").append(version).append(", ");
		sb.append("temporal:").append(temporal);
		if (temporal) {
			sb.append(", temporalType:").append(temporalType);
		}
		sb.append(" ]");
		return sb.toString();
	}

	/**
	 * This overridden method ...
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object that) {
		if (that == null || !(that instanceof FieldDescriptor)) {
			return -1;
		}		
		if (this.getName() == null || ((FieldDescriptor)that).getName() == null) {
			return 0;
		}
		return this.getName().compareTo(((FieldDescriptor)that).getName());
	}

}
