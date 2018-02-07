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

import org.kuali.rice.kim.impl.identity.external.EntityExternalIdentifierTypeBo;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
/* 
@IdClass(PersonDocumentExternalIdId.class)
@Entity
@Table(name = "KRIM_PND_EXT_ID_MT") */
public class PersonDocumentExternalId extends KimDocumentBoBase{
	
	private static final long serialVersionUID = 1L;

/*	@Id
	@Column(name = "ENTITY_EXT_ID_ID") */
	protected String entityExternalIdentifierId;
	
	//@Column(name = "EXT_ID_TYP_CD")
	protected String externalIdentifierTypeCode;

	//@Column(name = "EXT_ID")
	protected String externalId;
	
	protected EntityExternalIdentifierTypeBo externalIdentifierType;

	/**
	 * @see org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierContract#getEntityExternalIdentifierId()
	 */
	public String getEntityExternalIdentifierId() {
		return entityExternalIdentifierId;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierContract#getExternalId()
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierContract#getExternalIdentifierTypeCode()
	 */
	public String getExternalIdentifierTypeCode() {
		return externalIdentifierTypeCode;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierContract#setExternalId(java.lang.String)
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierContract#setExternalIdentifierTypeCode(java.lang.String)
	 */
	public void setExternalIdentifierTypeCode(String externalIdentifierTypeCode) {
		this.externalIdentifierTypeCode = externalIdentifierTypeCode;
	}

	public void setEntityExternalIdentifierId(String entityExternalIdentifierId) {
		this.entityExternalIdentifierId = entityExternalIdentifierId;
	}

	public EntityExternalIdentifierTypeBo getExternalIdentifierType() {
		return this.externalIdentifierType;
	}

	public void setExternalIdentifierType(EntityExternalIdentifierTypeBo externalIdentifierType) {
		this.externalIdentifierType = externalIdentifierType;
	}

}
