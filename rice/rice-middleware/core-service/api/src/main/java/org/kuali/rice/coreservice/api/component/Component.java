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
package org.kuali.rice.coreservice.api.component;

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
 * An immutable representation of an {@link ComponentContract}.
 *
 * Use the class {@link Component.Builder} to construct an Component object.
 * 
 * @see ComponentContract
 */
@XmlRootElement(name = Component.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = Component.Constants.TYPE_NAME, propOrder = {
    Component.Elements.NAMESPACE_CODE,
    Component.Elements.CODE,
    Component.Elements.NAME,
    Component.Elements.COMPONENT_SET_ID,
    Component.Elements.ACTIVE,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class Component extends AbstractDataTransferObject implements ComponentContract {
	
	private static final long serialVersionUID = -5114772381708593543L;

	@XmlElement(name = Elements.NAMESPACE_CODE, required=true)
	private final String namespaceCode;
	
    @XmlElement(name = Elements.CODE, required=true)
    private final String code;

    @XmlElement(name = Elements.NAME, required=true)
    private final String name;

    @XmlElement(name = Elements.COMPONENT_SET_ID, required = false)
    private final String componentSetId;

    @XmlElement(name = Elements.ACTIVE, required=false)
    private final boolean active;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    
    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
	private final String objectId;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /** 
     * This constructor should never be called.  It is only present for use during JAXB unmarshalling. 
     */
    private Component() {
    	this.namespaceCode = null;
    	this.code = null;
    	this.name = null;
        this.componentSetId = null;
    	this.active = true;
        this.versionNumber = null;
        this.objectId = null;
    }

	/**
	 * Constructs a Component from the given builder.  This constructor is private and should only
	 * ever be invoked from the builder.
	 *
	 * @param builder the Builder from which to construct the component
	 */
    private Component(Builder builder) {
		namespaceCode = builder.getNamespaceCode();
		code = builder.getCode();
        name = builder.getName();
        componentSetId = builder.getComponentSetId();
        active = builder.isActive();
        versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }
    
    
    @Override
    public String getNamespaceCode() {
		return namespaceCode;
	}

    @Override
	public String getCode() {
		return code;
	}

    @Override
	public String getName() {
		return name;
	}

    @Override
    public String getComponentSetId() {
        return componentSetId;
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
     * This builder is used to construct instances of Component.  It enforces the constraints of the {@link ComponentContract}.
     */
    public static final class Builder implements ComponentContract, ModelBuilder, Serializable {

		private static final long serialVersionUID = 5548130104299578283L;

		private String namespaceCode;
		private String code;
        private String name;
        private String componentSetId;
        private boolean active;
        private Long versionNumber;
        private String objectId;

		/**
		 * Private constructor for creating a builder with all of it's required attributes.
		 */
        private Builder(String namespaceCode, String code, String name) {
			setNamespaceCode(namespaceCode);
            setCode(code);
            setName(name);
			setActive(true);
        }

        /**
		 * Constructs a Namespace builder given the namcespace code, component code, and name
		 * which are all required.  Defaults the active indicator to true.
		 *
		 * @param namespaceCode the namespace code to use when constructing this builder
		 * @param code the component code to use when constructing this builder
		 * @param name the component name to use when constructing this builder
		 * @throws IllegalArgumentException if any of the parameters are null or blank
		 */
        public static Builder create(String namespaceCode, String code, String name) {
            return new Builder(namespaceCode, code, name);
        }

        /**
         * Creates a builder by populating it with data from the given {@link ComponentContract}.
         * 
         * @param contract the contract from which to populate this builder
         * @return an instance of the builder populated with data from the contract
         */
        public static Builder create(ComponentContract contract) {
            Builder builder = new Builder(contract.getNamespaceCode(), contract.getCode(), contract.getName());
            builder.setComponentSetId(contract.getComponentSetId());
            builder.setActive(contract.isActive());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

		/**
		 * Sets the value of the namespace code on this builder to the given value.
	     *
	     * @param namespaceCode the namespace code value to set, must not be null or blank
	     * @throws IllegalArgumentException if the given namespace code is null or blank
	     */
        public void setNamespaceCode(String namespaceCode) {
			if (StringUtils.isBlank(namespaceCode)) {
			   throw new IllegalArgumentException("namespaceCode is null");
		   }
		   this.namespaceCode = namespaceCode;
	   }
		
		/**
 		 * Sets the value of the component code on this builder to the given value.
		 *
		 * @param code the component code value to set, must not be null or blank
		 * @throws IllegalArgumentException if the given component code is null or blank
		 */
        public void setCode(String code) {
            if (StringUtils.isBlank(code)) {
                throw new IllegalArgumentException("code is blank");
            }
            this.code = code;
        }

		/**
		 * Sets the value of the component name on this builder to the given value.
	     *
	     * @param name the component name value to set, must not be null or blank
	     * @throws IllegalArgumentException if the given component name is null or blank
	     */
        public void setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name is blank");
            }
            this.name = name;
        }

        @Override
		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

        @Override
		public String getNamespaceCode() {
			return namespaceCode;
		}

        @Override
		public String getCode() {
			return code;
		}

        @Override
		public String getName() {
			return name;
		}

        @Override
        public String getComponentSetId() {
            return componentSetId;
        }

        public void setComponentSetId(String componentSetId) {
            if (componentSetId != null && StringUtils.isBlank(componentSetId)) {
                throw new IllegalArgumentException("componentSetId should be either null or a non-blank value");
            }
            this.componentSetId = componentSetId;
        }

        @Override
		public Long getVersionNumber() {
			return versionNumber;
		}

		public void setVersionNumber(Long versionNumber) {
			this.versionNumber = versionNumber;
		}
		
		@Override
    	public String getObjectId() {
    		return objectId;
    	}
		
		public void setObjectId(String objectId) {
        	this.objectId = objectId;
        }

		/**
		 * Builds an instance of a Component based on the current state of the builder.
		 *
		 * @return the fully-constructed Component
		 */
		@Override
        public Component build() {
            return new Component(this);
        }

    }
	
	/**
	 * Defines some internal constants used on this class.
	 */
	static class Constants {
	   final static String ROOT_ELEMENT_NAME = "component";
	   final static String TYPE_NAME = "ComponentType";
	}
   
	/**
	 * A private class which exposes constants which define the XML element names to use
	 * when this object is marshalled to XML.
	 */
	static class Elements {
	    final static String CODE = "code";
	    final static String NAME = "name";
		final static String NAMESPACE_CODE = "namespaceCode";
        final static String COMPONENT_SET_ID = "componentSetId";
		final static String ACTIVE = "active";
	}

    public static class Cache {
        public static final String NAME = CoreConstants.Namespaces.CORE_NAMESPACE_2_0 + "/" + Component.Constants.TYPE_NAME;
    }
   
}