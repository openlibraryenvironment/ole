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
package org.kuali.rice.krms.api.repository.type;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.mo.ModelObjectUtils;
import org.kuali.rice.krms.api.KrmsConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An immutable, concrete model object implementation of a {@link KrmsTypeDefinitionContract}.
 * <p>To construct an instance of a KrmsTypeDefinition, use the {@link KrmsTypeDefinition.Builder} class.
 * Instances of KrmsType can be (un)marshalled to and from XML.<p/>
 *
 * @see KrmsTypeDefinitionContract
 */
@XmlRootElement(name = KrmsTypeDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = KrmsTypeDefinition.Constants.TYPE_NAME, propOrder = {
		KrmsTypeDefinition.Elements.ID,
		KrmsTypeDefinition.Elements.NAME,
		KrmsTypeDefinition.Elements.NAMESPACE,
		KrmsTypeDefinition.Elements.SERVICENAME,
		KrmsTypeDefinition.Elements.ACTIVE,
		KrmsTypeDefinition.Elements.ATTRIBUTES,
        CoreConstants.CommonElements.VERSION_NUMBER,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class KrmsTypeDefinition extends AbstractDataTransferObject implements KrmsTypeDefinitionContract{
	private static final long serialVersionUID = -8314397393380856301L;

	@XmlElement(name = Elements.ID, required = false)
	private String id;
	@XmlElement(name = Elements.NAME, required = true)
	private String name;
	@XmlElement(name = Elements.NAMESPACE, required = true)
	private String namespace;
	@XmlElement(name = Elements.SERVICENAME, required = false)
	private String serviceName;
	@XmlElement(name = Elements.ACTIVE, required = false)
	private boolean active;
	@XmlElement(name = Elements.ATTRIBUTE, required = false)
	private List<KrmsTypeAttribute> attributes;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<org.w3c.dom.Element> _futureElements = null;

	 /**
     * This constructor should never be called.  It is only present for use during JAXB unmarshalling.
     */
    private KrmsTypeDefinition() {
    	this.id = null;
    	this.name = null;
    	this.namespace = null;
    	this.serviceName = null;
    	this.active = true;
    	this.attributes = null;
        this.versionNumber = null;
    }

    /**
	 * Constructs a KRMS KrmsType from the given builder.  This constructor is private and should only
	 * ever be invoked from the builder.
	 *
	 * @param builder the Builder from which to construct the KRMS type
	 */
    private KrmsTypeDefinition(Builder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.namespace = builder.getNamespace();
        this.serviceName = builder.getServiceName();
        this.active = builder.isActive();
        this.attributes = ModelObjectUtils.buildImmutableCopy(builder.attributes);
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
	public List<KrmsTypeAttribute> getAttributes() {
		return this.attributes;
	}

    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

	/**
     * This builder is used to construct instances of KrmsTypeDefinition.  It enforces the constraints of the {@link KrmsTypeDefinitionContract}.
     */
    public static class Builder implements KrmsTypeDefinitionContract, ModelBuilder, Serializable {
		private static final long serialVersionUID = -3469525730879441547L;

		private String id;
        private String name;
        private String namespace;
        private String serviceName = "";
        private boolean active;
        private List<KrmsTypeAttribute.Builder> attributes;
        private Long versionNumber;

		/**
		 * Private constructor for creating a builder with all of it's required attributes.
		 */
        private Builder(String name, String namespace) {
            setName(name);
            setNamespace(namespace);
			setActive(true);
            setAttributes(new ArrayList<KrmsTypeAttribute.Builder>());
        }

        /**
         * fluent interface that sets the serviceName field of the Builder.
         *
         * @param serviceName the service used to resolve attribute values
         * @return a Builder object with the serviceName field set
         */
        public Builder serviceName(String serviceName){
        	this.serviceName = serviceName;
        	return this;
        }

        /**
         * fluent interface that sets the attributes of KrmsTypeDefinition builder.
         *
         * @param attributes List of KrmsTypeAttribute builder objects. {@link KrmsTypeAttribute.Builder}
         * @return a Builder object with the attributes collection set.
         */
        public Builder attributes(List<KrmsTypeAttribute.Builder> attributes){
        	setAttributes(attributes);
        	return this;
        }

        /**
         * Creates a KrmsTypeDefinition builder from the given parameters.
         *
         * @param name of the KrmsTypeDefinition
         * @param namespace to which the KrmsTypeDefinition belongs
         * @return an instance of the builder with the fields already populated
         * @throws IllegalArgumentException if the either the name or namespace is null or blank
         */
        public static Builder create(String name, String namespace) {
            return new Builder(name, namespace);
        }

        /**
         * Creates a builder by populating it with data from the given {@link KrmsTypeDefinitionContract}.
         *
         * @param contract the contract from which to populate this builder
         * @return an instance of the builder populated with data from the contract
         * @throws IllegalArgumentException if the contract is null
         */
        public static Builder create(KrmsTypeDefinitionContract contract) {
        	if (contract == null) {
                throw new IllegalArgumentException("contract is null");
            }
            Builder builder =  new Builder(contract.getName(), contract.getNamespace());
            builder.setId(contract.getId());
            builder.setNamespace(contract.getNamespace());
            builder.setActive(contract.isActive());
            builder.setServiceName(contract.getServiceName());
            List <KrmsTypeAttribute.Builder> attrBuilderList = new ArrayList<KrmsTypeAttribute.Builder>();
            if (contract.getAttributes() != null) {
            	for(KrmsTypeAttributeContract attr : contract.getAttributes()){
            		KrmsTypeAttribute.Builder myBuilder =
            			KrmsTypeAttribute.Builder.create(attr);
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
		 * @param id the id value to set; can be null; a null id is an indicator
         * the this has not yet been persisted to the database.
         * @throws IllegalArgumentException if the id is blank
		 */
        public void setId(String id) {
            if (id != null && StringUtils.isBlank(id)) {
                throw new IllegalArgumentException("ID must be non-blank");
            }
            this.id = id;
        }

        /**
         * Sets the name of the KrmsTypeDefinition
         * @param name string value to assign to the name; cannot be null or blank
         * @throws IllegalArgumentException if the name is null or blank
         */
		public void setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name is blank");
            }
			this.name = name;
		}

        /**
         * Sets the namespace of the KrmsTypeDefinition
         * @param namespace string value to assign to the namespace; cannot be null or blank
         * @throws IllegalArgumentException if the name is null or blank
         */
		public void setNamespace(String namespace) {
            if (StringUtils.isBlank(namespace)) {
                throw new IllegalArgumentException("namespace is blank");
            }
			this.namespace = namespace;
		}

        /**
         * Sets the name of the KRMS type service
         * @param serviceName can be null.
         */
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}

        /**
         * sets the List of attributes related to this KrmsTypeDefinition.
         *
         * @param attributes list of {@link KrmsTypeAttribute.Builder} representing the
         * attributes assigned to this KrmsTypeDefinition; List may be empty, but not null
         */
		public void setAttributes(List<KrmsTypeAttribute.Builder> attributes){
			this.attributes = attributes;
		}

        /**
         * sets the active indicator value
         * @param active boolean value to set
         */
		public void setActive(boolean active) {
			this.active = active;
		}

        /**
         * Sets the version number for this object.  In general, this value should only
         * be null if the object has not yet been stored to a persistent data store.
         * This version number is generally used for the purposes of optimistic locking.
         * @param versionNumber the version number, or null if one has not been assigned yet.
         */
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
		public List<KrmsTypeAttribute.Builder> getAttributes(){
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
		 * Builds an instance of a KrmsTypeDefinition based on the current state of the builder.
		 *
		 * @return the fully-constructed KrmsTypeDefinition
		 */
        @Override
        public KrmsTypeDefinition build() {
            return new KrmsTypeDefinition(this);
        }

    }

	/**
	 * Defines some internal constants used on this class.
	 */
	static class Constants {
		final static String ROOT_ELEMENT_NAME = "KRMSType";
		final static String TYPE_NAME = "KRMSTypeType";
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

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + KrmsTypeDefinition.Constants.TYPE_NAME;
    }
}
