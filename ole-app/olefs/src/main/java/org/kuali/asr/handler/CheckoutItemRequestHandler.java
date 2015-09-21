package org.kuali.asr.handler;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.ncip.bo.OLENCIPConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenchulakshmig on 8/24/15.
 */
public class CheckoutItemRequestHandler extends RequestHandler{

    public Map parseRequest(JSONObject jsonObject) throws JSONException {
        String patronBarcode = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.PATRON_BARCODE);
        String itemBarcode = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.ITEM_BARCODE);
        String operatorId = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.OPERATOR_ID);
        
        String requestFormatType = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.REQUEST_FORMAT_TYPE);
        String responseFormatType = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.RESPONSE_FORMAT_TYPE);

        Map checkoutParameters = new HashMap();
        checkoutParameters.put("patronBarcode", patronBarcode);
        checkoutParameters.put("itemBarcode", itemBarcode);
        checkoutParameters.put("operatorId", operatorId);
        checkoutParameters.put("requestFormatType", requestFormatType);
        checkoutParameters.put("responseFormatType", responseFormatType);
        return checkoutParameters;
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
