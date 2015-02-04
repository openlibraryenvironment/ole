/*
 * Copyright 2007-2008 The Kuali Foundation
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
package org.kuali.ole.module.purap.document;


import org.junit.Test;
import org.kuali.ole.KualiTestBase;
import org.kuali.ole.fixture.UserNameFixture;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.ContractManagerAssignmentDetail;
import org.kuali.ole.module.purap.fixture.ContractManagerAssignmentDocumentFixture;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.AccountingDocumentTestUtils;
import org.kuali.ole.sys.document.workflow.WorkflowTestUtils;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;


/**
 * This class is used to test create and route populated Assign Contract Manager Documents.
 */

public class ContractManagerAssignmentDocumentTest extends KualiTestBase {
    public static final Class<ContractManagerAssignmentDocument> DOCUMENT_CLASS = ContractManagerAssignmentDocument.class;
    private static final String ACCOUNT_REVIEW = "Account";

    private ContractManagerAssignmentDocument acmDocument = null;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        changeCurrentUser(UserNameFixture.parke);
    }

    @Override
    public void tearDown() throws Exception {
        acmDocument = null;
        super.tearDown();
    }

    private int getExpectedPrePeCount() {
        return 0;
    }

    /**
     * Tests the routing of ContractManagerAssignmentDocument to final.
     * 
     * @throws Exception
     */

    @Test
    public final void testRouteDocument() throws Exception {
        acmDocument = buildSimpleDocument();
        
        acmDocument.prepareForSave();
        DocumentService documentService = SpringContext.getBean(DocumentService.class);
        assertFalse(DocumentStatus.ENROUTE.equals(acmDocument.getDocumentHeader().getWorkflowDocument().getStatus()));
        routeDocument(acmDocument, "saving copy source document", documentService);
        
        WorkflowTestUtils.waitForDocumentApproval(acmDocument.getDocumentNumber());
        Document document = documentService.getByDocumentHeaderId(acmDocument.getDocumentNumber());
        assertTrue("Document should now be final.", document.getDocumentHeader().getWorkflowDocument().isFinal());
    }
    
    /**
     * Tests the routing of ContractManagerAssignmentDocument to final.
     * 
     * @throws Exception
     */
    public final String testRouteDocument2() throws Exception {
        acmDocument = buildSimpleDocument2();
        
        acmDocument.prepareForSave();
        DocumentService documentService = SpringContext.getBean(DocumentService.class);
        assertFalse(DocumentStatus.ENROUTE.equals(acmDocument.getDocumentHeader().getWorkflowDocument().getStatus()));
        routeDocument(acmDocument, "saving copy source document", documentService);
        WorkflowTestUtils.waitForDocumentApproval(acmDocument.getDocumentNumber());
        ContractManagerAssignmentDocument document = (ContractManagerAssignmentDocument) documentService.getByDocumentHeaderId(acmDocument.getDocumentNumber());
        assertTrue("Document should now be final.", document.getDocumentHeader().getWorkflowDocument().isFinal());
        return acmDocument.getContractManagerAssignmentDetail(0).getRequisition().getDocumentNumber();
    }
    
    /**
     * Helper method to route the document.
     * 
     * @param document                 The assign contract manager document to be routed.
     * @param annotation               The annotation String.
     * @param documentService          The service to use to route the document.
     * @throws WorkflowException
     */
    private void routeDocument(Document document, String annotation, DocumentService documentService) throws WorkflowException {
        try {
            documentService.routeDocument(document, annotation, null);
        }
        catch (ValidationException e) {
            // If the business rule evaluation fails then give us more info for debugging this test.
            fail(e.getMessage() + ", " + GlobalVariables.getMessageMap());
        }
    }

    /**
     * Helper method to create a new valid ContractManagerAssignmentDocument.
     * 
     * @return            The ContractManagerAssignmentDocument created by this method.
     * @throws Exception
     */
    private ContractManagerAssignmentDocument buildSimpleDocument() throws Exception {
        List<ContractManagerAssignmentDetail> details = ContractManagerAssignmentDocumentFixture.ACM_DOCUMENT_VALID.getContractManagerAssignmentDetails();
        for (ContractManagerAssignmentDetail detail : details) {
            detail.getRequisition().setAccountDistributionMethod("S");
            RequisitionDocument routedReq = routeRequisitionUntilAwaitingContractManager(detail.getRequisition());
            detail.setRequisitionIdentifier(routedReq.getPurapDocumentIdentifier());
            detail.refreshNonUpdateableReferences();
        }
        acmDocument = ContractManagerAssignmentDocumentFixture.ACM_DOCUMENT_VALID.createContractManagerAssignmentDocument();
        for (ContractManagerAssignmentDetail detail : details) {
            detail.getRequisition().setAccountDistributionMethod("S");
            detail.setContractManagerAssignmentDocument(acmDocument);
        }
        acmDocument.setContractManagerAssignmentDetailss(details);
        return acmDocument;
    }

    /**
     * Helper method to create a new valid ContractManagerAssignmentDocument.
     * 
     * @return            The ContractManagerAssignmentDocument created by this method.
     * @throws Exception
     */
    private ContractManagerAssignmentDocument buildSimpleDocument2() throws Exception {
        List<ContractManagerAssignmentDetail> details = ContractManagerAssignmentDocumentFixture.ACM_DOCUMENT_VALID_2.getContractManagerAssignmentDetails();
        for (ContractManagerAssignmentDetail detail : details) {
            RequisitionDocument routedReq = routeRequisitionUntilAwaitingContractManager2(detail.getRequisition());
            detail.setRequisitionIdentifier(routedReq.getPurapDocumentIdentifier());
            detail.refreshNonUpdateableReferences();
        }
        acmDocument = ContractManagerAssignmentDocumentFixture.ACM_DOCUMENT_VALID_2.createContractManagerAssignmentDocument();
        for (ContractManagerAssignmentDetail detail : details) {
            detail.setContractManagerAssignmentDocument(acmDocument);
        }
        acmDocument.setContractManagerAssignmentDetailss(details);
        return acmDocument;
    }

    /**
     * Helper method to route a requisition document until AwaitingContractManager status.
     * The requisition document will be used to create the ContractManagerAssignmentDocument.
     * 
     * @param requisitionDocument The RequisitionDocument to be routed until AwaitingContractManager status.
     * @return                    The RequisitionDocument that was routed until AwaitingContractManager status.
     * @throws Exception
     */
    private RequisitionDocument routeRequisitionUntilAwaitingContractManager(RequisitionDocument requisitionDocument) throws Exception {
        final String docId = requisitionDocument.getDocumentNumber();
        AccountingDocumentTestUtils.routeDocument(requisitionDocument, SpringContext.getBean(DocumentService.class));
        WorkflowTestUtils.waitForNodeChange(requisitionDocument.getDocumentHeader().getWorkflowDocument(), ACCOUNT_REVIEW);

        // the document should now be routed to vputman as Fiscal Officer
        changeCurrentUser(UserNameFixture.sterner);
        requisitionDocument = (RequisitionDocument) SpringContext.getBean(DocumentService.class).getByDocumentHeaderId(docId);
        assertTrue("At incorrect node.", WorkflowTestUtils.isAtNode(requisitionDocument, ACCOUNT_REVIEW));
        assertTrue("Document should be enroute.", requisitionDocument.getDocumentHeader().getWorkflowDocument().isEnroute());
        assertTrue("rorenfro should have an approve request.", requisitionDocument.getDocumentHeader().getWorkflowDocument().isApprovalRequested());
        SpringContext.getBean(DocumentService.class).approveDocument(requisitionDocument, "Test approving as rorenfro", null);

        changeCurrentUser(UserNameFixture.jgerhart);
        requisitionDocument = (RequisitionDocument) SpringContext.getBean(DocumentService.class).getByDocumentHeaderId(docId);
        SpringContext.getBean(DocumentService.class).acknowledgeDocument(requisitionDocument, "Acknowledging as jgerhart", null);
        
        WorkflowTestUtils.waitForDocumentApproval(requisitionDocument.getDocumentNumber());

        changeCurrentUser(UserNameFixture.khuntley);
        requisitionDocument = (RequisitionDocument) SpringContext.getBean(DocumentService.class).getByDocumentHeaderId(docId);
        assertTrue("Document should now be final.", requisitionDocument.getDocumentHeader().getWorkflowDocument().isFinal());
        changeCurrentUser(UserNameFixture.parke);
        return requisitionDocument;
    }

    /**
     * Helper method to route a requisition document until AwaitingContractManager status.
     * The requisition document will be used to create the ContractManagerAssignmentDocument.
     * 
     * @param requisitionDocument The RequisitionDocument to be routed until AwaitingContractManager status.
     * @return                    The RequisitionDocument that was routed until AwaitingContractManager status.
     * @throws Exception
     */
    private RequisitionDocument routeRequisitionUntilAwaitingContractManager2(RequisitionDocument requisitionDocument) throws Exception {
        final String docId = requisitionDocument.getDocumentNumber();
        
        SpringContext.getBean(DocumentService.class).routeDocument(requisitionDocument, "saving copy source document", null);
        WorkflowTestUtils.waitForNodeChange(requisitionDocument.getDocumentHeader().getWorkflowDocument(), ACCOUNT_REVIEW);

        // the document should now be routed to sterner as Fiscal Officer
        changeCurrentUser(UserNameFixture.sterner);
        requisitionDocument = (RequisitionDocument) SpringContext.getBean(DocumentService.class).getByDocumentHeaderId(docId);
        assertTrue("At incorrect node.", WorkflowTestUtils.isAtNode(requisitionDocument, ACCOUNT_REVIEW));
        assertTrue("Document should be enroute.", requisitionDocument.getDocumentHeader().getWorkflowDocument().isEnroute());
        assertTrue("sterner should have an approve request.", requisitionDocument.getDocumentHeader().getWorkflowDocument().isApprovalRequested());
        SpringContext.getBean(DocumentService.class).approveDocument(requisitionDocument, "Test approving as sterner", null);

        WorkflowTestUtils.waitForDocumentApproval(requisitionDocument.getDocumentNumber());

        changeCurrentUser(UserNameFixture.khuntley);
        requisitionDocument = (RequisitionDocument) SpringContext.getBean(DocumentService.class).getByDocumentHeaderId(docId);
        assertTrue("Document should now be Awaiting Contract Manager Assignment.", requisitionDocument.getApplicationDocumentStatus().equals(PurapConstants.RequisitionStatuses.APPDOC_AWAIT_CONTRACT_MANAGER_ASSGN));
        changeCurrentUser(UserNameFixture.parke);
        return requisitionDocument;
    }

}

