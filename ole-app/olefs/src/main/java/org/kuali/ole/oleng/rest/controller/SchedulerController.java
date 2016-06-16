package org.kuali.ole.oleng.rest.controller;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.oleng.scheduler.OleNGBatchJobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;

/**
 * Created by SheikS on 4/19/2016.
 */
@Controller
@RequestMapping("/schedulerController")
public class SchedulerController {

    @Autowired
    private OleNGBatchJobScheduler oleNGBatchJobScheduler;

    @RequestMapping(method = RequestMethod.GET, value = "/scheduleAllJobs", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String scheduleAllJobs() {
        JSONObject jsonObject = new JSONObject();
        try {
            oleNGBatchJobScheduler.initializeAllJobs();
            jsonObject.put("Status", "Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
