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
import org.apache.commons.lang.RandomStringUtils;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class IdentityPersonCreateNewAbstractSmokeTestBase extends AdminTmplMthdSTNavBase{

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
   
    public void testIdentityPersonCreateNewBookmark(Failable failable) throws Exception {
        testIdentityPersonCreateNew();
        passed();
    }

    public void testIdentityPersonCreateNewNav(Failable failable) throws Exception {
        testIdentityPersonCreateNew();
        passed();
    }
    
    public void testIdentityPersonCreateNew() throws Exception {
        selectFrameIframePortlet();
        waitAndClickByXpath(CREATE_NEW_XPATH);
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
        selectByName("newName.nameCode", "Primary");
        waitAndTypeByName("newName.firstName","Marco");
        waitAndTypeByName("newName.lastName","Simoncelli");
        waitAndClickByName("newName.dflt");
        waitAndClickByName("methodToCall.addName.anchor");
        selectByName("newAddress.addressTypeCode", "Work");
        waitAndTypeByName("newAddress.line1","123 Address Ln");
        waitAndTypeByName("newAddress.city","Bloomington");
        selectByName("newAddress.stateProvinceCode", "INDIANA");
        waitAndTypeByName("newAddress.postalCode","47408");
        selectByName("newAddress.countryCode","United States");
        waitAndClickByName("newAddress.dflt");
        waitAndClickByName("methodToCall.addAddress.anchor");
        selectByName("newPhone.phoneTypeCode","Work");
        waitAndTypeByName("newPhone.phoneNumber", "855-555-5555");
        selectByName("newPhone.countryCode","United States");
        waitAndClickByName("newPhone.dflt");
        waitAndClickByName("methodToCall.addPhone.anchor");
        waitAndTypeByName("newEmail.emailAddress","email@provider.net");
        selectByName("newEmail.emailTypeCode","Work");
        waitAndClickByName("newEmail.dflt");
        waitAndClickByName("methodToCall.addEmail.anchor");
        waitAndTypeByName("document.principalName", RandomStringUtils.randomAlphabetic(12).toLowerCase());
                
        //Expand All , Submit , Close and Don't Save.        
        waitAndClickByName("methodToCall.route");
        checkForDocError();
//        assertTextPresent("Document was successfully submitted.");
        waitAndClickByName("methodToCall.close");
        waitAndClickByName("methodToCall.processAnswer.button1");        
    }
}
