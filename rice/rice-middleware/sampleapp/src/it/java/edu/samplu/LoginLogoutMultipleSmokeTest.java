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

import static org.junit.Assert.*;

import edu.samplu.common.SmokeTestBase;
import org.junit.Assert;
import org.junit.Test;

import edu.samplu.common.ITUtil;

public class LoginLogoutMultipleSmokeTest extends SmokeTestBase {

    /**
     * "//div[@id='login-info']/strong[2]"
     */
    public static final String LOGIN_INFO_STRONG_2_XPATH = "//div[@id='login-info']/strong[2]";

    @Override
    public void fail(String message) {
        Assert.fail(message);
    }

    @Override
    protected String getBookmarkUrl() {
        return ITUtil.PORTAL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickMainMenu(this);
        waitForPageToLoad();
    }

    @Override
    public String getTestUrl() {
        return ITUtil.PORTAL;
    }

    @Override
    public String getUserName() {
        return "admin";
    }

    @Test
    public void testMultipleLoginLogoutNav() throws Exception {
        testMultipleLoginLogout();
        passed();
    }

    public void testMultipleLoginLogout() throws Exception {
        assertEquals("Logged in User: admin",getTextByXpath("//div[@id='login-info']/strong[1]"));
        assertEquals(Boolean.FALSE, isElementPresentByXpath(LOGIN_INFO_STRONG_2_XPATH));
        waitAndTypeByName("backdoorId", "employee");
        waitAndClickByXpath("//input[@value='Login']");
        waitForPageToLoad();
        assertElementPresentByXpath(LOGIN_INFO_STRONG_2_XPATH);
        assertEquals("  Impersonating User: employee",getTextByXpath(LOGIN_INFO_STRONG_2_XPATH));
        waitAndClickLogout();
        assertEquals(Boolean.FALSE, isElementPresentByXpath(LOGIN_INFO_STRONG_2_XPATH));
        waitAndClickLogout();
        passed();
    }
}
