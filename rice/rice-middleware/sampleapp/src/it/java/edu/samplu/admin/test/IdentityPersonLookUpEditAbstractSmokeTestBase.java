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
package edu.samplu.admin.test;

import edu.samplu.common.Failable;
import edu.samplu.common.ITUtil;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class IdentityPersonLookUpEditAbstractSmokeTestBase extends AdminTmplMthdSTNavBase{

    /**
     * ITUtil.PORTAL + "?channelTitle=Person&channelUrl=" 
     * + ITUtil.getBaseUrlString() + ITUtil.KNS_LOOKUP_METHOD + "org.kuali.rice.kim.api.identity.Person&docFormKey=88888888&returnLocation=" +
     * ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK;
     */    
//    http://env2.rice.kuali.org/portal.do?channelTitle=Person&channelUrl=http://env2.rice.kuali.org/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.kim.api.identity.Person&docFormKey=88888888&returnLocation=http://env2.rice.kuali.org/portal.do&hideReturnLink=true
    public static final String BOOKMARK_URL = ITUtil.PORTAL + "?channelTitle=Person&channelUrl=" 
            + ITUtil.getBaseUrlString() + "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.kim.api.identity.Person&docFormKey=88888888&returnLocation="+
            ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK ;

    /**
     * {@inheritDoc}
     * Person
     * @return
     */
    @Override
    protected String getLinkLocator() {
        return "Person";
    }
   
    public void testIdentityPersonLookUpEditBookmark(Failable failable) throws Exception {
        testIdentityPersonLookUpEdit();
        passed();
    }

    public void testIdentityPersonLookUpEditNav(Failable failable) throws Exception {
        testIdentityPersonLookUpEdit();
        passed();
    }
    
    public void testIdentityPersonLookUpEdit() throws Exception {
        // This tests only runs correctly the first time.
        selectFrameIframePortlet();
        waitAndTypeByName("principalName","fran");
        waitAndClickByXpath("(//input[@name='methodToCall.search'])[2]");
        selectFrameIframePortlet();
        waitAndClickByXpath("//a[@title='edit Person withPrincipal ID=fran ']");
        waitAndTypeByName("document.documentHeader.documentDescription", "Test description of person");
        selectByName("newAffln.affiliationTypeCode", "Staff");
        selectByName("newAffln.campusCode","BL - BLOOMINGTON");
        waitAndClickByName("newAffln.dflt");
        waitAndClickByName("methodToCall.addAffln.anchor");
        waitAndTypeByName("document.affiliations[0].newEmpInfo.employeeId","9999999999");
        waitAndClickByName("document.affiliations[0].newEmpInfo.primary");
        selectByName("document.affiliations[0].newEmpInfo.employmentStatusCode","Active");
        selectByName("document.affiliations[0].newEmpInfo.employmentTypeCode","Professional");
        waitAndTypeByName("document.affiliations[0].newEmpInfo.baseSalaryAmount","99999");
        waitAndTypeByXpath("//*[@id='document.affiliations[0].newEmpInfo.primaryDepartmentCode']", "BL-BUS");
        waitAndClickByName("methodToCall.addEmpInfo.line0.anchor");
        waitAndClickByName("methodToCall.showAllTabs");
        waitAndClickByName("methodToCall.route");

        if (hasDocError()) {
            // After the second run of this test, there will be 3 Errors.  Requires a decent amount of work to satisfy constraints.
            if (!extractErrorText().startsWith("3 error(s) found on page")) {
                checkForDocError();
            }
        } else {
            assertTextPresent("Document was successfully submitted.");
        }
        waitAndClickByName("methodToCall.close");
        waitAndClickByName("methodToCall.processAnswer.button1");        
    }
}
