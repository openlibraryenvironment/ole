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
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.impl.repository.ActionAttributeBo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;
import static org.kuali.rice.core.api.criteria.PredicateFactory.in;


/**
 *   RuleManagementRuleDefinitionTest is to test the methods of ruleManagementServiceImpl relating to RuleDefinitions
 *
 *   Each test focuses on one of the methods.
 */
public class RuleManagementRuleDefinitionTest  extends RuleManagementBaseTest{
    @Override
    @Before
    public void setClassDiscriminator() {
        // set a unique discriminator for test objects of this class
        CLASS_DISCRIMINATOR = "RMRDT";
    }

    /**
     *  Test testGetRuleByNameAndNamespace()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getRuleByNameAndNamespace("rule name", "namespace") method
     */
    @Test
    public void testGetRuleByNameAndNamespace() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t0 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t0");

        RuleDefinition ruleDefinition = buildTestRuleDefinition(t0.namespaceName, t0.object0);

        RuleDefinition returnRuleDefinition = ruleManagementService.getRuleByNameAndNamespace(
                ruleDefinition.getName(), ruleDefinition.getNamespace());

        assertEquals("rule not found", ruleDefinition.getId(), returnRuleDefinition.getId());

        // try getRuleByNameAndNamespace with null Name parameter
        try {
            ruleManagementService.getRuleByNameAndNamespace(null, t0.namespaceName);
            fail("Should have thrown IllegalArgumentException: name is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: name is null or blank
        }

        // try getRuleByNameAndNamespace with null Name parameter
        try {
            ruleManagementService.getRuleByNameAndNamespace("   ", t0.namespaceName);
            fail("Should have thrown IllegalArgumentException: name is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: name is null or blank
        }

        // try getRuleByNameAndNamespace with null namespace parameter
        try {
            ruleManagementService.getRuleByNameAndNamespace(ruleDefinition.getName(),null);
            fail("should throw IllegalArgumentException: namespace is null or blank");
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException: namespace is null or blank
        }

        // try getRuleByNameAndNamespace with blank namespace parameter
        try {
            ruleManagementService.getRuleByNameAndNamespace(ruleDefinition.getName(),"    ");
            fail("should throw IllegalArgumentException: namespace is null or blank");
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException: namespace is null or blank
        }


    }

    /**
     *  Test testCreateRule()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .createRule(RuleDefinition) method
     */
    @Test
    public void testCreateRule() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t1 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t1");

        // create a Rule
        RuleDefinition ruleFirstCreate = buildTestRuleDefinition(t1.namespaceName, t1.object0);
        assertTrue("created Rule not found", ruleManagementService.getRule(ruleFirstCreate.getId()).getId().contains(t1.rule_0_Id));

        // try to create a duplicate Rule
        try {
            RuleDefinition ruleSecondCreate = ruleManagementService.createRule(ruleFirstCreate);
            fail("should have thrown RiceIllegalArgumentException");
        } catch (RiceIllegalArgumentException e) {
            //  throw new RiceIllegalArgumentException(ruleDefinition.getId());
        }

        // try to create a malformed Rule
        RuleDefinition malformedRule = buildTestRuleDefinition(t1.namespaceName, t1.object1);
        RuleDefinition.Builder builder = RuleDefinition.Builder.create(malformedRule);
        builder.setPropId("invalidValue");
        malformedRule =  builder.build();
        try {
            ruleManagementService.createRule(malformedRule);
            fail("should have thrown RiceIllegalArgumentException");
        } catch (RiceIllegalArgumentException e) {
            // throw new RiceIllegalArgumentException("propId does not match proposition.getId"
        }
    }

    /**
     *  Test testUpdateRule()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .updateRule(RuleDefinition) method
     */
    @Test
    public void testUpdateRule() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t2 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t2");

        // build a rule to test with
        RuleDefinition.Builder ruleBuilder0 = RuleDefinition.Builder.create(buildTestRuleDefinition(t2.namespaceName,
                t2.object0));

        // update the rule's Name
        ruleBuilder0.setName("updatedName");
        ruleManagementService.updateRule(ruleBuilder0.build());

        // verify update
        RuleDefinition rule0 = ruleManagementService.getRule(t2.rule_0_Id);
        assertNotEquals("Rule Name Not Updated", t2.rule_0_Name, rule0.getName());
        assertEquals("Rule Name Not Updated", "updatedName", rule0.getName());

        // build new rule for test
        RuleDefinition.Builder ruleBuilder1 = RuleDefinition.Builder.create(buildTestRuleDefinition(t2.namespaceName,
                t2.object1));
        assertEquals("Expected Proposition not found in Rule",t2.proposition_1_Descr,ruleBuilder1.getProposition().getDescription());

        // create new proposition to update rule with
        String newPropId = "PropNewId";
        PropositionDefinition prop = createTestSimpleProposition(t2.namespaceName, newPropId, "TSI_"+newPropId,
                "ABC", "=", "java.lang.String", t2.rule_0_Id, "TSI_" + newPropId + "_Descr");
        PropositionDefinition.Builder propBuilder = PropositionDefinition.Builder.create(prop);
        ruleBuilder1.setPropId(newPropId);
        ruleBuilder1.setProposition(propBuilder);

