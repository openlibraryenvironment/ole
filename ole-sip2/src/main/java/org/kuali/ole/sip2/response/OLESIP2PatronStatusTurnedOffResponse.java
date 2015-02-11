package org.kuali.ole.sip2.response;

import org.kuali.ole.sip2.sip2Server.MessageUtil;

/**
 * Created by gayathria on 17/9/14.
 */
public class OLESIP2PatronStatusTurnedOffResponse extends OLESIP2TurnedOffResponse {


    public OLESIP2PatronStatusTurnedOffResponse() {
        code = "24";
    }

    public String getOLESIP2PatronStatusTurnedOffResponse(String requestData, String requestType) {

        //941AY3AZFDFA<CR>
        String[] requestDataArray = requestData.split("\\|");
        StringBuilder builder = new StringBuilder();
        builder.append(code);
        builder.append("              ");
        builder.append("001");
        builder.append(MessageUtil.getSipDateTime());
        builder.append("AO");
        builder.append("|AA");
        for (String data : requestDataArray) {
            if (data.startsWith("AA")) {
                builder.append((data.replaceFirst("AA", "")).trim());
            }
        }
        builder.append("|AE");
        builder.append("|AF");
        builder.append(requestType + " Service Currently turned Off");

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
