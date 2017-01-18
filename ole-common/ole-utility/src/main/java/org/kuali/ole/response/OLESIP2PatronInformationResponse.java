package org.kuali.ole.response;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.bo.OLECheckedOutItem;
import org.kuali.ole.bo.OLEHold;
import org.kuali.ole.bo.OLELookupUser;
import org.kuali.ole.common.MessageUtil;
import org.kuali.ole.common.OLESIP2Util;
import org.kuali.ole.constants.OLESIP2Constants;
import org.kuali.ole.request.OLESIP2PatronInformationRequestParser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gayathria on 17/9/14.
 */
public class OLESIP2PatronInformationResponse extends OLESIP2Response {

    public OLESIP2PatronInformationResponse() {
        code = OLESIP2Constants.PATRON_INFORMATION_RESPONSE;
    }


    public String getSIP2PatronInfoResponse(OLELookupUser oleLookupUser, OLESIP2PatronInformationRequestParser sip2PatronInformationRequestParser, String institution, BigDecimal balanceAmount) {

        StringBuilder builder = new StringBuilder();
        List<String> overDueItemList = new ArrayList<>();
        List<String> holdItemList = new ArrayList<>();
        List<String> recallItemList = new ArrayList<>();
        List<String> readyForPickupList = new ArrayList<>();

        if(oleLookupUser.getOleCheckedOutItems()!=null && oleLookupUser.getOleCheckedOutItems().getCheckedOutItems()!=null){
            for(OLECheckedOutItem oleCheckedOutItem : oleLookupUser.getOleCheckedOutItems().getCheckedOutItems()){
                if(oleCheckedOutItem.isOverDue()){
                    overDueItemList.add(oleCheckedOutItem.getItemId());
                }
            }
        }
        if(oleLookupUser.getOleHolds()!=null && oleLookupUser.getOleHolds().getOleHoldList()!=null){
            for(OLEHold oleHold : oleLookupUser.getOleHolds().getOleHoldList()){
                if(oleHold.getRecallStatus().equalsIgnoreCase(OLESIP2Constants.YES)){
                    recallItemList.add(oleHold.getItemId());
                }
                if(oleHold.getAvailableStatus().equalsIgnoreCase(OLESIP2Constants.ONHOLD)){
                    readyForPickupList.add(oleHold.getItemId());
                }else{
                    holdItemList.add(oleHold.getItemId());
                }
            }
        }

        builder.append(code);
        //TODO


        if(oleLookupUser.isValidPatron()==true){
            builder.append(" ");
        }else{
            builder.append(OLESIP2Constants.Y);
        }
        builder.append("             ");
        builder.append(StringUtils.isNotBlank(sip2PatronInformationRequestParser.getLanguage())?
                sip2PatronInformationRequestParser.getLanguage():"001");
        builder.append(MessageUtil.getSipDateTime());
        //Hold Items count
        builder.append(OLESIP2Util.intToFixedLengthString(readyForPickupList.size(), 4));
        //Overdue Items count
        builder.append(OLESIP2Util.intToFixedLengthString(overDueItemList.size(), 4));
        //Charged items count
        builder.append(OLESIP2Util.intToFixedLengthString(oleLookupUser.getOleCheckedOutItems() != null &&
                oleLookupUser.getOleCheckedOutItems().getCheckedOutItems() != null ?
                oleLookupUser.getOleCheckedOutItems().getCheckedOutItems().size():0, 4));
        //Fine Items count
        builder.append(OLESIP2Util.intToFixedLengthString(oleLookupUser.getOleItemFines() != null &&
                oleLookupUser.getOleItemFines().getOleItemFineList() != null ?
                oleLookupUser.getOleItemFines().getOleItemFineList().size():0, 4));
        //Recall Items count
        builder.append(OLESIP2Util.intToFixedLengthString(recallItemList.size(), 4));
        //Unavailable holds count
        builder.append(OLESIP2Util.intToFixedLengthString(holdItemList.size(), 4));


        builder.append(OLESIP2Constants.INSTITUTION_ID_CODE);
        builder.append(StringUtils.isNotBlank(sip2PatronInformationRequestParser.getInstitutionId()) ?
                sip2PatronInformationRequestParser.getInstitutionId() : institution);
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PATRON_IDENTIFIER_CODE);
        builder.append(oleLookupUser.getPatronId() != null ?
                oleLookupUser.getPatronId() : sip2PatronInformationRequestParser.getPatronIdentifier());
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.PERSONAL_NAME_CODE);
        builder.append(oleLookupUser.getPatronName() != null ?
                oleLookupUser.getPatronName().getFirstName() + " " + oleLookupUser.getPatronName().getLastName() : "");
        //Hold Items Limit
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.HOLD_ITEMS_LIMIT_CODE);
        builder.append(OLESIP2Util.intToFixedLengthString(0, 4));
        //Overdue Items Limit
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.OVERDUE_ITEMS_LIMIT_CODE);
        builder.append(OLESIP2Util.intToFixedLengthString(0, 4));
        //Charged items Limit
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.CHARGED_ITEMS_LIMIT_CODE);
        builder.append(OLESIP2Util.intToFixedLengthString(0, 4));
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.VALID_PATRON_CODE);
        builder.append(oleLookupUser.isValidPatron()==true?OLESIP2Constants.Y:OLESIP2Constants.N);
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.CURRENCY_TYPE_CODE);
        builder.append(OLESIP2Util.getDefaultCurrency().getCurrencyCode());
        if (oleLookupUser.getOleItemFines() != null) {
            builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.FEE_AMOUNT_CODE);
            builder.append(balanceAmount);
        }
        //TODO FeeLimit
        /* //Hold Items
        for(String readyForPickupItemBarcode : readyForPickupList){
            builder.append("|AS"+readyForPickupItemBarcode);
        }
        //Overdue Items
        for(String overDueItemBarcode : overDueItemList){
            builder.append("|AT"+overDueItemBarcode);
        }*/
        //Charged Items
        /*if(oleLookupUser.getOleCheckedOutItems() != null && oleLookupUser.getOleCheckedOutItems().getCheckedOutItems() != null){
            for(OLECheckedOutItem oleCheckedOutItem : oleLookupUser.getOleCheckedOutItems().getCheckedOutItems()){
                builder.append(OLESIP2Constants.SPLIT+
                        OLESIP2Constants.CHARGED_ITEMS_CODE+
                        oleCheckedOutItem.getItemId());
            }
        }*/

        if (oleLookupUser.getPatronAddress() != null) {
            builder.append(OLESIP2Constants.SPLIT+
                    OLESIP2Constants.HOME_ADDRESS_CODE);
            builder.append(StringUtils.isNotBlank(oleLookupUser.getPatronAddress().getLine1()) ?
                    oleLookupUser.getPatronAddress().getLine1() : "");
            builder.append(StringUtils.isNotBlank(oleLookupUser.getPatronAddress().getCity()) ?
                    ", " + oleLookupUser.getPatronAddress().getCity() : "");
            builder.append(StringUtils.isNotBlank(oleLookupUser.getPatronAddress().getStateProvinceCode()) ?
                    ", " + oleLookupUser.getPatronAddress().getStateProvinceCode() : "");
            builder.append(StringUtils.isNotBlank(oleLookupUser.getPatronAddress().getPostalCode()) ?
                    ", " + oleLookupUser.getPatronAddress().getPostalCode() : "");
            builder.append(StringUtils.isNotBlank(oleLookupUser.getPatronAddress().getCountryCode()) ?
                    ", " + oleLookupUser.getPatronAddress().getCountryCode() : "");
        }
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.EMAIL_ADDRESS_CODE);
        builder.append(oleLookupUser.getPatronEmail() != null ? oleLookupUser.getPatronEmail().getEmailAddress() : "");
        builder.append(OLESIP2Constants.SPLIT+
                OLESIP2Constants.HOME_PHONE_NUM_CODE);
        builder.append(oleLookupUser.getPatronPhone() != null ? oleLookupUser.getPatronPhone().getPhoneNumber() : "");

        if (StringUtils.isNotBlank(oleLookupUser.getMessage())) {
            builder.append(OLESIP2Constants.SPLIT+
                 OLESIP2Constants.SCREEN_MSG_CODE);
            if(oleLookupUser.isValidPatron()==true){
                builder.append(OLESIP2Constants.SCREEN_SUCCESS_MSG);
            }else{
                builder.append(OLESIP2Constants.SCREEN_FAILURE_MSG);
            }
        }

        if (StringUtils.isNotBlank(sip2PatronInformationRequestParser.getSequenceNum())) {
            builder.append(OLESIP2Constants.SPLIT+OLESIP2Constants.SEQUENCE_NUM_CODE);
            builder.append(sip2PatronInformationRequestParser.getSequenceNum());
            builder.append(MessageUtil.computeChecksum(builder.toString()));
        }
        return builder.toString() + '\r';

    }


}
