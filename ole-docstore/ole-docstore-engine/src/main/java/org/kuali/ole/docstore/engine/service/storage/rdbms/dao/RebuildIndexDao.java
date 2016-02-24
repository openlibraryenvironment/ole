package org.kuali.ole.docstore.engine.service.storage.rdbms.dao;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.util.BibInfoStatistics;
import org.kuali.ole.docstore.common.util.ReindexBatchStatistics;
import org.kuali.ole.docstore.process.BibHoldingItemReindexer;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by jayabharathreddy on 2/18/16.
 */
public class RebuildIndexDao extends PlatformAwareDaoBaseJdbc {
    private static final Logger LOG = LoggerFactory.getLogger(CallNumberMigrationDao.class);
    private int chunkSize = 1000;
    public static Map<String, String> callNumberType = new HashMap<>();
    public static Map<String, String> receiptStatus = new HashMap<>();
    public static Map<String, String> authenticationType = new HashMap<>();
    public static Map<String, String> itemTypeMap = new HashMap<>();
    public static Map<String, String> itemStatusMap = new HashMap<>();
    public static Map<String, String> statisticalSearchCodeMap = new HashMap<>();
    public static Map<String, String> extentOfOwnershipTypeMap = new HashMap<>();
    private static ReindexBatchStatistics reindexBatchStatistics = new ReindexBatchStatistics();


    public static Map<String, Map<String, String>> commomFields = new HashMap<>();

    private void init() throws Exception {
        fetchCallNumberType();
        fetchReceiptStatus();
        fetchAuthenticationType();
        fetchItemType();
        fetchItemStatus();
        fetchStatisticalSearchCode();
        fetchExtentOfOwnershipType();
    }

    public String indexFromFile(String filePath) throws Exception {
        init();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String bibid = null;
        List<String> bibids = new ArrayList<>();
        while ((bibid = reader.readLine()) != null) {
            if(StringUtils.isNotBlank(bibid)){
                bibids.add(DocumentUniqueIDPrefix.getDocumentId(bibid));
            }
        }
        reindexBatchStatistics.setBibCount(bibids.size());
        Date date = new Date();
        reindexBatchStatistics.setStartTime(date);
        List<List<String>> tempBibIdLists = Lists.partition(bibids, chunkSize);
        List<Future> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (List<String> tempBibIdList : tempBibIdLists) {
            futures.add(executorService.submit(new RebuildIndexBibDao(commomFields, getJdbcTemplate(), tempBibIdList,reindexBatchStatistics)));
        }

        for (Iterator<Future> iterator = futures.iterator(); iterator.hasNext(); ) {
            Future future = iterator.next();
            try {
                ReindexBatchStatistics response = (ReindexBatchStatistics) future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        Date EndDate = new Date();
        reindexBatchStatistics.setEndTime(EndDate);
        return reindexBatchStatistics.toString();
    }


    private void fetchCallNumberType() throws SQLException {
        SqlRowSet resultSet = getJdbcTemplate().queryForRowSet("SELECT SHVLG_SCHM_ID,SHVLG_SCHM_CD,SHVLG_SCHM_NM from OLE_CAT_SHVLG_SCHM_T");
        while (resultSet.next()) {
            callNumberType.put(resultSet.getString("SHVLG_SCHM_ID"), resultSet.getString("SHVLG_SCHM_CD") + "|" + resultSet.getString("SHVLG_SCHM_NM"));
        }
        commomFields.put("callNumberType", callNumberType);
    }

    private void fetchReceiptStatus() throws SQLException {
        SqlRowSet resultSet = getJdbcTemplate().queryForRowSet("SELECT RCPT_STAT_CD,RCPT_STAT_NM from OLE_CAT_RCPT_STAT_T");
        while (resultSet.next()) {
            receiptStatus.put(resultSet.getString("RCPT_STAT_CD"), resultSet.getString("RCPT_STAT_NM"));
        }
        commomFields.put("receiptStatus", receiptStatus);
    }

    private void fetchAuthenticationType() throws SQLException {
        SqlRowSet resultSet = getJdbcTemplate().queryForRowSet("SELECT CODE,NAME from OLE_DS_AUTHENTICATION_TYPE_T");
        while (resultSet.next()) {
            authenticationType.put(resultSet.getString("CODE"), resultSet.getString("NAME"));
        }
        commomFields.put("authenticationType", authenticationType);
    }

    private void fetchItemType() throws SQLException {
        SqlRowSet resultSet = getJdbcTemplate().queryForRowSet("SELECT ITM_TYP_CD_ID,ITM_TYP_CD,ITM_TYP_NM from OLE_CAT_ITM_TYP_T");
        while (resultSet.next()) {
            itemTypeMap.put(resultSet.getString("ITM_TYP_CD_ID"), resultSet.getString("ITM_TYP_CD") + "|" + resultSet.getString("ITM_TYP_NM"));
        }
        commomFields.put("itemTypeMap", itemTypeMap);
    }

    private void fetchItemStatus() throws SQLException {
        SqlRowSet resultSet = getJdbcTemplate().queryForRowSet("SELECT ITEM_AVAIL_STAT_ID,ITEM_AVAIL_STAT_CD,ITEM_AVAIL_STAT_NM from OLE_DLVR_ITEM_AVAIL_STAT_T");
        while (resultSet.next()) {
            itemStatusMap.put(resultSet.getString("ITEM_AVAIL_STAT_ID"), resultSet.getString("ITEM_AVAIL_STAT_CD") + "|" + resultSet.getString("ITEM_AVAIL_STAT_NM"));
        }
        commomFields.put("itemStatusMap", itemStatusMap);
    }

    private void fetchStatisticalSearchCode() throws SQLException {
        SqlRowSet resultSet = getJdbcTemplate().queryForRowSet("SELECT STAT_SRCH_CD_ID, STAT_SRCH_CD, STAT_SRCH_NM from OLE_CAT_STAT_SRCH_CD_T");
        while (resultSet.next()) {
            statisticalSearchCodeMap.put(resultSet.getString("STAT_SRCH_CD_ID"), resultSet.getString("STAT_SRCH_CD") + "|" + resultSet.getString("STAT_SRCH_NM"));
        }
        commomFields.put("statisticalSearchCodeMap", statisticalSearchCodeMap);
    }

    private void fetchExtentOfOwnershipType() throws SQLException {
        SqlRowSet resultSet = getJdbcTemplate().queryForRowSet("SELECT TYPE_OWNERSHIP_ID, TYPE_OWNERSHIP_CD,TYPE_OWNERSHIP_NM  from OLE_CAT_TYPE_OWNERSHIP_T");
        while (resultSet.next()) {
            extentOfOwnershipTypeMap.put(resultSet.getString("TYPE_OWNERSHIP_ID"), resultSet.getString("TYPE_OWNERSHIP_CD") + "|" + resultSet.getString("TYPE_OWNERSHIP_NM"));
        }
        commomFields.put("extentOfOwnershipTypeMap", extentOfOwnershipTypeMap);
    }
}
