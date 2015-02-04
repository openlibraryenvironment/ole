package org.kuali.ole.docstore.transaction;

import org.kuali.ole.docstore.indexer.solr.DocumentIndexerManagerFactory;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.service.BeanLocator;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 6/20/13
 * Time: 12:56 PM
 * To change this template use File | Settings | File Templates.
 */
abstract public class AbstractTransactionManager implements TransactionManager {

    public IndexerService getIndexerService(RequestDocument requestDocument) {
        IndexerService indexerService = BeanLocator.getDocstoreFactory().getDocumentIndexManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
        return indexerService;
    }

}
