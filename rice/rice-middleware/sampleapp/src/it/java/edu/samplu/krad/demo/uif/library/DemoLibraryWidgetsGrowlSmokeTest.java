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
import edu.samplu.krad.demo.uif.library.DemoLibraryBase;
import org.junit.Test;
import edu.samplu.common.SmokeTestBase;
import org.kuali.rice.krad.uif.UifConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLibraryWidgetsGrowlSmokeTest extends DemoLibraryBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-Growls-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-Growls-View&methodToCall=start";

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-LibraryLink", "");
        waitAndClickByLinkText("Widgets");
        waitAndClickByLinkText("Growls");
    }

    protected void testWidgetsGrowlDefault() throws Exception {

        //check if growl text is present
        assertTextNotPresent("Sample Message Text. Data passed: none");

        //create growl
        waitAndClick(By.id("u100085"));

        //give it a second to display
        Thread.sleep(1000);

        //check growl text is present
        assertTextPresent("Sample Message Text. Data passed: none");
    }

    protected void testWidgetsDatePickerWidget() throws Exception {
        waitAndClickByLinkText(">> Open Library Navigation");
        waitAndClickByLinkText("Suggest");
        waitAndClickByLinkText("Tooltip");
        select(By.id("Demo-LargeExampleDropdown_control"), "Tooltip On Focus");

        //focus label
        fireMouseOverEvent(By.id("u100101_label"));
        Thread.sleep(1000);
        WebElement tooltipFocusLabel = driver.findElement(By.xpath("//div[@data-for=\"u100101_label\"]")).findElement(
                By.className("jquerybubblepopup-innerHtml"));
        if (!tooltipFocusLabel.isDisplayed()) {
            fail("Tooltip help for label not displayed.");
        }
        if (!tooltipFocusLabel.getText().equals("Click on the text box for a tool tip")) {
            fail("Incorrect inner html text for label focus tooltip.");
        }

        //focus control
        waitAndClickById("u100101_control");
        Thread.sleep(1000);
        WebElement tooltipFocusControl = driver.findElement(By.xpath("//div[@data-for=\"u100101_control\"]"))
                .findElement(By.className("jquerybubblepopup-innerHtml"));
        if (!tooltipFocusControl.isDisplayed()) {
            fail("Tooltip help for control not displayed.");
        }
        if (!tooltipFocusControl.getText().equals("This tooltip appears when the field receives focus")) {
            fail("Incorrect inner html text for tooltip focus control.");
        }
    }

    @Test
    public void testWidgetsGrowlBookmark() throws Exception {
        testWidgetsGrowlDefault();
        driver.close();
        passed();
    }

    @Test
    public void testWidgetsGrowlNav() throws Exception {
        testWidgetsGrowlDefault();
        driver.close();
        passed();
    }
}
