package org.kuali.ole.sip2.response;

import org.kuali.ole.sip2.sip2Server.MessageUtil;
import org.kuali.ole.sip2.sip2Server.StringUtil;

/**
 * Created by gayathria on 15/12/14.
 */
public class OLESIP2FeePaidTurnedOffResponse extends OLESIP2TurnedOffResponse {

    public OLESIP2FeePaidTurnedOffResponse() {
        code = "38";
    }

    public String getOLESIP2FeePaidTurnedOffResponse(String requestData) {

        String[] requestDataArray = requestData.split("\\|");
        StringBuilder sip2FeePaidResponseBuilder = new StringBuilder();
        sip2FeePaidResponseBuilder.append(code);
        sip2FeePaidResponseBuilder.append(StringUtil.bool2Char(false));
        sip2FeePaidResponseBuilder.append(MessageUtil.getSipDateTime());
        sip2FeePaidResponseBuilder.append("AO");
        sip2FeePaidResponseBuilder.append("|AA");
        for (String data : requestDataArray) {
            if (data.startsWith("AA")) {
                sip2FeePaidResponseBuilder.append((data.replaceFirst("AA", "")).trim());
            }
        }

        sip2FeePaidResponseBuilder.append("|AF");
        sip2FeePaidResponseBuilder.append("Fee Service Currently turned Off");


        for (String data : requestDataArray) {
            if (data.startsWith("AY")) {
                sip2FeePaidResponseBuilder.append("|AY");
                sip2FeePaidResponseBuilder.append(data.substring(2, 5));
                sip2FeePaidResponseBuilder.append(MessageUtil.computeChecksum(sip2FeePaidResponseBuilder.toString()));
            }
        }

        return sip2FeePaidResponseBuilder.toString() + '\r';

    }

}
