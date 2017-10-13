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
public class DemoLibraryValidationRegexBasedConstraintsSmokeTest extends SmokeTestBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-ConfigurationBasedRegexPatternConstraint-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-ConfigurationBasedRegexPatternConstraint-View&methodToCall=start";

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-LibraryLink", "");
        waitAndClickByLinkText("Validation");
        waitAndClickByLinkText("Regex Based Constraints");
    }

    protected void testValidationRegexBasedConstraintsPhone() throws Exception {
       //Scenario-1
       waitAndTypeByName("inputField1","1234567890");
       waitAndClickByLinkText("Usage");
       fireEvent("inputField1", "focus");
       Thread.sleep(200);
       assertElementPresentByXpath("//input[@name='inputField1' and @class='uif-textControl validChar-inputField10 dirty error']");
    }
    
    protected void testValidationRegexBasedConstraintsEmail() throws Exception {
        waitAndClickByLinkText("Email");
        
        //Scenario-1
        waitAndTypeByName("inputField2","s1!@f.xoh");
        waitAndClickByLinkText("Usage");
        assertElementPresentByXpath("//input[@name='inputField2' and @class='uif-textControl validChar-inputField20 dirty error']");
    }
    
    protected void testValidationRegexBasedConstraintsUrl() throws Exception {
        waitAndClickByLinkText("Url");
        
        //Scenario-1
        waitAndTypeByName("inputField3","www.google.com");
        waitAndClickByLinkText("Usage");
        assertElementPresentByXpath("//input[@name='inputField3' and @class='uif-textControl validChar-inputField30 dirty error']");
    }
    
    protected void testValidationRegexBasedConstraintsNowhitespace() throws Exception {
        waitAndClickByLinkText("No whitespace");
        
        //Scenario-1
        waitAndTypeByName("inputField4","aw e");
        waitAndClickByLinkText("Usage");
        assertElementPresentByXpath("//input[@name='inputField4' and @class='uif-textControl validChar-inputField40 dirty error']");
    }
    
    protected void testValidationRegexBasedConstraints12hTime() throws Exception {
        waitAndClickByLinkText("12h Time");
        
        //Scenario-1
        waitAndTypeByName("inputField5","22:00");
        waitAndClickByLinkText("Usage");
        
        //Currently this field is not working
		//assertElementPresentByXpath("//input[@name='inputField5' and @class='uif-textControl validChar-inputField50 dirty error']");
    }
    
    protected void testValidationRegexBasedConstraints24hTime() throws Exception {
        waitAndClickByLinkText("24h Time");
        
        //Scenario-1
        waitAndTypeByName("inputField6","01:00AM");
        waitAndClickByLinkText("Usage");
        
        //Currently this field is not working
		//assertElementPresentByXpath("//input[@name='inputField6' and @class='uif-textControl validChar-inputField60 dirty error']");
    }
    
    protected void testValidationRegexBasedConstraintsTimestamp() throws Exception {
        waitAndClickByLinkText("Timestamp");
        
        //Scenario-1
        waitAndTypeByName("inputField7","1234-12-30 23:23:23.23");
        waitAndClickByLinkText("Usage");
        assertElementPresentByXpath("//input[@name='inputField7' and @class='uif-textControl validChar-inputField70 dirty error']");
    }
    
     protected void testValidationRegexBasedConstraintsYear() throws Exception {
        waitAndClickByLinkText("Year");
        
        //Scenario-1
        waitAndTypeByName("inputField8","1599");
        waitAndClickByLinkText("Usage");
        assertElementPresentByXpath("//input[@name='inputField8' and @class='uif-textControl validChar-inputField80 dirty error']");
    }
    
     protected void testValidationRegexBasedConstraintsMonth() throws Exception {
        waitAndClickByLinkText("Month");
        
        //Scenario-1
        waitAndTypeByName("inputField9","13");
        waitAndClickByLinkText("Usage");
        assertElementPresentByXpath("//input[@name='inputField9' and @class='uif-textControl validChar-inputField90 dirty error']");
    }
    
     protected void testValidationRegexBasedConstraintsZipcode() throws Exception {
        waitAndClickByLinkText("Zipcode");
        
        //Scenario-1
        waitAndTypeByName("inputField10","94101");
        waitAndClickByLinkText("Usage");
        
        //Currently not working
        //assertElementPresentByXpath("//input[@name='inputField10' and @class='uif-textControl validChar-inputField100 dirty error']");
    }
    
    protected void testValidationRegexBasedConstraintsJavaclassname() throws Exception {
        waitAndClickByLinkText("Java classname");
        
        //Scenario-1
        waitAndTypeByName("inputField11","asdf");
        waitAndClickByLinkText("Usage");
        
        //Currently not working
        //assertElementPresentByXpath("//input[@name='inputField11' and @class='uif-textControl validChar-inputField110 dirty error']");
    }
    
     protected void testValidationRegexBasedConstraintsCustom() throws Exception {
        waitAndClickByLinkText("Custom");
        
        //Scenario-1
        waitAndTypeByName("inputField12","ab.9");
        waitAndTypeByName("inputField13","Demo-hi hello");
        waitAndClickByLinkText("Usage");
        assertElementPresentByXpath("//input[@name='inputField12' and @class='uif-textControl validChar-inputField120 dirty error']");
        assertElementPresentByXpath("//input[@name='inputField13' and @class='uif-textControl validChar-inputField130 dirty error']");
    }
    
    @Test
    public void testValidationRegexBasedConstraintsBookmark() throws Exception {
        testValidationRegexBasedConstraintsPhone();
        testValidationRegexBasedConstraintsEmail();
        testValidationRegexBasedConstraintsUrl();
        testValidationRegexBasedConstraintsNowhitespace();
        testValidationRegexBasedConstraints12hTime();
        testValidationRegexBasedConstraints24hTime();
        testValidationRegexBasedConstraintsTimestamp();
        testValidationRegexBasedConstraintsYear();
        testValidationRegexBasedConstraintsMonth();
        testValidationRegexBasedConstraintsZipcode();
        testValidationRegexBasedConstraintsJavaclassname();
        testValidationRegexBasedConstraintsCustom();
        passed();
    }

    @Test
    public void testValidationRegexBasedConstraintsNav() throws Exception {
        testValidationRegexBasedConstraintsPhone();
        testValidationRegexBasedConstraintsEmail();
        testValidationRegexBasedConstraintsUrl();
        testValidationRegexBasedConstraintsNowhitespace();
        testValidationRegexBasedConstraints12hTime();
        testValidationRegexBasedConstraints24hTime();
        testValidationRegexBasedConstraintsTimestamp();
        testValidationRegexBasedConstraintsYear();
        testValidationRegexBasedConstraintsMonth();
        testValidationRegexBasedConstraintsZipcode();
        testValidationRegexBasedConstraintsJavaclassname();
        testValidationRegexBasedConstraintsCustom();
        passed();
    }

    @Test
    public void testValidationRegexBasedConstraintsEmailNav() throws Exception {
        testValidationRegexBasedConstraintsEmail();
        passed();
    }
}