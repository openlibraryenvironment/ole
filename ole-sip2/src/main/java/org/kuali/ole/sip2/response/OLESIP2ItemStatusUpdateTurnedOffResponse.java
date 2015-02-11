package org.kuali.ole.sip2.response;

import org.kuali.ole.sip2.sip2Server.MessageUtil;
import org.kuali.ole.sip2.sip2Server.StringUtil;

/**
 * Created by gayathria on 15/12/14.
 */
public class OLESIP2ItemStatusUpdateTurnedOffResponse extends OLESIP2TurnedOffResponse {

    public OLESIP2ItemStatusUpdateTurnedOffResponse() {
        code = "20";
    }

    public String getOLESIP2ItemStatusUpdateTurnedOffResponse(String requestData) {

        StringBuilder builder = new StringBuilder();
        String[] requestDataArray = requestData.split("\\|");
        builder.append(this.code);
        builder.append(StringUtil.bool2Int(false));
        builder.append(MessageUtil.getSipDateTime());
        builder.append("AB");
        for (String data : requestDataArray) {
            if (data.startsWith("AB")) {
                builder.append((data.replaceFirst("AB", "")).trim());
            }
        }
        builder.append("|AF");
        builder.append("Item Status Update Service - Not supported in OLE");
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
