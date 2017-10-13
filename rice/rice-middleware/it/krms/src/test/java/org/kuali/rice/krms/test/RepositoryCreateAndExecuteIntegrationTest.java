/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.krms.test;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.kew.util.PerformanceLogger;
import org.kuali.rice.krms.api.KrmsApiServiceLocator;
import org.kuali.rice.krms.api.engine.EngineResults;
import org.kuali.rice.krms.api.engine.ExecutionFlag;
import org.kuali.rice.krms.api.engine.ExecutionOptions;
import org.kuali.rice.krms.api.engine.Facts;
import org.kuali.rice.krms.api.engine.ResultEvent;
import org.kuali.rice.krms.api.engine.SelectionCriteria;
import org.kuali.rice.krms.api.repository.LogicalOperator;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.function.FunctionDefinition;
import org.kuali.rice.krms.api.repository.function.FunctionParameterDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameter;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameterType;
import org.kuali.rice.krms.api.repository.proposition.PropositionType;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.api.repository.term.TermDefinition;
import org.kuali.rice.krms.api.repository.term.TermParameterDefinition;
import org.kuali.rice.krms.api.repository.term.TermResolverDefinition;
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.impl.repository.ActionBoService;
import org.kuali.rice.krms.impl.repository.AgendaBoService;
import org.kuali.rice.krms.impl.repository.FunctionBoServiceImpl;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;
import org.kuali.rice.krms.impl.repository.RuleBoService;
import org.kuali.rice.krms.impl.repository.TermBo;
import org.kuali.rice.krms.impl.repository.TermBoService;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@BaselineMode(Mode.CLEAR_DB)
public class RepositoryCreateAndExecuteIntegrationTest extends AbstractAgendaBoTest {

    static final String NAME = "name";
    static final String PREREQ_TERM_VALUE = "prereqValue";
    static final String NAMESPACE_CODE = "namespaceCode";

    static boolean localInitNeeded = true;

    /**
     *   Override of setup of AbstractAgendaBoTest (not setUp) to ensure correct test values
     */
    @Override
    @Before
    public void setup() {
        // Reset TestActionTypeService
        TestActionTypeService.resetActionsFired();

        termBoService = KrmsRepositoryServiceLocator.getTermBoService();
        contextRepository = KrmsRepositoryServiceLocator.getContextBoService();
        krmsTypeRepository = KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService();

        ruleBoService = KrmsRepositoryServiceLocator.getRuleBoService();
        agendaBoService = KrmsRepositoryServiceLocator.getAgendaBoService();
        actionBoService = KrmsRepositoryServiceLocator.getBean("actionBoService");
        functionBoService = KrmsRepositoryServiceLocator.getBean("functionRepositoryService");
        krmsAttributeDefinitionService = KrmsRepositoryServiceLocator.getKrmsAttributeDefinitionService();

        ContextDefinition contextDefintion1 = contextRepository.getContextByNameAndNamespace(CONTEXT1, NAMESPACE1);

        // only set this stuff up if we don't already have Context1 (we don't clear out KRMS tables between test methods)
        // run at least once in case previous tests have used this context and to ensure correct values
        if (contextDefintion1 == null || localInitNeeded) {
            localInitNeeded = false;
            PerformanceLogger perfLog = new PerformanceLogger();
            perfLog.log("starting agenda creation");

            contextDefintion1 = createContextDefinition(NAMESPACE1, CONTEXT1, Collections.singletonMap(CONTEXT1_QUALIFIER,
                    CONTEXT1_QUALIFIER_VALUE));
            createAgendaDefinition(AGENDA1, contextDefintion1, TSUNAMI_EVENT, NAMESPACE1);

            ContextDefinition contextDefinition2 = createContextDefinition(NAMESPACE2, CONTEXT2,
                    Collections.singletonMap(CONTEXT2_QUALIFIER, CONTEXT2_QUALIFIER_VALUE));

            ContextDefinition contextDefinition3 = createContextDefinition(NAMESPACE1, CONTEXT3,
                    Collections.<String,String>emptyMap());

            // Create multiple agendas so that we can test selection
            createAgendaDefinition(AGENDA2, contextDefinition2, EARTHQUAKE_EVENT, NAMESPACE2);
            createAgendaDefinition(AGENDA3, contextDefinition2, EARTHQUAKE_EVENT, NAMESPACE2);
            createAgendaDefinition(AGENDA4, contextDefinition2, TSUNAMI_EVENT, NAMESPACE2);
            createAgendaDefinition2(AGENDA5, contextDefinition3, NAMESPACE1);

            perfLog.log("finished agenda creation", true);
        }
    }

