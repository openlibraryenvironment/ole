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
package org.kuali.rice.krms.impl.ui;

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.impl.repository.ActionBoService;
import org.kuali.rice.krms.impl.repository.ActionBoServiceImpl;
import org.kuali.rice.krms.impl.repository.AgendaBo;
import org.kuali.rice.krms.impl.repository.AgendaBoService;
import org.kuali.rice.krms.impl.repository.AgendaBoServiceImpl;
import org.kuali.rice.krms.impl.repository.ContextBoServiceImpl;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;
import org.kuali.rice.krms.impl.repository.PropositionBoService;
import org.kuali.rice.krms.impl.repository.PropositionBoServiceImpl;
import org.kuali.rice.krms.impl.repository.RuleBoService;
import org.kuali.rice.krms.impl.repository.RuleBoServiceImpl;
import org.kuali.rice.krms.impl.repository.TermBoService;
import org.kuali.rice.krms.impl.repository.TermBoServiceImpl;
import org.kuali.rice.krms.test.AbstractBoTest;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Validation Integration Test
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineMode(Mode.CLEAR_DB)
public class AgendaEditorMaintainableIntegrationTest extends AbstractBoTest {

    static final String NAMESPACE1 = "AEMIT_KRMS_TEST_1";
    static final String CONTEXT1 = "AEMIT_Context1";

    static final String CONTEXT1_QUALIFIER = "Context1Qualifier";
    static final String CONTEXT1_QUALIFIER_VALUE = "BLAH1";
    static final String AGENDA1 = "TestAgenda1";

    private PropositionBoService propositionBoService;
    private TermBoService termBoService;
    private AgendaBoService agendaBoService;
    private RuleBoService ruleBoService;
    private ActionBoService actionBoService;

    @Before
    public void setup() {

        krmsAttributeDefinitionService = KrmsRepositoryServiceLocator.getKrmsAttributeDefinitionService();
        krmsTypeRepository = KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService();

        // like RepositoryCreateAndExecuteIntegrationTest
        propositionBoService = new PropositionBoServiceImpl();
        ((PropositionBoServiceImpl)propositionBoService).setBusinessObjectService(getBoService());
        termBoService = new TermBoServiceImpl();
        ((TermBoServiceImpl)termBoService).setBusinessObjectService(getBoService());
        contextRepository = new ContextBoServiceImpl();
        ((ContextBoServiceImpl)contextRepository).setBusinessObjectService(getBoService());
        agendaBoService = new AgendaBoServiceImpl();
        ((AgendaBoServiceImpl)agendaBoService).setBusinessObjectService(getBoService());
        ((AgendaBoServiceImpl)agendaBoService).setAttributeDefinitionService(krmsAttributeDefinitionService);
        ruleBoService = new RuleBoServiceImpl();
        ((RuleBoServiceImpl)ruleBoService).setBusinessObjectService(getBoService());
        actionBoService = new ActionBoServiceImpl();
        ((ActionBoServiceImpl)actionBoService).setBusinessObjectService(getBoService());
    }

    @Test
    public void testEmptyAgendaDelete() {
        ContextDefinition contextDefintion1 = createContextDefinition(NAMESPACE1, CONTEXT1, Collections.singletonMap(
                CONTEXT1_QUALIFIER, CONTEXT1_QUALIFIER_VALUE));
        createAgendaDefinition(AGENDA1, "AgendaLabel", contextDefintion1);
        AgendaDefinition agendaDefinition = agendaBoService.getAgendaByNameAndContextId(AGENDA1, contextDefintion1.getId());

        lookupAndSaveDataObject(agendaDefinition);
    }

/*
   This test requires:
   1. changing the BaselineMode to (Mode.NONE).
   2. Setting a breakpoint on the first line.
   3. Debug the test.
   4. When you reach the breakpoint, re-impex (and then apply the db test script) your database.
   5. Continue.  No exceptions should be thrown.
    @Test
    public void testDbAgendaItems() {
        AgendaDefinition agendaDefinition = agendaBoService.getAgendaByNameAndContextId("My Fabulous Agenda", "CONTEXT1"); // values from populated test db
        lookupAndSaveDataObject(agendaDefinition);
        agendaDefinition = agendaBoService.getAgendaByNameAndContextId("SimpleAgendaCompoundProp", "CONTEXT1"); // values from populated test db
        lookupAndSaveDataObject(agendaDefinition);
        agendaDefinition = agendaBoService.getAgendaByNameAndContextId("One Big Rule", "CONTEXT1"); // values from populated test db
        lookupAndSaveDataObject(agendaDefinition);
    }
*/

    private void lookupAndSaveDataObject(AgendaDefinition agendaDefinition) {
        AgendaBo blah = findAgendaByPrimaryKey(agendaDefinition);

        AgendaEditorMaintainable aem = new AgendaEditorMaintainable();
        AgendaEditor ae = new AgendaEditor();
        ae.setAgenda(blah);
        aem.setDataObject(ae);
        aem.saveDataObject();
    }

    private AgendaBo findAgendaByPrimaryKey(AgendaDefinition agendaDefinition) {Map<String,String>
            primaryKeys = new HashMap<String, String>();
        primaryKeys.put("id", agendaDefinition.getId());
        return getBoService().findByPrimaryKey(AgendaBo.class, primaryKeys);
    }

    private void createAgendaDefinition(String agendaName, String agendaLabel, ContextDefinition contextDefinition) {
        KrmsTypeDefinition krmsGenericTypeDefinition = createKrmsGenericTypeDefinition(contextDefinition.getNamespace(), "testAgendaTypeService", agendaName, agendaLabel);

        AgendaDefinition agendaDefinition =
                AgendaDefinition.Builder.create(null, agendaName, krmsGenericTypeDefinition.getId(), contextDefinition.getId()).build();
        agendaDefinition = agendaBoService.createAgenda(agendaDefinition);
        agendaBoService.updateAgenda(agendaDefinition);

        AgendaBo blah = findAgendaByPrimaryKey(agendaDefinition);

        AgendaDefinition.Builder agendaDefBuilder1 = AgendaDefinition.Builder.create(agendaBoService.to(blah));
        agendaDefinition = agendaDefBuilder1.build();

        agendaBoService.updateAgenda(agendaDefinition);
    }
}
