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
package edu.samplu.krad.compview;

import static org.junit.Assert.assertNotSame;

import edu.samplu.common.Failable;
import edu.samplu.common.ITUtil;
import edu.samplu.common.WebDriverLegacyITBase;

/**
 * Tests the Component section in Rice.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class DeleteSubCollectionLineAbstractSmokeTestBase extends WebDriverLegacyITBase {

    /**
     * /kr-krad/uicomponents?viewId=UifCompView&methodToCall=start&readOnlyFields=field91
     */
    public static final String BOOKMARK_URL = "/kr-krad/uicomponents?viewId=UifCompView&methodToCall=start&readOnlyFields=field91";
    
    /**
     * list4[0].subList[0].field1
     */
    public static final String FIELD_ELEMENT_NAME = "list4[0].subList[0].field1";
    
    /**
     * Nav tests start at {@link edu.samplu.common.ITUtil#PORTAL}.  Bookmark Tests should override and return {@link DeleteSubCollectionLineAbstractSmokeTestBase#BOOKMARK_URL}
     * {@inheritDoc}
     * @return
     */    
    @Override
    public String getTestUrl() {
        return ITUtil.PORTAL;
    }

    protected void navigation() throws Exception {
        waitAndClickKRAD();
        waitAndClickByXpath(KITCHEN_SINK_XPATH);
        switchToWindow(KUALI_UIF_COMPONENTS_WINDOW_XPATH);
    }

    protected void testDeleteSubCollectionLineNav(Failable failable) throws Exception {
        navigation();
        testDeleteSubCollectionLine();
        passed();
    }

    protected void testDeleteSubCollectionLineBookmark(Failable failable) throws Exception {
        testDeleteSubCollectionLine();
        passed();
    }

    protected void testDeleteSubCollectionLine() throws Exception {
        // click on collections page link
        waitAndClickByLinkText(COLLECTIONS_LINK_TEXT);
        Thread.sleep(5000);

        // wait for collections page to load by checking the presence of a sub collection line item
        waitForElementPresentByName(FIELD_ELEMENT_NAME);

        // change a value in the line to be deleted
        waitAndTypeByName(FIELD_ELEMENT_NAME, "selenium");

        // click the delete button
        waitAndClickById("subCollection1_line0_del_line0_line0");
        Thread.sleep(2000);

        // confirm that the input box containing the modified value is not present
        for (int second = 0;; second++) {
            if (second >= waitSeconds)fail(TIMEOUT_MESSAGE);
            
            try {
                if (!"selenium".equals(waitAndGetAttributeByName(FIELD_ELEMENT_NAME, "value")))
                    break;
            } catch (Exception e) {}
            
            Thread.sleep(1000);
        }

        // verify that the value has changed for the input box in the line that has replaced the deleted one
        assertNotSame("selenium", waitAndGetAttributeByName(FIELD_ELEMENT_NAME, "value"));
    }
}
