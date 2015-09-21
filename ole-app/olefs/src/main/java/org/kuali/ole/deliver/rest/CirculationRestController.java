package org.kuali.ole.deliver.rest;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.asr.handler.*;
import org.kuali.ole.ncip.service.CirculationRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 25/6/15.
 */
@Controller
@RequestMapping("/circ")
public class CirculationRestController {

    @Autowired
    private CirculationRestService circulationRestService;

    /* Rest POST Methods Start*/

    @RequestMapping(method = RequestMethod.POST, value = "/renewItem", produces = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ResponseBody
    public String renewItem(@RequestBody String body) throws Exception {
        String responseString = "";
        JSONObject jsonObject = new JSONObject(body);
        Map renewParameters = new RenewItemRequestHandler().parseRequest(jsonObject);
        responseString = getCirculationRestService().renewItems(renewParameters);
        return responseString;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/renewItemSIP2", produces = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ResponseBody
    public String renewItemForSIP2(@RequestBody String body) throws Exception {
        String responseString = "";
        JSONObject jsonObject = new JSONObject(body);
        Map renewParameters = new RenewItemRequestHandler().parseRequest(jsonObject);
        responseString = getCirculationRestService().renewItemsSIP2(renewParameters);
        return responseString;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/checkoutItem", produces = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ResponseBody
    public String checkoutItem(@RequestBody String body) throws Exception {
        String responseString = "";
        JSONObject jsonObject = new JSONObject(body);
        Map checkoutParameters = new CheckoutItemRequestHandler().parseRequest(jsonObject);
        responseString = getCirculationRestService().checkoutItem(checkoutParameters);
        return responseString;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/checkoutItemSIP2", produces = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ResponseBody
    public String checkoutItemSIP2(@RequestBody String body) throws Exception {
        String responseString = "";
        JSONObject jsonObject = new JSONObject(body);
        Map checkoutParameters = new CheckoutItemRequestHandler().parseRequest(jsonObject);
        responseString = getCirculationRestService().checkoutItemSIP2(checkoutParameters);
        return responseString;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/checkinItem", produces = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ResponseBody
    public String checkinItem(@RequestBody String body) throws Exception {
        String responseString = "";
        JSONObject jsonObject = new JSONObject(body);
        Map checkinParameters = new CheckinItemRequestHandler().parseRequest(jsonObject);
        responseString = getCirculationRestService().checkinItem(checkinParameters);
        return responseString;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/checkinItemSIP2", produces = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ResponseBody
    public String checkinItemSIP2(@RequestBody String body) throws Exception {
        String responseString = "";
        JSONObject jsonObject = new JSONObject(body);
        Map checkinParameters = new CheckinItemRequestHandler().parseRequest(jsonObject);
        responseString = getCirculationRestService().checkinItemSIP2(checkinParameters);
        return responseString;
    }

    public CirculationRestService getCirculationRestService() {
        return circulationRestService;
    }

    public void setCirculationRestService(CirculationRestService circulationRestService) {
        this.circulationRestService = circulationRestService;
    }
}
