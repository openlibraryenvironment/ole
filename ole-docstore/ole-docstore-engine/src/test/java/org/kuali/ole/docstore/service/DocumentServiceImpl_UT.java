package org.kuali.ole.docstore.service;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.discovery.service.ServiceLocator;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.*;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecord;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.WorkBibMarcRecordProcessor;
import org.kuali.ole.docstore.process.batch.BulkProcessRequest;
import org.kuali.ole.docstore.service.impl.OleWebServiceProviderImpl;
import org.kuali.ole.docstore.transaction.TransactionManager;
import org.kuali.ole.docstore.utility.BatchIngestStatistics;
import org.kuali.ole.docstore.utility.BulkIngestStatistics;
import org.kuali.ole.docstore.utility.FileIngestStatistics;
import org.kuali.ole.pojo.OleException;
import org.kuali.ole.repository.NodeHandler;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 1/8/13
 * Time: 12:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@Deprecated
public class DocumentServiceImpl_UT
        extends BaseTestCase {
    private Response response = null;
    private Logger logger = LoggerFactory.getLogger(DocumentServiceImpl_UT.class);
    @Mock
    private TransactionManager mockTransactionManager;
    @Mock
    private BusinessObjectService mockBusinessObjectService;


    @Before
    public void setUp() throws Exception{
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }


    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProcessForIngest() throws Exception {
        DocumentServiceImpl d = BeanLocator.getDocumentServiceImpl();
        Request request = getRequestObjectIngest();
        logger.debug("req xml:" + new RequestHandler().toXML(request));
        Response response = d.process(request);
        //logger.debug("uuid-->" + response.getDocuments().get(0).getUuid());
        logger.debug("response " + response);
        String responseString = new ResponseHandler().toXML(response);
        logger.debug("responseString " + responseString);
    }


    @Test
    public void testProcessForCheckin() throws Exception {
        String existingUUIDToCheckIn = null;
        Request request1 = getRequestObjectIngest();
        request1.setOperation("ingest");
        DocumentServiceImpl d = BeanLocator.getDocumentServiceImpl();
        d.setTransactionManager(mockTransactionManager);
        Response response1 = d.process(request1);
       // existingUUIDToCheckIn = response1.getDocuments().get(0).getUuid();
        String path = "/org/kuali/ole/repository/checkInRequest.xml";
        URL resource = getClass().getResource(path);
        File file = new File(resource.toURI());
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(readFile(file));
        //request.getRequestDocuments().get(0).setId(existingUUIDToCheckIn);
        Response response = d.process(request);
        logger.debug("response " + response);
        String responseString = new ResponseHandler().toXML(response);
        logger.debug("responseString " + responseString);
        List<RequestDocument> requestDocuments = request.getRequestDocuments();
        for (RequestDocument rd : requestDocuments) {
            if ("bibliographic".equalsIgnoreCase(rd.getType()) && "marc".equalsIgnoreCase(rd.getFormat())) {
                WorkBibMarcRecordProcessor recordProcessor = new WorkBibMarcRecordProcessor();
                WorkBibMarcRecord workBibMarcRecord = recordProcessor.fromXML(rd.getContent().getContent()).getRecords()
                        .get(0);
                System.out
                        .println("getCode " + workBibMarcRecord.getDataFields().get(0).getSubFields().get(0).getCode());
                System.out
                        .println("getValue " + workBibMarcRecord.getDataFields().get(0).getSubFields().get(0).getValue());
                for (ResponseDocument responseDocument : response.getDocuments()) {
                    if ("bibliographic".equalsIgnoreCase(responseDocument.getType()) && "marc"
                            .equalsIgnoreCase(responseDocument.getFormat())) {
                        rd.setUuid(responseDocument.getUuid());
                    }
                }
                logger.debug("getUuid " + rd.getUuid());
            }

        }
    }

    @Test
    public void testProcessForCheckOut() throws Exception {
        DocumentServiceImpl d = BeanLocator.getDocumentServiceImpl();
        Request request = getRequestObjectIngest();
        Response responseIngest = d.process(request);
        String responseString = new ResponseHandler().toXML(responseIngest);
        logger.debug("responseString Ingest " + responseString);
        String marcUUID = responseIngest.getDocuments().get(0).getUuid();
        String instanceUUID = responseIngest.getDocuments().get(1).getUuid();
        String dublinUUID = responseIngest.getDocuments().get(2).getUuid();
        String dublinUnqUUID = responseIngest.getDocuments().get(3).getUuid();


        //Checkout Marc
        request = getRequest(marcUUID, null, null, DocFormat.MARC.getDescription());
        Response response = d.process(request);
        responseString = new ResponseHandler().toXML(response);
        logger.debug("checkOut content of Marc " + responseString);

        //heckout Instance
        request = getRequest(instanceUUID, null, null, DocFormat.OLEML.getDescription());
        response = d.process(request);
        responseString = new ResponseHandler().toXML(response);
        logger.debug("checkOut content of Instance " + responseString);


        //Checkout Dublin
        request = getRequest(dublinUUID, null, null, DocFormat.DUBLIN_CORE.getDescription());
        response = d.process(request);
        responseString = new ResponseHandler().toXML(response);
        logger.debug("checkOut content of Dublin " + responseString);

        //Checkout Dublin Unq
        request = getRequest(dublinUnqUUID, null, null, DocFormat.DUBLIN_UNQUALIFIED.getDescription());
        response = d.process(request);
        responseString = new ResponseHandler().toXML(response);
        logger.debug("checkOut content of Dublin Unq " + responseString);

        //Ivalid MARC UUID checkout
        try {
            request = getRequest("111", null, null, DocFormat.MARC.getDescription());
        } catch (Exception e) {
            logger.debug("Invalid UUID" , e );
        }


        //Ivalid Dublin UUID checkout
        try {
            request = getRequest("112", null, null, DocFormat.DUBLIN_CORE.getDescription());
        } catch (Exception e) {
            logger.debug("Invalid UUID" , e);
        }

        //Ivalid Dublin Unq UUID checkout
        try {
            request = getRequest("113", null, null, DocFormat.DUBLIN_UNQUALIFIED.getDescription());
            request = getRequest("114", null, null, DocFormat.OLEML.getDescription());
        } catch (Exception e) {
            logger.debug("Invalid UUID" , e);
        }

        //Ivalid OLEML UUID checkout
        try {
            request = getRequest("114", null, null, DocFormat.OLEML.getDescription());
        } catch (Exception e) {
            logger.debug("Invalid UUID" , e);
        }
    }

    private Request getRequest(String uuid, String userId, String action, String docFormat)
            throws OleDocStoreException, OleException {

        Node nodeByUUID = null;
        Session session = null;
        Request req = new Request();
        if (action == null || "".equalsIgnoreCase(action)) {
            action = "checkOut";
        }
        if (userId == null || "".equalsIgnoreCase(action)) {
            userId = "checkOutUser";
        }
        try {
            session = RepositoryManager.getRepositoryManager().getSession(userId, action);
        } catch (Exception e) {
            logger.debug("Exception while creating a session" + e );
            throw new OleDocStoreException(e.getMessage(), e);
        }
        String cat = null;
        String type = null;
        String format = null;
        try {
            NodeHandler nodeHandler = new NodeHandler();
            nodeByUUID = nodeHandler.getNodeByUUID(session, uuid);
            String nodePath = nodeByUUID.getPath();
            String[] splitLine = nodePath.split("/");

            if (splitLine != null && splitLine.length >= 4) {
                cat = splitLine[1];
                type = splitLine[2];
                format = splitLine[3];
            } else {
                if (docFormat.equalsIgnoreCase(DocFormat.MARC.getDescription())) {
                    throw new OleDocStoreException(" This is not a valid UUID ");
                } else if (docFormat.equalsIgnoreCase(DocFormat.DUBLIN_CORE.getDescription())) {
                    throw new OleDocStoreException("Invalid UUID", new Throwable(""));
                } else if (docFormat.equalsIgnoreCase(DocFormat.DUBLIN_UNQUALIFIED.getDescription())) {
                    throw new OleDocStoreException(new Throwable(""));
                } else {
                    throw new OleDocStoreException();
                }
            }
            if (docFormat == null || "".equalsIgnoreCase(docFormat)) {
                docFormat = format;
            }
            req.setUser(userId);
            req.setOperation(action);
            List<RequestDocument> reqDocList = new ArrayList<RequestDocument>();
            RequestDocument reqDoc = buildRequest(cat, type, format, uuid);
            reqDocList.add(reqDoc);
            req.setRequestDocuments(reqDocList);
        } catch (Exception e) {
            logger.debug(e.getMessage() ,  e );
            throw new OleDocStoreException(e);
        } finally {
            session.logout();
        }
        return req;
    }

    private RequestDocument buildRequest(String cat, String type, String format, String uuid) {
        RequestDocument reqDoc = new RequestDocument();
        reqDoc.setCategory(cat);
        reqDoc.setType(type);
        reqDoc.setFormat(format);
        reqDoc.setUuid(uuid);
        return reqDoc;

    }


    public void testProcessForDelete() throws Exception {
        DocumentServiceImpl d = BeanLocator.getDocumentServiceImpl();
        Request request = getRequestObjectIngest();
        Response responseIngest = d.process(request);
        String responseString = new ResponseHandler().toXML(responseIngest);
        logger.debug("Ingest Response" + responseString);
        String marcUUID = responseIngest.getDocuments().get(0).getUuid();
        String instanceUUID = responseIngest.getDocuments().get(1).getUuid();
        String dublinUUID = responseIngest.getDocuments().get(2).getUuid();
        String dublinUnqUUID = responseIngest.getDocuments().get(3).getUuid();

        String UUIDsToDelete = marcUUID + "," + instanceUUID + "," + dublinUUID + "," + dublinUnqUUID;
        request = buildRequestForDelete(marcUUID, "", "delete");
        logger.debug("delete req xml:" + new RequestHandler().toXML(request));
        logger.debug("request for delete:" + request);
        response = d.process(request);

        responseString = new ResponseHandler().toXML(response);
        logger.debug("response:" + responseString);

    }


    private Request buildRequestForDelete(String ids, String identifierType, String operation) throws Exception {
        String[] Id = ids.split(",");
        List<String> idList = new ArrayList<String>();
        List<String> uuidList = new ArrayList<String>();
        Request request = null;
        for (int i = 0; i < Id.length; i++) {
            idList.add(Id[i]);
            logger.debug("adding -->" + idList);
        }
        if ((!StringUtils.isBlank(identifierType)) && (identifierType.equalsIgnoreCase("SCN") || identifierType
                .equalsIgnoreCase("ISBN"))) {
            uuidList = ServiceLocator.getQueryService().getUUIDList(idList, identifierType);
        } else {
            uuidList = idList;
        }
        request = identifyDeleteableDocuments(uuidList, operation);
        return request;
    }

    public Request identifyDeleteableDocuments(List<String> uuidsList, String operation) throws Exception {
        Request dsRequest = new Request();
        dsRequest.setUser("ole-khuntley");
        dsRequest.setOperation(operation);

        List<RequestDocument> requestDocuments = null;
        Response response = null;
        String uuidsNotInOle = null;
        String serviceURL = null;
        StringBuilder uuidsSB = null;
        // Build a csv of UUIDs of the documents to be deleted.
        uuidsSB = new StringBuilder();
        for (String uuid : uuidsList) {
            uuidsSB.append(uuid).append(",");
        }
        serviceURL = ConfigContext.getCurrentContextConfig().getProperty("uuidCheckServiceURL");
        logger.debug(" uuidCheckServiceURL --------> " + serviceURL);
        //        uuidsNotInOle = uuidsSB.substring(0, uuidsSB.length() - 1);

        OleWebServiceProvider oleWebServiceProvider = new OleWebServiceProviderImpl();

        OleUuidCheckWebService oleUuidCheckWebService = (OleUuidCheckWebService) oleWebServiceProvider
                .getService("org.kuali.ole.service.OleUuidCheckWebService", "oleUuidCheckWebService", serviceURL);

        try {
            uuidsNotInOle = oleUuidCheckWebService.checkUuidExsistence(uuidsSB.substring(0, uuidsSB.length() - 1));
        } catch (Exception e) {
            logger.debug("Unable to connect OLE Server" , e);
        }

        logger.debug("response uuids from OLE " + uuidsNotInOle);
        // If the UUIDs do not exist in OLE, delete them from docstore.
        if ((uuidsNotInOle != null) && (uuidsNotInOle.length() > 0)) {
            String[] uuids = StringUtils.split(uuidsNotInOle, ",");
            requestDocuments = new ArrayList<RequestDocument>();
            for (String id : uuids) {
                RequestDocument requestDocument = new RequestDocument();
                requestDocument.setCategory(DocCategory.WORK.getCode());
                requestDocument.setFormat(DocFormat.MARC.getCode());
                requestDocument.setType(DocType.BIB.getDescription());
                requestDocument.setUuid(id);
                requestDocument.setOperation(dsRequest.getOperation());
                requestDocuments.add(requestDocument);
            }
            dsRequest.setRequestDocuments(requestDocuments);
        }
        return dsRequest;
    }

    private Request getRequestObjectIngest() throws URISyntaxException, IOException {
        String path = "/org/kuali/ole/repository/request-new.xml";
        URL resource = getClass().getResource(path);
        File file = new File(resource.toURI());
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(readFile(file));
        return request;
    }

    @Test
    public void testBibStatus() throws Exception {
        try {
            String path = "/org/kuali/ole/repository/IngestRequestWithAdditionalAttributes.xml";
            Request request = getRequestObject(path);
            //Ingesting new record
            AdditionalAttributes additionalAttributes = new AdditionalAttributes();
            request.getRequestDocuments().get(0).setAdditionalAttributes(additionalAttributes);
            String reqContent = new RequestHandler().toXML(request);
            logger.debug("req content:" + reqContent);
            Response response = DocumentServiceImpl.getInstance().process(request);
            String responseContent = new ResponseHandler().toXML(response);
            logger.debug("responseContent:" + responseContent);
            String marcUUID = response.getDocuments().get(0).getUuid();
            String instanceUUID = response.getDocuments().get(0).getLinkedDocuments().get(0).getUuid();
            //            logger.debug("testMarcAdditionalAttributes response " + new ResponseHandler().toXML(response));
            additionalAttributes = response.getDocuments().get(0).getAdditionalAttributes();
            assertNotNull(additionalAttributes.getAttribute(AdditionalAttributes.DATE_ENTERED));
            assertNotNull(additionalAttributes.getAttribute(AdditionalAttributes.CREATED_BY));
            assertNull(additionalAttributes.getAttribute(AdditionalAttributes.LAST_UPDATED));
            assertNull(additionalAttributes.getAttribute(AdditionalAttributes.UPDATED_BY));
            assertNull(additionalAttributes.getAttribute(AdditionalAttributes.STATUS_UPDATED_BY));
            assertNull(additionalAttributes.getAttribute(AdditionalAttributes.STATUS_UPDATED_ON));

            //Updating marc record fields other than status field
            request.getRequestDocuments().get(0).setAdditionalAttributes(additionalAttributes);
            request.setOperation("checkIn");
            request.getRequestDocuments().get(0).setId(marcUUID);
            request.getRequestDocuments().get(0).getLinkedRequestDocuments().get(0).setId(instanceUUID);
            response = DocumentServiceImpl.getInstance().process(request);
            responseContent = new ResponseHandler().toXML(response);
            logger.debug("responseContent:" + responseContent);
            additionalAttributes = response.getDocuments().get(0).getAdditionalAttributes();
            assertNotNull(additionalAttributes.getAttribute(AdditionalAttributes.DATE_ENTERED));
            assertNotNull(additionalAttributes.getAttribute(AdditionalAttributes.CREATED_BY));
            assertNotNull(additionalAttributes.getAttribute(AdditionalAttributes.LAST_UPDATED));
            assertNotNull(additionalAttributes.getAttribute(AdditionalAttributes.UPDATED_BY));
            assertNull(additionalAttributes.getAttribute(AdditionalAttributes.STATUS_UPDATED_BY));
            assertNull(additionalAttributes.getAttribute(AdditionalAttributes.STATUS_UPDATED_ON));


            //Updating marc record after changing status field
            additionalAttributes.setAttribute(AdditionalAttributes.STATUS, "None");
            request.getRequestDocuments().get(0).setAdditionalAttributes(additionalAttributes);
            request.setOperation("checkIn");
            request.getRequestDocuments().get(0).setId(marcUUID);
            request.getRequestDocuments().get(0).getLinkedRequestDocuments().get(0).setId(instanceUUID);
            response = DocumentServiceImpl.getInstance().process(request);
            additionalAttributes = response.getDocuments().get(0).getAdditionalAttributes();
            assertNotNull(additionalAttributes.getAttribute(AdditionalAttributes.DATE_ENTERED));
            assertNotNull(additionalAttributes.getAttribute(AdditionalAttributes.CREATED_BY));
            assertNotNull(additionalAttributes.getAttribute(AdditionalAttributes.LAST_UPDATED));
            assertNotNull(additionalAttributes.getAttribute(AdditionalAttributes.UPDATED_BY));
            assertNotNull(additionalAttributes.getAttribute(AdditionalAttributes.STATUS_UPDATED_BY));
            assertNotNull(additionalAttributes.getAttribute(AdditionalAttributes.STATUS_UPDATED_ON));
            assertEquals("None", additionalAttributes.getAttribute(AdditionalAttributes.STATUS));
            assertEquals("ole-khuntley", additionalAttributes.getAttribute(AdditionalAttributes.STATUS_UPDATED_BY));
            responseContent = new ResponseHandler().toXML(response);
            logger.debug("responseContent:" + responseContent);
        } catch (Exception e) {
            logger.error("Exception:" + e.getMessage(), e);
        }

    }

    @Test
    public void testIngestProcessWithMarcAdditionalAttributes() {
        try {
            String path = "/org/kuali/ole/repository/IngestRequestWithAdditionalAttributes.xml";
            Request request = getRequestObject(path);

            String reqContent = new RequestHandler().toXML(request);
            logger.debug("req content:" + reqContent);
            Request request1 = new RequestHandler().toObject(reqContent);
            logger.debug("request xml:" + new RequestHandler().toXML(request1));

            Response response = DocumentServiceImpl.getInstance().process(request);
            logger.debug("testMarcAdditionalAttributes response " + new ResponseHandler().toXML(response));
            String responseContent = new ResponseHandler().toXML(response);
            logger.debug("responseContent:" + responseContent);
        } catch (Exception e) {
            logger.error(e.getMessage() ,  e );
        }

    }

    @Test
    public void testCheckInProcessWithMarcAdditionalAttributes() throws Exception {
        String path = "/org/kuali/ole/repository/IngestRequestWithAdditionalAttributes.xml";
        Request requestIngest = getRequestObject(path);

        Response responseIngest = DocumentServiceImpl.getInstance().process(requestIngest);
        logger.debug("Ingest response--------- " + new ResponseHandler().toXML(responseIngest));
        String marcUUID = responseIngest.getDocuments().get(0).getUuid();
        path = "/org/kuali/ole/repository/checkInRequestWithAdditionalAttributes.xml";
        Request request = getRequestObject(path);
        request.getRequestDocuments().get(0).setId(marcUUID);

        logger.debug("uuid to update:" + request.getRequestDocuments().get(0).getId());
        Response response = DocumentServiceImpl.getInstance().process(request);

        logger.debug("CheckIn response " + new ResponseHandler().toXML(response));
    }

    @Test
    public void testCheckOutProcessWithMarcAdditionalAttributes() throws Exception {
        DocumentServiceImpl documentService = BeanLocator.getDocumentServiceImpl();
        String path = "/org/kuali/ole/repository/IngestRequestWithAdditionalAttributes.xml";
        Request requestIngest = getRequestObject(path);
        Response responseIngest = documentService.process(requestIngest);
        logger.debug(new ResponseHandler().toXML(responseIngest));
        String marcUUID = responseIngest.getDocuments().get(0).getUuid();
        String responseString = "";

        //Checkout Marc
        Request request = getRequest(marcUUID, null, null, DocFormat.MARC.getDescription());
        logger.debug("req xml:" + new RequestHandler().toXML(request));
        Response response = documentService.process(request);

        responseString = new ResponseHandler().toXML(response);

        response = new ResponseHandler().toObject(responseString);
        logger.debug("content:" + response.getDocuments().get(0).getContent().getContent());
        logger.debug("checkOut content of Marc " + responseString);

    }

    private Request getRequestObject(String resourceLocation) throws URISyntaxException, IOException {
        URL resource = getClass().getResource(resourceLocation);
        File file = new File(resource.toURI());
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(readFile(file));
        return request;
    }

    @Test
    public void testBulkIngestStandardDocFormat() throws Exception {
        String path = "/org/kuali/ole/repository/1Marc.xml";
        File inputDir = new File(this.getClass().getResource(path).toURI());
        BulkProcessRequest bulkProcessRequest = new BulkProcessRequest();
        bulkProcessRequest.setDocCategory(DocCategory.WORK.getDescription());
        bulkProcessRequest.setDocType(DocType.BIB.getDescription());
        bulkProcessRequest.setDocFormat(DocFormat.MARC.getDescription());
        bulkProcessRequest.setOperation(BulkProcessRequest.BulkProcessOperation.INGEST);
        bulkProcessRequest.setAction(BulkProcessRequest.BulkProcessAction.START);
        bulkProcessRequest.setDataFormat(BulkProcessRequest.BulkIngestDataFormat.STANDARD);
        bulkProcessRequest.setStatus(BulkProcessRequest.BulkProcessStatus.STARTED);
        bulkProcessRequest.setDoIndex(true);
        bulkProcessRequest.setCommitSize(1000);
        bulkProcessRequest.setDataFolder("bulkIngest");
        bulkProcessRequest.setBulkIngestFolder(inputDir.getAbsolutePath());
        assertNotNull(bulkProcessRequest);
        BeanLocator.getDocumentServiceImpl().bulkProcess(bulkProcessRequest);
        logger.info("BulkProcessStatus: " + bulkProcessRequest.getStatus());
        logger.info("is DoIndex: " + bulkProcessRequest.isDoIndex());
        logger.info("CommitSize: " + bulkProcessRequest.getCommitSize());
    }

    @Test
    public void testBulkIngestStatistics() throws Exception {
        BulkProcessRequest bulkProcessRequest = new BulkProcessRequest();
        String path = "/org/kuali/ole/bulkIngest/bulkIngest-Work-Bib-Marc-20.xml";
        File inputDir = new File(this.getClass().getResource(path).toURI());
        bulkProcessRequest.setUser("ole-khuntley");
        bulkProcessRequest.setDocCategory(DocCategory.WORK.getDescription());
        bulkProcessRequest.setDocType(DocType.BIB.getDescription());
        bulkProcessRequest.setDocFormat(DocFormat.MARC.getDescription());
        bulkProcessRequest.setOperation(BulkProcessRequest.BulkProcessOperation.INGEST);
        bulkProcessRequest.setAction(BulkProcessRequest.BulkProcessAction.STATUS);
        bulkProcessRequest.setDataFormat(BulkProcessRequest.BulkIngestDataFormat.STANDARD);
        bulkProcessRequest.setDataFolder("bulkIngest");
        bulkProcessRequest.setBulkIngestFolder(inputDir.getAbsolutePath());
        bulkProcessRequest.setBulkIngestStatistics(getBulkIngestStatistics());
        assertNotNull(bulkProcessRequest);
        try {
            BeanLocator.getDocumentServiceImpl().bulkProcess(bulkProcessRequest);
        } catch (Exception e) {
            logger.debug("Exception due to:" + e.getMessage() , e);
            logger.info("Exception due to:" + e.getMessage(), e);
        }
    }

    @Test
    public void testClearBulkIngestStatistics() throws Exception {
        BulkProcessRequest bulkProcessRequest = new BulkProcessRequest();
        String path = "/org/kuali/ole/bulkIngest/bulkIngest-Work-Bib-Marc-20.xml";
        File inputDir = new File(this.getClass().getResource(path).toURI());
        bulkProcessRequest.setUser("ole-khuntley");
        bulkProcessRequest.setDocCategory(DocCategory.WORK.getDescription());
        bulkProcessRequest.setDocType(DocType.BIB.getDescription());
        bulkProcessRequest.setDocFormat(DocFormat.MARC.getDescription());
        bulkProcessRequest.setOperation(BulkProcessRequest.BulkProcessOperation.INGEST);
        bulkProcessRequest.setAction(BulkProcessRequest.BulkProcessAction.STATUS);
        bulkProcessRequest.setDataFormat(BulkProcessRequest.BulkIngestDataFormat.STANDARD);
        bulkProcessRequest.setDataFolder("bulkIngest");
        bulkProcessRequest.setBulkIngestFolder(inputDir.getAbsolutePath());
        bulkProcessRequest.setBulkIngestStatistics(getBulkIngestStatistics());
        assertNotNull(bulkProcessRequest);
        logger.info("Json String before clearing:" + bulkProcessRequest.getBulkIngestStatistics().getJsonString());
        DocumentServiceImpl documentService = BeanLocator.getDocumentServiceImpl();
        documentService.setBulkIngestRequest(bulkProcessRequest);
        documentService.bulkProcess(bulkProcessRequest);
        bulkProcessRequest.setAction(BulkProcessRequest.BulkProcessAction.CLEAR);
        BeanLocator.getDocumentServiceImpl().bulkProcess(bulkProcessRequest);
        logger.info("Json String after clearing:" + bulkProcessRequest.getBulkIngestStatistics().getJsonString());

    }


    @Test
    public void testStopBulkIngestProcess() throws Exception {
        BulkProcessRequest bulkProcessRequest = new BulkProcessRequest();
        String path = "/org/kuali/ole/bulkIngest/bulkIngest-Work-Bib-Marc-20.xml";
        File inputDir = new File(this.getClass().getResource(path).toURI());
        bulkProcessRequest.setUser("ole-khuntley");
        bulkProcessRequest.setDocCategory(DocCategory.WORK.getDescription());
        bulkProcessRequest.setDocType(DocType.BIB.getDescription());
        bulkProcessRequest.setDocFormat(DocFormat.MARC.getDescription());
        bulkProcessRequest.setOperation(BulkProcessRequest.BulkProcessOperation.INGEST);
        bulkProcessRequest.setAction(BulkProcessRequest.BulkProcessAction.STOP);
        bulkProcessRequest.setDataFormat(BulkProcessRequest.BulkIngestDataFormat.STANDARD);
        bulkProcessRequest.setDataFolder("bulkIngest");
        bulkProcessRequest.setBulkIngestFolder(inputDir.getAbsolutePath());
        assertNotNull(bulkProcessRequest);
        BeanLocator.getDocumentServiceImpl().bulkProcess(bulkProcessRequest);
    }


    public BulkIngestStatistics getBulkIngestStatistics() {
        BulkIngestStatistics bulkIngestStatistics = BulkIngestStatistics.getInstance();
        bulkIngestStatistics.setFirstBatch(true);
        bulkIngestStatistics.setLastBatch(true);
        bulkIngestStatistics.setBatchSize("10");
        bulkIngestStatistics.setFileRecCount(2);
        bulkIngestStatistics.setCommitRecCount(1);
        List<BatchIngestStatistics> batchIngestStatisticsList = new ArrayList<BatchIngestStatistics>();
        List<FileIngestStatistics> fileIngestStatisticsList = new ArrayList<FileIngestStatistics>();
        FileIngestStatistics fileIngestStatistics = bulkIngestStatistics.startFile();
        StringBuilder sb = new StringBuilder();
        fileIngestStatistics.setFileName("sample1.txt");
        fileIngestStatistics.setFileStatus("");
        BatchIngestStatistics batchIngestStatistics = fileIngestStatistics.startBatch();
        long timeToConvertStringToReqObj = 1200;
        long timeToCreateNodesInJcr = 3555;
        long timeToSaveJcrSession = 1222;
        long ingestingTime = 3400;
        long timeToConvertXmlToPojo = 2000;
        long timeToConvertToSolrInputDocs = 1400;
        long timeToIndexSolrInputDocs = 1860;
        long timeToSolrCommit = 1700;
        long timeToSolrOptimize = 2500;
        long indexingTime = 2200;
        long ingestNIndexTotalTime = 1400;
        long batchTime = 2500;
        String batchStartTime = "0:0:1.333";
        String batchEndTime = "0:0:5.222";
        long startTime = 2200;
        long endTime = 3200;
        batchIngestStatistics.setTimeToConvertStringToReqObj(timeToConvertStringToReqObj);
        batchIngestStatistics.setTimeToCreateNodesInJcr(timeToCreateNodesInJcr);
        batchIngestStatistics.setTimeToSaveJcrSession(timeToSaveJcrSession);
        batchIngestStatistics.setIngestingTime(ingestingTime);
        batchIngestStatistics.setTimeToConvertXmlToPojo(timeToConvertXmlToPojo);
        batchIngestStatistics.setTimeToConvertToSolrInputDocs(timeToConvertToSolrInputDocs);
        batchIngestStatistics.setTimeToIndexSolrInputDocs(timeToIndexSolrInputDocs);
        batchIngestStatistics.setTimeToSolrCommit(timeToSolrCommit);
        batchIngestStatistics.setTimeToSolrOptimize(timeToSolrOptimize);
        batchIngestStatistics.setIndexingTime(indexingTime);
        batchIngestStatistics.setIngestNIndexTotalTime(ingestNIndexTotalTime);
        batchIngestStatistics.setBatchTime(batchTime);
        batchIngestStatisticsList.add(batchIngestStatistics);
        batchIngestStatistics.setBatchStartTime(batchStartTime);
        batchIngestStatistics.setBatchEndTime(batchEndTime);
        batchIngestStatistics.buildBatchMetric(sb, batchIngestStatistics);
        fileIngestStatistics.setBatchStatisticsList(batchIngestStatisticsList);
        bulkIngestStatistics.setFileIngestStatistics(fileIngestStatistics);
        return bulkIngestStatistics;
    }

    public String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        return stringBuilder.toString();
    }
}
