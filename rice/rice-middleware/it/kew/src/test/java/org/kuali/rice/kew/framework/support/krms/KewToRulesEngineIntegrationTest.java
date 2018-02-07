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
package org.kuali.rice.kew.framework.support.krms;

import org.junit.Test;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.peopleflow.PeopleFlowDefinition;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.api.KrmsApiServiceLocator;
import org.kuali.rice.krms.api.KrmsConstants;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeAttribute;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import org.kuali.rice.krms.impl.repository.ActionAttributeBo;
import org.kuali.rice.krms.impl.repository.ActionBo;
import org.kuali.rice.krms.impl.repository.AgendaAttributeBo;
import org.kuali.rice.krms.impl.repository.AgendaBo;
import org.kuali.rice.krms.impl.repository.AgendaItemBo;
import org.kuali.rice.krms.impl.repository.ContextBo;
import org.kuali.rice.krms.impl.repository.KrmsAttributeDefinitionBo;
import org.kuali.rice.krms.impl.repository.KrmsAttributeDefinitionService;
import org.kuali.rice.krms.impl.repository.RuleBo;
import org.kuali.rice.krms.impl.util.KrmsServiceLocatorInternal;
import org.kuali.rice.test.BaselineTestCase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * An integration test which tests KEW integration with KRMS producing PeopleFlows for routing purposes.  KEW provides
 * standard integration with KRMS through the use of it's {@code <rulesEngine executorClass="..."/>} element, which is
 * what this test is testing.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.CLEAR_DB)
public class KewToRulesEngineIntegrationTest extends KEWTestCase {

    private static final String SIMPLE_DOCUMENT_TYPE = "RulesEngineIntegration-Simple";

    private static final String PEOPLE_FLOW_ID_ATTRIBUTE = "peopleFlowId";
    private static final String PEOPLE_FLOW_NAME_ATTRIBUTE = "peopleFlowName";
    private static final String EVENT_ATTRIBUTE = "Event";

    private BusinessObjectService businessObjectService;

    private KrmsAttributeDefinition peopleFlowIdAttributeDefinition;
    private KrmsAttributeDefinition peopleFlowNameAttributeDefinition;
    private KrmsTypeDefinition approvalPeopleFlowActionType;
    private RuleBo ruleBo;


    @Override
    protected void loadTestData() throws Exception {
        loadXmlFile("KewToRulesEngineIntegrationTest.xml");
        businessObjectService = KRADServiceLocator.getBusinessObjectService();
        assertNotNull(businessObjectService);
        PeopleFlowDefinition peopleFlow = createFirstPeopleFlow();
        this.peopleFlowIdAttributeDefinition = createPeopleFlowIdAttributeDefinition();
        this.peopleFlowNameAttributeDefinition = createPeopleFlowNameAttributeDefinition();
        KrmsAttributeDefinitionBo eventAttributeDefinition = createEventAttributeDefinition();
        this.approvalPeopleFlowActionType = createApprovalPeopleFlowActionType(peopleFlowIdAttributeDefinition);
        this.ruleBo = createRule(approvalPeopleFlowActionType, peopleFlowIdAttributeDefinition,
                                 peopleFlowNameAttributeDefinition, peopleFlow.getId());
        ContextBo contextBo = createContext();
        createAgenda(ruleBo, contextBo, eventAttributeDefinition);
    }

    private PeopleFlowDefinition createFirstPeopleFlow() {
        String user1 = getPrincipalIdForName("user1");
        String user2 = getPrincipalIdForName("user2");
        String testWorkgroup = getGroupIdForName("KR-WKFLW", "TestWorkgroup");
        PeopleFlowDefinition.Builder peopleFlow = PeopleFlowDefinition.Builder.create("TEST", "PeopleFlow1");
        peopleFlow.addPrincipal(user1).setPriority(1);
        peopleFlow.addPrincipal(user2).setPriority(2);
        peopleFlow.addGroup(testWorkgroup).setPriority(3);
        return KewApiServiceLocator.getPeopleFlowService().createPeopleFlow(peopleFlow.build());
    }

    /**
     * Create an attribute definition for "peopleFlowId" which can be used on PeopleFlow-related action types.
     */
    private KrmsAttributeDefinition createPeopleFlowIdAttributeDefinition() {
        return createPeopleFlowAttributeDefinition(PEOPLE_FLOW_ID_ATTRIBUTE, "PeopleFlow ID");
    }

    /**
     * Create an attribute definition for "peopleFlowName" 
     */
    private KrmsAttributeDefinition createPeopleFlowNameAttributeDefinition() {
        return createPeopleFlowAttributeDefinition(PEOPLE_FLOW_NAME_ATTRIBUTE, "PeopleFlow Name");
    }

