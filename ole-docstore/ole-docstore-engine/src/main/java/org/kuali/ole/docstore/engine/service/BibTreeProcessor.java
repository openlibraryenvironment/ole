package org.kuali.ole.docstore.engine.service;

import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.engine.service.storage.DocstoreRDBMSStorageService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by pvsubrah on 5/26/15.
 */
public class BibTreeProcessor implements Callable {
    private BibTree bibTree;
    private PlatformTransactionManager transactionManager;

    public BibTreeProcessor(BibTree bibTree) {
        this.bibTree = bibTree;
    }

    @Override
    public Object call() throws Exception {
        final BibTree localBibTree = this.bibTree;
        final TransactionTemplate template = new TransactionTemplate(getTransactionManager());
        try {
            template.execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    new DocstoreRDBMSStorageService().processBibTree(localBibTree);
                    return localBibTree;

                }
            });
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof IOException) {
                throw (IOException) ex.getCause();
            } else if (ex.getCause() instanceof ServletException) {
                throw (ServletException) ex.getCause();
            }
            throw ex;
        } finally {
            bibTree = null;
            this.transactionManager = null;

        }
        return localBibTree;
    }

    public PlatformTransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = GlobalResourceLoader.getService("transactionManager");
        }
        return this.transactionManager;
    }
}
