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
package org.kuali.rice.core.api.criteria;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;

/**
 * An immutable predicate which represents an "is null" statement which is
 * evaluated the property defined by the property path on this predicate.
 * 
 * @see PredicateFactory for a convenient way to construct this class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = NullPredicate.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = NullPredicate.Constants.TYPE_NAME, propOrder = {
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class NullPredicate extends AbstractPredicate implements PropertyPathPredicate {
	
	private static final long serialVersionUID = 2397296074921454859L;
	
	@XmlAttribute(name = CriteriaSupportUtils.PropertyConstants.PROPERTY_PATH)
	private final String propertyPath;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

	/**
     * Should only be invoked by JAXB.
     */
    @SuppressWarnings("unused")
    private NullPredicate() {
        this.propertyPath = null;
    }
    
    /**
	 * Constructs a NullPredicate for the given propertyPath.
	 * 
	 * @param propertyPath the property path for the predicate, must not be null or blank
	 * 
	 * @throws IllegalArgumentException if the propertyPath is null or blank
	 */
    NullPredicate(String propertyPath) {
    	if (StringUtils.isBlank(propertyPath)) {
			throw new IllegalArgumentException("Property path cannot be null or blank.");
		}
		this.propertyPath = propertyPath;
    }

    @Override
    public String getPropertyPath() {
    	return propertyPath;
    }
        
	/**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "null";
        final static String TYPE_NAME = "NullType";
    }

    @Override
    public String toString() {
        return new StringBuilder(CriteriaSupportUtils.findDynName(this.getClass().getSimpleName()))
                .append("(").append(this.getPropertyPath()).append(")").toString();
    }
}
