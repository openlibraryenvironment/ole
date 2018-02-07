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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLibraryWidgetsDisclosureSmokeTest extends DemoLibraryBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-Disclosure-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-Disclosure-View&methodToCall=start";

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-LibraryLink", "");
        waitAndClickByLinkText("Widgets");
        waitAndClickByLinkText("Disclosure");
    }

    protected void testWidgetsDisclosureDefault() throws Exception {
        waitAndClickByLinkText("Default");
        WebElement exampleDiv = navigateToExample("Demo-Disclosure-Example1");

        //first example
        WebElement disclosure1 = findElement(By.id("u100085_disclosureContent"), exampleDiv);

        if (!disclosure1.isDisplayed()) {
            fail("First disclosure not displayed");
        }

        waitAndClickByLinkText("Disclosure Section");
        Thread.sleep(1000);

        if (disclosure1.isDisplayed()) {
            fail("First disclosure did not close");
        }

        //second example
        WebElement disclosure2 = findElement(By.id("u100105_disclosureContent"), exampleDiv);

        if (!disclosure2.isDisplayed()) {
            fail("Second disclosure not displayed");
        }

        waitAndClickByLinkText("Predefined Disclosure Section");
        Thread.sleep(1000);

        if (disclosure2.isDisplayed()) {
            fail("Second disclosure did not close");
        }
    }

    protected void testWidgetsDisclosureClosed() throws Exception {
        waitAndClickByLinkText("Closed");
        WebElement exampleDiv = navigateToExample("Demo-Disclosure-Example2");
        WebElement disclosure = findElement(By.cssSelector(".uif-disclosureContent"), exampleDiv);

        if (disclosure.isDisplayed()) {
            fail("Disclosure did not default closed");
        }

        waitAndClickByLinkText("Default Closed Section");
        Thread.sleep(1000);

        if (!disclosure.isDisplayed()) {
            fail("Disclosure did not open");
        }
    }

    @Test
    public void testWidgetsDisclosureBookmark() throws Exception {
        testWidgetsDisclosureDefault();
        testWidgetsDisclosureClosed();

        driver.close();
        passed();
    }

    @Test
    public void testWidgetsDisclosureNav() throws Exception {
        testWidgetsDisclosureDefault();
        testWidgetsDisclosureClosed();

        driver.close();
        passed();
    }
}
