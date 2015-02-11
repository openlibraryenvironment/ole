package org.kuali.ole.sip2.response;

import org.kuali.ole.sip2.sip2Server.MessageUtil;


/**
 * Created by gayathria on 1/9/14.
 */
public class OLESIP2CheckInTurnedOffResponse extends OLESIP2TurnedOffResponse {

    public OLESIP2CheckInTurnedOffResponse() {
        this.code = "10";
    }


    public String getOLESIP2CheckInTurnedOffResponse(String requestData) {

        String[] requestDataArray = requestData.split("\\|");


        StringBuilder builder = new StringBuilder();
        builder.append(code);

        builder.append("0");
        builder.append("N");
        builder.append("N");
        builder.append("N");

        builder.append(MessageUtil.getSipDateTime());
        builder.append("AO");
        builder.append("|AB");
        for (String data : requestDataArray) {
            if (data.startsWith("AB")) {
                builder.append((data.replaceFirst("AB", "")).trim());
            }
        }
        builder.append("|AQ");
        builder.append("|AFCheckIn service is currently turned off");
        for (String data : requestDataArray) {
            if (data.startsWith("AY")) {
                builder.append("|AY");
                builder.append(data.substring(2, 5));
                builder.append(MessageUtil.computeChecksum(builder.toString()));
            }
        }
        return builder.toString() + '\r';
    }

}