    /**
     * Create an attribute definition for "peopleFlow" using given attribute and label
     */
    private KrmsAttributeDefinition createPeopleFlowAttributeDefinition(String attribute, String label) {
        KrmsAttributeDefinitionService service = KrmsServiceLocatorInternal.getService("krmsAttributeDefinitionService");
        assertNotNull(service);
        KrmsAttributeDefinitionBo attributeDefinitionBo = new KrmsAttributeDefinitionBo();
        attributeDefinitionBo.setNamespace(KrmsConstants.KRMS_NAMESPACE);
        attributeDefinitionBo.setName(attribute);
        attributeDefinitionBo.setLabel(label);
        attributeDefinitionBo.setActive(true);
        attributeDefinitionBo = businessObjectService.save(attributeDefinitionBo);
        assertNotNull(attributeDefinitionBo.getId());
        return KrmsAttributeDefinitionBo.to(attributeDefinitionBo);
    }


    /**
     * Creates the KRMS Type for PeopleFlow approval actions.
     */
    private KrmsTypeDefinition createApprovalPeopleFlowActionType(KrmsAttributeDefinition peopleFlowIdAttributeDefinition) {
        KrmsTypeRepositoryService krmsTypeRepositoryService = KrmsApiServiceLocator.getKrmsTypeRepositoryService();
        KrmsTypeDefinition.Builder typeDefinition = KrmsTypeDefinition.Builder.create(KrmsConstants.KRMS_NAMESPACE, "approvalPeopleFlowActionType");
        typeDefinition.setServiceName("approvalPeopleFlowActionTypeService");
        KrmsTypeAttribute.Builder attributeDefinition = KrmsTypeAttribute.Builder.create(null, peopleFlowIdAttributeDefinition.getId(), 1);
        typeDefinition.getAttributes().add(attributeDefinition);
        KrmsTypeDefinition approvalPeopleFlowActionType = krmsTypeRepositoryService.createKrmsType(typeDefinition.build());
        assertNotNull(approvalPeopleFlowActionType);
        assertNotNull(approvalPeopleFlowActionType.getId());
        assertEquals(1, approvalPeopleFlowActionType.getAttributes().size());
        assertNotNull(approvalPeopleFlowActionType.getAttributes().get(0).getId());
        assertEquals(approvalPeopleFlowActionType.getId(), approvalPeopleFlowActionType.getAttributes().get(0).getTypeId());
        return approvalPeopleFlowActionType;
    }

    /**
     * Creates a rule linked with the given action type and people flow action
     * @param actionType
     * @param peopleFlowIdAttributeDefinition
     * @param peopleFlowId
     */
    private RuleBo createRule(KrmsTypeDefinition actionType, KrmsAttributeDefinition peopleFlowIdAttributeDefinition,
            KrmsAttributeDefinition peopleFlowNameAttributeDefinition, String peopleFlowId) {
        RuleBo rule = new RuleBo();
        rule.setNamespace("TEST");
        rule.setName("PeopleFlowRule");
        // no propositions on this rule so it should (hopefully) always evaluate to true

        List<ActionBo> actions = new ArrayList<ActionBo>();
        rule.setActions(actions);

        // create the action with an attribute pointing to a peopleflow
        ActionBo peopleFlowAction = new ActionBo();
        actions.add(peopleFlowAction);
        peopleFlowAction.setNamespace("TEST");
        peopleFlowAction.setName("PeopleFlowApprovalAction");
        peopleFlowAction.setSequenceNumber(1);
        peopleFlowAction.setTypeId(actionType.getId());
        Set<ActionAttributeBo> actionAttributes = new HashSet<ActionAttributeBo>();
        peopleFlowAction.setAttributeBos(actionAttributes);

        ActionAttributeBo actionAttribute = new ActionAttributeBo();
        actionAttributes.add(actionAttribute);
        actionAttribute.setAttributeDefinitionId(peopleFlowIdAttributeDefinition.getId());
        actionAttribute.setAttributeDefinition(KrmsAttributeDefinitionBo.from(peopleFlowIdAttributeDefinition));
        actionAttribute.setValue(peopleFlowId);

        ActionAttributeBo actionNameAttribute = new ActionAttributeBo();
        actionAttributes.add(actionNameAttribute);
        actionNameAttribute.setAttributeDefinitionId(peopleFlowNameAttributeDefinition.getId());
        actionNameAttribute.setAttributeDefinition(KrmsAttributeDefinitionBo.from(peopleFlowNameAttributeDefinition));
        actionNameAttribute.setValue(peopleFlowAction.getName() + " Name attr");

        // set up a simple default type for the rule
        KrmsTypeRepositoryService krmsTypeRepositoryService = KrmsApiServiceLocator.getKrmsTypeRepositoryService();
        KrmsTypeDefinition.Builder typeDefinition = KrmsTypeDefinition.Builder.create("PeopleFlowRule Name", KrmsConstants.KRMS_NAMESPACE);
        typeDefinition.setServiceName("defaultRuleTypeService");
        KrmsTypeDefinition defaultRuleType = krmsTypeRepositoryService.createKrmsType(typeDefinition.build());
        assertNotNull(defaultRuleType);
        assertNotNull(defaultRuleType.getId());

        // now assign the default type to the rule and save it
        rule.setTypeId(defaultRuleType.getId());
        rule = businessObjectService.save(rule);
        assertNotNull(rule.getId());
        assertEquals(1, rule.getActions().size());
        assertNotNull(rule.getActions().get(0).getId());
        assertEquals(2, rule.getActions().get(0).getAttributeBos().size());
        return rule;
    }

