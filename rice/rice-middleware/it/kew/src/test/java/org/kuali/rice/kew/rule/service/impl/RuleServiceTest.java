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
package org.kuali.rice.kew.rule.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;

import javax.persistence.PersistenceException;

import org.junit.Test;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleExtensionBo;
import org.kuali.rice.kew.rule.RuleExtensionValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.test.BaselineTestCase;
import org.springframework.dao.DataIntegrityViolationException;

@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class RuleServiceTest extends KEWTestCase {

    protected void loadTestData() throws Exception {
        loadXmlFile("org/kuali/rice/kew/rule/RouteTemplateConfig.xml");
    }

    /**
     * Tests the effect of adding a rule with an extension that has an empty value.
     * Currently, depending on database, this may yield a constraint violation as the empty
     * string may be interpreted as a NULL value by the database.
     * @see https://test.kuali.org/jira/browse/KULRNE-6182
     */
    @Test
    public void testEmptyRuleExtension() throws Exception {
        final RuleBaseValues rbv = new RuleBaseValues();
        rbv.setActive(Boolean.TRUE);
        rbv.setCurrentInd(Boolean.TRUE);
        rbv.setDescription("A test rule");
        rbv.setDocTypeName("TestDocumentType");
        rbv.setForceAction(Boolean.FALSE);

        RuleExtensionBo ext = new RuleExtensionBo();
        RuleExtensionValue val = new RuleExtensionValue();
        val.setKey("emptyvalue");
        val.setValue("");
        ext.getExtensionValues().add(val);
        rbv.getRuleExtensions().add(ext);

        /*
         * The AssertThrows below should work with OJB, but some problems are occurring when using JPA/Hibernate (see below).
         * 
         * FIXME: When the operation below throws the expected exception (when JPA/Hibernate is used), it appears to do so while returning from the method call
         * (at which time the expected-to-fail saving operation gets committed by Hibernate). Unfortunately, the exception gets thrown at the wrong time when returning,
         * because any attempts to run subsequent unit tests or unit test methods during the same JUnit test run will fail, due to NOWAIT exceptions
         * during test case startup.
         * 
         * A temporary hack to bypass this problem is to add the following line at the end of the 3-argument RuleServiceImpl.save2() method, which will force the
         * bad saving operation to take place at the right time for a proper rollback to occur:
         * 
         * getRuleDAO().findRuleBaseValuesById(ruleBaseValues.getId());
         * 
         * However, a longer-term solution will be needed in case there are similar areas in the system with these kinds of problems.
         */
        final boolean isKewJpaEnabled = OrmUtils.isJpaEnabled("rice.kew");
        try {
            KEWServiceLocator.getRuleService().save2(rbv);
            fail("exception did not happen");
        } catch (RuntimeException e) {
            boolean fail = !isKewJpaEnabled ? e instanceof PersistenceException : e instanceof DataIntegrityViolationException;
            if (fail) {
                fail("Did not throw exception as expected.  If rule service behavior has changed, update this test.");
            }
        }

        //fail("Saving a rule extension value with an empty string as the value yields a constraint violation");
    }
    
    /**
     * Tests the RuleService's ability to retrieve RuleBaseValues instances that lack an associated rule responsibility. 
     * @see https://test.kuali.org/jira/browse/KULRICE-3513
     */
    @Test
    public void testRetrievalOfRulesWithoutResponsibilities() throws Exception {
    	loadXmlFile("org/kuali/rice/kew/rule/RulesWithoutResponsibilities.xml");
    	final String NULL_ID = null;
    	final String[] expectedRuleNames = {"NoResponsibilitiesRule1", "NoResponsibilitiesRule2", "NoResponsibilitiesRule3"};
    	final String[] expectedRuleDocTypes = {"RiceDocument.RuleDocument", "RiceDocument.child1", "RiceDocument.child1child"};
    	final String[] expectedRuleDescriptions = {"A rule with no responsibilities", "Another rule without responsibilities", "A third rule lacking responsibilities"};
    	final String[] personResponsibilities = {"rkirkend", "rkirkend", "user1"};
    	final String[] groupResponsibilities = {"TestWorkgroup", "NonSIT", "TestWorkgroup"};
    	int actualResponsibilitylessRuleCount = 0;
    	List<?> ruleList = null;
    	
    	// First, check that a blank search will retrieve all of the expected responsibility-less rules above.
    	ruleList = KEWServiceLocator.getRuleService().search(null, NULL_ID, null, null, null, null, null, null, null, "");
    	assertNotNull("The returned rule list should not be null", ruleList);
    	for (Iterator<?> ruleIter = ruleList.iterator(); ruleIter.hasNext();) {
    		RuleBaseValues rBaseValues = (RuleBaseValues) ruleIter.next();
    		if (rBaseValues.getRuleResponsibilities() == null || rBaseValues.getRuleResponsibilities().isEmpty()) {
   				actualResponsibilitylessRuleCount++;
    		}
    	}
    	assertEquals("Wrong number of responsibility-less rules found", expectedRuleNames.length, actualResponsibilitylessRuleCount);
    	
    	// Next, test the retrieval of each of these rules independently.
    	for (int i = 0; i < expectedRuleNames.length; i++) {
    		ruleList = KEWServiceLocator.getRuleService().search(expectedRuleDocTypes[i], NULL_ID, null, expectedRuleDescriptions[i], null, null, null, null, null, "");
    		assertNotNull("The returned rule list should not be null when searching for rule '" + expectedRuleNames[i] + "'", ruleList);
    		assertEquals("Exactly one rule should have been retrieved when searching for rule '" + expectedRuleNames[i] + "'", 1, ruleList.size());
    		RuleBaseValues rBaseValues = (RuleBaseValues) ruleList.get(0);
    		assertEquals("The retrieved rule has the wrong name", expectedRuleNames[i], rBaseValues.getName());
    		assertEquals("Rule '" + expectedRuleNames[i] + "' has the wrong doc type name", expectedRuleDocTypes[i], rBaseValues.getDocTypeName());
    		assertEquals("Rule '" + expectedRuleNames[i] + "' has the wrong description", expectedRuleDescriptions[i], rBaseValues.getDescription());
    		assertTrue("Rule '" + expectedRuleNames[i] + "' should not have any responsibilities",
    				rBaseValues.getRuleResponsibilities() == null || rBaseValues.getRuleResponsibilities().isEmpty());
    	}
    	
    	// Verify that when searching for rules with the same doc types but with a person responsibility specified, the responsibility-less rules are not retrieved.
    	for (int i = 0; i < expectedRuleNames.length; i++) {
    		ruleList = KEWServiceLocator.getRuleService().search(expectedRuleDocTypes[i], NULL_ID, null, null, null,
    				KEWServiceLocator.getIdentityHelperService().getPrincipalByPrincipalName(personResponsibilities[i]).getPrincipalId(), null, null, null, "user");
    		assertNotNull("The returned rule list should not be null for doc type '" + expectedRuleDocTypes[i] + "'", ruleList);
    		assertFalse("The returned rule list should not be empty for doc type '" + expectedRuleDocTypes[i] + "'", ruleList.isEmpty());
    		for (Iterator<?> ruleIter = ruleList.iterator(); ruleIter.hasNext();) {
        		RuleBaseValues rBaseValues = (RuleBaseValues) ruleIter.next();
        		assertTrue((new StringBuilder()).append("Found a rule without responsibilities for doc type '").append(
        				expectedRuleDocTypes[i]).append("' and principal '").append(personResponsibilities[i]).append("'").toString(),
        					rBaseValues.getRuleResponsibilities() != null && !rBaseValues.getRuleResponsibilities().isEmpty());
        	}
    	}
    	
    	// Verify that when searching for rules with the same doc types but with a group responsibility specified, the responsibility-less rules are not retrieved.
    	for (int i = 0; i < expectedRuleNames.length; i++) {
    		ruleList = KEWServiceLocator.getRuleService().search(expectedRuleDocTypes[i], NULL_ID, null, null,
    				KEWServiceLocator.getIdentityHelperService().getGroupByName("KR-WKFLW", groupResponsibilities[i]).getId(), null, null, null, null, "");
    		assertNotNull("The returned rule list should not be null for doc type '" + expectedRuleDocTypes[i] + "'", ruleList);
    		assertFalse("The returned rule list should not be empty for doc type '" + expectedRuleDocTypes[i] + "'", ruleList.isEmpty());
    		for (Iterator<?> ruleIter = ruleList.iterator(); ruleIter.hasNext();) {
        		RuleBaseValues rBaseValues = (RuleBaseValues) ruleIter.next();
        		assertTrue((new StringBuilder()).append("Found a rule without responsibilities for doc type '").append(
        				expectedRuleDocTypes[i]).append("' and group '").append(groupResponsibilities[i]).append("' with namespace 'KR-WKFLW'").toString(),
        					rBaseValues.getRuleResponsibilities() != null && !rBaseValues.getRuleResponsibilities().isEmpty());
        	}
    	}
    }
}