    @Transactional
    @Test
    public void testNullFact() {

        Map<String,String> contextQualifiers = new HashMap<String,String>();
        contextQualifiers.put(NAMESPACE_CODE, NAMESPACE1);
        contextQualifiers.put(NAME, CONTEXT3);

        Map<String,String> agendaQualifiers = new HashMap<String,String>();
        agendaQualifiers.put(NAME, AGENDA5);

        DateTime now = new DateTime();

        SelectionCriteria sc1 = SelectionCriteria.createCriteria(now, contextQualifiers, agendaQualifiers);

        Facts.Builder factsBuilder1 = Facts.Builder.create();
        factsBuilder1.addFact(NULL_FACT, null);

        ExecutionOptions xOptions1 = new ExecutionOptions();
        xOptions1.setFlag(ExecutionFlag.LOG_EXECUTION, true);

        PerformanceLogger perfLog = new PerformanceLogger();
        perfLog.log("starting rule execution");
        EngineResults eResults1 = KrmsApiServiceLocator.getEngine().execute(sc1, factsBuilder1.build(), xOptions1);
        perfLog.log("finished rule execution", true);
        List<ResultEvent> rEvents1 = executeEngileResults(eResults1);

        List<ResultEvent> ruleEvaluationResults1 = eResults1.getResultsOfType(ResultEvent.RULE_EVALUATED.toString());

        assertEquals("1 rules should have been evaluated", 1, ruleEvaluationResults1.size());

        assertTrue("rule 0 should have evaluated to true", ruleEvaluationResults1.get(0).getResult());

        // ONLY agenda 5 should have been selected
        assertTrue(TestActionTypeService.actionFired("Agenda5::Rule5::TestAction"));

        assertAgendaDidNotExecute(AGENDA1);
        assertAgendaDidNotExecute(AGENDA2);
        assertAgendaDidNotExecute(AGENDA3);
        assertAgendaDidNotExecute(AGENDA4);
    }    

