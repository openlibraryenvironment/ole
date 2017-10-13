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
package org.kuali.rice.kim.api.identity.affiliation;

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

@XmlRootElement(name = EntityAffiliation.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = EntityAffiliation.Constants.TYPE_NAME, propOrder = {
    EntityAffiliation.Elements.ID,
    EntityAffiliation.Elements.ENTITY_ID,
    EntityAffiliation.Elements.AFFILIATION_TYPE,
    EntityAffiliation.Elements.CAMPUS_CODE,
    EntityAffiliation.Elements.DEFAULT_VALUE,
    EntityAffiliation.Elements.ACTIVE,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class EntityAffiliation extends AbstractDataTransferObject
    implements EntityAffiliationContract
{

    @XmlElement(name = Elements.ENTITY_ID, required = false)
    private final String entityId;
    @XmlElement(name = Elements.AFFILIATION_TYPE, required = false)
    private final EntityAffiliationType affiliationType;
    @XmlElement(name = Elements.CAMPUS_CODE, required = false)
    private final String campusCode;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;
    @XmlElement(name = Elements.DEFAULT_VALUE, required = false)
    private final boolean defaultValue;
    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;
    @XmlElement(name = Elements.ID, required = false)
    private final String id;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private EntityAffiliation() {
        this.entityId = null;
        this.affiliationType = null;
        this.campusCode = null;
        this.versionNumber = null;
        this.objectId = null;
        this.defaultValue = false;
        this.active = false;
        this.id = null;
    }

    private EntityAffiliation(Builder builder) {
        this.entityId = builder.getEntityId();
        this.affiliationType = builder.getAffiliationType() != null ? builder.getAffiliationType().build() : null;
        this.campusCode = builder.getCampusCode();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
        this.defaultValue = builder.isDefaultValue();
        this.active = builder.isActive();
        this.id = builder.getId();
    }

    @Override
    public String getEntityId() {
        return this.entityId;
    }

    @Override
    public EntityAffiliationType getAffiliationType() {
        return this.affiliationType;
    }

    @Override
    public String getCampusCode() {
        return this.campusCode;
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
    public boolean isDefaultValue() {
        return this.defaultValue;
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
     * A builder which can be used to construct {@link EntityAffiliation} instances.  Enforces the constraints of the {@link EntityAffiliationContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, EntityAffiliationContract
    {

        private String entityId;
        private EntityAffiliationType.Builder affiliationType;
        private String campusCode;
        private Long versionNumber;
        private String objectId;
        private boolean defaultValue;
        private boolean active;
        private String id;

        private Builder() { }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(EntityAffiliationContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setEntityId(contract.getEntityId());
            if (contract.getAffiliationType() != null) {
                builder.setAffiliationType(EntityAffiliationType.Builder.create(contract.getAffiliationType()));
            }
            builder.setCampusCode(contract.getCampusCode());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            builder.setDefaultValue(contract.isDefaultValue());
            builder.setActive(contract.isActive());
            builder.setId(contract.getId());
            return builder;
        }

        public EntityAffiliation build() {
            return new EntityAffiliation(this);
        }

        @Override
        public String getEntityId() {
            return this.entityId;
        }

        @Override
        public EntityAffiliationType.Builder getAffiliationType() {
            return this.affiliationType;
        }

        @Override
        public String getCampusCode() {
            return this.campusCode;
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
        public boolean isDefaultValue() {
            return this.defaultValue;
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

        public void setAffiliationType(EntityAffiliationType.Builder affiliationType) {
            this.affiliationType = affiliationType;
        }

        public void setCampusCode(String campusCode) {
            this.campusCode = campusCode;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public void setDefaultValue(boolean defaultValue) {
            this.defaultValue = defaultValue;
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

        final static String ROOT_ELEMENT_NAME = "entityAffiliation";
        final static String TYPE_NAME = "EntityAffiliationType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String ENTITY_ID = "entityId";
        final static String AFFILIATION_TYPE = "affiliationType";
        final static String CAMPUS_CODE = "campusCode";
        final static String DEFAULT_VALUE = "defaultValue";
        final static String ACTIVE = "active";
        final static String ID = "id";

    }

}