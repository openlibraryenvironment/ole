package org.kuali.ole.docstore.process;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.time.StopWatch;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.RepositoryBrowser;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibMarc;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.BibTrees;
import org.kuali.ole.docstore.common.document.content.instance.FormerIdentifier;
import org.kuali.ole.docstore.common.document.content.instance.Instance;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.common.util.BatchBibTreeDBUtil;
import org.kuali.ole.docstore.common.util.BibInfoStatistics;
import org.kuali.ole.docstore.common.util.ReindexBatchStatistics;
import org.kuali.ole.docstore.discovery.service.SolrServerManager;
import org.kuali.ole.docstore.discovery.solr.work.bib.marc.WorkBibMarcDocBuilder;
import org.kuali.ole.docstore.document.rdbms.RdbmsWorkEInstanceDocumentManager;
import org.kuali.ole.docstore.document.rdbms.RdbmsWorkInstanceDocumentManager;
import org.kuali.ole.docstore.engine.service.index.solr.BibMarcIndexer;
import org.kuali.ole.docstore.engine.service.index.solr.DocumentIndexer;
import org.kuali.ole.docstore.engine.service.storage.rdbms.dao.RebuildIndexDao;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.EInstanceRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.InstanceRecord;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.metrics.reindex.ReIndexingBatchStatus;
import org.kuali.ole.docstore.metrics.reindex.ReIndexingStatus;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.model.xstream.work.oleml.WorkEInstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.DocumentIngester;
import org.kuali.ole.docstore.service.ServiceLocator;
import org.kuali.ole.pojo.OleException;
import org.kuali.ole.repository.CheckoutManager;
import org.kuali.ole.repository.NodeHandler;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class to Rebuild Indexes.
 *
 * @author Rajesh Chowdary K
 * @created May 2, 2012
 */
