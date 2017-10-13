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
package org.kuali.rice.krms.api.repository.function;

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
import org.kuali.rice.krms.api.repository.category.CategoryDefinition;
import org.kuali.rice.krms.api.repository.category.CategoryDefinitionContract;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.w3c.dom.Element;

/**
 * An immutable representation of a function definition.
 * 
 * @see FunctionDefinitionContract
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = FunctionDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = FunctionDefinition.Constants.TYPE_NAME, propOrder = {
		FunctionDefinition.Elements.ID,
		FunctionDefinition.Elements.NAMESPACE,
		FunctionDefinition.Elements.NAME,
		FunctionParameterDefinition.Elements.DESCRIPTION,
		FunctionDefinition.Elements.RETURN_TYPE,
		FunctionDefinition.Elements.TYPE_ID,
		FunctionDefinition.Elements.ACTIVE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        FunctionDefinition.Elements.PARAMETERS,
        FunctionDefinition.Elements.CATEGORIES,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class FunctionDefinition extends AbstractDataTransferObject implements FunctionDefinitionContract {

	private static final long serialVersionUID = 1391030685309770560L;

	@XmlElement(name = Elements.ID, required = false)
	private final String id;
	
	@XmlElement(name = Elements.NAMESPACE, required = true)
	private final String namespace;
	
	@XmlElement(name = Elements.NAME, required = true)
	private final String name;
	
	@XmlElement(name = Elements.DESCRIPTION, required = false)
	private final String description;
	
	@XmlElement(name = Elements.RETURN_TYPE, required = true)
	private final String returnType;
	
	@XmlElement(name = Elements.TYPE_ID, required = true)
	private final String typeId;
	
	@XmlElement(name = Elements.ACTIVE, required = true)
	private final boolean active;
	
	@XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
	private final Long versionNumber;
	
	@XmlElementWrapper(name = Elements.PARAMETERS, required = false)
	@XmlElement(name = Elements.PARAMETER, required = false)
	private final List<FunctionParameterDefinition> parameters;

    @XmlElementWrapper(name = Elements.CATEGORIES, required = false)
    @XmlElement(name = Elements.CATEGORY, required = false)
    private final List<CategoryDefinition> categories;
	
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;
	
	/**
     * Private constructor used only by JAXB.
     */
    private FunctionDefinition() {
    	this.id = null;
    	this.namespace = null;
    	this.name = null;
    	this.description = null;
    	this.returnType = null;
    	this.typeId = null;
    	this.active = true;
    	this.versionNumber = null;
    	this.parameters = null;
        this.categories = null;
    }

    /**
     * Constructs a FunctionDefinition from the given builder.  This constructor is private and should only
     * ever be invoked from the builder.
     *
     * @param builder the Builder from which to construct the FunctionDefinition
     */
    private FunctionDefinition(Builder builder) {
    	this.id = builder.getId();
    	this.namespace = builder.getNamespace();
    	this.name = builder.getName();
    	this.description = builder.getDescription();
    	this.returnType = builder.getReturnType();
    	this.typeId = builder.getTypeId();
    	this.active = builder.isActive();
    	this.versionNumber = builder.getVersionNumber();
    	this.parameters = new ArrayList<FunctionParameterDefinition>();
    	for (FunctionParameterDefinition.Builder parameter : builder.getParameters()) {
    		this.parameters.add(parameter.build());
    	}
        this.categories = new ArrayList<CategoryDefinition>();
        for (CategoryDefinition.Builder category : builder.getCategories()) {
            this.categories.add(category.build());
        }
    }
    
	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getNamespace() {
		return namespace;
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
	public String getReturnType() {
		return returnType;
	}

	@Override
	public String getTypeId() {
		return typeId;
	}

	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	public Long getVersionNumber() {
		return versionNumber;
	}
	
	@Override
	public List<FunctionParameterDefinition> getParameters() {
		return Collections.unmodifiableList(parameters);
	}

    @Override
    public List<CategoryDefinition> getCategories() {
        return Collections.unmodifiableList(categories);
    }

	/**
	 * A builder which can be used to construct {@link FunctionDefinition}
	 * instances.  Enforces the constraints of the {@link FunctionDefinitionContract}.
	 * 
	 * @author Kuali Rice Team (rice.collab@kuali.org)
	 *
	 */
	public static final class Builder implements FunctionDefinitionContract, ModelBuilder, Serializable  {
    	    	
    	private static final long serialVersionUID = -4470376239998290245L;
    	
		private String id;
    	private String namespace;
    	private String name;
    	private String description;
    	private String returnType;
    	private String typeId;
    	private boolean active;
    	private Long versionNumber;
    	private List<FunctionParameterDefinition.Builder> parameters;
        private List<CategoryDefinition.Builder> categories;

        /**
         * Private constructor, use create method
         * @param namespace to use when building
         * @param name to use when building
         * @param returnType to use when building
         * @param typeId to use when building
         */
        private Builder(String namespace, String name, String returnType, String typeId) {
        	setNamespace(namespace);
        	setName(name);
        	setReturnType(returnType);
        	setTypeId(typeId);
        	setActive(true);
        	setParameters(new ArrayList<FunctionParameterDefinition.Builder>());
            setCategories(new ArrayList<CategoryDefinition.Builder>());
        }
        
        /**
         * Creates a function definition builder with the given required values.  This builder
         * is the only means by which a {@link FunctionDefinition} object should be created.
         * 
         * <p>Will default the active flag to true.
         * 
         * @param namespace the namespace of the function definition to create, must not be null or blank
         * @param name the name of the function definition to create, must not be null or blank
         * @param returnType the return type of the function definition to create, must not be null or blank
         * @param typeId the return type id of the function definition to create, must not be null or blank
         * 
         * @return a builder with the required values already initialized
         * 
         * @throws IllegalArgumentException if any of the given arguments is null or blank
         */
        public static Builder create(String namespace, String name, String returnType, String typeId) {
        	return new Builder(namespace, name, returnType, typeId);
        }
        
        /**
         * Creates and populates a builder with the data on the given {@link FunctionDefinitionContract}.
         * This is similar in nature to a "copy constructor" for {@link FunctionDefinition}.
         * 
         * @param contract an object implementing the {@link FunctionDefinitionContract} from which
         * to copy property values
         *  
         * @return a builder with the values from the contract already initialized
         * 
         * @throws IllegalArgumentException if the given contract is null
         */
        public static Builder create(FunctionDefinitionContract contract) {
        	if (contract == null) {
        		throw new IllegalArgumentException("contract was null");
        	}
        	Builder builder = create(contract.getNamespace(), contract.getName(), contract.getReturnType(), contract.getTypeId());
        	builder.setId(contract.getId());
        	builder.setDescription(contract.getDescription());
        	builder.setActive(contract.isActive());
        	builder.setVersionNumber(contract.getVersionNumber());
        	for (FunctionParameterDefinitionContract parameter : contract.getParameters()) {
        		builder.getParameters().add(FunctionParameterDefinition.Builder.create(parameter));
        	}
            for (CategoryDefinitionContract category : contract.getCategories()) {
                builder.getCategories().add(CategoryDefinition.Builder.create(category));
            }
        	return builder;
        }

        @Override
        public FunctionDefinition build() {
        	return new FunctionDefinition(this);
        }
        
        @Override
		public String getId() {
			return this.id;
		}

        /**
         * Sets the id for the function definition that will be returned by this builder.
         * 
         * @param id the function definition id to set
         */
		public void setId(String id) {
			this.id = id;
		}

		@Override
		public String getNamespace() {
			return this.namespace;
		}

        /**
         * Sets the namespace code for the function definition that will be returned by this builder.
         * The namespace must not be null or blank.
         * 
         * @param namespace the namespace code to set on this builder, must not be null or blank
         * 
         * @throws IllegalArgumentException if the given namespace is null or blank
         */
		public void setNamespace(String namespace) {
			if (StringUtils.isBlank(namespace)) {
				throw new IllegalArgumentException("namespace was blank");
			}
			this.namespace = namespace;
		}

		@Override
		public String getName() {
			return this.name;
		}

		/**
         * Sets the name for the function definition that will be returned by this builder.
         * The name must not be null or blank.
         * 
         * @param name the name to set on this builder, must not be null or blank
         * 
         * @throws IllegalArgumentException if the given name is null or blank
         */
		public void setName(String name) {
			if (StringUtils.isBlank(name)) {
				throw new IllegalArgumentException("name was blank");
			}
			this.name = name;
		}
		
		@Override
		public String getDescription() {
			return this.description;
		}

        /**
         * Sets the description for the function definition that will be returned by this builder.
         * 
         * @param description the description to set on this builder
         */
		public void setDescription(String description) {
			this.description = description;
		}

		@Override
		public String getReturnType() {
			return this.returnType;
		}

		/**
         * Sets the return type for the function definition that will be
         * returned by this builder.  This can be one of a set of "built-in"
         * data types or a custom datatype represented as a fully qualified
         * java class name.  The returnType must not be null or blank.
         * 
         * @param returnType the returnType to set on this builder, must not be null or blank
         * 
         * @throws IllegalArgumentException if the given returnType is null or blank
         */
		public void setReturnType(String returnType) {
			if (StringUtils.isBlank(returnType)) {
				throw new IllegalArgumentException("returnType was blank");
			}
			this.returnType = returnType;
		}

		@Override
		public String getTypeId() {
			return this.typeId;
		}

		/**
         * Sets the id of the {@link KrmsTypeDefinition} which defines the
         * actual implementation of this function.  The typeId must not be
         * null or blank.
         * 
         * @param typeId the typeId to set on this builder, must not be null or blank
         * 
         * @throws IllegalArgumentException if the given typeId is null or blank
         */
		public void setTypeId(String typeId) {
			if (StringUtils.isBlank(typeId)) {
				throw new IllegalArgumentException("typeId was blank");
			}
			this.typeId = typeId;
		}

		@Override
		public boolean isActive() {
			return this.active;
		}

		/**
         * Sets the active flag for the function definition that will be
         * returned by this builder.
         * 
         * @param active the active flag to set
         */
		public void setActive(boolean active) {
			this.active = active;
		}

		@Override
		public Long getVersionNumber() {
			return this.versionNumber;
		}

		/**
         * Sets the version number for the function definition that will be
         * returned by this builder.
         * 
         * <p>In general, this value should not be manually set on the builder,
         * but rather copied from an existing {@link FunctionDefinitionContract} when
         * invoking {@link Builder#create(FunctionDefinitionContract)}.
         * 
         * @param versionNumber the version number to set
         */
		public void setVersionNumber(Long versionNumber) {
			this.versionNumber = versionNumber;
		}
		
		@Override
		public List<FunctionParameterDefinition.Builder> getParameters() {
			return this.parameters;
		}

		/**
         * Sets the parameters for the function definition that will be returned by this builder.
         * This list is a list of builders for each of the {@link FunctionParameterDefinition}
         * instances that will form the parameters of this function definition.  The given list
         * must not be null.
         * 
         * @param parameters a list of builders for the parameters which will be specified on this function definition
         * 
         * @throws IllegalArgumentException if the given parameters list is null 
         */
		public void setParameters(List<FunctionParameterDefinition.Builder> parameters) {
			if (parameters == null) {
				throw new IllegalArgumentException("parameters was null");
			}
			this.parameters = parameters;
		}

        @Override
        public List<CategoryDefinition.Builder> getCategories() {
            return this.categories;
        }

        /**
         * Sets the category for the function definition that will be returned by this builder.
         * This list is a list of builders for each of the {@link CategoryDefinition}
         * instances that will form the categories of this function definition.  The given list
         * must not be null.
         *
         * @param categories a list of builders for the categories which will be specified on this function definition
         *
         * @throws IllegalArgumentException if the given categories list is null
         */
        public void setCategories(List<CategoryDefinition.Builder> categories) {
            if (categories == null) {
                throw new IllegalArgumentException("categories was null");
            }
            this.categories = categories;
        }

	}
	
	/**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "function";
        final static String TYPE_NAME = "FunctionType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        final static String ID = "id";
        final static String NAMESPACE = "namespace";
        final static String NAME = "name";
        final static String DESCRIPTION = "description";
        final static String RETURN_TYPE = "returnType";
        final static String TYPE_ID = "typeId";
        final static String ACTIVE = "active";
        final static String PARAMETERS = "parameters";
        final static String PARAMETER = "parameter";
        final static String CATEGORIES = "categories";
        final static String CATEGORY = "category";
    }
    
}
