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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
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
import org.kuali.rice.krms.framework.engine.AgendaTreeEntry;
import org.kuali.rice.krms.framework.engine.BasicAgenda;
import org.kuali.rice.krms.framework.engine.BasicAgendaTree;
import org.kuali.rice.krms.framework.engine.BasicAgendaTreeEntry;
import org.kuali.rice.krms.framework.engine.BasicContext;
import org.kuali.rice.krms.framework.engine.BasicRule;
import org.kuali.rice.krms.framework.engine.ComparableTermBasedProposition;
import org.kuali.rice.krms.framework.engine.expression.ComparisonOperator;
import org.kuali.rice.krms.framework.engine.Context;
import org.kuali.rice.krms.framework.engine.ContextProvider;
import org.kuali.rice.krms.framework.engine.Proposition;
import org.kuali.rice.krms.framework.engine.ProviderBasedEngine;
import org.kuali.rice.krms.framework.engine.ResultLogger;
import org.kuali.rice.krms.framework.engine.Rule;
import org.kuali.rice.krms.framework.engine.expression.ComparisonOperatorServiceImpl;

@Ignore
public class AgendaTest {
	private static final ResultLogger LOG = ResultLogger.getInstance();

	// totalCostTerm will resolve to the Integer value 5
    private ComparisonOperator operatorGreaterThan;
	private Proposition trueProp;
	private Proposition falseProp;

	@Before
	public void setUp() {
        operatorGreaterThan = ComparisonOperator.GREATER_THAN;
        operatorGreaterThan.setComparisonOperatorService(ComparisonOperatorServiceImpl.getInstance());
        trueProp = new ComparableTermBasedProposition(operatorGreaterThan, totalCostTerm, Integer.valueOf(1));
        falseProp = new ComparableTermBasedProposition(operatorGreaterThan, totalCostTerm, Integer.valueOf(1000));

		ActionMock.resetActionsFired();
	}


	@Test
	public void testAllRulesAgenda() {

		Rule rule1 = new BasicRule("r1", trueProp, Collections.<Action>singletonList(new ActionMock("a1")));
		Rule rule2 = new BasicRule("r2", falseProp, Collections.<Action>singletonList(new ActionMock("a2")));
		Rule rule3 = new BasicRule("r3", trueProp, Collections.<Action>singletonList(new ActionMock("a3")));
		
		AgendaTreeEntry entry1 = new BasicAgendaTreeEntry(rule1);
		AgendaTreeEntry entry2 = new BasicAgendaTreeEntry(rule2);
		AgendaTreeEntry entry3 = new BasicAgendaTreeEntry(rule3);
		BasicAgendaTree agendaTree = new BasicAgendaTree(entry1, entry2, entry3); 
		Agenda agenda = new BasicAgenda(Collections.singletonMap(AgendaDefinition.Constants.EVENT, "test"), agendaTree);
		
		execute(agenda);

		assertTrue(ActionMock.actionFired("a1"));
		assertFalse(ActionMock.actionFired("a2"));
		assertTrue(ActionMock.actionFired("a3"));
	}
	
	@Test
	public void testIfTrueSubAgenda() {

		Rule rule1 = new BasicRule("r1", trueProp, Collections.<Action>singletonList(new ActionMock("a1")));
		Rule rule2 = new BasicRule("r2", falseProp, Collections.<Action>singletonList(new ActionMock("a2")));
		Rule subRule1 = new BasicRule("r1s1", trueProp, Collections.<Action>singletonList(new ActionMock("a3")));
		
		BasicAgendaTree subAgendaTree1 = new BasicAgendaTree(new BasicAgendaTreeEntry(subRule1));
		BasicAgendaTree agendaTree1 = new BasicAgendaTree(new BasicAgendaTreeEntry(rule1, subAgendaTree1, null)); 
		Agenda agenda1 = new BasicAgenda(Collections.singletonMap(AgendaDefinition.Constants.EVENT, "test"), agendaTree1);
		
		execute(agenda1);

		assertTrue(ActionMock.actionFired("a1"));
		assertTrue(ActionMock.actionFired("a3"));
		
		// RESET
		ActionMock.resetActionsFired();
		
		BasicAgendaTree subAgendaTree2 = new BasicAgendaTree(new BasicAgendaTreeEntry(subRule1));
		BasicAgendaTree agendaTree2 = new BasicAgendaTree(new BasicAgendaTreeEntry(rule2, subAgendaTree2, null)); 
		Agenda agenda2 = new BasicAgenda(Collections.singletonMap(AgendaDefinition.Constants.EVENT, "test"), agendaTree2);
		
		execute(agenda2);

		assertFalse(ActionMock.actionFired("a2"));
		assertFalse(ActionMock.actionFired("a3"));
	}

