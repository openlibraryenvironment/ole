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

import org.hibernate.annotations.Type;
import org.kuali.rice.kim.document.IdentityManagementRoleDocument;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@MappedSuperclass
public class RoleDocumentBoBase  extends PersistableBusinessObjectBase {
    @Column(name="DOC_HDR_ID")
    protected String documentNumber;
	@Type(type="yes_no")
	@Column(name="ACTV_IND")
    protected boolean active;
	@Type(type="yes_no")
	@Column(name="EDIT_FLAG")
    protected boolean edit;
	@Transient
	protected IdentityManagementRoleDocument roleDocument;

	public String getDocumentNumber() {
		return this.documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public IdentityManagementRoleDocument getRoleDocument() {
		return this.roleDocument;
	}

	public void setRoleDocument(IdentityManagementRoleDocument roleDocument) {
		this.roleDocument = roleDocument;
	}

	public boolean isEdit() {
		return this.edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

}
