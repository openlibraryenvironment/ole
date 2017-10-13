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
public abstract class ClearIndividualCashesAbstractSmokeTestBase extends AdminTmplMthdSTNavBase{

    /**
     * ITUtil.PORTAL+"?channelTitle=Cache%20Admin&channelUrl="+ITUtil.getBaseUrlString()+
     * "/kr-krad/core/admin/cache?viewId=CacheAdmin-view1&methodToCall=start"+    
     */
    public static final String BOOKMARK_URL = ITUtil.PORTAL+"?channelTitle=Cache%20Admin&channelUrl="+ITUtil.getBaseUrlString()+
            "/kr-krad/core/admin/cache?viewId=CacheAdmin-view1&methodToCall=start";
         
    /**
     * {@inheritDoc}
     * Cache Admin
     * @return
     */
    @Override
    protected String getLinkLocator() {
        return "Cache Admin";
    }

    public void testClearIndividualCashesBookmark(Failable failable) throws Exception {
        testClearIndividualCashes();
        passed();
    }

    public void testClearIndividualCashesNav(Failable failable) throws Exception {
        testClearIndividualCashes();
        passed();
    }    
    
    public void testClearIndividualCashes() throws Exception
    {
        selectFrameIframePortlet();
        waitAndClickByXpath("//li[@id='u27_node_3_parent_root']/ins");
        waitAndClickByXpath("//li[@id='u27_node_11_parent_node_3_parent_root']/a/ins");
        waitAndClickByXpath("//button[@id='u50']");
        Thread.sleep(10000);
        assertTextPresent("Cache: http://rice.kuali.org/krms/v2_0/TermSpecificationType was flushed for the CacheManager: krmsDistributedCacheManager.");
        assertTextPresent(" Cache Management ");
    }
}
