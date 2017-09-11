package org.kuali.ole.ncip.bo;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 8/7/13
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLENCIPConstants {

    private static final Logger LOG = Logger.getLogger(OLENCIPConstants.class);
    public static final String CIRCULATION_SERVICE = "oleCirculationService";
    public static final String CIRCULATION_HELPER_SERVICE = "oleCirculationHelperService";
    public static final String CHECKIN_ITEM_CONVERTER= "oleCheckInItemConverter";
    public static final String CHECKOUT_ITEM_CONVERTER = "oleCheckOutItemConverter";
    public static final String LOOKUP_USER_CONVERTER = "oleLookupUserConverter";

    /*Circulation Service Required Parameter Constants*/
    public static final String LOOKUPUSER ="service,patronBarcode,operatorId";
    public static final String GETCHECKOUTITEMS ="service,patronBarcode,operatorId";
    public static final String PLACEREQUEST ="service,patronBarcode,operatorId,itemBarcode,requestType";
    public static final String CANCELREQUEST ="service,operatorId,requestId";
    public static final String RENEWITEM ="service,patronBarcode,operatorId,itemBarcode";
    public static final String ACCEPTITEM ="service,patronBarcode,operatorId,itemBarcode,callNumber,title,author,itemType,itemLocation,dateExpires,requestType";
    public static final String CHECKINITEM ="service,patronBarcode,operatorId,itemBarcode,deleteIndicator";
    public static final String CHECKOUTITEM ="service,patronBarcode,operatorId,itemBarcode";
    public static final String FINE ="service,patronBarcode,operatorId";
    public static final String HOLDS ="service,patronBarcode,operatorId";

    /*Circulation Service name Constants*/
    public static final String PLACEREQUEST_SERVICE ="placeRequest";
    public static final String CANCELREQUEST_SERVICE ="cancelRequest";
    public static final String RENEWITEM_SERVICE ="renewItem";
    public static final String RENEWITEMLIST_SERVICE ="renewItemList";
    public static final String ACCEPTITEM_SERVICE ="acceptItem";
    public static final String CHECKINITEM_SERVICE ="checkInItem";
    public static final String CHECKOUTITEM_SERVICE ="checkOutItem";
    public static final String GETCHECKEDOUTITEM_SERVICE ="getCheckedOutItems";
    public static final String FINE_SERVICE ="fine";
    public static final String HOLDS_SERVICE ="holds";
    public static final String LOOKUPUSER_SERVICE ="lookupUser";

    /*Circulation Service parameter name's constants*/
    public static final String OLE_CIRCULATION_SERVICE ="service";
    public static final String PATRON_ID ="patronId";
    public static final String PATRON_BARCODE="patronBarcode";
    public static final String OPERATOR_ID ="operatorId";
    public static final String REQUEST_ID ="requestId";
    public static final String ITEM_BARCODE ="itemBarcode";
    public static final String ITEM_ID ="itemId";
    public static final String CALLNUMBER ="callNumber";
    public static final String TITLE ="title";
    public static final String AUTHOR ="author";
    public static final String ITEM_TYPE ="itemType";
    public static final String ITEM_LOCATION ="itemLocation";
    public static final String DATE_EXPIRES ="dateExpires";
    public static final String FORMAT ="format";
    public static final String DELETE_INDICATOR ="deleteIndicator";
    public static final String PICKUP_LOCATION ="pickupLocation";
    public static final String REQUEST_TYPE ="requestType";
    public static final String REQUEST_NOTE="requestNote";
    public static final String NCIPAPI_PARAMETER_NAME = "NCIPAPI_VALUES";
    public static final String TEMP_ITEM_DELETE_INDICATOR="TEMP_ITEM_DELETE_INDICATOR";

    /*Ciculation Service message Constants*/
    public static final String INVALID_PARAMETERS ="Contains Invalid Parameters";
    public static final String PARAMETER_MISSING ="Parameter is Missing";
    public static final String INVALID_FORMAT ="Unknown output format. It should be either XML or JSON";
    public static final String UNKNOWN_SERVICE ="Unknown Service Name";
    public static final String XML_FORMAT ="xml";
    public static final String JSON_FORMAT ="json";
    public static final String NULL_SERVICE ="<message>Service is NULL</message>";
    public static final String INVALID_DATA ="<response>Invalid Data</response>";
    public static final String MESSAGE ="message";
    public static final String INVALID_INPUT ="Invalid Input";
    public static final String XML_CONTENT_TYPE ="text/xml";
    public static final String XML_CHAR_ENCODING ="UTF-8";

    public static final String TELEPHONE_CODE = "TEL";
    public static final String ADDRESS_TYPE_SCHEME = "http://www.niso.org/ncip/v1_0/imp1/schemes/physicaladdresstype/physicaladdresstype.scm";
    public static final String EMAIL = "NCIP_EMAILADDRESS_TYPE";

    public static final String CHECK_ALL_OVERDUE_FINE_AMT = "Check all Overdue fine amount";
    public static final String CHECK_OVERALL_CHARGES = "Check Overall Charges";
    public static final String CHECK_REPLACEMENT_FEE_AMT = "Check Replacement fee amt";

    public static final String USER = "USER";
    public static final String OPERATOR = "OPERATOR";

    public static final String USER_UN_AVAILABLE = "error.user.unavailable";
    public static final String USER_DOES_NOT_EXIST = "error.user.doesnt.exist";
    public static final String USER_IDENTIFIER_VALUE_DOES_NOT_EXIST = "error.user.identifier.value.doesnt.exist";
    public static final String ITEM_IDENTIFIER_VALUE_DOES_NOT_EXIST = "error.item.identifier.value.doesnt.exist";
    public static final String TITLE_DOES_NOT_EXIST = "error.title.doesnt.exist";
    public static final String OPERATOR_UN_AVAILABLE = "error.operator.unavailable";


    public static final String AGENCY_ID = "AGENCY ID";
    public static final String INVALID_AGENCY_ID = "error.invalid.agency.id";
    public static final String IDENTIFIER_TYPE = "Identifier Type";
    public static final String MEDIUM_TYPE = "Medium Type";
    public static final String REQUEST_TYPES = "Request Type";
    public static final String PAYMENT_METHOD_TYPE = "Payment Method Type";
    public static final String FISCAL_ACTION_TYPE = "Fiscal Action Type";
    public static final String FISCAL_TRANSACTION_TYPE = "Fiscal Transaction Type";
    public static final String ITEM_BARCODES = "Item Barcode";
    public static final String CASH = "Cash";
    public static final String FINES = "Fine";
    public static final String REQUEST_IDS = "Request Id";
    public static final String SCHEME = "Scheme";
    public static final String ITEM = "Item";
    public static final String REQUEST_FAIL = "error.request.failed";
    public static final String RENEW_FAIL = "error.renew.fail";
    public static final String CHECK_IN_FAIL="error.checkin.fail";
    public static final String CHECK_OUT_FAIL="error.checkout.fail";
    public static final String ACCEPT_ITEM_FAIL="error.accept.item.fail";
    public static final String AGENCY_ID_PARAMETER="AGENCY_ID";
    public static final String USD="USD";


    public static final String REQUEST_FORMAT_TYPE = "requestFormatType";
    public static final String RESPONSE_FORMAT_TYPE = "responseFormatType";
    public static final String CIRC_ITEM_BARCODES = "itemBarcodes";
}
