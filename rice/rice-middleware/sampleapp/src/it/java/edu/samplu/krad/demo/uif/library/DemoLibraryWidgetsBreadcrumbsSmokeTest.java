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

import com.thoughtworks.selenium.SeleneseTestBase;
import org.junit.Test;
import edu.samplu.common.SmokeTestBase;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLibraryWidgetsBreadcrumbsSmokeTest extends SmokeTestBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-Breadcrumbs-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-Breadcrumbs-View&methodToCall=start";

    public static final String FIELD_TO_CHECK = "inputField9";
    public static final String START_PAGE_TITLE = "Kuali";
    public static final String TARGET_PAGE_TITLE = "Kuali :: View Title";
    public static final String TARGET_URL_CHECK = "/kr-krad/kradsampleapp?viewId=Demo-Breadcrumbs-View";

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-LibraryLink", "");
        waitAndClickByLinkText("Widgets");
        waitAndClickByLinkText("Breadcrumbs");
    }

    protected void testWidgetsBreadcrumbDefault() throws Exception {
        waitAndClickByLinkText("Default Breadcrumbs");
        switchToWindow(TARGET_PAGE_TITLE);
        SeleneseTestBase.assertTrue(driver.getCurrentUrl().contains(TARGET_URL_CHECK + "1"));
        assertElementPresentByName(FIELD_TO_CHECK);
        driver.close();
        switchToWindow(START_PAGE_TITLE);
    }

    protected void testWidgetsBreadcrumbParentLocation() throws Exception {
        waitAndClickByLinkText("ParentLocation");
        waitAndClickByLinkText("Home ParentLocation");
        switchToWindow(TARGET_PAGE_TITLE);
        SeleneseTestBase.assertTrue(driver.getCurrentUrl().contains(TARGET_URL_CHECK + "2"));
        assertElementPresentByName(FIELD_TO_CHECK);
        driver.close();
        switchToWindow(START_PAGE_TITLE);
    }

    protected void testWidgetsBreadcrumbParentLocationChain() throws Exception {
        waitAndClickByLinkText("ParentLocation Chain");
        waitAndClickByLinkText("ParentLocation Chain/Trail");
        switchToWindow(TARGET_PAGE_TITLE);
        SeleneseTestBase.assertTrue(driver.getCurrentUrl().contains(TARGET_URL_CHECK + "3"));
        assertElementPresentByName(FIELD_TO_CHECK);
        driver.close();
        switchToWindow(START_PAGE_TITLE);
    }

    protected void testWidgetsBreadcrumbParentLocationPage() throws Exception {
        waitAndClickByLinkText("ParentLocation Page");
        waitAndClickByLinkText("ParentLocation View and Page");
        switchToWindow(TARGET_PAGE_TITLE);
        SeleneseTestBase.assertTrue(driver.getCurrentUrl().contains(TARGET_URL_CHECK + "4"));
        assertElementPresentByName(FIELD_TO_CHECK);
        driver.close();
        switchToWindow(START_PAGE_TITLE);
    }

    protected void testWidgetsBreadcrumbPreViewAndPrePage() throws Exception {
        waitAndClickByLinkText("preView and prePage");
        waitAndClickByLinkText("preView and prePage breadcrumbs");
        switchToWindow(TARGET_PAGE_TITLE);
        SeleneseTestBase.assertTrue(driver.getCurrentUrl().contains(TARGET_URL_CHECK + "5"));
        assertElementPresentByName(FIELD_TO_CHECK);
        driver.close();
        switchToWindow(START_PAGE_TITLE);
    }

    protected void testWidgetsBreadcrumbBreadcrumbLabel() throws Exception {
        waitAndClickByLinkText("Breadcrumb Label");
        waitAndClickByLinkText("Override Breadcrumb Label");
        switchToWindow(TARGET_PAGE_TITLE);
        SeleneseTestBase.assertTrue(driver.getCurrentUrl().contains(TARGET_URL_CHECK + "6"));
        assertElementPresentByName(FIELD_TO_CHECK);
        driver.close();
        switchToWindow(START_PAGE_TITLE);
    }

    protected void testWidgetsBreadcrumbHomewardPath() throws Exception {
        waitAndClickByLinkText("Homeward Path");
        waitAndClickByLinkText("Homeward Path Breadcrumbs");
        switchToWindow(TARGET_PAGE_TITLE);
        SeleneseTestBase.assertTrue(driver.getCurrentUrl().contains(TARGET_URL_CHECK + "7"));
        assertElementPresentByName(FIELD_TO_CHECK);
        driver.close();
        switchToWindow(START_PAGE_TITLE);
    }

    protected void testWidgetsBreadcrumbPathBased() throws Exception {
        waitAndClickByLinkText("Path-based");
        waitAndClickByLinkText("Path-based Breadcrumbs");
        switchToWindow(TARGET_PAGE_TITLE);
        SeleneseTestBase.assertTrue(driver.getCurrentUrl().contains(TARGET_URL_CHECK + "8"));
        waitAndClickByLinkText("Page 2");
        assertElementPresentByName("inputField9");
        driver.close();
        switchToWindow(START_PAGE_TITLE);
    }

    protected void testWidgetsBreadcrumbOverrides() throws Exception {
        waitAndClickByLinkText("Overrides");
        waitAndClickByLinkText("Breadcrumb Overrides");
        switchToWindow(TARGET_PAGE_TITLE);
        SeleneseTestBase.assertTrue(driver.getCurrentUrl().contains(TARGET_URL_CHECK + "9"));
        assertElementPresentByName(FIELD_TO_CHECK);
        driver.close();
        switchToWindow(START_PAGE_TITLE);
    }

    protected void testWidgetsBreadcrumbSiblingBreadcrumbs() throws Exception {
        waitAndClickByLinkText("Sibling Breadcrumbs");
        waitAndClickById("u100164");
        switchToWindow(TARGET_PAGE_TITLE);
        SeleneseTestBase.assertTrue(driver.getCurrentUrl().contains(TARGET_URL_CHECK + "10"));
        assertElementPresentByName(FIELD_TO_CHECK);
        driver.close();
        switchToWindow(START_PAGE_TITLE);
    }

    @Test
    public void testWidgetsBreadcrumbBookmark() throws Exception {
        testWidgetsBreadcrumbDefault();
        testWidgetsBreadcrumbParentLocation();
        testWidgetsBreadcrumbParentLocationChain();
        testWidgetsBreadcrumbParentLocationPage();
        testWidgetsBreadcrumbPreViewAndPrePage();
        testWidgetsBreadcrumbBreadcrumbLabel();
        testWidgetsBreadcrumbHomewardPath();
        testWidgetsBreadcrumbPathBased();
        testWidgetsBreadcrumbOverrides();
        testWidgetsBreadcrumbSiblingBreadcrumbs();
        driver.close();
        passed();
    }

    @Test
    public void testWidgetsBreadcrumbNav() throws Exception {
        testWidgetsBreadcrumbDefault();
        testWidgetsBreadcrumbParentLocation();
        testWidgetsBreadcrumbParentLocationChain();
        testWidgetsBreadcrumbParentLocationPage();
        testWidgetsBreadcrumbPreViewAndPrePage();
        testWidgetsBreadcrumbBreadcrumbLabel();
        testWidgetsBreadcrumbHomewardPath();
        testWidgetsBreadcrumbPathBased();
        testWidgetsBreadcrumbOverrides();
        testWidgetsBreadcrumbSiblingBreadcrumbs();
        driver.close();
        passed();
    }
}
