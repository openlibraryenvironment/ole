package org.kuali.ole.request;

import org.apache.log4j.Logger;
import org.kuali.ole.constants.OLESIP2Constants;

/**
 * Created by gayathria on 2/12/14.
 */
public class OLESIP2BlockPatronRequestParser extends OLESIP2RequestParser {

    private static final Logger LOG = Logger.getLogger(OLESIP2BlockPatronRequestParser.class);

    private Boolean cardRetained;
    private String blockedCardMessage;

    public OLESIP2BlockPatronRequestParser(String requestData) {
        this.parseBlockPatron(requestData);
    }

    public void parseBlockPatron(String requestData) {

        LOG.info("Entry OLESIP2BlockPatronRequestParser.parseBlockPatron >>> " + requestData);
        String[] requestDataArray = requestData.split("\\|");
        try {
            for (String data : requestDataArray) {
                if (data.startsWith(OLESIP2Constants.BLOCK_PATRON_REQUEST)) {
                    code = data.substring(0, 2);
                    cardRetained = charToBool(data.charAt(2));
                    transactionDate = data.substring(3, 21);
                    institutionId = data.substring(23);
                }
                if (data.startsWith(OLESIP2Constants.BLOCKED_CARD_MSG)) {
                    blockedCardMessage = (data.replaceFirst(OLESIP2Constants.BLOCKED_CARD_MSG, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.PATRON_IDENTIFIER_CODE)) {
                    patronIdentifier = (data.replaceFirst(OLESIP2Constants.PATRON_IDENTIFIER_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.TERMINAL_PWD_CODE)) {
                    terminalPassword = (data.replaceFirst(OLESIP2Constants.TERMINAL_PWD_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.SEQUENCE_NUM_CODE)) {
                    sequenceNum = data.substring(2, 5);
                    checkSum = data.substring(5);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("Exit OLESIP2BlockPatronRequestParser.parseBlockPatron(String requestData)");
    }

    public Boolean getCardRetained() {
        return cardRetained;
    }

    public void setCardRetained(Boolean cardRetained) {
        this.cardRetained = cardRetained;
    }

    public String getBlockedCardMessage() {
        return blockedCardMessage;
    }

    public void setBlockedCardMessage(String blockedCardMessage) {
        this.blockedCardMessage = blockedCardMessage;
    }
}
