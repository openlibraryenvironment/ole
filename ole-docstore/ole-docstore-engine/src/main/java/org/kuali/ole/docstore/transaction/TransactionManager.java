package org.kuali.ole.docstore.transaction;

import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.process.batch.BulkProcessRequest;
import org.kuali.ole.docstore.utility.BatchIngestStatistics;

import javax.jcr.AccessDeniedException;
import javax.jcr.ItemExistsException;
import javax.jcr.RepositoryException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 6/20/13
 * Time: 12:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TransactionManager {

    public enum TransactionState {
        IDLE, STARTED, COMMITTED, ABORTED, FAILED
    }

    public List<ResponseDocument> ingest(List<RequestDocument> requestDocuments) throws Exception;

    public List<ResponseDocument> checkIn(List<RequestDocument> requestDocuments) throws Exception;

    public List<ResponseDocument> checkOut(List<RequestDocument> requestDocuments, Object object) throws Exception;

    public List<ResponseDocument> bind(List<RequestDocument> requestDocuments, String operation) throws Exception;

    public List<ResponseDocument> unbind(List<RequestDocument> requestDocuments, String operation) throws Exception;

    public List<ResponseDocument> delete(List<RequestDocument> requestDocuments) throws Exception;

    public List<ResponseDocument> deleteVerify(List<RequestDocument> requestDocuments) throws Exception;

    public void transferInstances(List<RequestDocument> requestDocuments) throws Exception;

    public void transferItems(List<RequestDocument> requestDocuments) throws Exception;

    public void batchIngest(BulkProcessRequest bulkProcessRequest, List<RequestDocument> requestDocuments)
            throws Exception;

    public void startTransaction(String user, String operation) throws Exception;

    public void startSession(String user, String operation) throws Exception;

    public void commit() throws Exception;

    public void closeSession();

    public void abort();

}
