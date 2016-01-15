package org.kuali.ole.oleng.rest.controller;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.oleng.handler.CreateReqAndPOServiceHandler;
import org.kuali.ole.pojo.OleOrderRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;

/**
 * Created by SheikS on 12/18/2015.
 */
public class OleNGOrderController extends OleNgControllerBase {

    @Autowired
    private CreateReqAndPOServiceHandler orderRequestHandler;

    @RequestMapping(method = RequestMethod.POST, value = "/order/createOrder", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String createOrder(@RequestBody String requestBody) throws Exception {
        OleOrderRecord oleOrderRecord = getObjectMapper().readValue(requestBody, OleOrderRecord.class);
        Integer purapIdentifier = orderRequestHandler.processOrder(oleOrderRecord);
        JSONObject jsonObject = new JSONObject();
        if (null != purapIdentifier) {
            jsonObject.put("status", "Success");
            jsonObject.put("requisitionId", purapIdentifier);
            return jsonObject.toString();
        }
        jsonObject.put("status", "failure");
        return jsonObject.toString();
    }

}
