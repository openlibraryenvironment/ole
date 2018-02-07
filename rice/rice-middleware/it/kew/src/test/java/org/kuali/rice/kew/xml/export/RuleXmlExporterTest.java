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
package org.kuali.rice.kew.xml.export;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.kew.export.KewExportDataSet;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleDelegationBo;
import org.kuali.rice.kew.rule.RuleExtensionBo;
import org.kuali.rice.kew.rule.RuleExtensionValue;
import org.kuali.rice.kew.rule.RuleResponsibilityBo;
import org.kuali.rice.kew.rule.web.WebRuleUtils;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.ClearDatabaseLifecycle;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;


/**
 * Tests the RuleXmlExporter by importing XML, exporting it, and then re-importing the xml.<br><br>
 *
 * NOTE: It's important to note that the success of this test depends on all of the Rules in any
 * XML having unique descriptions as this is the only way for the test to identify
 * the rules from the original imported XML and the XML imported from the export.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class RuleXmlExporterTest extends XmlExporterTestCase {

	@Test public void testExport() throws Exception {
        loadXmlFile("org/kuali/rice/kew/actions/ActionsConfig.xml");
        loadXmlStream(new FileInputStream(getBaseDir() + "/src/test/resources/org/kuali/rice/kew/batch/data/RuleAttributeContent.xml"));
        loadXmlStream(new FileInputStream(getBaseDir() + "/src/test/resources/org/kuali/rice/kew/batch/data/RuleTemplateContent.xml"));
        loadXmlStream(new FileInputStream(getBaseDir() + "/src/test/resources/org/kuali/rice/kew/batch/data/DocumentTypeContent.xml"));
        loadXmlStream(new FileInputStream(getBaseDir() + "/src/test/resources/org/kuali/rice/kew/batch/data/RuleContent.xml"));
        assertRuleBaseValuesStateIndependence();
        assertExport();
    }

    /**
     * Note that the assertion here will fail if you have multiple rules with the same description.
     */
    protected void assertExport() throws Exception {
        // export all existing rules and their dependencies (document types, rule templates, rule attributes)
        List oldRules = KEWServiceLocator.getRuleService().fetchAllRules(true);
        assertAllRulesHaveUniqueNames(oldRules);
        List oldRuleDelegations = KEWServiceLocator.getRuleDelegationService().findAllCurrentRuleDelegations();
        assertAllRuleDelegationsHaveUniqueNames(oldRuleDelegations);

        KewExportDataSet dataSet = new KewExportDataSet();
        dataSet.getRules().addAll(oldRules);
        dataSet.getRuleDelegations().addAll(oldRuleDelegations);
        dataSet.getDocumentTypes().addAll(KEWServiceLocator.getDocumentTypeService().findAllCurrent());
        dataSet.getRuleTemplates().addAll(KEWServiceLocator.getRuleTemplateService().findAll());
        dataSet.getRuleAttributes().addAll(KEWServiceLocator.getRuleAttributeService().findAll());
        byte[] xmlBytes = CoreApiServiceLocator.getXmlExporterService().export(dataSet.createExportDataSet());
        assertTrue("XML should be non empty.", xmlBytes != null && xmlBytes.length > 0);
        
        // now clear the tables
        ClearDatabaseLifecycle clearLifeCycle = new ClearDatabaseLifecycle();
        clearLifeCycle.getTablesToClear().add("KREW_RULE_T");
        clearLifeCycle.getTablesToClear().add("KREW_RULE_RSP_T");
        clearLifeCycle.getTablesToClear().add("KREW_DLGN_RSP_T");
        clearLifeCycle.getTablesToClear().add("KREW_RULE_ATTR_T");
        clearLifeCycle.getTablesToClear().add("KREW_RULE_TMPL_T");
        clearLifeCycle.getTablesToClear().add("KREW_DOC_TYP_T");
        clearLifeCycle.start();
        new ClearCacheLifecycle().stop();

        // import the exported xml
        loadXmlStream(new BufferedInputStream(new ByteArrayInputStream(xmlBytes)));

        List newRules = KEWServiceLocator.getRuleService().fetchAllRules(true);
        assertEquals("Should have same number of old and new Rules.", oldRules.size(), newRules.size());
        for (Iterator iterator = oldRules.iterator(); iterator.hasNext();) {
            RuleBaseValues oldRule = (RuleBaseValues) iterator.next();
            boolean foundRule = false;
            for (Iterator iterator2 = newRules.iterator(); iterator2.hasNext();) {
                RuleBaseValues newRule = (RuleBaseValues) iterator2.next();
                if (oldRule.getDescription().equals(newRule.getDescription())) {
                    assertRuleExport(oldRule, newRule);
                    foundRule = true;
                }
            }
            assertTrue("Could not locate the new rule for description " + oldRule.getDescription(), foundRule);
        }
        
        List newRuleDelegations = KEWServiceLocator.getRuleDelegationService().findAllCurrentRuleDelegations();
        assertDelegations(oldRuleDelegations, newRuleDelegations);
    }
    
    /**
     * verifies that rule exports are the same regardless of whether the rule is ready for render, or
     * for persistance.
     */
    protected void assertRuleBaseValuesStateIndependence() throws Exception {
    	for (Object o : KEWServiceLocator.getRuleService().fetchAllRules(true)) {
        	RuleBaseValues rule = (RuleBaseValues)o;
        	KewExportDataSet dataSet = new KewExportDataSet();
        	dataSet.getRules().add(rule);
        	
        	// first, do a conversion in the just-loaded state:
        	byte[] saveXmlBytes = CoreApiServiceLocator.getXmlExporterService().export(dataSet.createExportDataSet());
        	String saveStr = new String(saveXmlBytes);
        	
        	// now, convert for render:
        	WebRuleUtils.populateRuleMaintenanceFields(rule);
        	
        	// do another conversion in the ready-for-render state:
        	byte[] loadXmlBytes = CoreApiServiceLocator.getXmlExporterService().export(dataSet.createExportDataSet());
        	String loadStr = new String(loadXmlBytes);

            // FIXME: currently failing due to:
            // * WebRuleUtils.populateRuleMaintenanceFields clears rule extensions
            // * RuleXmlExporter detects missing extensions and re-adds them: but in a different order...

        	// check that the results are identical:
        	assertTrue("The load/render state of the RuleBaseValues shouldn't effect the export: \n" + 
        			saveStr + "\n\n != \n\n" + loadStr, 
        			StringUtils.equals(saveStr, loadStr));
        }
    }

    private void assertRuleExport(RuleBaseValues oldRule, RuleBaseValues newRule) {
        assertFalse("Ids should be different.", oldRule.getId().equals(newRule.getId()));
        assertEquals(oldRule.isActive(), newRule.isActive());
        assertEquals(DateUtils.round(oldRule.getActivationDate(), Calendar.DATE), DateUtils.round(newRule.getActivationDate(), Calendar.DATE));
        assertEquals(oldRule.getName(), newRule.getName());
        assertEquals(oldRule.getCurrentInd(), newRule.getCurrentInd());
        assertEquals(oldRule.getDeactivationDate(), newRule.getDeactivationDate());
        assertEquals(oldRule.getDelegateRule(), newRule.getDelegateRule());
        assertEquals(oldRule.getDescription(), newRule.getDescription());
        assertEquals(oldRule.getDocTypeName(), newRule.getDocTypeName());
        
        if (oldRule.getFromDateValue() == null) {
        	assertNull(newRule.getFromDateValue());
        } else {
        	assertEquals(DateUtils.round(oldRule.getFromDateValue(), Calendar.DATE), DateUtils.round(newRule.getFromDateValue(), Calendar.DATE));
        }
        if (oldRule.getToDateValue() == null) {
        	assertNull(newRule.getToDateValue());
        } else {
        	assertEquals(DateUtils.round(oldRule.getToDateValue(), Calendar.DATE), DateUtils.round(newRule.getToDateValue(), Calendar.DATE));
        }
        assertEquals(oldRule.getFromDateString(),newRule.getFromDateString() );
        assertEquals(oldRule.getToDateString(),newRule.getToDateString() );
        
        assertEquals(oldRule.isForceAction(), newRule.isForceAction());
        
        if(!oldRule.getDelegateRule().booleanValue())
        	assertEquals(oldRule.getPreviousRuleId(), newRule.getPreviousRuleId());
        
        assertEquals(oldRule.getDocumentId(), newRule.getDocumentId());
        
        if (oldRule.getRuleTemplate() == null) {
            assertNull(newRule.getRuleTemplate());
        } else {
            assertEquals(oldRule.getRuleTemplate().getName(), newRule.getRuleTemplate().getName());
        }
        if (oldRule.getRuleExpressionDef() == null) {
            assertNull(newRule.getRuleExpressionDef());
        } else {
            assertEquals(oldRule.getRuleExpressionDef().getExpression(), newRule.getRuleExpressionDef().getExpression());
            assertEquals(oldRule.getRuleExpressionDef().getType(), newRule.getRuleExpressionDef().getType());
        }
        if(!oldRule.getDelegateRule().booleanValue())
        	assertEquals(oldRule.getVersionNbr(), newRule.getVersionNbr());

        assertRuleExtensions(oldRule.getRuleExtensions(), newRule.getRuleExtensions());
        assertResponsibilities(oldRule.getRuleResponsibilities(), newRule.getRuleResponsibilities());


    }

    private void assertRuleExtensions(List oldRuleExtensions, List newRuleExtensions) {
        assertEquals(oldRuleExtensions.size(), newRuleExtensions.size());
        for (Iterator iterator = oldRuleExtensions.iterator(); iterator.hasNext();) {
            RuleExtensionBo oldExtension = (RuleExtensionBo) iterator.next();
            boolean foundExtension = false;
            for (Iterator iterator2 = newRuleExtensions.iterator(); iterator2.hasNext();) {
                RuleExtensionBo newExtension = (RuleExtensionBo) iterator2.next();
                if (oldExtension.getRuleTemplateAttribute().getRuleAttribute().getName().equals(newExtension.getRuleTemplateAttribute().getRuleAttribute().getName()) &&
                        oldExtension.getRuleTemplateAttribute().getRuleTemplate().getName().equals(newExtension.getRuleTemplateAttribute().getRuleTemplate().getName())) {
                        assertExtensionValues(oldExtension.getExtensionValues(), newExtension.getExtensionValues());
                        foundExtension = true;
                        break;
                }
            }
            assertTrue("Could not locate rule extension.", foundExtension);
        }
    }

    private void assertExtensionValues(List oldExtensionValues, List newExtensionValues) {
        assertEquals(oldExtensionValues.size(), newExtensionValues.size());
        for (Iterator iterator = oldExtensionValues.iterator(); iterator.hasNext();) {
            RuleExtensionValue oldValue = (RuleExtensionValue) iterator.next();
            boolean foundValue = false;
            for (Iterator iterator2 = oldExtensionValues.iterator(); iterator2.hasNext();) {
                RuleExtensionValue newValue = (RuleExtensionValue) iterator2.next();
                if (oldValue.getKey().equals(newValue.getKey())) {
                    assertEquals(oldValue.getValue(), newValue.getValue());
                    foundValue = true;
                    break;
                }
            }
            assertTrue("Could not locate extension value.", foundValue);
        }
    }

    private void assertResponsibilities(List oldResps, List newResps) {
        assertEquals(oldResps.size(), newResps.size());
        for (Iterator iterator = oldResps.iterator(); iterator.hasNext();) {
            RuleResponsibilityBo oldResp = (RuleResponsibilityBo) iterator.next();
            boolean foundResp = false;
            for (Iterator iterator2 = newResps.iterator(); iterator2.hasNext();) {
                RuleResponsibilityBo newResp = (RuleResponsibilityBo) iterator2.next();
                if (oldResp.getRuleResponsibilityName().equals(newResp.getRuleResponsibilityName())) {
                    assertEquals(oldResp.getActionRequestedCd(), newResp.getActionRequestedCd());
                    assertEquals(oldResp.getApprovePolicy(), newResp.getApprovePolicy());
                    assertEquals(oldResp.getResolvedRoleName(), newResp.getResolvedRoleName());
                    assertEquals(oldResp.getRole(), newResp.getRole());
                    assertEquals(oldResp.getRuleResponsibilityType(), newResp.getRuleResponsibilityType());
                    assertEquals(oldResp.getPriority(), newResp.getPriority());
                    foundResp = true;
                    break;
                }
            }
            assertTrue("Could not locate responsibility "+oldResp.getRuleResponsibilityName()+" on rule "+oldResp.getRuleBaseValues().getDescription(), foundResp);
        }
    }

    private void assertDelegations(List oldDelegations, List newDelegations) {
        assertEquals(oldDelegations.size(), newDelegations.size());
        for (Iterator iterator = oldDelegations.iterator(); iterator.hasNext();) {
            RuleDelegationBo oldDelegation = (RuleDelegationBo) iterator.next();
            boolean foundDelegation = false;
            for (Iterator iterator2 = newDelegations.iterator(); iterator2.hasNext();) {
                RuleDelegationBo newDelegation = (RuleDelegationBo) iterator2.next();
                if (oldDelegation.getDelegationRule().getName().equals(newDelegation.getDelegationRule().getName())) {
                    assertEquals(oldDelegation.getDelegationType(), newDelegation.getDelegationType());
                    assertFalse(oldDelegation.getResponsibilityId().equals(newDelegation.getResponsibilityId()));
                    assertRuleExport(oldDelegation.getDelegationRule(), newDelegation.getDelegationRule());
                    foundDelegation = true;
                    break;
                }
            }
            assertTrue("Could not locate delegation.", foundDelegation);
        }
    }

    private void assertAllRulesHaveUniqueNames(List rules) throws Exception {
    	Set<String> ruleDescriptions = new HashSet<String>();
    	for (Iterator iterator = rules.iterator(); iterator.hasNext();) {
			RuleBaseValues rule = (RuleBaseValues) iterator.next();
			assertFalse("Found 2 rules with the same description '" + rule.getDescription() + "'.  " +
					"In order for this test to work, all rules in the configuration files must have unique descriptions.",
					ruleDescriptions.contains(rule.getDescription()));
			ruleDescriptions.add(rule.getDescription());
		}
    }
    
    private void assertAllRuleDelegationsHaveUniqueNames(List<RuleDelegationBo> ruleDelegations) throws Exception {
    	List<RuleBaseValues> rules = new ArrayList<RuleBaseValues>();
    	for (RuleDelegationBo ruleDelegation : ruleDelegations) {
    		rules.add(ruleDelegation.getDelegationRule());
    	}
    	assertAllRulesHaveUniqueNames(rules);
    }

}
