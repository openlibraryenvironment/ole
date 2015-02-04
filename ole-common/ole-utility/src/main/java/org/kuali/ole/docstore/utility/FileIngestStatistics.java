package org.kuali.ole.docstore.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vk8458
 * Date: 8/27/12
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileIngestStatistics {
    private List<BatchIngestStatistics> batchStatisticsList = new ArrayList<BatchIngestStatistics>();
    private String fileName;
    private String fileStatus;

    public FileIngestStatistics() {

    }

    public String getFileName() {
        return fileName;
    }

    public BatchIngestStatistics startBatch() {
        BatchIngestStatistics batchStatistics = new BatchIngestStatistics();
        batchStatisticsList.add(batchStatistics);
        return batchStatistics;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public List<BatchIngestStatistics> getBatchStatisticsList() {
        return batchStatisticsList;
    }

    public void setBatchStatisticsList(List<BatchIngestStatistics> batchStatisticsList) {
        this.batchStatisticsList = batchStatisticsList;
    }

    @Override
    public String toString() {
        return "FileIngestStatistics{" +
                "batchStatisticsList=" + batchStatisticsList +
                ", fileName='" + fileName + '\'' +
                ", fileStatus='" + fileStatus + '\'' +
                '}';
    }
}
