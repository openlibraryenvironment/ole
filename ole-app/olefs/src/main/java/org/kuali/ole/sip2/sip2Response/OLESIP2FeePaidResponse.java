package org.kuali.ole.sip2.sip2Response;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.sip2.common.MessageUtil;
import org.kuali.ole.sip2.common.OLESIP2Util;
import org.kuali.ole.sip2.constants.OLESIP2Constants;
import org.kuali.ole.sip2.requestParser.OLESIP2FeePaidRequestParser;

/**
 * Created by gayathria on 15/12/14.
 */
public class OLESIP2FeePaidResponse extends OLESIP2Response {

    public OLESIP2FeePaidResponse() {
        code = OLESIP2Constants.FEE_PAID_RESPONSE;
    }

    public String getFeePaidResponse(String message, OLESIP2FeePaidRequestParser sip2FeePaidRequestParser) {
        StringBuilder sip2FeePaidResponseBuilder = new StringBuilder();
        sip2FeePaidResponseBuilder.append(code);
        if (message.equalsIgnoreCase(OLESIP2Constants.FEE_PAID)) {
            sip2FeePaidResponseBuilder.append(OLESIP2Util.bool2Char(true));
        } else {
            sip2FeePaidResponseBuilder.append(OLESIP2Util.bool2Char(false));
        }
        sip2FeePaidResponseBuilder.append(MessageUtil.getSipDateTime());
        sip2FeePaidResponseBuilder.append(OLESIP2Constants.INSTITUTION_ID_CODE);
        sip2FeePaidResponseBuilder.append(sip2FeePaidRequestParser.getInstitutionId() != null ? sip2FeePaidRequestParser.getInstitutionId() : "");
        sip2FeePaidResponseBuilder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PATRON_IDENTIFIER_CODE);
        sip2FeePaidResponseBuilder.append(sip2FeePaidRequestParser.getPatronIdentifier());
        if (StringUtils.isNotBlank(sip2FeePaidRequestParser.getTransactionId())) {
            sip2FeePaidResponseBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.TRANSACTION_ID_CODE);
            sip2FeePaidResponseBuilder.append(sip2FeePaidRequestParser.getTransactionId());
        }
        if (StringUtils.isNotBlank(message)) {
            sip2FeePaidResponseBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SCREEN_MSG_CODE);
            sip2FeePaidResponseBuilder.append(message);
        } else {
            sip2FeePaidResponseBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SCREEN_MSG_CODE);
            sip2FeePaidResponseBuilder.append(OLESIP2Constants.SERVICE_ERROR);
        }
        if (StringUtils.isNotBlank(sip2FeePaidRequestParser.getSequenceNum())) {
            sip2FeePaidResponseBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SEQUENCE_NUM_CODE);
            sip2FeePaidResponseBuilder.append(sip2FeePaidRequestParser.getSequenceNum());
            sip2FeePaidResponseBuilder.append(MessageUtil.computeChecksum(sip2FeePaidResponseBuilder.toString()));
        }
        return sip2FeePaidResponseBuilder.toString() + '\r';
    }
}

