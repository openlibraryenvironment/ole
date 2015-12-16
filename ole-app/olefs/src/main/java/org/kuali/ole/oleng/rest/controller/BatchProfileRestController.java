package org.kuali.ole.oleng.rest.controller;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.BatchProfileRequestHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;

/**
 * Created by rajeshbabuk on 12/9/15.
 */

@Controller
@RequestMapping("/batchProfileRestController")
public class BatchProfileRestController extends OleNgControllerBase {

    @RequestMapping(method = RequestMethod.POST, value = "/submit", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String submitProfile(@RequestBody String requestBody) {
        BatchProcessProfile batchProcessProfile;
        try {
            batchProcessProfile = new BatchProfileRequestHandler().convertJsonToProfile(requestBody);
            batchProcessProfile.setContent(requestBody.getBytes());
            getBusinessObjectService().save(batchProcessProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestBody;
    }
}
