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
package org.kuali.rice.kew.api.repository.type;

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

/**
 * Concrete model object implementation of KEW KewAttributeDefinition. 
 * immutable. 
 * Instances of KewAttributeDefinition can be (un)marshalled to and from XML.
 *
 * @see KewAttributeDefinitionContract
 */
@XmlRootElement(name = KewAttributeDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = KewAttributeDefinition.Constants.TYPE_NAME, propOrder = {
		KewAttributeDefinition.Elements.ID,
		KewAttributeDefinition.Elements.NAME,
		KewAttributeDefinition.Elements.NAMESPACE,
        KewAttributeDefinition.Elements.LABEL,
        KewAttributeDefinition.Elements.DESCRIPTION,
		KewAttributeDefinition.Elements.ACTIVE,
		KewAttributeDefinition.Elements.COMPONENT_NAME,
        CoreConstants.CommonElements.VERSION_NUMBER,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class KewAttributeDefinition extends AbstractDataTransferObject implements KewAttributeDefinitionContract {
	private static final long serialVersionUID = -6356968810972165031L;
	
	@XmlElement(name = Elements.ID, required=true)
	private final String id;
	@XmlElement(name = Elements.NAME, required=true)
	private final String name;
	@XmlElement(name = Elements.NAMESPACE, required=true)
	private final String namespace;
    @XmlElement(name = Elements.LABEL, required=false)
    private final String label;
    @XmlElement(name = Elements.DESCRIPTION, required=false)
    private final String description;
	@XmlElement(name = Elements.ACTIVE, required=false)
	private final boolean active;
	@XmlElement(name = Elements.COMPONENT_NAME, required=false)
	private final String componentName;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
	
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<org.w3c.dom.Element> _futureElements = null;
	
	 /** 
     * This constructor should never be called.  It is only present for use during JAXB unmarshalling. 
     */
    private KewAttributeDefinition() {
    	this.id = null;
    	this.name = null;
    	this.namespace = null;
    	this.label = null;
        this.description = null;
    	this.active = false;
    	this.componentName = null;
        this.versionNumber = null;
    }
    
    /**
	 * Constructs a KewAttributeDefinition from the given builder.  This constructor is private and should only
	 * ever be invoked from the builder.
	 * 
	 * @param builder the Builder from which to construct the KewAttributeDefinition
	 */
    private KewAttributeDefinition(Builder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.namespace = builder.getNamespace();
        this.label = builder.getLabel();
        this.description = builder.getDescription();
        this.active = builder.isActive();
        this.componentName = builder.getComponentName();
        this.versionNumber = builder.getVersionNumber();
    }
    
	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}

	public String getNamespace() {
		return this.namespace;
	}

	public String getLabel() {
		return this.label;
	}

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
		return this.active; 
	}

	public String getComponentName() {
		return this.componentName;
	}
	
    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }
        
	/**
     * This builder is used to construct instances of KewAttributeDefinition.  It enforces the constraints of the {@link KewAttributeDefinitionContract}.
     */
    public static class Builder implements KewAttributeDefinitionContract, ModelBuilder, Serializable {		
		private static final long serialVersionUID = -2110564370088779631L;
		
		private String id;
        private String name;
        private String namespace;
        private String label;
        private String description;
        private boolean active;
        private String componentName;
        private Long versionNumber;

		/**
		 * Private constructor for creating a builder with all of it's required attributes.
		 */
        private Builder(String id, String name, String namespace) {
            setId(id);
            setName(name);
            setNamespace(namespace);
			setActive(true);
        }

        public Builder label(String label){
        	setLabel(label);
        	return this;
        }
        public Builder componentName(String componentName){
        	setComponentName(componentName);
        	return this;
        }
        /**
         * Creates a builder from the given parameters.
         * 
         * @param id the KewAttributeDefinition id
         * @param name the KewAttributeDefinition name
         * @param namespace the KewAttributeDefinition namespace
         * @return an instance of the builder with the fields already populated
         * @throws IllegalArgumentException if the either the id, name or namespace is null or blank
         */
        public static Builder create(String id, String name, String namespace) {
            return new Builder(id, name, namespace);
        }

        /**
         * Creates a builder by populating it with data from the given {@link KewAttributeDefinitionContract}.
         * 
         * @param contract the contract from which to populate this builder
         * @return an instance of the builder populated with data from the contract
         */
        public static Builder create(KewAttributeDefinitionContract contract) {
        	if (contract == null) {
                throw new IllegalArgumentException("contract is null");
            }
            Builder builder =  new Builder(contract.getId(), contract.getName(), contract.getNamespace());
            builder.setActive(contract.isActive());
            builder.setLabel(contract.getLabel());
            builder.setDescription(contract.getDescription());
            builder.setComponentName(contract.getComponentName());
            builder.setVersionNumber(contract.getVersionNumber());
            return builder;
        }

		/**
		 * Sets the value of the id on this builder to the given value.
		 * 
		 * @param id the id value to set, must be null or non-blank
		 * @throws IllegalArgumentException if the id is non-null and blank
		 */
        public void setId(String id) {
            if (null != id && StringUtils.isBlank(id)) {
                throw new IllegalArgumentException("id must be null or non-blank");
            }
            this.id = id;
        }

		public void setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name is blank");
            }
			this.name = name;
		}

		public void setNamespace(String namespace) {
            if (StringUtils.isBlank(namespace)) {
                throw new IllegalArgumentException("namespace is blank");
            }
			this.namespace = namespace;
		}
		
		public void setLabel(String label) {
			this.label = label;
		}

        public void setDescription(String description) {
            this.description = description;
        }


		public void setComponentName(String componentName) {
			this.componentName = componentName;
		}
		
		public void setActive(boolean active) {
			this.active = active;
		}

        public void setVersionNumber(Long versionNumber){
            this.versionNumber = versionNumber;
        }
        
		@Override
		public String getId() {
			return id;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getNamespace() {
			return namespace;
		}

		@Override
		public String getComponentName() {
			return componentName;
		}

		@Override
		public String getLabel() {
			return label;
		}

        @Override
        public String getDescription() {
            return description;
        }

        @Override
		public boolean isActive() {
			return active;
		}

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

		/**
		 * Builds an instance of a CampusType based on the current state of the builder.
		 * 
		 * @return the fully-constructed CampusType
		 */
        @Override
        public KewAttributeDefinition build() {
            return new KewAttributeDefinition(this);
        }
		
    }

	/**
	 * Defines some internal constants used on this class.
	 */
	static class Constants {
		final static String ROOT_ELEMENT_NAME = "KewAttributeDefinition";
		final static String TYPE_NAME = "KewAttributionDefinitionType";
	}
	
	/**
	 * A private class which exposes constants which define the XML element names to use
	 * when this object is marshalled to XML.
	 */
	public static class Elements {
		final static String ID = "id";
		final static String NAME = "name";
		final static String NAMESPACE = "namespace";
		final static String LABEL = "label";
        final static String DESCRIPTION = "description";
		final static String COMPONENT_NAME = "componentName";
		final static String ACTIVE = "active";
    }
}
