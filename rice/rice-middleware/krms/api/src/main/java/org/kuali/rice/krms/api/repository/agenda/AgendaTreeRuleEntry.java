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

/**
 * Concrete model object implementation of KRMS AgendaTreeRuleEntry
 * immutable.
 * Instances of AgendaTreeRuleEntry can be (un)marshalled to and from XML.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = AgendaTreeRuleEntry.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = AgendaTreeRuleEntry.Constants.TYPE_NAME, propOrder = {
		AgendaTreeRuleEntry.Elements.AGENDA_ITEM_ID,
		AgendaTreeRuleEntry.Elements.RULE_ID,
		AgendaTreeRuleEntry.Elements.IF_TRUE,
		AgendaTreeRuleEntry.Elements.IF_FALSE,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class AgendaTreeRuleEntry extends AbstractDataTransferObject implements AgendaTreeEntryDefinitionContract {

	private static final long serialVersionUID = 8594116503548506936L;

	@XmlElement(name = Elements.AGENDA_ITEM_ID, required = true)
	private final String agendaItemId;
	
	@XmlElement(name = Elements.RULE_ID, required = true)
	private final String ruleId;
	
	@XmlElement(name = Elements.IF_TRUE, required = false)
	private final AgendaTreeDefinition ifTrue;
	
	@XmlElement(name = Elements.IF_FALSE, required = false)
	private final AgendaTreeDefinition ifFalse;
		
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<org.w3c.dom.Element> _futureElements = null;

    /**
     * This constructor should never be called.
     * It is only present for use during JAXB unmarshalling.
     */
	private AgendaTreeRuleEntry() {
		this.agendaItemId = null;
		this.ruleId = null;
		this.ifTrue = null;
		this.ifFalse = null;
	}

    /**
     * Constructs a AgendaTreeRuleEntry from the given builder.
     * This constructor is private and should only ever be invoked from the builder.
     *
     * @param builder the Builder from which to construct the AgendaTreeRuleEntry
     */
	private AgendaTreeRuleEntry(Builder builder) {
		this.agendaItemId = builder.getAgendaItemId();
		this.ruleId = builder.getRuleId();
		this.ifTrue = builder.getIfTrue() == null ? null : builder.getIfTrue().build();
		this.ifFalse = builder.getIfFalse() == null ? null : builder.getIfFalse().build();
	}
	
	@Override
	public String getAgendaItemId() {
		return agendaItemId;
	}

    /**
     * Returns the rule id.
     * @return ruleId of the AgendaTreeRuleEntry
     */
	public String getRuleId() {
		return this.ruleId;
	}

    /**
     * Returns the AgendaTreeDefinition for ifTrue.
     * @return {@link AgendaTreeDefinition} for ifTrue
     */
	public AgendaTreeDefinition getIfTrue() {
		return this.ifTrue;
	}

    /**
     * Returns the AgendaTreeDefinition for ifFalse.
     * @return {@link AgendaTreeDefinition} for ifFalse
     */
	public AgendaTreeDefinition getIfFalse() {
		return this.ifFalse;
	}

    /**
     * This builder is used to construct instances of AgendaTreeRuleEntry.
     * It enforces the constraints of the {@link AgendaTreeEntryDefinitionContract}.
     */
	public static class Builder implements AgendaTreeEntryDefinitionContract, Serializable {
        
		private static final long serialVersionUID = 3548736700798501429L;
		
		private String agendaItemId;
		private String ruleId;
		private AgendaTreeDefinition.Builder ifTrue;
		private AgendaTreeDefinition.Builder ifFalse;

		/**
		 * Private constructor for creating a builder with all of it's required attributes.
         *
         * @param agendaItemId the agendaItemId value to set, must not be null or blank
         * @param ruleId the propId value to set, must not be null or blank
         */
        private Builder(String agendaItemId, String ruleId) {
        	setAgendaItemId(agendaItemId);
        	setRuleId(ruleId);
        }

        /**
         * Create a builder using the given values
         *
         * @param agendaItemId the agendaItemId value to set, must not be null or blank
         * @param ruleId the propId value to set, must not be null or blank
         * @return Builder with the given values set
         */
        public static Builder create(String agendaItemId, String ruleId){
        	return new Builder(agendaItemId, ruleId);
        }
        
        @Override
        public String getAgendaItemId() {
			return this.agendaItemId;
		}

        /**
         * Returns the rule id.
         * @return ruleId of the AgendaTreeRuleEntry
         */
		public String getRuleId() {
			return this.ruleId;
		}

        /**
         * Returns the AgendaTreeDefinition.Builder for ifTrue.
         * @return {@link AgendaTreeDefinition.Builder} for ifTrue
         */
		public AgendaTreeDefinition.Builder getIfTrue() {
			return this.ifTrue;
		}

        /**
         * Returns the AgendaTreeDefinition.Builder for ifFalse.
         * @return {@link AgendaTreeDefinition.Builder} for ifFalse
         */
		public AgendaTreeDefinition.Builder getIfFalse() {
			return this.ifFalse;
		}

        /**
         * Sets the agendaItemId, cannot be null or blank.
         * @param agendaItemId the agendaItemId value to set, must not be null or blank
         * @throws IllegalArgumentException if agendaItemId is null or blank.
         */
		public void setAgendaItemId(String agendaItemId) {
			if (StringUtils.isBlank(agendaItemId)) {
				throw new IllegalArgumentException("agendaItemId was null or blank");
			}
			this.agendaItemId = agendaItemId;
		}

        /**
         * @param ruleId the propId value to set, must not be null or blank
         * @throws IllegalArgumentException if ruleId is null or blank.
         */
		public void setRuleId(String ruleId) {
			if (StringUtils.isBlank(ruleId)) {
				throw new IllegalArgumentException("ruleId was null or blank");
			}
			this.ruleId = ruleId;
		}

        /**
         * Set the ifTrue {@link AgendaTreeDefinition.Builder}
         * @param ifTrue {@link AgendaTreeDefinition.Builder} for ifTrue
         */
		public void setIfTrue(AgendaTreeDefinition.Builder ifTrue) {
			this.ifTrue = ifTrue;
		}

        /**
         * Set the ifFalse {@link AgendaTreeDefinition.Builder}
         * @param ifFalse {@link AgendaTreeDefinition.Builder} for ifFalse
         */
		public void setIfFalse(AgendaTreeDefinition.Builder ifFalse) {
			this.ifFalse = ifFalse;
		}

        /**
         * Build the {@link AgendaTreeRuleEntry} with the builders values
         * @return {@link AgendaTreeRuleEntry} with the builders values
         */
        public AgendaTreeRuleEntry build() {
            return new AgendaTreeRuleEntry(this);
        }
		
    }
	
	/**
	 * Defines some internal constants used on this class.
	 */
	static class Constants {
		final static String ROOT_ELEMENT_NAME = "agendaTreeRuleEntry";
		final static String TYPE_NAME = "AgendaTreeRuleEntryType";
	}
	
	/**
	 * A private class which exposes constants which define the XML element names to use
	 * when this object is marshalled to XML.
	 */
	static class Elements {
		final static String AGENDA_ITEM_ID = "agendaItemId";
		final static String RULE_ID = "ruleId";
		final static String IF_TRUE = "ifTrue";
		final static String IF_FALSE = "ifFalse";
	}

}
