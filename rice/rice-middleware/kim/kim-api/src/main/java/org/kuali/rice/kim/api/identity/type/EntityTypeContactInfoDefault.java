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

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.identity.address.EntityAddress;
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.kuali.rice.kim.api.identity.phone.EntityPhone;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Collection;

@XmlRootElement(name = EntityTypeContactInfoDefault.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = EntityTypeContactInfoDefault.Constants.TYPE_NAME, propOrder = {
    EntityTypeContactInfoDefault.Elements.ENTITY_TYPE_CODE,
    EntityTypeContactInfoDefault.Elements.DEFAULT_ADDRESS,
    EntityTypeContactInfoDefault.Elements.DEFAULT_EMAIL_ADDRESS,
    EntityTypeContactInfoDefault.Elements.DEFAULT_PHONE_NUMBER,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class EntityTypeContactInfoDefault extends AbstractDataTransferObject
{
    @XmlElement(name = Elements.ENTITY_TYPE_CODE, required = true)
    private final String entityTypeCode;
    @XmlElement(name = Elements.DEFAULT_ADDRESS, required = false)
    private final EntityAddress defaultAddress;
    @XmlElement(name = Elements.DEFAULT_EMAIL_ADDRESS, required = false)
    private final EntityEmail defaultEmailAddress;
    @XmlElement(name = Elements.DEFAULT_PHONE_NUMBER, required = false)
    private final EntityPhone defaultPhoneNumber;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private EntityTypeContactInfoDefault() {
        this.entityTypeCode = null;
        this.defaultAddress = null;
        this.defaultEmailAddress = null;
        this.defaultPhoneNumber = null;

    }

    public EntityTypeContactInfoDefault(String entityTypeCode, EntityAddress defaultAddress,
            EntityEmail defaultEmailAddress, EntityPhone defaultPhoneNumber) {
        this.entityTypeCode = entityTypeCode;
        this.defaultAddress = defaultAddress;
        this.defaultEmailAddress = defaultEmailAddress;
        this.defaultPhoneNumber = defaultPhoneNumber;
    }

    public EntityTypeContactInfoDefault(Builder builder) {
        this.entityTypeCode = builder.getEntityTypeCode();
        this.defaultAddress = builder.getDefaultAddress() == null ? null : builder.getDefaultAddress().build();
        this.defaultEmailAddress = builder.getDefaultEmailAddress() == null ? null : builder.getDefaultEmailAddress().build();
        this.defaultPhoneNumber = builder.getDefaultPhoneNumber() == null ? null : builder.getDefaultPhoneNumber().build();

    }

    public String getEntityTypeCode() {
        return this.entityTypeCode;
    }
    public EntityAddress getDefaultAddress() {
        return this.defaultAddress;
    }

    public EntityEmail getDefaultEmailAddress() {
        return this.defaultEmailAddress;
    }

    public EntityPhone getDefaultPhoneNumber() {
        return this.defaultPhoneNumber;
    }

    public final static class Builder
        implements Serializable, ModelBuilder
    {
        private String entityTypeCode;
        private EntityAddress.Builder defaultAddress;
        private EntityEmail.Builder defaultEmailAddress;
        private EntityPhone.Builder defaultPhoneNumber;

        private Builder() { }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(EntityTypeContactInfoDefault immutable) {
            if (immutable == null) {
                throw new IllegalArgumentException("EntityTypeDataDefault is null");
            }
            Builder builder = new Builder();
            builder.setEntityTypeCode(immutable.entityTypeCode);
            if (immutable.getDefaultAddress() != null) {
                builder.setDefaultAddress(EntityAddress.Builder.create(immutable.getDefaultAddress()));
            }
            if (immutable.getDefaultEmailAddress() != null) {
                builder.setDefaultEmailAddress(EntityEmail.Builder.create(immutable.getDefaultEmailAddress()));
            }
            if (immutable.getDefaultPhoneNumber() != null) {
                builder.setDefaultPhoneNumber(EntityPhone.Builder.create(immutable.getDefaultPhoneNumber()));
            }
            return builder;
        }

        public static Builder create(EntityTypeContactInfoContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract is null");
            }
            Builder builder = new Builder();
            builder.setEntityTypeCode(contract.getEntityTypeCode());
            if (contract.getDefaultAddress() != null) {
                builder.setDefaultAddress(EntityAddress.Builder.create(contract.getDefaultAddress()));
            }
            if (contract.getDefaultEmailAddress() != null) {
                builder.setDefaultEmailAddress(EntityEmail.Builder.create(contract.getDefaultEmailAddress()));
            }
            if (contract.getDefaultPhoneNumber() != null) {
                builder.setDefaultPhoneNumber(EntityPhone.Builder.create(contract.getDefaultPhoneNumber()));
            }
            return builder;
        }

        public EntityTypeContactInfoDefault build() {
            return new EntityTypeContactInfoDefault(this);
        }

        public String getEntityTypeCode() {
            return entityTypeCode;
        }

        public void setEntityTypeCode(String entityTypeCode) {
            this.entityTypeCode = entityTypeCode;
        }

        public EntityAddress.Builder getDefaultAddress() {
            return defaultAddress;
        }

        public void setDefaultAddress(EntityAddress.Builder defaultAddress) {
            this.defaultAddress = defaultAddress;
        }

        public EntityEmail.Builder getDefaultEmailAddress() {
            return defaultEmailAddress;
        }

        public void setDefaultEmailAddress(EntityEmail.Builder defaultEmailAddress) {
            this.defaultEmailAddress = defaultEmailAddress;
        }

        public EntityPhone.Builder getDefaultPhoneNumber() {
            return defaultPhoneNumber;
        }

        public void setDefaultPhoneNumber(EntityPhone.Builder defaultPhoneNumber) {
            this.defaultPhoneNumber = defaultPhoneNumber;
        }
    }



    /**
     * Defines some internal constants used on this class.
     *
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "entityTypeDataDefault";
        final static String TYPE_NAME = "EntityTypeDataDefaultType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {
        final static String ENTITY_TYPE_CODE = "entityTypeCode";
        final static String DEFAULT_ADDRESS = "defaultAddress";
        final static String DEFAULT_EMAIL_ADDRESS = "defaultEmailAddress";
        final static String DEFAULT_PHONE_NUMBER = "defaultPhoneNumber";

    }

}
