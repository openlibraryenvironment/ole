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
package org.kuali.rice.kns.lookup;

import java.util.Collection;
import java.util.Set;

import org.kuali.rice.krad.bo.BusinessObject;

/**
 * Contract for strategies which can help LokoupResultsService with aspects (mainly id generation and result lookup) of multi value lookup support 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface LookupResultsSupportStrategyService {

	/**
	 * Returns a list of BOs that were selected.
	 * 
	 * This implementation makes an attempt to retrieve all BOs with the given object IDs, unless they have been deleted or the object ID changed.
	 * Since data may have changed since the search, the returned BOs may not match the criteria used to search.
	 * 
	 * @param lookupResultsSequenceNumber the sequence number identifying the lookup results in the database
	 * @param boClass the class of the business object to retrieve
	 * @param personId the id of the principal performing this search
	 * @param lookupResultsService an implementation of the lookupResultsService to do some of the dirty work...
	 * @return a Collection of retrieved BusinessObjects
	 * @throws Exception if anything goes wrong...well, just blow up, okay?
	 */
    public abstract <T extends BusinessObject> Collection<T> retrieveSelectedResultBOs(Class<T> boClass, Set<String> lookupIds) throws Exception;
    
    /**
     * Generates a String id which is used as an id on a checkbox for result rows returning the business object in a multiple value lookup
     * 
     * @param businessObject the lookup to generate an id for
     * @return the String id
     */
    public abstract String getLookupIdForBusinessObject(BusinessObject businessObject);
    
    /**
     * Determines if the given class is supported by this strategy
     * 
     * @param boClass the class to test the determination on
     * @return true if this strategy supports it, false otherwise
     */
    public abstract boolean qualifiesForStrategy(Class<? extends BusinessObject> boClass);
}
