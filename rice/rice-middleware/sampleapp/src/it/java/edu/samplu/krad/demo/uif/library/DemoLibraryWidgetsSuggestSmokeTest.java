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
package edu.samplu.krad.demo.uif.library;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLibraryWidgetsSuggestSmokeTest extends DemoLibraryBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-Suggest-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-Suggest-View&methodToCall=start";

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-LibraryLink", "");
        waitAndClickByLinkText("Widgets");
        waitAndClickByLinkText("Suggest");
    }

    protected void testWidgetsSuggest(String exampleLink, String inputFieldName, String search,
            String result) throws Exception {

        //go to correct example
        waitAndClickByLinkText(exampleLink);

        //enter value
        waitAndTypeByName(inputFieldName, search);

        //verify expect suggest results
        waitForElementPresent(By.linkText(result), "Library Widget Suggest, CAT not suggested");
        assertElementPresentByLinkText(result);
        waitAndClickByLinkText(result);

        //verify text is populated
        if (!driver.findElement(By.name(inputFieldName)).getAttribute("value").equals(result)) {
            fail("input text is incorrect");
        }

    }

    protected void testWidgetsSuggestHelperMethod2() throws Exception {

        final String EXAMPLE_LINK_NAME = "View Helper Method Configuration 2";
        final String INPUT_FIELD_NAME = "inputField4";
        final String SEARCH_VALUE = "a";
        final String RESULT = "a6";

        //go to correct example
        waitAndClickByLinkText(EXAMPLE_LINK_NAME);

        //enter values
        waitAndTypeByName("inputField3", "a6-sub");
        waitAndTypeByName(INPUT_FIELD_NAME, SEARCH_VALUE);

        //verify expect suggest results
        assertElementPresentByLinkText(RESULT);
        waitAndClickByLinkText(RESULT);

        //verify text is populated
        if (!driver.findElement(By.name(INPUT_FIELD_NAME)).getAttribute("value").equals(RESULT)) {
            fail("input text is incorrect");
        }
    }

    protected void testWidgetsSuggestRichText() throws Exception {

        final String EXAMPLE_LINK_NAME = "Rich suggest options";
        final String INPUT_FIELD_NAME = "inputField8";
        final String SEARCH_VALUE = "r";

        //go to correct example
        waitAndClickByLinkText(EXAMPLE_LINK_NAME);

        //enter values
        waitAndTypeByName(INPUT_FIELD_NAME, SEARCH_VALUE);

        //verify expect suggest results
        WebElement resultLink = driver.findElement(By.partialLinkText("Rich Option 1"));
        resultLink.click();

        //verify text is populated
        if (!driver.findElement(By.name(INPUT_FIELD_NAME)).getAttribute("value").equals("r1")) {
            fail("input text is incorrect");
        }
    }

    @Test
    public void testWidgetsTooltipBookmark() throws Exception {
        testWidgetsSuggest("Auto-Query Configuration", "inputField1", "ca", "CAT");
        testWidgetsSuggest("View Helper Method Configuration 1", "inputField2", "a1", "a14");
        testWidgetsSuggestHelperMethod2();
        testWidgetsSuggest("Service Method and Sorting Configuration", "inputField6", "sub", "sub-a3");
        testWidgetsSuggest("Local suggest options", "inputField7", "cold", "ColdFusion");
        testWidgetsSuggestRichText();
        testWidgetsSuggest("Configured suggest options", "inputField9", "cold", "ColdFusion");
        testWidgetsSuggest("Custom selection", "inputField10", "jm", "jmcross");
        driver.close();
        passed();
    }

    @Test
    public void testWidgetsTooltipNav() throws Exception {
        testWidgetsSuggest("Auto-Query Configuration", "inputField1", "ca", "CAT");
        testWidgetsSuggest("View Helper Method Configuration 1", "inputField2", "a1", "a14");
        testWidgetsSuggestHelperMethod2();
        testWidgetsSuggest("Service Method and Sorting Configuration", "inputField6", "sub", "sub-a3");
        testWidgetsSuggest("Local suggest options", "inputField7", "cold", "ColdFusion");
        testWidgetsSuggestRichText();
        testWidgetsSuggest("Configured suggest options", "inputField9", "cold", "ColdFusion");
        testWidgetsSuggest("Custom selection", "inputField10", "jm", "jmcross");
        driver.close();
        passed();
    }
}


