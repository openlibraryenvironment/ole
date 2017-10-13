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
import edu.samplu.common.WebDriverUtil;
import org.junit.Test;

import static com.thoughtworks.selenium.SeleneseTestBase.assertTrue;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LoginInvalidUserSmokeTest extends SmokeTestBase {

    @Override
    protected String getBookmarkUrl() {
        return ITUtil.PORTAL;
    }

    @Override
    protected void navigate() throws Exception {
        // no-op should not need to navigate for invalid login test
    }

    @Override
    public void testSetUp()  {
        System.setProperty(ITUtil.REMOTE_AUTOLOGIN_PROPERTY, "false"); // turn off auto login so we can test invalid login
        super.testSetUp();
    }

    /**
     * Invalid user name test
     * @throws InterruptedException
     */
    @Test
    public void testInvalidUserName() throws InterruptedException {
        try {
            System.setProperty(ITUtil.REMOTE_AUTOLOGIN_PROPERTY, "true");
            WebDriverUtil.loginKradOrKns(driver, ITUtil.DTS_TWO, this);
            fail("Expected Invalid Login exception with user " + ITUtil.DTS_TWO);
        } catch (AssertionError e) {
            assertTrue(e.getMessage().contains("Invalid"));
            passed();
        }
    }
}
