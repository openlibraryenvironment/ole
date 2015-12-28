package org.kuali.ole.docstore.common.dao;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by jayabharathreddy on 12/23/15.
 */

public class HoldingsCallNumberProcessor implements Callable {
    private static final Logger LOG = LoggerFactory.getLogger(HoldingsCallNumberProcessor.class);
    Map<String, String> holdingsDetails;
    Map<String, String> callNumberType;
    private PlatformTransactionManager transactionManager;
    StringBuilder holdingsQuery = new StringBuilder("UPDATE OLE_DS_HOLDINGS_T SET SHELVING_ORDER = ");

    public HoldingsCallNumberProcessor(Map<String, String> holdingsDetails, Map<String, String> callNumberType) {
        this.holdingsDetails = holdingsDetails;
        this.callNumberType = callNumberType;
    }

    @Override
    public Object call() throws Exception {
        final  Map<String, String> localHoldingsDetails = this.holdingsDetails;
        final TransactionTemplate template = new TransactionTemplate(getTransactionManager());

        try {
            template.execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    String callNumberTypeId = holdingsDetails.get("callNumberTypeId");
                    String callNumber = holdingsDetails.get("callNumber");
                    String holdingsId = holdingsDetails.get("holdingsId");
                    if (StringUtils.isNotEmpty(callNumberTypeId) && StringUtils.isNotEmpty(callNumber)) {
                        String callNumberCode = callNumberType.get(callNumberTypeId);
                        holdingsQuery.append("'" + CallNumberMigrationDao.getShelfKey(callNumber, callNumberCode) + "'  WHERE  HOLDINGS_ID = '");
                        holdingsQuery.append(holdingsId + "'");
                    }
                    return holdingsQuery.toString();
                }
            });
        } catch (Exception ex) {
            throw ex;
        } finally {
            holdingsDetails.clear();
            this.transactionManager = null;

        }
        return holdingsQuery.toString();
    }

    public PlatformTransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = GlobalResourceLoader.getService("transactionManager");
        }
        return this.transactionManager;
    }
}