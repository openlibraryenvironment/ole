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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;

/**
 * Concrete model object implementation of KEW KewType. 
 * immutable. 
 * Instances of KewType can be (un)marshalled to and from XML.
 *
 * @see KewTypeDefinitionContract
 */
@XmlRootElement(name = KewTypeDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = KewTypeDefinition.Constants.TYPE_NAME, propOrder = {
		KewTypeDefinition.Elements.ID,
		KewTypeDefinition.Elements.NAME,
		KewTypeDefinition.Elements.NAMESPACE,
		KewTypeDefinition.Elements.SERVICENAME,
		KewTypeDefinition.Elements.ACTIVE,
		KewTypeDefinition.Elements.ATTRIBUTES,
        CoreConstants.CommonElements.VERSION_NUMBER,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class KewTypeDefinition extends AbstractDataTransferObject implements KewTypeDefinitionContract{
	private static final long serialVersionUID = -8314397393380856301L;

	@XmlElement(name = Elements.ID, required=true)
	private String id;
	@XmlElement(name = Elements.NAME, required=true)
	private String name;
	@XmlElement(name = Elements.NAMESPACE, required=true)
	private String namespace;
	@XmlElement(name = Elements.SERVICENAME, required=false)
	private String serviceName;
	@XmlElement(name = Elements.ACTIVE, required=false)
	private boolean active;
	@XmlElement(name = Elements.ATTRIBUTE, required=false)
	private List<KewTypeAttribute> attributes;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
	
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<org.w3c.dom.Element> _futureElements = null;
	
	 /** 
     * This constructor should never be called.  It is only present for use during JAXB unmarshalling. 
     */
    private KewTypeDefinition() {
    	this.id = null;
    	this.name = null;
    	this.namespace = null;
    	this.serviceName = null;
    	this.active = false;
    	this.attributes = null;
        this.versionNumber = null;
    }
    
    /**
	 * Constructs a KEW KewType from the given builder.  This constructor is private and should only
	 * ever be invoked from the builder.
	 * 
	 * @param builder the Builder from which to construct the KEW type
	 */
    private KewTypeDefinition(Builder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.namespace = builder.getNamespace();
        this.serviceName = builder.getServiceName();
        this.active = builder.isActive();
        List<KewTypeAttribute> attrList = new ArrayList<KewTypeAttribute>();
        if (builder.attributes != null){
        		for (KewTypeAttribute.Builder b : builder.attributes){
        			attrList.add(b.build());
        		}
        }
        this.attributes = Collections.unmodifiableList(attrList);
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
	public String getNamespace() {
		return this.namespace;
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}
	
	@Override
	public boolean isActive() {
		return this.active; 
	}

	@Override
	public List<KewTypeAttribute> getAttributes() {
		return this.attributes; 
	}

    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

    /**
     * Gets the KewTypeAttribute matching the name of it's KewAttribute.  If no attribute definition exists with that
     * name then null is returned.
     *
     * <p>
     * If multiple exist with the same name then the first match is returned.  Since name
     * is supposed to be unique this should not be a problem in practice.
     * </p>
     *
     * @param name the KewTypeAttribute's name
     * @return the KewTypeAttribute or null
     * @throws IllegalArgumentException if the name is blank
     */
	public KewAttributeDefinition getAttributeDefinitionByName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name was a null or blank value");
        }
        if (CollectionUtils.isNotEmpty(getAttributes())) {
            for (KewTypeAttribute attribute : getAttributes()) {
                if (name.equals(attribute.getAttributeDefinition().getName())) {
                    return attribute.getAttributeDefinition();
                }
            }
        }
        return null;
	}

	/**
     * This builder is used to construct instances of KEW KewType.  It enforces the constraints of the {@link KewTypeDefinitionContract}.
     */
    public static class Builder implements KewTypeDefinitionContract, ModelBuilder, Serializable {		
		private static final long serialVersionUID = -3469525730879441547L;
		
		private String id;
        private String name;
        private String namespace;
        private String serviceName = "";
        private boolean active;
        private List<KewTypeAttribute.Builder> attributes;
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

        public Builder serviceName(String serviceName){
        	this.serviceName = serviceName;
        	return this;
        }
        
        public Builder attributes(List<KewTypeAttribute.Builder> attributes){
        	setAttributes(attributes);
        	return this;
        }

        /**
         * Creates a builder from the given parameters.
         * 
         * @param id the KEW type id
         * @param name the KEW type name
         * @param namespace the KEW type namespace
         * @return an instance of the builder with the fields already populated
         * @throws IllegalArgumentException if the either the id, name or namespace is null or blank
         */
        public static Builder create(String id, String name, String namespace) {
            return new Builder(id, name, namespace);
        }

        /**
         * Creates a builder by populating it with data from the given {@link KewTypeDefinitionContract}.
         * 
         * @param contract the contract from which to populate this builder
         * @return an instance of the builder populated with data from the contract
         */
        public static Builder create(KewTypeDefinitionContract contract) {
        	if (contract == null) {
                throw new IllegalArgumentException("contract is null");
            }
            Builder builder =  new Builder(contract.getId(), contract.getName(), contract.getNamespace());
            builder.setNamespace(contract.getNamespace());
            builder.setActive(contract.isActive());
            builder.setServiceName(contract.getServiceName());
            List <KewTypeAttribute.Builder> attrBuilderList = new ArrayList<KewTypeAttribute.Builder>();
            if (contract.getAttributes() != null) {
            	for(KewTypeAttributeContract attr : contract.getAttributes()){
            		KewTypeAttribute.Builder myBuilder = 
            			KewTypeAttribute.Builder.create(attr);
            		attrBuilderList.add(myBuilder);
            	}
            }
            builder.setAttributes(attrBuilderList);
            builder.setVersionNumber(contract.getVersionNumber());
            return builder;
        }

		/**
		 * Sets the value of the id on this builder to the given value.
		 * 
		 * @param id the id value to set, must not be blank
		 * @throws IllegalArgumentException if the id is blank
		 */
        public void setId(String id) {
            if (id != null && StringUtils.isBlank(id)) {
                throw new IllegalArgumentException("id is blank");
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
		
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
		
		public void setAttributes(List<KewTypeAttribute.Builder> attributes){
			if (attributes == null || attributes.isEmpty()){
				this.attributes = Collections.unmodifiableList(new ArrayList<KewTypeAttribute.Builder>());
				return;
			}
			this.attributes = Collections.unmodifiableList(attributes);
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
		public String getServiceName() {
			return serviceName;
		}
		
		@Override
		public List<KewTypeAttribute.Builder> getAttributes(){
			return attributes;
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
		 * Builds an instance of a KewType based on the current state of the builder.
		 * 
		 * @return the fully-constructed KewType
		 */
        @Override
        public KewTypeDefinition build() {
            return new KewTypeDefinition(this);
        }
		
    }

	/**
	 * Defines some internal constants used on this class.
	 */
	static class Constants {
		final static String ROOT_ELEMENT_NAME = "KEWType";
		final static String TYPE_NAME = "KEWTypeType";
	}
	
	/**
	 * A private class which exposes constants which define the XML element names to use
	 * when this object is marshalled to XML.
	 */
	public static class Elements {
		final static String ID = "id";
		final static String NAME = "name";
		final static String NAMESPACE = "namespace";
		final static String SERVICENAME = "serviceName";
		final static String ACTIVE = "active";
		final static String ATTRIBUTE = "attribute";
		final static String ATTRIBUTES = "attributes";
	}
}
