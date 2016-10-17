package org.kuali.ole.response;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.constants.OLESIP2Constants;
import org.kuali.ole.request.OLESIP2ItemInformationRequestParser;
import org.kuali.ole.common.MessageUtil;
import org.kuali.ole.common.OLESIP2Util;

import java.util.Map;

/**
 * Created by gayathria on 17/9/14.
 */
public class OLESIP2ItemInformationResponse extends OLESIP2Response {


    public OLESIP2ItemInformationResponse() {
        this.code = OLESIP2Constants.ITEM_INFORMATION_RESPONSE;
    }


    public String getSIP2ItemInfoResponse(Map responseMap, OLESIP2ItemInformationRequestParser sip2ItemInformationRequestParser) {

        StringBuilder builder = new StringBuilder();

        builder.append(code);
        builder.append(responseMap.get("itemStatusCode"));
        builder.append("00");
        builder.append(OLESIP2Constants.BLOCK_PATRON_REQUEST);
        builder.append(MessageUtil.getSipDateTime());
        builder.append(OLESIP2Constants.ITEM_IDENTIFIER_CODE);
        String itemBarCode = (String) responseMap.get("itemBarCode");
        builder.append(itemBarCode != null ? itemBarCode : sip2ItemInformationRequestParser.getItemIdentifier());
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.TITLE_IDENTIFIER_CODE);
        String title = (String) responseMap.get("title");
        builder.append(title != null ? title.replaceAll(OLESIP2Constants.NON_ROMAN_REGEX, "") : "");
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.CURRENCY_TYPE_CODE);
        builder.append(OLESIP2Util.getDefaultCurrency().getCurrencyCode());
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.CURRENT_LOCATION_CODE);
        String shelvingLocation = (String) responseMap.get("shelvingLocation");
        builder.append(StringUtils.isNotBlank(shelvingLocation) ? shelvingLocation : " ");
        String itemStatus = (String) responseMap.get("itemStatus");
        if(StringUtils.isBlank(itemBarCode) && StringUtils.isBlank(itemStatus)){
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SCREEN_MSG_CODE);
            builder.append(OLESIP2Constants.ITEM_UNAVAILABLE);
        }
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.ITEM_PROPERTIES_CODE);
        String author = (String) responseMap.get("author");
        builder.append((StringUtils.isNotBlank(author) ? OLESIP2Constants.AUTHOR +"  : " + author.replaceAll(OLESIP2Constants.NON_ROMAN_REGEX, "") : " ") + (StringUtils.isNotBlank(itemStatus) ? " Status : " + itemStatus : " "));
        if(StringUtils.isBlank(itemBarCode) && StringUtils.isBlank(itemStatus)){
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SCREEN_MSG_CODE);
            builder.append(OLESIP2Constants.ITEM_UNAVAILABLE);
        }
        if (StringUtils.isNotBlank(sip2ItemInformationRequestParser.getSequenceNum())) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SEQUENCE_NUM_CODE);
            builder.append(sip2ItemInformationRequestParser.getSequenceNum());
            builder.append(MessageUtil.computeChecksum(builder.toString()));
        }
        return builder.toString() + '\r';

    }


}
