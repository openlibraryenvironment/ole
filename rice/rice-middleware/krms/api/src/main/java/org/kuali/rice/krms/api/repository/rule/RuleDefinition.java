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
package org.kuali.rice.krms.api.repository.rule;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.krms.api.KrmsConstants;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.api.repository.action.ActionDefinitionContract;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete model object implementation of KRMS Repository Rule 
 * immutable. 
 * Instances of Rule can be (un)marshalled to and from XML.
 *
 * @see RuleDefinitionContract
 * @see org.kuali.rice.krms.framework.engine.Rule
 */
@XmlRootElement(name = RuleDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RuleDefinition.Constants.TYPE_NAME, propOrder = {
		RuleDefinition.Elements.ID,
		RuleDefinition.Elements.NAME,
        RuleDefinition.Elements.NAMESPACE,
        RuleDefinition.Elements.DESCRIPTION,
		RuleDefinition.Elements.TYPE_ID,
        RuleDefinition.Elements.ACTIVE,
		RuleDefinition.Elements.PROPOSITION,
		RuleDefinition.Elements.ACTIONS,
		RuleDefinition.Elements.ATTRIBUTES,
        CoreConstants.CommonElements.VERSION_NUMBER,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class RuleDefinition extends AbstractDataTransferObject implements RuleDefinitionContract {
	private static final long serialVersionUID = 2783959459503209577L;

	@XmlElement(name = Elements.ID, required=true)
	private final String id;
    @XmlElement(name = Elements.NAME, required=true)
	private final String name;
	@XmlElement(name = Elements.NAMESPACE, required=true)
	private final String namespace;
    @XmlElement(name = Elements.DESCRIPTION, required=false)
    private final String description;
	@XmlElement(name = Elements.TYPE_ID, required=true)
	private final String typeId;
	@XmlElement(name = Elements.PROPOSITION, required=true)
	private final PropositionDefinition proposition;
    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;

	@XmlElementWrapper(name = Elements.ACTIONS)
	@XmlElement(name = Elements.ACTION, required=false)
	private final List<ActionDefinition> actions;
	
	@XmlElement(name = Elements.ATTRIBUTES, required = false)
	@XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
	private final Map<String, String> attributes;
	
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    	
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<org.w3c.dom.Element> _futureElements = null;
	
	@XmlTransient
	private String propId;

	/** 
     * This constructor should never be called.  
     * It is only present for use during JAXB unmarshalling. 
     */
    private RuleDefinition() {
    	this.id = null;
    	this.name = null;
    	this.namespace = null;
        this.description = null;
    	this.typeId = null;
    	this.propId = null;
        this.active = true;
    	this.proposition = null;
    	this.actions = null;
    	this.attributes = null;
        this.versionNumber = null;
    }
    
    /**
	 * Constructs a KRMS Repository Rule object from the given builder.  
	 * This constructor is private and should only ever be invoked from the builder.
	 * 
	 * @param builder the Builder from which to construct the Rule
	 */
    private RuleDefinition(Builder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.namespace = builder.getNamespace();
        this.typeId = builder.getTypeId();
        this.propId = builder.getPropId();
        this.description = builder.getDescription();
        this.active = builder.isActive();

        if (builder.getProposition() != null) {
            this.proposition = builder.getProposition().build();
        } else {
            this.proposition = null;
        }
        
        List<ActionDefinition> actionList = new ArrayList<ActionDefinition> ();
        if (builder.getActions() != null){
        	for (ActionDefinition.Builder b : builder.actions){
        		actionList.add(b.build());
        	}
            this.actions = Collections.unmodifiableList(actionList);
        } else {
            this.actions = Collections.emptyList();
        }
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
    public String getDescription() {
        return this.description;
    }

    @Override
	public String getNamespace() {
		return this.namespace;
	}

	@Override
	public String getTypeId() {
		return this.typeId;
	}

	@Override
	public String getPropId(){
		return this.propId;
	}

    @Override
    public boolean isActive() {
        return this.active;
    }

	@Override
	public PropositionDefinition getProposition(){
		return this.proposition;
	}
	
	@Override
	public List<ActionDefinition> getActions(){
		return this.actions;
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
     * This builder is used to construct instances of KRMS Repository Rule.  It enforces the constraints of the {@link RuleDefinitionContract}.
     */
    public static class Builder implements RuleDefinitionContract, ModelBuilder, Serializable {		
        private static final long serialVersionUID = -7850514191699945347L;
        
		private String id;
        private String name;
        private String description;
        private String namespace;
        private String typeId;
        private String propId;
        private boolean active;
        private PropositionDefinition.Builder proposition;
        private List<ActionDefinition.Builder> actions;
        private Map<String, String> attributes;
        private Long versionNumber;

        /**
         * Private constructor for creating a builder with all of it's required attributes.
         * 
         * @param ruleId the id value to set, must not be null or blank
         * @param name the name value to set, must not be null or blank
         * @param namespace the namespace value to set, must not be null or blank
         * @param typeId the typeId value to set
         * @param propId the propId value to set, must not be null or blank
         */
        private Builder(String ruleId, String name, String namespace, String typeId, String propId) {
            setId(ruleId);
            setName(name);
            setNamespace(namespace);
            setTypeId(typeId);
            setPropId(propId);
            setActive(true);
            setAttributes(new HashMap<String, String>());
        }

        /**
         * Create a builder with the given parameters.
         *
         * @param ruleId the id value to set, must not be null or blank
         * @param name the name value to set, must not be null or blank
         * @param namespace the namespace value to set, must not be null or blank
         * @param typeId the typeId value to set
         * @param propId the propId value to set, must not be null or blank
         * @return Builder with the given values set
         */
        public static Builder create(String ruleId, String name, String namespace, String typeId, String propId){
        	return new Builder(ruleId, name, namespace, typeId, propId);
        }
        
        /**
         * Creates a builder by populating it with data from the given {@link RuleDefinitionContract}.
         * 
         * @param contract the contract from which to populate this builder
         * @return an instance of the builder populated with data from the contract
         */
        public static Builder create(RuleDefinitionContract contract) {
        	if (contract == null) {
                throw new IllegalArgumentException("contract is null");
            }

        	List <ActionDefinition.Builder> actionList = new ArrayList<ActionDefinition.Builder>();
        	if (contract.getActions() != null){
        		for (ActionDefinitionContract actionContract : contract.getActions()){
        			ActionDefinition.Builder actBuilder = ActionDefinition.Builder.create(actionContract);
        			actionList.add(actBuilder);
        		}
        	}
        	
            Builder builder =  new Builder(contract.getId(), contract.getName(),
            		contract.getNamespace(), contract.getTypeId(), contract.getPropId());
            if (contract.getProposition() != null) {
                builder.setProposition(PropositionDefinition.Builder.create(contract.getProposition()));
            }
        	if (contract.getAttributes() != null){
                builder.setAttributes(new HashMap<String, String>(contract.getAttributes()));
        	}
            builder.setActions(actionList);
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setDescription(contract.getDescription());
            builder.setActive(contract.isActive());
            return builder;
        }

		/**
		 * Sets the value of the id on this builder to the given value.
		 * 
		 * @param ruleId the id value to set, must not be null or blank
		 * @throws IllegalArgumentException if the id is null or blank
		 */

        public void setId(String ruleId) {
            if (ruleId != null && StringUtils.isBlank(ruleId)) {
                throw new IllegalArgumentException("rule ID must be null or else non-blank");
            }
			this.id = ruleId;
		}

        /**
         * Sets the value of the name on this builder to the given value
         * @param name the name value to set, must not be null or blank
         * @throws IllegalArgumentException if the name is null or blank
         */
        public void setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name is blank");
            }
            this.name = name;
        }

        /**
         * Sets the value of the description on this builder to the given value
         * @param description
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * Sets the value of the namespace on this builder to the given value
         * @param namespace the namespace value to set, must not be null or blank
         * @throws IllegalArgumentException if the namespace is null or blank
         */
        public void setNamespace(String namespace) {
            if (StringUtils.isBlank(namespace)) {
                throw new IllegalArgumentException("namespace is blank");
            }
			this.namespace = namespace;
		}

        /**
         * Sets the value of the typeId on this builder to the given value
         * @param typeId the typeId value to set
         */
		public void setTypeId(String typeId) {
			this.typeId = typeId;
		}

        /**
         * Sets the value of the active on this builder to the given value
         * @param active the active value to set
         */
        public void setActive(boolean active) {
            this.active = active;
        }

        /**
         * Sets the value of the propId on this builder to the given value
         * @param propId the propId value to set, must not be null or blank
         * @throws IllegalArgumentException if the propId is null or blank
         */
		public void setPropId(String propId) {
		    if (propId != null && StringUtils.isBlank(propId)) {
		        throw new IllegalArgumentException("propId must be null or non-blank");
		    }
			this.propId = propId;
		}

        /**
         * Sets the value of the proposition on this builder to the given value
         * @param prop the proposition value to set, must not be null
         */
		public void setProposition(PropositionDefinition.Builder prop) {
			this.proposition = prop;
			this.setPropId(prop.getId());
		}

        /**
         * Sets the value of the actions on this builder to the given value
         * @param actions the actions value to set, can be null
         */
		public void setActions(List<ActionDefinition.Builder> actions) {
			if (actions == null){
				this.actions = Collections.unmodifiableList(new ArrayList<ActionDefinition.Builder>());
				return;
			}
			this.actions = Collections.unmodifiableList(actions);
		}

        /**
         * Sets the value of the attributes on this builder to the given value
         * @param attributes the attributes values to set, can be null
         */
		public void setAttributes(Map<String, String> attributes){
			if (attributes == null){
				this.attributes = Collections.emptyMap();
			}
			this.attributes = Collections.unmodifiableMap(attributes);
		}

        /**
         * Sets the value of the versionNumber on this builder to the given value
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
		public String getDescription() {
		    return description;
		}

		@Override
		public String getNamespace() {
			return namespace;
		}

		@Override
		public String getTypeId() {
			return typeId;
		}

		@Override
		public String getPropId() {
			return propId;
		}

        @Override
        public boolean isActive() {
            return active;
        }

		@Override
		public PropositionDefinition.Builder getProposition() {
			return proposition;
		}

		@Override
		public List<ActionDefinition.Builder> getActions(){
			return actions;
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
		 * Builds an instance of a Rule based on the current state of the builder.
		 * 
		 * @return the fully-constructed Rule
		 */
        @Override
        public RuleDefinition build() {
            return new RuleDefinition(this);
        }
		
    }
	
	/**
	 * Defines some internal constants used on this class.
	 */
	public static class Constants {
		final static String ROOT_ELEMENT_NAME = "rule";
		final static String TYPE_NAME = "RuleType";
	}
	
	/**
	 * A private class which exposes constants which define the XML element names to use
	 * when this object is marshalled to XML.
	 */
	public static class Elements {
		final static String ID = "id";
        final static String NAME = "name";
        final static String DESCRIPTION = "description";
		final static String NAMESPACE = "namespace";
		final static String TYPE_ID = "typeId";
		final static String PROPOSITION = "proposition";
		final static String ACTIONS = "actions";
		final static String ACTION = "action";
        final static String ACTIVE = "active";
		final static String ATTRIBUTES = "attributes";
	}

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + RuleDefinition.Constants.TYPE_NAME;
    }
}
