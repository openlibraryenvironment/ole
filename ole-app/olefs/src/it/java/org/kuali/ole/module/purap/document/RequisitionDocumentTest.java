/*
 * Copyright 2007 The Kuali Foundation
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

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.kuali.ole.fixture.UserNameFixture.sterner;
import static org.kuali.ole.fixture.UserNameFixture.jkitchen;
import static org.kuali.ole.fixture.UserNameFixture.khuntley;
import static org.kuali.ole.fixture.UserNameFixture.rjweiss;
import static org.kuali.ole.fixture.UserNameFixture.jgerhart;
import static org.kuali.ole.fixture.UserNameFixture.rorenfro;
import static org.kuali.ole.sys.document.AccountingDocumentTestUtils.testGetNewDocument_byDocumentClass;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.kuali.ole.DocumentTestUtils;
import org.kuali.ole.KualiTestBase;
import org.kuali.ole.fixture.UserNameFixture;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurchasingItem;
import org.kuali.ole.module.purap.businessobject.RequisitionAccount;
import org.kuali.ole.module.purap.businessobject.RequisitionItem;
import org.kuali.ole.module.purap.fixture.RequisitionDocumentFixture;
import org.kuali.ole.module.purap.fixture.RequisitionDocumentWithCommodityCodeFixture;
import org.kuali.ole.module.purap.fixture.RequisitionItemFixture;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.document.OleRequisitionDocument;

import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.AccountingDocumentTestUtils;
import org.kuali.ole.sys.document.workflow.WorkflowTestUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.kns.service.TransactionalDocumentDictionaryService;
import org.kuali.rice.krad.dao.DocumentDao;
import org.kuali.rice.krad.rules.rule.event.RouteDocumentEvent;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KualiRuleService;
import org.kuali.rice.krad.service.impl.DocumentServiceImpl;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.ksb.util.KSBConstants;

/**
 * Used to create and test populated Requisition Documents of various kinds.
 */
public class RequisitionDocumentTest extends KualiTestBase {
    public static final Class<OleRequisitionDocument> DOCUMENT_CLASS = OleRequisitionDocument.class;
    private static final String ACCOUNT_REVIEW = "Account";
    private static final String ORGANIZATION = "Organization";
    private static final String COMMODITY_CODE_REVIEW = "Commodity";

