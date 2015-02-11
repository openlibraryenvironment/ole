package org.kuali.ole.sip2.response;

import org.kuali.ole.sip2.sip2Server.MessageUtil;
import org.kuali.ole.sip2.sip2Server.StringUtil;

/**
 * Created by gayathria on 10/11/14.
 */
public class OLESIP2RenewAllTurnedOffResponse extends OLESIP2TurnedOffResponse {
    public OLESIP2RenewAllTurnedOffResponse() {
        this.code = "";
    }

    public String getOLESIP2RenewAllTurnedOffResponse(String requestData) {
        StringBuilder renewAllResponseBuilder = new StringBuilder();

        String[] requestDataArray = requestData.split("\\|");
        renewAllResponseBuilder.append(code);

        renewAllResponseBuilder.append(StringUtil.bool2Int(false));


        renewAllResponseBuilder.append(StringUtil.intToFixedLengthString(0, 4));
        renewAllResponseBuilder.append(StringUtil.intToFixedLengthString(0, 4));

        renewAllResponseBuilder.append(MessageUtil.getSipDateTime());

        renewAllResponseBuilder.append("AO");


        renewAllResponseBuilder.append("|AF");
        renewAllResponseBuilder.append("Renew All Service Currently turned Off");

        for (String data : requestDataArray) {
            if (data.startsWith("AY")) {
                renewAllResponseBuilder.append("|AY");
                renewAllResponseBuilder.append(data.substring(2, 5));
                renewAllResponseBuilder.append(MessageUtil.computeChecksum(renewAllResponseBuilder.toString()));
            }
        }
        return renewAllResponseBuilder.toString() + '\r';
    }
}
