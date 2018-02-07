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
package edu.samplu.mainmenu.test;

import edu.samplu.common.Failable;
import edu.samplu.common.ITUtil;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class WorkFlowRouteRulesBlanketAppAbstractSmokeTestBase extends MainTmplMthdSTNavBase{

    /**
     * ITUtil.PORTAL + "?channelTitle=Routing%20Rules&channelUrl=" + ITUtil.getBaseUrlString() +
     * "/kr/lookup.do?businessObjectClassName=org.kuali.rice.kew.rule.RuleBaseValues&docFormKey=88888888&returnLocation=" +
     * ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK+ "&showMaintenanceLinks=true";
     */
    public static final String BOOKMARK_URL = ITUtil.PORTAL + "?channelTitle=Routing%20Rules&channelUrl=" + ITUtil.getBaseUrlString() +
            "/kr/lookup.do?businessObjectClassName=org.kuali.rice.kew.rule.RuleBaseValues&docFormKey=88888888&returnLocation=" +
            ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK+ "&showMaintenanceLinks=true";
    /**
     * {@inheritDoc}
     * Routing Rules
     * @return
     */
    @Override
    protected String getLinkLocator() {
        return "Routing Rules";
    }

    public void testWorkFlowRouteRulesBlanketAppBookmark(Failable failable) throws Exception {
        testWorkFlowRouteRulesBlanketApp();
        passed();
    }
    public void testWorkFlowRouteRulesBlanketAppNav(Failable failable) throws Exception {
        testWorkFlowRouteRulesBlanketApp();
        passed();
    }
}
