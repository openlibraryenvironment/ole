package org.kuali.ole.select.document;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.kuali.ole.ConfigureContext;
import org.kuali.ole.KualiTestBase;
import org.kuali.ole.docstore.common.client.DocstoreRestClient;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.fixture.UserNameFixture;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderItem;
import org.kuali.ole.module.purap.document.PaymentRequestDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.module.purap.fixture.PaymentRequestDocumentFixture;
import org.kuali.ole.module.purap.fixture.PurApItemFixture;
import org.kuali.ole.module.purap.fixture.PurchaseOrderDocumentFixture;
import org.kuali.ole.module.purap.fixture.PurchaseOrderItemFixture;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.businessobject.OleRequisitionCopies;
import org.kuali.ole.select.document.service.OleCopyHelperService;
import org.kuali.ole.select.document.service.OleDocstoreHelperService;
import org.kuali.ole.select.document.service.impl.OleCopyHelperServiceImpl;
import org.kuali.ole.select.fixture.OLERequisitionCopiesFixture;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.AccountingDocumentTestUtils;
import org.kuali.ole.sys.document.workflow.WorkflowTestUtils;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.dao.DocumentDao;
import org.kuali.rice.krad.dao.NoteDao;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.service.AttachmentService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.NoteService;
import org.kuali.rice.krad.service.impl.DocumentServiceImpl;
import org.kuali.rice.krad.service.impl.NoteServiceImpl;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.kuali.ole.fixture.UserNameFixture.khuntley;

/**
 * Created with IntelliJ IDEA.
 * User: meenau
 * Date: 5/15/14
 * Time: 12:17 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Used to create and test populated PO Documents of various kinds.
 */

public class OLEPurchaseOrderChangeDocumentTest extends KualiTestBase {

    protected static PurchaseOrderService poService = null;
    protected static PurchaseOrderDocument poDocument = null;
    protected static PurchaseOrderDocument poChange = null;
    protected static DocumentServiceImpl documentService = null;
    protected static NoteService noteService;
    protected static OleDocstoreHelperService oleDocstoreHelperService = null;
    protected static OleCopyHelperServiceImpl oleCopyHelperService = null;
    private DocstoreRestClient restClient = new DocstoreRestClient();

    public void setUp() throws Exception {
        super.setUp();
        changeCurrentUser(UserNameFixture.khuntley);
        poService = SpringContext.getBean(PurchaseOrderService.class);
        // Create and route a minimally-populated basic PO document for each test.
        poDocument = buildSimpleDocument();
        documentService = (DocumentServiceImpl) SpringContext.getBean("documentService");
        documentService.setDocumentDao((DocumentDao) SpringContext.getBean("documentDao"));
        noteService = SpringContext.getBean(NoteService.class);
        oleCopyHelperService = (OleCopyHelperServiceImpl) SpringContext.getBean(OleCopyHelperService.class);
        oleDocstoreHelperService = SpringContext.getBean(OleDocstoreHelperService.class);
        poDocument.prepareForSave();
        AccountingDocumentTestUtils.routeDocument(poDocument, "PO test annotation", null, documentService);
        WorkflowTestUtils.waitForDocumentApproval(poDocument.getDocumentNumber());
    }

    public void tearDown() throws Exception {
        poDocument = null;
        super.tearDown();
    }

    /**
     * creates a simple PO document with required fields
     */
    public PurchaseOrderDocument buildSimpleDocument() throws Exception {
        return PurchaseOrderDocumentFixture.PO_ONLY_REQUIRED_FIELDS_WITH_COPIES.createPurchaseOrderDocument();
    }

    private void createAndSavePOChangeDocument(String documentType, String documentStatus) throws Exception {
        try {
            poChange = poService.createAndSavePotentialChangeDocument(
                    poDocument, documentType, documentStatus);
            poChange = (PurchaseOrderDocument) KRADServiceLocatorWeb.getDocumentService().getByDocumentHeaderId(poChange.getDocumentNumber());
            poDocument = poService.getPurchaseOrderByDocumentNumber(poDocument.getDocumentNumber());
        }
        catch (ValidationException ve) {
            throw new ValidationException(GlobalVariables.getMessageMap().toString() + ve);
        }
    }

