package org.kuali.ole.constants;

/**
 * Created by chenchulakshmig on 8/27/15.
 */
public class OLESIP2Constants {

    public static final String NON_ROMAN_REGEX="[^\\w\\s\\-_,:\\(\\)\\[\\]\\/\\!]";
    public static final String SIP2_DATE_FORMAT = "yyyyMMdd    HHmmss";
    public static final String SPLIT = "|";

    //SIP Version
    public static final String SIP_VERSION = "2.00";
    //YES or NO
    public static final String Y="Y";
    public static final String N="N";

    public static final String TIME_OUT_PERIOD="600";
    public static final String RETRIES_ALLOWED="010";
    public static final String ACS_SCREEN_MSG="System is Available";

    //SIP2 Codes
    public static final String PATRON_IDENTIFIER_CODE="AA";
    public static final String ITEM_IDENTIFIER_CODE="AB";
    public static final String TERMINAL_PWD_CODE="AC";
    public static final String PATRON_PWD_CODE="AD";
    public static final String PERSONAL_NAME_CODE="AE";
    public static final String SCREEN_MSG_CODE="AF";
    public static final String PRINT_LINE_CODE="AG";
    public static final String DUE_DATE_CODE="AH";
    public static final String TITLE_IDENTIFIER_CODE="AJ";
    public static final String BLOCKED_CARD_MSG="AL";
    public static final String LIBRARY_NAME_CODE="AN";
    public static final String TERMINAL_LOCATION_CODE="AM";
    public static final String INSTITUTION_ID_CODE="AO";
    public static final String CURRENT_LOCATION_CODE="AP";
    public static final String PERMANENT_LOCATION_CODE="AQ";
    public static final String HOLE_ITEMS_CODE="AS";
    public static final String OVERDUE_ITEMS_CODE="AT";
    public static final String CHARGED_ITEMS_CODE="AU";
    public static final String FINE_ITEMS_CODE="AV";
    public static final String SEQUENCE_NUM_CODE="AY";
    public static final String CHECKSUM_CODE="AZ";
    public static final String HOME_ADDRESS_CODE="BD";
    public static final String EMAIL_ADDRESS_CODE="BE";
    public static final String HOME_PHONE_NUM_CODE="BF";
    public static final String OWNER_CODE="BG";
    public static final String CURRENCY_TYPE_CODE="BH";
    public static final String CANCEL_CODE="BI";
    public static final String TRANSACTION_ID_CODE="BK";
    public static final String VALID_PATRON_CODE="BL";
    public static final String RENEWED_ITEMS_CODE="BM";
    public static final String UN_RENEWED_ITEMS_CODE="BN";
    public static final String FEE_ACKNOWLEDGED_CODE="BO";
    public static final String START_ITEM_CODE="BP";
    public static final String END_ITEM_CODE="BQ";
    public static final String QUEUE_POSITION_CODE="BR";
    public static final String PICKUP_LOCATION_CODE="BS";
    public static final String FEE_TYPE_CODE="BT";
    public static final String RECALL_ITEMS_CODE="BU";
    public static final String FEE_AMOUNT_CODE="BV";
    public static final String EXPIRATION_DATE_CODE="BW";
    public static final String SUPPORTED_MSG_CODE="BX";
    public static final String HOLD_TYPE_CODE="BY";
    public static final String HOLD_ITEMS_LIMIT_CODE="BZ";
    public static final String OVERDUE_ITEMS_LIMIT_CODE="CA";
    public static final String CHARGED_ITEMS_LIMIT_CODE="CB";
    public static final String FEE_LIMIT_CODE="CC";
    public static final String UN_AVA_HOLD_ITEMS_CODE="CD";
    public static final String HOLD_QUEUE_LENGTH_CODE="CF";
    public static final String FEE_IDENTIFIER_CODE="CG";
    public static final String ITEM_PROPERTIES_CODE="CH";
    public static final String SECURITY_INHIBIT_CODE="CI";
    public static final String RECALL_DATE_CODE="CJ";
    public static final String MEDIA_TYPE_CODE="CK";
    public static final String SORT_BIN_CODE="CL";
    public static final String HOLD_PICKUP_DATE_CODE="CM";
    public static final String LOGIN_USER_ID_CODE="CN";
    public static final String LOGIN_PWD_ID_CODE="CO";
    public static final String LOCATION_CODE_SIP="CP";
    public static final String VALID_PATRON_PWD="CQ";

