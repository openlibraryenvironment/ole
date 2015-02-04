package org.kuali.ole.utility;

import org.junit.Test;
import org.kuali.ole.docstore.utility.BatchIngestStatistics;
import org.kuali.ole.docstore.utility.BulkIngestStatistics;
import org.kuali.ole.docstore.utility.FileIngestStatistics;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: ?
 * Time: ?
 * To change this template use File | Settings | File Templates.
 */
public class BulkIngestStatistics_UT extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(BulkIngestStatistics_UT.class);

    @Test
    public void testBulkIngestStatistics() throws Exception {
        BulkIngestStatistics bulkIngestStatistics = BulkIngestStatistics.getInstance();
        bulkIngestStatistics.setFirstBatch(false);
        bulkIngestStatistics.setLastBatch(false);
        bulkIngestStatistics.setBatchSize("10");
        bulkIngestStatistics.setFileRecCount(2);
        bulkIngestStatistics.setCommitRecCount(1);
        bulkIngestStatistics.setSplitSize("2");
        List<BatchIngestStatistics> batchIngestStatisticsList = new ArrayList<BatchIngestStatistics>();
        List<FileIngestStatistics> fileIngestStatisticsList = new ArrayList<FileIngestStatistics>();
        FileIngestStatistics fileIngestStatistics = bulkIngestStatistics.startFile();
        StringBuilder sb = new StringBuilder();
        fileIngestStatistics.setFileName("sample1.txt");
        fileIngestStatistics.setFileStatus("");
        BatchIngestStatistics batchIngestStatistics = fileIngestStatistics.startBatch();
        long timeToConvertStringToReqObj = 1200;
        long timeToCreateNodesInJcr = 3555;
        long timeToSaveJcrSession = 1222;
        long ingestingTime = 3400;
        long timeToConvertXmlToPojo = 2000;
        long timeToConvertToSolrInputDocs = 1400;
        long timeToIndexSolrInputDocs = 1860;
        long timeToSolrCommit = 1700;
        long timeToSolrOptimize = 2500;
        long indexingTime = 2200;
        long ingestNIndexTotalTime = 1400;
        long batchTime = 2500;
        String batchStartTime = "0:0:1.333";
        String batchEndTime = "0:0:5.222";
        long startTime = 2200;
        long endTime = 3200;
        LOG.info(fileIngestStatistics.toString());
        batchIngestStatistics.setTimeToConvertStringToReqObj(timeToConvertStringToReqObj);
        batchIngestStatistics.setTimeToCreateNodesInJcr(timeToCreateNodesInJcr);
        batchIngestStatistics.setTimeToSaveJcrSession(timeToSaveJcrSession);
        batchIngestStatistics.setIngestingTime(ingestingTime);
        batchIngestStatistics.setTimeToConvertXmlToPojo(timeToConvertXmlToPojo);
        batchIngestStatistics.setTimeToConvertToSolrInputDocs(timeToConvertToSolrInputDocs);
        batchIngestStatistics.setTimeToIndexSolrInputDocs(timeToIndexSolrInputDocs);
        batchIngestStatistics.setTimeToSolrCommit(timeToSolrCommit);
        batchIngestStatistics.setTimeToSolrOptimize(timeToSolrOptimize);
        batchIngestStatistics.setIndexingTime(indexingTime);
        batchIngestStatistics.setIngestNIndexTotalTime(ingestNIndexTotalTime);
        batchIngestStatistics.setBatchTime(batchTime);
        batchIngestStatisticsList.add(batchIngestStatistics);
        batchIngestStatistics.setBatchStartTime(batchStartTime);
        batchIngestStatistics.setBatchEndTime(batchEndTime);
        batchIngestStatistics.buildBatchMetric(sb, batchIngestStatistics);
        LOG.info(batchIngestStatistics.toString());
        LOG.info("Batch Metrics:" + sb.toString());
        //LOG.info(batchIngestStatistics.toString());
        fileIngestStatistics.setBatchStatisticsList(batchIngestStatisticsList);
        fileIngestStatisticsList.add(fileIngestStatistics);
        bulkIngestStatistics.setBatchStatisticsList(null);
        assertNotNull(bulkIngestStatistics.getCurrentBatch());
        bulkIngestStatistics.setBatchStatisticsList(batchIngestStatisticsList);
        bulkIngestStatistics.setFileIngestStatistics(fileIngestStatistics);
        bulkIngestStatistics.setFileIngestStatisticsList(fileIngestStatisticsList);
        assertNotNull(bulkIngestStatistics.getFileIngestStatisticsList());
        assertNotNull(bulkIngestStatistics.getFileIngestStatistics());
        assertNotNull(bulkIngestStatistics.getBatchStatisticsList());
        if (bulkIngestStatistics.isFirstBatch()) {
            LOG.info("first batch");
        } else if (bulkIngestStatistics.isLastBatch()) {
            LOG.info("last batch");
        }

        for (FileIngestStatistics fileIngestStatistics1 : fileIngestStatisticsList) {
            LOG.info("file name:" + fileIngestStatistics1.getFileName());
            for (BatchIngestStatistics batchIngestStatistics1 : fileIngestStatistics1.getBatchStatisticsList()) {
                LOG.info("Time To Convert String To ReqObj:" + DateTimeUtil
                        .formatTime(batchIngestStatistics1.getTimeToConvertStringToReqObj()));
                LOG.info("Time To Create Nodes In Jcr:" + DateTimeUtil
                        .formatTime(batchIngestStatistics1.getTimeToCreateNodesInJcr()));
                LOG.info("Time To Save Jcr Session:" + DateTimeUtil
                        .formatTime(batchIngestStatistics1.getTimeToSaveJcrSession()));
                LOG.info("Ingesting Time:" + DateTimeUtil.formatTime(batchIngestStatistics1.getIngestingTime()));
                LOG.info("Time To Convert Xml To Pojo:" + DateTimeUtil
                        .formatTime(batchIngestStatistics1.getTimeToConvertXmlToPojo()));
                LOG.info("Time To Convert To SolrInputDocs:" + DateTimeUtil
                        .formatTime(batchIngestStatistics1.getTimeToConvertToSolrInputDocs()));
                LOG.info("Time To Index SolrInputDocs:" + DateTimeUtil
                        .formatTime(batchIngestStatistics1.getTimeToIndexSolrInputDocs()));
                LOG.info(
                        "Time To Solr Commit:" + DateTimeUtil.formatTime(batchIngestStatistics1.getTimeToSolrCommit()));
                LOG.info("Time To Solr Optimize:" + DateTimeUtil
                        .formatTime(batchIngestStatistics1.getTimeToSolrOptimize()));
                LOG.info("Indexing Time:" + DateTimeUtil.formatTime(batchIngestStatistics1.getIndexingTime()));
                LOG.info("IngestNIndexTotalTime:" + DateTimeUtil
                        .formatTime(batchIngestStatistics1.getIngestNIndexTotalTime()));
                LOG.info("Batch Time:" + DateTimeUtil.formatTime(batchIngestStatistics1.getBatchTime()));
            }
            DateTimeUtil dateTimeUtil = new DateTimeUtil();
            String formatTime = dateTimeUtil.formatTime(3200, 2500);
            LOG.info("Format Time:" + formatTime);


        }

        BatchIngestStatistics batchIngestStatistics1 = bulkIngestStatistics.getCurrentBatch();
        sb = new StringBuilder();
        batchIngestStatistics.buildBatchMetric(sb, batchIngestStatistics1);
        LOG.info("Batch Size:" + bulkIngestStatistics.getBatchSize());
        LOG.info("Split Size:" + bulkIngestStatistics.getSplitSize());
        LOG.info("File Rec Count:" + bulkIngestStatistics.getFileRecCount());
        LOG.info("Commit Rec Count:" + bulkIngestStatistics.getCommitRecCount());
        LOG.info("Current Batch Metrics:" + sb.toString());
        LOG.info("BulkIngestStatistics toString:" + bulkIngestStatistics.toString());
        LOG.info("BulkIngestStatistics json:" + bulkIngestStatistics.getJsonString());
        bulkIngestStatistics.clearBulkIngestStatistics();
    }
}
