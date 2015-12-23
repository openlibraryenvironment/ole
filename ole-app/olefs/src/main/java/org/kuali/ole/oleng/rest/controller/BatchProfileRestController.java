package org.kuali.ole.oleng.rest.controller;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.BatchProfileRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Created by rajeshbabuk on 12/9/15.
 */
public class BatchProfileRestController extends BatchProfileUtilController{

    @Autowired
    private BatchProfileRequestHandler batchProfileRequestHandler;

    @RequestMapping(method = RequestMethod.POST, value = "/profile/submit", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String submitProfile(@RequestBody String requestBody) {
        BatchProcessProfile batchProcessProfile;
        try {
            batchProcessProfile = getBatchProfileRequestHandler().convertJsonToProfile(requestBody);
            BatchProcessProfile matching = getBatchProfileRequestHandler().getBatchProcessProfileById(batchProcessProfile.getBatchProcessProfileId());
            if (null == matching) {
                batchProcessProfile.setContent(requestBody.getBytes());
                getBusinessObjectService().save(batchProcessProfile);
            } else {
                matching.setBatchProcessProfileName(batchProcessProfile.getBatchProcessProfileName());
                matching.setDescription(batchProcessProfile.getDescription());
                matching.setContent(requestBody.getBytes());
                getBusinessObjectService().save(matching);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestBody;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/profile/search", produces = {MediaType.APPLICATION_JSON})
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

    @RequestMapping(method = RequestMethod.POST, value = "/profile/edit", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String editProfile(@RequestBody String requestBody) {
        String responseString = "{}";
        try {
            responseString = getBatchProfileRequestHandler().prepareProfileForEdit(requestBody);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/profile/delete", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String deleteProfile(@RequestBody String requestBody) {
        String responseString = "{}";
        try {
            responseString = getBatchProfileRequestHandler().deleteProfile(requestBody);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }

    public BatchProfileRequestHandler getBatchProfileRequestHandler() {
        return batchProfileRequestHandler;
    }

    public void setBatchProfileRequestHandler(BatchProfileRequestHandler batchProfileRequestHandler) {
        this.batchProfileRequestHandler = batchProfileRequestHandler;
    }
}
