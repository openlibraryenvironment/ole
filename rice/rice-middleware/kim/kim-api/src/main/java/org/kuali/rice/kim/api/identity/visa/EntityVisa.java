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
package org.kuali.rice.kim.api.identity.visa;

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

@XmlRootElement(name = EntityVisa.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = EntityVisa.Constants.TYPE_NAME, propOrder = {
    EntityVisa.Elements.ID,
    EntityVisa.Elements.ENTITY_ID,
    EntityVisa.Elements.VISA_TYPE_KEY,
    EntityVisa.Elements.VISA_ENTRY,
    EntityVisa.Elements.VISA_ID,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class EntityVisa extends AbstractDataTransferObject
    implements EntityVisaContract
{

    @XmlElement(name = Elements.ENTITY_ID, required = false)
    private final String entityId;
    @XmlElement(name = Elements.VISA_TYPE_KEY, required = false)
    private final String visaTypeKey;
    @XmlElement(name = Elements.VISA_ENTRY, required = false)
    private final String visaEntry;
    @XmlElement(name = Elements.VISA_ID, required = false)
    private final String visaId;
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
    private EntityVisa() {
        this.entityId = null;
        this.visaTypeKey = null;
        this.visaEntry = null;
        this.visaId = null;
        this.versionNumber = null;
        this.objectId = null;
        this.id = null;
    }

    private EntityVisa(Builder builder) {
        this.entityId = builder.getEntityId();
        this.visaTypeKey = builder.getVisaTypeKey();
        this.visaEntry = builder.getVisaEntry();
        this.visaId = builder.getVisaId();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
        this.id = builder.getId();
    }

    @Override
    public String getEntityId() {
        return this.entityId;
    }

    @Override
    public String getVisaTypeKey() {
        return this.visaTypeKey;
    }

    @Override
    public String getVisaEntry() {
        return this.visaEntry;
    }

    @Override
    public String getVisaId() {
        return this.visaId;
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
     * A builder which can be used to construct {@link EntityVisa} instances.  Enforces the constraints of the {@link EntityVisaContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, EntityVisaContract
    {

        private String entityId;
        private String visaTypeKey;
        private String visaEntry;
        private String visaId;
        private Long versionNumber;
        private String objectId;
        private String id;

        private Builder() { }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(EntityVisaContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setEntityId(contract.getEntityId());
            builder.setVisaTypeKey(contract.getVisaTypeKey());
            builder.setVisaEntry(contract.getVisaEntry());
            builder.setVisaId(contract.getVisaId());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            builder.setId(contract.getId());
            return builder;
        }

        public EntityVisa build() {
            return new EntityVisa(this);
        }

        @Override
        public String getEntityId() {
            return this.entityId;
        }

        @Override
        public String getVisaTypeKey() {
            return this.visaTypeKey;
        }

        @Override
        public String getVisaEntry() {
            return this.visaEntry;
        }

        @Override
        public String getVisaId() {
            return this.visaId;
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

        public void setVisaTypeKey(String visaTypeKey) {
            this.visaTypeKey = visaTypeKey;
        }

        public void setVisaEntry(String visaEntry) {
            this.visaEntry = visaEntry;
        }

        public void setVisaId(String visaId) {
            this.visaId = visaId;
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

        final static String ROOT_ELEMENT_NAME = "entityVisa";
        final static String TYPE_NAME = "EntityVisaType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String ENTITY_ID = "entityId";
        final static String VISA_TYPE_KEY = "visaTypeKey";
        final static String VISA_ENTRY = "visaEntry";
        final static String VISA_ID = "visaId";
        final static String ID = "id";

    }

}