	@Test
	public void testIfFalseSubAgenda() {

		Rule rule1 = new BasicRule("r1", trueProp, Collections.<Action>singletonList(new ActionMock("a1")));
		Rule rule2 = new BasicRule("r2", falseProp, Collections.<Action>singletonList(new ActionMock("a2")));
		Rule subRule1 = new BasicRule("r1s1", trueProp, Collections.<Action>singletonList(new ActionMock("a3")));
		
		BasicAgendaTree subAgendaTree1 = new BasicAgendaTree(new BasicAgendaTreeEntry(subRule1));
		BasicAgendaTree agendaTree1 = new BasicAgendaTree(new BasicAgendaTreeEntry(rule1, null, subAgendaTree1)); 
		Agenda agenda1 = new BasicAgenda(Collections.singletonMap(AgendaDefinition.Constants.EVENT, "test"), agendaTree1);
		
		execute(agenda1);

		assertTrue(ActionMock.actionFired("a1"));
		assertFalse(ActionMock.actionFired("a3"));
		
		// RESET
		ActionMock.resetActionsFired();
		
		BasicAgendaTree subAgendaTree2 = new BasicAgendaTree(new BasicAgendaTreeEntry(subRule1));
		BasicAgendaTree agendaTree2 = new BasicAgendaTree(new BasicAgendaTreeEntry(rule2, null, subAgendaTree2)); 
		Agenda agenda2 = new BasicAgenda(Collections.singletonMap(AgendaDefinition.Constants.EVENT, "test"), agendaTree2);
		
		execute(agenda2);

		assertFalse(ActionMock.actionFired("a2"));
		assertTrue(ActionMock.actionFired("a3"));
	}
	
	@Test
	public void testAfterAgenda() {

		Rule rule1 = new BasicRule("r1", trueProp, Collections.<Action>singletonList(new ActionMock("a1")));
		Rule rule2 = new BasicRule("r2", falseProp, Collections.<Action>singletonList(new ActionMock("a2")));
		Rule subRule1 = new BasicRule("r1s1", trueProp, Collections.<Action>singletonList(new ActionMock("a3")));
		
		BasicAgendaTree agendaTree1 = new BasicAgendaTree(new BasicAgendaTreeEntry(rule1), new BasicAgendaTreeEntry(subRule1)); 
		Agenda agenda1 = new BasicAgenda(Collections.singletonMap(AgendaDefinition.Constants.EVENT, "test"), agendaTree1);
		
		execute(agenda1);

		assertTrue(ActionMock.actionFired("a1"));
		assertTrue(ActionMock.actionFired("a3"));
		
		// RESET
		ActionMock.resetActionsFired();
		
		BasicAgendaTree agendaTree2 = new BasicAgendaTree(new BasicAgendaTreeEntry(rule2), new BasicAgendaTreeEntry(subRule1)); 
		Agenda agenda2 = new BasicAgenda(Collections.singletonMap(AgendaDefinition.Constants.EVENT, "test"), agendaTree2);
		
		execute(agenda2);

		assertFalse(ActionMock.actionFired("a2"));
		assertTrue(ActionMock.actionFired("a3"));
	}

    /**
     * Make sure agenda qualifier matching is based on the provided qualifiers
     * see https://jira.kuali.org/browse/KULRICE-6098
     */
    public void testQualifierMissingFromAgenda() {
        // RESET
		ActionMock.resetActionsFired();

        Rule rule1 = new BasicRule("r1", trueProp, Collections.<Action>singletonList(new ActionMock("a1")));

        BasicAgendaTree agendaTree1 = new BasicAgendaTree(new BasicAgendaTreeEntry(rule1));
        Agenda agenda1 = new BasicAgenda(Collections.<String, String>emptyMap(), agendaTree1);

        // this shouldn't select any agendas, so no rules will really get executed
        execute(agenda1, Collections.singletonMap(AgendaDefinition.Constants.EVENT, "test"));

        // Expected: the agenda didn't get selected, so the action didn't fire
        assertFalse("the agenda should not have been selected and executed", ActionMock.actionFired("a1"));
    }

    /**
     * execute the engine against a trivial context containing the given agenda.
     * a default agenda qualifier of Event=test will be used.
     * @param agenda
     */
    private void execute(Agenda agenda) {
        execute(agenda, Collections.singletonMap(AgendaDefinition.Constants.EVENT, "test"));
    }

    /**
     * execute the engine against a trivial context containing the given agenda.
     * the given agenda qualifier will be used.
     * @param agenda
     * @param agendaQualifiers
     */
	private void execute(Agenda agenda, Map<String, String> agendaQualifiers) {
		Map<String, String> contextQualifiers = new HashMap<String, String>();
		contextQualifiers.put("docTypeName", "Proposal");

		List<TermResolver<?>> testResolvers = new ArrayList<TermResolver<?>>();
		testResolvers.add(testResolver);
		
		Context context = new BasicContext(Arrays.asList(agenda), testResolvers);
		ContextProvider contextProvider = new ManualContextProvider(context);
		
		SelectionCriteria selectionCriteria = SelectionCriteria.createCriteria(null, contextQualifiers,
                agendaQualifiers);
		
		ProviderBasedEngine engine = new ProviderBasedEngine();
		engine.setContextProvider(contextProvider);

		// Set execution options to log execution
		ExecutionOptions executionOptions = new ExecutionOptions().setFlag(ExecutionFlag.LOG_EXECUTION, true);
		
		EngineResults results = engine.execute(selectionCriteria, Facts.EMPTY_FACTS, executionOptions);
		assertNotNull(results);
	}
	
	private static final Term totalCostTerm = new Term("totalCost");
	
	private static final TermResolver<Integer> testResolver = new TermResolverMock<Integer>(totalCostTerm.getName(), 10);
}
