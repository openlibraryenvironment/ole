package org.kuali.ole.oleng.rest.controller;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.BatchProfileRequestHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Created by rajeshbabuk on 12/9/15.
 */

@Controller
@RequestMapping("/batchProfileRestController")
public class BatchProfileRestController extends OleNgControllerBase {

    private BatchProfileRequestHandler batchProfileRequestHandler;

    @RequestMapping(method = RequestMethod.POST, value = "/submit", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String submitProfile(@RequestBody String requestBody) {
        BatchProcessProfile batchProcessProfile;
        try {
            batchProcessProfile = batchProfileRequestHandler.convertJsonToProfile(requestBody);
            batchProcessProfile.setContent(requestBody.getBytes());
            getBusinessObjectService().save(batchProcessProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestBody;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/search", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String searchProfile(@RequestBody String requestBody) {
        String responseString = "[]";
        try {
            responseString = getBatchProfileRequestHandler().prepareProfileForSearch(requestBody);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }

    public BatchProfileRequestHandler getBatchProfileRequestHandler() {
        if(null == batchProfileRequestHandler) {
            batchProfileRequestHandler = new BatchProfileRequestHandler();
        }
        return batchProfileRequestHandler;
    }

    public void setBatchProfileRequestHandler(BatchProfileRequestHandler batchProfileRequestHandler) {
        this.batchProfileRequestHandler = batchProfileRequestHandler;
    }
}
