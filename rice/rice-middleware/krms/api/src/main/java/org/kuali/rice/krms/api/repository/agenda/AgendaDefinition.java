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
 * Concrete model object implementation of KRMS Repository Agenda 
 * immutable. 
 * Instances of Agenda can be (un)marshalled to and from XML.
 *
 * @see AgendaDefinitionContract
 */
@XmlRootElement(name = AgendaDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = AgendaDefinition.Constants.TYPE_NAME, propOrder = {
		AgendaDefinition.Elements.AGENDA_ID,
		AgendaDefinition.Elements.NAME,
		AgendaDefinition.Elements.TYPE_ID,
		AgendaDefinition.Elements.CONTEXT_ID,
        AgendaDefinition.Elements.ACTIVE,
		AgendaDefinition.Elements.FIRST_ITEM_ID,
		AgendaDefinition.Elements.ATTRIBUTES,
        CoreConstants.CommonElements.VERSION_NUMBER,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class AgendaDefinition extends AbstractDataTransferObject implements AgendaDefinitionContract {
	private static final long serialVersionUID = 2783959459503209577L;

	@XmlElement(name = Elements.AGENDA_ID, required = false)
	private final String id;
	
	@XmlElement(name = Elements.NAME, required = true)
	private final String name;
	
	@XmlElement(name = Elements.TYPE_ID, required = false)
	private final String typeId;
	
	@XmlElement(name = Elements.CONTEXT_ID, required = true)
	private final String contextId;

    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;
	
	@XmlElement(name = Elements.FIRST_ITEM_ID, required = false)
	private final String firstItemId;
	
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
    private AgendaDefinition() {
    	this.id = null;
    	this.name = null;
    	this.typeId = null;
    	this.contextId = null;
        this.active = true;
    	this.firstItemId = null;
    	this.attributes = null;
        this.versionNumber = null;
    }
    
    /**
	 * Constructs a KRMS Repository Agenda object from the given builder.  
	 * This constructor is private and should only ever be invoked from the builder.
	 * 
	 * @param builder the Builder from which to construct the Agenda
	 */
    private AgendaDefinition(Builder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.typeId = builder.getTypeId();
        this.contextId = builder.getContextId();
        this.active = builder.isActive();
        this.firstItemId = builder.getFirstItemId();
        if (builder.getAttributes() != null){
        	this.attributes = Collections.unmodifiableMap(new HashMap<String, String>(builder.getAttributes()));
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
	public String getTypeId() {
		return this.typeId;
	}

	@Override
	public String getContextId(){
		return this.contextId;
	}

    @Override
    public boolean isActive() {
        return this.active;
    }

	@Override
	public String getFirstItemId(){
		return this.firstItemId;
	}

    /**
     * Returns the internal list of custom/remote attributes associated with the
     * agenda.
     *
     * @return the internal list of custom/remote attribute of the agenda.
     */
	public Map<String, String> getAttributes() {
		return this.attributes; 
	}

    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }
    
 	/**
     * This builder is used to construct instances of KRMS Repository Agenda.  It enforces the constraints of the {@link AgendaDefinitionContract}.
     */
    public static class Builder implements AgendaDefinitionContract, ModelBuilder, Serializable {
		
        private static final long serialVersionUID = -8862851720709537839L;
        
		private String id;
        private String name;
        private String typeId;
        private String contextId;
        private boolean active;
        private String firstItemId;
        private Map<String, String> attributes;
        private Long versionNumber;

		/**
		 * Private constructor for creating a builder with all of it's required attributes.
		 */
        private Builder(String id, String name, String typeId, String contextId) {
        	setId(id);
            setName(name);
            setTypeId(typeId);
            setContextId(contextId);
            setActive(true);
            setAttributes(new HashMap<String, String>());
        }

         /**
          * Create a builder with the given parameters.
          *
          * @param id
          * @param name
          * @param typeId
          * @param contextId
          * @return Builder
          */
        public static Builder create(String id, String name, String typeId, String contextId){
        	return new Builder(id, name, typeId, contextId);
        }

        /**
         * Creates a builder by populating it with data from the given {@link AgendaDefinitionContract}.
         * 
         * @param contract the contract from which to populate this builder
         * @return an instance of the builder populated with data from the contract
         * @throws IllegalArgumentException if the contract is null
         */
        public static Builder create(AgendaDefinitionContract contract) {
        	if (contract == null) {
                throw new IllegalArgumentException("contract is null");
            }
            Builder builder =  new Builder(contract.getId(), contract.getName(), contract.getTypeId(), contract.getContextId());
            builder.setActive(contract.isActive());
            builder.setFirstItemId( contract.getFirstItemId() );
            if (contract.getAttributes() != null) {
                builder.setAttributes(new HashMap<String, String>(contract.getAttributes()));
            }
            builder.setVersionNumber(contract.getVersionNumber());
            return builder;
        }

		/**
		 * Sets the value of the id on this builder to the given value.
		 * 
		 * @param agendaId the agenda id value to set, may be null, must not be blank
         * <p>The agenda id is generated by the system.  For new agendas (not yet persisted) this field is null.
         *    For existing agendas this field is the generated id.</p>
		 * @throws IllegalArgumentException if the id is blank
		 */
        public void setId(String agendaId) {
            if (agendaId != null && StringUtils.isBlank(agendaId)) {
                throw new IllegalArgumentException("agenda ID must be null or non-blank");
            }
			this.id = agendaId;
		}

        /**
         * Set the value of the name on this builder to the given value.
         *
         * @param name the name of the agenda to set, must not be null or blank
         * @throws IllegalArgumentException if the name is null or blank
         */
        public void setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name is blank");
            }
			this.name = name;
		}

         /**
          * Set the value of the type id on this builder to the given value.
          * @param typeId the type id of the agenda to set
          */
		public void setTypeId(String typeId) {
			this.typeId = typeId;
		}

        /**
         * Set the value of the context id on this builder to the given value.
         *
         * @param contextId the context id of the agenda to set, must not be null or blank
         * @throws IllegalArgumentException if the name is null or blank
         */
		public void setContextId(String contextId) {
			if (StringUtils.isBlank(contextId)) {
                throw new IllegalArgumentException("context id is blank");
		}
			this.contextId = contextId;
		}

         /**
          * Set the value of the active indicator on this builder to the given value.
          *
          * @param active the active indicator of the agenda to set
          */
        public void setActive(boolean active) {
            this.active = active;
        }

         /**
          * Set the value of the first agenda item id on this builder to the given value.
          *
          * @param firstItemId the first agenda item of the agenda tree to set
          */
		public void setFirstItemId(String firstItemId) {
			this.firstItemId = firstItemId;
		}

         /**
          * Set the value of the remote/custom attributes on this builder to the given value.
          *
          * @param attributes the remote/custom attributes of the agenda to set
          */
		public void setAttributes(Map<String, String> attributes){
			if (attributes == null){
				this.attributes = Collections.emptyMap();
			} else {
                this.attributes = Collections.unmodifiableMap(attributes);
            }
		}
		
		/**
         * Sets the version number on this builder to the given value.
         *
         * @param versionNumber the version number to set
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
		public String getTypeId() {
			return typeId;
		}

		@Override
		public String getContextId() {
			return contextId;
		}

        @Override
        public boolean isActive() {
            return active;
        }

		@Override
		public String getFirstItemId() {
			return firstItemId;
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
		 * Builds an instance of a Agenda based on the current state of the builder.
		 * 
		 * @return the fully-constructed Agenda
		 */
        @Override
        public AgendaDefinition build() {
            return new AgendaDefinition(this);
        }
		
    }
	
	/**
	 * Defines some constants used on this class.
	 */
	public static class Constants {
		final static String ROOT_ELEMENT_NAME = "agenda";
		final static String TYPE_NAME = "AgendaType";
		final static String[] HASH_CODE_EQUALS_EXCLUDE = { "_futureElements" };
        public final static String EVENT = "Event";   // key for event attribute
	}
	
	/**
	 * A private class which exposes constants which define the XML element names to use
	 * when this object is marshalled to XML.
	 */
	public static class Elements {
		public final static String AGENDA_ID = "id";
		final static String NAME = "name";
		final static String TYPE_ID = "typeId";
		final static String CONTEXT_ID = "contextId";
        final static String ACTIVE = "active";
		final static String FIRST_ITEM_ID = "firstItemId";
		final static String ATTRIBUTES = "attributes";
		final static String ATTRIBUTE = "attribute";
	}

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + AgendaDefinition.Constants.TYPE_NAME;
    }
}
