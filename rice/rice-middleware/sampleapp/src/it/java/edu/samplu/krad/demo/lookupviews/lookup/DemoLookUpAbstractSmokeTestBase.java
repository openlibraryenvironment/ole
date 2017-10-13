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
package edu.samplu.krad.demo.lookupviews.lookup;

import edu.samplu.common.Failable;
import edu.samplu.common.SmokeTestBase;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLookUpAbstractSmokeTestBase extends SmokeTestBase {

    /**
     * /kr-krad/lookup?methodToCall=start&viewId=LookupSampleView&hideReturnLink=true
     */
    public static final String BOOKMARK_URL = "/kr-krad/lookup?methodToCall=start&viewId=LookupSampleView&hideReturnLink=true";

    /**
     *  lookupCriteria[number]
     */
    private static final String LOOKUP_CRITERIA_NUMBER_NAME="lookupCriteria[number]";
    
    /**
     *  Search
     */
    private static final String SEARCH="Search";
    
    /**
     * Clear Values
     */
    private static final String CLEAR_VALUES="Clear Values";
    
    @Override
    public String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-DemoLink", "");
        waitAndClickByLinkText("Lookup");
    }

    protected void testLookUp() throws InterruptedException {
        waitAndTypeByName(LOOKUP_CRITERIA_NUMBER_NAME,"a1");
        waitAndClickButtonByText(SEARCH);
        Thread.sleep(3000);
        assertElementPresentByXpath("//a[contains(text(), 'a1')]");
        waitAndClickButtonByText(CLEAR_VALUES);
        waitAndClickButtonByText(SEARCH);
        Thread.sleep(3000);
        assertElementPresentByXpath("//a[contains(text(), 'a1')]");
        assertElementPresentByXpath("//a[contains(text(), 'a2')]");
    }

    public void testLookUpBookmark(Failable failable) throws Exception {
        testLookUp();
        passed();
    }

    public void testLookUpNav(Failable failable) throws Exception {
        testLookUp();
        passed();
    }
}