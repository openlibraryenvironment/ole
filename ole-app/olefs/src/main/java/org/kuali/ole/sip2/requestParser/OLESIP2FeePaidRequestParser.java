package org.kuali.ole.sip2.requestParser;

import org.apache.log4j.Logger;
import org.kuali.ole.request.OLESIP2RequestParser;
import org.kuali.ole.sip2.constants.OLESIP2Constants;

/**
 * Created by gayathria on 2/12/14.
 */
public class OLESIP2FeePaidRequestParser extends OLESIP2RequestParser {

    private String feeType;
    private String paymentType;
    private String currencyType;
    private String feeAmount;
    private String feeIdentifier;
    private String transactionId;

    private static final Logger LOG = Logger.getLogger(OLESIP2FeePaidRequestParser.class);


    public OLESIP2FeePaidRequestParser(String requestData) {
        this.parseFeePaidMessage(requestData);
    }


    public void parseFeePaidMessage(String requestData) {

        LOG.info("Entry OLESIP2FeePaidRequestParser.parseFeePaidMessage(String requestData)");

        String[] requestDataArray = requestData.split("\\|");
        try {

            for (String data : requestDataArray) {
                if (data.startsWith(OLESIP2Constants.FEE_PAID_REQUEST)) {
                    code = data.substring(0, 2);
                    transactionDate = data.substring(2, 20);
                    feeType = getFeeType(data.substring(20, 22));
                    paymentType = data.substring(22, 24);
                    currencyType = data.substring(24, 27);
                    feeAmount = data.substring(29);
                }
                if (data.startsWith(OLESIP2Constants.INSTITUTION_ID_CODE)) {
                    institutionId = (data.replaceFirst(OLESIP2Constants.INSTITUTION_ID_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.PATRON_IDENTIFIER_CODE)) {
                    patronIdentifier = (data.replaceFirst(OLESIP2Constants.PATRON_IDENTIFIER_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.TERMINAL_PWD_CODE)) {
                    terminalPassword = (data.replaceFirst(OLESIP2Constants.TERMINAL_PWD_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.PATRON_PWD_CODE)) {
                    patronPassword = (data.replaceFirst(OLESIP2Constants.PATRON_PWD_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.FEE_IDENTIFIER_CODE)) {
                    feeIdentifier = (data.replaceFirst(OLESIP2Constants.FEE_IDENTIFIER_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.TRANSACTION_ID_CODE)) {
                    transactionId = (data.replaceFirst(OLESIP2Constants.TRANSACTION_ID_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.SEQUENCE_NUM_CODE)) {
                    sequenceNum = data.substring(2, 5);
                    checkSum = data.substring(5);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public String getFeeType(String code) throws Exception {
        if (code.equals("01")) {
            // return "other/unknown";
            return "SER_FEE";
        } else if (code.equals("02")) {
            return "ADMINISTRATIVE";
        } else if (code.equals("03")) {
            return "DAMAGE";
        } else if (code.equals("04")) {
            return "OVR_DUE";
        } else if (code.equals("05")) {
            return "PROCESSING";
        } else if (code.equals("06")) {
            //return FeeType.RENTAL;
            return "SER_FEE";
        } else if (code.equals("07")) {
            return "REPL_FEE";
        } else if (code.equals("08")) {
            //return FeeType.COMPUTER_ACCESS_CHARGE;
            return "SER_FEE";
        } else if (code.equals("09")) {
            return "HOLD_FEE";
        } else {
            // throw new InvalidSIP2ResponseValueException("Invalid fee type code! The given code \"" + code + "\" doesn't match with any fee type!");
            throw new Exception("Invalid fee type code! The given code \"" + code + "\" doesn't match with any fee type!");
        }
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getFeeIdentifier() {
        return feeIdentifier;
    }

    public void setFeeIdentifier(String feeIdentifier) {
        this.feeIdentifier = feeIdentifier;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
