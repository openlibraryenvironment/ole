package org.kuali.ole.select.document;

import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.ole.KualiTestBase;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.docstore.common.client.DocstoreRestClient;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.EHoldings;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.fixture.UserNameFixture;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.fixture.PurchaseOrderDocumentFixture;
import org.kuali.ole.select.bo.OLEEditorResponse;
import org.kuali.ole.select.businessobject.OleDocstoreResponse;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.fixture.OLEEResourceRecordDocumentFixture;
import org.kuali.ole.service.OLEEResourceSearchService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.AccountingDocumentTestUtils;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.dao.DocumentDao;
import org.kuali.rice.krad.service.impl.DocumentServiceImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.kuali.ole.fixture.UserNameFixture.khuntley;

/**
 * Created with IntelliJ IDEA.
 * User: meenau
 * Date: 5/26/14
 * Time: 12:29 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Used to create and test populated EResource Documents of various kinds.
 */

public class OLEEResourceRecordDocumentTest extends KualiTestBase {

    protected static DocumentServiceImpl documentService = null;
    protected OlePurchaseOrderDocument olePurchaseOrderDocument = null;
    private DocstoreRestClient restClient = new DocstoreRestClient();
    private OLEEResourceSearchService oleEResourceSearchService = null;

    public void setUp() throws Exception {
        super.setUp();
        documentService = (DocumentServiceImpl) SpringContext.getBean("documentService");
        documentService.setDocumentDao((DocumentDao) SpringContext.getBean("documentDao"));
        changeCurrentUser(UserNameFixture.khuntley);
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * test case for testing save document
     */
    @Test
    public final void testSaveDocument() throws Exception {
        OLEEResourceRecordDocument oleeResourceRecordDocument = buildSimpleEResourceDocument();
        documentService.saveDocument(oleeResourceRecordDocument);
        OLEEResourceRecordDocument result = (OLEEResourceRecordDocument) documentService.getByDocumentHeaderId(oleeResourceRecordDocument.getDocumentNumber());
        assertTrue("Document should now be saved.",result.getDocumentHeader().getWorkflowDocument().getStatus().getLabel().equalsIgnoreCase("SAVED"));
        assertEquals(oleeResourceRecordDocument.getDocumentNumber(), result.getDocumentNumber());
    }

    /**
     * test case for testing route document
     */
    @Test
    public final void testRouteDocument() throws Exception {
        OLEEResourceRecordDocument oleeResourceRecordDocument = buildSimpleEResourceDocument();
        documentService.routeDocument(oleeResourceRecordDocument, "E-Resource Document", null);
        OLEEResourceRecordDocument result = (OLEEResourceRecordDocument) documentService.getByDocumentHeaderId(oleeResourceRecordDocument.getDocumentNumber());
        assertTrue(result.getDocumentHeader().getWorkflowDocument().getStatus().getLabel().equalsIgnoreCase("ENROUTE"));
        assertEquals(oleeResourceRecordDocument.getDocumentNumber(), result.getDocumentNumber());
    }


    /**
     * test case for testing EResource document approval
     */
    @Test
    public final void testRouteDocumentToFinal() throws Exception {
        OLEEResourceRecordDocument oleeResourceRecordDocument = buildSimpleEResourceDocument();
        documentService.routeDocument(oleeResourceRecordDocument,"E-Resource Document",null);
        OLEEResourceRecordDocument result = (OLEEResourceRecordDocument) documentService.getByDocumentHeaderId(oleeResourceRecordDocument.getDocumentNumber());
        assertTrue(result.getDocumentHeader().getWorkflowDocument().getStatus().getLabel().equalsIgnoreCase("ENROUTE"));
        assertEquals(oleeResourceRecordDocument.getDocumentNumber(), result.getDocumentNumber());

        changeCurrentUser(UserNameFixture.admin);
        result = (OLEEResourceRecordDocument) documentService.getByDocumentHeaderId(oleeResourceRecordDocument.getDocumentNumber());
        documentService.approveDocument(result,"EResource Approve Document",null);
        assertTrue(result.getDocumentHeader().getWorkflowDocument().getStatus().getLabel().equalsIgnoreCase("ENROUTE"));

        changeCurrentUser(UserNameFixture.admin1);
        result = (OLEEResourceRecordDocument) documentService.getByDocumentHeaderId(oleeResourceRecordDocument.getDocumentNumber());
        documentService.approveDocument(result,"EResource Approve Document",null);

        changeCurrentUser(khuntley);
        result = (OLEEResourceRecordDocument) documentService.getByDocumentHeaderId(oleeResourceRecordDocument.getDocumentNumber());
        assertTrue(result.getDocumentHeader().getWorkflowDocument().getStatus().getLabel().equalsIgnoreCase("FINAL"));
    }


    /**
     * test case for testing createEInstance
     */
    @Test
    public final void testCreateEInstance() throws Exception {
        OLEEResourceRecordDocument oleeResourceRecordDocument = buildSimpleEResourceDocument();
        documentService.saveDocument(oleeResourceRecordDocument);
        OLEEResourceRecordDocument result = (OLEEResourceRecordDocument) documentService.getByDocumentHeaderId(oleeResourceRecordDocument.getDocumentNumber());
        assertTrue(result.getDocumentHeader().getWorkflowDocument().getStatus().getLabel().equalsIgnoreCase("SAVED"));

        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/org/kuali/ole/BibMarc.xml").toURI());
            input = FileUtils.readFileToString(file);

        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Bib bibMarc = new Bib();
        bibMarc = (Bib) bibMarc.deserialize(input);
        restClient.createBib(bibMarc);

        try {
            file = new File(getClass().getResource("/org/kuali/ole/EHoldings.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Holdings holdings = new EHoldings();
        holdings = (EHoldings) holdings.deserialize(input);
        holdings.setBib(bibMarc);
        restClient.createHoldings(holdings);

        HashMap<String, OLEEditorResponse> oleEditorResponseMap = new HashMap<>();
        OLEEditorResponse oleEditorResponse = new OLEEditorResponse();
        oleEditorResponse.setLinkedInstanceId(holdings.getId());
        oleEditorResponseMap.put(oleeResourceRecordDocument.getDocumentNumber(),oleEditorResponse);
        OleDocstoreResponse.getInstance().setEditorResponse(oleEditorResponseMap);
        oleeResourceRecordDocument.setSelectInstance(OLEConstants.OLEEResourceRecord.CREATE_NEW_INSTANCE);

        oleEResourceSearchService = GlobalResourceLoader.getService(OLEConstants.OLEEResourceRecord.ERESOURSE_SEARCH_SERVICE);
        oleEResourceSearchService.getNewInstance(oleeResourceRecordDocument,oleeResourceRecordDocument.getDocumentNumber());
        assertEquals(1,oleeResourceRecordDocument.getOleERSInstances().size());
        assertEquals(holdings.getId(), oleeResourceRecordDocument.getOleERSInstances().get(0).getInstanceId());
        assertEquals("platformId", oleeResourceRecordDocument.getOleERSInstances().get(0).getPlatformId());
        assertEquals("public",oleeResourceRecordDocument.getOleERSInstances().get(0).getSubscriptionStatus());
        assertEquals("05/15/2014 - 05/20/2014", oleeResourceRecordDocument.getOleERSInstances().get(0).getInstanceHoldings());
        assertEquals("publisher", oleeResourceRecordDocument.getOleERSInstances().get(0).getInstancePublisher());
    }

    /**
     * test case for testing PO and EResource link
     */
    @Test
    public final void testPOForEResource() throws Exception {
        //create a simple po document for testing PO and EResource link.
        olePurchaseOrderDocument = (OlePurchaseOrderDocument) buildSimplePODocument();
        AccountingDocumentTestUtils.routeDocument(olePurchaseOrderDocument, "test po annotation", null, documentService);
        assertEquals(1,olePurchaseOrderDocument.getItems().size());
        OlePurchaseOrderItem olePurchaseOrderItem = (OlePurchaseOrderItem) olePurchaseOrderDocument.getItem(0);
        assertEquals(1,olePurchaseOrderItem.getCopyList().size());
        String holdingId = olePurchaseOrderItem.getCopyList().get(0).getInstanceId();

        OLEEResourceRecordDocument oleeResourceRecordDocument = buildSimpleEResourceDocument();
        documentService.saveDocument(oleeResourceRecordDocument);
        OLEEResourceRecordDocument result = (OLEEResourceRecordDocument) documentService.getByDocumentHeaderId(oleeResourceRecordDocument.getDocumentNumber());
        assertTrue(result.getDocumentHeader().getWorkflowDocument().getStatus().getLabel().equalsIgnoreCase("SAVED"));

        HashMap<String, OLEEditorResponse> oleEditorResponseMap = new HashMap<>();
        OLEEditorResponse oleEditorResponse = new OLEEditorResponse();
        oleEditorResponse.setLinkedInstanceId(holdingId);
        oleEditorResponseMap.put(oleeResourceRecordDocument.getDocumentNumber(),oleEditorResponse);
        OleDocstoreResponse.getInstance().setEditorResponse(oleEditorResponseMap);
        oleeResourceRecordDocument.setSelectInstance(OLEConstants.OLEEResourceRecord.LINK_EXIST_INSTANCE);

        oleEResourceSearchService = GlobalResourceLoader.getService(OLEConstants.OLEEResourceRecord.ERESOURSE_SEARCH_SERVICE);
        oleEResourceSearchService.getNewInstance(oleeResourceRecordDocument,oleeResourceRecordDocument.getDocumentNumber());
        assertEquals(1,oleeResourceRecordDocument.getOleERSInstances().size());
        assertEquals(holdingId, oleeResourceRecordDocument.getOleERSInstances().get(0).getInstanceId());

        oleEResourceSearchService.getPOInvoiceForERS(oleeResourceRecordDocument);
        assertEquals(olePurchaseOrderDocument.getPurapDocumentIdentifier(), oleeResourceRecordDocument.getOleERSPOItems().get(0).getOlePOItemId());
    }

    /**
     * creates a simple EResource document with required fields
     */
    public OLEEResourceRecordDocument buildSimpleEResourceDocument() throws Exception {
        return OLEEResourceRecordDocumentFixture.ERESOURCE_ONLY_REQUIRED_FIELDS.createOLEEResourceDocument();
    }

    /**
     * creates a simple PO document with required fields
     */
    public PurchaseOrderDocument buildSimplePODocument() throws Exception {
        return PurchaseOrderDocumentFixture.ERES_PO_ONLY_REQUIRED_FIELDS_WITH_COPIES.createPurchaseOrderDocument();
    }



}
