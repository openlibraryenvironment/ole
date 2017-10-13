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
public class DemoTravelAccountLookUpSmokeTest extends SmokeTestBase {

    /**
     * /kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krad.demo.travel.account.TravelAccount&hideReturnLink=true&showMaintenanceLinks=true
     */
    public static final String BOOKMARK_URL = "/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krad.demo.travel.account.TravelAccount&hideReturnLink=true&showMaintenanceLinks=true";
    
    /**
     * Search
     */
    public static final String SEARCH = "Search";
    
    /**
     * Clear Values
     */
    public static final String CLEAR_VALUES = "Clear Values";

    /**
     * Travel account number field
     */
    public static final String TRAVEL_ACCOUNT_NUMBER_FIELD = "lookupCriteria[number]";

    /**
     * Travel account name field
     */
    public static final String TRAVEL_ACCOUNT_NAME_FIELD = "lookupCriteria[name]";

    /**
     * Sub account field
     */
    public static final String SUB_ACCOUNT_FIELD = "lookupCriteria[subAccount]";

    /**
     * Sub account name field
     */
    public static final String SUB_ACCOUNT_NAME_FIELD = "lookupCriteria[subAccountName]";

    /**
     * Fiscal officer user id
     */
    public static final String FISCCAL_OFFICER_USER_ID = "lookupCriteria[fiscalOfficer.principalName]";

    @Override
    public String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-DemoLink", "");
        waitAndClickByLinkText("Account Lookup");
    }

    protected void testTravelAccountLookUp() throws Exception {
        waitAndTypeByName(TRAVEL_ACCOUNT_NUMBER_FIELD,"a1");
        waitAndClickButtonByText(SEARCH);
        waitForElementPresentByXpath("//a[contains(text(), 'a1')]");
        waitAndClickButtonByText(CLEAR_VALUES);
        Thread.sleep(3000);
        waitAndClickButtonByText(SEARCH);
        Thread.sleep(3000);
        assertElementPresentByXpath("//a[contains(text(), 'a1')]");
        assertElementPresentByXpath("//a[contains(text(), 'a2')]");
    }

    protected void testTravelAccountLookUpXss(String fieldName) throws Exception {
        waitAndTypeByName(fieldName,"\"/><script>alert('!')</script>");
        waitAndClickButtonByText(SEARCH);
        Thread.sleep(1000);
        if(isAlertPresent())    {
            fail(fieldName + " caused XSS.");
        }
        waitAndClickButtonByText(CLEAR_VALUES);
        Thread.sleep(1000);
    }

    public boolean isAlertPresent()
    {
        try
        {
            driver.switchTo().alert();
            return true;
        }   // try
        catch (Exception Ex)
        {
            return false;
        }   // catch
    }   // isAlertPresent()


    @Test
    public void testTravelAccountLookUpBookmark() throws Exception {
        testTravelAccountLookUp();
        testTravelAccountLookUpXss(TRAVEL_ACCOUNT_NUMBER_FIELD);
//        testTravelAccountLookUpXss(TRAVEL_ACCOUNT_NAME_FIELD); // in 2.3 this is readonly (and masked)
        testTravelAccountLookUpXss(SUB_ACCOUNT_FIELD);
//        testTravelAccountLookUpXss(SUB_ACCOUNT_NAME_FIELD);  // in 2.3 this is readonly (and masked)
        testTravelAccountLookUpXss("lookupCriteria[foId]"); // in 2.3 this has a different name
        passed();
    }

    @Test
    public void testTravelAccountLookUpNav() throws Exception {
        testTravelAccountLookUp();
        testTravelAccountLookUpXss(TRAVEL_ACCOUNT_NUMBER_FIELD);
//        testTravelAccountLookUpXss(TRAVEL_ACCOUNT_NAME_FIELD); // in 2.3 this is readonly (and masked)
        testTravelAccountLookUpXss(SUB_ACCOUNT_FIELD);
//        testTravelAccountLookUpXss(SUB_ACCOUNT_NAME_FIELD);  // in 2.3 this is readonly (and masked)
        testTravelAccountLookUpXss("lookupCriteria[foId]"); // in 2.3 this has a different name
        passed();
    }
}