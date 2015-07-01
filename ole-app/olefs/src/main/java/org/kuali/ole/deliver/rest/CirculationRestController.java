package org.kuali.ole.deliver.rest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.asr.handler.RenewItemResponseHandler;
import org.kuali.asr.handler.ResponseHandler;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.ncip.bo.OLERenewItemList;
import org.kuali.ole.ncip.service.CirculationRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 25/6/15.
 */
@Controller
@RequestMapping("/circ")
public class CirculationRestController {

    @Autowired
    private CirculationRestService circulationRestService;
    private ResponseHandler responseHandler;

    /* Rest POST Methods Start*/

    @RequestMapping(method = RequestMethod.POST, value = "/renewItem", produces = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ResponseBody
    public String renewItem(@RequestBody String body) throws Exception {
        String responseString = "";
        JSONObject jsonObject = new JSONObject(body);

        String patronBarcode = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.PATRON_BARCODE);
        String operatorId = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.OPERATOR_ID);
        JSONArray itemBarcodes = (JSONArray) jsonObject.get(OLENCIPConstants.CIRC_ITEM_BARCODES);
        List<String> items = new ArrayList<>();
        for (int i = 0; i < itemBarcodes.length(); i++) {
            Integer itemBarcode = (Integer) itemBarcodes.get(i);
            items.add(itemBarcode.toString());
        }
        String requestFormatType = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.REQUEST_FORMAT_TYPE);

        String responseFormatType = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.RESPONSE_FORMAT_TYPE);


        OLERenewItemList oleRenewItemList = getCirculationRestService().renewItems(patronBarcode, operatorId, items);

         switch (responseFormatType){
            case "XML":
                return getResponseHandler().marshalObjectToXml(oleRenewItemList);
            case "JSON":
                return getResponseHandler().marshalObjectToJson(oleRenewItemList);
        }
        return responseString;
    }

    private ResponseHandler getResponseHandler() {
        if (null == responseHandler) {
            responseHandler = new RenewItemResponseHandler();
        }
        return responseHandler;
    }
    /* Rest POST Methods End*/


    public String getStringValueFromJsonObject(JSONObject jsonObject, String key) {
        String returnValue = null;
        try {
            returnValue = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public CirculationRestService getCirculationRestService() {
        return circulationRestService;
    }

    public void setCirculationRestService(CirculationRestService circulationRestService) {
        this.circulationRestService = circulationRestService;
    }
}
