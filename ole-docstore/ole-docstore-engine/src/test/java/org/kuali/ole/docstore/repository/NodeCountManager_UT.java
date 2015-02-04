package org.kuali.ole.docstore.repository;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.service.DocumentIndexer;
import org.kuali.ole.docstore.service.DocumentIngester;
import org.kuali.ole.docstore.service.IngestNIndexHandlerService;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import java.io.File;
import java.util.Iterator;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/21/12
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodeCountManager_UT extends BaseTestCase {

    private static Logger logger = LoggerFactory.getLogger(NodeCountManager_UT.class);
    private Map<String, Long> nodeCountMap = null;
    @Mock
    private RepositoryManager mockRepositoryManager;
    @Mock
    private Session mockSession;
    @Mock
    private Node mockRootNode;
    @Mock
    private NodeIterator mockNodeIterator;
    @Mock
    private Node mockChildNode;

    @Before
    public void setUp() throws Exception{
        super.setUp();
        MockitoAnnotations.initMocks(this);

        Mockito.when(mockRepositoryManager.getSession(Mockito.anyString(), Mockito.anyString())).thenReturn(mockSession);
        Mockito.when(mockSession.getRootNode()).thenReturn(mockRootNode);
        Mockito.when(mockRootNode.hasProperty("nodeType")).thenReturn(true);
        Mockito.when(mockRootNode.getNodes()).thenReturn(mockNodeIterator);
        Mockito.when(mockNodeIterator.hasNext()).thenReturn(true);
        Mockito.when(mockNodeIterator.next()).thenReturn(mockChildNode);
        Mockito.when(mockChildNode.getName()).thenReturn("MockFile");
    }

    @Test
    public void testUpdateNodeCount() throws Exception {
        NodeCountManager nodeCountManager = NodeCountManager.getNodeCountManager();
        nodeCountManager.setRepositoryManager(mockRepositoryManager);
        CustomNodeManager customNodeManager = CustomNodeManager.getInstance();
        nodeCountMap = nodeCountManager.generateNodeCountMap();
        assertNotNull(nodeCountMap);
        assertTrue(!nodeCountMap.isEmpty());

        //TODO: This section isn't complete, and not sure what its doing.
//        File inputDir = new File(this.getClass().getResource("/org/kuali/ole/repository/requestInstance.xml").toURI());
//        String input = FileUtils.readFileToString(inputDir);
//        RequestHandler requestHandler = new RequestHandler();
//        Request request = requestHandler.toObject(input);
//        IngestNIndexHandlerService ingestNIndexHandlerService = new IngestNIndexHandlerService();
//        ingestNIndexHandlerService.setRequestHandler(requestHandler);
//        ingestNIndexHandlerService.setDocumentIndexer(new DocumentIndexer());
//        ingestNIndexHandlerService.setDocumentIngester(new DocumentIngester());
//        ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
//        RequestDocument requestDocument = request.getRequestDocuments().get(0);
//        Session session = RepositoryManager.getRepositoryManager().getSession(requestDocument.getUser(), requestDocument.getOperation());
//        Node node = customNodeManager.getNodeByUUID(session, requestDocument.getUuid());
//        nodeCountManager.updateNodeCount(node, 6);
    }
}
