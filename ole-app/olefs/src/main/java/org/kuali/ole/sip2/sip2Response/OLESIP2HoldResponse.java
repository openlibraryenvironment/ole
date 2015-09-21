package org.kuali.ole.sip2.sip2Response;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.ncip.bo.OLECancelRequest;
import org.kuali.ole.ncip.bo.OLEPlaceRequest;
import org.kuali.ole.response.OLESIP2Response;
import org.kuali.ole.common.MessageUtil;
import org.kuali.ole.common.OLESIP2Util;
import org.kuali.ole.sip2.constants.OLESIP2Constants;
import org.kuali.ole.sip2.requestParser.OLESIP2HoldRequestParser;
import org.kuali.ole.sip2.sip2Server.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by gayathria on 2/12/14.
 */
public class OLESIP2HoldResponse extends OLESIP2Response {
    public OLESIP2HoldResponse() {
        code = OLESIP2Constants.HOLD_RESPONSE;
    }

    public String getSIP2PlaceHoldRequestService(OLEPlaceRequest olePlaceRequest, OLESIP2HoldRequestParser sip2HoldRequestParser) {

        StringBuilder builder = new StringBuilder();
        builder.append(this.code);
        if (olePlaceRequest.getMessage().trim().equalsIgnoreCase(OLESIP2Constants.REQUEST_RAISED)) {
            builder.append(OLESIP2Util.bool2Int(true));
            builder.append(OLESIP2Util.bool2Char(true));
        } else {
            builder.append(OLESIP2Util.bool2Int(true));
            builder.append(OLESIP2Util.bool2Char(true));
        }
        builder.append(MessageUtil.getSipDateTime());
        if (olePlaceRequest.getExpirationDate() != null) {
            builder.append(OLESIP2Constants.EXPIRATION_DATE_CODE);
            try {
                builder.append(MessageUtil.toSipDateTime(new SimpleDateFormat().parse(olePlaceRequest.getExpirationDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            builder.append(OLESIP2Constants.SPLIT);
        }
        if (StringUtils.isNotBlank(olePlaceRequest.getQueuePosition())) {
            builder.append(OLESIP2Constants.QUEUE_POSITION_CODE);
            builder.append(olePlaceRequest.getQueuePosition());
            builder.append(OLESIP2Constants.SPLIT);
        }
        if (StringUtils.isNotBlank(sip2HoldRequestParser.getPickupLocation())) {
            builder.append(OLESIP2Constants.PICKUP_LOCATION_CODE);
            builder.append(sip2HoldRequestParser.getPickupLocation());
            builder.append(OLESIP2Constants.SPLIT);
        }
        builder.append(OLESIP2Constants.INSTITUTION_ID_CODE);
        builder.append(StringUtils.isNotBlank(sip2HoldRequestParser.getInstitutionId()) ? sip2HoldRequestParser.getInstitutionId() : "");
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PATRON_IDENTIFIER_CODE);
        builder.append(StringUtils.isNotBlank(sip2HoldRequestParser.getPatronIdentifier()) ? sip2HoldRequestParser.getPatronIdentifier() : "");
        if (StringUtils.isNotBlank(sip2HoldRequestParser.getItemIdentifier())) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.ITEM_IDENTIFIER_CODE);
            builder.append(sip2HoldRequestParser.getItemIdentifier());
        }
        if (StringUtils.isNotBlank(sip2HoldRequestParser.getTitleIdentifier())) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.TITLE_IDENTIFIER_CODE);
            builder.append(sip2HoldRequestParser.getTitleIdentifier().replaceAll(OLESIP2Constants.NON_ROMAN_REGEX, ""));
        }
        if (StringUtils.isNotBlank(olePlaceRequest.getMessage())) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SCREEN_MSG_CODE);
            builder.append(olePlaceRequest.getMessage().trim());
        }
        if (StringUtils.isNotBlank(sip2HoldRequestParser.getSequenceNum())) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SEQUENCE_NUM_CODE);
            builder.append(sip2HoldRequestParser.getSequenceNum());
            builder.append(MessageUtil.computeChecksum(builder.toString()));
        }

        return builder.toString() + '\r';
    }

    public String getSIP2CancelHoldRequestService(OLECancelRequest oleCancelRequest, OLESIP2HoldRequestParser sip2HoldRequestParser) {

        StringBuilder builder = new StringBuilder();
        builder.append(this.code);
        if (oleCancelRequest.getMessage().trim().contains(OLESIP2Constants.SUCCESSFULLY)) {
            builder.append(OLESIP2Util.bool2Int(true));
            builder.append(OLESIP2Util.bool2Char(true));
        } else {
            builder.append(OLESIP2Util.bool2Int(false));
            builder.append(OLESIP2Util.bool2Char(false));
        }
        builder.append(MessageUtil.getSipDateTime());
        if (sip2HoldRequestParser.getPickupLocation() != null) {
            builder.append(OLESIP2Constants.PICKUP_LOCATION_CODE);
            builder.append(sip2HoldRequestParser.getPickupLocation());
            builder.append(OLESIP2Constants.SPLIT);
        }
        builder.append(OLESIP2Constants.INSTITUTION_ID_CODE);
        builder.append(StringUtils.isNotBlank(sip2HoldRequestParser.getInstitutionId()) ? sip2HoldRequestParser.getInstitutionId() : "");
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PATRON_IDENTIFIER_CODE);
        builder.append(StringUtils.isNotBlank(sip2HoldRequestParser.getPatronIdentifier()) ? sip2HoldRequestParser.getPatronIdentifier() : "");
        if (StringUtils.isNotBlank(sip2HoldRequestParser.getItemIdentifier())) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.ITEM_IDENTIFIER_CODE);
            builder.append(sip2HoldRequestParser.getItemIdentifier());
        }
        if (StringUtils.isNotBlank(sip2HoldRequestParser.getTitleIdentifier())) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.TITLE_IDENTIFIER_CODE);
            builder.append(sip2HoldRequestParser.getTitleIdentifier().replaceAll(OLESIP2Constants.NON_ROMAN_REGEX, ""));
        }
        if (StringUtils.isNotBlank(oleCancelRequest.getMessage())) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SCREEN_MSG_CODE);
            builder.append(oleCancelRequest.getMessage().trim());
        }
        if (StringUtils.isNotBlank(sip2HoldRequestParser.getSequenceNum())) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SEQUENCE_NUM_CODE);
            builder.append(sip2HoldRequestParser.getSequenceNum());
            builder.append(MessageUtil.computeChecksum(builder.toString()));
        }

        return builder.toString() + '\r';
    }

    public String getSIP2UpdateHoldRequestService(OLESIP2HoldRequestParser sip2HoldRequestParser) {

        StringBuilder builder = new StringBuilder();
        builder.append(this.code);
        builder.append(StringUtil.bool2Int(false));
        builder.append(StringUtil.bool2Char(false));
        builder.append(org.kuali.ole.sip2.sip2Server.MessageUtil.getSipDateTime());
        builder.append(OLESIP2Constants.INSTITUTION_ID_CODE);
        builder.append(StringUtils.isNotBlank(sip2HoldRequestParser.getInstitutionId()) ? sip2HoldRequestParser.getInstitutionId() : "");
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PATRON_IDENTIFIER_CODE);
        builder.append(StringUtils.isNotBlank(sip2HoldRequestParser.getPatronIdentifier()) ? sip2HoldRequestParser.getPatronIdentifier() : "");
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.SCREEN_MSG_CODE);
        builder.append(OLESIP2Constants.SERVICE_UNAVAILABLE);
        if (StringUtils.isNotBlank(sip2HoldRequestParser.getSequenceNum())) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SEQUENCE_NUM_CODE);
            builder.append(sip2HoldRequestParser.getSequenceNum());
            builder.append(MessageUtil.computeChecksum(builder.toString()));
        }
        return builder.toString() + '\r';
    }

}
