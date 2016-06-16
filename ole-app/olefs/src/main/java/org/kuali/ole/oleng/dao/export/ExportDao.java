package org.kuali.ole.oleng.dao.export;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.OleNGBatchExportResponse;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.handler.BatchExportHandler;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by rajeshbabuk on 4/25/16.
 */

public class ExportDao extends PlatformAwareDaoBaseJdbc {
    private static final Logger LOG = LoggerFactory.getLogger(ExportDao.class);
    private int start = 0;
    private long totalCount;
    private int chunkSize = 1000;
    public static Map<String, String> callNumberType = new HashMap<>();
    public static Map<String, String> receiptStatus = new HashMap<>();
    public static Map<String, String> authenticationType = new HashMap<>();
    public static Map<String, String> itemTypeMap = new HashMap<>();
    public static Map<String, String> itemStatusMap = new HashMap<>();
    public static Map<String, String> statisticalSearchCodeMap = new HashMap<>();
    public static Map<String, String> extentOfOwnershipTypeMap = new HashMap<>();

    public static Map<String, Map<String, String>> commonFields = new HashMap<>();

    private void init() throws Exception {
        start = 0;
        fetchCallNumberType();
        fetchReceiptStatus();
        fetchAuthenticationType();
        fetchItemType();
        fetchItemStatus();
        fetchStatisticalSearchCode();
        fetchExtentOfOwnershipType();
    }

