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
public class DemoLibraryValidationFloatingPointConstraintsSmokeTest extends SmokeTestBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-FloatingPointPatternConstraint-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-FloatingPointPatternConstraint-View&methodToCall=start";

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-LibraryLink", "");
        waitAndClickByLinkText("Validation");
        waitAndClickByLinkText("Floating Point Constraint");
    }

    protected void testValidationFloatingPointConstraints() throws Exception {
       //Scenario-1
       waitAndTypeByName("inputField1","-9.3");
       waitAndClickByLinkText("Usage");
       assertElementPresentByXpath("//input[@name='inputField1' and @class='uif-textControl validChar-inputField10 dirty error']");
    }
    
    protected void testValidationFloatingPointConstraintsAdditional() throws Exception {
        waitAndClickByLinkText("Flags");
        
        //Scenario-1
        waitAndTypeByName("inputField2","-0.92F");
        waitAndClickByLinkText("Usage");
        assertElementPresentByXpath("//input[@name='inputField2' and @class='uif-textControl validChar-inputField20 dirty error']");
    }
    
    @Test
    public void testValidationFloatingPointConstraintsBookmark() throws Exception {
        testValidationFloatingPointConstraints();
        testValidationFloatingPointConstraintsAdditional();
        passed();
    }

    @Test
    public void testValidationFloatingPointConstraintsNav() throws Exception {
        testValidationFloatingPointConstraints();
        testValidationFloatingPointConstraintsAdditional();
        passed();
    }
}