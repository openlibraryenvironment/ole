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
import org.w3c.dom.Element;

/**
 * An immutable representation of a function parameter definition.
 * 
 * @see FunctionParameterDefinitionContract
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = FunctionParameterDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = FunctionParameterDefinition.Constants.TYPE_NAME, propOrder = {
		FunctionParameterDefinition.Elements.ID,
		FunctionParameterDefinition.Elements.NAME,
		FunctionParameterDefinition.Elements.DESCRIPTION,
		FunctionParameterDefinition.Elements.PARAMETER_TYPE,
		FunctionParameterDefinition.Elements.SEQUENCE,
		FunctionParameterDefinition.Elements.FUNCTION_ID,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class FunctionParameterDefinition extends AbstractDataTransferObject implements FunctionParameterDefinitionContract {

	private static final long serialVersionUID = 1391030685309770560L;

	@XmlElement(name = Elements.ID, required = false)
	private final String id;
		
	@XmlElement(name = Elements.NAME, required = true)
	private final String name;
	
	@XmlElement(name = Elements.DESCRIPTION, required = false)
	private final String description;
	
	@XmlElement(name = Elements.TYPE, required = true)
	private final String parameterType;
	
	@XmlElement(name = Elements.FUNCTION_ID, required = true)
	private final String functionId;
	
	@XmlElement(name = Elements.SEQUENCE, required=true)
	private Integer sequenceNumber;

	@XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
	private final Long versionNumber;
	
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;
	
	/**
     * Private constructor used only by JAXB.
     */
    private FunctionParameterDefinition() {
    	this.id = null;
    	this.name = null;
    	this.description = null;
    	this.parameterType = null;
    	this.functionId = null;
    	this.sequenceNumber = null;
    	this.versionNumber = null;
    }

    /**
     * Constructs a FunctionParameterDefinition from the given builder.  This constructor is private and should only
     * ever be invoked from the builder.
     *
     * @param builder the Builder from which to construct the FunctionParameterDefinition
     */
    private FunctionParameterDefinition(Builder builder) {
    	this.id = builder.getId();
    	this.name = builder.getName();
    	this.description = builder.getDescription();
    	this.parameterType = builder.getParameterType();
    	this.functionId = builder.getFunctionId();
    	this.sequenceNumber = builder.getSequenceNumber();
    	this.versionNumber = builder.getVersionNumber();
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
	public String getParameterType() {
		return parameterType;
	}
	
	@Override
	public String getFunctionId() {
		return functionId;
	}
	
	@Override
	public Long getVersionNumber() {
		return versionNumber;
	}

	@Override
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * A builder which can be used to construct {@link FunctionParameterDefinition}
	 * instances.  Enforces the constraints of the {@link FunctionParameterDefinitionContract}.
	 * 
	 * @author Kuali Rice Team (rice.collab@kuali.org)
	 *
	 */
	public static final class Builder implements FunctionParameterDefinitionContract, ModelBuilder, Serializable  {
    	    	
    	private static final long serialVersionUID = -4470376239998290245L;
    	
		private String id;
    	private String name;
    	private String description;
    	private String functionId;
    	private String parameterType;
    	private Integer sequenceNumber;
    	private Long versionNumber;

        /**
         * Private constructor use the create method.
         * @param name to use when building
         * @param type to use when building
         * @param sequenceNumber to use when building
         */
        private Builder(String name, String type, Integer sequenceNumber) {
        	setName(name);
        	setParameterType(type);
        	setSequenceNumber(sequenceNumber);
        }
        
        /**
         * Creates a function parameter definition builder with the given required values.  This builder
         * is the only means by which a {@link FunctionParameterDefinition} object should be created.
         * 
         * @param name the name of the function parameter definition to create, must not be null or blank
         * @param type the type of the function parameter definition to create, must not be null or blank
         * 
         * @return a builder with the required values already initialized
         * 
         * @throws IllegalArgumentException if any of the given arguments is null or blank
         */
        public static Builder create(String name, String type, Integer sequenceNumber) {
        	return new Builder(name, type, sequenceNumber);
        }
        
        /**
         * Creates and populates a builder with the data on the given {@link FunctionParameterDefinitionContract}.
         * This is similar in nature to a "copy constructor" for {@link FunctionParameterDefinition}.
         * 
         * @param contract an object implementing the {@link FunctionParameterDefinitionContract} from which
         * to copy property values
         *  
         * @return a builder with the values from the contract already initialized
         * 
         * @throws IllegalArgumentException if the given contract is null
         */
        public static Builder create(FunctionParameterDefinitionContract contract) {
        	if (contract == null) {
        		throw new IllegalArgumentException("contract was null");
        	}
        	Builder builder = create(contract.getName(), contract.getParameterType(), contract.getSequenceNumber());
        	builder.setId(contract.getId());
        	builder.setDescription(contract.getDescription());
        	builder.setParameterType(contract.getParameterType());
        	builder.setFunctionId(contract.getFunctionId());
        	builder.setVersionNumber(contract.getVersionNumber());
        	return builder;
        }

        @Override
        public FunctionParameterDefinition build() {
        	return new FunctionParameterDefinition(this);
        }
        
        @Override
		public String getId() {
			return this.id;
		}

        /**
         * Sets the id for the function parameter definition that will be returned by this builder.
         * 
         * @param id the function parameter definition id to set
         */
		public void setId(String id) {
			this.id = id;
		}

		@Override
		public String getName() {
			return this.name;
		}

		/**
         * Sets the name for the function parameter definition that will be returned by this builder.
         * The name must not be null or blank.
         * 
         * @param name the name to set on this builder, must not be null or blank
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
         * Sets the description for the function parameter definition that will be returned by this builder.
         * 
         * @param description the description to set on this builder
         */
		public void setDescription(String description) {
			this.description = description;
		}

		@Override
		public String getParameterType() {
			return this.parameterType;
		}

		/**
         * Sets the type for the function parameter definition that will be
         * returned by this builder.  This can be one of a set of "built-in"
         * data types or a custom datatype represented as a fully qualified
         * java class name.  The type must not be null or blank.
         * 
         * @param type the type to set on this builder, must not be null or blank
         */
		public void setParameterType(String type) {
			if (StringUtils.isBlank(type)) {
				throw new IllegalArgumentException("type was blank");
			}
			this.parameterType = type;
		}

		@Override
		public String getFunctionId() {
			return this.functionId;
		}

		/**
         * Sets the type for the function id
         * If provided, the function id must be non-blank.
         * Must allow id to be null, to prevent chicken/egg problems.
         * 
         * @param functionId the functionId to set on this builder, must be either null or non-blank
         */
		public void setFunctionId(String functionId) {
			if (functionId != null && StringUtils.isBlank(functionId)) {
				throw new IllegalArgumentException("functionId must be null or non-blank");
			}
			this.functionId = functionId;
		}

		@Override
		public Integer getSequenceNumber() {
			return this.sequenceNumber;
		}

		/**
         * Sets the sequence number for the function parameter definition that
         * will be returned by this builder. This is the position in the functions
         * parameter list.
         * 
         * @param sequenceNumber the position of the parameter in the function parameter list
         */
		public void setSequenceNumber(Integer sequenceNumber) {
			this.sequenceNumber = sequenceNumber;
		}

		@Override
		public Long getVersionNumber() {
			return this.versionNumber;
		}

		/**
         * Sets the version number for the function parameter definition that
         * will be returned by this builder.
         * 
         * <p>In general, this value should not be manually set on the builder,
         * but rather copied from an existing {@link FunctionParameterDefinitionContract} when
         * invoking {@link Builder#create(FunctionParameterDefinitionContract)}.
         * 
         * @param versionNumber the version number to set
         */
		public void setVersionNumber(Long versionNumber) {
			this.versionNumber = versionNumber;
		}

	}
	
	/**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "functionParameter";
        final static String TYPE_NAME = "FunctionParameterType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        final static String ID = "id";
        final static String NAME = "name";
        final static String DESCRIPTION = "description";
        final static String PARAMETER_TYPE = "parameterType";
        final static String TYPE = "type";
        final static String FUNCTION_ID = "functionId";
		final static String SEQUENCE = "sequenceNumber";
    }
    
}
