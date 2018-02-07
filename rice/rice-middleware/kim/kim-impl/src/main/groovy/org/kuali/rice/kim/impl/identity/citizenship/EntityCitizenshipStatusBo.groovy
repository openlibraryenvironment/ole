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

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import org.kuali.rice.kim.api.identity.CodedAttribute
import org.kuali.rice.kim.api.identity.CodedAttributeContract
import org.kuali.rice.kim.framework.identity.citizenship.EntityCitizenshipStatusEbo;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

@Entity
@Table(name="KRIM_CTZNSHP_STAT_T")
public class EntityCitizenshipStatusBo extends PersistableBusinessObjectBase implements EntityCitizenshipStatusEbo {
    @Id
    @Column(name="CTZNSHP_STAT_CD")
    String code;
    @Column(name="NM")
    String name;
    @org.hibernate.annotations.Type(type="yes_no")
    @Column(name="ACTV_IND")
    boolean active;
    @Column(name="DISPLAY_SORT_CD")
    String sortCode;


    /**
   * Converts a mutable EntityCitizenshipStatusBo to an immutable EntityCitizenshipStatus representation.
   * @param bo
   * @return an immutable EntityCitizenshipStatus
   */
  static CodedAttribute to(EntityCitizenshipStatusBo bo) {
    if (bo == null) { return null }
    return CodedAttribute.Builder.create(bo).build()
  }

  /**
   * Creates a EntityCitizenshipStatusBo business object from an immutable representation of a EntityCitizenshipStatus.
   * @param an immutable EntityCitizenshipStatus
   * @return a EntityCitizenshipStatusBo
   */
  static EntityCitizenshipStatusBo from(CodedAttribute immutable) {
    if (immutable == null) {return null}

    EntityCitizenshipStatusBo bo = new EntityCitizenshipStatusBo()
    bo.code = immutable.code
    bo.name = immutable.name
    bo.sortCode = immutable.sortCode
    bo.active = immutable.active
    bo.versionNumber = immutable.versionNumber
    bo.objectId = immutable.objectId

    return bo;
  }
}
