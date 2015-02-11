package org.kuali.ole.sip2.response;

import org.kuali.ole.sip2.sip2Server.MessageUtil;

/**
 * Created by gayathria on 2/12/14.
 */
public class OLESIP2EndPatronSessionTurnedOffResponse extends OLESIP2TurnedOffResponse {

    public OLESIP2EndPatronSessionTurnedOffResponse() {
        code = "36";
    }

    public String getOLESIP2EndPatronSessionTurnedOffResponse(String requestData) {
        String[] requestDataArray = requestData.split("\\|");
        StringBuilder endPatronSessionStringBuilder = new StringBuilder();
        endPatronSessionStringBuilder.append(code);
        endPatronSessionStringBuilder.append("N");
        endPatronSessionStringBuilder.append(MessageUtil.getSipDateTime());
        endPatronSessionStringBuilder.append("AO");
        endPatronSessionStringBuilder.append("|AA");
        for (String data : requestDataArray) {
            if (data.startsWith("AA")) {
                endPatronSessionStringBuilder.append((data.replaceFirst("AA", "")).trim());
            }
        }
        endPatronSessionStringBuilder.append("|AF");
        endPatronSessionStringBuilder.append("End Patron Session Service Currently turned Off");

        for (String data : requestDataArray) {
            if (data.startsWith("AY")) {
                endPatronSessionStringBuilder.append("|AY");
                endPatronSessionStringBuilder.append(data.substring(2, 5));
                endPatronSessionStringBuilder.append(MessageUtil.computeChecksum(endPatronSessionStringBuilder.toString()));
            }
        }


        return endPatronSessionStringBuilder.toString() + '\r';

    }


}
