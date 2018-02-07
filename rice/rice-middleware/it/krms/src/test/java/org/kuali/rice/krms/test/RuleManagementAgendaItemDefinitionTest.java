/*
 * Copyright 2006-2013 The Kuali Foundation
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

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeEntryDefinitionContract;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.impl.repository.ActionAttributeBo;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 *   RuleManagementAgendaItemDefinitionTest is to test the methods of ruleManagementServiceImpl relating to krms AgendaItems
 *
 *   Each test focuses on one of the methods.
 */
public class RuleManagementAgendaItemDefinitionTest extends RuleManagementBaseTest {

    @Override
    @Before
    public void setClassDiscriminator() {
        // set a unique discriminator for test objects of this class
        CLASS_DISCRIMINATOR = "RMAIDT";
    }

    /**
     *  Test testCreateAgendaItem()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .createAgendaItem(AgendaItemDefinition) method
     */
    @Test
    public void testCreateAgendaItem() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t0 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t0");

        String ruleId = buildTestRuleDefinition(t0.namespaceName, t0.object0).getId();
        String agendaId = createTestAgenda(t0.object0).getId();
        buildTestAgendaItemDefinition(t0.agendaItem_Id, agendaId, ruleId);
        AgendaDefinition agendaDefinition = ruleManagementService.getAgenda(agendaId);

        assertEquals("Expected Context not found",t0.contextId,agendaDefinition.getContextId());
        assertEquals("Expected AgendaId not found",t0.agenda_Id,agendaDefinition.getId());

