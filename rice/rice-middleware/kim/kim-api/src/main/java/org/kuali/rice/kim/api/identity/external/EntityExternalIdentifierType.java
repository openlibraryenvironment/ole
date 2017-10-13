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
package org.kuali.rice.kim.api.identity.external;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Collection;

@XmlRootElement(name = EntityExternalIdentifierType.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = EntityExternalIdentifierType.Constants.TYPE_NAME, propOrder = {
    EntityExternalIdentifierType.Elements.CODE,
    EntityExternalIdentifierType.Elements.NAME,
    EntityExternalIdentifierType.Elements.SORT_CODE,
    EntityExternalIdentifierType.Elements.ACTIVE,
    EntityExternalIdentifierType.Elements.ENCRYPTION_REQUIRED,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class EntityExternalIdentifierType extends AbstractDataTransferObject
    implements EntityExternalIdentifierTypeContract
{
    @XmlElement(name = Elements.CODE, required = true)
    private final String code;
    @XmlElement(name = Elements.NAME, required = false)
    private final String name;
    @XmlElement(name = Elements.SORT_CODE, required = false)
    private final String sortCode;
    @XmlElement(name = Elements.ENCRYPTION_REQUIRED, required = false)
    private final boolean encryptionRequired;
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
    private EntityExternalIdentifierType() {
        this.name = null;
        this.code = null;
        this.sortCode = null;
        this.versionNumber = null;
        this.objectId = null;
        this.active = false;
        this.encryptionRequired = false;
    }

    private EntityExternalIdentifierType(Builder builder) {
        this.name = builder.getName();
        this.code = builder.getCode();
        this.sortCode = builder.getSortCode();
        this.encryptionRequired = builder.isEncryptionRequired();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
        this.active = builder.isActive();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getSortCode() {
        return this.sortCode;
    }

    @Override
    public boolean isEncryptionRequired() {
        return this.encryptionRequired;
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
     * A builder which can be used to construct {@link CodedAttribute} instances.  Enforces the constraints of the {@link CodedAttributeContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, EntityExternalIdentifierTypeContract
    {

        private String name;
        private String code;
        private String sortCode;
        private boolean encryptionRequired;
        private Long versionNumber;
        private String objectId;
        private boolean active;

        private Builder(String code) {
            setCode(code);
        }

        public static Builder create(String code) {
            return new Builder(code);
        }

        public static Builder create(EntityExternalIdentifierTypeContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getCode());
            builder.setName(contract.getName());
            builder.setSortCode(contract.getSortCode());
            builder.setEncryptionRequired(contract.isEncryptionRequired());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            builder.setActive(contract.isActive());
            return builder;
        }

        public EntityExternalIdentifierType build() {
            return new EntityExternalIdentifierType(this);
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getCode() {
            return this.code;
        }

        @Override
        public String getSortCode() {
            return this.sortCode;
        }

        @Override
        public boolean isEncryptionRequired() {
            return this.encryptionRequired;
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

        public void setName(String name) {
            this.name = name;
        }

        public void setCode(String code) {
            if (StringUtils.isWhitespace(code)) {
                throw new IllegalArgumentException("code is empty");
            }
            this.code = code;
        }

        public void setSortCode(String sortCode) {
            this.sortCode = sortCode;
        }

        public void setEncryptionRequired(boolean encryptionRequired) {
            this.encryptionRequired = encryptionRequired;
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

        final static String ROOT_ELEMENT_NAME = "entityExternalIdentifierType";
        final static String TYPE_NAME = "entityExternalIdentifierTypeType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String NAME = "name";
        final static String CODE = "code";
        final static String SORT_CODE = "sortCode";
        final static String ACTIVE = "active";
        final static String ENCRYPTION_REQUIRED = "encryptionRequired";

    }

}
