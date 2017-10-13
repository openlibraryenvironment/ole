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

import edu.samplu.common.Failable;
import edu.samplu.common.ITUtil;
import edu.samplu.common.WebDriverLegacyITBase;

/**
 * Tests the Component section in Rice.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class RichMessagesAbstractSmokeTestBase extends WebDriverLegacyITBase {

    /**
     * /kr-krad/uicomponents?viewId=RichMessagesView&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/uicomponents?viewId=RichMessagesView&methodToCall=start";
            
    /**
     * Nav tests start at {@link edu.samplu.common.ITUtil#PORTAL}.  Bookmark Tests should override and return {@link RichMessagesAbstractSmokeTestBase#BOOKMARK_URL}
     * {@inheritDoc}
     * @return
     */    
    @Override
    public String getTestUrl() {
        return ITUtil.PORTAL;
    }

    protected void navigation() throws Exception {
        waitAndClickKRAD();
        waitAndClickByXpath("(//a[contains(text(),'Rich Messages')])[2]");
        switchToWindow(RICH_MESSAGES_WINDOW_TITLE);        
        checkForIncidentReport(RICH_MESSAGES_WINDOW_TITLE);
        Thread.sleep(9000);
    }

    protected void testRichMessagesNav(Failable failable) throws Exception {
        navigation();
        //Verify Basic Functionality Section
        super.verifyRichMessagesValidationBasicFunctionality();
                
        //Verify Advanced Functionality Section
        super.verifyRichMessagesValidationAdvancedFunctionality();
                
        //Verify Letters and Numbers Validation
        super.verifyRichMessagesValidationLettersNumbersValidation();
                
        //Verify Radio and Checkbox groups rich messages Section
        super.verifyRichMessagesValidationRadioAndCheckBoxGroupFunctionality();
                
        //Verify Link Declarations Section
        super.verifyRichMessagesValidationLinkDeclarationsFunctionality();
        passed();
    }

    protected void testRichMessagesBookmark(Failable failable) throws Exception {
        checkForIncidentReport(getTestUrl());
        Thread.sleep(9000);
        
        //Verify Basic Functionality Section
        super.verifyRichMessagesValidationBasicFunctionality();
                
        //Verify Advanced Functionality Section
        super.verifyRichMessagesValidationAdvancedFunctionality();
                
        //Verify Letters and Numbers Validation
        super.verifyRichMessagesValidationLettersNumbersValidation();
                
        //Verify Radio and Checkbox groups rich messages Section
        super.verifyRichMessagesValidationRadioAndCheckBoxGroupFunctionality();
                
        //Verify Link Declarations Section
        super.verifyRichMessagesValidationLinkDeclarationsFunctionality();
        passed();
    }
  
}
