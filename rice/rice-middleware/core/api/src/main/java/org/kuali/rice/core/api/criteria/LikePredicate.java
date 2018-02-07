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

/**
 * An immutable predicate which represents a "like" statement which is
 * evaluated the {@link CriteriaValue} of this predicate.  The criteria
 * value for a like predicate should support wildcards using "*" for multiple
 * values and "?" for a single value.
 * 
 * @see PredicateFactory for a convenient way to construct this class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = LikePredicate.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = LikePredicate.Constants.TYPE_NAME, propOrder = {
    CriteriaSupportUtils.PropertyConstants.VALUE,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class LikePredicate extends AbstractPredicate implements SingleValuedPredicate {

	private static final long serialVersionUID = 6406122080039813800L;
	
	@XmlAttribute(name = CriteriaSupportUtils.PropertyConstants.PROPERTY_PATH)
	private final String propertyPath;

	@XmlElements(value = {
    		@XmlElement(name = CriteriaStringValue.Constants.ROOT_ELEMENT_NAME, type = CriteriaStringValue.class, required = true)
    })
	private final CriteriaValue<?> value;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Should only be invoked by JAXB.
     */
    @SuppressWarnings("unused")
    private LikePredicate() {
        this.propertyPath = null;
        this.value = null;
    }
    
    /**
	 * Constructs a LikePredicate for the given path and value.  LikePredicate supports only the
	 * {@link CriteriaStringValue}.
	 * 
	 * @param propertyPath the property path for the predicate, must not be null or blank
	 * @param value the value to evaluation the path against, must not be null.
	 * 
	 * @throws IllegalArgumentException if the propertyPath is null or blank
	 * @throws IllegalArgumentException if the value is null
	 * @throws IllegalArgumentException if this predicate does not support the given type of {@link CriteriaValue}
	 */
    LikePredicate(String propertyPath, CriteriaValue<?> value) {
    	CriteriaSupportUtils.validateValuedConstruction(getClass(), propertyPath, value);
		this.propertyPath = propertyPath;
		this.value = value;
    }
    
    @Override
    public String getPropertyPath() {
    	return propertyPath;
    }
    
	@Override
	public CriteriaValue<?> getValue() {
		return value;
	}

	/**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "like";
        final static String TYPE_NAME = "LikeType";
    }

    @Override
    public String toString() {
        return CriteriaSupportUtils.toString(this);
    }
}
