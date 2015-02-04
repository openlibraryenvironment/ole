package org.kuali.ole.docstore.factory;

import org.kuali.ole.docstore.document.DocumentManager;
import org.kuali.ole.docstore.transaction.TransactionManager;
import org.kuali.ole.docstore.service.DocumentService;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 6/20/13
 * Time: 12:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocstoreFactory {

    public DocumentService getDocumentService();

    public TransactionManager getTransactionManager(String docCategory, String docType, String docFormat);

    public DocumentManager getDocumentManager(String docCategory, String docType, String docFormat);

    //  public IndexerService getIndexerService();

    public org.kuali.ole.docstore.indexer.solr.IndexerService getDocumentIndexManager(String docCategory, String docType, String docFormat);

}
