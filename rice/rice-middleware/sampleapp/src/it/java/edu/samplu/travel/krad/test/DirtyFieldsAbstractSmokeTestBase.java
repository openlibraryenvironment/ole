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
package edu.samplu.travel.krad.test;

import com.thoughtworks.selenium.SeleneseTestBase;
import edu.samplu.common.Failable;
import edu.samplu.common.ITUtil;
import edu.samplu.common.WebDriverLegacyITBase;

/**
 * Tests the Component section in Rice.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class DirtyFieldsAbstractSmokeTestBase extends WebDriverLegacyITBase {

    /**
     * "/kr-krad/uicomponents?viewId=UifCompView&methodToCall=start&readOnlyFields=field91";
     */
    public static final String BOOKMARK_URL = "/kr-krad/uicomponents?viewId=UifCompView&methodToCall=start&readOnlyFields=field91";

    protected void bookmark() {
        open(ITUtil.getBaseUrlString() + BOOKMARK_URL);
    }

    /**
     * Nav tests start at {@link edu.samplu.common.ITUtil#PORTAL}.
     * Bookmark Tests should override and return {@link edu.samplu.travel.krad.test.DirtyFieldsAbstractSmokeTestBase#BOOKMARK_URL}
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getTestUrl() {
        return ITUtil.PORTAL;
    }

    protected void navigation() throws InterruptedException {
        waitAndClickKRAD();
        waitAndClickByLinkText(UIF_COMPONENTS_KITCHEN_SINK_LINK_TEXT);
        switchToWindow(KUALI_UIF_COMPONENTS_WINDOW_XPATH);
    }

    protected void testDirtyFieldsCheckNav(Failable failable) throws Exception {
        navigation();
        testDirtyFieldsCheck();
        passed();
    }

    protected void testDirtyFieldsCheckBookmark(Failable failable) throws Exception {
        testDirtyFieldsCheck();
        passed();
    }


    protected void testDirtyFieldsCheck() throws Exception {
        checkForIncidentReport(getTestUrl());
        waitAndClickByLinkText("Text Controls");
        waitAndTypeByName("field1", "test 1");
        waitAndTypeByName("field102", "test 2");
        assertCancelConfirmation();

        // testing manually
        waitForElementPresentByName("field100");
        waitAndTypeByName("field100", "here");
        waitAndTypeByName("field103", "there");

        // 'Validation' navigation link
        assertCancelConfirmation();

        // testing manually
        waitForElementPresentByName("field106");

        // //Asserting text-field style to uppercase. This style would display
        // input text in uppercase.
        SeleneseTestBase.assertTrue(waitAndGetAttributeByName("field112", "style").contains("text-transform: uppercase;"));
        assertCancelConfirmation();
        waitForElementPresentByName("field101");
        SeleneseTestBase.assertEquals("val", waitAndGetAttributeByName("field101", "value"));
        clearTextByName("field101");
        waitAndTypeByName("field101", "1");
        waitAndTypeByName("field104", "");
        SeleneseTestBase.assertEquals("1", waitAndGetAttributeByName("field101", "value"));
        waitAndTypeByName("field104", "2");

        // 'Progressive Disclosure' navigation link
        assertCancelConfirmation();
    }
}
