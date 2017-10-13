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
public abstract class ReviewAllServicesAndWSDLAbstractSmokeTestBase extends AdminTmplMthdSTNavBase{

    /**
     * ITUtil.PORTAL+"?channelTitle=Service%20Registry&channelUrl="+ITUtil.getBaseUrlString()+
     *  "/ksb/ServiceRegistry.do";
     */
    public static final String BOOKMARK_URL = ITUtil.PORTAL+"?channelTitle=Service%20Registry&channelUrl="+ITUtil.getBaseUrlString()+
            "/ksb/ServiceRegistry.do";
    
    /**
     * {@inheritDoc}
     * Service Registry
     * @return
     */
    @Override
    protected String getLinkLocator() {
        return "Service Registry";
    }

    public void testReviewAllServicesAndWSDLBookmark(Failable failable) throws Exception {
        testReviewAllServicesAndWSDL();
        passed();
    }

    public void testReviewAllServicesAndWSDLNav(Failable failable) throws Exception {
        testReviewAllServicesAndWSDL();
        passed();
    }    
    
    public void testReviewAllServicesAndWSDL() throws Exception
    {
        Thread.sleep(10000);
        selectFrameIframePortlet();
        assertTextPresent("{http://rice.kuali.org/core/v2_0}componentService");
        waitAndClickByXpath("//input[@value='Refresh Service Registry']");
        Thread.sleep(10000);
        assertTextPresent("{http://rice.kuali.org/core/v2_0}componentService");
        waitAndClickByXpath("//a[contains(text(),'/remoting/soap/core/v2_0/componentService')]");
    }
}
