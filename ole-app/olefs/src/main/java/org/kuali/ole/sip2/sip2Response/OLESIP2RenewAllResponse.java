package org.kuali.ole.sip2.sip2Response;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.bo.OLERenewItem;
import org.kuali.ole.common.OLESIP2Util;
import org.kuali.ole.ncip.bo.OLERenewItemList;
import org.kuali.ole.response.OLESIP2Response;
import org.kuali.ole.sip2.constants.OLESIP2Constants;
import org.kuali.ole.sip2.requestParser.OLESIP2RenewAllRequestParser;
import org.kuali.ole.sip2.sip2Server.MessageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gayathria on 10/11/14.
 */
public class OLESIP2RenewAllResponse extends OLESIP2Response {

    public OLESIP2RenewAllResponse() {
        this.code = "";
    }

    public String getSIP2RenewAllResponse(OLERenewItemList oleRenewItemList, OLESIP2RenewAllRequestParser sip2RenewAllRequestParser) {
        StringBuilder renewAllResponseBuilder = new StringBuilder();
        renewAllResponseBuilder.append(code);
        if (oleRenewItemList.getRenewItemList().size() > 0) {
            renewAllResponseBuilder.append(OLESIP2Util.bool2Int(true));
        } else {
            renewAllResponseBuilder.append(OLESIP2Util.bool2Int(false));
        }
        int renewedCount = 0;
        int unRenewedCount = 0;
        List<String> renewedItems = new ArrayList<>();
        /**
         * List of unrenewd items.
         */
        List<String> unRenewedItems = new ArrayList<>();

        for (int i = 0; i < oleRenewItemList.getRenewItemList().size(); i++) {
            OLERenewItem oleRenewItem = oleRenewItemList.getRenewItemList().get(i);
            if (oleRenewItem.getMessage().equalsIgnoreCase(OLESIP2Constants.CHECK_OUT_SUCCESS)) {
                renewedCount = renewedCount + 1;
                renewedItems.add(oleRenewItem.getItemBarcode());

            } else {
                unRenewedCount = unRenewedCount + 1;
                if(StringUtils.isNotBlank(oleRenewItem.getItemBarcode()))
                    unRenewedItems.add(oleRenewItem.getItemBarcode());
            }
        }
        renewAllResponseBuilder.append(OLESIP2Util.intToFixedLengthString(renewedCount, 4));
        renewAllResponseBuilder.append(OLESIP2Util.intToFixedLengthString(unRenewedCount, 4));
        renewAllResponseBuilder.append(MessageUtil.getSipDateTime());
        renewAllResponseBuilder.append(OLESIP2Constants.INSTITUTION_ID_CODE);
        renewAllResponseBuilder.append(StringUtils.isNotBlank(sip2RenewAllRequestParser.getInstitutionId()) ? sip2RenewAllRequestParser.getInstitutionId() : "");
        for (String item : renewedItems) {
            renewAllResponseBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.RENEWED_ITEMS_CODE);
            renewAllResponseBuilder.append(item);
        }
        for (String item : unRenewedItems) {
            renewAllResponseBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.UN_RENEWED_ITEMS_CODE);
            renewAllResponseBuilder.append(item);
        }
        renewAllResponseBuilder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.SCREEN_MSG_CODE);
        renewAllResponseBuilder.append(OLESIP2Constants.REQUEST_PROCESSED);
        renewAllResponseBuilder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PRINT_LINE_CODE);
        renewAllResponseBuilder.append(" " +renewedCount + " - Renewed succesfully. " + unRenewedCount + " - Not Renewed. ");
        if (StringUtils.isNotBlank(sip2RenewAllRequestParser.getSequenceNum())) {
            renewAllResponseBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SEQUENCE_NUM_CODE);
            renewAllResponseBuilder.append(sip2RenewAllRequestParser.getSequenceNum());
            renewAllResponseBuilder.append(MessageUtil.computeChecksum(sip2RenewAllRequestParser.toString()));
        }
        return renewAllResponseBuilder.toString() + '\r';
    }

    public String getSIP2RenewAllResponse(String message, OLESIP2RenewAllRequestParser sip2RenewAllRequestParser) {

        StringBuilder renewAllResponseBuilder = new StringBuilder();
        renewAllResponseBuilder.append(code);
        renewAllResponseBuilder.append(OLESIP2Util.bool2Int(false));
        renewAllResponseBuilder.append(OLESIP2Util.intToFixedLengthString(0, 4));
        renewAllResponseBuilder.append(OLESIP2Util.intToFixedLengthString(0, 4));
        renewAllResponseBuilder.append(MessageUtil.getSipDateTime());
        renewAllResponseBuilder.append(OLESIP2Constants.INSTITUTION_ID_CODE);
        renewAllResponseBuilder.append(StringUtils.isNotBlank(sip2RenewAllRequestParser.getInstitutionId()) ? sip2RenewAllRequestParser.getInstitutionId() : "");
        renewAllResponseBuilder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.SCREEN_MSG_CODE);
        renewAllResponseBuilder.append(message);
        if (StringUtils.isNotBlank(sip2RenewAllRequestParser.getSequenceNum())) {
            renewAllResponseBuilder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.SEQUENCE_NUM_CODE);
            renewAllResponseBuilder.append(sip2RenewAllRequestParser.getSequenceNum());
            renewAllResponseBuilder.append(MessageUtil.computeChecksum(sip2RenewAllRequestParser.toString()));
        }
        return renewAllResponseBuilder.toString() + '\r';

    }
}
