package org.kuali.ole.docstore.factory;

import org.kuali.ole.docstore.document.DocumentManager;
import org.kuali.ole.docstore.document.jcr.JcrDocumentManagerFactory;
import org.kuali.ole.docstore.transaction.JcrTransactionManager;
import org.kuali.ole.docstore.transaction.TransactionManager;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 6/20/13
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class JcrDocstoreFactory extends AbstractDocstoreFactory implements DocstoreFactory {

    @Override
    public TransactionManager getTransactionManager(String docCategory, String docType, String docFormat) {
        TransactionManager transactionManager = new JcrTransactionManager();
        return transactionManager;
    }

    @Override
    public DocumentManager getDocumentManager(String docCategory, String docType, String docFormat) {
        DocumentManager documentManager = JcrDocumentManagerFactory.getInstance().getDocumentManager(docCategory, docType, docFormat);
        return documentManager;
    }

}