public class RebuildIndexesHandler
        implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(RebuildIndexesHandler.class);
    private static RebuildIndexesHandler reBuilder = null;
    private boolean isRunning = false;
    private boolean isStop = false;
    private static final Logger logger = LoggerFactory.getLogger(RebuildIndexesHandler.class);
    private String docCategory;
    private String docType;
    private String docFormat;
    private BibInfoStatistics bibInfoStatistics = null;
    private CheckoutManager checkoutManager;
    //    private ReIndexingStatus reIndexingStatus;
    private int batchSize;
    private int startIndex;
    private int endIndex;
    private String updateDate;

    public static String EXCEPION_FILE_NAME = "";
    public static String STATUS_FILE_NAME = "";
    public static String STORAGE_EXCEPTION_FILE_NAME = "";
    public static String STORAGE_STATUS_FILE_NAME = "";
    public static BatchBibTreeDBUtil bibTreeDBUtil = new BatchBibTreeDBUtil();

    private String filePath =  System.getProperty("solr.solr.home");
    public synchronized void setRunning(boolean running) {
        isRunning = running;
    }

    public synchronized void setStop(boolean stop) {
        isStop = stop;
    }

    private RebuildIndexesHandler() {
        checkoutManager = new CheckoutManager();
    }

    public static RebuildIndexesHandler getInstance() {
        if (reBuilder == null) {
            reBuilder = new RebuildIndexesHandler();
        }
        return reBuilder;
    }

    /**
     * Method to get running status.
     *
     * @return
     */
    public synchronized boolean isRunning() {
        return isRunning;
    }

    public synchronized boolean isStop() {
        return isStop;
    }

    /**
     * Method to startProcess
     */
    public String startProcess(String docCategory, String docType, String docFormat) throws InterruptedException {
        String status = null;
        if (isRunning()) {
            status = "ReIndexing process is already running. Click 'Show Status' button to know the status. ";
        } else {
            setRunning(true);
            setStop(false);
            status = "ReIndexing process has started. Click 'Show Status' button to know the status. ";
            ReIndexingStatus reIndexingStatus = ReIndexingStatus.getInstance();
            reIndexingStatus.reset();
            if (docCategory == null || docCategory.equals("")) {
                docCategory = "all";
            }
            if (docType == null || docType.equals("")) {
                docType = "all";
            }
            if (docFormat == null || docType.equals("")) {
                docFormat = "all";
            }
            this.docCategory = docCategory;
            this.docType = docType;
            this.docFormat = docFormat;
            Thread reBuilderThread = new Thread(this);
            reBuilderThread.start();
            //            reBuilderThread.join();
            setRunning(false);
        }
        return status;
    }
    public String startProcess(String docCategory, String docType, String docFormat, int batchSize, int startIndex, int endIndex,String updateDate) throws InterruptedException {
        String status = null;
        if (isRunning()) {
            status = "ReIndexing process is already running. Click 'Show Status' button to know the status. ";
        } else {
            setRunning(true);
            setStop(false);
            status = "ReIndexing process has started. Click 'Show Status' button to know the status. ";
            ReIndexingStatus reIndexingStatus = ReIndexingStatus.getInstance();
            reIndexingStatus.reset();
            if (docCategory == null || docCategory.equals("")) {
                docCategory = "all";
            }
            if (docType == null || docType.equals("")) {
                docType = "all";
            }
            if (docFormat == null || docType.equals("")) {
                docFormat = "all";
            }
            this.docCategory = docCategory;
            this.docType = docType;
            this.docFormat = docFormat;
            this.batchSize = batchSize;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.updateDate=updateDate;
            Thread reBuilderThread = new Thread(this);
            reBuilderThread.start();
            //            reBuilderThread.join();
            setRunning(false);
        }
        return status;
    }

    public String stopProcess() throws Exception {
        String status = null;
        if (isRunning()) {
            status = "ReIndexing process is running. ReIndexing will stop after current batch. ";
            setStop(true);
            setRunning(false);
        } else {
            status = "ReIndexing process is not running.";
        }
        return status;

    }

    public void run() {
        DocCategoryTypeFormat docCategoryTypeFormat = new DocCategoryTypeFormat();
        List<String> categoryList = docCategoryTypeFormat.getCategories();
        List<String> typeList = null;
        List<String> formatList = null;
        for (String docCategoryCurr : categoryList) {
            if (docCategory.equals("all") || docCategory.equals(docCategoryCurr)) {
                typeList = docCategoryTypeFormat.getDocTypes(docCategoryCurr);
                for (String docTypeCurr : typeList) {
                    if (docType.equals("all") || docType.equals(docTypeCurr)) {
                        formatList = docCategoryTypeFormat.getDocFormats(docCategoryCurr, docTypeCurr);
                        for (String docFormatCurr : formatList) {
                            if (docFormat.equals("all") || docFormat.equals(docFormatCurr)) {
                                if (!isStop()) {
                                    ReIndexingStatus.getInstance()
                                            .startDocType(docCategoryCurr, docTypeCurr, docFormatCurr);
                                    reIndex(docCategoryCurr, docTypeCurr, docFormatCurr);
                                } else {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        setRunning(false);
    }

    private void reIndex(String docCategory, String docType, String docFormat) {
        Session session = null;
        setRunning(true);
        logger.info("Rebuild Indexes Run(" + docCategory + " : " + docType + " : " + docFormat + "): ");
        try {
            if (docCategory.equals(DocCategory.WORK.getCode())) {
                if (docType.equals(DocType.BIB.getDescription())) {
                    if (docFormat.equals(DocFormat.MARC.getCode()) || docFormat.equals(DocFormat.DUBLIN_CORE.getCode())
                            || docFormat.equals(DocFormat.DUBLIN_UNQUALIFIED.getCode())) {
                        org.springframework.util.StopWatch stopWatch = new org.springframework.util.StopWatch();
                        stopWatch.start("total time taken");
                        Date date = new Date();
                        EXCEPION_FILE_NAME = "ReindexErrors-" + date.toString() + ".txt";
                        STATUS_FILE_NAME = "ReindexBatchStatus-" + date.toString() + ".txt";
                        BatchBibTreeDBUtil.writeStatusToFile(filePath, RebuildIndexesHandler.EXCEPION_FILE_NAME, "Reindex started at:" + date);
                        BibHoldingItemReindexer bibHoldingItemReindexer = BibHoldingItemReindexer.getInstance();
                        bibHoldingItemReindexer.setTotalBatchStatistics(new ReindexBatchStatistics());
                        bibHoldingItemReindexer.index(batchSize, startIndex, endIndex,updateDate);
                        date = new Date();
                        BatchBibTreeDBUtil.writeStatusToFile(filePath, RebuildIndexesHandler.EXCEPION_FILE_NAME, "Reindex ended at:" + date);
                        stopWatch.stop();
                        logger.info(stopWatch.prettyPrint());
//                        workBibMarcAndDublinAll(docCategory, docType, docFormat);
                    } else {
                        logger.info(
                                "Rebuild Indexes Run(" + docCategory + " : " + docType + " : " + docFormat + "): FAIL");
                    }
                } else if (docType.equals(DocType.INSTANCE.getDescription())) {
                    if (docFormat.equals(DocFormat.OLEML.getCode())) {
                        workInstanceOLEML(docCategory, docType, docFormat);
                    } else {
                        logger.info(
                                "Rebuild Indexes Run(" + docCategory + " : " + docType + " : " + docFormat + "): FAIL");
                    }
                } else if (docType.equals(DocType.LICENSE.getDescription())) {
                    if (docFormat.equals(DocFormat.ONIXPL.getCode()) || docFormat.equals(DocFormat.PDF.getCode())
                            || docFormat.equals(DocFormat.DOC.getCode())) {
                        workLicense(docCategory, docType, docFormat);
                    } else {
                        logger.info(
                                "Rebuild Indexes Run(" + docCategory + " : " + docType + " : " + docFormat + "): FAIL");
                    }
                } else if (docType.equals(DocType.EINSTANCE.getCode())) {
                    if (docFormat.equals(DocFormat.OLEML.getCode())) {
                        workEInstanceOLEML(docCategory, docType, docFormat);
                    } else {
                        logger.info(
                                "Rebuild Indexes Run(" + docCategory + " : " + docType + " : " + docFormat + "): FAIL");
                    }
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
        } finally {
            try {
                if (isStop) {
                    ReIndexingStatus.getInstance().getDocTypeList().setStatus("Stopped");
                } else {
                    ReIndexingStatus.getInstance().getDocTypeList().setStatus("Done");
                }
                RepositoryManager.getRepositoryManager().logout(session);
            } catch (OleException e) {
                logger.error(e.getMessage(), e);
            }
        }

    }

    private void workEInstanceOLEML(String docCategory, String docType, String docFormat) {
        long totalCount = 0;
        long nodeCount = 0;
        List<RequestDocument> docs = new ArrayList<RequestDocument>();
        WorkEInstanceOlemlRecordProcessor workEInstanceOlemlRecordProcessor = new WorkEInstanceOlemlRecordProcessor();
        try {
            RequestDocument rd = new RequestDocument();
            rd.setCategory(docCategory);
            rd.setType(docType);
            rd.setFormat(docFormat);
            List<ReIndexingBatchStatus> batchStatusList = new ArrayList<ReIndexingBatchStatus>();
            BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
            List<EInstanceRecord> instanceRecords = (List<EInstanceRecord>) businessObjectService.findAll(EInstanceRecord.class);
            StopWatch loadTimer = new StopWatch();
            StopWatch batchTimer = new StopWatch();
            loadTimer.start();
            batchTimer.start();
            for (int i = 0; i < instanceRecords.size(); i++) {
                if (docs.size() == ProcessParameters.BULK_PROCESSOR_SPLIT_SIZE) {
                    if (!isStop()) {
                        ReIndexingBatchStatus reIndexingBatchStatus = indexBeforeParams(loadTimer);
                        indexDocs(docs, totalCount, nodeCount, batchStatusList, reIndexingBatchStatus);
                        indexAfterParams(batchTimer, reIndexingBatchStatus, batchStatusList);
                        resetTimers(batchTimer, loadTimer);
                        totalCount = 0;
                        logger.info("Rebuild");
                    } else {
                        return;
                    }
                } else {
                    EInstanceRecord instanceRecord = instanceRecords.get(i);
                    String uuid = DocumentUniqueIDPrefix.getPrefixedId(instanceRecord.getUniqueIdPrefix(), instanceRecord.geteInstanceIdentifier());
                    RequestDocument requestDocument = buildRequestDocumentForCheckout(docCategory, docType, docFormat, uuid);
                    ResponseDocument responseDocument = RdbmsWorkEInstanceDocumentManager.getInstance().checkoutContent(requestDocument, businessObjectService);
                    String content = responseDocument.getContent().getContent();
                    RequestDocument requestDocumentForIndex = (RequestDocument) rd.clone();
                    requestDocumentForIndex.setAdditionalAttributes(responseDocument.getAdditionalAttributes());
                    requestDocumentForIndex.setId(uuid);
                    requestDocumentForIndex.setUuid(uuid);
                    org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection instanceCollection = workEInstanceOlemlRecordProcessor.fromXML(content);
                    content = workEInstanceOlemlRecordProcessor.toXML(instanceCollection);
                    Content contentObj = new Content();
                    contentObj.setContent(content);
                    contentObj.setContentObject(instanceCollection);
                    requestDocumentForIndex.setContent(contentObj);
                    docs.add(requestDocumentForIndex);
                    totalCount++;
                }
            }
            if (docs.size() > 0 && !isStop()) {
                ReIndexingBatchStatus reIndexingBatchStatus = indexBeforeParams(loadTimer);
                indexDocs(docs, totalCount, nodeCount, batchStatusList, reIndexingBatchStatus);
                indexAfterParams(batchTimer, reIndexingBatchStatus, batchStatusList);
            }
        } catch (Exception e) {
            logger.error(
                    "Rebuild Indexes Process(" + docCategory + " : " + docType + " : " + docFormat + ") Processed(" + (
                            totalCount - docs.size()) + "), Failed @ batch(" + docs.size() + "): Cause: " + e, e);
        } finally {
            if (isStop) {
                ReIndexingStatus.getInstance().getDocTypeList().setStatus("Stopped");
            } else {
                ReIndexingStatus.getInstance().getDocTypeList().setStatus("Done");
            }
        }

    }


    private void indexBibDocs(List<BibTree> bibTreeList, long records, long recCount,
                              List<ReIndexingBatchStatus> batchStatusList, ReIndexingBatchStatus reIndexingBatchStatus) {
        StopWatch indexTimer = new StopWatch();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
        try {
            Date startDate = new Date();
            reIndexingBatchStatus.setBatchStartTime(dateFormat.format(startDate));
            indexTimer.start();
            reIndexingBatchStatus.setStatus("Indexing");
            reIndexingBatchStatus.setBatchIndexingTime(indexTimer.toString());
            reIndexingBatchStatus.setRecordsProcessed(records);
            reIndexingBatchStatus.setBatchEndTime(" ");
            batchStatusList.add(reIndexingBatchStatus);
            ReIndexingStatus.getInstance().getDocTypeList().setReIndBatStatusList(batchStatusList);
            DocumentIndexer documentIndexer = BibMarcIndexer.getInstance();
            BibTrees bibTrees = new BibTrees();
            bibTrees.getBibTrees().addAll(bibTreeList);
            documentIndexer.createTrees(bibTrees);
            //logger.debug(result);
            indexTimer.stop();
            Date endDate = new Date();
            reIndexingBatchStatus.setBatchEndTime(dateFormat.format(endDate));
            reIndexingBatchStatus.setBatchIndexingTime(indexTimer.toString());
            reIndexingBatchStatus.setRecordsProcessed(records);
            reIndexingBatchStatus.setStatus("Done");
            reIndexingBatchStatus.setRecordsRemaining(recCount - records);
            ReIndexingStatus.getInstance().getDocTypeList().setReIndBatStatusList(batchStatusList);
            bibTreeList.clear();
        } catch (Exception e) {
            String firstBibId = bibTreeList.get(0).getBib().getId();
            String lastBibId = bibTreeList.get(bibTreeList.size()-1).getBib().getId();
            logger.error(
                    "Rebuild Indexes Process(" + docCategory + " : " + docType + " : " + docFormat + ") Processed(" + (
                            records - bibTreeList.size()) + "), Failed @ bibId( First BibId: " + firstBibId + "   :  Last BibId : "+ lastBibId +"): Cause: " + e, e);
            indexTimer.stop();
            Date endDate = new Date();
            reIndexingBatchStatus.setBatchEndTime(dateFormat.format(endDate));
            reIndexingBatchStatus.setBatchIndexingTime(indexTimer.toString());
            reIndexingBatchStatus.setRecordsProcessed(0L);
            reIndexingBatchStatus.setStatus("Done");
            reIndexingBatchStatus.setRecordsRemaining(recCount - records);
            ReIndexingStatus.getInstance().getDocTypeList().setReIndBatStatusList(batchStatusList);
            bibTreeList.clear();
        }
    }


    private void indexDocs(List<RequestDocument> docs, long records, long recCount,
                           List<ReIndexingBatchStatus> batchStatusList, ReIndexingBatchStatus reIndexingBatchStatus) {
        try {
            StopWatch indexTimer = new StopWatch();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
            Date startDate = new Date();
            reIndexingBatchStatus.setBatchStartTime(dateFormat.format(startDate));
            indexTimer.start();
            reIndexingBatchStatus.setStatus("Indexing");
            reIndexingBatchStatus.setBatchIndexingTime(indexTimer.toString());
            reIndexingBatchStatus.setRecordsProcessed(records);
            reIndexingBatchStatus.setBatchEndTime(" ");
            batchStatusList.add(reIndexingBatchStatus);
            ReIndexingStatus.getInstance().getDocTypeList().setReIndBatStatusList(batchStatusList);
            IndexerService indexerService = BeanLocator.getDocstoreFactory().getDocumentIndexManager(docs.get(0).getCategory(), docs.get(0).getType(), docs.get(0).getFormat());
            String result = indexerService.indexDocuments(docs, false);
            logger.debug(result);
            indexTimer.stop();
            Date endDate = new Date();
            reIndexingBatchStatus.setBatchEndTime(dateFormat.format(endDate));
            reIndexingBatchStatus.setBatchIndexingTime(indexTimer.toString());
            reIndexingBatchStatus.setRecordsProcessed(records);
            reIndexingBatchStatus.setStatus("Done");
            reIndexingBatchStatus.setRecordsRemaining(recCount - records);
            ReIndexingStatus.getInstance().getDocTypeList().setReIndBatStatusList(batchStatusList);
            docs.clear();
        } catch (Exception e) {
            logger.error("Rebuild Indexes Processed(" + (records - docs.size()) + "), Failed @ batch(" + docs.size()
                    + "): Cause: " + e + "\n\tContinuous", e);
        }
    }

    private void workBibMarcAndDublinAll(String docCategory, String docType, String docFormat) throws SolrServerException, IOException {
        long totalCount = 0;
        long nodeCount = 0;
        int start = 0;
        String sqlQuery = null;
        long startTime = 0;
        long commitEndTime = 0;
        long commitStartTime = 0;
        int batchSize = 50000;  //ProcessParameters.BULK_PROCESSOR_SPLIT_SIZE;
        int commitSize = 50000;
        long endIndexBatch = 0;
        String dbVendor = ConfigContext.getCurrentContextConfig().getProperty("db.vendor");
        List<BibTree> bibTrees = new ArrayList<BibTree>();
        //Map<String, BibTree> bibMap = new HashMap<String, BibTree>();
        try {
            String prefix = DocumentUniqueIDPrefix.getPrefix(docCategory, docType, docFormat);
            Map prefixMap = new HashMap(0);
            prefixMap.put("uniqueIdPrefix", prefix);
            startTime = System.currentTimeMillis();   //t1

            BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
            int bibCount = businessObjectService.countMatching(BibRecord.class, prefixMap);
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            if (bibCount > 0) {
                connection = getConnection();
                if (dbVendor.equalsIgnoreCase("mysql")) {
                    sqlQuery = "select * from ole_ds_bib_t b ORDER BY b.bib_id LIMIT ?,?";
                } else {
                    sqlQuery = "select * from (select b.*,ROWNUM r from OLE_DS_BIB_T b) where  r between ? and ?";
                }
                preparedStatement = connection.prepareStatement(sqlQuery);
            }
            List<ReIndexingBatchStatus> batchStatusList = new ArrayList<ReIndexingBatchStatus>();
            StopWatch loadTimer = new StopWatch();
            StopWatch batchTimer = new StopWatch();
            loadTimer.start();
            batchTimer.start();

            for (int i = 0; i < bibCount; i++) {
                if (bibTrees.size() == batchSize) {
                    if (!isStop()) {

                        ReIndexingBatchStatus reIndexingBatchStatus = indexBeforeParams(loadTimer);
                        indexBibDocs(bibTrees, totalCount, nodeCount, batchStatusList, reIndexingBatchStatus);
                        indexAfterParams(batchTimer, reIndexingBatchStatus, batchStatusList);
                        resetTimers(batchTimer, loadTimer);
                        totalCount = 0;
                        logger.info("Rebuild");
                        i = start;
                        if (start % commitSize == 0) {
                            commitStartTime = System.currentTimeMillis(); //t2
                            logger.info("Time elapsed since start ====>>>>>>  " + (commitStartTime - startTime)); // t2-t1
                            logger.info("Time elapsed since last commit ====>>>>>>  " + (commitStartTime - commitEndTime));   //t2-t3
                            logger.info("commit started ====>>>>>>  " + commitStartTime);
                            SolrServer solr = SolrServerManager.getInstance().getSolrServer();
                            solr.commit();
                            logger.info("No..of records committed ====>>>>>>  " + start);
                            commitEndTime = System.currentTimeMillis(); //t3
                            logger.info("Time Taken for commit ====>>>>>>  " + (commitEndTime - commitStartTime));  //t3-t2

                        }
                    } else {
                        return;
                    }
                } else {
                    if (start < bibCount) {
                        long b2time = System.currentTimeMillis();
                        if (dbVendor.equalsIgnoreCase("mysql")) {
                            preparedStatement.setInt(1, start);
                            preparedStatement.setInt(2, batchSize);
                        } else {
                            preparedStatement.setInt(1, start + 1);
                            preparedStatement.setInt(2, start + batchSize);
                        }
                        ResultSet resultSet = preparedStatement.executeQuery();
                        logger.info("time taking for getting records from DB end======>>>>>" + (System.currentTimeMillis() - b2time));
                        while (resultSet.next()) {

                                BibTree bibTree = new BibTree();
                                Bib bib = new BibMarc();
                                bib.setCategory(docCategory);
                                bib.setType(docType);
                                bib.setFormat(docFormat);
                                bib.setCreatedBy(resultSet.getString("CREATED_BY"));
                                bib.setCreatedOn(resultSet.getString("DATE_CREATED"));
                                bib.setStaffOnly((resultSet.getString("STAFF_ONLY").equalsIgnoreCase("Y") ? Boolean.TRUE : Boolean.FALSE));
                                bib.setContent(resultSet.getString("CONTENT"));
                                bib.setUpdatedBy(resultSet.getString("UPDATED_BY"));
                                bib.setUpdatedOn(resultSet.getString("DATE_UPDATED"));
                                bib.setLastUpdated(resultSet.getString("DATE_UPDATED"));
                                bib.setStatus(resultSet.getString("STATUS"));
                                bib.setStatusUpdatedBy(resultSet.getString("STATUS_UPDATED_BY"));
                                bib.setStatusUpdatedOn(resultSet.getString("STATUS_UPDATED_DATE"));
                                String uuid = DocumentUniqueIDPrefix.getPrefixedId(resultSet.getString("UNIQUE_ID_PREFIX"), resultSet.getString(1));
                                bib.setId(uuid);
                                bib.setLocalId(uuid);
                                bibTree.setBib(bib);

                                start++;
                                totalCount++;
                                bibTrees.add(bibTree);
                        }
                        resultSet.close();
                    }
                }

            }
            if (bibTrees.size() > 0 && !isStop()) {
                ReIndexingBatchStatus reIndexingBatchStatus = indexBeforeParams(loadTimer);
                indexBibDocs(bibTrees, totalCount, nodeCount, batchStatusList, reIndexingBatchStatus);
                indexAfterParams(batchTimer, reIndexingBatchStatus, batchStatusList);
                commitStartTime = System.currentTimeMillis();
                logger.info("commit started : ---->  " + commitStartTime);
                SolrServer solr = SolrServerManager.getInstance().getSolrServer();
                solr.commit();
                logger.info("No..of records committed : ---->  " + start);
                commitEndTime = System.currentTimeMillis();
                logger.info("Time Taken for commit ======>>>  " + (commitEndTime - commitStartTime));

            }
            endIndexBatch = System.currentTimeMillis();   //t1
            logger.info("Time elapsed since end ====>>>>>>  " + endIndexBatch);
        } catch (Exception e) {
            logger.error(
                    "Rebuild Indexes Process(" + docCategory + " : " + docType + " : " + docFormat + ") Processed(" + (
                            totalCount - bibTrees.size()) + "), Failed @ batch(" + bibTrees.size() + "): Cause: " + e, e);
        } finally {
            if (isStop) {
                ReIndexingStatus.getInstance().getDocTypeList().setStatus("Stopped");
            } else {
                ReIndexingStatus.getInstance().getDocTypeList().setStatus("Done");
            }
        }
    }

    private void workInstanceOLEML(String docCategory, String docType, String docFormat) {
        long totalCount = 0;
        long nodeCount = 0;
        List<RequestDocument> docs = new ArrayList<RequestDocument>();
        InstanceOlemlRecordProcessor instanceOlemlRecordProcessor = new InstanceOlemlRecordProcessor();
        try {
            RequestDocument rd = new RequestDocument();
            rd.setCategory(docCategory);
            rd.setType(docType);
            rd.setFormat(docFormat);
            List<ReIndexingBatchStatus> batchStatusList = new ArrayList<ReIndexingBatchStatus>();
            BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
            List<InstanceRecord> instanceRecords = (List<InstanceRecord>) businessObjectService.findAll(InstanceRecord.class);
            StopWatch loadTimer = new StopWatch();
            StopWatch batchTimer = new StopWatch();
            loadTimer.start();
            batchTimer.start();
            for (int i = 0; i < instanceRecords.size(); i++) {
                if (docs.size() == ProcessParameters.BULK_PROCESSOR_SPLIT_SIZE) {
                    if (!isStop()) {
                        ReIndexingBatchStatus reIndexingBatchStatus = indexBeforeParams(loadTimer);
                        indexDocs(docs, totalCount, nodeCount, batchStatusList, reIndexingBatchStatus);
                        indexAfterParams(batchTimer, reIndexingBatchStatus, batchStatusList);
                        resetTimers(batchTimer, loadTimer);
                        totalCount = 0;
                        logger.info("Rebuild");
                    } else {
                        return;
                    }
                } else {
                    InstanceRecord instanceRecord = instanceRecords.get(i);
                    String uuid = DocumentUniqueIDPrefix.getPrefixedId(instanceRecord.getUniqueIdPrefix(), instanceRecord.getInstanceId());
                    RequestDocument requestDocument = buildRequestDocumentForCheckout(docCategory, docType, docFormat, uuid);
                    ResponseDocument responseDocument = RdbmsWorkInstanceDocumentManager.getInstance().checkoutContent(requestDocument, businessObjectService);
                    String content = responseDocument.getContent().getContent();
                    RequestDocument requestDocumentForIndex = (RequestDocument) rd.clone();
                    requestDocumentForIndex.setAdditionalAttributes(responseDocument.getAdditionalAttributes());
                    requestDocumentForIndex.setId(uuid);
                    requestDocumentForIndex.setUuid(uuid);
                    InstanceCollection instanceCollection = instanceOlemlRecordProcessor.fromXML(content);
//                    logger.debug("REBUILD_INDEXING_LINKING " + ProcessParameters.REBUILD_INDEXING_LINKING);
//                    if (!ProcessParameters.REBUILD_INDEXING_LINKING) {
//                        instanceCollection.getInstance().get(0).getResourceIdentifier().clear();
//                    }
                    content = instanceOlemlRecordProcessor.toXML(instanceCollection);
                    Content contentObj = new Content();
                    contentObj.setContent(content);
                    contentObj.setContentObject(instanceCollection);
                    requestDocumentForIndex.setContent(contentObj);
                    docs.add(requestDocumentForIndex);
                    totalCount++;
                }
            }
            if (docs.size() > 0 && !isStop()) {
                ReIndexingBatchStatus reIndexingBatchStatus = indexBeforeParams(loadTimer);
                indexDocs(docs, totalCount, nodeCount, batchStatusList, reIndexingBatchStatus);
                indexAfterParams(batchTimer, reIndexingBatchStatus, batchStatusList);
            }
        } catch (Exception e) {
            logger.error(
                    "Rebuild Indexes Process(" + docCategory + " : " + docType + " : " + docFormat + ") Processed(" + (
                            totalCount - docs.size()) + "), Failed @ batch(" + docs.size() + "): Cause: " + e, e);
        } finally {
            if (isStop) {
                ReIndexingStatus.getInstance().getDocTypeList().setStatus("Stopped");
            } else {
                ReIndexingStatus.getInstance().getDocTypeList().setStatus("Done");
            }
        }
    }

    private RequestDocument buildRequestDocumentForCheckout(String docCategory, String docType, String docFormat, String uuid) {
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setCategory(docCategory);
        requestDocument.setType(docType);
        requestDocument.setFormat(docFormat);
        requestDocument.setUuid(uuid);
        return requestDocument;
    }

    private void linkingInstanceWithBib(InstanceCollection instanceCollection, Session session, Node fileNode) {
        for (Instance instance : instanceCollection.getInstance()) {
            instance.getResourceIdentifier().clear();
            for (FormerIdentifier frids : instance.getFormerResourceIdentifier()) {
                try {
                    if (frids != null && frids.getIdentifier() != null &&
                            frids.getIdentifier().getIdentifierValue() != null &&
                            frids.getIdentifier().getIdentifierValue().trim().length() >= 0) {
                        List<SolrDocument> solrBibDocs = ServiceLocator.getIndexerService()
                                .getSolrDocument("SystemControlNumber",
                                        frids.getIdentifier()
                                                .getIdentifierValue());
                        SolrInputDocument solrInputDocument = new SolrInputDocument();
                        WorkBibMarcDocBuilder marcDocBuilder = new WorkBibMarcDocBuilder();
                        List<SolrInputDocument> solrInputDocs = new ArrayList<SolrInputDocument>();
                        if (solrBibDocs != null && solrBibDocs.size() > 0) {
                            for (SolrDocument solrbibDoc : solrBibDocs) {
                                if (checkApplicability(frids.getIdentifier().getIdentifierValue(),
                                        solrbibDoc.getFieldValue("SystemControlNumber"))) {

                                    compareObjNAddValue(instance.getInstanceIdentifier(),
                                            solrbibDoc.getFieldValue("instanceIdentifier"), solrbibDoc,
                                            "instanceIdentifier");
                                    solrInputDocument = new SolrInputDocument();
                                    marcDocBuilder.buildSolrInputDocFromSolrDoc(solrbibDoc, solrInputDocument);
                                    solrInputDocs.add(solrInputDocument);
                                    String bibId = compareListRString(solrbibDoc.getFieldValue("id"));
                                    instance.getResourceIdentifier().add(bibId);
                                    modifyContentAddLinkedIdsInDocStore(instance, bibId, session, fileNode);
                                    indexSolrDocs(solrInputDocs);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("error message" + e.getMessage(), e);
                }
            }
        }
    }

    private void modifyContentAddLinkedIdsInDocStore(Instance instance, String id, Session session, Node fileNode) {

        try {
            Node bibNode = getNodeByUUID(session, id);
            bibNode.setProperty("instanceIdentifier", instance.getInstanceIdentifier());
            fileNode.setProperty("bibIdentifier", id);

            InstanceOlemlRecordProcessor recordProcessor = new InstanceOlemlRecordProcessor();
            NodeIterator nodeIterator = fileNode.getNodes();
            while (nodeIterator.hasNext()) {
                Node instNode = nodeIterator.nextNode();
                if (instNode.getName().equalsIgnoreCase("instanceFile")) {
                    InstanceCollection instCol = new InstanceCollection();
                    Instance inst = new Instance();
                    inst.setResourceIdentifier(instance.getResourceIdentifier());
                    inst.setFormerResourceIdentifier(instance.getFormerResourceIdentifier());
                    inst.setExtension(instance.getExtension());
                    inst.setInstanceIdentifier(instance.getInstanceIdentifier());
                    List<Instance> instanceList = new ArrayList<Instance>();
                    instanceList.add(inst);
                    instCol.setInstance(instanceList);

                    byte[] documentBytes = recordProcessor.toXML(instCol).getBytes();
                    Binary binary = null;
                    if (documentBytes != null && instNode != null && documentBytes.length > 0) {
                        binary = session.getValueFactory().createBinary(new ByteArrayInputStream(documentBytes));
                        instNode.getNode("jcr:content").setProperty("jcr:data", binary);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("error while updating Docstore in reindexing Process" + e.getMessage(), e);
        }
    }

    private void indexSolrDocs(List<SolrInputDocument> solrInputDocs) {

        try {
            ServiceLocator.getIndexerService().indexSolrDocuments(solrInputDocs);
            logger.info("Linking Bib and Instance Records (" + solrInputDocs.size() + "): ");
            solrInputDocs.clear();
        } catch (Exception e) {
            logger.error(
                    "Linking Bib and Instance Records (" + (solrInputDocs.size()) + "), Failed @ batch(" + solrInputDocs
                            .size() + "): Cause: " + e + "\n\tContinuous", e);
        }
    }


    private boolean checkApplicability(Object value, Object fieldValue) {
        if (fieldValue instanceof Collection) {
            for (Object object : (Collection) fieldValue) {
                if (object.equals(value)) {
                    return true;
                }
            }
            return false;
        } else {
            return value.equals(fieldValue);
        }
    }


    private String compareListRString(Object id) {
        if (id != null) {
            if (id instanceof List) {
                List<String> idList = (List<String>) id;
                return idList.get(0);
            } else if (id instanceof String) {
                String strId = (String) id;
                return strId;
            }
        }
        return null;
    }

    private void compareObjNAddValue(String id, Object idObj, SolrDocument solrDoc, String identifier) {
        if (idObj != null) {
            if (idObj instanceof List) {
                List<String> instBibIdList = (List<String>) idObj;
                if (!instBibIdList.contains(id)) {
                    solrDoc.addField(identifier, id);
                }
            } else if (idObj instanceof String) {
                String instBibId = (String) idObj;
                if (!instBibId.equalsIgnoreCase(id)) {
                    solrDoc.addField(identifier, id);
                }
            }
        } else {
            solrDoc.addField(identifier, id);
        }
    }

    private void workLicense(String docCategory, String docType, String docFormat) {
        Session session = null;
        long totalCount = 0;
        long nodeCount = 0;
        List<RequestDocument> docs = new ArrayList<RequestDocument>();
        try {
            session = RepositoryManager.getRepositoryManager().getSession(ProcessParameters.BULK_DEFAULT_USER,
                    ProcessParameters.BULK_DEFUALT_ACTION);
            RequestDocument rd = new RequestDocument();
            rd.setCategory(docCategory);
            rd.setType(docType);
            rd.setFormat(docFormat);
            DocumentIngester docIngester = new DocumentIngester();
            Node nodeFormat = docIngester.getStaticFormatNode(rd, session);
            NodeIterator nodesL1 = nodeFormat.getNodes();
            List<ReIndexingBatchStatus> batchStatusList = new ArrayList<ReIndexingBatchStatus>();
            StopWatch loadTimer = new StopWatch();
            StopWatch batchTimer = new StopWatch();
            loadTimer.start();
            RepositoryBrowser repositoryBrowser = new RepositoryBrowser();
            while (nodesL1.hasNext()) {
                Node nodeL1 = nodesL1.nextNode();
                NodeIterator nodesFile = nodeL1.getNodes();
                nodeCount = nodesFile.getSize();
                batchTimer.start();
                while (nodesFile.hasNext()) {
                    if (docs.size() == ProcessParameters.BULK_PROCESSOR_SPLIT_SIZE && !isStop()) {
                        if (!isStop()) {
                            ReIndexingBatchStatus reIndexingBatchStatus = indexBeforeParams(loadTimer);
                            indexDocs(docs, totalCount, nodeCount, batchStatusList, reIndexingBatchStatus);
                            indexAfterParams(batchTimer, reIndexingBatchStatus, batchStatusList);
                            resetTimers(batchTimer, loadTimer);
                            totalCount = 0;
                            logger.info("Rebuild");
                        } else {
                            return;
                        }
                    } else {

                        Node fileNode = nodesFile.nextNode();
                        String content = null;
                        if (docFormat.equals(DocFormat.ONIXPL.getCode())) {
                            content = checkoutManager.getData(fileNode);
                        } else if (docFormat.equals(DocFormat.PDF.getCode()) || docFormat
                                .equals(DocFormat.DOC.getCode())) {
                            content = checkoutManager
                                    .checkOutBinary(fileNode.getIdentifier(), ProcessParameters.BULK_DEFAULT_USER,
                                            ProcessParameters.BULK_DEFUALT_ACTION, docFormat);
                        }
                        RequestDocument reqDoc = (RequestDocument) rd.clone();
                        reqDoc.setId(fileNode.getIdentifier());
                        reqDoc.setUuid(fileNode.getIdentifier());
                        Content contentObj = new Content();
                        contentObj.setContent(content);
                        reqDoc.setContent(contentObj);
                        docs.add(reqDoc);
                        totalCount++;
                    }
                }
            }
            if (docs.size() > 0 && !isStop()) {
                ReIndexingBatchStatus reIndexingBatchStatus = indexBeforeParams(loadTimer);
                indexDocs(docs, totalCount, nodeCount, batchStatusList, reIndexingBatchStatus);
                indexAfterParams(batchTimer, reIndexingBatchStatus, batchStatusList);
            }
        } catch (Exception e) {
            logger.error(
                    "Rebuild Indexes Process(" + docCategory + " : " + docType + " : " + docFormat + ") Processed(" + (
                            totalCount - docs.size()) + "), Failed @ batch(" + docs.size() + "): Cause: " + e, e);
        } finally {
            try {
                if (isStop) {
                    ReIndexingStatus.getInstance().getDocTypeList().setStatus("Stopped");
                } else {
                    ReIndexingStatus.getInstance().getDocTypeList().setStatus("Done");
                }
                RepositoryManager.getRepositoryManager().logout(session);
            } catch (OleException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void resetTimers(StopWatch batchTimer, StopWatch loadTimer) {
        batchTimer.reset();
        batchTimer.start();
        loadTimer.reset();
        loadTimer.start();
    }

    private void indexAfterParams(StopWatch batchTimer, ReIndexingBatchStatus reIndexingBatchStatus,
                                  List<ReIndexingBatchStatus> batchStatusList) {
        batchTimer.stop();
        reIndexingBatchStatus.setBatchTotalTime(batchTimer.toString());
        ReIndexingStatus.getInstance().getDocTypeList().setReIndBatStatusList(batchStatusList);
    }

    private ReIndexingBatchStatus indexBeforeParams(StopWatch loadTimer) {
        loadTimer.stop();
        ReIndexingBatchStatus reIndexingBatchStatus = new ReIndexingBatchStatus();
        reIndexingBatchStatus.setBatchTotalTime(" ");
        reIndexingBatchStatus.setBatchLoadTime(loadTimer.toString());
        return reIndexingBatchStatus;
    }

    private Node getNodeByUUID(Session newSession, String uuid) throws OleException {
        return new NodeHandler().getNodeByUUID(newSession, uuid);
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            /* InputStream in =getClass().getResourceAsStream("/mysql.properties");
            Properties properties = new Properties();
            properties.load(in);*/
            String connectionUrl = ConfigContext.getCurrentContextConfig().getProperty("datasource.url");
            String userName = ConfigContext.getCurrentContextConfig().getProperty("datasource.username");
            String passWord = ConfigContext.getCurrentContextConfig().getProperty("datasource.password");
            String driverName = ConfigContext.getCurrentContextConfig().getProperty("jdbc.driver");
            Class.forName(driverName);
            connection = DriverManager.getConnection(connectionUrl, userName, passWord);
        } catch (Exception e) {
            LOG.error("Exception : ", e);
        }
        return connection;
    }

    public String showStatus() {
        BibHoldingItemReindexer bibHoldingItemReindexer = BibHoldingItemReindexer.getInstance();
        return bibHoldingItemReindexer.showStats();
    }

    public String showBibStatus() {
        if(bibInfoStatistics == null) {
            bibInfoStatistics = new BibInfoStatistics();
        }
        return bibInfoStatistics.toString();
    }


    public String storeBibInfo(int batchSize) throws Exception {

        Date date = new Date();
        String STORAGE_EXCEPTION_FILE_NAME = "BibInfoLoadingErrors-" + date.toString() + ".txt";
        String STORAGE_STATUS_FILE_NAME = "BibInfoLoadingStatus" + date.toString() + ".txt";

        long startTime = System.currentTimeMillis();
        bibInfoStatistics = new BibInfoStatistics();
        bibInfoStatistics.setStartDateTime(date);

        bibTreeDBUtil.init(0, 0,null);

        int batchNo = 0;
        int count = bibTreeDBUtil.storeBibInfo(batchSize, filePath, STORAGE_EXCEPTION_FILE_NAME, bibInfoStatistics, batchNo);
        long batchStartTime = startTime;
        long batchEndTime = System.currentTimeMillis();
        long totalTimeForBatch = batchEndTime - batchStartTime;
        BatchBibTreeDBUtil.writeStatusToFile(filePath, STORAGE_STATUS_FILE_NAME, "Time taken for batch " + totalTimeForBatch);
        while(count > 0) {
            Date batchStartDate = new Date();
            batchStartTime = System.currentTimeMillis();
            bibInfoStatistics.setBatchStartDateTime(batchStartDate);
            count = bibTreeDBUtil.storeBibInfo(batchSize, filePath, STORAGE_EXCEPTION_FILE_NAME, bibInfoStatistics, batchNo++);
            batchEndTime = System.currentTimeMillis();
            Date batchEndDate = new Date();
            bibInfoStatistics.setBatchEndDateTime(batchEndDate);
            bibInfoStatistics.setBatchTotalTime((batchEndTime - batchStartTime));
            totalTimeForBatch = batchEndTime - batchStartTime;
            BatchBibTreeDBUtil.writeStatusToFile(filePath, STORAGE_STATUS_FILE_NAME, "Time taken for batch " + totalTimeForBatch);
        }

        long endTime = System.currentTimeMillis();
        Date endDate = new Date();
        bibInfoStatistics.setEndDateTime(endDate);
        long totalTime = endTime - startTime;
        bibInfoStatistics.setTotalTime(totalTime);
        BatchBibTreeDBUtil.writeStatusToFile(filePath, STORAGE_STATUS_FILE_NAME, "Total Time taken " + totalTime);
        return bibInfoStatistics.toString();
    }


    public String  reindexFromFile(String filePath, RebuildIndexDao rebuildIndexDao) throws Exception {
        return rebuildIndexDao.indexFromFile(filePath);
    }

}
