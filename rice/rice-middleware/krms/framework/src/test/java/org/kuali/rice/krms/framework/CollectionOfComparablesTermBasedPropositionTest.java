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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.krms.api.engine.EngineResults;
import org.kuali.rice.krms.api.engine.ExecutionOptions;
import org.kuali.rice.krms.api.engine.ExecutionFlag;
import org.kuali.rice.krms.api.engine.Facts;
import org.kuali.rice.krms.api.engine.SelectionCriteria;
import org.kuali.rice.krms.api.engine.Term;
import org.kuali.rice.krms.api.engine.TermResolver;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.framework.engine.Agenda;
import org.kuali.rice.krms.framework.engine.BasicAgenda;
import org.kuali.rice.krms.framework.engine.BasicAgendaTree;
import org.kuali.rice.krms.framework.engine.BasicAgendaTreeEntry;
import org.kuali.rice.krms.framework.engine.BasicContext;
import org.kuali.rice.krms.framework.engine.BasicRule;
import org.kuali.rice.krms.framework.engine.CollectionOfComparablesTermBasedProposition;
import org.kuali.rice.krms.framework.engine.CollectionOperator;
import org.kuali.rice.krms.framework.engine.Context;
import org.kuali.rice.krms.framework.engine.ContextProvider;
import org.kuali.rice.krms.framework.engine.Proposition;
import org.kuali.rice.krms.framework.engine.ProviderBasedEngine;
import org.kuali.rice.krms.framework.engine.ResultLogger;
import org.kuali.rice.krms.framework.engine.Rule;
import org.kuali.rice.krms.framework.engine.expression.ComparisonOperator;
import org.kuali.rice.krms.framework.engine.expression.ComparisonOperatorServiceImpl;

public class CollectionOfComparablesTermBasedPropositionTest {
	private static final ResultLogger LOG = ResultLogger.getInstance();
	
    private ComparisonOperator operatorGreaterThan;

	@Before
	public void setUp() {
        operatorGreaterThan = ComparisonOperator.GREATER_THAN;
        operatorGreaterThan.setComparisonOperatorService(ComparisonOperatorServiceImpl.getInstance());

		ActionMock.resetActionsFired();
	}

	@Test
	public void allPropositionTest() {

		// True cases:
		
		// this can be read as "for the collection (100, 1000), all elements are greater than 1"
		assertRuleTrue(Arrays.asList(100f, 1000f), CollectionOperator.ALL, operatorGreaterThan, 1f);
		
		assertRuleTrue(Arrays.asList(100f), CollectionOperator.ALL, operatorGreaterThan, 1f);
		// This can bend the intuition a bit, but this behavior is correct -- see Wikipedia's entry on the empty set
		assertRuleTrue(Arrays.<Float>asList(), CollectionOperator.ALL, operatorGreaterThan, 1f);
		
		// False cases:
		assertRuleFalse(Arrays.asList(100f, 1000f), CollectionOperator.ALL, operatorGreaterThan, 10000f);
		assertRuleFalse(Arrays.asList(100f, 1000f), CollectionOperator.ALL, operatorGreaterThan, 500f);
		assertRuleFalse(Arrays.asList(1000f, 100f), CollectionOperator.ALL, operatorGreaterThan, 500f);
		assertRuleFalse(Arrays.asList(100f), CollectionOperator.ALL, operatorGreaterThan, 10000f);

	}
	
	@Test
	public void oneOrMorePropositionTest() {

		// True cases:
		
		// this can be read as "for the collection (100, 1000), one or more elements are greater than 500"
		assertRuleTrue(Arrays.asList(100f, 1000f), CollectionOperator.ONE_OR_MORE, operatorGreaterThan, 500f);
		assertRuleTrue(Arrays.asList(1000f, 100f), CollectionOperator.ONE_OR_MORE, operatorGreaterThan, 500f);
		assertRuleTrue(Arrays.asList(1000f), CollectionOperator.ONE_OR_MORE, operatorGreaterThan, 500f);

		// False cases:
		assertRuleFalse(Arrays.asList(1000f, 2000f), CollectionOperator.ONE_OR_MORE, operatorGreaterThan, 5000f);
		assertRuleFalse(Arrays.asList(2000f, 1000f), CollectionOperator.ONE_OR_MORE, operatorGreaterThan, 5000f);
		assertRuleFalse(Arrays.asList(1000f), CollectionOperator.ONE_OR_MORE, operatorGreaterThan, 5000f);
		assertRuleFalse(Arrays.<Float>asList(), CollectionOperator.ONE_OR_MORE, operatorGreaterThan, 5000f);

	}
	
