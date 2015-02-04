package org.kuali.ole.docstore.model.repopojo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class for representing the data in repository
 * User: Pranitha
 * Date: 2/28/12
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class RepositoryData {

    private FolderNode rootNode = new FolderNode();

    public RepositoryData() {
        rootNode.setPath("/");
        rootNode.setName("/");
    }


    public FolderNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(FolderNode rootNode) {
        this.rootNode = rootNode;
    }


    public RepositoryData loadDump(String dump) {
        RepositoryData repositoryData = new RepositoryData();
        loadDump(dump, repositoryData);
        return repositoryData;
    }

    public void loadDump(String dump, RepositoryData repositoryData) {
        String splitDump[];
        StringBuilder content = new StringBuilder();
        String key = null;
        String value = null;
        String path = null;
        boolean status = true;
        splitDump = dump.split("\\n");
        for (int i = 0; i < splitDump.length; i++) {
            //set jcr:data key
            if (splitDump[i].contains("jcr:data")) {
                content = new StringBuilder();
                key = splitDump[i].substring(0, splitDump[i].indexOf("=")).trim();
                path = key.substring(0, key.lastIndexOf("/"));
                status = content(splitDump[i], status, content, path);
            }
            // set jcr:data value
            if (!status && !splitDump[i].contains("jcr:data")) {
                status = content(splitDump[i], status, content, path);
                value = content.toString();
                List<String> propPathElements = getPathElements(key);
                FolderNode folderNode = loadPath(repositoryData,
                        propPathElements.subList(0, propPathElements.size() - 1));
                String propName = propPathElements.get(propPathElements.size() - 1);
                folderNode.setProperty(propName, value);
            }
            if (status) {
                loadLine(repositoryData, splitDump[i]);
            }
        }
    }

    public FolderNode loadLine(RepositoryData repositoryData, String line) {
        FolderNode folderNode = new FolderNode();
        if (!line.contains("=")) {
            String[] nodeList = line.split("/");
            List<String> pathElements = getPathElements(line);
            folderNode = loadPath(repositoryData, pathElements);
            folderNode.setPath(line);
        } else {
            String propPath = line.substring(0, line.indexOf("=")).trim();
            List<String> propPathElements = getPathElements(propPath);
            folderNode = loadPath(repositoryData, propPathElements.subList(0, propPathElements.size() - 1));
            String propName = propPathElements.get(propPathElements.size() - 1);
            String value = line.substring(line.indexOf("=") + 1, line.length()).trim();
            folderNode.setProperty(propName, value);
        }
        return folderNode;
    }

    private FolderNode loadPath(RepositoryData repositoryData, List<String> pathElements) {
        FolderNode folderNode = repositoryData.getRootNode();
        for (int i = 0; i < pathElements.size(); i++) {
            folderNode = getChildNode(folderNode, pathElements.get(i));
        }
        return folderNode;
    }

    private FolderNode getChildNode(FolderNode folderNode, String childNodeName) {
        List<DocStoreNode> childNodes = folderNode.getChildren();
        if (childNodes == null) {
            childNodes = new ArrayList<DocStoreNode>();
            folderNode.setChildren(childNodes);
        }
        FolderNode childNode = null;
        for (DocStoreNode node : childNodes) {
            if (node instanceof FolderNode && node.getName().equalsIgnoreCase(childNodeName)) {
                childNode = (FolderNode) node;
                break;
            }

        }
        if (childNode == null) {
            childNode = new FolderNode();
            childNode.setName(childNodeName);
            childNodes.add(childNode);
        }

        return childNode;
    }

    private List<String> getPathElements(String line) {
        String[] nodeList = line.split("/");
        List<String> pathElements = new ArrayList<String>();
        for (int i = 0; i < nodeList.length; i++) {
            if (nodeList[i] != null && nodeList[i].length() > 0) {
                pathElements.add(nodeList[i]);
            }
        }
        return pathElements;
    }

    private boolean content(String data, boolean status, StringBuilder content, String path) {
        status = false;
        if (data.contains(path) && !data.contains("jcr:data")) {
            status = true;
            return status;
        }
        if (data.contains("jcr:data")) {
            content.append(data.substring(data.indexOf("=") + 1, data.length()).trim());
        } else {
            content.append("\n" + data);
        }
        return status;
    }

    public String getRepositoryDump(RepositoryData repositoryData) {
        StringBuffer repositoryDump = new StringBuffer();
        repositoryDump(repositoryData.getRootNode(), repositoryDump);
        return repositoryDump.toString();
    }

    private void repositoryDump(FolderNode rootNode, StringBuffer repositoryDump) {
        DocStoreNode docStoreNode = rootNode;
        repositoryDump.append(rootNode.toString());
        if (rootNode != null && rootNode.getChildren() != null && rootNode.getChildren().size() > 0) {
            for (Iterator<DocStoreNode> folderIterator = rootNode.getChildren().iterator(); folderIterator.hasNext(); ) {
                repositoryDump((FolderNode) folderIterator.next(), repositoryDump);
            }
        }
    }
}
