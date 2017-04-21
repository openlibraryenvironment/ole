package org.kuali.ole.oleng.handler;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.OleNGBatchExportResponse;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.dao.export.ExportDao;
import org.kuali.ole.oleng.util.BatchExportUtil;
import org.kuali.ole.sys.context.SpringContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by rajeshbabuk on 4/20/16.
 */
@Service("batchExportHandler")
public class BatchExportHandler extends BatchExportUtil {

    private ExportDao exportDao = (ExportDao) SpringContext.getBean("exportDao");

    public void processExport(BatchProcessTxObject batchProcessTxObject, OleNGBatchExportResponse oleNGBatchExportResponse) {
        try {
            String exportScope = batchProcessTxObject.getBatchProcessProfile().getExportScope();
            if (StringUtils.isNotBlank(exportScope)) {
                switch (exportScope) {
                    case OleNGConstants.FULL_EXPORT:
                        processFullExport(batchProcessTxObject, oleNGBatchExportResponse);
                        break;
                    case OleNGConstants.FULL_EXCEPT_STAFF_ONLY:
                        processFullExceptStaffOnly(batchProcessTxObject, oleNGBatchExportResponse);
                        break;
                    case OleNGConstants.INCREMENTAL:
                        processIncremental(batchProcessTxObject, oleNGBatchExportResponse);
                        break;
                    case OleNGConstants.INCREMENTAL_EXCEPT_STAFF_ONLY:
                        processIncrementalExceptStaffOnly(batchProcessTxObject, oleNGBatchExportResponse);
                        break;
                    case OleNGConstants.FILTER:
                        processFilterExport(batchProcessTxObject, oleNGBatchExportResponse);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            addBatchExportFailureResponseToExchange(e, null, batchProcessTxObject.getExchangeObjectForBatchExport());
        }
    }

    private void processFullExport(BatchProcessTxObject batchProcessTxObject, OleNGBatchExportResponse oleNGBatchExportResponse) {
        String query = "SELECT BIB_ID FROM OLE_DS_BIB_T";
        exportDao.export(this, query, batchProcessTxObject, oleNGBatchExportResponse, false);
    }

    private void processFullExceptStaffOnly(BatchProcessTxObject batchProcessTxObject, OleNGBatchExportResponse oleNGBatchExportResponse) {
        String query = "SELECT BIB_ID FROM OLE_DS_BIB_T WHERE STAFF_ONLY='N'";
        exportDao.export(this, query, batchProcessTxObject, oleNGBatchExportResponse, false);
    }

    private void processIncremental(BatchProcessTxObject batchProcessTxObject, OleNGBatchExportResponse oleNGBatchExportResponse) {
        Date lastExportDate = getLastExportDateForProfile(batchProcessTxObject);
        String query = getIncrementalSolrQuery(lastExportDate);
        exportDao.export(this, query, batchProcessTxObject, oleNGBatchExportResponse, true);
        processDeletedAndStaffOnlyBibs(lastExportDate, batchProcessTxObject);
    }

    private void processIncrementalExceptStaffOnly(BatchProcessTxObject batchProcessTxObject, OleNGBatchExportResponse oleNGBatchExportResponse) {
        Date lastExportDateForProfile = getLastExportDateForProfile(batchProcessTxObject);
        String query = getIncrementalExceptStaffOnlySolrQuery(lastExportDateForProfile);
        exportDao.export(this, query, batchProcessTxObject, oleNGBatchExportResponse, true);
        processDeletedAndStaffOnlyBibs(lastExportDateForProfile, batchProcessTxObject);
    }

    private void processFilterExport(BatchProcessTxObject batchProcessTxObject, OleNGBatchExportResponse oleNGBatchExportResponse) {
        List<String> queryList = getFilterSolrQuery(batchProcessTxObject, oleNGBatchExportResponse);
        for(String query : queryList) {
            exportDao.export(this, query, batchProcessTxObject, oleNGBatchExportResponse, false);
        }
    }

}
