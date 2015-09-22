package org.kuali.ole.request;

import org.apache.log4j.Logger;
import org.kuali.ole.constants.OLESIP2Constants;

/**
 * Created by gayathria on 2/12/14.
 *
 */
public class OLESIP2CheckInRequestParser extends OLESIP2RequestParser {

    private static final Logger LOG = Logger.getLogger(OLESIP2CheckInRequestParser.class);

    private String returnDate;
    private String currentLocation;
    private Boolean noBlock;
    private Boolean cancel;

    public OLESIP2CheckInRequestParser(String requestData) {
        this.parseCheckInRequest(requestData);
    }

    public void parseCheckInRequest(String requestData) {

        LOG.info("Entry OLESIP2CheckInRequestParser.parseCheckinRequest(String requestData)");
        String[] requestDataArray = requestData.split("\\|");
        try {

            for (String data : requestDataArray) {
                if (data.startsWith(OLESIP2Constants.CHECK_IN_REQUEST)) {
                    code = data.substring(0, 2);
                    noBlock = charToBool(data.charAt(2));
                    transactionDate = data.substring(3, 21);
                    returnDate = data.substring(21, 39);
                    currentLocation = data.substring(41);
                }
                if (data.startsWith(OLESIP2Constants.INSTITUTION_ID_CODE)) {
                    institutionId = (data.replaceFirst(OLESIP2Constants.INSTITUTION_ID_CODE, "")).trim();
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
        LOG.info("Exit OLESIP2CheckInRequestParser.parseCheckInRequest(String requestData)");
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Boolean getNoBlock() {
        return noBlock;
    }

    public void setNoBlock(Boolean noBlock) {
        this.noBlock = noBlock;
    }

    public Boolean getCancel() {
        return cancel;
    }

    public void setCancel(Boolean cancel) {
        this.cancel = cancel;
    }
}