    @Transactional
    @Test
    public void testSelectAgendaByAttributeAndName() {

        Map<String,String> contextQualifiers = new HashMap<String,String>();
        contextQualifiers.put(NAMESPACE_CODE, NAMESPACE1);
        contextQualifiers.put(NAME, CONTEXT1);
        contextQualifiers.put(CONTEXT1_QUALIFIER, CONTEXT1_QUALIFIER_VALUE);

        Map<String,String> agendaQualifiers = new HashMap<String,String>();
        agendaQualifiers.put(AgendaDefinition.Constants.EVENT, TSUNAMI_EVENT);
        agendaQualifiers.put(NAME, AGENDA1);

        DateTime now = new DateTime();

        SelectionCriteria sc1 = SelectionCriteria.createCriteria(now, contextQualifiers,
                Collections.singletonMap(AgendaDefinition.Constants.EVENT, TSUNAMI_EVENT));

        Facts.Builder factsBuilder1 = Facts.Builder.create();
        factsBuilder1.addFact(CAMPUS_CODE_TERM_NAME, "BL");
        factsBuilder1.addFact(BOOL1, "true");
        factsBuilder1.addFact(BOOL2, Boolean.TRUE);
        factsBuilder1.addFact(PREREQ_TERM_NAME, PREREQ_TERM_VALUE);

        ExecutionOptions xOptions1 = new ExecutionOptions();
        xOptions1.setFlag(ExecutionFlag.LOG_EXECUTION, true);

        PerformanceLogger perfLog = new PerformanceLogger();
        perfLog.log("starting rule execution");
        EngineResults eResults1 = KrmsApiServiceLocator.getEngine().execute(sc1, factsBuilder1.build(), xOptions1);
        perfLog.log("finished rule execution", true);
        List<ResultEvent> rEvents1 = executeEngileResults(eResults1);

        List<ResultEvent> ruleEvaluationResults1 = eResults1.getResultsOfType(ResultEvent.RULE_EVALUATED.toString());

        assertEquals("4 rules should have been evaluated", 4, ruleEvaluationResults1.size());

        assertTrue("rule 0 should have evaluated to true", ruleEvaluationResults1.get(0).getResult());
        assertFalse("rule 1 should have evaluated to false", ruleEvaluationResults1.get(1).getResult());
        assertTrue("rule 2 should have evaluated to true", ruleEvaluationResults1.get(2).getResult());

        // ONLY agenda 1 should have been selected
        assertTrue(TestActionTypeService.actionFired("TestAgenda1::Rule1::TestAction"));
        assertFalse(TestActionTypeService.actionFired("TestAgenda1::Rule2::TestAction"));
        assertTrue(TestActionTypeService.actionFired("TestAgenda1::Rule3::TestAction"));

        assertAgendaDidNotExecute(AGENDA2);
        assertAgendaDidNotExecute(AGENDA3);
        assertAgendaDidNotExecute(AGENDA4);
        assertAgendaDidNotExecute(AGENDA5);
    }

    @Transactional
    @Test
    public void testSelectAgendaByName() {
        Map<String,String> contextQualifiers = new HashMap<String,String>();
        contextQualifiers.put(NAMESPACE_CODE, NAMESPACE2);
        contextQualifiers.put(NAME, CONTEXT2);
        contextQualifiers.put(CONTEXT2_QUALIFIER, CONTEXT2_QUALIFIER_VALUE);
        Map<String,String> agendaQualifiers = new HashMap<String,String>();

        /*
         * We'll specifically NOT select this attribute to make sure that matching only takes place against qualifiers
         * in the selection criteria
         */
        // agendaQualifiers.put(AgendaDefinition.Constants.EVENT, EARTHQUAKE_EVENT);

        agendaQualifiers.put(NAME, AGENDA3);
        DateTime now = new DateTime();

        SelectionCriteria selectionCriteria = SelectionCriteria.createCriteria(now, contextQualifiers, agendaQualifiers);

        Facts.Builder factsBuilder2 = Facts.Builder.create();
        factsBuilder2.addFact(BOOL1, "true");
        factsBuilder2.addFact(BOOL2, Boolean.TRUE);
        factsBuilder2.addFact(CAMPUS_CODE_TERM_NAME, "BL");
        factsBuilder2.addFact(PREREQ_TERM_NAME, PREREQ_TERM_VALUE);

        ExecutionOptions xOptions2 = new ExecutionOptions();
        xOptions2.setFlag(ExecutionFlag.LOG_EXECUTION, true);


        PerformanceLogger perfLog = new PerformanceLogger();
        perfLog.log("starting rule execution 1");
        EngineResults eResults1 = KrmsApiServiceLocator.getEngine().execute(selectionCriteria, factsBuilder2.build(), xOptions2);
        perfLog.log("finished rule execution 1");
        List<ResultEvent> rEvents1 = executeEngileResults(eResults1);

        List<ResultEvent> ruleEvaluationResults1 = eResults1.getResultsOfType(ResultEvent.RULE_EVALUATED.toString());

        selectionCriteria = SelectionCriteria.createCriteria(now, contextQualifiers, agendaQualifiers);

        assertEquals("4 rules should have been evaluated", 4, ruleEvaluationResults1.size());

        assertAgendaDidNotExecute(AGENDA1);
        assertAgendaDidNotExecute(AGENDA2);

        // ONLY agenda 3 should have been selected
        assertTrue(TestActionTypeService.actionFired("Agenda3::Rule1::TestAction"));
        assertFalse(TestActionTypeService.actionFired("Agenda3::Rule2::TestAction"));
        assertTrue(TestActionTypeService.actionFired("Agenda3::Rule3::TestAction"));

        assertAgendaDidNotExecute(AGENDA4);
        assertAgendaDidNotExecute(AGENDA5);
    }


