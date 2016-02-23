package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.docstore.common.response.BatchProcessFailureResponse;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.reports.BatchBibFailureReportLogHandler;
import org.kuali.ole.oleng.describe.processor.bibimport.MatchPointProcessor;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.marc4j.marc.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public JSONObject processBatch(String  rawMarc, String profileId, String reportDirectoryName) {
        JSONObject response = new JSONObject();
        BatchProcessProfile batchProcessProfile = new BatchProcessProfile();
        String responseData = "";
        try {
            batchProcessProfile = fetchBatchProcessProfile(profileId);
            List<Record> records = getMarcXMLConverter().convertRawMarchToMarc(rawMarc);
            responseData = processRecords(records, batchProcessProfile, reportDirectoryName);
            String date = simpleDateFormat.format(new Date());
            String batchProcessProfileName = batchProcessProfile.getBatchProcessProfileName();
            String fileName = getReportingFilePath()+ File.separator+batchProcessProfileName+"_"+  date+".txt";
            FileUtils.write(new File(fileName), responseData);
            response.put(OleNGConstants.STATUS, true);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            BatchProcessFailureResponse batchProcessFailureResponse = new BatchProcessFailureResponse();
            batchProcessFailureResponse.setBatchProcessProfileName((null != batchProcessProfile ? batchProcessProfile.getBatchProcessProfileName(): "ProfileId : " + profileId));
            batchProcessFailureResponse.setResponseData(responseData);
            batchProcessFailureResponse.setFailureReason(e.toString());
            batchProcessFailureResponse.setFailedRawMarcContent(rawMarc);
            batchProcessFailureResponse.setDirectoryName(reportDirectoryName);
            BatchBibFailureReportLogHandler batchBibFailureReportLogHandler = BatchBibFailureReportLogHandler.getInstance();;
            batchBibFailureReportLogHandler.logMessage(batchProcessFailureResponse,reportDirectoryName);
            throw e;
        }
        return response;
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

    public abstract String processRecords(List<Record> records, BatchProcessProfile batchProcessProfile, String reportDirectoryName) throws JSONException;
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
}
