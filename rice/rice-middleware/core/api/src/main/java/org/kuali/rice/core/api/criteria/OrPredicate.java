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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.Set;

/**
 * An immutable composite predicate which implements "or-ing" of multiple
 * predicates together.
 * 
 * @see PredicateFactory for a convenient way to construct this class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = OrPredicate.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = OrPredicate.Constants.TYPE_NAME, propOrder = {
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class OrPredicate extends AbstractCompositePredicate {
	
	private static final long serialVersionUID = -6575256900578172242L;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

	/**
	 * Used only by JAXB for construction.
	 */
	@SuppressWarnings("unused")
	private OrPredicate() {
		super();
	}
	
	/**
	 * Construct an "Or" predicate from the given list of predicates.  The given list
	 * of predicates can be null or empty.  If the list is null then it will be
	 * translated internally to an empty list.
	 * 
	 * @param predicates the List of predicates to set on the And predicate
	 */
	OrPredicate(Set<Predicate> predicates) {
	    //don't worry about defensive copy of list here - super class takes care of it.
        super(predicates);
	}
	
	/**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "or";
        final static String TYPE_NAME = "OrType";
    }

}