    @Transactional
    @Test
    public void testSelectMultipleAgendasByAttribute() {
        Map<String,String> contextQualifiers = new HashMap<String,String>();
        contextQualifiers.put(NAMESPACE_CODE, NAMESPACE2);
        contextQualifiers.put(NAME, CONTEXT2);
        contextQualifiers.put(CONTEXT2_QUALIFIER, CONTEXT2_QUALIFIER_VALUE);

        Map<String,String> agendaQualifiers = new HashMap<String,String>();
        agendaQualifiers.put(AgendaDefinition.Constants.EVENT, EARTHQUAKE_EVENT);

        DateTime now = new DateTime();

        SelectionCriteria selectionCriteria = SelectionCriteria.createCriteria(now, contextQualifiers, agendaQualifiers);

        Facts.Builder factsBuilder2 = Facts.Builder.create();
        factsBuilder2.addFact(BOOL1, "true");
        factsBuilder2.addFact(BOOL2, Boolean.TRUE);
        factsBuilder2.addFact(CAMPUS_CODE_TERM_NAME, "BL");
        factsBuilder2.addFact(PREREQ_TERM_NAME, PREREQ_TERM_VALUE);

        ExecutionOptions xOptions2 = new ExecutionOptions();
        xOptions2.setFlag(ExecutionFlag.LOG_EXECUTION, true);


        PerformanceLogger perfLog = new PerformanceLogger();
        perfLog.log("starting rule execution 1");
        EngineResults eResults1 = KrmsApiServiceLocator.getEngine().execute(selectionCriteria, factsBuilder2.build(), xOptions2);
        perfLog.log("finished rule execution 1");
        List<ResultEvent> rEvents1 = executeEngileResults(eResults1);

        List<ResultEvent> ruleEvaluationResults1 = eResults1.getResultsOfType(ResultEvent.RULE_EVALUATED.toString());

        selectionCriteria = SelectionCriteria.createCriteria(now, contextQualifiers, agendaQualifiers);

        assertEquals("8 rules should have been evaluated", 8, ruleEvaluationResults1.size());

        assertAgendaDidNotExecute(AGENDA1);

        // ONLY agendas 2 & 3 should have been selected

        assertTrue(TestActionTypeService.actionFired("Agenda2::Rule1::TestAction"));
        assertFalse(TestActionTypeService.actionFired("Agenda2::Rule2::TestAction"));
        assertTrue(TestActionTypeService.actionFired("Agenda2::Rule3::TestAction"));

        assertTrue(TestActionTypeService.actionFired("Agenda3::Rule1::TestAction"));
        assertFalse(TestActionTypeService.actionFired("Agenda3::Rule2::TestAction"));
        assertTrue(TestActionTypeService.actionFired("Agenda3::Rule3::TestAction"));

        assertAgendaDidNotExecute(AGENDA4);
        assertAgendaDidNotExecute(AGENDA5);
    }

    private List<ResultEvent> executeEngileResults(EngineResults eResults1) {
        try {
            return eResults1.getAllResults(); // CI NPE
        } catch (NullPointerException npe) {
            fail("https://jira.kuali.org/browse/KULRICE-8625 KRMS RepositoryCreateAndExecuteIntegrationTest fails with NPE in CI passes locally." + ExceptionUtils.getStackTrace(npe));
        }
        return null;
    }

    private void assertAgendaDidNotExecute(String agendaName) {
        assertFalse(TestActionTypeService.actionFired(agendaName+"::Rule1::TestAction"));
        assertFalse(TestActionTypeService.actionFired(agendaName+"::Rule2::TestAction"));
        assertFalse(TestActionTypeService.actionFired(agendaName+"::Rule3::TestAction"));
    }

}
