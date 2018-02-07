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
package org.kuali.rice.krms.api.repository.proposition;


import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.krms.api.repository.term.TermDefinition;

/**
 * The contract for {@link PropositionParameter}
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface PropositionParameterContract extends Identifiable, Versioned {
	/**
	 * Returns the Id for the parent proposition.
	 *
	 * <p>
	 * It is the Id of the parent proposition.
	 * </p>
	 * @return Id for parent parameter.
	 */
	public String getPropId();

	
	/**
	 * Returns the value of the proposition parameter
	 *
	 * <p>
	 * It is the value of the parameter
	 * </p>
	 * @return value of the parameter
	 */
	public String getValue();

        
	/**
	 * Returns the term value of the proposition parameter if the 
         * proposition parameter is a term.
	 *
	 * <p>
	 * It is the term value of the parameter
	 * </p>
	 * @return value of the term parameter
	 */
	public TermDefinition getTermValue();
        
	/**
	 * Returns the type of the parameter.
	 * Proposition parameters are one of the following types:
	 *    Constant Values:  numbers, strings, dates, etc.
	 *    Terms: data available in the execution environment or provided by a term resolver
	 *    Functions: custom functions that resolve to a value, 
	 *    	or standard operators (equals, greater than, less than, ...)
	 *
	 * <p>
	 * It identified the type of the parameter.
	 * </p>
	 * @return the parameter type code. Valid values are C, T, and F.
	 */
	public String getParameterType();

	/**
	 * Returns the sequence number of the proposition parameter.
	 * Proposition parameters are listed in Reverse Polish Notation.
	 * The sequence number (starting with 1) identifies the position of the 
	 * parameter in the list.
	 * 
	 * @return the sequence number of the proposition parameter
	 */
	public Integer getSequenceNumber();
}