    private ContextBo createContext() {
        // set up a simple default type for the context
        KrmsTypeRepositoryService krmsTypeRepositoryService = KrmsApiServiceLocator.getKrmsTypeRepositoryService();
        KrmsTypeDefinition.Builder typeDefinition = KrmsTypeDefinition.Builder.create(KrmsConstants.KRMS_NAMESPACE, "DefaultContextType");
        KrmsTypeDefinition defaultContextType = krmsTypeRepositoryService.createKrmsType(typeDefinition.build());

        ContextBo contextBo = new ContextBo();
        contextBo.setNamespace(KrmsConstants.KRMS_NAMESPACE);
        contextBo.setName("MyContext");
        contextBo.setTypeId(defaultContextType.getId());
        return businessObjectService.save(contextBo);
    }

    /**
     * Create an attribute definition for "Event" which is used to define the triggering event on an agenda.
     */
    private KrmsAttributeDefinitionBo createEventAttributeDefinition() {
        KrmsAttributeDefinitionService service = KrmsServiceLocatorInternal.getService("krmsAttributeDefinitionService");
        assertNotNull(service);
        KrmsAttributeDefinitionBo attributeDefinitionBo = new KrmsAttributeDefinitionBo();
        attributeDefinitionBo.setNamespace(KrmsConstants.KRMS_NAMESPACE);
        attributeDefinitionBo.setName(EVENT_ATTRIBUTE);
        attributeDefinitionBo.setLabel("Event");
        attributeDefinitionBo.setActive(true);
        attributeDefinitionBo = businessObjectService.save(attributeDefinitionBo);
        assertNotNull(attributeDefinitionBo.getId());
        return attributeDefinitionBo;
    }

    private AgendaBo createAgenda(RuleBo ruleBo, ContextBo contextBo, KrmsAttributeDefinitionBo eventAttributeDefinition) {
        // set up a simple default type for the agenda
        AgendaBo agendaBo = new AgendaBo();
        agendaBo.setActive(true);
        agendaBo.setContextId(contextBo.getId());
        agendaBo.setName("MyAgenda");
        agendaBo.setTypeId(null);
        agendaBo = businessObjectService.save(agendaBo);

        agendaBo.setFirstItemId(ruleBo.getId());
        AgendaItemBo agendaItemBo = new AgendaItemBo();
        agendaItemBo.setRule(ruleBo);
        agendaItemBo.setAgendaId(agendaBo.getId());
        agendaItemBo = businessObjectService.save(agendaItemBo);

        List<AgendaItemBo> agendaItems = new ArrayList<AgendaItemBo>();
        agendaItems.add(agendaItemBo);
        agendaBo.setItems(agendaItems);
        agendaBo.setFirstItemId(agendaItemBo.getId());

        // also add attribute to the agenda to store event
        Set<AgendaAttributeBo> agendaAttributes = new HashSet<AgendaAttributeBo>();
        agendaBo.setAttributeBos(agendaAttributes);
        AgendaAttributeBo agendaAttribute = new AgendaAttributeBo();
        agendaAttributes.add(agendaAttribute);
        agendaAttribute.setAttributeDefinitionId(eventAttributeDefinition.getId());
        agendaAttribute.setAttributeDefinition(eventAttributeDefinition);
        agendaAttribute.setValue("workflow");
        agendaBo = businessObjectService.save(agendaBo);

        contextBo.getAgendas().add(agendaBo);

        return agendaBo;
    }

    @Test
    public void testSimpleKrmsPeopleFlowRules() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user3"), SIMPLE_DOCUMENT_TYPE);
        document.route("");
        assertTrue(document.isEnroute());

        String user1 = getPrincipalIdForName("user1");
        String user2 = getPrincipalIdForName("user2");
        String ewestfal = getPrincipalIdForName("ewestfal"); // ewestfal is a member of TestWorkgroup
        // at this point, the PeopleFlow should have triggered requests to user1, user2, and TestWorkgroup, in that order
        // but only the request to user1 should be activated
        document.switchPrincipal(ewestfal);
        assertFalse(document.isApprovalRequested());
        document.switchPrincipal(user2);
        assertFalse(document.isApprovalRequested());
        document.switchPrincipal(user1);
        assertTrue(document.isApprovalRequested());

