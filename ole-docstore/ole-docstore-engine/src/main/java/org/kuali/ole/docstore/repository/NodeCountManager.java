package org.kuali.ole.docstore.repository;

import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.kuali.ole.pojo.OleException;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: SG7940
 * Date: 10/11/12
 * Time: 12:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodeCountManager {

    private static NodeCountManager nodeCountManager;
    private Map<String, Long> nodeCountMap = null;
    private RepositoryManager repositoryManager;

    private NodeCountManager() {

    }

    public static NodeCountManager getNodeCountManager() {
        if (null == nodeCountManager) {
            nodeCountManager = new NodeCountManager();
        }
        return nodeCountManager;
    }

    public Map generateNodeCountMap() throws OleException, RepositoryException {
        nodeCountMap = new LinkedHashMap<String, Long>();
        RepositoryManager oleRepositoryManager = getRepositoryManager();
        Session session = oleRepositoryManager.getSession("repositoryBrowser", "generateNodeCount");
        computeNodeCountMap(session.getRootNode(), nodeCountMap);
        oleRepositoryManager.logout(session);
        return nodeCountMap;
    }

    private RepositoryManager getRepositoryManager() throws OleException {
        if (null == repositoryManager) {
            repositoryManager = RepositoryManager.getRepositoryManager();
        }
        return repositoryManager;
    }

    private void computeNodeCountMap(Node node, Map<String, Long> nodeCountMap) throws RepositoryException {
        Long size = null;
        if (node != null) {
            if (!node.hasProperty("nodeType") && !node.getName().equalsIgnoreCase("") && node.getName()
                    .equalsIgnoreCase(
                            "jcr:system")) {
                return;
            } else {
                if (isLastFolderNodeByName(node)) {
                    size = new Long(node.getNodes().getSize());
                    nodeCountMap.put(node.getPath(), size);
                } else {
                    nodeCountMap.put(node.getPath(), 0l);
                    long count = 0;
                    long longValue = 0;
                    for (Iterator<NodeIterator> it = node.getNodes(); it.hasNext(); ) {
                        Node childNode = (Node) it.next();
                        computeNodeCountMap(childNode, nodeCountMap);
                        if (nodeCountMap.containsKey(childNode.getPath())) {
                            longValue = nodeCountMap.get(childNode.getPath());
                            count = count + longValue;
                        }
                    }
                    size = new Long(count);
                    if (node.getPath() != null) {
                        nodeCountMap.put(node.getPath(), size);
                    }
                }
            }
        }
    }

    public boolean isLastFolderNodeByName(Node node) throws RepositoryException {
        Iterator it = node.getNodes();
        while (it.hasNext()) {
            Node childNode = (Node) it.next();
            if (childNode.getName().equals(ProcessParameters.NODE_INSTANCE)) {
                return true;
            } else if (childNode.getName().endsWith("File")) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public void updateNodeCount(Node node, long count) throws RepositoryException, OleException {
        if (node.getName().length() == 0 || node.getName().equals("jcr:system")) {
        } else {
            long size = 0;
            String path = node.getPath();
            if (nodeCountMap != null) {
                if (nodeCountMap.containsKey(path)) {
                    size = nodeCountMap.get(node.getPath());
                }
                nodeCountMap.put(path, size + count);
                updateNodeCount(node.getParent(), count);
            }
        }
    }


    public Map getNodeCountMap() {
        return nodeCountMap;
    }

    public void setRepositoryManager(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }
}
