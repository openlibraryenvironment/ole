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
import edu.samplu.common.Failable;
import org.junit.Test;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLibraryNavigationSmokeTest extends DemoLibraryNavigationBase {

    /**
     * /kr-krad/kradsampleapp?viewId=ComponentLibraryHome
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-NavigationGroup-View";

    @Override
    public String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        navigateToExample("Demo-NavigationGroup-Example1");
    }

    protected void testNavigationTabs() throws Exception {
        waitAndClickByLinkText("Navigation Group Tab Example");
        try {
            selectWindow(driver.getWindowHandles().toArray()[1].toString());
        } catch (Throwable t) {
            fail("Expected another window to be opened " + t.getCause());
        }
        waitForElementPresentByClassName("uif-headerText-span");
        SeleneseTestBase.assertTrue(driver.getTitle().contains("Kuali :: Navigation View"));
        assertTextPresent("Navigation View");
    }

    public void testNavigationMenuBookmark(Failable failable) throws Exception {
        testNavigationTabs();
        testNavigationView();
        passed();
    }

    public void testNavigationMenuNav(Failable failable) throws Exception {
        navigateToLibraryDemo("Navigation", "Navigation Group");
        testNavigationTabs();
        testNavigationView();
        passed();
    }

    @Test
    public void testNavigationMenuBookmark() throws Exception {
        testNavigationMenuBookmark(this);
    }

    @Test
    public void testNavigationMenuNav() throws Exception {
        testNavigationMenuNav(this);
    }
}
