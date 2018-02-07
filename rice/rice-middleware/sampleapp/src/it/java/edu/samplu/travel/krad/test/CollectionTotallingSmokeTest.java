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
import edu.samplu.common.SmokeTestBase;
import org.junit.Test;

/**
 * Tests the Component section in Rice.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CollectionTotallingSmokeTest extends SmokeTestBase {

    /**
     * "/kr-krad/uicomponents?viewId=UifCompView&methodToCall=start&readOnlyFields=field91";
     */
    public static final String BOOKMARK_URL = "/kr-krad/uicomponents?viewId=Demo-CollectionTotaling&methodToCall=start";

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    protected void navigate() throws InterruptedException {
        waitAndClickKRAD();
        waitAndClickByXpath(KUALI_COLLECTION_TOTALLING_XPATH);
        switchToWindow(KUALI_COLLECTION_TOTALLING_WINDOW_XPATH);               
    }

    //Code for KRAD Test Package.
    protected void testCollectionTotalling() throws Exception {
        //Scenario Asserts Changes in Total at client side
        waitForElementPresent("div#Demo-CollectionTotaling-Section1 div[role='grid'] div[data-label='Total']");
        SeleneseTestBase.assertEquals("Total: 419", getText(
                "div#Demo-CollectionTotaling-Section1 div[role='grid'] div[data-label='Total']"));
        clearText("div#Demo-CollectionTotaling-Section1 > div[role='grid'] > table > tbody div[data-parent=Demo-CollectionTotaling-Section1]  input[name='list1[0].field1']");
        waitAndType("div#Demo-CollectionTotaling-Section1 > div[role='grid'] > table > tbody div[data-parent=Demo-CollectionTotaling-Section1]  input[name='list1[0].field1']","10");
        waitAndClick("div#Demo-CollectionTotaling-Section1 div[role='grid'] div[data-label='Total']");
        Thread.sleep(5000);
        SeleneseTestBase.assertEquals("Total: 424", getText(
                "div#Demo-CollectionTotaling-Section1 div[role='grid'] div[data-label='Total']"));

        //Scenario Asserts Changes in Total at client side on keyUp
        SeleneseTestBase.assertEquals("Total: 419", getText(
                "div#Demo-CollectionTotaling-Section2 div[role='grid'] div[data-label='Total']"));
        clearText("div#Demo-CollectionTotaling-Section2 > div[role='grid'] > table > tbody div[data-parent=Demo-CollectionTotaling-Section2] input[name='list1[0].field1']");
        waitAndType("div#Demo-CollectionTotaling-Section2 > div[role='grid'] > table > tbody div[data-parent=Demo-CollectionTotaling-Section2] input[name='list1[0].field1']","9");
        waitAndClick("div#Demo-CollectionTotaling-Section2 div[role='grid'] div[data-label='Total']");
        Thread.sleep(5000);
        SeleneseTestBase.assertEquals("Total: 423", getText(
                "div#Demo-CollectionTotaling-Section2 div[role='grid'] div[data-label='Total']"));

        //Asserts absence of Total in 2nd column at the footer for Demonstrating totaling on only some columns
        SeleneseTestBase.assertEquals("", getTextByXpath("//div[3]/div[3]/table/tfoot/tr/th[2]"));

        //Asserts Presence of Total in 2nd column at the footer for Demonstrating totaling on only some columns
        SeleneseTestBase.assertEquals("Total: 369", getTextByXpath(
                "//div[3]/div[3]/table/tfoot/tr/th[3]/div/fieldset/div/div[2]/div[2]"));

        //Asserts Presence of Total in left most column only being one with no totaling itself
        SeleneseTestBase.assertEquals("Total:", getTextByXpath("//*[@id='u100213_span']"));
        SeleneseTestBase.assertEquals("419", getTextByXpath(
                "//div[4]/div[3]/table/tfoot/tr/th[2]/div/fieldset/div/div[2]/div[2]"));

        //Asserts changes in value in Total and Decimal for Demonstrating multiple types of calculations for a single column (also setting average to 3 decimal places to demonstrate passing data to calculation function)
        SeleneseTestBase.assertEquals("Total: 382", getTextByXpath("//div[2]/div/fieldset/div/div[2]/div[2]"));
        clearText("div#Demo-CollectionTotaling-Section6 > div[role='grid'] > table > tbody div[data-parent=Demo-CollectionTotaling-Section6] input[name='list1[0].field4']");
        waitAndType("div#Demo-CollectionTotaling-Section6 > div[role='grid'] > table > tbody div[data-parent=Demo-CollectionTotaling-Section6] input[name='list1[0].field4']","11");
        waitAndClick("div#Demo-CollectionTotaling-Section2 div[role='grid'] div[data-label='Total']");
        Thread.sleep(5000);
        SeleneseTestBase.assertEquals("Total: 385", getTextByXpath("//div[2]/div/fieldset/div/div[2]/div[2]"));

        // Assert changes in Decimal..
        clearText("div#Demo-CollectionTotaling-Section6 > div[role='grid'] > table > tbody div[data-parent=Demo-CollectionTotaling-Section6] input[name='list1[0].field4']");
        waitAndType("div#Demo-CollectionTotaling-Section6 > div[role='grid'] > table > tbody div[data-parent=Demo-CollectionTotaling-Section6] input[name='list1[0].field4']","15.25");
        waitAndClick("div#Demo-CollectionTotaling-Section2 div[role='grid'] div[data-label='Total']");
        Thread.sleep(5000);
        SeleneseTestBase.assertEquals("Page Average: 11.917", getTextByXpath("//div[2]/fieldset/div/div[2]/div"));
    }

    @Test
    public void testCollectionTotallingBookmark() throws Exception {
        testCollectionTotalling();
        passed();
    }

    @Test
    public void testCollectionTotallingNav() throws Exception {
        testCollectionTotalling();
        passed();
    }
}
