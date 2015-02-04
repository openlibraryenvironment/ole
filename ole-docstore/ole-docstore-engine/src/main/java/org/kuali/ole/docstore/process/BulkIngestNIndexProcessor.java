package org.kuali.ole.docstore.process;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.jcr.Session;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.time.StopWatch;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xstream.ingest.IngestDocumentHandler;
import org.kuali.ole.docstore.process.batch.BulkProcessRequest;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.IngestNIndexHandlerService;
import org.kuali.ole.docstore.utility.BatchIngestStatistics;
import org.kuali.ole.docstore.utility.BulkIngestStatistics;
import org.kuali.ole.docstore.utility.FileIngestStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class to Process IngestDocuments of Bulk.
 *
 * @author Rajesh Chowdary K
 * @created Mar 15, 2012
 */
public class BulkIngestNIndexProcessor
        implements Processor {

    private static Logger logger = LoggerFactory.getLogger(BulkIngestNIndexProcessor.class);
    private String user;
    private String action;
    private Session session = null;
    private BatchIngestStatistics batchStatistics = null;
    /**
     * Singleton instance of IngestNIndexHandlerService.
     */
    private IngestNIndexHandlerService ingestNIndexHandlerService = BeanLocator.getIngestNIndexHandlerService();
    private BulkIngestStatistics bulkLoadStatistics = BulkIngestStatistics.getInstance();
    //public  FileIngestStatistics       fileIngestStatistics       = null;
    private BulkProcessRequest bulkProcessRequest = null;

    public BulkIngestNIndexProcessor(String user, String action) {
        this.user = user;
        this.action = action;
    }

    @Override
    /*public void process(Exchange exchange) throws Exception {
        IngestDocumentHandler ingestDocumentHandler = new IngestDocumentHandler();
        String fileName = "";
        String filePath = "";
        BulkIngestStatistics bulkLoadStatistics = BulkIngestStatistics.getInstance();
        FileIngestStatistics fileIngestStatistics = bulkLoadStatistics.getFileIngestStatistics();
        batchStatistics = fileIngestStatistics.startBatch();
        filePath = exchange.getProperty("CamelFileExchangeFile").toString();
        fileName = filePath.substring(filePath.lastIndexOf(File.separator), filePath.length());
        fileName = fileName.replace(File.separator, "");
        fileName = fileName.replace("]", "");

        if (bulkLoadStatistics.isFirstBatch()) {
            fileIngestStatistics.setFileName(fileName);
            fileIngestStatistics.setFileStatus("Started");
            bulkLoadStatistics.setFirstBatch(false);
        }
        StopWatch batchIngestNIndexTimer = new StopWatch();
        StopWatch convertInputToReqTimer = new StopWatch();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        Date date = new Date();
        batchStatistics.setBatchStartTime(dateFormat.format(date));
        batchIngestNIndexTimer.start();
        logger.info("Bulk ingest: Batch Start time : \t" + dateFormat.format(date));
        long commitSize = ProcessParameters.BULK_INGEST_COMMIT_SIZE;
        try {
            ArrayList<RequestDocument> ingestDocs = new ArrayList<RequestDocument>();
            convertInputToReqTimer.start();
            if (exchange.getIn().getBody() instanceof List) {
                for (String ingestDocXml : (List<String>) exchange.getIn().getBody()) {
                    ingestDocs.add(ingestDocumentHandler.toObject(ingestDocXml));
                }
            }
            Request request = new Request();
            request.setUser(user);
            request.setOperation(action);
            request.setRequestDocuments(ingestDocs);
            convertInputToReqTimer.stop();
            logger.debug("converting input xml to request object " + convertInputToReqTimer);
            if (session == null) {
                session = RepositoryManager.getRepositoryManager()
                                           .getSession(request.getUser(), request.getOperation());
            }
            List<String> ids = ingestNIndexHandlerService.bulkIngestNIndex(request, session);
            logger.debug("Bulk Ingest Batch(UUIDs):" + ids);
            batchIngestNIndexTimer.stop();
            logger.debug("Bulk Ingest and Index Process Batch took " + batchIngestNIndexTimer + " time");
            batchStatistics.setTimeToConvertStringToReqObj(convertInputToReqTimer.getTime());
            batchStatistics.setBatchTime(batchIngestNIndexTimer.getTime());
            date = new Date();
            batchStatistics.setBatchEndTime(dateFormat.format(date));
            logger.info("Bulk ingest: Batch metrics : " + "\n" + batchStatistics.toString());
            if (bulkLoadStatistics.isLastBatch()) {
                fileIngestStatistics.setFileStatus("Done");
                bulkLoadStatistics.setLastBatch(false);
                logger.info("Bulk ingest: File metrics :  \n" + bulkLoadStatistics.toString());
                bulkLoadStatistics.setFileRecCount(0);
            }
            logger.info("Bulk ingest: Batch End time : \t" + dateFormat.format(date));
        }
        catch (Exception e) {
            logger.error("Bulk Processor Failed @ Batch : " + exchange.getIn(), e);
            exchange.setException(e);
            throw e;
        }
        finally {
            if (session != null) {
                try {
                    if (bulkLoadStatistics.isLastBatch()) {
                        RepositoryManager.getRepositoryManager().logout(session);
                        session = null;
                    }
                }
                catch (OleException e) {
                }
            }

        }
    }*/

    public void process(Exchange exchange) throws Exception {
        IngestDocumentHandler ingestDocumentHandler = new IngestDocumentHandler();
        String fileName = "";
        String filePath = "";
        BulkIngestStatistics bulkLoadStatistics = bulkProcessRequest.getBulkIngestStatistics();
        FileIngestStatistics fileIngestStatistics = bulkLoadStatistics.getFileIngestStatistics();
        batchStatistics = fileIngestStatistics.startBatch();
        filePath = exchange.getProperty("CamelFileExchangeFile").toString();
        fileName = filePath.substring(filePath.lastIndexOf(File.separator), filePath.length());
        fileName = fileName.replace(File.separator, "");
        fileName = fileName.replace("]", "");

        // record file level statistics
        if (bulkProcessRequest.getBulkIngestStatistics().isFirstBatch()) {
            fileIngestStatistics.setFileName(fileName);
            fileIngestStatistics.setFileStatus("Started");
            bulkProcessRequest.getBulkIngestStatistics().setFirstBatch(false);
        }
        StopWatch batchIngestNIndexTimer = new StopWatch();
        StopWatch convertInputToReqTimer = new StopWatch();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        Date date = new Date();
        batchStatistics.setBatchStartTime(dateFormat.format(date));
        batchIngestNIndexTimer.start();
        logger.info("Bulk ingest: Batch Start time : \t" + dateFormat.format(date));
        long commitSize = ProcessParameters.BULK_INGEST_COMMIT_SIZE;
        try {
            ArrayList<RequestDocument> ingestDocs = new ArrayList<RequestDocument>();
            convertInputToReqTimer.start();
            // build input request documents for the current batch.
            if (exchange.getIn().getBody() instanceof List) {
                for (String ingestDocXml : (List<String>) exchange.getIn().getBody()) {
                    ingestDocs.add(ingestDocumentHandler.toObject(ingestDocXml));
                }
            }
            convertInputToReqTimer.stop();
            // call the bulk ingest service
            BeanLocator.getDocumentServiceImpl().bulkIngest(bulkProcessRequest, ingestDocs);
            // get the document manager for the current batch of documents (belong to same cat-type-format)
            //            DocumentManager documentManager = DocumentManagerFactory.getInstance()
            //                                                                    .getDocumentManager(ingestDocs.get(0));
            //            documentManager.bulkIngest(bulkProcessRequest, ingestDocs);
            // record file/batch level statistics/metrics
            batchIngestNIndexTimer.stop();
            batchStatistics.setTimeToConvertStringToReqObj(convertInputToReqTimer.getTime());
            batchStatistics.setBatchTime(batchIngestNIndexTimer.getTime());
            date = new Date();
            batchStatistics.setBatchEndTime(dateFormat.format(date));
            logger.info("Bulk ingest: Batch metrics : " + "\n" + batchStatistics.toString());
            if (bulkLoadStatistics.isLastBatch()) {
                fileIngestStatistics.setFileStatus("Done");
                bulkLoadStatistics.setLastBatch(false);
                logger.info("Bulk ingest: File metrics :  \n" + bulkLoadStatistics.toString());
                bulkLoadStatistics.setFileRecCount(0);
            }
            logger.info("Bulk ingest: Batch End time : \t" + dateFormat.format(date));
        } catch (Exception e) {
            logger.error("Bulk Processor Failed @ Batch : " + exchange.getIn(), e);
            exchange.setException(e);
            throw e;
        } finally {

        }
    }

    public BulkIngestStatistics getBulkLoadStatistics() {
        return bulkLoadStatistics;
    }

    public void setBulkLoadStatistics(BulkIngestStatistics bulkLoadStatistics) {
        this.bulkLoadStatistics = bulkLoadStatistics;
    }

    public BulkProcessRequest getBulkProcessRequest() {
        return bulkProcessRequest;
    }

    public void setBulkProcessRequest(BulkProcessRequest bulkProcessRequest) {
        this.bulkProcessRequest = bulkProcessRequest;
    }
}
