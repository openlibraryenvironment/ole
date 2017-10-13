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

import edu.samplu.common.ITUtil;
import edu.samplu.common.SmokeTestBase;
import org.junit.Test;
import org.kuali.rice.krad.util.KRADConstants;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests whether the ENABLE_FIELD_LEVEL_HELP_IND parameter is being considered and loaded on each request.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ParameterEnableFieldLevelHelpIndSmokeTest extends SmokeTestBase {

    @Override
    protected String getBookmarkUrl() {
        return ITUtil.PORTAL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickAdministration(this);
        waitForPageToLoad();
    }

    /**
     * Sets the ENABLE_FIELD_LEVEL_HELP_IND parameter to 'Y', checks whether the field level help links are enabled
     * on the Person document, then sets ENABLE_FIELD_LEVEL_HELP_IND to 'N' and checks the opposite.
     *
     * @throws Exception
     */
    @Test
    public void testEnableFieldLevelHelpIndParameterNav() throws Exception {
        setParameter(KRADConstants.SystemGroupParameterNames.ENABLE_FIELD_LEVEL_HELP_IND, "Y");

        waitAndClickByLinkText("Person");
        waitForPageToLoad();
        selectFrameIframePortlet();
        assertTrue("The help field did not appear", isElementPresent("img[alt='[Help]Principal Name']"));

        setParameter(KRADConstants.SystemGroupParameterNames.ENABLE_FIELD_LEVEL_HELP_IND, "N");

        waitAndClickByLinkText("Person");
        waitForPageToLoad();
        selectFrameIframePortlet();
        assertFalse("The help field appeared", isElementPresent("img[alt='[Help]Principal Name']"));

        passed();
    }

    private void setParameter(String parameterName, String parameterValue) throws Exception {
        selectTopFrame();
        waitAndClickAdministration(this);
        waitAndClickByLinkText("Parameter");

        selectFrameIframePortlet();
        waitAndType("input#name", parameterName);
        waitAndClickSearch();
        waitAndClickByLinkText(EDIT_LINK_TEXT);

        waitAndTypeByXpath(DOC_DESCRIPTION_XPATH, "Setting Field Level Help Indicator to " + parameterValue);
        clearTextByXpath("//textarea[@id='document.newMaintainableObject.value']");
        waitAndTypeByXpath("//textarea[@id='document.newMaintainableObject.value']", parameterValue);
        waitAndClickByName(BLANKET_APPROVE_NAME);
    }


}
