package org.kuali.ole.web;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.base.BaseTestCase;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.DocumentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/15/12
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
public class DocumentServlet_UT
        extends BaseTestCase {
    private Response response = null;
    private Logger   logger   = LoggerFactory.getLogger(DocumentServlet_UT.class);
    DocumentServlet documentServlet = new DocumentServlet();
    private MockHttpServletRequest  mockRequest  = new MockHttpServletRequest();
    private MockHttpServletResponse mockResponse = new MockHttpServletResponse();


    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @After
    public void tearDown() throws Exception {
    }
    @Ignore
    @Test
    public void testProcessForIngest() throws Exception {
        byte[] fileContent = FileCopyUtils.copyToByteArray(getClass().getResourceAsStream("repository/request.xml"));
        mockRequest.setParameter("docAction", "ingestContent");
        mockRequest.setParameter("stringContent", new String(fileContent));
        documentServlet.doGet(mockRequest, mockResponse);
    }

    @Ignore
    @Test
    public void testProcessForCheckin() throws Exception {
        String existingUUIDToCheckIn = null;
        Request requestObjectIngest = getRequestObjectIngest();
        DocumentServiceImpl d = BeanLocator.getDocumentServiceImpl();
        Response response1 = d.process(requestObjectIngest);
        existingUUIDToCheckIn = response1.getDocuments().get(0).getUuid();
        URL resource = getClass().getResource("repository/checkInRequest.xml");
        File file = new File(resource.toURI());
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(readFile(file));
        request.getRequestDocuments().get(0).setId(existingUUIDToCheckIn);
        System.out.println("req content for checkIn-->" + new RequestHandler().toXML(request));
        mockRequest.setParameter("docAction", "checkIn");
        mockRequest.setParameter("stringContent", new RequestHandler().toXML(request));
        documentServlet.doGet(mockRequest, mockResponse);
    }
    @Ignore
    @Test
    public void testProcessForCheckOut() throws Exception {
        DocumentServiceImpl d = BeanLocator.getDocumentServiceImpl();
        Request request = getRequestObjectIngest();
        Response responseIngest = d.process(request);
        String responseString = new org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler().toXML(responseIngest);
        System.out.println("responseString Ingest " + responseString);
        String marcUUID = responseIngest.getDocuments().get(0).getUuid();
        String instanceUUID = responseIngest.getDocuments().get(1).getUuid();
        String dublinUUID = responseIngest.getDocuments().get(2).getUuid();
        String dublinUnqUUID = responseIngest.getDocuments().get(3).getUuid();

        //Checkout Marc
        mockRequest.setParameter("docAction", "checkOut");
        mockRequest.setParameter("uuid", marcUUID);
        mockRequest.setParameter("docFormat", DocFormat.MARC.getDescription());
        documentServlet.doGet(mockRequest, mockResponse);

        //Checkout Instance
        mockRequest.setParameter("docAction", "checkOut");
        mockRequest.setParameter("uuid", instanceUUID);
        mockRequest.setParameter("docFormat", DocFormat.OLEML.getDescription());
        documentServlet.doGet(mockRequest, mockResponse);

        //Checkout Dublin Unq
        mockRequest.setParameter("docAction", "checkOut");
        mockRequest.setParameter("uuid", dublinUUID);
        mockRequest.setParameter("docFormat", DocFormat.DUBLIN_CORE.getDescription());
        documentServlet.doGet(mockRequest, mockResponse);

        //Checkout Dublin
        mockRequest.setParameter("docAction", "checkOut");
        mockRequest.setParameter("uuid", dublinUnqUUID);
        mockRequest.setParameter("docFormat", DocFormat.DUBLIN_UNQUALIFIED.getDescription());
        documentServlet.doGet(mockRequest, mockResponse);

        //Invalid UUID checkout
        mockRequest.setParameter("docAction", "checkOut");
        mockRequest.setParameter("uuid", "111");
        mockRequest.setParameter("docFormat", DocFormat.MARC.getDescription());
        try {
            documentServlet.doGet(mockRequest, mockResponse);
        }
        catch (Exception e) {
            System.out.println("Invalid UUID checkout");
        }
    }

    @Ignore
    @Test
    public void testProcessForDelete() throws Exception {
        DocumentServiceImpl d = BeanLocator.getDocumentServiceImpl();
        Request request = getRequestObjectIngest();
        Response responseIngest = d.process(request);
        String responseString = new org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler().toXML(responseIngest);
        System.out.println("Ingest Response" + responseString);
        String marcUUID = responseIngest.getDocuments().get(0).getUuid();
        String instanceUUID = responseIngest.getDocuments().get(1).getUuid();
        String dublinUUID = responseIngest.getDocuments().get(2).getUuid();
        String dublinUnqUUID = responseIngest.getDocuments().get(3).getUuid();

        String UUIDsToDelete = marcUUID + "," + instanceUUID + "," + dublinUUID + "," + dublinUnqUUID;

        mockRequest.setParameter("identifierType", "UUID");
        mockRequest.setParameter("docAction", "delete");
        mockRequest.setParameter("requestContent", UUIDsToDelete);
        documentServlet.doGet(mockRequest, mockResponse);

    }

    private Request getRequestObjectIngest() throws URISyntaxException, IOException {
        File inputDir = new File(this.getClass().getResource("repository/request.xml").toURI());
        String fileContent = FileUtils.readFileToString(inputDir);
        RequestHandler rh = new RequestHandler();
        Request request = rh.toObject(fileContent);
        return request;
    }
}
