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

import org.joda.time.DateTime;
import org.kuali.rice.krms.api.engine.Engine;
import org.kuali.rice.krms.api.engine.EngineResults;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.engine.ExecutionOptions;
import org.kuali.rice.krms.api.engine.Facts;
import org.kuali.rice.krms.api.engine.ResultEvent;
import org.kuali.rice.krms.api.engine.SelectionCriteria;
import org.kuali.rice.krms.api.engine.Term;
import org.kuali.rice.krms.framework.engine.result.TimingResult;

/**
 * An implementation of {@link Engine}
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ProviderBasedEngine implements Engine {

	private static final Term effectiveExecutionTimeTerm = new Term("effectiveExecutionTime", null);
	
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ProviderBasedEngine.class);
	private static final ResultLogger KLog = ResultLogger.getInstance();

	private ContextProvider contextProvider;

    @Override
    public EngineResults execute(SelectionCriteria selectionCriteria, Map<String, Object> facts,
            ExecutionOptions executionOptions) {
        return execute(selectionCriteria,
                Facts.Builder.create().addFactsByName(facts).build(),
                executionOptions);
    }

    @Override
	public EngineResults execute(SelectionCriteria selectionCriteria, Facts facts, ExecutionOptions executionOptions) {
		DateTime start, end;
		start = new DateTime();
		ExecutionEnvironment environment = establishExecutionEnvironment(selectionCriteria, facts.getFactMap(), executionOptions);
		
		// set execution time
		Long effectiveExecutionTime = environment.getSelectionCriteria().getEffectiveExecutionTime();
		if (effectiveExecutionTime == null) { effectiveExecutionTime = System.currentTimeMillis(); }
		environment.publishFact(effectiveExecutionTimeTerm, effectiveExecutionTime);

		Context context = selectContext(selectionCriteria, facts.getFactMap(), executionOptions);
		if (context == null) {
			LOG.info("Failed to locate a Context for the given qualifiers, skipping rule engine execution: " + selectionCriteria.getContextQualifiers());
			return null;
		}
		context.execute(environment);
		end = new DateTime();
		if (KLog.isEnabled(environment)){
			KLog.logResult(new TimingResult(ResultEvent.TIMING_EVENT, this, environment, start, end));
		}
		return environment.getEngineResults();
	}

    /**
     * Return a {@link BasicExecutionEnvironment} using the given parameters
     * @param selectionCriteria {@link SelectionCriteria}
     * @param facts
     * @param executionOptions {@link ExecutionOptions}
     * @return {@link ExecutionEnvironment} created with the given parameters
     */
	protected ExecutionEnvironment establishExecutionEnvironment(SelectionCriteria selectionCriteria, Map<Term, Object> facts, ExecutionOptions executionOptions) {
		return new BasicExecutionEnvironment(selectionCriteria, facts, executionOptions, new TermResolutionEngineImpl());
	}

    /**
     * Load a Context from the contextProvider using the given parameters
     * @see ContextProvider loadContext
     * @param selectionCriteria
     * @param facts
     * @param executionOptions
     * @return {@link Context}
     * @throws IllegalStateException if the contextProvider is null;
     */
	protected Context selectContext(SelectionCriteria selectionCriteria, Map<Term, Object> facts, ExecutionOptions executionOptions) {
		if (contextProvider == null) {
			throw new IllegalStateException("No ContextProvider was configured.");
		}
		return contextProvider.loadContext(selectionCriteria, facts, executionOptions);
	}

    /**
     * Set the {@link ContextProvider}
     * @param contextProvider to loadContext from.
     */
	public void setContextProvider(ContextProvider contextProvider) {
		this.contextProvider = contextProvider;
	}
	
}
