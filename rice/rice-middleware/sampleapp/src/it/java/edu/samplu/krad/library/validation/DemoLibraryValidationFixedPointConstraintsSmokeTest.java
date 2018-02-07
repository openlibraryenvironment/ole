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
package edu.samplu.krad.library.validation;

import org.junit.Test;

import edu.samplu.common.SmokeTestBase;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLibraryValidationFixedPointConstraintsSmokeTest extends SmokeTestBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-FixedPointPatternConstraint-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-FixedPointPatternConstraint-View&methodToCall=start";
    
    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-LibraryLink", "");
        waitAndClickByLinkText("Validation");
        waitAndClickByLinkText("Fixed Point Constraint");
    }

    protected void testValidationFixedPointConstraints() throws Exception {
       //Scenario-1
       waitAndTypeByName("inputField1","1234.12");
       waitAndClickByLinkText("Usage");
       assertElementPresentByXpath("//input[@name='inputField1' and @class='uif-textControl validChar-inputField10 dirty error']");
    }
    
    protected void testValidationFixedPointConstraintsAdditional() throws Exception {
        waitAndClickByLinkText("Additional Examples");
        
        //Scenario-1
        waitAndTypeByName("inputField2","12.3");
        waitAndTypeByName("inputField3","2334.89");
        assertElementPresentByXpath("//input[@name='inputField2' and @class='uif-textControl validChar-inputField20 dirty error']");
        waitAndTypeByName("inputField2","");
        assertElementPresentByXpath("//input[@name='inputField3' and @class='uif-textControl validChar-inputField30 dirty error']");
    }
    
    @Test
    public void testValidationFixedPointConstraintsNav() throws Exception {
        testValidationFixedPointConstraints();
        testValidationFixedPointConstraintsAdditional();
        passed();
    }
    
    @Test
    public void testValidationFixedPointConstraintsBookmark() throws Exception {
        testValidationFixedPointConstraints();
        testValidationFixedPointConstraintsAdditional();
        passed();
    }
}