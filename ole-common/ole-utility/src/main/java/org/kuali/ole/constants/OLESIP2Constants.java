package org.kuali.ole.constants;

/**
 * Created by chenchulakshmig on 8/27/15.
 */
public class OLESIP2Constants {

    public static final String NON_ROMAN_REGEX="[^\\w\\s\\-_,:\\(\\)\\[\\]\\/\\!]";
    public static final String SIP2_DATE_FORMAT = "yyyyMMdd    HHmmss";
    public static final String SPLIT = "|";

    //YES or NO
    public static final String Y="Y";
    public static final String N="N";


    //SIP2 Codes
    public static final String PATRON_IDENTIFIER_CODE="AA";
    public static final String ITEM_IDENTIFIER_CODE="AB";
    public static final String TERMINAL_PWD_CODE="AC";
    public static final String PATRON_PWD_CODE="AD";
    public static final String SCREEN_MSG_CODE="AF";
    public static final String PRINT_LINE_CODE="AG";
    public static final String DUE_DATE_CODE="AH";
    public static final String TITLE_IDENTIFIER_CODE="AJ";
    public static final String INSTITUTION_ID_CODE="AO";
    public static final String SEQUENCE_NUM_CODE="AY";
    public static final String CURRENCY_TYPE_CODE="BH";
    public static final String CANCEL_CODE="BI";
    public static final String FEE_ACKNOWLEDGED_CODE="BO";
    public static final String ITEM_PROPERTIES_CODE="CH";
    public static final String PERMANENT_LOCATION_CODE="AQ";
    public static final String PERSONAL_NAME_CODE="AE";

    //SIP2 Request Code
    public static final String CHECKOUT_REQUEST="11";
    public static final String CHECK_IN_REQUEST="09";
    public static final String PATRON_INFORMATION_REQUEST="63";
    public static final String BLOCK_PATRON_REQUEST="01";
    public static final String PATRON_STATUS_REQUEST="23";

    //SIP2 Response Code
    public static final String CHECKOUT_RESPONSE="12";
    public static final String CHECK_IN_RESPONSE="10";
    public static final String PATRON_INFORMATION_RESPONSE="64";
    public static final String PATRON_STATUS_RESPONSE="24";

    public static final String CHECK_IN_SUCCESS="SuccessFully Checked-in";
    public static final String RENEW_SUCCESS="Item successFully renewed";
    public static final String CHECK_OUT_SUCCESS="Successfully loaned";
    public static final String CHECK_OUT_SUCCESSFULLY="Item Successfully loaned";
    public static final String CHECK_OUT_FAILED="Item cannot be loaned : any query see help desk";
    public static final String YES="YES";
    public static final String ONHOLD="ONHOLD";


    public static final String AUTHOR="Author";
    public static final String HOLD_ITEMS_LIMIT_CODE="BZ";
    public static final String OVERDUE_ITEMS_LIMIT_CODE="CA";
    public static final String CHARGED_ITEMS_LIMIT_CODE="CB";
    public static final String VALID_PATRON_CODE="BL";
    public static final String FEE_AMOUNT_CODE="BV";
    public static final String CHARGED_ITEMS_CODE="AU";
    public static final String HOME_ADDRESS_CODE="BD";
    public static final String EMAIL_ADDRESS_CODE="BE";
    public static final String HOME_PHONE_NUM_CODE="BF";
    public static final String BLOCKED_CARD_MSG="AL";


}
