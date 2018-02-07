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
package edu.samplu.travel.test;

import edu.samplu.common.Failable;
import edu.samplu.common.ITUtil;
import edu.samplu.common.WebDriverLegacyITBase;

/**
 * Tests the Component section in Rice.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class FiscalOfficerInfoMaintenanceAbstractSmokeTestBase extends WebDriverLegacyITBase {

    /**
     * ITUtil.PORTAL
     * + "?channelTitle=FiscalOfficerInfo%20Maintenance%20(New)&channelUrl="
     * + ITUtil.getBaseUrlString()
     * + "/kr-krad/maintenance?methodToCall=start&dataObjectClassName=edu.sampleu.travel.dto.FiscalOfficerInfo&returnLocation="
     * + ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK
     */
    public static final String BOOKMARK_URL = ITUtil.PORTAL
            + "?channelTitle=FiscalOfficerInfo%20Maintenance%20(New)&channelUrl="
            + ITUtil.getBaseUrlString()
            + "/kr-krad/maintenance?methodToCall=start&dataObjectClassName=edu.sampleu.travel.dto.FiscalOfficerInfo&returnLocation="
            + ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK;

    protected void bookmark() {
        open(ITUtil.getBaseUrlString() + BOOKMARK_URL);
    }

    /**
     * Nav tests start at {@link edu.samplu.common.ITUtil#PORTAL}.
     * Bookmark Tests should override and return {@link edu.samplu.travel.krad.test.CollectionTotallingAbstractSmokeTestBase#BOOKMARK_URL}
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getTestUrl() {
        return ITUtil.PORTAL;
    }

    protected void navigation() throws InterruptedException {
        waitAndClickKRAD();
        waitAndClickByXpath(FISCAL_OFFICER_INFO_MAINTENANCE_NEW_XPATH);
    }

    protected void testFiscalOfficerInfoMaintenanceNav(Failable failable) throws Exception {
        navigation();
        testFiscalOfficerInfoMaintenanceNew(); 
        passed();
    }

    protected void testFiscalOfficerInfoMaintenanceBookmark(Failable failable) throws Exception {
        testFiscalOfficerInfoMaintenanceNew();
        passed();
    }    
}
