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
package org.kuali.rice.kim.impl.identity.personal

import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import javax.persistence.Column
import javax.persistence.Transient
import org.kuali.rice.kim.api.identity.personal.EntityEthnicity
import org.kuali.rice.kim.api.identity.personal.EntityEthnicityContract
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences

import org.kuali.rice.kim.api.KimApiConstants

@Entity
@Table(name = "KRIM_ENTITY_ETHNIC_T")
class EntityEthnicityBo extends PersistableBusinessObjectBase implements EntityEthnicityContract {
    	private static final long serialVersionUID = 4870141334376945160L;

	@Id
	@GeneratedValue(generator="KRIM_ENTITY_ETHNIC_ID_S")
	@GenericGenerator(name="KRIM_ENTITY_ETHNIC_ID_S",strategy="org.kuali.rice.core.jpa.spring.RiceNumericStringSequenceStyleGenerator",parameters=[
			@Parameter(name="sequence_name",value="KRIM_ENTITY_ETHNIC_ID_S"),
			@Parameter(name="value_column",value="id")
		])	
	@Column(name = "ID")
	String id

	@Column(name = "ENTITY_ID")
	String entityId

	@Column(name = "ETHNCTY_CD")
	String ethnicityCode

	@Column(name = "SUB_ETHNCTY_CD")
	String subEthnicityCode

	@Transient
    boolean suppressPersonal
    
      /*
   * Converts a mutable EntityEthnicityBo to an immutable EntityEthnicity representation.
   * @param bo
   * @return an immutable EntityEthnicity
   */
  static EntityEthnicity to(EntityEthnicityBo bo) {
    if (bo == null) { return null }
    return EntityEthnicity.Builder.create(bo).build()
  }

  /**
   * Creates a EntityEthnicityBo business object from an immutable representation of a EntityEthnicity.
   * @param an immutable EntityEthnicity
   * @return a EntityEthnicityBo
   */
  static EntityEthnicityBo from(EntityEthnicity immutable) {
    if (immutable == null) {return null}

    EntityEthnicityBo bo = new EntityEthnicityBo()
    bo.entityId = immutable.entityId
    bo.id = immutable.id
    bo.ethnicityCode = immutable.ethnicityCode
    bo.subEthnicityCode = immutable.subEthnicityCode
    bo.versionNumber = immutable.versionNumber
    bo.objectId = immutable.objectId

    return bo;
  }

    @Override
    boolean isSuppressPersonal() {
        if (this.suppressPersonal == null) {
            EntityPrivacyPreferences privacy = KimApiServiceLocator.getIdentityService().getEntityPrivacyPreferences(getEntityId())
            if (privacy != null) {
               this.suppressPersonal = privacy.isSuppressPersonal()
            } else {
                this.suppressPersonal = false
            }
        }

        return suppressPersonal;
    }

    @Override
    String getEthnicityCode() {
        if (isSuppressPersonal()) {
            return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK
        }
        return this.ethnicityCode
    }

    @Override
    String getSubEthnicityCode() {
        if (isSuppressPersonal()) {
            return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK
        }
        return this.subEthnicityCode
    }

    @Override
    String getEthnicityCodeUnmasked() {
        return this.ethnicityCode
    }

    @Override
    String getSubEthnicityCodeUnmasked() {
        return this.subEthnicityCode
    }
}
