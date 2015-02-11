package org.kuali.ole.sip2.sip2Response;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.deliver.bo.OleItemSearch;
import org.kuali.ole.sip2.common.MessageUtil;
import org.kuali.ole.sip2.common.OLESIP2Util;
import org.kuali.ole.sip2.constants.OLESIP2Constants;
import org.kuali.ole.sip2.requestParser.OLESIP2ItemInformationRequestParser;

/**
 * Created by gayathria on 17/9/14.
 */
public class OLESIP2ItemInformationResponse extends OLESIP2Response {


    public OLESIP2ItemInformationResponse() {
        this.code = OLESIP2Constants.ITEM_INFORMATION_RESPONSE;
    }


    public String getSIP2ItemInfoResponse(OleItemSearch oleItemSearch, OLESIP2ItemInformationRequestParser sip2ItemInformationRequestParser) {

        StringBuilder builder = new StringBuilder();
        builder.append(code);
        builder.append("08");
        builder.append("00");
        builder.append(OLESIP2Constants.BLOCK_PATRON_REQUEST);
        builder.append(MessageUtil.getSipDateTime());
        builder.append(OLESIP2Constants.ITEM_IDENTIFIER_CODE);
        builder.append(oleItemSearch.getItemBarCode() != null ? oleItemSearch.getItemBarCode() : sip2ItemInformationRequestParser.getItemIdentifier());
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.TITLE_IDENTIFIER_CODE);
        builder.append(oleItemSearch.getTitle() != null ? oleItemSearch.getTitle() : " ");
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.CURRENCY_TYPE_CODE);
        builder.append(OLESIP2Util.getDefaultCurrency().getCurrencyCode());
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.MEDIA_TYPE_CODE);
        builder.append(StringUtils.isNotBlank(oleItemSearch.getItemType()) ? oleItemSearch.getItemType() : " ");
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.CURRENT_LOCATION_CODE);
        builder.append(StringUtils.isNotBlank(oleItemSearch.getShelvingLocation()) ? oleItemSearch.getShelvingLocation() : " ");
        if(StringUtils.isBlank(oleItemSearch.getItemBarCode()) && StringUtils.isBlank(oleItemSearch.getItemStatus())){
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SCREEN_MSG_CODE);
            builder.append(OLESIP2Constants.ITEM_UNAVAILABLE);
        }
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.ITEM_PROPERTIES_CODE);
        builder.append((StringUtils.isNotBlank(oleItemSearch.getAuthor()) ? "Author : " + oleItemSearch.getAuthor() : " ") + (StringUtils.isNotBlank(oleItemSearch.getItemStatus()) ? " Status : " + oleItemSearch.getItemStatus() : " "));
        if(StringUtils.isBlank(oleItemSearch.getItemBarCode()) && StringUtils.isBlank(oleItemSearch.getItemStatus())){
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
