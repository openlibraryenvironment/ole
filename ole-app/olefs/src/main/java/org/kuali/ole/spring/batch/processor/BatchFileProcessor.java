package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OleCamelContext;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.docstore.common.response.BatchProcessFailureResponse;
import org.kuali.ole.docstore.common.response.FailureResponse;
import org.kuali.ole.docstore.common.response.OleNgBatchResponse;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.reports.BatchBibFailureReportLogHandler;
import org.kuali.ole.oleng.describe.processor.bibimport.MatchPointProcessor;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.ole.spring.batch.util.MarcStreamingUtil;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pvsubrah on 12/7/15.
 */
public abstract class BatchFileProcessor extends BatchUtil {

    @Autowired
    private MatchPointProcessor matchPointProcessor;

    private static final Logger LOG = LoggerFactory.getLogger(BatchFileProcessor.class);
    private SolrRequestReponseHandler solrRequestReponseHandler;
    protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");
    private MarcStreamingUtil marcStreamingUtil;

    public JSONObject processBatch(File inputFileDirectoryPath, String fileType, String profileId, String reportDirectoryName,
                                   BatchJobDetails batchJobDetails) throws Exception {
        JSONObject response = new JSONObject();

        BatchProcessProfile batchProcessProfile = new BatchProcessProfile();
        String responseData = "";
        try {
            BATCH_JOB_EXECUTION_DETAILS_MAP.put(batchJobDetails.getJobId() + "_" + batchJobDetails.getJobDetailId(), batchJobDetails);
            batchProcessProfile = fetchBatchProcessProfile(profileId);
            BatchProcessTxObject batchProcessTxObject = new BatchProcessTxObject();
            batchProcessTxObject.setBatchProcessProfile(batchProcessProfile);
            batchProcessTxObject.setBatchFileProcessor(this);
            batchProcessTxObject.setBatchJobDetails(batchJobDetails);
            batchProcessTxObject.setFileExtension(fileType);
            batchProcessTxObject.setReportDirectoryName(reportDirectoryName);
            batchProcessTxObject.getOleStopWatch().start();
            batchProcessTxObject.setIncomingFileDirectoryPath(inputFileDirectoryPath.getAbsolutePath());

            if (fileType.equalsIgnoreCase(OleNGConstants.MARC)) {
                OleCamelContext oleCamelContext = OleCamelContext.getInstance();
                int chunkSize = getBatchChunkSize();
                getMarcStreamingUtil().addDynamicMarcStreamRoute(oleCamelContext, inputFileDirectoryPath.getAbsolutePath(), chunkSize, batchProcessTxObject);
            } else  if (fileType.equalsIgnoreCase(OleNGConstants.EDI) || fileType.equalsIgnoreCase(OleNGConstants.INV)) {
                OleNgBatchResponse oleNgBatchResponse = processRecords(new HashMap<Integer, RecordDetails>(), batchProcessTxObject, batchProcessProfile);
                int noOfFailureRecord = oleNgBatchResponse.getNoOfFailureRecord();
                batchJobDetails.setTotalFailureRecords(String.valueOf(noOfFailureRecord));
                OleStopWatch oleStopWatch = batchProcessTxObject.getOleStopWatch();
                oleStopWatch.end();
                String totalTimeTaken = String.valueOf(oleStopWatch.getTotalTime()) + "ms";
                batchJobDetails.setEndTime(new Timestamp(System.currentTimeMillis()));
                batchJobDetails.setTimeSpent(totalTimeTaken);
                updateBatchJobDetails(batchJobDetails,OleNGConstants.COMPLETED);
                responseData = oleNgBatchResponse.getResponse();
                response.put(OleNGConstants.STATUS, true);
                writeBatchRunningStatusToFile(batchProcessTxObject.getIncomingFileDirectoryPath(), OleNGConstants.COMPLETED, totalTimeTaken);
                BATCH_JOB_EXECUTION_DETAILS_MAP.remove(batchJobDetails.getJobId() + "_" + batchJobDetails.getJobDetailId());
            }
        } catch (Exception e) {
            updateBatchJobDetails(batchJobDetails,OleNGConstants.FAILED);
            writeBatchRunningStatusToFile(inputFileDirectoryPath.getAbsolutePath(), OleNGConstants.FAILED, null);
            BatchProcessFailureResponse batchProcessFailureResponse = new BatchProcessFailureResponse();
            batchProcessFailureResponse.setBatchProcessProfileName((null != batchProcessProfile ? batchProcessProfile.getBatchProcessProfileName(): "ProfileId : " + profileId));
            batchProcessFailureResponse.setResponseData(responseData);
            batchProcessFailureResponse.setFailureReason(e.toString());
//            batchProcessFailureResponse.setFailedRawMarcContent(rawContent);
            batchProcessFailureResponse.setDetailedMessage(getDetailedMessage(e));
            batchProcessFailureResponse.setDirectoryName(reportDirectoryName);
            BatchBibFailureReportLogHandler batchBibFailureReportLogHandler = BatchBibFailureReportLogHandler.getInstance();
            batchBibFailureReportLogHandler.logMessage(Collections.singletonList(batchProcessFailureResponse),reportDirectoryName);
            BATCH_JOB_EXECUTION_DETAILS_MAP.remove(batchJobDetails.getJobId() + "_" + batchJobDetails.getJobDetailId());
            throw e;
        }
        return response;
    }

