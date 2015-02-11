package org.kuali.ole.sip2.response;


import org.kuali.ole.sip2.sip2Server.MessageUtil;
import org.kuali.ole.sip2.sip2Server.StringUtil;

/**
 * Created by gayathria on 17/9/14.
 */
public class OLESIP2PatronInformationTurnedOffResponse extends OLESIP2TurnedOffResponse {


    public OLESIP2PatronInformationTurnedOffResponse() {
        code = "64";
    }


    public String getOLESIP2PatronInformationTurnedOffResponse(String requestData) {

        //941AY3AZFDFA<CR>
        String[] requestDataArray = requestData.split("\\|");
        StringBuilder builder = new StringBuilder();
        builder.append(code);
        builder.append("              ");
        builder.append("001");
        builder.append(MessageUtil.getSipDateTime());

        builder.append(StringUtil.intToFixedLengthString(0, 4));
        builder.append(StringUtil.intToFixedLengthString(0, 4));
        builder.append(StringUtil.intToFixedLengthString(0, 4));
        builder.append(StringUtil.intToFixedLengthString(0, 4));
        builder.append(StringUtil.intToFixedLengthString(0, 4));
        builder.append(StringUtil.intToFixedLengthString(0, 4));

        builder.append("AO");
        builder.append("|AA");
        for (String data : requestDataArray) {
            if (data.startsWith("AA")) {
                builder.append((data.replaceFirst("AA", "")).trim());
            }
        }
        builder.append("|");
        builder.append("AE");

        builder.append("|AF");
        builder.append("Patron Information Service Currently turned Off");

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
