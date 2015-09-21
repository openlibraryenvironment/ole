package org.kuali.ole.request;


import org.apache.log4j.Logger;

public class OLESIP2RequestParser {

    private final static Logger LOG = Logger.getLogger(OLESIP2RequestParser.class.getName());

    protected String code;
    protected String sequenceNum;
    protected String checkSum;
    protected String institutionId;
    protected String patronIdentifier;
    protected String itemIdentifier;
    protected String transactionDate;
    protected String terminalPassword;
    protected String itemProperties;
    protected String patronPassword;
    protected Boolean feeAcknowledged;

    /**
     * Converts the given character to a boolean value. Character
     * must be Y or N. Otherwise an exception is
     * thrown.
     *
     * @param character char to be converted
     * @return boolean value
     * @throws Exception
     */
    protected boolean charToBool(char character) throws Exception {
        if (character == 'N') {
            return false;
        } else if (character == 'Y') {
            return true;
        } else {
            throw new Exception("Response message contains an invalid value. Allowed values are: Y and N.");
        }
    }


    /**
     * Converts the given string to an integer.
     *
     * @param value string to be converted
     * @return integer value
     * @throws Exception
     */
    protected int stringToInt(String value) throws Exception {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            throw new Exception("Response message contains an invalid value. Unable to parse an integer from the given string: \"" + value + "\".");
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSequenceNum() {
        return sequenceNum;
    }

    public void setSequenceNum(String sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public String getPatronIdentifier() {
        return patronIdentifier;
    }

    public void setPatronIdentifier(String patronIdentifier) {
        this.patronIdentifier = patronIdentifier;
    }

    public String getItemIdentifier() {
        return itemIdentifier;
    }

    public void setItemIdentifier(String itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTerminalPassword() {
        return terminalPassword;
    }

    public void setTerminalPassword(String terminalPassword) {
        this.terminalPassword = terminalPassword;
    }

    public String getItemProperties() {
        return itemProperties;
    }

    public void setItemProperties(String itemProperties) {
        this.itemProperties = itemProperties;
    }

    public String getPatronPassword() {
        return patronPassword;
    }

    public void setPatronPassword(String patronPassword) {
        this.patronPassword = patronPassword;
    }

    public Boolean getFeeAcknowledged() {
        return feeAcknowledged;
    }

    public void setFeeAcknowledged(Boolean feeAcknowledged) {
        this.feeAcknowledged = feeAcknowledged;
    }
}
