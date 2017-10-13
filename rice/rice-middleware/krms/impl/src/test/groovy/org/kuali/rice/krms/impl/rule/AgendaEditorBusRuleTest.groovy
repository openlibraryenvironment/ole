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
package org.kuali.rice.krms.impl.rule

import org.junit.Test
import org.kuali.rice.krms.impl.repository.AgendaItemBo
import org.kuali.rice.krms.impl.repository.AgendaBo
import groovy.mock.interceptor.MockFor
import org.kuali.rice.krms.impl.repository.RuleBoService
import org.kuali.rice.krms.impl.repository.RuleBo
import org.junit.Assert
import org.kuali.rice.krad.maintenance.MaintenanceDocument
import org.kuali.rice.krms.impl.ui.AgendaEditor
import org.kuali.rice.krms.impl.ui.AgendaEditorMaintainable
import org.kuali.rice.krms.impl.repository.ActionBo
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator
import java.lang.reflect.Field

class AgendaEditorBusRuleTest {

    def agendaEditorBusRule
    def mockRuleBoService = new MockFor(RuleBoService)
    def mockRuleBoServiceInstance
    def mockKrmsTypeRepositoryService = new MockFor(KrmsTypeRepositoryService)
    def mockKrmsTypeRepositoryServiceInstance

    void setup() {
        mockRuleBoServiceInstance = mockRuleBoService.proxyDelegateInstance()
        mockKrmsTypeRepositoryServiceInstance = mockKrmsTypeRepositoryService.proxyDelegateInstance()
        agendaEditorBusRule = [getRuleBoService: { mockRuleBoServiceInstance },
                               getKrmsTypeRepositoryService: { mockKrmsTypeRepositoryServiceInstance },
                               getActionTypeService: { new ActionTypeServiceBase() }] as AgendaEditorBusRule

        // Nasty hack to set mock service in service locator
        Field field = KrmsRepositoryServiceLocator.class.getDeclaredField("krmsTypeRepositoryService");
        field.setAccessible(true);
        field.set(null, mockKrmsTypeRepositoryServiceInstance);
    }

    /**
     * Check that validation is successful
     */
    @Test
    void test_processAddAgendaItemBusinessRule_validateRuleName() {
        mockRuleBoService.demand.getRuleByNameAndNamespace(1) {name, namespace -> null }
        setup()
        Assert.assertTrue(agendaEditorBusRule.processAgendaItemBusinessRules(getMaintenanceDocument(getAgendaItem(), new ActionBo(), getAgenda(null), getAgenda(null))))
        mockRuleBoService.verify(agendaEditorBusRule.getRuleBoService())
   }

    /**
     * Check that error is thrown when the rule name is missing
     */
    @Test
    void test_processAddAgendaItemBusinessRule_validateRuleName_missing() {
        setup()
        def agendaItem = getAgendaItem()
        agendaItem.getRule().setName("")
        Assert.assertFalse(agendaEditorBusRule.processAgendaItemBusinessRules(getMaintenanceDocument(agendaItem, new ActionBo(), getAgenda(null), getAgenda(null))))
        mockRuleBoService.verify(agendaEditorBusRule.getRuleBoService())
   }

    /**
     * Check that error is thrown when the rule name already exist in the agenda
     */
    @Test
    void test_processAddAgendaItemBusinessRule_validateRuleName_duplicateInBo() {
        AgendaItemBo agendaItem = getAgendaItem();
        mockRuleBoService.demand.getRuleByNameAndNamespace(0) {name, namespace -> RuleBo.to(agendaItem.getRule()) }
        setup()
        Assert.assertFalse(agendaEditorBusRule.processAgendaItemBusinessRules(getMaintenanceDocument(getAgendaItem(), new ActionBo(), getAgenda(agendaItem), getAgenda(null))))
        mockRuleBoService.verify(agendaEditorBusRule.getRuleBoService())
    }

    /**
     * Check that error is thrown when the rule name already exist in any other agenda
     */
    @Test
    void test_processAddAgendaItemBusinessRule_validateRuleName_duplicateInDatabase() {
        AgendaItemBo agendaItem = getAgendaItem();
        AgendaItemBo existingAgendaItem = getAgendaItem();
        existingAgendaItem.getRule().setId ("existingRule");
        mockRuleBoService.demand.getRuleByNameAndNamespace(1) {name, namespace -> RuleBo.to(existingAgendaItem.getRule()) }
        setup()
        Assert.assertFalse(agendaEditorBusRule.processAgendaItemBusinessRules(getMaintenanceDocument(agendaItem, new ActionBo(), getAgenda(null), getAgenda(null))))
        mockRuleBoService.verify(agendaEditorBusRule.getRuleBoService())
    }

    /**
     * Check that error is thrown when the rule type is invalid
     */
    @Test
    void test_processAddAgendaItemBusinessRule_validateRuleType_invalid() {
        mockRuleBoService.demand.getRuleByNameAndNamespace(1) {name, namespace -> null }
        mockKrmsTypeRepositoryService.demand.getRuleTypeByRuleTypeIdAndContextId(1) {ruleTypeId, contextId -> null}
        mockKrmsTypeRepositoryService.demand.getTypeById(1) {typeId -> null }
        setup()
        def agendaItem = getAgendaItem()
        agendaItem.getRule().setTypeId("invalid")
        Assert.assertFalse(agendaEditorBusRule.processAgendaItemBusinessRules(getMaintenanceDocument(agendaItem, new ActionBo(), getAgenda(null), getAgenda(null))))
        mockRuleBoService.verify(agendaEditorBusRule.getRuleBoService())
    }

