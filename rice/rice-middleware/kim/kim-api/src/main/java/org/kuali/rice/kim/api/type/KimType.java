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
package org.kuali.rice.kim.api.type;

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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An immutable representation of a {@link KimTypeContract}.
 *
 * <p>To construct an instance of a KimType, use the {@link KimType.Builder} class.<p/>
 *
 * @see KimTypeContract
 */
@XmlRootElement(name = KimType.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = KimType.Constants.TYPE_NAME, propOrder = {
        KimType.Elements.ID,
        KimType.Elements.SERVICE_NAME,
        KimType.Elements.NAMESPACE_CODE,
        KimType.Elements.NAME,
        KimType.Elements.ATTRIBUTE_DEFNS,
        KimType.Elements.ACTIVE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class KimType extends AbstractDataTransferObject implements KimTypeContract {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = KimType.Elements.ID, required = false)
    private final String id;

    @XmlElement(name = KimType.Elements.SERVICE_NAME, required = false)
    private final String serviceName;

    @XmlElement(name = KimType.Elements.NAMESPACE_CODE, required = false)
    private final String namespaceCode;

    @XmlElement(name = KimType.Elements.NAME, required = false)
    private final String name;

    @XmlElementWrapper(name = Elements.ATTRIBUTE_DEFNS, required = false)
    @XmlElement(name = KimType.Elements.ATTRIBUTE_DEFN, required = false)
    private final List<KimTypeAttribute> attributeDefinitions;

    @XmlElement(name = KimType.Elements.ACTIVE, required = false)
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
    private KimType() {
        this.id = null;
        this.serviceName = null;
        this.namespaceCode = null;
        this.name = null;
        this.attributeDefinitions = Collections.<KimTypeAttribute>emptyList();
        this.active = false;
        this.versionNumber = Long.valueOf(1L);
        this.objectId = null;
    }

    private KimType(Builder builder) {
        this.id = builder.getId();
        this.serviceName = builder.getServiceName();
        this.namespaceCode = builder.getNamespaceCode();
        this.name = builder.getName();
        final List<KimTypeAttribute> temp = new ArrayList<KimTypeAttribute>();
        for (KimTypeAttribute.Builder attr : builder.getAttributeDefinitions()) {
            //associate each attribute with this kimType's id
            attr.setKimTypeId(this.id);
            temp.add(attr.build());
        }
        this.attributeDefinitions = Collections.unmodifiableList(temp);

        this.active = builder.isActive();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }

    /**
     * Gets the KimTypeAttribute matching the id of it's KimAttribute.  If no attribute definition exists with that
     * id then null is returned.
     *
     * <p>
     * If multiple exist with the same id then the first match is returned.  Since id
     * is supposed to be unique this should not be a problem in practice.
     * </p>
     *
     * @param id the KimTypeAttribute.KimAttribute's id
     * @return the KimTypeAttribute or null
     * @throws IllegalArgumentException if the id is blank
     */
    public KimTypeAttribute getAttributeDefinitionById(String id) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("id is blank");
        }

        if (this.attributeDefinitions != null) {
            for (KimTypeAttribute att : this.attributeDefinitions) {
                if (att != null && att.getKimAttribute() != null
                        && id.equals(att.getKimAttribute().getId())) {
                    return att;
                }
            }
        }
		return null;
	}

    /**
     * Gets the KimTypeAttribute matching the name of it's KimAttribute.  If no attribute definition exists with that
     * name then null is returned.
     *
     * <p>
     * If multiple exist with the same name then the first match is returned.  Since name
     * is supposed to be unique this should not be a problem in practice.
     * </p>
     *
     * @param name the KimTypeAttribute's name
     * @return the KimTypeAttribute or null
     * @throws IllegalArgumentException if the name is blank
     */
	public KimTypeAttribute getAttributeDefinitionByName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name is blank");
        }

        if (this.attributeDefinitions != null) {
            for (KimTypeAttribute att : this.attributeDefinitions) {
                if (att != null && att.getKimAttribute() != null
                        && name.equals(att.getKimAttribute().getAttributeName())) {
                    return att;
                }
            }
        }
		return null;
	}

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public String getNamespaceCode() {
        return namespaceCode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<KimTypeAttribute> getAttributeDefinitions() {
        return attributeDefinitions;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

    @Override
    public String getObjectId() {
        return objectId;
    }

    /**
     * This builder constructs an KimType enforcing the constraints of the {@link KimTypeContract}.
     */
    public static final class Builder implements KimTypeContract, ModelBuilder, Serializable {
        private String id;
        private String serviceName;
        private String namespaceCode;
        private String name;
        private List<KimTypeAttribute.Builder> attributeDefinitions = new ArrayList<KimTypeAttribute.Builder>();
        private boolean active;
        private Long versionNumber = 1L;
        private String objectId;

        private Builder() {
        }

        /**
         * creates a KimType with the required fields.
         */
        public static Builder create() {
            return new Builder();
        }

        /**
         * creates a KimType from an existing {@link KimTypeContract}.
         */
        public static Builder create(KimTypeContract contract) {
        	if (contract == null) {
        		throw new IllegalArgumentException("contract was null");
        	}
            Builder builder = new Builder();
            builder.setId(contract.getId());
            builder.setServiceName(contract.getServiceName());
            builder.setNamespaceCode(contract.getNamespaceCode());
            builder.setName(contract.getName());

            if (contract.getAttributeDefinitions() != null) {
                final List<KimTypeAttribute.Builder> temp = new ArrayList<KimTypeAttribute.Builder>();
                for (KimTypeAttributeContract attr : contract.getAttributeDefinitions()) {
                    temp.add(KimTypeAttribute.Builder.create(attr));
                }

                builder.setAttributeDefinitions(Collections.unmodifiableList(temp));
            }

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
        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(final String serviceName) {
            this.serviceName = serviceName;
        }

        @Override
        public String getNamespaceCode() {
            return namespaceCode;
        }

        public void setNamespaceCode(final String namespaceCode) {
            this.namespaceCode = namespaceCode;
        }

        @Override
        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @Override
        public List<KimTypeAttribute.Builder> getAttributeDefinitions() {
            return attributeDefinitions;
        }

        public void setAttributeDefinitions(final List<KimTypeAttribute.Builder> attributeDefinitions) {
            if (attributeDefinitions == null) {
                throw new IllegalArgumentException("attributeDefinitions is null");
            }

            this.attributeDefinitions = attributeDefinitions;
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
            if (versionNumber != null && versionNumber <= 0) {
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
        public KimType build() {
            return new KimType(this);
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        static final String ROOT_ELEMENT_NAME = "kimType";
        static final String TYPE_NAME = "KimTypeType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        static final String ID = "id";
        static final String SERVICE_NAME = "serviceName";
        static final String NAMESPACE_CODE = "namespaceCode";
        static final String NAME = "name";
        static final String ATTRIBUTE_DEFNS = "attributeDefinitions";
        static final String ATTRIBUTE_DEFN = "attributeDefinition";
        static final String ACTIVE = "active";
    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + KimType.Constants.TYPE_NAME;
    }
}
