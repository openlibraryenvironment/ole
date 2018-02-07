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
package org.kuali.rice.kim.impl.identity.phone

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Transient
import org.apache.commons.lang.StringUtils
import org.hibernate.annotations.Type

import org.kuali.rice.kim.api.identity.phone.EntityPhone
import org.kuali.rice.kim.api.identity.phone.EntityPhoneContract
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences
import org.kuali.rice.kim.api.services.KimApiServiceLocator
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.kim.api.KimApiConstants

@Entity
@Table(name = "KRIM_ENTITY_PHONE_T")
public class EntityPhoneBo extends PersistableBusinessObjectBase implements EntityPhoneContract {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ENTITY_PHONE_ID")
	String id;

	@Column(name = "ENTITY_ID")
    String entityId;

	@Column(name = "ENT_TYP_CD")
	String entityTypeCode;

	@Column(name = "PHONE_TYP_CD")
	String phoneTypeCode;

	@Column(name = "PHONE_NBR")
	String phoneNumber;

	@Column(name = "PHONE_EXTN_NBR")
	String extensionNumber;

	@Column(name = "POSTAL_CNTRY_CD")
	String countryCode;

	@ManyToOne(targetEntity=EntityPhoneTypeBo.class, fetch = FetchType.EAGER, cascade = [])
	@JoinColumn(name = "PHONE_TYP_CD", insertable = false, updatable = false)
	EntityPhoneTypeBo phoneType;

	@Transient
    boolean suppressPhone;

    @Type(type="yes_no")
	@Column(name="ACTV_IND")
    boolean active;

    @Type(type="yes_no")
	@Column(name="DFLT_IND")
	boolean defaultValue;


    public String getPhoneTypeCode(){
        return this.phoneTypeCode
    }
 /*
   * Converts a mutable EntityPhoneBo to an immutable EntityPhone representation.
   * @param bo
   * @return an immutable Country
   */
  static EntityPhone to(EntityPhoneBo bo) {
    if (bo == null) { return null }
    return EntityPhone.Builder.create(bo).build()
  }

  /**
   * Creates a CountryBo business object from an immutable representation of a Country.
   * @param an immutable Country
   * @return a CountryBo
   */
  static EntityPhoneBo from(EntityPhone immutable) {
    if (immutable == null) {return null}

    EntityPhoneBo bo = new EntityPhoneBo()
    bo.id = immutable.id
    bo.active = immutable.active

    bo.entityId = immutable.entityId
    bo.entityTypeCode = immutable.entityTypeCode
    if (immutable.phoneType != null) {
    	bo.phoneTypeCode = immutable.phoneType.code
  	}
    bo.phoneType = EntityPhoneTypeBo.from(immutable.phoneType)
    bo.defaultValue = immutable.defaultValue
    bo.countryCode = immutable.countryCodeUnmasked
    bo.phoneNumber = immutable.phoneNumberUnmasked
    bo.extensionNumber = immutable.extensionNumberUnmasked
    bo.versionNumber = immutable.versionNumber
    bo.objectId = immutable.objectId

    return bo;
  }

    @Override
    EntityPhoneTypeBo getPhoneType() {
        return this.phoneType
    }

    public void setPhoneType(EntityPhoneTypeBo phoneType) {
            this.phoneType = phoneType
    }

    @Override
    boolean isSuppressPhone() {
        if (this.suppressPhone == null) {
            EntityPrivacyPreferences privacy = KimApiServiceLocator.getIdentityService().getEntityPrivacyPreferences(getEntityId())
            if (privacy != null) {
               this.suppressPhone = privacy.isSuppressPhone()
            } else {
               this.suppressPhone = false
            }
        }

        return this.suppressPhone;
    }

    @Override
    String getFormattedPhoneNumber() {
        if (isSuppressPhone())  {
            return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK
        }
        return getFormattedPhoneNumberUnmasked()
    }

    @Override
    String getPhoneNumberUnmasked() {
        return this.phoneNumber
    }

    @Override
    String getExtensionNumberUnmasked() {
        return this.extensionNumber
    }

    @Override
    String getCountryCodeUnmasked() {
        return this.countryCode
    }

    @Override
    String getFormattedPhoneNumberUnmasked() {
        StringBuffer sb = new StringBuffer( 30 )

        // TODO: get extension from country code table
        // TODO: append "+xxx" if country is not the default country
        sb.append( this.phoneNumber )
        if ( StringUtils.isNotBlank( this.extensionNumber ) ) {
            sb.append( " x" )
            sb.append( this.extensionNumber )
        }

        return sb.toString();
    }

    @Override
    String getPhoneNumber() {
        if (isSuppressPhone()) {
            return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK_PHONE
        }
        return this.phoneNumber
    }

    @Override
    String getCountryCode() {
        if (isSuppressPhone()){
            return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK_CODE
        }
        return this.countryCode
    }

    @Override
    String getExtensionNumber() {
        if (isSuppressPhone()) {
            return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK
        }
        return this.extensionNumber
    }

}
