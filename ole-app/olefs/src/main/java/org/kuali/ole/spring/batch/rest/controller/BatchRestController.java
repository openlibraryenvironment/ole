package org.kuali.ole.spring.batch.rest.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.asr.handler.*;
import org.kuali.ole.ncip.service.CirculationRestService;
import org.kuali.ole.spring.batch.processor.BatchBibFileProcessor;
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

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    void UploadFile(@RequestParam("file") MultipartFile file, @RequestParam("profileName") String profileName, HttpServletRequest request) throws IOException {
        if (null != file && StringUtils.isNotBlank(profileName)) {
            String rawContent = IOUtils.toString(file.getBytes());
            batchBibFileProcessor.processBatch(rawContent, profileName);
        }
    }
}