    private void createAndSavePOSplitDocument(List<PurchaseOrderItem> newPOItems, boolean copyNotes, String splitNoteText) throws Exception {
        try {
            poDocument.setStatusCode(PurapConstants.PurchaseOrderStatuses.APPDOC_IN_PROCESS);
            poChange = poService.createAndSavePurchaseOrderSplitDocument(
                    newPOItems, poDocument, copyNotes, splitNoteText);
            poDocument = poService.getPurchaseOrderByDocumentNumber(poDocument.getDocumentNumber());
        }
        catch (ValidationException ve) {
            throw new ValidationException(GlobalVariables.getMessageMap().toString() + ve);
        }
    }

    private void createAndRoutePOChangeDocument(String documentType, String documentStatus) throws Exception {
        try {
            poChange = poService.createAndRoutePotentialChangeDocument(
                    poDocument, documentType, "unit test", new ArrayList(), documentStatus);
            poDocument = poService.getPurchaseOrderByDocumentNumber(poDocument.getDocumentNumber());
        }
        catch (ValidationException ve) {
            throw new ValidationException(GlobalVariables.getMessageMap().toString() + ve);
        }
    }

    /**
     * test case for testing PO Amend document
     */
    @Test
    public final void testAmendPurchaseOrder() throws Exception {
        createAndSavePOChangeDocument(
                PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT,
                PurapConstants.PurchaseOrderStatuses.APPDOC_AMENDMENT);
        assertMatchChangePO(poDocument, poChange);
        if (!poChange.getDocumentHeader().getWorkflowDocument().getStatus().equals("F")) {
            assertTrue(poDocument.isPurchaseOrderCurrentIndicator());
            assertTrue(poDocument.isPendingActionIndicator());
            assertTrue(poDocument.getDocumentHeader().getWorkflowDocument().getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_AMENDMENT));
            assertFalse(poChange.isPurchaseOrderCurrentIndicator());
            assertFalse(poChange.isPendingActionIndicator());
            assertTrue(poChange.getDocumentHeader().getWorkflowDocument().getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_CHANGE_IN_PROCESS));
        }
    }

    /**
     * test case for testing PO Cancel Amend document
     */
    @Test
    public final void testCancelAmendPurchaseOrder() throws Exception {
        createAndSavePOChangeDocument(
                PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT,
                PurapConstants.PurchaseOrderStatuses.APPDOC_AMENDMENT);
        assertMatchChangePO(poDocument, poChange);
        if (!poChange.getDocumentHeader().getWorkflowDocument().getStatus().equals("F")) {
            assertTrue(poDocument.isPurchaseOrderCurrentIndicator());
            assertTrue(poDocument.isPendingActionIndicator());
            assertTrue(poDocument.getDocumentHeader().getWorkflowDocument().getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_AMENDMENT));
            assertFalse(poChange.isPurchaseOrderCurrentIndicator());
            assertFalse(poChange.isPendingActionIndicator());
            assertTrue(poChange.getDocumentHeader().getWorkflowDocument().getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_CHANGE_IN_PROCESS));
        }
        SpringContext.getBean(DocumentService.class).cancelDocument(poChange, "");
        assertTrue(poChange.getDocumentHeader().getWorkflowDocument().getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_CANCELLED));
    }

    /**
     * test case for testing PO Amend document Quantity Change
     */
    @Test
    public final void testPurchaseOrderAmendQuantityChange() throws Exception {
        createAndSavePOChangeDocument(
                PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT,
                PurapConstants.PurchaseOrderStatuses.APPDOC_AMENDMENT);
        OlePurchaseOrderItem olePurchaseOrderItem = (OlePurchaseOrderItem) poChange.getItem(0);
        KualiDecimal itemQuantityBeforeAddingCopies = olePurchaseOrderItem.getItemQuantity();
        String itemLocationBeforeAddingCopies = olePurchaseOrderItem.getItemLocation();
        olePurchaseOrderItem.getCopyList().clear();
        OleRequisitionCopies oleRequisitionCopies = OLERequisitionCopiesFixture.BASIC_COPY_1.createOleRequisitionCopies();
        List<String> volChar = new ArrayList<>();
        String[] volNumbers = oleRequisitionCopies.getVolumeNumber() != null ? oleRequisitionCopies.getVolumeNumber().split(",") : new String[0];
        for (String volStr : volNumbers) {
            volChar.add(volStr);
        }
        List<OleCopy> copyList = oleCopyHelperService.setCopyValues(oleRequisitionCopies, olePurchaseOrderItem.getItemTitleId(), volChar);
        olePurchaseOrderItem.getCopyList().addAll(copyList);
        olePurchaseOrderItem.setItemNoOfParts(oleRequisitionCopies.getParts());
        olePurchaseOrderItem.getCopies().add(oleRequisitionCopies);
        AccountingDocumentTestUtils.routeDocument(poChange, "test amend annotation", null, documentService);
        WorkflowTestUtils.waitForDocumentApproval(poChange.getDocumentNumber());
        assertNotSame(itemQuantityBeforeAddingCopies.intValue(),olePurchaseOrderItem.getCopyList().size());
        assertEquals(3,olePurchaseOrderItem.getCopyList().size());
        assertNotSame(itemLocationBeforeAddingCopies, olePurchaseOrderItem.getCopyList().get(0).getLocation());
    }

    /**
     * test case for testing PO Amend document Location change
     */
    @Test
    public final void testPurchaseOrderAmendLocationChangeInPO() throws Exception {
        createAndSavePOChangeDocument(
                PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT,
                PurapConstants.PurchaseOrderStatuses.APPDOC_AMENDMENT);
        OlePurchaseOrderItem olePurchaseOrderItem = (OlePurchaseOrderItem)poChange.getItem(0);
        String itemLocationChangeBeforeUpdate = olePurchaseOrderItem.getItemLocation();
        //this itemLocationChangeFlag indicates whether the itemLocation should be updated in docstore or not.
        //if it is false, location change is reflected only in PO document and not in docstore.
        olePurchaseOrderItem.setItemLocation("PALCI");
        olePurchaseOrderItem.setItemLocationChangeFlag(false);
        AccountingDocumentTestUtils.routeDocument(poChange, "test amend annotation", null, documentService);
        WorkflowTestUtils.waitForDocumentApproval(poChange.getDocumentNumber());
        Holdings holdings = restClient.retrieveHoldings(olePurchaseOrderItem.getCopyList().get(0).getInstanceId());
        OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(holdings.getContent());
        assertEquals(itemLocationChangeBeforeUpdate,oleHoldings.getLocation().getLocationLevel().getName());
        assertNotSame("PALCI", oleHoldings.getLocation().getLocationLevel().getName());
        assertNotSame(itemLocationChangeBeforeUpdate,olePurchaseOrderItem.getItemLocation());
        assertEquals("PALCI", olePurchaseOrderItem.getItemLocation());

    }

    /**
     * test case for testing PO Amend document Location Change in Docstore
     */
    @Test
    public final void testPurchaseOrderAmendLocationChangeInDocstore() throws Exception {
        createAndSavePOChangeDocument(
                PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT,
                PurapConstants.PurchaseOrderStatuses.APPDOC_AMENDMENT);
        OlePurchaseOrderItem olePurchaseOrderItem = (OlePurchaseOrderItem)poChange.getItem(0);
        String itemLocationChangeBeforeUpdate = olePurchaseOrderItem.getItemLocation();
        //this itemLocationChangeFlag indicates whether the itemLocation should be updated in docstore or not.
        //if it is true, location change is reflected in PO document and also  in docstore.
        olePurchaseOrderItem.setItemLocation("PALCI");
        olePurchaseOrderItem.setItemLocationChangeFlag(true);
        AccountingDocumentTestUtils.routeDocument(poChange, "test amend annotation", null, documentService);
        WorkflowTestUtils.waitForDocumentApproval(poChange.getDocumentNumber());
        Holdings holdings = restClient.retrieveHoldings(olePurchaseOrderItem.getCopyList().get(0).getInstanceId());
        OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(holdings.getContent());
        assertNotSame(itemLocationChangeBeforeUpdate,oleHoldings.getLocation().getLocationLevel().getName());
        assertEquals("PALCI", oleHoldings.getLocation().getLocationLevel().getName());
        assertNotSame(itemLocationChangeBeforeUpdate,olePurchaseOrderItem.getItemLocation());
        assertEquals("PALCI",olePurchaseOrderItem.getItemLocation());
    }

    /**
     * test case for testing PO Split document
     */
    @Test
    public void testSplitPurchaseOrder() throws Exception {
        List<PurchaseOrderItem> purchaseOrderItemList = new ArrayList<>();
        purchaseOrderItemList.add((PurchaseOrderItem) PurchaseOrderItemFixture.PO_QTY_UNRESTRICTED_ITEM_2.createPurchaseOrderItem(PurApItemFixture.BASIC_QTY_ITEM_2));
        createAndSavePOSplitDocument(purchaseOrderItemList, true, "Reason for splitting.");
        // Proving that most things other than items are the same.
        assertMatchChangePO(poDocument, poChange);
        assertTrue(poDocument.getPurapDocumentIdentifier().compareTo(poChange.getPurapDocumentIdentifier()) < 0);
        // Neither the original nor the resulting PO may have no items.
        assertFalse(poChange.getItems().size() == 1);
        assertTrue(poDocument.getItems().size() == 1);
        List<PurchaseOrderItem> splitPOItems = (List<PurchaseOrderItem>)poChange.getItems();
        // Check renumbering.
        int i = 0;
        for (PurchaseOrderItem splitPOItem : splitPOItems ) {
            if (splitPOItem.getItemType().isLineItemIndicator()) {
                assertTrue(splitPOItem.getItemLineNumber().intValue() == ++i);
            }
        }
    }

    /**
     * test case for testing PO Close document
     */
    @Test
    public final void testPurchaseOrderClose() throws Exception {
        // There must be a PREQ against this PO in order to close this PO.

        PaymentRequestDocument preq = PaymentRequestDocumentFixture.PREQ_FOR_PO_CLOSE_DOC.createPaymentRequestDocument();
        preq.setPurchaseOrderIdentifier(poDocument.getPurapDocumentIdentifier());
        AccountingDocumentTestUtils.saveDocument(preq, documentService);
        createAndRoutePOChangeDocument(
                PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_CLOSE_DOCUMENT,
                PurapConstants.PurchaseOrderStatuses.APPDOC_PENDING_CLOSE);
        assertMatchChangePO(poDocument, poChange);
        assertFalse(poDocument.isPurchaseOrderCurrentIndicator());
        assertFalse(poDocument.isPendingActionIndicator());
        assertTrue(poDocument.getDocumentHeader().getWorkflowDocument().getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_RETIRED_VERSION));
        assertTrue(poChange.isPurchaseOrderCurrentIndicator());
        assertFalse(poChange.isPendingActionIndicator());
        assertTrue(poChange.getDocumentHeader().getWorkflowDocument().getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_CLOSED));
    }

    /**
     * test case for testing PO Payment Hold document
     */
    @Test
    public final void testPurchaseOrderPaymentHold() throws Exception {
        createAndRoutePOChangeDocument(
                PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_PAYMENT_HOLD_DOCUMENT,
                PurapConstants.PurchaseOrderStatuses.APPDOC_PENDING_PAYMENT_HOLD);
        assertMatchChangePO(poDocument, poChange);
        assertFalse(poDocument.isPurchaseOrderCurrentIndicator());
        assertFalse(poDocument.isPendingActionIndicator());
        assertTrue(poDocument.getDocumentHeader().getWorkflowDocument().getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_RETIRED_VERSION));
        assertTrue(poChange.isPurchaseOrderCurrentIndicator());
        assertFalse(poChange.isPendingActionIndicator());
        assertTrue(poChange.getDocumentHeader().getWorkflowDocument().getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_PAYMENT_HOLD));
    }

    /**
     * test case for testing PO Remove document
     */
    @Test
    public final void testPurchaseOrderRemoveHold() throws Exception {
        poDocument.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus((PurapConstants.PurchaseOrderStatuses.APPDOC_PENDING_PAYMENT_HOLD));
        poDocument.refreshNonUpdateableReferences();
        createAndRoutePOChangeDocument(
                PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REMOVE_HOLD_DOCUMENT,
                PurapConstants.PurchaseOrderStatuses.APPDOC_PENDING_PAYMENT_HOLD);
        assertMatchChangePO(poDocument, poChange);
        assertFalse(poDocument.isPurchaseOrderCurrentIndicator());
        assertFalse(poDocument.isPendingActionIndicator());
        assertTrue(poDocument.getDocumentHeader().getWorkflowDocument().getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_RETIRED_VERSION));
        assertTrue(poChange.isPurchaseOrderCurrentIndicator());
        assertFalse(poChange.isPendingActionIndicator());
        assertTrue(poChange.getDocumentHeader().getWorkflowDocument().getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN));
    }

    /**
     * test case for testing PO Reopen document
     */
    @Test
    public final void testPurchaseOrderReopen() throws Exception {
        poDocument.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus((PurapConstants.PurchaseOrderStatuses.APPDOC_CLOSED));
        poDocument.refreshNonUpdateableReferences();
        OlePurchaseOrderDocument olePurchaseOrderDocument = (OlePurchaseOrderDocument)poDocument;
        OlePurchaseOrderItem olePurchaseOrderItem = (OlePurchaseOrderItem) olePurchaseOrderDocument.getItems().get(0);
        Item item = restClient.retrieveItem(olePurchaseOrderItem.getCopyList().get(0).getItemUUID());
        item.setStaffOnly(true);
        restClient.updateItem(item);
        createAndRoutePOChangeDocument(
                PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REOPEN_DOCUMENT,
                PurapConstants.PurchaseOrderStatuses.APPDOC_PENDING_REOPEN);
        assertMatchChangePO(poDocument, poChange);
        assertFalse(poDocument.isPurchaseOrderCurrentIndicator());
        assertFalse(poDocument.isPendingActionIndicator());
        assertTrue(poDocument.getDocumentHeader().getWorkflowDocument().getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_RETIRED_VERSION));
        OlePurchaseOrderReopenDocument olePurchaseOrderReopenDocument = (OlePurchaseOrderReopenDocument)poChange;
        olePurchaseOrderItem = (OlePurchaseOrderItem) olePurchaseOrderReopenDocument.getItems().get(0);
        item = restClient.retrieveItem(olePurchaseOrderItem.getCopyList().get(0).getItemUUID());
        assertFalse(item.isStaffOnly());
        assertTrue(poChange.isPurchaseOrderCurrentIndicator());
        assertFalse(poChange.isPendingActionIndicator());
        assertTrue(poChange.getDocumentHeader().getWorkflowDocument().getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN));
    }

    /**
     * test case for testing PO Void document
     */
    @Test
    public final void testPurchaseOrderVoid() throws Exception {
        String voidNoteText = PurapConstants.PODocumentsStrings.VOID_NOTE_PREFIX + "Void PO test";
        Note voidNote = documentService.createNoteFromDocument(poDocument, voidNoteText);
        poDocument.addNote(voidNote);
        noteService.save(voidNote);
        createAndRoutePOChangeDocument(
                PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_VOID_DOCUMENT,
                PurapConstants.PurchaseOrderStatuses.APPDOC_PENDING_VOID);
        assertMatchChangePO(poDocument, poChange);
        assertFalse(poDocument.isPurchaseOrderCurrentIndicator());
        assertFalse(poDocument.isPendingActionIndicator());
        assertTrue(poDocument.getDocumentHeader().getWorkflowDocument().getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_RETIRED_VERSION));
        OlePurchaseOrderVoidDocument olePurchaseOrderVoidDocument = (OlePurchaseOrderVoidDocument)poChange;
        OlePurchaseOrderItem olePurchaseOrderItem = (OlePurchaseOrderItem) olePurchaseOrderVoidDocument.getItems().get(0);
        Item item = restClient.retrieveItem(olePurchaseOrderItem.getCopyList().get(0).getItemUUID());
        org.kuali.ole.docstore.common.document.content.instance.Item itemContent=new ItemOlemlRecordProcessor().fromXML(item.getContent());
        assertTrue(item.isStaffOnly());
        assertEquals(OLEConstants.NON_PUBLIC, itemContent.getNote().get(0).getType());
        assertEquals("Void PO test", itemContent.getNote().get(0).getValue());
        assertTrue(poChange.isPurchaseOrderCurrentIndicator());
        assertFalse(poChange.isPendingActionIndicator());
        assertTrue(poChange.getDocumentHeader().getWorkflowDocument().getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_VOID));
    }

    /**
     * Matches an existing Purchase Order Document with a change PO Document newly generated from it;
     * Fails the assertion if any of the copied persistent fields don't match.
     */
    public static void assertMatchChangePO(PurchaseOrderDocument doc1, PurchaseOrderDocument doc2) {
        // match posting year
        if (StringUtils.isNotBlank(doc1.getPostingPeriodCode()) && StringUtils.isNotBlank(doc2.getPostingPeriodCode())) {
            assertEquals(doc1.getPostingPeriodCode(), doc2.getPostingPeriodCode());
        }
        assertEquals(doc1.getPostingYear(), doc2.getPostingYear());

        // match important fields in PO
        assertEquals(doc1.getVendorHeaderGeneratedIdentifier(), doc2.getVendorHeaderGeneratedIdentifier());
        assertEquals(doc1.getVendorDetailAssignedIdentifier(), doc2.getVendorDetailAssignedIdentifier());
        assertEquals(doc1.getVendorName(), doc2.getVendorName());
        assertEquals(doc1.getVendorNumber(), doc2.getVendorNumber());

        assertEquals(doc1.getChartOfAccountsCode(), doc2.getChartOfAccountsCode());
        assertEquals(doc1.getOrganizationCode(), doc2.getOrganizationCode());
        assertEquals(doc1.getDeliveryCampusCode(), doc2.getDeliveryCampusCode());
        assertEquals(doc1.getDeliveryRequiredDate(), doc2.getDeliveryRequiredDate());
        assertEquals(doc1.getRequestorPersonName(), doc2.getRequestorPersonName());
        assertEquals(doc1.getContractManagerCode(), doc2.getContractManagerCode());
        assertEquals(doc1.getVendorContractName(), doc2.getVendorContractName());
        assertEquals(doc1.getPurchaseOrderAutomaticIndicator(), doc2.getPurchaseOrderAutomaticIndicator());
        assertEquals(doc1.getPurchaseOrderTransmissionMethodCode(), doc2.getPurchaseOrderTransmissionMethodCode());

        assertEquals(doc1.getRequisitionIdentifier(), doc2.getRequisitionIdentifier());
        assertEquals(doc1.getPurchaseOrderPreviousIdentifier(), doc2.getPurchaseOrderPreviousIdentifier());

    }

}
