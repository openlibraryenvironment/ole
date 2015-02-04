package org.kuali.ole.docstore.service;

import org.apache.commons.lang.time.StopWatch;
import org.apache.jackrabbit.commons.flat.*;
import org.kuali.ole.docstore.common.document.content.instance.Instance;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.documenthandler.InstanceRequestDocumentResolver;
import org.kuali.ole.repository.NodeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.kuali.ole.docstore.process.ProcessParameters.*;


/**
 * Class to Ingest Documents.
 *
 * @author Rajesh Chowdary K
 * @created Feb 16, 2012
 */
public class DocumentIngester {

    private static Logger logger = LoggerFactory.getLogger(DocumentIngester.class);
    private NodeHandler nodeHandler = new NodeHandler();
    private TreeManager treeManager;
    private NodeSequence nodeSequence;
    private int i = 0;

    public Node getStaticFormatNode(RequestDocument doc, Session session) throws RepositoryException {
        Node formatNode = null;
        Node root = session.getRootNode();
        Node categoryNode = nodeHandler.initStaticNode(doc.getCategory(), root, session);
        Node typeNode = nodeHandler.initStaticNode(doc.getType(), categoryNode, session);
        formatNode = nodeHandler.initStaticNode(doc.getFormat(), typeNode, session);
        return formatNode;
    }

    /**
     * Method to ingest a Bib RequestDocument.
     *
     * @param reqDoc
     * @param session
     * @param formatNode
     * @return
     * @throws Exception
     */
    protected synchronized Node ingestBibDocument(RequestDocument reqDoc, Session session, Node formatNode)
            throws Exception {
        Node bibFileNode = null;
        try {
            String file = "file";
            if (DocFormat.MARC.isEqualTo(reqDoc.getFormat())) {
                file = FILE_MARC;
            } else {
                file = reqDoc.getFormat() + FILE;
            }

            Node bibFormatNode = null;
            if (formatNode == null) {
                bibFormatNode = getStaticFormatNode(reqDoc, session);
            } else {
                bibFormatNode = formatNode;
            }
            Node l1 = null;
            Node l3 = null;
            synchronized (nodeHandler) {
                l1 = nodeHandler.initLevelNode(NODE_LEVEL1, bibFormatNode, false, session);
//                Node l2 = nodeHandler.initLevelNode(NODE_LEVEL2, l1, false, session);
//                l3 = nodeHandler.initLevelNode(NODE_LEVEL3, l2, false, session);
            }
//            bibFileNode = nodeHandler.initFileNode(reqDoc, file, l3, session);
            bibFileNode = nodeHandler.initFileNode(reqDoc, file, l1, session);
        } catch (Exception e) {
            logger.error("Ingest failed for RequestDocument: ", e);
            throw e;
        }
        return bibFileNode;
    }

    /**
     * Method to ingest a Bib RequestDocument using Btree manager.
     *
     * @param reqDocs
     * @param session
     * @param formatNode
     * @return
     * @throws Exception
     */
    protected synchronized List<Node> ingestBibDocumentUsingBTreeMgr(List<RequestDocument> reqDocs, Session session,
                                                                     Node formatNode) throws Exception {
        List<Node> fileNodes = null;
        fileNodes = new ArrayList<Node>();
        try {
            /*String file = "file";
            if (DocFormat.MARC.isEqualTo(reqDoc.getFormat()))
                file = FILE_MARC;
            else
                file = reqDoc.getFormat() + FILE;
            Node bibFormatNode = null;
            if (formatNode == null)
                bibFormatNode = getStaticFormatNode(reqDoc, session);
            else
                bibFormatNode = formatNode;
            Node l3 = null;
            synchronized (nodeHandler) {
                Node l1 = nodeHandler.initLevelNode(NODE_LEVEL1, bibFormatNode, false, session);
                Node l2 = nodeHandler.initLevelNode(NODE_LEVEL2, l1, false, session);
                l3 = nodeHandler.initLevelNode(NODE_LEVEL3, l2, false, session);
            } */
            StopWatch btreeTimer = new StopWatch();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
            Date date = new Date();
            btreeTimer.start();
            treeManager = new BTreeManager(formatNode, 500, 1000, Rank.<String>comparableComparator(), true);
            // Create a new NodeSequence with that tree manager
            nodeSequence = ItemSequence.createNodeSequence(treeManager);
            btreeTimer.stop();
            logger.info("Time taken for initializing btree manager sequence=" + btreeTimer.toString());
            StopWatch btreeAddNodeTimer = new StopWatch();
            Node node = null;
            btreeAddNodeTimer.start();
            Random generator = new Random(19580427);
            Format formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
            Date date1 = null;
            for (RequestDocument reqDoc : reqDocs) {
                node = null;
                date1 = new Date();
                String dateStr = formatter.format(date1);
                node = nodeSequence.addNode(dateStr + "-" + generator.nextInt(), NodeType.NT_UNSTRUCTURED);
                nodeHandler.initFileNode(node, reqDoc, FILE_MARC, null, session);
                fileNodes.add(node);
                //i++;
            }
            btreeAddNodeTimer.stop();
            logger.info("Time taken for adding " + reqDocs.size() + " nodes to btree: " + btreeAddNodeTimer.toString());

        } catch (Exception e) {
            logger.error("Ingest failed for RequestDocument: ", e);
            throw new Exception(e);
        }
        return fileNodes;
    }


