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
package org.kuali.rice.kim.api.common.attribute;

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

/**
 * An immutable representation of a {@link KimAttributeContract}.
 *
 * <p>To construct an instance of a KimAttribute, use the {@link KimAttribute.Builder} class.<p/>
 *
 * @see KimAttributeContract
 */
@XmlRootElement(name = KimAttribute.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = KimAttribute.Constants.TYPE_NAME, propOrder = {
        KimAttribute.Elements.ID,
        KimAttribute.Elements.COMPONENT_NAME,
        KimAttribute.Elements.ATTRIBUTE_NAME,
        KimAttribute.Elements.NAMESPACE_CODE,
        KimAttribute.Elements.ATTRIBUTE_LABEL,
        KimAttribute.Elements.ACTIVE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class KimAttribute extends AbstractDataTransferObject implements KimAttributeContract {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = KimAttribute.Elements.ID, required = false)
    private final String id;

    @XmlElement(name = KimAttribute.Elements.COMPONENT_NAME, required = false)
    private final String componentName;

    @XmlElement(name = KimAttribute.Elements.ATTRIBUTE_NAME, required = true)
    private final String attributeName;

    @XmlElement(name = KimAttribute.Elements.NAMESPACE_CODE, required = true)
    private final String namespaceCode;

    @XmlElement(name = KimAttribute.Elements.ATTRIBUTE_LABEL, required = false)
    private final String attributeLabel;

    @XmlElement(name = KimAttribute.Elements.ACTIVE, required = false)
    private final boolean active;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * This constructor should never be called except during JAXB unmarshalling.
     */
    private KimAttribute() {
        this.id = null;
        this.componentName = null;
        this.attributeName = null;
        this.namespaceCode = null;
        this.attributeLabel = null;
        this.active = false;
        this.versionNumber = Long.valueOf(1L);
        this.objectId = null;
    }

    private KimAttribute(Builder builder) {
        this.id = builder.getId();
        this.componentName = builder.getComponentName();
        this.attributeName = builder.getAttributeName();
        this.namespaceCode = builder.getNamespaceCode();
        this.attributeLabel = builder.getAttributeLabel();
        this.active = builder.isActive();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getComponentName() {
        return componentName;
    }

    @Override
    public String getAttributeName() {
        return attributeName;
    }

    @Override
    public String getNamespaceCode() {
        return namespaceCode;
    }

    @Override
    public String getAttributeLabel() {
        return attributeLabel;
    }

    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public String getObjectId() {
        return objectId;
    }

    /**
     * This builder constructs an KimAttribute enforcing the constraints of the {@link KimAttributeContract}.
     */
    public static final class Builder implements KimAttributeContract, ModelBuilder, Serializable {
        private String id;
        private String componentName;
        private String attributeName;
        private String namespaceCode;
        private String attributeLabel;
        private boolean active;
        private Long versionNumber = 1L;
        private String objectId;

        private Builder(String componentName, String attributeName, String namespaceCode) {
            setComponentName(componentName);
            setAttributeName(attributeName);
            setNamespaceCode(namespaceCode);
        }

        /**
         * creates a KimAttribute with the required fields.
         */
        public static Builder create(String componentName, String attributeName, String namespaceCode) {
            return new Builder(componentName, attributeName, namespaceCode);
        }

        /**
         * creates a KimAttribute from an existing {@link KimAttributeContract}.
         */
        public static Builder create(KimAttributeContract contract) {
            Builder builder = new Builder(contract.getComponentName(), contract.getAttributeName(), contract.getNamespaceCode());
            builder.setId(contract.getId());
            builder.setAttributeLabel(contract.getAttributeLabel());
            builder.setActive(contract.isActive());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }


        @Override
        public String getId() {
            return id;
        }

        public void setId(final String id) {
            this.id = id;
        }

        @Override
        public String getComponentName() {
            return componentName;
        }

        public void setComponentName(final String componentName) {
            this.componentName = componentName;
        }

        @Override
        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(final String attributeName) {
            if (StringUtils.isBlank(attributeName)) {
                throw new IllegalArgumentException("attributeName is blank");
            }

            this.attributeName = attributeName;
        }

        @Override
        public String getNamespaceCode() {
            return namespaceCode;
        }

        public void setNamespaceCode(final String namespaceCode) {
            if (StringUtils.isBlank(namespaceCode)) {
                throw new IllegalArgumentException("namespaceCode is blank");
            }

            this.namespaceCode = namespaceCode;
        }

        @Override
        public String getAttributeLabel() {
            return attributeLabel;
        }

        public void setAttributeLabel(final String attributeLabel) {
            this.attributeLabel = attributeLabel;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        public void setActive(final boolean active) {
            this.active = active;
        }

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(final Long versionNumber) {
            if (versionNumber <= 0) {
                throw new IllegalArgumentException("versionNumber is invalid");
            }

            this.versionNumber = versionNumber;
        }

        @Override
        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(final String objectId) {
            this.objectId = objectId;
        }

        @Override
        public KimAttribute build() {
            return new KimAttribute(this);
        }
    }


    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        static final String ROOT_ELEMENT_NAME = "kimAttribute";
        static final String TYPE_NAME = "KimAttributeType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        static final String ID = "id";
        static final String COMPONENT_NAME = "componentName";
        static final String ATTRIBUTE_NAME = "attributeName";
        static final String NAMESPACE_CODE = "namespaceCode";
        static final String ATTRIBUTE_LABEL = "attributeLabel";
        static final String ACTIVE = "active";
    }

}
