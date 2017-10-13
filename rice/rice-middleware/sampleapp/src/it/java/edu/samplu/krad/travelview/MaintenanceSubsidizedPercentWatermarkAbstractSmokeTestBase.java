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
package edu.samplu.krad.travelview;

import edu.samplu.common.Failable;
import edu.samplu.common.ITUtil;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class MaintenanceSubsidizedPercentWatermarkAbstractSmokeTestBase extends KradTmplMthdSTNavBase{

    /**
     * ITUtil.PORTAL + "?channelTitle=Travel%20Account%20Maintenance%20(New)&channelUrl=" + ITUtil.getBaseUrlString() +
     * "/kr-krad/maintenance?methodToCall=start&dataObjectClassName=edu.sampleu.travel.bo.TravelAccount&viewName=ks_maintenance&returnLocation=" +
     * ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK
     */
    public static final String BOOKMARK_URL = ITUtil.PORTAL + "?channelTitle=Travel%20Account%20Maintenance%20(New)&channelUrl=" + ITUtil.getBaseUrlString() +
            "/kr-krad/maintenance?methodToCall=start&dataObjectClassName=edu.sampleu.travel.bo.TravelAccount&viewName=ks_maintenance&returnLocation=" +
            ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK;
    /**
     * {@inheritDoc}
     * Travel Account Maintenance (New)
     * @return
     */
    @Override
    protected String getLinkLocator() {
        return "Travel Account Maintenance (New)";
    }

    public void testMaintenanceSubsidizedPercentWatermarkBookmark(Failable failable) throws Exception {
        testVerifySubsidizedPercentWatermarkLegacy();
        passed();
    }
    
    public void testMaintenanceSubsidizedPercentWatermarkNav(Failable failable) throws Exception {
        testVerifySubsidizedPercentWatermarkLegacy();
        passed();
    }
}
