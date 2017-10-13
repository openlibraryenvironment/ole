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

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class JoinColumnDescriptor implements java.io.Serializable {

	private static final long serialVersionUID = -9127161936848994757L;

	private String name = "";
	private boolean unique = false;
	private boolean nullable = true;
	private boolean insertable = true;
	private boolean updateable = true;
	private String referencedColumName = "";
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isUnique() {
		return this.unique;
	}
	
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	
	public boolean isNullable() {
		return this.nullable;
	}
	
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	
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

	/**
	 * @return the referencedColumName
	 */
	public String getReferencedColumName() {
		return this.referencedColumName;
	}

	/**
	 * @param referencedColumName the referencedColumName to set
	 */
	public void setReferencedColumName(String referencedColumName) {
		this.referencedColumName = referencedColumName;
	}
		
}
