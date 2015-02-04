package org.kuali.ole.repository;

import org.kuali.ole.BaseTestCase;
import org.kuali.ole.RepositoryBrowser;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.repopojo.RepositoryData;
import org.kuali.ole.pojo.OleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.File;
import java.util.Iterator;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 2/28/12
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class RepositoryData_UT
        extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(RepositoryData_UT.class);

    public void getRepositoryData() throws Exception {
        RepositoryBrowser repositoryBrowser = new RepositoryBrowser();
        cleanRepository(DocCategory.WORK.getCode(), DocType.BIB.getDescription());
        cleanRepository(DocCategory.WORK.getCode(), DocType.INSTANCE.getDescription());
        String dump = repositoryBrowser.getRepositoryDump();
        bulkIngestBibData();
        dump = repositoryBrowser.getRepositoryDump();
        LOG.info("JCR Dump" + dump);
        RepositoryData repositoryData = new RepositoryData();
        RepositoryData repoData = repositoryData.loadDump(dump);
        String repoDump = repositoryData.getRepositoryDump(repoData);
        LOG.info("repoDump" + repoDump);
        assertEquals(dump, repoDump);
    }


    public void cleanRepository(String category, String type) throws OleException, RepositoryException {
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        Session session = repositoryManager.getSession("mockUser", "test");
        Node rootNode = session.getRootNode();
        Node catNode = rootNode.getNode(category);
        Node typeNode = catNode.getNode(type);
        for (Iterator<Node> typeIterator = typeNode.getNodes(); typeIterator.hasNext(); ) {
            Node formatNode = typeIterator.next();
            if (!formatNode.getName().equals("jcr:system")) {
                formatNode.remove();
            }
        }
        session.save();
    }

    private void bulkIngestBibData() throws Exception {
        File file = new File(
                getClass().getResource("/org/kuali/ole/bulkhandler/OLE-Bib-bulkIngest-IU-Set1-split.xml").toURI());
        try {
            File file1 = new File("");
            //            BulkLoadHandler bulkLoadHandler = new BulkLoadHandler();
            //            List<String> recordUUIDs = bulkLoadHandler
            //                    .loadBulk(DocCategory.WORK.getCode(), DocType.BIB.getDescription(), DocFormat.MARC.getCode(), file, "testUser", "testLoadBulk");
            //           LOG.info("recordUUIDs " + recordUUIDs);
        } catch (Exception e) {
            LOG.info("in excep" + e.getMessage() , e);
        }
    }
}
