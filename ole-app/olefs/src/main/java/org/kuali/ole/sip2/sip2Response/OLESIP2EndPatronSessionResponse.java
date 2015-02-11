package org.kuali.ole.sip2.sip2Response;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.sip2.common.MessageUtil;
import org.kuali.ole.sip2.constants.OLESIP2Constants;
import org.kuali.ole.sip2.requestParser.OLESIP2EndPatronSessionRequestParser;

/**
 * Created by gayathria on 2/12/14.
 */
public class OLESIP2EndPatronSessionResponse extends OLESIP2Response {
    public OLESIP2EndPatronSessionResponse() {
        code = OLESIP2Constants.END_PATRON_SESSION_RESPONSE;
    }

    public String getEndPatronSession(OLESIP2EndPatronSessionRequestParser sip2EndPatronSessionRequestParser) {

        StringBuilder endPatronSessionStringBuilder = new StringBuilder();
        endPatronSessionStringBuilder.append(code);
        endPatronSessionStringBuilder.append(OLESIP2Constants.Y);
        endPatronSessionStringBuilder.append(MessageUtil.getSipDateTime());
        endPatronSessionStringBuilder.append(OLESIP2Constants.INSTITUTION_ID_CODE);
        endPatronSessionStringBuilder.append(StringUtils.isNotBlank(sip2EndPatronSessionRequestParser.getInstitutionId()) ? sip2EndPatronSessionRequestParser.getInstitutionId() : "");
        endPatronSessionStringBuilder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PATRON_IDENTIFIER_CODE);
        endPatronSessionStringBuilder.append(sip2EndPatronSessionRequestParser.getPatronIdentifier());
        endPatronSessionStringBuilder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.SCREEN_MSG_CODE);
        endPatronSessionStringBuilder.append(OLESIP2Constants.PATRON_SESSION_END);
        endPatronSessionStringBuilder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PRINT_LINE_CODE);
        endPatronSessionStringBuilder.append(OLESIP2Constants.PATRON_SESSION_END);
        if (StringUtils.isNotBlank(sip2EndPatronSessionRequestParser.getSequenceNum())) {
            endPatronSessionStringBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SEQUENCE_NUM_CODE);
            endPatronSessionStringBuilder.append(sip2EndPatronSessionRequestParser.getSequenceNum());
            endPatronSessionStringBuilder.append(MessageUtil.computeChecksum(endPatronSessionStringBuilder.toString()));
        }

        return endPatronSessionStringBuilder.toString() + '\r';
    }
}