    /**
     * Method to ingest a License RequestDocument.
     *
     * @param reqDoc
     * @param session
     * @param formatNode
     * @return
     * @throws Exception
     */
    protected synchronized Node ingestLicenseDocument(RequestDocument reqDoc, Session session, Node formatNode)
            throws Exception {
        Node licenseFileNode = null;
        try {
            String file = "file";
            if (DocFormat.ONIXPL.isEqualTo(reqDoc.getFormat())) {
                file = FILE_ONIXPL;
            } else {
                file = reqDoc.getFormat() + FILE;
            }
            Node licenseFormatNode = null;
            if (formatNode == null) {
                licenseFormatNode = getStaticFormatNode(reqDoc, session);
            } else {
                licenseFormatNode = formatNode;
            }
            synchronized (nodeHandler) {
                Node l1 = nodeHandler.initLevelNode(NODE_LEVEL1, licenseFormatNode, false, session);
                licenseFileNode = nodeHandler.initFileNode(reqDoc, file, l1, session);
            }
        } catch (Exception e) {
            logger.error("Ingest failed for RequestDocument: ", e);
            throw e;
        }
        return licenseFileNode;
    }

    /**
     * Method to ingest an Instance Request Document.
     *
     * @param reqDoc
     * @param session
     * @param ingestedIds  - can even be null if ingested Ids are not required for Outside.
     * @param linkedBibIds - Pass Linked Bib Id's only if linking is necessary & be used in making a resource Linking.
     * @param formatNode
     * @return
     * @throws Exception - Throws Exception if it cannot ingest any of the instance / item / holdings documents.
     */
    protected synchronized Node ingestInstanceDocument(RequestDocument reqDoc, Session session,
                                                       List<String> ingestedIds, List<String> linkedBibIds,
                                                       Node formatNode) throws Exception {
        Node instanceNode = null;
        if (ingestedIds == null) {
            ingestedIds = new ArrayList<String>();
        }
        try {
            InstanceRequestDocumentResolver resolver = new InstanceRequestDocumentResolver();
            List<RequestDocument> resolvedDocs = resolver.getParsedHoldingsNItemDocuments(reqDoc, linkedBibIds);
            Node instFormatNode = null;
            if (formatNode == null) {
                instFormatNode = getStaticFormatNode(reqDoc, session);
            } else {
                instFormatNode = formatNode;
            }
            synchronized (nodeHandler) {
                Node l1 = nodeHandler.initLevelNode(NODE_LEVEL1, instFormatNode, false, session);
                Node l2 = nodeHandler.initLevelNode(NODE_LEVEL2, l1, false, session);
                instanceNode = nodeHandler.initLevelNode(NODE_INSTANCE, l2, false, session);
            }
            Node holdingsNode = nodeHandler.initNonStaticNode(NODE_HOLDINGS, instanceNode);
            reqDoc.setUuid(instanceNode.getIdentifier());
            ingestedIds.add(nodeHandler.initFileNode(resolvedDocs.get(0), FILE_INSTANCE, instanceNode, session)
                    .getIdentifier());
            ingestedIds.add(nodeHandler.initFileNode(resolvedDocs.get(1), FILE_HOLDINGS, holdingsNode, session)
                    .getIdentifier());
            ingestedIds.add(nodeHandler.initFileNode(resolvedDocs.get(2), FILE_SOURCE_HOLDINGS, holdingsNode, session)
                    .getIdentifier());
            for (int i = 3; i < resolvedDocs.size(); i++) {
                ingestedIds.add(nodeHandler.initFileNode(resolvedDocs.get(i), FILE_ITEM, holdingsNode, session)
                        .getIdentifier());
            }
            ((InstanceCollection) reqDoc.getContent().getContentObject()).getInstance().get(0).setInstanceIdentifier(
                    instanceNode.getIdentifier());
        } catch (Exception e) {
            logger.error("Ingest failed for RequestDocument: ", e);
            throw e;
        }
        return instanceNode;
    }

