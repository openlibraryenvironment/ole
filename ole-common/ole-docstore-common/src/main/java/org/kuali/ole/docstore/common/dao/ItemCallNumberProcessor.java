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

public class ItemCallNumberProcessor implements Callable {
    private static final Logger LOG = LoggerFactory.getLogger(ItemCallNumberProcessor.class);
    private SqlRowSet itemCallNumberResultSet;
    private JdbcTemplate jdbcTemplate;
    Map<String, String> callNumberType;
    private PlatformTransactionManager transactionManager;

    public ItemCallNumberProcessor(SqlRowSet itemCallNumberResultSet, JdbcTemplate jdbcTemplate, Map<String, String> callNumberType) {
        this.itemCallNumberResultSet = itemCallNumberResultSet;
        this.jdbcTemplate = jdbcTemplate;
        this.callNumberType = callNumberType;
    }

    @Override
    public Object call() throws Exception {
        final SqlRowSet locaItemCallNumberResultSet = this.itemCallNumberResultSet;
        final TransactionTemplate template = new TransactionTemplate(getTransactionManager());
        try {
            template.execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    StringBuilder itemQuery = new StringBuilder("UPDATE OLE_DS_ITEM_T SET SHELVING_ORDER = ");
                    String callNumberTypeId = itemCallNumberResultSet.getString("CALL_NUMBER_TYPE_ID");
                    String callNumber = itemCallNumberResultSet.getString("CALL_NUMBER");
                    int itemId = itemCallNumberResultSet.getInt("ITEM_ID");
                    if (StringUtils.isNotEmpty(callNumberTypeId) && StringUtils.isNotEmpty(callNumber)) {
                        String callNumberCode = callNumberType.get(callNumberTypeId);
                        itemQuery.append("'" + CallNumberMigrationDao.getShelfKey(callNumber, callNumberCode) + "'  WHERE  ITEM_ID = '");
                        itemQuery.append(itemId + "'");
                        try {
                            jdbcTemplate.update(itemQuery.toString());
                        } catch (Exception e1) {
                            LOG.error("Exception while updating into OLE_DS_ITEM_T, Item Id = " + itemId + " callNumber = " + callNumber + " : ", e1);
                        }
                    }
                    return locaItemCallNumberResultSet;

                }
            });
        } catch (Exception ex) {
            throw ex;
        } finally {
            itemCallNumberResultSet = null;
            this.transactionManager = null;

        }
        return locaItemCallNumberResultSet;
    }

    public PlatformTransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = GlobalResourceLoader.getService("transactionManager");
        }
        return this.transactionManager;
    }
}