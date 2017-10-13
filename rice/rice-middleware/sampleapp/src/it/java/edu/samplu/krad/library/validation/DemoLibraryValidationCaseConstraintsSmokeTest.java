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
import org.openqa.selenium.By;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLibraryValidationCaseConstraintsSmokeTest extends SmokeTestBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-CaseConstraint-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-CaseConstraint-View&methodToCall=start";

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-LibraryLink", "");
        waitAndClickByLinkText("Validation");
        waitAndClickByLinkText("Case Constraint");
    }

    protected void testValidationCaseConstraintsBasic() throws Exception {
       //Scenario-1
       waitAndClickByXpath("//input[@type='radio' and @value='case1']");
       waitAndTypeByName("inputField1","");
       waitAndTypeByName("inputField2","");
       isVisible(By.xpath("//li[@class='uif-errorMessageItem-field']"));
      
       //Scenario-2
       waitAndClickByXpath("//input[@type='radio' and @value='case2']");
       waitAndTypeByName("inputField1","a_+");
       waitAndTypeByName("inputField2","");
       fireMouseOverEventByName("inputField1");
       isVisible(By.xpath("//li[@class='uif-errorMessageItem-field']"));
       
       //Scenario-3
       waitAndClickByXpath("//input[@type='radio' and @value='case3']");
       waitAndTypeByName("inputField2","567823");
       waitAndTypeByName("inputField1","");
       fireMouseOverEventByName("inputField2");
        isVisible(By.xpath("//li[@class='uif-errorMessageItem-field']"));
       
       //Scenario-4
       waitAndClickByXpath("//input[@type='radio' and @value='case4']");
       waitAndTypeByName("inputField3","a");
       waitAndTypeByName("inputField4","");
       waitAndTypeByName("inputField3","");
       fireMouseOverEventByName("inputField4");
       isVisible(By.xpath("//li[@class='uif-errorMessageItem-field']"));
    }
    
    protected void testValidationCaseConstraintsNested() throws Exception {
       waitAndClickByLinkText("Nested Example");
       
       //Scenario-1
       waitAndTypeByName("inputField5","a");
       waitAndTypeByName("inputField7","");
       waitAndTypeByName("inputField6","");
       assertElementPresentByXpath("//input[@name='inputField7' and @class='uif-textControl dependsOn-inputField5 dependsOn-inputField6 error']");
    }
    
    @Test
    public void testValidationCaseConstraintsBookmark() throws Exception {
        testValidationCaseConstraintsBasic();
        testValidationCaseConstraintsNested();
        passed();
    }

    @Test
    public void testValidationCaseConstraintsNav() throws Exception {
        testValidationCaseConstraintsBasic();
        testValidationCaseConstraintsNested();
        passed();
    }
}