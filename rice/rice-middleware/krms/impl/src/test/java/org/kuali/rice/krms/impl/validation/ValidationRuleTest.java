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
package org.kuali.rice.krms.impl.validation;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.engine.ExecutionFlag;
import org.kuali.rice.krms.api.engine.ExecutionOptions;
import org.kuali.rice.krms.api.engine.SelectionCriteria;
import org.kuali.rice.krms.api.engine.Term;
import org.kuali.rice.krms.api.engine.TermResolutionEngine;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.framework.engine.BasicExecutionEnvironment;
import org.kuali.rice.krms.framework.engine.ComparableTermBasedProposition;
import org.kuali.rice.krms.framework.engine.Rule;
import org.kuali.rice.krms.framework.engine.TermResolutionEngineImpl;
import org.kuali.rice.krms.framework.engine.expression.ComparisonOperator;
import org.kuali.rice.krms.framework.engine.expression.ComparisonOperatorServiceImpl;
import org.kuali.rice.krms.framework.type.ValidationRuleType;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 *  Test of the @{link ValidationRule}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ValidationRuleTest {
    Term term = new Term("true");
    final SelectionCriteria testEvent = SelectionCriteria.createCriteria(new DateTime(),
            Collections.EMPTY_MAP, Collections.singletonMap(AgendaDefinition.Constants.EVENT, "testEvent"));
    Map<Term, Object> facts = new HashMap<Term, Object>();
    TermResolutionEngine termResolutionEngine = new TermResolutionEngineImpl();
	// Set execution options to log execution
    ExecutionOptions executionOptions = new ExecutionOptions().setFlag(ExecutionFlag.LOG_EXECUTION, true);
    private ActionMock actionMock;

    private ComparisonOperator operatorEquals;

    @Before
    public void setUp() {
        operatorEquals = ComparisonOperator.EQUALS;
        operatorEquals.setComparisonOperatorService(ComparisonOperatorServiceImpl.getInstance());

        actionMock = new ActionMock("a1");
        actionMock.resetActionsFired();
        facts.put(term, "true");
    }

    @Test
    public void testValidRulePassesActionDoesntFire() {
        Rule validationRule = new ValidationRule(ValidationRuleType.VALID, "testValidRulePassesActionDoesntFire",
                new ComparableTermBasedProposition(operatorEquals, term, "true"), Collections
                .<Action>singletonList(actionMock));
        assertTrue(validationRule.evaluate(new BasicExecutionEnvironment(testEvent, facts, executionOptions,
                termResolutionEngine)));
        assertFalse(actionMock.actionFired("a1"));
    }

    @Test
    public void testValidRuleFailsActionFires() {
        Rule validationRule = new ValidationRule(ValidationRuleType.VALID, "testValidRuleFailsActionFires",
                new ComparableTermBasedProposition(operatorEquals, term, "false"), Collections
                .<Action>singletonList(actionMock));
        assertFalse(validationRule.evaluate(new BasicExecutionEnvironment(testEvent, facts, executionOptions,
                termResolutionEngine)));
        assertTrue(actionMock.actionFired("a1"));
    }

    @Test
    public void testInvalidRulePassesActionFires() {
        Rule validationRule = new ValidationRule(ValidationRuleType.INVALID, "testInvalidRulePassesActionFires",
                new ComparableTermBasedProposition(operatorEquals, term, "true"), Collections
                .<Action>singletonList(actionMock));
        assertTrue(validationRule.evaluate(new BasicExecutionEnvironment(testEvent, facts, executionOptions,
                termResolutionEngine)));
        assertTrue(actionMock.actionFired("a1"));
    }

    @Test
    public void testInvalidRuleFalseActionDoesntFire() {
        Rule validationRule = new ValidationRule(ValidationRuleType.INVALID, "testInvalidRuleFalseActionDoesntFire",
                new ComparableTermBasedProposition(operatorEquals, term, "false"), Collections
                .<Action>singletonList(actionMock));
        assertFalse(validationRule.evaluate(new BasicExecutionEnvironment(testEvent, facts, executionOptions,
                termResolutionEngine)));
        assertFalse(actionMock.actionFired("a1"));
    }

    // Copy of org.kuali.rice.krms.framework.ActionMock IntelliJ couldn't find to add dependency
    private class ActionMock implements Action {

        private final Set<String> actionsFired = new HashSet<String>();

        public void resetActionsFired() {
            actionsFired.clear();
        }

        public boolean actionFired(String name) {
            return actionsFired.contains(name);
        }

        public ActionMock(String name) {
            this.name = name;
        }

        private String name;

        @Override
        public void execute(ExecutionEnvironment environment) {
            actionsFired.add(name);
        }

        @Override
        public void executeSimulation(ExecutionEnvironment environment) {
            throw new UnsupportedOperationException();
        }

        public boolean actionFired() {
            return actionFired(name);
        }
    }
}
