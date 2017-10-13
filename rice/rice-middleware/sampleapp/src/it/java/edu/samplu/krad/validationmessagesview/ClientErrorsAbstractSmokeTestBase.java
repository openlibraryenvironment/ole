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
package edu.samplu.krad.validationmessagesview;

import com.thoughtworks.selenium.SeleneseTestBase;
import edu.samplu.common.Failable;
import edu.samplu.common.ITUtil;
import edu.samplu.common.WebDriverLegacyITBase;
import org.openqa.selenium.By;

/**
 * Tests the Component section in Rice.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class ClientErrorsAbstractSmokeTestBase extends WebDriverLegacyITBase {

    /**
     * "/kr-krad/uicomponents?viewId=Demo-ValidationLayout&methodToCall=start"
     */
    public static final String BOOKMARK_URL = "/kr-krad/uicomponents?viewId=Demo-ValidationLayout&methodToCall=start";

    protected void bookmark() {
        open(ITUtil.getBaseUrlString() + BOOKMARK_URL);
    }

    /**
     * Nav tests start at {@link edu.samplu.common.ITUtil#PORTAL}.
     * Bookmark Tests should override and return {@link edu.samplu.krad.validationmessagesview.ClientErrorsAbstractSmokeTestBase#BOOKMARK_URL}
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getTestUrl() {
        return ITUtil.PORTAL;
    }

    protected void navigation() throws InterruptedException {
        waitAndClickKRAD();
        waitAndClickByXpath(VALIDATION_FRAMEWORK_DEMO_XPATH);
        switchToWindow(KUALI_VIEW_WINDOW_TITLE);
    }

    protected void testClientErrorsNav(Failable failable) throws Exception {
        navigation();
        testClientErrors();
        passed();
    }

    protected void testClientErrorsBookmark(Failable failable) throws Exception {
        testClientErrors();
        passed();
    }

    protected void testClientErrors() throws Exception {
        fireEvent("field1", "focus");
        fireEvent("field1", "blur");
        Thread.sleep(3000);
        fireMouseOverEventByName("field1");
        SeleneseTestBase.assertEquals("true", waitAndGetAttributeByName("field1", "aria-invalid"));
        assertAttributeClassRegexMatches("field1", REGEX_ERROR);
        SeleneseTestBase.assertTrue(isTextPresent("Required"));

        fireMouseOverEventByName("field1");

        for (int second = 0;; second++) {
            if (second >= 10) {
                SeleneseTestBase.fail(TIMEOUT_MESSAGE);
            }

            try {
                if (isVisibleByXpath("//div[@class='jquerybubblepopup jquerybubblepopup-kr-error-cs']")) {
                    break;
                }
            } catch (Exception e) {}

            Thread.sleep(1000);
        }

        SeleneseTestBase.assertTrue(isVisible(".jquerybubblepopup-innerHtml > .uif-clientMessageItems  .uif-errorMessageItem-field"));

        waitAndTypeByName("field1", "a");
        fireEvent("field1", "blur");
        fireMouseOverEventByName("field1");

        for (int second = 0;; second++) {
            if (second >= 10) {
                SeleneseTestBase.fail(TIMEOUT_MESSAGE);
            }

            try {
                if (!isVisibleByXpath("//div[@class='jquerybubblepopup jquerybubblepopup-kr-error-cs']")) {
                    break;
                }
            } catch (Exception e) {}

            Thread.sleep(1000);
        }

        SeleneseTestBase.assertFalse(isVisibleByXpath(
                "//div[@class='jquerybubblepopup jquerybubblepopup-kr-error-cs']"));

        fireEvent("field1", "blur");
        Thread.sleep(500);
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//*[@name='field1' and @aria-invalid]"));
        assertAttributeClassRegexMatches("field1", REGEX_VALID);
        SeleneseTestBase.assertTrue(isTextPresent("Required"));

        fireEvent("field2", "focus");
        fireEvent("field2", "blur");
        fireMouseOverEventByName("field2");
        Thread.sleep(500);
        //        SeleneseTestBase.assertEquals("true", waitAndGetAttributeByName("field2", "aria-invalid"));
        SeleneseTestBase.assertEquals("true", waitAndGetAttributeByName("field2", "aria-required"));
        assertAttributeClassRegexMatches("field2", REGEX_ERROR);
        SeleneseTestBase.assertTrue(isTextPresent("Required"));

        fireEvent("field2", "focus");
        waitAndTypeByName("field2", "a");
        fireEvent("field2", "blur");
        Thread.sleep(500);
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//*[@name='field2' and @aria-invalid]"));
        assertAttributeClassRegexMatches("field2", REGEX_VALID);
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//textarea[@name='field2']/../img[@alt='Error']"));

        fireEvent("field3", "focus");
        fireEvent("field3", "blur");
        fireMouseOverEventByName("field3");
        Thread.sleep(500);
        SeleneseTestBase.assertEquals("true", waitAndGetAttributeByName("field3", "aria-invalid"));
        assertAttributeClassRegexMatches("field3", REGEX_ERROR);
        SeleneseTestBase.assertTrue(isTextPresent("Required"));

        fireEvent("field3", "focus");
        selectByName("field3", "Option 1");
        fireEvent("field3", "blur");
        Thread.sleep(500);
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//*[@name='field3' and @aria-invalid]"));
        assertAttributeClassRegexMatches("field3", REGEX_VALID);
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//select[@name='field3']/../img[@alt='Error']"));

        fireEvent("field114", "focus");
        fireMouseOverEventByName("field114");
        driver.findElement(By.name("field114")).findElements(By.tagName("option")).get(0).click();
        fireEvent("field114", "blur");
        Thread.sleep(500);
        SeleneseTestBase.assertEquals("true", waitAndGetAttributeByName("field114", "aria-invalid"));
        assertAttributeClassRegexMatches("field114", REGEX_ERROR);
        SeleneseTestBase.assertTrue(isTextPresent("Required"));

        fireEvent("field114", "focus");
        selectByName("field114", "Option 1");
        fireEvent("field114", "blur");
        Thread.sleep(500);
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//*[@name='field114' and @aria-invalid]"));
        assertAttributeClassRegexMatches("field114", REGEX_VALID);
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//select[@name='field114']/../img[@alt='Error']"));

        fireEvent("field117", "3", "focus");
        uncheckByXpath("//*[@name='field117' and @value='3']");
        fireEvent("field117", "blur");
        fireMouseOverEventByName("field117");

        for (int second = 0;; second++) {
            if (second >= 10) {
                SeleneseTestBase.fail(TIMEOUT_MESSAGE);
            }

            try {
                if (isElementPresentByXpath("//div[@class='jquerybubblepopup jquerybubblepopup-kr-error-cs']")) {
                    break;
                }
            } catch (Exception e) {}

            Thread.sleep(1000);
        }

        SeleneseTestBase.assertEquals("true", waitAndGetAttributeByXpath("//*[@name='field117' and @value='1']",
                "aria-invalid"));
        SeleneseTestBase.assertTrue(waitAndGetAttributeByXpath("//*[@name='field117' and @value='1']", "class").matches(
                REGEX_ERROR));
        SeleneseTestBase.assertTrue(isTextPresent("Required"));

        fireEvent("field117", "3", "focus");
        checkByXpath("//*[@name='field117' and @value='3']");
        fireEvent("field117", "3", "blur");

        for (int second = 0;; second++) {
            if (second >= waitSeconds) {
                SeleneseTestBase.fail(TIMEOUT_MESSAGE);
            }

            try {
                if (!isElementPresentByXpath("//input[@name='field117']/../../../img[@alt='Error']")) {
                    break;
                }
            } catch (Exception e) {}

            Thread.sleep(1000);
        }

        SeleneseTestBase.assertFalse(isElementPresentByXpath("//*[@name='field117' and @value='3' and @aria-invalid]"));
        SeleneseTestBase.assertTrue(waitAndGetAttributeByXpath("//*[@name='field117' and @value='3']", "class").matches(REGEX_VALID));
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//input[@name='field117']/../../../img[@alt='Error']"));

        fireEvent("bField1", "focus");
        uncheckByName("bField1");
        fireEvent("bField1", "blur");
        fireMouseOverEventByName("bField1");
        Thread.sleep(500);
        SeleneseTestBase.assertEquals("true", waitAndGetAttributeByName("bField1", "aria-invalid"));
        assertAttributeClassRegexMatches("bField1", REGEX_ERROR);
        SeleneseTestBase.assertTrue(isTextPresent("Required"));

        fireEvent("bField1", "focus");
        checkByName("bField1");
        fireEvent("bField1", "blur");
        Thread.sleep(500);
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//*[@name='bField1' and @aria-invalid]"));
        assertAttributeClassRegexMatches("bField1", REGEX_VALID);
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//input[@name='bField1' and following-sibling::img[@alt='Error']]"));

        fireEvent("field115", "3", "focus");
        uncheckByXpath("//*[@name='field115' and @value='3']");
        uncheckByXpath("//*[@name='field115' and @value='4']");
        fireEvent("field115", "blur");
        fireMouseOverEventByName("field115");

        for (int second = 0;; second++) {
            if (second >= waitSeconds) {
                SeleneseTestBase.fail(TIMEOUT_MESSAGE);
            }

            try {
                if (isElementPresentByXpath("//div[@class='jquerybubblepopup jquerybubblepopup-kr-error-cs']")) {
                    break;
                }
            } catch (Exception e) {}

            Thread.sleep(1000);
        }

        SeleneseTestBase.assertEquals("true", waitAndGetAttributeByXpath("//*[@name='field115' and @value='1']",
                "aria-invalid"));
        SeleneseTestBase.assertTrue(waitAndGetAttributeByXpath("//*[@name='field115' and @value='1']", "class").matches(REGEX_ERROR));
        SeleneseTestBase.assertTrue(isTextPresent("Required"));

        fireEvent("field115", "3", "focus");
        checkByXpath("//*[@name='field115' and @value='3']");
        checkByXpath("//*[@name='field115' and @value='4']");
        fireEvent("field115", "blur");

        for (int second = 0;; second++) {
            if (second >= waitSeconds) {
                SeleneseTestBase.fail(TIMEOUT_MESSAGE);
            }

            try {
                if (!isElementPresentByXpath("//input[@name='field115']/../../../img[@alt='Error']")) {
                    break;
                }
            } catch (Exception e) {}

            Thread.sleep(1000);
        }

        SeleneseTestBase.assertFalse(isElementPresentByXpath("//*[@name='field115' and @value='3' and @aria-invalid]"));
        SeleneseTestBase.assertFalse(isElementPresentByXpath("//input[@name='field115']/../../../img[@alt='Error']"));
    }
}
