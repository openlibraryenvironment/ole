package org.kuali.ole.sip2.sip2Server.processor;

import org.kuali.ole.request.OLESIP2EndPatronSessionRequestParser;
import org.kuali.ole.response.OLESIP2EndPatronSessionResponse;
import org.kuali.ole.sip2.response.OLESIP2EndPatronSessionTurnedOffResponse;

import java.util.Properties;

/**
 * Created by chenchulakshmig on 9/29/15.
 */
public class EndPatronSessionNettyProcessor extends NettyProcessor {
    private final Properties properties;

    public EndPatronSessionNettyProcessor(Properties properties) {
        this.properties = properties;
    }

    @Override
    public boolean isInterested(String code) {
        return code.equals("35");
    }

    @Override
    public String process(String requestData) {
        String response = "";
        OLESIP2EndPatronSessionRequestParser sip2EndPatronSessionRequestParser = new OLESIP2EndPatronSessionRequestParser(requestData);
        OLESIP2EndPatronSessionResponse sip2EndPatronSessionResponse = new OLESIP2EndPatronSessionResponse();

        response = sip2EndPatronSessionResponse.getEndPatronSession(sip2EndPatronSessionRequestParser);

        return response;
    }

    @Override
    public boolean isServiceTurnedOn() {
        return properties.getProperty("sip2.service.endPatronSession").equalsIgnoreCase("yes");
    }

    @Override
    public String getResponseForServiceTurnedOff(String requestData) {
        OLESIP2EndPatronSessionTurnedOffResponse endPatronSessionTurnedOffResponse = new OLESIP2EndPatronSessionTurnedOffResponse();
        return endPatronSessionTurnedOffResponse.getOLESIP2EndPatronSessionTurnedOffResponse(requestData);
    }
}
