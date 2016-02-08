package org.kuali.ole.spring.batch.rest.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.spring.batch.processor.BatchBibFileProcessor;
import org.kuali.ole.spring.batch.processor.BatchFileProcessor;
import org.kuali.ole.spring.batch.processor.BatchInvoiceImportProcessor;
import org.kuali.ole.spring.batch.processor.BatchOrderImportProcessor;
import org.kuali.ole.utility.OleStopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Created by sheiksalahudeenm on 25/6/15.
 */
@Controller
@RequestMapping("/batch")
public class BatchRestController {

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