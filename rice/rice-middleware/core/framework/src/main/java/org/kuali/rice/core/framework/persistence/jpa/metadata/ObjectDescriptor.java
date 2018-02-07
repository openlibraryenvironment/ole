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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;

/**
 * Base class for the "one" side of a relationship.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ObjectDescriptor implements java.io.Serializable {

	private static final long serialVersionUID = -6414242817173646832L;
	
	protected String attributeName;
	protected Class targetEntity;
	protected CascadeType [] cascade = new CascadeType[0];
	protected FetchType fetch = FetchType.EAGER;
	protected boolean optional = true;
	protected List<JoinColumnDescriptor> joinColumnDescriptors = new ArrayList<JoinColumnDescriptor>();
	protected List<String> fkFields = new ArrayList<String>();
	protected boolean insertable;
	protected boolean updateable;
	
	public boolean isInsertable() {
		return this.insertable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	public boolean isUpdateable() {
		return this.updateable;
	}

	public void setUpdateable(boolean updateable) {
		this.updateable = updateable;
	}

	public Class getTargetEntity() {
		return this.targetEntity;
	}
	
	public void setTargetEntity(Class targetEntity) {
		this.targetEntity = targetEntity;
	}
	
	public CascadeType[] getCascade() {
		return this.cascade;
	}
	
	public void setCascade(CascadeType[] cascade) {
		this.cascade = cascade;
	}
	
	public FetchType getFetch() {
		return this.fetch;
	}
	
	public void setFetch(FetchType fetch) {
		this.fetch = fetch;
	}
	
	public boolean isOptional() {
		return this.optional;
	}
	
	public void setOptional(boolean optional) {
		this.optional = optional;
	}
	
	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	
	public void addJoinColumnDescriptor(JoinColumnDescriptor joinColumnDescriptor) {
		joinColumnDescriptors.add(joinColumnDescriptor);
	}

	public void addFkField(String fk) {
		fkFields.add(fk);
	}

	public List<JoinColumnDescriptor> getJoinColumnDescriptors() {
		return joinColumnDescriptors;
	}
	
	public List<String> getForeignKeyFields() {
		return fkFields;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ObjectDescriptor = [ ");
		sb.append("targetEntity:").append(targetEntity.getName()).append(", ");
		sb.append("cascade = { ");
		for (CascadeType ct : cascade) {
			sb.append(ct).append(" ");
		}
		sb.append("}, ");
		sb.append("fetch:").append(fetch).append(", ");
		sb.append("optional:").append(optional);
		if (!joinColumnDescriptors.isEmpty()) {
			sb.append(", join columns = { ");
			for (JoinColumnDescriptor joinColumnDescriptor : joinColumnDescriptors) {				
				sb.append(" jc = { ");
				sb.append("name:").append(joinColumnDescriptor.getName()).append(", ");
				sb.append("insertable:").append(joinColumnDescriptor.isInsertable()).append(", ");
				sb.append("nullable:").append(joinColumnDescriptor.isNullable()).append(", ");
				sb.append("unique:").append(joinColumnDescriptor.isUnique()).append(", ");
				sb.append("updateable:").append(joinColumnDescriptor.isUpdateable());
				sb.append(" }");
			}
			sb.append(" } ");
		}
		sb.append(" ]");
		return sb.toString();
	}
	
}
