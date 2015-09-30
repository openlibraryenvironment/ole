package org.kuali.ole.sip2.sip2Server.processor;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.request.OLESIP2ItemInformationRequestParser;
import org.kuali.ole.response.OLESIP2ItemInformationResponse;
import org.kuali.ole.sip2.response.OLESIP2ItemInfoTurnedOffResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by chenchulakshmig on 9/29/15.
 */
public class ItemInformationNettyProcessor extends NettyProcessor {
    private final Properties properties;
    private String serverURL;

    public ItemInformationNettyProcessor(Properties properties, String serverURL) {
        this.properties = properties;
        this.serverURL = serverURL;
    }

    @Override
    public boolean isInterested(String code) {
        return code.equals("17");
    }

    @Override
    public String process(String requestData) {
        String response = "";
        OLESIP2ItemInformationRequestParser sip2ItemInformationRequestParser = new OLESIP2ItemInformationRequestParser(requestData);
        requestData = createJSONForItemInfo(sip2ItemInformationRequestParser.getItemIdentifier());

        response = postRequest(requestData, "/itemInfoSIP2", serverURL);
        JSONObject jsonObject = null;
        Map responseMap = new HashMap<>();
        try {
            jsonObject = new JSONObject(response);
            if (null != jsonObject.keys()) {
                for (java.util.Iterator iterator = jsonObject.keys(); iterator.hasNext(); ) {
                    Object key = iterator.next();
                    try {
                        responseMap.put(key, jsonObject.get((String) key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (responseMap.size() > 0) {
            OLESIP2ItemInformationResponse sip2ItemInfoParser = new OLESIP2ItemInformationResponse();
            response = sip2ItemInfoParser.getSIP2ItemInfoResponse(responseMap, sip2ItemInformationRequestParser);
        }
        return response;
    }

    private String createJSONForItemInfo(String itemIdentifier) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("itemIdentifier", itemIdentifier);
            jsonObject.put("requestFormatType", "JSON");
            jsonObject.put("responseFormatType", "JSON");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public boolean isServiceTurnedOn() {
        return properties.getProperty("sip2.service.itemInformation").equalsIgnoreCase("yes");
    }

    @Override
    public String getResponseForServiceTurnedOff(String requestData) {
        OLESIP2ItemInfoTurnedOffResponse olesip2ItemInfoTurnedOffResponse = new OLESIP2ItemInfoTurnedOffResponse();
        return olesip2ItemInfoTurnedOffResponse.getOLESIP2ItemInfoTurnedOffResponse(requestData);
    }
}
