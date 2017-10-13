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
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.jdom.IllegalAddException;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.krms.api.KrmsConstants;
import org.kuali.rice.krms.api.repository.BuilderUtils;

/**
 * Immutable DTO for Terms.  Construction must be done via the {@link Builder} inner class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = TermDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = TermDefinition.Constants.TYPE_NAME, propOrder = {
		TermDefinition.Elements.ID,
        TermDefinition.Elements.SPECIFICATION,
        TermDefinition.Elements.DESCRIPTION,
		TermDefinition.Elements.PARAMETERS,
        CoreConstants.CommonElements.VERSION_NUMBER,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class TermDefinition extends AbstractDataTransferObject implements TermDefinitionContract {
	
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = Elements.ID, required=false)
	private final String id;
	@XmlElement(name = Elements.SPECIFICATION, required=true)
	private final TermSpecificationDefinition specification;
    @XmlElement(name = Elements.DESCRIPTION, required=false)
    private final String description;
	@XmlElementWrapper(name = Elements.PARAMETERS, required=false)
	@XmlElement(name = "parameter", required=false)
	private final List<TermParameterDefinition> parameters;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
	
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<org.w3c.dom.Element> _futureElements = null;
	
	/**
	 * This constructor is for JAXB only.  Do not invoke directly.
	 */
	private TermDefinition() {
		id = null;
		specification = null;
        description = null;
		parameters = null;
        versionNumber = null;
	}
	
	private TermDefinition(Builder builder) {
		id = builder.getId();
		specification = builder.getSpecification().build();
        description = builder.getDescription();
		parameters = BuilderUtils.convertFromBuilderList(builder.getParameters());
		versionNumber = builder.getVersionNumber();
	}
	
	/**
	 * {@link ModelBuilder} for {@link TermDefinition}s.
	 * 
	 * @author Kuali Rice Team (rice.collab@kuali.org)
	 *
	 */
	public static class Builder implements TermDefinitionContract, ModelBuilder, Serializable {
		
		private static final long serialVersionUID = 1L;
		
		private String id;
        private String description;
		private TermSpecificationDefinition.Builder specification;
		private List<TermParameterDefinition.Builder> parameters;
        private Long versionNumber;
		
		private Builder(String id, TermSpecificationDefinition.Builder termSpecificationDefinition, 
				List<TermParameterDefinition.Builder> termParameters) {
			setId(id);
			setSpecification(termSpecificationDefinition);
			setParameters(termParameters);
		}

		/**
		 * static factory for creating a {@link Builder}.
		 * 
		 * @param id may be null.
		 * @param termSpecification must not be null.
		 * @param termParameters may be null.
		 */
		public static Builder create(String id, TermSpecificationDefinition.Builder termSpecification, 
				List<TermParameterDefinition.Builder> termParameters) {
			return new Builder(id, termSpecification, termParameters);
		}
		
		/**
		 * static factory for creating a {@link Builder} from a {@link TermDefinitionContract}.
		 * 
		 * @param term must be non-null.
		 */
		public static Builder create(TermDefinitionContract term) {
			if (term == null) throw new IllegalAddException("term may not be null");
			
			// Convert TermParameterDefinitionContract to TermParameterDefinition:
			List<TermParameterDefinition.Builder> outParams =
				BuilderUtils.transform(term.getParameters(), TermParameterDefinition.Builder.toBuilder);

			Builder builder = create(term.getId(), 
					// doing my TermSpecificationDefinitionContract conversion inline:
					TermSpecificationDefinition.Builder.create(term.getSpecification()),
					// this is made immutable in the setter
					outParams 
					);
            builder.setDescription(term.getDescription());
			builder.setVersionNumber(term.getVersionNumber());
			return builder;
		}

        public void setDescription(String description) {
            this.description = description;
        }

        // Builder setters:
		
		/**
		 * @param id the id to set.  Should be null to build {@link TermDefinition}s for creation operations.
		 * @throws IllegalArgumentException if the id is non-null and only contains whitespace
		 */
		public void setId(String id) {
			if (id != null && StringUtils.isBlank(id)) {
				throw new IllegalArgumentException("id must contain non-whitespace chars");
			}
			this.id = id;
		}
		
		/**
		 * @param termSpecification the termSpecification to set
		 * @throws IllegalArgumentException if termSpecification is null
		 */
		public void setSpecification(TermSpecificationDefinition.Builder termSpecification) {
			if (termSpecification == null) {
				throw new IllegalArgumentException("termSpecification must not be null");
			}
			this.specification = termSpecification;
		}
		
		/**
		 * @param parameters the termParameters to set.  May be null, or empty.
		 */
		public void setParameters(List<TermParameterDefinition.Builder> parameters) {
			this.parameters = parameters;
		}

		/**
		 * @param versionNumber the versionNumber to set.  May be null.
		 */
        public void setVersionNumber(Long versionNumber){
            this.versionNumber = versionNumber;
        }
        
		// Builder getters:
		
		/**
		 * @return the id
		 */
		@Override
		public String getId() {
			return id;
		}

		/**
		 * @return the termSpecification
		 */
		@Override
		public TermSpecificationDefinition.Builder getSpecification() {
			return specification;
		}

        @Override
        public String getDescription() {
            return description;
        }

        /**
		 * @return the termParameters
		 */
		@Override
		public List<TermParameterDefinition.Builder> getParameters() {
			return parameters;
		}
		
		/**
		 * @return the version number
		 */
        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }
        
		/**
		 * Builds the {@link TermDefinition}, or dies trying.
		 * 
		 * @see org.kuali.rice.core.api.mo.ModelBuilder#build()
		 * @throws IllegalStateException if builder validation fails
		 */
		@Override
		public TermDefinition build() {
			return new TermDefinition(this);
		}
	}
	
	/**
	 * @return the termId.  May be null if this {@link TermDefinition} hasn't been persisted.
	 */
	@Override
	public String getId() {
		return this.id;
	}
	/**
	 * @return the specification.  Will never be null.
	 */
	@Override
	public TermSpecificationDefinition getSpecification() {
		return this.specification;
	}

    @Override
    public String getDescription() {
        return description;
    }

    /**
	 * @return the parameters.  May be empty, but will never be null.
	 */
	@Override
	public List<TermParameterDefinition> getParameters() {
		return this.parameters;
	}
	
	/**
	 * @see org.kuali.rice.core.api.mo.common.Versioned#getVersionNumber()
	 */
    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

	static class Constants {
		public static final String ROOT_ELEMENT_NAME = "termDefinition";
		public static final String TYPE_NAME = "termDefinitionType";
	}

	static class Elements {
		public static final String ID = "id";
		public static final String SPECIFICATION = "specification";
		public static final String PARAMETERS = "parameters";
        public static final String DESCRIPTION = "description";
    }

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + TermDefinition.Constants.TYPE_NAME;
    }
}
