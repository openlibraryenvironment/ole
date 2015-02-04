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
package org.kuali.ole.docstore.service;

import org.apache.commons.lang.time.StopWatch;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.common.document.content.instance.Instance;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.docstore.process.BulkIngestTimeManager;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.kuali.ole.docstore.utility.BatchIngestStatistics;
import org.kuali.ole.docstore.utility.BulkIngestStatistics;
import org.kuali.ole.pojo.OleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.jcr.Session;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class to IngestNIndexHandlerService.
 *
 * @author Rajesh Chowdary K
 * @created Feb 15, 2012
 * <p/>
 * Singleton instance of this class is created by Spring.
 */
public class IngestNIndexHandlerService {

    private static Logger logger = LoggerFactory.getLogger(IngestNIndexHandlerService.class);

    /**
     * Singleton instance of  RequestHandler initialized by Spring DI.
     */
    private RequestHandler requestHandler;
    /**
     * Singleton instance of  DocumentIngester initialized by Spring DI.
     */
    private DocumentIngester documentIngester;
    /**
     * Singleton instance of  DocumentIndexer initialized by Spring DI.
     */
    private DocumentIndexer documentIndexer;
    private static long docCount = 0;
    private BulkIngestStatistics bulkLoadStatistics = BulkIngestStatistics.getInstance();
    private static List<RequestDocument> prevRequestDocs = null;
    private RepositoryManager repositoryManager;

    @Required
    public void setDocumentIngester(DocumentIngester documentIngester) {
        this.documentIngester = documentIngester;
    }

    @Required
    public void setDocumentIndexer(DocumentIndexer documentIndexer) {
        this.documentIndexer = documentIndexer;
    }

    @Required
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    /**
     * Method to ingest & index xml String Request Document
     *
     * @param xmlRequestString
     * @return
     * @throws Exception
     */
    public String ingestNIndexRequestDocuments(String xmlRequestString) throws Exception {
        Request request = null;
        request = requestHandler.toObject(xmlRequestString);
        Response response = ingestNIndexRequestDocuments(request);
        String xmlResponse = new ResponseHandler().toXML(response);
        return xmlResponse;
    }

