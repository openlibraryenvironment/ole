package org.kuali.ole.oleng.dao.export;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrDocumentList;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.deliver.service.ParameterValueResolver;
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

    public void export(BatchExportHandler batchExportHandler, String query, BatchProcessTxObject batchProcessTxObject,
                       OleNGBatchExportResponse oleNGBatchExportResponse, boolean isIncremental) {
        try {
            init();
            this.chunkSize = batchExportHandler.getBatchChunkSize();
            List<Future> futures = new ArrayList<>();
            ExecutorService executorService = Executors.newFixedThreadPool(getMaximumNumberOfThreadForExportService());
            int fileCount = 1;
            int fileSize = batchProcessTxObject.getBatchJobDetails().getNumOfRecordsInFile();
            int numOfRecordsInFile = 0;
            SolrDocumentList solrDocumentList = batchExportHandler.getSolrRequestReponseHandler().getSolrDocumentList(
                    query, 0, batchProcessTxObject.getBatchJobDetails().getTotalRecords()!=null ? Integer.parseInt(batchProcessTxObject.getBatchJobDetails().getTotalRecords()) : null, OleNGConstants.BIB_IDENTIFIER);
            totalCount = solrDocumentList.getNumFound();

            List<String> bibIds = new ArrayList<>();

            if(isIncremental) {
                bibIds = getBibIds(query, totalCount, chunkSize, batchExportHandler, batchProcessTxObject);
                totalCount = bibIds.size();
                if(fileSize < chunkSize) {
                    chunkSize = fileSize;
                }
            }

            batchProcessTxObject.getBatchJobDetails().setTotalRecords(String.valueOf(totalCount));
            oleNGBatchExportResponse.setTotalNumberOfRecords((int) totalCount);
            batchExportHandler.updateBatchJob(batchProcessTxObject.getBatchJobDetails());

            if(isIncremental) {
                List<List<String>> partition = Lists.partition(bibIds, chunkSize);
                for (Iterator<List<String>> iterator = partition.iterator(); iterator.hasNext(); ) {
                    List<String> bibIdLists = iterator.next();
                    futures.add(executorService.submit(new IncrementalExportCallableImpl(commonFields, getJdbcTemplate(),bibIdLists,
                            fileCount, batchExportHandler, batchProcessTxObject)));
                    numOfRecordsInFile += chunkSize;
                    if (numOfRecordsInFile >= fileSize) {
                        fileCount++;
                        numOfRecordsInFile = 0;
                    }
                }
            } else {
                do {
                    futures.add(executorService.submit(new ExportDaoCallableImpl(commonFields, getJdbcTemplate(), query,
                            start, chunkSize, fileCount, batchExportHandler, batchProcessTxObject)));
                    numOfRecordsInFile += chunkSize;
                    if (numOfRecordsInFile == fileSize) {
                        fileCount++;
                        numOfRecordsInFile = 0;
                    }
                    start += chunkSize;
                } while (start <= totalCount);
            }
            prepareBatchExportResponse(futures, batchExportHandler, batchProcessTxObject, oleNGBatchExportResponse);
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            batchExportHandler.addBatchExportFailureResponseToExchange(e, null, batchProcessTxObject.getExchangeObjectForBatchExport());
        }
    }

    private List<String> getBibIds(String query, long totalCount, int chunkSize, BatchExportHandler batchExportHandler, BatchProcessTxObject batchProcessTxObject) {
        Set<String> bibIdentifiers = new HashSet<>();
        List<Future> futures = new ArrayList<>();
        int startIndex = 0;
        ExecutorService executorService = Executors.newFixedThreadPool(getMaximumNumberOfThreadForExportService());
        do {
            futures.add(executorService.submit(new ExportBibIdFinderCallable(query,startIndex, chunkSize, batchExportHandler)));
            startIndex += chunkSize;
        } while (startIndex <= totalCount);

        for (Future future : futures) {
            try {
                Object response = future.get();
                if (null != response) {
                    Set<String> bibIds = (Set<String>) response;
                    bibIdentifiers.addAll(bibIds);
                }
            } catch (Exception e) {
                e.printStackTrace();
                batchExportHandler.addBatchExportFailureResponseToExchange(e, null, batchProcessTxObject.getExchangeObjectForBatchExport());
            }
        }
        return new ArrayList<String>(bibIdentifiers);
    }

    private void prepareBatchExportResponse(List<Future> futures, BatchExportHandler batchExportHandler, BatchProcessTxObject batchProcessTxObject, OleNGBatchExportResponse originalResponse) {
        if (CollectionUtils.isNotEmpty(futures)) {
            for (Future future : futures) {
                try {
                    Object response = future.get();
                    if (null != response) {
                        OleNGBatchExportResponse exportResponse = (OleNGBatchExportResponse) response;
                        mergeResponses(originalResponse, exportResponse);
                        batchProcessTxObject.getBatchJobDetails().setTotalFailureRecords(String.valueOf(originalResponse.getNoOfFailureRecords()));
                        batchProcessTxObject.getBatchJobDetails().setTotalRecordsProcessed(String.valueOf(originalResponse.getNoOfSuccessRecords() + originalResponse.getNoOfFailureRecords()));
                        batchExportHandler.updateBatchJob(batchProcessTxObject.getBatchJobDetails());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    batchExportHandler.addBatchExportFailureResponseToExchange(e, null, batchProcessTxObject.getExchangeObjectForBatchExport());
                }
            }
        }
    }

    private void mergeResponses(OleNGBatchExportResponse originalResponse, OleNGBatchExportResponse exportResponse) {
        originalResponse.addNoOfSuccessRecords(exportResponse.getNoOfSuccessRecords());
        originalResponse.addNoOfFailureRecords(exportResponse.getNoOfFailureRecords());
        originalResponse.getBatchExportSuccessResponseList().addAll(exportResponse.getBatchExportSuccessResponseList());
        originalResponse.getBatchExportFailureResponseList().addAll(exportResponse.getBatchExportFailureResponseList());
        originalResponse.getDeletedBibIds().addAll(exportResponse.getDeletedBibIds());
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

    public int getMaximumNumberOfThreadForExportService() {
        String maxNumberOfThreadFromParameter = ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants.DESCRIBE_COMPONENT, OleNGConstants.MAX_NO_OF_THREAD_FOR_EXPORT_SERVICE);
        int maxNumberOfThread = 10;
        if(StringUtils.isNotBlank(maxNumberOfThreadFromParameter)){
            try{
                int maxNumberOfThreadFromParameterInterger = Integer.parseInt(maxNumberOfThreadFromParameter);
                if(maxNumberOfThreadFromParameterInterger > 0){
                    maxNumberOfThread = maxNumberOfThreadFromParameterInterger;
                }else{
                    LOG.info("Invalid max number of thread for export service from system parameter. So taking the default max number of thread : " + maxNumberOfThread);
                }
            }catch(Exception exception){
                LOG.info("Invalid max number of thread for export service from system parameter. So taking the default max number of thread : " + maxNumberOfThread);
            }
        }else{
            LOG.info("Invalid max number of thread for export service from system parameter. So taking the default max number of thread : " + maxNumberOfThread);
        }
        return maxNumberOfThread;
    }


}
