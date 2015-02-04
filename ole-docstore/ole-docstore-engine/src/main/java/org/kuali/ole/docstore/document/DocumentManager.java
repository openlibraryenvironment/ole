package org.kuali.ole.docstore.document;

import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.process.batch.BulkProcessRequest;
import org.kuali.ole.pojo.OleException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Defines the operations that can be performed on one or more documents.
 *
 * @author tirumalesh.b
 * @version %I%, %G%
 *          Date: 28/8/12 Time: 12:17 PM
 */
public interface DocumentManager {

    /**
     * Ingests (stores and indexes) the given documents.
     * Either all documents are saved or none.
     *
     * @param requestDocuments
     * @return list of ResponseDocuments, if all RequestDocuments are ingested successfully.
     * @throws OleDocStoreException
     */
    public List<ResponseDocument> ingest(List<RequestDocument> requestDocuments, Object object)
            throws OleDocStoreException;

    /**
     * Ingests (stores and indexes) the given document.
     *
     * @param requestDocument
     * @param respDoc
     * @return
     * @throws OleDocStoreException
     */
    public ResponseDocument ingest(RequestDocument requestDocument, Object object, ResponseDocument respDoc) throws OleDocStoreException;

    public List<ResponseDocument> checkout(List<RequestDocument> requestDocuments, Object object) throws OleDocStoreException;

    public ResponseDocument checkout(RequestDocument requestDocument, Object object) throws OleDocStoreException;

    public List<ResponseDocument> checkin(List<RequestDocument> requestDocuments, Object object) throws OleDocStoreException;

    public ResponseDocument checkin(RequestDocument requestDocument, Object object, ResponseDocument respDoc) throws OleDocStoreException;

    public List<ResponseDocument> delete(List<RequestDocument> requestDocuments, Object object) throws Exception;

    public ResponseDocument delete(RequestDocument requestDocument, Object object) throws Exception;

    public ResponseDocument buildResponseDocument(RequestDocument requestDocument);

    public void validateInput(RequestDocument requestDocument, Object object, List<String> valuesList) throws OleDocStoreException;

    /**
     * Performs bulk ingest as per the details in the given bulkProcessRequest.
     *
     * @param bulkProcessRequest
     * @param requestDocuments   documents to be processed in the current invocation.
     */
    public void bulkIngest(BulkProcessRequest bulkProcessRequest, List<RequestDocument> requestDocuments)
            throws OleDocStoreException;

    /**
     * Stores the given documents in docstore, but does not save(commit) the changes.
     * Useful in cases (such as bulk ingest) where saving(committing) depends on other conditions.
     *
     * @param requestDocuments
     * @param session
     * @throws OleDocStoreException
     */
    //public void store(List<RequestDocument> requestDocuments, Session session) throws OleDocStoreException;

    /**
     * Stores the given document in docstore, but does not save(commit) the changes.
     * Useful in cases (such as bulk ingest) where saving(committing) depends on other conditions.
     *
     * @param requestDocument
     * @param session
     * @throws OleDocStoreException
     */
    //public void store(RequestDocument requestDocument, Session session) throws OleDocStoreException;

    /**
     * Indexes the given documents in discovery.
     *
     * @param requestDocuments
     * @param commit           indicates whether to commit the changes.
     * @throws OleDocStoreException
     */
    public void index(List<RequestDocument> requestDocuments, boolean commit) throws OleDocStoreException;

    /**
     * Deletes the given documents, along with linked documents, from the discovery.
     *
     * @param requestDocument
     * @param responseDocument
     */
    //public void deleteIndex(List<RequestDocument> requestDocuments) throws OleDocStoreException;
    public Node storeDocument(RequestDocument requestDocument, Object object, ResponseDocument responseDocument) throws OleDocStoreException;

    ResponseDocument bind(RequestDocument requestDocument, Object object, String operation) throws OleDocStoreException, RepositoryException, OleException, FileNotFoundException;

    public ResponseDocument unbind(RequestDocument requestDocument, Object object, String operation) throws OleDocStoreException, RepositoryException, OleException, FileNotFoundException;

    public List<ResponseDocument> deleteVerify(List<RequestDocument> requestDocument, Object object);

    public ResponseDocument deleteVerify(RequestDocument requestDocument, Object object) throws Exception;

    public void addResourceId(RequestDocument requestDocument, ResponseDocument respDoc);
}
