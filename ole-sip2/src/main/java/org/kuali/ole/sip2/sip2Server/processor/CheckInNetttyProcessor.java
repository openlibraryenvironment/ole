package org.kuali.ole.sip2.sip2Server.processor;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.bo.OLECheckInItem;
import org.kuali.ole.converter.OLECheckInItemConverter;
import org.kuali.ole.request.OLESIP2CheckInRequestParser;
import org.kuali.ole.response.OLESIP2CheckInResponse;
import org.kuali.ole.sip2.response.OLESIP2CheckInTurnedOffResponse;

import java.util.Properties;

/**
 * Created by chenchulakshmig on 9/28/15.
 */
public class CheckInNetttyProcessor extends NettyProcessor {

    private final Properties properties;
    private String serverURL;

    public CheckInNetttyProcessor(Properties properties, String serverURL) {
        this.properties = properties;
        this.serverURL = serverURL;
    }

    @Override
    public boolean isInterested(String code) {
        return code.equals("09");
    }

    @Override
    public String process(String requestData) {
        String response = "";
        OLESIP2CheckInRequestParser sip2CheckInRequestParser = new OLESIP2CheckInRequestParser(requestData);
        requestData = createJSONForCheckinItemRequest(sip2CheckInRequestParser.getItemIdentifier(), "SIP2_OPERATOR_ID", "N");

        response = postRequest(requestData, "/checkinItemSIP2", serverURL);

        if (StringUtils.isNotBlank(response)) {
            OLECheckInItem oleCheckInItem = (OLECheckInItem) new OLECheckInItemConverter().generateCheckInItemObject(response);
            OLESIP2CheckInResponse sip2CheckInResponseParser = new OLESIP2CheckInResponse();
            response = sip2CheckInResponseParser.getSIP2CheckInResponse(oleCheckInItem, sip2CheckInRequestParser);
        }
        return response;
    }

    @Override
    public boolean isServiceTurnedOn() {
        return properties.getProperty("sip2.service.checkIn").equalsIgnoreCase("yes");
    }

    @Override
    public String getResponseForServiceTurnedOff(String requestData) {
        OLESIP2CheckInTurnedOffResponse olesip2CheckInTurnedOffResponse = new OLESIP2CheckInTurnedOffResponse();
        return olesip2CheckInTurnedOffResponse.getOLESIP2CheckInTurnedOffResponse(requestData);
    }

    private String createJSONForCheckinItemRequest(String itemBarcode, String operatorId, String deleteIndicator) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("itemBarcode", itemBarcode);
            jsonObject.put("operatorId", operatorId);
            jsonObject.put("deleteIndicator", deleteIndicator);
            jsonObject.put("requestFormatType", "JSON");
            jsonObject.put("responseFormatType", "XML");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
