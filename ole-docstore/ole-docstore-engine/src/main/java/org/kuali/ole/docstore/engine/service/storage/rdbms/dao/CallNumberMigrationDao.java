package org.kuali.ole.docstore.engine.service.storage.rdbms.dao;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.utility.callnumber.CallNumberFactory;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.util.StopWatch;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by jayabharathreddy on 12/16/15.
 */
public class CallNumberMigrationDao extends PlatformAwareDaoBaseJdbc {

    private static final Logger LOG = LoggerFactory.getLogger(CallNumberMigrationDao.class);
    private static Map<String, String> callNumberType = new HashMap<>();


    private String holdingsCallNumberQuery = "SELECT HOLDINGS_ID,CALL_NUMBER_TYPE_ID,CALL_NUMBER  FROM OLE_DS_HOLDINGS_T Where CALL_NUMBER  !='null' AND  CALL_NUMBER  !='' ORDER BY HOLDINGS_ID";
    private String itemCallNumberQuery = "SELECT ITEM_ID,CALL_NUMBER_TYPE_ID,CALL_NUMBER  FROM OLE_DS_ITEM_T Where CALL_NUMBER  !='null' AND  CALL_NUMBER  !='' ORDER BY ITEM_ID";

    public void init() throws Exception {
        fetchCallNumberType();
        retriveAndUpdateHoldingsCallNumber();
        retriveAndUpdateItemCallNumber();
    }


    private void fetchCallNumberType() throws Exception {
        SqlRowSet resultSet = getJdbcTemplate().queryForRowSet("SELECT SHVLG_SCHM_ID,SHVLG_SCHM_CD from OLE_CAT_SHVLG_SCHM_T");
        while (resultSet.next()) {
            callNumberType.put(resultSet.getString("SHVLG_SCHM_ID"), resultSet.getString("SHVLG_SCHM_CD"));
        }
    }

    private void retriveAndUpdateHoldingsCallNumber() throws Exception {
        SqlRowSet holdingsCallNumberResultSet = getJdbcTemplate().queryForRowSet(holdingsCallNumberQuery);
        calculateAndUpdateHoldingsShelvingOrder(holdingsCallNumberResultSet);
    }


    private void retriveAndUpdateItemCallNumber() throws Exception {
        SqlRowSet itemCallNumberResultSet = getJdbcTemplate().queryForRowSet(itemCallNumberQuery);
        calculateAndUpdateItemShelvingOrder(itemCallNumberResultSet);

    }

    private void calculateAndUpdateHoldingsShelvingOrder(SqlRowSet holdingsCallNumberResultSet) throws Exception {
        StopWatch stopWatch = new StopWatch();
        List<Future> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        stopWatch.start();
        int count = 0;

        while (holdingsCallNumberResultSet.next()) {
            count++;
            Map<String, String> holdingsDetails = new HashMap<>();
            holdingsDetails.put("callNumberTypeId", holdingsCallNumberResultSet.getString("CALL_NUMBER_TYPE_ID"));
            holdingsDetails.put("callNumber", holdingsCallNumberResultSet.getString("CALL_NUMBER"));
            holdingsDetails.put("holdingsId", String.valueOf(holdingsCallNumberResultSet.getInt("HOLDINGS_ID")));
            futures.add(executorService.submit(new HoldingsCallNumberProcessor(holdingsDetails, callNumberType)));
        }
        List<String> batchSqls = new ArrayList<>();
        for (Iterator<Future> iterator = futures.iterator(); iterator.hasNext(); ) {
            Future future = iterator.next();
            try {
                String sql = (String) future.get();
                batchSqls.add(sql);
            } catch (InterruptedException e) {
                LOG.info(e.getMessage());
            } catch (ExecutionException e) {
                LOG.info(e.getMessage());
            }

            if (batchSqls.size() == 1000) {
                String[] arraysqls = batchSqls.toArray(new String[batchSqls.size()]);
                getJdbcTemplate().batchUpdate(arraysqls);
                batchSqls.clear();
            }
        }
        executorService.shutdown();

        if (batchSqls.size() > 0) {
            String[] arraysqls = batchSqls.toArray(new String[batchSqls.size()]);
            getJdbcTemplate().batchUpdate(arraysqls);
        }
        stopWatch.stop();
        LOG.debug("Total Time taken " + count + " - for Holdings ::" + stopWatch.getTotalTimeMillis());
    }

    private void calculateAndUpdateItemShelvingOrder(SqlRowSet itemCallNumberResultSet) throws Exception {
        StopWatch stopWatch = new StopWatch();
        List<Future> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        stopWatch.start();
        int count = 0;
        while (itemCallNumberResultSet.next()) {
            count++;
            Map<String, String> ItemDetails = new HashMap<>();
            ItemDetails.put("callNumberTypeId", itemCallNumberResultSet.getString("CALL_NUMBER_TYPE_ID"));
            ItemDetails.put("callNumber", itemCallNumberResultSet.getString("CALL_NUMBER"));
            ItemDetails.put("itemId", String.valueOf(itemCallNumberResultSet.getInt("ITEM_ID")));
            futures.add(executorService.submit(new ItemCallNumberProcessor(ItemDetails, callNumberType)));
        }
        List<String> batchSqls = new ArrayList<>();
        for (Iterator<Future> iterator = futures.iterator(); iterator.hasNext(); ) {
            Future future = iterator.next();
            try {
                String sql = (String) future.get();
                batchSqls.add(sql);
            } catch (InterruptedException e) {
                LOG.info(e.getMessage());
            } catch (ExecutionException e) {
                LOG.info(e.getMessage());
            }
            if (batchSqls.size() == 1000) {
                String[] arraysqls = batchSqls.toArray(new String[batchSqls.size()]);
                getJdbcTemplate().batchUpdate(arraysqls);
                batchSqls.clear();
            }
        }


        executorService.shutdown();

        if (batchSqls.size() > 0) {
            String[] arraysqls = batchSqls.toArray(new String[batchSqls.size()]);
            getJdbcTemplate().batchUpdate(arraysqls);
        }

        stopWatch.stop();
        LOG.debug("Total Time taken " + count + " - for Item ::" + stopWatch.getTotalTimeMillis());
    }

    public static String getShelfKey(String callNumber, String codeValue) {
        String Shelfkey = null;
        if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
            org.solrmarc.callnum.CallNumber callNumberObj = CallNumberFactory.getInstance().getCallNumber(codeValue);
            if (callNumberObj != null) {
                callNumberObj.parse(callNumber);
                Shelfkey = callNumberObj.getShelfKey();
            }
        }
        return Shelfkey;
    }


}