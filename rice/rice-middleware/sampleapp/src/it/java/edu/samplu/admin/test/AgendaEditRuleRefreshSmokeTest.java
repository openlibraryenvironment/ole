/**
 * Copyright 2005-2011 The Kuali Foundation
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

import edu.samplu.common.SmokeTestBase;
import org.junit.Test;

import edu.samplu.common.ITUtil;

/**
 * test that repeated ajax refreshes work
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AgendaEditRuleRefreshSmokeTest extends SmokeTestBase {

    public static final String BOOKMARK_URL =ITUtil.PORTAL+"?channelTitle=Agenda%20Lookup&channelUrl="
            + ITUtil.getBaseUrlString() + ITUtil.KRAD_LOOKUP_METHOD
            + "org.kuali.rice.krms.impl.repository.AgendaBo"
            + ITUtil.SHOW_MAINTENANCE_LINKS
            + "&returnLocation=" + ITUtil.PORTAL_URL + ITUtil.HIDE_RETURN_LINK;

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickByLinkText(AGENDA_LOOKUP_LINK_TEXT);
        waitForPageToLoad();
    }

    protected void testAgendaEditRuleRefresh() throws Exception {
        selectFrameIframePortlet();
        waitAndClickButtonByText("Search");
        //        waitAndClickByXpath("//div[@class='uif-boxLayout uif-horizontalBoxLayout clearfix']/button[1]"); //  jiraAwareWaitAndClick("id=32");
        Thread.sleep(3000);
        waitAndClickByXpath("//a[@title='edit Agenda Definition with Agenda Id=T1000']",
                "Does user have edit permissions?"); // jiraAwareWaitAndClick("id=194_line0");
        checkForIncidentReport("");
        Thread.sleep(3000);
        waitAndClickByXpath("//li/a[@class='agendaNode ruleNode']"); // jiraAwareWaitAndClick("//li[@id='473_node_0_parent_root']/a");
        waitAndClickByXpath("//li/a[@class='agendaNode logicNode whenTrueNode']");
        waitAndClickByLinkText("[-] collapse all");

        // click refresh  several times
        for (int i = 0; i < 6; i++) {
            for (int second = 0;; second++) {
                if (second >= waitSeconds)
                    failableFail(TIMEOUT_MESSAGE);
                try {
                    if (isElementPresent(".kr-refresh-button"))
                        break;
                } catch (Exception e) {}
                Thread.sleep(1000);
            }
            waitAndClick("button.kr-refresh-button");
        }
        passed();
    }

    /**
     * test that repeated ajax refreshes work
     */
    @Test
    public void testAgendaEditRuleRefreshBookmark() throws Exception {
        testAgendaEditRuleRefresh();
    }

    /**
     * test that repeated ajax refreshes work
     */
    @Test
    public void testAgendaEditRuleRefreshNav() throws Exception {
        testAgendaEditRuleRefresh();
    }
}

