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
package org.kuali.rice.kew.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleTemplateOptionBo;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;


/**
 * This is a Test class to test the rule template xml parser
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RuleTemplateXmlParserTest extends KEWTestCase {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RuleTemplateXmlParserTest.class);

    private static final String RULE_ATTRIBUTE_ONE = "TemplateTestRuleAttribute1";
    private static final String RULE_ATTRIBUTE_TWO = "TemplateTestRuleAttribute2";
    private static final String RULE_ATTRIBUTE_THREE = "TemplateTestRuleAttribute3";
    private static final String RULE_ATTRIBUTE_FOUR = "TemplateTestRuleAttribute4";
    private static final String RULE_ATTRIBUTE_FIVE = "TemplateTestRuleAttribute5";

    private enum TemplateParserGeneralFixture {
        VALID_TEMPLATE_MIN_XML("ValidRuleTemplate", "RuleTemplate_Valid", new String[]{RULE_ATTRIBUTE_ONE}, new String[0], new String[]{RULE_ATTRIBUTE_ONE}, new String[0]),
        VALID_TEMPLATE_FULL_XML("ValidRuleTemplate_Full", "RuleTemplate_Valid_Full", new String[]{RULE_ATTRIBUTE_ONE}, new String[0], new String[]{RULE_ATTRIBUTE_ONE}, new String[0]),
        // below has allowOverwrite=true
        VALID_TEMPLATE_OVERWRITE("ValidRuleTemplateOverwrite", "RuleTemplate_Valid", new String[]{RULE_ATTRIBUTE_ONE}, new String[]{RULE_ATTRIBUTE_FOUR}, new String[]{RULE_ATTRIBUTE_FOUR}, new String[]{RULE_ATTRIBUTE_ONE}),
        VALID_TEMPLATE_FULL_OVERWRITE("ValidRuleTemplateFullOverwrite", "RuleTemplate_Valid_Full", new String[]{RULE_ATTRIBUTE_ONE}, new String[]{RULE_ATTRIBUTE_FOUR}, new String[]{RULE_ATTRIBUTE_FOUR}, new String[]{RULE_ATTRIBUTE_ONE}),
        INVALID_TEMPLATE_OVERWRITE("InvalidRuleTemplateOverwrite", "RuleTemplate_Valid", new String[]{RULE_ATTRIBUTE_ONE}, new String[0], new String[]{RULE_ATTRIBUTE_ONE}, new String[0]),
        VALID_TEMPLATE_WITH_FULL_DEFAULTS("ValidRuleTemplateWithFullDefaults", "RuleTemplate_Valid_Defaults", new String[]{RULE_ATTRIBUTE_TWO}, new String[0], new String[]{RULE_ATTRIBUTE_TWO}, new String[0]),
        VALID_TEMPLATE_WITH_LIMITED_DEFAULTS("ValidRuleTemplateWithSomeDefaults", "RuleTemplate_Valid_Some_Defaults", new String[]{RULE_ATTRIBUTE_THREE}, new String[0], new String[0], new String[]{RULE_ATTRIBUTE_THREE}),
        VALID_TEMPLATE_WITH_LIMITED_DEFAULTS_OVERWRITE("ValidRuleTemplateWithSomeDefaultsOverwrite", "RuleTemplate_Valid_Some_Defaults", new String[]{RULE_ATTRIBUTE_THREE}, new String[0], new String[0], new String[]{RULE_ATTRIBUTE_THREE}),
        VALID_TEMPLATE_WITH_DESCRIPTION_DEFAULT("ValidRuleTemplateWithDefaultsDescriptionOnly", "RuleTemplate_Valid_Description_Default", new String[]{RULE_ATTRIBUTE_TWO}, new String[0], new String[]{RULE_ATTRIBUTE_TWO}, new String[0]),
        VALID_TEMPLATE_WITH_LIMITED_DEFAULTS_REMOVED("ValidRuleTemplateWithSomeRemovedDefaultsOverwrite", "RuleTemplate_Valid_Some_Defaults", new String[]{RULE_ATTRIBUTE_THREE}, new String[0], new String[0], new String[]{RULE_ATTRIBUTE_THREE})
        ;

        public String fileNameParameter;
        public String ruleTemplateName;
        public String[] activeAttributeNames;
        public String[] inactiveAttributeNames;
        public String[] requiredAttributeNames;
        public String[] nonRequiredAttributeNames;

        private TemplateParserGeneralFixture(String fileNameParameter, String ruleTemplateName, String[] activeAttributeNames, String[] inactiveAttributeNames, String[] requiredAttributeNames, String[] nonRequiredAttributeNames) {
            this.fileNameParameter = fileNameParameter;
            this.ruleTemplateName = ruleTemplateName;
            this.activeAttributeNames = activeAttributeNames;
            this.inactiveAttributeNames = inactiveAttributeNames;
            this.requiredAttributeNames = requiredAttributeNames;
            this.nonRequiredAttributeNames = nonRequiredAttributeNames;
        }
    }

    protected void loadTestData() throws Exception {
        loadXmlFile("RuleTemplateConfig.xml");
    }

    private void testTemplate(String docName, Class expectedException) throws Exception {
        RuleTemplateXmlParser parser = new RuleTemplateXmlParser();
        String filename = "RT_" +  docName + ".xml";
        try {
            parser.parseRuleTemplates(getClass().getResourceAsStream(filename));
            if (expectedException != null) {
                fail(filename + " successfully loaded.  Expected exception of class '" + expectedException + "'");
            }
        } catch (Exception e) {
            if (expectedException == null || !(expectedException.isAssignableFrom(e.getClass()))) {
                throw e;
            } else {
                log.error(filename + " exception: " + e);
            }
        }
    }

    private void testListOfTemplateAttributes(List ruleTemplateAttributes, String[] activeRuleTemplateAttributeNames, String[] requiredRuleTemplateAttributeNames) {
        for (Iterator iterator = ruleTemplateAttributes.iterator(); iterator.hasNext();) {
            RuleTemplateAttributeBo templateAttribute = (RuleTemplateAttributeBo) iterator.next();
            String ruleAttributeName = templateAttribute.getRuleAttribute().getName();
            
            LOG.info("Attribute name '" + ruleAttributeName +"' active indicator is " + templateAttribute.isActive());
            if (activeRuleTemplateAttributeNames == null) {
                assertEquals("Active indicator should be false for all attributes but is not for attribute '" + ruleAttributeName + "'",Boolean.FALSE, templateAttribute.getActive());
            } else {
                runTestsOnTemplateAttributeField(ruleAttributeName, templateAttribute.isActive(), activeRuleTemplateAttributeNames, "active");
            }

            LOG.info("Attribute name '" + ruleAttributeName +"' required indicator is " + templateAttribute.isRequired());
            if (requiredRuleTemplateAttributeNames == null) {
                assertEquals("Required indicator should be false for all attributes but is not for attribute '" + ruleAttributeName + "'",Boolean.FALSE, templateAttribute.getRequired());
            } else {
                runTestsOnTemplateAttributeField(ruleAttributeName, templateAttribute.isRequired(), requiredRuleTemplateAttributeNames, "required");
            }
        }
    }
    
    private void testAllAttributesActive(List activeRuleTemplateAttributes, String[] activeRuleTemplateAttributeNames) {
        for (Iterator iterator = activeRuleTemplateAttributes.iterator(); iterator.hasNext();) {
            RuleTemplateAttributeBo activeTemplateAttribute = (RuleTemplateAttributeBo) iterator.next();
            String ruleAttributeName = activeTemplateAttribute.getRuleAttribute().getName();
            assertEquals("Template Attribute with name '" + ruleAttributeName + "' has invalid active value", Boolean.TRUE, activeTemplateAttribute.getActive());
            boolean foundAttribute = false;
            for (int i = 0; i < activeRuleTemplateAttributeNames.length; i++) {
                String shouldBeActiveAttributeName = activeRuleTemplateAttributeNames[i];
                if (shouldBeActiveAttributeName.equals(ruleAttributeName)) {
                    foundAttribute = true;
                    break;
                }
            }
            if (!foundAttribute) {
                fail("Template Attribute with name '" + ruleAttributeName + "' should have been in active template name list but was not found");
            }
        }
    }
    
    private void runTestsOnTemplateAttributeField(String ruleAttributeName, boolean valueToConfirm, String[] attributeNamesShouldBeTrue, String errorMessageIdentifier) {
        boolean foundAttribute = false;
        for (String attributeNameThatShouldPass : attributeNamesShouldBeTrue) {
            if (ruleAttributeName.equals(attributeNameThatShouldPass)) {
                foundAttribute = true;
                if (!valueToConfirm) {
                    fail("Attribute with name '" + ruleAttributeName + "' should have been " + errorMessageIdentifier + " but is not");
                }
            }
        }
        if ( (!foundAttribute) && (valueToConfirm) ) {
            fail("Attribute with name '" + ruleAttributeName + "' should not be " + errorMessageIdentifier + " but is");
        }
    }

    /**
     * Loads valid template definitions
     */
    @Test public void testLoadValidTemplate() throws Exception {
        testTemplate(TemplateParserGeneralFixture.VALID_TEMPLATE_FULL_XML.fileNameParameter, null);

        testTemplate(TemplateParserGeneralFixture.VALID_TEMPLATE_MIN_XML.fileNameParameter, null);
        RuleTemplateBo template = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(TemplateParserGeneralFixture.VALID_TEMPLATE_MIN_XML.ruleTemplateName);
        testListOfTemplateAttributes(template.getRuleTemplateAttributes(), TemplateParserGeneralFixture.VALID_TEMPLATE_MIN_XML.activeAttributeNames, TemplateParserGeneralFixture.VALID_TEMPLATE_MIN_XML.requiredAttributeNames);
        testAllAttributesActive(template.getActiveRuleTemplateAttributes(), TemplateParserGeneralFixture.VALID_TEMPLATE_MIN_XML.activeAttributeNames);

        assertNoDefaultsSpecified(template);
    }

    /**
     * Loads a "minimal" template definition and then updates/overwrites it
     */
    @Test public void testLoadValidTemplateWithOverwrite() throws Exception {
        testTemplate(TemplateParserGeneralFixture.VALID_TEMPLATE_MIN_XML.fileNameParameter, null);
        testTemplate(TemplateParserGeneralFixture.VALID_TEMPLATE_OVERWRITE.fileNameParameter, null); // allowOverwrite=true
        
        RuleTemplateBo template = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(TemplateParserGeneralFixture.VALID_TEMPLATE_OVERWRITE.ruleTemplateName);
        testListOfTemplateAttributes(template.getRuleTemplateAttributes(), TemplateParserGeneralFixture.VALID_TEMPLATE_OVERWRITE.activeAttributeNames, TemplateParserGeneralFixture.VALID_TEMPLATE_OVERWRITE.requiredAttributeNames);
        testAllAttributesActive(template.getActiveRuleTemplateAttributes(), TemplateParserGeneralFixture.VALID_TEMPLATE_OVERWRITE.activeAttributeNames);
        
        assertNoDefaultsSpecified(template);
    }
    
    /**
     * Loads a "full" template definition and then updates/overwrites it
     */
    @Test public void testLoadValidTemplateFullWithOverwrite() throws Exception {
        testTemplate(TemplateParserGeneralFixture.VALID_TEMPLATE_FULL_XML.fileNameParameter, null);
        testTemplate(TemplateParserGeneralFixture.VALID_TEMPLATE_FULL_OVERWRITE.fileNameParameter, null); // allowOverwrite=true
        
        RuleTemplateBo template = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(TemplateParserGeneralFixture.VALID_TEMPLATE_FULL_OVERWRITE.ruleTemplateName);
        testListOfTemplateAttributes(template.getRuleTemplateAttributes(), TemplateParserGeneralFixture.VALID_TEMPLATE_FULL_OVERWRITE.activeAttributeNames, TemplateParserGeneralFixture.VALID_TEMPLATE_FULL_OVERWRITE.requiredAttributeNames);
        testAllAttributesActive(template.getActiveRuleTemplateAttributes(), TemplateParserGeneralFixture.VALID_TEMPLATE_FULL_OVERWRITE.activeAttributeNames);

        assertNoDefaultsSpecified(template);
    }

    /**
     * Tests attempting to overwrite a template without the allowOverwrite flag set to true
     */
    @Test public void testLoadInvalidTemplateWithOverwrite() throws Exception {
        testTemplate(TemplateParserGeneralFixture.VALID_TEMPLATE_MIN_XML.fileNameParameter, null);
        testTemplate(TemplateParserGeneralFixture.VALID_TEMPLATE_MIN_XML.fileNameParameter, RuntimeException.class);  // no allowOverwrite specified
        testTemplate(TemplateParserGeneralFixture.INVALID_TEMPLATE_OVERWRITE.fileNameParameter, RuntimeException.class);  // allowOverwrite=false
    }

    /**
     * Tests loading a template with a full ruleDefaults section
     */
    @Test public void testLoadValidTemplateWithFullDefaults() throws Exception {
        testTemplate(TemplateParserGeneralFixture.VALID_TEMPLATE_WITH_FULL_DEFAULTS.fileNameParameter, null);

        RuleTemplateBo template = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(TemplateParserGeneralFixture.VALID_TEMPLATE_WITH_FULL_DEFAULTS.ruleTemplateName);

        // test the rule template options
        List<RuleTemplateOptionBo> options = template.getRuleTemplateOptions();
        assertEquals(5, options.size());
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, "false");
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_APPROVE_REQ, "true");
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_COMPLETE_REQ, "false");
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_FYI_REQ, "false");
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_DEFAULT_CD, "A");

        // test those set in the default/template rule
        RuleBaseValues ruleDefaults = KEWServiceLocator.getRuleService().findDefaultRuleByRuleTemplateId(template.getId());
        assertTrue(ruleDefaults.getTemplateRuleInd());
        assertEquals("Testy Me A Template", ruleDefaults.getDescription());
        assertEquals("01/11/2006", ruleDefaults.getFromDateString());
        assertEquals("01/01/2100", ruleDefaults.getToDateString());
        assertTrue(ruleDefaults.isForceAction());
        assertFalse(ruleDefaults.isActive());
    }

    /**
     * Tests loading a template with a partial ruleDefaults section
     */
    @Test public void testLoadValidTemplateWithSomeDefaults() throws Exception {
        testTemplate(TemplateParserGeneralFixture.VALID_TEMPLATE_WITH_LIMITED_DEFAULTS.fileNameParameter, null);
        
        RuleTemplateBo template = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(TemplateParserGeneralFixture.VALID_TEMPLATE_WITH_LIMITED_DEFAULTS.ruleTemplateName);

        // test the rule template options
        List<RuleTemplateOptionBo> options = template.getRuleTemplateOptions();
        assertEquals(5, options.size());
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, "false");
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_APPROVE_REQ, "true");
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_COMPLETE_REQ, "false");
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_FYI_REQ, "false");
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_DEFAULT_CD, "A");

        // test those set in the default/template rule
        RuleBaseValues ruleDefaults = KEWServiceLocator.getRuleService().findDefaultRuleByRuleTemplateId(template.getId());
        assertTrue(ruleDefaults.getTemplateRuleInd());
        assertEquals("a rule based on RuleTemplate_Valid_Some_Defaults", ruleDefaults.getDescription());
        assertFalse(ruleDefaults.isForceAction());
        assertFalse(ruleDefaults.isActive());
        assertEquals("01/11/2006", ruleDefaults.getFromDateString());
        assertEquals("01/01/2100", ruleDefaults.getToDateString());
        assertNotNull(ruleDefaults.getActivationDate());
        assertTrue(new Date(System.currentTimeMillis() - 10000).before(ruleDefaults.getActivationDate()) && new Date(System.currentTimeMillis() + 100).after(ruleDefaults.getActivationDate()));
        assertNull(ruleDefaults.getDeactivationDate());
    }
        
    /**
     * Tests loading a template with a partial ruleDefaults section and then updating/overwriting it
     */
    @Test public void testLoadValidTemplateWithSomeDefaultsOverwrite() throws Exception {
        testTemplate(TemplateParserGeneralFixture.VALID_TEMPLATE_WITH_LIMITED_DEFAULTS.fileNameParameter, null);
        
        RuleTemplateBo template = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(TemplateParserGeneralFixture.VALID_TEMPLATE_WITH_LIMITED_DEFAULTS.ruleTemplateName);
        
        // test the rule template options
        List<RuleTemplateOptionBo> options = template.getRuleTemplateOptions();
        assertEquals(5, options.size());
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, "false");
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_APPROVE_REQ, "true");
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_COMPLETE_REQ, "false");
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_FYI_REQ, "false");
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_DEFAULT_CD, "A");

        // test those set in the default/template rule
        RuleBaseValues ruleDefaults = KEWServiceLocator.getRuleService().findDefaultRuleByRuleTemplateId(template.getId());
        assertTrue(ruleDefaults.getTemplateRuleInd());
        assertEquals("a rule based on RuleTemplate_Valid_Some_Defaults", ruleDefaults.getDescription());
        assertFalse(ruleDefaults.isForceAction());
        assertFalse(ruleDefaults.isActive());
        assertEquals("01/11/2006", ruleDefaults.getFromDateString());
        assertEquals("01/01/2100", ruleDefaults.getToDateString());
        
        testTemplate(TemplateParserGeneralFixture.VALID_TEMPLATE_WITH_LIMITED_DEFAULTS_OVERWRITE.fileNameParameter, null);

        template = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(TemplateParserGeneralFixture.VALID_TEMPLATE_WITH_LIMITED_DEFAULTS_OVERWRITE.ruleTemplateName);

        // we overwrite the template and specify a new subset of defaults...any setting omitted should be removed, i.e. reset to a default
        // value
        options = template.getRuleTemplateOptions();
        assertEquals(1, options.size());
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, "true");

        // test those set in the default/template rule
        ruleDefaults = KEWServiceLocator.getRuleService().findDefaultRuleByRuleTemplateId(template.getId());
        assertTrue(ruleDefaults.getTemplateRuleInd());
        assertEquals("a rule based on (updated) RuleTemplate_Valid_Some_Defaults", ruleDefaults.getDescription());
        assertFalse(ruleDefaults.isForceAction());
        assertFalse(ruleDefaults.isActive());
        // activation date defaults to current time
        assertNull(ruleDefaults.getFromDateValue());
        assertNotNull(ruleDefaults.getActivationDate());
        assertTrue(new Date(System.currentTimeMillis() - 10000).before(ruleDefaults.getActivationDate()) && new Date(System.currentTimeMillis() + 100).after(ruleDefaults.getActivationDate()));
        assertNull(ruleDefaults.getToDateString());
        assertNull(ruleDefaults.getDeactivationDate());
    }

    /**
     * Tests loading a template with a partial ruleDefaults section, and then updating/overwriting it with one with no ruleDefaults
     * section defined at all 
     */
    @Test public void testLoadValidTemplateWithSomeRemovedDefaults() throws Exception {
        testTemplate(TemplateParserGeneralFixture.VALID_TEMPLATE_WITH_LIMITED_DEFAULTS.fileNameParameter, null);
        
        RuleTemplateBo template = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(TemplateParserGeneralFixture.VALID_TEMPLATE_WITH_LIMITED_DEFAULTS.ruleTemplateName);
        
        // test the rule template options
        List<RuleTemplateOptionBo> options = template.getRuleTemplateOptions();
        assertEquals(5, options.size());
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, "false");
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_APPROVE_REQ, "true");
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_COMPLETE_REQ, "false");
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_FYI_REQ, "false");
        assertOptionValue(template, KewApiConstants.ACTION_REQUEST_DEFAULT_CD, "A");

        // test those set in the default/template rule
        RuleBaseValues ruleDefaults = KEWServiceLocator.getRuleService().findDefaultRuleByRuleTemplateId(template.getId());
        assertTrue(ruleDefaults.getTemplateRuleInd());
        assertEquals("a rule based on RuleTemplate_Valid_Some_Defaults", ruleDefaults.getDescription());
        assertFalse(ruleDefaults.isForceAction());
        assertFalse(ruleDefaults.isActive());
        assertEquals("01/11/2006", ruleDefaults.getFromDateString());
        assertEquals("01/01/2100", ruleDefaults.getToDateString());
        
        testTemplate(TemplateParserGeneralFixture.VALID_TEMPLATE_WITH_LIMITED_DEFAULTS_REMOVED.fileNameParameter, null);

        template = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(TemplateParserGeneralFixture.VALID_TEMPLATE_WITH_LIMITED_DEFAULTS_REMOVED.ruleTemplateName);

        // we have removed all defaults, make sure they are gone...
        // specifically that the default/template rule is GONE
        assertNoDefaultsSpecified(template);
    }

    /**
     * Tests loading a template with the minimal required ruleDefaults
     */
    @Test public void testLoadValidTemplateWithDescriptionDefault() throws Exception {
        testTemplate(TemplateParserGeneralFixture.VALID_TEMPLATE_WITH_DESCRIPTION_DEFAULT.fileNameParameter, null);
        
        RuleTemplateBo template = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(TemplateParserGeneralFixture.VALID_TEMPLATE_WITH_DESCRIPTION_DEFAULT.ruleTemplateName);

        // test the rule template options; this one just has the instructions, nothing else
        List<RuleTemplateOptionBo> options = template.getRuleTemplateOptions();
        assertEquals(0, options.size());

        // test those set in the default/template rule; everything default exception description which is specified
        RuleBaseValues ruleDefaults = KEWServiceLocator.getRuleService().findDefaultRuleByRuleTemplateId(template.getId());
        assertTrue(ruleDefaults.getTemplateRuleInd());
        assertEquals("a description", ruleDefaults.getDescription());
        assertFalse(ruleDefaults.isForceAction());
        assertFalse(ruleDefaults.isActive());
        // activation date defaults to current time
        assertNull(ruleDefaults.getFromDateValue());
        assertNotNull(ruleDefaults.getActivationDate());
        assertTrue(new Date(System.currentTimeMillis() - 10000).before(ruleDefaults.getActivationDate()) && new Date(System.currentTimeMillis() + 100).after(ruleDefaults.getActivationDate()));
        assertNull(ruleDefaults.getToDateString());
        assertNull(ruleDefaults.getDeactivationDate());
    }

    /**
     * Asserts that if the defaults element is omitted, the correct defaults are set
     * @param template the RuleTemplate to check
     */
    protected void assertNoDefaultsSpecified(RuleTemplateBo template) {
        // test the rule template options
        assertDefaultTemplateOptions(template);
        // test those set in the default/template rule
        assertDefaultRuleDefaults(template);
    }

    /**
     * Asserts that if the defaults element is omitted, the correct RuleTemplateOptions are set (or not set)
     * @param template the RuleTemplate to check
     */
    protected void assertDefaultTemplateOptions(RuleTemplateBo template) {
        List<RuleTemplateOptionBo> options = template.getRuleTemplateOptions();
        assertEquals(0, options.size());
    }

    /**
     * Asserts that if the defaults element is omitted, the correct template rule defaults are set
     * @param template the RuleTemplate to check
     */
    protected void assertDefaultRuleDefaults(RuleTemplateBo template) {
        RuleBaseValues ruleDefaults = KEWServiceLocator.getRuleService().findDefaultRuleByRuleTemplateId(template.getId());
        // if ruleDefaults were not specified, then the defaults/template rule should not have been created, or should have been deleted
        assertNull(ruleDefaults);
    }

    /**
     * Tests that the rule default template options are present.  Must be kept in sync with defaults
     * defined in RuleTemplate object
     * @param ruleTemplate the ruleTemplate to check
     * @param present whether the option should be present or not
     */
    protected void assertRuleDefaultsArePresent(RuleTemplateBo template, boolean present) {
        for (String key: RuleTemplateBo.DEFAULT_OPTION_KEYS) {
            assertOptionPresence(template, key, present);
        }
    }

    /**
     * Tests that the rule default template options are present and set to null.  Must be kept in sync with defaults
     * defined in RuleTemplate object
     * @param ruleTemplate the ruleTemplate to check
     */
    protected void assertRuleDefaultsAreNull(RuleTemplateBo template) {
        for (String key: RuleTemplateBo.DEFAULT_OPTION_KEYS) {
            assertOptionValue(template, key, null);
        }
    }

    /**
     * Asserts that the rule template has a specific rule template option defined (with any value)
     * @param template the rule template
     * @param key the RuleTemplateOption key
     * @param present whether the option should be present
     */
    protected void assertOptionPresence(RuleTemplateBo template, String key, boolean present) {
        RuleTemplateOptionBo option = template.getRuleTemplateOption(key);
        if (present) {
            if (option == null) fail("Rule template option '" + key + "' is not defined on template: " + template);
        } else {
            if (option != null) fail("Rule template option '" + key + "' is defined on template: " + template);
        }
    }

    /**
     * Asserts that the rule template has a specific rule template option defined with a specific value
     * @param template the rule template
     * @param key the RuleTemplateOption key
     * @param value the RuleTemplateOption value
     */
    protected void assertOptionValue(RuleTemplateBo template, String key, String value) {
        RuleTemplateOptionBo option = template.getRuleTemplateOption(key);
        if (option == null) fail("Rule template option '" + key + "' not defined on template: " + template);
        assertEquals("Incorrect rule template option value for key '" + key + "'.  Expected '" + value + "' but found '" + option.getValue() + "'", value, option.getValue());
    }

    private enum TemplateParserAttributeActivationFixture {
        ATTRIBUTE_1(new String[]{RULE_ATTRIBUTE_ONE}, new String[]{RULE_ATTRIBUTE_TWO}),
        ATTRIBUTE_2(new String[]{}, new String[]{RULE_ATTRIBUTE_ONE,RULE_ATTRIBUTE_TWO}),
        ATTRIBUTE_3(new String[]{RULE_ATTRIBUTE_ONE,RULE_ATTRIBUTE_THREE}, new String[]{RULE_ATTRIBUTE_TWO}),
        ATTRIBUTE_4(new String[]{RULE_ATTRIBUTE_TWO,RULE_ATTRIBUTE_FIVE}, new String[]{RULE_ATTRIBUTE_ONE,RULE_ATTRIBUTE_THREE,RULE_ATTRIBUTE_FOUR,}),
        ;

        public static final String RULE_TEMPLATE_XML_FILENAME_PARM = "ActivationAttributesTest_";
        public static final String RULE_TEMPLATE_NAME = "RuleTemplate_Activation_Test";

        public String[] activeAttributeNames;
        public String[] inactiveAttributeNames;

        private TemplateParserAttributeActivationFixture(String[] activeAttributeNames, String[] inactiveAttributeNames) {
            this.activeAttributeNames = activeAttributeNames;
            this.inactiveAttributeNames = inactiveAttributeNames;
        }
    }

    @Test public void testAttributeActivationAndRemoval() throws Exception {
        RuleTemplateBo template = null;
        int totalAttributes = -1;
        for (TemplateParserAttributeActivationFixture currentEnum : TemplateParserAttributeActivationFixture.values()) {
            String fileNameParameter = TemplateParserAttributeActivationFixture.RULE_TEMPLATE_XML_FILENAME_PARM + (currentEnum.ordinal() + 1);
            testTemplate(fileNameParameter, null);
            template = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(TemplateParserAttributeActivationFixture.RULE_TEMPLATE_NAME);
            assertEquals("Total Number of Active Attributes from Rule Template is wrong",currentEnum.activeAttributeNames.length,template.getActiveRuleTemplateAttributes().size());
            totalAttributes = currentEnum.activeAttributeNames.length + currentEnum.inactiveAttributeNames.length; 
            assertEquals("Total Number of Attributes from Rule Template is wrong",totalAttributes,template.getRuleTemplateAttributes().size());
            testListOfTemplateAttributes(template.getRuleTemplateAttributes(), currentEnum.activeAttributeNames, null);
            testAllAttributesActive(template.getActiveRuleTemplateAttributes(), currentEnum.activeAttributeNames);
        }
    }

    // test for ingesting active attribute

    // test for ingesting inactive attribute

    // test for ingesting active attribute and then reingest to test manual inactivation

    // test for ingesting active attribute and then reingest to test automatic inactivation

}
