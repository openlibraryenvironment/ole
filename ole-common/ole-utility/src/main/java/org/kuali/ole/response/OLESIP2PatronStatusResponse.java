package org.kuali.ole.response;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.bo.OLELookupUser;
import org.kuali.ole.common.MessageUtil;
import org.kuali.ole.common.OLESIP2Util;
import org.kuali.ole.constants.OLESIP2Constants;
import org.kuali.ole.request.OLESIP2BlockPatronRequestParser;
import org.kuali.ole.request.OLESIP2PatronStatusRequestParser;

import java.math.BigDecimal;

/**
 * Created by gayathria on 17/9/14.
 */
public class OLESIP2PatronStatusResponse extends OLESIP2Response {


    public OLESIP2PatronStatusResponse() {
        code = OLESIP2Constants.PATRON_STATUS_RESPONSE;
    }

    public String getSIP2PatronStatusResponse(OLELookupUser oleLookupUser, OLESIP2PatronStatusRequestParser sip2PatronStatusRequestParser, BigDecimal balanceAmount) {

        StringBuilder builder = new StringBuilder();
        builder.append(OLESIP2Constants.PATRON_STATUS_RESPONSE);
        builder.append("              ");
        builder.append("001");
        builder.append(MessageUtil.getSipDateTime());
        builder.append(OLESIP2Constants.INSTITUTION_ID_CODE);
        builder.append(StringUtils.isNotBlank(sip2PatronStatusRequestParser.getInstitutionId()) ? sip2PatronStatusRequestParser.getInstitutionId() : "");
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PATRON_IDENTIFIER_CODE);
        builder.append(oleLookupUser.getPatronId() != null ? oleLookupUser.getPatronId() : sip2PatronStatusRequestParser.getPatronIdentifier());
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PERSONAL_NAME_CODE);
        builder.append(oleLookupUser.getPatronName() != null ? oleLookupUser.getPatronName().getFirstName() + " " + oleLookupUser.getPatronName().getLastName() : "");
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.VALID_PATRON_CODE);
        builder.append(OLESIP2Util.bool2Char(oleLookupUser.isValidPatron()));
        builder.append(OLESIP2Constants.SPLIT);
        builder.append(OLESIP2Constants.CURRENCY_TYPE_CODE);
        builder.append(OLESIP2Util.getDefaultCurrency().getCurrencyCode());
        if (oleLookupUser.getOleItemFines() != null) {
            builder.append(OLESIP2Constants.SPLIT);
            builder.append(OLESIP2Constants.FEE_AMOUNT_CODE);
            builder.append(balanceAmount);
        }
        if (StringUtils.isNotBlank(oleLookupUser.getMessage())) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SCREEN_MSG_CODE);
            builder.append(oleLookupUser.getMessage());
        }
        if (StringUtils.isNotBlank(sip2PatronStatusRequestParser.getSequenceNum())) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SEQUENCE_NUM_CODE);
            builder.append(sip2PatronStatusRequestParser.getSequenceNum());
            builder.append(MessageUtil.computeChecksum(builder.toString()));
        }
        return builder.toString() + '\r';
    }

    public String getSIP2BlockPatronResponse(OLELookupUser oleLookupUser, OLESIP2BlockPatronRequestParser sip2BlockPatronRequestParser, BigDecimal balanceAmount) {

        StringBuilder builder = new StringBuilder();
        builder.append(OLESIP2Constants.PATRON_STATUS_RESPONSE);
        builder.append("              ");
        builder.append("001");
        builder.append(MessageUtil.getSipDateTime());
        builder.append(OLESIP2Constants.INSTITUTION_ID_CODE);
        builder.append(StringUtils.isNotBlank(sip2BlockPatronRequestParser.getInstitutionId()) ? sip2BlockPatronRequestParser.getInstitutionId() : "");
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PATRON_IDENTIFIER_CODE);
        builder.append(oleLookupUser.getPatronId() != null ? oleLookupUser.getPatronId() : sip2BlockPatronRequestParser.getPatronIdentifier());
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PERSONAL_NAME_CODE);
        builder.append(oleLookupUser.getPatronName() != null ? oleLookupUser.getPatronName().getFirstName() + " " + oleLookupUser.getPatronName().getLastName() : "");
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.VALID_PATRON_CODE);
        builder.append(OLESIP2Util.bool2Char(oleLookupUser.isValidPatron()));
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.CURRENCY_TYPE_CODE);
        builder.append(OLESIP2Util.getDefaultCurrency().getCurrencyCode());
        if (oleLookupUser.getOleItemFines() != null) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.FEE_AMOUNT_CODE);
            builder.append(balanceAmount);
        }
        if (StringUtils.isNotBlank(oleLookupUser.getMessage())) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SCREEN_MSG_CODE);
            builder.append(oleLookupUser.getMessage());
        }
        if (StringUtils.isNotBlank(sip2BlockPatronRequestParser.getSequenceNum())) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SEQUENCE_NUM_CODE);
            builder.append(sip2BlockPatronRequestParser.getSequenceNum());
            builder.append(MessageUtil.computeChecksum(builder.toString()));
        }
        return builder.toString() + '\r';
    }


}
