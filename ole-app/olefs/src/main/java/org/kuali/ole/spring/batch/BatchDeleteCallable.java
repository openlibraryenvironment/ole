package org.kuali.ole.spring.batch;

import org.codehaus.jettison.json.JSONArray;
import org.kuali.ole.utility.OleDsNgRestClient;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.concurrent.Callable;

/**
 * Created by rajeshbabuk on 4/6/16.
 */
public class BatchDeleteCallable implements Callable {
    private static final Logger LOG = LoggerFactory.getLogger(BatchDeleteCallable.class);
    OleDsNgRestClient oleDsNgRestClient = null;
    JSONArray jsonArray = null;
    private PlatformTransactionManager transactionManager;

    public BatchDeleteCallable(JSONArray jsonArray, OleDsNgRestClient oleDsNgRestClient) {
        this.oleDsNgRestClient = oleDsNgRestClient;
        this.jsonArray = jsonArray;
    }

    @Override
    public Object call() throws Exception {
        final OleDsNgRestClient oleDsNgRestClient = this.oleDsNgRestClient;
        final JSONArray jsonArray = this.jsonArray;
        final TransactionTemplate template = new TransactionTemplate(getTransactionManager());
        final String[] responseString = new String[1];

        try {
            template.execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    responseString[0] = oleDsNgRestClient.postData(OleDsNgRestClient.Service.PROCESS_DELETE_BIBS, jsonArray, OleDsNgRestClient.Format.JSON);
                    return responseString[0];
                }
            });
        } catch (Exception ex) {
            throw ex;
        } finally {
            this.transactionManager = null;
        }
        return responseString[0];
    }

    public PlatformTransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = GlobalResourceLoader.getService("transactionManager");
        }
        return this.transactionManager;
    }

}