    //SIP2 Request Code
    public static final String PATRON_STATUS_REQUEST="23";
    public static final String CHECKOUT_REQUEST="11";
    public static final String CHECK_IN_REQUEST="09";
    public static final String BLOCK_PATRON_REQUEST="01";
    public static final String SC_STATUS_REQUEST="99";
    public static final String ACS_REQUEST="97";
    public static final String LOGIN_REQUEST="93";
    public static final String PATRON_INFORMATION_REQUEST="63";
    public static final String END_PATRON_SESSION_REQUEST="35";
    public static final String FEE_PAID_REQUEST="37";
    public static final String ITEM_INFORMATION_REQUEST="17";
    public static final String ITEM_STATUS_UPDATE_REQUEST="19";
    public static final String PATRON_ENABLE_REQUEST="25";
    public static final String HOLD_REQUEST="15";
    public static final String RENEW_REQUEST="29";
    public static final String RENEW_ALL_REQUEST="65";

    //SIP2 Response Code
    public static final String PATRON_STATUS_RESPONSE="24";
    public static final String CHECKOUT_RESPONSE="12";
    public static final String CHECK_IN_RESPONSE="10";
    public static final String ACS_STATUS_RESPONSE="98";
    public static final String SC_RESEND_RESPONSE="96";
    public static final String LOGIN_RESPONSE="94";
    public static final String PATRON_INFORMATION_RESPONSE="64";
    public static final String END_PATRON_SESSION_RESPONSE="36";
    public static final String FEE_PAID_RESPONSE="38";
    public static final String ITEM_INFORMATION_RESPONSE="18";
    public static final String ITEM_STATUS_UPDATE_RESPONSE="20";
    public static final String PATRON_ENABLE_RESPONSE="26";
    public static final String HOLD_RESPONSE="16";
    public static final String RENEW_RESPONSE="30";
    public static final String RENEW_ALL_RESPONSE="66";

    public static final String CHECK_IN_SUCCESS="SuccessFully Checked-in";
    public static final String RENEW_SUCCESS="Item successFully renewed";
    public static final String CHECK_OUT_SUCCESS="Successfully loaned";
    public static final String CHECK_OUT_SUCCESSFULLY="Item Successfully loaned";
    public static final String CHECK_OUT_FAILED="Item cannot be loaned : please consult circulation staff for more information.";
    public static final String PATRON_SESSION_END="Patron Session Ended Successfully";
    public static final String FEE_PAID="Successfully Paid the Amount";
    public static final String SERVICE_ERROR="Cannot Able to process this service";
    public static final String REQUEST_RAISED="Request Raised Succesfully";
    public static final String SUCCESSFULLY="Succesfully";
    public static final String SERVICE_UNAVAILABLE="Update Hold Request Service Currently Not Available in OLE";
    public static final String ITEM_UNAVAILABLE="Item is Not Available";
    public static final String ITEM_SERVICE_NOT_SUPPORTED="Item Status Update Service - Not supported in OLE";
    public static final String PATRON_ENABLED="Patron Enabled successfully";
    public static final String YES="YES";
    public static final String ONHOLD="ONHOLD";
    public static final String SCREEN_FAILURE_MSG="Checkout Privileges Denied";
    public static final String SCREEN_SUCCESS_MSG="Checkout Privileges Permitted";
    public static final String REQUEST_PROCESSED="Requested process completed successfully.";


    public static final String AUTHOR="Author";

}
