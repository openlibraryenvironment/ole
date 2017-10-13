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
package edu.samplu;

import edu.samplu.common.ITUtil;
import edu.samplu.common.SmokeTestBase;
import org.junit.Test;

/**
 * Abstract base class for LoginLogout Smoke Tests.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LoginLogoutSmokeTest extends SmokeTestBase {

    @Override
    protected String getBookmarkUrl() {
        return ITUtil.PORTAL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickMainMenu(this);
        waitForPageToLoad();
    }

    /**
     * Navigate to the page under test click logout.  {@link LoginLogoutSmokeTest#waitAndClickLogout(edu.samplu.common.Failable)}
     *
     * @throws {@link Exception}
     */
    @Test
    public void testLogoutNav() throws Exception {
        testLogout();
        passed();
    }

    private void testLogout() throws InterruptedException {
        jGrowl("Logging Out");
        waitAndClickLogout();
        assertIsVisibleById("Rice-LoginButton");
    }
}
