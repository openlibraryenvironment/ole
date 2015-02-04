package org.kuali.ole.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.RepositoryBrowser;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.base.BaseTestCase;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.IngestNIndexHandlerService;
import org.kuali.ole.pojo.OleException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.Iterator;


/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/19/13
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@Deprecated
public class OleDocstoreDumpService_UT extends BaseTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(OleDocstoreDumpService_UT.class);
    private IngestNIndexHandlerService ingestNIndexHandlerService = BeanLocator.getIngestNIndexHandlerService();

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
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        System.getProperties().put("app.environment", "local");
        Mockito.when(mockRepositoryManager.getSession(Mockito.anyString(), Mockito.anyString())).thenReturn(mockSession);
        Mockito.when(mockSession.getRootNode()).thenReturn(mockRootNode);
        Mockito.when(mockRootNode.hasProperty("nodeType")).thenReturn(true);
        Mockito.when(mockRootNode.getNodes()).thenReturn(mockNodeIterator);
        Mockito.when(mockNodeIterator.hasNext()).thenReturn(true);
        Mockito.when(mockNodeIterator.next()).thenReturn(mockChildNode);
        Mockito.when(mockChildNode.getName()).thenReturn("MockFile");
    }

    @Ignore
    @Test
    public void OleExportMarcRequest() throws OleException, RepositoryException {
        cleanRepository("work","bibliographic");
        try{
/*         File file = new File(getClass().getResource("/org/kuali/ole/web/repository/exportProfileRequest.xml").toURI());
         String input = FileUtils.readFileToString(file);
         RequestHandler rh = new RequestHandler();
         Request request = rh.toObject(input);
         Response response = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);*/

        RepositoryBrowser repositoryBrowser = new RepositoryBrowser();
        String dump = repositoryBrowser.getRepositoryDump();
        LOG.info("dump" + dump);
        /*File file = new File(getClass().getResource("/org/kuali/ole/web/repository/exportProfile.xml").toURI());
        String requestXML = FileUtils.readFileToString(file);
        OleDocstoreDumpService oleDocstoreDumpService = new OleDocstoreDumpService();
        boolean dumpStatus = oleDocstoreDumpService.exportDocstoreData(requestXML);
         assertTrue(dumpStatus);*/
        }catch (Exception e){
            LOG.error(e.getMessage(), e);
        }
    }

    private void cleanRepository(String category, String type) throws OleException, RepositoryException {
        RepositoryManager repositoryManager = mockRepositoryManager;
        Session session = repositoryManager.getSession("mockUser", "test");
        Node rootNode = session.getRootNode();
        Node catNode = rootNode.getNode(category);
        //Node typeNode = catNode.getNode(type);
        Node typeNode = mockRootNode;
        /*for (Iterator<Node> typeiterator = typeNode.getNodes(); typeiterator.hasNext(); ) {
            Node formatNode = typeiterator.next();
            if (!formatNode.getName().equals("jcr:system")) {
                formatNode.remove();
            }
        }*/
        session.save();
        repositoryManager.logout(session);
    }
}
