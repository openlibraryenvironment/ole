package org.kuali.ole.docstore.process.batch;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.jcr.Session;

import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.transaction.TransactionManager;
import org.kuali.ole.docstore.utility.BulkIngestStatistics;

/**
 * Contains the information related to a batch process.
 * User: tirumalesh.b
 * Date: 14/9/12 Time: 1:29 PM
 */
public class BulkProcessRequest {

    public static Set<String> validOperationSet = getValidOperationSet();
    protected String user;
    protected BulkProcessOperation operation;
    protected BulkProcessAction action;
    protected String docCategory;
    protected String docType;
    protected String docFormat;
    protected BulkIngestDataFormat dataFormat;
    protected String dataFolder;
    protected boolean doIndex; // whether to do indexing along with ingesting.
    protected int commitSize;

    protected TransactionManager transactionManager;
    protected Session session;
    protected BulkProcessStatus status;
    protected BulkIngestStatistics bulkIngestStatistics = BulkIngestStatistics.getInstance();
    protected List<RequestDocument> previousBatchDocuments;

    protected String bulkIngestFolder;

    public enum BulkIngestDataFormat {
        DOCSTORE, STANDARD
    }

    public enum BulkProcessAction {
        START, STOP, STATUS, CLEAR
    }

    public enum BulkProcessStatus {
        STARTED, STOPPED, DONE
    }

    public enum BulkProcessOperation {
        INGEST, REINDEX, LINK
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public BulkProcessOperation getOperation() {
        return operation;
    }

    public void setOperation(BulkProcessOperation operation) {
        this.operation = operation;
    }

    public BulkProcessAction getAction() {
        return action;
    }

    public void setAction(BulkProcessAction action) {
        this.action = action;
    }

    public String getDocCategory() {
        return docCategory;
    }

    public void setDocCategory(String docCategory) {
        this.docCategory = docCategory;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }

    public BulkIngestDataFormat getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(BulkIngestDataFormat dataFormat) {
        this.dataFormat = dataFormat;
    }

    public BulkProcessStatus getStatus() {
        return status;
    }

    public void setStatus(BulkProcessStatus status) {
        this.status = status;
    }

    public int getCommitSize() {
        return commitSize;
    }

    public void setCommitSize(int commitSize) {
        this.commitSize = commitSize;
    }

    public boolean isDoIndex() {
        return doIndex;
    }

    public void setDoIndex(boolean doIndex) {
        this.doIndex = doIndex;
    }

    public BulkIngestStatistics getBulkIngestStatistics() {
        return bulkIngestStatistics;
    }

    public void setBulkIngestStatistics(BulkIngestStatistics bulkIngestStatistics) {
        this.bulkIngestStatistics = bulkIngestStatistics;
    }

    public String getDataFolder() {
        return dataFolder;
    }

    public void setDataFolder(String dataFolder) {
        this.dataFolder = dataFolder;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<RequestDocument> getPreviousBatchDocuments() {
        return previousBatchDocuments;
    }

    public void setPreviousBatchDocuments(List<RequestDocument> previousBatchDocuments) {
        this.previousBatchDocuments = previousBatchDocuments;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public String getBulkIngestFolder() {
        return bulkIngestFolder;
    }

    public void setBulkIngestFolder(String bulkIngestFolder) {
        this.bulkIngestFolder = bulkIngestFolder;
    }

    private static Set<String> getValidOperationSet() {
        validOperationSet = new HashSet<String>();
        validOperationSet.add(BulkProcessOperation.INGEST.toString());
        validOperationSet.add(BulkProcessOperation.REINDEX.toString());
        validOperationSet.add(BulkProcessOperation.LINK.toString());
        return validOperationSet;
    }
}
