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
package org.kuali.rice.kim.impl.identity.external

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierType
import org.kuali.rice.kim.framework.identity.external.EntityExternalIdentifierTypeEbo
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

@Entity
@Table(name="KRIM_EXT_ID_TYP_T")
public class EntityExternalIdentifierTypeBo extends PersistableBusinessObjectBase implements EntityExternalIdentifierTypeEbo {
    @Id
    @Column(name="EXT_ID_TYP_CD")
    String code;
    @Column(name="NM")
    String name;
    @org.hibernate.annotations.Type(type="yes_no")
    @Column(name="ACTV_IND")
    boolean active;
    @Column(name="DISPLAY_SORT_CD")
    String sortCode;

    @org.hibernate.annotations.Type(type="yes_no")
	@Column(name="ENCR_REQ_IND")
	boolean encryptionRequired;


    /**
   * Converts a mutable AddressTypeBo to an immutable AddressType representation.
   * @param bo
   * @return an immutable AddressType
   */
  static EntityExternalIdentifierType to(EntityExternalIdentifierTypeBo bo) {
    if (bo == null) { return null }
    return EntityExternalIdentifierType.Builder.create(bo).build()
  }

  /**
   * Creates a AddressType business object from an immutable representation of a AddressType.
   * @param an immutable AddressType
   * @return a AddressTypeBo
   */
  static EntityExternalIdentifierTypeBo from(EntityExternalIdentifierType immutable) {
    if (immutable == null) {return null}

    EntityExternalIdentifierTypeBo bo = new EntityExternalIdentifierTypeBo()
    bo.code = immutable.code
    bo.name = immutable.name
    bo.sortCode = immutable.sortCode
    bo.active = immutable.active
    bo.encryptionRequired = immutable.encryptionRequired
    bo.versionNumber = immutable.versionNumber
    bo.objectId = immutable.objectId

    return bo;
  }
}
