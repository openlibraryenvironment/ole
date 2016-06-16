package org.kuali.ole.spring.batch.processor;

import org.apache.commons.io.FileUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.docstore.common.response.BatchProcessFailureResponse;
import org.kuali.ole.docstore.common.response.OleNGBatchDeleteResponse;
import org.kuali.ole.docstore.common.response.OleNgBatchResponse;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileMatchPoint;
import org.kuali.ole.oleng.batch.reports.BatchBibFailureReportLogHandler;
import org.kuali.ole.oleng.batch.reports.BatchDeleteReportLogHandler;
import org.kuali.ole.oleng.handler.BatchDeleteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by rajeshbabuk on 4/4/16.
 */
@Service("batchDeleteProcessor")
public class BatchDeleteFileProcessor extends BatchFileProcessor {

    @Autowired
    private BatchDeleteHandler batchDeleteHandler;

    @Override
    public JSONObject processBatch(File inputFile, String fileType, String profileId, String reportDirectoryName, BatchJobDetails batchJobDetails) throws Exception {
        JSONObject jsonResponse = new JSONObject();
        OleNGBatchDeleteResponse oleNGBatchDeleteResponse = new OleNGBatchDeleteResponse();
        BatchProcessProfile batchProcessProfile = null;
        String fileContent = null;
        try {
            oleNGBatchDeleteResponse.setDirectoryName(reportDirectoryName);
            fileContent = FileUtils.readFileToString(new File(inputFile.getAbsolutePath() + File.separator + batchJobDetails.getFileName()));
            batchProcessProfile = fetchBatchProcessProfile(profileId);
            oleNGBatchDeleteResponse.setProfileName(batchProcessProfile.getBatchProcessProfileName());
            BatchProfileMatchPoint batchProfileMatchPoint = getMatchPointFromProfile(batchProcessProfile);
            Set<String> deletableBibIds = batchDeleteHandler.processInputFileAndGetDeletableBibIds(fileContent, fileType, batchProfileMatchPoint, batchJobDetails, oleNGBatchDeleteResponse);
            batchDeleteHandler.performDeletion(deletableBibIds, batchJobDetails, oleNGBatchDeleteResponse);
            generateBatchReport(inputFile, batchJobDetails, oleNGBatchDeleteResponse);
            jsonResponse.put(OleNGConstants.STATUS, true);
        } catch (Exception e) {
            e.printStackTrace();
            generateFailureReport(inputFile, reportDirectoryName, fileContent, profileId, batchJobDetails, batchProcessProfile, e);
            throw e;
        }
        return jsonResponse;
    }

    private void generateBatchReport(File inputFile, BatchJobDetails batchJobDetails, OleNGBatchDeleteResponse oleNGBatchDeleteResponse) {
        oleNGBatchDeleteResponse.setJobDetailId(String.valueOf(batchJobDetails.getJobDetailId()));
        oleNGBatchDeleteResponse.setJobName(String.valueOf(batchJobDetails.getJobName()));
        oleNGBatchDeleteResponse.setNoOfSuccessRecords(oleNGBatchDeleteResponse.getBatchDeleteSuccessResponseList().size());
        oleNGBatchDeleteResponse.setNoOfFailureRecords(oleNGBatchDeleteResponse.getBatchDeleteFailureResponseList().size());

        BatchDeleteReportLogHandler batchDeleteReportLogHandler = BatchDeleteReportLogHandler.getInstance();
        batchDeleteReportLogHandler.logMessage(oleNGBatchDeleteResponse, oleNGBatchDeleteResponse.getDirectoryName());

        batchJobDetails.setTotalRecordsProcessed(String.valueOf(oleNGBatchDeleteResponse.getTotalNumberOfRecords()));
        batchJobDetails.setTotalFailureRecords(String.valueOf(oleNGBatchDeleteResponse.getNoOfFailureRecords()));
        batchJobDetails.setStatus(OleNGConstants.COMPLETED);
        batchJobDetails.setEndTime(new Timestamp(System.currentTimeMillis()));
        updateBatchJob(batchJobDetails);
        writeBatchRunningStatusToFile(inputFile.getAbsolutePath(), OleNGConstants.COMPLETED, batchJobDetails.getTimeSpent());

    }

    private void generateFailureReport(File inputFile, String reportDirectoryName, String rawContent, String profileId, BatchJobDetails batchJobDetails, BatchProcessProfile batchProcessProfile, Exception e) {
        BatchProcessFailureResponse batchProcessFailureResponse = new BatchProcessFailureResponse();
        batchProcessFailureResponse.setBatchProcessProfileName((null != batchProcessProfile ? batchProcessProfile.getBatchProcessProfileName() : OleNGConstants.PROFILEID + profileId));
        batchProcessFailureResponse.setFailureReason(e.toString());
        batchProcessFailureResponse.setDetailedMessage(getDetailedMessage(e));
        batchProcessFailureResponse.setFailedRawMarcContent(rawContent);
        batchProcessFailureResponse.setDirectoryName(String.valueOf(batchJobDetails.getJobDetailId()));

        BatchBibFailureReportLogHandler batchBibFailureReportLogHandler = BatchBibFailureReportLogHandler.getInstance();
        batchBibFailureReportLogHandler.logMessage(batchProcessFailureResponse, reportDirectoryName);

        batchJobDetails.setStatus(OleNGConstants.FAILED);
        batchJobDetails.setEndTime(new Timestamp(System.currentTimeMillis()));
        updateBatchJob(batchJobDetails);
        writeBatchRunningStatusToFile(inputFile.getAbsolutePath(), OleNGConstants.FAILED, batchJobDetails.getTimeSpent());
    }

    @Override
    public OleNgBatchResponse processRecords(Map<Integer, RecordDetails> recordsMap, BatchProcessTxObject batchProcessTxObject, BatchProcessProfile batchProcessProfile) throws JSONException {
        return null;
    }

    @Override
    public String getReportingFilePath() {
        return null;
    }
}

