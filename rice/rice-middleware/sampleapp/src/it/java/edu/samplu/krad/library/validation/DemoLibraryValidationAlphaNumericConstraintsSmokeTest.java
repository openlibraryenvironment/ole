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

import edu.samplu.common.SmokeTestBase;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLibraryValidationAlphaNumericConstraintsSmokeTest extends SmokeTestBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-AlphaNumericPatternConstraint-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-AlphaNumericPatternConstraint-View&methodToCall=start";

    /**
     *  Can only be alpha characters, whitespace, newlines, periods, parentheses, forward slashes, double quotes, apostrophes, colons, semi-colons, question marks, exclaimation marks, dashes
     */
    private static final String ERROR_MSG= "  Can only be alphanumeric characters, whitespace, newlines, periods, parentheses, forward slashes, double quotes, apostrophes, colons, semi-colons, question marks, exclaimation marks, dashes ";
    
    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-LibraryLink", "");
        waitAndClickByLinkText("Validation");
        waitAndClickByLinkText("AlphaNumeric Constraint");
    }

    protected void testValidationAlphaNumericConstraints() throws Exception {
       //Scenario-1
       waitAndTypeByName("inputField1","_as");
       waitAndClickByLinkText("Usage");
       fireMouseOverEventByName("inputField1");
       isVisible(By.xpath("//div[@class='uif-clientMessageItems uif-clientErrorDiv']"));
    }
    
    protected void testValidationAlphaNumericConstraintsFlags() throws Exception {
        waitAndClickByLinkText("Flags");
        
        //Scenario-1
        waitAndTypeByName("inputField2","as 1_4");
        waitAndTypeByName("inputField3","a_s");
        isVisible(By.xpath("//li[@class='uif-errorMessageItem-field']"));
        waitAndTypeByName("inputField2","");
        isNotVisible(By.xpath("//li[@class='uif-errorMessageItem-field']"));
    }
    
    protected void testValidationAlphaNumericConstraintsPreconfiguredBeans() throws Exception {
        waitAndClickByLinkText("Preconfigured Bean(s)");
        
        //Scenario-1
        waitAndTypeByName("inputField4","as#");
        waitAndClickByLinkText("Usage");
        fireMouseOverEventByName("inputField4");
        isVisible(By.xpath("//li[@class='uif-errorMessageItem-field']"));
     }
    
    @Test
    public void testValidationAlphaNumericConstraintsBookmark() throws Exception {
        testValidationAlphaNumericConstraints();
        testValidationAlphaNumericConstraintsFlags();
        testValidationAlphaNumericConstraintsPreconfiguredBeans();
        passed();
    }

    @Test
    public void testValidationAlphaNumericConstraintsNav() throws Exception {
        testValidationAlphaNumericConstraints();
        testValidationAlphaNumericConstraintsFlags();
        testValidationAlphaNumericConstraintsPreconfiguredBeans();
        passed();
    }
}