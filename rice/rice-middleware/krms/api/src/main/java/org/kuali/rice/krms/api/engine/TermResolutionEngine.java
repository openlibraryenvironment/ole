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
package org.kuali.rice.krms.api.engine;

/**
 * Interface for the engine that is used to resolve {@link Term}s.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface TermResolutionEngine {

	/**
	 * Resolves a given term into a fact
	 * @param term the {@link Term} to resolve
	 * @return the fact value for the given {@link Term}
	 * @throws {@link TermResolutionException} if the given {@link Term} can't be resolved
	 */
	<T> T resolveTerm(Term term) throws TermResolutionException;
	
	/**
	 * Adds a fact value to the {@link TermResolutionEngine}'s internal state
	 * @param term the named Term
	 * @param value the fact value
	 */
	void addTermValue(Term term, Object value);
	
	/**
	 * Adds a {@link TermResolver} to the {@link TermResolutionEngine}.  Once added, it may
	 * be used (unsurprisingly) by the engine to resolve {@link Term}s.
	 * @param termResolver the {@link TermResolver} to add.
	 */
	void addTermResolver(TermResolver<?> termResolver);
	
}
