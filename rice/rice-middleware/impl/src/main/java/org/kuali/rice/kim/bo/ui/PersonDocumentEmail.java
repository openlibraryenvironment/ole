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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.kim.api.identity.CodedAttributeContract;
import org.kuali.rice.kim.impl.identity.email.EntityEmailTypeBo;


import javax.persistence.*;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@IdClass(PersonDocumentEmailId.class)
@Entity
@Table(name = "KRIM_PND_EMAIL_MT")
public class PersonDocumentEmail extends PersonDocumentBoDefaultBase{
	@Id
	@GeneratedValue(generator="KRIM_ENTITY_EMAIL_ID_S")
	@GenericGenerator(name="KRIM_ENTITY_EMAIL_ID_S",strategy="org.kuali.rice.core.jpa.spring.RiceNumericStringSequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KRIM_ENTITY_EMAIL_ID_S"),
			@Parameter(name="value_column",value="id")
		})
	@Column(name = "ENTITY_EMAIL_ID")
	protected String entityEmailId;

	@Column(name = "ENT_TYP_CD")
	protected String entityTypeCode;

	@Column(name = "EMAIL_TYP_CD")
	protected String emailTypeCode;

	@Column(name = "EMAIL_ADDR")
	protected String emailAddress;

	@ManyToOne(targetEntity=EntityEmailTypeBo.class, fetch = FetchType.EAGER, cascade = {})
	@JoinColumn(name = "EMAIL_TYP_CD", insertable = false, updatable = false)
	protected EntityEmailTypeBo emailType;
	
	public PersonDocumentEmail() {
		this.active = true;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.email.EntityEmailContract#getEmailAddress()
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.email.EntityEmailContract#getEmailType()
	 */
	public String getEmailTypeCode() {
		return emailTypeCode;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.email.EntityEmailContract#getId()
	 */
	public String getEntityEmailId() {
		return entityEmailId;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.email.EntityEmailContract#setEmailAddress(java.lang.String)
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.email.EntityEmailContract#setEmailType(java.lang.String)
	 */
	public void setEmailTypeCode(String emailTypeCode) {
		this.emailTypeCode = emailTypeCode;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimDefaultableEntityTypeData#getEntityTypeCode()
	 */
	public String getEntityTypeCode() {
		return entityTypeCode;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimDefaultableEntityTypeData#setEntityTypeCode(java.lang.String)
	 */
	public void setEntityTypeCode(String entityTypeCode) {
		this.entityTypeCode = entityTypeCode;
	}

	public void setEntityEmailId(String entityEmailId) {
		this.entityEmailId = entityEmailId;
	}

	public EntityEmailTypeBo getEmailType() {
		return this.emailType;
	}

	public void setEmailType(EntityEmailTypeBo emailType) {
		this.emailType = emailType;
	}

}
