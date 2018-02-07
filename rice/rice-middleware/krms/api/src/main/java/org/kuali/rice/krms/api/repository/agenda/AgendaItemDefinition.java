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
package org.kuali.rice.krms.api.repository.agenda;

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
import org.kuali.rice.krms.api.KrmsConstants;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;

/**
 * Concrete model object implementation of KRMS Repository AgendaItemDefinition
 * immutable. 
 * Instances of AgendaItemDefinition can be (un)marshalled to and from XML.
 *
 * @see AgendaItemDefinitionContract
 */
@XmlRootElement(name = AgendaItemDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = AgendaItemDefinition.Constants.TYPE_NAME, propOrder = {
		AgendaItemDefinition.Elements.ID,
		AgendaItemDefinition.Elements.AGENDA_ID,
		AgendaItemDefinition.Elements.RULE_ID,
		AgendaItemDefinition.Elements.SUB_AGENDA_ID,
		AgendaItemDefinition.Elements.WHEN_TRUE_ID,
		AgendaItemDefinition.Elements.WHEN_FALSE_ID,
		AgendaItemDefinition.Elements.ALWAYS_ID,
		AgendaItemDefinition.Elements.RULE,
		AgendaItemDefinition.Elements.SUB_AGENDA,
		AgendaItemDefinition.Elements.WHEN_TRUE,
		AgendaItemDefinition.Elements.WHEN_FALSE,
		AgendaItemDefinition.Elements.ALWAYS,
        CoreConstants.CommonElements.VERSION_NUMBER,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class AgendaItemDefinition extends AbstractDataTransferObject implements AgendaItemDefinitionContract {
	private static final long serialVersionUID = 2783959459503209577L;

	@XmlElement(name = Elements.ID, required=true)
	private String id;
	@XmlElement(name = Elements.AGENDA_ID, required=true)
	private String agendaId;
	@XmlElement(name = Elements.RULE_ID, required=false)
	private String ruleId;
	@XmlElement(name = Elements.SUB_AGENDA_ID, required=false)
	private String subAgendaId;
	@XmlElement(name = Elements.WHEN_TRUE_ID, required=false)
	private String whenTrueId;
	@XmlElement(name = Elements.WHEN_FALSE_ID, required=false)
	private String whenFalseId;
	@XmlElement(name = Elements.ALWAYS_ID, required=false)
	private String alwaysId;
	
	@XmlElement(name = Elements.RULE, required=false)
	private RuleDefinition rule;;
	@XmlElement(name = Elements.SUB_AGENDA, required=false)
	private AgendaDefinition subAgenda;
	@XmlElement(name = Elements.WHEN_TRUE, required=false)
	private AgendaItemDefinition whenTrue;
	@XmlElement(name = Elements.WHEN_FALSE, required=false)
	private AgendaItemDefinition whenFalse;
	@XmlElement(name = Elements.ALWAYS, required=false)
	private AgendaItemDefinition always;
	
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    	
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<org.w3c.dom.Element> _futureElements = null;
	
	
	 /** 
     * This constructor should never be called.  
     * It is only present for use during JAXB unmarshalling. 
     */
    private AgendaItemDefinition() {
    	this.id = null;
    	this.agendaId = null;
    	this.ruleId = null;
    	this.subAgendaId = null;
    	this.whenTrueId = null;
    	this.whenFalseId = null;
    	this.alwaysId = null;
    	
    	this.rule = null;
    	this.subAgenda = null;
    	
    	this.whenTrue = null;
    	this.whenFalse = null;
    	this.always = null;
    	
        this.versionNumber = null;
    }
    
    /**
	 * Constructs a KRMS Repository AgendaItemDefinition object from the given builder.
	 * This constructor is private and should only ever be invoked from the builder.
	 * 
	 * @param builder the Builder from which to construct the AgendaItemDefinition
	 */
    private AgendaItemDefinition(Builder builder) {
    	this.id = builder.getId();
    	this.agendaId = builder.getAgendaId();
    	this.ruleId = builder.getRuleId();
    	this.subAgendaId = builder.getSubAgendaId();
    	this.whenTrueId = builder.getWhenTrueId();
    	this.whenFalseId = builder.getWhenFalseId();
    	this.alwaysId = builder.getAlwaysId();
        this.versionNumber = builder.getVersionNumber();

    	if (builder.getRule() != null) { this.rule = builder.getRule().build(); }
    	if (builder.getSubAgenda() != null) { this.subAgenda = builder.getSubAgenda().build(); }
    	if (builder.getWhenTrue() != null) { this.whenTrue  = builder.getWhenTrue().build(); }
    	if (builder.getWhenFalse() != null) { this.whenFalse = builder.getWhenFalse().build(); }
    	if (builder.getAlways() != null) { this.always = builder.getAlways().build(); }
    }

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getAgendaId() {
		return this.agendaId;
	}

	@Override
	public String getRuleId() {
		return this.ruleId;
	}

	@Override
	public String getSubAgendaId() {
		return this.subAgendaId;
	}

	@Override
	public String getWhenTrueId() {
		return this.whenTrueId;
	}

	@Override
	public String getWhenFalseId() {
		return this.whenFalseId;
	}

	@Override
	public String getAlwaysId() {
		return this.alwaysId;
	}

	@Override
	public RuleDefinition getRule() {
		return this.rule; 
	}

	@Override
	public AgendaDefinition getSubAgenda() {
		return this.subAgenda; 
	}

	@Override
	public AgendaItemDefinition getWhenTrue() {
		return this.whenTrue; 
	}

	@Override
	public AgendaItemDefinition getWhenFalse() {
		return this.whenFalse; 
	}

	@Override
	public AgendaItemDefinition getAlways() {
		return this.always; 
	}

    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }
        
	/**
     * This builder is used to construct instances of KRMS Repository AgendaItemDefinition.  It enforces the constraints of the {@link AgendaItemDefinitionContract}.
     */
    public static class Builder implements AgendaItemDefinitionContract, ModelBuilder, Serializable {
		
        private String id;
        private String agendaId;
        private String ruleId;
        private String subAgendaId;
        private String whenTrueId;
        private String whenFalseId;
        private String alwaysId;
        private Long versionNumber;
        
        private RuleDefinition.Builder rule;
        private AgendaDefinition.Builder subAgenda;
        
        private AgendaItemDefinition.Builder whenTrue;
        private AgendaItemDefinition.Builder whenFalse;
        private AgendaItemDefinition.Builder always;
        

		/**
		 * Private constructor for creating a builder with all of it's required attributes.
		 */
        private Builder(String id, String agendaId) {
        	setId(id);
        	setAgendaId(agendaId);
        }

        /**
         * Create a builder with the given parameters.
         *
         * @param id
         * @param agendaId
         * @return Builder
         */
        public static Builder create(String id, String agendaId){
        	return new Builder(id, agendaId);
        }

        /**
         * Creates a builder by populating it with data from the given {@link AgendaItemDefinitionContract}.
         * 
         * @param contract the contract from which to populate this builder
         * @return an instance of the builder populated with data from the contract
         * @throws IllegalArgumentException if the contract is null
         */
        public static Builder create(AgendaItemDefinitionContract contract) {
        	if (contract == null) {
                throw new IllegalArgumentException("contract is null");
        	}
        	Builder builder =  new Builder(contract.getId(), contract.getAgendaId());
        	builder.setRuleId(contract.getRuleId());
        	builder.setSubAgendaId(contract.getSubAgendaId());
        	builder.setWhenTrueId(contract.getWhenTrueId());
        	builder.setWhenFalseId(contract.getWhenFalseId());
        	builder.setAlwaysId(contract.getAlwaysId());
        	
        	if (contract.getRule() != null){
        		builder.setRule(RuleDefinition.Builder.create( contract.getRule() ));
        	}
        	if (contract.getSubAgenda() != null){
        		builder.setSubAgenda( AgendaDefinition.Builder.create( contract.getSubAgenda()));
        	}
        	if (contract.getWhenTrue() != null){
        		builder.setWhenTrue( AgendaItemDefinition.Builder.create( contract.getWhenTrue()));
        	}
        	if (contract.getWhenFalse() != null){
        		builder.setWhenFalse( AgendaItemDefinition.Builder.create( contract.getWhenFalse()));
        	}
        	if (contract.getAlways() != null){
        		builder.setAlways( AgendaItemDefinition.Builder.create( contract.getAlways()));
        	}
            builder.setVersionNumber(contract.getVersionNumber());
        	return builder;
        }

		/**
		 * Sets the value of the id on this builder to the given value.
		 * 
		 * @param agendaItemId the agenda item id to set, may be null, must not be blank
         * <p>The agenda item id is generated by the system.  For new agenda items (not yet persisted) this field is null.
         *    For existing agenda items this field is the generated id.</p>
		 * @throws IllegalArgumentException if the id is blank
		 */
        public void setId(String agendaItemId) {
            if (agendaItemId != null && StringUtils.isBlank(agendaItemId)) {
                throw new IllegalArgumentException("agendaItemId must be null or non-blank");
            }
			this.id = agendaItemId;
		}

        /**
         * Set the value of the agenda id on this builder to the given value.
         *
         * @param agendaId the agenda id of the agenda item to set, must not be null or blank
         * @throws IllegalArgumentException if the agenda id is null or blank
         */
        public void setAgendaId(String agendaId) {
            if (StringUtils.isBlank(agendaId)) {
                throw new IllegalArgumentException("agendaId is blank");
            }
			this.agendaId = agendaId;
		}

        /**
         * Set the value of the rule id on this builder to the given value.
         * @param ruleId the rule id of the agenda item to set
         */
		public void setRuleId(String ruleId) {
			this.ruleId = ruleId;
		}

        /**
         * Set the value of the sub agenda id on this builder to the given value.
         * @param subAgendaId the sub agenda id of the agenda item to set
         */
		public void setSubAgendaId(String subAgendaId) {
			this.subAgendaId = subAgendaId;
		}

        /**
         * Set the value of the agenda item id for the "when true" condition on this builder to the given value.
         * @param whenTrueId the agenda item id for the "when true" condition of the agenda item to set
         */
		public void setWhenTrueId(String whenTrueId) {
			this.whenTrueId = whenTrueId;
		}

        /**
         * Set the value of the agenda item id for the "when false" condition on this builder to the given value.
         * @param whenFalseId the agenda item id for the "when false" condition of the agenda item to set
         */
		public void setWhenFalseId(String whenFalseId) {
			this.whenFalseId = whenFalseId;
		}

        /**
         * Set the value of the agenda item id for the "always" condition on this builder to the given value.
         * @param alwaysId the agenda item id for the "always" condition of the agenda item to set
         */
		public void setAlwaysId(String alwaysId) {
			this.alwaysId = alwaysId;
		}

        /**
         * Set the value of the rule on this builder to the given value.
         * @param rule the rule of the agenda item to set
         */
		public void setRule(RuleDefinition.Builder rule) {
			this.rule = rule;
		}

        /**
         * Set the value of the sub agenda on this builder to the given value.
         * @param subAgenda the sub agenda of the agenda item to set
         */
		public void setSubAgenda(AgendaDefinition.Builder subAgenda) {
			this.subAgenda = subAgenda;
		}

        /**
         * Set the value of the agenda item for the "when true" condition on this builder to the given value.
         * @param whenTrue the agenda item for the "when true" condition of the agenda item to set
         */
		public void setWhenTrue(AgendaItemDefinition.Builder whenTrue) {
			this.whenTrue = whenTrue;
		}

        /**
         * Set the value of the agenda item for the "when false" condition on this builder to the given value.
         * @param whenFalse the agenda item for the "when false" condition of the agenda item to set
         */
		public void setWhenFalse(AgendaItemDefinition.Builder whenFalse) {
			this.whenFalse = whenFalse;
		}

        /**
         * Set the value of the agenda item for the "always" condition on this builder to the given value.
         * @param always the agenda item for the "always" condition of the agenda item to set
         */
		public void setAlways(AgendaItemDefinition.Builder always) {
			this.always = always;
		}

        /**
         * Set the value of the version number on this builder to the given value.
         * @param versionNumber the version number set
         */
		public void setVersionNumber(Long versionNumber){
            this.versionNumber = versionNumber;
        }
        		
		@Override
		public String getId() {
			return id;
		}

		@Override
		public String getAgendaId() {
			return agendaId;
		}

		@Override
		public String getRuleId() {
			return ruleId;
		}

		@Override
		public String getSubAgendaId() {
			return subAgendaId;
		}

		@Override
		public String getWhenTrueId() {
			return whenTrueId;
		}

		@Override
		public String getWhenFalseId() {
			return whenFalseId;
		}

		@Override
		public String getAlwaysId() {
			return alwaysId;
		}

		@Override
		public RuleDefinition.Builder getRule() {
			return rule;
		}

		@Override
		public AgendaDefinition.Builder getSubAgenda() {
			return subAgenda;
		}

		@Override
		public AgendaItemDefinition.Builder getWhenTrue() {
			return whenTrue;
		}

		@Override
		public AgendaItemDefinition.Builder getWhenFalse() {
			return whenFalse;
		}

		@Override
		public AgendaItemDefinition.Builder getAlways() {
			return always;
		}

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

		/**
		 * Builds an instance of a AgendaItemDefinition based on the current state of the builder.
		 * 
		 * @return the fully-constructed AgendaItemDefinition
		 */
        @Override
        public AgendaItemDefinition build() {
            return new AgendaItemDefinition(this);
        }
		
    }
	
	/**
	 * Defines some internal constants used on this class.
	 */
	static class Constants {
		final static String ROOT_ELEMENT_NAME = "AgendaItemDefinition";
		final static String TYPE_NAME = "AgendaItemType";
	}
	
	/**
	 * A private class which exposes constants which define the XML element names to use
	 * when this object is marshalled to XML.
	 */
	public static class Elements {
		final static String ID = "id";
		final static String AGENDA_ID = "agendaId";
		final static String RULE_ID = "ruleId";
		final static String SUB_AGENDA_ID = "subAgendaId";
		final static String WHEN_TRUE_ID = "whenTrueId";
		final static String WHEN_FALSE_ID = "whenFalseId";
		final static String ALWAYS_ID = "alwaysId";

		final static String RULE = "rule";
		final static String SUB_AGENDA = "subAgenda";
		final static String WHEN_TRUE = "whenTrue";
		final static String WHEN_FALSE = "whenFalse";
		final static String ALWAYS = "always";
	}

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + AgendaItemDefinition.Constants.TYPE_NAME;
    }
}
