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
package org.kuali.rice.kew.rule;

import mocks.MockDocumentRefreshQueueImpl;
import org.junit.Test;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.test.BaselineTestCase;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests adding a delegation rule
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.CLEAR_DB)
public class AddRuleDelegationTest extends KEWTestCase {

	private static final String DELEGATE_USER = "user2";
	private static final String DELEGATE_USER2 = "pmckown";

	private static final String DOCTYPE = "AddDelegationTest_DocType";
	private static final String RULE_TEMPLATE = "AddDelegationTest_RuleTemplate";
	private static final String DELEGATION_TEMPLATE = "AddDelegationTest_DelegationTemplate";

	protected void loadTestData() throws Exception {
		loadXmlFile("AddRuleDelegationTestData.xml");
	}

	/**
	 *
	 * Tests that adding a delegation for a rule for which a document has a pending action request causes
	 * the document to be requeued. See KULRICE-3575
	 *
	 * @throws Exception
	 */
    @Test public void testNewDelegationTriggersRequeue() throws Exception {
    	String docType = "RiceDocument.testNewDelegationTriggersRequeue";

    	// route a document of this type
    	WorkflowDocument wd = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), DOCTYPE);
    	wd.route("");

    	// clear the current set of requeued document ids
		MockDocumentRefreshQueueImpl.clearRequeuedDocumentIds();

    	// create and save a rule delegation
		RuleTestUtils.createDelegationToUser(DOCTYPE, RULE_TEMPLATE, DELEGATE_USER);

		assertTrue("our document should have been requeued!",
				MockDocumentRefreshQueueImpl.getRequeuedDocumentIds().contains(wd.getDocumentId()));
    }


	/**
	 * Tests adding a delegation rule.  The implementation is mostly a cut-and-paste copy of
	 * createDelegateRule and routeRule methods from DelegatRule2Action Struts action.
	 */
	@Test
	public void testAddRuleDelegation() throws Exception {

		RuleBaseValues originalRule = RuleTestUtils.getRule(DOCTYPE, RULE_TEMPLATE);

    	List<RuleResponsibilityBo> originalResps = originalRule.getRuleResponsibilities();
    	assertTrue("assuming there is 1 responsibility", originalResps != null && originalResps.size() == 1);

    	RuleResponsibilityBo originalResp = originalResps.get(0);

		RuleTestUtils.createDelegationToUser(DOCTYPE, RULE_TEMPLATE, DELEGATE_USER);

		Principal principal2 = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(DELEGATE_USER);

		// check the original rule, it should be the same (i.e. not be re-versioned as KEW used to do pre 1.0 when a delegate was added)
		originalRule = KEWServiceLocator.getRuleService().findRuleBaseValuesById(originalRule.getId());
		assertTrue("Original rule should be current.", originalRule.getCurrentInd());
		List<RuleResponsibilityBo> responsibilities = originalRule.getRuleResponsibilities();
		originalResp = responsibilities.get(0);
		assertEquals("Original rule should have 1 delegation now.", 1, originalResp.getDelegationRules().size());

		List<RuleDelegationBo> newRuleDelegations = KEWServiceLocator.getRuleDelegationService().findByResponsibilityId(originalResp.getResponsibilityId());
		assertEquals("Should be 1 delegation", 1, newRuleDelegations.size());

		RuleDelegationBo newRuleDelegation = newRuleDelegations.get(0);
		assertEquals("Incorrect responsibility id", originalResp.getResponsibilityId(), newRuleDelegation.getResponsibilityId());
		assertNotNull("Name should not be null", newRuleDelegation.getDelegationRule().getName());
		assertTrue("delegate rule should be current", newRuleDelegation.getDelegationRule().getCurrentInd());
		assertTrue("delegate rule should be flagged as a delegate", newRuleDelegation.getDelegationRule().getDelegateRule());
		assertEquals("Should have 1 responsibility", 1, newRuleDelegation.getDelegationRule().getRuleResponsibilities().size());
		assertEquals("Incorrect responsibility name", principal2.getPrincipalId(), newRuleDelegation.getDelegationRule().getRuleResponsibilities().get(0).getRuleResponsibilityName());
		assertEquals("Incorrect responsibility type", KewApiConstants.RULE_RESPONSIBILITY_WORKFLOW_ID, newRuleDelegation.getDelegationRule().getRuleResponsibilities().get(0).getRuleResponsibilityType());
		assertEquals("Incorrect delegation type", DelegationType.PRIMARY, newRuleDelegation.getDelegationType());


		/**
		 * Let's add another delegate rule.
		 */

		Principal delegatePrincipal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(DELEGATE_USER2);

		// let's save the new rule delegation
		RuleTestUtils.createRuleDelegationToUser(originalRule, originalResp, delegatePrincipal);

		List<RuleDelegationBo> ruleDelegations = KEWServiceLocator.getRuleDelegationService().findByResponsibilityId(originalResp.getResponsibilityId());
		assertEquals("There should be 2 delegation rules", 2, ruleDelegations.size());
		boolean foundFirstDelegateRule = false;
		for (RuleDelegationBo ruleDelegation : ruleDelegations) {
			if (ruleDelegation.getRuleDelegationId().equals(newRuleDelegation.getRuleDelegationId())) {
				foundFirstDelegateRule = true;
				assertEquals("Rule Version should not have changed.", ruleDelegation.getVersionNumber(), newRuleDelegation.getVersionNumber());
			} else {
				// this should be our new rule delegation
				assertEquals("Incorrect responsibility id", originalResp.getResponsibilityId(), ruleDelegation.getResponsibilityId());
				assertNotNull("Name should not be null", ruleDelegation.getDelegationRule().getName());
				assertTrue("delegate rule should be current", ruleDelegation.getDelegationRule().getCurrentInd());
				assertTrue("delegate rule should be flagged as a delegate", ruleDelegation.getDelegationRule().getDelegateRule());
				assertEquals("Should have 1 responsibility", 1, ruleDelegation.getDelegationRule().getRuleResponsibilities().size());
				assertEquals("Incorrect responsibility name", delegatePrincipal.getPrincipalId(), ruleDelegation.getDelegationRule().getRuleResponsibilities().get(0).getRuleResponsibilityName());
				assertEquals("Incorrect responsibility type", KewApiConstants.RULE_RESPONSIBILITY_WORKFLOW_ID, ruleDelegation.getDelegationRule().getRuleResponsibilities().get(0).getRuleResponsibilityType());
			}
		}
		assertTrue("Failed to find the first delegate rule", foundFirstDelegateRule);

		/**
		 *  now let's try editing our first delegate rule
		 */

		String newRuleDelegationId = newRuleDelegation.getRuleDelegationId();
		// change the delegation type to secondary
		newRuleDelegation.setDelegationType(DelegationType.SECONDARY);
		saveNewVersion(newRuleDelegation);
		String newRuleDelegationId2 = newRuleDelegation.getRuleDelegationId();

		// let's check the original and verify that its been re-versioned
		newRuleDelegation = KEWServiceLocator.getRuleDelegationService().findByRuleDelegationId(newRuleDelegationId);
		assertNotNull(newRuleDelegation);
		assertFalse("Rule delegation should no longer be current.", newRuleDelegation.getDelegationRule().getCurrentInd());

		// there should still be 2 rule delegations, however one of them has been reversioned
		ruleDelegations = KEWServiceLocator.getRuleDelegationService().findByResponsibilityId(originalResp.getResponsibilityId());
		assertEquals("There should be 2 delegation rules", 2, ruleDelegations.size());
		boolean foundReversionedDelegateRule = false;
		for (RuleDelegationBo ruleDelegation : ruleDelegations) {
			if (ruleDelegation.getRuleDelegationId().equals(newRuleDelegationId2)) {
				// this is our reversioned rule
				foundReversionedDelegateRule = true;
				assertEquals("Previous version relationship should be set up now", newRuleDelegation.getDelegationRule().getId(), ruleDelegation.getDelegationRule().getPreviousRuleId());
				assertEquals("Rule Version should have been incremented.",
						Long.valueOf(newRuleDelegation.getVersionNumber().longValue() + 1),
						ruleDelegation.getVersionNumber());
			}
		}
		assertTrue("Failed to find the reversioned delegate rule", foundReversionedDelegateRule);
	}

	private void saveNewVersion(RuleDelegationBo ruleDelegation) {
		// clear out the keys
		ruleDelegation.setRuleDelegationId(null);
		ruleDelegation.setDelegateRuleId(null);
        ruleDelegation.setObjectId(null);
		for (RuleResponsibilityBo ruleResponsibility : ruleDelegation.getDelegationRule().getRuleResponsibilities()) {
			ruleResponsibility.setRuleBaseValuesId(null);
			//ruleResponsibility.setRuleBaseValues(null);
			ruleResponsibility.setResponsibilityId(null);
			ruleResponsibility.setId(null);
            ruleResponsibility.setObjectId(null);
		}
		KEWServiceLocator.getRuleService().saveRuleDelegation(ruleDelegation, true);
	}

}
