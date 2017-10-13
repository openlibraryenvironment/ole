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
package org.kuali.rice.kew.actions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.kuali.rice.edl.framework.util.EDLFunctions;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;

public class RouteLogAuthenticationTest extends KEWTestCase {

	public static final String DOCUMENT_TYPE_NAME = "BlanketApproveSequentialTest";

    protected void loadTestData() throws Exception {
        loadXmlFile("ActionsConfig.xml");
    }

	/**
     * Tests EDLFunctions.isUserRouteLogAuthenticated
     */
    @Test public void testUserRouteLogAuthenticated() throws Exception {
    	String user1PrincipalId = getPrincipalIdForName("user1");

    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(user1PrincipalId, DOCUMENT_TYPE_NAME);
    	document.route("");

    	// ensure the UserSession is cleared out (could have been set up by other tests)
    	GlobalVariables.setUserSession(null);

        // false because we didn't set up the user session properly
        assertFalse(EDLFunctions.isUserRouteLogAuthenticated(document.getDocumentId() + ""));

        // these two should be in the route log
        GlobalVariables.setUserSession(new UserSession("user1"));
        assertTrue(EDLFunctions.isUserRouteLogAuthenticated(document.getDocumentId() + ""));
        GlobalVariables.setUserSession(new UserSession("bmcgough"));
        assertTrue(EDLFunctions.isUserRouteLogAuthenticated(document.getDocumentId() + ""));

        // user2 should NOT be in the route log
        GlobalVariables.setUserSession(new UserSession("user2"));
        assertFalse(EDLFunctions.isUserRouteLogAuthenticated(document.getDocumentId() + ""));
    }

}
