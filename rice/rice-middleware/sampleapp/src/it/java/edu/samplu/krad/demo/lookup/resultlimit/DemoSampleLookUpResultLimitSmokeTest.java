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
package edu.samplu.krad.demo.lookup.resultlimit;

import edu.samplu.common.SmokeTestBase;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoSampleLookUpResultLimitSmokeTest extends SmokeTestBase {

    /**
     * /kr-krad/lookup?methodToCall=start&viewId=LookupSampleViewResultsLimit&hideReturnLink=true
     */
    public static final String BOOKMARK_URL = "/kr-krad/lookup?methodToCall=start&viewId=LookupSampleViewResultsLimit&hideReturnLink=true";

    /**
     * Search
     */
    public static final String SEARCH = "Search";

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-DemoLink", "");
        waitAndClickByLinkText("Lookup Results Limit");
    }

    protected void testDemoSampleLookUpResultLimit() throws Exception {
        waitAndTypeByName("lookupCriteria[number]", "a*");
        waitAndClickButtonByText(SEARCH);
        Thread.sleep(3000);
        if(isElementPresentByXpath("//table[@class='table table-condensed table-bordered uif-tableCollectionLayout dataTable']/tbody/tr[3]")) {
            fail("Search Results contains more than 2 rows.");
        }
    }

    @Ignore // this demo page has been removed
    @Test
    public void testDemoSampleLookUpResultLimitNav() throws Exception {
        testDemoSampleLookUpResultLimit();
        passed();
    }

    @Ignore // this demo page has been removed
    @Test
    public void testDemoDemoSampleLookUpResultLimitBookmark() throws Exception {
        testDemoSampleLookUpResultLimit();
        passed();
    }
}