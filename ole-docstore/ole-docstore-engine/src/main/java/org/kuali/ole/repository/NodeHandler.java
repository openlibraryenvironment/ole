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
package org.kuali.ole.repository;

import org.apache.commons.io.FileUtils;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.DocStoreConstants;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.documenthandler.WorkBibMarcContentHandler;
import org.kuali.ole.documenthandler.WorkInstanceOleMLContentHandler;
import org.kuali.ole.pojo.OleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.kuali.ole.docstore.process.ProcessParameters.*;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 9/11/11
 * Time: 10:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class NodeHandler {

    private static final Logger logger = LoggerFactory.getLogger(NodeHandler.class);

    public Node initStaticNode(String nodeName, Node parentNode, Session session) throws RepositoryException {
        Node node;
        if (!parentNode.hasNode(nodeName)) {
            synchronized (session) {
                node = parentNode.addNode(nodeName, "nt:unstructured");
                node.setProperty("nodeType", "folder");
                node.addMixin("mix:referenceable");
                session.save();
            }
        } else {
            node = parentNode.getNode(nodeName);
        }
        return node;
    }

    public Node initNonStaticNode(String nodeName, Node parentNode) throws RepositoryException {
        Node node;
        node = parentNode.addNode(nodeName, "nt:unstructured");
        node.setProperty("nodeType", "folder");
        node.addMixin("mix:referenceable");
        return node;
    }

    public synchronized Node initFileNode(RequestDocument document, String name, Node parentNode, Session session)
            throws Exception {
        DocStoreConstants docStoreConstants = new DocStoreConstants();
        String uuid = null;
        Node fileNode = null;
        try {
            NodeIterator nodes = parentNode.getNodes(name);
            if (nodes.getSize() >= BUCKET_SIZE_FILE_NODES) {
                if (DocFormat.OLEML.isEqualTo(document.getFormat())) {
                    throw new RuntimeException("FileNode creation failed as the BUCKET_SIZE[" + BUCKET_SIZE_FILE_NODES
                            + "] is FULL: for the doc: " + document.getFormat() + "\n@ level: "
                            + parentNode.getPath() + "/" + name + "[" + (nodes.getSize() + 1) + "]");
                } else {
                    parentNode = initLevelNode(parentNode.getName(), parentNode.getParent(), true, session);
                }
            }

            fileNode = parentNode.addNode(name, "olefile");
            fileNode.addMixin("mix:referenceable");
            if (DocType.LICENSE.isEqualTo(document.getType()) || docStoreConstants.isVersioningEnabled) {
                fileNode.addMixin("mix:versionable");
            }

            AdditionalAttributes additionalAttributes = document.getAdditionalAttributes();
            if (DocType.LICENSE.isEqualTo(document.getType()) && !DocFormat.ONIXPL.isEqualTo(document.getFormat())) {
                String docName = new File(document.getDocumentName()).getName();
                additionalAttributes.setAttribute("dateLoaded", Calendar.getInstance().toString());
                additionalAttributes.setAttribute("fileName", docName);
                additionalAttributes.setAttribute("owner", document.getUser());
            }

            if (additionalAttributes != null) {
                Collection<String> attributeNames = additionalAttributes.getAttributeNames();
                if (attributeNames != null && attributeNames.size() > 0) {
                    for (Iterator<String> iterator = attributeNames.iterator(); iterator.hasNext(); ) {
                        String attributeName = iterator.next();
                        String attributeValue = additionalAttributes.getAttribute(attributeName);
                        fileNode.setProperty(attributeName, attributeValue);
                    }
                }

            } else {
                fileNode.setProperty("dateEntered", Calendar.getInstance());
                fileNode.setProperty("lastUpdated", Calendar.getInstance());
            }

            Node resNode = fileNode.addNode("jcr:content", "nt:resource");
            resNode.setProperty("jcr:mimeType", "application/xml");
            resNode.setProperty("jcr:encoding", "");

            String charset = "UTF-8";
            byte[] documentBytes = null;
            try {
                uuid = fileNode.getIdentifier();
                document.setUuid(uuid);
                // Content Manipulations
                if (DocFormat.MARC.isEqualTo(document.getFormat())) {
                    new WorkBibMarcContentHandler().doPreIngestContentManipulations(document, uuid);
                } else if (DocFormat.OLEML.isEqualTo(document.getFormat())) {
                    (new WorkInstanceOleMLContentHandler())
                            .doInstanceOleMLContentManipulations(document, uuid, parentNode);
                }

                if (document.getContent() != null && document.getContent().getContent() != null) {
                    documentBytes = document.getContent().getContent().getBytes();
                } else if (document.getDocumentName() != null) {
                    File file = new File(document.getDocumentName());
                    if (file.exists()) {
                        documentBytes = FileUtils.readFileToByteArray(file);
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to convert document string to byte[] with charset " + charset, e);
            }
            InputStream docInputStream = new ByteArrayInputStream(documentBytes);
            Binary binary = session.getValueFactory().createBinary(docInputStream);
            resNode.setProperty("jcr:data", binary);
            Calendar lastModified = Calendar.getInstance();
            lastModified.setTimeInMillis(new Date().getTime());
            resNode.setProperty("jcr:lastModified", lastModified);
            logger.debug(fileNode.getPath() + " : " + uuid);
        } catch (RepositoryException e) {
            logger.error("File Node Cannot be Created: " + e.getMessage(), e);
        }
        return fileNode;
    }

    /**
     * Initializes the given fileNode with info from given requestDocument.
     *
     * @param fileNode
     * @param document
     * @param name
     * @param parentNode
     * @param session
     * @return
     * @throws Exception
     */
    public synchronized Node initFileNode(Node fileNode, RequestDocument document, String name, Node parentNode,
                                          Session session) throws Exception {
        String uuid = null;
        //Node fileNode = null;
        DocStoreConstants docStoreConstants = new DocStoreConstants();
        try {
            /*NodeIterator nodes = parentNode.getNodes(name);
            if (nodes.getSize() >= BUCKET_SIZE_FILE_NODES) {
                if (DocFormat.OLEML.isEqualTo(document.getFormat()))
                    throw new RuntimeException("FileNode creation failed as the BUCKET_SIZE[" + BUCKET_SIZE_FILE_NODES + "] is FULL: for the doc: "
                            + document.getFormat() + "\n@ level: " + parentNode.getPath() + "/" + name + "[" + (nodes.getSize() + 1) + "]");
                else
                    parentNode = initLevelNode(parentNode.getName(), parentNode.getParent(), true, session);
            }

            fileNode = parentNode.addNode(name, "olefile"); */
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
            Date date = new Date();
            //logger.info("NODE INIT: STARTING TIME \t" + dateFormat.format(date));
            fileNode.addMixin("mix:referenceable");
            if (DocType.LICENSE.isEqualTo(document.getType()) || docStoreConstants.isVersioningEnabled) {
                fileNode.addMixin("mix:versionable");
            }

            AdditionalAttributes additionalAttributes = document.getAdditionalAttributes();
            if (DocType.LICENSE.isEqualTo(document.getType()) && !DocFormat.ONIXPL.isEqualTo(document.getFormat())) {
                String docName = new File(document.getDocumentName()).getName();
                additionalAttributes.setAttribute("dateLoaded", Calendar.getInstance().toString());
                additionalAttributes.setAttribute("fileName", docName);
                additionalAttributes.setAttribute("owner", document.getUser());
            }

            if (additionalAttributes != null) {
                Collection<String> attributeNames = additionalAttributes.getAttributeNames();
                if (attributeNames != null && attributeNames.size() > 0) {
                    for (Iterator<String> iterator = attributeNames.iterator(); iterator.hasNext(); ) {
                        String attributeName = iterator.next();
                        String attributeValue = additionalAttributes.getAttribute(attributeName);
                        fileNode.setProperty(attributeName, attributeValue);
                    }
                }

            }
            //            else {
            //                fileNode.setProperty("dateEntered", Calendar.getInstance());
            //                fileNode.setProperty("lastUpdated", Calendar.getInstance());
            //            }

            Node resNode = fileNode.addNode("jcr:content", "nt:resource");
            resNode.setProperty("jcr:mimeType", "application/xml");
            resNode.setProperty("jcr:encoding", "");

            String charset = "UTF-8";
            byte[] documentBytes = null;
            try {
                uuid = fileNode.getIdentifier();
                document.setUuid(uuid);
                // Content Manipulations
                if (DocFormat.MARC.isEqualTo(document.getFormat())) {
                    new WorkBibMarcContentHandler().doPreIngestContentManipulations(document, uuid);
                } else if (DocFormat.OLEML.isEqualTo(document.getFormat())) {
                    (new WorkInstanceOleMLContentHandler())
                            .doInstanceOleMLContentManipulations(document, uuid, parentNode);
                }

                if (document.getContent() != null && document.getContent().getContent() != null) {
                    documentBytes = document.getContent().getContent().getBytes();
                } else if (document.getDocumentName() != null) {
                    File file = new File(document.getDocumentName());
                    if (file.exists()) {
                        documentBytes = FileUtils.readFileToByteArray(file);
                    }
                }


            } catch (Exception e) {
                logger.error("Failed to convert document string to byte[] with charset " + charset, e);
            }
            InputStream docInputStream = new ByteArrayInputStream(documentBytes);
            Binary binary = session.getValueFactory().createBinary(docInputStream);
            resNode.setProperty("jcr:data", binary);
            Calendar lastModified = Calendar.getInstance();
            lastModified.setTimeInMillis(new Date().getTime());
            resNode.setProperty("jcr:lastModified", lastModified);
            logger.debug(fileNode.getPath() + " : " + uuid);
            //logger.info("NODE INIT: ENDING TIME \t" + dateFormat.format(date));
        } catch (RepositoryException e) {
            logger.error("File Node Cannot be Created: " + e.getMessage(), e);
        }

        return fileNode;
    }

    public synchronized Node initLevelNode(String name, Node parent, boolean isRecursiveCall, Session session)
            throws Exception {
        long existing = 0;
        try {
            long bucketSize = BUCKET_SIZES.get(name);
            boolean hasRepeatedChild = HAS_REPEATED_CHILD.get(name);
            if (parent.hasNode(name)) {
                NodeIterator existingNodes = parent.getNodes(name);
                existing = existingNodes.getSize();
                if (existing <= bucketSize && !((isRecursiveCall && existing == bucketSize) || (!hasRepeatedChild
                        && existing
                        == bucketSize))) {
                    if (hasRepeatedChild && !isRecursiveCall) {
                        existingNodes.skip(existing - 1);
                        return existingNodes.nextNode();
                    } else {
                        Node levelNode = initNonStaticNode(name, parent);
                        if (existing == 0) {
                            session.save();
                        }
                        return levelNode;
                    }
                } else {
                    if (!STATIC_NODES.contains(parent.getPath())) {
                        parent = initLevelNode(parent.getName(), parent.getParent(), true, session);
                        return initNonStaticNode(name, parent);
                    } else {
                        throw new Exception("Node [" + parent.getName() + "/" + name + "[" + (existing + 1)
                                + "]] Cannot Be Created. CAUSE: TREE [" + bucketSize + "] FULL ");
                    }
                }
            } else {
                return initNonStaticNode(name, parent);
            }
        } catch (Exception e) {
            try {
                logger.error(
                        "Exception While initializing Node: " + parent.getName() + "/" + name + "[" + (existing + 1)
                                + "] \t to Parent: " + parent.getName(), e);
            } catch (RepositoryException e1) {
            }
            throw e;
        }
    }

    public Node getNodeByUUID(Session session, String uuid) throws OleException {
        logger.debug("Started getting node for UUID:" + uuid);
        boolean isNewSession = false;
        if (null == session) {
            isNewSession = true;
            logger.debug("Initilalizing new session");
            session = RepositoryManager.getRepositoryManager().getSession("nodeHandler", "getNodeByUUID");
        }
        try {
            return session.getNodeByIdentifier(uuid);
        } catch (RepositoryException e) {
            throw new OleException("getNodeByUUID failed", e);
        } finally {
            if (isNewSession) {
                RepositoryManager.getRepositoryManager().logout(session);
            }
        }
    }

}
