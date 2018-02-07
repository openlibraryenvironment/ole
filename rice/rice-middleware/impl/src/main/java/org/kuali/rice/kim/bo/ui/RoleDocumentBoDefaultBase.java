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
package org.kuali.rice.kim.bo.ui;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.kuali.rice.kim.document.IdentityManagementRoleDocument;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@MappedSuperclass
public class RoleDocumentBoDefaultBase extends KimDocumentBoBase{
    @Type(type="yes_no")
	@Column(name="DFLT_IND")
	protected boolean dflt;

    @Transient
	protected IdentityManagementRoleDocument roleDocument;
	
	/**
	 * @return the roleDocument
	 */
	public IdentityManagementRoleDocument getRoleDocument() {
		return this.roleDocument;
	}

	/**
	 * @param roleDocument the roleDocument to set
	 */
	public void setRoleDocument(IdentityManagementRoleDocument roleDocument) {
		this.roleDocument = roleDocument;
	}

	public boolean isDflt() {
		return this.dflt;
	}

	public void setDflt(boolean dflt) {
		this.dflt = dflt;
	}

}
