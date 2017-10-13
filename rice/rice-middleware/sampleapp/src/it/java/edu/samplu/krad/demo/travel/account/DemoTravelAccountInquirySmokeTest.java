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
package edu.samplu.krad.demo.travel.account;

import edu.samplu.common.SmokeTestBase;
import org.junit.Test;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoTravelAccountInquirySmokeTest extends SmokeTestBase {

    /**
     * //a[@title='Travel Account ']
     */
    public static final String ANCHOR_TITLE_TRAVEL_ACCOUNT = "//a[@title='Travel Account ']";

    /**
     * /kr-krad/inquiry?methodToCall=start&number=a14&dataObjectClassName=org.kuali.rice.krad.demo.travel.account.TravelAccount
     */
    public static final String BOOKMARK_URL = "/kr-krad/inquiry?methodToCall=start&number=a14&dataObjectClassName=org.kuali.rice.krad.demo.travel.account.TravelAccount";

    /**
     * Collapse All
     */
    public static final String COLLAPSE_ALL = "Collapse All";

    /**
     * Expand All
     */
    public static final String EXPAND_ALL = "Expand All";

    @Override
    public String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-DemoLink", "");
        waitAndClickByLinkText("Account Inquiry");
    }

    protected void testInquiryBackButton() throws Exception {
        waitAndClickButtonByText("Back");
        Thread.sleep(2000); // seems to take a while to load
        waitForElementPresentByXpath("//a[contains(text(), 'Account Inquiry')]");
        assertTextPresent("Demo - Travel Application", "Expected Demo - Travel Application did back work?");
    }

    protected void testInquiryBackToLoginButton() throws Exception {
        waitAndClickButtonByText("Back");
        Thread.sleep(2000); // seems to take a while to load
        waitForElementPresentById("Rice-LoginButton", "Expected Login - Travel Application Bookmark back work?");
    }

    protected void testCollapseExpand() throws InterruptedException {
        waitForElementPresentByClassName("demo-contactInfo");
        assertTextPresent("Travel Account Number:");
        assertTextPresent(EXPAND_ALL);
        assertTextPresent(COLLAPSE_ALL);
        assertIsVisibleByXpath(ANCHOR_TITLE_TRAVEL_ACCOUNT, "");

        waitAndClickButtonByText(COLLAPSE_ALL);
        assertIsNotVisibleByXpath(ANCHOR_TITLE_TRAVEL_ACCOUNT, " after clicking " + COLLAPSE_ALL);

        waitAndClickButtonByText(EXPAND_ALL);
        assertIsVisibleByXpath(ANCHOR_TITLE_TRAVEL_ACCOUNT, " after clicking " + EXPAND_ALL);
    }

    protected void testInquiryLightBox() throws Exception {
        waitAndClickByLinkText("a6");
        assertIsVisibleByXpath(ANCHOR_TITLE_TRAVEL_ACCOUNT, "");
    }

    @Test
    public void testInquiryCollapseExpandBookmark() throws Exception {
        testCollapseExpand();
        passed();
    }

    @Test
    public void testInquiryLightBoxBookmark() throws Exception {
        testInquiryLightBox();
        passed();
    }

    @Test
    public void testInquiryBackButtonBookmark() throws Exception {
        testInquiryBackToLoginButton();
        passed();
    }

    @Test
    public void testInquiryCollapseExpandNav() throws Exception {
        testCollapseExpand();
        passed();
    }

    @Test
    public void testInquiryLightBoxNav() throws Exception {
        testInquiryLightBox();
        passed();
    }

    @Test
    public void testInquiryBackButtonNav() throws Exception {
        testInquiryBackButton();
        passed();
    }
}