	@Test
	public void nonePropositionTest() {
		
		// True cases:
		
		// this can be read as "for the collection (100, 1000), none of the elements are greater than 5000"
		assertRuleTrue(Arrays.asList(100f, 1000f), CollectionOperator.NONE, operatorGreaterThan, 5000f);
		assertRuleTrue(Arrays.asList(1000f), CollectionOperator.NONE, operatorGreaterThan, 5000f);
		assertRuleTrue(Arrays.<Float>asList(), CollectionOperator.NONE, operatorGreaterThan, 5000f);
		
		// False cases:
		assertRuleFalse(Arrays.asList(1000f, 7000f), CollectionOperator.NONE, operatorGreaterThan, 5000f);
		assertRuleFalse(Arrays.asList(7000f, 1000f), CollectionOperator.NONE, operatorGreaterThan, 5000f);
		assertRuleFalse(Arrays.asList(7000f), CollectionOperator.NONE, operatorGreaterThan, 5000f);

	}
	
	/**
	 * @param agenda
	 */
	private void execute(Agenda agenda, TermResolver ... termResolvers) {
		Map<String, String> contextQualifiers = new HashMap<String, String>();
		contextQualifiers.put("docTypeName", "Proposal");

		List<TermResolver<?>> testResolvers = Arrays.<TermResolver<?>>asList(termResolvers);
		
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
	
	private void assertRuleTrue(List<Float> termValue,
			CollectionOperator collectionOper,
			ComparisonOperator comparisonOper, Float compareValue) {
		assertRule(termValue, collectionOper, comparisonOper, compareValue, true);
	}
	
	private void assertRuleFalse(List<Float> termValue,
			CollectionOperator collectionOper,
			ComparisonOperator comparisonOper, Float compareValue) {
		assertRule(termValue, collectionOper, comparisonOper, compareValue, false);
	}
	
	private void assertRule(List<Float> termValue,
			CollectionOperator collectionOper,
			ComparisonOperator comparisonOper, Float compareValue, boolean assertTrue) {
		
		boolean actionFired = executeTestAgenda(termValue, collectionOper, comparisonOper, compareValue).actionFired();
		
		if (assertTrue) { 
			assertTrue(actionFired); 
		} else { 
			assertFalse(actionFired); 
		}
		
		ActionMock.resetActionsFired();
	}

	/**
	 * This method ...
	 * 
	 * @param termValue
	 * @param collectionOper
	 * @param comparisonOper
	 * @param compareValue
	 * @return
	 */
	private ActionMock executeTestAgenda(List<Float> termValue,
			CollectionOperator collectionOper,
			ComparisonOperator comparisonOper, Float compareValue) {
		Term expensesTerm = new Term("expenses");
		TermResolver<Collection<Float>> expensesResolver = new TermResolverMock<Collection<Float>>(expensesTerm.getName(), termValue);
		
		Proposition prop1 = new CollectionOfComparablesTermBasedProposition(collectionOper, comparisonOper, expensesTerm, compareValue);
		ActionMock prop1Action = new ActionMock("prop1Action");
		
		Rule rule = new BasicRule("ALL", prop1, Collections.<Action>singletonList(prop1Action));
		
		BasicAgendaTree agendaTree = new BasicAgendaTree(new BasicAgendaTreeEntry(rule)); 
		Agenda agenda = new BasicAgenda(Collections.singletonMap(AgendaDefinition.Constants.EVENT, "test"), agendaTree);
		
		execute(agenda, expensesResolver);
		return prop1Action;
	}

}
