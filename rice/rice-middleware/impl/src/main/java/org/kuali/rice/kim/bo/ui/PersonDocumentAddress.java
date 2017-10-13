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
package org.kuali.rice.kim.bo.ui;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.kuali.rice.kim.impl.identity.address.EntityAddressTypeBo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@IdClass(PersonDocumentAddressId.class)
@Entity
@Table(name = "KRIM_PND_ADDR_MT")
public class PersonDocumentAddress extends PersonDocumentBoDefaultBase {
	@Id
	@GeneratedValue(generator="KRIM_ENTITY_ADDR_ID_S")
	@GenericGenerator(name="KRIM_ENTITY_ADDR_ID_S",strategy="org.kuali.rice.core.jpa.spring.RiceNumericStringSequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KRIM_ENTITY_ADDR_ID_S"),
			@Parameter(name="value_column",value="id")
		})
	@Column(name = "ENTITY_ADDR_ID")
	protected String entityAddressId;


	@Column(name = "ADDR_TYP_CD")
	protected String addressTypeCode;

	//@Column(name = "ENT_TYP_CD")
	@Transient
	protected String entityTypeCode;

	@Column(name = "CITY_NM")
	protected String city;

	@Column(name = "STATE_PVC_CD")
	protected String stateProvinceCode;

	@Column(name = "POSTAL_CD")
	protected String postalCode;

	@Column(name = "POSTAL_CNTRY_CD")
	protected String countryCode;

    @Column(name = "ATTN_LINE")
	protected String attentionLine;

	@Column(name = "ADDR_LINE_1")
	protected String line1;

	@Column(name = "ADDR_LINE_2")
	protected String line2;

	@Column(name = "ADDR_LINE_3")
	protected String line3;

    @Column(name = "ADDR_FMT")
    protected String addressFormat;

    @Column(name = "MOD_DT")
    protected Timestamp modifiedDate;

    @Column(name = "VALID_DT")
    protected Timestamp validatedDate;

    @Type(type="yes_no")
	@Column(name="VALID_IND")
    protected boolean validated;

    @Column(name = "NOTE_MSG")
	protected String noteMessage;

	@ManyToOne(targetEntity=EntityAddressTypeBo.class, fetch = FetchType.EAGER, cascade = {})
	@JoinColumn(name = "ADDR_TYP_CD", insertable = false, updatable = false)
	private EntityAddressTypeBo addressType;

	// Waiting until we pull in from KFS
	// protected State state;
	// protected PostalCode postalCode;
	// protected Country country;
	public PersonDocumentAddress() {
		this.active = true;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#getAddressTypeCode()
	 */
	public String getAddressTypeCode() {
		return addressTypeCode;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#getCity()
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#getCountryCode()
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#getEntityAddressId()
	 */
	public String getEntityAddressId() {
		return entityAddressId;
	}

    /**
	 * @see org.kuali.rice.kim.api.identity.address.EntityAddressContract#getAttentionLine()
	 */
	public String getAttentionLine() {
		return attentionLine;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#getLine1()
	 */
	public String getLine1() {
		return line1;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#getLine2()
	 */
	public String getLine2() {
		return line2;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#getLine3()
	 */
	public String getLine3() {
		return line3;
	}

    /**
	 * @see @see org.kuali.rice.kim.api.identity.address.EntityAddressContract#getAddressFormat
	 */
	public String getAddressFormat() {
		return addressFormat;
	}

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public Timestamp getValidatedDate() {
        return validatedDate;
    }

    public boolean isValidated() {
        return validated;
    }

    public String getNoteMessage() {
        return noteMessage;
    }

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#getPostalCode()
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#getStateCode()
	 */
	public String getStateProvinceCode() {
		return stateProvinceCode;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#setAddressTypeCode(java.lang.String)
	 */
	public void setAddressTypeCode(String addressTypeCode) {
		this.addressTypeCode = addressTypeCode;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#setCity(java.lang.String)
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#setCountryCode(java.lang.String)
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

    /**
	 * @see org.kuali.rice.kim.api.identity.address.EntityAddressContract#getAttentionLine()
	 */
	public void setAttentionLine(String attentionLine) {
		this.attentionLine = attentionLine;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#setLine1(java.lang.String)
	 */
	public void setLine1(String line1) {
		this.line1 = line1;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#setLine2(java.lang.String)
	 */
	public void setLine2(String line2) {
		this.line2 = line2;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#setLine3(java.lang.String)
	 */
	public void setLine3(String line3) {
		this.line3 = line3;
	}

    /**
	 * @see org.kuali.rice.kim.api.identity.address.EntityAddressContract#getAddressFormat()
	 */
	public void setAddressFormat(String addressFormat) {
		this.addressFormat = addressFormat;
	}

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setValidatedDate(Timestamp validatedDate) {
        this.validatedDate = validatedDate;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public void setNoteMessage(String noteMessage) {
        this.noteMessage = noteMessage;
    }

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#setPostalCode(java.lang.String)
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @see org.kuali.rice.kim.bo.entity.KimEntityAddress#setStateCode(java.lang.String)
	 */
	public void setStateProvinceCode(String stateProvinceCode) {
		this.stateProvinceCode = stateProvinceCode;
	}

	/**
	 * @see org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo#getEntityTypeCode()
	 */
	public String getEntityTypeCode() {
		return entityTypeCode;
	}

	public void setEntityTypeCode(String entityTypeCode) {
		this.entityTypeCode = entityTypeCode;
	}

	public void setEntityAddressId(String entityAddressId) {
		this.entityAddressId = entityAddressId;
	}

	public EntityAddressTypeBo getAddressType() {
		return this.addressType;
	}

	public void setAddressType(EntityAddressTypeBo addressType) {
		this.addressType = addressType;
	}
}
