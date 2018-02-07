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
import org.kuali.rice.krms.api.repository.LogicalOperator;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameter;
import org.kuali.rice.krms.api.repository.proposition.PropositionType;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;

/**
 *   RuleManagementPropositionDefinitionTest is to test the methods of ruleManagementServiceImpl relating to PropositionDefinitions
 *
 *   Each test focuses on one of the methods.
 */
public class RuleManagementPropositionDefinitionTest extends RuleManagementBaseTest {
    @Override
    @Before
    public void setClassDiscriminator() {
        // set a unique class discriminator for test objects of this class
        CLASS_DISCRIMINATOR = "RMPDT";
    }

    /**
     *  Test testCreateProposition()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .createProposition(PropositionDefinition) method
     */
    @Test
    public void testCreateProposition() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t0 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t0");

        PropositionDefinition propositionDefinition = createTestSimpleProposition(
                t0.namespaceName, t0.proposition_0_Id, "campusCode", "BL", "=", "java.lang.String", t0.rule_0_Id, "Campus Code");

        assertEquals("created Proposition not found", t0.rule_0_Id, propositionDefinition.getRuleId());

        try {
            // returnPropositionDefinition has existing propositionId
            ruleManagementService.createProposition(propositionDefinition);
            fail("should throw exception if trying to create and already exists");
        } catch (RiceIllegalArgumentException e) {
            //  throw new RiceIllegalArgumentException(propositionDefinition.getId());
        }
    }

    /**
     *  Test testCompoundCreateProposition()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .createProposition(PropositionDefinition) method
     */
    @Test
    public void testCompoundCreateProposition() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t1 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t1");

        // **************************
        // Create a complex Compound Proposition (True if Account is 54321 and Occasion is either a Conference or Training)
        // ***************************
        //  C1_compound_proposition            "COMPOUND" "S1_simple_proposition"  "C2_compound_proposition" "&"
        //      S1_simple_proposition          "SIMPLE"   "Account"                "54321"                   "="
        //      C2_compound_proposition        "COMPOUND" "S2_simple_proposition"  "S2_simple_proposition"   "|"
        //          S2_simple_proposition      "SIMPLE"   "Occasion"               "Conference"              "="
        //          S3_simple_proposition      "SIMPLE"   "Occasion"               "Training"                "="

        PropositionDefinition propS3 = createTestSimpleProposition(
                t1.namespaceName, "S3", "Occasion", "Training", "=", "java.lang.String", t1.rule_0_Id, "Special Event");
        PropositionDefinition.Builder propBuilderS3 = PropositionDefinition.Builder.create(propS3);

        PropositionDefinition propS2 = createTestSimpleProposition(
                t1.namespaceName, "S2", "Occasion", "Conference", "=", "java.lang.String", t1.rule_0_Id, "Special Event");
        PropositionDefinition.Builder propBuilderS2 = PropositionDefinition.Builder.create(propS2);

        PropositionDefinition propS1 = createTestSimpleProposition(
                t1.namespaceName, "S1", "Account", "54321", "=", "java.lang.String", t1.rule_0_Id, "Charged To Account");
        PropositionDefinition.Builder propBuilderS1 = PropositionDefinition.Builder.create(propS1);

        PropositionDefinition.Builder propBuilderC2 = PropositionDefinition.Builder.create(
                null,PropositionType.COMPOUND.getCode(),t1.rule_0_Id,null,new ArrayList<PropositionParameter.Builder>());

        propBuilderC2.compoundOpCode(LogicalOperator.OR.getCode());
        List<PropositionDefinition.Builder> compoundComponentsC2 = new ArrayList<PropositionDefinition.Builder>();
        compoundComponentsC2.add(propBuilderS2);
        compoundComponentsC2.add(propBuilderS3);
        propBuilderC2.setCompoundComponents(compoundComponentsC2);
        propBuilderC2.setDescription("C2_compound_proposition");

        PropositionDefinition.Builder propBuilderC1 = PropositionDefinition.Builder.create(
                null,PropositionType.COMPOUND.getCode(),t1.rule_0_Id,null,new ArrayList<PropositionParameter.Builder>());

        propBuilderC1.compoundOpCode(LogicalOperator.AND.getCode());
        List<PropositionDefinition.Builder> compoundComponentsC1 = new ArrayList<PropositionDefinition.Builder>();
        compoundComponentsC1.add(propBuilderS1);
        compoundComponentsC1.add(propBuilderC2);
        propBuilderC1.setCompoundComponents(compoundComponentsC1);
        propBuilderC1.setDescription("C1_compound_proposition");
        PropositionDefinition propC1 = ruleManagementService.createProposition(propBuilderC1.build());

        propC1 = ruleManagementService.getProposition(propC1.getId());
        assertEquals("proposition not in database","C1_compound_proposition",propC1.getDescription());

        List<String> propositionDescrs = Arrays.asList("C1_compound_proposition", "C2_compound_proposition",
                "S1_simple_proposition", "S2_simple_proposition", "S3_simple_proposition");

        Set<PropositionDefinition> propsFound = ruleManagementService.getPropositionsByRule(t1.rule_0_Id);
        for (PropositionDefinition propTemp : propsFound) {
            assertTrue(propositionDescrs.contains(propTemp.getDescription()));
        }

        assertEquals("invalid number of propositions found for ruleId", 5, propsFound.size());
    }

    /**
     *  Test testGetProposition()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getProposition("proposition id") method
     */
    @Test
    public void testGetProposition() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t2 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t2");

        PropositionDefinition propositionDefinition = createTestPropositionForRule(t2.object0);

        PropositionDefinition returnPropositionDefinition = ruleManagementService.getProposition(propositionDefinition.getId());

        //  match ruleIds from returned proposition returned by get by propositionId
        assertEquals("", propositionDefinition.getRuleId(), returnPropositionDefinition.getRuleId());

        try {
            ruleManagementService.getProposition(null);
            fail("should throw RiceIllegalArgumentException");
        } catch (RiceIllegalArgumentException e) {
            // throw new RiceIllegalArgumentException (id);
        }
    }

    /**
     *  Test testGetPropositionsByType()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getPropositionsByType("proposition type id") method
     */
    @Test
    public void testGetPropositionsByType() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t3 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t3");

        PropositionDefinition propositionDefinition = createTestPropositionForRule(t3.object0);

        Set<PropositionDefinition> propositionDefinitionSet = ruleManagementService.getPropositionsByType(propositionDefinition.getTypeId());

        boolean propositionFound = false;
        for ( PropositionDefinition pd : propositionDefinitionSet ) {
            if (pd.getId().equals(propositionDefinition.getId())){
                assertEquals("unexpected PropositionTypeId returned",propositionDefinition.getTypeId(),pd.getTypeId());
                propositionFound = true;
            }
        }

        assertTrue("proposition not found by PropositionTypeId",propositionFound);
    }

    /**
     *  Test testGetPropositionsByRule()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getPropositionsByRule("rule id") method
     */
    @Test
    public void testGetPropositionsByRule() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t4 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t4");

        PropositionDefinition propositionDefinition = createTestPropositionForRule(t4.object0);

        Set<PropositionDefinition> propositionDefinitionSet = ruleManagementService.getPropositionsByRule(
                propositionDefinition.getRuleId());

        boolean propositionFound = false;
        for ( PropositionDefinition pd : propositionDefinitionSet ) {
            if (pd.getId().equals(propositionDefinition.getId())){
                propositionFound = true;
            }
        }

        assertTrue("proposition not found by RuleId",propositionFound);
    }

    /**
     *  Test testUpdateProposition()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .updateProposition(PropositionDefinition) method
     */
    @Test
    public void testUpdateProposition() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t5 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t5");

        PropositionDefinition propositionDefinition = createTestPropositionForRule(t5.object0);
        PropositionDefinition.Builder builder = PropositionDefinition.Builder.create(propositionDefinition);
        builder.setDescription("UpdatedDescription");
        builder.setPropositionTypeCode(PropositionType.COMPOUND.getCode());

        // focus of test is on this instruction
        ruleManagementService.updateProposition(builder.build());

        PropositionDefinition returnPropositionDefinition = ruleManagementService.getProposition(propositionDefinition.getId());

        assertEquals("description was not updated", "UpdatedDescription", returnPropositionDefinition.getDescription());
        assertEquals("propositionType was not updated", PropositionType.COMPOUND.getCode(), returnPropositionDefinition.getPropositionTypeCode());
    }

    /**
     *  Test testDeleteProposition()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .deleteProposition("proposition id") method
     */
    @Test
    public void testDeleteProposition() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t6 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t6");

        PropositionDefinition propositionDefinition = createTestPropositionForRule(t6.object0);

        ruleManagementService.deleteProposition(propositionDefinition.getId());

        assertTrue("proposition should have been deleted", ruleManagementService.getPropositionsByRule(propositionDefinition.getRuleId()).isEmpty());

        try {
            ruleManagementService.deleteProposition(propositionDefinition.getId());
            fail("should fail with IllegalStateException: the Proposition to delete does not exists");
        } catch (IllegalStateException e) {
            // IllegalStateException: the Proposition to delete does not exists
        }
    }

    /**
     *  Test testFindPropositionIds()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .findPropositionIds(QueryByCriteria) method
     */
    @Test
    public void testFindPropositionIds() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t6 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t6");

        List<String> propositionIds = Arrays.asList(
                createTestPropositionForRule(t6.object0).getId(),
                createTestPropositionForRule(t6.object1).getId(),
                createTestPropositionForRule(t6.object2).getId(),
                createTestPropositionForRule(t6.object3).getId());

        for (String propositionId : propositionIds) {
            PropositionDefinition.Builder builder = PropositionDefinition.Builder.create(
                    ruleManagementService.getProposition(propositionId));
            builder.setDescription("targetOfQuery");
            ruleManagementService.updateProposition(builder.build());
        }

        QueryByCriteria.Builder query = QueryByCriteria.Builder.create();
        query.setPredicates(equal("description", "targetOfQuery"));

        List<String> returnedPropositionIds = ruleManagementService.findPropositionIds(query.build());

        for (String returnedPropositionId : returnedPropositionIds ) {
            assertTrue(propositionIds.contains(returnedPropositionId));
        }

        assertEquals("incorrect number of Propositions found", 4, returnedPropositionIds.size());
    }

    /**
     * Tests whether the {@code PropositionDefinition} cache is being evicted properly by checking the status the
     * dependent objects before and after creating an {@code PropositionDefinition} (and consequently emptying the cache).
     *
     * <p>
     * The following object caches are affected:
     * {@code PropositionDefinition}, {@code RuleDefinition}
     * </p>
     */
    @Test
    public void testPropositionCacheEvict() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t7 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t7");

        verifyEmptyProposition(t7);

        // Proposition is built as part of a Rule
        buildTestRuleDefinition(t7.namespaceName, t7.object0);

        verifyFullProposition(t7);
    }

    private void verifyEmptyProposition(RuleManagementBaseTestObjectNames t) {
        Set<PropositionDefinition> propositions = ruleManagementService.getPropositionsByRule(t.rule_Id);
        assertFalse("Proposition is not null", propositions != null && !propositions.isEmpty());

        RuleDefinition rule = ruleManagementService.getRule(t.rule_Id);
        assertFalse("Proposition in Rule found", rule != null);
    }

    private void verifyFullProposition(RuleManagementBaseTestObjectNames t) {
        Set<PropositionDefinition> propositions = ruleManagementService.getPropositionsByRule(t.rule_Id);
        assertTrue("Proposition is not null", propositions != null && !propositions.isEmpty());

        String propositionId = new ArrayList<PropositionDefinition>(propositions).get(0).getId();

        RuleDefinition rule = ruleManagementService.getRule(t.rule_Id);
        assertTrue("Proposition in Rule not found", rule != null);
        assertTrue("Proposition in Rule not found", StringUtils.equals(propositionId, rule.getPropId()));
    }
}
