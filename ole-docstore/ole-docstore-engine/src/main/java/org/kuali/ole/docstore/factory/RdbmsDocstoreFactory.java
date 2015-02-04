package org.kuali.ole.docstore.factory;

import org.kuali.ole.docstore.document.DocumentManager;
import org.kuali.ole.docstore.document.rdbms.RdbmsDocumentManagerFactory;
import org.kuali.ole.docstore.transaction.RdbmsTransactionManager;
import org.kuali.ole.docstore.transaction.TransactionManager;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 6/20/13
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class RdbmsDocstoreFactory extends AbstractDocstoreFactory {

    @Override
    public TransactionManager getTransactionManager(String docCategory, String docType, String docFormat) {
        TransactionManager transactionManager = new RdbmsTransactionManager();
        return transactionManager;
    }

    @Override
    public DocumentManager getDocumentManager(String docCategory, String docType, String docFormat) {
        DocumentManager documentManager = RdbmsDocumentManagerFactory.getInstance().getDocumentManager(docCategory, docType, docFormat);
        return documentManager;
    }

}
