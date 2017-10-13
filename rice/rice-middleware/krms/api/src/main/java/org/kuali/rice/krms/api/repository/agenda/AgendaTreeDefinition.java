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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.krms.api.KrmsConstants;

/**
 * Concrete model object implementation of KRMS Repository AgendaTreeDefinition
 * immutable. 
 * Instances of Agenda can be (un)marshalled to and from XML.
 *
 * @see AgendaDefinitionContract
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = AgendaTreeDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = AgendaTreeDefinition.Constants.TYPE_NAME, propOrder = {
		AgendaTreeDefinition.Elements.AGENDA_ID,
		AgendaTreeDefinition.Elements.ENTRIES,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class AgendaTreeDefinition extends AbstractDataTransferObject {
	
	private static final long serialVersionUID = 3355519740298280591L;

	@XmlElement(name = Elements.AGENDA_ID, required = false)
	private final String agendaId;
	
	@XmlElements(value = {
            @XmlElement(name = Elements.RULE, type = AgendaTreeRuleEntry.class, required = false),
            @XmlElement(name = Elements.SUB_AGENDA, type = AgendaTreeSubAgendaEntry.class, required = false)
	})
	private final List<AgendaTreeEntryDefinitionContract> entries;
		
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<org.w3c.dom.Element> _futureElements = null;
	
	/** 
     * This constructor should never be called.  
     * It is only present for use during JAXB unmarshalling. 
     */
    private AgendaTreeDefinition() {
    	this.agendaId = null;
    	this.entries = null;
    }

    /**
     * Constructs a AgendaTreeDefinition from the given builder.
     * This constructor is private and should only ever be invoked from the builder.
     *
     * @param builder the Builder from which to construct the AgendaTreeDefinition
     */
    private AgendaTreeDefinition(Builder builder) {
    	this.agendaId = builder.getAgendaId();
        this.entries = builder.getEntries();
    }

    /**
     * Returns the agendaId
     * @return agendaId of the AgendaTreeDefinition
     */
    public String getAgendaId() {
    	return agendaId;
    }

    /**
     * Returns the {@link AgendaTreeEntryDefinitionContract}s
     * @return List<{@link AgendaTreeEntryDefinitionContract}>s
     */
	public List<AgendaTreeEntryDefinitionContract> getEntries() {
		if (entries == null){
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(entries);
	}


    /**
     * This builder is used to construct instances of AgendaTreeDefinition.
     */
    public static class Builder implements ModelBuilder, Serializable {
		        
		private static final long serialVersionUID = 7981215392039022620L;
		
		private String agendaId;
		private List<AgendaTreeEntryDefinitionContract> entries;

		/**
		 * Private constructor for creating a builder with all of it's required attributes.
		 */
        private Builder() {
        	this.entries = new ArrayList<AgendaTreeEntryDefinitionContract>();
        }

        /**
         * Create a new Builder
         * @return a new Builder
         */
        public static Builder create(){
        	return new Builder();
        }

        /**
         * Sets the agendaId to the given parameter
         * @param agendaId to set the apendaId value to, must not be null or blank
         * @thows IllegalArgumentException if the agendaId is null or blank
         */
        public void setAgendaId(String agendaId) {
			if (StringUtils.isBlank(agendaId)) {
				throw new IllegalArgumentException("agendaItemId was null or blank");
			}
        	this.agendaId = agendaId;
        }

        /**
         * Adds the given {@link AgendaTreeRuleEntry} to the entries.
         * @param ruleEntry {@link AgendaTreeRuleEntry} to be added to the entries, must not be null
         * @thows IllegalArgumentException if the ruleEntry is null
         */
        public void addRuleEntry(AgendaTreeRuleEntry ruleEntry) {
        	if (ruleEntry == null) {
        		throw new IllegalArgumentException("ruleEntry was null");
        	}
        	entries.add(ruleEntry);
        }

        /**
         * Adds the given {@link AgendaTreeSubAgendaEntry} to the entries.
         * @param subAgendaEntry {@link AgendaTreeSubAgendaEntry} to add to the entries, must not be null
         * @thows IllegalArgumentException if the subAgendaEntry is null
         */
        public void addSubAgendaEntry(AgendaTreeSubAgendaEntry subAgendaEntry) {
        	if (subAgendaEntry == null) {
        		throw new IllegalArgumentException("subAgendaEntry was null");
        	}
        	entries.add(subAgendaEntry);
        }

        /**
         * Returns the agendaId
         * @return agendaId
         */
        public String getAgendaId() {
        	return this.agendaId;
        }

        /**
         * Returns the list of {@link AgendaTreeEntryDefinitionContract}s entries
         * @return List<{@link AgendaTreeEntryDefinitionContract}> of entries
         */
        public List<AgendaTreeEntryDefinitionContract> getEntries() {
        	return this.entries;
        }

        @Override
        public AgendaTreeDefinition build() {
            return new AgendaTreeDefinition(this);
        }
		
    }
	
	/**
	 * Defines some internal constants used on this class.
	 */
	static class Constants {
		final static String ROOT_ELEMENT_NAME = "agendaTreeDefinition";
		final static String TYPE_NAME = "AgendaTreeDefinition";
	}
	
	/**
	 * A private class which exposes constants which define the XML element names to use
	 * when this object is marshalled to XML.
	 */
	static class Elements {
		final static String AGENDA_ID = "agendaId";
		final static String ENTRIES = "entries";
		final static String RULE = "rule";
		final static String SUB_AGENDA = "subAgenda";
	}

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + AgendaTreeDefinition.Constants.TYPE_NAME;
    }

}
