package org.kuali.ole.sip2.sip2Server.processor;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.bo.OLECheckOutItem;
import org.kuali.ole.converter.OLECheckOutItemConverter;
import org.kuali.ole.request.OLESIP2CheckOutRequestParser;
import org.kuali.ole.response.OLESIP2CheckOutResponse;
import org.kuali.ole.sip2.response.OLESIP2CheckOutTurnedOffResponse;

import java.util.Properties;

/**
 * Created by chenchulakshmig on 9/28/15.
 */
public class CheckoutNetttyProcessor extends NettyProcessor {

    private final Properties properties;
    private String serverURL;

    public CheckoutNetttyProcessor(Properties properties, String serverURL) {
        this.properties = properties;
        this.serverURL = serverURL;
    }

    @Override
    public boolean isInterested(String code) {
        return code.equals("11");
    }

    @Override
    public String process(String requestData) {
        String response = "";
        OLESIP2CheckOutRequestParser sip2CheckOutRequestParser = new OLESIP2CheckOutRequestParser(requestData);
        requestData = createJSONForCheckoutItemRequest(sip2CheckOutRequestParser.getPatronIdentifier(), sip2CheckOutRequestParser.getItemIdentifier(), "SIP2_OPERATOR_ID");

        response = postRequest(requestData, "/checkoutItemSIP2", serverURL);

        if (StringUtils.isNotBlank(response)) {
            OLECheckOutItem oleCheckOutItem = (OLECheckOutItem) new OLECheckOutItemConverter().generateCheckoutItemObject(response);
            OLESIP2CheckOutResponse sip2CheckOutResponseParser = new OLESIP2CheckOutResponse();
            response = sip2CheckOutResponseParser.getSIP2CheckOutResponse(oleCheckOutItem, sip2CheckOutRequestParser);
        }
        return response;
    }

    @Override
    public boolean isServiceTurnedOn() {
        return properties.getProperty("sip2.service.checkOut").equalsIgnoreCase("yes");
    }

    @Override
    public String getResponseForServiceTurnedOff(String requestData) {
        OLESIP2CheckOutTurnedOffResponse olesip2CheckOutTurnedOffResponse = new OLESIP2CheckOutTurnedOffResponse();
        return olesip2CheckOutTurnedOffResponse.getOLESIP2CheckOutTurnedOffResponse(requestData);
    }

    private String createJSONForCheckoutItemRequest(String patronBarcode, String itemBarcode, String operatorId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("patronBarcode", patronBarcode);
            jsonObject.put("itemBarcode", itemBarcode);
            jsonObject.put("operatorId", operatorId);
            jsonObject.put("requestFormatType", "JSON");
            jsonObject.put("responseFormatType", "XML");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
