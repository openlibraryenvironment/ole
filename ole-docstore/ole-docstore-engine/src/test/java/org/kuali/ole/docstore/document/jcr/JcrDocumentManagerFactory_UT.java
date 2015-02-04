package org.kuali.ole.docstore.document.jcr;

import org.apache.commons.io.FileUtils;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.xmlpojo.ingest.*;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.kuali.ole.docstore.process.batch.BulkProcessRequest;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.DocumentServiceImpl;
import org.kuali.ole.pojo.OleException;
import org.kuali.ole.solr.DummyIndexerServiceImpl;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 1/4/13
 * Time: 12:14 PM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@Deprecated
public class JcrDocumentManagerFactory_UT extends BaseTestCase {


    @Test
    public void testWorkLicenseDocumentManager() throws OleDocStoreException, RepositoryException, IOException, URISyntaxException, OleException {
        ResponseDocument responseDocument = new ResponseDocument();

        JcrWorkLicenseDocumentManager jcrWorkLicenseDocumentManager = JcrWorkLicenseDocumentManager.getInstance();
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        Session session = repositoryManager.getSession();
        File file = new File(getClass().getResource("/org/kuali/ole/repository/request.xml").toURI());
        String input = FileUtils.readFileToString(file);
        RequestHandler rh = new RequestHandler();
        Request request = rh.toObject(input);
        List<RequestDocument> requestDocuments = request.getRequestDocuments();
        RequestDocument requestDocument = requestDocuments.get(0);
        jcrWorkLicenseDocumentManager.convertContentToBytes(requestDocument);
        jcrWorkLicenseDocumentManager.isVersioningEnabled();
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        requestDocument.setAdditionalAttributes(additionalAttributes);
        additionalAttributes = requestDocument.getAdditionalAttributes();
        additionalAttributes.setAttribute("dateEntered", Calendar.getInstance().toString());
        requestDocument.setDocumentName("documentName");
        Node nodeByUUID = session.getNodeByIdentifier(requestDocument.getUuid());
        jcrWorkLicenseDocumentManager.modifyAdditionalAttributes(requestDocument, nodeByUUID);
        JcrAbstractDocumentManager abstractDocumentManager = JcrWorkLicenseDocumentManager.getInstance();
        Node node = abstractDocumentManager.storeDocument(requestDocument, session, responseDocument);
        jcrWorkLicenseDocumentManager.checkOutContent(node, "all", "checkOut");
        jcrWorkLicenseDocumentManager.checkOutContent(node, DocFormat.PDF.getCode(), "checkOut");
    }

    @Test
    public void testAbstractDocumentManager() throws OleException, RepositoryException, OleDocStoreException, URISyntaxException, IOException {
        JcrAbstractDocumentManager abstractDocumentManager = JcrWorkLicenseDocumentManager.getInstance();
        ResponseDocument responseDocument = new ResponseDocument();
        Session session = RepositoryManager.getRepositoryManager().getSession("CheckinManager", "checkIn");
        File file = new File(getClass().getResource("/org/kuali/ole/repository/request-new.xml").toURI());
        String input = FileUtils.readFileToString(file);
        RequestHandler rh = new RequestHandler();
        Request request = rh.toObject(input);
        request.setOperation("ingest");
        DocumentServiceImpl d = BeanLocator.getDocumentServiceImpl();
        Response response1 = d.process(request);
        String existingUUIDToCheckIn = response1.getDocuments().get(0).getUuid();
        List<RequestDocument> requestDocuments = request.getRequestDocuments();
        RequestDocument requestDocument = requestDocuments.get(0);
        requestDocument.setId(existingUUIDToCheckIn);
        BulkProcessRequest bulkProcessRequest = new BulkProcessRequest();
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        requestDocument.setAdditionalAttributes(additionalAttributes);
        bulkProcessRequest.setUser(ProcessParameters.BULK_DEFAULT_USER);
        bulkProcessRequest.setOperation(BulkProcessRequest.BulkProcessOperation.INGEST);
        abstractDocumentManager.bulkIngest(bulkProcessRequest, requestDocuments);
        abstractDocumentManager.getVersionManager(session);
        abstractDocumentManager.updateIndex(requestDocument);
        List<String> valuesList = new ArrayList<String>();
        abstractDocumentManager.validateInput(requestDocument, session, valuesList);
        abstractDocumentManager.convertContentToBytes(requestDocument);
        abstractDocumentManager.buildResponseDocument(requestDocument);
        requestDocument.setDocumentName(file.getName());
        requestDocument.setUser(ProcessParameters.BULK_DEFAULT_USER);
        requestDocument.setUuid(existingUUIDToCheckIn);
        requestDocument.setOperation(Request.Operation.deleteWithLinkedDocs.name());
        requestDocument.setUuid(requestDocument.getId());
        ResponseDocument respDoc = new ResponseDocument();
        abstractDocumentManager.ingest(requestDocument, session, respDoc);
        List list = new ArrayList<RequestDocument>();
        list.add(requestDocument);
        abstractDocumentManager.ingest(list, session);
        abstractDocumentManager.checkout(requestDocument, session);
        abstractDocumentManager.batchIngest(bulkProcessRequest, list, session);
        abstractDocumentManager.delete(list, session);
        abstractDocumentManager.index(list, true);
        Node node = abstractDocumentManager.storeDocument(requestDocument, session, responseDocument);
        System.out.println("  ");
        abstractDocumentManager.checkOutContent(node, DocFormat.DOC.getCode(), "checkOut");
        abstractDocumentManager.updateVersion(session, node);
        abstractDocumentManager.buildResponseDocument(requestDocument, session, responseDocument);
    }

    @Test
    public void testDummyIndexerServiceImpl() {
        DummyIndexerServiceImpl dummyIndexerService = DummyIndexerServiceImpl.getInstance();
        dummyIndexerService.deleteDocument(new String("12"), new String("123"));
        dummyIndexerService.indexDocumentsFromDirBySolrDoc("1", "2", "3", "4");
        List list = new ArrayList<File>();
        dummyIndexerService.indexDocumentsFromFiles("1", "2", "3", list);
        list = new ArrayList<String>();
        dummyIndexerService.deleteDocuments("1", list);
        dummyIndexerService.indexDocumentsFromFileBySolrDoc("1", "2", "3", "4");
        dummyIndexerService.getSolrDocumentBySolrId("1");
        dummyIndexerService.getSolrDocument("1", "2");
        list = new ArrayList<SolrInputDocument>();
        dummyIndexerService.indexSolrDocuments(list);
    }

    @Test
    public void testDocumentManagerFactory() {
        JcrDocumentManagerFactory jcrDocumentManagerFactory = JcrDocumentManagerFactory.getInstance();
        jcrDocumentManagerFactory.getDocumentManager("docCategory", "docType", "docFormat");
    }
}
