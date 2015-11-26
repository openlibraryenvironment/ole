package org.kuali.ole.dsng.rest.controller;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.dsng.rest.processor.OleDsNgRestAPIProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created by SheikS on 11/25/2015.
 */
@Controller
@RequestMapping("/oledsng")
public class OleDsNGRestAPIController {

    private OleDsNgRestAPIProcessor oleDsNgRestAPIProcessor;

    @RequestMapping(method = RequestMethod.POST, value = "/createBib", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String createBib(@RequestBody String body) throws Exception {
        String savedBibJsonObject = getOleDsNgRestAPIProcessor().createBib(body);
        return savedBibJsonObject;
    }

    public OleDsNgRestAPIProcessor getOleDsNgRestAPIProcessor() {
        if(null == oleDsNgRestAPIProcessor) {
            oleDsNgRestAPIProcessor = new OleDsNgRestAPIProcessor();
        }
        return oleDsNgRestAPIProcessor;
    }

    public void setOleDsNgRestAPIProcessor(OleDsNgRestAPIProcessor oleDsNgRestAPIProcessor) {
        this.oleDsNgRestAPIProcessor = oleDsNgRestAPIProcessor;
    }
}