    protected synchronized List<String> ingestPatronRequestDocument(RequestDocument reqDoc, Session session,
                                                                    Node formatNode) throws Exception {
        List<String> ingestedIds = new ArrayList<String>();
        try {
            // Validation at Content Level.
            validateContent(reqDoc.getFormat(), reqDoc.getContent().getContent());
            // Ingest
            Node patronFormatNode = null;
            if (formatNode == null) {
                patronFormatNode = getStaticFormatNode(reqDoc, session);
            } else {
                patronFormatNode = formatNode;
            }
            String uuid = nodeHandler.initFileNode(reqDoc, FILE_PATRON_OLEML, patronFormatNode, session)
                    .getIdentifier();
            reqDoc.setUuid(uuid);
            ingestedIds.add(uuid);
        } catch (Exception e) {
            logger.error("Ingest failed for RequestDocument: ", e);
            throw e;
        }
        return ingestedIds;
    }

    /**
     * Method to ingest RequestDocuments For Bulk Ingest Opertaions.
     *
     * @param reqDocs
     * @param session
     * @return
     * @throws Exception
     */
    public List<String> ingestRequestDocumentsForBulk(List<RequestDocument> reqDocs, Session session) throws Exception {
        List<String> ingestedIds = new ArrayList<String>();
        if (reqDocs != null && reqDocs.size() > 0) {
            Node formatNode = getStaticFormatNode(reqDocs.get(0), session);
            String docType = reqDocs.get(0).getType();
            if (DocType.BIB.isEqualTo(docType)) {
                ingestedIds = ingestBatch(reqDocs, session, formatNode);
            } else {
                for (RequestDocument reqDoc : reqDocs) {
//                                    if (DocType.BIB.isEqualTo(reqDoc.getType())) {
//                                        ingestedIds.add(ingestBibDocument(reqDoc, session, formatNode).getIdentifier());
//                                    }
//                                    else
                    if (DocType.INSTANCE.isEqualTo(reqDoc.getType())) {
                        ingestedIds.addAll(ingestInstaceRequestDocumentForBulk(reqDoc, session, formatNode));
                    } else if (DocType.PATRON.isEqualTo(reqDoc.getType())) {
                        ingestedIds.addAll(ingestPatronRequestDocument(reqDoc, session, formatNode));
                    }
                }
            }
        }
        return ingestedIds;
    }

    /**
     * This method is specifically written only for testing performance for 1 Million bib records.
     *
     * @param reqDocs
     * @param session
     * @param formatNode
     * @return
     */
    public List<String> ingestBatch(List<RequestDocument> reqDocs, Session session, Node formatNode) throws Exception {
        String nodeName = reqDocs.get(0).getFormat() + FILE;
        List<String> idList = new ArrayList<String>();
        // Get a new level1 node and add file nodes to it.
        Node levelNode = null;
        synchronized (nodeHandler) {
            levelNode = nodeHandler.initNonStaticNode(NODE_LEVEL1, formatNode);
        }
        for (RequestDocument requestDocument : reqDocs) {
            Node fileNode = nodeHandler.initFileNode(requestDocument, nodeName, levelNode, session);
            idList.add(fileNode.getIdentifier());
        }
        return idList;
    }


    /**
     * Method to ingest RequestDocuments For Bulk Ingest Opertaions using Btree Manager.
     *
     * @param reqDocs
     * @param session
     * @return
     * @throws Exception
     */
    @Deprecated
    public List<String> ingestRequestDocumentsForBulkUsingBTreeMgr(List<RequestDocument> reqDocs, Session session)
            throws Exception {
        List<String> ingestedIds = new ArrayList<String>();
        if (reqDocs != null && reqDocs.size() > 0) {
            Node formatNode = getStaticFormatNode(reqDocs.get(0), session);
            List<Node> nodes = ingestBibDocumentUsingBTreeMgr(reqDocs, session, formatNode);
            for (Node node : nodes) {
                ingestedIds.add(node.getIdentifier());
            }
        }
        return ingestedIds;
    }

