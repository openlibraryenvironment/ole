package org.kuali.ole.batch.impl;

import org.kuali.ole.docstore.common.client.DocstoreClient;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by jayabharathreddy on 1/4/16.
 */
public class BatchprocessDelete implements Callable {
    private static final Logger LOG = LoggerFactory.getLogger(BatchprocessDelete.class);
    List<String> tempBibIdList = new ArrayList<>();
    DocstoreClient docstoreClient = null;
    private PlatformTransactionManager transactionManager;

    public BatchprocessDelete(List<String> tempBibIdList, DocstoreClient docstoreClient) {
        this.tempBibIdList = tempBibIdList;
        this.docstoreClient = docstoreClient;

    }

    @Override
    public Object call() throws Exception {
        final List<String> locaTempBibIdList = this.tempBibIdList;
        final DocstoreClient locaDocstoreClient = this.docstoreClient;
        final TransactionTemplate template = new TransactionTemplate(getTransactionManager());

        try {
            template.execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    locaDocstoreClient.deleteBibs(tempBibIdList);
                    return locaTempBibIdList;
                }
            });
        } catch (Exception ex) {
            throw ex;
        } finally {
            this.transactionManager = null;

        }
        return locaTempBibIdList;
    }

    public PlatformTransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = GlobalResourceLoader.getService("transactionManager");
        }
        return this.transactionManager;
    }
}