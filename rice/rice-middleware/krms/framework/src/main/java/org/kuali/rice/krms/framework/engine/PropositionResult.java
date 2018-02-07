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
package org.kuali.rice.krms.framework.engine;

import java.util.Collections;
import java.util.Map;

/**
 * PropositionResults are returned by {@link Proposition}'s evaluate method.
 * @see Proposition evaluate
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PropositionResult {

	final boolean result;
	Map<String,?> executionDetails;

    /**
     * Create a PropositionResult with the given result
     * @param result to set the result to
     */
	public PropositionResult(boolean result) {
	    this(result, null);
	}

    /**
     * Create a PropositionResult with the given values
     * @param result to set the result to
     * @param executionDetails to set executionDetails to
     */
	public PropositionResult(boolean result, Map<String,?> executionDetails) {
		this.result = result;
		
		if (executionDetails == null) {
		    this.executionDetails = Collections.emptyMap();
		} else {
		    this.executionDetails = Collections.unmodifiableMap(executionDetails);
		}
	}

    /**
     * Returns the result.
     * @return the result
     */
	public boolean getResult() {
		return result;
	}

    /**
     * Returns the executionDetails
     * @return the executionDetails
     */
	public Map<String,?> getExecutionDetails() {
		return executionDetails;
	}
		
}