    private void updateBatchJobDetails(BatchJobDetails batchJobDetails, String status) {
        if (batchJobDetails.getJobId() != 0 && batchJobDetails.getJobDetailId() != 0) {
            batchJobDetails.setStatus(status);
            getBusinessObjectService().save(batchJobDetails);
        }
    }

    private Map<Integer, RecordDetails> getRecordDetailsMap(String rawContent) {
        Map<Integer, RecordDetails> recordDetailsMap = new HashMap<>();
        MarcReader reader = new MarcStreamReader(IOUtils.toInputStream(rawContent));
        Record nextRecord = null;
        int position = getBatchChunkSize();
        do {
            RecordDetails recordDetails = new RecordDetails();
            try {
                nextRecord = getMarcXMLConverter().getNextRecord(reader);
                recordDetails.setRecord(nextRecord);
            } catch (Exception e) {
                e.printStackTrace();
                recordDetails.setMessage("Unable to parse the marc file. Allowed format is UTF-8. " + e.toString());
            }
            recordDetails.setIndex(position);
            recordDetailsMap.put(position, recordDetails);
            position++;
        } while(reader.hasNext());
        return recordDetailsMap;
    }

    public BatchProcessProfile fetchBatchProcessProfile(String profileId) {
        BatchProcessProfile batchProcessProfile = null;

        Map parameterMap = new HashedMap();
        parameterMap.put("batchProcessProfileId",profileId);
        List<BatchProcessProfile> matching = (List<BatchProcessProfile>) getBusinessObjectService().findMatching(BatchProcessProfile.class, parameterMap);
        if(CollectionUtils.isNotEmpty(matching)){
            try {
                batchProcessProfile = matching.get(0);
                getObjectMapper().setVisibilityChecker(getObjectMapper().getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                batchProcessProfile = getObjectMapper().readValue(IOUtils.toString(batchProcessProfile.getContent()), BatchProcessProfile.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return batchProcessProfile;
    }

    public abstract OleNgBatchResponse processRecords(Map<Integer, RecordDetails> recordsMap, BatchProcessTxObject batchProcessTxObject, BatchProcessProfile batchProcessProfile) throws JSONException;
    public abstract String getReportingFilePath();

    public String getUpdatedUserName() {
        UserSession userSession = GlobalVariables.getUserSession();
        if(null != userSession) {
            return userSession.getPrincipalName();
        }
        return null;
    }

    @Override
    public SolrRequestReponseHandler getSolrRequestReponseHandler() {
        if(null == solrRequestReponseHandler) {
            solrRequestReponseHandler = new SolrRequestReponseHandler();
        }
        return solrRequestReponseHandler;
    }

    @Override
    public void setSolrRequestReponseHandler(SolrRequestReponseHandler solrRequestReponseHandler) {
        this.solrRequestReponseHandler = solrRequestReponseHandler;
    }


    public MatchPointProcessor getMatchPointProcessor() {
        return matchPointProcessor;
    }

    public void setMatchPointProcessor(MatchPointProcessor matchPointProcessor) {
        this.matchPointProcessor = matchPointProcessor;
    }

    public MarcStreamingUtil getMarcStreamingUtil() {
        if(null == marcStreamingUtil) {
            marcStreamingUtil = new MarcStreamingUtil();
        }
        return marcStreamingUtil;
    }

    public void setMarcStreamingUtil(MarcStreamingUtil marcStreamingUtil) {
        this.marcStreamingUtil = marcStreamingUtil;
    }

    public int getFailureRecordsCount(List<? extends FailureResponse> failureResponses) {
        Set<Integer> failureRecords = new HashSet<>();
        if(CollectionUtils.isNotEmpty(failureResponses)) {
            for (Iterator<? extends FailureResponse> iterator = failureResponses.iterator(); iterator.hasNext(); ) {
                FailureResponse bibFailureResponse = iterator.next();
                Integer index = bibFailureResponse.getIndex();
                if(null != index && index != 0) {
                    failureRecords.add(index);
                }
            }
        }
        return  failureRecords.size();
    }
}
