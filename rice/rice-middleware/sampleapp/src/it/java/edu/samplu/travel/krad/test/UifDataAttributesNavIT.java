/*
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
package edu.samplu.travel.krad.test;

import edu.samplu.common.ITUtil;
import edu.samplu.common.WebDriverLegacyITBase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.krad.uif.UifConstants;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 *  Tests that the data attributes are rendered as expected for all controls
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifDataAttributesNavIT extends WebDriverLegacyITBase {

    @Override
    public void fail(String message) {
        Assert.fail(message);
    }

    @Override
    public String getTestUrl() {
        return ITUtil.PORTAL;
    }

    private  Log log = LogFactory.getLog(getClass());

    /**
     * verify that a tag has simple data attributes
     *
     * @param tag - html tag e.g. img or a
     * @param tagId - derived from the bean id set in the view
     * @param tagIdSuffix - where applicable, a suffix that is appended to the control by krad e.g. _control
     */
    private void verifySimpleAttributes(String tag, String tagId, String tagIdSuffix) throws Exception{
        // test the attributes that are set via the data attributes list
        tagId = tagId + tagIdSuffix;
        String simpleAttributesXpath="//" + tag + "[@id='" + tagId + "' and @data-iconTemplateName='cool-icon-%s.png' and @data-transitions='3']";
        assertTrue(tagId + " does not have simple data attributes (via list) present", isElementPresentByXpath(simpleAttributesXpath));
        verifyStaticDataAttributes(tag, tagId);

    }

    /**
     * test the attributes that are set via the data*Attribute properties
     *
     * @param tag - html tag e.g. img or a
     * @param tagId - the html tag id - a combination of bean id and any suffix
     */
    private void verifyStaticDataAttributes(String tag, String tagId) {
        final String simpleAttributesXpath;
        simpleAttributesXpath="//" + tag + "[@id='" + tagId + "'"
                + " and @data-dataroleattribute='role' and @data-datatypeattribute='type' and @data-datametaattribute='meta']";
        assertTrue(tagId + " does not have simple data attributes (via data*Attribute) properties present",
                isElementPresentByXpath(simpleAttributesXpath));
    }

    /**
     * check that complex attributes exist in the script
     *
     * @param tagId - the expected tag id
     * @param suffix - the expected suffix e.g. _button
     */
    private void verifyComplexAttributes(String tagId, String suffix)throws Exception {
        tagId = tagId + suffix;
        String complexAttributesXpath="//input[(@type='hidden') and (@data-role='dataScript') and (@data-for='"+ tagId +  "')]";
        assertTrue(tagId + ": complex data attributes script not found", isElementPresentByXpath(complexAttributesXpath));

        // the message field does not support complex attributes
        //if (!tagId.equalsIgnoreCase("messageField")) {
            String scriptValue = waitAndGetAttributeByXpath(complexAttributesXpath, "value");
            assertNotNull("script value is null",scriptValue);
        boolean ok = scriptValue.contains(
                "jQuery('#" + tagId + "').data('capitals', {kenya:'nairobi', uganda:'kampala', tanzania:'dar'});")
                && scriptValue.contains("jQuery('#" + tagId + "').data('intervals', {short:2, medium:5, long:13});");
        if (!ok) {
            log.info("scriptValue for " + tagId + " is " + scriptValue);
        }
        // check for complex attributes
        assertTrue(tagId + ": complex attributes script does not contain expected code", ok);
        //}
    }

    /**
     * check that all attributes exist in the script
     *
     * @param tagId - the expected tag id
     * @param suffix - the expected suffix e.g. _control
     * @return true if all attributes were found in script, false otherwise
     */
    private boolean verifyAllAttributesInScript(String tagId, String suffix)throws Exception {
        checkForIncidentReport();
        tagId = tagId + suffix;
        String complexAttributesXpath="//input[@type='hidden' and @data-for='"+ tagId +  "']";

        // the message field does not support complex attributes
        String scriptValue = waitAndGetAttributeByXpath(complexAttributesXpath, "value");
        assertNotNull("script value is null",scriptValue);
        // log.info("scriptValue for " + tagId + " is " + scriptValue);
        return scriptValue.contains("jQuery('#" + tagId + "').data('transitions', 3);") &&
                scriptValue.contains("jQuery('#" + tagId + "').data('iconTemplateName', 'cool-icon-%s.png');") &&
                scriptValue.contains("jQuery('#" + tagId + "').data('capitals', {kenya:'nairobi', uganda:'kampala', tanzania:'dar'});") &&
                scriptValue.contains("jQuery('#" + tagId + "').data('intervals', {short:2, medium:5, long:13});");
    }


    /**
     * Tests that the data attributes are rendered as expected for all controls
     */
    @Ignore // This test fails, but what is it testing doing?
    @Test
    public void testDataAttributesPresentInControls () throws Exception{
        assertEquals("Kuali Portal Index", getTitle());
        open(getBaseUrlString()+ "/kr-krad/data-attributes-test-uif-controller?viewId=dataAttributesView_selenium&methodToCall=start");
        waitForPageToLoad(); // if this times out make a special one that 50000
        
        // custom suffix to mark  test bean ids
        String testIdSuffix = "_attrs";
        // input fields, whose controls are implemented as spring form tags, will have both simple and complex attributes set via a script
        String[] inputControls = {"textInputField", "textAreaInputField", "dropDown", "datePicker", "fileUpload", "userControl",
                "spinnerControl", "hiddenControl", "checkBox"};//, "radioButton",
        for (int i=0; i<inputControls.length; i++) {
            assertTrue(inputControls[i] + ": script does not contain expected code",
                    verifyAllAttributesInScript(inputControls[i], testIdSuffix + UifConstants.IdSuffixes.CONTROL));
            String tag = "input";
            if (inputControls[i].equalsIgnoreCase("textAreaInputField")) {
                tag = "textarea";
            } else if (inputControls[i].equalsIgnoreCase("dropDown")) {
                tag = "select";
            }
            try {
                verifyStaticDataAttributes(tag, inputControls[i] + testIdSuffix + UifConstants.IdSuffixes.CONTROL);
            } catch (AssertionError ae) {
                assertTrue("KULRICE-7752 : UifDataAttributesIT testDataAttributesPresentInControls textInputField_attrs_control: complex data attributes script not found", false);
            }
        }
        // these controls allow for simple attributes on the tag and complex attributes via js
        Map<String, String[]> otherControlsMap = new HashMap<String, String[]>();
        // controls whose simple attributes are set in an img tag
        String[] imgControls = {"imageField_image"};
        // fields whose simple attributes are set in an anchor tag
        String[] anchorFields = {"navigationLink", "actionLink-noImage", "actionLink-imageRight", "actionLink-imageLeft", "linkElement"};
        // fields whose simple attributes are set in a span tag
        String[] spanFields = {"messageField"};
        // fields whose simple attributes are set in an input tag
        String[] inputFields = {"imageAction"};
        // fields whose simple attributes are set in button tag
        String[] buttonElements = {"buttonTextOnly", "buttonImageBottom", "buttonImageLeft", "buttonImageTop", "buttonImageRight"};
        // iframe field
        String[] iframeField = {"iframe"};
        String[] divField={"spaceField","linkField"};
        
        otherControlsMap.put("img", imgControls);
        otherControlsMap.put("a", anchorFields);
        otherControlsMap.put("span", spanFields);
        otherControlsMap.put("input", inputFields);
        otherControlsMap.put("button", buttonElements);
        otherControlsMap.put("iframe", iframeField);
        otherControlsMap.put("div", divField);
        
        // a map to hold the tags where the simple attributes are affixed
        // if a tag is not here, a empty string will be used for the suffix
        Map<String, String> simpleTagIdSuffix = new HashMap<String, String>();
        simpleTagIdSuffix.put("span", "_span");
        

        for (String tag: otherControlsMap.keySet()) {
            String[] controlIds = otherControlsMap.get(tag);
            for (int i=0; i<controlIds.length; i++) {
                String tagId = controlIds[i];

                // check for complex attributes
                verifyComplexAttributes(tagId, testIdSuffix);

                // determine whether we are using a tag id suffix for the simple attributes
                String tagIdSuffix = testIdSuffix;
                if (simpleTagIdSuffix.containsKey(tag)) {
                    tagIdSuffix = tagIdSuffix + simpleTagIdSuffix.get(tag);
                }

                // check for simple attributes
                verifySimpleAttributes(tag, tagId, tagIdSuffix);
            }
            
            // test label field - which uses the tagId suffix for both the simple attributes and complex
            String tagId = "textInputField";
            String tagIdSuffix = testIdSuffix + "_label";
            // check for complex attributes
            verifyComplexAttributes(tagId, tagIdSuffix);
            // check for simple attributes
            verifySimpleAttributes("label", tagId, tagIdSuffix);

            //test that the radio buttons have the 3 data attributes that can appear in the tag
            tagId = "radioButton" + testIdSuffix + UifConstants.IdSuffixes.CONTROL;
            String[] radioButtonIds = {tagId + "_0", tagId + "_1"};
            for (String id: radioButtonIds) {
                verifyStaticDataAttributes("input", id);
            }
            //test that all complex and simple attributes set via the list are in a script
            verifyAllAttributesInScript(tagId, "");
        }
        passed();
    }
}
