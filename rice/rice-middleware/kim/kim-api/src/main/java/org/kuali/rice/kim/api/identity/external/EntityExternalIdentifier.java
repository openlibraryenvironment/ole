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

import java.io.Serializable;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

@XmlRootElement(name = EntityExternalIdentifier.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = EntityExternalIdentifier.Constants.TYPE_NAME, propOrder = {
    EntityExternalIdentifier.Elements.ID,
    EntityExternalIdentifier.Elements.ENTITY_ID,
    EntityExternalIdentifier.Elements.EXTERNAL_IDENTIFIER_TYPE_CODE,
    EntityExternalIdentifier.Elements.EXTERNAL_IDENTIFIER_TYPE,
    EntityExternalIdentifier.Elements.EXTERNAL_ID,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class EntityExternalIdentifier extends AbstractDataTransferObject
    implements EntityExternalIdentifierContract
{
    @XmlElement(name = Elements.ENTITY_ID, required = false)
    private final String entityId;
    @XmlElement(name = Elements.EXTERNAL_IDENTIFIER_TYPE_CODE, required = false)
    private final String externalIdentifierTypeCode;
    @XmlElement(name = Elements.EXTERNAL_IDENTIFIER_TYPE, required = false)
    private final EntityExternalIdentifierType externalIdentifierType;
    @XmlElement(name = Elements.EXTERNAL_ID, required = false)
    private final String externalId;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;
    @XmlElement(name = Elements.ID, required = false)
    private final String id;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private EntityExternalIdentifier() {
        this.entityId = null;
        this.externalIdentifierTypeCode = null;
        this.externalIdentifierType = null;
        this.externalId = null;
        this.versionNumber = null;
        this.objectId = null;
        this.id = null;
    }

    private EntityExternalIdentifier(Builder builder) {
        this.entityId = builder.getEntityId();
        this.externalIdentifierTypeCode = builder.getExternalIdentifierTypeCode();
        this.externalIdentifierType = builder.getExternalIdentifierType() != null ? builder.getExternalIdentifierType().build() : null;
        this.externalId = builder.getExternalId();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
        this.id = builder.getId();
    }

    @Override
    public String getEntityId() {
        return this.entityId;
    }

    @Override
    public String getExternalIdentifierTypeCode() {
        return this.externalIdentifierTypeCode;
    }

    @Override
    public EntityExternalIdentifierType getExternalIdentifierType() {
        return this.externalIdentifierType;
    }

    @Override
    public String getExternalId() {
        return this.externalId;
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
    public String getId() {
        return this.id;
    }

    /**
     * A builder which can be used to construct {@link EntityExternalIdentifier} instances.  Enforces the constraints of the {@link EntityExternalIdentifierContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, EntityExternalIdentifierContract
    {

        private String entityId;
        private String externalIdentifierTypeCode;
        private EntityExternalIdentifierType.Builder externalIdentifierType;
        private String externalId;
        private Long versionNumber;
        private String objectId;
        private String id;

        private Builder() { }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(EntityExternalIdentifierContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setEntityId(contract.getEntityId());
            builder.setExternalIdentifierTypeCode(contract.getExternalIdentifierTypeCode());
            if (contract.getExternalIdentifierType() != null) {
                builder.setExternalIdentifierType(
                        EntityExternalIdentifierType.Builder.create(contract.getExternalIdentifierType()));
            }
            builder.setExternalId(contract.getExternalId());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            builder.setId(contract.getId());
            return builder;
        }

        public EntityExternalIdentifier build() {
            return new EntityExternalIdentifier(this);
        }

        @Override
        public String getEntityId() {
            return this.entityId;
        }

        @Override
        public String getExternalIdentifierTypeCode() {
            return this.externalIdentifierTypeCode;
        }

        @Override
        public EntityExternalIdentifierType.Builder getExternalIdentifierType() {
            return this.externalIdentifierType;
        }

        @Override
        public String getExternalId() {
            return this.externalId;
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
        public String getId() {
            return this.id;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public void setExternalIdentifierTypeCode(String externalIdentifierTypeCode) {
            this.externalIdentifierTypeCode = externalIdentifierTypeCode;
        }

        public void setExternalIdentifierType(EntityExternalIdentifierType.Builder externalIdentifierType) {
            this.externalIdentifierType = externalIdentifierType;
        }

        public void setExternalId(String externalId) {
            this.externalId = externalId;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
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

        final static String ROOT_ELEMENT_NAME = "entityExternalIdentifier";
        final static String TYPE_NAME = "EntityExternalIdentifierType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String ENTITY_ID = "entityId";
        final static String EXTERNAL_IDENTIFIER_TYPE = "externalIdentifierType";
        final static String EXTERNAL_IDENTIFIER_TYPE_CODE = "externalIdentifierTypeCode";
        final static String EXTERNAL_ID = "externalId";
        final static String ID = "id";

    }

}