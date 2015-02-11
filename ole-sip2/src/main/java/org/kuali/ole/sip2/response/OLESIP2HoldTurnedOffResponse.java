package org.kuali.ole.sip2.response;

import org.kuali.ole.sip2.sip2Server.MessageUtil;
import org.kuali.ole.sip2.sip2Server.StringUtil;

/**
 * Created by gayathria on 2/12/14.
 */
public class OLESIP2HoldTurnedOffResponse extends OLESIP2TurnedOffResponse {

    public OLESIP2HoldTurnedOffResponse() {
        code = "16";
    }

    public String getOLESIP2HoldTurnedOffResponse(String requestData) {

        StringBuilder builder = new StringBuilder();
        String[] requestDataArray = requestData.split("\\|");
        builder.append(this.code);

        builder.append(StringUtil.bool2Int(false));
        builder.append(StringUtil.bool2Char(false));


        builder.append(MessageUtil.getSipDateTime());


        builder.append("AO");
        builder.append("|AA");

        for (String data : requestDataArray) {
            if (data.startsWith("AA")) {
                builder.append((data.replaceFirst("AA", "")).trim());
            }
        }
        builder.append("|AF");
        builder.append("Hold Service Currently turned Off");

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
