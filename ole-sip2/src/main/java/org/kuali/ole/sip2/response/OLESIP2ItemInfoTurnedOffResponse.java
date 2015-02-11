package org.kuali.ole.sip2.response;


import org.kuali.ole.sip2.sip2Server.MessageUtil;

/**
 * Created by gayathria on 17/9/14.
 */
public class OLESIP2ItemInfoTurnedOffResponse extends OLESIP2TurnedOffResponse {


    public OLESIP2ItemInfoTurnedOffResponse() {
        this.code = "18";
    }


    public String getOLESIP2ItemInfoTurnedOffResponse(String requestData) {

        String[] requestDataArray = requestData.split("\\|");

        StringBuilder builder = new StringBuilder();
        builder.append(code);
        builder.append("08");
        builder.append("00");
        builder.append("01");
        builder.append(MessageUtil.getSipDateTime());
        builder.append("AB");
        for (String data : requestDataArray) {
            if (data.startsWith("AB")) {
                builder.append((data.replaceFirst("AB", "")).trim());
            }
        }
        builder.append("|AJ  ");
        /*for (String data : requestDataArray) {
            if (data.startsWith("AJ")) {
                builder.append((data.replaceFirst("|AJ", "")).trim());
            }
        }*/
        builder.append("|AF");
        builder.append("Item Information Service Currently turned Off");

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
