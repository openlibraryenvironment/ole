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
package org.kuali.rice.kim.impl.identity.privacy

import javax.persistence.Entity
import javax.persistence.Table
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferencesContract
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import javax.persistence.Column
import javax.persistence.Id
import org.hibernate.annotations.Type

import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences

@Entity
@Table(name = "KRIM_ENTITY_PRIV_PREF_T")
class EntityPrivacyPreferencesBo extends PersistableBusinessObjectBase implements EntityPrivacyPreferencesContract {
    	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ENTITY_ID")
	String entityId;

	@Type(type="yes_no")
	@Column(name="SUPPRESS_NM_IND")
	boolean suppressName;

	@Type(type="yes_no")
	@Column(name="SUPPRESS_EMAIL_IND")
	boolean suppressEmail;

	@Type(type="yes_no")
	@Column(name="SUPPRESS_ADDR_IND")
	boolean suppressAddress;

	@Type(type="yes_no")
	@Column(name="SUPPRESS_PHONE_IND")
	boolean suppressPhone;

	@Type(type="yes_no")
	@Column(name="SUPPRESS_PRSNL_IND")
	boolean suppressPersonal;

 /*
   * Converts a mutable EntityPhoneBo to an immutable EntityPhone representation.
   * @param bo
   * @return an immutable Country
   */
  static EntityPrivacyPreferences to(EntityPrivacyPreferencesBo bo) {
    if (bo == null) { return null }
    return EntityPrivacyPreferences.Builder.create(bo).build()
  }

  /**
   * Creates a CountryBo business object from an immutable representation of a Country.
   * @param an immutable Country
   * @return a CountryBo
   */
  static EntityPrivacyPreferencesBo from(EntityPrivacyPreferences immutable) {
    if (immutable == null) {return null}

    EntityPrivacyPreferencesBo bo = new EntityPrivacyPreferencesBo()

    bo.entityId = immutable.entityId
    bo.suppressAddress = immutable.suppressAddress
    bo.suppressEmail = immutable.suppressEmail
    bo.suppressName = immutable.suppressName
    bo.suppressPersonal = immutable.suppressPersonal
    bo.suppressPhone = immutable.suppressPhone
    bo.versionNumber = immutable.versionNumber
    bo.objectId = immutable.objectId

    return bo;
  }

}
