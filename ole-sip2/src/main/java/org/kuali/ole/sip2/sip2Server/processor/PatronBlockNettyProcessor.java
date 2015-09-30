package org.kuali.ole.sip2.sip2Server.processor;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.bo.OLELookupUser;
import org.kuali.ole.converter.OLELookupUserConverter;
import org.kuali.ole.request.OLESIP2BlockPatronRequestParser;
import org.kuali.ole.response.OLESIP2PatronStatusResponse;
import org.kuali.ole.sip2.response.OLESIP2PatronStatusTurnedOffResponse;

import java.util.Properties;

/**
 * Created by chenchulakshmig on 9/29/15.
 */
public class PatronBlockNettyProcessor extends NettyProcessor {
    private final Properties properties;
    private String serverURL;

    public PatronBlockNettyProcessor(Properties properties, String serverURL) {
        this.properties = properties;
        this.serverURL = serverURL;
    }

    @Override
    public boolean isInterested(String code) {
        return code.equals("01");
    }

    @Override
    public String process(String requestData) {
        String response = "";
        OLESIP2BlockPatronRequestParser sip2BlockPatronRequestParser = new OLESIP2BlockPatronRequestParser(requestData);
        requestData = createJSONForPatronBlock(sip2BlockPatronRequestParser.getPatronIdentifier(), "SIP2_OPERATOR_ID", sip2BlockPatronRequestParser.getBlockedCardMessage());

        response = this.postRequest(requestData, "/patronBlockSIP2", serverURL);

        if (StringUtils.isNotBlank(response)) {
            OLELookupUser oleLookupUser = new OLELookupUserConverter().getLookupUser(response);
            OLESIP2PatronStatusResponse sip2PatronStatusResponse = new OLESIP2PatronStatusResponse();

            response = sip2PatronStatusResponse.getSIP2BlockPatronResponse(oleLookupUser,
                    sip2BlockPatronRequestParser, calculateTotalFineBalance(oleLookupUser));
        }
        return response;
    }

    private String createJSONForPatronBlock(String patronBarcode, String operatorId, String blockedCardMessage) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("patronBarcode", patronBarcode);
            jsonObject.put("operatorId", operatorId);
            jsonObject.put("blockedCardMessage", blockedCardMessage);
            jsonObject.put("requestFormatType", "JSON");
            jsonObject.put("responseFormatType", "XML");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public boolean isServiceTurnedOn() {
        return properties.getProperty("sip2.service.blockPatron").equalsIgnoreCase("yes");
    }

    @Override
    public String getResponseForServiceTurnedOff(String requestData) {
        OLESIP2PatronStatusTurnedOffResponse patronStatusTurnedOffResponse = new OLESIP2PatronStatusTurnedOffResponse();
        return patronStatusTurnedOffResponse.getOLESIP2PatronStatusTurnedOffResponse(requestData, "Patron Block Request");
    }
}
