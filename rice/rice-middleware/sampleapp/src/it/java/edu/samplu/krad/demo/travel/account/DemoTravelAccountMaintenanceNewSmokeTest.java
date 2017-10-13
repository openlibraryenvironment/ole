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
public class DemoTravelAccountMaintenanceNewSmokeTest extends SmokeTestBase {

    /**
     * //div[@class='fancybox-item fancybox-close']
     */
    public static final String FANCY_BOX_CLOSE_XPATH = "//div[@class='fancybox-item fancybox-close']";
    
    /**
     * //div[@class='fancybox-item fancybox-close']
     */
    public static final String FANCY_BOX_IFRAME_XPATH = "//iframe[@class='fancybox-iframe']";

    /**
     * /kr-krad/maintenance?methodToCall=start&dataObjectClassName=org.kuali.rice.krad.demo.travel.account.TravelAccount&hideReturnLink=true
     */
    public static final String BOOKMARK_URL = "/kr-krad/maintenance?methodToCall=start&dataObjectClassName=org.kuali.rice.krad.demo.travel.account.TravelAccount&hideReturnLink=true";


    /**
     * Description field
     */
    public static final String DESCRIPTION_FIELD = "document.documentHeader.documentDescription";

    /**
     * Explanation field
     */
    public static final String EXPLANATION_FIELD = "document.documentHeader.explanation";

    /**
     * Organization document number field
     */
    public static final String ORGANIZATION_DOCUMENT_NUMBER_FIELD = "document.documentHeader.organizationDocumentNumber";

    /**
     * Travel account name field
     */
    public static final String TRAVEL_ACCOUNT_NAME_FIELD = "document.newMaintainableObject.dataObject.name";

    /**
     * Travel account nUMBER field
     */
    public static final String TRAVEL_ACCOUNT_NUMBER_FIELD = "document.newMaintainableObject.dataObject.number";

    /**
     * Travel account type code field
     */
    public static final String TRAVEL_ACCOUNT_TYPE_CODE_FIELD = "document.newMaintainableObject.dataObject.extension.accountTypeCode";

    /**
     * Travel sub account field
     */
    public static final String SUB_ACCOUNT_FIELD = "document.newMaintainableObject.dataObject.subAccount";

    /**
     * Travel sub account name field
     */
    public static final String SUB_ACCOUNT_NAME_FIELD = "document.newMaintainableObject.dataObject.subAccountName";

    /**
     * Subsidized percent
     */
    public static final String SUBSIDIZED_PERCENT_FIELD = "document.newMaintainableObject.dataObject.subsidizedPercent";

    /**
     * Date created.
     */
    public static final String DATE_CREATED_FIELD = "document.newMaintainableObject.dataObject.createDate";

    /**
     * Fiscal officer ID
     */
    public static final String FISCAL_OFFICER_ID_FIELD = "document.newMaintainableObject.dataObject.foId";

    @Override
    public String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    protected void navigate() throws Exception {
        waitAndClickById("Demo-DemoLink", "");
        waitAndClickByLinkText("Account Maintenance (New)");
    }

    protected void testTravelAccountMaintenanceNew() throws Exception {
        waitAndTypeByName("document.documentHeader.documentDescription","Travel Account Maintenance New Test Document");
        waitAndTypeByName("document.newMaintainableObject.dataObject.number","a1");
        waitAndClickByXpath("//input[@alt='Direct Inquiry']");
        waitAndClickByXpath(FANCY_BOX_CLOSE_XPATH);
        assertTextPresent("Travel Account Maintenance");
    }

    protected void testTravelAccountMaintenanceEditXss() throws Exception {
        waitAndTypeByName(DESCRIPTION_FIELD,"\"/><script>alert('!')</script>");
        waitAndTypeByName(EXPLANATION_FIELD,"\"/><script>alert('!')</script>");
        waitAndTypeByName(ORGANIZATION_DOCUMENT_NUMBER_FIELD,"\"/><script>alert('!')</script>");
        waitAndTypeByName(TRAVEL_ACCOUNT_NAME_FIELD,"blah");
        waitAndTypeByName(TRAVEL_ACCOUNT_NUMBER_FIELD,"blah");
        waitAndTypeByName(TRAVEL_ACCOUNT_TYPE_CODE_FIELD,"CAT");
        waitAndTypeByName(SUB_ACCOUNT_FIELD,"a1");
        waitAndTypeByName(SUB_ACCOUNT_NAME_FIELD,"\"/><script>alert('!')</script>");
        waitAndTypeByName(SUBSIDIZED_PERCENT_FIELD,"\"/><script>alert('!')</script>");
        waitAndTypeByName(DATE_CREATED_FIELD,"\"/><script>alert('!')</script>");
        waitAndTypeByName(FISCAL_OFFICER_ID_FIELD,"\"/><script>alert('!')</script>");
        waitAndClickButtonByText("Save");
        Thread.sleep(1000);
        if(isAlertPresent())    {
            fail("XSS vulnerability identified.");
        }
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
    }

    @Test
    public void testDemoTravelAccountMaintenanceNewBookmark() throws Exception {
        testTravelAccountMaintenanceEditXss();
        testTravelAccountMaintenanceNew();
        passed();
    }

    @Test
    public void testDemoTravelAccountMaintenanceNewNav() throws Exception {
        testTravelAccountMaintenanceEditXss();
        testTravelAccountMaintenanceNew();
        passed();
    }
}