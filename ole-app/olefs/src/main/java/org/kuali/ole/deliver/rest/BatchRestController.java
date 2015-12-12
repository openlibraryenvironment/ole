package org.kuali.ole.deliver.rest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.asr.handler.*;
import org.kuali.ole.ncip.service.CirculationRestService;
import org.kuali.ole.spring.batch.processor.BatchBibFileProcessor;
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

    @RequestMapping(value="/upload", method = RequestMethod.POST)
    public @ResponseBody void UploadFile(@RequestParam("file") MultipartFile file, @RequestParam("profileName") String profileName,HttpServletRequest request) throws IOException {

        if(null != file && StringUtils.isNotBlank(profileName)) {
            String rawContent = IOUtils.toString(file.getBytes());
            batchBibFileProcessor.processBatch(rawContent,profileName);

        }
        System.out.println("working************************");
    }
    @RequestMapping(value="/test", method = RequestMethod.GET)
    public @ResponseBody void test() {
        System.out.println("working************************");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/sala", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String sala(@RequestBody String body) throws Exception {
        JSONObject responseJsonObject = new JSONObject();
        JSONObject jsonObject = new JSONObject(body);
        responseJsonObject.put("status","Success");

        return responseJsonObject.toString();
    }
}
