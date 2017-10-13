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
package edu.samplu.krad.library.clientresponsiveness;

import org.junit.Test;

import edu.samplu.common.Failable;
import edu.samplu.common.ITUtil;
import edu.samplu.common.SmokeTestBase;
import edu.samplu.common.WebDriverLegacyITBase;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LibraryClientResponsivenessDisableSmokeTest extends SmokeTestBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-Disable-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-Disable-View&methodToCall=start";
   
    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-LibraryLink", "");
        waitAndClickByLinkText("Client Responsiveness");
        waitAndClickByLinkText("Disable");
    }

    protected void testClientResponsivenessDisable() throws Exception {
       //Scenario-1 - enabled
       waitAndClickByXpath("//input[@type='radio' and @value='enable']");
       waitAndTypeByName("inputField2","a");
       waitAndTypeByName("inputField3","b");
       selectByName("inputField4", "Option 2");
       selectByName("multiSelectField1","Option 3");
       waitAndClickByXpath("//input[@type='radio' and @value='1']");
       waitAndClickByName("checkboxesField1");
       
       //Scenario-2 - disabled
       waitAndClickByXpath("//input[@type='radio' and @value='disable']");
       if(isEnabledByName("inputField2") && isEnabledByName("inputField3") && isEnabledByName("inputField4") && isEnabledByName("inputField6") &&
               isEnabledByName("inputField7") && isEnabledByName("multiSelectField1"))
       {
           fail("Field Not Disabled Properly.");
       }
    }
    
    protected void testClientResponsivenessDisableOnChange() throws Exception {
        selectByName("exampleShown","On change");
        
        //Scenario-1
        assertElementPresentByXpath("//button[@class='btn btn-primary uif-primaryActionButton uif-boxLayoutHorizontalItem disabled']");
        waitAndTypeByName("inputField10","a");
        waitAndTypeByName("inputField11","a");
        if(isElementPresentByXpath("//button[@class='btn btn-primary uif-primaryActionButton uif-boxLayoutHorizontalItem disabled']"))
        {
            fail("Constraint not working properly.");
        }
        waitAndTypeByName("inputField10", "");
        assertElementPresentByXpath("//button[@class='btn btn-primary uif-primaryActionButton uif-boxLayoutHorizontalItem disabled']");
      }
    
    protected void testClientResponsivenessDisableOnKeyUp() throws Exception {
        selectByName("exampleShown","On keyup");
        waitAndTypeByName("inputField13","disable");
        assertElementPresentByXpath("//button[contains(text(),'Action Button (keyUp)') and @class='btn btn-primary uif-primaryActionButton uif-boxLayoutHorizontalItem disabled']");
        waitAndClickByLinkText("Usage");
        assertElementPresentByXpath("//button[contains(text(),'Action Button (change)') and @class='btn btn-primary uif-primaryActionButton uif-boxLayoutHorizontalItem disabled']");
     }
    
    protected void testClientResponsivenessDisableInCollections() throws Exception {
        selectByName("exampleShown","In Collections");
        assertElementPresentByXpath("//input[@name='newCollectionLines['collection1'].field2' and @class='uif-textControl valid ignoreValid']");
        selectByName("newCollectionLines['collection1'].field1","Disable");
        assertElementPresentByXpath("//input[@name='newCollectionLines['collection1'].field2' and @class='uif-textControl ignoreValid disabled']");
     }
    
    protected void testClientResponsivenessDisableColl() throws Exception {
        selectByName("exampleShown","Coll. SpringEL Functions");
        waitAndClickByXpath("//input[@name='checkboxesField2' and value='1']");
        assertElementPresentByXpath("//input[@name='inputField20' and @class='uif-textControl disabled']");
        waitAndClickByXpath("//input[@name='checkboxesField2' and value='2']");
        waitAndClickByXpath("//input[@name='checkboxesField2' and value='3']");
        assertElementPresentByXpath("//input[@name='inputField21' and @class='uif-textControl disabled']");
     }
    
    @Test
    public void testClientResponsivenessDisableBookmark() throws Exception {
        testClientResponsivenessDisable();
        testClientResponsivenessDisableOnChange();
        testClientResponsivenessDisableOnKeyUp();
        testClientResponsivenessDisableInCollections();
        testClientResponsivenessDisableColl();
        passed();
    }

    @Test
    public void testClientResponsivenessDisableNav() throws Exception {
        testClientResponsivenessDisable();
        testClientResponsivenessDisableOnChange();
        testClientResponsivenessDisableOnKeyUp();
        testClientResponsivenessDisableInCollections();
        testClientResponsivenessDisableColl();
        
        passed();
    }  
}