        // now approve as user1
        document.approve("");
        assertTrue(document.isEnroute());

        // should now be activated to user2
        document.switchPrincipal(user2);
        assertTrue(document.isApprovalRequested());
        document.approve("");
        assertTrue(document.isEnroute());

        // should now be activated to TestWorkgroup, of which ewestfal is a member
        document.switchPrincipal(ewestfal);
        assertTrue(document.isApprovalRequested());
        document.approve("");

        // all approvals have been taken, document should now be final
        assertTrue(document.isFinal());
    }

    @Test
    public void testMultipleKrmsPeopleFlowRules() throws Exception {
        // first, let's add a second peopleflow to the rule action setup
        addAnotherPeopleFlow(ruleBo);

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user3"), SIMPLE_DOCUMENT_TYPE);
        document.route("");
        assertTrue(document.isEnroute());

        String user1 = getPrincipalIdForName("user1");
        String user2 = getPrincipalIdForName("user2");
        String ewestfal = getPrincipalIdForName("ewestfal"); // ewestfal is a member of TestWorkgroup
        // at this point, the PeopleFlow should have triggered requests to user1, user2, and TestWorkgroup, in that order
        // but only the request to user1 should be activated
        document.switchPrincipal(ewestfal);
        assertFalse(document.isApprovalRequested());
        document.switchPrincipal(user2);
        assertFalse(document.isApprovalRequested());
        document.switchPrincipal(user1);
        assertTrue(document.isApprovalRequested());
        // there should also only be 3 action requests, action request to second peopleflow should not yet be generated
        assertEquals(3, document.getRootActionRequests().size());

        // now approve as user1
        document.approve("");
        assertTrue(document.isEnroute());

        // should now be activated to user2
        document.switchPrincipal(user2);
        assertTrue(document.isApprovalRequested());
        document.approve("");
        assertTrue(document.isEnroute());

        // should now be activated to TestWorkgroup, of which ewestfal is a member
        document.switchPrincipal(ewestfal);
        assertTrue(document.isApprovalRequested());
        document.approve("");

        // document should still be enroute, and now we should be routed to second peopleflow
        assertTrue(document.isEnroute());
        String testuser1 = getPrincipalIdForName("testuser1");
        document.switchPrincipal(testuser1);
        assertTrue(document.isApprovalRequested());
        // there should be 4 action requests total now
        assertEquals(4, document.getRootActionRequests().size());
        document.approve("");

        // all approvals have been taken, document should now be final
        assertTrue(document.isFinal());
    }

    private void addAnotherPeopleFlow(RuleBo ruleBo) {
        String testuser1 = getPrincipalIdForName("testuser1");
        PeopleFlowDefinition.Builder peopleFlowBuilder = PeopleFlowDefinition.Builder.create("TEST", "PeopleFlow2");
        peopleFlowBuilder.addPrincipal(testuser1).setPriority(1);
        PeopleFlowDefinition peopleFlow =  KewApiServiceLocator.getPeopleFlowService().createPeopleFlow(peopleFlowBuilder.build());

        // create the action with an attribute pointing to a peopleflow
        ActionBo peopleFlowAction = new ActionBo();
        ruleBo.getActions().add(peopleFlowAction);
        peopleFlowAction.setNamespace("TEST");
        peopleFlowAction.setName("PeopleFlowApprovalAction2");
        peopleFlowAction.setSequenceNumber(2);
        peopleFlowAction.setTypeId(approvalPeopleFlowActionType.getId());
        Set<ActionAttributeBo> actionAttributes = new HashSet<ActionAttributeBo>();
        peopleFlowAction.setAttributeBos(actionAttributes);
        ActionAttributeBo actionAttribute = new ActionAttributeBo();
        actionAttributes.add(actionAttribute);
        actionAttribute.setAttributeDefinitionId(peopleFlowIdAttributeDefinition.getId());
        actionAttribute.setAttributeDefinition(KrmsAttributeDefinitionBo.from(peopleFlowIdAttributeDefinition));
        actionAttribute.setValue(peopleFlow.getId());

        ActionAttributeBo actionNameAttribute = new ActionAttributeBo();
        actionAttributes.add(actionNameAttribute);
        actionNameAttribute.setAttributeDefinitionId(peopleFlowNameAttributeDefinition.getId());
        actionNameAttribute.setAttributeDefinition(KrmsAttributeDefinitionBo.from(peopleFlowNameAttributeDefinition));
        actionNameAttribute.setValue(peopleFlowAction.getName() + " Name attr");

        businessObjectService.save(ruleBo);
        
    }

}

