package org.kuali.ole.sip2.sip2Server.processor;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.bo.OLELookupUser;
import org.kuali.ole.converter.OLELookupUserConverter;
import org.kuali.ole.request.OLESIP2PatronStatusRequestParser;
import org.kuali.ole.response.OLESIP2PatronStatusResponse;
import org.kuali.ole.sip2.response.OLESIP2PatronStatusTurnedOffResponse;

import java.util.Properties;

/**
 * Created by chenchulakshmig on 9/29/15.
 */
public class PatronStatusNettyProcessor extends NettyProcessor {

    private final Properties properties;
    private String serverURL;

    public PatronStatusNettyProcessor(Properties properties, String serverURL) {
        this.properties = properties;
        this.serverURL = serverURL;
    }

    @Override
    public boolean isInterested(String code) {
        return code.equals("23");
    }

    @Override
    public String process(String requestData) {
        String response = "";
        OLESIP2PatronStatusRequestParser sip2PatronStatusRequestParser = new OLESIP2PatronStatusRequestParser(requestData);
        requestData = createJSONForLookupUser(sip2PatronStatusRequestParser.getPatronIdentifier(), "SIP2_OPERATOR_ID");

        response = this.postRequest(requestData, "/patronStatusSIP2", serverURL);

        if (StringUtils.isNotBlank(response)) {
            OLELookupUser oleLookupUser = new OLELookupUserConverter().getLookupUser(response);
            OLESIP2PatronStatusResponse sip2PatronStatusResponse = new OLESIP2PatronStatusResponse();

            response = sip2PatronStatusResponse.getSIP2PatronStatusResponse(oleLookupUser,
                    sip2PatronStatusRequestParser, calculateTotalFineBalance(oleLookupUser));
        }
        return response;
    }

    @Override
    public boolean isServiceTurnedOn() {
        return properties.getProperty("sip2.service.patronStatus").equalsIgnoreCase("yes");
    }

    @Override
    public String getResponseForServiceTurnedOff(String requestData) {
        OLESIP2PatronStatusTurnedOffResponse patronStatusTurnedOffResponse = new OLESIP2PatronStatusTurnedOffResponse();
        return patronStatusTurnedOffResponse.getOLESIP2PatronStatusTurnedOffResponse(requestData, "Patron Status Request");
    }

    private String createJSONForLookupUser(String patronBarcode, String operatorId) {
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

    public Properties getProperties() {
        return properties;
    }
}
