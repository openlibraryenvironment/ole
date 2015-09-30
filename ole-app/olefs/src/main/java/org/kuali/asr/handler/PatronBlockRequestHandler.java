package org.kuali.asr.handler;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.ncip.bo.OLENCIPConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenchulakshmig on 9/30/15.
 */
public class PatronBlockRequestHandler extends RequestHandler {

    public Map parseRequest(JSONObject jsonObject) throws JSONException {
        String patronBarcode = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.PATRON_BARCODE);
        String operatorId = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.OPERATOR_ID);
        String blockedCardMessage = getStringValueFromJsonObject(jsonObject, "blockedCardMessage");

        String requestFormatType = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.REQUEST_FORMAT_TYPE);
        String responseFormatType = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.RESPONSE_FORMAT_TYPE);

        Map lookupUserParameters = new HashMap();
        lookupUserParameters.put("patronBarcode", patronBarcode);
        lookupUserParameters.put("operatorId", operatorId);
        lookupUserParameters.put("blockedCardMessage", blockedCardMessage);
        lookupUserParameters.put("requestFormatType", requestFormatType);
        lookupUserParameters.put("responseFormatType", responseFormatType);
        return lookupUserParameters;
    }

    public String getStringValueFromJsonObject(JSONObject jsonObject, String key) {
        String returnValue = null;
        try {
            returnValue = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

}
