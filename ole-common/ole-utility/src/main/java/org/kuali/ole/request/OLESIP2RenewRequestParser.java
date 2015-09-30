package org.kuali.ole.request;

import org.apache.log4j.Logger;
import org.kuali.ole.constants.OLESIP2Constants;

/**
 * Created by gayathria on 2/12/14.
 */
public class OLESIP2RenewRequestParser extends OLESIP2RequestParser {

    private Boolean thirdPartyAllowed;
    private Boolean noBlock;
    private String nbDueDate;
    private String titleIdentifier;

    private static final Logger LOG = Logger.getLogger(OLESIP2RenewRequestParser.class);


    public OLESIP2RenewRequestParser(String requestData) {
        this.parseRenewRequest(requestData);
    }


    public void parseRenewRequest(String requestData) {
        LOG.info("Entry OLESIP2RenewRequestParser.parseRenewRequest(String requestData)");

        LOG.info(requestData);

        String[] requestDataArray = requestData.split("\\|");
        try {
            for (String data : requestDataArray) {
                LOG.info(data);
                if (data.startsWith(OLESIP2Constants.RENEW_REQUEST)) {
                    code = data.substring(0, 2);
                    thirdPartyAllowed = charToBool(data.charAt(2));
                    noBlock = charToBool(data.charAt(3));
                    transactionDate = data.substring(4, 22);
                    nbDueDate = data.substring(22, 40);
                    institutionId = data.substring(42) != null && !data.substring(42).equalsIgnoreCase("") ? data.substring(42) : "";
                }
                if (data.startsWith(OLESIP2Constants.PATRON_IDENTIFIER_CODE)) {
                    patronIdentifier = (data.replaceFirst(OLESIP2Constants.PATRON_IDENTIFIER_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.PATRON_PWD_CODE)) {
                    patronPassword = (data.replaceFirst(OLESIP2Constants.PATRON_PWD_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.ITEM_IDENTIFIER_CODE)) {
                    itemIdentifier = (data.replaceFirst(OLESIP2Constants.ITEM_IDENTIFIER_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.TITLE_IDENTIFIER_CODE)) {
                    titleIdentifier = (data.replaceFirst(OLESIP2Constants.TITLE_IDENTIFIER_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.TERMINAL_PWD_CODE)) {
                    terminalPassword = (data.replaceFirst(OLESIP2Constants.TERMINAL_PWD_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.ITEM_PROPERTIES_CODE)) {
                    itemProperties = (data.replaceFirst(OLESIP2Constants.ITEM_PROPERTIES_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.FEE_ACKNOWLEDGED_CODE)) {
                    feeAcknowledged = charToBool(data.charAt(2));
                }
                if (data.startsWith(OLESIP2Constants.SEQUENCE_NUM_CODE)) {
                    sequenceNum = data.substring(2, 5);
                    checkSum = data.substring(5);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("Exit OLESIP2RenewRequestParser.parseRenewRequest(String requestData)");

    }

    public Boolean getThirdPartyAllowed() {
        return thirdPartyAllowed;
    }

    public void setThirdPartyAllowed(Boolean thirdPartyAllowed) {
        this.thirdPartyAllowed = thirdPartyAllowed;
    }

    public Boolean getNoBlock() {
        return noBlock;
    }

    public void setNoBlock(Boolean noBlock) {
        this.noBlock = noBlock;
    }

    public String getNbDueDate() {
        return nbDueDate;
    }

    public void setNbDueDate(String nbDueDate) {
        this.nbDueDate = nbDueDate;
    }

    public String getTitleIdentifier() {
        return titleIdentifier;
    }

    public void setTitleIdentifier(String titleIdentifier) {
        this.titleIdentifier = titleIdentifier;
    }
}
