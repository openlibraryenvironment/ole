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

import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLibraryWidgetsHelpSmokeTest extends DemoLibraryBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-Help-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-Help-View&methodToCall=start";

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-LibraryLink", "");
        waitAndClickByLinkText("Widgets");
        waitAndClickByLinkText("Help");
    }

    protected void testWidgetsTooltipHelp() throws Exception {

        fireMouseOverEvent(By.id("Demo-Help-Field1_label"));
        WebElement helpExample1 = driver.findElement(By.xpath("//div[@data-for=\"Demo-Help-Field1_label\"]"))
                .findElement(By.className("jquerybubblepopup-innerHtml"));
        if (!helpExample1.isDisplayed()) {
            fail("Example 1 help not displayed.");
        }
        if (!helpExample1.getText().equals("Sample text for field help - label left")) {
            fail("Incorrect inner html text.");
        }
    }

    @Test
    public void testWidgetsHelpBookmark() throws Exception {
        testWidgetsTooltipHelp();

        driver.close();
        passed();
    }

    @Test
    public void testWidgetsHelpNav() throws Exception {
        testWidgetsTooltipHelp();

        driver.close();
        passed();
    }
}