    /**
     * `
     * <p/>
     * Method to ingest & index xml String Request Document
     *
     * @param request
     * @return
     * @throws Exception
     */
    public Response ingestNIndexRequestDocuments(Request request) throws Exception {

        for (RequestDocument doc : request.getRequestDocuments()) {
            doc.setUser(request.getUser());
        }
        Session session = null;
        List<String> docUUIDs = new ArrayList<String>();
        try {
            session = getRepositoryManager().getSession(request.getUser(), request.getOperation());

            // Ingest & check for any unsupported Category/Type/Formats
            for (RequestDocument reqDoc : request.getRequestDocuments()) {
                if (DocCategory.WORK.isEqualTo(reqDoc.getCategory())) {
                    if (DocType.BIB.isEqualTo(reqDoc.getType())) { // Biblographic
                        if (DocFormat.MARC.isEqualTo(reqDoc.getFormat())
                                || DocFormat.DUBLIN_CORE.isEqualTo(reqDoc.getFormat()) || DocFormat.DUBLIN_UNQUALIFIED
                                .isEqualTo(reqDoc.getFormat())) {
                            docUUIDs.addAll(documentIngester.ingestBibNLinkedInstanceRequestDocuments(reqDoc, session));
                            documentIndexer.indexDocument(reqDoc);
                        } else {
                            logger.error("Unsupported Document Format : " + reqDoc.getFormat() + " Called.");
                            throw new Exception("Unsupported Document Format : " + reqDoc.getFormat() + " Called.");
                        }
                    } else if (DocType.INSTANCE.isEqualTo(reqDoc.getType())) { // Instace
                        if (DocFormat.OLEML.isEqualTo(reqDoc.getFormat())) { // OLE-ML
                            documentIngester.ingestInstanceDocument(reqDoc, session, docUUIDs, null, null);
                            documentIndexer.indexDocument(reqDoc);
                        } else {
                            logger.error("Unsupported Document Format : " + reqDoc.getFormat() + " Called.");
                            throw new Exception("Unsupported Document Format : " + reqDoc.getFormat() + " Called.");
                        }
                    } else if (DocType.LICENSE.isEqualTo(reqDoc.getType())) { // License
                        if (DocFormat.ONIXPL.isEqualTo(reqDoc.getFormat())
                                || DocFormat.PDF.isEqualTo(reqDoc.getFormat())
                                || DocFormat.DOC.isEqualTo(reqDoc.getFormat()) || DocFormat.XSLT
                                .isEqualTo(reqDoc.getFormat())) { //Onixpl, pdf, doc, xslt.
                            documentIngester.ingestWorkLicenseOnixplRequestDocument(reqDoc, session, docUUIDs);
                            documentIndexer.indexDocument(reqDoc);
                        } else {
                            logger.error("Unsupported Document Format : " + reqDoc.getFormat() + " Called.");
                            throw new Exception("Unsupported Document Format : " + reqDoc.getFormat() + " Called.");
                        }
                    } else {
                        logger.error("Unsupported Document Type : " + reqDoc.getType() + " Called.");
                        throw new Exception("Unsupported Document Type : " + reqDoc.getType() + " Called.");
                    }
                } else if (DocCategory.SECURITY.isEqualTo(reqDoc.getCategory())) { // Security
                    if (DocType.PATRON.isEqualTo(reqDoc.getType())) { // Patron
                        if (DocFormat.OLEML.isEqualTo(reqDoc.getFormat())) { // oleml
                            docUUIDs.addAll(documentIngester.ingestPatronRequestDocument(reqDoc, session, null));
                            documentIndexer.indexDocument(reqDoc);
                        } else {
                            logger.error("Unsupported Document Format : " + reqDoc.getFormat() + " Called.");
                            throw new Exception("Unsupported Document Format : " + reqDoc.getFormat() + " Called.");
                        }
                    } else {
                        logger.error("Unsupported Document Type : " + reqDoc.getType() + " Called.");
                        throw new Exception("Unsupported Document Type : " + reqDoc.getType() + " Called.");
                    }
                } else {
                    logger.error("Unsupported Category : " + reqDoc.getCategory() + " Called.");
                    throw new Exception("Unsupported Document Category : " + reqDoc.getCategory() + " Called.");
                }
            }

            // Commit: DocStore
            session.save();

        } catch (Exception e) {
            logger.error("Document Ingest & Index Failed, Cause: " + e.getMessage(), e);
            documentIngester.rollbackDocStoreIngestedData(session, request.getRequestDocuments());
            documentIndexer.rollbackIndexedData(request.getRequestDocuments());
            throw e;
        } finally {
            if (session != null) {
                getRepositoryManager().logout(session);
            }
        }
        Response response = buildResponse(request);
        return response;
    }

    private RepositoryManager getRepositoryManager() throws OleException {
        if (null == repositoryManager) {
            repositoryManager = RepositoryManager.getRepositoryManager();
        }
        return repositoryManager;
    }