        assertEquals("Expected AgendaItemId not found",t0.agendaItem_0_Id,agendaDefinition.getFirstItemId());
        assertEquals("Expected Rule of AgendaItem not found",t0.rule_0_Id,
                ruleManagementService.getAgendaItem(agendaDefinition.getFirstItemId()).getRule().getId());
    }

    /**
     *  Test testCreateComplexAgendaItem()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .createAgendaItem(AgendaItemDefinition) method
     *  with a complex definition
     */
    @Test
    public void testCreateComplexAgendaItem() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t1 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t1");

        // createComplexAgenda uses the ruleManagementServiceImpl.createAgendaItem method
        buildComplexAgenda(t1);
        List<AgendaItemDefinition> agendaItems = ruleManagementService.getAgendaItemsByContext(t1.contextId);

        // the complex agendas created should have 8 agendaItems associated with it
        assertEquals("Invalid number of agendaItems created", 8, agendaItems.size());
    }

    /**
     *  Test testGetAgendaItem()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getAgendaItem(AgendaItemId) method
     */
    @Test
    public void testGetAgendaItem() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t2 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t2");

        buildComplexAgenda(t2);

        AgendaItemDefinition agendaItem = ruleManagementService.getAgendaItem(t2.agendaItem_0_Id);
        assertEquals("Invalid AgendaItem value",t2.agendaItem_0_Id,agendaItem.getId());
        assertEquals("Invalid AgendaItem value",t2.agenda_Id,agendaItem.getAgendaId());
        assertEquals("Invalid AgendaItem value",t2.agendaItem_3_Id,agendaItem.getAlwaysId());
        assertEquals("Invalid AgendaItem value",t2.agendaItem_3_Id,agendaItem.getAlways().getId());
        assertEquals("Invalid AgendaItem value",t2.rule_0_Id,agendaItem.getRuleId());
        assertEquals("Invalid AgendaItem value",t2.agendaItem_1_Id,agendaItem.getWhenTrue().getId());
        assertEquals("Invalid AgendaItem value",t2.agendaItem_1_Id,agendaItem.getWhenTrueId());
        assertEquals("Invalid AgendaItem value",t2.agendaItem_2_Id,agendaItem.getWhenFalse().getId());
        assertEquals("Invalid AgendaItem value",t2.agendaItem_2_Id,agendaItem.getWhenFalseId());

        // check rule information populated into agendaItem
        RuleDefinition ruleDefinition = agendaItem.getRule();
        assertEquals("Invalid RuleId found for agendaItem",t2.rule_0_Id,ruleDefinition.getId());
        assertEquals("Invalid RuleId found for agendaItem",t2.rule_0_Name,ruleDefinition.getName());
        assertEquals("Invalid RuleId found for agendaItem",t2.proposition_0_Descr,ruleDefinition.getProposition().getDescription());
        assertEquals("Invalid RuleId found for agendaItem","S",ruleDefinition.getProposition().getPropositionTypeCode());

        // check agendaItem count of associated items
        List<AgendaItemDefinition> agendaItems = ruleManagementService.getAgendaItemsByContext(t2.contextId);
        assertEquals("Invalid number of agendaItems created", 8, agendaItems.size());

        AgendaItemDefinition junkAgendaItem = ruleManagementService.getAgendaItem("junk");
        assertNull("AgendaItem is not null", junkAgendaItem);
    }

    /**
     *  Test testGetAgendaItemsByType()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getAgendaItemsByType(NamespaceType) method
     */
    @Test
    public void testGetAgendaItemsByType() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t3 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t3");
        createComplexAgenda(t3.namespaceName, t3.namespaceType, t3);

        // get a second set of object names for the creation of second agenda
        RuleManagementBaseTestObjectNames t4 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t4");

        // create second agenda with same namespace type as the first agenda
        createComplexAgenda(t4.namespaceName, t3.namespaceType, t4);

        // get agendaItems for all agendas of this type
        // these complex agendas are both of type namespaceType
        // each complex agenda has 8 agendaItems
        assertEquals("Incorrect number of agendaItems found",16,
                ruleManagementService.getAgendaItemsByType(t3.namespaceType).size());
    }

    /**
     *  Test testGetAgendaItemsByContext()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getAgendaItemsByContext(ContextId) method
     */
    @Test
    public void testGetAgendaItemsByContext() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t5 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t5");
        buildComplexAgenda(t5);

        // get a second set of object names for the creation of second agenda
        RuleManagementBaseTestObjectNames t6 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t6");
        buildComplexAgenda(t6);

        // get agendaItems for all agendas with this Context
        // each complex agenda has 8 agendaItems
        // each complex agenda has a unique Context
        assertEquals("Incorrect number of agendaItems returned", 8, ruleManagementService.getAgendaItemsByContext(t5.contextId).size());
        assertEquals("No agendaItems should have been returned",0,
                ruleManagementService.getAgendaItemsByContext("junk").size());
    }

    /**
     *  Test testGetAgendaItemsByTypeAndContext()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getAgendaItemsByTypeAndContext(NamespaceType, ContextId) method
     */
    @Test
    public void testGetAgendaItemsByTypeAndContext() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t7 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t7");
        createComplexAgenda(t7.namespaceName, t7.namespaceType, t7);

        // get a second set of object names for the creation of second agenda
        RuleManagementBaseTestObjectNames t8 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t8");
        createComplexAgenda(t8.namespaceName, t7.namespaceType, t8);

        // get agendaItems for all agendas of this type with this Context
        // each complex agenda has 8 agendaItems
        // complex agendas are of type "firstNamespaceType"
        // each complex agenda has a unique Context
        assertEquals("Incorrect number of agendaItems returned",8,
                ruleManagementService.getAgendaItemsByTypeAndContext(t7.namespaceType,t8.contextId).size());
        assertEquals("Incorrect number of agendaItems returned",8,
                ruleManagementService.getAgendaItemsByTypeAndContext(t7.namespaceType,t7.contextId).size());
        assertEquals("Incorrect number of agendaItems returned",0,
                ruleManagementService.getAgendaItemsByTypeAndContext("badType",t7.contextId).size());
        assertEquals("Incorrect number of agendaItems returned",0,
                ruleManagementService.getAgendaItemsByTypeAndContext(t7.namespaceType,"badContext").size());

        try {
            ruleManagementService.getAgendaItemsByTypeAndContext(null,t7.contextId);
            fail("Should have thrown RiceIllegalArgumentException: type ID is null or blank");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: type ID is null or blank
        }

        try {
            ruleManagementService.getAgendaItemsByTypeAndContext("    ",t7.contextId);
            fail("Should have thrown RiceIllegalArgumentException: type ID is null or blank");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: type ID is null or blank
        }

        try {
            ruleManagementService.getAgendaItemsByTypeAndContext(t7.namespaceType,null);
            fail("Should have thrown RiceIllegalArgumentException: context ID is null or blank");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: context ID is null or blank
        }
    }

    /**
     *  Test testDeleteAgendaItem()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .deleteAgendaItem(AgendaItemId) method
     */
    @Test
    public void testDeleteAgendaItem() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t9 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t9");

        // each complex agenda has 8 agendaItems
        AgendaDefinition.Builder agendaBuilder4900 = buildComplexAgenda(t9);
        // check the number created before delete
        List<AgendaItemDefinition> agendaItems = ruleManagementService.getAgendaItemsByContext(t9.contextId);
        assertEquals("Invalid number of agendaItems created", 8, agendaItems.size());

        // delete one of the eight agendaItems
        ruleManagementService.deleteAgendaItem(t9.agendaItem_0_Id);

        // check agendaItem count of items for Agenda, one should now be deleted
        agendaItems = ruleManagementService.getAgendaItemsByContext(t9.contextId);
        assertEquals("Invalid number of agendaItems created", 7, agendaItems.size());

        // look for a agendaItem which does not exist
        AgendaItemDefinition junkAgendaItem = ruleManagementService.getAgendaItem("junk");
        assertNull("AgendaItem is not null", junkAgendaItem);
    }

    /**
     *  Test testUpdateAgendaItem()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .updateAgendaItem(AgendaItemDefinition) method
     */
    @Test
    public void testUpdateAgendaItem() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t10 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t10");

        buildComplexAgenda(t10);

        // validate default attributes before update
        AgendaItemDefinition agendaItem = ruleManagementService.getAgendaItem(t10.agendaItem_0_Id);
        assertEquals("Invalid AgendaItem value",t10.agendaItem_0_Id,agendaItem.getId());
        assertEquals("Invalid AgendaItem value",t10.agenda_Id,agendaItem.getAgendaId());
        assertEquals("Invalid AgendaItem value",t10.agendaItem_3_Id,agendaItem.getAlwaysId());
        assertEquals("Invalid AgendaItem value",t10.agendaItem_3_Id,agendaItem.getAlways().getId());
        assertEquals("Invalid AgendaItem value",t10.rule_0_Id,agendaItem.getRuleId());
        assertEquals("Invalid AgendaItem value",t10.agendaItem_1_Id,agendaItem.getWhenTrue().getId());
        assertEquals("Invalid AgendaItem value",t10.agendaItem_1_Id,agendaItem.getWhenTrueId());
        assertEquals("Invalid AgendaItem value",t10.agendaItem_2_Id,agendaItem.getWhenFalse().getId());
        assertEquals("Invalid AgendaItem value",t10.agendaItem_2_Id,agendaItem.getWhenFalseId());

        // update some of the agendaItem attributes  ( reverse whenTrue and whenFalse values and unset always
        AgendaItemDefinition.Builder agendaItemBuilder = AgendaItemDefinition.Builder.create(agendaItem);
        agendaItemBuilder.setWhenFalse(AgendaItemDefinition.Builder.create(ruleManagementService.getAgendaItem(t10.agendaItem_1_Id)));
        agendaItemBuilder.setWhenFalseId(t10.agendaItem_1_Id);
        agendaItemBuilder.setWhenTrue(AgendaItemDefinition.Builder.create(ruleManagementService.getAgendaItem(t10.agendaItem_2_Id)));
        agendaItemBuilder.setWhenTrueId(t10.agendaItem_2_Id);
        agendaItemBuilder.setAlways(null);
        agendaItemBuilder.setAlwaysId(null);
        ruleManagementService.updateAgendaItem(agendaItemBuilder.build());

        // check the update
        agendaItem = ruleManagementService.getAgendaItem(t10.agendaItem_0_Id);
        assertEquals("Invalid AgendaItem value",t10.agendaItem_0_Id,agendaItem.getId());
        assertEquals("Invalid AgendaItem value",t10.agenda_Id,agendaItem.getAgendaId());
        assertEquals("Invalid AgendaItem value",null,agendaItem.getAlwaysId());
        assertEquals("Invalid AgendaItem value",null,agendaItem.getAlways());
        assertEquals("Invalid AgendaItem value",t10.rule_0_Id,agendaItem.getRuleId());
        assertEquals("Invalid AgendaItem value",t10.agendaItem_2_Id,agendaItem.getWhenTrue().getId());
        assertEquals("Invalid AgendaItem value",t10.agendaItem_2_Id,agendaItem.getWhenTrueId());
        assertEquals("Invalid AgendaItem value",t10.agendaItem_1_Id,agendaItem.getWhenFalse().getId());
        assertEquals("Invalid AgendaItem value",t10.agendaItem_1_Id,agendaItem.getWhenFalseId());

        // update some of the agendaItem attributes
        agendaItem = ruleManagementService.getAgendaItem(t10.agendaItem_1_Id);
        agendaItemBuilder = AgendaItemDefinition.Builder.create(agendaItem);
        agendaItemBuilder.setWhenFalseId(null);
        agendaItemBuilder.setWhenTrueId(null);
        agendaItemBuilder.setAlwaysId(null);
        ruleManagementService.updateAgendaItem(agendaItemBuilder.build());
        // check the update  ( should be no change - clearing Ids should not effect agendaItem
        agendaItem = ruleManagementService.getAgendaItem(t10.agendaItem_0_Id);
        assertEquals("Invalid AgendaItem value",null,agendaItem.getAlwaysId());
        assertEquals("Invalid AgendaItem value",null,agendaItem.getAlways());
        assertEquals("Invalid AgendaItem value",t10.agendaItem_2_Id,agendaItem.getWhenTrue().getId());
        assertEquals("Invalid AgendaItem value",t10.agendaItem_2_Id,agendaItem.getWhenTrueId());
        assertEquals("Invalid AgendaItem value",t10.agendaItem_1_Id,agendaItem.getWhenFalse().getId());
        assertEquals("Invalid AgendaItem value",t10.agendaItem_1_Id,agendaItem.getWhenFalseId());

        // update some of the agendaItem attributes  ( unset when true false and always
        agendaItem = ruleManagementService.getAgendaItem(t10.agendaItem_0_Id);
        agendaItemBuilder = AgendaItemDefinition.Builder.create(agendaItem);
        agendaItemBuilder.setWhenFalse(null);
        agendaItemBuilder.setWhenTrue(null);
        agendaItemBuilder.setAlways(null);
        ruleManagementService.updateAgendaItem(agendaItemBuilder.build());
        // check the update  ( should have removed when true and false
        agendaItem = ruleManagementService.getAgendaItem(t10.agendaItem_0_Id);
        assertEquals("Invalid AgendaItem value",null,agendaItem.getAlwaysId());
        assertEquals("Invalid AgendaItem value",null,agendaItem.getAlways());
        assertEquals("Invalid AgendaItem value",null,agendaItem.getWhenTrue());
        assertEquals("Invalid AgendaItem value",null,agendaItem.getWhenTrueId());
        assertEquals("Invalid AgendaItem value",null,agendaItem.getWhenFalse());
        assertEquals("Invalid AgendaItem value",null,agendaItem.getWhenFalseId());

        // update some of the agendaItem action attributes
        agendaItem = ruleManagementService.getAgendaItem(t10.agendaItem_7_Id);
        agendaItemBuilder = AgendaItemDefinition.Builder.create(agendaItem);
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(t10.actionAttribute_Key, t10.actionAttribute1_Value);
        for (ActionDefinition.Builder actionBuilder : agendaItemBuilder.getRule().getActions()) {
            actionBuilder.setAttributes(attributes);
        }
        ruleManagementService.updateAgendaItem(agendaItemBuilder.build());
        // check the update  ( should have changed the action attribute
        agendaItem = ruleManagementService.getAgendaItem(t10.agendaItem_7_Id);
        assertNotNull("Invalid AgendaItem Rule",agendaItem.getRule());
        assertNotNull("Invalid AgendaItem Rule Actions",agendaItem.getRule().getActions());
        assertEquals("Invalid AgendaItem Rule Actions count",1,agendaItem.getRule().getActions().size());
        for (ActionDefinition action : agendaItem.getRule().getActions()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("actionId", action.getId());
            Collection<ActionAttributeBo> actionAttributes = businessObjectService.findMatching(ActionAttributeBo.class, map);
            assertEquals("Invalid AgendaItem Rule Actions attribute count",1,actionAttributes.size());
            for (ActionAttributeBo actionAttribute : actionAttributes) {
                String expectedAttribute = t10.actionAttribute1_Value;
                String actualAttribute = actionAttribute.getValue();
                assertEquals("Invalid AgendaItem Rule Actions attribute",expectedAttribute,actualAttribute);
            }
        }
    }

    /**
     * Tests whether the {@code AgendaItemDefinition} cache is being evicted properly by checking the status the
     * dependent objects before and after creating an {@code AgendaItemDefinition} (and consequently emptying the cache).
     *
     * <p>
     * The following object caches are affected:
     * {@code AgendaTreeDefinition}, {@code AgendaDefinition}, {@code AgendaItemDefinition}, {@code ContextDefinition}
     * </p>
     */
    @Test
    public void testAgendaItemCacheEvict() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t11 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t11");

        verifyEmptyAgendaItem(t11);

        RuleDefinition ruleDefinition = buildTestRuleDefinition(t11.namespaceName, t11.object0);
        AgendaDefinition agendaDefinition = createTestAgenda(t11.object0);
        buildTestAgendaItemDefinition(t11.agendaItem_Id, agendaDefinition.getId(), ruleDefinition.getId());

        verifyFullAgendaItem(t11);
    }

    private void verifyEmptyAgendaItem(RuleManagementBaseTestObjectNames t) {
        AgendaItemDefinition agendaItem = ruleManagementService.getAgendaItem(t.agendaItem_Id);
        assertNull("AgendaItem is not null", agendaItem);

        AgendaDefinition agenda = ruleManagementService.getAgenda(t.agenda_Id);
        assertFalse("AgendaItem in Agenda found", agenda != null);

        boolean foundAgendaItem = false;
        ContextDefinition context = ruleManagementService.getContext(t.contextId);
        if (context != null) {
            for (AgendaDefinition contextAgenda : context.getAgendas()) {
                if (StringUtils.equals(t.agendaItem_Id, contextAgenda.getFirstItemId())) {
                    foundAgendaItem = true;
                    break;
                }
            }
        }
        assertFalse("AgendaItem in Context found", foundAgendaItem);

        AgendaTreeDefinition agendaTree = ruleManagementService.getAgendaTree(t.agenda_Id);
        assertFalse("AgendaItem in AgendaTree found", agendaTree != null);
    }

    private void verifyFullAgendaItem(RuleManagementBaseTestObjectNames t) {
        AgendaItemDefinition agendaItem = ruleManagementService.getAgendaItem(t.agendaItem_Id);
        assertNotNull("AgendaItem is null", agendaItem);

        AgendaDefinition agenda = ruleManagementService.getAgenda(t.agenda_Id);
        assertTrue("AgendaItem in Agenda not found", agenda != null);
        assertTrue("AgendaItem in Agenda not found", StringUtils.equals(t.agendaItem_Id, agenda.getFirstItemId()));

        boolean foundAgendaItem = false;
        ContextDefinition context = ruleManagementService.getContext(t.contextId);
        if (context != null) {
            for (AgendaDefinition contextAgenda : context.getAgendas()) {
                if (StringUtils.equals(t.agendaItem_Id, contextAgenda.getFirstItemId())) {
                    foundAgendaItem = true;
                    break;
                }
            }
        }
        assertTrue("AgendaItem in Context not found", foundAgendaItem);

        foundAgendaItem = false;
        AgendaTreeDefinition agendaTree = ruleManagementService.getAgendaTree(t.agenda_Id);
        if (agendaTree != null) {
            for (AgendaTreeEntryDefinitionContract agendaTreeEntry : agendaTree.getEntries()) {
                if (StringUtils.equals(t.agendaItem_Id, agendaTreeEntry.getAgendaItemId())) {
                    foundAgendaItem = true;
                    break;
                }
            }
        }
        assertTrue("AgendaItem in AgendaTree not found", foundAgendaItem);
    }
}
