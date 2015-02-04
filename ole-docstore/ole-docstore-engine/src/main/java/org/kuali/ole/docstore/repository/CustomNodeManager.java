package org.kuali.ole.docstore.repository;

import org.apache.commons.io.FileUtils;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.repository.NodeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.io.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import static org.kuali.ole.docstore.process.ProcessParameters.*;

/**
 * Implements custom way of creating levels and nodes.
 *
 * @author tirumalesh.b
 * @version %I%, %G%
 *          Date: 28/8/12 Time: 6:09 PM
 */
public class CustomNodeManager
        implements NodeManager {
    private static final Logger logger = LoggerFactory.getLogger(NodeHandler.class);
    private static CustomNodeManager ourInstance = new CustomNodeManager();

    protected int numLevels = 3;

    public static CustomNodeManager getInstance() {
        return ourInstance;
    }

    public CustomNodeManager() {
    }

    public Node getParentNode(RequestDocument requestDocument, Session session) throws OleDocStoreException {
        Node levelNode = null;
        try {
            Node formatNode = getStaticFormatNode(requestDocument, session);
            synchronized (this.getClass()) {
                for (int i = 1; i <= numLevels; i++) {
                    levelNode = initLevelNode("l" + i, formatNode, false, session);
                    formatNode = levelNode;
                }
                //                Node l1 = initLevelNode(NODE_LEVEL1, formatNode, false, session);
                //                Node l2 = initLevelNode(NODE_LEVEL2, l1, false, session);
                //                     l3 = initLevelNode(NODE_LEVEL3, l2, false, session);
            }
        } catch (Exception e) {
            throw new OleDocStoreException(e);
        }
        return levelNode;
    }


    @Override
    public void linkNodes(Node node, Node linkedDocumentNode, Session session) throws OleDocStoreException {
    }

    public Node getStaticFormatNode(RequestDocument doc, Session session) throws RepositoryException {
        Node root = session.getRootNode();
        Node categoryNode = initStaticNode(doc.getCategory(), root, session);
        Node typeNode = initStaticNode(doc.getType(), categoryNode, session);
        Node formatNode = initStaticNode(doc.getFormat(), typeNode, session);
        return formatNode;
    }

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

    protected void modifyAdditionalAttributes(AdditionalAttributes additionalAttributes,
                                              RequestDocument requestDocument) {
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

    @Deprecated
    public synchronized Node initFileNode(RequestDocument document, String name, Node parentNode, Session session)
            throws Exception {
        String uuid = null;
        Node fileNode = createFileNode(document, name, parentNode, session);
        //        Node contentNode = createContentNode(fileNode, document, parentNode, session);
        return fileNode;
    }

    public synchronized Node createFileNode(RequestDocument document, String name, Node parentNode, Session session)
            throws OleDocStoreException {
        Node fileNode = null;
        try {
            NodeIterator nodes = parentNode.getNodes(name);
            if (nodes.getSize() >= BUCKET_SIZE_FILE_NODES) {
                if (document != null && DocFormat.OLEML.isEqualTo(document.getFormat())) {
                    throw new RuntimeException("FileNode creation failed as the BUCKET_SIZE[" + BUCKET_SIZE_FILE_NODES
                            + "] is FULL: for the doc: " + document.getFormat() + "\n@ level: "
                            + parentNode.getPath() + "/" + name + "[" + (nodes.getSize() + 1) + "]");
                } else {
                    parentNode = initLevelNode(parentNode.getName(), parentNode.getParent(), true, session);
                }
            }

            fileNode = parentNode.addNode(name, "olefile");
            fileNode.addMixin("mix:referenceable");
            //            if (isVersioningEnabled()) {
            //                fileNode.addMixin("mix:versionable");
            //            }
            //            if (DocType.LICENSE.isEqualTo(document.getType())) {
            //                fileNode.addMixin("mix:versionable");
            //            }

            AdditionalAttributes additionalAttributes = document.getAdditionalAttributes();
            //            if (DocType.LICENSE.isEqualTo(document.getType()) && !DocFormat.ONIXPL.isEqualTo(document.getFormat())) {
            //                String docName = new File(document.getDocumentName()).getName();
            //                additionalAttributes.setAttribute("dateLoaded", Calendar.getInstance().toString());
            //                additionalAttributes.setAttribute("fileName", docName);
            //                additionalAttributes.setAttribute("owner", document.getUser());
            //            }

            //modifyAdditionalAttributes(additionalAttributes, document);

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

            if (document != null) {
                document.setUuid(fileNode.getIdentifier());
            }
        } catch (Exception e) {
            throw new OleDocStoreException("File node cannot be created.", e);
        }
        return fileNode;
    }

    public synchronized Node createContentNode(Node fileNode, RequestDocument document, Node parentNode,
                                               Session session) throws OleDocStoreException {
        Node resNode = null;
        try {

            resNode = fileNode.addNode("jcr:content", "nt:resource");
            resNode.setProperty("jcr:mimeType", "application/xml");
            resNode.setProperty("jcr:encoding", "");

            String charset = "UTF-8";
            byte[] documentBytes = null;
            try {
                //                String uuid = fileNode.getIdentifier();
                //                document.setUuid(uuid);
                // Content Manipulations
                //                if (DocFormat.MARC.isEqualTo(document.getFormat())) {
                //                    new WorkBibMarcContentHandler().doPreIngestContentManipulations(document, uuid);
                //                }
                //                            else if (DocFormat.OLEML.isEqualTo(document.getFormat())) {
                //                                  WorkInstanceDocumentManager resolver = WorkInstanceDocumentManager.getInstance();
                //                                    resolver.modifyDocumentContent(document, fileNode.getIdentifier(), parentNode.getIdentifier());
                //                                           .doInstanceOleMLContentManipulations(document, fileNode.getIdentifier(), parentNode);
                //                                }

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
        } catch (RepositoryException e) {
            throw new OleDocStoreException(e);
        }
        return resNode;
    }

    public void enableVersioning(Node node) throws OleDocStoreException {
        try {
            node.addMixin("mix:versionable");
        } catch (Exception e) {
            throw new OleDocStoreException(e);
        }
    }

    public Node getNodeByUUID(Session session, String uuid) throws OleDocStoreException {
        logger.debug("Started getting node for UUID:" + uuid);
        try {
            return session.getNodeByIdentifier(uuid);
        } catch (RepositoryException e) {
            throw new OleDocStoreException("getNodeByUUID failed", e);
        }
    }

    public String getData(Node nodeByUUID) throws OleDocStoreException, RepositoryException, FileNotFoundException {
        StringBuffer stringBuffer = new StringBuffer();
        if (null != nodeByUUID) {
            Node jcrContent = nodeByUUID.getNode("jcr:content");
            Binary binary = jcrContent.getProperty("jcr:data").getBinary();
            InputStream content = binary.getStream();

            Writer writer;
            try {
                writer = new StringWriter();
                char[] buffer = new char[1024];
                try {
                    Reader reader = new BufferedReader(new InputStreamReader(content, "UTF-8"));
                    int n;
                    while ((n = reader.read(buffer)) != -1) {
                        writer.write(buffer, 0, n);
                    }
                } finally {
                    stringBuffer.append(writer.toString());
                    content.close();
                }

            } catch (IOException e) {
                logger.info("failure during checkOut of ", e);
            }

        }

        return stringBuffer.toString();
    }

    public byte[] getBinaryData(Node nodeByUUID) throws RepositoryException, IOException {
        byte[] bytes = null;
        if (null != nodeByUUID) {
            Node jcrContent = nodeByUUID.getNode("jcr:content");
            Binary binary = jcrContent.getProperty("jcr:data").getBinary();
            InputStream inputStream = binary.getStream();
            bytes = getBytesFromInputStream(inputStream);
        }
        return bytes;
    }

    public byte[] getBytesFromInputStream(InputStream is) throws IOException {
        long length = is.available();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file ");
        }
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    @Override
    public void ingestItemRecForInstance(RequestDocument reqDoc, String id, Session session) throws OleDocStoreException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getInstanceData(Node instanceNode)
            throws RepositoryException, OleDocStoreException, FileNotFoundException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
