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
package org.kuali.rice.krms.api.repository.action;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.krms.api.KrmsConstants;

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
 * Concrete model object implementation of KRMS Repository Action 
 * immutable. 
 * Instances of Action can be (un)marshalled to and from XML.
 *
 * @see org.kuali.rice.krms.framework.engine.Action
 * @see ActionDefinitionContract
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = ActionDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = ActionDefinition.Constants.TYPE_NAME, propOrder = {
		ActionDefinition.Elements.ID,
		ActionDefinition.Elements.NAME,
		ActionDefinition.Elements.NAMESPACE,
		ActionDefinition.Elements.DESC,
		ActionDefinition.Elements.TYPE_ID,
		ActionDefinition.Elements.RULE_ID,
		ActionDefinition.Elements.SEQUENCE_NUMBER,
		ActionDefinition.Elements.ATTRIBUTES,
        CoreConstants.CommonElements.VERSION_NUMBER,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class ActionDefinition extends AbstractDataTransferObject implements ActionDefinitionContract {
	private static final long serialVersionUID = 2783959459503209577L;

	@XmlElement(name = Elements.ID, required=true)
	private String id;
	@XmlElement(name = Elements.NAME, required=true)
	private String name;
	@XmlElement(name = Elements.NAMESPACE, required=true)
	private String namespace;
	@XmlElement(name = Elements.DESC, required=true)
	private String description;
	@XmlElement(name = Elements.TYPE_ID, required=true)
	private String typeId;
	@XmlElement(name = Elements.RULE_ID, required=true)
	private String ruleId;
	@XmlElement(name = Elements.SEQUENCE_NUMBER, required=true)
	private Integer sequenceNumber;
	
	@XmlElement(name = Elements.ATTRIBUTES, required = false)
	@XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
	private final Map<String, String> attributes;
	
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    	
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<org.w3c.dom.Element> _futureElements = null;
	
	
	 /** 
     * This constructor should never be called.  
     * It is only present for use during JAXB unmarshalling. 
     */
    private ActionDefinition() {
    	this.id = null;
    	this.name = null;
    	this.namespace = null;
    	this.description = null;
    	this.typeId = null;
    	this.ruleId = null;
    	this.sequenceNumber = null;
    	this.attributes = null;
        this.versionNumber = null;
    }
    
    /**
	 * Constructs a KRMS Repository Action object from the given builder.  
	 * This constructor is private and should only ever be invoked from the builder.
	 * 
	 * @param builder the Builder from which to construct the Action
	 */
    private ActionDefinition(Builder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.namespace = builder.getNamespace();
        this.description = builder.getDescription();
        this.typeId = builder.getTypeId();
        this.ruleId = builder.getRuleId();
        this.sequenceNumber = builder.getSequenceNumber();
        if (builder.attributes != null){
        	this.attributes = Collections.unmodifiableMap(builder.getAttributes());
        } else {
        	this.attributes = null;
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
	public String getNamespace() {
		return this.namespace;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public String getTypeId() {
		return this.typeId;
	}

	@Override
	public String getRuleId() {
		return this.ruleId;
	}

	@Override
	public Integer getSequenceNumber() {
		return this.sequenceNumber;
	}

    /**
     * Returns the internal representation of the set of attributes associated with the
     * Action.  The attributes are represented as name/value pairs.
     *
     * @return internal representation of the set of ActionAttribute objects.
     */
	@Override
	public Map<String, String> getAttributes() {
		return this.attributes; 
	}

    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }
        
	/**
     * This builder is used to construct instances of KRMS Repository Action.  It enforces the constraints of the {@link ActionDefinitionContract}.
     */
    public static class Builder implements ActionDefinitionContract, ModelBuilder, Serializable {
        private static final long serialVersionUID = -6773634512570180267L;

        private String id;
        private String name;
        private String namespace;
        private String description;
        private String typeId;
        private String ruleId;
        private Integer sequenceNumber;
        private Map<String, String> attributes;
        private Long versionNumber;

        /**
         * Private constructor for creating a builder with all of it's required attributes.
         *
         * @param actionId the actionId value to set, must no tbe null or blank
         * @param name the name value to set, must not be null or blank
         * @param namespace the namespace value to set, must not be null or blank
         * @param typeId the typeId value to set
         * @param ruleId the ruleId value to set, must not be null or blank
         * @param sequenceNumber the sequenceNumber value to set, must not be null or blank
         */
        private Builder(String actionId, String name, String namespace, String typeId, String ruleId, Integer sequenceNumber) {
            setId(actionId);
            setName(name);
            setNamespace(namespace);
            setTypeId(typeId);
            setRuleId(ruleId);
            setSequenceNumber(sequenceNumber);
            setAttributes(new HashMap<String, String>());
        }

        /**
         * Create a builder with the given parameters
         * 
         * @param actionId the actionId value to set, must no tbe null or blank
         * @param name the name value to set, must not be null or blank
         * @param namespace the namespace value to set, must not be null or blank
         * @param typeId the typeId value to set
         * @param ruleId the ruleId value to set, must not be null or blank
         * @param sequenceNumber the sequenceNumber value to set, must not be null or blank
         * @return an instance of the builder populated with given data
         */
        public static Builder create(String actionId, String name, String namespace, String typeId, String ruleId, Integer sequenceNumber){
        	return new Builder(actionId, name, namespace, typeId, ruleId, sequenceNumber);
        }
        /**
         * Creates a builder by populating it with data from the given {@link ActionDefinitionContract}.
         * 
         * @param contract the contract from which to populate this builder
         * @return an instance of the builder populated with data from the contract
         * @throws IllegalArgumentException if the contract is null
         */
        public static Builder create(ActionDefinitionContract contract) {
        	if (contract == null) {
                throw new IllegalArgumentException("contract is null");
            }
            Builder builder =  new Builder(contract.getId(), contract.getName(),
            		contract.getNamespace(), contract.getTypeId(), contract.getRuleId(),
            		contract.getSequenceNumber());
            builder.setDescription(contract.getDescription());
        	if (contract.getAttributes() != null){
                builder.setAttributes(new HashMap<String, String>(contract.getAttributes()));
        	}
            builder.setVersionNumber(contract.getVersionNumber());
            return builder;
        }

		/**
		 * Sets the value of the id on this builder to the given value.
		 * 
         * @param actionId the actionId value to set, must no tbe null or blank
		 * @throws IllegalArgumentException if the actionId is non-null and blank
		 */
        public void setId(String actionId) {
            if (actionId != null && StringUtils.isBlank(actionId)) {
                throw new IllegalArgumentException("action ID must be null or non-blank");
            }
			this.id = actionId;
		}


        /**
         * Sets the value of the name on this builder to the given value.
         *
         * @param name the name value to set, must not be null or blank
         * @throws IllegalArgumentException if the name is non-null and blank
         */
        public void setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name is blank");
            }
			this.name = name;
		}

        /**
         * Sets the value of the namespace on this builder to the given value.
         *
         * @param namespace the namespace value to set, must not be null or blank
         * @throws IllegalArgumentException if the namespace is non-null and blank
         */
        public void setNamespace(String namespace) {
            if (StringUtils.isBlank(namespace)) {
                throw new IllegalArgumentException("namespace is blank");
            }
			this.namespace = namespace;
		}

        /**
         * Sets the value of the description on this builder to the given value.
         *
         * @param desc the description value to set
         */
		public void setDescription(String desc) {
			this.description = desc;
		}

        /**
         * Sets the value of the typeId on this builder to the given value.
         *
         * @param typeId the typeId value to set, must not be null or blank.
         * @throws IllegalArgumentException if the typeId is null or blank
         */
		public void setTypeId(String typeId) {
			if (StringUtils.isBlank(typeId)) {
	                throw new IllegalArgumentException("KRMS type id is blank");
			}
			this.typeId = typeId;
		}

        /**
         * Sets the value of the ruleId on this builder to the given value.
         *
         * @param ruleId the ruleId value to set, must not be null or blank
         * @throws IllegalArgumentException if the ruleId is null or blank
         */
		public void setRuleId(String ruleId) {
			if (StringUtils.isBlank(ruleId)) {
	                throw new IllegalArgumentException("rule id is blank");
			}
			this.ruleId = ruleId;
		}

        /**
         * Sets the value of the sequenceNumber on this builder to the given value.
         *
         * @param sequenceNumber the sequenceNumber value to set, must not be null or blank
         * @throws IllegalArgumentException if the sequenceNumber is null or blank
         */
		public void setSequenceNumber(Integer sequenceNumber) {
			if (sequenceNumber == null) {
	                throw new IllegalArgumentException("sequence number is null");
			}
			this.sequenceNumber = sequenceNumber;
		}

        /**
         * Sets the value of the attributes on this builder to the given value.
         *
         * @param attributes the attributes value to set, can be null
         */
		public void setAttributes(Map<String, String> attributes){
			if (attributes == null){
				this.attributes = Collections.emptyMap();
			}
			this.attributes = Collections.unmodifiableMap(attributes);
		}

        /**
         * Sets the value of the versionNumber on this builder to the given value.
         *
         * @param versionNumber the versionNumber value to set
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
		public String getDescription() {
			return description;
		}

		@Override
		public String getTypeId() {
			return typeId;
		}

		@Override
		public String getRuleId() {
			return ruleId;
		}

		@Override
		public Integer getSequenceNumber() {
			return sequenceNumber;
		}

		@Override
		public Map<String, String> getAttributes() {
			return attributes;
		}

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

		/**
		 * Builds an instance of a Action based on the current state of the builder.
		 * 
		 * @return the fully-constructed Action
		 */
        @Override
        public ActionDefinition build() {
            return new ActionDefinition(this);
        }
		
    }
	
	/**
	 * Defines some internal constants used on this class.
	 */
	static class Constants {
		final static String ROOT_ELEMENT_NAME = "action";
		final static String TYPE_NAME = "ActionType";
	}
	
	/**
	 * A private class which exposes constants which define the XML element names to use
	 * when this object is marshalled to XML.
	 */
	public static class Elements {
		final static String ID = "id";
		final static String NAME = "name";
		final static String NAMESPACE = "namespace";
		final static String DESC = "description";
		final static String TYPE_ID = "typeId";
		final static String RULE_ID = "ruleId";
		final static String SEQUENCE_NUMBER = "sequenceNumber";
		final static String ATTRIBUTES = "attributes";
	}

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + ActionDefinition.Constants.TYPE_NAME;
    }

}
