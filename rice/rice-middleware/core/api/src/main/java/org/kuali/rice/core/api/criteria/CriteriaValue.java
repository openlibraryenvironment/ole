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

/**
 * A wrapper for values that are used on a {@link Criteria}.  This wrapper
 * allows for single parameterized type for all criteria values and
 * aids primarily in mapping these values to a message format for use on a
 * remotable service (i.e. with marhaling to XML using JAXB)
 * 
 * <p>Has a single method {@link #getValue()} which simply returns the
 * wrapped value.
 * 
 * @param <T> the type of the value stored by this CriteriaValue
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface CriteriaValue<T> {

	/**
	 * Returns the stored value.  This value should never be null.
	 * 
	 * @return the stored value, should never be null
	 */
    T getValue();
    
}
