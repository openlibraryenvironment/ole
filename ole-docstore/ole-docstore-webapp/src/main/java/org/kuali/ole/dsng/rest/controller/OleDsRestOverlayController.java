package org.kuali.ole.dsng.rest.controller;

import org.kuali.ole.dsng.rest.processor.OleDsNgRestAPIProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;

/**
 * Created by SheikS on 12/8/2015.
 */
public class OleDsRestOverlayController extends OleDsNGRestSearchController {


    @RequestMapping(method = RequestMethod.POST, value = "/processBibOverlay", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String processBibOverlay(@RequestBody String body) throws Exception {
        String savedBibJsonObject = getOleDsNgRestAPIProcessor().processOverlayForBib(body);
        return savedBibJsonObject;
    }
}
