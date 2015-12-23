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
    private SqlRowSet holdingsCallNumberResultSet;
    private JdbcTemplate jdbcTemplate;
    Map<String, String> callNumberType;
    private PlatformTransactionManager transactionManager;

    public HoldingsCallNumberProcessor(SqlRowSet holdingsCallNumberResultSet, JdbcTemplate jdbcTemplate, Map<String, String> callNumberType) {
        this.holdingsCallNumberResultSet = holdingsCallNumberResultSet;
        this.jdbcTemplate = jdbcTemplate;
        this.callNumberType = callNumberType;
    }

    @Override
    public Object call() throws Exception {
        final SqlRowSet localholdingsCallNumberResultSet = this.holdingsCallNumberResultSet;
        final TransactionTemplate template = new TransactionTemplate(getTransactionManager());
        try {
            template.execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    StringBuilder holdingsQuery = new StringBuilder("UPDATE OLE_DS_HOLDINGS_T SET SHELVING_ORDER = ");
                    String callNumberTypeId = holdingsCallNumberResultSet.getString("CALL_NUMBER_TYPE_ID");
                    String callNumber = holdingsCallNumberResultSet.getString("CALL_NUMBER");
                    int holdingsId = holdingsCallNumberResultSet.getInt("HOLDINGS_ID");
                    if (StringUtils.isNotEmpty(callNumberTypeId) && StringUtils.isNotEmpty(callNumber)) {
                        String callNumberCode = callNumberType.get(callNumberTypeId);
                        holdingsQuery.append("'" + CallNumberMigrationDao.getShelfKey(callNumber, callNumberCode) + "'  WHERE  HOLDINGS_ID = '");
                        holdingsQuery.append(holdingsId + "'");
                        try {
                            jdbcTemplate.update(holdingsQuery.toString());
                        } catch (Exception e1) {
                            LOG.error("Exception while updating into OLE_DS_HOLDINGS_T, Holdings Id = " + holdingsId + " callNumber = " + callNumber + " : ", e1);
                        }
                    }
                    return localholdingsCallNumberResultSet;

                }
            });
        } catch (Exception ex) {
            throw ex;
        } finally {
            holdingsCallNumberResultSet = null;
            this.transactionManager = null;

        }
        return localholdingsCallNumberResultSet;
    }

    public PlatformTransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = GlobalResourceLoader.getService("transactionManager");
        }
        return this.transactionManager;
    }
}