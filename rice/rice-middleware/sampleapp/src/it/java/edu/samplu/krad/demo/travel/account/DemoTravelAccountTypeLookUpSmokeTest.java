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
public class DemoTravelAccountTypeLookUpSmokeTest extends SmokeTestBase {

    /**
     * /kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krad.demo.travel.account.TravelAccountType&hideReturnLink=true
     */
    public static final String BOOKMARK_URL = "/kr-krad/lookup?methodToCall=start&dataObjectClassName=org.kuali.rice.krad.demo.travel.account.TravelAccountType&hideReturnLink=true";
    
    /**
     * Search
     */
    public static final String SEARCH = "Search";
    
    /**
     * Clear Values
     */
    public static final String CLEAR_VALUES = "Clear Values";

    /**
     * Account type code field
     */
    public static final String TRAVEL_ACCOUNT_TYPE_CODE_FIELD = "lookupCriteria[accountTypeCode]";

    /**
     * Account type name field
     */
    public static final String TRAVEL_ACCOUNT_TYPE_NAME_FIELD = "lookupCriteria[name]";
    
    @Override
    public String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-DemoLink", "");
        waitAndClickByLinkText("Account Type Lookup");
    }

    protected void testTravelAccountTypeLookUp() throws Exception {
        waitAndTypeByName(TRAVEL_ACCOUNT_TYPE_CODE_FIELD,"CAT");
        waitAndClickButtonByText(SEARCH);
        assertElementPresentByXpath("//span[contains(text(),'CAT')]");
        waitAndClickButtonByText(CLEAR_VALUES);
        waitAndClickButtonByText(SEARCH);
        waitForElementsPresentByXpath("//span[contains(text(),'IAT')]");
        assertElementPresentByXpath("//span[contains(text(),'CAT')]");
        assertElementPresentByXpath("//span[contains(text(),'EAT')]");
        assertElementPresentByXpath("//span[contains(text(),'IAT')]");
    }

    protected void testTravelAccountTypeLookUpXss(String fieldName) throws Exception {
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
    public void testTravelAccountTypeLookUpNav() throws Exception {
        testTravelAccountTypeLookUp();
        testTravelAccountTypeLookUpXss(TRAVEL_ACCOUNT_TYPE_CODE_FIELD);
        testTravelAccountTypeLookUpXss(TRAVEL_ACCOUNT_TYPE_NAME_FIELD);
        passed();
    }

    @Test
    public void testTravelAccountTypeLookUpBookmark() throws Exception {
        testTravelAccountTypeLookUp();
        testTravelAccountTypeLookUpXss(TRAVEL_ACCOUNT_TYPE_CODE_FIELD);
        testTravelAccountTypeLookUpXss(TRAVEL_ACCOUNT_TYPE_NAME_FIELD);
        passed();
    }
}