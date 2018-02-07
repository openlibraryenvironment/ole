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

import com.thoughtworks.selenium.SeleneseTestBase;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLibraryControlCheckboxDefaultSmokeTest extends DemoLibraryBase {

    /**
     * /kr-krad/kradsampleapp?viewId=ComponentLibraryHome
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-CheckboxControl-View";

    @Override
    public String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        navigateToExample("Demo-CheckboxControl-Example1");
    }

    protected void testCheckboxControlDefault() throws Exception {
        waitForElementPresentById("ST-DemoCheckboxControlExample1-Input1_control");
        assertTextPresent("Default Checkbox Control");
        assertLabelFor("ST-DemoCheckboxControlExample1-Input1_control", "Add me to your mailing list");
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample1-Input1_control"));
        SeleneseTestBase.assertTrue(isEnabledById("ST-DemoCheckboxControlExample1-Input1_control"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample1-Input1_control"));
        checkById("ST-DemoCheckboxControlExample1-Input1_control");
        SeleneseTestBase.assertTrue(isCheckedById("ST-DemoCheckboxControlExample1-Input1_control"));
    }


    @Test
    public void testCheckboxControlDefaultBookmark() throws Exception {
        testCheckboxControlDefault();
        passed();
    }

    @Test
    public void testCheckboxControlDefaultNav() throws Exception {
        navigateToLibraryDemo("Controls", "Checkbox");
        testCheckboxControlDefault();
        passed();
    }

    protected void testCheckboxControlOptionsFinder() throws Exception {
        navigateToExample("Demo-CheckboxControl-Example2");

        waitForElementPresentById("ST-DemoCheckboxControlExample2-Input1_control_0");
        waitForElementPresentById("ST-DemoCheckboxControlExample2-Input1_control_1");
        waitForElementPresentById("ST-DemoCheckboxControlExample2-Input1_control_2");
        waitForElementPresentById("ST-DemoCheckboxControlExample2-Input1_control_3");
        waitForElementPresentById("ST-DemoCheckboxControlExample2-Input1_control_4");
        assertTextPresent("CheckboxControl with optionsFinder set");
        assertLabelFor("ST-DemoCheckboxControlExample2-Input1_control_0", "Option 1");
        assertLabelFor("ST-DemoCheckboxControlExample2-Input1_control_1", "Option 2");
        assertLabelFor("ST-DemoCheckboxControlExample2-Input1_control_2", "Option 3");
        assertLabelFor("ST-DemoCheckboxControlExample2-Input1_control_3", "Option 4");
        assertLabelFor("ST-DemoCheckboxControlExample2-Input1_control_4", "Option 5");

        // check that checkbox controls are enabled and visible but not selected
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample2-Input1_control_0"));
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample2-Input1_control_1"));
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample2-Input1_control_2"));
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample2-Input1_control_3"));
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample2-Input1_control_4"));
        SeleneseTestBase.assertTrue(isEnabledById("ST-DemoCheckboxControlExample2-Input1_control_0"));
        SeleneseTestBase.assertTrue(isEnabledById("ST-DemoCheckboxControlExample2-Input1_control_1"));
        SeleneseTestBase.assertTrue(isEnabledById("ST-DemoCheckboxControlExample2-Input1_control_2"));
        SeleneseTestBase.assertTrue(isEnabledById("ST-DemoCheckboxControlExample2-Input1_control_3"));
        SeleneseTestBase.assertTrue(isEnabledById("ST-DemoCheckboxControlExample2-Input1_control_4"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample2-Input1_control_0"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample2-Input1_control_1"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample2-Input1_control_2"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample2-Input1_control_3"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample2-Input1_control_4"));

        // set check mark on second checkbox control
       checkById("ST-DemoCheckboxControlExample2-Input1_control_1");

        // check that only second checkbox controls is selected
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample2-Input1_control_0"));
        SeleneseTestBase.assertTrue(isCheckedById("ST-DemoCheckboxControlExample2-Input1_control_1"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample2-Input1_control_2"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample2-Input1_control_3"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample2-Input1_control_4"));
    }


    @Test
    public void testCheckboxControlOptionsFinderBookmark() throws Exception {
        testCheckboxControlOptionsFinder();
        passed();
    }

    @Test
    public void testCheckboxControlOptionsFinderNav() throws Exception {
        navigateToLibraryDemo("Controls", "Checkbox");
        testCheckboxControlOptionsFinder();
        passed();
    }

    protected void testCheckboxControlKeyValuePair() throws Exception {
        navigateToExample("Demo-CheckboxControl-Example3");

        waitForElementPresentById("ST-DemoCheckboxControlExample3-Input1_control_0");
        waitForElementPresentById("ST-DemoCheckboxControlExample3-Input1_control_1");
        assertTextPresent("CheckboxControl with key value set as options");
        assertTextPresent("CheckboxGroupControl");
        assertLabelFor("ST-DemoCheckboxControlExample3-Input1_control_0", "Check 1");
        assertLabelFor("ST-DemoCheckboxControlExample3-Input1_control_1", "Check 2");

        // check that fieldset is vertical
        WebElement we = driver.findElement(By.id("ST-DemoCheckboxControlExample3-Input1_fieldset"));
        SeleneseTestBase.assertTrue(we.getAttribute("class").matches(".*\\buif-verticalCheckboxesFieldset\\b.*"));

        // check that checkbox controls are enabled and visible but not selected
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample3-Input1_control_0"));
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample3-Input1_control_1"));
        SeleneseTestBase.assertTrue(isEnabledById("ST-DemoCheckboxControlExample3-Input1_control_0"));
        SeleneseTestBase.assertTrue(isEnabledById("ST-DemoCheckboxControlExample3-Input1_control_1"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample3-Input1_control_0"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample3-Input1_control_1"));

        // set check mark on second checkbox control
        checkById("ST-DemoCheckboxControlExample3-Input1_control_1");

        // check that only second checkbox controls is selected
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample3-Input1_control_0"));
        SeleneseTestBase.assertTrue(isCheckedById("ST-DemoCheckboxControlExample3-Input1_control_1"));
    }


    @Test
    public void testCheckboxControlKeyValuePairBookmark() throws Exception {
        testCheckboxControlOptionsFinder();
        passed();
    }

    @Test
    public void testCheckboxControlKeyValuePairNav() throws Exception {
        navigateToLibraryDemo("Controls", "Checkbox");
        testCheckboxControlKeyValuePair();
        passed();
    }

    protected void testCheckboxControlDisabled() throws Exception {
        navigateToExample("Demo-CheckboxControl-Example4");

        waitForElementPresentById("ST-DemoCheckboxControlExample4-Input1_control");
        assertTextPresent("CheckboxControl with disabled set to true");
        assertLabelFor("ST-DemoCheckboxControlExample4-Input1_control", "Add me to your mailing list");

        // check that checkbox controls is visible, not selected and disabled
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample4-Input1_control"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample4-Input1_control"));
        SeleneseTestBase.assertFalse(isEnabledById("ST-DemoCheckboxControlExample4-Input1_control"));
    }


    @Test
    public void testCheckboxControlDisabledBookmark() throws Exception {
        testCheckboxControlDisabled();
        passed();
    }

    @Test
    public void testCheckboxControlDisabledNav() throws Exception {
        navigateToLibraryDemo("Controls", "Checkbox");
        testCheckboxControlDisabled();
        passed();
    }

    protected void testCheckboxControlDelimiter() throws Exception {
        navigateToExample("Demo-CheckboxControl-Example6");

        waitForElementPresentById("ST-DemoCheckboxControlExample6-Input1_control_0");
        waitForElementPresentById("ST-DemoCheckboxControlExample6-Input1_control_1");
        assertTextPresent("HorizontalCheckboxesControl with delimiter set to ;");
        assertLabelFor("ST-DemoCheckboxControlExample6-Input1_control_0", "Check 1");
        assertLabelFor("ST-DemoCheckboxControlExample6-Input1_control_1", "Check 2");

        // check that fieldset is horizonal
        WebElement we = driver.findElement(By.id("ST-DemoCheckboxControlExample6-Input1_fieldset"));
        SeleneseTestBase.assertTrue(we.getAttribute("class").matches(".*\\buif-horizontalCheckboxesFieldset\\b.*"));

        // check that checkbox controls are enabled and visible but not selected
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample6-Input1_control_0"));
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample6-Input1_control_1"));
        SeleneseTestBase.assertTrue(isEnabledById("ST-DemoCheckboxControlExample6-Input1_control_0"));
        SeleneseTestBase.assertTrue(isEnabledById("ST-DemoCheckboxControlExample6-Input1_control_1"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample6-Input1_control_0"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample6-Input1_control_1"));

        // set check mark on second checkbox control
        checkById("ST-DemoCheckboxControlExample6-Input1_control_1");

        // check that only second checkbox controls is selected
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample6-Input1_control_0"));
        SeleneseTestBase.assertTrue(isCheckedById("ST-DemoCheckboxControlExample6-Input1_control_1"));
    }

    @Test
    public void testCheckboxControlDelimiterBookmark() throws Exception {
        testCheckboxControlDelimiter();
        passed();
    }

    @Test
    public void testCheckboxControlDelimiterNav() throws Exception {
        navigateToLibraryDemo("Controls", "Checkbox");
        testCheckboxControlDelimiter();
        passed();
    }

    protected void testCheckboxControlDisabledOnKeyEvent() throws Exception {
        navigateToExample("Demo-CheckboxControl-Example7");

        waitForElementPresentById("ST-DemoCheckboxControlExample7-Input1_control");
        waitForElementPresentById("ST-DemoCheckboxControlExample7-Input2_control");
        assertTextPresent("Evaluate the disable condition on controls which disable it on each key up event");

        // check that checkbox controls is visible, not selected and enabled
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample7-Input2_control"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample7-Input2_control"));
        SeleneseTestBase.assertTrue(isEnabledById("ST-DemoCheckboxControlExample7-Input2_control"));

        // backspace input1
        driver.findElement(By.id("ST-DemoCheckboxControlExample7-Input1_control")).sendKeys(Keys.BACK_SPACE);

        // check that checkbox controls is visible, not selected and disabled
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample7-Input2_control"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample7-Input2_control"));
        SeleneseTestBase.assertFalse(isEnabledById("ST-DemoCheckboxControlExample7-Input2_control"));
    }


    @Test
    public void testCheckboxControlDisabledOnKeyEventBookmark() throws Exception {
        testCheckboxControlDisabledOnKeyEvent();
        passed();
    }

    @Test
    public void testCheckboxControlDisabledOnKeyEventNav() throws Exception {
        navigateToLibraryDemo("Controls", "Checkbox");
        testCheckboxControlDisabledOnKeyEvent();
        passed();
    }

    protected void testCheckboxControlEnableWhenChanged() throws Exception {
        navigateToExample("Demo-CheckboxControl-Example8");

        waitForElementPresentById("ST-DemoCheckboxControlExample8-Input1_control");
        waitForElementPresentById("ST-DemoCheckboxControlExample8-Input2_control");
        assertTextPresent("Specifies the property names of fields that when changed, will enable this component");

        // check that checkbox controls is visible, not selected and disabled
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample8-Input2_control"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample8-Input2_control"));
        SeleneseTestBase.assertFalse(isEnabledById("ST-DemoCheckboxControlExample8-Input2_control"));

        // backspace input1 and remove focus from input1 (by doing an arrow down on the div)
        driver.findElement(By.id("ST-DemoCheckboxControlExample8-Input1_control")).sendKeys(Keys.BACK_SPACE);
        driver.findElement(By.id("Demo-CheckboxControl-Example8")).sendKeys(Keys.ARROW_DOWN);

        // check that checkbox controls is visible, not selected and disabled
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample8-Input2_control"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample8-Input2_control"));
        SeleneseTestBase.assertFalse(isEnabledById("ST-DemoCheckboxControlExample8-Input2_control"));

        // type "Hello" in input1 and remove focus from input1 (by doing an arrow down on the div)
        driver.findElement(By.id("ST-DemoCheckboxControlExample8-Input1_control")).sendKeys("Hello");
        driver.findElement(By.id("Demo-CheckboxControl-Example8")).sendKeys(Keys.ARROW_DOWN);

        // check that checkbox controls is visible, not selected and enabled
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample8-Input2_control"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample8-Input2_control"));
        SeleneseTestBase.assertTrue(isEnabledById("ST-DemoCheckboxControlExample8-Input2_control"));
    }

    @Test
    public void testCheckboxControlEnableWhenChangedBookmark() throws Exception {
        testCheckboxControlEnableWhenChanged();
        passed();
    }

    @Test
    public void testCheckboxControlEnableWhenChangedNav() throws Exception {
        navigateToLibraryDemo("Controls", "Checkbox");
        testCheckboxControlEnableWhenChanged();
        passed();
    }

    protected void testCheckboxControlDisableWhenChanged() throws Exception {
        navigateToExample("Demo-CheckboxControl-Example9");

        waitForElementPresentById("ST-DemoCheckboxControlExample9-Input1_control");
        waitForElementPresentById("ST-DemoCheckboxControlExample9-Input2_control");
        assertTextPresent("Specifies the property names of fields that when changed, will disable this component");

        // check that checkbox controls is visible, not selected and enabled
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample9-Input2_control"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample9-Input2_control"));
        SeleneseTestBase.assertTrue(isEnabledById("ST-DemoCheckboxControlExample9-Input2_control"));

        // backspace input1 and remove focus from input1 (by doing an arrow down on the checkbox)
        driver.findElement(By.id("ST-DemoCheckboxControlExample9-Input1_control")).sendKeys(Keys.BACK_SPACE);
        driver.findElement(By.id("Demo-CheckboxControl-Example9")).sendKeys(Keys.ARROW_DOWN);

        // check that checkbox controls is visible, not selected and enabled
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample9-Input2_control"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample9-Input2_control"));
        SeleneseTestBase.assertTrue(isEnabledById("ST-DemoCheckboxControlExample9-Input2_control"));

        // type "Hello" in input1 and remove focus from input1 (by doing an arrow down on the checkbox)
        driver.findElement(By.id("ST-DemoCheckboxControlExample9-Input1_control")).sendKeys("Hello");
        driver.findElement(By.id("Demo-CheckboxControl-Example9")).sendKeys(Keys.ARROW_DOWN);

        // check that checkbox controls is visible, not selected and disabled
        SeleneseTestBase.assertTrue(isVisibleById("ST-DemoCheckboxControlExample9-Input2_control"));
        SeleneseTestBase.assertFalse(isCheckedById("ST-DemoCheckboxControlExample9-Input2_control"));
        SeleneseTestBase.assertFalse(isEnabledById("ST-DemoCheckboxControlExample9-Input2_control"));
    }

    @Test
    public void testCheckboxControlDisableWhenChangedBookmark() throws Exception {
        testCheckboxControlDisableWhenChanged();
        passed();
    }

    @Test
    public void testCheckboxControlDisableWhenChangedNav() throws Exception {
        navigateToLibraryDemo("Controls", "Checkbox");
        testCheckboxControlDisableWhenChanged();
        passed();
    }
}
