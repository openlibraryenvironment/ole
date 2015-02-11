package org.kuali.ole.sip2.response;


import org.kuali.ole.sip2.sip2Server.MessageUtil;

/**
 * Created by gayathria on 2/9/14.
 */
public class OLESIP2LoginTurnedOffResponse extends OLESIP2TurnedOffResponse {


    public OLESIP2LoginTurnedOffResponse() {
        this.code = "94";
    }

    public String getOLESIP2LoginTurnedOffResponse(String requestData) {

        //941AY3AZFDFA<CR>
        String[] requestDataArray = requestData.split("\\|");
        StringBuilder builder = new StringBuilder();
        builder.append(code);
        builder.append("0");

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
