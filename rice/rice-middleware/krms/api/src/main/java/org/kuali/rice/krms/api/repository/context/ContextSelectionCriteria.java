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
package org.kuali.rice.krms.api.repository.context;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A set of criteria for selecting a {@link ContextDefinition}.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = ContextSelectionCriteria.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = ContextSelectionCriteria.Constants.TYPE_NAME, propOrder = {
		ContextSelectionCriteria.Elements.NAMESPACE_CODE,
		ContextSelectionCriteria.Elements.NAME,
		ContextSelectionCriteria.Elements.CONTEXT_QUALIFIERS,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class ContextSelectionCriteria extends AbstractDataTransferObject {

	@XmlElement(name = Elements.NAMESPACE_CODE, required = true)
	private final String namespaceCode;
	
	@XmlElement(name = Elements.NAME, required = false)
	private final String name;
	
	@XmlElement(name = Elements.CONTEXT_QUALIFIERS)
	@XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
	private final Map<String, String> contextQualifiers;
	
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;
	
    /**
     * Only used by JAXB.
     */
    @SuppressWarnings("unused")
	private ContextSelectionCriteria() {
		this.namespaceCode = null;
		this.name = null;
		this.contextQualifiers = null;
	}

    /**
     * Private constructor used by factory methods
     */
    private ContextSelectionCriteria(String namespaceCode, String name, Map<String, String> contextQualifiers) {
		this.namespaceCode = namespaceCode;
		this.name = name;
		this.contextQualifiers = new HashMap<String, String>();
		if (contextQualifiers != null) {
			this.contextQualifiers.putAll(contextQualifiers);
		}
	}
	
    /**
     * Factory method returns a new context selection criteria object with the fields set to
     * the parameters provided.
     * @param namespaceCode the namespace of the context
     * @param name the name of the context
     * @param contextQualifiers a Map of name value pair strings representing the list of qualifiers
     * to use as selection criteria.
     */
	public static ContextSelectionCriteria newCriteria(String namespaceCode, String name, Map<String, String> contextQualifiers) {
		return new ContextSelectionCriteria(namespaceCode, name, contextQualifiers);
	}
	
    /**
     * Factory method returns a new context selection criteria object with the namespace and contextQualifiers fields
     * set to the parameters provided. The name field is set to null.
     * @param namespaceCode the namespace of the context
     * @param contextQualifiers a Map of name value pair strings representing the list of qualifiers
     * to use as selection criteria.
     */
	public static ContextSelectionCriteria newCriteria(String namespaceCode, Map<String, String> contextQualifiers) {
		return newCriteria(namespaceCode, null, contextQualifiers);
	}
	
    /**
    * Factory method returns a new context selection criteria object with the contextQualifiers property
    * set to the parameter provided. The name and namespace properties are set to null.
    * @param contextQualifiers a Map of name value pair strings representing the list of qualifiers
    * to use as selection criteria.
    */
	public static ContextSelectionCriteria newCriteria(Map<String, String> contextQualifiers) {
		return newCriteria(null, contextQualifiers);
	}

    /**
     * Returns the namespace of the context.
     * @return the namespace code of the context
     */
	public String getNamespaceCode() {
		return this.namespaceCode;
	}

    /**
     * Returns the name of the context
     * @return the name of the context
     */
	public String getName() {
		return this.name;
	}

    /**
     * Returns the list of qualifiers as a map to name, value pair strings.
     * @return a map containing the qualifier name, value pairs.
     */
	public Map<String, String> getContextQualifiers() {
		return this.contextQualifiers;
	}
	
    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "contextSelectionCriteria";
        final static String TYPE_NAME = "ContextSelectionCriteriaType";
    }
    
	/**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshaled to XML.
     */
    static class Elements {
        final static String NAMESPACE_CODE = "namespaceCode";
        final static String NAME = "name";
        final static String CONTEXT_QUALIFIERS = "contextQualifiers";
    }
	
}
