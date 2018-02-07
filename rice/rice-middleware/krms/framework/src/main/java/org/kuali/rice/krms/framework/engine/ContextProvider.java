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

import java.util.Map;

import org.kuali.rice.krms.api.engine.ExecutionOptions;
import org.kuali.rice.krms.api.engine.SelectionCriteria;
import org.kuali.rice.krms.api.engine.Term;

/**
 * Loads a {@link Context} for the given set of criteria.  Applications who
 * want to provide their own means for creating a context and supplying it to
 * the KRMS engine can do so by implementing a custom ContextProvider. 
 * 
 * @see Context
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface ContextProvider {

	/**
	 * Loads the context for the given selection criteria, facts, and execution
	 * options.  If no context can be located based on the given criteria, then
	 * this method should return null.
	 * 
	 * <p>In the case where multiple Contexts could potentially be identified
	 * from the same set of selection criteria, it is up to the implementer
	 * of the ContextProvider to ensure that the most appropriate Context is
	 * returned.  Or alternatively, an exception could be thrown indicating
	 * context ambiguity.
	 * 
	 * <p>The sectionCriteria, facts, and executionOptions which are passed to
	 * this method should never be null.  However, they might be empty.
	 * 
	 * @param selectionCriteria the criteria to use during the selection phase of engine operation
	 * @param facts the set of facts that are supplied to the engine at execution time
	 * @param executionOptions a collection of options that can be used to customize engine execution behavior
	 * 
	 * @return the context which matches the given criteria, or null if no context matches
	 */
	public Context loadContext(SelectionCriteria selectionCriteria, Map<Term, Object> facts, ExecutionOptions executionOptions);
	
}
