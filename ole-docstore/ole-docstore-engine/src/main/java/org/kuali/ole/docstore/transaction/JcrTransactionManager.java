package org.kuali.ole.docstore.transaction;

import org.apache.commons.lang.time.StopWatch;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.document.DocumentManager;
import org.kuali.ole.docstore.document.jcr.JcrWorkInstanceDocumentManager;
import org.kuali.ole.docstore.factory.DocstoreFactory;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.indexer.solr.WorkBibDocumentIndexer;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseStatus;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.kuali.ole.docstore.process.batch.BulkProcessRequest;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.utility.BatchIngestStatistics;
import org.kuali.ole.docstore.utility.BulkIngestStatistics;
import org.kuali.ole.pojo.OleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 6/20/13
 * Time: 12:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class JcrTransactionManager extends AbstractTransactionManager {

    private static final Logger logger = LoggerFactory.getLogger(JcrTransactionManager.class);
    private Session session;
    private TransactionState transactionState = TransactionState.IDLE;
    private List<RequestDocument> newStateRequestDocuments = null;
    private List<RequestDocument> oldStateRequestDocuments = new ArrayList<RequestDocument>();

    private IndexerService indexerService;

    @Override
    public void startSession(String user, String operation) throws OleDocStoreException {
        try {
            if (null == session) {
                session = RepositoryManager.getRepositoryManager().getSession(user, operation);
            }
        } catch (Exception e) {
            throw new OleDocStoreException(e);
        }
    }

    @Override
    public void closeSession() {
        if (null != session) {
            try {
                RepositoryManager.getRepositoryManager().logout(session);
            } catch (OleException e) {
                logger.error(" Error while closing session :" + e);
            }
        }
    }

    @Override
    public void startTransaction(String user, String operation) throws Exception {
        if (transactionState == TransactionState.STARTED) {
            throw new OleDocStoreException("Transaction already started.");
        }
        try {
            if (null == session) {
                session = RepositoryManager.getRepositoryManager().getSession(user, operation);
            }
            /*if (null == indexerService) {
                indexerService = BeanLocator.getDocstoreFactory().getIndexerService();
            }*/
        } catch (Exception e) {
            throw new OleDocStoreException(e);
        }
        transactionState = TransactionManager.TransactionState.STARTED;
    }

    @Override
    public void commit() throws Exception {
        commit(null);
    }

    @Override
    public void abort() {
        try {
            session.refresh(false);
        } catch (Exception re) {
            // Ignore this, as we want to rollback anyway.
            logger.error("Exception during abort. Unable to rollback changes to docstore. :", re);

        }
        try {
//            indexerService.rollback();
        } catch (Exception e) {
            // Ignore this, as we want to rollback anyway.
            logger.error("Exception during abort. Unable to rollback changes to index. :", e);
        }
        transactionState = TransactionState.ABORTED;
    }


    @Override
    public List<ResponseDocument> ingest(List<RequestDocument> requestDocuments) throws Exception {

        DocstoreFactory docstoreFactory = BeanLocator.getDocstoreFactory();

        // Save the request documents so that they can be rolled back if necessary.
        newStateRequestDocuments = requestDocuments;
        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        List<String> valuesList = new ArrayList<String>();
        for (RequestDocument requestDocument : requestDocuments) {
            // Each document could be of different cat-type-format.
            DocumentManager documentManager = docstoreFactory.getDocumentManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
            ResponseDocument respDoc = new ResponseDocument();
            // validate given input

            try {
                documentManager.validateInput(requestDocument, session, valuesList);
            } catch (OleDocStoreException e) {
                respDoc.setStatus(ResponseStatus.INVALID_DATA.toString());
                logger.debug("validationMessage-->" + e.getMessage(), e);
                throw new OleDocStoreException(e.getMessage(), e);
            }

            // Store
            documentManager.ingest(requestDocument, session, respDoc);
            responseDocuments.add(respDoc);
        }
        // Index
        for (RequestDocument requestDocument : requestDocuments) {
            String result = getIndexerService(requestDocument).indexDocument(requestDocument, false);
            if (!result.startsWith("success")) {
                throw new OleDocStoreException(result);
            }
        }

        return responseDocuments;

    }

    @Override
    public List<ResponseDocument> checkIn(List<RequestDocument> requestDocuments) throws Exception {
        DocumentManager documentManager = null;
        // get the previous content from docStore to rollback the records if saving the session fails.

        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        List<String> fieldValueList = new ArrayList<String>();
        for (RequestDocument requestDocument : requestDocuments) {
            // Each document could be of different cat-type-format.
            documentManager = BeanLocator.getDocstoreFactory().getDocumentManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
            if (requestDocument.getId() != null || requestDocument.getId().trim().length() > 0) {
                requestDocument.setUuid(requestDocument.getId());
            }
            //requestDocument.setOperation(operation);
            //requestDocument.setUser(user);
            // validate given input
            ResponseDocument respDoc = new ResponseDocument();

            try {
                documentManager.validateInput(requestDocument, session, fieldValueList);
            } catch (OleDocStoreException e) {
                respDoc.setStatus(ResponseStatus.INVALID_DATA.toString());
                logger.debug("validationMessage-->" + e.getMessage(), e);
                throw new OleDocStoreException(e.getMessage());
            }
            // Each document could be of different cat-type-format.
            //documentManager = BeanLocator.getDocumentManagerFactory().getDocumentManager(requestDocument);

            // update Docstore
            documentManager.checkin(requestDocument, session, respDoc);
            responseDocuments.add(respDoc);
        }
        // Index to solr
        for (RequestDocument requestDocument : requestDocuments) {
            String result = getIndexerService(requestDocument).indexDocument(requestDocument, false);
            if (!result.startsWith("success")) {
                throw new OleDocStoreException(result);
            }
        }

        return responseDocuments;
    }

    @Override
    public List<ResponseDocument> checkOut(List<RequestDocument> requestDocuments, Object object) throws Exception {
        DocumentManager documentManager = null;
        // Save the request documents so that they can be rolled back if necessary.
        newStateRequestDocuments = requestDocuments;
        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        for (RequestDocument requestDocument : requestDocuments) {
            // Each document could be of different cat-type-format.
            documentManager = BeanLocator.getDocstoreFactory().getDocumentManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
            // To differentiate between restfull API and Normal CheckOut.
//            requestDocument.setUser(user);
            // checkOut
            responseDocuments.add(documentManager.checkout(requestDocument, session));
        }
        return responseDocuments;
    }

    @Override
    public List<ResponseDocument> bind(List<RequestDocument> requestDocuments, String operation) throws Exception {
        DocumentManager documentManager = null;
        // get the previous content from docStore to rollback the records if saving the session fails.

        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        for (RequestDocument requestDocument : requestDocuments) {
            documentManager = BeanLocator.getDocstoreFactory().getDocumentManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
            // update Docstore
            responseDocuments.add(documentManager.bind(requestDocument, session, operation));
        }

        // Index to solr
        String result = getIndexerService(requestDocuments.get(0)).bind(requestDocuments);
        if (!result.startsWith("success")) {
            throw new OleDocStoreException(result);
        }

        return responseDocuments;
    }

    @Override
    public List<ResponseDocument> unbind(List<RequestDocument> requestDocuments, String operation) throws Exception {
        DocumentManager documentManager = null;
        // get the previous content from docStore to rollback the records if saving the session fails.

        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        for (RequestDocument requestDocument : requestDocuments) {
            documentManager = BeanLocator.getDocstoreFactory().getDocumentManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
            // update Docstore
            responseDocuments.add(documentManager.unbind(requestDocument, session, operation));

        }

        // Index to solr
        String result = getIndexerService(requestDocuments.get(0)).unbind(requestDocuments);
        if (!result.startsWith("success")) {
            throw new OleDocStoreException(result);
        }

        return responseDocuments;
    }

    @Override
    public List<ResponseDocument> delete(List<RequestDocument> requestDocuments) throws Exception {
        DocumentManager documentManager = null;
        // Save the request documents so that they can be rolled back if necessary.
        newStateRequestDocuments = requestDocuments;
        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        for (RequestDocument requestDocument : requestDocuments) {
            // Each document could be of different cat-type-format.
            documentManager = BeanLocator.getDocstoreFactory().getDocumentManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
            //Store
            responseDocuments.add(documentManager.delete(requestDocument, session));
        }
        // Index
        String result = getIndexerService(requestDocuments.get(0)).delete(requestDocuments);
        if (!result.startsWith("success")) {
            throw new OleDocStoreException(result);
        }
        return responseDocuments;
    }

    @Override
    public List<ResponseDocument> deleteVerify(List<RequestDocument> requestDocuments) throws Exception {
        DocumentManager documentManager = null;
        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        for (RequestDocument requestDocument : requestDocuments) {
            documentManager = BeanLocator.getDocstoreFactory().getDocumentManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
            responseDocuments.add(documentManager.deleteVerify(requestDocument, session));
        }
        return responseDocuments;

    }

    @Override
    public void transferInstances(List<RequestDocument> requestDocuments) throws Exception {
        JcrWorkInstanceDocumentManager.getInstance().transferInstances(requestDocuments, session);
        if (requestDocuments.size() > 0)
            getIndexerService(requestDocuments.get(0)).transferInstances(requestDocuments);
    }

    @Override
    public void transferItems(List<RequestDocument> requestDocuments) throws Exception {
        logger.debug("TransactionManager transferItems");
        JcrWorkInstanceDocumentManager.getInstance().transferItems(requestDocuments, session);
        if (requestDocuments.size() > 0)
            getIndexerService(requestDocuments.get(0)).transferItems(requestDocuments);
    }

    @Override
    public void batchIngest(BulkProcessRequest bulkProcessRequest, List<RequestDocument> requestDocuments)
            throws Exception {

        BulkIngestStatistics bulkIngestStatistics = bulkProcessRequest.getBulkIngestStatistics();
        BatchIngestStatistics batchStatistics = bulkIngestStatistics.getCurrentBatch();
        long commitSize = ProcessParameters.BULK_INGEST_COMMIT_SIZE;
        newStateRequestDocuments = new ArrayList<RequestDocument>();
        // Save the request documents so that they can be rolled back if necessary.
        newStateRequestDocuments.addAll(requestDocuments);
        DocumentManager documentManager = BeanLocator.getDocstoreFactory()
                .getDocumentManager(requestDocuments.get(0).getCategory(), requestDocuments.get(0).getType(), requestDocuments.get(0).getFormat());

        // Store
        StopWatch createNodesTimer = new StopWatch();
        createNodesTimer.start();
        documentManager.ingest(requestDocuments, session);
        createNodesTimer.stop();
        batchStatistics.setTimeToCreateNodesInJcr(createNodesTimer.getTime());
        batchStatistics.setCommitSize(commitSize);

        StopWatch indexSolrDocsTime = new StopWatch();
        indexSolrDocsTime.start();

        // Index
        String result = getIndexerService(requestDocuments.get(0)).indexDocuments(requestDocuments, false);
        indexSolrDocsTime.stop();
        batchStatistics.setTimeToIndexSolrInputDocs(indexSolrDocsTime.getTime());

        if (!result.startsWith("success")) {
            throw new OleDocStoreException(result);
        }

    }

    public void commit(BatchIngestStatistics batchIngestStatistics) throws OleDocStoreException {
        logger.info("Commit: Saving changes to index...");
        StopWatch solrCommitTimer = new StopWatch();
        try {
            if (null != batchIngestStatistics) {
                solrCommitTimer = new StopWatch();
                solrCommitTimer.start();
            }
            WorkBibDocumentIndexer.getInstance().commit();
            if (null != batchIngestStatistics) {
                solrCommitTimer.stop();
                batchIngestStatistics.setTimeToSolrCommit(solrCommitTimer.getTime());
            }
        } catch (Exception e) {
            transactionState = TransactionState.FAILED;
            logger.error("Exception during commit: Unable to save changes to index. :", e);
            try {
                session.refresh(false);
            } catch (Exception re) {
                // Ignore this, as data would not be saved anyway.
                logger.error("Exception during commit. Unable to rollback changes to docstore. :", re);
            }
            throw new OleDocStoreException("Commit failed.", e);
        }

        logger.info("Commit: Saving changes to docstore...");
        StopWatch sessionSaveTimer = null;
        try {
            if (null != batchIngestStatistics) {
                sessionSaveTimer = new StopWatch();
                sessionSaveTimer.start();
            }
            session.save();
            if (null != batchIngestStatistics) {
                sessionSaveTimer.stop();
                batchIngestStatistics.setTimeToSaveJcrSession(sessionSaveTimer.getTime());
            }
        } catch (Exception e) {
            transactionState = TransactionState.FAILED;
            logger.error("Exception during commit. Unable to save changes to docstore. :", e);
            logger.info("Commit: Reverting changes to index...");
            // TODO: Implement now. Delete data saved by indexerService.
            try {
                rollBackDataInIndexer(oldStateRequestDocuments);
//                rollBackDataInIndexer(oldStateRequestDocuments,newStateRequestDocuments);
            } catch (Exception ex) {
                logger.error("error while performing roll back in Indexer");
            }
            throw new OleDocStoreException("Commit failed.", e);
        }
        //Session logout is handled in DocumentServiceImpl.java
        // session.logout();
        // We are all done!!!
        transactionState = TransactionState.COMMITTED;
        newStateRequestDocuments = null;
        oldStateRequestDocuments = null;
    }

    private void rollBackDataInIndexer(List<RequestDocument> oldStateRequestDocuments) throws OleDocStoreException {
        if (oldStateRequestDocuments != null && oldStateRequestDocuments.size() > 0) {
            for (RequestDocument requestDocument : oldStateRequestDocuments) {
                String result = getIndexerService(requestDocument).indexDocument(requestDocument, false);
                if (!result.startsWith("success")) {
                    throw new OleDocStoreException(result);
                }
            }
        }
    }


}

