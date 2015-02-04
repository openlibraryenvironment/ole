package org.kuali.ole.docstore.service;

import org.apache.commons.lang.time.StopWatch;
import org.apache.cxf.common.util.StringUtils;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.factory.DocstoreFactory;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.docstore.process.BulkIngestNIndexProcessor;
import org.kuali.ole.docstore.process.BulkLoadHandler;
import org.kuali.ole.docstore.process.DocStoreCamelContext;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.kuali.ole.docstore.process.batch.BulkProcessRequest;
import org.kuali.ole.docstore.transaction.TransactionManager;
import org.kuali.ole.docstore.util.ItemExistsException;
import org.kuali.ole.docstore.utility.BatchIngestStatistics;
import org.kuali.ole.docstore.utility.BulkIngestStatistics;
import org.kuali.ole.pojo.OleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Implements DocumentService interface.
 * User: tirumalesh.b
 * Date: 28/8/12 Time: 12:09 PM
 */
public class DocumentServiceImpl
        implements DocumentService {
    private static Logger logger = LoggerFactory.getLogger(BulkIngestNIndexProcessor.class);
    private static DocumentServiceImpl documentService = new DocumentServiceImpl();
    private BulkProcessRequest bulkIngestRequest = null;
    //    private        BulkLoadHandler              bulkLoadHandler              = null;
    //    private        BulkIngestNIndexRouteBuilder bulkIngestNIndexRouteBuilder = null;
    //    private        BulkIngestNIndexProcessor    bulkIngestNIndexProcessor    = null;
    //    private        BulkIngestStatistics         bulkLoadStatistics           = null;
    //    private BulkIngestProcessHandlerService bIService        = (BulkIngestProcessHandlerService) BeanLocator
    //                                                                     .getBean("bulkIngestProcessHandlerService");
    protected RepositoryManager repositoryManager;
    private TransactionManager transactionManager;

    public static DocumentServiceImpl getInstance() {
        return documentService;
    }

    private DocumentServiceImpl() {
        try {
            this.repositoryManager = RepositoryManager.getRepositoryManager();
        } catch (OleException oe) {
            //throw new OleDocStoreException(oe);
            // TODO: log the exception
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public Response process(Request request) throws OleDocStoreException, RepositoryException, OleException, FileNotFoundException {
        authenticateAndAuthorizeUser(request);
        validateInput(request);
        DocstoreFactory docstoreFactory = BeanLocator.getDocstoreFactory();

        List<RequestDocument> requestDocuments = request.getRequestDocuments();
        if (transactionManager==null){
            transactionManager = docstoreFactory.getTransactionManager(requestDocuments.get(0).getCategory(), requestDocuments.get(0).getType(), requestDocuments.get(0).getFormat());
        }


        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        Response response = new Response();
        response.setOperation(request.getOperation());
        response.setUser(request.getUser());
        try {
            if (request.getOperation().equals(Request.Operation.ingest.toString())) {
                transactionManager.startTransaction(request.getUser(), request.getOperation());
                try {
                    responseDocuments = transactionManager.ingest(requestDocuments);
                    transactionManager.commit();
                    transactionManager.closeSession();
                } catch (OleDocStoreException e) {
                    transactionManager.abort();
                    transactionManager.closeSession();
                    throw e;
                }
                response.setStatus("Success");
                response.setMessage("Documents ingested.");
                response.setStatusMessage("Documents ingested successfully.");
                response.setDocuments(responseDocuments);
            } else if (request.getOperation().equals(Request.Operation.checkIn.toString())) {
                transactionManager.startTransaction(request.getUser(), request.getOperation());
                try {
                    responseDocuments = transactionManager.checkIn(request.getRequestDocuments());
                    transactionManager.commit();
                    transactionManager.closeSession();
                } catch (OleDocStoreException e) {
                    transactionManager.abort();
                    transactionManager.closeSession();
                    throw e;
                }
                response.setStatus("Success");
                response.setMessage("Documents Checked In .");
                response.setStatusMessage("Documents Checked In successfully.");
                response.setDocuments(responseDocuments);
            } else if (request.getOperation().equals(Request.Operation.checkOut.toString())) {
                transactionManager.startSession(request.getUser(), request.getOperation());
                responseDocuments = transactionManager.checkOut(request.getRequestDocuments(), request.getUser());
                transactionManager.closeSession();
                response.setStatus("Success");
                response.setMessage("Documents Checked Out.");
                response.setStatusMessage("Documents Checked Out successfully.");
                response.setDocuments(responseDocuments);
            } else if (request.getOperation().equalsIgnoreCase(Request.Operation.deleteVerify.toString())) {
                transactionManager.startTransaction(request.getUser(), request.getOperation());
                try {
                    responseDocuments = transactionManager.deleteVerify(request.getRequestDocuments());
                    response.setStatus("Success");
                    response.setMessage("Delete Verify success");
                    response.setStatusMessage("Delete Verify success");
                    response.setDocuments(responseDocuments);
                    //                response.setOperation("deleteVerify");
                    //                response.setMessage("success");
                    //                response.setStatus("success");
                    //                response.setUser("kuali");
                    //                response.setDocuments(responseDocuments);
                    logger.debug("deleteVerify toXML " + new ResponseHandler().toXML(response));

                } catch (OleDocStoreException e) {
                    transactionManager.abort();
                    transactionManager.closeSession();
                    throw e;
                }
            } else if (request.getOperation().contains(Request.Operation.delete.toString())) {
                transactionManager.startTransaction(request.getUser(), request.getOperation());
                try {
                    responseDocuments = transactionManager.delete(request.getRequestDocuments());
                    transactionManager.commit();
                    transactionManager.closeSession();
                } catch (OleDocStoreException e) {
                    transactionManager.abort();
                    transactionManager.closeSession();
                    throw e;
                }
                response.setStatus("Success");
                response.setMessage("Documents deleted");
                response.setStatusMessage("Documents deleted successfully");
                response.setDocuments(responseDocuments);
            } else if (request.getOperation().equalsIgnoreCase(Request.Operation.bind.toString())) {
                transactionManager.startTransaction(request.getUser(), request.getOperation());
                try {
                    responseDocuments = transactionManager.bind(request.getRequestDocuments(), request.getOperation());
                    transactionManager.commit();
                    transactionManager.closeSession();
                } catch (Exception e) {
                    transactionManager.abort();
                    transactionManager.closeSession();
                    logger.error(e.getMessage() , e);
                }
                response.setStatus("Success");
                response.setMessage("Documents bounded In .");
                response.setStatusMessage("Documents bounded In successfully.");
                response.setDocuments(responseDocuments);
//            } else if (request.getOperation().equalsIgnoreCase(Request.Operation.unbind.toString())) {
//                transactionManager.startTransaction(request.getUser(), request.getOperation());
//                responseDocuments = transactionManager.unbind(request.getRequestDocuments(), request.getOperation());
//                transactionManager.commit();
//                transactionManager.closeSession();
//                response.setStatus("Success");
//                response.setMessage("Documents unbounded.");
//                response.setStatusMessage("Documents unbounded successfully.");
//                response.setDocuments(responseDocuments);
            } else if (request.getOperation().contains(Request.Operation.transferInstances.toString())) {
                try {

                    transactionManager.startTransaction(request.getUser(), request.getOperation());
                    transactionManager.transferInstances(request.getRequestDocuments());
                    transactionManager.commit();
                    transactionManager.closeSession();
                } catch (Exception e) {
                    transactionManager.abort();
                    transactionManager.closeSession();
                    logger.error(e.getMessage() , e);
                }
            } else if (request.getOperation().contains(Request.Operation.transferItems.toString())) {
                try {
                    transactionManager.startTransaction(request.getUser(), request.getOperation());
                    transactionManager.transferItems(request.getRequestDocuments());
                    transactionManager.commit();
                    transactionManager.closeSession();
                    response.setStatus("success");
                    response.setMessage("Transfer Items success ");
                    response.setStatusMessage("Status : Transfer of items success ");
                    response.setDocuments(responseDocuments);
                } catch (ItemExistsException itemExistsException) {
                    System.out.println("itemExistsException" + itemExistsException);
                    response.setStatus("failure");
                    response.setMessage("Transfer Items Failed " + itemExistsException.getMessage());
                    response.setStatusMessage("Status : Transfer of items failed " + itemExistsException.getMessage());
                    response.setDocuments(responseDocuments);
                    transactionManager.abort();
                    transactionManager.closeSession();
                } catch (Exception e) {
                    logger.error(e.getMessage() , e);
                    transactionManager.abort();
                    transactionManager.closeSession();
                }
            }

        } catch (OleDocStoreException ode) {
            logger.error("", ode);
            response.setStatus("Failed");
            response.setMessage("Operation failed.");
            response.setStatusMessage(ode.getMessage());
            response.setDocuments(responseDocuments);
        } catch (Exception e) {
            logger.error(e.getMessage() , e);
        }
        return response;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void bulkProcess(BulkProcessRequest request) throws Exception {
        authenticateAndAuthorizeUser(request);
        //The following line is commented out as it was handled in method bulkIngest()
        // validateBulkProcessInput(request, null);

        Response response = null;
        if (request.getOperation().equals(BulkProcessRequest.BulkProcessOperation.INGEST)) {
            response = bulkIngestManage(request);
        }
    }

    /**
     * Manages the bulk ingest process.
     */
    private Response bulkIngestManage(BulkProcessRequest request) throws Exception {
        if ((request.getAction()).equals(BulkProcessRequest.BulkProcessAction.START)) {
            if (request.getDataFormat().equals(BulkProcessRequest.BulkIngestDataFormat.DOCSTORE)) {
                if (bulkIngestRequest != null) {
                    //DocStoreCamelContext.getInstance().resume();
                    throw new OleDocStoreException("Bulk ingest already running!");
                } else {
                    BeanLocator.getBulkIngestProcessHandlerService().startBulkIngestForDocStoreRequestFormat(request.getBulkIngestFolder());
                    BulkLoadHandler bulkLoadHandler = BeanLocator.getBulkIngestProcessHandlerService().getLoadHandler();
                    BulkIngestNIndexProcessor bulkIngestNIndexProcessor = bulkLoadHandler.getBulkRoute()
                            .getBulkIngestNIndexProcessor();
                    request.setBulkIngestStatistics(BulkIngestStatistics.getInstance());
                    bulkIngestNIndexProcessor.setBulkProcessRequest(request);
                    //bulkLoadStatistics = bulkIngestNIndexProcessor.getBulkLoadStatistics();
                    bulkIngestRequest = request;
                    DocStoreCamelContext.getInstance().resume();
                }
            } else if (request.getDataFormat().equals(BulkProcessRequest.BulkIngestDataFormat.STANDARD)) {
                String folder = request.getDataFolder();
                if (folder != null && folder.trim().length() != 0) {
                    BeanLocator.getBulkIngestProcessHandlerService()
                            .startBulkIngestForStandardXMLFormat(request.getDataFolder(), request.getDocCategory(),
                                    request.getDocType(), request.getDocFormat(), request.getBulkIngestFolder());
                }
            }
        } else if ((request.getAction()).equals(BulkProcessRequest.BulkProcessAction.STATUS)) {
            logger.info(bulkIngestRequest.getBulkIngestStatistics().getJsonString());
        } else if ((request.getAction()).equals(BulkProcessRequest.BulkProcessAction.STOP)) {
            DocStoreCamelContext.getInstance().suspend();
        } else if ((request.getAction()).equals(BulkProcessRequest.BulkProcessAction.CLEAR)) {
            bulkIngestRequest.getBulkIngestStatistics().clearBulkIngestStatistics();
        }
        return null;
    }

    public void bulkIngest(BulkProcessRequest bulkProcessRequest, List<RequestDocument> requestDocuments)
            throws Exception {
        TransactionManager transactionManager = bulkProcessRequest.getTransactionManager();
        validateBulkProcessInput(bulkProcessRequest, requestDocuments);
        if (null == transactionManager) {
            transactionManager = BeanLocator.getDocstoreFactory().getTransactionManager(requestDocuments.get(0).getCategory(), requestDocuments.get(0).getType(), requestDocuments.get(0).getFormat());
            transactionManager.startTransaction(bulkProcessRequest.getUser(),
                    "bulk" + bulkProcessRequest.getOperation().toString());
            bulkProcessRequest.setTransactionManager(transactionManager);
        }
        //        if (bulkProcessRequest.getPreviousBatchDocuments() == null) {
        //            List<RequestDocument> newList = new ArrayList<RequestDocument>();
        //            bulkProcessRequest.setPreviousBatchDocuments(newList);
        //        }
        //        bulkProcessRequest.getPreviousBatchDocuments().addAll(requestDocuments);

        batchIngest(bulkProcessRequest, requestDocuments);

    }

    public void batchIngest(BulkProcessRequest bulkProcessRequest, List<RequestDocument> requestDocuments) {
        BulkIngestStatistics bulkIngestStatistics = bulkProcessRequest.getBulkIngestStatistics();
        BatchIngestStatistics batchStatistics = bulkIngestStatistics.getCurrentBatch();
        long commitSize = ProcessParameters.BULK_INGEST_COMMIT_SIZE;
        logger.debug("commitSize = " + commitSize);
        logger.debug("bulkIngestNIndex(" + requestDocuments.size() + ") START");
        logger.debug("BULK_INGEST_IS_LINKING_ENABLED=" + ProcessParameters.BULK_INGEST_IS_LINKING_ENABLED);
        StopWatch totalTimer = new StopWatch();
        StopWatch sessionSaveTimer = new StopWatch();
        long recCount = requestDocuments.size();
        batchStatistics.setRecCount(recCount);

        boolean isCommit = false;
        totalTimer.start();
        TransactionManager transactionManager = bulkProcessRequest.getTransactionManager();
        try {
            transactionManager.batchIngest(bulkProcessRequest, requestDocuments);
            bulkIngestStatistics.setCommitRecCount(bulkIngestStatistics.getCommitRecCount() + recCount);
            if (bulkIngestStatistics.getCommitRecCount() == commitSize || bulkIngestStatistics.isLastBatch()) {
                isCommit = true;
            }
            if (isCommit) {
                sessionSaveTimer.start();
                logger.info("Bulk ingest: Commit started. Number of records being committed : " + bulkIngestStatistics
                        .getCommitRecCount());
                transactionManager.commit();
                bulkIngestStatistics.setCommitRecCount(0);
                sessionSaveTimer.stop();
            }

            // Documents processed can be different from records processed as in the case of Instance data.
            logger.debug("Documents processed: " + recCount);
            bulkIngestStatistics.setFileRecCount(bulkIngestStatistics.getFileRecCount() + recCount);
            logger.info(
                    "Bulk ingest: Records processed in the current file :" + bulkIngestStatistics.getFileRecCount());
        } catch (Exception e) {
            transactionManager.abort();
            bulkIngestStatistics.setCommitRecCount(0);
            logger.error("Document Ingest & Index Failed, Cause: " + e.getMessage(), e);
        }
        totalTimer.stop();
        batchStatistics.setTimeToSaveJcrSession(sessionSaveTimer.getTime());
        batchStatistics.setIngestingTime(
                batchStatistics.getTimeToCreateNodesInJcr() + batchStatistics.getTimeToSaveJcrSession());
        batchStatistics
                .setIndexingTime(batchStatistics.getTimeToIndexSolrInputDocs() + batchStatistics.getTimeToSolrCommit());
        batchStatistics.setIngestNIndexTotalTime(totalTimer.getTime());
    }


    public void setRepositoryManager(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    /*
     *
     *
     * @param request
     */

    /**
     * Validates the given request for normal processing.
     *
     * @param request
     * @throws OleDocStoreException - if the operation is invalid
     *                              - if no documents are specified
     */
    private void validateInput(Request request) throws OleDocStoreException {
        Set<String> validOperationSet = Request.validOperationSet;
        if (StringUtils.isEmpty(request.getUser())) {
            throw new OleDocStoreException("User cannot be null or empty. Please verify input file.");
        }

        if (StringUtils.isEmpty(request.getOperation())) {
            throw new OleDocStoreException("Operation cannot be null or empty. Please verify input file");
        } else {
            //verify for valid docstore operation
            if (validOperationSet.contains(request.getOperation())) {
                for (RequestDocument requestDocument : request.getRequestDocuments()) {
                    requestDocument.setUser(request.getUser());
                    requestDocument.setOperation(request.getOperation());
                }
            } else {
                throw new OleDocStoreException("Not a valid Docstore operation:" + request.getOperation());
            }
        }
    }

    /**
     * Verifies whether the user is authenticated and authorized to execute the request.
     *
     * @param request
     * @throws OleDocStoreException
     */
    private void authenticateAndAuthorizeUser(Request request) throws OleDocStoreException {
        // TODO: Implement later.
    }

    private void authenticateAndAuthorizeUser(BulkProcessRequest request) throws OleDocStoreException {
        // TODO: Implement later.
    }

    /**
     * Validates the given request for bulk processing.
     *
     * @param request
     * @throws OleDocStoreException - if the operation is invalid
     *                              - if no documents are specified
     *                              - if all documents are not of same [cat-type-format]
     */
    private void validateBulkProcessInput(BulkProcessRequest request, List<RequestDocument> requestDocuments)
            throws OleDocStoreException {

        Set<String> validOperationSet = BulkProcessRequest.validOperationSet;
        if (StringUtils.isEmpty(request.getUser())) {
            request.setUser("BulkIngest-User");
        }

        if (StringUtils.isEmpty(request.getOperation().toString())) {
            throw new OleDocStoreException("Operation cannot be null or empty. Please verify input file");
        } else {
            //verify for valid docstore operation
            if (validOperationSet.contains(request.getOperation().toString())) {
                for (RequestDocument requestDocument : requestDocuments) {
                    requestDocument.setUser(request.getUser());
                    requestDocument.setOperation(request.getOperation().toString());
                }
            } else {
                throw new OleDocStoreException("Not a valid Docstore operation:" + request.getOperation());
            }
        }
    }

    public BulkProcessRequest getBulkIngestRequest() {
        return bulkIngestRequest;
    }

    public void setBulkIngestRequest(BulkProcessRequest bulkIngestRequest) {
        this.bulkIngestRequest = bulkIngestRequest;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

}
