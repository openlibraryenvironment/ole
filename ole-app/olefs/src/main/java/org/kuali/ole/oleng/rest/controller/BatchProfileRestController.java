package org.kuali.ole.oleng.rest.controller;

import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.BatchProfileRequestHandler;
import org.kuali.ole.utility.OleStopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Created by rajeshbabuk on 12/9/15.
 */
public class BatchProfileRestController extends BatchProfileUtilController {

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
                matching.setBatchProcessType(batchProcessProfile.getBatchProcessType());
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

    @RequestMapping(value = "/profile/import", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String UploadFile(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws IOException, JSONException {
        JSONObject responseObject = new JSONObject();
        String rawContent = IOUtils.toString(file.getBytes());
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        JSONObject jsonObject = new JSONObject(rawContent);
        jsonObject.put(OleNGConstants.PROFILE_ID,0);
        BatchProcessProfile batchProcessProfile = getBatchProfileRequestHandler().convertJsonToProfile(jsonObject.toString());
        if (null != batchProcessProfile) {
            batchProcessProfile.setContent(jsonObject.toString().getBytes());
            getBatchProfileRequestHandler().getBatchProfileService().saveProfile(batchProcessProfile);
        }
        oleStopWatch.end();
        long totalTime = oleStopWatch.getTotalTime();
        responseObject.put("processTime",totalTime + "ms");
        return responseObject.toString();
    }

    public BatchProfileRequestHandler getBatchProfileRequestHandler() {
        return batchProfileRequestHandler;
    }

    public void setBatchProfileRequestHandler(BatchProfileRequestHandler batchProfileRequestHandler) {
        this.batchProfileRequestHandler = batchProfileRequestHandler;
    }
}
