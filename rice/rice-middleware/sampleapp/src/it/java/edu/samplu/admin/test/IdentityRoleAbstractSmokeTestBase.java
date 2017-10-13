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
public abstract class IdentityRoleAbstractSmokeTestBase extends AdminTmplMthdSTNavBase{

    /**
     * ITUtil.PORTAL + "?channelTitle=Role&channelUrl=" 
     * + ITUtil.getBaseUrlString() + ITUtil.KNS_LOOKUP_METHOD + "org.kuali.rice.kim.impl.role.RoleBo&docFormKey=88888888&returnLocation=" +
     * ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK;
     */
    public static final String BOOKMARK_URL = ITUtil.PORTAL + "?channelTitle=Role&channelUrl=" 
            + ITUtil.getBaseUrlString() + ITUtil.KNS_LOOKUP_METHOD +
            "org.kuali.rice.kim.impl.role.RoleBo&docFormKey=88888888&returnLocation=" +
            ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK_FALSE + ITUtil.SHOW_MAINTENANCE_LINKS;

    /**
     * {@inheritDoc}
     * Role
     * @return
     */
    @Override
    protected String getLinkLocator() {
        return "Role";
    }
   
    public void testIdentityRoleBookmark(Failable failable) throws Exception {
        testCreateNewSearchReturnValueCancelConfirmation();
        driver.navigate().to(ITUtil.getBaseUrlString() + BOOKMARK_URL);
        testSearchEditCancel();
        passed();
    }

    public void testIdentityRoleNav(Failable failable) throws Exception {
        testEditCancel();
        navigate();
        testCreateNewSearchReturnValueCancelConfirmation();
        passed();
    }
}
