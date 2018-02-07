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
package org.kuali.rice.kim.impl.identity.affiliation

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationType
import org.kuali.rice.kim.framework.identity.affiliation.EntityAffiliationTypeEbo
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

@Entity
@Table(name="KRIM_AFLTN_TYP_T")
public class EntityAffiliationTypeBo extends PersistableBusinessObjectBase implements EntityAffiliationTypeEbo {
    @Id
    @Column(name="EMP_TYP_CD")
    String code;
    @Column(name="NM")
    String name;
    @org.hibernate.annotations.Type(type="yes_no")
    @Column(name="ACTV_IND")
    boolean active;
    @Column(name="DISPLAY_SORT_CD")
    String sortCode;
    
    @org.hibernate.annotations.Type(type="yes_no")
    @Column(name="EMP_AFLTN_TYP_IND")
    boolean employmentAffiliationType;


    /**
   * Converts a mutable EntityAffiliationTypeBo to an immutable EntityAffiliationType representation.
   * @param bo
   * @return an immutable EntityAffiliationType
   */
  static EntityAffiliationType to(EntityAffiliationTypeBo bo) {
    if (bo == null) { return null }
    return EntityAffiliationType.Builder.create(bo).build()
  }

  /**
   * Creates a EntityAffiliationType business object from an immutable representation of a EntityAffiliationType.
   * @param an immutable EntityAffiliationType
   * @return a EntityAffiliationTypeBo
   */
  static EntityAffiliationTypeBo from(EntityAffiliationType immutable) {
    if (immutable == null) {return null}

    EntityAffiliationTypeBo bo = new EntityAffiliationTypeBo()
    bo.code = immutable.code
    bo.name = immutable.name
    bo.sortCode = immutable.sortCode
    bo.employmentAffiliationType = immutable.employmentAffiliationType
    bo.active = immutable.active
    bo.versionNumber = immutable.versionNumber
    bo.objectId = immutable.objectId

    return bo;
  }
}
