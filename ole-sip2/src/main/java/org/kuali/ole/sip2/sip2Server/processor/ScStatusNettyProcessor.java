package org.kuali.ole.sip2.sip2Server.processor;

import org.kuali.ole.request.OLESIP2SCStatusRequestParser;
import org.kuali.ole.response.OLESIP2ACSStatusResponse;

import java.util.Properties;

/**
 * Created by chenchulakshmig on 9/29/15.
 */
public class ScStatusNettyProcessor extends NettyProcessor {

    private final Properties properties;

    public ScStatusNettyProcessor(Properties properties) {
        this.properties = properties;
    }

    @Override
    public boolean isInterested(String code) {
        return code.equals("99");
    }

    @Override
    public String process(String requestData) {
        String response = "";
        OLESIP2ACSStatusResponse sip2ACSStatusResponse = new OLESIP2ACSStatusResponse();
        OLESIP2SCStatusRequestParser sip2SCStatusRequestParser = new OLESIP2SCStatusRequestParser(requestData);

        response = sip2ACSStatusResponse.getSIP2ACSStatusResponse(sip2SCStatusRequestParser, properties.getProperty("sip2.institution"));

        return response;
    }

    @Override
    public boolean isServiceTurnedOn() {
        return true;
    }

    @Override
    public String getResponseForServiceTurnedOff(String requestData) {
        return null;
    }
}
