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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.HashSet;

import org.apache.commons.lang.ObjectUtils;
import org.kuali.rice.krms.api.engine.EngineResults;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.engine.ExecutionOptions;
import org.kuali.rice.krms.api.engine.SelectionCriteria;
import org.kuali.rice.krms.api.engine.Term;
import org.kuali.rice.krms.api.engine.TermResolutionEngine;
import org.kuali.rice.krms.api.engine.TermResolutionException;
import org.kuali.rice.krms.api.engine.TermResolver;
import org.kuali.rice.krms.api.engine.ResultEvent;

/**
 * An implementation of {@link ExecutionEnvironment} given {@link SelectionCriteria}, facts (Map<{@link Term}, Object> ), {@link ExecutionOptions} and {@link TermResolutionEngine}.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class BasicExecutionEnvironment implements ExecutionEnvironment {

	private final SelectionCriteria selectionCriteria;
	private final Map<Term, Object> facts;
	private final ExecutionOptions executionOptions;
	private final EngineResults engineResults;
	private final TermResolutionEngine termResolutionEngine;
	private Map<Object, Set<Term>> termPropositionMap;

    /**
     * Constructor for a BasicExecutionEnvironment with the given {@link SelectionCriteria}, facts, {@link ExecutionOptions} and {@link TermResolutionEngine}
     * @param selectionCriteria to set selectionCriteria to, cannot be null
     * @param facts to set facts to, cannot be null
     * @param executionOptions to set executionOptions to
     * @param termResolutionEngine to set termResolutionEngine to
     * @throws IllegalArgumentException if the selectionCriteria or facts are null
     */
	public BasicExecutionEnvironment(SelectionCriteria selectionCriteria, Map<Term, Object> facts, ExecutionOptions executionOptions, TermResolutionEngine termResolutionEngine) {
		if (selectionCriteria == null) {
			throw new IllegalArgumentException("Selection criteria must not be null.");
		}
		if (facts == null) {
			throw new IllegalArgumentException("Facts must not be null.");
		}
		this.selectionCriteria = selectionCriteria;
		this.executionOptions = new ExecutionOptions(executionOptions);
		this.engineResults = new EngineResultsImpl();
				
		this.termResolutionEngine = new TermResolutionEngineImpl();
		
		// Add facts
		this.facts = new HashMap<Term, Object>(facts.size());
		this.facts.putAll(facts);
		
		for (Entry<Term, Object> factsEntry : facts.entrySet()) {
			this.termResolutionEngine.addTermValue(factsEntry.getKey(), factsEntry.getValue());
		}
	}
	
	@Override
	public SelectionCriteria getSelectionCriteria() {
		return this.selectionCriteria;
	}
	
	@Override
	public Map<Term, Object> getFacts() {
		return Collections.unmodifiableMap(facts);
	}
	
	@Override
	public void addTermResolver(TermResolver<?> termResolver) {
		termResolutionEngine.addTermResolver(termResolver);
	}
	
	@Override
	public <T> T resolveTerm(Term term, Object caller) throws TermResolutionException {
		T value;
		
		// This looks funny, but works around a javac bug: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6302954
		// Specifically, using <T> below works around it.
		value = termResolutionEngine.<T>resolveTerm(term);
		
		if (caller != null) {
		    if(termPropositionMap == null) {
		        termPropositionMap = new HashMap<Object, Set<Term>>();
		    }

		    // update the Proposition-Term mapping
		    if(termPropositionMap.containsKey(caller)) {
		        termPropositionMap.get(caller).add(term);
		    } else {
		        termPropositionMap.put(caller, new HashSet<Term>());
		        termPropositionMap.get(caller).add(term);
		    }
		}
		
		publishFact(term, value);		
		
		return value;
	}

    @Override
	public Set<Term> getTermsForCaller(Object caller) {
		return termPropositionMap.get(caller);
	}
	
	@Override
	public boolean publishFact(Term factName, Object factValue) {
		if (facts.containsKey(factName) && ObjectUtils.equals(facts.get(factName), factValue)) {
			return false;
		}
		facts.put(factName, factValue);
		termResolutionEngine.addTermValue(factName, factValue);
		return true;
	}

	@Override
	public ExecutionOptions getExecutionOptions() {
		return executionOptions;
	}
	
	@Override
	public EngineResults getEngineResults() {
		return engineResults;
	}
	

}
