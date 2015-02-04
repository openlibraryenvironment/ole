package org.kuali.ole.docstore.service;

import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.process.batch.BulkProcessRequest;
import org.kuali.ole.pojo.OleException;

import javax.jcr.RepositoryException;
import java.io.FileNotFoundException;

/**
 * Defines the services/operations that can be performed on one or more documents.
 * This is the API for clients running in the same JVM as DocStore.
 * User: tirumalesh.b
 * Date: 28/8/12 Time: 11:54 AM
 */
public interface DocumentService {

    /**
     * Processes the documents, as per the operation specified, in the given request.
     * The given request specifies a single operation to be performed on one or more documents
     * of same or different [cat-type-format].
     *
     * @param request
     * @return
     * @throws OleDocStoreException
     */
    public Response process(Request request) throws OleDocStoreException, RepositoryException, OleException, FileNotFoundException;

    /**
     * Processes the documents, as per the operation specified, in the given request.
     * The given request specifies a single operation to be performed on generally a large number of documents
     * of same [cat-type-format].
     *
     * @param request
     * @return
     * @throws OleDocStoreException
     */
    public void bulkProcess(BulkProcessRequest request) throws Exception;

    /*
        public Response ingest(Request request) throws OleDocStoreException;

        public Response checkout(Request request) throws OleDocStoreException;

        public Response checkin(Request request) throws OleDocStoreException;

        public Response delete(Request request) throws OleDocStoreException;
    */

}
