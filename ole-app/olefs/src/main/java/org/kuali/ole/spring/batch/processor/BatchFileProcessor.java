package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONException;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileMatchPoint;
import org.kuali.ole.oleng.describe.processor.bibimport.MatchPointProcessor;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
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

    public void processBatch(String  rawMarc, String profileId) {
        try {
            BatchProcessProfile batchProcessProfile = fetchBatchProcessProfile(profileId);
            List<Record> records = getMarcXMLConverter().convertRawMarchToMarc(rawMarc);
            String responseData = processRecords(records, batchProcessProfile);
            String date = simpleDateFormat.format(new Date());
            String batchProcessProfileName = batchProcessProfile.getBatchProcessProfileName();
            String fileName = getReportingFilePath()+ File.separator+batchProcessProfileName+"_"+  date+".txt";
            FileUtils.write(new File(fileName), responseData);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public abstract String processRecords(List<Record> records, BatchProcessProfile batchProcessProfile) throws JSONException;
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
