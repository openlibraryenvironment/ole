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
package org.kuali.rice.kim.api.identity.principal;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.KimConstants;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Collection;

@XmlRootElement(name = Principal.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = Principal.Constants.TYPE_NAME, propOrder = {
    Principal.Elements.PRINCIPAL_ID,
    Principal.Elements.PRINCIPAL_NAME,
    Principal.Elements.ENTITY_ID,
    Principal.Elements.ACTIVE,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class Principal extends AbstractDataTransferObject
    implements PrincipalContract
{

    @XmlElement(name = Elements.PRINCIPAL_ID, required = false)
    private final String principalId;
    @XmlElement(name = Elements.PRINCIPAL_NAME, required = false)
    private final String principalName;
    @XmlElement(name = Elements.ENTITY_ID, required = false)
    private final String entityId;
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
    private Principal() {
        this.principalId = null;
        this.principalName = null;
        this.entityId = null;
        this.active = false;
        this.versionNumber = null;
        this.objectId = null;
    }

    private Principal(Builder builder) {
        this.principalId = builder.getPrincipalId();
        this.principalName = builder.getPrincipalName();
        this.entityId = builder.getEntityId();
        this.active = builder.isActive();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }

    @Override
    public String getPrincipalId() {
        return this.principalId;
    }

    @Override
    public String getPrincipalName() {
        return this.principalName;
    }

    @Override
    public String getEntityId() {
        return this.entityId;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getObjectId() {
        return this.objectId;
    }

    /**
     * A builder which can be used to construct {@link Principal} instances.  Enforces the constraints of the {@link PrincipalContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, PrincipalContract
    {

        private String principalId;
        private String principalName;
        private String entityId;
        private boolean active;
        private Long versionNumber;
        private String objectId;

        private Builder(String principalName) {
            setPrincipalName(principalName);
        }

        public static Builder create(String principalName) {
            return new Builder(principalName);
        }

        public static Builder create(PrincipalContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getPrincipalName());
            builder.setPrincipalId(contract.getPrincipalId());
            builder.setEntityId(contract.getEntityId());
            builder.setActive(contract.isActive());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

        public Principal build() {
            return new Principal(this);
        }


        @Override
        public String getPrincipalId() {
            return this.principalId;
        }

        @Override
        public String getPrincipalName() {
            return this.principalName;
        }

        @Override
        public String getEntityId() {
            return this.entityId;
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        @Override
        public String getObjectId() {
            return this.objectId;
        }

        public void setPrincipalId(String principalId) {
            if (StringUtils.isWhitespace(principalId)) {
                throw new IllegalArgumentException("principalId is blank");
            }
            this.principalId = principalId;
        }

        public void setPrincipalName(String principalName) {
            if (StringUtils.isEmpty(principalName)) {
                throw new IllegalArgumentException("principalName is blank");
            }
            this.principalName = principalName;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "principal";
        final static String TYPE_NAME = "PrincipalType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String PRINCIPAL_ID = "principalId";
        final static String PRINCIPAL_NAME = "principalName";
        final static String ENTITY_ID = "entityId";
        final static String ACTIVE = "active";

    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Principal.Constants.TYPE_NAME;
    }
}