    /**
     * Method to ingest Bib & Linked Instance RequestDocuments for String Ingest.
     *
     * @param reqDoc
     * @param session
     * @return - Ingested List of Documents UUIDs.
     * @throws Exception
     */
    public List<String> ingestBibNLinkedInstanceRequestDocuments(RequestDocument reqDoc, Session session)
            throws Exception {
        List<String> ingestedIds = new ArrayList<String>();
        try {
            Node bibNode = ingestBibDocument(reqDoc, session, null);
            ingestedIds.add(bibNode.getIdentifier());
            List<String> linkedBibIds = new ArrayList<String>();
            linkedBibIds.add(bibNode.getIdentifier());
            for (RequestDocument linkedDoc : reqDoc.getLinkedRequestDocuments()) {
                Node instanceNode = ingestInstanceDocument(linkedDoc, session, ingestedIds, linkedBibIds, null);
                instanceNode.setProperty("bibIdentifier", reqDoc.getUuid());
                bibNode.setProperty("instanceIdentifier", instanceNode.getIdentifier());
            }
        } catch (Exception e) {
            logger.error("Ingest failed for Request Document: ", e);
            throw e;
        }
        return ingestedIds;
    }

    /**
     * Method to ingest Bib & Linked Instance RequestDocuments for String Ingest.
     *
     * @param reqDoc
     * @param session
     * @return - Ingested List of Documents UUIDs.
     * @throws Exception
     */
    public Node ingestWorkLicenseOnixplRequestDocument(RequestDocument reqDoc, Session session,
                                                       List<String> ingestedIds) throws Exception {
        Node licenseNode;
        if (ingestedIds == null) {
            ingestedIds = new ArrayList<String>();
        }
        try {
            licenseNode = ingestLicenseDocument(reqDoc, session, null);
            ingestedIds.add(licenseNode.getIdentifier());
        } catch (Exception e) {
            logger.error("Ingest failed for Request Document: ", e);
            throw e;
        }
        return licenseNode;
    }

    /**
     * Method to ingestInstaceRequestDocument
     *
     * @param reqDoc
     * @param session
     * @param formatNode
     * @throws Exception
     */
    public synchronized List<String> ingestInstaceRequestDocumentForBulk(RequestDocument reqDoc, Session session,
                                                                         Node formatNode) throws Exception {
        List<String> parsedDocs = new ArrayList<String>();
        try {
            // Validation at Content Level.
            validateContent(reqDoc.getFormat(), reqDoc.getContent().getContent());
            reqDoc.getContent().setContentObject(reqDoc.getContent().getContent());
            Node instanceNode = ingestInstanceDocument(reqDoc, session, parsedDocs, null, formatNode);
            Instance instance = ((InstanceCollection) reqDoc.getContent().getContentObject()).getInstance().get(0);
            for (String resourceId : instance.getResourceIdentifier()) {
                try {
                    Node bibNode = nodeHandler.getNodeByUUID(session, resourceId);
                    bibNode.setProperty("instanceIdentifier", instanceNode.getIdentifier());
                    instanceNode.setProperty("bibIdentifier", resourceId);
                } catch (Exception e) {
                    logger.info("Mapping Not Successful: From Bib(" + resourceId + ") --> Instance("
                            + instanceNode.getIdentifier() + ")");
                }
            }
        } catch (Exception e) {
            logger.error("Ingest failed for Request Document: ", e);
            throw new Exception("Ingest failed for Request Document: ", e);
        }
        return parsedDocs;
    }

    /**
     * Method to rollback Doc Store Ingested Data.
     *
     * @param session
     * @param requestDocuments
     */
    public void rollbackDocStoreIngestedData(Session session, List<RequestDocument> requestDocuments) {
        try {
            for (RequestDocument document : requestDocuments) {
                try {
                    session.getNodeByIdentifier(document.getUuid()).remove();
                } catch (Exception e) {
                    logger.error(e.getMessage() , e);
                }
                for (RequestDocument linkedDoc : document.getLinkedRequestDocuments()) {
                    try {
                        session.getNodeByIdentifier(linkedDoc.getUuid()).remove();
                    } catch (Exception e) {
                        logger.error(e.getMessage() , e);
                    }
                }
            }
            session.save();
        } catch (Exception e) {
            logger.info(e.getMessage() , e);
        }
    }

    /**
     * Method to validate Content of a given format Request Document
     *
     * @param format
     * @param content
     */
    private void validateContent(String format, String content) {
    }
}
