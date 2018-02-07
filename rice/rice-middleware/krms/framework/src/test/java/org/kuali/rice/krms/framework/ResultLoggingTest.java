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
package org.kuali.rice.krms.framework;

import static junit.framework.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.kuali.rice.krms.api.engine.EngineResults;
import org.kuali.rice.krms.api.engine.ExecutionOptions;
import org.kuali.rice.krms.api.engine.ExecutionFlag;
import org.kuali.rice.krms.api.engine.Facts;
import org.kuali.rice.krms.api.engine.SelectionCriteria;
import org.kuali.rice.krms.api.engine.Term;
import org.kuali.rice.krms.api.engine.TermResolver;
import org.kuali.rice.krms.api.repository.LogicalOperator;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.framework.engine.Agenda;
import org.kuali.rice.krms.framework.engine.BasicAgenda;
import org.kuali.rice.krms.framework.engine.BasicAgendaTree;
import org.kuali.rice.krms.framework.engine.BasicAgendaTreeEntry;
import org.kuali.rice.krms.framework.engine.BasicContext;
import org.kuali.rice.krms.framework.engine.BasicRule;
import org.kuali.rice.krms.framework.engine.ComparableTermBasedProposition;
import org.kuali.rice.krms.framework.engine.CompoundProposition;
import org.kuali.rice.krms.framework.engine.Context;
import org.kuali.rice.krms.framework.engine.ContextProvider;
import org.kuali.rice.krms.framework.engine.Proposition;
import org.kuali.rice.krms.framework.engine.ProviderBasedEngine;
import org.kuali.rice.krms.framework.engine.ResultLogger;
import org.kuali.rice.krms.framework.engine.Rule;
import org.kuali.rice.krms.framework.engine.expression.ComparisonOperator;
import org.kuali.rice.krms.framework.engine.expression.ComparisonOperatorServiceImpl;

public class ResultLoggingTest {
	private static final ResultLogger LOG = ResultLogger.getInstance();

	@Test
	public void integrationTest() {

		// build a simple rule
        ComparisonOperator greaterThan = ComparisonOperator.GREATER_THAN;
        greaterThan.setComparisonOperatorService(ComparisonOperatorServiceImpl.getInstance());
        ComparisonOperator lessThan = ComparisonOperator.LESS_THAN;
        lessThan.setComparisonOperatorService(ComparisonOperatorServiceImpl.getInstance());

		Proposition prop1 = new ComparableTermBasedProposition(greaterThan, totalCostTerm, Integer.valueOf(1));
		Proposition prop2 = new ComparableTermBasedProposition(lessThan, totalCostTerm, Integer.valueOf(1000));
		CompoundProposition compoundProp1 = new CompoundProposition(LogicalOperator.AND, Arrays.asList(prop1, prop2));
		
		Action action1 = new SayHelloAction();
		Rule rule = new BasicRule("InBetween",compoundProp1, Arrays.asList(action1));
		
		BasicAgendaTree agendaTree = new BasicAgendaTree(new BasicAgendaTreeEntry(rule)); 
		Agenda agenda = new BasicAgenda(Collections.singletonMap(AgendaDefinition.Constants.EVENT, "test"), agendaTree);
		
		Map<String, String> contextQualifiers = new HashMap<String, String>();
		contextQualifiers.put("docTypeName", "Proposal");
		
		List<TermResolver<?>> testResolvers = new ArrayList<TermResolver<?>>();
		testResolvers.add(testResolver);
		
		Context context = new BasicContext(Arrays.asList(agenda), testResolvers);
		ContextProvider contextProvider = new ManualContextProvider(context);
		
		SelectionCriteria selectionCriteria = SelectionCriteria.createCriteria(null, contextQualifiers,
                Collections.singletonMap(AgendaDefinition.Constants.EVENT, "test"));
		
		ProviderBasedEngine engine = new ProviderBasedEngine();
		engine.setContextProvider(contextProvider);
		
		// Set execution options to log execution
		ExecutionOptions executionOptions = new ExecutionOptions().setFlag(ExecutionFlag.LOG_EXECUTION, true);
		
		EngineResults results = engine.execute(selectionCriteria, Facts.EMPTY_FACTS, executionOptions);
		assertNotNull(results);
		
	}
	
	private static final Term totalCostTerm = new Term("totalCost");
	
	private static final TermResolver<Integer> testResolver = new TermResolver<Integer>(){
		
		@Override
		public int getCost() { return 1; }
		
		@Override
		public String getOutput() { return totalCostTerm.getName(); }
		
		@Override
		public Set<String> getPrerequisites() { return Collections.emptySet(); }
		
		@Override
		public Set<String> getParameterNames() {
			return Collections.emptySet();
		}
		
		@Override
		public Integer resolve(Map<String, Object> resolvedPrereqs, Map<String, String> parameters) {
			return 5;
		}
	};

}
