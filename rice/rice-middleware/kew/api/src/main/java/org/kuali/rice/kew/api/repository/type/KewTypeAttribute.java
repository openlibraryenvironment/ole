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
 * Concrete model object implementation of KEW KewTypeAttribute. 
 * immutable. 
 * Instances of KewTypeAttribute can be (un)marshalled to and from XML.
 *
 */
@XmlRootElement(name = KewTypeAttribute.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = KewTypeAttribute.Constants.TYPE_NAME, propOrder = {
		KewTypeAttribute.Elements.ID,
		KewTypeAttribute.Elements.TYPE_ID,
		KewTypeAttribute.Elements.ATTR_DEFN_ID,
		KewTypeAttribute.Elements.SEQ_NO,
		KewTypeAttribute.Elements.ACTIVE,
		KewTypeAttribute.Elements.ATTR_DEFN,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class KewTypeAttribute extends AbstractDataTransferObject implements KewTypeAttributeContract {
	private static final long serialVersionUID = -304265575559412478L;
	
	@XmlElement(name = Elements.ID, required=true)
	private String id;
	@XmlElement(name = Elements.TYPE_ID, required=true)
	private String typeId;
	@XmlElement(name = Elements.ATTR_DEFN_ID, required=true)
	private String attributeDefinitionId;
	@XmlElement(name = Elements.SEQ_NO, required=true)
	private Integer sequenceNumber;
	@XmlElement(name = Elements.ACTIVE, required=false)
	private boolean active;
	@XmlElement(name = Elements.ATTR_DEFN, required=false)
	private KewAttributeDefinition attributeDefinition;
	
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<org.w3c.dom.Element> _futureElements = null;
	
	 /** 
     * This constructor should never be called.  It is only present for use during JAXB unmarshalling. 
     */
    private KewTypeAttribute() {
    	this.id = null;
    	this.typeId = null;
    	this.attributeDefinitionId = null;
    	this.sequenceNumber = null;
    	this.active = false;
    	this.attributeDefinition = null;
    }
    
    /**
	 * Constructs a KEW KewTypeAttribute from the given builder.  
	 * This constructor is private and should only ever be invoked from the builder.
	 * 
	 * @param builder the Builder from which to construct the KewTypeAttribute
	 */
    private KewTypeAttribute(Builder builder) {
        this.id = builder.getId();
        this.typeId = builder.getTypeId();
        this.attributeDefinitionId = builder.getAttributeDefinitionId();
        this.sequenceNumber = builder.getSequenceNumber();
        this.active = builder.isActive();
        if (builder.getAttributeDefinition() != null) {
        	this.attributeDefinition = builder.getAttributeDefinition().build();
        }
    }
    
	public String getId() {
		return this.id;
	}
	
	public String getTypeId() {
		return this.typeId;
	}

	public String getAttributeDefinitionId() {
		return this.attributeDefinitionId;
	}

	public Integer getSequenceNumber() {
		return this.sequenceNumber;
	}
	
	public boolean isActive() {
		return this.active; 
	}

	public KewAttributeDefinition getAttributeDefinition() {
		return this.attributeDefinition;
	}
	
	/**
     * This builder is used to construct instances of KewTypeAttribute.  
     */
    public static class Builder implements KewTypeAttributeContract, ModelBuilder, Serializable {
		private static final long serialVersionUID = 2729964674427296346L;

		private String id;
        private String typeId;
        private String attributeDefinitionId;
        private Integer sequenceNumber;
        private boolean active;
        private KewAttributeDefinition.Builder attributeDefinition;

		/**
		 * Private constructor for creating a builder with all of it's required attributes.
		 */
        private Builder(String id, String typeId, String attributeDefinitionId, Integer sequenceNumber) {
            setId(id);
            setTypeId(typeId);
            setAttributeDefinitionId(attributeDefinitionId);
            setSequenceNumber(sequenceNumber);
			setActive(true);
        }

        public Builder attributeDefinition(KewAttributeDefinition.Builder attributeDefinition){
        	setAttributeDefinition(attributeDefinition);
        	return this;
        }
        
        /**
         * Creates a builder from the given parameters.
         * 
         * @param id the KewTypeAtribute id
         * @param typeId the KewType Id 
         * @param attributeDefinitionId The attributeDefinitionId
         * @param sequenceNumber 
         * @return an instance of the builder with the fields already populated
         * @throws IllegalArgumentException if the either the id, name or namespace is null or blank
         */
        public static Builder create(String id, String typeId, String attributeDefinitionId, Integer sequenceNumber) {
            return new Builder(id, typeId, attributeDefinitionId, sequenceNumber);
        }
        
        public static Builder create(KewTypeAttributeContract contract){
        	if (contract == null) {
                throw new IllegalArgumentException("contract is null");
            }
        	Builder builder = new Builder(contract.getId(), 
        			contract.getTypeId(),
        			contract.getAttributeDefinitionId(),
        			contract.getSequenceNumber());
        	if (contract.getAttributeDefinition() != null){
        		KewAttributeDefinition.Builder attrBuilder = 
        			KewAttributeDefinition.Builder.create(contract.getAttributeDefinition());
        		builder.setAttributeDefinition(attrBuilder);
        	}
        	return builder;
        }

		/**
		 * Sets the value of the id on this builder to the given value.
		 * 
		 * @param id the id value to set, must  be null or non-blank
		 * @throws IllegalArgumentException if the id is not null and blank
		 */
        public void setId(String id) {
            if (null != id && StringUtils.isBlank(id)) {
                throw new IllegalArgumentException("id must be null or non-blank");
            }
            this.id = id;
        }

		public void setTypeId(String typeId) {
            if (null != typeId && StringUtils.isBlank(typeId)) {
                throw new IllegalArgumentException("typeId must be null or non-blank");
            }
			this.typeId = typeId;
		}

		public void setAttributeDefinitionId(String attributeDefinitionId) {
            if (StringUtils.isBlank(attributeDefinitionId)) {
                throw new IllegalArgumentException("the attribute definition id is blank");
            }
			this.attributeDefinitionId = attributeDefinitionId;
		}
		
		public void setSequenceNumber(Integer sequenceNumber) {
			if (sequenceNumber == null){
				 throw new IllegalArgumentException("the sequence number is null");
			}
			this.sequenceNumber = sequenceNumber;
		}
		
		public void setAttributeDefinition(KewAttributeDefinition.Builder attributeDefinition) {
			this.attributeDefinition = attributeDefinition;
			//TODO: verify that the attributeDefinitionID field matches the id field in the builder
		}
		
		public void setActive(boolean active) {
			this.active = active;
		}

		public String getId() {
			return id;
		}

		public String getTypeId() {
			return typeId;
		}

		public String getAttributeDefinitionId() {
			return attributeDefinitionId;
		}
		
		public Integer getSequenceNumber() {
			return sequenceNumber;
		}

		public KewAttributeDefinition.Builder getAttributeDefinition() {
			return attributeDefinition;
		}


		public boolean isActive() {
			return active;
		}

		/**
		 * Builds an instance of a KewTypeAttribute based on the current state of the builder.
		 * 
		 * @return the fully-constructed KewTypeAttribute
		 */
        @Override
        public KewTypeAttribute build() {
            return new KewTypeAttribute(this);
        }
		
    }

	/**
	 * Defines some internal constants used on this class.
	 */
	static class Constants {
		final static String ROOT_ELEMENT_NAME = "KewTypeAttribute";
		final static String TYPE_NAME = "KewTypeAttributeType";
	}
	
	/**
	 * A private class which exposes constants which define the XML element names to use
	 * when this object is marshalled to XML.
	 */
	public static class Elements {
		final static String ID = "id";
		final static String TYPE_ID = "typeId";
		final static String ATTR_DEFN_ID = "attributeDefinitionId";
		final static String SEQ_NO = "sequenceNumber";
		final static String ACTIVE = "active";
		final static String ATTR_DEFN = "attributeDefinition";
	}
}
