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
package org.kuali.rice.kim.impl.identity.employment

import javax.persistence.Column
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import org.hibernate.annotations.Type
import org.kuali.rice.core.api.util.type.KualiDecimal
import org.kuali.rice.kim.api.identity.employment.EntityEmployment
import org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

class EntityEmploymentBo extends PersistableBusinessObjectBase implements EntityEmploymentContract {
    	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ENTITY_EMP_ID")
	String id;

	@Column(name = "ENTITY_ID")
	String entityId;

	@Column(name = "EMP_ID")
	String employeeId;

	@Column(name = "EMP_REC_ID")
	String employmentRecordId;

	@Column(name = "ENTITY_AFLTN_ID")
	String entityAffiliationId;

	@Column(name = "EMP_STAT_CD")
	String employeeStatusCode;

	@Column(name = "EMP_TYP_CD")
	String employeeTypeCode;

	@Column(name = "PRMRY_DEPT_CD")
	String primaryDepartmentCode;
	
	@Type(type="rice_decimal")
	@Column(name = "BASE_SLRY_AMT")
	KualiDecimal baseSalaryAmount;

	@Type(type="yes_no")
	@Column(name="PRMRY_IND")
	boolean primary;

    @Type(type="yes_no")
    @Column(name="ACTV_IND")
    boolean active;

	@ManyToOne(targetEntity=EntityEmploymentTypeBo.class, fetch=FetchType.EAGER, cascade = [])
	@JoinColumn(name="EMP_TYP_CD", insertable = false, updatable = false)
	EntityEmploymentTypeBo employeeType;

	@ManyToOne(targetEntity=EntityEmploymentStatusBo.class, fetch = FetchType.EAGER, cascade = [])
	@JoinColumn(name="EMP_STAT_CD", insertable = false, updatable = false)
	EntityEmploymentStatusBo employeeStatus;
	
	@ManyToOne(targetEntity=EntityAffiliationBo.class, fetch = FetchType.EAGER, cascade = [])
	@JoinColumn(name="ENTITY_AFLTN_ID", insertable = false, updatable = false)
	EntityAffiliationBo entityAffiliation;

    @Override
    public EntityAffiliationBo getEntityAffiliation() {
        return this.entityAffiliation
    }

    @Override
    public EntityEmploymentStatusBo getEmployeeStatus() {
        return this.employeeStatus
    }

    @Override
    public EntityEmploymentTypeBo getEmployeeType() {
        return this.employeeType
    }

      /*
       * Converts a mutable EntityEmploymentBo to an immutable EntityEmployment representation.
       * @param bo
       * @return an immutable EntityEmployment
       */
      static EntityEmployment to(EntityEmploymentBo bo) {
        if (bo == null) { return null }
        return EntityEmployment.Builder.create(bo).build()
      }

      /**
       * Creates a EntityEmploymentBo business object from an immutable representation of a EntityEmployment.
       * @param an immutable EntityEmployment
       * @return a EntityEmploymentBo
       */
      static EntityEmploymentBo from(EntityEmployment immutable) {
        if (immutable == null) {return null}

        EntityEmploymentBo bo = new EntityEmploymentBo()
        bo.id = immutable.id
        bo.active = immutable.active

        bo.entityId = immutable.entityId
        if (immutable.employeeType != null) {
            bo.employeeTypeCode = immutable.employeeType.code
            bo.employeeType = EntityEmploymentTypeBo.from(immutable.employeeType)
        }
        if (immutable.employeeStatus != null) {
            bo.employeeStatusCode = immutable.employeeStatus.code
            bo.employeeStatus = EntityEmploymentStatusBo.from(immutable.employeeStatus)
        }
        if (immutable.getEntityAffiliation() != null) {
            bo.entityAffiliationId = immutable.getEntityAffiliation().getId()
            bo.entityAffiliation = EntityAffiliationBo.from(immutable.getEntityAffiliation())
        }

        bo.primaryDepartmentCode = immutable.primaryDepartmentCode
        bo.employeeId = immutable.employeeId
        bo.employmentRecordId = immutable.employmentRecordId
        bo.baseSalaryAmount = immutable.getBaseSalaryAmount()
        bo.primary = immutable.primary

        bo.versionNumber = immutable.versionNumber
        bo.objectId = immutable.objectId

        return bo;
      }
}
