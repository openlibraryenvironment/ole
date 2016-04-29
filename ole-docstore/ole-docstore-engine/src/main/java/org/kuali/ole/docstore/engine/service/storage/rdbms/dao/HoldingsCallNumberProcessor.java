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

public class HoldingsCallNumberProcessor implements Callable {
    private static final Logger LOG = LoggerFactory.getLogger(HoldingsCallNumberProcessor.class);

    Map<String, String> callNumberType;
    private PlatformTransactionManager transactionManager;
    private String holdingsCallNumberQuery = null;
    JdbcTemplate jdbcTemplate;

    public HoldingsCallNumberProcessor(int start, int end, Map<String, String> callNumberType, JdbcTemplate jdbcTemplate) {
        this.holdingsCallNumberQuery = "SELECT HOLDINGS_ID,CALL_NUMBER_TYPE_ID,CALL_NUMBER  FROM OLE_DS_HOLDINGS_T WHERE HOLDINGS_ID   BETWEEN " + start + "  AND " + end +" AND CALL_NUMBER  !='null' AND  CALL_NUMBER  !='' ORDER BY HOLDINGS_ID " ;
        this.callNumberType = callNumberType;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Object call() throws Exception {
        final TransactionTemplate template = new TransactionTemplate(getTransactionManager());
        final SqlRowSet holdingsCallNumberResultSet = this.jdbcTemplate.queryForRowSet(holdingsCallNumberQuery);
        try {
            template.execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    List<String> holdingsQuerylist = new ArrayList<>();
                    while (holdingsCallNumberResultSet.next()) {
                        String callNumberTypeId = holdingsCallNumberResultSet.getString("CALL_NUMBER_TYPE_ID");
                        String callNumber = holdingsCallNumberResultSet.getString("CALL_NUMBER");
                        String holdingsId = String.valueOf(holdingsCallNumberResultSet.getInt("HOLDINGS_ID"));
                        if (StringUtils.isNotEmpty(callNumberTypeId) && StringUtils.isNotEmpty(callNumber)) {
                            StringBuilder holdingsQuery = new StringBuilder("UPDATE OLE_DS_HOLDINGS_T SET SHELVING_ORDER = ");
                            String callNumberCode = callNumberType.get(callNumberTypeId);
                            holdingsQuery.append("'" + CallNumberUtil.getShelfKey(callNumber, callNumberCode) + "'  WHERE  HOLDINGS_ID = '");
                            holdingsQuery.append(holdingsId + "'");
                            holdingsQuerylist.add(holdingsQuery.toString());
                        }
                    }

                    if(holdingsQuerylist.toArray(new String[holdingsQuerylist.size()]).length > 0){
                        jdbcTemplate.batchUpdate(holdingsQuerylist.toArray(new String[holdingsQuerylist.size()]));
                    }
                    holdingsQuerylist.clear();
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