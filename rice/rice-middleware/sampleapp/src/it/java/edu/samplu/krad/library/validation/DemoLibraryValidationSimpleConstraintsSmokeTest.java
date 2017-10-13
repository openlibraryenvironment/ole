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
public class DemoLibraryValidationSimpleConstraintsSmokeTest extends SmokeTestBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-SimpleConstraint-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-SimpleConstraint-View&methodToCall=start";
   
    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-LibraryLink", "");
        waitAndClickByLinkText("Validation");
        waitAndClickByLinkText("Simple Constraints");
    }

    protected void testValidationSimpleConstraintsRequired() throws Exception {
       waitAndTypeByName("inputField1","");
       waitAndTypeByName("inputField2","");
       waitAndTypeByName("inputField3","");
       Thread.sleep(1000);
       assertElementPresentByXpath("//input[@name='inputField1' and @class='uif-textControl required error']");
       assertElementPresentByXpath("//input[@name='inputField2' and @class='uif-textControl required error']");
       waitAndTypeByName("inputField1","");
       assertElementPresentByXpath("//input[@name='inputField3' and @class='uif-textControl required error']");
    }
    
    protected void testValidationSimpleConstraintsMinMaxLength() throws Exception {
        waitAndClickByLinkText("Min/Max Length");
        waitAndTypeByName("inputField4","deepmoteria");
        waitAndTypeByName("inputField5","de");
        waitAndTypeByName("inputField4","");
        Thread.sleep(1000);
        assertElementPresentByXpath("//input[@name='inputField5' and @class='uif-textControl dirty error']");
    }

    protected void testValidationSimpleConstraintsMinMaxValue() throws Exception {
        waitAndClickByLinkText("Min/Max Value");
        waitAndTypeByName("inputField6","21");
        waitAndTypeByName("inputField7","2");
        waitAndTypeByName("inputField6","");
        assertElementPresentByXpath("//input[@name='inputField6' and @class='uif-textControl dirty error']");
        assertElementPresentByXpath("//input[@name='inputField7' and @class='uif-textControl dirty error']");
    }
    
    @Test
    public void testValidationSimpleConstraintsBookmark() throws Exception {
        testValidationSimpleConstraintsRequired();
        testValidationSimpleConstraintsMinMaxLength();
        testValidationSimpleConstraintsMinMaxValue();
        passed();
    }

    @Test
    public void testValidationSimpleConstraintsNav() throws Exception {
        testValidationSimpleConstraintsRequired();
        testValidationSimpleConstraintsMinMaxLength();
        testValidationSimpleConstraintsMinMaxValue();
        passed();
    }
}