package org.kuali.ole.docstore.repository;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.service.DocumentIndexer;
import org.kuali.ole.docstore.service.DocumentIngester;
import org.kuali.ole.docstore.service.IngestNIndexHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 1/23/13
 * Time: 5:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@Deprecated
public class WorkBibNodeManager_UT extends BaseTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(WorkBibNodeManager_UT.class);

    @Test
    public void testLinkNodes() throws Exception {
        WorkBibNodeManager workBibNodeManager = WorkBibNodeManager.getInstance();
        File inputDir = new File(this.getClass().getResource("/org/kuali/ole/repository/requestInstance.xml").toURI());
        String input = FileUtils.readFileToString(inputDir);
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(input);
        IngestNIndexHandlerService ingestNIndexHandlerService = new IngestNIndexHandlerService();
        ingestNIndexHandlerService.setRequestHandler(requestHandler);
        ingestNIndexHandlerService.setDocumentIndexer(new DocumentIndexer());
        ingestNIndexHandlerService.setDocumentIngester(new DocumentIngester());
        ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
        RequestDocument requestDocument = request.getRequestDocuments().get(0);
        CustomNodeManager customNodeManager = CustomNodeManager.getInstance();
        Session session = RepositoryManager.getRepositoryManager().getSession(requestDocument.getUser(), requestDocument.getOperation());
        Node node = customNodeManager.getNodeByUUID(session, requestDocument.getUuid());
        node.setProperty("bibIdentifier", requestDocument.getUuid());
        node.getParent().setProperty("instanceIdentifier", node.getParent().getIdentifier());
        workBibNodeManager.linkNodes(node.getParent(), node, session);
        LOG.info("ParentNodePath :" + workBibNodeManager.getParentNodePath());
    }
}
