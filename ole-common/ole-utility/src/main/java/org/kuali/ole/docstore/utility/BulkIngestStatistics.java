package org.kuali.ole.docstore.utility;

import org.json.simple.JSONObject;
import org.kuali.ole.utility.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 6/11/12
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class BulkIngestStatistics {
    private static final Logger log = LoggerFactory.getLogger(BulkIngestStatistics.class);
    public List<BatchIngestStatistics> batchStatisticsList = new ArrayList<BatchIngestStatistics>();
    public List<FileIngestStatistics> fileIngestStatisticsList = new ArrayList<FileIngestStatistics>();

    public String batchSize;
    public String splitSize;
    public boolean isFirstBatch;
    public boolean isLastBatch;
    public long fileRecCount;
    public long commitRecCount;

    private static BulkIngestStatistics instance = new BulkIngestStatistics();
    public FileIngestStatistics fileIngestStatistics = null;

    private BulkIngestStatistics() {

    }

    public static BulkIngestStatistics getInstance() {
        if (instance == null) {
            instance = new BulkIngestStatistics();
        }
        return instance;
    }


    public FileIngestStatistics startFile() {
        FileIngestStatistics fileIngestStatistics = new FileIngestStatistics();
        batchStatisticsList = fileIngestStatistics.getBatchStatisticsList();
        fileIngestStatistics.setBatchStatisticsList(batchStatisticsList);
        fileIngestStatisticsList.add(fileIngestStatistics);
        return fileIngestStatistics;
    }


    public void clearBulkIngestStatistics() {
        batchStatisticsList = new ArrayList<BatchIngestStatistics>();
        fileIngestStatisticsList = new ArrayList<FileIngestStatistics>();
        isLastBatch = false;
    }


    public BatchIngestStatistics getCurrentBatch() {
        BatchIngestStatistics batchStatistics = null;
        if (batchStatisticsList != null && batchStatisticsList.size() > 0) {
            batchStatistics = batchStatisticsList.get(batchStatisticsList.size() - 1);
        } else {
            batchStatistics = new BatchIngestStatistics();
        }
        return batchStatistics;
    }


    public List<BatchIngestStatistics> getBatchStatisticsList() {
        return batchStatisticsList;
    }

    public void setBatchStatisticsList(List<BatchIngestStatistics> batchStatisticsList) {
        this.batchStatisticsList = batchStatisticsList;
    }

    public FileIngestStatistics getFileIngestStatistics() {
        return fileIngestStatistics;
    }

    public void setFileIngestStatistics(FileIngestStatistics fileIngestStatistics) {
        this.fileIngestStatistics = fileIngestStatistics;
    }

    public List<FileIngestStatistics> getFileIngestStatisticsList() {
        return fileIngestStatisticsList;
    }

    public void setFileIngestStatisticsList(List<FileIngestStatistics> fileIngestStatisticsList) {
        this.fileIngestStatisticsList = fileIngestStatisticsList;
    }


    public String getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public String getSplitSize() {
        return splitSize;
    }

    public void setSplitSize(String splitSize) {
        this.splitSize = splitSize;
    }

    public boolean isLastBatch() {
        return isLastBatch;
    }

    public boolean isFirstBatch() {
        return isFirstBatch;
    }

    public void setFirstBatch(boolean firstBatch) {
        isFirstBatch = firstBatch;
    }

    public void setLastBatch(boolean lastBatch) {
        isLastBatch = lastBatch;
    }

    public long getFileRecCount() {
        return fileRecCount;
    }

    public void setFileRecCount(long fileRecCount) {
        this.fileRecCount = fileRecCount;
    }

    public long getCommitRecCount() {
        return commitRecCount;
    }

    public void setCommitRecCount(long commitRecCount) {
        this.commitRecCount = commitRecCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(BatchIngestStatistics.mertixHeading);
        long totalBatchTime = 0;
        long totalTimeToconvertStringToReqObj = 0;
        long totalTimeToCreateNodesInJcr = 0;
        long totalTimeToSaveJcrSession = 0;
        long totalIngestingTime = 0;
        long totalTimeToConvertXmlToPojo = 0;
        long totalTimeToConvertToSolrInputDocs = 0;
        long totalTimeToIndexSolrInputDocs = 0;
        long totalTimeToSolrCommit = 0;
        long totalIndexingTime = 0;
        long totalIngestNIndexTotalTime = 0;
        long totalTimeToSolrOptimize = 0;

        for (FileIngestStatistics fileStatistics : fileIngestStatisticsList) {
            long batchTime = 0;
            long timeToConvertStringToReqObj = 0;
            long timeToCreateNodesInJcr = 0;
            long timeToSaveJcrSession = 0;
            long ingestingTime = 0;
            long timeToConvertXmlToPojo = 0;
            long timeToConvertToSolrInputDocs = 0;
            long timeToIndexSolrInputDocs = 0;
            long timeToSolrCommit = 0;
            long indexingTime = 0;
            long ingestNIndexTotalTime = 0;
            long timeToSolrOptimize = 0;
            for (BatchIngestStatistics bS : fileStatistics.getBatchStatisticsList()) {
                timeToConvertStringToReqObj = timeToConvertStringToReqObj + bS.getTimeToConvertStringToReqObj();
                totalTimeToconvertStringToReqObj = totalTimeToconvertStringToReqObj + bS
                        .getTimeToConvertStringToReqObj();
                timeToCreateNodesInJcr = timeToCreateNodesInJcr + bS.getTimeToCreateNodesInJcr();
                totalTimeToCreateNodesInJcr = totalTimeToCreateNodesInJcr + bS.getTimeToCreateNodesInJcr();
                timeToSaveJcrSession = timeToSaveJcrSession + bS.getTimeToSaveJcrSession();
                totalTimeToSaveJcrSession = totalTimeToSaveJcrSession + bS.getTimeToSaveJcrSession();
                ingestingTime = ingestingTime + bS.getIngestingTime();
                totalIngestingTime = totalIngestingTime + bS.getIngestingTime();
                timeToConvertXmlToPojo = timeToConvertXmlToPojo + bS.getTimeToConvertXmlToPojo();
                totalTimeToConvertXmlToPojo = totalTimeToConvertXmlToPojo + bS.getTimeToConvertXmlToPojo();
                timeToConvertToSolrInputDocs = timeToConvertToSolrInputDocs + bS.getTimeToConvertToSolrInputDocs();
                totalTimeToConvertToSolrInputDocs = totalTimeToConvertToSolrInputDocs + bS
                        .getTimeToConvertToSolrInputDocs();
                timeToIndexSolrInputDocs = timeToIndexSolrInputDocs + bS.getTimeToIndexSolrInputDocs();
                totalTimeToIndexSolrInputDocs = totalTimeToIndexSolrInputDocs + bS.getTimeToIndexSolrInputDocs();
                timeToSolrCommit = timeToSolrCommit + bS.getTimeToSolrCommit();
                totalTimeToSolrCommit = totalTimeToSolrCommit + bS.getTimeToSolrCommit();
                indexingTime = indexingTime + bS.getIndexingTime();
                totalIndexingTime = totalIndexingTime + bS.getIndexingTime();
                ingestNIndexTotalTime = ingestNIndexTotalTime + bS.getIngestNIndexTotalTime();
                totalIngestNIndexTotalTime = totalIngestNIndexTotalTime + bS.getIngestNIndexTotalTime();
                batchTime = batchTime + bS.getBatchTime();
                totalBatchTime = totalBatchTime + bS.getBatchTime();
                timeToSolrOptimize = timeToSolrOptimize + bS.getTimeToSolrOptimize();
                totalTimeToSolrOptimize = totalTimeToSolrOptimize + bS.getTimeToSolrOptimize();
                bS.buildBatchMetric(sb, bS);
            }
            sb.append("\n");
            sb.append("File Total Time\n");
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(timeToConvertStringToReqObj));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(timeToCreateNodesInJcr));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(timeToSaveJcrSession));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(ingestingTime));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(timeToConvertXmlToPojo));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(timeToConvertToSolrInputDocs));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(timeToIndexSolrInputDocs));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(timeToSolrCommit));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(indexingTime));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(ingestNIndexTotalTime));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(batchTime));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(timeToSolrOptimize));

            sb.append("\n");
            sb.append("Total Time\n");
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(totalTimeToconvertStringToReqObj));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(totalTimeToCreateNodesInJcr));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(totalTimeToSaveJcrSession));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(totalIngestingTime));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(totalTimeToConvertXmlToPojo));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(totalTimeToConvertToSolrInputDocs));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(totalTimeToIndexSolrInputDocs));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(totalTimeToSolrCommit));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(totalIndexingTime));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(totalIngestNIndexTotalTime));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(totalBatchTime));
            sb.append(" \t");
            sb.append(DateTimeUtil.formatTime(totalTimeToSolrOptimize));

        }

        return sb.toString();
    }


    public String getJsonString() {
        StringWriter out = new StringWriter();
        JSONObject obj = new JSONObject();
        LinkedHashMap bulkIngestMap = null;
        LinkedList bulkIngestList = new LinkedList();
        long totalBatchTime = 0;
        long totalTimeToconvertStringToReqObj = 0;
        long totalTimeToCreateNodesInJcr = 0;
        long totalTimeToSaveJcrSession = 0;
        long totalIngestingTime = 0;
        long totalTimeToConvertXmlToPojo = 0;
        long totalTimeToConvertToSolrInputDocs = 0;
        long totalTimeToIndexSolrInputDocs = 0;
        long totalTimeToSolrCommit = 0;
        long totalIndexingTime = 0;
        long totalIngestNIndexTotalTime = 0;
/*        bulkIngestMap = getBulkIngestMap("File Name", "Bulk Ingest Status", "Batch Start Time",
                                         "Time To Convert String To ReqObj", "Time To Create Nodes In Jcr",
                                         "Time To Save Jcr Session", "Ingesting Time", "Time To Convert Xml To Pojo",
                                         "Time To Convert To Solr InputDocs", "Time To Index Solr InputDocs",
                                         "Time To Solr Commit", "Indexing Time", "Ingest and Index Total Time",
                                         "Batch Time", "Batch End Time");
        bulkIngestList.add(bulkIngestMap);*/

        for (FileIngestStatistics fileStatistics : fileIngestStatisticsList) {
            long batchTime = 0;
            long timeToconvertStringToReqObj = 0;
            long timeToCreateNodesInJcr = 0;
            long timeToSaveJcrSession = 0;
            long ingestingTime = 0;
            long timeToConvertXmlToPojo = 0;
            long timeToConvertToSolrInputDocs = 0;
            long timeToIndexSolrInputDocs = 0;
            long timeToSolrCommit = 0;
            long indexingTime = 0;
            long ingestNIndexTotalTime = 0;
            bulkIngestMap = getBulkIngestMap(fileStatistics.getFileName(), fileStatistics.getFileStatus(), "", "", "",
                    "", "", "", "", "", "", "", "", "", "");
            bulkIngestList.add(bulkIngestMap);
            for (BatchIngestStatistics bS : fileStatistics.getBatchStatisticsList()) {
                bulkIngestMap = getBulkIngestMap("", "", bS.getBatchStartTime(),
                        DateTimeUtil.formatTime(bS.getTimeToConvertStringToReqObj()),
                        DateTimeUtil.formatTime(bS.getTimeToCreateNodesInJcr()),
                        DateTimeUtil.formatTime(bS.getTimeToSaveJcrSession()),
                        DateTimeUtil.formatTime(bS.getIngestingTime()),
                        DateTimeUtil.formatTime(bS.getTimeToConvertXmlToPojo()),
                        DateTimeUtil.formatTime(bS.getTimeToConvertToSolrInputDocs()),
                        DateTimeUtil.formatTime(bS.getTimeToIndexSolrInputDocs()),
                        DateTimeUtil.formatTime(bS.getTimeToSolrCommit()),
                        DateTimeUtil.formatTime(bS.getIndexingTime()),
                        DateTimeUtil.formatTime(bS.getIngestNIndexTotalTime()),
                        DateTimeUtil.formatTime(bS.getBatchTime()), bS.getBatchEndTime());
                timeToconvertStringToReqObj = timeToconvertStringToReqObj + bS.getTimeToConvertStringToReqObj();
                totalTimeToconvertStringToReqObj = totalTimeToconvertStringToReqObj + bS
                        .getTimeToConvertStringToReqObj();
                timeToCreateNodesInJcr = timeToCreateNodesInJcr + bS.getTimeToCreateNodesInJcr();
                totalTimeToCreateNodesInJcr = totalTimeToCreateNodesInJcr + bS.getTimeToCreateNodesInJcr();
                timeToSaveJcrSession = timeToSaveJcrSession + bS.getTimeToSaveJcrSession();
                totalTimeToSaveJcrSession = totalTimeToSaveJcrSession + bS.getTimeToSaveJcrSession();
                ingestingTime = ingestingTime + bS.getIngestingTime();
                totalIngestingTime = totalIngestingTime + bS.getIngestingTime();
                timeToConvertXmlToPojo = timeToConvertXmlToPojo + bS.getTimeToConvertXmlToPojo();
                totalTimeToConvertXmlToPojo = totalTimeToConvertXmlToPojo + bS.getTimeToConvertXmlToPojo();
                timeToConvertToSolrInputDocs = timeToConvertToSolrInputDocs + bS.getTimeToConvertToSolrInputDocs();
                totalTimeToConvertToSolrInputDocs = totalTimeToConvertToSolrInputDocs + bS
                        .getTimeToConvertToSolrInputDocs();
                timeToIndexSolrInputDocs = timeToIndexSolrInputDocs + bS.getTimeToIndexSolrInputDocs();
                totalTimeToIndexSolrInputDocs = totalTimeToIndexSolrInputDocs + bS.getTimeToIndexSolrInputDocs();
                timeToSolrCommit = timeToSolrCommit + bS.getTimeToSolrCommit();
                totalTimeToSolrCommit = totalTimeToSolrCommit + bS.getTimeToSolrCommit();
                indexingTime = indexingTime + bS.getIndexingTime();
                totalIndexingTime = totalIndexingTime + bS.getIndexingTime();
                ingestNIndexTotalTime = ingestNIndexTotalTime + bS.getIngestNIndexTotalTime();
                totalIngestNIndexTotalTime = totalIngestNIndexTotalTime + bS.getIngestNIndexTotalTime();
                batchTime = batchTime + bS.getBatchTime();
                totalBatchTime = totalBatchTime + bS.getBatchTime();
                bulkIngestList.add(bulkIngestMap);
            }
            bulkIngestMap = getBulkIngestMap("", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
            bulkIngestList.add(bulkIngestMap);
            bulkIngestMap = getBulkIngestMap("File Total Time", "", "",
                    DateTimeUtil.formatTime(timeToconvertStringToReqObj),
                    DateTimeUtil.formatTime(timeToCreateNodesInJcr),
                    DateTimeUtil.formatTime(timeToSaveJcrSession),
                    DateTimeUtil.formatTime(ingestingTime),
                    DateTimeUtil.formatTime(timeToConvertXmlToPojo),
                    DateTimeUtil.formatTime(timeToConvertToSolrInputDocs),
                    DateTimeUtil.formatTime(timeToIndexSolrInputDocs),
                    DateTimeUtil.formatTime(timeToSolrCommit),
                    DateTimeUtil.formatTime(indexingTime),
                    DateTimeUtil.formatTime(ingestNIndexTotalTime),
                    DateTimeUtil.formatTime(batchTime), "");
            bulkIngestList.add(bulkIngestMap);
            bulkIngestMap = getBulkIngestMap("Total Time", "", "",
                    DateTimeUtil.formatTime(totalTimeToconvertStringToReqObj),
                    DateTimeUtil.formatTime(totalTimeToCreateNodesInJcr),
                    DateTimeUtil.formatTime(totalTimeToSaveJcrSession),
                    DateTimeUtil.formatTime(totalIngestingTime),
                    DateTimeUtil.formatTime(totalTimeToConvertXmlToPojo),
                    DateTimeUtil.formatTime(totalTimeToConvertToSolrInputDocs),
                    DateTimeUtil.formatTime(totalTimeToIndexSolrInputDocs),
                    DateTimeUtil.formatTime(totalTimeToSolrCommit),
                    DateTimeUtil.formatTime(totalIndexingTime),
                    DateTimeUtil.formatTime(totalIngestNIndexTotalTime),
                    DateTimeUtil.formatTime(totalBatchTime), "");
            bulkIngestList.add(bulkIngestMap);
        }

        obj.put("rows", bulkIngestList);
        try {
            obj.writeJSONString(out);
        } catch (IOException e) {
            log.error("Error occurred due to :", e);
        }
        return out.toString();
    }

    public LinkedHashMap getBulkIngestMap(String fileName, String status, String batchStartTime,
                                          String timeToConvertStringToReqObj, String timeToCreateNodesInJcr,
                                          String timeToSaveJcrSession, String ingestingTime,
                                          String timeToConvertXmlToPojo, String timeToConvertToSolrInputDocs,
                                          String timeToIndexSolrInputDocs, String timeToSolrCommit, String indexingTime,
                                          String ingestNIndexTotalTime, String batchTime, String batchEndTime) {
        LinkedHashMap bulkIngestMap = new LinkedHashMap();
        bulkIngestMap.put("fileName", fileName);
        bulkIngestMap.put("status", status);
        bulkIngestMap.put("batchStartTime", batchStartTime);
        bulkIngestMap.put("timeToConvertStringToReqObj", timeToConvertStringToReqObj);
        bulkIngestMap.put("timeToCreateNodesInJcr", timeToCreateNodesInJcr);
        bulkIngestMap.put("timeToSaveJcrSession", timeToSaveJcrSession);
        bulkIngestMap.put("ingestingTime", ingestingTime);
        bulkIngestMap.put("timeToConvertXmlToPojo", timeToConvertXmlToPojo);
        bulkIngestMap.put("timeToConvertToSolrInputDocs", timeToConvertToSolrInputDocs);
        bulkIngestMap.put("timeToIndexSolrInputDocs", timeToIndexSolrInputDocs);
        bulkIngestMap.put("timeToSolrCommit", timeToSolrCommit);
        bulkIngestMap.put("indexingTime", indexingTime);
        bulkIngestMap.put("ingestNIndexTotalTime", ingestNIndexTotalTime);
        bulkIngestMap.put("batchTime", batchTime);
        bulkIngestMap.put("batchEndTime", batchEndTime);
        return bulkIngestMap;
    }

}
