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
public class DemoLibraryValidationPrerequisiteConstraintsSmokeTest extends SmokeTestBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-PrerequisiteConstraint-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-PrerequisiteConstraint-View&methodToCall=start";
   
    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-LibraryLink", "");
        waitAndClickByLinkText("Validation");
        waitAndClickByLinkText("Prerequisite Constraint");
    }

    protected void testValidationPrerequisiteConstraintsRequired() throws Exception {
       waitAndTypeByName("inputField1","a");
       waitAndTypeByName("inputField2","");
       waitAndTypeByName("inputField3","");
       waitAndTypeByName("inputField1","");
       assertElementPresentByXpath("//input[@name='inputField2' and @class='uif-textControl dependsOn-inputField1 required error']");
       assertElementPresentByXpath("//input[@name='inputField3' and @class='uif-textControl dependsOn-inputField1 prConstraint-inputField31 required error']");
       clearTextByName("inputField1");
       waitAndTypeByName("inputField3","a");
       waitAndTypeByName("inputField1","");
       assertElementPresentByXpath("//input[@name='inputField1' and @class='uif-textControl dependsOn-inputField2 dependsOn-inputField3 required error']");
    }
    
    @Test
    public void testValidationPrerequisiteConstraintsBookmark() throws Exception {
        testValidationPrerequisiteConstraintsRequired();
        passed();
    }

    @Test
    public void testValidationPrerequisiteConstraintsNav() throws Exception {
        testValidationPrerequisiteConstraintsRequired();
        passed();
    }
}