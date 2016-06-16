package org.kuali.ole.docstore.engine.service.storage.rdbms.dao;

import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
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
    private int holdingsTotalCount = 0;
    private int itemTotalCount = 0;
    private int chunkSize = 1000;

    public void init() throws Exception {
        this.chunkSize = Integer.parseInt(getParameter());
        fetchCallNumberType();
        getHoldingsTotalCount();
        getItemTotalCount();
        retriveAndUpdateHoldingsCallNumber();
        retriveAndUpdateItemCallNumber();
    }

    private void getHoldingsTotalCount() {
        SqlRowSet totalCountSet = getJdbcTemplate().queryForRowSet("SELECT MAX(HOLDINGS_ID) as total FROM OLE_DS_HOLDINGS_T");
        while (totalCountSet.next()) {
            holdingsTotalCount = totalCountSet.getInt("total");
        }
    }


    private void getItemTotalCount() {
        SqlRowSet totalCountSet = getJdbcTemplate().queryForRowSet("SELECT MAX(ITEM_ID)  as total FROM OLE_DS_ITEM_T");
        while (totalCountSet.next()) {
            itemTotalCount = totalCountSet.getInt("total");
        }
    }


    private void fetchCallNumberType() throws Exception {
        SqlRowSet resultSet = getJdbcTemplate().queryForRowSet("SELECT SHVLG_SCHM_ID,SHVLG_SCHM_CD from OLE_CAT_SHVLG_SCHM_T");
        while (resultSet.next()) {
            callNumberType.put(resultSet.getString("SHVLG_SCHM_ID"), resultSet.getString("SHVLG_SCHM_CD"));
        }
    }

    private void retriveAndUpdateHoldingsCallNumber() throws Exception {
        int start = 0;
        int end = 0;
        int tempHoldingsRecord = holdingsTotalCount;
        StopWatch stopWatch = new StopWatch();
        List<Future> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        stopWatch.start();

        while (start < holdingsTotalCount) {
            tempHoldingsRecord = tempHoldingsRecord - chunkSize;
            if (tempHoldingsRecord < chunkSize) {
                end = start + tempHoldingsRecord;
            } else {
                end = start + chunkSize;
            }
            futures.add(executorService.submit(new HoldingsCallNumberProcessor(start, end, callNumberType, getJdbcTemplate())));
            start = end;
        }

        for (Iterator<Future> iterator = futures.iterator(); iterator.hasNext(); ) {
            Future future = iterator.next();
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
        stopWatch.stop();
        LOG.info("Time Taken for " + holdingsTotalCount + "  holdings Call Number Migration :: " + stopWatch.prettyPrint());
    }


    private void retriveAndUpdateItemCallNumber() throws Exception {
        int start = 0;
        int end = 0;
        int tempItemRecord = itemTotalCount;
        StopWatch stopWatch = new StopWatch();
        List<Future> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        stopWatch.start();
        chunkSize = 100000;
        while (start < holdingsTotalCount) {
            tempItemRecord = tempItemRecord - chunkSize;
            if (tempItemRecord < chunkSize) {
                end = start + tempItemRecord;
            } else {
                end = start + chunkSize;
            }
            futures.add(executorService.submit(new ItemCallNumberProcessor(start, end, callNumberType, getJdbcTemplate())));
            start = end;
        }


        for (Iterator<Future> iterator = futures.iterator(); iterator.hasNext(); ) {
            Future future = iterator.next();
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        stopWatch.stop();
        LOG.info("Time Taken for " + itemTotalCount + "  Items Call Number Migration :: " + stopWatch.prettyPrint());
    }


    public String getParameter() {
        ParameterKey parameterKey = ParameterKey.create("OLE", "OLE-DESC", "Describe", "CALL_NUMBER_MIGRATION_CHUNK_SIZE");
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter!=null?parameter.getValue():null;
    }

}