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
package org.kuali.rice.kim.api.identity.address;

import java.io.Serializable;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.DateTimeAdapter;
import org.kuali.rice.kim.api.KimApiConstants;
import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.w3c.dom.Element;

@XmlRootElement(name = EntityAddress.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = EntityAddress.Constants.TYPE_NAME, propOrder = {
    EntityAddress.Elements.ID,
    EntityAddress.Elements.ENTITY_TYPE_CODE,
    EntityAddress.Elements.ENTITY_ID,
    EntityAddress.Elements.ADDRESS_TYPE,
    EntityAddress.Elements.ATTENTION_LINE,
    EntityAddress.Elements.LINE1,
    EntityAddress.Elements.LINE2,
    EntityAddress.Elements.LINE3,
    EntityAddress.Elements.CITY,
    EntityAddress.Elements.STATE_PROVINCE_CODE,
    EntityAddress.Elements.POSTAL_CODE,
    EntityAddress.Elements.COUNTRY_CODE,
    EntityAddress.Elements.ATTENTION_LINE_UNMASKED,
    EntityAddress.Elements.LINE1_UNMASKED,
    EntityAddress.Elements.LINE2_UNMASKED,
    EntityAddress.Elements.LINE3_UNMASKED,
    EntityAddress.Elements.CITY_UNMASKED,
    EntityAddress.Elements.STATE_PROVINCE_CODE_UNMASKED,
    EntityAddress.Elements.POSTAL_CODE_UNMASKED,
    EntityAddress.Elements.COUNTRY_CODE_UNMASKED,
    EntityAddress.Elements.ADDRESS_FORMAT,
    EntityAddress.Elements.MODIFIED_DATE,
    EntityAddress.Elements.VALIDATED_DATE,
    EntityAddress.Elements.VALIDATED,
    EntityAddress.Elements.NOTE_MESSAGE,
    EntityAddress.Elements.SUPPRESS_ADDRESS,
    EntityAddress.Elements.DEFAULT_VALUE,
    EntityAddress.Elements.ACTIVE,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class EntityAddress extends AbstractDataTransferObject
    implements EntityAddressContract
{
    @XmlElement(name = Elements.ID, required = false)
    private final String id;
    @XmlElement(name = Elements.ENTITY_TYPE_CODE, required = false)
    private final String entityTypeCode;
    @XmlElement(name = Elements.ENTITY_ID, required = false)
    private final String entityId;
    @XmlElement(name = Elements.ADDRESS_TYPE, required = false)
    private final CodedAttribute addressType;
    @XmlElement(name = Elements.ATTENTION_LINE, required = false)
    private final String attentionLine;
    @XmlElement(name = Elements.LINE1, required = false)
    private final String line1;
    @XmlElement(name = Elements.LINE2, required = false)
    private final String line2;
    @XmlElement(name = Elements.LINE3, required = false)
    private final String line3;
    @XmlElement(name = Elements.CITY, required = false)
    private final String city;
    @XmlElement(name = Elements.STATE_PROVINCE_CODE, required = false)
    private final String stateProvinceCode;
    @XmlElement(name = Elements.POSTAL_CODE, required = false)
    private final String postalCode;
    @XmlElement(name = Elements.COUNTRY_CODE, required = false)
    private final String countryCode;
    @XmlElement(name = Elements.ATTENTION_LINE_UNMASKED, required = false)
    private final String attentionLineUnmasked;
    @XmlElement(name = Elements.LINE1_UNMASKED, required = false)
    private final String line1Unmasked;
    @XmlElement(name = Elements.LINE2_UNMASKED, required = false)
    private final String line2Unmasked;
    @XmlElement(name = Elements.LINE3_UNMASKED, required = false)
    private final String line3Unmasked;
    @XmlElement(name = Elements.CITY_UNMASKED, required = false)
    private final String cityUnmasked;
    @XmlElement(name = Elements.STATE_PROVINCE_CODE_UNMASKED, required = false)
    private final String stateProvinceCodeUnmasked;
    @XmlElement(name = Elements.POSTAL_CODE_UNMASKED, required = false)
    private final String postalCodeUnmasked;
    @XmlElement(name = Elements.COUNTRY_CODE_UNMASKED, required = false)
    private final String countryCodeUnmasked;
    @XmlElement(name = Elements.ADDRESS_FORMAT, required = false)
    private final String addressFormat;
    @XmlElement(name = Elements.MODIFIED_DATE, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime modifiedDate;
    @XmlElement(name = Elements.VALIDATED_DATE, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime validatedDate;
    @XmlElement(name = Elements.VALIDATED, required = false)
    private final boolean validated;
    @XmlElement(name = Elements.NOTE_MESSAGE, required = false)
    private final String noteMessage;
    @XmlElement(name = Elements.SUPPRESS_ADDRESS, required = false)
    private final boolean suppressAddress;
    @XmlElement(name = Elements.DEFAULT_VALUE, required = false)
    private final boolean defaultValue;
    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private EntityAddress() {
        this.entityId = null;
        this.entityTypeCode = null;
        this.addressType = null;
        this.attentionLine = null;
        this.line1 = null;
        this.line2 = null;
        this.line3 = null;
        this.city = null;
        this.stateProvinceCode = null;
        this.postalCode = null;
        this.countryCode = null;
        this.attentionLineUnmasked = null;
        this.line1Unmasked = null;
        this.line2Unmasked = null;
        this.line3Unmasked = null;
        this.cityUnmasked = null;
        this.stateProvinceCodeUnmasked = null;
        this.postalCodeUnmasked = null;
        this.countryCodeUnmasked = null;
        this.addressFormat = null;
        this.modifiedDate = null;
        this.validatedDate = null;
        this.validated = true; // assume it's valid?
        this.noteMessage = null;
        this.suppressAddress = false;
        this.defaultValue = false;
        this.versionNumber = null;
        this.objectId = null;
        this.active = false;
        this.id = null;
    }

    private EntityAddress(Builder builder) {
        this.entityId = builder.getEntityId();
        this.entityTypeCode = builder.getEntityTypeCode();

        this.addressType = (builder.getAddressType() != null) ? builder.getAddressType().build() : null;
        this.attentionLine = builder.getAttentionLine();
        this.line1 = builder.getLine1();
        this.line2 = builder.getLine2();
        this.line3 = builder.getLine3();
        this.city = builder.getCity();
        this.stateProvinceCode = builder.getStateProvinceCode();
        this.postalCode = builder.getPostalCode();
        this.countryCode = builder.getCountryCode();
        this.attentionLineUnmasked = builder.getAttentionLineUnmasked();
        this.line1Unmasked = builder.getLine1Unmasked();
        this.line2Unmasked = builder.getLine2Unmasked();
        this.line3Unmasked = builder.getLine3Unmasked();
        this.cityUnmasked = builder.getCityUnmasked();
        this.stateProvinceCodeUnmasked = builder.getStateProvinceCodeUnmasked();
        this.postalCodeUnmasked = builder.getPostalCodeUnmasked();
        this.countryCodeUnmasked = builder.getCountryCodeUnmasked();
        this.addressFormat = builder.getAddressFormat();
        this.modifiedDate = builder.getModifiedDate();
        this.validatedDate = builder.getValidatedDate();
        this.validated = builder.isValidated();
        this.noteMessage = builder.getNoteMessage();
        this.suppressAddress = builder.isSuppressAddress();
        this.defaultValue = builder.isDefaultValue();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
        this.active = builder.isActive();
        this.id = builder.getId();
    }

    @Override
    public String getEntityId() {
        return this.entityId;
    }

    @Override
    public String getEntityTypeCode() {
        return this.entityTypeCode;
    }

    @Override
    public CodedAttribute getAddressType() {
        return this.addressType;
    }

    @Override
    public String getAttentionLine() {
        return this.attentionLine;
    }

    @Override
    public String getLine1() {
        return this.line1;
    }

    @Override
    public String getLine2() {
        return this.line2;
    }

    @Override
    public String getLine3() {
        return this.line3;
    }

    @Override
    public String getCity() {
        return this.city;
    }

    @Override
    public String getStateProvinceCode() {
        return this.stateProvinceCode;
    }

    @Override
    public String getPostalCode() {
        return this.postalCode;
    }

    @Override
    public String getCountryCode() {
        return this.countryCode;
    }

    @Override
    public String getAttentionLineUnmasked() {
        return this.attentionLineUnmasked;
    }

    @Override
    public String getLine1Unmasked() {
        return this.line1Unmasked;
    }

    @Override
    public String getLine2Unmasked() {
        return this.line2Unmasked;
    }

    @Override
    public String getLine3Unmasked() {
        return this.line3Unmasked;
    }

    @Override
    public String getCityUnmasked() {
        return this.cityUnmasked;
    }

    @Override
    public String getStateProvinceCodeUnmasked() {
        return this.stateProvinceCodeUnmasked;
    }

    @Override
    public String getPostalCodeUnmasked() {
        return this.postalCodeUnmasked;
    }

    @Override
    public String getCountryCodeUnmasked() {
        return this.countryCodeUnmasked;
    }

    @Override
    public String getAddressFormat() {
        return this.addressFormat;
    }

    @Override
    public DateTime getModifiedDate() {
        return modifiedDate;
    }

    @Override
    public DateTime getValidatedDate() {
        return validatedDate;
    }

    @Override
    public boolean isValidated() {
        return validated;
    }

    @Override
    public String getNoteMessage() {
        return noteMessage;
    }

    @Override
    public boolean isSuppressAddress() {
        return this.suppressAddress;
    }

    @Override
    public boolean isDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getObjectId() {
        return this.objectId;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public String getId() {
        return this.id;
    }

    /**
     * A builder which can be used to construct {@link EntityAddress} instances.  Enforces the constraints of the {@link EntityAddressContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, EntityAddressContract
    {

        private String entityId;
        private String entityTypeCode;
        private CodedAttribute.Builder addressType;
        private String attentionLineUnmasked;
        private String line1Unmasked;
        private String line2Unmasked;
        private String line3Unmasked;
        private String cityUnmasked;
        private String stateProvinceCodeUnmasked;
        private String postalCodeUnmasked;
        private String countryCodeUnmasked;
        private String addressFormat;
        private DateTime modifiedDate;
        private DateTime validatedDate;
        private boolean validated;
        private String noteMessage;
        private boolean suppressAddress;
        private boolean defaultValue;
        private Long versionNumber;
        private String objectId;
        private boolean active;
        private String id;

        private Builder() {}

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(EntityAddressContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setEntityId(contract.getEntityId());
            builder.setSuppressAddress(contract.isSuppressAddress());
            builder.setEntityTypeCode(contract.getEntityTypeCode());
            if (contract.getAddressType() != null) {
                builder.setAddressType(CodedAttribute.Builder.create(contract.getAddressType()));
            }
            builder.setAttentionLine(contract.getAttentionLineUnmasked());
            builder.setLine1(contract.getLine1Unmasked());
            builder.setLine2(contract.getLine2Unmasked());
            builder.setLine3(contract.getLine3Unmasked());
            builder.setCity(contract.getCityUnmasked());
            builder.setStateProvinceCode(contract.getStateProvinceCodeUnmasked());
            builder.setPostalCode(contract.getPostalCodeUnmasked());
            builder.setCountryCode(contract.getCountryCodeUnmasked());
            builder.setAddressFormat(contract.getAddressFormat());
            builder.setModifiedDate(contract.getModifiedDate());
            builder.setValidatedDate(contract.getValidatedDate());
            builder.setValidated(contract.isValidated());
            builder.setNoteMessage(contract.getNoteMessage());
            builder.setDefaultValue(contract.isDefaultValue());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            builder.setActive(contract.isActive());
            builder.setId(contract.getId());
            return builder;
        }

        public EntityAddress build() {
            return new EntityAddress(this);
        }

        @Override
        public String getEntityId() {
            return this.entityId;
        }

        @Override
        public String getEntityTypeCode() {
            return this.entityTypeCode;
        }

        @Override
        public CodedAttribute.Builder getAddressType() {
            return this.addressType;
        }

        @Override
        public String getAttentionLine() {
            if (isSuppressAddress()) {
                return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK;
            }
            return this.attentionLineUnmasked;

        }

        @Override
        public String getLine1() {
            if (isSuppressAddress()) {
                return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK;
            }
            return this.line1Unmasked;

        }

        @Override
        public String getLine2() {
            if (isSuppressAddress()) {
                return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK;
            }
            return this.line2Unmasked;
        }

        @Override
        public String getLine3() {
            if (isSuppressAddress()) {
                return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK;
            }
            return this.line3Unmasked;
        }

        @Override
        public String getCity() {
            if (isSuppressAddress()) {
                return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK;
            }
            return this.cityUnmasked;
        }

        @Override
        public String getStateProvinceCode() {
            if (isSuppressAddress()) {
                return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK_CODE;
            }
            return this.stateProvinceCodeUnmasked;
        }

        @Override
        public String getPostalCode() {
            if (isSuppressAddress()) {
                return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK_ZIP;
            }
            return this.postalCodeUnmasked;
        }

        @Override
        public String getCountryCode() {
            if (isSuppressAddress()) {
                return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK_CODE;
            }
            return this.countryCodeUnmasked;
        }

        @Override
        public String getAttentionLineUnmasked() {
            return this.attentionLineUnmasked;
        }

        @Override
        public String getLine1Unmasked() {
            return this.line1Unmasked;
        }

        @Override
        public String getLine2Unmasked() {
            return this.line2Unmasked;
        }

        @Override
        public String getLine3Unmasked() {
            return this.line3Unmasked;
        }

        @Override
        public String getCityUnmasked() {
            return this.cityUnmasked;
        }

        @Override
        public String getStateProvinceCodeUnmasked() {
            return this.stateProvinceCodeUnmasked;
        }

        @Override
        public String getPostalCodeUnmasked() {
            return this.postalCodeUnmasked;
        }

        @Override
        public String getCountryCodeUnmasked() {
            return this.countryCodeUnmasked;
        }

        @Override
        public String getAddressFormat() {
            return this.addressFormat;
        }

        @Override
        public DateTime getModifiedDate() {
            return modifiedDate;
        }

        @Override
        public DateTime getValidatedDate() {
            return validatedDate;
        }

        @Override
        public boolean isValidated() {
            return validated;
        }

        @Override
        public String getNoteMessage() {
            return noteMessage;
        }

        @Override
        public boolean isSuppressAddress() {
            return this.suppressAddress;
        }

        @Override
        public boolean isDefaultValue() {
            return this.defaultValue;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        @Override
        public String getObjectId() {
            return this.objectId;
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        @Override
        public String getId() {
            return this.id;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public void setEntityTypeCode(String entityTypeCode) {
            this.entityTypeCode = entityTypeCode;
        }

        public void setAddressType(CodedAttribute.Builder addressType) {
            this.addressType = addressType;
        }

        public void setAttentionLine(String attnLine) {
            this.attentionLineUnmasked = attnLine;
        }

        public void setLine1(String line1) {
            this.line1Unmasked = line1;
        }

        public void setLine2(String line2) {
            this.line2Unmasked = line2;
        }

        public void setLine3(String line3) {
            this.line3Unmasked = line3;
        }

        public void setCity(String city) {
            this.cityUnmasked = city;
        }

        public void setStateProvinceCode(String stateProvinceCode) {
            this.stateProvinceCodeUnmasked = stateProvinceCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCodeUnmasked = postalCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCodeUnmasked = countryCode;
        }

        public void setAddressFormat(String addressFormat) {
            this.addressFormat = addressFormat;
        }

        public void setModifiedDate(DateTime modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public void setValidatedDate(DateTime validatedDate) {
            this.validatedDate = validatedDate;
        }

        public void setValidated(boolean validated) {
            this.validated = validated;
        }

        public void setNoteMessage(String noteMessage) {
            this.noteMessage = noteMessage;
        }

        private void setSuppressAddress(boolean suppressAddress) {
            this.suppressAddress = suppressAddress;
        }

        public void setDefaultValue(boolean defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.id = id;
        }

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "entityAddress";
        final static String TYPE_NAME = "EntityAddressType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String ENTITY_ID = "entityId";
        final static String ENTITY_TYPE_CODE = "entityTypeCode";
        final static String ADDRESS_TYPE = "addressType";
        final static String ATTENTION_LINE = "attentionLine";
        final static String LINE1 = "line1";
        final static String LINE2 = "line2";
        final static String LINE3 = "line3";
        final static String CITY = "city";
        final static String STATE_PROVINCE_CODE = "stateProvinceCode";
        final static String POSTAL_CODE = "postalCode";
        final static String COUNTRY_CODE = "countryCode";
        final static String ATTENTION_LINE_UNMASKED = "attentionLineUnmasked";
        final static String LINE1_UNMASKED = "line1Unmasked";
        final static String LINE2_UNMASKED = "line2Unmasked";
        final static String LINE3_UNMASKED = "line3Unmasked";
        final static String CITY_UNMASKED = "cityUnmasked";
        final static String STATE_PROVINCE_CODE_UNMASKED = "stateProvinceCodeUnmasked";
        final static String POSTAL_CODE_UNMASKED = "postalCodeUnmasked";
        final static String COUNTRY_CODE_UNMASKED = "countryCodeUnmasked";
        final static String ADDRESS_FORMAT= "addressFormat";
        final static String MODIFIED_DATE = "modifiedDate";
        final static String VALIDATED_DATE = "validatedDate";
        final static String VALIDATED = "validated";
        final static String NOTE_MESSAGE = "noteMessage";
        final static String SUPPRESS_ADDRESS = "suppressAddress";
        final static String DEFAULT_VALUE = "defaultValue";
        final static String ACTIVE = "active";
        final static String ID = "id";

    }

}
