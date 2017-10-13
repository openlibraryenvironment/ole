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
package edu.samplu.krad.demo.lookupviews.lookup.search;

import edu.samplu.common.Failable;
import edu.samplu.common.SmokeTestBase;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLookUpSearchAbstractSmokeTestBase extends SmokeTestBase {

    /**
     * /kr-krad/lookup?methodToCall=search&viewId=LookupSampleViewURLSearch&lookupCriteria['number']=a1*&hideReturnLink=true
     */
    public static final String BOOKMARK_URL = "/kr-krad/lookup?methodToCall=search&viewId=LookupSampleViewURLSearch&lookupCriteria['number']=a1*&hideReturnLink=true";

    @Override
    public String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-DemoLink", "");
        waitAndClickByLinkText("Lookup Search");
    }

    protected void testLookUpSearch() throws InterruptedException {
        assertTextPresent("a1*");
        assertTextPresent("a1");
        assertTextPresent("a14");
        assertTextNotPresent("a2"," Test Success ! Only records with a1* appears.");
    }

    public void testLookUpSearchBookmark(Failable failable) throws Exception {
        testLookUpSearch();
        passed();
    }

    public void testLookUpSearchNav(Failable failable) throws Exception {
        testLookUpSearch();
        passed();
    }
}