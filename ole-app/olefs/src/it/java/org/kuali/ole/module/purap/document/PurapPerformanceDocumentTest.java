/*
 * Copyright 2009 The Kuali Foundation
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


import java.util.List;

import org.junit.Test;
import org.kuali.ole.ConfigureContext;
import org.kuali.ole.KualiTestBase;
import org.kuali.ole.fixture.UserNameFixture;
import org.kuali.ole.module.purap.businessobject.ContractManagerAssignmentDetail;
import org.kuali.ole.module.purap.fixture.ContractManagerAssignmentDocumentFixture;
import org.kuali.ole.module.purap.fixture.RequisitionDocumentFixture;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.AccountingDocumentTestUtils;
import org.kuali.ole.sys.document.workflow.WorkflowTestUtils;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.GlobalVariables;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

/**
 * Sets up documents optionally to be used by the following PurAp JMeter performance tests:
 * <p/>
 * purapComplexSearch.jmx (needs Closed RequisitionDocument)
 * purapPaymentRequestDocument.jmx (needs Open PurchaseOrderDocument)
 * <p/>
 * To run, change the value of SETUP_PERFORMANCE_TESTS to true.
 */
public class PurapPerformanceDocumentTest extends KualiTestBase {

    protected RequisitionDocument requisitionDocument = null;
    protected boolean SETUP_PERFORMANCE_TESTS = false; // Set to true locally to setup performance tests.

    protected DocumentService documentService = null;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        documentService = SpringContext.getBean(DocumentService.class);
        changeCurrentUser(UserNameFixture.parke);
    }

    @Override
    public void tearDown() throws Exception {
        requisitionDocument = null;
        super.tearDown();
    }


    @Test
    public final void testFullRoutePerformanceDocuments() throws Exception {
        if (!SETUP_PERFORMANCE_TESTS) {
            assertTrue(true);
        } else {
            requisitionDocument = RequisitionDocumentFixture.REQ_PERFORMANCE.createRequisitionDocument();
            requisitionDocument.getDocumentHeader().setDocumentDescription("Load Testing");
            final String docId = requisitionDocument.getDocumentNumber();
            AccountingDocumentTestUtils.routeDocument(requisitionDocument, documentService);
            WorkflowTestUtils.waitForNodeChange(requisitionDocument.getDocumentHeader().getWorkflowDocument(), OLEConstants.RouteLevelNames.ACCOUNT);

            changeCurrentUser(UserNameFixture.rorenfro);  //Approving as Fiscal Officer.
            requisitionDocument = (RequisitionDocument) documentService.getByDocumentHeaderId(docId);
            assertTrue("At incorrect node.", WorkflowTestUtils.isAtNode(requisitionDocument, OLEConstants.RouteLevelNames.ACCOUNT));
            assertTrue("Document should be enroute.", requisitionDocument.getDocumentHeader().getWorkflowDocument().isEnroute());
            assertTrue("Fiscal Officer should have an approve request.", requisitionDocument.getDocumentHeader().getWorkflowDocument().isApprovalRequested());
            SpringContext.getBean(DocumentService.class).approveDocument(requisitionDocument, "Test approving as Fiscal Officer.", null);

            WorkflowTestUtils.waitForDocumentApproval(requisitionDocument.getDocumentNumber());

            changeCurrentUser(UserNameFixture.parke);  //Switch back to original User.
            requisitionDocument = (RequisitionDocument) documentService.getByDocumentHeaderId(docId);
            assertTrue("Document should now be final.", requisitionDocument.getDocumentHeader().getWorkflowDocument().isFinal());

            ContractManagerAssignmentDocument cmaDocument = buildContractManagerAssignmentDocument();
            cmaDocument.prepareForSave();

            assertFalse(DocumentStatus.ENROUTE.equals(cmaDocument.getDocumentHeader().getWorkflowDocument().getStatus()));
            routeDocument(cmaDocument, "saving copy source document", documentService);

            WorkflowTestUtils.waitForDocumentApproval(cmaDocument.getDocumentNumber());
            cmaDocument = (ContractManagerAssignmentDocument) documentService.getByDocumentHeaderId(cmaDocument.getDocumentNumber());
            assertTrue("Document should now be final.", cmaDocument.getDocumentHeader().getWorkflowDocument().isFinal());

            String poNumber = requisitionDocument.getRelatedViews().getRelatedPurchaseOrderViews().get(0).getDocumentNumber();
            PurchaseOrderDocument poDoc = (PurchaseOrderDocument) documentService.getByDocumentHeaderId(poNumber);

            poDoc.setVendorName("KUALI UNIVERSITY");
            poDoc.setVendorHeaderGeneratedIdentifier(1001);
            poDoc.setVendorDetailAssignedIdentifier(0);
            poDoc.setVendorLine1Address("341 PINE TREE RD");
            poDoc.setVendorCityName("ITHACA");
            poDoc.setVendorStateCode("NY");
            poDoc.setVendorPostalCode("14850");
            poDoc.setVendorCountryCode("US");
            poDoc.setVendorPaymentTermsCode("00N30");
            poDoc.setPurchaseOrderVendorChoiceCode("ONLY");

            routeDocument(poDoc, "Test routing as parke", documentService);

            changeCurrentUser(UserNameFixture.butt);  //Approving at the Budget Level
            poDoc = (PurchaseOrderDocument) documentService.getByDocumentHeaderId(poNumber);
            assertTrue("Document should be enroute.", poDoc.getDocumentHeader().getWorkflowDocument().isEnroute());
            assertTrue("Budget Approver should have an approve request.", poDoc.getDocumentHeader().getWorkflowDocument().isApprovalRequested());
            SpringContext.getBean(DocumentService.class).approveDocument(poDoc, "Test approving as Budget Approver.", null);

            WorkflowTestUtils.waitForDocumentApproval(poDoc.getDocumentNumber());

            changeCurrentUser(UserNameFixture.parke);  //Switch back to original User.
            poDoc = (PurchaseOrderDocument) documentService.getByDocumentHeaderId(poNumber);
            assertTrue("Document should now be final.", poDoc.getDocumentHeader().getWorkflowDocument().isFinal());
        }
    }

    private ContractManagerAssignmentDocument buildContractManagerAssignmentDocument() throws Exception {
        ContractManagerAssignmentDocument cmaDocument = null;
        List<ContractManagerAssignmentDetail> details = ContractManagerAssignmentDocumentFixture.ACM_DOCUMENT_PERFORMANCE.getContractManagerAssignmentDetails();
        for (ContractManagerAssignmentDetail detail : details) {
            detail.setRequisitionIdentifier(requisitionDocument.getPurapDocumentIdentifier());
            detail.refreshNonUpdateableReferences();
        }
        cmaDocument = ContractManagerAssignmentDocumentFixture.ACM_DOCUMENT_PERFORMANCE.createContractManagerAssignmentDocument();
        for (ContractManagerAssignmentDetail detail : details) {
            detail.setContractManagerAssignmentDocument(cmaDocument);
        }
        cmaDocument.setContractManagerAssignmentDetailss(details);
        return cmaDocument;
    }

    private void routeDocument(Document document, String annotation, DocumentService documentService) throws WorkflowException {
        try {
            documentService.routeDocument(document, annotation, null);
        } catch (ValidationException e) {
            // If the business rule evaluation fails then give us more info for debugging this test.
            fail(e.getMessage() + ", " + GlobalVariables.getMessageMap());
        }
    }
}
