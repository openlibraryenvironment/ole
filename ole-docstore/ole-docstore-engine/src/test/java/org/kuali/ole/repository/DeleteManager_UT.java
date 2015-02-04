package org.kuali.ole.repository;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.IngestNIndexHandlerService;
import org.kuali.ole.pojo.OleException;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
@Ignore
@Deprecated
public class DeleteManager_UT
        extends BaseTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteManager_UT.class);
    List<String> bibIds;
    List<String> instanceIds;

    private IngestNIndexHandlerService ingestNIndexHandlerService = BeanLocator.getIngestNIndexHandlerService();

    @Before
    public void setUp() throws Exception {
        super.setUp();
        LOG.info("setup");
        MockitoAnnotations.initMocks(this);
        bibIds = new ArrayList<String>();
        instanceIds = new ArrayList<String>();
        ingestBibData();
    }

    public void testDeleteDocs() throws Exception {
        Session session = null;
        Request request = buildRequest(bibIds);
        assertNotNull(request);
        List<RequestDocument> requestDocuments = request.getRequestDocuments();
        assertNotNull(requestDocuments);
        request.setOperation("delete");
        Response response = new DeleteManager().deleteDocs(request);
        LOG.info("status-->" + response);
        session = RepositoryManager.getRepositoryManager().getSession(request.getUser(), request.getOperation());
        LOG.info("session-->" + session);
        checkUuidInDocStore(bibIds, session);
        ResponseHandler responseHandler = new ResponseHandler();
        String deleteResponse = responseHandler.toXML(response);
        LOG.info("delete response-->" + deleteResponse);
    }

    public void testDeleteWithLinkedDocs() throws Exception {
        Session session = null;
        Request req = buildRequest(bibIds);
        List<RequestDocument> requestDocuments = req.getRequestDocuments();
        assertNotNull(requestDocuments);
        req.setOperation("deleteWithLinkedDocs");
        Response response = new DeleteManager().deleteDocs(req);
        session = RepositoryManager.getRepositoryManager().getSession(req.getUser(), req.getOperation());
        LOG.info("status-->" + response);
        checkUuidInDocStore(instanceIds, session);
        checkUuidInDocStore(bibIds, session);
        ResponseHandler responseHandler = new ResponseHandler();
        String deleteResponse = responseHandler.toXML(response);
        LOG.info("deleteWithLinkedDocs response-->" + deleteResponse);

    }

    private Request buildRequest(List<String> uuidList) {
        Request request = new Request();
        List<RequestDocument> requestDocumentList = new ArrayList<RequestDocument>();
        request.setUser("ole-khuntley");
        request.setOperation("delete");
        for (int i = 0; i < uuidList.size(); i++) {
            RequestDocument requestDocument = new RequestDocument();
            requestDocument.setId(uuidList.get(i));
            requestDocumentList.add(requestDocument);

        }
        request.setRequestDocuments(requestDocumentList);
        return request;
    }

    private List<String> ingestBibData() throws Exception {
        File file = new File(getClass().getResource("/org/kuali/ole/repository/request.xml").toURI());
        try {
            String input = FileUtils.readFileToString(file);
            RequestHandler rh = new RequestHandler();
            Request request = rh.toObject(input);
            Response response = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
            for (ResponseDocument resDoc : response.getDocuments()) {
                bibIds.add(resDoc.getUuid());
                for (ResponseDocument linkedDoc : resDoc.getLinkedDocuments()) {
                    instanceIds.add(linkedDoc.getUuid());
                }
            }
            LOG.info("Bib Ids-->" + bibIds);
            LOG.info("Instance Ids-->" + instanceIds);

        } catch (Exception e) {
            LOG.info("in excep" + e.getMessage() , e );
        }
        return bibIds;
    }

    private void checkUuidInDocStore(List<String> uuidsList, Session session) throws Exception {
        for (int i = 0; i < uuidsList.size(); i++) {
            Node deleteNode = new NodeHandler().getNodeByUUID(session, uuidsList.get(i));
            LOG.info("deletedNodes..." + deleteNode);
            assertNull(deleteNode);
        }
    }

    @Test
    public void testCleanupDocStoreData() throws Exception, OleException {
        List<String> ingestedIds = ingestBibData();
        LOG.info("bib ids-->" + ingestedIds);
        new DeleteManager().cleanUpDocStoreData();
        LOG.info("bib ids after delete-->" + ingestedIds);
    }
}
