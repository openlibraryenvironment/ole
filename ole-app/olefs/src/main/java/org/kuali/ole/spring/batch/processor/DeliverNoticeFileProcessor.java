package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.docstore.common.response.*;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.reports.BatchBibFailureReportLogHandler;
import org.kuali.ole.oleng.batch.reports.processors.BatchNoticeReportLogHandler;
import org.kuali.ole.oleng.handler.BatchExportHandler;
import org.kuali.ole.oleng.handler.DeliverNoticeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by govindarajank on 4/20/16.
 */
@Service("deliverNoticeProcessor")
public class DeliverNoticeFileProcessor extends BatchFileProcessor {

    @Autowired
    private DeliverNoticeHandler deliverNoticeHandler;

    @Override
    public JSONObject processBatch(File inputFileDirectoryPath, String fileType, String profileId, String reportDirectoryName, BatchJobDetails batchJobDetails) throws Exception {
        JSONObject jsonResponse = new JSONObject();
        OleNGBatchNoticeResponse oleNGBatchNoticeResponse = new OleNGBatchNoticeResponse();
        BatchProcessProfile batchProcessProfile = null;
        try {
            oleNGBatchNoticeResponse.setJobDetailId(Long.toString(batchJobDetails.getJobDetailId()));
            oleNGBatchNoticeResponse.setJobName(batchJobDetails.getJobName());
            oleNGBatchNoticeResponse.setProfileName(batchJobDetails.getProfileName());
            oleNGBatchNoticeResponse.setReportDirectoryName(reportDirectoryName);
            batchProcessProfile = fetchBatchProcessProfile(profileId);
            //batchProcessTxObject = buildBatchProcessTxObject(inputFileDirectoryPath, fileType, batchProcessProfile, reportDirectoryName, batchJobDetails);
            deliverNoticeHandler.processExport(batchProcessProfile ,batchJobDetails, oleNGBatchNoticeResponse);
            generateBatchReport(inputFileDirectoryPath, batchJobDetails, oleNGBatchNoticeResponse);
            jsonResponse.put(OleNGConstants.STATUS, true);
        } catch (Exception e) {
            e.printStackTrace();
            generateFailureReport(inputFileDirectoryPath, reportDirectoryName, null, profileId, batchJobDetails, batchProcessProfile, e);
        }

        return jsonResponse;
    }

    private BatchProcessTxObject buildBatchProcessTxObject(File inputFileDirectoryPath, String fileType, BatchProcessProfile batchProcessProfile, String reportDirectoryName, BatchJobDetails batchJobDetails) {
        BatchProcessTxObject batchProcessTxObject = new BatchProcessTxObject();
        batchProcessTxObject.setBatchProcessProfile(batchProcessProfile);
        batchProcessTxObject.setBatchFileProcessor(this);
        batchProcessTxObject.setBatchJobDetails(batchJobDetails);
        batchProcessTxObject.setFileExtension(fileType);
        batchProcessTxObject.setReportDirectoryName(reportDirectoryName);
        batchProcessTxObject.setIncomingFileDirectoryPath(inputFileDirectoryPath.getAbsolutePath());
        return batchProcessTxObject;
    }

    private void generateBatchReport(File inputFile, BatchJobDetails batchJobDetails, OleNGBatchNoticeResponse oleNGBatchNoticeResponse) {
        oleNGBatchNoticeResponse.setJobDetailId(String.valueOf(batchJobDetails.getJobDetailId()));
        oleNGBatchNoticeResponse.setJobName(String.valueOf(batchJobDetails.getJobName()));

        BatchNoticeReportLogHandler batchNoticeReportLogHandler = BatchNoticeReportLogHandler.getInstance();
        batchNoticeReportLogHandler.logMessage(oleNGBatchNoticeResponse, oleNGBatchNoticeResponse.getReportDirectoryName());

        //batchJobDetails.setTotalRecords(String.valueOf(oleNGBatchExportResponse.getTotalNumberOfRecords()));
        batchJobDetails.setTotalRecordsProcessed(String.valueOf(oleNGBatchNoticeResponse.getNoOfSuccessNotice() + oleNGBatchNoticeResponse.getNoOfFailureNotice()));
        batchJobDetails.setTotalFailureRecords(String.valueOf(oleNGBatchNoticeResponse.getNoOfFailureNotice()));
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
