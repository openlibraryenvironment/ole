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
import edu.samplu.common.Failable;
import edu.samplu.common.ITUtil;
import edu.samplu.common.WebDriverLegacyITBase;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class BreadcrumbSmokeTestBase extends WebDriverLegacyITBase {

    /**
     * /kr-krad/uicomponents?viewId=UifCompView&methodToCall=start&pageId=UifCompView-Page3
     */
    public static final String BOOKMARK_URL = "/kr-krad/uicomponents?viewId=UifCompView&methodToCall=start&pageId=UifCompView-Page1";

    /**
     * //label[contains(text(), 'Navigate to')]
     */
    public static final String NAVIGATE_TO_LABEL_XPATH = "//label[contains(text(), 'Navigate to')]";

    /**
     * //*[@class='uif-optionList']
     */
    public static final String SECOND_BREADCRUMB_NAV_XPATH = "//*[@class='uif-optionList']";

    /**
     * //*[@id='Uif-BreadcrumbWrapper']/ol/li[2]/a
     */
    public static final String SECOND_DOWN_TRIANGLE_XPATH = "(//a[@class='uif-breadcrumbSiblingLink'])";

    /**
     * Nav tests start at {@link edu.samplu.common.ITUtil#PORTAL}.  Bookmark Tests should override and return {@link BreadcrumbSmokeTestBase#BOOKMARK_URL}
     * {@inheritDoc}
     * @return
     */

    String[][] selectAsserts = {{"UifCompView", "Uif Components"},
            {"ConfigurationTestView", "Configuration Test View"},
            {"RichMessagesView", "Rich Messages"},
            {"Demo-ReadOnlyTestView", "ReadOnly Test"},
            {"ClientDisableView", "Client-side Disable"}};

    int[] breadcrumbOrderIndexes = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 1};

    @Override
    public String getTestUrl() {
        return ITUtil.PORTAL;
    }

    protected void navigation() throws Exception {
        waitAndClickKRAD();
        waitAndClickByXpath(KITCHEN_SINK_XPATH);
        switchToWindow(KUALI_UIF_COMPONENTS_WINDOW_XPATH);
    }

    protected void testBreadcrumbBookmark(Failable failable) throws Exception {
        testBreadcrumbs();
        passed();
    }

    protected void testBreadcrumbShuffledBookmark(Failable failable) throws Exception {
        testBreadcrumbsShuffled();
        passed();
    }

    protected void testBreadcrumbShuffledNav(Failable failable) throws Exception {
        navigation();
        testBreadcrumbsShuffled();
        passed();
    }

    protected void testBreadcrumbNav(Failable failable) throws Exception {
        navigation();
        testBreadcrumbs();
        passed();
    }

    protected void testBreadcrumbNavigateToBookmark(Failable failable) throws Exception {
        testBreadcrumbNavigateTo();
        passed();
    }

    protected void testBreadcrumbNavigateToShuffledBookmark(Failable failable) throws Exception {
        testBreadcrumbNavigateToShuffled();
        passed();
    }

    protected void testBreadcrumbNavigateToNav(Failable failable) throws Exception {
        navigation();
        testBreadcrumbNavigateTo();
        passed();
    }

    protected void testBreadcrumbNavigateToShuffledNav(Failable failable) throws Exception {
        navigation();
        testBreadcrumbNavigateToShuffled();
        passed();
    }

    protected void testBreadcrumb(int pageNumber) throws Exception {
        // <ul id="u13_control" class="uif-optionList" data-control_for="u13" tabindex="0"><li class="uif-optionList-item uif-optionList-selectedItem"><a href="http://env1.rice.kuali.org/kr-krad/uicomponents?methodToCall=start&pageId=UifCompView-Page1&viewId=UifCompView" data-key="UifCompView-Page1">
        //         Input Fields and Controls
        // </a></li>
        // <li class="uif-optionList-item"><a href="http://env1.rice.kuali.org/kr-krad/uicomponents?methodToCall=start&pageId=UifCompView-Page2&viewId=UifCompView" data-key="UifCompView-Page2">
        //         Other Fields
        // </a></li>
        // etc.
        SeleneseTestBase.assertFalse(isVisibleByXpath(SECOND_BREADCRUMB_NAV_XPATH));
        // The second ▼
        waitAndClickByXpath(SECOND_DOWN_TRIANGLE_XPATH);
        SeleneseTestBase.assertTrue(isVisibleByXpath(SECOND_BREADCRUMB_NAV_XPATH));
        waitAndClickByXpath(SECOND_DOWN_TRIANGLE_XPATH);
        SeleneseTestBase.assertFalse(isVisibleByXpath(SECOND_BREADCRUMB_NAV_XPATH));
        waitAndClickByXpath(SECOND_DOWN_TRIANGLE_XPATH);

        // The Second selection of the second ▼
        // you can't just click by link text as the same clickable text is on the left navigation.
        waitAndClickByXpath(SECOND_BREADCRUMB_NAV_XPATH +"/li[" + pageNumber + "]/a");
        waitForElementPresentById("TopLink" + pageNumber); // bottom jump to top link
        driver.getCurrentUrl().contains("pageId=UifCompView-Page" + pageNumber);
    }

    protected void testBreadcrumbs() throws Exception {
        for (int i = 0, s = breadcrumbOrderIndexes.length; i < s; i++) {
            testBreadcrumb(breadcrumbOrderIndexes[i]);
        }
    }

    protected void testBreadcrumbsShuffled() throws Exception {
        int[] copiedBreadcrumbOrderIndex = Arrays.copyOf(breadcrumbOrderIndexes, breadcrumbOrderIndexes.length);

        Collections.shuffle(Arrays.asList(copiedBreadcrumbOrderIndex));
        for (int i = 0, s = copiedBreadcrumbOrderIndex.length; i < s; i++) {
            testBreadcrumb(copiedBreadcrumbOrderIndex[i]);
        }
    }

    protected void testBreadcrumbNavigateToShuffled() throws Exception {
        testBreadcrumbNavigateToSetup();

        Collections.shuffle(Arrays.asList(selectAsserts));
        for (int i = 0, s = selectAsserts.length; i < s; i++) {
            selectAndAssertNavigationTo(selectAsserts[i]);
        }
    }

    protected void testBreadcrumbNavigateTo() throws Exception {
        testBreadcrumbNavigateToSetup();

        // Not in a loop here so failures are easier to track
        selectAndAssertNavigationTo(selectAsserts[1][0], selectAsserts[1][1]);
        selectAndAssertNavigationTo(selectAsserts[2][0], selectAsserts[2][1]);
        selectAndAssertNavigationTo(selectAsserts[3][0], selectAsserts[3][1]);
        selectAndAssertNavigationTo(selectAsserts[4][0], selectAsserts[4][1]);
        selectAndAssertNavigationTo(selectAsserts[0][0], selectAsserts[0][1]);
    }

    protected void testBreadcrumbNavigateToSetup() throws InterruptedException {
        selectTopFrame();

        // div id="Uif-BreadcrumbWrapper" class="uif-sticky" data-sticky="true" style="position:fixed; left: 0; top: 39.55000305175781px;">
        waitForElementPresentById("Uif-BreadcrumbWrapper");

        // <span data-role="breadcrumb" id="u12">Input Fields and Controls</span>
        waitForElementPresentById("u12");
        SeleneseTestBase.assertEquals("Input Fields and Controls", getTextById("u12"));

        // <label id="u6610_label" for="u6610_control" data-label_for="u6610">
        //        Navigate to:
        // </label>
        SeleneseTestBase.assertFalse(isVisibleByXpath(NAVIGATE_TO_LABEL_XPATH));
        // the first ▼
        waitAndClickByLinkText("▼");
        SeleneseTestBase.assertTrue(isVisibleByXpath(NAVIGATE_TO_LABEL_XPATH));
        SeleneseTestBase.assertEquals("Navigate to:",getTextByXpath(NAVIGATE_TO_LABEL_XPATH));
        // the first ▼
        waitAndClickByLinkText("▼");
        SeleneseTestBase.assertFalse(isVisibleByXpath(NAVIGATE_TO_LABEL_XPATH));
    }

    protected void selectAndAssertNavigationTo(String[] selectAssert) throws InterruptedException {
        selectAndAssertNavigationTo(selectAssert[0], selectAssert[1]);
    }

    protected void selectAndAssertNavigationTo(String selectText, String assertText) throws InterruptedException {
        if (isTextPresent("KRAD Labs")) {
            this.fail("Should not be on KRAD Labs page, did back after Breadcrumb Navigate To work?");
        }
        waitAndClickByLinkText("▼");
        selectOption(By.xpath("//select[@name='viewId']"), selectText);
        checkForIncidentReport();
        waitForElementPresentByXpath("//span[@class='uif-headerText-span']");
        checkForIncidentReport();
        SeleneseTestBase.assertEquals(assertText, getText(By.xpath("//span[@class='uif-headerText-span']")));
        back();
    }
}