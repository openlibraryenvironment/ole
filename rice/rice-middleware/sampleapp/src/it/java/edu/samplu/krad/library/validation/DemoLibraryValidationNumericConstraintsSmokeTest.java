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
public class DemoLibraryValidationNumericConstraintsSmokeTest extends SmokeTestBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-NumericPatternConstraint-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-NumericPatternConstraint-View&methodToCall=start";
   
    /**
     *  Can only be alpha characters, whitespace, newlines, periods, parentheses, forward slashes, double quotes, apostrophes, colons, semi-colons, question marks, exclaimation marks, dashes
     */
    private static final String ERROR_MSG= "  Can only be numeric characters, whitespace, newlines, periods, parentheses, forward slashes, dashes, plus signs, equals signs, *";
    
    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-LibraryLink", "");
        waitAndClickByLinkText("Validation");
        waitAndClickByLinkText("Numeric Constraint");
    }

    protected void testValidationNumericConstraints() throws Exception {
       //Scenario-1
       waitAndTypeByName("inputField1","a");
       waitAndClickByLinkText("Usage");
       assertElementPresentByXpath("//input[@name='inputField1' and @class='uif-textControl validChar-inputField10 dirty error']");
    }
    
    protected void testValidationNumericConstraintsFlags() throws Exception {
        waitAndClickByLinkText("Flags");
        
        //Scenario-1
        waitAndTypeByName("inputField2","a s");
        waitAndTypeByName("inputField3","a#s");
        assertElementPresentByXpath("//input[@name='inputField2' and @class='uif-textControl validChar-inputField20 dirty error']");
        waitAndTypeByName("inputField2","");
        assertElementPresentByXpath("//input[@name='inputField3' and @class='uif-textControl validChar-inputField30 dirty error']");
    }
    
    protected void testValidationNumericConstraintsPreconfiguredBeans() throws Exception {
        waitAndClickByLinkText("Preconfigured Bean(s)");
        
        //Scenario-1
        waitAndTypeByName("inputField4","1@2");
        waitAndClickByLinkText("Usage");
        fireMouseOverEventByName("inputField4");
        assertTextPresent(ERROR_MSG);
     }
    
    @Test
    public void testValidationNumericConstraintsBookmark() throws Exception {
        testValidationNumericConstraints();
        testValidationNumericConstraintsFlags();
        testValidationNumericConstraintsPreconfiguredBeans();
        passed();
    }

    @Test
    public void testValidationNumericConstraintsNav() throws Exception {
        testValidationNumericConstraints();
        testValidationNumericConstraintsFlags();
        testValidationNumericConstraintsPreconfiguredBeans();
        passed();
    }  
}