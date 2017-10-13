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
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;

/**
 *   RuleManagementActionDefinitionTest is to test the methods of ruleManagementServiceImpl relating to krms Actions
 *
 *   Each test focuses on one of the methods.
 */
    public class RuleManagementActionDefinitionTest extends RuleManagementBaseTest {

    @Override
    @Before
    public void setClassDiscriminator() {
        // set a unique discriminator for test objects of this class
        CLASS_DISCRIMINATOR = "RMADT";
    }

    /**
     *  Test testCreateAction()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .createAction(ActionDefinition) method
     */
    @Test
    public void testCreateAction() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t0 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t0");

        KrmsTypeDefinition krmsTypeDefinition = createKrmsActionTypeDefinition(t0.namespaceName);
        RuleDefinition ruleDefintion = buildTestRuleDefinition(t0.namespaceName, t0.discriminator);

        ActionDefinition actionDefinition = ActionDefinition.Builder.create(t0.action0_Id, t0.action0_Name,
                t0.namespaceName,krmsTypeDefinition.getId(),ruleDefintion.getId(),1).build();

        assertNull("action should not be in database", ruleManagementService.getAction(t0.action0_Id));

        // primary statement for test
        actionDefinition =  ruleManagementService.createAction(actionDefinition);

        ActionDefinition returnActionDefinition = ruleManagementService.getAction(actionDefinition.getId());

        assertNotNull("created action not found", (Object) returnActionDefinition);
        assertEquals("create action error:", t0.action0_Id, returnActionDefinition.getId());
    }

    /**
     *  Test testUpdateAction()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .testUpdateAction(ActionDefinition) method
     */
    @Test
    public void testUpdateAction() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t1 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t1");

        KrmsTypeDefinition krmsTypeDefinition = createKrmsActionTypeDefinition(t1.namespaceName);
        RuleDefinition ruleDefinition = buildTestRuleDefinition(t1.namespaceName, t1.object0);

        ActionDefinition actionDefinition = ActionDefinition.Builder.create(t1.action0_Id,t1.action0_Name,
                t1.namespaceName,krmsTypeDefinition.getId(),ruleDefinition.getId(),1).build();

        assertNull("action should not be in database", ruleManagementService.getAction(t1.action0_Id));

        actionDefinition =  ruleManagementService.createAction(actionDefinition);

        ActionDefinition returnActionDefinition = ruleManagementService.getAction(actionDefinition.getId());
        ActionDefinition.Builder builder = ActionDefinition.Builder.create(returnActionDefinition);
        builder.setDescription("ChangedDescr");

        // primary statement for test
        ruleManagementService.updateAction(builder.build());

        returnActionDefinition = ruleManagementService.getAction(actionDefinition.getId());

        assertNotNull("action not found", returnActionDefinition);
        assertEquals("update action error:","ChangedDescr", returnActionDefinition.getDescription());
    }

    /**
     *  Test testDeleteAction()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .testDeleteAction(ActionDefinition) method
     */
    @Test
    public void testDeleteAction() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t2 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t2");

        KrmsTypeDefinition krmsTypeDefinition = createKrmsActionTypeDefinition(t2.namespaceName);
        RuleDefinition ruleDefintion = buildTestRuleDefinition(t2.namespaceName, t2.object0);

        ActionDefinition actionDefinition = ActionDefinition.Builder.create(t2.action0_Id,t2.action0_Name,
                t2.namespaceName,krmsTypeDefinition.getId(),ruleDefintion.getId(),1).build();

        assertNull("action should not be in database", ruleManagementService.getAction(t2.action0_Id));

        actionDefinition =  ruleManagementService.createAction(actionDefinition);
        actionDefinition = ruleManagementService.getAction(actionDefinition.getId());
        assertNotNull("action not found", ruleManagementService.getAction(actionDefinition.getId()));

        try {
            // primary statement for test
            ruleManagementService.deleteAction(t2.action0_Id);
            fail("should fail deleteAction not implemented");
        }   catch (RiceIllegalArgumentException e) {
            // RiceIllegalArgumentException ("not implemented yet because not supported by the bo service");
        }

        actionDefinition = ruleManagementService.getAction(actionDefinition.getId());
        assertNotNull("action not found", (Object) actionDefinition);
    }

    /**
     *  Test testGetAction()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .testGetAction(Action_Id) method
     */
    @Test
    public void testGetAction() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t3 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t3");

        KrmsTypeDefinition krmsTypeDefinition = createKrmsActionTypeDefinition(t3.namespaceName);
        RuleDefinition ruleDefintion = buildTestRuleDefinition(t3.namespaceName, t3.object0);

        ActionDefinition actionDefinition = ActionDefinition.Builder.create(t3.action0_Id,t3.action0_Name,
                t3.namespaceName,krmsTypeDefinition.getId(),ruleDefintion.getId(),1).build();

        assertNull("action should not be in database", ruleManagementService.getAction(t3.action0_Id));
        actionDefinition =  ruleManagementService.createAction(actionDefinition);

        // primary statement being tested
        ActionDefinition returnActionDefinition = ruleManagementService.getAction(actionDefinition.getId());

        assertNotNull("action not found", (Object) returnActionDefinition);
        assertEquals("getAction error:", t3.action0_Id, returnActionDefinition.getId());

    }

    /**
     *  Test testGetActions()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .testGetActions(List<Action_Id>) method
     */
    @Test
    public void testGetActions() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t4 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t4");

        RuleDefinition ruleDefinition0 = buildTestRuleDefinition(t4.namespaceName, t4.object0);
        RuleDefinition ruleDefinition1 = buildTestRuleDefinition(t4.namespaceName, t4.object1);
        RuleDefinition ruleDefinition2 = buildTestRuleDefinition(t4.namespaceName, t4.object2);
        RuleDefinition ruleDefinition3 = buildTestRuleDefinition(t4.namespaceName, t4.object3);

        buildTestActionDefinition(t4.action0_Id, t4.action0_Name, t4.action0_Descr, 1, ruleDefinition0.getId(),
                t4.namespaceName);
        buildTestActionDefinition(t4.action1_Id, t4.action1_Name, t4.action1_Descr, 1, ruleDefinition1.getId(),
                t4.namespaceName);
        buildTestActionDefinition(t4.action2_Id, t4.action2_Name, t4.action2_Descr, 1, ruleDefinition2.getId(),
                t4.namespaceName);
        buildTestActionDefinition(t4.action3_Id, t4.action3_Name, t4.action3_Descr, 1, ruleDefinition3.getId(),
                t4.namespaceName);
        List<String> actionIds = Arrays.asList(t4.action0_Id, t4.action1_Id, t4.action2_Id, t4.action3_Id);

        // primary statement being tested
        List<ActionDefinition> returnActionDefinitions = ruleManagementService.getActions(actionIds);

        assertEquals("incorrect number of actions returned",4,returnActionDefinitions.size());

        // count the returned actions, returnActionDefinitions.size() may reflect nulls for not found
        int actionsFound = 0;
        for( ActionDefinition actionDefinition : returnActionDefinitions ) {
            if(actionIds.contains(actionDefinition.getId())) {
                actionsFound++;
            }
        }

        assertEquals("incorrect number of actions returned",4,actionsFound);
        assertEquals("action not found",t4.action0_Descr, ruleManagementService.getAction(t4.action0_Id).getDescription());
        assertEquals("action not found",t4.action1_Descr, ruleManagementService.getAction(t4.action1_Id).getDescription());
        assertEquals("action not found",t4.action2_Descr, ruleManagementService.getAction(t4.action2_Id).getDescription());
        assertEquals("action not found",t4.action3_Descr, ruleManagementService.getAction(t4.action3_Id).getDescription());
    }

    /**
     *  Test testFindActionIds()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .testFindActionIds(QueryByCriteria) method
     */
    @Test
    public void testFindActionIds() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t5 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t5");
        RuleDefinition ruleDefinition = buildTestRuleDefinition(t5.namespaceName, t5.object0);
        buildTestActionDefinition(t5.action0_Id, t5.action0_Name, t5.action0_Descr, 1, ruleDefinition.getId(),
                t5.namespaceName);

        QueryByCriteria.Builder builder = QueryByCriteria.Builder.create();
        builder.setPredicates(equal("name", t5.action0_Name));

        List<String> actionIds = ruleManagementService.findActionIds(builder.build());

        if(!actionIds.contains(t5.action0_Id)){
            fail("actionId not found");
        }
    }

    /**
     * Tests whether the {@code ActionDefinition} cache is being evicted properly by checking the status the
     * dependent objects before and after creating an {@code ActionDefinition} (and consequently emptying the cache).
     *
     * <p>
     * The following object caches are affected:
     * {@code ActionDefinition}, {@code RuleDefinition}
     * </p>
     */
    @Test
    public void testActionCacheEvict() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t6 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t6");

        verifyEmptyAction(t6);

        RuleDefinition ruleDefinition = buildTestRuleDefinition(t6.namespaceName, t6.object0);
        buildTestActionDefinition(t6.action_Id, t6.action_Name, t6.action_Descr, 1, ruleDefinition.getId(), t6.namespaceName);

        verifyFullAction(t6);
    }

    private void verifyEmptyAction(RuleManagementBaseTestObjectNames t) {
        ActionDefinition action = ruleManagementService.getAction(t.action_Id);
        assertNull("Action is not null", action);

        RuleDefinition rule = ruleManagementService.getRule(t.rule_Id);
        assertFalse("Action in Rule found", rule != null);
    }

    private void verifyFullAction(RuleManagementBaseTestObjectNames t) {
        ActionDefinition action = ruleManagementService.getAction(t.action_Id);
        assertNotNull("Action is null", action);

        boolean foundRule = false;
        RuleDefinition rule = ruleManagementService.getRule(t.rule_Id);
        if (rule != null) {
            for (ActionDefinition ruleAction : rule.getActions()) {
                if (StringUtils.equals(t.rule_Id, ruleAction.getRuleId())) {
                    foundRule = true;
                    break;
                }
            }
        }
        assertTrue("Action in Rule not found", foundRule);
    }
}
