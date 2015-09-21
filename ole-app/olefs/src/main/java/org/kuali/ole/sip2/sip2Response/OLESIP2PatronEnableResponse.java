package org.kuali.ole.sip2.sip2Response;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.response.OLESIP2Response;
import org.kuali.ole.common.MessageUtil;
import org.kuali.ole.common.OLESIP2Util;
import org.kuali.ole.sip2.constants.OLESIP2Constants;
import org.kuali.ole.sip2.requestParser.OLESIP2PatronEnableRequestParser;

/**
 * Created by gayathria on 10/11/14.
 */
public class OLESIP2PatronEnableResponse extends OLESIP2Response {


    public OLESIP2PatronEnableResponse() {
        this.code = OLESIP2Constants.PATRON_ENABLE_RESPONSE;
    }

    public String getSIP2PatronEnableResponse(OlePatronDocument olePatronDocument, OLESIP2PatronEnableRequestParser sip2PatronEnableRequestParser, boolean isValidPatron) {

        StringBuilder builder = new StringBuilder();
        builder.append(code);
        builder.append("YYYYYYYYYYYYYY");
        builder.append("001");
        builder.append(MessageUtil.getSipDateTime());
        builder.append(OLESIP2Constants.INSTITUTION_ID_CODE);
        builder.append(StringUtils.isNotBlank(sip2PatronEnableRequestParser.getInstitutionId()) ? sip2PatronEnableRequestParser.getInstitutionId() : "");
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PATRON_IDENTIFIER_CODE);
        builder.append(StringUtils.isNotBlank(olePatronDocument.getBarcode()) ? olePatronDocument.getBarcode() : sip2PatronEnableRequestParser.getPatronIdentifier());
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PERSONAL_NAME_CODE);
        builder.append(olePatronDocument.getEntity().getNames().get(0).getFirstName() + " " + olePatronDocument.getEntity().getNames().get(0).getLastName());
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.VALID_PATRON_CODE);
        builder.append(OLESIP2Util.bool2Char(isValidPatron));
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








    /*
      StringBuilder builder = new StringBuilder();
        builder.append(code);
        builder.append(status);

        builder.append(this.language);
        builder.append(this.transactionDate);
        builder.append("AO");
        builder.append(this.institutionId);
        builder.append("|AA");
        builder.append(this.patronIdentifier);
        builder.append("|AE");
        builder.append(this.personalName);

        if (validPatronUsed) {
            builder.append("|BL");
            builder.append(StringUtil.bool2Char(validPatron));
        }
        if (validPatronPasswordUsed) {
            builder.append("|CQ");
            builder.append(StringUtil.bool2Char(validPatronPassword));
        }

        for (String msg : screenMessage) {
            builder.append("|AF");
            builder.append(msg);
        }

        for (String msg : printLine) {
            builder.append("|AG");
            builder.append(msg);
        }

        builder.append("|");

        if (isSequence()) {
            builder.append("AY");
            builder.append(sequence);
        }
        builder.append("AZ");
        return MessageUtil.computeChecksum(builder.toString());
     */
}
