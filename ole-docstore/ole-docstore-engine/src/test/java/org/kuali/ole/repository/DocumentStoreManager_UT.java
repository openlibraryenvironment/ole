package org.kuali.ole.repository;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.DocumentIndexer;
import org.kuali.ole.docstore.service.DocumentIngester;
import org.kuali.ole.docstore.service.IngestNIndexHandlerService;
import org.kuali.ole.logger.DocStoreLogger;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 1/15/13
 * Time: 11:35 AM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@Deprecated
public class DocumentStoreManager_UT extends BaseTestCase {

    DocStoreLogger docStoreLogger = new DocStoreLogger(this.getClass().getName());

    @Test
    public void testDocumentStoreManager() throws Exception {
        DocumentStoreManager documentStoreManager = BeanLocator.getDocumentStoreManager();
        File inputDir = new File(this.getClass().getResource("/org/kuali/ole/repository/request.xml").toURI());
        String input = FileUtils.readFileToString(inputDir);
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(input);
        IngestNIndexHandlerService ingestNIndexHandlerService = new IngestNIndexHandlerService();
        ingestNIndexHandlerService.setRequestHandler(requestHandler);
        ingestNIndexHandlerService.setDocumentIndexer(new DocumentIndexer());
        ingestNIndexHandlerService.setDocumentIngester(new DocumentIngester());
        RequestDocument requestDocument = request.getRequestDocuments().get(0);
        Response xmlResponse = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
        docStoreLogger.log("Response :" + xmlResponse);
        DeleteManager deleteManager = (DeleteManager) BeanLocator.getBean("deleteManagerService");
        CheckoutManager checkoutManager = (CheckoutManager) BeanLocator.getBean("checkoutManagerService");
        CheckinManager checkinManager = (CheckinManager) BeanLocator.getBean("checkinManagerService");
        documentStoreManager.setCheckinManager(checkinManager);
        documentStoreManager.setDeleteManager(deleteManager);
        documentStoreManager.setRequestHandler(requestHandler);
        documentStoreManager.setCheckoutManager(checkoutManager);
        documentStoreManager.updateRecord(requestDocument);
        documentStoreManager.processDeleteRequest(input);
        documentStoreManager.checkOutMultiPart(request);
        documentStoreManager.addReference("1", "2", "ole-khuntly", "checkIn");
        documentStoreManager.checkOut(requestDocument.getUuid(), requestDocument.getUser(), requestDocument.getOperation());
        documentStoreManager.checkOutBinary(requestDocument.getUuid(), requestDocument.getUser(), requestDocument.getOperation(), requestDocument.getFormat());
    }
}
