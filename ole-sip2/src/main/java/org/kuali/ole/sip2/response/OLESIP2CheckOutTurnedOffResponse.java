package org.kuali.ole.sip2.response;


import org.kuali.ole.sip2.sip2Server.MessageUtil;
import org.kuali.ole.sip2.sip2Server.StringUtil;

/**
 * Created by gayathria on 27/8/14.
 */
public class OLESIP2CheckOutTurnedOffResponse extends OLESIP2TurnedOffResponse {

    public OLESIP2CheckOutTurnedOffResponse() {
        this.code = "12";
    }

    public String getOLESIP2CheckOutTurnedOffResponse(String requestData) {

        String[] requestDataArray = requestData.split("\\|");
        StringBuilder checkOutResponseBuilder = new StringBuilder();
        checkOutResponseBuilder.append(code);

        checkOutResponseBuilder.append(StringUtil.bool2Int(false));
        checkOutResponseBuilder.append(StringUtil.bool2Char(false));
        checkOutResponseBuilder.append("U");
        checkOutResponseBuilder.append(StringUtil.bool2Char(false));

        checkOutResponseBuilder.append(MessageUtil.getSipDateTime());
        checkOutResponseBuilder.append("AO");

        checkOutResponseBuilder.append("|AA");
        for (String data : requestDataArray) {
            if (data.startsWith("AA")) {
                checkOutResponseBuilder.append((data.replaceFirst("AA", "")).trim());
            }
        }
        checkOutResponseBuilder.append("|AB");
        for (String data : requestDataArray) {
            if (data.startsWith("AB")) {
                checkOutResponseBuilder.append((data.replaceFirst("AB", "")).trim());
            }
        }

        checkOutResponseBuilder.append("|AJ");
        checkOutResponseBuilder.append("|AH");


        checkOutResponseBuilder.append("|AFCheckOut service is currently turned off");


        for (String data : requestDataArray) {
            if (data.startsWith("AY")) {
                checkOutResponseBuilder.append("|AY");
                checkOutResponseBuilder.append(data.substring(2, 5));
                checkOutResponseBuilder.append(MessageUtil.computeChecksum(checkOutResponseBuilder.toString()));
            }
        }

        return checkOutResponseBuilder.toString() + '\r';

    }
}
