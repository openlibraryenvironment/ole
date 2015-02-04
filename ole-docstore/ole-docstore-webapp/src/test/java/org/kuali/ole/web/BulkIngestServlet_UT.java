package org.kuali.ole.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.base.BaseTestCase;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.File;


/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/15/12
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class BulkIngestServlet_UT
        extends BaseTestCase {
    private Logger                  logger       = LoggerFactory.getLogger(BulkIngestServlet_UT.class);
    private MockHttpServletRequest  mockRequest  = new MockHttpServletRequest();
    private MockHttpServletResponse mockResponse = new MockHttpServletResponse();
    BulkIngestServlet bulkIngestServlet = new BulkIngestServlet();

    /**
     * Method to setUp
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Method to tearDown
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void testBulkIngestDocstoreRequest() throws Exception {
        File inputDir = new File(
                this.getClass().getResource("repository" + File.separator + "bulkIngest-Work-Bib-Marc-20.xml").toURI());
        mockRequest.setParameter("bulkIngestFolder1", inputDir.getAbsolutePath());
        mockRequest.setParameter("bulkIngestDocCategory", DocCategory.WORK.getDescription());
        mockRequest.setParameter("bulkIngestDocType", DocType.BIB.getDescription());
        mockRequest.setParameter("bulkIngestDocFormat", DocFormat.MARC.getDescription());
        mockRequest.setParameter("bulkIngestDataFormat", "DocStore Request");
        mockRequest.setParameter("action", "Start");
        try {
            bulkIngestServlet.doGet(mockRequest, mockResponse);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    @Test
    public void testBulkIngestStandardDocFormat() throws Exception {
        File inputDir = new File(
                this.getClass().getResource("repository" + File.separator + "bulkIngest-Work-Bib-Marc-20.xml").toURI());
        mockRequest.setParameter("bulkIngestFolder1", inputDir.getAbsolutePath());
        mockRequest.setParameter("bulkIngestDocCategory", DocCategory.WORK.getDescription());
        mockRequest.setParameter("bulkIngestDocType", DocType.BIB.getDescription());
        mockRequest.setParameter("bulkIngestDocFormat", DocFormat.MARC.getDescription());
        mockRequest.setParameter("bulkIngestDataFormat", "Standard Doc Format");
        mockRequest.setParameter("action", "Start");
        try {
            bulkIngestServlet.doGet(mockRequest, mockResponse);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testStopBulkIngestProcess() throws Exception {
        File inputDir = new File(
                this.getClass().getResource("repository" + File.separator + "bulkIngest-Work-Bib-Marc-20.xml").toURI());
        mockRequest.setParameter("bulkIngestFolder1", inputDir.getAbsolutePath());
        mockRequest.setParameter("bulkIngestDocCategory", DocCategory.WORK.getDescription());
        mockRequest.setParameter("bulkIngestDocType", DocType.BIB.getDescription());
        mockRequest.setParameter("bulkIngestDocFormat", DocFormat.MARC.getDescription());
        mockRequest.setParameter("bulkIngestDataFormat", "Standard Doc Format");
        mockRequest.setParameter("action", "Stop");
        bulkIngestServlet.doGet(mockRequest, mockResponse);
    }


}
