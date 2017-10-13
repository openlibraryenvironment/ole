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
package org.kuali.rice.kim.impl.identity.citizenship

import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Column
import java.sql.Timestamp
import javax.persistence.ManyToOne
import javax.persistence.JoinColumn
import javax.persistence.FetchType
import org.kuali.rice.kim.api.identity.citizenship.EntityCitizenship
import org.hibernate.annotations.Type
import org.kuali.rice.kim.api.identity.citizenship.EntityCitizenshipContract
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.joda.time.DateTime

@Entity
@Table(name = "KRIM_ENTITY_CTZNSHP_T")
class EntityCitizenshipBo extends PersistableBusinessObjectBase implements EntityCitizenshipContract {
    private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ENTITY_CTZNSHP_ID")
	String id;

	@Column(name = "ENTITY_ID")
	String entityId;
	
	@Column(name = "POSTAL_CNTRY_CD")
	String countryCode;

	@Column(name = "CTZNSHP_STAT_CD")
	String statusCode;

	@Column(name = "strt_dt")
	Timestamp startDateValue;

	@Column(name = "end_dt")
	Timestamp endDateValue;

	@ManyToOne(targetEntity=EntityCitizenshipStatusBo.class, fetch=FetchType.EAGER, cascade=[])
	@JoinColumn(name = "CTZNSHP_STAT_CD", insertable = false, updatable = false)
	EntityCitizenshipStatusBo status;

    @Type(type="yes_no")
	@Column(name="ACTV_IND")
    boolean active;
    
  /*
   * Converts a mutable EntityCitizenshipBo to an immutable EntityCitizenship representation.
   * @param bo
   * @return an immutable EntityCitizenship
   */
  static EntityCitizenship to(EntityCitizenshipBo bo) {
    if (bo == null) { return null }
    return EntityCitizenship.Builder.create(bo).build()
  }

  /**
   * Creates a EntityCitizenshipBo business object from an immutable representation of a EntityCitizenship.
   * @param an immutable EntityCitizenship
   * @return a EntityCitizenshipBo
   */
  static EntityCitizenshipBo from(EntityCitizenship immutable) {
    if (immutable == null) {return null}

    EntityCitizenshipBo bo = new EntityCitizenshipBo()
    bo.active = immutable.active
    if (immutable.status != null) {
    	bo.statusCode = immutable.status.code
        bo.status = EntityCitizenshipStatusBo.from(immutable.status)
  	}
    bo.id = immutable.id
    bo.entityId = immutable.entityId
    bo.countryCode = immutable.countryCode
    if (immutable.startDate != null) {
        bo.startDateValue = immutable.startDate.toDate().toTimestamp()
    }
    if (immutable.endDate != null) {
        bo.endDateValue = immutable.endDate.toDate().toTimestamp()
    }
    bo.active = immutable.active
    bo.versionNumber = immutable.versionNumber
    bo.objectId = immutable.objectId

    return bo;
  }

    @Override
    DateTime getStartDate() {
        if (this.startDateValue != null) {
            return new DateTime(this.startDateValue)
        }
        return null
    }

    @Override
    DateTime getEndDate() {
        if (this.endDateValue != null) {
            return new DateTime(this.endDateValue)
        }
        return null
    }

    @Override
    EntityCitizenshipStatusBo getStatus() {
        return this.status
    }
}
