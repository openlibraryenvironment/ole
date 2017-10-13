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
package edu.samplu.krad.demo.uif.library;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.rice.krad.uif.UifConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLibraryDataFieldSmokeTest extends DemoLibraryBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-DataField-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-DataField-View&methodToCall=start";

    @Override
    public String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        navigateToLibraryDemo("Fields", "Data Field");
    }

    protected void testDataFieldDefault() throws Exception {
        WebElement exampleDiv = navigateToExample("Demo-DataField-Example1");
        WebElement field = exampleDiv.findElement(By.cssSelector("div[data-label='DataField 1']"));

        String fieldId = field.getAttribute("id");
        String controlId = fieldId + UifConstants.IdSuffixes.CONTROL;

        assertIsVisible("#" + fieldId);
        assertIsVisible("label[for='" + controlId + "']");

        WebElement label = field.findElement(By.cssSelector("label[for='" + controlId + "']"));
        if(!label.getText().contains("DataField 1:")){
            fail("Label text does not match");
        }

        assertIsVisible("#" + controlId);

        assertTextPresent("1001", "#" + controlId, "DataField value not correct");
    }

    protected void testDataFieldLabelTop() throws Exception {
        WebElement exampleDiv = navigateToExample("Demo-DataField-Example2");
        WebElement field = exampleDiv.findElement(By.cssSelector("div[data-label='DataField 1']"));

        String fieldId = field.getAttribute("id");
        String controlId = fieldId + UifConstants.IdSuffixes.CONTROL;

        assertIsVisible("#" + fieldId);
        assertIsVisible("label[for='" + controlId + "']");

        WebElement label = field.findElement(By.cssSelector("label[for='" + controlId + "']"));
        if(!label.getText().contains("DataField 1:")){
            fail("Label text does not match");
        }

        WebElement labelspan = field.findElement(By.cssSelector("span[data-label_for='" + fieldId + "']"));
        // top and bottom add the uif-labelBlock class
        if(!labelspan.getAttribute("class").contains("uif-labelBlock")){
            fail("Label span does not contain the appropriate class expected");
        }

        assertIsVisible("#" + controlId);
    }

    protected void testDataFieldLabelRight() throws Exception {
        WebElement exampleDiv = navigateToExample("Demo-DataField-Example3");
        WebElement field = findElement(By.cssSelector("div[data-label='DataField 1']"), exampleDiv);

        String fieldId = field.getAttribute("id");
        String controlId = fieldId + UifConstants.IdSuffixes.CONTROL;

        assertIsVisible("#" + fieldId);
        assertIsVisible("label[for='" + controlId + "']");

        WebElement label = findElement(By.cssSelector("[for='" + controlId + "']"), field);
        if(!label.getText().contains("DataField 1")){
            fail("Label text does not match");
        }

        assertIsVisible("#" + controlId);

        // validate that the label comes after the value
        findElement(By.cssSelector("span[id='" + controlId + "'] + span[data-label_for='" + fieldId + "']"), exampleDiv);
    }

    protected void testDataFieldDefaultValue() throws Exception {
        String valueText = textValueUnderTest("Demo-DataField-Example4", "DataField 2");
        Assert.assertEquals("2012", valueText);
    }

    protected void testDataFieldAppendProperty() throws Exception {
        String valueText = textValueUnderTest("Demo-DataField-Example5", "DataField 1");
        Assert.assertTrue(valueText.endsWith("ID Val"));
    }

    protected void testDataFieldReplaceProperty() throws Exception {
        String valueText = textValueUnderTest("Demo-DataField-Example6", "DataField 1");
        Assert.assertEquals("ID Val", valueText);
    }

    protected void testDataFieldReplacePropertyWithField() throws Exception {
        String valueText = textValueUnderTest("Demo-DataField-Example7", "DataField 1");
        Assert.assertEquals("My Book Title", valueText);
    }

    protected void testDataFieldAppendPropertyWithField() throws Exception {
        String valueText = textValueUnderTest("Demo-DataField-Example8", "DataField 1");
        Assert.assertEquals("1001 *-* My Book Title", valueText);
    }

    private String textValueUnderTest(String example, String testLabel) throws Exception {
        WebElement exampleDiv = navigateToExample(example);
        WebElement field = findElement(By.cssSelector("div[data-label='" + testLabel + "']"), exampleDiv);

        String fieldId = field.getAttribute("id");
        String controlId = fieldId + UifConstants.IdSuffixes.CONTROL;

        assertIsVisible("#" + fieldId);
        assertIsVisible("label[for='" + controlId + "']");

        WebElement label = findElement(By.cssSelector("[for='" + controlId + "']"), field);
        if(!label.getText().contains(testLabel)){
            fail("Label text does not match");
        }

        assertIsVisible("#" + controlId);

        return findElement(By.id(controlId), field).getText();
    }

    protected void testDataFieldExamples() throws Exception{
        testDataFieldDefault();
        testDataFieldLabelTop();
        testDataFieldLabelRight();
        testDataFieldDefaultValue();
        testDataFieldAppendProperty();
        testDataFieldReplaceProperty();
        testDataFieldReplacePropertyWithField();
        testDataFieldAppendPropertyWithField();
    }

    @Test
    public void testDataFieldExamplesBookmark() throws Exception {
        testDataFieldExamples();
        passed();
    }

    @Test
    public void testDataFieldExamplesNav() throws Exception {
        testDataFieldExamples();
        passed();
    }

    @Test
    public void testDataFieldDefaultBookmark() throws Exception {
        testDataFieldDefault();
        passed();
    }

    @Test
    public void testDataFieldDefaultNav() throws Exception {
        testDataFieldDefault();
        passed();
    }

    @Test
    public void testDataFieldLabelTopBookmark() throws Exception {
        testDataFieldLabelTop();
        passed();
    }

    @Test
    public void testDataFieldLabelTopNav() throws Exception {
        testDataFieldLabelTop();
        passed();
    }

    @Test
    public void testDataFieldLabelRightBookmark() throws Exception {
        testDataFieldLabelRight();
        passed();
    }

    @Test
    public void testDataFieldLabelRightNav() throws Exception {
        testDataFieldLabelRight();
        passed();
    }

    @Test
    public void testDataFieldDefaultValueBookmark() throws Exception {
        testDataFieldDefaultValue();
        passed();
    }

    @Test
    public void testDataFieldDefaultValueNav() throws Exception {
        testDataFieldDefaultValue();
        passed();
    }

    @Test
    public void testDataFieldAppendPropertyBookmark() throws Exception {
        testDataFieldAppendProperty();
        passed();
    }

    @Test
    public void testDataFieldAppendPropertyNav() throws Exception {
        testDataFieldAppendProperty();
        passed();
    }

    @Test
    public void testDataFieldReplacePropertyBookmark() throws Exception {
        testDataFieldReplaceProperty();
        passed();
    }

    @Test
    public void testDataFieldReplacePropertyNav() throws Exception {
        testDataFieldReplaceProperty();
        passed();
    }

    @Test
    public void testDataFieldReplacePropertyWithFieldBookmark() throws Exception {
        testDataFieldReplacePropertyWithField();
        passed();
    }

    @Test
    public void testDataFieldReplacePropertyWithFieldNav() throws Exception {
        testDataFieldReplacePropertyWithField();
        passed();
    }

    @Test
    public void testDataFieldAppendPropertyWithFieldBookmark() throws Exception {
        testDataFieldAppendPropertyWithField();
        passed();
    }

    @Test
    public void testDataFieldAppendPropertyWithFieldNav() throws Exception {
        testDataFieldAppendPropertyWithField();
        passed();
    }
}
