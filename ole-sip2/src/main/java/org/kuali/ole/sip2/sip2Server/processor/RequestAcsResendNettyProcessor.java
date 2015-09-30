package org.kuali.ole.sip2.sip2Server.processor;

import org.apache.log4j.Logger;
import org.kuali.ole.sip2.sip2Server.MessageUtil;

import java.util.Map;

/**
 * Created by chenchulakshmig on 9/29/15.
 */
public class RequestAcsResendNettyProcessor extends NettyProcessor {

    private final static Logger LOG = Logger.getLogger(RequestAcsResendNettyProcessor.class.getName());

    private Map<String, String> lastResponseSendToClient;
    private String clientIP;

    public RequestAcsResendNettyProcessor(Map<String, String> lastResponseSendToClient, String clientIP) {
        this.lastResponseSendToClient = lastResponseSendToClient;
        this.clientIP = clientIP;
    }

    @Override
    public boolean isInterested(String code) {
        return code.equals("97");
    }

    @Override
    public String process(String requestData) {
        String response = "";
        if (lastResponseSendToClient.containsKey(clientIP)) {
            response = this.removeSeqNumFromResponse(lastResponseSendToClient.get(clientIP));
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("96AZ");
            builder.append(MessageUtil.computeChecksum(builder.toString()));
            response = builder.toString() + '\r';
        }
        return response;
    }

    private String removeSeqNumFromResponse(String lastResponse) {
        LOG.info("Inside OLENettyServerHandler.removeSeqNumFromResponse");
        StringBuilder builder = new StringBuilder();
        if (lastResponse.contains("|AY")) {
            int indexOfSeqNum = lastResponse.indexOf("|AY");
            String oldRespose = lastResponse.replace(lastResponse.substring(indexOfSeqNum), "");
            builder.append(oldRespose);
            builder.append("|AZ" + MessageUtil.computeChecksum(oldRespose));
        }
        LOG.info("Inside OLENettyServerHandler.removeSeqNumFromResponse   " + builder.toString());
        return builder.toString() + '\r';
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
