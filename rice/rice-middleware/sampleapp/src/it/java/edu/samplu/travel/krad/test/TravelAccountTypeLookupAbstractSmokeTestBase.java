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
package edu.samplu.travel.krad.test;

import edu.samplu.common.Failable;
import edu.samplu.common.ITUtil;
import edu.samplu.common.WebDriverLegacyITBase;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class TravelAccountTypeLookupAbstractSmokeTestBase extends WebDriverLegacyITBase {

    /**
     * /portal.do?channelTitle=Travel%20Account%20Type%20Lookup&channelUrl=" +ITUtil.getBaseUrlString()+ /kr-krad/uicomponents?viewId=UifCompView&methodToCall=start&pageId=UifCompView-Page3
     */
    public static final String BOOKMARK_URL = "/portal.do?channelTitle=Travel%20Account%20Type%20Lookup&channelUrl="
            + ITUtil.getBaseUrlString() + ITUtil.KRAD_LOOKUP_METHOD
            + "edu.sampleu.travel.bo.TravelAccountType";

    /**
     * //*[contains(button,"earch")]/button
     */
    public static final String SEARCH_BUTTON_XPATH ="//*[contains(button,\"earch\")]/button";

    protected String getDataTableTrTd1ContainsXpath(String contains) {
        return "//table[@class='uif-tableCollectionLayout dataTable']//tr[contains(td[1],'" + contains + "')]";
    }

    /**
     * Nav tests start at {@link edu.samplu.common.ITUtil#PORTAL}.  Bookmark Tests should override and return {@link TravelAccountTypeLookupAbstractSmokeTestBase#BOOKMARK_URL}
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getTestUrl() {
        return ITUtil.PORTAL;
    }

    protected void navigation() throws Exception {
        waitAndClickByLinkText("KRAD");
        waitAndClickByLinkText("Travel Account Type Lookup");
    }

    protected void testTravelAccountTypeLookupNav(Failable failable) throws Exception {
        navigation();
        testTravelAccountTypeLookup();
        passed();
    }

    protected void testTravelAccountTypeLookupBookmark(Failable failable) throws Exception {
        testTravelAccountTypeLookup();
        passed();
    }

    protected void testTravelAccountTypeLookup() throws Exception {
        selectFrameIframePortlet();

        //Blank Search
        waitAndClickByXpath(SEARCH_BUTTON_XPATH + "[1]");
        Thread.sleep(4000);
        assertElementPresentByXpath(getDataTableTrTd1ContainsXpath("CAT"));
        assertElementPresentByXpath(getDataTableTrTd1ContainsXpath("EAT"));
        assertElementPresentByXpath(getDataTableTrTd1ContainsXpath("IAT"));

        //search with each field
        waitAndTypeByName("lookupCriteria[accountTypeCode]", "CAT");
        waitAndClickByXpath(SEARCH_BUTTON_XPATH + "[1]");
        Thread.sleep(2000);
        assertElementPresentByXpath(getDataTableTrTd1ContainsXpath("CAT"));
        waitAndClickByXpath(SEARCH_BUTTON_XPATH + "[2]");
        Thread.sleep(2000);
        waitAndTypeByName("lookupCriteria[name]", "Expense Account Type");
        waitAndClickByXpath(SEARCH_BUTTON_XPATH + "[1]");
        Thread.sleep(4000);
        assertElementPresentByXpath(getDataTableTrTd1ContainsXpath("EAT"));

        //Currently No links available for Travel Account Type Inquiry so cant verify heading and values.
    }
}