package org.kuali.ole.sip2.requestParser;

import org.apache.log4j.Logger;
import org.kuali.ole.sip2.constants.OLESIP2Constants;

/**
 * Created by gayathria on 2/12/14.
 */
public class OLESIP2CheckOutRequestParser extends OLESIP2RequestParser {

    private static final Logger LOG = Logger.getLogger(OLESIP2CheckOutRequestParser.class);
    private Boolean scRenewalPolicy;
    private Boolean noBlock;
    private String nbDueDate;
    private Boolean cancel;

    public OLESIP2CheckOutRequestParser(String requestData) {
        this.parseCheckoutRequest(requestData);
    }

    public void parseCheckoutRequest(String requestData) {

        LOG.info("Entry OLESIP2CheckOutRequestParser.parseCheckoutRequest(String requestData)");
        String[] requestDataArray = requestData.split("\\|");

        try {
            for (String data : requestDataArray) {
                LOG.info(data);
                if (data.startsWith(OLESIP2Constants.CHECKOUT_REQUEST)) {
                    code = data.substring(0, 2);
                    scRenewalPolicy = charToBool(data.charAt(2));
                    noBlock = charToBool(data.charAt(3));
                    transactionDate = data.substring(4, 22);
                    nbDueDate = data.substring(22, 40);
                    institutionId = data.substring(42);
                }
                if (data.startsWith(OLESIP2Constants.PATRON_IDENTIFIER_CODE)) {
                    patronIdentifier = (data.replaceFirst(OLESIP2Constants.PATRON_IDENTIFIER_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.ITEM_IDENTIFIER_CODE)) {
                    itemIdentifier = (data.replaceFirst(OLESIP2Constants.ITEM_IDENTIFIER_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.TERMINAL_PWD_CODE)) {
                    terminalPassword = (data.replaceFirst(OLESIP2Constants.TERMINAL_PWD_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.ITEM_PROPERTIES_CODE)) {
                    itemProperties = (data.replaceFirst(OLESIP2Constants.ITEM_PROPERTIES_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.PATRON_PWD_CODE)) {
                    patronPassword = (data.replaceFirst(OLESIP2Constants.PATRON_PWD_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.FEE_ACKNOWLEDGED_CODE)) {
                    feeAcknowledged = charToBool(data.charAt(2));
                }
                if (data.startsWith(OLESIP2Constants.CANCEL_CODE)) {
                    cancel = charToBool(data.charAt(2));
                }
                if (data.startsWith(OLESIP2Constants.SEQUENCE_NUM_CODE)) {
                    sequenceNum = data.substring(2, 5);
                    checkSum = data.substring(5);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);

        }
        LOG.info("Exit OLESIP2CheckOutRequestParser.parseCheckoutRequest(String requestData)");
    }

    public Boolean getScRenewalPolicy() {
        return scRenewalPolicy;
    }

    public void setScRenewalPolicy(Boolean scRenewalPolicy) {
        this.scRenewalPolicy = scRenewalPolicy;
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

    public Boolean getCancel() {
        return cancel;
    }

    public void setCancel(Boolean cancel) {
        this.cancel = cancel;
    }
}
