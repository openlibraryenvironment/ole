package org.kuali.ole.docstore.document.jcr;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.Items;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.document.AbstractDocumentManager;
import org.kuali.ole.docstore.document.DocumentManager;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.*;
import org.kuali.ole.docstore.common.document.content.instance.Instance;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.kuali.ole.docstore.process.batch.BulkProcessRequest;
import org.kuali.ole.docstore.repository.CustomNodeManager;
import org.kuali.ole.docstore.repository.NodeManager;
import org.kuali.ole.docstore.repository.WorkInstanceNodeManager;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.OleUuidCheckWebService;
import org.kuali.ole.docstore.service.OleWebServiceProvider;
import org.kuali.ole.docstore.service.ServiceLocator;
import org.kuali.ole.docstore.service.impl.OleWebServiceProviderImpl;
import org.kuali.ole.docstore.utility.BatchIngestStatistics;
import org.kuali.ole.docstore.utility.BulkIngestStatistics;
import org.kuali.ole.pojo.OleException;
import org.kuali.ole.repository.NodeHandler;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import static org.kuali.ole.docstore.process.ProcessParameters.FILE;

/**
 * Provides the common and basic implementation for the DocumentManager interface.
 *
 * @author tirumalesh.b
 * @version %I%, %G%
 *          Date: 28/8/12 Time: 12:22 PM
 */
