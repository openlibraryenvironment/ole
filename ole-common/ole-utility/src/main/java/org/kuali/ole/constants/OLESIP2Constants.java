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

    //SIP2 Request Code
    public static final String CHECKOUT_REQUEST="11";

    //SIP2 Response Code
    public static final String CHECKOUT_RESPONSE="12";

    public static final String RENEW_SUCCESS="Item successFully renewed";
    public static final String CHECK_OUT_SUCCESS="Successfully loaned";
    public static final String CHECK_OUT_SUCCESSFULLY="Item Successfully loaned";
    public static final String CHECK_OUT_FAILED="Item cannot be loaned : any query see help desk";
    public static final String YES="YES";
    public static final String ONHOLD="ONHOLD";

}
