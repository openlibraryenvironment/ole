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
package edu.samplu.common;

import org.openqa.selenium.By;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class NavTemplateMethodSTBase extends SmokeTestBase {

    @Override
    protected String getBookmarkUrl() {
        return testUrl;
    }

    @Override
    public String getTestUrl() {
        return testUrl;
    }

    /**
     * TODO when the UpgradedSelenium tests have been converted over, rename this to getMenuLinkText
     * Override to return menu click selector (e.g. "Main Menu")
     * @return selenium locator to click on
     */
    protected abstract String getMenuLinkLocator();

    /**
     * TODO when the UpgradedSelenium tests have been converted over, rename this to getLinkText
     * Override to return main menu click selector (e.g. "Agenda lookup")
     * @return selenium locator to click on
     */
    protected abstract String getLinkLocator();


    /**
     * Override to return main menu click selector (e.g. "//img[@alt='create new']")
     * @return selenium locator to click on
     */
    protected abstract String getCreateNewLinkLocator();

    /**
     * go to the getMenuLinkLocator() Menu and click the getLinkLocator()
     */
    protected void gotoMenuLinkLocator(String message) throws Exception {
        waitForTitleToEqualKualiPortalIndex();
        selectTopFrame();
        waitAndClickByLinkText(getMenuLinkLocator(), message);
        waitForTitleToEqualKualiPortalIndex();
        waitAndClickByLinkText(getLinkLocator(), message);
        waitForTitleToEqualKualiPortalIndex(message);
        selectFrameIframePortlet();
        checkForIncidentReport(getLinkLocator(), message);
    }


    protected void navigate() throws Exception {
        gotoMenuLinkLocator("");
    }

    /**
     * go to having clicked create new of the getLinkLocator()
     */
    protected void gotoCreateNew() throws Exception {
        navigate();
        waitAndClick(By.xpath(getCreateNewLinkLocator()));
        //        selectFrame("relative=up");
        checkForIncidentReport(getCreateNewLinkLocator());
    }
}
