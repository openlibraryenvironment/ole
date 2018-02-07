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
package org.kuali.rice.kew.api.document.attribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.kuali.rice.kew.api.document.PropertyDefinition;
import org.w3c.dom.Element;

/**
 * Encapsulates parameters that can be sent to an attribute when using that attribute to perform various operations
 * (primarily, in the case of workflow attributes, during the generation of XML).
 *
 * The distinction between parameters and properties is that parameters are used to initially construct the attribute
 * (via the attribute class constructor), while property definitions are used to set properties on the attribute thereafter.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = WorkflowAttributeDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = WorkflowAttributeDefinition.Constants.TYPE_NAME, propOrder = {
		WorkflowAttributeDefinition.Elements.ATTRIBUTE_NAME,
		WorkflowAttributeDefinition.Elements.PARAMETERS,
		WorkflowAttributeDefinition.Elements.PROPERTY_DEFINITIONS,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class WorkflowAttributeDefinition extends AbstractDataTransferObject {
    
	@XmlElement(name = Elements.ATTRIBUTE_NAME, required = true)
    private final String attributeName;
	
	@XmlElementWrapper(name = Elements.PARAMETERS, required = false)
	@XmlElement(name = Elements.PARAMETER, required = false)
    private final List<String> parameters;
	
	@XmlElementWrapper(name = Elements.PROPERTY_DEFINITIONS, required = false)
	@XmlElement(name = Elements.PROPERTY_DEFINITION, required = false)
    private final List<PropertyDefinition> propertyDefinitions;
    
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;
	
	/**
     * Private constructor used only by JAXB.
     */
	private WorkflowAttributeDefinition() {
	    this.attributeName = null;
	    this.parameters = null;
	    this.propertyDefinitions = null;
	}
	
	private WorkflowAttributeDefinition(Builder builder) {
		this.attributeName = builder.getAttributeName();
		if (builder.getParameters() == null) {
			this.parameters = Collections.emptyList();
		} else {
			this.parameters = new ArrayList<String>(builder.getParameters());
		}
		if (builder.getPropertyDefinitions() == null) {
			this.propertyDefinitions = Collections.emptyList();
		} else {
			this.propertyDefinitions = new ArrayList<PropertyDefinition>(builder.getPropertyDefinitions());
		}
	}

    /**
     * Returns the name of the attribute for this workflow attribute definition.  Should never be a null or blank value.
     *
     * @return the name of the attribute for this workflow attribute definition
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Returns an unmodifiable list of parameters that will be used to construct the attribute as a list of string
     * values.  This list will never be null but it may be empty.
     *
     * @return the list of parameters used to construct the attribute
     */
    public List<String> getParameters() {
    	return Collections.unmodifiableList(parameters);
    }

    /**
     * Returns an unmodifiable list of property names and values that will be passed to the attribute upon construction.
     * This list will never be null but it may be empty.
     *
     * @return the list of property names and values to will be be passed to the attribute upon construction
     */
    public List<PropertyDefinition> getPropertyDefinitions() {
    	return Collections.unmodifiableList(propertyDefinitions);
    }

    /**
     * Returns the property definitions on this attribute definition as a map of strings instead of a list of
     * {@code PropertyDefinition} objects.
     *
     * @return a map representation of the property definitions on this workflow attribute definition
     */
    public Map<String, String> getPropertyDefinitionsAsMap() {
        Map<String, String> propertiesDefinitionsMap = new HashMap<String, String>();
        for (PropertyDefinition propertyDefinition : getPropertyDefinitions()) {
            propertiesDefinitionsMap.put(propertyDefinition.getName(), propertyDefinition.getValue());
        }
        return Collections.unmodifiableMap(propertiesDefinitionsMap);
    }

    /**
     * A builder which can be used to construct instances of {@code WorkflowAttributeDefinition}.
     */
    public final static class Builder implements Serializable, ModelBuilder {

		private static final long serialVersionUID = 7549637048594326790L;

		private String attributeName;
		private List<String> parameters;
		private List<PropertyDefinition> propertyDefinitions;

		private Builder(String attributeName) {
			setAttributeName(attributeName);
			setParameters(new ArrayList<String>());
			setPropertyDefinitions(new ArrayList<PropertyDefinition>());
		}
		
		private Builder(WorkflowAttributeDefinition definition) {
		    setAttributeName(definition.getAttributeName());
		    setParameters(definition.getParameters());
		    setPropertyDefinitions(definition.getPropertyDefinitions());
		}

        /**
         * Creates a new builder copying the properties from the given definition into it.
         *
         * @param definition the definition from which to copy properties
         * @return a builder initialized with the properties copied from the given definition
         */
		public static Builder create(WorkflowAttributeDefinition definition) {
		    if (definition == null) {
		        throw new IllegalArgumentException("definition was null");
		    }
		    return new Builder(definition);
		}

        /**
         * Constructs a builder which is initialized with the given attribute name.
         *
         * @param attributeName the attribute name to use when initializing this builder, cannot be a null or empty
         * value
         *
         * @return an instance of a builder initialized with the given attribute name
         *
         * @throws IllegalArgumentException if {@code attributeName} is a null or blank value
         */
		public static Builder create(String attributeName) {
			return new Builder(attributeName);
			
		}

        @Override
		public WorkflowAttributeDefinition build() {
			return new WorkflowAttributeDefinition(this);
		}

        /**
         * Returns the attribute name that is set on this builder.
         *
         * @return the attribute name this is set on this builder
         */
		public String getAttributeName() {
			return attributeName;
		}

        /**
         * Returns a list of string parameters that have been set on this builder.
         *
         * @return a list of string parameters that have been set on this builder
         */
		public List<String> getParameters() {
        	return parameters;
		}

        /**
         * Returns a list of {@code PropertyDefinition} objects that have been set on this builder.
         *
         * @return a list of property definitions that have been set on this builder
         */
		public List<PropertyDefinition> getPropertyDefinitions() {
			return propertyDefinitions;
		}

        /**
         * Sets the attribute name on this builder to the given value.  Must not be a null or blank value.
         *
         * @param attributeName the value of the attributeName to set
         * @throws IllegalArgumentException if {@code attributeName} is a null or blank value
         */
		public void setAttributeName(String attributeName) {
			if (StringUtils.isBlank(attributeName)) {
				throw new IllegalArgumentException("attributeName was null or blank");
			}
			this.attributeName = attributeName;
		}

        /**
         * Adds a parameter to the list of parameters maintained by this builder.
         *
         * @param parameter the parameter value to add
         */
		public void addParameter(String parameter) {
			parameters.add(parameter);
		}

        /**
         * Removes a parameter with the given value from the list of parameters maintained by this builder.
         *
         * @param parameter the parameter value to remove
         */
		public void removeParameter(String parameter) {
			parameters.remove(parameter);
		}

        /**
         * Sets the list of parameters on this builder.
         *
         * @param parameters the list of parameters to set
         */
		public void setParameters(List<String> parameters) {
			this.parameters = new ArrayList<String>(parameters);
		}

        /**
         * Adds the given property definition to the list of property definitions maintained by this builder.
         *
         * @param propertyDefinition the property definition to set, should not be null
         * @throws IllegalArgumentException if the given property definition is null
         */
		public void addPropertyDefinition(PropertyDefinition propertyDefinition) {
			if (propertyDefinition == null) {
				throw new IllegalArgumentException("propertyDefinition must be non-null.");
			}
			propertyDefinitions.add(propertyDefinition);
		}

        /**
         * Sets the list of property definitions maintained by this build to the given list.
         *
         * @param propertyDefinitions the list of property definitions to set
         */
		public void setPropertyDefinitions(List<PropertyDefinition> propertyDefinitions) {
			if (propertyDefinitions == null) {
				setPropertyDefinitions(new ArrayList<PropertyDefinition>());
			}
			this.propertyDefinitions = new ArrayList<PropertyDefinition>(propertyDefinitions);
			
		}

        /**
         * Add a property definition constructed from the given name and value to the list of property definitions
         * on this builder.
         *
         * @param name name of the property definition to add, must not be a null or blank value
         * @param value value of the property definition to add
         *
         * @throws IllegalArgumentException if the given name is a null or blank value
         */
		public void addPropertyDefinition(String name, String value) {
			addPropertyDefinition(PropertyDefinition.create(name, value));
		}

        /**
         * Returns the property definition on this build which has the given name if it exists.  This method will return
         * a null value if a definition with the given name cannot be found.
         *
         * @param name the name of the property definition to retrieve
         *
         * @return the property definition with the given name, or null if no such property definition is found
         *
         * @throws IllegalArgumentException if the given name is a null or blank value
         */
	    public PropertyDefinition getPropertyDefinition(String name) {
	        if (StringUtils.isBlank(name)) {
	            throw new IllegalArgumentException("name was null or blank");
	        }
	        for (PropertyDefinition propertyDefinition : propertyDefinitions) {
	            if (propertyDefinition.equals(name)) {
	                return propertyDefinition;
	            }
	        }
	        return null;
	    }

		
    }
    
    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "workflowAttributeDefinition";
        final static String TYPE_NAME = "WorkflowAttributeDefinitionType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String ATTRIBUTE_NAME = "attributeName";
        final static String PARAMETERS = "parameters";
        final static String PARAMETER = "parameter";
        final static String PROPERTY_DEFINITIONS = "propertyDefinitions";
        final static String PROPERTY_DEFINITION = "propertyDefinition";
    }
    
}