        // Update Proposition in rule
        ruleManagementService.updateRule(ruleBuilder1.build());
        rule0 = ruleManagementService.getRule(ruleBuilder1.getId());
        assertEquals("Expected Proposition not found in Rule","PropNewId_simple_proposition",rule0.getProposition().getDescription());

        // build new rule for test
        RuleDefinition.Builder ruleBuilder2 = RuleDefinition.Builder.create(buildTestRuleDefinition(t2.namespaceName,t2.object7));
        createTestKrmsAttribute(t2.actionAttribute, t2.actionAttribute_Key, t2.namespaceName);
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(t2.actionAttribute_Key, t2.actionAttribute_Value);
        List<ActionDefinition.Builder> actionBuilders = new ArrayList<ActionDefinition.Builder>();
        KrmsTypeDefinition krmsTypeDefinition = createKrmsActionTypeDefinition(t2.namespaceName);
        ActionDefinition.Builder actionBuilder = ActionDefinition.Builder.create(t2.action_Id, t2.action_Name,
                t2.namespaceName, krmsTypeDefinition.getId(), rule0.getId(), 1);
        actionBuilder.setDescription(t2.action_Descr);
        actionBuilder.setAttributes(attributes);
        actionBuilders.add(actionBuilder);
        ruleBuilder2.setActions(actionBuilders);
        ruleManagementService.updateRule(ruleBuilder2.build());

