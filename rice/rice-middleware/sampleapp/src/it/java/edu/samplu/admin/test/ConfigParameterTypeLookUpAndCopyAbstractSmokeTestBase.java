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
public abstract class ConfigParameterTypeLookUpAndCopyAbstractSmokeTestBase extends AdminTmplMthdSTNavBase{

    /**
     * ITUtil.PORTAL+"?channelTitle=Parameter%20Type&channelUrl="+ITUtil.getBaseUrlString()+
     * "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.coreservice.impl.parameter.ParameterTypeBo&docFormKey=88888888&returnLocation="
     * +ITUtil.PORTAL_URL+ ITUtil.HIDE_RETURN_LINK;
     */
    public static final String BOOKMARK_URL = ITUtil.PORTAL+"?channelTitle=Parameter%20Type&channelUrl="+ITUtil.getBaseUrlString()+
            "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.coreservice.impl.parameter.ParameterTypeBo&docFormKey=88888888&returnLocation="+
            ITUtil.PORTAL_URL+ ITUtil.HIDE_RETURN_LINK;

    /**
     * {@inheritDoc}
     * Parameter Type
     * @return
     */
    @Override
    protected String getLinkLocator() {
        return "Parameter Type";
    }

    public void testConfigParameterTypeLookUpAndCopyBookmark(Failable failable) throws Exception {
        testConfigParameterTypeLookUpAndCopy();
        passed();
    }

    public void testConfigParameterTypeLookUpAndCopyNav(Failable failable) throws Exception {
        testConfigParameterTypeLookUpAndCopy();
        passed();
    }    
    
    public void testConfigParameterTypeLookUpAndCopy() throws Exception
    {
        selectFrameIframePortlet();
        waitAndClickByXpath("(//input[@name='methodToCall.search'])[2]");
        waitAndClickByLinkText("copy");
        waitAndTypeByName("document.documentHeader.documentDescription","Test description of parameter type copy");
        waitAndTypeByName("document.newMaintainableObject.code","AUTH2");
        waitAndTypeByName("document.newMaintainableObject.name","AuthorizationCopy");
        waitAndClickByName("methodToCall.route");
        waitAndClickByName("methodToCall.close");
        waitAndClickByName("methodToCall.processAnswer.button1");        
    }
}
