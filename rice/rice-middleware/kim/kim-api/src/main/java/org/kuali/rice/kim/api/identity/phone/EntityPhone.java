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
package org.kuali.rice.kim.api.identity.phone;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.KimApiConstants;
import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Collection;

@XmlRootElement(name = EntityPhone.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = EntityPhone.Constants.TYPE_NAME, propOrder = {
    EntityPhone.Elements.ID,
    EntityPhone.Elements.ENTITY_TYPE_CODE,
    EntityPhone.Elements.ENTITY_ID,
    EntityPhone.Elements.PHONE_TYPE,
    EntityPhone.Elements.COUNTRY_CODE,
    EntityPhone.Elements.PHONE_NUMBER,
    EntityPhone.Elements.EXTENSION_NUMBER,
    EntityPhone.Elements.FORMATTED_PHONE_NUMBER,
    EntityPhone.Elements.COUNTRY_CODE_UNMASKED,
    EntityPhone.Elements.PHONE_NUMBER_UNMASKED,
    EntityPhone.Elements.EXTENSION_NUMBER_UNMASKED,
    EntityPhone.Elements.FORMATTED_PHONE_NUMBER_UNMASKED,
    EntityPhone.Elements.SUPPRESS_PHONE,
    EntityPhone.Elements.DEFAULT_VALUE,
    EntityPhone.Elements.ACTIVE,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class EntityPhone extends AbstractDataTransferObject
    implements EntityPhoneContract
{

    @XmlElement(name = Elements.ID, required = false)
    private final String id;
    @XmlElement(name = Elements.ENTITY_TYPE_CODE, required = false)
    private final String entityTypeCode;
    @XmlElement(name = Elements.ENTITY_ID, required = false)
    private final String entityId;
    @XmlElement(name = Elements.PHONE_TYPE, required = false)
    private final CodedAttribute phoneType;
    @XmlElement(name = Elements.PHONE_NUMBER, required = false)
    private final String phoneNumber;
    @XmlElement(name = Elements.EXTENSION_NUMBER, required = false)
    private final String extensionNumber;
    @XmlElement(name = Elements.COUNTRY_CODE, required = false)
    private final String countryCode;
    @XmlElement(name = Elements.PHONE_NUMBER_UNMASKED, required = false)
    private final String phoneNumberUnmasked;
    @XmlElement(name = Elements.EXTENSION_NUMBER_UNMASKED, required = false)
    private final String extensionNumberUnmasked;
    @XmlElement(name = Elements.COUNTRY_CODE_UNMASKED, required = false)
    private final String countryCodeUnmasked;
    @XmlElement(name = Elements.FORMATTED_PHONE_NUMBER, required = false)
    private final String formattedPhoneNumber;
    @XmlElement(name = Elements.FORMATTED_PHONE_NUMBER_UNMASKED, required = false)
    private final String formattedPhoneNumberUnmasked;
    @XmlElement(name = Elements.SUPPRESS_PHONE, required = false)
    private final boolean suppressPhone;
    @XmlElement(name = Elements.DEFAULT_VALUE, required = false)
    private final boolean defaultValue;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;
    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     *
     */
    private EntityPhone() {
        this.id = null;
        this.entityId = null;
        this.entityTypeCode = null;
        this.phoneType = null;
        this.phoneNumber = null;
        this.extensionNumber = null;
        this.countryCode = null;
        this.phoneNumberUnmasked = null;
        this.extensionNumberUnmasked = null;
        this.countryCodeUnmasked = null;
        this.formattedPhoneNumber = null;
        this.formattedPhoneNumberUnmasked = null;
        this.suppressPhone = false;
        this.defaultValue = false;
        this.versionNumber = null;
        this.objectId = null;
        this.active = false;
    }

    private EntityPhone(Builder builder) {
        this.id = builder.getId();
        this.entityId = builder.getEntityId();
        this.entityTypeCode = builder.getEntityTypeCode();
        this.phoneType = (builder.getPhoneType() != null) ? builder.getPhoneType().build() : null;
        this.phoneNumber = builder.getPhoneNumber();
        this.extensionNumber = builder.getExtensionNumber();
        this.countryCode = builder.getCountryCode();
        this.phoneNumberUnmasked = builder.getPhoneNumberUnmasked();
        this.extensionNumberUnmasked = builder.getExtensionNumberUnmasked();
        this.countryCodeUnmasked = builder.getCountryCodeUnmasked();
        this.formattedPhoneNumber = builder.getFormattedPhoneNumber();
        this.formattedPhoneNumberUnmasked = builder.getFormattedPhoneNumberUnmasked();
        this.suppressPhone = builder.isSuppressPhone();
        this.defaultValue = builder.isDefaultValue();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
        this.active = builder.isActive();
    }

    @Override
    public String getId() {
        return this.id;
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
    public CodedAttribute getPhoneType() {
        return this.phoneType;
    }

    @Override
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    @Override
    public String getExtensionNumber() {
        return this.extensionNumber;
    }

    @Override
    public String getCountryCode() {
        return this.countryCode;
    }

    @Override
    public String getPhoneNumberUnmasked() {
        return this.phoneNumberUnmasked;
    }

    @Override
    public String getExtensionNumberUnmasked() {
        return this.extensionNumberUnmasked;
    }

    @Override
    public String getCountryCodeUnmasked() {
        return this.countryCodeUnmasked;
    }

    @Override
    public String getFormattedPhoneNumber() {
        return this.formattedPhoneNumber;
    }

    @Override
    public String getFormattedPhoneNumberUnmasked() {
        return this.formattedPhoneNumberUnmasked;
    }

    @Override
    public boolean isSuppressPhone() {
        return this.suppressPhone;
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

    /**
     * A builder which can be used to construct {@link EntityPhone} instances.  Enforces the constraints of the {@link EntityPhoneContract}.
     *
     */
    public final static class Builder
        implements Serializable, ModelBuilder, EntityPhoneContract
    {

        private String id;
        private String entityId;
        private String entityTypeCode;
        private CodedAttribute.Builder phoneType;
        //private String phoneNumber;
        //private String extensionNumber;
        //private String countryCode;
        private String phoneNumberUnmasked;
        private String extensionNumberUnmasked;
        private String countryCodeUnmasked;
        //private String formattedPhoneNumber;
        //private String formattedPhoneNumberUnmasked;
        private boolean suppressPhone;
        private boolean defaultValue;
        private Long versionNumber;
        private String objectId;
        private boolean active;

        private Builder() {

        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(EntityPhoneContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setId(contract.getId());
            builder.setEntityId(contract.getEntityId());
            builder.setEntityTypeCode(contract.getEntityTypeCode());
            builder.setSuppressPhone(contract.isSuppressPhone());
            if (contract.getPhoneType() != null) {
                builder.setPhoneType(CodedAttribute.Builder.create(contract.getPhoneType()));
            }
            builder.setPhoneNumber(contract.getPhoneNumberUnmasked());
            builder.setExtensionNumber(contract.getExtensionNumberUnmasked());
            builder.setCountryCode(contract.getCountryCodeUnmasked());
            builder.setDefaultValue(contract.isDefaultValue());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            builder.setActive(contract.isActive());
            return builder;
        }

        public EntityPhone build() {
            return new EntityPhone(this);
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getEntityTypeCode() {
            return this.entityTypeCode;
        }

        @Override
        public String getEntityId() {
            return this.entityId;
        }

        @Override
        public CodedAttribute.Builder getPhoneType() {
            return this.phoneType;
        }

        @Override
        public String getPhoneNumber() {
            if (isSuppressPhone()) {
                return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK_PHONE;
            }
            return this.phoneNumberUnmasked;
        }

        @Override
        public String getExtensionNumber() {
            if (isSuppressPhone()) {
                return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK;
            }
            return this.extensionNumberUnmasked;
        }

        @Override
        public String getCountryCode() {
            if (isSuppressPhone()) {
                return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK_CODE;
            }
            return this.countryCodeUnmasked;
        }

        @Override
        public String getPhoneNumberUnmasked() {
            return this.phoneNumberUnmasked;
        }

        @Override
        public String getExtensionNumberUnmasked() {
            return this.extensionNumberUnmasked;
        }

        @Override
        public String getCountryCodeUnmasked() {
            return this.countryCodeUnmasked;
        }

        @Override
        public String getFormattedPhoneNumber() {
            if (isSuppressPhone()) {
                return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK;
            }
            return this.getFormattedPhoneNumberUnmasked();
        }

        @Override
        public String getFormattedPhoneNumberUnmasked() {
            StringBuffer sb = new StringBuffer( 30 );

            // TODO: get extension from country code table
            // TODO: append "+xxx" if country is not the default country
            sb.append( this.phoneNumberUnmasked );
            if ( StringUtils.isNotBlank( this.extensionNumberUnmasked) ) {
                sb.append( " x" );
                sb.append( this.extensionNumberUnmasked );
            }

            return sb.toString();
        }

        @Override
        public boolean isSuppressPhone() {
            return this.suppressPhone;
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

        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.id = id;
        }

        public void setEntityTypeCode(String entityTypeCode) {
            this.entityTypeCode = entityTypeCode;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public void setPhoneType(CodedAttribute.Builder phoneType) {
            this.phoneType = phoneType;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumberUnmasked = phoneNumber;
        }

        public void setExtensionNumber(String extensionNumber) {
            this.extensionNumberUnmasked = extensionNumber;
        }

        public void setCountryCode(String countryCode) {
            this.countryCodeUnmasked = countryCode;
        }

        private void setSuppressPhone(boolean suppressPhone) {
            this.suppressPhone = suppressPhone;
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

    }


    /**
     * Defines some internal constants used on this class.
     *
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "entityPhone";
        final static String TYPE_NAME = "EntityPhoneType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {

        final static String ID = "id";
        final static String ENTITY_TYPE_CODE = "entityTypeCode";
        final static String ENTITY_ID = "entityId";
        final static String PHONE_TYPE = "phoneType";
        final static String PHONE_NUMBER = "phoneNumber";
        final static String EXTENSION_NUMBER = "extensionNumber";
        final static String COUNTRY_CODE = "countryCode";
        final static String PHONE_NUMBER_UNMASKED = "phoneNumberUnmasked";
        final static String EXTENSION_NUMBER_UNMASKED = "extensionNumberUnmasked";
        final static String COUNTRY_CODE_UNMASKED = "countryCodeUnmasked";
        final static String FORMATTED_PHONE_NUMBER = "formattedPhoneNumber";
        final static String FORMATTED_PHONE_NUMBER_UNMASKED = "formattedPhoneNumberUnmasked";
        final static String SUPPRESS_PHONE = "suppressPhone";
        final static String DEFAULT_VALUE = "defaultValue";
        final static String ACTIVE = "active";

    }

}