        // Update Action in rule
        ruleBuilder2 = RuleDefinition.Builder.create(ruleManagementService.getRule(t2.rule_7_Id));
        Map<String, String> newAttributes = new HashMap<String, String>();
        newAttributes.put(t2.actionAttribute_Key, t2.actionAttribute1_Value);
        for (ActionDefinition.Builder ruleActionBuilder : ruleBuilder2.getActions()) {
            ruleActionBuilder.setAttributes(newAttributes);
        }
        ruleManagementService.updateRule(ruleBuilder2.build());
        rule0 = ruleManagementService.getRule(t2.rule_7_Id);
        assertEquals("Invalid AgendaItem Rule Actions count",1,rule0.getActions().size());
        for (ActionDefinition action : rule0.getActions()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("actionId", action.getId());
            Collection<ActionAttributeBo> actionAttributes = businessObjectService.findMatching(ActionAttributeBo.class, map);
            assertEquals("Invalid AgendaItem Rule Actions attribute count",1,actionAttributes.size());
            for (ActionAttributeBo actionAttribute : actionAttributes) {
                String expectedAttribute = t2.actionAttribute1_Value;
                String actualAttribute = actionAttribute.getValue();
                assertEquals("Invalid AgendaItem Rule Actions attribute",expectedAttribute,actualAttribute);
            }
        }
    }

    /**
     *  Test testDeleteRule()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .deleteRule("rule id") method
     */
    @Test
    public void testDeleteRule() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t3 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t3");

        // create a Rule
        RuleDefinition rule = buildTestRuleDefinition(t3.namespaceName, t3.object0);
        assertTrue("created Rule not found", ruleManagementService.getRule(rule.getId()).getId().contains(t3.rule_0_Id));
        String propositionId = rule.getPropId();
        assertEquals("Proposition for Rule not found", t3.proposition_0_Descr,
                ruleManagementService.getProposition(propositionId).getDescription());


        ruleManagementService.deleteRule(rule.getId());

        assertNull("Rule was not deleted", ruleManagementService.getRule(rule.getId()));

        // make sure proposition was cleaned up when rule was deleted
        try {
            ruleManagementService.deleteProposition(propositionId);
            fail("should fail with IllegalStateException: the Proposition to delete does not exists");
        } catch (IllegalStateException e) {
            // IllegalStateException: the Proposition to delete does not exists
        }
    }

    /**
     *  Test testFindRuleIds()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .findRuleIds( QueryByCriteria) method
     */
    @Test
    public void testFindRuleIds() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t4 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t4");

        RuleDefinition rule0 = buildTestRuleDefinition(t4.namespaceName, t4.object0);
        RuleDefinition rule1 = buildTestRuleDefinition(t4.namespaceName, t4.object1);
        RuleDefinition rule2 = buildTestRuleDefinition(t4.namespaceName, t4.object2);
        RuleDefinition rule3 = buildTestRuleDefinition(t4.namespaceName, t4.object3);
        String ruleNameSpace = rule0.getNamespace();
        List<String> ruleNames =  new ArrayList<String>();
        ruleNames.add(rule0.getName());
        ruleNames.add(rule1.getName());
        ruleNames.add(rule2.getName());
        ruleNames.add(rule3.getName());

        QueryByCriteria.Builder builder = QueryByCriteria.Builder.create();

        builder.setPredicates(equal("namespace", ruleNameSpace), in("name", ruleNames.toArray(new String[]{})));

        List<String> ruleIds = ruleManagementService.findRuleIds(builder.build());
        assertEquals("Wrong number of RuleIds returned", 4, ruleIds.size());

        if(!ruleIds.contains(rule0.getId())){
            fail("RuleId not found in results");
        }
    }

    /**
     *  Test testGetRule()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getRule("rule id") method
     */
    @Test
    public void testGetRule() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t5 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t5");

        // create a rule to test with
        RuleDefinition ruleDefinition = buildTestRuleDefinition(t5.namespaceName, t5.object0);

        assertNotNull(ruleManagementService.getRule(ruleDefinition.getId()));

        assertNull("Should have returned null", ruleManagementService.getRule(null));
        assertNull("Should have returned null", ruleManagementService.getRule("   "));
        assertNull("Should have returned null", ruleManagementService.getRule("badValueId"));
    }

    /**
     *  Test testGetRules()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getRules(List<String> ruleIds) method
     */
    @Test
    public void testGetRules() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t6 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t6");

        // build two rules for testing
        buildTestRuleDefinition(t6.namespaceName, t6.object0);
        buildTestRuleDefinition(t6.namespaceName, t6.object1);

        // build List rule ids for the rules created
        List<String> ruleIds = new ArrayList<String>();
        ruleIds.add(t6.rule_0_Id);
        ruleIds.add(t6.rule_1_Id);

        // get test rules by List of rule ids
        List<RuleDefinition> ruleDefinitions = ruleManagementService.getRules(ruleIds);
        assertEquals("Two RuleDefintions should have been returned",2,ruleDefinitions.size());

        for(RuleDefinition ruleDefinition : ruleDefinitions) {
            if (!ruleIds.contains(ruleDefinition.getId())) {
                fail("Invalid RuleDefinition returned");
            }
        }

        try {
            ruleManagementService.getRules(null);
            fail("Should have failed with RiceIllegalArgumentException: ruleIds must not be null");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: ruleIds must not be null
        }

        assertEquals("No RuleDefinitions should have been returned",0,
                ruleManagementService.getRules(new ArrayList<String>()).size());

        ruleIds = Arrays.asList("badValueId");
        assertEquals("No RuleDefinitions should have been returned",0, ruleManagementService.getRules(ruleIds).size());
    }

    /**
     * Tests whether the {@code RuleDefinition} cache is being evicted properly by checking the status the dependent
     * objects before and after creating an {@code RuleDefinition} (and consequently emptying the cache).
     *
     * <p>
     * The following object caches are affected:
     * {@code RuleDefinition}, {@code PropositionDefinition}, {@code ActionDefinition}, {@code AgendaItemDefinition}
     * </p>
     */
    @Test
    public void testRuleCacheEvict() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t7 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t7");

        verifyEmptyRule(t7);

        String ruleId = buildTestRuleDefinition(t7.namespaceName, t7.object0).getId();
        buildTestActionDefinition(t7.action_Id, t7.action_Name, t7.action_Descr, 1, ruleId, t7.namespaceName);
        String agendaId = createTestAgenda(t7.object0).getId();
        buildTestAgendaItemDefinition(t7.agendaItem_Id, agendaId, ruleId);

        verifyFullRule(t7);
    }

    private void verifyEmptyRule(RuleManagementBaseTestObjectNames t) {
        RuleDefinition rule = ruleManagementService.getRule(t.rule_Id);
        assertNull("Rule is not null", rule);

        Set<PropositionDefinition> propositions = ruleManagementService.getPropositionsByRule(t.rule_Id);
        assertFalse("Rule in Proposition found", propositions != null && !propositions.isEmpty());

        ActionDefinition action = ruleManagementService.getAction(t.action_Id);
        assertFalse("Rule in Action found", action != null);

        AgendaItemDefinition agendaItem = ruleManagementService.getAgendaItem(t.agendaItem_Id);
        assertFalse("Rule in AgendaItem found", agendaItem != null);
    }

    private void verifyFullRule(RuleManagementBaseTestObjectNames t) {
        RuleDefinition rule = ruleManagementService.getRule(t.rule_Id);
        assertNotNull("Rule is null", rule);

        boolean foundRule = false;
        Set<PropositionDefinition> propositions = ruleManagementService.getPropositionsByRule(t.rule_Id);
        if (propositions != null) {
            for (PropositionDefinition proposition : propositions) {
                if (StringUtils.equals(t.rule_Id, proposition.getRuleId())) {
                    foundRule = true;
                    break;
                }
            }
        }
        assertTrue("Rule in Proposition not found", foundRule);

        ActionDefinition action = ruleManagementService.getAction(t.action_Id);
        assertTrue("Rule in Action not found", action != null);
        assertTrue("Rule in Action not found", StringUtils.equals(t.rule_Id, action.getRuleId()));

        AgendaItemDefinition agendaItem = ruleManagementService.getAgendaItem(t.agendaItem_Id);
        assertTrue("Rule in AgendaItem not found", agendaItem != null);
        assertTrue("Rule in AgendaItem not found", StringUtils.equals(t.rule_Id, agendaItem.getRuleId()));
    }
}
