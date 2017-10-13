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
package org.kuali.rice.kim.api.identity.type;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.identity.EntityUtils;
import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.CodedAttributeContract;
import org.kuali.rice.kim.api.identity.address.EntityAddress;
import org.kuali.rice.kim.api.identity.address.EntityAddressContract;
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.kuali.rice.kim.api.identity.email.EntityEmailContract;
import org.kuali.rice.kim.api.identity.phone.EntityPhone;
import org.kuali.rice.kim.api.identity.phone.EntityPhoneContract;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@XmlRootElement(name = EntityTypeContactInfo.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = EntityTypeContactInfo.Constants.TYPE_NAME, propOrder = {
    EntityTypeContactInfo.Elements.ENTITY_ID,
    EntityTypeContactInfo.Elements.ENTITY_TYPE_CODE,
    EntityTypeContactInfo.Elements.ENTITY_TYPE,
    EntityTypeContactInfo.Elements.ADDRESSES,
    EntityTypeContactInfo.Elements.EMAIL_ADDRESSES,
    EntityTypeContactInfo.Elements.PHONE_NUMBERS,
    EntityTypeContactInfo.Elements.DEFAULT_ADDRESS,
    EntityTypeContactInfo.Elements.DEFAULT_EMAIL_ADDRESS,
    EntityTypeContactInfo.Elements.DEFAULT_PHONE_NUMBER,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    EntityTypeContactInfo.Elements.ACTIVE,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class EntityTypeContactInfo extends AbstractDataTransferObject
    implements EntityTypeContactInfoContract
{

    @XmlElement(name = Elements.ENTITY_ID, required = true)
    private final String entityId;

    @XmlElement(name = Elements.ENTITY_TYPE_CODE, required = true)
    private final String entityTypeCode;

    @XmlElement(name = Elements.ENTITY_TYPE, required = false)
    private final CodedAttribute entityType;

    @XmlElementWrapper(name = Elements.ADDRESSES, required = false)
    @XmlElement(name = Elements.ADDRESS, required = false)
    private final List<EntityAddress> addresses;

    @XmlElementWrapper(name = Elements.EMAIL_ADDRESSES, required = false)
    @XmlElement(name = Elements.EMAIL_ADDRESS, required = false)
    private final List<EntityEmail> emailAddresses;

    @XmlElementWrapper(name = Elements.PHONE_NUMBERS, required = false)
    @XmlElement(name = Elements.PHONE_NUMBER, required = false)
    private final List<EntityPhone> phoneNumbers;

    @XmlElement(name = Elements.DEFAULT_ADDRESS, required = false)
    private final EntityAddress defaultAddress;

    @XmlElement(name = Elements.DEFAULT_EMAIL_ADDRESS, required = false)
    private final EntityEmail defaultEmailAddress;

    @XmlElement(name = Elements.DEFAULT_PHONE_NUMBER, required = false)
    private final EntityPhone defaultPhoneNumber;

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
     */
    private EntityTypeContactInfo() {
        this.entityId = null;
        this.entityTypeCode = null;
        this.entityType = null;
        this.addresses = null;
        this.emailAddresses = null;
        this.phoneNumbers = null;
        this.defaultAddress = null;
        this.defaultEmailAddress = null;
        this.defaultPhoneNumber = null;
        this.versionNumber = null;
        this.objectId = null;
        this.active = false;
    }

    private EntityTypeContactInfo(Builder builder) {
        this.entityId = builder.getEntityId();
        this.entityTypeCode = builder.getEntityTypeCode();
        this.entityType = (builder.getEntityType() != null) ? builder.getEntityType().build() : null;
        this.addresses = new ArrayList<EntityAddress>();
        if (!CollectionUtils.isEmpty(builder.getAddresses())) {
            for (EntityAddress.Builder address : builder.getAddresses()) {
                this.addresses.add(address.build());
            }
        }

        this.emailAddresses = new ArrayList<EntityEmail>();
        if (!CollectionUtils.isEmpty(builder.getEmailAddresses())) {
            for (EntityEmail.Builder email : builder.getEmailAddresses()) {
                this.emailAddresses.add(email.build());
            }
        }
        this.phoneNumbers = new ArrayList<EntityPhone>();
        if (!CollectionUtils.isEmpty(builder.getPhoneNumbers())) {
            for (EntityPhone.Builder phoneNumber : builder.getPhoneNumbers()) {
                this.phoneNumbers.add(phoneNumber.build());
            }
        }
        this.defaultAddress = builder.getDefaultAddress() != null ? builder.getDefaultAddress().build() : null;
        this.defaultEmailAddress = builder.getDefaultEmailAddress() != null ? builder.getDefaultEmailAddress().build() : null;
        this.defaultPhoneNumber = builder.getDefaultPhoneNumber() != null ? builder.getDefaultPhoneNumber().build() : null;
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
        this.active = builder.isActive();
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
    public CodedAttributeContract getEntityType() {
        return this.entityType;
    }

    @Override
    public List<EntityAddress> getAddresses() {
        return this.addresses;
    }

    @Override
    public List<EntityEmail> getEmailAddresses() {
        return this.emailAddresses;
    }

    @Override
    public List<EntityPhone> getPhoneNumbers() {
        return this.phoneNumbers;
    }

    @Override
    public EntityAddressContract getDefaultAddress() {
        return this.defaultAddress;
    }

    @Override
    public EntityEmail getDefaultEmailAddress() {
        return this.defaultEmailAddress;
    }

    @Override
    public EntityPhoneContract getDefaultPhoneNumber() {
        return this.defaultPhoneNumber;
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
     * A builder which can be used to construct {@link EntityTypeContactInfo} instances.  Enforces the constraints of the {@link EntityTypeContactInfoContract}.
     *
     */
    public final static class Builder
        implements Serializable, ModelBuilder, EntityTypeContactInfoContract
    {
        private String entityId;
        private String entityTypeCode;
        private CodedAttribute.Builder entityType;
        private List<EntityAddress.Builder> addresses;
        private List<EntityEmail.Builder> emailAddresses;
        private List<EntityPhone.Builder> phoneNumbers;
        private Long versionNumber;
        private String objectId;
        private boolean active;

        private Builder(String entityId, String entityTypeCode) {
            setEntityId(entityId);
            setEntityTypeCode(entityTypeCode);
            setEntityType(CodedAttribute.Builder.create(entityTypeCode));
        }

        public static Builder create(String entityId, String entityTypeCode) {
            return new Builder(entityId, entityTypeCode);
        }

        public static Builder create(EntityTypeContactInfoContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getEntityId(), contract.getEntityTypeCode());
            if (contract.getEntityType() != null) {
                builder.setEntityType(CodedAttribute.Builder.create(contract.getEntityType()));
            }
            builder.addresses = new ArrayList<EntityAddress.Builder>();
            if (!CollectionUtils.isEmpty(contract.getAddresses())) {
                for (EntityAddressContract addressContract : contract.getAddresses()) {
                    builder.addresses.add(EntityAddress.Builder.create(addressContract));
                }
            }
            builder.emailAddresses = new ArrayList<EntityEmail.Builder>();
            if (!CollectionUtils.isEmpty(contract.getEmailAddresses())) {
                for (EntityEmailContract emailContract : contract.getEmailAddresses()) {
                    builder.emailAddresses.add(EntityEmail.Builder.create(emailContract));
                }
            }
            builder.phoneNumbers = new ArrayList<EntityPhone.Builder>();
            if (!CollectionUtils.isEmpty(contract.getPhoneNumbers())) {
                for (EntityPhoneContract phoneContract : contract.getPhoneNumbers()) {
                    builder.phoneNumbers.add(EntityPhone.Builder.create(phoneContract));
                }
            }
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            builder.setActive(contract.isActive());
            return builder;
        }

        public EntityTypeContactInfo build() {
            return new EntityTypeContactInfo(this);
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
        public CodedAttribute.Builder getEntityType() {
            return this.entityType;
        }

        @Override
        public List<EntityAddress.Builder> getAddresses() {
            return this.addresses;
        }

        @Override
        public List<EntityEmail.Builder> getEmailAddresses() {
            return this.emailAddresses;
        }

        @Override
        public List<EntityPhone.Builder> getPhoneNumbers() {
            return this.phoneNumbers;
        }

        @Override
        public EntityAddress.Builder getDefaultAddress() {
            return EntityUtils.getDefaultItem(this.addresses);
        }

        @Override
        public EntityEmail.Builder getDefaultEmailAddress() {
            return EntityUtils.getDefaultItem(this.emailAddresses);
        }

        @Override
        public EntityPhone.Builder getDefaultPhoneNumber() {
            return EntityUtils.getDefaultItem(this.phoneNumbers);
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

        public void setEntityId(String entityId) {
            if (StringUtils.isEmpty(entityId)) {
                throw new IllegalArgumentException("entityId is empty");
            }
            this.entityId = entityId;
        }

        public void setEntityTypeCode(String entityTypeCode) {
            if (StringUtils.isEmpty(entityTypeCode)) {
                throw new IllegalArgumentException("entityTypeCode is empty");
            }
            this.entityTypeCode = entityTypeCode;
        }

        public void setEntityType(CodedAttribute.Builder entityType) {
            this.entityType = entityType;
        }

        public void setAddresses(List addresses) {
            this.addresses = addresses;
        }

        public void setEmailAddresses(List emailAddresses) {
            this.emailAddresses = emailAddresses;
        }

        public void setPhoneNumbers(List phoneNumbers) {
            this.phoneNumbers = phoneNumbers;
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

        final static String ROOT_ELEMENT_NAME = "entityTypeContactInfo";
        final static String TYPE_NAME = "EntityTypeContactInfoType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {

        final static String ENTITY_ID = "entityId";
        final static String ENTITY_TYPE_CODE = "entityTypeCode";
        final static String ENTITY_TYPE = "entityType";
        final static String ADDRESSES = "addresses";
        final static String ADDRESS = "address";
        final static String EMAIL_ADDRESSES = "emailAddresses";
        final static String EMAIL_ADDRESS = "emailAddress";
        final static String PHONE_NUMBERS = "phoneNumbers";
        final static String PHONE_NUMBER = "phoneNumber";
        final static String DEFAULT_ADDRESS = "defaultAddress";
        final static String DEFAULT_EMAIL_ADDRESS = "defaultEmailAddress";
        final static String DEFAULT_PHONE_NUMBER = "defaultPhoneNumber";
        final static String ACTIVE = "active";

    }

}
