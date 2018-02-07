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

import javax.persistence.*;
import java.sql.Date;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@IdClass(PersonDocumentCitizenshipId.class)
@Entity
@Table(name = "KRIM_PND_CTZNSHP_MT")
public class PersonDocumentCitizenship extends KimDocumentBoActivatableEditableBase {
	@Id
	@GeneratedValue(generator="KRIM_ENTITY_CTZNSHP_ID_S")
	@GenericGenerator(name="KRIM_ENTITY_CTZNSHP_ID_S",strategy="org.kuali.rice.core.jpa.spring.RiceNumericStringSequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KRIM_ENTITY_CTZNSHP_ID_S"),
			@Parameter(name="value_column",value="id")
		})
	@Column(name = "ENTITY_CTZNSHP_ID")
	protected String entityCitizenshipId;
	
	@Column(name = "ENTITY_ID")
	protected String entityId;
	
	@Column(name = "POSTAL_CNTRY_CD")
	protected String countryCode;

	@Column(name = "CTZNSHP_STAT_CD")
	protected String citizenshipStatusCode;

	@Column(name = "strt_dt")
	protected Date startDate;

	@Column(name = "end_dt")
	protected Date endDate;

	
	/**
	 * @see org.kuali.rice.kim.api.identity.citizenship.EntityCitizenshipContract#getCitizenshipStatusCode()
	 */
	public String getCitizenshipStatusCode() {
		return citizenshipStatusCode;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.citizenship.EntityCitizenshipContract#getEndDate()
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.citizenship.EntityCitizenshipContract#getId()
	 */
	public String getEntityCitizenshipId() {
		return entityCitizenshipId;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.citizenship.EntityCitizenshipContract#getStartDate()
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.citizenship.EntityCitizenshipContract#setStatusCode(java.lang.String)
	 */
	public void setCitizenshipStatusCode(String citizenshipStatusCode) {
		this.citizenshipStatusCode = citizenshipStatusCode;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.citizenship.EntityCitizenshipContract#setEndDate(java.util.Date)
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.citizenship.EntityCitizenshipContract#startDate(java.util.Date)
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public String getEntityId() {
		return this.entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setEntityCitizenshipId(String entityCitizenshipId) {
		this.entityCitizenshipId = entityCitizenshipId;
	}
}
