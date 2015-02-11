package org.kuali.ole.sip2.sip2Response;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.ncip.bo.OLERenewItem;
import org.kuali.ole.sip2.common.MessageUtil;
import org.kuali.ole.sip2.common.OLESIP2Util;
import org.kuali.ole.sip2.constants.OLESIP2Constants;
import org.kuali.ole.sip2.requestParser.OLESIP2RenewRequestParser;

/**
 * Created by gayathria on 10/11/14.
 */
public class OLESIP2RenewResponse extends OLESIP2Response {
    public OLESIP2RenewResponse() {
        code = OLESIP2Constants.RENEW_RESPONSE;
    }

    public String getSIP2RenewResponse(OLERenewItem oleRenewItem, OLESIP2RenewRequestParser sip2RenewRequestParser) {
        StringBuilder renewResponseBuilder = new StringBuilder();
        renewResponseBuilder.append(code);
        if (oleRenewItem.getCode().equalsIgnoreCase("030") || oleRenewItem.getMessage().equalsIgnoreCase(OLESIP2Constants.CHECK_OUT_SUCCESS)) {
            renewResponseBuilder.append(OLESIP2Util.bool2Int(true));
            renewResponseBuilder.append(OLESIP2Util.bool2Char(false));
            renewResponseBuilder.append("U");
            renewResponseBuilder.append(OLESIP2Util.bool2Char(true));
        } else {
            renewResponseBuilder.append(OLESIP2Util.bool2Int(false));
            renewResponseBuilder.append(OLESIP2Util.bool2Char(false));
            renewResponseBuilder.append("U");
            renewResponseBuilder.append(OLESIP2Util.bool2Char(false));
        }
        renewResponseBuilder.append(MessageUtil.getSipDateTime());
        renewResponseBuilder.append(OLESIP2Constants.INSTITUTION_ID_CODE);
        renewResponseBuilder.append(StringUtils.isNotBlank(sip2RenewRequestParser.getInstitutionId()) ? sip2RenewRequestParser.getInstitutionId() : "");
        renewResponseBuilder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PATRON_IDENTIFIER_CODE);
        renewResponseBuilder.append(oleRenewItem.getPatronBarcode() != null ? oleRenewItem.getPatronBarcode() : sip2RenewRequestParser.getPatronIdentifier());
        renewResponseBuilder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.ITEM_IDENTIFIER_CODE);
        renewResponseBuilder.append(oleRenewItem.getItemBarcode() != null ? oleRenewItem.getItemBarcode() : sip2RenewRequestParser.getItemIdentifier());
        renewResponseBuilder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.TITLE_IDENTIFIER_CODE);
        renewResponseBuilder.append(StringUtils.isNotBlank(oleRenewItem.getTitleIdentifier()) ? oleRenewItem.getTitleIdentifier() : "");
        renewResponseBuilder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.DUE_DATE_CODE);
        renewResponseBuilder.append(StringUtils.isNotBlank(oleRenewItem.getNewDueDate()) ? oleRenewItem.getNewDueDate() : "");
        if (OLESIP2Util.getDefaultCurrency() != null) {
            renewResponseBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.CURRENCY_TYPE_CODE);
            renewResponseBuilder.append(OLESIP2Util.getDefaultCurrency().getCurrencyCode());
        }
        if (StringUtils.isNotBlank(oleRenewItem.getMessage())) {
            renewResponseBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SCREEN_MSG_CODE + oleRenewItem.getMessage().replaceAll("<br/>", ""));
            renewResponseBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.PRINT_LINE_CODE + oleRenewItem.getMessage().replaceAll("<br/>", ""));
        }
        if (StringUtils.isNotBlank(sip2RenewRequestParser.getSequenceNum())) {
            renewResponseBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SEQUENCE_NUM_CODE);
            renewResponseBuilder.append(sip2RenewRequestParser.getSequenceNum());
            renewResponseBuilder.append(MessageUtil.computeChecksum(sip2RenewRequestParser.toString()));
        }

        return renewResponseBuilder.toString() + '\r';
    }
}
