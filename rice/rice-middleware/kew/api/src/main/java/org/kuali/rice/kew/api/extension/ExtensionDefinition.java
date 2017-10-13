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
package org.kuali.rice.kew.api.extension;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kew.api.KewApiConstants;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Immutable implementation of the {@link ExtensionDefinitionContract}.  Defines an extension to some component of
 * Kuali Enterprise Workflow.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = ExtensionDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = ExtensionDefinition.Constants.TYPE_NAME, propOrder = {
        ExtensionDefinition.Elements.ID,
        ExtensionDefinition.Elements.NAME,
        ExtensionDefinition.Elements.APPLICATION_ID,
        ExtensionDefinition.Elements.LABEL,
        ExtensionDefinition.Elements.DESCRIPTION,
        ExtensionDefinition.Elements.TYPE,
        ExtensionDefinition.Elements.RESOURCE_DESCRIPTOR,
        ExtensionDefinition.Elements.CONFIGURATION,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class ExtensionDefinition extends AbstractDataTransferObject implements ExtensionDefinitionContract {

    private static final long serialVersionUID = 6234968409006917945L;
    
    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElement(name = Elements.NAME, required = true)
    private final String name;

    @XmlElement(name = Elements.APPLICATION_ID, required = false)
    private final String applicationId;

    @XmlElement(name = Elements.LABEL, required = false)
    private final String label;

    @XmlElement(name = Elements.DESCRIPTION, required = false)
    private final String description;

    @XmlElement(name = Elements.TYPE, required = true)
    private final String type;

    @XmlElement(name = Elements.RESOURCE_DESCRIPTOR, required = true)
    private final String resourceDescriptor;

    @XmlElement(name = Elements.CONFIGURATION, required = false)
    @XmlJavaTypeAdapter(MapStringStringAdapter.class)
    private final Map<String, String> configuration;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    @SuppressWarnings("unused")
    private ExtensionDefinition() {
        this.id = null;
        this.name = null;
        this.applicationId = null;
        this.label = null;
        this.description = null;
        this.type = null;
        this.resourceDescriptor = null;
        this.configuration = null;
        this.versionNumber = null;
    }

    private ExtensionDefinition(Builder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.applicationId = builder.getApplicationId();
        this.label = builder.getLabel();
        this.description = builder.getDescription();
        this.type = builder.getType();
        this.resourceDescriptor = builder.getResourceDescriptor();
        if (builder.getConfiguration() == null) {
            this.configuration = Collections.emptyMap();
        } else {
            this.configuration = Collections.unmodifiableMap(new HashMap<String, String>(builder.getConfiguration()));
        }
        this.versionNumber = builder.getVersionNumber();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getApplicationId() {
        return this.applicationId;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getResourceDescriptor() {
        return this.resourceDescriptor;
    }

    @Override
    public Map<String, String> getConfiguration() {
        return this.configuration;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    /**
     * A builder which can be used to construct {@link ExtensionDefinition} instances.  Enforces the constraints of the
     * {@link ExtensionDefinitionContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, ExtensionDefinitionContract {

        private String id;
        private String name;
        private String applicationId;
        private String label;
        private String description;
        private String type;
        private String resourceDescriptor;
        private Map<String, String> configuration;
        private Long versionNumber;

        private Builder(String name, String type, String resourceDescriptor) {
            setName(name);
            setType(type);
            setResourceDescriptor(resourceDescriptor);
            setConfiguration(new HashMap<String, String>());
        }

        public static Builder create(String name, String type, String resourceDescriptor) {
            return new Builder(name, type, resourceDescriptor);
        }

        public static Builder create(ExtensionDefinitionContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getName(), contract.getType(), contract.getResourceDescriptor());
            builder.setId(contract.getId());
            builder.setApplicationId(contract.getApplicationId());
            builder.setLabel(contract.getLabel());
            builder.setDescription(contract.getDescription());
            builder.setConfiguration(contract.getConfiguration());
            builder.setVersionNumber(contract.getVersionNumber());
            return builder;
        }

        public ExtensionDefinition build() {
            return new ExtensionDefinition(this);
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getApplicationId() {
            return this.applicationId;
        }

        @Override
        public String getLabel() {
            return this.label;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public String getType() {
            return this.type;
        }

        @Override
        public String getResourceDescriptor() {
            return this.resourceDescriptor;
        }

        @Override
        public Map<String, String> getConfiguration() {
            return this.configuration;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name was null or blank");
            }
            this.name = name;
        }

        public void setApplicationId(String applicationId) {
            this.applicationId = applicationId;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setType(String type) {
            if (StringUtils.isBlank(type)) {
                throw new IllegalArgumentException("type was null or blank");
            }

            this.type = type;
        }

        public void setResourceDescriptor(String resourceDescriptor) {
            if (StringUtils.isBlank(resourceDescriptor)) {
                throw new IllegalArgumentException("descriptor was null or blank");
            }
            this.resourceDescriptor = resourceDescriptor;
        }

        public void setConfiguration(Map<String, String> configuration) {
            this.configuration = configuration;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "extensionDefinition";
        final static String TYPE_NAME = "ExtensionDefinitionType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String ID = "id";
        final static String NAME = "name";
        final static String APPLICATION_ID = "applicationId";
        final static String LABEL = "label";
        final static String DESCRIPTION = "description";
        final static String TYPE = "type";
        final static String RESOURCE_DESCRIPTOR = "resourceDescriptor";
        final static String CONFIGURATION = "configuration";
    }
    
    public static class Cache {
        public static final String NAME = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0 + "/" + ExtensionDefinition.Constants.TYPE_NAME;
    }
}
