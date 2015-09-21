package org.kuali.asr.handler;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.ncip.bo.OLENCIPConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pvsubrah on 6/26/15.
 */
public class RenewItemRequestHandler extends RequestHandler {

    public Map parseRequest(JSONObject jsonObject) throws JSONException {
        String patronBarcode = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.PATRON_BARCODE);
        JSONArray itemBarcodes = (JSONArray) jsonObject.get(OLENCIPConstants.CIRC_ITEM_BARCODES);
        List<String> items = new ArrayList<>();
        for (int i = 0; i < itemBarcodes.length(); i++) {
            Object object = itemBarcodes.get(i);
            String itemBarcode = getStringValue(object);
            items.add(itemBarcode);
        }
        String requestFormatType = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.REQUEST_FORMAT_TYPE);
        String responseFormatType = getStringValueFromJsonObject(jsonObject, OLENCIPConstants.RESPONSE_FORMAT_TYPE);

        Map renewParameters = new HashMap();
        renewParameters.put("patronBarcode", patronBarcode);
        renewParameters.put("requestFormatType", requestFormatType);
        renewParameters.put("responseFormatType", responseFormatType);
        renewParameters.put("itemBarcodes", items);
        return renewParameters;
    }

    private String getStringValue(Object object) {
        String itemBarcode;
        if(object instanceof Integer)
            itemBarcode = ((Integer)object).toString();
        else if(object instanceof Boolean)
            itemBarcode = ((Boolean)object).toString();
        else if(object instanceof Double)
            itemBarcode = ((Double)object).toString();
        else
            itemBarcode = (String)object;
        return itemBarcode;
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
