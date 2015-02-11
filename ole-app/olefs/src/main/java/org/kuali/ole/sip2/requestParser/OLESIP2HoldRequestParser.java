package org.kuali.ole.sip2.requestParser;

import org.apache.log4j.Logger;
import org.kuali.ole.sip2.constants.OLESIP2Constants;

/**
 * Created by gayathria on 2/12/14.
 */
public class OLESIP2HoldRequestParser extends OLESIP2RequestParser {

    private String holdMode;
    private String ExpirationDate;
    private String pickupLocation;
    private String holdType;
    private String titleIdentifier;
    private static final Logger LOG = Logger.getLogger(OLESIP2HoldRequestParser.class);


    public OLESIP2HoldRequestParser(String requestData) {
        this.parseHoldRequest(requestData);
    }


    public void parseHoldRequest(String requestData) {

        LOG.info("Entry OLESIP2HoldRequestParser.parseHoldRequest(String requestData)");
        String[] requestDataArray = requestData.split("\\|");
        try {
            for (String data : requestDataArray) {
                code = data.substring(0, 2);
                if (data.startsWith(OLESIP2Constants.HOLD_REQUEST)) {
                    char holdMode = data.charAt(2);
                    if (holdMode == '+') {
                        holdType = OLESIP2Constants.ADD_HOLD;
                    } else if (holdMode == '-') {
                        holdType = OLESIP2Constants.DELETE_HOLD;
                    } else if (holdMode == '*') {
                        holdType = OLESIP2Constants.UPDATE_HOLD;
                    }
                    if(data.contains(OLESIP2Constants.PICKUP_LOCATION_CODE)){
                        if(data.length() > data.indexOf(OLESIP2Constants.PICKUP_LOCATION_CODE)+2)
                            pickupLocation = data.substring(data.indexOf(OLESIP2Constants.PICKUP_LOCATION_CODE)+2);
                    }
                }

                if (data.startsWith(OLESIP2Constants.PICKUP_LOCATION_CODE)) {
                    pickupLocation = (data.replaceFirst(OLESIP2Constants.PICKUP_LOCATION_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.HOLD_TYPE_CODE)) {
                    holdType = (data.replaceFirst(OLESIP2Constants.HOLD_TYPE_CODE, "")).trim();
                }
                if (data.startsWith(OLESIP2Constants.INSTITUTION_ID_CODE)) {
                    institutionId = (data.replaceFirst(OLESIP2Constants.INSTITUTION_ID_CODE, "")).trim();
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
        LOG.info("Exit OLESIP2HoldRequestParser.parseHoldRequest(String requestData)");
    }

    public String getHoldMode() {
        return holdMode;
    }

    public void setHoldMode(String holdMode) {
        this.holdMode = holdMode;
    }

    public String getExpirationDate() {
        return ExpirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        ExpirationDate = expirationDate;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getHoldType() {
        return holdType;
    }

    public void setHoldType(String holdType) {
        this.holdType = holdType;
    }

    public String getTitleIdentifier() {
        return titleIdentifier;
    }

    public void setTitleIdentifier(String titleIdentifier) {
        this.titleIdentifier = titleIdentifier;
    }
}
