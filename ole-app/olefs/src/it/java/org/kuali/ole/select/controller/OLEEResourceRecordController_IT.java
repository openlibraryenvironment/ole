package org.kuali.ole.select.controller;


import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.ole.KualiTestBase;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.docstore.common.client.DocstoreRestClient;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.EHoldings;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.fixture.UserNameFixture;
import org.kuali.ole.select.bo.OLEEditorResponse;
import org.kuali.ole.select.businessobject.OleDocstoreResponse;
import org.kuali.ole.select.controller.OLEEResourceRecordController;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.select.fixture.OLEEResourceRecordDocumentFixture;
import org.kuali.ole.select.form.OLEEResourceRecordForm;
import org.kuali.ole.service.OLEEResourceSearchService;
import org.kuali.ole.service.OleLicenseRequestWebService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.dao.DocumentDao;
import org.kuali.rice.krad.service.impl.DocumentServiceImpl;
import org.kuali.rice.krad.web.form.TransactionalDocumentFormBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.kuali.ole.fixture.UserNameFixture.khuntley;

/**
 * Created with IntelliJ IDEA.
 * User: meenau
 * Date: 5/29/14
 * Time: 1:28 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This test case tests the relation between EResource document and License Request
 */

public class OLEEResourceRecordController_IT extends KualiTestBase {

    @Mock
    private OLEEResourceRecordForm mockUifFormBase;
    @Mock
    private BindingResult mockBindingResult;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private ModelAndView mockModelView;

    public  MockOleEResourceRecordDocumentController mockOleEResourceRecordDocumentController;
    protected static DocumentServiceImpl documentService = null;
    private DocstoreRestClient restClient = new DocstoreRestClient();
    private OLEEResourceSearchService oleEResourceSearchService = null;
    private OleLicenseRequestWebService oleLicenseRequestWebService = null;

    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        mockOleEResourceRecordDocumentController = new MockOleEResourceRecordDocumentController();
        documentService = (DocumentServiceImpl) SpringContext.getBean("documentService");
        documentService.setDocumentDao((DocumentDao) SpringContext.getBean("documentDao"));
        oleLicenseRequestWebService = GlobalResourceLoader.getService(OLEConstants.OleLicenseRequest.HELPER_SERVICE);
        changeCurrentUser(UserNameFixture.khuntley);
    }

    /**
     * test case for testing relation between EResource and License Request.
     */
    @Test
    @Transactional
    public void testLicenseForEResource() throws Exception {
        mockUifFormBase = new OLEEResourceRecordForm();
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

        oleEResourceSearchService.getNewInstance(oleeResourceRecordDocument, oleeResourceRecordDocument.getDocumentNumber());
        mockUifFormBase.setDocument(oleeResourceRecordDocument);
        ModelAndView modelAndView =
                mockOleEResourceRecordDocumentController.performCreateLicenseRequest(mockUifFormBase, mockBindingResult, mockRequest, mockResponse);
        assertNotNull(modelAndView);
        OLEEResourceRecordForm oleeResourceRecordForm = mockUifFormBase;
        oleeResourceRecordDocument = (OLEEResourceRecordDocument) oleeResourceRecordForm.getDocument();
        assertEquals(oleeResourceRecordDocument.getOleERSLicenseRequests().size(), 1);
        assertEquals(oleeResourceRecordDocument.getOleERSLicenseRequests().get(0).getOleLicenseRequestBo().geteResourceDocNumber(),oleeResourceRecordDocument.getDocumentNumber());
    }

    /**
     * creates a simple EResource document with required fields
     */
    public OLEEResourceRecordDocument buildSimpleEResourceDocument() throws Exception {
        return OLEEResourceRecordDocumentFixture.ERESOURCE_ONLY_REQUIRED_FIELDS.createOLEEResourceDocument();
    }

    private class MockOleEResourceRecordDocumentController extends OLEEResourceRecordController {

    }
}