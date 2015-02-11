package org.kuali.ole.sip2.sip2Response;

import org.kuali.ole.sip2.common.MessageUtil;
import org.kuali.ole.sip2.constants.OLESIP2Constants;
import org.kuali.ole.sip2.requestParser.OLESIP2LoginRequestParser;

/**
 * Created by gayathria on 2/9/14.
 */
public class OLESIP2LoginResponse extends OLESIP2Response {


    public OLESIP2LoginResponse() {
        this.code = OLESIP2Constants.LOGIN_RESPONSE;
    }

    public String getSIP2LoginResponse(Boolean validateUser, OLESIP2LoginRequestParser loginRequestParser) {

        StringBuilder builder = new StringBuilder();
        builder.append(code);
        if (validateUser) {
            builder.append("1");
        } else {
            builder.append("0");
        }
        if (loginRequestParser.getSequenceNum() != null && !loginRequestParser.getSequenceNum().equalsIgnoreCase("")) {
            builder.append(OLESIP2Constants.SEQUENCE_NUM_CODE);
            builder.append(loginRequestParser.getSequenceNum());
            builder.append(MessageUtil.computeChecksum(builder.toString()));
        }
        return builder.toString() + '\r';
    }


}
