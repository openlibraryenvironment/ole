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

/**
 * Tests the Component section in Rice.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ComponentSmokeTest extends SmokeTestBase {

    String docId;

    /**
     *  for Component Parameter
     */
    public static final String FOR_TEST_MESSAGE = " for Component Parameter";

    /**
     * ITUtil.PORTAL + "?channelTitle=Component&channelUrl=" + ITUtil.getBaseUrlString() +
     "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.coreservice.impl.component.ComponentBo&docFormKey=88888888&returnLocation=" +
     ITUtil.PORTAL_URL + "&hideReturnLink=true";
     */
    public static final String BOOKMARK_URL = ITUtil.PORTAL + "?channelTitle=Component&channelUrl=" + ITUtil.getBaseUrlString() +
            "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.rice.coreservice.impl.component.ComponentBo&docFormKey=88888888&returnLocation=" +
            ITUtil.PORTAL_URL + "&hideReturnLink=true";

    @Override
    public String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    protected void navigate() throws InterruptedException {
        waitAndClickAdministration(this);
        waitForTitleToEqualKualiPortalIndex();
        checkForIncidentReport("Component");
        selectFrameIframePortlet();
        waitAndClickByLinkText("Component");
//        selectFrame("relative=up");
        checkForIncidentReport("Component");
    }

    protected void testComponentCreateNewCancel() throws Exception {
        waitAndCreateNew();
        testCancelConfirmation();
        passed();
    }

    protected void testComponentParameter() throws Exception {
        //Create New
        waitAndCreateNew();
        String componentName = "TestName" + ITUtil.createUniqueDtsPlusTwoRandomChars();
        String componentCode = "TestCode" + ITUtil.createUniqueDtsPlusTwoRandomChars();
        docId = testCreateNewComponent(componentName, componentCode, FOR_TEST_MESSAGE);

        //Lookup
        navigate();
        testLookUpComponent(docId, componentName, componentCode);

        //edit
        testEditComponent(docId, componentName, componentCode);

        //Verify if its edited
        navigate();
        testVerifyEditedComponent(docId, componentName, componentCode);

        //copy
        testCopyComponent(docId, componentName + "copy", componentCode + "copy");

        //Verify if its copied
        navigate();
        testVerifyCopyComponent(docId, componentName + "copy", componentCode + "copy");
        passed();
    }

    @Test
    public void testCreateNewCancelComponentBookmark() throws Exception {
        testComponentCreateNewCancel();
    }

    @Test
    public void testComponentCreateNewCancelComponentNav() throws Exception {
        testComponentCreateNewCancel();
    }

    @Test
    public void testComponentParameterBookmark() throws Exception {
        testComponentParameter();
    }

    @Test
    public void testComponentParameterNav() throws Exception {
        testComponentParameter();
    }
}
