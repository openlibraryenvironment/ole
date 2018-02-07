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
package org.kuali.rice.kim.api.identity.residency;

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

@XmlRootElement(name = EntityResidency.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = EntityResidency.Constants.TYPE_NAME, propOrder = {
    EntityResidency.Elements.ID,
    EntityResidency.Elements.ENTITY_ID,
    EntityResidency.Elements.DETERMINATION_METHOD,
    EntityResidency.Elements.IN_STATE,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class EntityResidency extends AbstractDataTransferObject
    implements EntityResidencyContract
{

    @XmlElement(name = Elements.ENTITY_ID, required = false)
    private final String entityId;
    @XmlElement(name = Elements.DETERMINATION_METHOD, required = false)
    private final String determinationMethod;
    @XmlElement(name = Elements.IN_STATE, required = false)
    private final String inState;
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
    private EntityResidency() {
        this.entityId = null;
        this.determinationMethod = null;
        this.inState = null;
        this.versionNumber = null;
        this.objectId = null;
        this.id = null;
    }

    private EntityResidency(Builder builder) {
        this.entityId = builder.getEntityId();
        this.determinationMethod = builder.getDeterminationMethod();
        this.inState = builder.getInState();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
        this.id = builder.getId();
    }

    @Override
    public String getEntityId() {
        return this.entityId;
    }

    @Override
    public String getDeterminationMethod() {
        return this.determinationMethod;
    }

    @Override
    public String getInState() {
        return this.inState;
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
     * A builder which can be used to construct {@link EntityResidency} instances.  Enforces the constraints of the {@link EntityResidencyContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, EntityResidencyContract
    {

        private String entityId;
        private String determinationMethod;
        private String inState;
        private Long versionNumber;
        private String objectId;
        private String id;

        private Builder() { }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(EntityResidencyContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setEntityId(contract.getEntityId());
            builder.setDeterminationMethod(contract.getDeterminationMethod());
            builder.setInState(contract.getInState());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            builder.setId(contract.getId());
            return builder;
        }

        public EntityResidency build() {
            return new EntityResidency(this);
        }

        @Override
        public String getEntityId() {
            return this.entityId;
        }

        @Override
        public String getDeterminationMethod() {
            return this.determinationMethod;
        }

        @Override
        public String getInState() {
            return this.inState;
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

        public void setDeterminationMethod(String determinationMethod) {
            this.determinationMethod = determinationMethod;
        }

        public void setInState(String inState) {
            this.inState = inState;
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

        final static String ROOT_ELEMENT_NAME = "entityResidency";
        final static String TYPE_NAME = "entityResidencyType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String ENTITY_ID = "entityId";
        final static String DETERMINATION_METHOD = "determinationMethod";
        final static String IN_STATE = "inState";
        final static String ID = "id";

    }

}