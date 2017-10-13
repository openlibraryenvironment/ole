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

import java.util.Map;
import java.util.Set;

/**
 * The ExecutionEnvironment manages contextual information which is made available to
 * different components of the rules engine during execution.  Facts can be retrieved
 * from and published to the environment.  It also provides a reference to the
 * {@link EngineResults} or tracking engine activity and returning values back to
 * the client of the rules engine.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface ExecutionEnvironment {
	
	/**
	 * Returns the selection criteria that was used to initialize the environment.
	 * 
	 * @return the selection criteria for this environment
	 */
	public SelectionCriteria getSelectionCriteria();
	
	/**
	 * Returns an immutable Map of facts available within this environment.
	 * 
	 * @return the facts in this environment
	 */
	public Map<Term, Object> getFacts();

	/**
	 * Publishes a new fact
	 * 
	 * @param factName name of the fact to publish
	 * @param factValue value of the fact to publish
	 * // TODO: we don't support updating facts, refactor this method
	 * @return true if an existing fact was updated, false if this was a new fact
	 */
	public boolean publishFact(Term factName, Object factValue);

    /**
     * Add a {@link TermResolver}
     * @param termResolver
     */
	public void addTermResolver(TermResolver<?> termResolver);

    /**
     * Resolve
     * @param term {@link Term}
     * @param caller
     * @return <T> T
     * @throws {@link TermResolutionException}
     */
	public <T> T resolveTerm(Term term, Object caller) throws TermResolutionException;

    /**
     * Return a set of Term for the given value
     * @param caller
     * @return Set<Term>
     */
	public Set<Term> getTermsForCaller(Object caller);

    /**
     * Return the {@link ExecutionOptions}
     * @return {@link ExecutionOptions}
     */
	public ExecutionOptions getExecutionOptions();

    /**
     * Return the {@link EngineResults}
     * @return {@link EngineResults}
     */
	public EngineResults getEngineResults();
	
}
