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
public abstract class ConfigParameterLookUpAndCopyAbstractSmokeTestBase extends AdminTmplMthdSTNavBase{

    /**
     * ITUtil.PORTAL+"?channelTitle=Parameter&channelUrl="+ITUtil.getBaseUrlString()+
     * "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.coreservice.impl.parameter.ParameterBo&docFormKey=88888888&returnLocation="
     * +ITUtil.PORTAL_URL+ ITUtil.HIDE_RETURN_LINK;
     */
    public static final String BOOKMARK_URL = ITUtil.PORTAL+"?channelTitle=Parameter&channelUrl="+ITUtil.getBaseUrlString()+
            "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.coreservice.impl.parameter.ParameterBo&docFormKey=88888888&returnLocation="
            +ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK;

    /**
     * {@inheritDoc}
     * Parameter
     * @return
     */
    @Override
    protected String getLinkLocator() {
        return "Parameter";
    }

    public void testConfigParameterLookUpAndCopyBookmark(Failable failable) throws Exception {
        testConfigParameterLookUpAndCopy();
        passed();
    }

    public void testConfigParameterLookUpAndCopyNav(Failable failable) throws Exception {
        testConfigParameterLookUpAndCopy();
        passed();
    }    
    
    public void testConfigParameterLookUpAndCopy() throws Exception
    {
        selectFrameIframePortlet();
        waitAndTypeByName("name","*email*");
        waitAndClickByXpath("(//input[@name='methodToCall.search'])[2]");
        waitAndClickByXpath("//a[@title='copy Parameter withParameter Name=EMAIL_NOTIFICATION_TEST_ADDRESS Application ID=KUALI Namespace Code=KR-WKFLW Component=ActionList ']");
        waitAndTypeByName("document.documentHeader.documentDescription","Test description of parameter copy " + ITUtil.createUniqueDtsPlusTwoRandomCharsNot9Digits());
        selectByName("document.newMaintainableObject.namespaceCode","KR-WKFLW - Workflow");
        waitAndTypeByName("document.newMaintainableObject.componentCode","ActionList");
        waitAndTypeByName("document.newMaintainableObject.applicationId","KUALI");
        waitAndTypeByName("document.newMaintainableObject.name","EMAIL_NOTIFICATION_TEST_ADDRESS_COPY_" + ITUtil.createUniqueDtsPlusTwoRandomChars());
        waitAndClickByName("methodToCall.route");
        checkForDocError();
        waitAndClickByName("methodToCall.close");
        waitAndClickByName("methodToCall.processAnswer.button1");        
    }
}
