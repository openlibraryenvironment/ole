package org.kuali.asr.handler;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.ncip.bo.OLENCIPConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenchulakshmig on 9/29/15.
 */
public class LoginRequestHandler extends RequestHandler {

    public Map parseRequest(JSONObject jsonObject) throws JSONException {
        String loginUserId = getStringValueFromJsonObject(jsonObject, "loginUserId");
        String loginPassword = getStringValueFromJsonObject(jsonObject, "loginPassword");

        String requestFormatType = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.REQUEST_FORMAT_TYPE);
        String responseFormatType = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.RESPONSE_FORMAT_TYPE);

        Map lookupUserParameters = new HashMap();
        lookupUserParameters.put("loginUserId", loginUserId);
        lookupUserParameters.put("loginPassword", loginPassword);
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
