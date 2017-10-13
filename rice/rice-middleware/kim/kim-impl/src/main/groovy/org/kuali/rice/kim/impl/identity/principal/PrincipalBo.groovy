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
package org.kuali.rice.kim.impl.identity.principal

import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Id
import javax.persistence.Column
import org.hibernate.annotations.Type
import org.kuali.rice.kim.api.identity.principal.PrincipalContract
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.kim.api.identity.principal.Principal

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KRIM_PRNCPL_T")
class PrincipalBo extends PersistableBusinessObjectBase implements PrincipalContract {
    private static final long serialVersionUID = 4480581610252159267L;

	@Id
	@Column(name="PRNCPL_ID", columnDefinition="VARCHAR(40)")
	String principalId;

	@Column(name="PRNCPL_NM")
	String principalName;

	@Column(name="ENTITY_ID")
	String entityId;

	@Column(name="PRNCPL_PSWD")
	String password;

	@Column(name="ACTV_IND")
	@Type(type="yes_no")
	boolean active;
    
     /*
   * Converts a mutable PrincipalBo to an immutable Principal representation.
   * @param bo
   * @return an immutable Principal
   */
  static Principal to(PrincipalBo bo) {
    if (bo == null) { return null }
    return Principal.Builder.create(bo).build()
  }

  /**
   * Creates a PrincipalBo business object from an immutable representation of a Principal.
   * @param an immutable Principal
   * @return a PrincipalBo
   */
  static PrincipalBo from(Principal immutable) {
    if (immutable == null) {return null}

    PrincipalBo bo = new PrincipalBo()
    bo.active = immutable.active
    bo.principalId = immutable.principalId
    bo.entityId = immutable.entityId
    bo.principalName = immutable.principalName
    bo.active = immutable.active
    bo.versionNumber = immutable.versionNumber
    bo.objectId = immutable.objectId

    return bo;
  }
}
