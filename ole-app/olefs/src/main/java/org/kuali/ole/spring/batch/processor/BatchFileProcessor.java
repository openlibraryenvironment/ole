package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.docstore.common.response.BatchProcessFailureResponse;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.docstore.common.response.FailureResponse;
import org.kuali.ole.docstore.common.response.OleNgBatchResponse;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.reports.BatchBibFailureReportLogHandler;
import org.kuali.ole.oleng.describe.processor.bibimport.MatchPointProcessor;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pvsubrah on 12/7/15.
 */
public abstract class BatchFileProcessor extends BatchUtil {

    @Autowired
    private MatchPointProcessor matchPointProcessor;

    private static final Logger LOG = LoggerFactory.getLogger(BatchFileProcessor.class);
    private MarcXMLConverter marcXMLConverter;
    private SolrRequestReponseHandler solrRequestReponseHandler;
    protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");

    public JSONObject processBatch(String rawContent ,String fileType, String profileId, String reportDirectoryName, BatchJobDetails batchJobDetails) {
        JSONObject response = new JSONObject();
        BatchProcessProfile batchProcessProfile = new BatchProcessProfile();
        String responseData = "";
        try {
            batchProcessProfile = fetchBatchProcessProfile(profileId);
            Map<Integer, RecordDetails> recordDetailsMap = new HashMap<>();
            if (fileType.equalsIgnoreCase(OleNGConstants.MARC)) {
                recordDetailsMap = getRecordDetailsMap(rawContent);
                if (batchJobDetails.getJobId() != 0 && batchJobDetails.getJobDetailId() != 0) {
                    batchJobDetails.setTotalRecords(String.valueOf(recordDetailsMap.size()));
                    getBusinessObjectService().save(batchJobDetails);
                }
            }
            OleNgBatchResponse oleNgBatchResponse = processRecords(rawContent, recordDetailsMap, fileType, batchProcessProfile, reportDirectoryName, batchJobDetails);
            int noOfFailureRecord = oleNgBatchResponse.getNoOfFailureRecord();
            batchJobDetails.setTotalFailureRecords(String.valueOf(noOfFailureRecord));
            responseData = oleNgBatchResponse.getResponse();
            response.put(OleNGConstants.STATUS, true);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            BatchProcessFailureResponse batchProcessFailureResponse = new BatchProcessFailureResponse();
            batchProcessFailureResponse.setBatchProcessProfileName((null != batchProcessProfile ? batchProcessProfile.getBatchProcessProfileName(): "ProfileId : " + profileId));
            batchProcessFailureResponse.setResponseData(responseData);
            batchProcessFailureResponse.setFailureReason(e.toString());
            batchProcessFailureResponse.setFailedRawMarcContent(rawContent);
            batchProcessFailureResponse.setDirectoryName(reportDirectoryName);
            BatchBibFailureReportLogHandler batchBibFailureReportLogHandler = BatchBibFailureReportLogHandler.getInstance();;
            batchBibFailureReportLogHandler.logMessage(batchProcessFailureResponse,reportDirectoryName);
            throw e;
        }
        return response;
    }

    private Map<Integer, RecordDetails> getRecordDetailsMap(String rawContent) {
        Map<Integer, RecordDetails> recordDetailsMap = new HashMap<>();
        MarcReader reader = new MarcStreamReader(IOUtils.toInputStream(rawContent));
        Record nextRecord = null;
        int position = 1;
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

    public abstract OleNgBatchResponse processRecords(String rawContent, Map<Integer, RecordDetails> recordsMap, String fileType,
                                                      BatchProcessProfile batchProcessProfile, String reportDirectoryName,
                                                      BatchJobDetails batchJobDetails) throws JSONException;
    public abstract String getReportingFilePath();

    public String getUpdatedUserName() {
        UserSession userSession = GlobalVariables.getUserSession();
        if(null != userSession) {
            return userSession.getPrincipalName();
        }
        return null;
    }

    public MarcXMLConverter getMarcXMLConverter() {
        if(null == marcXMLConverter) {
            marcXMLConverter = new MarcXMLConverter();
        }
        return marcXMLConverter;
    }

    public void setMarcXMLConverter(MarcXMLConverter marcXMLConverter) {
        this.marcXMLConverter = marcXMLConverter;
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
