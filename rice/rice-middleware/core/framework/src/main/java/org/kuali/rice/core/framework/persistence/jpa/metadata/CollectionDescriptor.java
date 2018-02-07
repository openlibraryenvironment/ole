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

public class CollectionDescriptor {
	
	protected String attributeName;
	protected Class targetEntity;
	protected CascadeType [] cascade = new CascadeType[0];
	protected FetchType fetch = FetchType.LAZY;
	protected String mappedBy = "";
	protected List<String> fkFields = new ArrayList<String>();
	protected List<JoinColumnDescriptor> joinColumnDescriptors = new ArrayList<JoinColumnDescriptor>();
	protected java.util.List<JoinColumnDescriptor> inverseJoinColumnDescriptors = new ArrayList<JoinColumnDescriptor>();
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

	public void addFkField(String fk) {
		fkFields.add(fk);
	}
	
	public List<String> getForeignKeyFields() {
		return fkFields;
	}
	
	public FetchType getFetch() {
		return this.fetch;
	}
	
	public void setFetch(FetchType fetch) {
		this.fetch = fetch;
	}

	public String getMappedBy() {
		return this.mappedBy;
	}

	public void setMappedBy(String mappedBy) {
		this.mappedBy = mappedBy;
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

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * @return the joinColumnDescriptors
	 */
	public List<JoinColumnDescriptor> getJoinColumnDescriptors() {
		return this.joinColumnDescriptors;
	}
	
	public void addJoinColumnDescriptor(JoinColumnDescriptor jcd) {
		joinColumnDescriptors.add(jcd);
	}
	
	public void addInverseJoinColumnDescriptor(JoinColumnDescriptor joinColumnDescriptor) {
		inverseJoinColumnDescriptors.add(joinColumnDescriptor);
	}
	
	public List<JoinColumnDescriptor> getInverseJoinColumnDescriptors() {
		return inverseJoinColumnDescriptors;
	}
}