    /**
     * Check that no error is thrown with a valid action
     */
    @Test
    void test_processAddAgendaItemBusinessRule_validateRuleAction() {
        mockRuleBoService.demand.getRuleByNameAndNamespace(1) {name, namespace -> null }
        mockKrmsTypeRepositoryService.demand.getActionTypeByActionTypeIdAndContextId(1) {typeId, contextId -> new KrmsTypeDefinition() }
        mockKrmsTypeRepositoryService.demand.getTypeById(2) {typeId -> getKrmsTypeDefinition() }
        setup()
        Assert.assertTrue(agendaEditorBusRule.processAgendaItemBusinessRules(getMaintenanceDocument(getAgendaItem(), getActionBo(), getAgenda(null), getAgenda(null))))
        mockRuleBoService.verify(agendaEditorBusRule.getRuleBoService())
    }

    /**
     * Check that error is thrown when the action type is invalid
     */
    @Test
    void test_processAddAgendaItemBusinessRule_validateRuleActionType_invalid() {
        mockRuleBoService.demand.getRuleByNameAndNamespace(1) {name, namespace -> null }
        mockKrmsTypeRepositoryService.demand.getActionTypeByActionTypeIdAndContextId(1) {typeId, contextId -> null }
        mockKrmsTypeRepositoryService.demand.getTypeById(1) {typeId -> null }
        setup()
        Assert.assertFalse(agendaEditorBusRule.processAgendaItemBusinessRules(getMaintenanceDocument(getAgendaItem(), getActionBo(), getAgenda(null), getAgenda(null))))
        mockRuleBoService.verify(agendaEditorBusRule.getRuleBoService())
    }

    /**
     * Check that error is thrown when the action name is missing
     */
    @Test
    void test_processAddAgendaItemBusinessRule_validateRuleActionName_missing() {
        mockRuleBoService.demand.getRuleByNameAndNamespace(1) {name, namespace -> null }
        mockKrmsTypeRepositoryService.demand.getActionTypeByActionTypeIdAndContextId(1) {typeId, contextId -> null }
        mockKrmsTypeRepositoryService.demand.getTypeById(2) {typeId -> getKrmsTypeDefinition() }
        setup()
        ActionBo actionBo = getActionBo();
        actionBo.setName("");
        Assert.assertFalse(agendaEditorBusRule.processAgendaItemBusinessRules(getMaintenanceDocument(getAgendaItem(), actionBo, getAgenda(null), getAgenda(null))))
        mockRuleBoService.verify(agendaEditorBusRule.getRuleBoService())
    }

    /**
     * Check that error is thrown when the action description is missing
     */
    @Test
    void test_processAddAgendaItemBusinessRule_validateRuleActionDescription_missing() {
        mockRuleBoService.demand.getRuleByNameAndNamespace(1) {name, namespace -> null }
        mockKrmsTypeRepositoryService.demand.getActionTypeByActionTypeIdAndContextId(1) {typeId, contextId -> null }
        mockKrmsTypeRepositoryService.demand.getTypeById(2) {typeId -> null }
        setup()
        ActionBo actionBo = getActionBo();
        actionBo.setDescription("");
        Assert.assertFalse(agendaEditorBusRule.processAgendaItemBusinessRules(getMaintenanceDocument(getAgendaItem(), actionBo, getAgenda(null), getAgenda(null))))
        mockRuleBoService.verify(agendaEditorBusRule.getRuleBoService())
    }

    private MaintenanceDocument getMaintenanceDocument(AgendaItemBo newAgendaItem, ActionBo newAgendaRuleAction, AgendaBo newAgenda, AgendaBo oldAgenda) {
        MaintenanceDocument document = new AgendaEditorMaintenanceDocumentDummy();

        AgendaEditorMaintainable newMaintainable = new AgendaEditorMaintainable();
        document.setNewMaintainableObject(newMaintainable);
        AgendaEditor newAgendaEditor = new AgendaEditor();
        newAgendaEditor.setAgendaItemLine(newAgendaItem);
        newAgendaEditor.setAgendaItemLineRuleAction(newAgendaRuleAction);
        newAgendaEditor.setAgenda(newAgenda);
        document.getNewMaintainableObject().setDataObject(newAgendaEditor);

        AgendaEditorMaintainable oldMaintainable = new AgendaEditorMaintainable();
        document.setOldMaintainableObject(oldMaintainable);
        AgendaEditor oldAgendaEditor = new AgendaEditor();
        oldAgendaEditor.setAgenda(oldAgenda);
        document.getOldMaintainableObject().setDataObject(oldAgendaEditor);
        return document;
    }

    private AgendaItemBo getAgendaItem() {
        AgendaItemBo agendaItem = new AgendaItemBo()
        agendaItem.setRule(new RuleBo())
        agendaItem.getRule().setId("testRule")
        agendaItem.getRule().setName("Test Rule")
        agendaItem.getRule().setNamespace("KRMS_TEST")
        return agendaItem;

    }

    private AgendaBo getAgenda(AgendaItemBo agendaItem) {
        AgendaBo agenda = new AgendaBo()
        agenda.setItems(new ArrayList())
        if (agendaItem != null) {
            agendaItem.getRule().setId("existingRule");
            agenda.getItems().add(agendaItem)
        }

        return agenda
    }

    private ActionBo getActionBo() {
        ActionBo actionBo = new ActionBo();
        actionBo.setTypeId("ActionType");
        actionBo.setName("Action Name");
        actionBo.setNamespace("KRMS_TEST");
        actionBo.setDescription("Action Description");
        return actionBo;
    }

    private KrmsTypeDefinition getKrmsTypeDefinition() {
        KrmsTypeDefinition krmsTypeDefinition = KrmsTypeDefinition.Builder.create(KrmsTypeDefinition.Builder.create("Test", "KRMS_TEST"))
                                                    .serviceName("TypeService")
                                                    .build();
        return krmsTypeDefinition;
    }

}
