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
    Map<String, String> itemdetails;
    private JdbcTemplate jdbcTemplate;
    Map<String, String> callNumberType;
    private PlatformTransactionManager transactionManager;

    public ItemCallNumberProcessor(Map<String, String> itemdetails, JdbcTemplate jdbcTemplate, Map<String, String> callNumberType) {
        this.itemdetails = itemdetails;
        this.jdbcTemplate = jdbcTemplate;
        this.callNumberType = callNumberType;
    }

    @Override
    public Object call() throws Exception {
        final Map<String, String> localItemdetails = this.itemdetails;
        final TransactionTemplate template = new TransactionTemplate(getTransactionManager());
        try {
            template.execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    StringBuilder itemQuery = new StringBuilder("UPDATE OLE_DS_ITEM_T SET SHELVING_ORDER = ");
                    String callNumberTypeId = localItemdetails.get("callNumberTypeId");
                    String callNumber = localItemdetails.get("callNumber");
                    String itemId = localItemdetails.get("itemId");
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
                    return localItemdetails;

                }
            });
        } catch (Exception ex) {
            throw ex;
        } finally {
            itemdetails.clear(); ;
            this.transactionManager = null;

        }
        return localItemdetails;
    }

    public PlatformTransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = GlobalResourceLoader.getService("transactionManager");
        }
        return this.transactionManager;
    }
}