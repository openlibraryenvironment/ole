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
package org.kuali.rice.coreservice.api.style;

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

/**
 * An immutable representation of a Style.  A style is essentially a block of
 * XML containing and XSL stylesheet. These can be used in various places for
 * the transformation of XML data from one form to another.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = Style.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = Style.Constants.TYPE_NAME, propOrder = {
		Style.Elements.ID,
		Style.Elements.NAME,
		Style.Elements.XML_CONTENT,
		Style.Elements.ACTIVE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class Style extends AbstractDataTransferObject implements StyleContract {

	private static final long serialVersionUID = -26426318682076660L;
	
	@XmlElement(name = Elements.ID, required = false)
	private final String id;
	
	@XmlElement(name = Elements.NAME, required = true)
    private final String name;
	
	@XmlElement(name = Elements.XML_CONTENT, required = false)
    private final String xmlContent;
	
	@XmlElement(name = Elements.ACTIVE, required = true)
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
     */
    private Style() {
    	this.id = null;
    	this.name = null;
    	this.xmlContent = null;
    	this.active = false;
    	this.versionNumber = null;
    	this.objectId = null;
    }
    
    private Style(Builder builder) {
    	this.id = builder.getId();
    	this.name = builder.getName();
    	this.xmlContent = builder.getXmlContent();
    	this.active = builder.isActive();
    	this.versionNumber = builder.getVersionNumber();
    	this.objectId = builder.getObjectId();
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
    public String getXmlContent() {
		return this.xmlContent;
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
	 * A builder which can be used to construct {@link Style} instances.
	 * Enforces the constraints of the {@link StyleContract}.
	 * 
	 * @author Kuali Rice Team (rice.collab@kuali.org)
	 *
	 */
	public static final class Builder implements StyleContract, ModelBuilder, Serializable  {
    	
    	private static final long serialVersionUID = -219369603932108436L;
    	
		private String id;
        private String name;
        private String xmlContent;
        private boolean active;
        private Long versionNumber;
        private String objectId;
        
        private Builder(String name) {
        	setName(name);
        	setActive(true);
        }
        
        /**
         * Creates a style builder with the given required values.  The builder
         * is the only means by which a {@link Style} object should be created.
         * 
         * <p>Will default the active flag to true.
         * 
         * @param name the name of the style to create, must not be null or blank
         * 
         * @return a builder with the required values already initialized
         * 
         * @throws IllegalArgumentException if the given name is null or blank
         */
        public static Builder create(String name) {
        	return new Builder(name);
        }
        
        /**
         * Creates a populates a builder with the data on the given StyleContract.
         * This is similar in nature to a "copy constructor" for Style.
         * 
         * @param contract an object implementing the StyleContract from which
         * to copy property values
         *  
         * @return a builder with the values from the contract already initialized
         * 
         * @throws IllegalArgumentException if the given contract is null
         */
        public static Builder create(StyleContract contract) {
        	if (contract == null) {
        		throw new IllegalArgumentException("contract was null");
        	}
        	Builder builder = create(contract.getName());
        	builder.setId(contract.getId());
        	builder.setXmlContent(contract.getXmlContent());
        	builder.setActive(contract.isActive());
        	builder.setVersionNumber(contract.getVersionNumber());
        	builder.setObjectId(contract.getObjectId());
        	return builder;
        }
        
        @Override
        public Style build() {
        	return new Style(this);
        }

        @Override
		public String getId() {
			return this.id;
		}

        /**
         * Sets the id for the style that will be returned by this builder.
         * 
         * @param id the id to set
         */
		public void setId(String id) {
			this.id = id;
		}

		@Override
		public String getName() {
			return this.name;
		}

		/**
         * Sets the name for the style that will be returned by this builder.
         * The name must not be blank or null.
         * 
         * @param name the name to set on this builder, must not be null or
         * blank
         * 
         * @throws IllegalArgumentException if the given name is null or blank
         */
		public void setName(String name) {
			if (StringUtils.isBlank(name)) {
				throw new IllegalArgumentException("name is blank");
			}
			this.name = name;
		}

		@Override
		public String getXmlContent() {
			return this.xmlContent;
		}

		/**
		 * Sets the XML content for the style that will be returned by this
		 * builder.
		 * 
		 * @param xmlContent the xmlContent to set on this builder
		 */
		public void setXmlContent(String xmlContent) {
			this.xmlContent = xmlContent;
		}

		@Override
		public boolean isActive() {
			return this.active;
		}

		/**
         * Sets the active flag for the style that will be returned by this
         * builder.
         * 
         * @param active the active flag to set
         */
		public void setActive(boolean active) {
			this.active = active;
		}

		@Override
		public Long getVersionNumber() {
			return this.versionNumber;
		}

		/**
         * Sets the version number for the style that will be returned by this
         * builder.
         * 
         * <p>In general, this value should not be manually set on the builder,
         * but rather copied from an existing {@link StyleContract} when
         * invoking {@link Builder#create(StyleContract)}.
         * 
         * @param versionNumber the version number to set
         */
		public void setVersionNumber(Long versionNumber) {
			this.versionNumber = versionNumber;
		}

		@Override
		public String getObjectId() {
			return objectId;
		}
		
		/**
         * Sets the globally unique object ID for the style that will be
         * returned by this builder.
         * 
         * <p>In general, this value should not be manually set on the builder,
         * but rather copied from an existing {@link StyleContract} when
         * invoking {@link Builder#create(StyleContract)}.
         * 
         * @param objectId the object ID to set
         */
		public void setObjectId(String objectId) {
			this.objectId = objectId;
		}
    	
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "style";
        final static String TYPE_NAME = "StyleType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        final static String ID = "id";
        final static String NAME = "name";
        final static String XML_CONTENT = "xmlContent";
        final static String ACTIVE = "active";
    }

    public static class Cache {
        public static final String NAME = CoreConstants.Namespaces.CORE_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}
