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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.krms.api.KrmsConstants;
import org.kuali.rice.krms.api.repository.BuilderUtils;
import org.kuali.rice.krms.api.repository.BuilderUtils.Transformer;
import org.kuali.rice.krms.api.repository.category.CategoryDefinition;
import org.kuali.rice.krms.api.repository.category.CategoryDefinitionContract;

/**
 * Immutable DTO for TermSpecifications.  Construction must be done via the {@link Builder} inner class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = TermSpecificationDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = TermSpecificationDefinition.Constants.TYPE_NAME, propOrder = {
		TermSpecificationDefinition.Elements.ID,
		TermSpecificationDefinition.Elements.NAME,
        TermSpecificationDefinition.Elements.NAMESPACE,
        TermSpecificationDefinition.Elements.TYPE,
        TermSpecificationDefinition.Elements.DESCRIPTION,
        TermSpecificationDefinition.Elements.ACTIVE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        TermSpecificationDefinition.Elements.CATEGORIES,
        "contextIds", // has to match the field name -- the element wrapper name does not
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class TermSpecificationDefinition extends AbstractDataTransferObject implements TermSpecificationDefinitionContract {
	
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = Elements.ID, required=false)
	private final String id;
    @XmlElement(name = Elements.NAME, required=true)
    private final String name;
    @XmlElement(name = Elements.NAMESPACE, required=true)
    private final String namespace;
	@XmlElement(name = Elements.TYPE, required=true)
	private final String type;
    @XmlElement(name = Elements.DESCRIPTION, required=false)
    private final String description;
    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @XmlElementWrapper(name = Elements.CATEGORIES, required = false)
    @XmlElement(name = Elements.CATEGORY, required = false)
    private final List<CategoryDefinition> categories;


    @XmlElementWrapper(name = Elements.CONTEXTS, required = false)
    @XmlElement(name = Elements.CONTEXT_ID, required = false)
    private final List<String> contextIds;

	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<org.w3c.dom.Element> _futureElements = null;
	
	/**
	 * For JAXB use only, shouldn't ever be invoked directly
	 */
	private TermSpecificationDefinition() {
		id = null;
		name = null;
        namespace = null;
		type = null;
        description = null;
        active = true;
        versionNumber = null;
        this.categories = null;
        this.contextIds = null;
	}
	
	/**
	 * Private constructor enforces use of Builder
	 * 
	 * @param b the builder to use
	 */
	private TermSpecificationDefinition(Builder b) {
		id = b.getId();
		name = b.getName();
        namespace = b.getNamespace();
		type = b.getType();
        description = b.getDescription();
        active = b.isActive();
		versionNumber = b.getVersionNumber();
        this.categories = constructCategories(b.getCategories());
        this.contextIds = Collections.unmodifiableList(new ArrayList(b.getContextIds()));
	}

    private static List<CategoryDefinition> constructCategories(List<CategoryDefinition.Builder> categoryBuilders) {
    	List<CategoryDefinition> categories = new ArrayList<CategoryDefinition>();
    	if (categoryBuilders != null) {
    		for (CategoryDefinition.Builder categoryBuilder : categoryBuilders) {
    			categories.add(categoryBuilder.build());
    		}
    	}
    	return categories;
    }

	/**
	 * Builder for the {@link TermSpecificationDefinition} immutable DTO.  Instantiate using static factory method(s).
	 * 
	 * @author Kuali Rice Team (rice.collab@kuali.org)
	 */
	public static class Builder implements TermSpecificationDefinitionContract, ModelBuilder, Serializable {
		
		private static final long serialVersionUID = 1L;
		
		private String termSpecificationId;
		private String name;
        private String namespace;
		private String type;
        private String description;
        private boolean active;
        private Long versionNumber;
        private List<CategoryDefinition.Builder> categories;
        private List<String> contextIds;

		private static final String NON_NULL_NON_EMPTY_ERROR =  " must be non-null and must contain non-whitespace chars"; 

		/**
		 * {@link Transformer} to ease converting lists to Builder types
		 */
		public static final Transformer<TermSpecificationDefinitionContract, TermSpecificationDefinition.Builder>
		toBuilder = new BuilderUtils.Transformer<TermSpecificationDefinitionContract, TermSpecificationDefinition.Builder>() {
			public TermSpecificationDefinition.Builder transform(TermSpecificationDefinitionContract input) {
				return TermSpecificationDefinition.Builder.create(input);
			}
		};
		
		private Builder(String termSpecificationId, String name, String namespace, String type) {
			// weird to use setters in constructor .. oh well.
			setId(termSpecificationId);
			setNamespace(namespace);
			setName(name);
			setType(type);
            setActive(true);
            setCategories(new ArrayList<CategoryDefinition.Builder>());
            setContextIds(new ArrayList<String>());
		}
		
		/**
		 * static factory for a {@link Builder} from a complete set of field values for this object.
		 * 
		 *
         * @param termSpecificationId the primary key field.  Must be null for service methods that
         * create {@link org.kuali.rice.krms.api.repository.term.TermSpecificationDefinitionContract}s, and must be non-null & contain non-whitespace
         * chars otherwise.
         * @param name the name for the {@link org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition}.  Must be non-null;.
         * @param namespace the namespace for the {@link org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition}.  Must be non-null & contain non-whitespace.
         *@param type the type for the {@link org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition}  @return a {@link Builder} object
		 * @throws IllegalArgumentException if invalid parameters are supplied.
		 */
		public static Builder create(String termSpecificationId, String name, String namespace, String type) {
			return new Builder(termSpecificationId, name, namespace, type);
		}
		
		/**
		 * static factory for a {@link Builder} from a {@link TermSpecificationDefinitionContract}.
		 * 
		 * @param termSpecification may not be null;
		 * @throws IllegalArgumentException if termSpecification is null, or violates the field invariants of the {@link Builder}.
		 */
		public static Builder create(TermSpecificationDefinitionContract termSpecification) {
			if (termSpecification == null) throw new IllegalArgumentException("termSpecification must be non-null");
			Builder builder = new Builder(termSpecification.getId(), termSpecification.getName(), termSpecification.getNamespace(),
                    termSpecification.getType());
            builder.setDescription(termSpecification.getDescription());
            builder.setActive(termSpecification.isActive());
			builder.setVersionNumber(termSpecification.getVersionNumber());
            for (CategoryDefinitionContract category : termSpecification.getCategories()) {
                builder.getCategories().add(CategoryDefinition.Builder.create(category));
            }
            if (termSpecification.getContextIds() != null) {
                builder.getContextIds().addAll(termSpecification.getContextIds());
            }

			return builder;
		}

        public void setDescription(String description) {
            this.description = description;
        }

        // Setters
		
		/**
		 * @param termSpecificationId the key for this {@link TermSpecificationDefinition}.  Must be null for
		 * service methods that create {@link TermSpecificationDefinitionContract}s, and otherwise must be non-null & contain 
		 * non-whitespace chars.
		 */
		public void setId(String termSpecificationId) {
			if (termSpecificationId != null && StringUtils.isBlank(termSpecificationId))
				throw new IllegalArgumentException("termSpecificationId must contain non-whitespace chars");
			this.termSpecificationId = termSpecificationId;
		}
		
		/**
		 * @param namespace the namespace to set.  Must be non-null and contain non-whitespace chars;
		 */
		public void setNamespace(String namespace) {
			if (StringUtils.isBlank(namespace)) {
				throw new IllegalArgumentException("namespace" + NON_NULL_NON_EMPTY_ERROR);
			}
			this.namespace = namespace;
		}
		
		/**
		 * @param name the name to set.  Must be non-null and contain non-whitespace chars;
		 */
		public void setName(String name) {
			if (StringUtils.isBlank(name)) {
				throw new IllegalArgumentException("name" + NON_NULL_NON_EMPTY_ERROR);
			}
			this.name = name;
		}
		/**
		 * @param type the type to set. Must be non-null and contain non-whitespace chars;
		 */
		public void setType(String type) {
			if (StringUtils.isBlank(type)) {
				throw new IllegalArgumentException("type" + NON_NULL_NON_EMPTY_ERROR);
			}
			this.type = type;
		}
		
		/**
		 * @param versionNumber the versionNumber to set.  May be null.
		 */
        public void setVersionNumber(Long versionNumber){
            this.versionNumber = versionNumber;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        /**
         * @param categories the categories to set.  May not be null but can be an empty set.
         */
        public void setCategories(List<CategoryDefinition.Builder> categories) {
            if (categories == null) {
                throw new IllegalArgumentException("categories was null");
            }
            this.categories = categories;
        }

        /**
         * @param contextIds the contextIds to set.  May not be null but may be empty.
         */
        public void setContextIds(List<String> contextIds) {
            if (contextIds == null) {
                throw new IllegalArgumentException("contextIds was null");
            }
            this.contextIds = contextIds;
        }

        // Getters
		
		/**
		 * @return the termSpecificationId
		 */
		@Override
		public String getId() {
			return this.termSpecificationId;
		}

		/**
		 * @return the namespace
		 */
		@Override
		public String getNamespace() {
			return this.namespace;
		}

		/**
		 * @return the name
		 */
		@Override
		public String getName() {
			return this.name;
		}

		/**
		 * @return the type
		 */
		@Override
		public String getType() {
			return this.type;
		}

        @Override
        public String getDescription() {
            return this.description;
        }

        /**
		 * @return the version number
		 */
        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        /**
         * @return the categories
         */
        @Override
        public List<CategoryDefinition.Builder> getCategories() {
            return this.categories;
        }

        @Override
        public List<String> getContextIds() {
            return contextIds;
        }

        /**
		 * Constructs a {@link TermSpecificationDefinition}
		 * @see org.kuali.rice.core.api.mo.ModelBuilder#build()
		 */
		@Override
		public TermSpecificationDefinition build() {
			return new TermSpecificationDefinition(this);
		}
	}

	/**
	 * This value will be non-null for persisted  
	 * @see org.kuali.rice.krms.api.repository.term.TermSpecificationDefinitionContract#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @see org.kuali.rice.krms.api.repository.term.TermSpecificationDefinitionContract#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

    @Override
    public String getNamespace() {
        return namespace;
    }

	/**
	 * @see org.kuali.rice.krms.api.repository.term.TermSpecificationDefinitionContract#getType()
	 */
	@Override
	public String getType() {
		return type;
	}

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    /**
	 * @see org.kuali.rice.core.api.mo.common.Versioned#getVersionNumber()
	 */
    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

    /**
     * @see TermSpecificationDefinitionContract#getCategories()
     */
    @Override
    public List<CategoryDefinition> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    @Override
    public List<String> getContextIds() {
        return contextIds;
    }

    /**
	 * Defines some internal constants used on this class.
	 */
	static class Constants {
		final static String ROOT_ELEMENT_NAME = "termSpecification";
		final static String TYPE_NAME = "TermSpecificationType";
	}
	
	static class Elements {
		public static final String ID = "id";
		public static final String NAME = "name";
        public final static String NAMESPACE = "namespace";
        public static final String TYPE = "type";
        public static final String DESCRIPTION = "description";
        public static final String ACTIVE = "active";
        public final static String CATEGORIES = "categories";
        public final static String CATEGORY = "category";
        public final static String CONTEXTS = "contexts";
        public final static String CONTEXT_ID = "contextId";
    }

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + TermSpecificationDefinition.Constants.TYPE_NAME;
    }
}
