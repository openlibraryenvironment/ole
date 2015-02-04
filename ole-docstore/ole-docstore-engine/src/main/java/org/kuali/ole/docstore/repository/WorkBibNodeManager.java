package org.kuali.ole.docstore.repository;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Session;

import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.kuali.ole.docstore.process.ProcessParameters.FILE;
import static org.kuali.ole.docstore.process.ProcessParameters.NODE_LEVEL1;
import static org.kuali.ole.docstore.process.ProcessParameters.NODE_LEVEL2;
import static org.kuali.ole.docstore.process.ProcessParameters.NODE_LEVEL3;

/**
 * User: tirumalesh.b
 * Date: 31/8/12 Time: 5:31 PM
 */
public class WorkBibNodeManager
        extends CustomNodeManager
        implements NodeManager {
    private static WorkBibNodeManager ourInstance = new WorkBibNodeManager();
    private static final Logger LOG = LoggerFactory.getLogger(WorkBibNodeManager.class);


    //protected NodeManager nodeManager;

    private WorkBibNodeManager() {
        super();
        numLevels = 3;
        //nodeManager = CustomNodeManager.getInstance();
    }

    public static WorkBibNodeManager getInstance() {
        return ourInstance;
    }

    @Override
    public void linkNodes(Node bibNode, Node instanceNode, Session session) throws OleDocStoreException {
        try {
            if (instanceNode.hasProperty("bibIdentifier")) {
                String bibId = instanceNode.getProperty("bibIdentifier").getString();
                bibId = bibId + "," + bibNode.getIdentifier();
                instanceNode.setProperty("bibIdentifier", bibId);
            } else {
                instanceNode.setProperty("bibIdentifier", bibNode.getIdentifier());
            }
            if (bibNode.hasProperty("instanceIdentifier")) {
                String instId = bibNode.getProperty("instanceIdentifier").getString();
                instId = instId + "," + instanceNode.getIdentifier();
                bibNode.setProperty("instanceIdentifier", instId);
            } else {
                bibNode.setProperty("instanceIdentifier", instanceNode.getIdentifier());
            }

        } catch (Exception e) {
            throw new OleDocStoreException(e);
        }
    }

    public String getParentNodePath() {
        return "/work/bib";
    }


}
