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

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;

/**
 * Concrete model object implementation of KRMS Repository AgendaTreeSubAgendaEntry
 * immutable.
 * Instances of Agenda can be (un)marshalled to and from XML.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = AgendaTreeSubAgendaEntry.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = AgendaTreeSubAgendaEntry.Constants.TYPE_NAME, propOrder = {
		AgendaTreeSubAgendaEntry.Elements.AGENDA_ITEM_ID,
		AgendaTreeSubAgendaEntry.Elements.SUB_AGENDA_ID,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class AgendaTreeSubAgendaEntry extends AbstractDataTransferObject implements AgendaTreeEntryDefinitionContract {

	private static final long serialVersionUID = 8594116503548506936L;

	@XmlElement(name = Elements.AGENDA_ITEM_ID, required = true)
	private final String agendaItemId;
	
	@XmlElement(name = Elements.SUB_AGENDA_ID, required = true)
	private final String subAgendaId;
		
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<org.w3c.dom.Element> _futureElements = null;

    /**
     * This constructor should never be called.
     * It is only present for use during JAXB unmarshalling.
     */
	private AgendaTreeSubAgendaEntry() {
		this.agendaItemId = null;
		this.subAgendaId = null;
	}

    /**
     * Constructs a AgendaTreeSubAgendaEntry from the given builder.
     * This constructor is private and should only ever be invoked from the builder.
     *
     * @param builder the Builder from which to construct the AgendaTreeSubAgendaEntry
     */
	private AgendaTreeSubAgendaEntry(Builder builder) {
		this.agendaItemId = builder.getAgendaItemId();
		this.subAgendaId = builder.getSubAgendaId();
	}
	
	@Override
	public String getAgendaItemId() {
		return agendaItemId;
	}

    /**
     * Returns the subAgendId
     * @return subAgendaId
     */
	public String getSubAgendaId() {
		return this.subAgendaId;
	}

    /**
     * This builder is used to construct instances of AgendaTreeSubAgendaEntry.
     */
	public static class Builder implements ModelBuilder, Serializable {
        
		private static final long serialVersionUID = 3548736700798501429L;
		
		private String agendaItemId;
		private String subAgendaId;

		/**
		 * Private constructor for creating a builder with all of it's required attributes.
         * @param agendaItemId to set the agendaItemId value to, must not be null
         * @param subAgendaId to set the subAgendaId value to, must not be null
         */
        private Builder(String agendaItemId, String subAgendaId) {
        	setAgendaItemId(agendaItemId);
        	setSubAgendaId(subAgendaId);
        }

        /**
         * Create a builder using the given values
         *
         * @param agendaItemId to set the agendaItemId value to, must not be null
         * @param subAgendaId to set the subAgendaId value to, must not be null
         * @return Builder with the given values set
         */
        public static Builder create(String agendaItemId, String subAgendaId){
        	return new Builder(agendaItemId, subAgendaId);
        }

        /**
         * Returns the agendaItemId
         * @return the agendaItemId of the builder
         */
        public String getAgendaItemId() {
			return this.agendaItemId;
		}

        /**
         * Returns the subAgendaId
         * @return the subAgendaId of the builder
         */
		public String getSubAgendaId() {
			return this.subAgendaId;
		}

        /**
         * Sets the agendaItemId of the builder, cannot be null
         * @param agendaItemId to set the value of the agendaItemId to, must not be null
         * @throws IllegalArgumentException if the agendaItemId is null
         */
		public void setAgendaItemId(String agendaItemId) {
			if (agendaItemId == null) {
				throw new IllegalArgumentException("agendaItemId was null");
			}
			this.agendaItemId = agendaItemId;
		}

        /**
         * Sets the subAgendaId of the builder, cannot be null
         * @param subAgendaId to set the subAgendaId value to, must not be null
         * @throws IllegalArgumentException if the subAgendaId is null
         */
		public void setSubAgendaId(String subAgendaId) {
			if (subAgendaId == null) {
				throw new IllegalArgumentException("subAgendaId was null");
			}
			this.subAgendaId = subAgendaId;
		}

		@Override
        public AgendaTreeSubAgendaEntry build() {
            return new AgendaTreeSubAgendaEntry(this);
        }
		
    }
	
	/**
	 * Defines some internal constants used on this class.
	 */
	static class Constants {
		final static String ROOT_ELEMENT_NAME = "agendaTreeSubAgendaEntry";
		final static String TYPE_NAME = "AgendaTreeSubAgendaEntryType";
	}
	
	/**
	 * A private class which exposes constants which define the XML element names to use
	 * when this object is marshalled to XML.
	 */
	static class Elements {
		final static String AGENDA_ITEM_ID = "agendaItemId";
		final static String SUB_AGENDA_ID = "subAgendaId";
	}

}
