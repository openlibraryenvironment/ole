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
package edu.samplu.krad.demo.lookupviews.lookup.conditionalcriteria;

import edu.samplu.common.Failable;
import edu.samplu.common.SmokeTestBase;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLookUpConditionalCriteriaAbstractSmokeTestBase extends SmokeTestBase {

    /**
     * /kr-krad/lookup?methodToCall=start&viewId=LookupSampleViewConditionalCriteria&hideReturnLink=true
     */
    public static final String BOOKMARK_URL = "/kr-krad/lookup?methodToCall=start&viewId=LookupSampleViewConditionalCriteria&hideReturnLink=true";
    
    /**
     *  lookupCriteria[number]
     */
    private static final String LOOKUP_CRITERIA_NUMBER_NAME="lookupCriteria[number]";
    
    /**
     *  Search
     */
    private static final String SEARCH="Search";
    
    /**
     *  lookupCriteria[rangeLowerBoundKeyPrefix_createDate]
     */
    private static final String LOOKUP_CRITERIA_DATE_NAME="lookupCriteria[rangeLowerBoundKeyPrefix_createDate]";
    
    /**
     *  Not read only. Date input field present.
     */
    private static final String FAILURE_MESSAGE="Not read only. Date input field present.";
    
    /**
     *  Date Created:
     */
    private static final String DATE_CREATED_MESSAGE="Date Created:";
    
    /**
     *  Date Created is a required field.
     */
    private static final String DATE_REQUIRED_MESSAGE="Date Created is a required field.";
    
    @Override
    public String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-DemoLink", "");
        waitAndClickByLinkText("Lookup Conditional Criteria");
    }

    protected void testLookUpConditionalCriteria() throws InterruptedException {
        //Case 1 - Date field required by number a1
        //It requires "Search" to be clicked twice, for showing message.
        waitAndTypeByName(LOOKUP_CRITERIA_NUMBER_NAME,"a1");
        waitAndClickButtonByText(SEARCH);
        waitAndClickButtonByText(SEARCH);
        Thread.sleep(3000);
        assertTextPresent(DATE_REQUIRED_MESSAGE);
        
        //Case 2 - Date field read only by number a2
        clearTextByName(LOOKUP_CRITERIA_NUMBER_NAME);
        waitAndTypeByName(LOOKUP_CRITERIA_NUMBER_NAME,"a2");
        waitAndClickButtonByText(SEARCH);
        Thread.sleep(3000);
        if(isElementPresentByName(LOOKUP_CRITERIA_DATE_NAME))
        {
            fail(FAILURE_MESSAGE);
        }
        
        //Case 3 - Date field hide by number a3
        clearTextByName(LOOKUP_CRITERIA_NUMBER_NAME);
        waitAndTypeByName(LOOKUP_CRITERIA_NUMBER_NAME,"a3");
        waitAndClickButtonByText(SEARCH);
        Thread.sleep(3000);
        assertTextNotPresent(DATE_CREATED_MESSAGE);
    }

    public void testLookUpConditionalCriteriaBookmark(Failable failable) throws Exception {
        testLookUpConditionalCriteria();
        passed();
    }

    public void testLookUpConditionalCriteriaNav(Failable failable) throws Exception {
        testLookUpConditionalCriteria();
        passed();
    }
}