package org.kuali.ole.docstore.utility;

import org.kuali.ole.utility.DateTimeUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 6/11/12
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchIngestStatistics {
    public static String mertixHeading = null;

    public long timeToConvertStringToReqObj;
    public long timeToCreateNodesInJcr;
    public long timeToSaveJcrSession;
    public long ingestingTime;
    public long timeToConvertXmlToPojo;
    public long timeToConvertToSolrInputDocs;
    public long timeToIndexSolrInputDocs;
    public long timeToSolrCommit;
    public long timeToSolrOptimize;
    public long indexingTime;
    public long ingestNIndexTotalTime;
    public long batchTime;
    public String batchStartTime;
    public String batchEndTime;
    public long commitSize;
    public long recCount;


    static {
        mertixHeading = "\"Batch Start Time\" \t" + "\"Time To Convert String To ReqObj\" \t"
                + "\"Time To Create Nodes In Jcr\"\t" + "\"Time To Save Jcr Session\"\t" + "\"ingesting Time\"\t"
                + "\"Time To Convert Xml To Pojo\"\t" + "\"Time To Convert To Solr InputDocs\"\t"
                + "\"Time To Index Solr InputDocs\"\t" + "\"Time To Solr Commit\"\t" + "\"Indexing Time\"\t"
                + "\"Ingest N Index TotalTime\"\t" + "\"Batch Time\"\t" + "\"Batch End Time\" \t"
                + "\"Solr Optimize Time\"";
    }

    public long getTimeToConvertStringToReqObj() {
        return timeToConvertStringToReqObj;
    }

    public void setTimeToConvertStringToReqObj(long timeToConvertStringToReqObj) {
        this.timeToConvertStringToReqObj = timeToConvertStringToReqObj;
    }

    public long getTimeToCreateNodesInJcr() {
        return timeToCreateNodesInJcr;
    }

    public void setTimeToCreateNodesInJcr(long timeToCreateNodesInJcr) {
        this.timeToCreateNodesInJcr = timeToCreateNodesInJcr;
    }

    public long getTimeToSaveJcrSession() {
        return timeToSaveJcrSession;
    }

    public void setTimeToSaveJcrSession(long timeToSaveJcrSession) {
        this.timeToSaveJcrSession = timeToSaveJcrSession;
    }

    public long getIngestingTime() {
        return ingestingTime;
    }

    public void setIngestingTime(long ingestingTime) {
        this.ingestingTime = ingestingTime;
    }

    public long getTimeToConvertXmlToPojo() {
        return timeToConvertXmlToPojo;
    }

    public void setTimeToConvertXmlToPojo(long timeToConvertXmlToPojo) {
        this.timeToConvertXmlToPojo = timeToConvertXmlToPojo;
    }

    public long getTimeToConvertToSolrInputDocs() {
        return timeToConvertToSolrInputDocs;
    }

    public void setTimeToConvertToSolrInputDocs(long timeToConvertToSolrInputDocs) {
        this.timeToConvertToSolrInputDocs = timeToConvertToSolrInputDocs;
    }

    public long getTimeToIndexSolrInputDocs() {
        return timeToIndexSolrInputDocs;
    }

    public void setTimeToIndexSolrInputDocs(long timeToIndexSolrInputDocs) {
        this.timeToIndexSolrInputDocs = timeToIndexSolrInputDocs;
    }

    public long getTimeToSolrCommit() {
        return timeToSolrCommit;
    }

    public void setTimeToSolrCommit(long timeToSolrCommit) {
        this.timeToSolrCommit = timeToSolrCommit;
    }

    public long getTimeToSolrOptimize() {
        return timeToSolrOptimize;
    }

    public void setTimeToSolrOptimize(long timeToSolrOptimize) {
        this.timeToSolrOptimize = timeToSolrOptimize;
    }

    public long getIndexingTime() {
        return indexingTime;
    }

    public void setIndexingTime(long indexingTime) {
        this.indexingTime = indexingTime;
    }

    public long getIngestNIndexTotalTime() {
        return ingestNIndexTotalTime;
    }

    public void setIngestNIndexTotalTime(long ingestNIndexTotalTime) {
        this.ingestNIndexTotalTime = ingestNIndexTotalTime;
    }

    public long getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(long batchTime) {
        this.batchTime = batchTime;
    }

    public String getBatchStartTime() {
        return batchStartTime;
    }

    public void setBatchStartTime(String batchStartTime) {
        this.batchStartTime = batchStartTime;
    }

    public String getBatchEndTime() {
        return batchEndTime;
    }

    public void setBatchEndTime(String batchEndTime) {
        this.batchEndTime = batchEndTime;
    }

    public long getCommitSize() {
        return commitSize;
    }

    public void setCommitSize(long commitSize) {
        this.commitSize = commitSize;
    }

    public long getRecCount() {
        return recCount;
    }

    public void setRecCount(long recCount) {
        this.recCount = recCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(mertixHeading);
        BatchIngestStatistics bS = BulkIngestStatistics.getInstance().getCurrentBatch();
        buildBatchMetric(sb, bS);

        return sb.toString();
    }

    public void buildBatchMetric(StringBuilder sb, BatchIngestStatistics bS) {
        sb.append("\n");
        sb.append(bS.getBatchStartTime());
        sb.append("\t");
        sb.append(DateTimeUtil.formatTime(bS.getTimeToConvertStringToReqObj()));
        sb.append("\t");
        sb.append(DateTimeUtil.formatTime(bS.getTimeToCreateNodesInJcr()));
        sb.append("\t");
        sb.append(DateTimeUtil.formatTime(bS.getTimeToSaveJcrSession()));
        sb.append("\t");
        sb.append(DateTimeUtil.formatTime(bS.getIngestingTime()));
        sb.append("\t");
        sb.append(DateTimeUtil.formatTime(bS.getTimeToConvertXmlToPojo()));
        sb.append("\t");
        sb.append(DateTimeUtil.formatTime(bS.getTimeToConvertToSolrInputDocs()));
        sb.append("\t");
        sb.append(DateTimeUtil.formatTime(bS.getTimeToIndexSolrInputDocs()));
        sb.append("\t");
        sb.append(DateTimeUtil.formatTime(bS.getTimeToSolrCommit()));
        sb.append("\t");
        sb.append(DateTimeUtil.formatTime(bS.getIndexingTime()));
        sb.append("\t");
        sb.append(DateTimeUtil.formatTime(bS.getIngestNIndexTotalTime()));
        sb.append("\t");
        sb.append(DateTimeUtil.formatTime(bS.getBatchTime()));
        sb.append("\t");
        sb.append(bS.getBatchEndTime());
        sb.append("\t");
        sb.append(DateTimeUtil.formatTime(bS.getTimeToSolrOptimize()));
        sb.append("\t");
    }


}
