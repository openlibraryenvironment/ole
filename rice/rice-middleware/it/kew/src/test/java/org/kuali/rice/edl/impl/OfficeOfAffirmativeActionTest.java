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
package org.kuali.rice.edl.impl;

import org.junit.Test;
import org.kuali.rice.kew.test.KEWTestCase;

/**
 * Tests the web GUI for the ActionList.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class OfficeOfAffirmativeActionTest extends KEWTestCase {

	//private static final String URL_PREFIX = "http://localhost:9952/en-test/";
	private final String OAA_DIR = getBaseDir() + "/src/test/config/edoclite/OfficeOfAffirmativeAction/";

	protected void loadTestData() throws Exception {
		// workgroups
		loadXmlFile("OAATestWorkgroups.xml");

		// attributes
		loadXmlFileFromFileSystem(OAA_DIR + "EdocliteDepartmentSearchAttribute.xml");
		loadXmlFileFromFileSystem(OAA_DIR + "EdocliteExpectedStartDateSearchAttribute.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "EdocliteInitiatorAttribute.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "EdocliteOAASearchAttribute.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "EdocliteSalaryGradeSearchAttribute.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "EdocliteSchoolAttribute.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "EdocliteSchoolSearchAttribute.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "EdocliteTotalAMFSearchAttribute.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "EdocliteTotalApplicantsSearchAttribute.xml");

        // templates
        loadXmlFileFromFileSystem(OAA_DIR + "OfficeOfAffirmativeActionInitiatorAcknowledgmentRuleTemplate.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "OfficeOfAffirmativeActionOfficerRuleTemplate.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "OfficeOfAffirmativeActionSchoolAcknowledgementRuleTemplate.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "OfficeOfAffirmativeActionSchoolRuleTemplate.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "OfficeOfAffirmativeActionViceChancellorRuleTemplate.xml");

        // document types
        loadXmlFileFromFileSystem(OAA_DIR + "OfficeOfAffirmativeActionDocType.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "InterviewRequestDocType.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "OfferRequestDocType.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "SearchStatusDocType.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "VacancyNoticeDocType.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "WaiverRequestDocType.xml");

        // forms
        loadXmlFileFromFileSystem(OAA_DIR + "InterviewRequestForm.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "OfferRequestForm.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "SearchStatusForm.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "VacancyNoticeForm.xml");
        loadXmlFileFromFileSystem(OAA_DIR + "WaiverRequestForm.xml");

        // rules
        loadXmlFileFromFileSystem(OAA_DIR + "OfficeOfAffirmativeActionPilotRules.xml");

        // widgets
        loadXmlFile(EDLXmlUtils.class, "default-widgets.xml");
    }

	@Test public void testOAAEdocLiteLoad() {
	    // just a test to allow the setup method above to run and verify the xml import
	    // of these files
	}

	/**
	 * Tests the Office of Affirmative Action interview request.
	 */
//	@Test public void testInterviewRequest() throws Exception {
//		WebClient webClient = new WebClient();
//
//		URL url = new URL (URL_PREFIX + "EDocLite?userAction=initiate&edlName=InterviewRequest");
//		HtmlPage page = (HtmlPage)webClient.getPage(url);
//
//		// On the first access, we should end up on the backdoor and login as quickstart
//		HtmlForm form = (HtmlForm) page.getForms().get(0);
//		HtmlTextInput textInput = (HtmlTextInput)form.getInputByName("__login_user");
//		assertEquals("quickstart", textInput.getDefaultValue());
//		page = (HtmlPage)form.submit();
//
//		// we should be on the EDL form now, check that theres a form here
//		assertEquals("Should be one form.", 1, page.getForms().size());
//
//		// TOOD, fill out the form, route it, verify it goes where it needs to go
//		// do a few different permutations of data on a few different EDL's
//	}
//
//
//	@Test public void testOfferRequest() throws Exception {
//		// TODO
//	}
//
//	@Test public void testSearchStatus() throws Exception {
//		// TODO
//	}
//
//	@Test public void testVacancyNotice() throws Exception {
//		// TODO
//	}
//
//	@Test public void testWaiverRequest() throws Exception {
//		// TODO
//	}

}
