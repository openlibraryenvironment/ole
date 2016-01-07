package org.kuali.ole.spring.batch.rest.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.asr.handler.*;
import org.kuali.ole.ncip.service.CirculationRestService;
import org.kuali.ole.spring.batch.processor.BatchBibFileProcessor;
import org.kuali.ole.spring.batch.processor.BatchFileProcessor;
import org.kuali.ole.spring.batch.processor.BatchOrderImportProcessor;
import org.kuali.ole.utility.OleStopWatch;
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
import java.util.Map;

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

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String UploadFile(@RequestParam("file") MultipartFile file, @RequestParam("profileName") String profileName,
                             @RequestParam("batchType") String batchType, HttpServletRequest request) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        if (null != file && StringUtils.isNotBlank(profileName) && StringUtils.isNotBlank(batchType)) {
            String rawContent = IOUtils.toString(file.getBytes());
            OleStopWatch oleStopWatch = new OleStopWatch();
            oleStopWatch.start();
            BatchFileProcessor batchProcessor = getBatchProcessor(batchType);
            batchProcessor.processBatch(rawContent, profileName);
            oleStopWatch.end();
            long totalTime = oleStopWatch.getTotalTime();
            jsonObject.put("processTime",totalTime + "ms");
            return jsonObject.toString();
        }
        return null;
    }

    public BatchFileProcessor getBatchProcessor(String batchType) {
        if(batchType.equalsIgnoreCase("Order Record Import")){
            return batchOrderImportProcessor;
        } else if(batchType.equalsIgnoreCase("Bib Import")) {
            return batchBibFileProcessor;
        }
        return null;
    }
}
