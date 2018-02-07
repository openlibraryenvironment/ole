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
package org.kuali.rice.krms.api.repository.term;

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
import org.kuali.rice.krms.api.repository.BuilderUtils.Transformer;

/**
 * Immutable DTO for TermParameters.  An instance represents a single parameter on a Term. 
 * Construction must be done via the {@link Builder} inner class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = TermParameterDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = TermParameterDefinition.Constants.TYPE_NAME, propOrder = {
		TermParameterDefinition.Elements.ID,
		TermParameterDefinition.Elements.NAME,
		TermParameterDefinition.Elements.VALUE,
        CoreConstants.CommonElements.VERSION_NUMBER,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class TermParameterDefinition extends AbstractDataTransferObject implements TermParameterDefinitionContract {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = Elements.ID, required=true)
	private final String id;
	
	private final String termId;

	@XmlElement(name = Elements.NAME, required=true)
	private final String name;
	
	@XmlElement(name = Elements.VALUE)
	private final String value;
	
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<org.w3c.dom.Element> _futureElements = null;
	
	// For JAXB use only, shouldn't be invoked directly
	private TermParameterDefinition() {
		id = null;
		termId = null;
		name = null;
		value = null;
        versionNumber = null;
	}
	
	private TermParameterDefinition(Builder builder) {
		id = builder.getId(); 
		termId = builder.getTermId();
		name = builder.getName();
		value = builder.getValue();
		versionNumber = builder.getVersionNumber();
	}
	
	public static class Builder implements TermParameterDefinitionContract, ModelBuilder, Serializable {

		private static final long serialVersionUID = 1L;
		
		private String id;
		private String termId;
		private String name;
		private String value;
        private Long versionNumber;
		
		private static final String NON_NULL_NON_EMPTY_ERROR =  
			" must be non-null and must contain non-whitespace chars";
		
		public static final Transformer<TermParameterDefinitionContract, TermParameterDefinition.Builder> toBuilder = 
			new Transformer<TermParameterDefinitionContract, TermParameterDefinition.Builder>() {
			public Builder transform(TermParameterDefinitionContract input) {
				return Builder.create(input);
			};
		};
		
		private Builder(String id, String termId, String name, String value) {
			setId(id);
			setTermId(termId);
			setName(name);
			setValue(value);
		}
		
		/**
		 * static factory to create a {@link Builder} from fields
		 * 
		 * @param id must be null, or contain non-whitespace
		 * @param termId must be null, or contain non-whitespace
		 * @param name must be non-null
		 * @param value
		 */
		public static Builder create(String id, String termId, String name, String value) {
			return new Builder(id, termId, name, value);
		}
		
		/**
		 * static factory to create a {@link Builder} from a {@link TermParameterDefinitionContract} 
		 * 
		 * @param termParameterDefinition
		 */
		public static Builder create(TermParameterDefinitionContract termParameterDefinition) {
			Builder builder = new Builder(termParameterDefinition.getId(), 
					termParameterDefinition.getTermId(),
					termParameterDefinition.getName(), 
					termParameterDefinition.getValue());
			builder.setVersionNumber(termParameterDefinition.getVersionNumber());
			return builder;
		}
		
		// Setters:
		
		/**
		 * @param id the id to set.  for {@link TermParameterDefinition}s used in creational 
		 * service methods, it must be null.  Otherwise, it must be non-null and contain non-whitespace chars.
		 * @throws IllegalArgumentException if id is all whitespace chars
		 */
		public void setId(String id) {
			if (id != null && StringUtils.isBlank(id)) {
				throw new IllegalArgumentException("id must contain non-whitespace chars");
			}
			this.id = id;
		}
		
		/**
		 * @param termId the termId to set
		 */
		public void setTermId(String termId) {
			if (termId != null && StringUtils.isBlank(termId)) {
				throw new IllegalArgumentException("termId must contain non-whitespace chars");
			}
			this.termId = termId;
		}
		
		/**
		 * @param name the name to set.  Must be non-null and contain non-whitespace chars.
		 * @throws IllegalArgumentException if name is null or is all whitespace chars
		 */
		public void setName(String name) {
			if (StringUtils.isBlank(name)) {
				throw new IllegalArgumentException("name" + NON_NULL_NON_EMPTY_ERROR);
			}
			this.name = name;
		}
		
		/**
		 * @param value the value to set.  May be null or empty.
		 */
		public void setValue(String value) {
			this.value = value;
		}
		
		/**
		 * @param versionNumber the versionNumber to set.  May be null.
		 */
        public void setVersionNumber(Long versionNumber){
            this.versionNumber = versionNumber;
        }
        
		// Getters:
		
		/**
		 * @return the id
		 */
		@Override
		public String getId() {
			return this.id;
		}
		
		/**
		 * @return the termId
		 */
		@Override
		public String getTermId() {
			return this.termId;
		}
		
		/**
		 * @return the name
		 */
		@Override
		public String getName() {
			return this.name;
		}
		
		/**
		 * @return the value
		 */
		@Override
		public String getValue() {
			return this.value;
		}		
		
		/**
		 * @return the version number
		 */
        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

		/**
		 * return a {@link TermParameterDefinition} instance constructed from this {@link Builder} 
		 * @see org.kuali.rice.core.api.mo.ModelBuilder#build()
		 */
		@Override
		public TermParameterDefinition build() {
			return new TermParameterDefinition(this);
		}
		
	}
	
	
	/**
	 * @return the id
	 */
	@Override
	public String getId() {
		return this.id;
	}
	/**
	 * @return the termId
	 */
	@Override
	public String getTermId() {
		return termId;
	}
	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return this.name;
	}
	/**
	 * @return the value
	 */
	@Override
	public String getValue() {
		return this.value;
	}
	
	/**
	 * @see org.kuali.rice.core.api.mo.common.Versioned#getVersionNumber()
	 */
    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }
	
	public static class Constants {
		public static final String ROOT_ELEMENT_NAME = "TermParameterDefinition";
		public static final String TYPE_NAME = "TermParameterDefinitionType";
	}
	
	public static class Elements {
		public static final String ID = "id";
		public static final String TERM_ID = "termId";
		public static final String NAME = "name";
		public static final String VALUE = "value";
	}
}
