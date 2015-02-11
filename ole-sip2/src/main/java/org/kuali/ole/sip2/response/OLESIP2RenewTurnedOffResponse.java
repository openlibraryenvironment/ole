package org.kuali.ole.sip2.response;

import org.kuali.ole.sip2.sip2Server.MessageUtil;
import org.kuali.ole.sip2.sip2Server.StringUtil;

/**
 * Created by gayathria on 10/11/14.
 */
public class OLESIP2RenewTurnedOffResponse extends OLESIP2TurnedOffResponse {
    public OLESIP2RenewTurnedOffResponse() {
        code = "30";
    }

    public String getOLESIP2RenewTurnedOffResponse(String requestData) {
        StringBuilder renewResponseBuilder = new StringBuilder();
        String[] requestDataArray = requestData.split("\\|");
        renewResponseBuilder.append(code);

        renewResponseBuilder.append(StringUtil.bool2Int(false));
        renewResponseBuilder.append(StringUtil.bool2Char(false));
        renewResponseBuilder.append("U");
        renewResponseBuilder.append(StringUtil.bool2Char(false));

        renewResponseBuilder.append(MessageUtil.getSipDateTime());
        renewResponseBuilder.append("AO");

        renewResponseBuilder.append("|AA");
        for (String data : requestDataArray) {
            if (data.startsWith("|AA")) {
                renewResponseBuilder.append((data.replaceFirst("AA", "")).trim());
            }
        }
        renewResponseBuilder.append("|AB");

        renewResponseBuilder.append("|AJ");

        renewResponseBuilder.append("|AH");

        renewResponseBuilder.append("|AFRenew Service Currently turned Off");

        for (String data : requestDataArray) {
            if (data.startsWith("AY")) {
                renewResponseBuilder.append("|AY");
                renewResponseBuilder.append(data.substring(2, 5));
                renewResponseBuilder.append(MessageUtil.computeChecksum(renewResponseBuilder.toString()));
            }
        }

        return renewResponseBuilder.toString() + '\r';
    }
}
