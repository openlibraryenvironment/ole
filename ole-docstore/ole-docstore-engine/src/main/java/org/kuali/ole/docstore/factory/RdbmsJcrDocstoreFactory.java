package org.kuali.ole.docstore.factory;

import org.kuali.ole.docstore.document.DocumentManager;
import org.kuali.ole.docstore.document.jcr.JcrDocumentManagerFactory;
import org.kuali.ole.docstore.document.rdbms.RdbmsDocumentManagerFactory;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.transaction.JcrTransactionManager;
import org.kuali.ole.docstore.transaction.RdbmsTransactionManager;
import org.kuali.ole.docstore.transaction.TransactionManager;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 6/20/13
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class RdbmsJcrDocstoreFactory extends AbstractDocstoreFactory {

    @Override
    public TransactionManager getTransactionManager(String docCategory, String docType, String docFormat) {

        TransactionManager transactionManager = null;
        if (isToStoreInRdbms(docCategory, docType, docFormat)) {
            transactionManager = new RdbmsTransactionManager();
        } else {
            transactionManager = new JcrTransactionManager();
        }

        return transactionManager;
    }

    @Override
    public DocumentManager getDocumentManager(String docCategory, String docType, String docFormat) {

        DocumentManager documentManager = null;
        if (isToStoreInRdbms(docCategory, docType, docFormat)) {
            documentManager = RdbmsDocumentManagerFactory.getInstance().getDocumentManager(docCategory, docType, docFormat);
        } else {
            documentManager = JcrDocumentManagerFactory.getInstance().getDocumentManager(docCategory, docType, docFormat);
        }

        return documentManager;
    }

    private boolean isToStoreInRdbms(String docCategory, String docType, String docFormat) {

        if (DocCategory.WORK.getCode().equals(docCategory)) {
            if (DocType.BIB.getCode().equals(docType) || DocType.INSTANCE.getCode().equals(docType) ||
                    DocType.HOLDINGS.getCode().equals(docType) || DocType.ITEM.getCode().equals(docType) || DocType.EINSTANCE.getCode().equals(docType) || DocType.EHOLDINGS.getCode().equals(docType)) {
                return true;
            }
        }
        return false;
    }

}
