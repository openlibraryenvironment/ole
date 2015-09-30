package org.kuali.ole.sip2.sip2Server.processor;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.bo.OLELookupUser;
import org.kuali.ole.converter.OLELookupUserConverter;
import org.kuali.ole.request.OLESIP2PatronEnableRequestParser;
import org.kuali.ole.response.OLESIP2PatronEnableResponse;
import org.kuali.ole.sip2.response.OLESIP2PatronEnableTurnedOffResponse;

import java.util.Properties;

/**
 * Created by chenchulakshmig on 9/29/15.
 */
public class PatronEnableNettyProcessor extends NettyProcessor {
    private final Properties properties;
    private String serverURL;

    public PatronEnableNettyProcessor(Properties properties, String serverURL) {
        this.properties = properties;
        this.serverURL = serverURL;
    }

    @Override
    public boolean isInterested(String code) {
        return code.equals("25");
    }

    @Override
    public String process(String requestData) {
        String response = "";
        OLESIP2PatronEnableRequestParser sip2PatronEnableRequestParser = new OLESIP2PatronEnableRequestParser(requestData);
        requestData = createJSONForPatronEnable(sip2PatronEnableRequestParser.getPatronIdentifier(), "SIP2_OPERATOR_ID");

        response = postRequest(requestData, "/patronEnableSIP2", serverURL);

        if (StringUtils.isNotBlank(response)) {
            OLELookupUser oleLookupUser = new OLELookupUserConverter().getLookupUser(response);
            OLESIP2PatronEnableResponse sip2PatronEnableResponse = new OLESIP2PatronEnableResponse();

            response = sip2PatronEnableResponse.getSIP2PatronEnableResponse(oleLookupUser, sip2PatronEnableRequestParser);
        }
        return response;
    }

    private String createJSONForPatronEnable(String patronBarcode, String operatorId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("patronBarcode", patronBarcode);
            jsonObject.put("operatorId", operatorId);
            jsonObject.put("requestFormatType", "JSON");
            jsonObject.put("responseFormatType", "XML");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public boolean isServiceTurnedOn() {
        return properties.getProperty("sip2.service.patronEnable").equalsIgnoreCase("yes");
    }

    @Override
    public String getResponseForServiceTurnedOff(String requestData) {
        OLESIP2PatronEnableTurnedOffResponse patronEnableTurnedOffResponse = new OLESIP2PatronEnableTurnedOffResponse();
        return patronEnableTurnedOffResponse.getOLESIP2PatronEnableTurnedOffResponse(requestData);
    }
}
