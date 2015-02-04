/*
 * Copyright 2005-2008 The Kuali Foundation
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

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.kuali.ole.fixture.UserNameFixture.khuntley;

import org.kuali.ole.fixture.UserNameFixture;
import org.kuali.ole.module.purap.businessobject.LineItemReceivingItem;
import org.kuali.ole.module.purap.document.LineItemReceivingDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.fixture.LineItemReceivingDocumentFixture;
import org.kuali.ole.module.purap.fixture.PurchaseOrderDocumentFixture;
import org.kuali.ole.select.businessobject.OleLineItemReceivingItem;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.AccountingDocumentTestUtils;
import org.kuali.ole.sys.document.workflow.WorkflowTestUtils;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.dao.DocumentDao;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.impl.DocumentServiceImpl;
import org.kuali.rice.krad.util.GlobalVariables;

/**
 * Used to create and test populated Receiving Line Documents of various kinds. 
 */

public class LineItemReceivingDocumentTest extends KualiTestBase {
    public static final Class<LineItemReceivingDocument> DOCUMENT_CLASS = LineItemReceivingDocument.class;
    protected static DocumentServiceImpl documentService = null;

    //TODO: This test passes but has issues with discovery service.
    //java.lang.NullPointerException
//    at org.kuali.ole.docstore.discovery.service.QueryServiceImpl.getWorkBibRecords(QueryServiceImpl.java:1742)


    @Override
    public void setUp() throws Exception {
        super.setUp();
        documentService = (DocumentServiceImpl) SpringContext.getBean("documentService");
        documentService.setDocumentDao((DocumentDao) SpringContext.getBean("documentDao"));
        changeCurrentUser(UserNameFixture.khuntley);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    private int getExpectedPrePeCount() {
        return 0;
    }

    @Test
    public final void testRouteDocument() throws Exception {
        //create PO
        PurchaseOrderDocument po = PurchaseOrderDocumentFixture.PO_ONLY_REQUIRED_FIELDS.createPurchaseOrderDocument();
        DocumentService documentService = SpringContext.getBean(DocumentService.class);
        po.prepareForSave();       
        AccountingDocumentTestUtils.routeDocument(po, "saving copy source document", null, documentService);
        WorkflowTestUtils.waitForDocumentApproval(po.getDocumentNumber());
        PurchaseOrderDocument poResult = (PurchaseOrderDocument) documentService.getByDocumentHeaderId(po.getDocumentNumber());
        
        //create Receiving
        LineItemReceivingDocument receivingLineDocument = LineItemReceivingDocumentFixture.EMPTY_LINE_ITEM_RECEIVING.createLineItemReceivingDocument();
        receivingLineDocument.populateReceivingLineFromPurchaseOrder(poResult);
        for(OleLineItemReceivingItem rli : (List<OleLineItemReceivingItem>)receivingLineDocument.getItems()){
            rli.setItemReceivedTotalQuantity( rli.getItemOrderedQuantity());
            rli.setItemReceivedTotalParts( rli.getItemOrderedParts());
        }
        receivingLineDocument.prepareForSave();
        assertFalse(DocumentStatus.ENROUTE.equals(receivingLineDocument.getDocumentHeader().getWorkflowDocument().getStatus()));
        routeDocument(receivingLineDocument, "routing line item receiving document", documentService);
        WorkflowTestUtils.waitForDocumentApproval(receivingLineDocument.getDocumentNumber());
        Document document = documentService.getByDocumentHeaderId(receivingLineDocument.getDocumentNumber());
        assertTrue("Document should now be final.", receivingLineDocument.getDocumentHeader().getWorkflowDocument().isFinal());
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

}

