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

import edu.samplu.common.SmokeTestBase;
import edu.samplu.common.WebDriverUtil;
import org.kuali.rice.krad.uif.UifConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class DemoLibraryBase extends SmokeTestBase {

    /**
     * Automatically selects the library link and then navigate to the appropriate demo in the category
     * specified
     *
     * @param libraryMenuCategoryName the category to select in the navigation accordion by name (text)
     * @param demoItemName the demo link by name (text)
     * @throws Exception
     */
    public void navigateToLibraryDemo(String libraryMenuCategoryName, String demoItemName) throws Exception {
        selectTopFrame();
        waitAndClickById("Demo-LibraryLink", "");
        waitAndClickByLinkText(libraryMenuCategoryName);
        waitAndClickByLinkText(demoItemName);
    }

    /**
     * Automatically selects and returns the appropriate example div by the id specified, works with demos
     * that use either the tab navigation or dropdown navigation for their example navigation;
     * if the example by id is already selected, this method still verifies that the group is visible and does not fail
     *
     * @param exampleId the id of the example
     * @return the WebElement representing the example group
     * @throws Exception
     */
    public WebElement navigateToExample(String exampleId) throws Exception {
        waitForElementPresentByClassName("demo-contactInfo"); // wait for page to load
        WebElement exampleTab;
        String tabId = "#" + exampleId + UifConstants.IdSuffixes.TAB;

        if(isElementPresentById("ComponentLibrary-TabGroup_tabList")
                && isElementPresentByDataAttributeValue(UifConstants.DataAttributes.TAB_FOR, exampleId)){
            WebElement menuItem = getElementByDataAttributeValue(UifConstants.DataAttributes.TAB_FOR, exampleId);
            WebDriverUtil.highlightElement(driver, menuItem.findElement(By.cssSelector("a")));
            menuItem.findElement(By.cssSelector("a")).click();

            waitForElementPresent(tabId);
            waitForElementVisible(tabId, "");
        } else if(isElementPresent(By.cssSelector("#Demo-LargeExampleDropdown_control"))){
            selectOption(By.cssSelector("#Demo-LargeExampleDropdown_control"), exampleId);
            waitForElementPresent(tabId);
            waitForElementVisible(tabId, "");
            driver.findElement(By.cssSelector(tabId));
        } else {
            return null; // don't fail to avoid exception during setUp which results in failure not being recorded.
        }
        exampleTab = driver.findElement(By.cssSelector(tabId));
        return exampleTab;
    }
}