    public void export(BatchExportHandler batchExportHandler, String query, BatchProcessTxObject batchProcessTxObject, OleNGBatchExportResponse oleNGBatchExportResponse) {
        try {
            init();
            this.chunkSize = batchExportHandler.getBatchChunkSize();
            List<Future> futures = new ArrayList<>();
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            int fileCount = 1;
            int fileSize = batchProcessTxObject.getBatchJobDetails().getNumOfRecordsInFile();
            int numOfRecordsInFile = 0;
            SolrDocumentList solrDocumentList = batchExportHandler.getSolrRequestReponseHandler().getSolrDocumentList(query, start, chunkSize, OleNGConstants.BIB_IDENTIFIER);
            totalCount = solrDocumentList.getNumFound();
            batchProcessTxObject.getBatchJobDetails().setTotalRecords(String.valueOf(totalCount));
            oleNGBatchExportResponse.setTotalNumberOfRecords((int) totalCount);
            batchExportHandler.updateBatchJob(batchProcessTxObject.getBatchJobDetails());
            do {
                futures.add(executorService.submit(new ExportDaoCallableImpl(commonFields, getJdbcTemplate(), start, chunkSize, query, fileCount, batchExportHandler, batchProcessTxObject, oleNGBatchExportResponse)));
                prepareBatchExportResponse(futures, batchExportHandler, batchProcessTxObject, oleNGBatchExportResponse);
                numOfRecordsInFile += chunkSize;
                if (numOfRecordsInFile == fileSize) {
                    fileCount++;
                    numOfRecordsInFile = 0;
                }
                start += chunkSize;
            } while (start <= totalCount);

            for (Iterator<Future> iterator = futures.iterator(); iterator.hasNext(); ) {
                Future future = iterator.next();
                try {
                    future.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            batchExportHandler.addBatchExportFailureResponseToExchange(e, null, batchProcessTxObject.getExchangeObjectForBatchExport());
        }
    }

    private void prepareBatchExportResponse(List<Future> futures, BatchExportHandler batchExportHandler, BatchProcessTxObject batchProcessTxObject, OleNGBatchExportResponse oleNGBatchExportResponse) {
        if (CollectionUtils.isNotEmpty(futures)) {
            for (Future future : futures) {
                try {
                    if (null != future.get()) {
                        OleNGBatchExportResponse exportResponse = (OleNGBatchExportResponse) future.get();
                        batchProcessTxObject.getBatchJobDetails().setTotalFailureRecords(String.valueOf(exportResponse.getNoOfFailureRecords()));
                        batchProcessTxObject.getBatchJobDetails().setTotalRecordsProcessed(String.valueOf(exportResponse.getNoOfSuccessRecords() + exportResponse.getNoOfFailureRecords()));
                        batchExportHandler.updateBatchJob(batchProcessTxObject.getBatchJobDetails());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void fetchCallNumberType() throws SQLException {
        SqlRowSet resultSet = getJdbcTemplate().queryForRowSet("SELECT SHVLG_SCHM_ID,SHVLG_SCHM_CD,SHVLG_SCHM_NM from OLE_CAT_SHVLG_SCHM_T");
        while (resultSet.next()) {
            callNumberType.put(resultSet.getString("SHVLG_SCHM_ID"), resultSet.getString("SHVLG_SCHM_CD") + "|" + resultSet.getString("SHVLG_SCHM_NM"));
        }
        commonFields.put("callNumberType", callNumberType);
    }

    private void fetchReceiptStatus() throws SQLException {
        SqlRowSet resultSet = getJdbcTemplate().queryForRowSet("SELECT RCPT_STAT_CD,RCPT_STAT_NM from OLE_CAT_RCPT_STAT_T");
        while (resultSet.next()) {
            receiptStatus.put(resultSet.getString("RCPT_STAT_CD"), resultSet.getString("RCPT_STAT_NM"));
        }
        commonFields.put("receiptStatus", receiptStatus);
    }

    private void fetchAuthenticationType() throws SQLException {
        SqlRowSet resultSet = getJdbcTemplate().queryForRowSet("SELECT CODE,NAME from OLE_DS_AUTHENTICATION_TYPE_T");
        while (resultSet.next()) {
            authenticationType.put(resultSet.getString("CODE"), resultSet.getString("NAME"));
        }
        commonFields.put("authenticationType", authenticationType);
    }

    private void fetchItemType() throws SQLException {
        SqlRowSet resultSet = getJdbcTemplate().queryForRowSet("SELECT ITM_TYP_CD_ID,ITM_TYP_CD,ITM_TYP_NM from OLE_CAT_ITM_TYP_T");
        while (resultSet.next()) {
            itemTypeMap.put(resultSet.getString("ITM_TYP_CD_ID"), resultSet.getString("ITM_TYP_CD") + "|" + resultSet.getString("ITM_TYP_NM"));
        }
        commonFields.put("itemTypeMap", itemTypeMap);
    }

    private void fetchItemStatus() throws SQLException {
        SqlRowSet resultSet = getJdbcTemplate().queryForRowSet("SELECT ITEM_AVAIL_STAT_ID,ITEM_AVAIL_STAT_CD,ITEM_AVAIL_STAT_NM from OLE_DLVR_ITEM_AVAIL_STAT_T");
        while (resultSet.next()) {
            itemStatusMap.put(resultSet.getString("ITEM_AVAIL_STAT_ID"), resultSet.getString("ITEM_AVAIL_STAT_CD") + "|" + resultSet.getString("ITEM_AVAIL_STAT_NM"));
        }
        commonFields.put("itemStatusMap", itemStatusMap);
    }

    private void fetchStatisticalSearchCode() throws SQLException {
        SqlRowSet resultSet = getJdbcTemplate().queryForRowSet("SELECT STAT_SRCH_CD_ID, STAT_SRCH_CD, STAT_SRCH_NM from OLE_CAT_STAT_SRCH_CD_T");
        while (resultSet.next()) {
            statisticalSearchCodeMap.put(resultSet.getString("STAT_SRCH_CD_ID"), resultSet.getString("STAT_SRCH_CD") + "|" + resultSet.getString("STAT_SRCH_NM"));
        }
        commonFields.put("statisticalSearchCodeMap", statisticalSearchCodeMap);
    }

    private void fetchExtentOfOwnershipType() throws SQLException {
        SqlRowSet resultSet = getJdbcTemplate().queryForRowSet("SELECT TYPE_OWNERSHIP_ID, TYPE_OWNERSHIP_CD,TYPE_OWNERSHIP_NM  from OLE_CAT_TYPE_OWNERSHIP_T");
        while (resultSet.next()) {
            extentOfOwnershipTypeMap.put(resultSet.getString("TYPE_OWNERSHIP_ID"), resultSet.getString("TYPE_OWNERSHIP_CD") + "|" + resultSet.getString("TYPE_OWNERSHIP_NM"));
        }
        commonFields.put("extentOfOwnershipTypeMap", extentOfOwnershipTypeMap);
    }


}
