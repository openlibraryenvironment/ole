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
package org.kuali.rice.kew.clientapp;

import org.junit.Test;
import org.kuali.rice.kew.actions.BlanketApproveTest;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;

import static org.junit.Assert.*;

/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
@BaselineMode(Mode.CLEAR_DB)
public class WorkflowInfoTest extends KEWTestCase {

    @Override
    protected void loadTestData() {
        // need this configuration to create a BlanketApproveParallelTest
        loadXmlFile(BlanketApproveTest.class, "ActionsConfig.xml");
    }

    /**
     * Tests the loading of a RouteHeaderVO using the WorkflowInfo.
     * 
     * Verifies that an NPE no longer occurrs as mentioned in KULRICE-765.
     */
    @Test
    public void testGetRouteHeader() throws Exception {
        // ensure the UserSession is cleared out (could have been set up by other tests)
        GlobalVariables.setUserSession(null);
        String ewestfalPrincipalId = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName("ewestfal")
                .getPrincipalId();
        GlobalVariables.setUserSession(new UserSession("ewestfal"));
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(ewestfalPrincipalId,
                "TestDocumentType");
        String documentId = workflowDocument.getDocumentId();
        assertNotNull(documentId);

        Document document = KewApiServiceLocator.getWorkflowDocumentService().getDocument(documentId);
        assertNotNull(document);

        assertEquals(documentId, document.getDocumentId());
        assertEquals(DocumentStatus.INITIATED, document.getStatus());
    }

    @Test
    public void testGetDocumentStatus() throws Exception {
        // verify that a null document id throws an exception
        try {
            DocumentStatus status = KewApiServiceLocator.getWorkflowDocumentService().getDocumentStatus(null);
            fail("A WorkflowException should have been thrown, instead returned status: " + status);
        } catch (IllegalArgumentException e) {}

        // verify that a bad document id throws an exception
        try {
            DocumentStatus status = KewApiServiceLocator.getWorkflowDocumentService().getDocumentStatus("-1");
            fail("A IllegalStateException Should have been thrown, instead returned status: " + status);
        } catch (IllegalStateException e) {}

        // now create a doc and load it's status
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"),
                "TestDocumentType");
        String documentId = document.getDocumentId();
        assertNotNull(documentId);

        DocumentStatus status = KewApiServiceLocator.getWorkflowDocumentService().getDocumentStatus(documentId);
        assertEquals("Document should be INITIATED.", KewApiConstants.ROUTE_HEADER_INITIATED_CD, status.getCode());

        // cancel the doc, it's status should be updated
        document.cancel("");
        status = KewApiServiceLocator.getWorkflowDocumentService().getDocumentStatus(documentId);
        assertEquals("Document should be CANCELED.", KewApiConstants.ROUTE_HEADER_CANCEL_CD, status.getCode());
    }

    /**
     * test for issue KFSMI-2979 This method verifies that
     * workflowInfo.getRoutedByPrincipalIdByDocumentId returns the blanket approver for a document that
     * was put onroute by that person (the blanket approver)
     */
    @Test
    public void testBlanketApproverSubmitted() throws WorkflowException {
        Person blanketApprover = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("ewestfal");

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(blanketApprover.getPrincipalId(),
                "BlanketApproveParallelTest");
        document.blanketApprove("");

        String routedByPrincipalId = KewApiServiceLocator.getWorkflowDocumentService().getRoutedByPrincipalIdByDocumentId(
                document.getDocumentId());
        assertEquals("the blanket approver should be the routed by", blanketApprover.getPrincipalId(),
                routedByPrincipalId);
    }

    @Test
    public void testGetAppDocId() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"),
                "TestDocumentType");
        document.saveDocumentData();

        String appDocId = KewApiServiceLocator.getWorkflowDocumentService().getApplicationDocumentId(document.getDocumentId());
        assertNull("appDocId should be null", appDocId);

        String appDocIdValue = "1234";
        document.setApplicationDocumentId(appDocIdValue);
        document.saveDocumentData();

        appDocId = KewApiServiceLocator.getWorkflowDocumentService().getApplicationDocumentId(document.getDocumentId());
        assertEquals("Incorrect appDocId", appDocIdValue, appDocId);
    }

    @Test
    public void testGetAppDocStatus() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"),
                "TestDocumentType");
        document.saveDocumentData();

        String appDocStatus = KewApiServiceLocator.getWorkflowDocumentService().getApplicationDocumentStatus(document.getDocumentId());
        assertNull("appDocStatus should be null", appDocStatus);

        String appDocStatusValue = "Approved";
        document.setApplicationDocumentStatus(appDocStatusValue);
        document.saveDocumentData();

        appDocStatus = KewApiServiceLocator.getWorkflowDocumentService().getApplicationDocumentStatus(document.getDocumentId());
        assertEquals("Incorrect appDocStatus", appDocStatusValue, appDocStatus);
    }

}
