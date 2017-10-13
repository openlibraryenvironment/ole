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

package edu.samplu.krad.compview;

import com.thoughtworks.selenium.SeleneseTestBase;
import edu.samplu.common.SmokeTestBase;
import org.junit.Test;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ConstraintsSmokeTest extends SmokeTestBase {

    /**
     * /kr-krad/uicomponents?viewId=UifCompView&methodToCall=start&pageId=UifCompView-Page3
     */
    public static final String BOOKMARK_URL = "/kr-krad/uicomponents?viewId=UifCompView&methodToCall=start&pageId=UifCompView-Page3";

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickKRAD();
        waitAndClickByXpath(KITCHEN_SINK_XPATH);
        switchToWindow(KUALI_UIF_COMPONENTS_WINDOW_XPATH);
        waitAndClickByLinkText("Validation");
    }

    protected void testConstraintsField9() throws Exception {
        testField9FocusBlur();
        passed();
    }

    private void testPreRequisiteConstraint() throws Exception {
        fireEvent("field6", "focus");
        waitAndTypeByName("field6", "A");
        fireEvent("field6", "blur");
        waitAndTypeByName("field7", "");
        fireEvent("field7", "blur");
        assertAttributeClassRegexMatches("field7", REGEX_ERROR);

        waitAndTypeByName("field7", "B");
        fireEvent("field7", "blur");
        assertAttributeClassRegexMatches("field7", REGEX_VALID);

        waitAndTypeByName("field8", "");
        fireEvent("field8", "blur");
        assertAttributeClassRegexMatches("field8", REGEX_ERROR);

        clearTextByName("field8");
        waitAndTypeByName("field8", "C");
        fireEvent("field8", "blur");
        assertAttributeClassRegexMatches("field8", REGEX_VALID);

        clearTextByName("field6");
        waitAndTypeByName("field6", "");
        fireEvent("field6", "blur");
        assertAttributeClassRegexMatches("field6", REGEX_ERROR);

        clearTextByName("field7");
        waitAndTypeByName("field7", "");
        fireEvent("field7", "blur");
        assertAttributeClassRegexMatches("field7", REGEX_VALID);

        clearTextByName("field8");
        waitAndTypeByName("field8", "");
        fireEvent("field8", "blur");
        assertAttributeClassRegexMatches("field6", REGEX_VALID);
        assertAttributeClassRegexMatches("field7", REGEX_VALID);
        assertAttributeClassRegexMatches("field8", REGEX_VALID);

        clearTextByName("field8");
        waitAndTypeByName("field8", "C");
        fireEvent("field8", "blur");
        assertAttributeClassRegexMatches("field6", REGEX_ERROR);
        assertAttributeClassRegexMatches("field7", REGEX_VALID);
        assertAttributeClassRegexMatches("field8", REGEX_ERROR);

        clearTextByName("field6");
        waitAndTypeByName("field6", "A");
        fireEvent("field6", "blur");
        assertAttributeClassRegexMatches("field6", REGEX_VALID);
        assertAttributeClassRegexMatches("field7", REGEX_ERROR);
        assertAttributeClassRegexMatches("field8", REGEX_VALID);
        passed();
    }

    private void testField9FocusBlur() throws InterruptedException {
        fireEvent("field9", "focus");
        waitAndTypeByName("field9", "1");
        fireEvent("field9", "blur");
        assertAttributeClassRegexMatches("field9", REGEX_ERROR); // Sometimes get a failure here.

        fireEvent("field9", "focus");
        clearTextByName("field9");
        waitAndTypeByName("field9", "12345");
        fireEvent("field9", "blur");
        assertAttributeClassRegexMatches("field9", REGEX_VALID);
    }

    protected void testConstraints() throws Exception {
        checkForIncidentReport("testConstraints");

        // TODO break out into smaller methods, especially if a test flaps. https://jira.kuali.org/browse/KULRICE-9711
        fireEvent("field10", "focus");
        waitAndTypeByName("field10", "2");
        fireEvent("field10", "blur");
        assertAttributeClassRegexMatches("field10", REGEX_ERROR);

        fireEvent("field10", "focus");
        clearTextByName("field10");
        waitAndTypeByName("field10", "51");
        fireEvent("field10", "blur");
        assertAttributeClassRegexMatches("field10", REGEX_ERROR);

        fireEvent("field10", "focus");
        clearTextByName("field10");
        waitAndTypeByName("field10", "25");
        fireEvent("field10", "blur");
        assertAttributeClassRegexMatches("field10", REGEX_VALID);

        waitAndTypeByName("field14", "A");
        fireEvent("field14", "blur");

        assertAttributeClassRegexMatches("field14", REGEX_ERROR);
        clearTextByName("field11");
        waitAndTypeByName("field11", "A");
        fireEvent("field11", "blur");
        assertAttributeClassRegexMatches("field11", REGEX_VALID);
        assertAttributeClassRegexMatches("field14", REGEX_VALID);

        clearTextByName("field11");
        waitAndTypeByName("field11", "");
        fireEvent("field11", "blur");
        assertAttributeClassRegexMatches("field14", REGEX_ERROR);

        clearTextByName("field12");
        waitAndTypeByName("field12", "A");
        fireEvent("field12", "blur");
        assertAttributeClassRegexMatches("field14", REGEX_ERROR);

        clearTextByName("field13");
        waitAndTypeByName("field13", "A");
        fireEvent("field13", "blur");
        assertAttributeClassRegexMatches("field13", REGEX_VALID);
        assertAttributeClassRegexMatches("field14", REGEX_VALID);

        clearTextByName("field11");
        waitAndTypeByName("field11", "A");
        fireEvent("field11", "blur");
        assertAttributeClassRegexMatches("field11", REGEX_VALID);
        assertAttributeClassRegexMatches("field14", REGEX_VALID);

        waitAndTypeByName("field18", "A");
        fireEvent("field18", "blur");
        assertAttributeClassRegexMatches("field18", REGEX_ERROR);

        waitAndTypeByName("field15", "A");
        fireEvent("field15", "blur");
        assertAttributeClassRegexMatches("field15", REGEX_VALID);
        assertAttributeClassRegexMatches("field18", REGEX_VALID);

        clearTextByName("field15");
        waitAndTypeByName("field15", "");
        fireEvent("field15", "blur");
        assertAttributeClassRegexMatches("field18", REGEX_ERROR);

        clearTextByName("field16");
        waitAndTypeByName("field16", "A");
        fireEvent("field16", "blur");
        assertAttributeClassRegexMatches("field18", REGEX_ERROR);

        clearTextByName("field17");
        waitAndTypeByName("field17", "A");
        fireEvent("field17", "blur");
        assertAttributeClassRegexMatches("field17", REGEX_VALID);
        assertAttributeClassRegexMatches("field18", REGEX_VALID);

        clearTextByName("field15");
        waitAndTypeByName("field15", "A");
        fireEvent("field15", "blur");
        assertAttributeClassRegexMatches("field18", REGEX_ERROR);

        waitAndTypeByName("field23", "A");
        fireEvent("field23", "blur");
        assertAttributeClassRegexMatches("field23", REGEX_ERROR);

        clearTextByName("field19");
        waitAndTypeByName("field19", "A");
        fireEvent("field19", "blur");
        assertAttributeClassRegexMatches("field23", REGEX_VALID);

        clearTextByName("field19");
        waitAndTypeByName("field19", "");
        fireEvent("field19", "blur");
        waitAndTypeByName("field20", "B");
        fireEvent("field20", "blur");
        assertAttributeClassRegexMatches("field23", REGEX_VALID);

        clearTextByName("field20");
        waitAndTypeByName("field20", "");
        fireEvent("field20", "blur");
        assertAttributeClassRegexMatches("field23", REGEX_ERROR);

        clearTextByName("field21");
        waitAndTypeByName("field21", "C");
        fireEvent("field21", "blur");
        assertAttributeClassRegexMatches("field23", REGEX_ERROR);

        clearTextByName("field22");
        waitAndTypeByName("field22", "D");
        fireEvent("field22", "blur");
        assertAttributeClassRegexMatches("field23", REGEX_VALID);

        clearTextByName("field19");
        waitAndTypeByName("field19", "D");
        fireEvent("field19", "blur");
        assertAttributeClassRegexMatches("field23", REGEX_VALID);

        clearTextByName("field20");
        waitAndTypeByName("field20", "D");
        fireEvent("field20", "blur");
        assertAttributeClassRegexMatches("field23", REGEX_VALID);

        checkByXpath("//*[@name='field24' and @value='case1']");
        clearTextByName("field25");
        waitAndTypeByName("field25", "");
        fireEvent("field25", "blur");
        assertAttributeClassRegexMatches("field25", REGEX_ERROR);

        checkByXpath("//*[@name='field24' and @value='case4']");
        fireEvent("field24", "blur");

        for (int second = 0;; second++) {
            if (second >= waitSeconds) {
                SeleneseTestBase.fail("timeout");
            }
            try {
                if (waitAndGetAttributeByName("field25", "class").matches(REGEX_VALID)) {
                    break;
                }
            } catch (Exception e) {}
            Thread.sleep(1000);
        }

        assertAttributeClassRegexMatches("field25", REGEX_VALID);

        checkByXpath("//*[@name='field24' and @value='case1']");
        fireEvent("field24", "blur");
        clearTextByName("field25");
        waitAndTypeByName("field25", "$100");
        fireEvent("field25", "blur");

        for (int second = 0;; second++) {
            if (second >= waitSeconds) {
                SeleneseTestBase.fail("timeout");
            }
            try {
                if (waitAndGetAttributeByName("field25", "class").matches(REGEX_VALID)) {
                    break;
                }
            } catch (Exception e) {}
            Thread.sleep(1000);
        }

        assertAttributeClassRegexMatches("field25", REGEX_VALID);

        checkByXpath("//*[@name='field24' and @value='case2']");
        fireEvent("field24", "blur");

        for (int second = 0;; second++) {
            if (second >= waitSeconds) {
                SeleneseTestBase.fail("timeout");
            }
            try {
                if (waitAndGetAttributeByName("field25", "class").matches(REGEX_ERROR)) {
                    break;
                }
            } catch (Exception e) {}
            Thread.sleep(1000);
        }

        assertAttributeClassRegexMatches("field25", REGEX_ERROR);

        clearTextByName("field25");
        waitAndTypeByName("field25", "A100");
        fireEvent("field25", "blur");
        assertAttributeClassRegexMatches("field25", REGEX_VALID);

        checkByXpath("//*[@name='field24' and @value='case3']");
        fireEvent("field24", "blur");
        clearTextByName("field26");
        waitAndTypeByName("field26", "6000");
        fireEvent("field26", "blur");

        for (int second = 0;; second++) {
            if (second >= waitSeconds) {
                SeleneseTestBase.fail("timeout");
            }
            try {
                if (waitAndGetAttributeByName("field26", "class").matches(REGEX_ERROR)) {
                    break;
                }
            } catch (Exception e) {}
            Thread.sleep(1000);
        }

        assertAttributeClassRegexMatches("field26", REGEX_ERROR);

        clearTextByName("field26");
        waitAndTypeByName("field26", "501");
        fireEvent("field26", "blur");
        assertAttributeClassRegexMatches("field26", REGEX_ERROR);

        clearTextByName("field26");
        waitAndTypeByName("field26", "499");
        fireEvent("field26", "blur");
        assertAttributeClassRegexMatches("field26", REGEX_VALID);

        clearTextByName("field26");
        waitAndTypeByName("field26", "6000");
        fireEvent("field26", "blur");
        checkByXpath("//*[@name='field24' and @value='case3']");
        fireEvent("field24", "blur");

        for (int second = 0;; second++) {
            if (second >= waitSeconds) {
                SeleneseTestBase.fail("timeout");
            }
            try {
                if (waitAndGetAttributeByName("field26", "class").matches(REGEX_ERROR)) {
                    break;
                }
            } catch (Exception e) {}
            Thread.sleep(1000);
        }

        assertAttributeClassRegexMatches("field26", REGEX_ERROR);

        checkByXpath("//*[@name='field24' and @value='case4']");
        clearTextByName("field27");
        waitAndTypeByName("field27", "A");
        fireEvent("field27", "blur");
        clearTextByName("field28");
        waitAndTypeByName("field28", "");
        fireEvent("field28", "blur");

        for (int second = 0;; second++) {
            if (second >= waitSeconds) {
                SeleneseTestBase.fail("timeout");
            }
            try {
                if (waitAndGetAttributeByName("field28", "class").matches(REGEX_ERROR)) {
                    break;
                }
            } catch (Exception e) {}
            Thread.sleep(1000);
        }

        assertAttributeClassRegexMatches("field28", REGEX_ERROR);

        checkByXpath("//*[@name='field24' and @value='case3']");
        fireEvent("field24", "blur");

        for (int second = 0;; second++) {
            if (second >= waitSeconds) {
                SeleneseTestBase.fail("timeout");
            }
            try {
                if (waitAndGetAttributeByName("field28", "class").matches(REGEX_VALID)) {
                    break;
                }
            } catch (Exception e) {}
            Thread.sleep(1000);
        }

        assertAttributeClassRegexMatches("field28", REGEX_VALID);

        clearTextByName("field28");
        waitAndTypeByName("field28", "B");
        fireEvent("field28", "blur");
        checkByXpath("//*[@name='field24' and @value='case4']");
        fireEvent("field24", "blur");

        for (int second = 0;; second++) {
            if (second >= waitSeconds) {
                SeleneseTestBase.fail("timeout");
            }
            try {
                if (waitAndGetAttributeByName("field28", "class").matches(REGEX_VALID)) {
                    break;
                }
            } catch (Exception e) {}
            Thread.sleep(1000);
        }

        assertAttributeClassRegexMatches("field28", REGEX_VALID);

        clearTextByName("field31");
        waitAndTypeByName("field31", "B");
        clearTextByName("field32");
        waitAndTypeByName("field32", "B");
        waitAndTypeByName("field33", "");
        fireEvent("field33", "blur");
        assertAttributeClassRegexMatches("field33", REGEX_ERROR);

        clearTextByName("field33");
        waitAndTypeByName("field33", "B");
        fireEvent("field33", "blur");
        assertAttributeClassRegexMatches("field33", REGEX_VALID);

        clearTextByName("field32");
        waitAndTypeByName("field32", "A");
        clearTextByName("field33");
        waitAndTypeByName("field33", "");
        fireEvent("field33", "blur");
        assertAttributeClassRegexMatches("field33", REGEX_VALID);
    }

    @Test
    public void testConstraintsBookmark() throws Exception {
        testConstraints();
        passed();
    }

    @Test
    public void testConstraintsField9Bookmark() throws Exception {
        testConstraintsField9();
        passed();
    }

    @Test
    public void testPreRequisiteConstraintBookmark() throws Exception {
        testPreRequisiteConstraint();
        passed();
    }

    @Test
    public void testConstraintsNav() throws Exception {
        testConstraints();
        passed();
    }

    @Test
    public void testConstraintsField9Nav() throws Exception {
        testConstraintsField9();
        passed();
    }

    @Test
    public void testPreRequisiteConstraintNav() throws Exception {
        testPreRequisiteConstraint();
        passed();
    }
}