    /**
     * Method to ingest and index bulk Request.
     *
     * @param request
     * @return
     */
    public List<String> bulkIngestNIndex(Request request, Session session) {
        //RequestDocument requestDocument = request.getRequestDocuments().get(0);
        //DocumentManager documentManager = BeanLocator.getDocumentManagerFactory().getDocumentManager(requestDocument);
        BatchIngestStatistics batchStatistics = BulkIngestStatistics.getInstance().getCurrentBatch();
        BulkIngestStatistics bulkLoadStatistics = BulkIngestStatistics.getInstance();
        long commitSize = ProcessParameters.BULK_INGEST_COMMIT_SIZE;
        logger.debug("commitSize = " + commitSize);
        logger.debug("bulkIngestNIndex(" + request.getRequestDocuments().size() + ") START");
        logger.debug("BULK_INGEST_IS_LINKING_ENABLED=" + ProcessParameters.BULK_INGEST_IS_LINKING_ENABLED);
        //Session session = null;
        List<String> docUUIDs = new ArrayList<String>();
        StopWatch ingestTimer = new StopWatch();
        StopWatch indexTimer = new StopWatch();
        StopWatch totalTimer = new StopWatch();
        StopWatch createNodesTimer = new StopWatch();
        StopWatch sessionSaveTimer = new StopWatch();
        StopWatch solrOptimizeTimer = new StopWatch();
        long recCount = request.getRequestDocuments().size();
        boolean isCommit = false;
        totalTimer.start();
        try {
            ingestTimer.start();
            createNodesTimer.start();
            //session = RepositoryManager.getRepositoryManager().getSession(request.getUser(), request.getOperation());
            List<RequestDocument> reqDocs = request.getRequestDocuments();
            if (prevRequestDocs == null) {
                prevRequestDocs = new ArrayList<RequestDocument>();
            }
            prevRequestDocs.addAll(request.getRequestDocuments());
            logger.info("prevRequestDocs" + prevRequestDocs.size());
            docUUIDs.addAll(documentIngester.ingestRequestDocumentsForBulk(reqDocs, session));
            //docUUIDs.addAll(documentIngester.ingestRequestDocumentsForBulkUsingBTreeMgr(reqDocs, session));
            //documentManager.store(reqDocs,session);
            createNodesTimer.stop();
            try {
                ingestTimer.suspend();
                indexTimer.start();
            } catch (Exception e2) {
                logger.error(e2.getMessage() , e2 );
            }
            bulkLoadStatistics.setCommitRecCount(bulkLoadStatistics.getCommitRecCount() + recCount);
            if (bulkLoadStatistics.getCommitRecCount() == commitSize || bulkLoadStatistics.isLastBatch()) {
                isCommit = true;
            }
            documentIndexer.indexDocumentsForBulk(reqDocs, isCommit);
            //documentManager.index(reqDocs,isCommit);
            try {
                indexTimer.suspend();
                ingestTimer.resume();
            } catch (Exception e2) {
                logger.error(e2.getMessage() , e2 );
            }
            if (isCommit) {
                sessionSaveTimer.start();
                logger.info("Bulk ingest: Repository commit started. Number of records being committed : "
                        + bulkLoadStatistics.getCommitRecCount());
                session.save();
                bulkLoadStatistics.setCommitRecCount(0);
                prevRequestDocs = null;
                sessionSaveTimer.stop();
            }

            try {
                ingestTimer.stop();
            } catch (Exception e2) {
                logger.error(e2.getMessage() , e2 );
            }
            // Documents processed can be different from records processed as in the case of Instance data.
            logger.debug("Documents processed:" + recCount);
            bulkLoadStatistics.setFileRecCount(bulkLoadStatistics.getFileRecCount() + recCount);
            logger.info("Bulk ingest: Records processed in the current file :" + bulkLoadStatistics.getFileRecCount());
        } catch (Exception e) {
            bulkLoadStatistics.setCommitRecCount(0);
            try {
                ingestTimer.resume();
            } catch (Exception e2) {
                logger.error(e2.getMessage() , e2 );
            }
            //documentIngester.rollbackDocStoreIngestedData(session, request.getRequestDocuments());
            documentIngester.rollbackDocStoreIngestedData(session, prevRequestDocs);
            ingestTimer.stop();
            try {
                indexTimer.resume();
            } catch (Exception e2) {
                logger.error(e2.getMessage() , e2 );
            }
            //documentIndexer.rollbackIndexedData(request.getRequestDocuments());
            //prevRequestDocs = prevRequestDocs.subList(0, prevRequestDocs.size() - request.getRequestDocuments().size());
            //logger.info("prevRequestDocs before remove INDEXES = " + prevRequestDocs.size());
            documentIndexer.rollbackIndexedData(prevRequestDocs);
            prevRequestDocs = null;
            try {
                indexTimer.stop();
            } catch (Exception e2) {
                logger.error(e2.getMessage() , e2 );
            }
            logger.error("Document Ingest & Index Failed, Cause: " + e.getMessage(), e);
            try {
                totalTimer.stop();
            } catch (Exception e2) {
                logger.error(e2.getMessage() , e2 );
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
            logger.error(exe.getMessage() , exe );
        }
        logger.debug(
                "Time Consumptions...:\tcreatingNodes(" + docUUIDs.size() + "):" + createNodesTimer + "\tSessionSave("
                        + docUUIDs.size() + "):" + sessionSaveTimer + "\tIngest(" + docUUIDs.size() + "):" + ingestTimer
                        + "\tIndexing(" + docUUIDs.size() + "):" + indexTimer + "\tTotal Time: " + totalTimer);
        logger.debug("bulkIngestNIndex(" + request.getRequestDocuments().size() + ") END");
        batchStatistics.setTimeToCreateNodesInJcr(createNodesTimer.getTime());
        batchStatistics.setTimeToSaveJcrSession(sessionSaveTimer.getTime());
        batchStatistics.setIngestingTime(ingestTimer.getTime());
        batchStatistics.setIndexingTime(indexTimer.getTime());
        batchStatistics.setIngestNIndexTotalTime(totalTimer.getTime());
        updateProcessTimer(docUUIDs.size(), ingestTimer, indexTimer, totalTimer);
        solrOptimizeTimer.start();
        optimizeSolr(docUUIDs.size());
        solrOptimizeTimer.stop();
        batchStatistics.setTimeToSolrOptimize(solrOptimizeTimer.getTime());
        return docUUIDs;
    }

    private void updateProcessTimer(int recordsProcessed, StopWatch ingest, StopWatch index, StopWatch total) {
        BulkIngestTimeManager timer = ProcessParameters.BULK_PROCESSOR_TIME_MANAGER;
        synchronized (timer) {
            timer.setRecordsCount(timer.getRecordsCount() + recordsProcessed);
            timer.setIngestingTimer(timer.getIngestingTimer() + ingest.getTime());
            timer.setIndexingTimer(timer.getIndexingTimer() + index.getTime());
            timer.setProcessTimer(timer.getProcessTimer() + total.getTime());
            if (timer.getRecordsCount() >= ProcessParameters.BULK_PROCESSOR_TIMER_DISPLAY) {
                logger.debug(
                        "----------------------------------------------------------------------------------------------------------------------");
                logger.debug(timer.toString());
                logger.debug(
                        "----------------------------------------------------------------------------------------------------------------------");
                timer.reset();
            }
        }
    }

    private void optimizeSolr(long recordsProcessed) {
        docCount += recordsProcessed;
        logger.debug("BULK_INGEST_OPTIMIZE_SIZE=" + ProcessParameters.BULK_INGEST_OPTIMIZE_SIZE
                + ". Records processed till now=" + docCount);
        logger.info("Bulk ingest: Records processed in the bulk ingest " + docCount);
        if (docCount >= ProcessParameters.BULK_INGEST_OPTIMIZE_SIZE) {
            docCount = 0;
            try {
                logger.debug("Solr Optimization: START");
                documentIndexer.optimizeSolr(false, false);
                logger.debug("Solr Optimization: END");
            } catch (Exception e) {
                logger.warn("Solr Optimization Failed: ", e);
            }
        }
    }

    public Response buildResponse(Request request) {
        Response docStoreResponse = new Response();
        docStoreResponse.setUser(request.getUser());
        docStoreResponse.setOperation(request.getOperation());
        docStoreResponse.setMessage("Documents ingested");
        docStoreResponse.setStatus("Success");
        docStoreResponse.setStatusMessage("Documents Ingested Successfully");
        List<ResponseDocument> responseDocuments = new ArrayList<ResponseDocument>();
        ResponseDocument linkedDocument = null;
        ResponseDocument responseDocument = null;
        ResponseDocument linkedInstanceDocument = null;
        ResponseDocument linkedInstanceItemDocument = null;
        ResponseDocument linkedInstanceSrHoldingDoc = null;
        // documents
        for (Iterator<RequestDocument> iterator = request.getRequestDocuments().iterator(); iterator.hasNext(); ) {
            RequestDocument docStoreDocument = iterator.next();
            docStoreDocument.getContent().setContent("");
            responseDocument = new ResponseDocument();
            setResponseParameters(responseDocument, docStoreDocument);
            responseDocuments.add(responseDocument);
            if (docStoreDocument.getLinkedRequestDocuments() != null
                    && docStoreDocument.getLinkedRequestDocuments().size() > 0 && request != null
                    && request.getOperation() != null && !request.getOperation().equalsIgnoreCase("checkIn")) {
                List<ResponseDocument> linkResponseDos = new ArrayList<ResponseDocument>();
                // linked instance documents
                for (Iterator<RequestDocument> linkIterator = docStoreDocument.getLinkedRequestDocuments()
                        .iterator(); linkIterator.hasNext(); ) {
                    RequestDocument linkedRequestDocument = linkIterator.next();
                    linkedRequestDocument.getContent().setContent("");
                    linkedDocument = new ResponseDocument();
                    setResponseParameters(linkedDocument, linkedRequestDocument);
                    linkResponseDos.add(linkedDocument);
                    List<ResponseDocument> linkInstanceDocs = new ArrayList<ResponseDocument>();
                    InstanceCollection instanceCollection = (InstanceCollection) linkedRequestDocument.getContent()
                            .getContentObject();
                    for (Instance oleInstance : instanceCollection.getInstance()) {
                        // holding from instance
                        linkedInstanceDocument = new ResponseDocument();
                        setResponseParameters(linkedInstanceDocument, linkedRequestDocument);
                        linkedInstanceDocument.setUuid(oleInstance.getOleHoldings().getHoldingsIdentifier());
                        linkedInstanceDocument.setType("holdings");
                        linkInstanceDocs.add(linkedInstanceDocument);

                        //SourceHolding from Instance
                        linkedInstanceSrHoldingDoc = new ResponseDocument();
                        setResponseParameters(linkedInstanceSrHoldingDoc, linkedRequestDocument);
                        if (oleInstance.getSourceHoldings() != null &&
                                oleInstance.getSourceHoldings().getHoldingsIdentifier() != null) {
                            linkedInstanceSrHoldingDoc.setUuid(oleInstance.getSourceHoldings().getHoldingsIdentifier());
                            linkedInstanceSrHoldingDoc.setType("sourceHoldings");
                            linkInstanceDocs.add(linkedInstanceSrHoldingDoc);
                        }


                        // item from instance
                        for (Iterator<Item> itemIterator = oleInstance.getItems().getItem().iterator(); itemIterator
                                .hasNext(); ) {
                            Item oleItem = itemIterator.next();
                            linkedInstanceItemDocument = new ResponseDocument();
                            setResponseParameters(linkedInstanceItemDocument, linkedRequestDocument);
                            linkedInstanceItemDocument.setUuid(oleItem.getItemIdentifier());
                            linkedInstanceItemDocument.setType("item");
                            linkInstanceDocs.add(linkedInstanceItemDocument);
                        }
                    }
                    responseDocument.setLinkedInstanceDocuments(linkInstanceDocs);
                }
                responseDocument.setLinkedDocuments(linkResponseDos);
            }
        }
        docStoreResponse.setDocuments(responseDocuments);
        return docStoreResponse;
    }

    private void setResponseParameters(ResponseDocument responseDocument, RequestDocument docStoreDocument) {
        responseDocument.setId(docStoreDocument.getId());
        responseDocument.setCategory(docStoreDocument.getCategory());
        responseDocument.setType(docStoreDocument.getType());
        responseDocument.setFormat(docStoreDocument.getFormat());
        responseDocument.setContent(docStoreDocument.getContent());
        responseDocument.setUuid(docStoreDocument.getUuid());
    }

    public void setRepositoryManager(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    public DocumentIngester getDocumentIngester() {
        return documentIngester;
    }
}
