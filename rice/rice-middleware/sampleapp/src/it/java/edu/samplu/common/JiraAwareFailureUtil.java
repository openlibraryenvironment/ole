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
package edu.samplu.common;

import edu.samplu.admin.test.ComponentSmokeTest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created as a way to link Rice Smoke Test failures to existing Jiras as a html link in Jenkins.  The more failures
 * the more useful it is to not have to keep tracking down the same Jiras.  Having this feature for Integration Tests
 * as well would be a huge help for the QA team.
 * TODO:
 * <ol>
 *   <li>Integration Test integration.  ITs often fail by the 10s tracking down existing Jiras is a huge time sink.</li>
 *   <li>Possible Extraction of jiraMatches data to property file.</li>
 * </ol>
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class JiraAwareFailureUtil {
    /**
     * KULRICE-8823 Fix broken smoke tests in CI
     */
    public static final String KULRICE_8823_FIX_BROKEN_SMOKE_TESTS_IN_CI = "KULRICE-8823 Fix broken smoke tests in CI";

    /**
     * https://jira.kuali.org/browse/
     */
    public static final String JIRA_BROWSE_URL = "https://jira.kuali.org/browse/";

    static Map<String, String> jiraMatches;

    static {
        jiraMatches = new HashMap<String, String>();

        jiraMatches.put(
                ComponentSmokeTest.CREATE_NEW_DOCUMENT_NOT_SUBMITTED_SUCCESSFULLY_MESSAGE_TEXT + ComponentSmokeTest.FOR_TEST_MESSAGE,
                KULRICE_8823_FIX_BROKEN_SMOKE_TESTS_IN_CI);

        jiraMatches.put("//*[@id='u229']", KULRICE_8823_FIX_BROKEN_SMOKE_TESTS_IN_CI);

        jiraMatches.put("after clicking Expand All", "KULRICE-3833 KRAD Sampleapp (Travel) Account Inquiry Collapse all toggles all and Expand all does nothing");

        jiraMatches.put("Library Widget Suggest, CAT not suggested", "KULRICE-10365 Library Widget Suggest not suggesting"); // already in 2.4 merged back as error exists in 2.3 as well.

        jiraMatches.put("Lookupable is null", "KULRICE-10207 DemoTravelAccountMultivalueLookUpSmokeTest Lookupable is null.");

        jiraMatches.put("Account Maintenance (Edit)", "KULRICE-10216 KRAD Demo Account Maintenance (Edit) Incident Report RiceRuntimeException: Exception trying to invoke action ROUTE");

        jiraMatches.put("An exception occurred processing JSP page /kr/WEB-INF/jsp/KualiMaintenanceDocument.jsp", "KULRICE-10235 JasperException: An exception occurred processing JSP page /kr/WEB-INF/jsp/KualiMaintenanceDocument.jsp");

        jiraMatches.put("BusinessObjectMetaDataServiceImpl.getBusinessObjectRelationship(BusinessObjectMetaDataServiceImpl.java:267)", "KULRICE-10354 Identity links throws NullPointerException");

        jiraMatches.put("//table[@id='row']/tbody/tr[1]/td[1]/a", "KULRICE-10355 Investigate DocSearchWDIT testBasicDocSearch failure");

        jiraMatches.put("BusinessObjectDaoProxy called with non-legacy class: class org.kuali.rice.coreservice.impl.namespace.NamespaceBo", "KULRICE-10356 Agenda edit BusinessObjectDaoProxy called with non-legacy class: class org.kuali.rice.coreservice.impl.namespace.NamespaceBo");

        jiraMatches.put("annot get new instance of class to check for KualiCode", "KULRICE-10358 Component search Action List Incident Report Cannot get new instance of class to check for KualiCode");

        jiraMatches.put("Library Widget Suggest, CAT not suggested", "KULRICE-10365 Library Widget Suggest not suggesting");

        jiraMatches.put("WorkFlowRouteRulesBlanketApp expected:<[FINAL]>", "KULRICE-9051 WorkFlow Route Rules Blanket Approval submit status results in Enroute, not Final");

        jiraMatches.put("HTTP Status 404 - /kew/RouteLog.do", "KULRICE-10540 Demo Travel Account Maintenance Edit Route log inline 404");
        //jiraMatches.put("", "");
    }

    /**
     * If the contents contents the jiraMatches key, call fail on failable passing in the jiraMatches value for the matched key.
     * @param contents to check for containing of the jiraMatches keys.
     * @param failable to fail with the jiraMatches value if the jiraMatches key is contained in the contents
     */
    public static void failOnMatchedJira(String contents, Failable failable) {
        Iterator<String> iter = jiraMatches.keySet().iterator();
        String key = null;

        while (iter.hasNext()) {
            key = iter.next();
            if (contents.contains(key)) {
                failable.fail(JIRA_BROWSE_URL + jiraMatches.get(key));
            }
        }
    }

    /**
     * Calls failOnMatchedJira with the contents and if no match is detected then the message.
     * @param contents to check for containing of the jiraMatches keys.
     * @param message to check for containing of the jiraMatches keys if contents doesn't
     * @param failable to fail with the jiraMatches value if the contents or message is detected
     */
    public static void failOnMatchedJira(String contents, String message, Failable failable) {
        failOnMatchedJira(contents, failable);
        failOnMatchedJira(message, failable);
    }
}
