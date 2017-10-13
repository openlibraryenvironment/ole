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
 * Concrete model object implementation of KRMS {@Link KrmsTypeAttributeContract}
 * <p>immutable. To construct an instance of a KrmsTypeAttribute, use the {@link KrmsTypeAttribute.Builder} class.
 * Instances of KrmsTypeAttribute can be (un)marshalled to and from XML.</p>
 *
 */
@XmlRootElement(name = KrmsTypeAttribute.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = KrmsTypeAttribute.Constants.TYPE_NAME, propOrder = {
		KrmsTypeAttribute.Elements.ID,
		KrmsTypeAttribute.Elements.TYPE_ID,
		KrmsTypeAttribute.Elements.ATTR_DEFN_ID,
		KrmsTypeAttribute.Elements.SEQ_NO,
		KrmsTypeAttribute.Elements.ACTIVE,
        CoreConstants.CommonElements.VERSION_NUMBER,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class KrmsTypeAttribute extends AbstractDataTransferObject implements KrmsTypeAttributeContract {
	private static final long serialVersionUID = -304265575559412478L;
	
	@XmlElement(name = Elements.ID, required = false)
	private String id;
	@XmlElement(name = Elements.TYPE_ID, required = true)
	private String typeId;
	@XmlElement(name = Elements.ATTR_DEFN_ID, required = true)
	private String attributeDefinitionId;
	@XmlElement(name = Elements.SEQ_NO, required = true)
	private Integer sequenceNumber;
	@XmlElement(name = Elements.ACTIVE, required = false)
	private boolean active;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<org.w3c.dom.Element> _futureElements = null;
	
	 /** 
     * This constructor should never be called.  It is only present for use during JAXB unmarshalling. 
     */
    private KrmsTypeAttribute() {
    	this.id = null;
    	this.typeId = null;
    	this.attributeDefinitionId = null;
    	this.sequenceNumber = null;
    	this.active = true;
        this.versionNumber = null;
    }
    
    /**
	 * Constructs a KRMS KrmsTypeAttribute from the given builder.  
	 * <p>This constructor is private and should only ever be invoked from the builder.</p>
	 * 
	 * @param builder the Builder from which to construct the KrmsTypeAttribute
	 */
    private KrmsTypeAttribute(Builder builder) {
        this.id = builder.getId();
        this.typeId = builder.getTypeId();
        this.attributeDefinitionId = builder.getAttributeDefinitionId();
        this.sequenceNumber = builder.getSequenceNumber();
        this.active = builder.isActive();
        this.versionNumber = builder.getVersionNumber();
    }

    @Override
	public String getId() {
		return this.id;
	}
	
    @Override
    public String getTypeId() {
		return this.typeId;
	}

    @Override
	public String getAttributeDefinitionId() {
		return this.attributeDefinitionId;
	}

    @Override
	public Integer getSequenceNumber() {
		return this.sequenceNumber;
	}
	
    @Override
	public boolean isActive() {
		return this.active; 
	}

    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

	/**
     * This builder is used to construct instances of KrmsTypeAttribute.  
     */
    public static class Builder implements KrmsTypeAttributeContract, ModelBuilder, Serializable {
		private static final long serialVersionUID = 2729964674427296346L;

		private String id;
        private String typeId;
        private String attributeDefinitionId;
        private Integer sequenceNumber;
        private boolean active;
        private Long versionNumber;

		/**
		 * Private constructor for creating a builder with all of it's required attributes.
		 */
        private Builder(String typeId, String attributeDefinitionId, Integer sequenceNumber) {
            setTypeId(typeId);
            setAttributeDefinitionId(attributeDefinitionId);
            setSequenceNumber(sequenceNumber);
			setActive(true);
        }

        /**
         * Creates a builder from the given parameters.
         * 
         * @param typeId the KrmsType Id
         * @param attributeDefinitionId The attributeDefinitionId
         * @param sequenceNumber 
         * @return an instance of the builder with the fields already populated
         * @throws IllegalArgumentException if the either the id, name or namespace is null or blank
         */
        public static Builder create(String typeId, String attributeDefinitionId, Integer sequenceNumber) {
            return new Builder(typeId, attributeDefinitionId, sequenceNumber);
        }

        /**
         * Creates a builder by populating it with data from the given {@link KrmsTypeAttributeContract}.
         *
         * @param contract the contract from which to populate this builder
         * @return an instance of the builder populated with data from the contract
         * @throws IllegalArgumentException if the contract is null
         */
        public static Builder create(KrmsTypeAttributeContract contract){
        	if (contract == null) {
                throw new IllegalArgumentException("contract is null");
            }
        	Builder builder = new Builder(contract.getTypeId(),
        			contract.getAttributeDefinitionId(),
        			contract.getSequenceNumber());
            builder.setId(contract.getId());
            builder.setVersionNumber(contract.getVersionNumber());
        	return builder;
        }

		/**
		 * Sets the value of the id on this builder to the given value.
		 * 
		 * @param id the id value to set; can be null; a null id is an indicator
         * the this has not yet been persisted to the database.
		 */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * Sets the id of the KrmsTypeDefinition to which this attribute belongs.
         * @param typeId the id of the KrmsTypeDefinition; may not be null or blank
         * @throws IllegalArgumentException if the typeId is null or blank
         *
         */
		public void setTypeId(String typeId) {
            if (null != typeId && StringUtils.isBlank(typeId)) {
                throw new IllegalArgumentException("typeId must be null or non-blank");
            }
			this.typeId = typeId;
		}

        /**
         * Sets the id of the KrmsAttributeDefinition that describes this attribute.
         * @param attributeDefinitionId id of the KrmsAttributeDefinition; may not be null or blank
         * @throws IllegalArgumentException if the attributeDefinitionId is null or blank
         *
         */
		public void setAttributeDefinitionId(String attributeDefinitionId) {
            if (StringUtils.isBlank(attributeDefinitionId)) {
                throw new IllegalArgumentException("the attribute definition id is blank");
            }
			this.attributeDefinitionId = attributeDefinitionId;
		}

        /**
         * Sets the sequence number. This represents the order of the attributes
         * within the KrmsTypeDefinition.
         * @param sequenceNumber the order of the attribute in the attribute list; cannot be null
         * @throws IllegalArgumentException if the sequenceNumber is null
         */
		public void setSequenceNumber(Integer sequenceNumber) {
			if (sequenceNumber == null){
				 throw new IllegalArgumentException("the sequence number is null");
			}
			this.sequenceNumber = sequenceNumber;
		}

        /**
         * sets the active indicator value
         * @param active boolean value to set
         */
		public void setActive(boolean active) {
			this.active = active;
		}

        /**
         * Sets the version number for this object.
         * <p>In general, this value should only
         * be null if the object has not yet been stored to a persistent data store.
         * This version number is generally used for the purposes of optimistic locking.</p>
         * @param versionNumber the version number, or null if one has not been assigned yet
         */
        public void setVersionNumber(Long versionNumber){
            this.versionNumber = versionNumber;
        }

        @Override
		public String getId() {
			return id;
		}

        @Override
		public String getTypeId() {
			return typeId;
		}

        @Override
		public String getAttributeDefinitionId() {
			return attributeDefinitionId;
		}
		
        @Override
		public Integer getSequenceNumber() {
			return sequenceNumber;
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
		 * Builds an instance of a KrmsTypeAttribute based on the current state of the builder.
		 * 
		 * @return the fully-constructed KrmsTypeAttribute
		 */
        @Override
        public KrmsTypeAttribute build() {
            return new KrmsTypeAttribute(this);
        }
		
    }

	/**
	 * Defines some internal constants used on this class.
	 */
	static class Constants {
		final static String ROOT_ELEMENT_NAME = "krmsTypeAttribute";
		final static String TYPE_NAME = "KrmsTypeAttributeType";
	}
	
	/**
	 * A private class which exposes constants which define the XML element names to use
	 * when this object is marshalled to XML.
	 */
	public static class Elements {
		final static String ID = "id";
		final static String TYPE_ID = "typeId";
		final static String ATTR_DEFN_ID = "attributeDefinitionId";
		public final static String SEQ_NO = "sequenceNumber";
		final static String ACTIVE = "active";
	}
}
