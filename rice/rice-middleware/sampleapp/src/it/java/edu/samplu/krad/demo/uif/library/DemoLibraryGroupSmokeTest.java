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

import com.thoughtworks.selenium.SeleneseTestBase;
import edu.samplu.common.Failable;
import org.junit.Test;
import org.kuali.rice.krad.uif.UifConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLibraryGroupSmokeTest extends DemoLibraryBase {

    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=ComponentLibraryHome";

    @Override
    public String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-LibraryLink", "");
    }

    protected void navigationMenu() throws Exception {
        waitAndClickByLinkText("Containers");
        waitAndClickByLinkText("Group");
    }

    public void testBasicGroupNav(Failable failable) throws Exception {
        testBasicGroupBookmark(this);
    }

    /**
     * Asserts basic group elements are present: header, validation messages,
     * instructional text, and the actual items
     *
     * @param failable
     * @throws Exception
     */
    public void testBasicGroupBookmark(Failable failable) throws Exception {
        navigationMenu();

        WebElement element = driver.findElement(By.id("Demo-Group-Example1"));
        element.findElement(By.tagName("h3"));

        getElementByDataAttributeValue(UifConstants.DataAttributes.MESSAGES_FOR, "Demo-Group-Example1");

        element.findElement(By.className("uif-instructionalMessage"));

        List<WebElement> inputFields = element.findElements(By.className("uif-inputField"));
        SeleneseTestBase.assertTrue("group does not contain correct number of items", inputFields.size() == 4);
        passed();
    }

    @Test
    public void testBasicGroupBookmark() throws Exception {
        testBasicGroupBookmark(this);
    }

    @Test
    public void testBasicGroupNav() throws Exception {
        testBasicGroupNav(this);
    }
}