public abstract class JcrAbstractDocumentManager
        extends AbstractDocumentManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static DocCategory docCategory;
    public static DocType docType;
    public static DocFormat docFormat;
    //    private static final String DELETE_WITH_LINKED_DOCS = "deleteWithLinkedDocs";
    private static final String BIBLIOGRAPHIC = "bibliographic";
    private static final String INSTANCE_IDENTIFIER = "instanceIdentifier";
    private static final String SUCCESS = "Success";
    private static final String FAILURE = "Failure";

    protected RepositoryManager repositoryManager;
    protected NodeManager nodeManager;

    public JcrAbstractDocumentManager() {
        try {
            this.repositoryManager = RepositoryManager.getRepositoryManager();
        } catch (OleException oe) {
            //throw new OleDocStoreException(oe);
            // TODO: log the exception
        }
        this.nodeManager = CustomNodeManager.getInstance();
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<ResponseDocument> ingest(List<RequestDocument> requestDocuments, Object object)
            throws OleDocStoreException {
        Session session = (Session) object;
        if (null == session) {
            throw new OleDocStoreException("Invalid session.");
        }
        ResponseDocument respDoc = new ResponseDocument();
        // Store data in docstore, but do not commit.
        for (RequestDocument requestDocument : requestDocuments) {
            ingest(requestDocument, session, respDoc);
        }

        List<ResponseDocument> responseDocuments = buildResponseDocuments(
                requestDocuments); //buildResponseDocuments(requestDocuments);
        return responseDocuments;
    }

    @Override
    public ResponseDocument ingest(RequestDocument requestDocument, Object object, ResponseDocument respDoc) throws OleDocStoreException {
        Session session = (Session) object;
        if (null == session) {
            throw new OleDocStoreException("Invalid session.");
        }
        // Store the document of the requestDocument.
        Node fileNode = storeDocument(requestDocument, session, respDoc);

        // Store the linked documents of the requestDocument.
        storeLinkedDocuments(requestDocument, fileNode, session, respDoc);
        return respDoc;
    }


    @Override
    public List<ResponseDocument> checkout(List<RequestDocument> requestDocuments, Object object) throws OleDocStoreException {
        // TODO: implement this.
        return null;
    }

    @Override
    public ResponseDocument checkout(RequestDocument requestDocument, Object object) throws OleDocStoreException {
        Session session = (Session) object;
        String content = "";
        String uuid = requestDocument.getUuid();
        if (session == null) {
            throw new OleDocStoreException("Invalid session.");
        }
        try {
            Node nodeByUUID = nodeManager.getNodeByUUID(session, uuid);
            if (nodeByUUID != null) {
                content = checkOutContent(nodeByUUID, requestDocument.getFormat(), requestDocument.getUser());
            }
        } catch (Exception e) {
            logger.error("Error in checking out the file. Please refer to the logs for more details!" + e.getMessage(),
                    e);
            throw new OleDocStoreException(e);
        }

        ResponseDocument respDoc = new ResponseDocument();
        Content contentObj = new Content();
        contentObj.setContent(content);
        respDoc.setContent(contentObj);
        String category = requestDocument.getCategory();
        String type = requestDocument.getType();
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        Map<String, String> map = additionalAttributes.getAttributeMap();
        Node node = null;
        try {
            node = session.getNodeByIdentifier(requestDocument.getUuid());
        } catch (RepositoryException e) {
            logger.info("Failed to get Node:" + e.getMessage(), e);
        }

        if (node != null) {
            try {

                Collection<String> attributeKeyCollection = additionalAttributes.getAdditionalAttributeKeyCollection();
                for (String key : attributeKeyCollection) {
                    if (node.hasProperty(key)) {
                        additionalAttributes.setAttribute(key, node.getProperty(key).getString());
                    }
                }

                if (additionalAttributes != null && category.equals(DocCategory.WORK.getDescription()) && type
                        .equals(DocType.BIB.getDescription())) {
                    respDoc.setAdditionalAttributes(additionalAttributes);
                }
            } catch (RepositoryException e) {
                logger.info("Failed to get node property:" + e.getMessage(), e);
            }
        }

        return respDoc;
    }

    protected String checkOutContent(Node nodeByUUID, String format, String user)
            throws RepositoryException, OleDocStoreException, FileNotFoundException {
        String content = nodeManager.getData(nodeByUUID);
        return content;
    }

    @Override
    public List<ResponseDocument> checkin(List<RequestDocument> requestDocuments, Object object) throws OleDocStoreException {
        // TODO: implement this.
        return null;
    }

    @Override
    public ResponseDocument checkin(RequestDocument requestDocument, Object object, ResponseDocument respDoc) throws OleDocStoreException {
        Session session = (Session) object;
        String checkInFail = "Check in failed. ";
        if (requestDocument.getId() != null || requestDocument.getId().trim().length() > 0) {
            requestDocument.setUuid(requestDocument.getId());
        }

        //adding new record to docstore but do not commit
        addNewRecordsToDocStore(requestDocument, session); // item for instance

        try {
            // Store data in docstore and update version if enables .
            String version = updateDocstore(requestDocument, session);
        } catch (Exception e) {
            logger.info(
                    "Document was updated in indexer but not in docStore, trying to rollback the changes from indexer",
                    e);
            /* try {
                session.refresh(false);
                if (session == null) {
                    session = repositoryManager.getSession();
                }
                Node nodeByUUID = nodeManager.getNodeByUUID(session, requestDocument.getId());
                RequestDocument prevRequestDoc = new RequestDocument();
                prevRequestDoc = requestDocument;
                String prevContent = nodeByUUID.getNode("jcr:content").getProperty("jcr:data").getValue().getString();
                prevRequestDoc.getContent().setContent(prevContent);
                ServiceLocator.getIndexerService().indexDocument(prevRequestDoc);
            }
            catch (Exception ex) {
                String failMsg = checkInFail + "Unable to Roll back the changes in indexer.  Record UUID "
                                 + requestDocument.getId() + "got updated in indexer, but not in docstore ";
                logger.info(failMsg, ex);
                throw new OleDocStoreException(failMsg, ex);
            }*/
            logger.info(checkInFail, e);
            throw new OleDocStoreException(checkInFail, e);
        }
        //return buildResponseDocument(requestDocument);
        buildResponseDocument(requestDocument, session, respDoc);
        //return buildResponseDocument(requestDocument, session);
        return respDoc;
    }

    protected void addNewRecordsToDocStore(RequestDocument requestDocument, Session session)
            throws OleDocStoreException {
    }


    public String updateDocstore(RequestDocument reqDoc, Session session)
            throws OleDocStoreException, RepositoryException, FileNotFoundException {

        Node nodeByUUID = session.getNodeByIdentifier(reqDoc.getUuid());
        modifyContent(reqDoc, session, nodeByUUID);
        byte[] documentBytes = convertContentToBytes(reqDoc);
        modifyAdditionalAttributes(reqDoc, nodeByUUID);
        updateContentToNode(reqDoc, session, documentBytes, nodeByUUID);
        //Get the updated version from docstore.
        String currentVersion = updateVersion(session, nodeByUUID);
        return currentVersion;
    }

    protected void modifyContent(RequestDocument reqDoc, Session session, Node node) throws RepositoryException, FileNotFoundException, OleDocStoreException {
    }

    protected String updateVersion(Session session, Node nodeByUUID) throws RepositoryException, OleDocStoreException {
        session.save();
        String currentVersion = null;
        if (isVersioningEnabled()) {
            VersionManager versionManager = getVersionManager(session);
            versionManager.checkpoint(nodeByUUID.getPath());
            VersionHistory versionHistory = versionManager.getVersionHistory(nodeByUUID.getPath());
            VersionIterator allVersions = versionHistory.getAllVersions();
            while (allVersions.hasNext()) {
                currentVersion = allVersions.nextVersion().getName();
            }
            logger.info("Version updated for UUID:" + nodeByUUID.getIdentifier() + "  ====  version:" + currentVersion);
        }
        return currentVersion;
    }

    protected void updateContentToNode(RequestDocument reqDoc, Session session, byte[] documentBytes, Node nodeByUUID)
            throws RepositoryException, OleDocStoreException {
        try {
            Binary binary = null;
            if (documentBytes != null) {
                binary = session.getValueFactory().createBinary(new ByteArrayInputStream(documentBytes));
                nodeByUUID.getNode("jcr:content").setProperty("jcr:data", binary);
            }

            AdditionalAttributes additionalAttributes = reqDoc.getAdditionalAttributes();
            if (additionalAttributes != null) {
                Collection<String> attributeNames = additionalAttributes.getAttributeNames();
                if (attributeNames != null && attributeNames.size() > 0) {
                    for (Iterator<String> iterator = attributeNames.iterator(); iterator.hasNext(); ) {
                        String attributeName = iterator.next();
                        String attributeValue = additionalAttributes.getAttribute(attributeName);
                        nodeByUUID.setProperty(attributeName, attributeValue);
                    }
                }

            }
            Calendar lastModified = Calendar.getInstance();
            lastModified.setTimeInMillis(lastModified.getTimeInMillis());
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new OleDocStoreException(e.getMessage(), e);
        }
    }

    protected byte[] convertContentToBytes(RequestDocument reqDoc) throws OleDocStoreException {
        String charset = "UTF-8";
        byte[] documentBytes = null;
        try {
            if (reqDoc.getContent().getContent() != null) {
                documentBytes = reqDoc.getContent().getContent().getBytes(charset);
            }
        } catch (Exception e) {
            logger.info("Failed to convert input string to byte[] with charset " + charset, e);
            throw new OleDocStoreException(e.getMessage());
        }
        return documentBytes;
    }

    protected String updateIndex(RequestDocument requestDocument) throws OleDocStoreException {
        String result = "failure";
        result = updateRecord(requestDocument);
        if (!result.startsWith("success")) {
            throw new OleDocStoreException("Check in failed. " + result);
        }
        return result;
    }

    protected String updateRecord(RequestDocument requestDocument) {
        String result = ServiceLocator.getIndexerService().indexDocument(requestDocument);
        return result;
    }


    @Override
    public List<ResponseDocument> delete(List<RequestDocument> requestDocuments, Object object) throws OleDocStoreException {
        // TODO: implement this.
        return null;
    }

    @Override
    public ResponseDocument delete(RequestDocument requestDocument, Object object) throws Exception {
        Session session = (Session) object;
        ResponseDocument rs = new ResponseDocument();
        try {
            rs = deleteDoc(requestDocument, session);
        } catch (Exception e) {
            throw new OleDocStoreException(e.getMessage(), e);
        }
        return rs;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void bulkIngest(BulkProcessRequest bulkProcessRequest, List<RequestDocument> requestDocuments)
            throws OleDocStoreException {
        Session session = bulkProcessRequest.getSession();
        if (session == null) {
            try {
                session = RepositoryManager.getRepositoryManager().getSession(bulkProcessRequest.getUser(),
                        bulkProcessRequest.getOperation()
                                .toString());
            } catch (Exception e) {
                throw new OleDocStoreException(e);
            }
            bulkProcessRequest.setSession(session);
        }
        if (bulkProcessRequest.getPreviousBatchDocuments() == null) {
            List<RequestDocument> newList = new ArrayList<RequestDocument>();
            bulkProcessRequest.setPreviousBatchDocuments(newList);
        }
        bulkProcessRequest.getPreviousBatchDocuments().addAll(requestDocuments);

        batchIngest(bulkProcessRequest, requestDocuments, session);

        if (session != null) {
            try {
                if (bulkProcessRequest.getBulkIngestStatistics().isLastBatch()) {
                    RepositoryManager.getRepositoryManager().logout(session);
                    session = null;
                }
            } catch (OleException e) {
            }
        }
    }

    private ResponseDocument deleteDoc(RequestDocument document, Session session1) throws Exception {
        //List<String> uuidsList = new ArrayList<String>();
        String status = null;
        String category = null;
        Response response = new Response();
        List<String> respositoryUuidList = new ArrayList<String>();
        category = document.getCategory();
        String uuid = document.getUuid();
        String operation = document.getOperation();
        respositoryUuidList = getLinkedDocsFromRepository(uuid, session1, respositoryUuidList, operation);
        logger.info("respository UuidList size-->" + respositoryUuidList);
        deleteFromRepository(respositoryUuidList, session1);
        String statusValue = ServiceLocator.getIndexerService().deleteDocuments(category, respositoryUuidList);
        return prepareResponseDocument(document);

    }

    private ResponseDocument prepareResponseDocument(RequestDocument requestDocument) {
        ResponseDocument responseDocument = new ResponseDocument();
        responseDocument.setStatus("success");
        responseDocument.setStatusMessage("Success");
        responseDocument.setUuid(requestDocument.getUuid());
        return responseDocument;
    }

    private List<String> getLinkedDocsFromRepository(String uuid, Session session, List<String> respositoryUuidList,
                                                     String operation) throws Exception {
        if (operation.equalsIgnoreCase(Request.Operation.deleteWithLinkedDocs.toString())) {
            Node node = session.getNodeByIdentifier(uuid);
            if (node.getPath().contains(BIBLIOGRAPHIC)) {
                String instanceId = node.getProperty(INSTANCE_IDENTIFIER).getString();
                respositoryUuidList.add(instanceId);
            }
        }
        respositoryUuidList.add(uuid);
        return respositoryUuidList;
    }

    protected void deleteFromRepository(List<String> uuidsList, Session session) throws Exception {
        if (uuidsList != null && uuidsList.size() > 0) {
            for (int i = 0; i < uuidsList.size(); i++) {
                Node deleteNode = new NodeHandler().getNodeByUUID(session, uuidsList.get(i));
                if (deleteNode != null) {
                    deleteNode.remove();
                }
            }
        }
    }

    public List<String> batchIngest(BulkProcessRequest bulkProcessRequest, List<RequestDocument> requestDocuments,
                                    Session session) {
        //RequestDocument requestDocument = request.getRequestDocuments().get(0);
        //DocumentManager documentManager = BeanLocator.getDocumentManagerFactory().getDocumentManager(requestDocument);
        BulkIngestStatistics bulkIngestStatistics = bulkProcessRequest.getBulkIngestStatistics();
        BatchIngestStatistics batchStatistics = bulkIngestStatistics.getCurrentBatch();
        long commitSize = ProcessParameters.BULK_INGEST_COMMIT_SIZE;
        logger.debug("commitSize = " + commitSize);
        logger.debug("bulkIngestNIndex(" + requestDocuments.size() + ") START");
        logger.debug("BULK_INGEST_IS_LINKING_ENABLED=" + ProcessParameters.BULK_INGEST_IS_LINKING_ENABLED);
        //Session session = null;
        List<String> docUUIDs = new ArrayList<String>();
        StopWatch ingestTimer = new StopWatch();
        StopWatch indexTimer = new StopWatch();
        StopWatch totalTimer = new StopWatch();
        StopWatch createNodesTimer = new StopWatch();
        StopWatch sessionSaveTimer = new StopWatch();
        StopWatch solrOptimizeTimer = new StopWatch();
        long recCount = requestDocuments.size();
        boolean isCommit = false;
        totalTimer.start();
        try {
            ingestTimer.start();
            createNodesTimer.start();
            //session = RepositoryManager.getRepositoryManager().getSession(request.getUser(), request.getOperation());
            //List<RequestDocument> reqDocs = requestDocuments;
            //docUUIDs.addAll(documentIngester.ingestRequestDocumentsForBulk(requestDocuments, session));
            //docUUIDs.addAll(documentIngester.ingestRequestDocumentsForBulkUsingBTreeMgr(reqDocs, session));
            store(requestDocuments, session);
            createNodesTimer.stop();
            try {
                ingestTimer.suspend();
                indexTimer.start();
            } catch (Exception e2) {
                logger.info("Exception :" + e2);
            }
            bulkIngestStatistics.setCommitRecCount(bulkIngestStatistics.getCommitRecCount() + recCount);
            if (bulkIngestStatistics.getCommitRecCount() == commitSize || bulkIngestStatistics.isLastBatch()) {
                isCommit = true;
            }
            //documentIndexer.indexDocumentsForBulk(requestDocuments, isCommit);
            index(requestDocuments, isCommit);
            try {
                indexTimer.suspend();
                ingestTimer.resume();
            } catch (Exception e2) {
                logger.info("Exception :" + e2);
            }
            if (isCommit) {
                sessionSaveTimer.start();
                logger.info("Bulk ingest: Repository commit started. Number of records being committed : "
                        + bulkIngestStatistics.getCommitRecCount());
                session.save();
                bulkIngestStatistics.setCommitRecCount(0);
                bulkProcessRequest.setPreviousBatchDocuments(null);
                sessionSaveTimer.stop();
            }

            try {
                ingestTimer.stop();
            } catch (Exception e2) {
                logger.info("Exception :" + e2);
            }
            // Documents processed can be different from records processed as in the case of Instance data.
            logger.debug("Documents processed:" + recCount);
            bulkIngestStatistics.setFileRecCount(bulkIngestStatistics.getFileRecCount() + recCount);
            logger.info(
                    "Bulk ingest: Records processed in the current file :" + bulkIngestStatistics.getFileRecCount());
        } catch (Exception e) {
            logger.info("Exception :" + e);
            bulkIngestStatistics.setCommitRecCount(0);
            try {
                ingestTimer.resume();
            } catch (Exception e2) {
                logger.info("Exception :" + e2);
            }
            //documentIngester.rollbackDocStoreIngestedData(session, request.getRequestDocuments());
            //documentIngester.rollbackDocStoreIngestedData(session, prevRequestDocs);
            delete(bulkProcessRequest.getPreviousBatchDocuments(), session);
            ingestTimer.stop();
            try {
                indexTimer.resume();
            } catch (Exception e2) {
                logger.info("Exception :" + e2);
            }
            //documentIndexer.rollbackIndexedData(request.getRequestDocuments());
            //prevRequestDocs = prevRequestDocs.subList(0, prevRequestDocs.size() - request.getRequestDocuments().size());
            //logger.info("prevRequestDocs before remove INDEXES = " + prevRequestDocs.size());
            //documentIndexer.rollbackIndexedData(prevRequestDocs);
            try {
                deleteIndex(bulkProcessRequest.getPreviousBatchDocuments());
                indexTimer.stop();
            } catch (Exception e2) {
                logger.info("Exception :" + e2);
            }
            bulkProcessRequest.setPreviousBatchDocuments(null);
            logger.error("Document Ingest & Index Failed, Cause: " + e.getMessage(), e);
            try {
                totalTimer.stop();
            } catch (Exception e2) {
                logger.info("Exception :" + e2);
            }
            logger.debug("Time Consumptions...:\tcreatingNodes(" + docUUIDs.size() + "):" + createNodesTimer
                    + "\tSessionSave(" + docUUIDs.size() + "):" + sessionSaveTimer + "\tIngest(" + docUUIDs.size()
                    + "):" + ingestTimer + "\tIndexing(" + docUUIDs.size() + "):" + indexTimer + "\tTotal Time: "
                    + totalTimer);
            docUUIDs.clear();
        } finally {
            /*if (session != null) {
                try {
                    RepositoryManager.getRepositoryManager().logout(session);
                } catch (OleException e) {
                }
            } */
        }
        try {
            totalTimer.stop();
        } catch (Exception exe) {
            logger.info("Exception :" + exe);
        }
        logger.debug(
                "Time Consumptions...:\tcreatingNodes(" + docUUIDs.size() + "):" + createNodesTimer + "\tSessionSave("
                        + docUUIDs.size() + "):" + sessionSaveTimer + "\tIngest(" + docUUIDs.size() + "):" + ingestTimer
                        + "\tIndexing(" + docUUIDs.size() + "):" + indexTimer + "\tTotal Time: " + totalTimer);
        logger.debug("bulkIngestNIndex(" + requestDocuments.size() + ") END");
        batchStatistics.setTimeToCreateNodesInJcr(createNodesTimer.getTime());
        batchStatistics.setTimeToSaveJcrSession(sessionSaveTimer.getTime());
        batchStatistics.setIngestingTime(ingestTimer.getTime());
        batchStatistics.setIndexingTime(indexTimer.getTime());
        batchStatistics.setIngestNIndexTotalTime(totalTimer.getTime());
        //updateProcessTimer(docUUIDs.size(), ingestTimer, indexTimer, totalTimer);
        solrOptimizeTimer.start();
        //optimizeSolr(docUUIDs.size());
        solrOptimizeTimer.stop();
        batchStatistics.setTimeToSolrOptimize(solrOptimizeTimer.getTime());
        return docUUIDs;
    }


    /**
     * @inheritDoc
     */
    //@Override
    public void store(List<RequestDocument> requestDocuments, Session session) throws OleDocStoreException {
        for (RequestDocument requestDocument : requestDocuments) {
            store(requestDocument, session);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void index(List<RequestDocument> requestDocuments, boolean commit) throws OleDocStoreException {
        if (commit == true) {
            String result = ServiceLocator.getIndexerService().indexDocuments(requestDocuments);
            if (!result.startsWith("success")) {
                throw new OleDocStoreException(result);
            }
        } else {
            String result = ServiceLocator.getIndexerService().bulkIndexDocuments(requestDocuments, commit);
            if (!result.startsWith("success")) {
                throw new OleDocStoreException(result);
            }
        }
    }

    protected void delete(List<RequestDocument> requestDocuments, Session session) {
        // TODO: implement this.
    }

    /**
     * @inheritDoc
     */
    //@Override
    public void deleteIndex(List<RequestDocument> requestDocuments) throws OleDocStoreException {
        try {
            Map<String, List<String>> uuids = new HashMap<String, List<String>>();
            for (RequestDocument document : requestDocuments) {
                for (RequestDocument linkedDoc : document.getLinkedRequestDocuments()) {
                    if (uuids.get(linkedDoc.getCategory()) == null) {
                        uuids.put(linkedDoc.getCategory(), new ArrayList<String>());
                    }
                    uuids.get(linkedDoc.getCategory()).add(linkedDoc.getUuid());
                }
                if (uuids.get(document.getCategory()) == null) {
                    uuids.put(document.getCategory(), new ArrayList<String>());
                }
                uuids.get(document.getCategory()).add(document.getUuid());
            }
            for (String category : uuids.keySet()) {
                ServiceLocator.getIndexerService().deleteDocuments(category, uuids.get(category));
            }
        } catch (Exception e) {
            throw new OleDocStoreException(e);
        }

    }

    public void store(RequestDocument requestDocument, Session session) throws OleDocStoreException {
        // Store the document of the requestDocument.
        ResponseDocument responseDocument = new ResponseDocument();
        Node fileNode = storeDocument(requestDocument, session, responseDocument);

        // Store the linked documents of the requestDocument.
        storeLinkedDocuments(requestDocument, fileNode, session, responseDocument);
    }

    public Node storeDocument(RequestDocument requestDocument, Object object, ResponseDocument responseDocument) throws OleDocStoreException {
        Session session = (Session) object;
        Node fileNode = null;
        String validationMsg = null;
        try {
            AdditionalAttributes additionalAttributes = requestDocument.getAdditionalAttributes();
            modifyAdditionalAttributes(requestDocument, null);

            // get the parent node
            Node parentNode = nodeManager.getParentNode(requestDocument, session);
            String fileName = requestDocument.getFormat() + FILE;

            // create the file node
            fileNode = nodeManager.createFileNode(requestDocument, fileName, parentNode, session);

            if (isVersioningEnabled()) {
                nodeManager.enableVersioning(fileNode);
            }

            // modify document content with node identifier information
            String parentNodeIdentifier = parentNode.getIdentifier();
            String nodeIdentifier = fileNode.getIdentifier();
            modifyDocumentContent(requestDocument, nodeIdentifier, parentNodeIdentifier);

            // create content node for the document
            Node contentNode = nodeManager.createContentNode(fileNode, requestDocument, parentNode, session);
            buildResponseDocument(requestDocument, session, responseDocument);
            if (validationMsg != null) {
                responseDocument.setStatusMessage(validationMsg);
                responseDocument.setStatus(ResponseStatus.INVALID_DATA.toString());
            }
        } catch (Exception e) {
            throw new OleDocStoreException(e);
        }
        return fileNode;
    }

    protected void storeLinkedDocuments(RequestDocument requestDocument, Node node, Session session, ResponseDocument responseDocument)
            throws OleDocStoreException {
        // store the linked documents if any.
        List<ResponseDocument> linkedResponseDocumentList = new ArrayList<ResponseDocument>();
        if (responseDocument.getLinkedDocuments() != null && responseDocument.getLinkedDocuments().size() > 0) {
            linkedResponseDocumentList.addAll(responseDocument.getLinkedDocuments());
        }
        responseDocument.setLinkedDocuments(linkedResponseDocumentList);
        for (RequestDocument linkedRequestDocument : requestDocument.getLinkedRequestDocuments()) {
            ResponseDocument linkedResponseDocument = new ResponseDocument();
            linkedResponseDocumentList.add(linkedResponseDocument);
            DocumentManager documentManager = BeanLocator.getDocstoreFactory()
                    .getDocumentManager(linkedRequestDocument.getCategory(), linkedRequestDocument.getType(), linkedRequestDocument.getFormat());
            if (linkedRequestDocument.getContent().getContentObject() instanceof InstanceCollection) {
                InstanceCollection instCol = (InstanceCollection) linkedRequestDocument.getContent().getContentObject();
                if (instCol != null) {
                    for (Instance inst : instCol.getInstance()) {
                        List<String> resIdList = new ArrayList<String>();
                        resIdList.addAll(inst.getResourceIdentifier());
                        for (String resId : inst.getResourceIdentifier()) {
                            try {
                                nodeManager.getNodeByUUID(session, resId);
                            } catch (Exception e) {
                                resIdList.remove(resId);
                            }
                        }
                        inst.setResourceIdentifier(resIdList);
                    }
                }
            }
            Node linkedDocumentNode = documentManager.storeDocument(linkedRequestDocument, session, linkedResponseDocument);
            nodeManager.linkNodes(node, linkedDocumentNode, session);
            //buildResponseDocument(linkedRequestDocument,session,linkedResponseDocument);
        }

    }

/*    protected void validateInput(List<RequestDocument> requestDocuments) throws OleDocStoreException, RepositoryException, FileNotFoundException {
        if ((null == requestDocuments) || (requestDocuments.size() == 0)) {
            throw new OleDocStoreException("RequestDocuments are not specified.");
        }
        for (RequestDocument requestDocument : requestDocuments) {
            validateInput(requestDocument );
        }
    }*/


    public List<ResponseDocument> buildResponseDocuments(List<RequestDocument> requestDocuments) {
        List<ResponseDocument> responseDocumentList = new ArrayList<ResponseDocument>();
        for (int i = 0; i < requestDocuments.size(); i++) {
            responseDocumentList.add(buildResponseDocument(requestDocuments.get(i)));
        }
        return responseDocumentList;
    }

    public ResponseDocument buildResponseDocument(RequestDocument requestDocument) {
        ResponseDocument responseDocument = new ResponseDocument();
        responseDocument.setId(requestDocument.getId());
        responseDocument.setCategory(requestDocument.getCategory());
        responseDocument.setType(requestDocument.getType());
        responseDocument.setFormat(requestDocument.getFormat());
        responseDocument.setUuid(requestDocument.getUuid());
        buildLinkedResponseDocuments(requestDocument, responseDocument);
        return responseDocument;
    }

    public void buildResponseDocument(RequestDocument requestDocument, Session session, ResponseDocument responseDocument) {
        responseDocument.setId(requestDocument.getId());
        responseDocument.setCategory(requestDocument.getCategory());
        responseDocument.setType(requestDocument.getType());
        responseDocument.setFormat(requestDocument.getFormat());
        responseDocument.setUuid(requestDocument.getUuid());
        String category = requestDocument.getCategory();
        String type = requestDocument.getType();
        Node node = null;
        try {
            node = session.getNodeByIdentifier(requestDocument.getUuid());
        } catch (RepositoryException e) {
            logger.info("Failed to get node:" + e.getMessage(), e);
        }

        if (node != null) {
            try {
                AdditionalAttributes additionalAttributes = requestDocument.getAdditionalAttributes();
                if (additionalAttributes != null && category.equals(DocCategory.WORK.getDescription()) && type
                        .equals(DocType.BIB.getDescription())) {
                    Collection<String> attributeNames = additionalAttributes.getAttributeNames();
                    if (attributeNames != null && attributeNames.size() > 0) {
                        for (Iterator<String> iterator = attributeNames.iterator(); iterator.hasNext(); ) {
                            String attributeName = iterator.next();
                            if (node.hasProperty(attributeName)) {
                                additionalAttributes
                                        .setAttribute(attributeName, node.getProperty(attributeName).getString());
                            }
                        }
                    }
                    responseDocument.setAdditionalAttributes(additionalAttributes);
                }
            } catch (RepositoryException e) {
                logger.info("Failed to get node property:" + e.getMessage(), e);
            }
        }

        if (requestDocument.getType().equalsIgnoreCase(DocType.INSTANCE.getCode())) {
            buildLinkedResponseDocuments(requestDocument, responseDocument);
        }
        // buildLinkedResponseDocuments(requestDocument, responseDocument);
        //  return responseDocument;
    }

    protected void buildLinkedResponseDocuments(RequestDocument requestDocument, ResponseDocument responseDocument) {

    }

    protected void setResponseParameters(ResponseDocument responseDocument, RequestDocument docStoreDocument) {
        responseDocument.setId(docStoreDocument.getId());
        responseDocument.setCategory(docStoreDocument.getCategory());
        responseDocument.setType(docStoreDocument.getType());
        responseDocument.setFormat(docStoreDocument.getFormat());
        responseDocument.setContent(docStoreDocument.getContent());
        responseDocument.setUuid(docStoreDocument.getUuid());
    }


    protected void modifyAdditionalAttributes(RequestDocument requestDocument, Node node) {
    }

    @Override
    public ResponseDocument bind(RequestDocument requestDocument, Object object, String operation) throws OleDocStoreException, RepositoryException, OleException, FileNotFoundException {
        Session session = (Session) object;
        return new JcrWorkInstanceDocumentManager().bind(requestDocument, session, operation);
    }

    @Override
    public ResponseDocument unbind(RequestDocument requestDocument, Object object, String operation) throws OleDocStoreException, RepositoryException, OleException, FileNotFoundException {
        Session session = (Session) object;
        return new JcrWorkInstanceDocumentManager().unbind(requestDocument, session, operation);
    }

    protected void modifyDocumentContent(RequestDocument requestDocument, String nodeIdentifier,
                                         String parentNodeIdentifier) {
    }

    public boolean isVersioningEnabled() {
        return false;
    }

    public VersionManager getVersionManager(Session session) throws OleDocStoreException, RepositoryException {
        return session.getWorkspace().getVersionManager();
    }

//    public boolean checkItemsExists( List<Item> itemIdentifierList,Session session) throws Exception{
//        for (Item item : itemIdentifierList) {
//           //TODO
//       }
//        return false;
//    }

    public boolean checkItemsExists() throws Exception {

        //TODO

        return false;
    }


    public boolean checkInstancesOrItemsExistsInOLE(List<String> uuidsList) {
        String uuidsNotInOle = null;
        String serviceURL = ConfigContext.getCurrentContextConfig().getProperty("uuidCheckServiceURL");
        OleWebServiceProvider oleWebServiceProvider = new OleWebServiceProviderImpl();
        OleUuidCheckWebService oleUuidCheckWebService = (OleUuidCheckWebService) oleWebServiceProvider
                .getService("org.kuali.ole.docstore.service.OleUuidCheckWebService", "oleUuidCheckWebService", serviceURL);
        StringBuilder uuidsSB = new StringBuilder();
        for (String uuid : uuidsList) {
            //uuidsSB.append(requestDocument.getId()).append(",");
            uuidsSB.append(uuid).append(",");
        }
        logger.debug("JcrAbstractDocumentManager checkInstancesOrItemsExistsInOLE :uuidsSB " + uuidsSB.toString());
        uuidsNotInOle = oleUuidCheckWebService.checkUuidExsistence(uuidsSB.substring(0, uuidsSB.length() - 1));
        logger.debug("JcrAbstractDocumentManager checkInstancesOrItemsExistsInOLE :uuidsNotInOle " + uuidsNotInOle);
        String[] uuids = StringUtils.split(uuidsNotInOle, ",");
        if (uuids.length == uuidsList.size()) {
            return false;
        } else {
            return true;
        }
        //false means not exists
    }

    //checks whether instance and its corresponding item identifiers are exists in ole
    public boolean checkInstancesOrItemsExistsInOLE(String instanceIdentifier, Session session) throws Exception {
        String uuidsNotInOle = null;
        List<String> instanceOrItemIdentifiersList = new ArrayList<String>();
        Node instanceNode = session.getNodeByIdentifier(instanceIdentifier);
        String instanceXML = WorkInstanceNodeManager.getInstance().getInstanceData(instanceNode);
        InstanceCollection instanceCollection = new InstanceOlemlRecordProcessor().fromXML(instanceXML);
        Items items = instanceCollection.getInstance().get(0).getItems();
        List<Item> itemIdentifierList = items.getItem();
        for (Item item : itemIdentifierList) {
            instanceOrItemIdentifiersList.add(item.getItemIdentifier());
        }
        instanceOrItemIdentifiersList.add(instanceIdentifier);
        return checkInstancesOrItemsExistsInOLE(instanceOrItemIdentifiersList);
    }

    public boolean checkInstanceForBoundsWith(String instanceIdentifier, RequestDocument requestDocument,
                                              Session session, ResponseDocument responseDocument) throws Exception {

        Node instanceNode = session.getNodeByIdentifier(instanceIdentifier);
        String bibIdentifier = instanceNode.getProperty("bibIdentifier").getString();
        String[] bibIds = bibIdentifier.split(",");
        logger.debug("JcrAbstractDocumentManager : checkInstanceForBoundsWith bibIds length " + bibIds.length);
        if (bibIds.length > 1) {
            responseDocument.setCategory(requestDocument.getCategory());
            responseDocument.setType(requestDocument.getType());
            responseDocument.setFormat(requestDocument.getFormat());
            responseDocument.setUuid(requestDocument.getUuid());
            responseDocument.setStatus("failure'");
            responseDocument.setStatusMessage("Instance is bound with more than one bid. So deletion cannot be done");
            return true;
        } else {
            return false;
        }
    }

    public RequestDocument prepareRequestDocument(ResponseDocument responseDocument) {
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setCategory(responseDocument.getCategory());
        requestDocument.setFormat(responseDocument.getFormat());
        requestDocument.setType(responseDocument.getType());
        requestDocument.setUuid(responseDocument.getUuid());
        return requestDocument;

    }
}
