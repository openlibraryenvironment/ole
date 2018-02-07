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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentStatusBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentTypeBo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@IdClass(PersonDocumentEmploymentInfoId.class)
@Entity
@Table(name = "KRIM_PND_EMP_INFO_MT")
public class PersonDocumentEmploymentInfo extends KimDocumentBoActivatableEditableBase {
	@Id
	@GeneratedValue(generator="KRIM_ENTITY_EMP_ID_S")
	@GenericGenerator(name="KRIM_ENTITY_EMP_ID_S",strategy="org.kuali.rice.core.jpa.spring.RiceNumericStringSequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KRIM_ENTITY_EMP_ID_S"),
			@Parameter(name="value_column",value="id")
		})
	@Column(name = "ENTITY_EMP_ID")
	protected String entityEmploymentId;

	@Column(name = "ENTITY_AFLTN_ID")
	protected String entityAffiliationId;

	@Column(name = "EMP_STAT_CD")
	protected String employmentStatusCode;

	@Column(name = "EMP_TYP_CD")
	protected String employmentTypeCode;

	@Column(name = "PRMRY_DEPT_CD")
	protected String primaryDepartmentCode;
	
	@Column(name = "BASE_SLRY_AMT")
	protected KualiDecimal baseSalaryAmount;
	@Column(name = "EMP_ID")
	protected String employeeId;

	@Column(name = "EMP_REC_ID")
	protected String employmentRecordId;

	@Type(type="yes_no")
	@Column(name="PRMRY_IND")
	protected boolean primary;

	@ManyToOne(targetEntity=EntityEmploymentTypeBo.class, fetch = FetchType.EAGER, cascade = {})
	@JoinColumn(name = "EMP_TYP_CD", insertable = false, updatable = false)
	protected EntityEmploymentTypeBo employmentType;

	@ManyToOne(targetEntity=EntityEmploymentStatusBo.class, fetch = FetchType.EAGER, cascade = {})
	@Fetch(value = FetchMode.SELECT)
	@JoinColumn(name = "EMP_STAT_CD", insertable = false, updatable = false)
	protected EntityEmploymentStatusBo employmentStatus;
	@Transient
	protected PersonDocumentAffiliation affiliation;
	
	public PersonDocumentEmploymentInfo() {
		this.active = true;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract#getBaseSalaryAmount()
	 */
	public KualiDecimal getBaseSalaryAmount() {
		return baseSalaryAmount;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract#getEmployeeStatusCode()
	 */
	public String getEmploymentStatusCode() {
		return employmentStatusCode;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract#getEmploymentTypeCode()
	 */
	public String getEmploymentTypeCode() {
		return employmentTypeCode;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract#getEntityAffiliationId()
	 */
	public String getEntityAffiliationId() {
		return entityAffiliationId;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract#getEntityEmploymentId()
	 */
	public String getEntityEmploymentId() {
		return entityEmploymentId;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract#isPrimary()
	 */
	public boolean isPrimary() {
		return primary;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract#setAffiliationId(java.lang.String)
	 */
	public void setEntityAffiliationId(String entityAffiliationId) {
		this.entityAffiliationId = entityAffiliationId;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract#setBaseSalaryAmount(java.math.BigDecimal)
	 */
	public void setBaseSalaryAmount(KualiDecimal baseSalaryAmount) {
		this.baseSalaryAmount = baseSalaryAmount;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract#setEmployeeStatusCode(java.lang.String)
	 */
	public void setEmploymentStatusCode(String employmentStatusCode) {
		this.employmentStatusCode = employmentStatusCode;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract#setEmploymentTypeCode(java.lang.String)
	 */
	public void setEmploymentTypeCode(String employmentTypeCode) {
		this.employmentTypeCode = employmentTypeCode;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract#setPrimary(boolean)
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public void setEntityEmploymentId(String entityEmploymentId) {
		this.entityEmploymentId = entityEmploymentId;
	}

	public EntityEmploymentTypeBo getEmploymentType() {
		return this.employmentType;
	}

	public void setEmploymentType(EntityEmploymentTypeBo employmentType) {
		this.employmentType = employmentType;
	}

	public EntityEmploymentStatusBo getEmploymentStatus() {
		return this.employmentStatus;
	}

	public void setEmploymentStatus(EntityEmploymentStatusBo employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	public String getPrimaryDepartmentCode() {
		return this.primaryDepartmentCode;
	}

	public void setPrimaryDepartmentCode(String primaryDepartmentCode) {
		this.primaryDepartmentCode = primaryDepartmentCode;
	}

	public PersonDocumentAffiliation getAffiliation() {
		return this.affiliation;
	}

	public void setAffiliation(PersonDocumentAffiliation affiliation) {
		this.affiliation = affiliation;
	}

	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmploymentRecordId() {
		return this.employmentRecordId;
	}

	public void setEmploymentRecordId(String employmentRecordId) {
		this.employmentRecordId = employmentRecordId;
	}

}
