package org.kuali.ole.docstore.engine.service.storage.rdbms.dao;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.common.util.CallNumberUtil;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by jayabharathreddy on 12/23/15.
 */

public class ItemCallNumberProcessor implements Callable {
    private static final Logger LOG = LoggerFactory.getLogger(ItemCallNumberProcessor.class);

    Map<String, String> callNumberType;
    private PlatformTransactionManager transactionManager;
    private String itemCallNumberQuery = null;
    JdbcTemplate jdbcTemplate;

    public ItemCallNumberProcessor(int start, int end, Map<String, String> callNumberType, JdbcTemplate jdbcTemplate) {
        this.itemCallNumberQuery = "SELECT ITEM_ID,CALL_NUMBER_TYPE_ID,CALL_NUMBER  FROM OLE_DS_ITEM_T WHERE ITEM_ID   BETWEEN " + start + "  AND " + end +" AND CALL_NUMBER  !='null' AND  CALL_NUMBER  !='' ORDER BY ITEM_ID " ;
        this.callNumberType = callNumberType;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Object call() throws Exception {
        final TransactionTemplate template = new TransactionTemplate(getTransactionManager());
        final SqlRowSet itemCallNumberResultSet = this.jdbcTemplate.queryForRowSet(itemCallNumberQuery);
        try {
            template.execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    List<String> itemQuerylist = new ArrayList<>();
                    while (itemCallNumberResultSet.next()) {
                        String callNumberTypeId = itemCallNumberResultSet.getString("CALL_NUMBER_TYPE_ID");
                        String callNumber = itemCallNumberResultSet.getString("CALL_NUMBER");
                        String itemId = String.valueOf(itemCallNumberResultSet.getInt("ITEM_ID"));
                        if (StringUtils.isNotEmpty(callNumberTypeId) && StringUtils.isNotEmpty(callNumber)) {
                            StringBuilder ItemQuery = new StringBuilder("UPDATE OLE_DS_ITEM_T SET SHELVING_ORDER = ");
                            String callNumberCode = callNumberType.get(callNumberTypeId);
                            ItemQuery.append("'" + CallNumberUtil.getShelfKey(callNumber, callNumberCode) + "'  WHERE  ITEM_ID = '");
                            ItemQuery.append(itemId + "'");
                            itemQuerylist.add(ItemQuery.toString());
                        }
                    }
                    if(itemQuerylist.toArray(new String[itemQuerylist.size()]).length > 0){
                        jdbcTemplate.batchUpdate(itemQuerylist.toArray(new String[itemQuerylist.size()]));
                    }
                    itemQuerylist.clear();
                    stopWatch.stop();

                    return null;
                }
            });
        } catch (Exception ex) {
            throw ex;
        } finally {
            this.transactionManager = null;

        }
        return null;
    }

    public PlatformTransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = GlobalResourceLoader.getService("transactionManager");
        }
        return this.transactionManager;
    }
}