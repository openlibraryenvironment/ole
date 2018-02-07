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
package org.kuali.rice.kim.impl.identity.address

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Transient
import org.hibernate.annotations.Type

import org.kuali.rice.kim.api.identity.address.EntityAddress
import org.kuali.rice.kim.api.identity.address.EntityAddressContract
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences
import org.kuali.rice.kim.api.services.KimApiServiceLocator
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.kim.api.KimApiConstants
import java.sql.Timestamp
import org.joda.time.DateTime

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name = "KRIM_ENTITY_ADDR_T")
public class EntityAddressBo extends PersistableBusinessObjectBase implements EntityAddressContract {
    private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ENTITY_ADDR_ID")
	String id

	@Column(name = "ENTITY_ID")
	String entityId

	@Column(name = "ADDR_TYP_CD")
	String addressTypeCode

	@Column(name = "ENT_TYP_CD")
	String entityTypeCode

	@Column(name = "CITY_NM")
	String city

	@Column(name = "STATE_PVC_CD")
	String stateProvinceCode

	@Column(name = "POSTAL_CD")
	String postalCode

	@Column(name = "POSTAL_CNTRY_CD")
	String countryCode

    @Column(name = "ATTN_LINE")
	String attentionLine;

	@Column(name = "ADDR_LINE_1")
	String line1

	@Column(name = "ADDR_LINE_2")
	String line2

	@Column(name = "ADDR_LINE_3")
	String line3

    @Type(type="yes_no")
	@Column(name="DFLT_IND")
	boolean defaultValue;

    @Type(type="yes_no")
	@Column(name="ACTV_IND")
    boolean active;

	@ManyToOne(targetEntity=EntityAddressTypeBo.class, fetch=FetchType.EAGER, cascade=[])
	@JoinColumn(name = "ADDR_TYP_CD", insertable = false, updatable = false)
	EntityAddressTypeBo addressType;

    @Column(name = "ADDR_FMT")
    String addressFormat;

    @Column(name = "MOD_DT")
    Timestamp modifiedDate;

    @Column(name = "VALID_DT")
    Timestamp validatedDate;

    @Type(type="yes_no")
	@Column(name="VALID_IND")
    boolean validated;

    @Column(name = "NOTE_MSG")
	String noteMessage;

	@Transient
    boolean suppressAddress;

    /*
     * Converts a mutable EntityAddressBo to an immutable EntityAddress representation.
     * @param bo
     * @return an immutable EntityAddress
     */
    static EntityAddress to(EntityAddressBo bo) {
      if (bo == null) { return null }
      return EntityAddress.Builder.create(bo).build()
    }

    /**
     * Creates a EntityAddressBo business object from an immutable representation of a EntityAddress.
     * @param an immutable EntityAddress
     * @return a EntityAddressBo
     */
    static EntityAddressBo from(EntityAddress immutable) {
        if (immutable == null) {return null}

        EntityAddressBo bo = new EntityAddressBo()
        bo.active = immutable.active
        bo.entityTypeCode = immutable.entityTypeCode
        if (immutable.addressType != null) {
            bo.addressTypeCode = immutable.addressType.code
        }
        bo.addressType = EntityAddressTypeBo.from(immutable.addressType)
        bo.defaultValue = immutable.defaultValue
        bo.attentionLine = immutable.attentionLineUnmasked
        bo.line1 = immutable.line1Unmasked
        bo.line2 = immutable.line2Unmasked
        bo.line3 = immutable.line3Unmasked
        bo.city = immutable.cityUnmasked
        bo.stateProvinceCode = immutable.stateProvinceCodeUnmasked
        bo.countryCode = immutable.countryCodeUnmasked
        bo.postalCode = immutable.postalCodeUnmasked
        bo.addressFormat = immutable.addressFormat
        if (immutable.modifiedDate != null) {
            bo.modifiedDate = new Timestamp(immutable.modifiedDate.millis);
        }
        if (immutable.validatedDate != null) {
            bo.validatedDate = new Timestamp(immutable.validatedDate.millis);
        }
        bo.validated = immutable.validated
        bo.noteMessage = immutable.noteMessage
        bo.id = immutable.id
        bo.entityId = immutable.entityId
        bo.active = immutable.active
        bo.versionNumber = immutable.versionNumber

        return bo;
    }


    @Override
    EntityAddressTypeBo getAddressType() {
        return addressType
    }

    public void setAddressType(EntityAddressTypeBo addressType) {
        this.addressType = addressType
    }

    @Override
    boolean isSuppressAddress() {
        if (this.suppressAddress == null) {
            EntityPrivacyPreferences privacy = KimApiServiceLocator.getIdentityService().getEntityPrivacyPreferences(getEntityId())
            if (privacy != null) {
               this.suppressAddress = privacy.isSuppressAddress()
            } else {
                this.suppressAddress = false
            }
        }

        return suppressAddress;
    }

    @Override
    String getAttentionLine() {
        if (isSuppressAddress()) {
            return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK;
        }
        return this.attentionLine;
    }

    @Override
    String getLine1() {
        if (isSuppressAddress()) {
            return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK;
        }
        return this.line1;
    }

    @Override
    String getLine2() {
        if (isSuppressAddress()) {
            return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK;
        }
        return this.line2;
    }

    @Override
    String getLine3() {
        if (isSuppressAddress()) {
            return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK;
        }
        return this.line3;
    }

    @Override
    String getCity() {
        if (isSuppressAddress()) {
            return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK;
        }
        return this.city;
    }

    @Override
    String getStateProvinceCode() {
        if (isSuppressAddress()) {
            return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK_CODE;
        }
        return this.stateProvinceCode;
    }

    @Override
    String getPostalCode() {
        if (isSuppressAddress()) {
            return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK_ZIP;
        }
        return this.postalCode;
    }

    @Override
    String getCountryCode() {
        if (isSuppressAddress()) {
            return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK_CODE;
        }
        return this.countryCode;
    }

    @Override
    String getAttentionLineUnmasked() {
        return attentionLine
    }
    @Override
    String getLine1Unmasked() {
        return line1
    }
    @Override
    String getLine2Unmasked() {
        return line2
    }
    @Override
    String getLine3Unmasked() {
        return line3
    }
    @Override
    String getCityUnmasked() {
        return city
    }
    @Override
    String getStateProvinceCodeUnmasked() {
        return stateProvinceCode
    }
    @Override
    String getPostalCodeUnmasked() {
        return postalCode
    }
    @Override
    String getCountryCodeUnmasked() {
        return countryCode
    }

    @Override
    DateTime getModifiedDate() {
        return modifiedDate ? new DateTime(modifiedDate.time) : null
    }

    @Override
    DateTime getValidatedDate() {
        return validatedDate ? new DateTime(validatedDate.time) : null
    }
}
