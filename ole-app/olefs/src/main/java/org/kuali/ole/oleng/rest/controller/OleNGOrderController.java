package org.kuali.ole.oleng.rest.controller;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.handler.CreateReqAndPOServiceHandler;
import org.kuali.ole.pojo.OleOrderRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;
import java.util.Collections;

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
        Exchange exchange = new Exchange();
        Integer purapIdentifier = orderRequestHandler.processOrder(Collections.singletonList(oleOrderRecord), exchange);
        JSONObject jsonObject = new JSONObject();
        if (null != purapIdentifier) {
            jsonObject.put(OleNGConstants.STATUS, OleNGConstants.SUCCESS);
            jsonObject.put(OleNGConstants.REQUISITION_ID, purapIdentifier);
            return jsonObject.toString();
        }
        jsonObject.put(OleNGConstants.STATUS, OleNGConstants.FAILURE);
        return jsonObject.toString();
    }

}
