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
package org.kuali.rice.krms.api.repository;

import java.io.Serializable;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;

/**
 * abstract base model object for KRMS Attribute immutables. 
 *
 * @see BaseAttributeContract
 */
@XmlTransient
public abstract class BaseAttribute extends AbstractDataTransferObject implements BaseAttributeContract {
	private static final long serialVersionUID = -6126133049308968098L;
	
	@XmlElement(name = Elements.ID, required=true)
	private final String id;

	@XmlElement(name = Elements.ATTR_DEFN_ID, required=false)
	private final String attributeDefinitionId;

	@XmlElement(name = Elements.VALUE, required=false)
	private final String value;
	
	@XmlElement(name = Elements.ATTR_DEFN, required=false)
	private final KrmsAttributeDefinition attributeDefinition;
	
    @SuppressWarnings("unused")
	@XmlAnyElement
	private final Collection<org.w3c.dom.Element> _futureElements = null;
	
	 /** 
     * This constructor should only be called by the private default constructor of subclasses,
     * which should only be used by JAXB and never invoked directly.
     */
    protected BaseAttribute() {
    	this.id = null;
    	this.attributeDefinitionId = null;
    	this.value = null;
    	this.attributeDefinition = null;
    }
    
    /**
	 * Constructs a BaseAttribute from the given builder.  
	 * This constructor is protected and should only ever be invoked from the builder.
	 * 
	 * @param builder the Builder from which to construct the BaseAttribute
	 */
    protected BaseAttribute(Builder builder) {
        this.id = builder.getId();
        this.attributeDefinitionId = builder.getAttributeDefinitionId();
        this.value = builder.getValue();
        if (builder.getAttributeDefinition() != null) {
        	this.attributeDefinition = builder.getAttributeDefinition().build();
        } else {
        	this.attributeDefinition = null;
        }
    }
    
	@Override
	public String getId() {
		return this.id;
	}
	
	@Override
	public String getAttributeDefinitionId() {
		return this.attributeDefinitionId;
	}

	@Override
	public String getValue() {
		return this.value;
	}
	
	@Override
	public KrmsAttributeDefinition getAttributeDefinition() {
		return this.attributeDefinition;
	}
	
	/**
     * This builder is used to construct the fields that {@link BaseAttribute} is responsible for.  It is abstract,
     * and intended to be subclassed by extenders of {@link BaseAttribute}.
     */
    public abstract static class Builder implements BaseAttributeContract, ModelBuilder, Serializable {		
		private static final long serialVersionUID = 5799994031051731535L;

		private String id;
        private String attributeDefinitionId;
        private String value;
        private KrmsAttributeDefinition.Builder attributeDefinition;
        
		/**
		 * Private constructor for creating a builder with all of it's required attributes.
		 */
        protected Builder(String id, String attributeDefinitionId, String value) {
            setId(id);
            setAttributeDefinitionId(attributeDefinitionId);
            setValue(value);
        }

        protected Builder(BaseAttributeContract attr) {
        	this (attr.getId(), attr.getAttributeDefinitionId(), attr.getValue());
        }

		/**
		 * Sets the value of the id on this builder to the given value.
		 * 
		 * @param id the id value to set, may be null if attribute has not yet
         * been stored in the repository
		 */
        public void setId(String id) {
//            if (StringUtils.isBlank(id)) {
//                throw new IllegalArgumentException("id is blank");
//            }
            this.id = id;
        }

        /**
         * Sets the attibuteDefinitionId value.
         * @param attributeDefinitionId; must not be null or blank
         * @throws IllegalArgumentException if the id is null or blank
         */
		public void setAttributeDefinitionId(String attributeDefinitionId) {
            if (StringUtils.isBlank(attributeDefinitionId)) {
                throw new IllegalArgumentException("the attribute definition id is blank");
            }
			this.attributeDefinitionId = attributeDefinitionId;
		}

        /**
         * Sets the value of the attribute
         * @param value a String representing the value of the attribute
         */
		public void setValue(String value) {
			this.value = value;
		}

        /**
         * Sets the attributeDefinition object related to the attribute.
         * @param attributeDefinition the attribute definition
         */
		public void setAttributeDefinition(KrmsAttributeDefinition.Builder attributeDefinition) {
			this.attributeDefinition = attributeDefinition;
		}
		
		@Override
		public String getId() {
			return id;
		}

		@Override
		public String getAttributeDefinitionId() {
			return attributeDefinitionId;
		}
		
		@Override
		public String getValue() {
			return value;
		}

		@Override
		public KrmsAttributeDefinition.Builder getAttributeDefinition() {
			return attributeDefinition;
		}

    }
	
	/**
	 * A protected class which exposes constants which define the XML element names to use
	 * when this object is marshalled to XML.
	 */
	public static class Elements {
		public final static String ID = "id";
		public final static String ATTR_DEFN_ID = "attributeDefinitionId";
		public final static String VALUE = "value";
		public final static String ATTR_DEFN = "attributeDefinition";
	}
}
