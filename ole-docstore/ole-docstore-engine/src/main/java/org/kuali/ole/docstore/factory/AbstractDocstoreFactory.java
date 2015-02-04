package org.kuali.ole.docstore.factory;

import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.indexer.solr.DocumentIndexerManagerFactory;
import org.kuali.ole.docstore.service.DocumentService;
import org.kuali.ole.docstore.service.DocumentServiceImpl;


/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 6/20/13
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
abstract public class AbstractDocstoreFactory implements DocstoreFactory {

    @Override
    public DocumentService getDocumentService() {

        DocumentService documentService = DocumentServiceImpl.getInstance();
        return documentService;
    }

/*    @Override
    public IndexerService getIndexerService() {
        IndexerService indexerService = IndexerServiceImpl.getInstance();
        return indexerService;
    }*/

    @Override
    public IndexerService getDocumentIndexManager(String docCategory, String docType, String docFormat) {
        IndexerService indexerService = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(docCategory, docType, docFormat);
        return indexerService;
    }
}
