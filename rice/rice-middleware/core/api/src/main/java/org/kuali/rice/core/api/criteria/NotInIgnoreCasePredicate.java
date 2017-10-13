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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An immutable predicate which represents a "not in" statement which is
 * evaluated against a list of values.
 * 
 * @see org.kuali.rice.core.api.criteria.PredicateFactory for a convenient way to construct this class.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = NotInIgnoreCasePredicate.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = NotInIgnoreCasePredicate.Constants.TYPE_NAME, propOrder = {
    CriteriaSupportUtils.PropertyConstants.VALUES,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class NotInIgnoreCasePredicate extends AbstractPredicate implements MultiValuedPredicate {

	private static final long serialVersionUID = -7676442296587603655L;

	@XmlAttribute(name = CriteriaSupportUtils.PropertyConstants.PROPERTY_PATH)
	private final String propertyPath;

	@XmlElements(value = {
            @XmlElement(name = CriteriaStringValue.Constants.ROOT_ELEMENT_NAME, type = CriteriaStringValue.class, required = true)
	})
	private final Set<CriteriaStringValue> values;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

	/**
     * Should only be invoked by JAXB.
     */
    @SuppressWarnings("unused")
    private NotInIgnoreCasePredicate() {
        this.propertyPath = null;
        this.values = null;
    }

    /**
	 * Constructs a NotInPredicate for the given propertyPath and list of criteria values.
	 *
	 * @param propertyPath the property path for the predicate, must not be null or blank
	 * @param values the list of criteria values to use for this predicate, must be non-null,
	 * non-empty.
	 *
	 * @throws IllegalArgumentException if the propertyPath is null or blank
	 * @throws IllegalArgumentException if the list of values is null, empty
	 */
    NotInIgnoreCasePredicate(String propertyPath, Set<CriteriaStringValue> values) {
    	if (StringUtils.isBlank(propertyPath)) {
			throw new IllegalArgumentException("Property path cannot be null or blank.");
		}
		this.propertyPath = propertyPath;

        if (values == null) {
            this.values = Collections.emptySet();
        } else {
            final Set<CriteriaStringValue> temp = new HashSet<CriteriaStringValue>();
            for (CriteriaStringValue value: values) {
                if (value != null) {
                    temp.add(value);
                }
            }
            this.values = Collections.unmodifiableSet(temp);
        }
    }

    @Override
    public String getPropertyPath() {
    	return propertyPath;
    }
    
    @Override
    public Set<CriteriaStringValue> getValues() {
    	return Collections.unmodifiableSet(values);
    }
        
	/**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "notInIgnoreCase";
        final static String TYPE_NAME = "NotInIgnoreCaseType";
    }

    @Override
    public String toString() {
        return CriteriaSupportUtils.toString(this);
    }
}
