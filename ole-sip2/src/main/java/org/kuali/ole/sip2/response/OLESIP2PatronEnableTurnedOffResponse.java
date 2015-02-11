package org.kuali.ole.sip2.response;

import org.kuali.ole.sip2.sip2Server.MessageUtil;

/**
 * Created by gayathria on 10/11/14.
 */
public class OLESIP2PatronEnableTurnedOffResponse extends OLESIP2TurnedOffResponse {


    public OLESIP2PatronEnableTurnedOffResponse() {
        this.code = "26";
    }

    public String getOLESIP2PatronEnableTurnedOffResponse(String requestData) {
        StringBuilder builder = new StringBuilder();
        String[] requestDataArray = requestData.split("\\|");
        builder.append(code);
        builder.append("              ");
        builder.append("001");
        builder.append(MessageUtil.getSipDateTime());
        builder.append("AO ");
        builder.append("|AA");
        for (String data : requestDataArray) {
            if (data.startsWith("AA")) {
                builder.append((data.replaceFirst("AA", "")).trim());
            }
        }
        builder.append("|AE ");
        builder.append("|AF");
        builder.append("Patron Enabled Service Currently turned Off");

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
