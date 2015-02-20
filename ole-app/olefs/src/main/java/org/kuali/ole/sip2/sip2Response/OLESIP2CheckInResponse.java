package org.kuali.ole.sip2.sip2Response;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.ncip.bo.OLECheckInItem;
import org.kuali.ole.sip2.common.MessageUtil;
import org.kuali.ole.sip2.constants.OLESIP2Constants;
import org.kuali.ole.sip2.requestParser.OLESIP2CheckInRequestParser;


/**
 * Created by gayathria on 1/9/14.
 */
public class OLESIP2CheckInResponse extends OLESIP2Response {

    public OLESIP2CheckInResponse() {
        this.code = OLESIP2Constants.CHECK_IN_RESPONSE;
    }


    public String getSIP2CheckInResponse(OLECheckInItem oleCheckInItem, OLESIP2CheckInRequestParser sip2CheckInRequestParser) {

        StringBuilder oleCheckInBuilder = new StringBuilder();

        oleCheckInBuilder.append(this.code);

        if (oleCheckInItem.getCode().equalsIgnoreCase("024") || oleCheckInItem.getMessage().equalsIgnoreCase(OLESIP2Constants.CHECK_IN_SUCCESS)) {
            oleCheckInBuilder.append("1");
            oleCheckInBuilder.append(OLESIP2Constants.Y);
            oleCheckInBuilder.append(OLESIP2Constants.Y);
            oleCheckInBuilder.append(OLESIP2Constants.Y);
        } else {
            oleCheckInBuilder.append("0");
            oleCheckInBuilder.append(OLESIP2Constants.N);
            oleCheckInBuilder.append(OLESIP2Constants.N);
            oleCheckInBuilder.append(OLESIP2Constants.N);
        }

        oleCheckInBuilder.append(MessageUtil.getSipDateTime());
        oleCheckInBuilder.append(OLESIP2Constants.INSTITUTION_ID_CODE);
        if (StringUtils.isNotBlank(sip2CheckInRequestParser.getInstitutionId())) {
            oleCheckInBuilder.append(sip2CheckInRequestParser.getInstitutionId());
        }
        oleCheckInBuilder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.ITEM_IDENTIFIER_CODE);
        oleCheckInBuilder.append(oleCheckInItem.getBarcode() != null ? oleCheckInItem.getBarcode() : sip2CheckInRequestParser.getItemIdentifier());
        oleCheckInBuilder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PERMANENT_LOCATION_CODE);
        if (StringUtils.isNotBlank(oleCheckInItem.getItemLocation())) {
            oleCheckInBuilder.append(oleCheckInItem.getItemLocation());
        }
        if (StringUtils.isNotBlank(oleCheckInItem.getTitle())) {
            oleCheckInBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.TITLE_IDENTIFIER_CODE);
            oleCheckInBuilder.append(oleCheckInItem.getTitle().replaceAll(OLESIP2Constants.NON_ROMAN_REGEX,""));
        }
        if (StringUtils.isNotBlank(oleCheckInItem.getPatronBarcode())) {
            oleCheckInBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.PATRON_IDENTIFIER_CODE);
            oleCheckInBuilder.append(oleCheckInItem.getPatronBarcode());
        }

        if (StringUtils.isNotBlank(oleCheckInItem.getItemType())) {
            oleCheckInBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.MEDIA_TYPE_CODE);
            oleCheckInBuilder.append(oleCheckInItem.getItemType());
        }


        if (StringUtils.isNotBlank(oleCheckInItem.getAuthor())) {
            oleCheckInBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.ITEM_PROPERTIES_CODE);
            oleCheckInBuilder.append("Author  :  " + oleCheckInItem.getAuthor().replaceAll(OLESIP2Constants.NON_ROMAN_REGEX,""));
        }

        if (StringUtils.isNotBlank(oleCheckInItem.getMessage())) {
            oleCheckInBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SCREEN_MSG_CODE);
            oleCheckInBuilder.append(oleCheckInItem.getMessage().replaceAll("<br/>", ""));
            oleCheckInBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.PRINT_LINE_CODE);
            oleCheckInBuilder.append(oleCheckInItem.getMessage().replaceAll("<br/>", ""));
        }

        if (StringUtils.isNotBlank(sip2CheckInRequestParser.getSequenceNum())) {
            oleCheckInBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SEQUENCE_NUM_CODE);
            oleCheckInBuilder.append(sip2CheckInRequestParser.getSequenceNum());
            oleCheckInBuilder.append(MessageUtil.computeChecksum(oleCheckInBuilder.toString()));
        }

        System.out.println(oleCheckInBuilder.toString());


        return oleCheckInBuilder.toString() + '\r';


    }

}
