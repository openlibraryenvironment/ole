/*
 * Copyright 2011 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole;

import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.repopojo.FolderNode;
import org.kuali.ole.docstore.model.repopojo.RepositoryData;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.kuali.ole.logger.DocStoreLogger;
import org.kuali.ole.logger.MetricsLogger;
import org.kuali.ole.pojo.OleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: peris
 * Date: 5/3/11
 * Time: 10:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class RepositoryBrowser {
    private DocStoreLogger docStoreLogger = new DocStoreLogger(this.getClass().getName());
    private MetricsLogger metricsLogger;
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());


    public void browseRepositoryContent() throws OleException {
        Session session = RepositoryManager.getRepositoryManager()
                .getSession("repositoryBrowser", "browseRepositoryContent");
        try {
            Node root = session.getRootNode();
            List<OleDocStoreData> oleDocStoreDatas = browseDataSetup();
            for (Iterator<OleDocStoreData> iterator = oleDocStoreDatas.iterator(); iterator.hasNext(); ) {
                OleDocStoreData oleDocStoreData = iterator.next();
                if (root.hasNode(oleDocStoreData.getCategory())) {
                    Node categoryNode = root.getNode(oleDocStoreData.getCategory());
                    NodeIterator nodes = categoryNode.getNodes();
                    while (nodes.hasNext()) {
                        Node node = nodes.nextNode();
                        NodeIterator childNodes = node.getNodes();
                        while (childNodes.hasNext()) {
                            Node childOfChildNode = childNodes.nextNode();
                            NodeIterator fileNodes = childOfChildNode.getNodes();
                            while (fileNodes.hasNext()) {
                                Node nextNode = fileNodes.nextNode();
                                if (nextNode.hasNode("jcr:content")) {
                                }
                            }

                        }
                    }
                }

            }
        } catch (RepositoryException e) {
            throw new OleException(e.getMessage());
        } finally {
            RepositoryManager.getRepositoryManager().logout(session);
        }

    }

    public List<OleDocStoreData> getFilesCount() throws OleException {
        List<OleDocStoreData> oleDocStoreDatas;
        getMetricsLogger().startRecording();
        Session session = RepositoryManager.getRepositoryManager().getSession("repositoryBrowser", "fileCount");
        Map<String, List<String>> formatLevelMap = new HashMap<String, List<String>>();
        try {
            Node root = session.getRootNode();
            oleDocStoreDatas = browseDataSetup();
            for (Iterator<OleDocStoreData> iterator = oleDocStoreDatas.iterator(); iterator.hasNext(); ) {
                OleDocStoreData oleDocStoreData = iterator.next();
                if (root.hasNode(oleDocStoreData.getCategory())) {
                    Node categoryNode = root.getNode(oleDocStoreData.getCategory());
                    Map<String, List<String>> typeFormatMap = oleDocStoreData.getTypeFormatMap();
                    Set<String> types = typeFormatMap.keySet();
                    for (Iterator<String> catIterator = types.iterator(); catIterator.hasNext(); ) {
                        String type = catIterator.next();
                        if (categoryNode.hasNode(type)) {
                            Node typeNode = categoryNode.getNode(type);
                            List<String> formats = typeFormatMap.get(type);
                            Map<String, Long> countMap = new HashMap<String, Long>();
                            if (formats != null && formats.size() > 0) {
                                for (Iterator<String> typIterator = formats.iterator(); typIterator.hasNext(); ) {
                                    String format = typIterator.next();
                                    if (format.equalsIgnoreCase(DocFormat.MARC.getCode())) {
                                        List<String> levelNodes = new ArrayList<String>();
                                        levelNodes.add(ProcessParameters.NODE_LEVEL1);
                                        formatLevelMap.put(DocFormat.MARC.getCode(), levelNodes);
                                    } else if (format.equalsIgnoreCase(DocFormat.DUBLIN_CORE.getCode())) {
                                        List<String> levelNodes = new ArrayList<String>();
                                        levelNodes.add(ProcessParameters.NODE_LEVEL1);
                                        formatLevelMap.put(DocFormat.DUBLIN_CORE.getCode(), levelNodes);
                                    } else if (format.equalsIgnoreCase(DocFormat.DUBLIN_UNQUALIFIED.getCode())) {
                                        List<String> levelNodes = new ArrayList<String>();
                                        levelNodes.add(ProcessParameters.NODE_LEVEL1);
                                        formatLevelMap.put(DocFormat.DUBLIN_UNQUALIFIED.getCode(), levelNodes);
                                    } else if (format.equalsIgnoreCase("oleml") && type.equalsIgnoreCase("instance")) {
                                        List<String> levelNodes = new ArrayList<String>();
                                        levelNodes.add(ProcessParameters.NODE_LEVEL1);
                                        formatLevelMap.put("oleml", levelNodes);
                                    }
                                    if (typeNode.hasNode(format)) {
                                        Node formatNode = typeNode.getNode(format);
                                        List<String> folderL1list = formatLevelMap.get(format);
                                        if (folderL1list != null && folderL1list.size() > 0) {
                                            for (Iterator<String> forIterator = folderL1list.iterator(); forIterator
                                                    .hasNext(); ) {
                                                String levelL1 = forIterator.next();
                                                if (format.equalsIgnoreCase(DocFormat.MARC.getCode())) {
                                                    List<String> levelNodes = new ArrayList<String>();
                                                    levelNodes.add(ProcessParameters.NODE_LEVEL2);
                                                    formatLevelMap.put(ProcessParameters.NODE_LEVEL1, levelNodes);
                                                } else if (format.equalsIgnoreCase(DocFormat.DUBLIN_CORE.getCode())) {
                                                    List<String> levelNodes = new ArrayList<String>();
                                                    levelNodes.add(ProcessParameters.NODE_LEVEL2);
                                                    formatLevelMap.put(ProcessParameters.NODE_LEVEL1, levelNodes);
                                                } else if (format
                                                        .equalsIgnoreCase(DocFormat.DUBLIN_UNQUALIFIED.getCode())) {
                                                    List<String> levelNodes = new ArrayList<String>();
                                                    levelNodes.add(ProcessParameters.NODE_LEVEL2);
                                                    formatLevelMap.put(ProcessParameters.NODE_LEVEL1, levelNodes);
                                                } else if (format.equalsIgnoreCase("oleml") && type
                                                        .equalsIgnoreCase("instance")) {
                                                    List<String> levelNodes = new ArrayList<String>();
                                                    levelNodes.add(ProcessParameters.NODE_LEVEL2);
                                                    formatLevelMap.put(ProcessParameters.NODE_LEVEL1, levelNodes);
                                                }
                                                //                                        addFormatCount(folder12Node, file, format, countMap, type,oleDocStoreData);
                                                if (formatNode.hasNode(levelL1)) {
                                                    Node levelL1Node = formatNode.getNode(levelL1);
                                                    List<String> folder12List = formatLevelMap.get(levelL1);
                                                    if (folder12List != null && folder12List.size() > 0) {
                                                        for (Iterator<String> levelL1Iterator = folder12List
                                                                .iterator(); levelL1Iterator.hasNext(); ) {
                                                            String levelL2 = levelL1Iterator.next();

                                                            if (format.equalsIgnoreCase(DocFormat.MARC.getCode())) {
                                                                List<String> levelNodes = new ArrayList<String>();
                                                                levelNodes.add(ProcessParameters.NODE_LEVEL3);
                                                                formatLevelMap
                                                                        .put(ProcessParameters.NODE_LEVEL2, levelNodes);
                                                            } else if (format.equalsIgnoreCase(
                                                                    DocFormat.DUBLIN_CORE.getCode())) {
                                                                List<String> levelNodes = new ArrayList<String>();
                                                                levelNodes.add(ProcessParameters.NODE_LEVEL3);
                                                                formatLevelMap
                                                                        .put(ProcessParameters.NODE_LEVEL2, levelNodes);
                                                            } else if (format.equalsIgnoreCase(
                                                                    DocFormat.DUBLIN_UNQUALIFIED.getCode())) {
                                                                List<String> levelNodes = new ArrayList<String>();
                                                                levelNodes.add(ProcessParameters.NODE_LEVEL3);
                                                                formatLevelMap
                                                                        .put(ProcessParameters.NODE_LEVEL2, levelNodes);
                                                            } else {
                                                                addFormatCount(levelL1Node, levelL2, format, countMap,
                                                                        type, oleDocStoreData);
                                                            }
                                                            if (levelL1Node.hasNode(levelL2)) {
                                                                Node folder12Node = levelL1Node.getNode(levelL2);
                                                                List<String> folderL3List = formatLevelMap.get(levelL2);
                                                                if (folderL3List != null && folderL3List.size() > 0) {
                                                                    for (Iterator<String> levelL2Iterator = folderL3List
                                                                            .iterator(); levelL2Iterator.hasNext(); ) {
                                                                        String file = levelL2Iterator.next();
                                                                        addFormatCount(folder12Node, file, format,
                                                                                countMap, type, oleDocStoreData);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (!type.equalsIgnoreCase(DocType.INSTANCE.getCode())
                                            && !format.equalsIgnoreCase(DocFormat.MARC.getCode()) && !format
                                            .contains("dublin")) {
                                        addFormatCount(typeNode, format, format, countMap, type, oleDocStoreData);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            getMetricsLogger().endRecording();
            getMetricsLogger().printTimes("Getting Files count took:: ");
        } catch (RepositoryException e) {
            throw new OleException(e.getMessage());
        } finally {
            RepositoryManager.getRepositoryManager().logout(session);
        }
        return oleDocStoreDatas;
    }

    private void addFormatCount(Node levelL1Node, String levelL2, String format, Map<String, Long> countMap,
                                String type, OleDocStoreData oleDocStoreData) throws RepositoryException {
        Long count = 0L;
        NodeIterator L2Nodes = levelL1Node.getNodes(levelL2);
        Map<String, Map<String, Long>> typeFormatMapWithNodeCount = oleDocStoreData.getTypeFormatMapWithNodeCount();
        while (L2Nodes.hasNext()) {
            Node fileNode = (Node) L2Nodes.next();
            NodeIterator filesNodes = fileNode.getNodes();
            if (countMap.get(format) != null) {
                count = countMap.get(format);
            }
            count = count + filesNodes.getSize();
            countMap.put(format, count);
        }
        typeFormatMapWithNodeCount.put(type, countMap);
    }


    /* public Map<String, Integer> getDocumentCount() throws OleException, RepositoryException {
        Map<String, Integer> documentCountMap = new HashMap<String, Integer>();
        RepositoryManager oleRepositoryManager = RepositoryManager.getRepositoryManager();
        Node node = oleRepositoryManager.getSession().getRootNode();
        getDocumentCount(node, documentCountMap);
        return documentCountMap;
    }

    private void getDocumentCount(Node node, Map<String, Integer> documentCountMap) throws RepositoryException {
        if (node.getName().equals("jcr:system")) {
            return;
        }
        PropertyIterator properties = node.getProperties();
        while (properties.hasNext()) {
            Property property = properties.nextProperty();
            if (property.getName().equals("jcr:primaryType") && property.getString().equalsIgnoreCase("olefile")) {
                String formatKey = property.getPath().substring(1, property.getPath().lastIndexOf("/"));
                formatKey = formatKey.substring(0, formatKey.lastIndexOf("/"));
                if (formatKey.contains("[")) {
                    formatKey = formatKey.substring(0, formatKey.lastIndexOf("["));
                }
                if (formatKey != null) {
                    if (documentCountMap.containsKey(formatKey)) {
                        int count = documentCountMap.get(formatKey);
                        documentCountMap.put(formatKey, ++count);
                    }
                    else {
                        documentCountMap.put(formatKey, 1);
                    }
                }
            }
        }
        NodeIterator nodes = node.getNodes();
        while (nodes.hasNext()) {
            getDocumentCount(nodes.nextNode(), documentCountMap);
        }                                                                    `
    }*/

    public String getRepositoryDump() throws OleException, RepositoryException {
        StringBuffer repositoryDump = new StringBuffer();
        RepositoryManager oleRepositoryManager = RepositoryManager.getRepositoryManager();
        Session session = oleRepositoryManager.getSession("repositoryBrowser", "getRepositoryDump");
        repositoryDump(session.getRootNode(), repositoryDump);
        oleRepositoryManager.logout(session);
        return repositoryDump.toString();
    }

    private void repositoryDump(Node node, StringBuffer repositoryDump) throws RepositoryException {
        repositoryDump.append(node.getPath() + "\n");
        if (node.getName().equals("jcr:system")) {
            return;
        }
        PropertyIterator properties = node.getProperties();
        while (properties.hasNext()) {
            Property property = properties.nextProperty();
            if (property.getDefinition().isMultiple()) {
                Value[] values = property.getValues();
                for (int i = 0; i < values.length; i++) {
                    repositoryDump.append(property.getPath() + " = " + values[i].getString() + "\n");
                }
            } else {
                repositoryDump.append(property.getPath() + " = " + property.getString() + "\n");
            }
        }
        NodeIterator nodes = node.getNodes();
        while (nodes.hasNext()) {
            repositoryDump(nodes.nextNode(), repositoryDump);
        }
    }

    public String getRepositoryRangeDump(String category, String type, String format, int fromIndex, int count)
            throws OleException, RepositoryException {
        StringBuffer repositoryDump = new StringBuffer();
        RepositoryManager oleRepositoryManager = RepositoryManager.getRepositoryManager();
        Node rootnode = oleRepositoryManager.getSession("repositoryBrowser", "getRepositoryRangeDump").getRootNode();
        repositoryRangeDump(rootnode, repositoryDump, category, type, format, fromIndex, count);
        return repositoryDump.toString();
    }

    private void repositoryRangeDump(Node rootnode, StringBuffer repositoryDump, String category, String type,
                                     String format, int fromIndex, int count) throws RepositoryException {
        int tempCount = 0;
        Node categoryNode = rootnode.getNode(category);
        Node typeNode = categoryNode.getNode(type);
        Node formatNode = typeNode.getNode(format);
        repositoryDump.append(categoryNode.toString().replace("node ", "") + "\n");
        getNodeDump(categoryNode, repositoryDump);
        repositoryDump.append(typeNode.toString().replace("node ", "") + "\n");
        getNodeDump(typeNode, repositoryDump);
        repositoryDump.append(formatNode.toString().replace("node ", "") + "\n");
        getNodeDump(formatNode, repositoryDump);
        for (Iterator<Node> formatiterator = formatNode.getNodes(); formatiterator.hasNext(); ) {
            Node fileNode = formatiterator.next();
            PropertyIterator properties = fileNode.getProperties();
            while (properties.hasNext()) {
                Property property = properties.nextProperty();
                if (property.getName().equals("jcr:primaryType") && property.getString().equalsIgnoreCase(
                        ProcessParameters.FILE_OLE)) {
                    tempCount++;
                    if (tempCount >= fromIndex && tempCount < fromIndex + count) {
                        repositoryDump.append(fileNode.toString().replace("node ", "") + "\n");
                        getNodeDump(fileNode, repositoryDump);
                        NodeIterator nodes = fileNode.getNodes();
                        while (nodes.hasNext()) {
                            getNodeDump(nodes.nextNode(), repositoryDump);
                        }
                    }
                }
            }
        }
    }

    public void getNodeDump(Node node, StringBuffer repositoryDump) throws RepositoryException {
        PropertyIterator properties = node.getProperties();
        while (properties.hasNext()) {
            Property property = properties.nextProperty();
            if (property.getDefinition().isMultiple()) {
                Value[] values = property.getValues();
                for (int i = 0; i < values.length; i++) {
                    repositoryDump.append(property.getPath() + " = " + values[i].getString() + "\n");
                }
            } else {
                repositoryDump.append(property.getPath() + " = " + property.getString() + "\n");
            }
        }
    }

    public List<OleDocStoreData> browseDataSetup() {
        List<OleDocStoreData> oleDocStoreDatas = new ArrayList<OleDocStoreData>();
        InputStream in = RepositoryBrowser.class.getResourceAsStream("docstore-category.properties");
        Properties categories = getPropertyValues(in);
        Set<Map.Entry<Object, Object>> entries = categories.entrySet();
        Map.Entry<Object, Object> next = entries.iterator().next();
        String value = (String) next.getValue();
        StringTokenizer tokenizer = new StringTokenizer(value, ",");
        while (tokenizer.hasMoreTokens()) {
            OleDocStoreData oleDocStoreData = new OleDocStoreData();
            oleDocStoreData.setCategory(tokenizer.nextToken());
            setDocTypes(oleDocStoreData);
            setFormats(oleDocStoreData);
            oleDocStoreDatas.add(oleDocStoreData);
        }
        return oleDocStoreDatas;
    }

    private void setFormats(OleDocStoreData oleDocStoreData) {
        InputStream in = RepositoryBrowser.class.getResourceAsStream("docstore-format.properties");
        Properties categories = getPropertyValues(in);
        Set<Map.Entry<Object, Object>> entries = categories.entrySet();
        for (Iterator<Map.Entry<Object, Object>> iterator = entries.iterator(); iterator.hasNext(); ) {
            Map.Entry<Object, Object> entry = iterator.next();
            String value = (String) entry.getValue();
            StringTokenizer tokenizer = new StringTokenizer(value, ",");
            if ((!oleDocStoreData.getDoctypes().isEmpty() && oleDocStoreData.getDoctypes().contains(entry.getKey()))) {
                while (tokenizer.hasMoreTokens()) {
                    oleDocStoreData.addFormat((String) entry.getKey(), tokenizer.nextToken());
                }
            }
        }

    }

    private void setDocTypes(OleDocStoreData oleDocStoreData) {
        InputStream in = RepositoryBrowser.class.getResourceAsStream("docstore-type.properties");
        Properties categories = getPropertyValues(in);
        Set<Map.Entry<Object, Object>> entries = categories.entrySet();
        for (Iterator<Map.Entry<Object, Object>> iterator = entries.iterator(); iterator.hasNext(); ) {
            Map.Entry<Object, Object> entry = iterator.next();
            String value = (String) entry.getValue();
            StringTokenizer tokenizer = new StringTokenizer(value, ",");
            if (entry.getKey().equals(oleDocStoreData.getCategory())) {
                while (tokenizer.hasMoreTokens()) {
                    oleDocStoreData.addDocType(tokenizer.nextToken());
                }
            }
        }
    }

    private Properties getPropertyValues(InputStream inputStream) {
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            docStoreLogger.log("Unable to load properties", e);
        }
        return properties;
    }

    public List<String> getUUIDs(String category, String type, String format, Integer numUUIDs) throws OleException {
        Node rootNode = null;
        ArrayList<String> uuids = new ArrayList<String>();
        Session session = null;
        try {
            session = RepositoryManager.getRepositoryManager().getSession("repositoryBrowser", "getUUIDs");
            String docType = new String(type);
            LOG.info("doc type in repository browser-->" + docType);
            rootNode = session.getRootNode();
            Node categoryNode = rootNode.getNode(category);
            if (type.equalsIgnoreCase(DocType.HOLDINGS.getCode()) || type.equalsIgnoreCase(DocType.ITEM.getCode())) {
                type = "instance";
            }
            Node typeNode = categoryNode.getNode(type);
            Node formatNode = typeNode.getNode(format);
            if (DocType.BIB.getDescription().equalsIgnoreCase(type)) {
                formatNode = formatNode.getNode(ProcessParameters.NODE_LEVEL1).getNode(ProcessParameters.NODE_LEVEL2)
                        .getNode(ProcessParameters.NODE_LEVEL3);
            } else if (DocType.LICENSE.getDescription().equals(type)) {
                formatNode = formatNode.getNode(ProcessParameters.NODE_LEVEL1);
            } else {
                formatNode = formatNode.getNode(ProcessParameters.NODE_LEVEL1).getNode(ProcessParameters.NODE_LEVEL2);

            }
            NodeIterator nodes = formatNode.getNodes();
            int count = 0;
            while (nodes.hasNext()) {
                Node node = nodes.nextNode();
                if (docType.equalsIgnoreCase(DocType.HOLDINGS.getCode())) {
                    node = node.getNode(ProcessParameters.NODE_HOLDINGS).getNode(ProcessParameters.FILE_HOLDINGS);
                    if (count < numUUIDs) {
                        uuids.add(node.getIdentifier());
                        count++;

                    }
                } else if (docType.equalsIgnoreCase(DocType.ITEM.getCode())) {
                    NodeIterator itemIterator = node.getNode(ProcessParameters.NODE_HOLDINGS)
                            .getNodes(ProcessParameters.FILE_ITEM);
                    while (itemIterator.hasNext()) {
                        Node itemNode = itemIterator.nextNode();
                        if (count < numUUIDs) {
                            uuids.add(itemNode.getIdentifier());
                            count++;
                        }
                    }

                } else if (count < numUUIDs) {
                    uuids.add(node.getIdentifier());
                    count++;
                }
            }
        } catch (PathNotFoundException e) {
            LOG.info(e.getMessage());
        } catch (RepositoryException e) {
            LOG.info(e.getMessage());
        } finally {
            RepositoryManager.getRepositoryManager().logout(session);
        }
        return uuids;
    }

    public List<String> getUUIDs(String category, String type, String format, Integer startIndex, Integer endIndex)
            throws OleException {
        Node rootNode = null;
        ArrayList<String> uuids = new ArrayList<String>();
        Session session = null;
        endIndex = startIndex + endIndex;
        try {
            session = RepositoryManager.getRepositoryManager().getSession("repositoryBrowser", "getUUIDs");
            String docType = new String(type);
            LOG.debug("doc type in repository browser-->" + docType);
            rootNode = session.getRootNode();
            Node categoryNode = rootNode.getNode(category);
            if (type.equalsIgnoreCase(DocType.HOLDINGS.getCode()) || type.equalsIgnoreCase(DocType.ITEM.getCode())) {
                type = "instance";
            }
            Node typeNode = categoryNode.getNode(type);
            Node formatNode = typeNode.getNode(format);
            if (DocType.BIB.getDescription().equalsIgnoreCase(type)) {
                formatNode = formatNode.getNode(ProcessParameters.NODE_LEVEL1).getNode(ProcessParameters.NODE_LEVEL2)
                        .getNode(ProcessParameters.NODE_LEVEL3);
            } else if (DocType.LICENSE.getDescription().equals(type)) {
                formatNode = formatNode.getNode(ProcessParameters.NODE_LEVEL1);
            } else {
                formatNode = formatNode.getNode(ProcessParameters.NODE_LEVEL1).getNode(ProcessParameters.NODE_LEVEL2);

            }
            NodeIterator nodes = formatNode.getNodes();
            int count = 1;
            while (nodes.hasNext()) {
                Node node = nodes.nextNode();
                if (docType.equalsIgnoreCase(DocType.HOLDINGS.getCode())) {
                    node = node.getNode(ProcessParameters.NODE_HOLDINGS).getNode(ProcessParameters.FILE_HOLDINGS);
                    if (count >= startIndex && count < endIndex) {
                        uuids.add(node.getIdentifier());

                    }
                } else if (docType.equalsIgnoreCase(DocType.ITEM.getCode())) {
                    NodeIterator itemIterator = node.getNode(ProcessParameters.NODE_HOLDINGS)
                            .getNodes(ProcessParameters.FILE_ITEM);
                    while (itemIterator.hasNext()) {
                        Node itemNode = itemIterator.nextNode();
                        if (count >= startIndex && count < endIndex) {
                            uuids.add(itemNode.getIdentifier());
                        }
                    }

                } else if (count >= startIndex && count < endIndex) {
                    uuids.add(node.getIdentifier());
                }
                count++;
            }
        } catch (PathNotFoundException e) {
            LOG.info(e.getMessage());
        } catch (RepositoryException e) {
            LOG.info(e.getMessage());
        } finally {
            RepositoryManager.getRepositoryManager().logout(session);
        }
        return uuids;
    }


    private MetricsLogger getMetricsLogger() {
        if (null == metricsLogger) {
            metricsLogger = new MetricsLogger(this.getClass().getName());
        }
        return metricsLogger;
    }


    public String generateNodeCount() throws OleException, RepositoryException {
        RepositoryManager oleRepositoryManager = RepositoryManager.getRepositoryManager();
        RepositoryData repositoryData = new RepositoryData();
        Session session = oleRepositoryManager.getSession("repositoryBrowser", "generateNodeCount");
        FolderNode folderNode = computeNodeCount(session.getRootNode(), repositoryData);
        String repoNodeCountDump = repositoryData.getRepositoryDump(repositoryData);
        oleRepositoryManager.logout(session);
        return repoNodeCountDump;
    }

    public FolderNode computeNodeCount(Node node, RepositoryData repositoryData) throws RepositoryException {
        Long size = null;
        FolderNode parentNode = null;
        if (node != null) {
            if (!node.hasProperty("nodeType") && !node.getName().equalsIgnoreCase("") && node.getName()
                    .equalsIgnoreCase(
                            "jcr:system")) {
                return null;
            } else {
                parentNode = repositoryData.loadLine(repositoryData, node.getPath());
                if (isLastFolderNode(node)) {
                    size = new Long(node.getNodes().getSize());
                    parentNode.setProperty(parentNode.FILE_NODE_COUNT, size);
                } else {
                    long count = 0;
                    long longValue = 0;
                    for (Iterator<NodeIterator> it = node.getNodes(); it.hasNext(); ) {
                        Node childNode = (Node) it.next();
                        FolderNode folderNode = computeNodeCount(childNode, repositoryData);
                        if (folderNode != null && folderNode.getPropertyMap() != null) {
                            Object value = folderNode.getPropertyMap().get(folderNode.FILE_NODE_COUNT);
                            if (value instanceof Long) {
                                longValue = ((Long) value).longValue();
                            }
                            if (value != null) {
                                count = count + longValue;
                            }
                        }
                    }
                    if (parentNode != null) {
                        size = new Long(count);
                        parentNode.setProperty(parentNode.FILE_NODE_COUNT, size);
                    }
                }
            }
        }
        return parentNode;
    }

    private boolean isLastFolderNode(Node node) throws RepositoryException {
        boolean status = false;
        if (node.getPath().contains(DocType.BIB.getDescription()) && node.getName()
                .startsWith(ProcessParameters.NODE_LEVEL1)) {
            status = true;
        } else if (node.getPath().contains(DocType.INSTANCE.getDescription()) && node.getName().startsWith(
                ProcessParameters.NODE_LEVEL2)) {
            status = true;
        } else if (node.getPath().contains(DocType.PATRON.getDescription())) {
            status = true;
        } else if (node.getPath().contains(DocType.LICENSE.getDescription()) && node.getName().startsWith(
                ProcessParameters.NODE_LEVEL1)) {
            status = true;
        }
        return status;
    }
}
