package org.kuali.ole.spring.batch.rest.controller;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.asr.handler.*;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.ncip.service.CirculationRestService;
import org.kuali.ole.oleng.batch.process.model.BatchJob;
import org.kuali.ole.oleng.batch.process.model.BatchProcessJob;
import org.kuali.ole.oleng.rest.controller.OleNgControllerBase;
import org.kuali.ole.spring.batch.processor.BatchBibFileProcessor;
import org.kuali.ole.spring.batch.processor.BatchFileProcessor;
import org.kuali.ole.spring.batch.processor.BatchInvoiceImportProcessor;
import org.kuali.ole.spring.batch.processor.BatchOrderImportProcessor;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 25/6/15.
 */
@Controller
@RequestMapping("/batch")
public class BatchRestController extends OleNgControllerBase {

    @Autowired
    private BatchBibFileProcessor batchBibFileProcessor;

    @Autowired
    private BatchOrderImportProcessor batchOrderImportProcessor;

    @Autowired
    private BatchInvoiceImportProcessor batchInvoiceImportProcessor;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String UploadFile(@RequestParam("file") MultipartFile file, @RequestParam("profileName") String profileName,
                             @RequestParam("batchType") String batchType, HttpServletRequest request) throws IOException, JSONException {
        if (null != file && StringUtils.isNotBlank(profileName) && StringUtils.isNotBlank(batchType)) {
            String rawContent = IOUtils.toString(file.getBytes());
            return processBatch(profileName, batchType, rawContent);
        }
        return null;
    }

    private String processBatch(String profileName, String batchType, String rawContent) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        BatchFileProcessor batchProcessor = getBatchProcessor(batchType);
        batchProcessor.processBatch(rawContent, profileName);
        oleStopWatch.end();
        long totalTime = oleStopWatch.getTotalTime();
        jsonObject.put("processTime",totalTime + "ms");
        return jsonObject.toString();
    }

    @RequestMapping(value = "/submit/api", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String submitApi(@RequestBody String requestBody) throws IOException, JSONException {
        JSONObject requestJson = new JSONObject(requestBody);
        String rawContent = requestJson.getString("marcContent");
        String batchType = requestJson.getString("batchType");
        String profileName = requestJson.getString("profileName");
        String batchResponse = processBatch(profileName, batchType, rawContent);
        return batchResponse;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/process/submit", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String submitProcess(@RequestBody String requestBody) {
        BatchProcessJob batchProcessJob;
        try {
            batchProcessJob = convertJsonToProcessJob(requestBody);
            String loginUser = GlobalVariables.getUserSession().getPrincipalName();
            batchProcessJob.setCreatedBy(loginUser);
            batchProcessJob.setCreatedOn(new Timestamp(new Date().getTime()));
            getBusinessObjectService().save(batchProcessJob);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestBody;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/process/launch", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String launchProcess(@RequestParam("processId") long processId) {
        try {
            BatchProcessJob matchedBatchJob = getBatchProcessJobById(processId);
            BatchJob batchJob = createBatchJob(matchedBatchJob);
            getBusinessObjectService().save(batchJob);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(processId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/process/destroy", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String destroyProcess(@RequestParam("processId") long processId) {
        try {
            BatchProcessJob matchedBatchJob = getBatchProcessJobById(processId);
            if (null != matchedBatchJob) {
                // Set Batch Process Job status to Destroyed
                //getBusinessObjectService().save(matchedBatchJob);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(processId);
    }

    private BatchJob createBatchJob(BatchProcessJob batchProcessJob) {
        BatchJob batchJob = new BatchJob();
        batchJob.setBatchProcessId(batchProcessJob.getBatchProcessId());
        batchJob.setJobName(batchProcessJob.getBatchProcessName());
        batchJob.setBatchProcessType(batchProcessJob.getBatchProcessType());
        batchJob.setProfileName(batchProcessJob.getBatchProfileName());
        batchJob.setCreatedBy(batchProcessJob.getCreatedBy());
        batchJob.setStartTime(new Timestamp(new Date().getTime()));
        batchJob.setStatus("RUNNING");
        return batchJob;
    }

    public BatchProcessJob convertJsonToProcessJob(String processJsonString) throws JSONException, IOException {
        getObjectMapper().setVisibilityChecker(getObjectMapper().getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        BatchProcessJob batchProcessJob = getObjectMapper().readValue(processJsonString, BatchProcessJob.class);
        return batchProcessJob;
    }

    public BatchProcessJob getBatchProcessJobById(Long processId) {
        Map map = new HashedMap();
        map.put(OleNGConstants.BATCH_PROCESS_ID, processId);
        return getBusinessObjectService().findByPrimaryKey(BatchProcessJob.class, map);
    }

    public BatchFileProcessor getBatchProcessor(String batchType) {
        if(batchType.equalsIgnoreCase("Order Record Import")){
            return batchOrderImportProcessor;
        } else if(batchType.equalsIgnoreCase("Bib Import")) {
            return batchBibFileProcessor;
        } else if(batchType.equalsIgnoreCase("Invoice Import")) {
            return batchInvoiceImportProcessor;
        }
        return null;
    }
}