package org.kuali.ole.docstore.transaction;

import org.apache.commons.lang.time.StopWatch;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.document.DocumentManager;
import org.kuali.ole.docstore.document.jcr.JcrWorkInstanceDocumentManager;
import org.kuali.ole.docstore.document.rdbms.RdbmsWorkInstanceDocumentManager;
import org.kuali.ole.docstore.factory.DocstoreFactory;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.indexer.solr.WorkBibDocumentIndexer;
import org.kuali.ole.docstore.indexer.solr.WorkInstanceDocumentIndexer;
import org.kuali.ole.docstore.indexer.solr.WorkItemDocumentIndexer;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseStatus;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.kuali.ole.docstore.process.batch.BulkProcessRequest;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.utility.BatchIngestStatistics;
import org.kuali.ole.docstore.utility.BulkIngestStatistics;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 6/20/13
 * Time: 12:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class RdbmsTransactionManager extends AbstractTransactionManager {

    private static final Logger logger = LoggerFactory.getLogger(RdbmsTransactionManager.class);
    private BusinessObjectService businessObjectService;
    private IndexerService indexerService;
    private TransactionState transactionState = TransactionState.IDLE;
    private List<RequestDocument> newStateRequestDocuments = null;
    private List<RequestDocument> oldStateRequestDocuments = new ArrayList<RequestDocument>();

    @Override
    public void startTransaction(String user, String operation) throws Exception {
        try {
            if (null == businessObjectService) {
                businessObjectService = KRADServiceLocator.getBusinessObjectService();
            }
           /* if (null == indexerService) {
                indexerService = BeanLocator.getDocstoreFactory().getIndexerService();
            }*/
        } catch (Exception e) {
            logger.error(e.getMessage() , e );

        }
    }


    @Override
    public List<ResponseDocument> ingest(List<RequestDocument> requestDocuments) throws Exception {

        DocstoreFactory docstoreFactory = BeanLocator.getDocstoreFactory();
        // Save the request documents so that they can be rolled back if necessary.
        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        List<String> valuesList = new ArrayList<String>();
        for (RequestDocument requestDocument : requestDocuments) {
            // Each document could be of different cat-type-format.
            DocumentManager documentManager = docstoreFactory.getDocumentManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
            ResponseDocument respDoc = new ResponseDocument();
            try {
                documentManager.validateInput(requestDocument, businessObjectService, valuesList);
            } catch (OleDocStoreException e) {
                respDoc.setStatus(ResponseStatus.INVALID_DATA.toString());
                logger.debug("validationMessage-->" + e.getMessage(), e);
                throw new OleDocStoreException(e.getMessage(), e);
            }

            documentManager.ingest(requestDocument, businessObjectService, respDoc);
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
        DocstoreFactory docstoreFactory = BeanLocator.getDocstoreFactory();
        // Save the request documents so that they can be rolled back if necessary.
        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        List<String> valuesList = new ArrayList<String>();
        for (RequestDocument requestDocument : requestDocuments) {
            // Each document could be of different cat-type-format.
            DocumentManager documentManager = BeanLocator.getDocstoreFactory().getDocumentManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
            if (requestDocument.getId() != null || requestDocument.getId().trim().length() > 0) {
                requestDocument.setUuid(requestDocument.getId());
            }
            //requestDocument.setOperation(operation);
            //requestDocument.setUser(user);
            // validate given input
            ResponseDocument respDoc = new ResponseDocument();

            try {
                documentManager.validateInput(requestDocument, businessObjectService, valuesList);
            } catch (OleDocStoreException e) {
                respDoc.setStatus(ResponseStatus.INVALID_DATA.toString());
                logger.debug("validationMessage-->" + e.getMessage(), e);
                throw new OleDocStoreException(e.getMessage());
            }
            // Each document could be of different cat-type-format.
            //documentManager = BeanLocator.getDocumentManagerFactory().getDocumentManager(requestDocument);

            // update Docstore
            documentManager.checkin(requestDocument, businessObjectService, respDoc);
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
        DocstoreFactory docstoreFactory = BeanLocator.getDocstoreFactory();
        // Save the request documents so that they can be rolled back if necessary.
        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        List<String> valuesList = new ArrayList<String>();
        for (RequestDocument requestDocument : requestDocuments) {
            // Each document could be of different cat-type-format.
            DocumentManager documentManager = docstoreFactory.getDocumentManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
            responseDocuments.add(documentManager.checkout(requestDocument, businessObjectService));
        }
        return responseDocuments;
    }

    @Override
    public List<ResponseDocument> bind(List<RequestDocument> requestDocuments, String operation) throws Exception {
        DocumentManager documentManager = null;
        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        for (RequestDocument requestDocument : requestDocuments) {
            documentManager = BeanLocator.getDocstoreFactory()
                    .getDocumentManager(requestDocument.getCategory(), requestDocument.getType(),
                            requestDocument.getFormat());
            responseDocuments.add(documentManager.bind(requestDocument, businessObjectService, operation));
        }

        // Index to solr
        for (RequestDocument requestDocument : requestDocuments) {
            String result = getIndexerService(requestDocument).bind(requestDocument);
            if (!result.startsWith("success")) {
                throw new OleDocStoreException(result);
            }

        }
        return responseDocuments;
    }

    @Override
    public List<ResponseDocument> unbind(List<RequestDocument> requestDocuments, String operation) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ResponseDocument> delete(List<RequestDocument> requestDocuments) throws Exception {
        DocumentManager documentManager = null;
        // Save the request documents so that they can be rolled back if necessary.
        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        for (RequestDocument requestDocument : requestDocuments) {
            // Each document could be of different cat-type-format.
            documentManager = BeanLocator.getDocstoreFactory()
                    .getDocumentManager(requestDocument.getCategory(), requestDocument.getType(),
                            requestDocument.getFormat());
            //Store
            responseDocuments.add(documentManager.delete(requestDocument, businessObjectService));
        }

        for (int index = 0; index < requestDocuments.size(); index++) {
            ResponseDocument responseDocument = responseDocuments.get(index);
            if ("success".equals(responseDocument.getStatus())) {
                RequestDocument requestDocument = requestDocuments.get(index);
                String result = getIndexerService(requestDocument).delete(requestDocument);
                if (!result.startsWith("success")) {
                    throw new OleDocStoreException(result);
                }
            }
        }
        return responseDocuments;
    }

    @Override
    public List<ResponseDocument> deleteVerify(List<RequestDocument> requestDocuments) throws Exception {
        DocumentManager documentManager = null;
        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        for (RequestDocument requestDocument : requestDocuments) {
            documentManager = BeanLocator.getDocstoreFactory().getDocumentManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
            responseDocuments.add(documentManager.deleteVerify(requestDocument, businessObjectService));
        }
        return responseDocuments;
    }

    @Override
    public void transferInstances(List<RequestDocument> requestDocuments) throws Exception {
        logger.debug("TransactionManager transferInstancessssssssssssssssss");
        RdbmsWorkInstanceDocumentManager.getInstance().transferInstances(requestDocuments, businessObjectService);
        WorkInstanceDocumentIndexer.getInstance().transferInstances(requestDocuments);
    }

    @Override
    public void transferItems(List<RequestDocument> requestDocuments) throws Exception {
        logger.debug("TransactionManager transferItemsssssssssss");
        RdbmsWorkInstanceDocumentManager.getInstance().transferItems(requestDocuments, businessObjectService);
        WorkItemDocumentIndexer.getInstance().transferItems(requestDocuments);
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
                .getDocumentManager(requestDocuments.get(0).getCategory(),
                        requestDocuments.get(0).getType(),
                        requestDocuments.get(0).getFormat());

        // Store
        StopWatch createNodesTimer = new StopWatch();
        createNodesTimer.start();
        documentManager.ingest(requestDocuments, businessObjectService);
        createNodesTimer.stop();
        batchStatistics.setTimeToCreateNodesInJcr(createNodesTimer.getTime());
        batchStatistics.setCommitSize(commitSize);

        StopWatch indexSolrDocsTime = new StopWatch();
        indexSolrDocsTime.start();

        // Index
        String result = getIndexerService(requestDocuments.get(0)).bulkIndexDocuments(requestDocuments, false);
//        for (RequestDocument requestDocument : requestDocuments) {
//            String result = getIndexerService(requestDocument).indexDocument(requestDocument, false);
//            if (!result.startsWith("success")) {
//                throw new OleDocStoreException(result);
//            }
//        }
        indexSolrDocsTime.stop();
        batchStatistics.setTimeToIndexSolrInputDocs(indexSolrDocsTime.getTime());

        if (!result.startsWith("success")) {
            throw new OleDocStoreException(result);
        }
    }

    @Override
    public void startSession(String user, String operation) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void commit() throws Exception {
        commit(null);
    }

    @Override
    public void closeSession() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void abort() {
        //To change body of implemented methods use File | Settings | File Templates.
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

            throw new OleDocStoreException("Commit failed.", e);
        }

        logger.info("Commit: Saving changes to docstore...");
        StopWatch sessionSaveTimer = null;
        try {
            if (null != batchIngestStatistics) {
                sessionSaveTimer = new StopWatch();
                sessionSaveTimer.start();
            }
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
                logger.error("error while performing roll back in Indexer" , ex);
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

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
}
