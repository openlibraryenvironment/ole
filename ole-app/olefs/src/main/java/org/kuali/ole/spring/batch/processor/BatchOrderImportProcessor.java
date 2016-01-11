package org.kuali.ole.spring.batch.processor;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.docstore.common.response.BibResponse;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.callable.POCallable;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.oleng.handler.OrderRequestHandler;
import org.kuali.ole.oleng.service.OrderImportService;
import org.kuali.ole.oleng.service.impl.OrderImportServiceImpl;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.marc4j.marc.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by SheikS on 1/6/2016.
 */
@Service("batchOrderImportProcessor")
public class BatchOrderImportProcessor extends BatchFileProcessor {

    private static final Logger LOG = Logger.getLogger(BatchOrderImportProcessor.class);


    @Autowired
    private OrderRequestHandler orderRequestHandler;

    @Autowired
    private DescribeDAO describeDAO;


    @Autowired
    private BatchBibFileProcessor batchBibFileProcessor;

    @Override
    public String processRecords(List<Record> records, BatchProcessProfile batchProcessProfile) throws JSONException {
        String response = "";
        StringBuilder stringBuilder= new StringBuilder();
        JSONObject jsonObject = new JSONObject();
        if(CollectionUtils.isNotEmpty(records)) {

            BatchProcessProfile bibImportProfile = getBibImportProfile(batchProcessProfile.getBibImportProfileForOrderImport());
            if (null != bibImportProfile) {
                OleNGBibImportResponse oleNGBibImportResponse = processBibImport(records, bibImportProfile);

                List<String> bibIds = getBibIds(oleNGBibImportResponse);

                int chunkSize = getNumberOfChunkSizeForOrderImport();
                List<List<String>> bibIdsSubLists = Lists.partition(bibIds, chunkSize);
                for (Iterator<List<String>> iterator = bibIdsSubLists.iterator(); iterator.hasNext(); ) {
                    List<String> bibIdSubList = iterator.next();

                }

                ExecutorService orderImportExecutorService;
                ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("create-order-thread-%d").build();
                int numberOfThreadForOrder = getNumberOfThreadForOrderImport();
                orderImportExecutorService = Executors.newFixedThreadPool(numberOfThreadForOrder, threadFactory);
                List<Future> futures = new ArrayList<>();

                for(int index = 0; index < 2; index++){
                    Future future = orderImportExecutorService.submit(new POCallable(batchProcessProfile,orderRequestHandler));
                    futures.add(future);
                }
                for (Iterator<Future> iterator = futures.iterator(); iterator.hasNext(); ) {
                    Future future = iterator.next();
                    try {
                        String orderImportResponse = (String) future.get();
                        appendContentToStrinBuilder(stringBuilder,orderImportResponse);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                orderImportExecutorService.shutdown();
                response = stringBuilder.toString();
            }
        } else {
            jsonObject.put("status", "failure");
            jsonObject.put("reason", "Invalid record.");
            response = jsonObject.toString();
        }
        return response;
    }

    private BatchProcessProfile getBibImportProfile(String profileName) {
        BatchProcessProfile batchProcessProfile = null;
        try {
            List<BatchProcessProfile> batchProcessProfiles = describeDAO.fetchProfileByNameAndType(profileName, "Bib Import");
            if(CollectionUtils.isNotEmpty(batchProcessProfiles)) {
                batchProcessProfile = batchProcessProfiles.get(0);
                getObjectMapper().setVisibilityChecker(getObjectMapper().getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                batchProcessProfile = getObjectMapper().readValue(IOUtils.toString(batchProcessProfile.getContent()), BatchProcessProfile.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return batchProcessProfile;
    }

    private OleNGBibImportResponse processBibImport(List<Record> records, BatchProcessProfile bibImportProfile) {
        OleNGBibImportResponse oleNGBibImportResponse = new OleNGBibImportResponse();
        try {
            String response = batchBibFileProcessor.processRecords(records, bibImportProfile);
            oleNGBibImportResponse = getObjectMapper().readValue(response, OleNGBibImportResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oleNGBibImportResponse;
    }

    private List<String> getBibIds(OleNGBibImportResponse oleNGBibImportResponse) {
        List<String> bibIds = new ArrayList<>();
        List<BibResponse> bibResponses = oleNGBibImportResponse.getBibResponses();
        if(CollectionUtils.isNotEmpty(bibResponses)) {
            for (Iterator<BibResponse> iterator = bibResponses.iterator(); iterator.hasNext(); ) {
                BibResponse bibResponse = iterator.next();
                bibIds.add(bibResponse.getBibId());
            }
        }
        return bibIds;
    }

    @Override
    public String getReportingFilePath() {
        return ConfigContext.getCurrentContextConfig().getProperty("batch.orderRecord.directory");
    }

    private void appendContentToStrinBuilder(StringBuilder stringBuilder, String content) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append("\n").append(content);
        } else {
            stringBuilder.append(content);
        }
    }

    public int getNumberOfThreadForOrderImport() {
        String maxNumberOfThreadFromParameter = ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.SELECT_NMSPC, OLEConstants.SELECT_CMPNT, OLEConstants.MAX_NO_OF_THREAD_FOR_ORDER_IMPORT);
        int maxNumberOfThread = 0;
        try{
            maxNumberOfThread = Integer.parseInt(maxNumberOfThreadFromParameter);
        }catch(Exception exception){
            LOG.error("Invalid max number of thread for order import service from system parameter.");
        }
        return maxNumberOfThread > 0 ? maxNumberOfThread : 10;
    }

    public int getNumberOfChunkSizeForOrderImport() {
        String chunkSizeFromParameter = ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.SELECT_NMSPC, OLEConstants.SELECT_CMPNT, OLEConstants.CHUNK_SIZE_FOR_ORDER_IMPORT);
        int chunkSize = 0;
        try{
            chunkSize = Integer.parseInt(chunkSizeFromParameter);
        }catch(Exception exception){
            LOG.error("Invalid chunk size for order import service from system parameter.");
        }
        return chunkSize > 0 ? chunkSize : 10;
    }

}
