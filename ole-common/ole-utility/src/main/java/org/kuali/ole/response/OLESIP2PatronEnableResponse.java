package org.kuali.ole.response;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.bo.OLELookupUser;
import org.kuali.ole.constants.OLESIP2Constants;
import org.kuali.ole.response.OLESIP2Response;
import org.kuali.ole.common.MessageUtil;
import org.kuali.ole.common.OLESIP2Util;
import org.kuali.ole.request.OLESIP2PatronEnableRequestParser;

/**
 * Created by gayathria on 10/11/14.
 */
public class OLESIP2PatronEnableResponse extends OLESIP2Response {


    public OLESIP2PatronEnableResponse() {
        this.code = OLESIP2Constants.PATRON_ENABLE_RESPONSE;
    }

    public String getSIP2PatronEnableResponse(OLELookupUser oleLookupUser, OLESIP2PatronEnableRequestParser sip2PatronEnableRequestParser) {

        StringBuilder builder = new StringBuilder();
        builder.append(code);
        builder.append("YYYYYYYYYYYYYY");
        builder.append("001");
        builder.append(MessageUtil.getSipDateTime());
        builder.append(OLESIP2Constants.INSTITUTION_ID_CODE);
        builder.append(StringUtils.isNotBlank(sip2PatronEnableRequestParser.getInstitutionId()) ? sip2PatronEnableRequestParser.getInstitutionId() : "");
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PATRON_IDENTIFIER_CODE);
        builder.append(StringUtils.isNotBlank(oleLookupUser.getPatronId()) ? oleLookupUser.getPatronId() : sip2PatronEnableRequestParser.getPatronIdentifier());
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PERSONAL_NAME_CODE);
        builder.append(oleLookupUser.getPatronName() != null ? oleLookupUser.getPatronName().getFirstName() + " " + oleLookupUser.getPatronName().getLastName() : "");
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.VALID_PATRON_CODE);
        builder.append(OLESIP2Util.bool2Char(oleLookupUser.isValidPatron()));
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.SCREEN_MSG_CODE);
        builder.append(OLESIP2Constants.PATRON_ENABLED);
        if (sip2PatronEnableRequestParser.getSequenceNum() != null && !sip2PatronEnableRequestParser.getSequenceNum().equalsIgnoreCase("")) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SEQUENCE_NUM_CODE);
            builder.append(sip2PatronEnableRequestParser.getSequenceNum());
            builder.append(MessageUtil.computeChecksum(builder.toString()));
        }
        return builder.toString() + '\r';

    }
}