    private RequisitionDocument requisitionDocument = null;
    protected static DocumentServiceImpl documentService = null;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        documentService = (DocumentServiceImpl) SpringContext.getBean("documentService");
        documentService.setDocumentDao((DocumentDao) SpringContext.getBean("documentDao"));
        ConfigContext.getCurrentContextConfig().putProperty(KSBConstants.Config.MESSAGE_DELIVERY, KSBConstants.MESSAGING_SYNCHRONOUS );
        changeCurrentUser(UserNameFixture.khuntley);
    }

    @Override
    public void tearDown() throws Exception {
        requisitionDocument = null;
        ConfigContext.getCurrentContextConfig().putProperty(KSBConstants.Config.MESSAGE_DELIVERY, "a"+KSBConstants.MESSAGING_SYNCHRONOUS );
        super.tearDown();
    }


    private List<RequisitionItemFixture> getItemParametersFromFixtures() {
        List<RequisitionItemFixture> list = new ArrayList<RequisitionItemFixture>();
        list.add(RequisitionItemFixture.REQ_ITEM_NO_APO);
        return list;
    }

    private int getExpectedPrePeCount() {
        return 0;
    }

    @Test
    public final void testAddItem() throws Exception {
        List<PurchasingItem> items = new ArrayList<PurchasingItem>();
        items.add(RequisitionItemFixture.REQ_ITEM_NO_APO.createRequisitionItem());

        int expectedItemTotal = items.size();
        PurchasingDocumentTestUtils.testAddItem(DocumentTestUtils.createDocument(SpringContext.getBean(DocumentService.class), DOCUMENT_CLASS), items, expectedItemTotal);
    }

    @Test
    public final void testAddItemWithCopy() throws Exception {
        List<OleRequisitionItem> items = new ArrayList<OleRequisitionItem>();
        items.add(RequisitionItemFixture.ITEM_WITH_MULTI_COPIES_MULTI_PARTS.createRequisitionItem());

        KualiDecimal expectedCopyTotal = items.get(0).getItemQuantity().multiply(items.get(0).getItemNoOfParts().kualiDecimalValue());
        AccountingDocumentTestUtils.testAddItemWithCopy(DocumentTestUtils.createDocument(SpringContext.getBean(DocumentService.class), DOCUMENT_CLASS), items, expectedCopyTotal.intValue());
    }

    @Test
    public final void testGetNewDocument() throws Exception {
        testGetNewDocument_byDocumentClass(DOCUMENT_CLASS, SpringContext.getBean(DocumentService.class));
    }

    @Test
    public final void testConvertIntoCopy_copyDisallowed() throws Exception {
        AccountingDocumentTestUtils.testConvertIntoCopy_copyDisallowed(buildSimpleDocument(), SpringContext.getBean(DataDictionaryService.class));

    }

    @Test
    public final void testConvertIntoErrorCorrection_documentAlreadyCorrected() throws Exception {
        AccountingDocumentTestUtils.testConvertIntoErrorCorrection_documentAlreadyCorrected(buildSimpleDocument(), SpringContext.getBean(TransactionalDocumentDictionaryService.class));
    }

    @Test
    public final void testConvertIntoErrorCorrection() throws Exception {
        requisitionDocument = buildSimpleDocument();
        AccountingDocumentTestUtils.testConvertIntoErrorCorrection(requisitionDocument, getExpectedPrePeCount(), documentService, SpringContext.getBean(TransactionalDocumentDictionaryService.class));
    }

    @Test
    public final void testRouteDocument() throws Exception {
        requisitionDocument = buildSimpleDocument();
        AccountingDocumentTestUtils.testRouteDocument(requisitionDocument, documentService);
    }

    @Test
    public final void testSaveDocument() throws Exception {
        requisitionDocument = buildSimpleDocument();
        AccountingDocumentTestUtils.testSaveDocument(requisitionDocument, documentService);
    }

    @Test
    public final void testSaveDocumentWithItemDeletion() throws Exception {
        requisitionDocument = buildSimpleDocument();
        AccountingDocumentTestUtils.testSaveDocument(requisitionDocument, documentService);
        List<RequisitionItem> items = requisitionDocument.getItems();
        RequisitionItem item = items.get(0);
        List<PurApAccountingLine> accounts = item.getSourceAccountingLines();
        RequisitionAccount account = (RequisitionAccount)item.getSourceAccountingLine(0);

        requisitionDocument.deleteItem(0);
        AccountingDocumentTestUtils.testSaveDocument(requisitionDocument, documentService);
    }

    @Test
    public final void testRouteSavedDocumentWithAccountDeletion() throws Exception {
        requisitionDocument = buildComplexDocument();
        List<RequisitionItem> items = requisitionDocument.getItems();
        RequisitionItem item = items.get(0);
        List<PurApAccountingLine> accounts = item.getSourceAccountingLines();
        AccountingDocumentTestUtils.testSaveDocument(requisitionDocument, documentService);
        requisitionDocument = (RequisitionDocument) documentService.getByDocumentHeaderId(requisitionDocument.getDocumentNumber());
        RequisitionAccount account = (RequisitionAccount)item.getSourceAccountingLine(0);
        accounts.remove(0);
        accounts.get(0).setAccountLinePercent(new BigDecimal("100"));

        AccountingDocumentTestUtils.testRouteDocument(requisitionDocument, documentService);
    }

    @Test
    public final void testConvertIntoCopy() throws Exception {
        requisitionDocument = buildSimpleDocument();
        AccountingDocumentTestUtils.testConvertIntoCopy(requisitionDocument, documentService, getExpectedPrePeCount());
    }

    @Test
    public final void testRouteDocumentToFinal() throws Exception {
        requisitionDocument = RequisitionDocumentFixture.REQ_NO_APO_VALID.createRequisitionDocument();
        final String docId = requisitionDocument.getDocumentNumber();
        AccountingDocumentTestUtils.routeDocument(requisitionDocument, documentService);
        /*WorkflowTestUtils.waitForNodeChange(requisitionDocument.getDocumentHeader().getWorkflowDocument(), ACCOUNT_REVIEW);

        // the document should now be routed to the Fiscal Officer
        changeCurrentUser(sterner);
        requisitionDocument = (RequisitionDocument) SpringContext.getBean(DocumentService.class).getByDocumentHeaderId(docId);
        assertTrue("At incorrect node.", WorkflowTestUtils.isAtNode(requisitionDocument, ACCOUNT_REVIEW));
        assertTrue("Document should be enroute.", requisitionDocument.getDocumentHeader().getWorkflowDocument().isEnroute());
        assertTrue("sterner should have an approve request.", requisitionDocument.getDocumentHeader().getWorkflowDocument().isApprovalRequested());
        SpringContext.getBean(DocumentService.class).approveDocument(requisitionDocument, "Test approving as sterner", null);

        changeCurrentUser(jgerhart);
        requisitionDocument = (RequisitionDocument)documentService.getByDocumentHeaderId(docId);
        documentService.acknowledgeDocument(requisitionDocument, "Acknowledging as jgerhart", null);

        WorkflowTestUtils.waitForDocumentApproval(requisitionDocument.getDocumentNumber());
        */

        changeCurrentUser(khuntley);
        requisitionDocument = (RequisitionDocument) documentService.getByDocumentHeaderId(docId);
        assertTrue("Document should now be final.", requisitionDocument.getDocumentHeader().getWorkflowDocument().isFinal());
    }

    @Test
    public final void testCreateAPOAlternateRequisition() throws Exception {
        RequisitionDocument altAPORequisition = RequisitionDocumentFixture.REQ_ALTERNATE_APO.createRequisitionDocument();
        AccountingDocumentTestUtils.testSaveDocument(altAPORequisition, documentService);
    }

    /*@Test
    public final void testRouteDocumentToFinalWithBasicActiveCommodityCode() throws Exception {
        requisitionDocument = RequisitionDocumentWithCommodityCodeFixture.REQ_VALID_ACTIVE_COMMODITY_CODE.createRequisitionDocument();
        final String docId = requisitionDocument.getDocumentNumber();
        AccountingDocumentTestUtils.routeDocument(requisitionDocument, SpringContext.getBean(DocumentService.class));
        WorkflowTestUtils.waitForNodeChange(requisitionDocument.getDocumentHeader().getWorkflowDocument(), ACCOUNT_REVIEW);

        // the document should now be routed to rorenfro as Fiscal Officer
        changeCurrentUser(sterner);
        requisitionDocument = (RequisitionDocument) SpringContext.getBean(DocumentService.class).getByDocumentHeaderId(docId);
        assertTrue("At incorrect node.", WorkflowTestUtils.isAtNode(requisitionDocument, ACCOUNT_REVIEW));
        assertTrue("Document should be enroute.", requisitionDocument.getDocumentHeader().getWorkflowDocument().isEnroute());
        assertTrue("sterner should have an approve request.", requisitionDocument.getDocumentHeader().getWorkflowDocument().isApprovalRequested());
        SpringContext.getBean(DocumentService.class).approveDocument(requisitionDocument, "Test approving as sterner", null);

        // the document should now be routed to Awaiting Commodity Code approval which is to the PA_PUR_COMM_CODE workgroup,
        // we'll use jkitchen as the user.
        changeCurrentUser(jkitchen);
        requisitionDocument = (RequisitionDocument) SpringContext.getBean(DocumentService.class).getByDocumentHeaderId(docId);
        WorkflowDocument workflowDocument = requisitionDocument.getDocumentHeader().getWorkflowDocument();
        //assertTrue("At incorrect node.", WorkflowTestUtils.isAtNode(requisitionDocument, COMMODITY_CODE_REVIEW));
        //assertTrue("Document should be enroute.", workflowDocument.isEnroute());
        //assertTrue("jkitchen should have an approve request.", workflowDocument.isApprovalRequested());
        //SpringContext.getBean(DocumentService.class).approveDocument(requisitionDocument, "Test approving as jkitchen", null);

        changeCurrentUser(jgerhart);
        requisitionDocument = (RequisitionDocument) SpringContext.getBean(DocumentService.class).getByDocumentHeaderId(docId);
        SpringContext.getBean(DocumentService.class).acknowledgeDocument(requisitionDocument, "Acknowledging as jgerhart", null);

        WorkflowTestUtils.waitForDocumentApproval(requisitionDocument.getDocumentNumber());

        changeCurrentUser(khuntley);
        requisitionDocument = (RequisitionDocument) SpringContext.getBean(DocumentService.class).getByDocumentHeaderId(docId);
        assertTrue("Document should now be final.", requisitionDocument.getDocumentHeader().getWorkflowDocument().isFinal());
    }*/

    private RequisitionDocument buildSimpleDocument() throws Exception {
        return RequisitionDocumentFixture.REQ_ONLY_REQUIRED_FIELDS.createRequisitionDocument();
    }

    private RequisitionDocument buildComplexDocument() throws Exception {
        RequisitionDocument complexRequisitionDocument = RequisitionDocumentFixture.REQ_ONLY_REQUIRED_FIELDS_MULTIPLE_ACCOUNTS.createRequisitionDocument();

        return complexRequisitionDocument;
    }

    public void testRouteBrokenDocument_ItemQuantityBased_NoQuantity() {
        requisitionDocument = RequisitionDocumentFixture.REQ_INVALID_ITEM_QUANTITY_BASED_NO_QUANTITY.createRequisitionDocument();
        SpringContext.getBean(KualiRuleService.class).applyRules(new RouteDocumentEvent(requisitionDocument));
        assertFalse(GlobalVariables.getMessageMap().hasNoErrors());
    }

    private UserNameFixture getInitialUserName() {
        return rjweiss;
    }

    protected UserNameFixture getTestUserName() {
        return rorenfro;
    }

    // create document fixture
}

