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

import org.junit.Test;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.actionrequest.bo.RuleMaintenanceActionRequestCodeValuesFinder;
import org.kuali.rice.kew.api.KEWPropertyConstants;
import org.kuali.rice.kew.document.RoutingRuleMaintainable;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.document.MaintenanceDocumentBase;
import org.kuali.rice.kns.maintenance.Maintainable;
import org.kuali.rice.kns.util.KNSGlobalVariables;
import org.kuali.rice.kns.web.struts.form.KualiForm;
import org.kuali.rice.kns.web.struts.form.KualiMaintenanceForm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * This class tests the code that handles the default values for the rule templates.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RuleTemplateDefaultsTest extends KEWTestCase {

	/**
	 * Creates a KualiMaintenanceForm with the given rule template inside of its RuleBaseValues instance.
	 * 
	 * @param rtName The rule template to use.
	 */
	private void createNewKualiMaintenanceForm(String rtName) {
		// Initialize the required variables.
		final KualiMaintenanceForm kmForm = new KualiMaintenanceForm();
		final MaintenanceDocument maintDoc = new MaintenanceDocumentBase();
		final Maintainable oldMaint = new RoutingRuleMaintainable();
		final Maintainable newMaint = new RoutingRuleMaintainable();
		final RuleBaseValues rbValues = new RuleBaseValues();
		// Setup the rule base and the maintainables.
		rbValues.setRuleTemplate(KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(rtName));
		oldMaint.setBusinessObject(rbValues);
		oldMaint.setBoClass(rbValues.getClass());
		newMaint.setBusinessObject(rbValues);
		newMaint.setBoClass(rbValues.getClass());
		// Setup the maintenance document and the maintenance form.
		maintDoc.setOldMaintainableObject(oldMaint);
		maintDoc.setNewMaintainableObject(newMaint);
		maintDoc.getDocumentHeader().setDocumentDescription("This is a rule template test");
		kmForm.setDocument(maintDoc);
		KNSGlobalVariables.setKualiForm(kmForm);
	}
	
	/**
	 * A convenience method for creating a set of expected key label pairs.
	 * 
	 * @param hasAcknowledge Indicates that a KeyValue for "acknowledge" options should exist.
	 * @param hasComplete Indicates that a KeyValue for "complete" options should exist.
	 * @param hasApprove Indicates that a KeyValue for "approve" options should exist.
	 * @param hasFyi Indicates that a KeyValue for "fyi" options should exist.
	 * @return A Set containing the desired expected KeyValue keys.
	 */
	private Set<String> createExpectedKeysSet(boolean hasAcknowledge, boolean hasComplete, boolean hasApprove, boolean hasFyi) {
		final Set<String> expectedKeys = new HashSet<String>();
		// Insert the desired expected options into the set.
		if (hasAcknowledge) { expectedKeys.add(KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ); }
		if (hasComplete) { expectedKeys.add(KewApiConstants.ACTION_REQUEST_COMPLETE_REQ); }
		if (hasApprove) { expectedKeys.add(KewApiConstants.ACTION_REQUEST_APPROVE_REQ); }
		if (hasFyi) { expectedKeys.add(KewApiConstants.ACTION_REQUEST_FYI_REQ); }
		return expectedKeys;
	}
	
	/**
	 * A convenience method for placing the keys from a KeyValue list into a set.
	 * 
	 * @param kValues The KeyValues to process.
	 * @return A Set containing the keys of each KeyValue.
	 */
	private Set<String> createSetOfKeyValueKeys(List<KeyValue> klpList) {
		final Set<String> actualKeys = new HashSet<String>();
		for (Iterator<KeyValue> iterator = klpList.iterator(); iterator.hasNext();) {
			actualKeys.add((String) iterator.next().getKey());
		}
		return actualKeys;
	}
	
	/**
	 * Tests to ensure that the "TestRuleTemplate" in DefaultTestData.xml has the four action request options defined as "true",
	 * either explicitly or by default.
	 */
	@Test public void testAllTrueOptionsInTestRuleTemplate() throws Exception {
		createNewKualiMaintenanceForm("TestRuleTemplate");
		assertRuleTemplateHasExpectedKeyValues(
				createExpectedKeysSet(true, true, true, true),
				createSetOfKeyValueKeys((new RuleMaintenanceActionRequestCodeValuesFinder()).getKeyValues()));
	}

	/**
	 * Tests to ensure that the proper key values are returned based upon the class type of the currently-set Kuali form.
	 * 
	 * @throws Exception
	 */
	@Test public void testCorrectKeyValuesReturnedBasedOnKualiFormInstance() throws Exception {
		// First, check that the proper values are returned when the Kuali form is *not* a KualiMaintenanceForm.
		KNSGlobalVariables.setKualiForm(new KualiForm());
		assertRuleTemplateHasExpectedKeyValues(
				createExpectedKeysSet(true, true, true, true),
				createSetOfKeyValueKeys((new RuleMaintenanceActionRequestCodeValuesFinder()).getKeyValues()));
		// Next, check that the proper values are returned when the Kuali form is a KualiMaintenanceForm containing a given rule template.
		loadXmlFile("RT_ValidRuleTemplatesWithVaryingDefaults.xml");
		createNewKualiMaintenanceForm("Test_Rule_Template2");
		assertRuleTemplateHasExpectedKeyValues(
				createExpectedKeysSet(false, false, false, true),
				createSetOfKeyValueKeys((new RuleMaintenanceActionRequestCodeValuesFinder()).getKeyValues()));
	}
	
	/**
	 * Tests to ensure that the rule template in RT_ValidRuleTemplateWithFullDefaults.xml has the expected action request options.
	 * 
	 * @throws Exception
	 */
	@Test public void testOptionsInRT_ValidRuleTemplatesWithVaryingDefaults() throws Exception {
		loadXmlFile("RT_ValidRuleTemplatesWithVaryingDefaults.xml");
		final String[] ruleTemplates = {"RuleTemplate_With_Valid_Defaults", "RuleTemplate_With_More_Valid_Defaults"};
		final boolean[][] kSetBools = { {false, false, true, false}, {true, true, false, false} };
		final String[][] defaultActions = {
			{KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_APPROVE_REQ,KewApiConstants.ACTION_REQUEST_APPROVE_REQ},
			{KewApiConstants.ACTION_REQUEST_COMPLETE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ,KewApiConstants.ACTION_REQUEST_COMPLETE_REQ}};
		// Test each rule template from the given file.
		for (int i = 0; i < ruleTemplates.length; i++) {
			createNewKualiMaintenanceForm(ruleTemplates[i]);
			assertRuleTemplateHasExpectedKeyValues(
					createExpectedKeysSet(kSetBools[i][0], kSetBools[i][1], kSetBools[i][2], kSetBools[i][3]),
					createSetOfKeyValueKeys((new RuleMaintenanceActionRequestCodeValuesFinder()).getKeyValues()));
			assertRuleTemplateHasExpectedDefaultActions(defaultActions[i]);
		}
	}
	
	/**
	 * A convenience method for performing KeyValue existence/nonexistence tests.
	 * 
	 * @param expectedKeys The expected KeyValue keys.
	 * @param actualKeys The actual KeyValue keys.
	 * @throws Exception
	 */
	private void assertRuleTemplateHasExpectedKeyValues(Set<String> expectedKeys, Set<String> actualKeys) throws Exception {
		// Check to see if all required keys are in the set.
		for (Iterator<String> iterator = expectedKeys.iterator(); iterator.hasNext();) {
			final String expKey = iterator.next();
			assertTrue("The key label pair with a key of '" + expKey + "' should have been true.", actualKeys.contains(expKey));
			actualKeys.remove(expKey);
		}
		// If any keys are still in the list, then fail the test because we expected their equivalent rule template options to
		// have a non-true value.
		if (!actualKeys.isEmpty()) {
			// Construct the error message.
			final String pluralStr = (actualKeys.size() != 1) ? "s" : "";
			final StringBuilder errMsg = new StringBuilder();
			errMsg.append("The key label pair").append(pluralStr).append(" with the key").append(pluralStr).append(" of ");
			for (Iterator<String> iterator = actualKeys.iterator(); iterator.hasNext();) {
				errMsg.append("'").append(iterator.next()).append(iterator.hasNext() ? "', " : "' ");
			}
			errMsg.append("should have been false.");
			// Fail the test.
			fail(errMsg.toString());
		}
	}
	
	/**
	 * A convenience method for verifying that a rule template contains the expected default action.
	 * 
	 * @param expectedDefActions The default actions expected by each responsibility (person, then group, then role).
	 * @throws Exception
	 */
	private void assertRuleTemplateHasExpectedDefaultActions(String[] expectedDefActions) throws Exception {
		// Acquire the Maintainable and the responsibility constants.
		final RoutingRuleMaintainable rrMaint = (RoutingRuleMaintainable) ((MaintenanceDocument) ((KualiMaintenanceForm)
				KNSGlobalVariables.getKualiForm()).getDocument()).getNewMaintainableObject();
		final String[] respSectionConsts = { KEWPropertyConstants.PERSON_RESP_SECTION, KEWPropertyConstants.GROUP_RESP_SECTION,
				KEWPropertyConstants.ROLE_RESP_SECTION };
		// Check each responsibility's default action.
		for (int i = 0; i < respSectionConsts.length; i++) {
			final String actualDefAction =
					((RuleResponsibilityBo) rrMaint.initNewCollectionLine(respSectionConsts[i])).getActionRequestedCd();
			assertEquals("The rule template does not have the expected default approve action.", expectedDefActions[i], actualDefAction);
		}
	}
}
