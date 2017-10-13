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
package org.kuali.rice.krad.service;

import org.junit.Test;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.test.document.AccountRequestDocument;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.MessageMap;
import org.kuali.rice.krad.test.KRADTestCase;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the DocumentService (currently only getNewDocument is tested).
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DocumentServiceTest extends KRADTestCase {

    public DocumentServiceTest() {
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        GlobalVariables.setMessageMap(new MessageMap());
        GlobalVariables.setUserSession(new UserSession("quickstart"));
    }

    @Override
    public void tearDown() throws Exception {
        GlobalVariables.setMessageMap(new MessageMap());
        GlobalVariables.setUserSession(null);
        super.tearDown();
    }

    /**
     * This method tests getNewDocument
     *
     * @throws Exception
     */
    @Test public void testGetNewDocument() throws Exception {
        AccountRequestDocument travelDocument = (AccountRequestDocument) KRADServiceLocatorWeb.getDocumentService().getNewDocument("AccountRequest");
        WorkflowDocument wd =  travelDocument.getDocumentHeader().getWorkflowDocument();

        assertEquals("Initiator should be the current user", wd.getInitiatorPrincipalId(), GlobalVariables.getUserSession().getPerson().getPrincipalId());
    }

    /**
     * This method tests getNewDocument but the initiator is not the current user
     *
     * @throws Exception
     */
    @Test public void testGetNewDocumentDifferentInitiatorThanCurrentUser() throws Exception {
        AccountRequestDocument travelDocument = (AccountRequestDocument) KRADServiceLocatorWeb.getDocumentService().getNewDocument("AccountRequest", "testuser1");
        WorkflowDocument wd =  travelDocument.getDocumentHeader().getWorkflowDocument();

        assertEquals("Initiator should be testuser1", wd.getInitiatorPrincipalId(), "testuser1");
    }

    /**
     * This method tests getNewDocument but the initiator is invalid
     *
     * @throws Exception
     */
    @Test public void testGetNewDocumentInvalidInitiator() throws Exception {
        AccountRequestDocument travelDocument = (AccountRequestDocument) KRADServiceLocatorWeb.getDocumentService().getNewDocument("AccountRequest", "notValidUserAtAll");
        WorkflowDocument wd =  travelDocument.getDocumentHeader().getWorkflowDocument();

        assertEquals("Initiator should be the current user", wd.getInitiatorPrincipalId(), GlobalVariables.getUserSession().getPerson().getPrincipalId());
    }
}
