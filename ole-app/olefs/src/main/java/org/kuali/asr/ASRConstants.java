package org.kuali.asr;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 1/16/14
 * Time: 11:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class ASRConstants {

    /* ASR ERROR MESSAGES*/
    public static final String ITEM_NOT_FOUND="error.no.item";
    public static final String HAVING_HOLD="We could not place the hold.  You already have a hold on this item";
    public static final String CHECKED_IN="We could not place the hold.  You already have this item checked out";
    public static final String NO_CIRCULATION="We could not place the hold.  This item may not be available for circulation.  Please contact your library for assistance";
    public static final String EXCEED_NO_OF_HOLD="We could not place the hold.  You have exceeded the limit for the number of holds per user.";
    public static final String CARD_BLOCKED="We could not place the hold.  Your library card may be blocked.  Please contact your library for assistance";
    public static final String HOLD_PLACED="Your hold has been placed";
    public static final String PATRON_NOT_FOUND="error.no.patron";
    public static final String OPERATOR_NOT_FOUND="error.no.operator";
    public static final String ITEM_STATUS_NOT_UPDATED="error.no.item.status.update";
    public static final String REQUEST_NOT_FOUND="error.no.request.found";
    public static final String REQUEST_CANCELED="request.cancelled";
    public static final String REQUEST_NOT_FOUND_FOR_HOLD_ID="error.request.id.not.found";
    public static final String REQUEST_STATUS_UPDATION_FAILED="Request Status updation is failed for request id:";
    public static final String REQUEST_STATUS_UPDATED="Request Status successfully updated for request id:";
    public static final String ASR_LIBRARY_NOT_FOUND="ASR Library is not found";
    public static final String ITEM_DELETED="item.deleted.message";
    public static final String NOT_ASR_ITEM="error.no.asr.item";
    public static final String ITEM_STATUS_UPDATED="item.status.updated";
    public static final String ITEM_NOT_IN_TRANSIT="error.item.not.transit";
    public static final String LOCATION_MISMATCH="error.invalid.transit.location";
    public static final String ACCESS_FORBIDDEN="Access Forbidden";
    public static final String INVALID_PKUP_LOCN ="error.invalid.pickup.location";
    public static final String SUCESS_REQUEST_MESG = "item.requested.message";
    public static final String FAILURE_REQUEST_MESG = "internal.server.error";
    public static final String SUCESS_ITEM_MESG = "item.add.message";
    public static final String EXCEPTION = "method.failure";
    public static final String STATUS_NOT_MATCH = "error.status.mismatch";


    /*ASR ERROR MESSAGES CODE*/
    public static final String ITEM_NOT_FOUND_CODE="007";
    public static final String HAVING_HOLD_CODE="002";
    public static final String CHECKED_IN_CODE="003";
    public static final String NO_CIRCULATION_CODE="004";
    public static final String EXCEED_NO_OF_HOLD_CODE="005";
    public static final String CARD_BLOCKED_CODE="006";
    public static final String HOLD_PLACED_CODE="001";
    public static final String PATRON_NOT_FOUND_CODE="008";
    public static final String OPERATOR_NOT_FOUND_CODE="009";
    public static final String ITEM_STATUS_NOT_UPDATED_CODE="010";
    public static final String REQUEST_NOT_FOUND_CODE="011";
    public static final String REQUEST_CANCELED_CODE="012";
    public static final String REQUEST_NOT_FOUND_FOR_HOLD_ID_CODE="013";
    public static final String REQUEST_STATUS_UPDATION_FAILED_CODE="014";
    public static final String REQUEST_STATUS_UPDATED_CODE="015";
    public static final String ASR_LIBRARY_NOT_FOUND_CODE="016";
    public static final String ITEM_DELETED_CODE="017";
    public static final String NOT_ASR_ITEM_CODE="018";
    public static final String ITEM_STATUS_UPDATED_CODE="000";
    public static final String ITEM_NOT_IN_TRANSIT_CODE="002";
    public static final String LOCATION_MISMATCH_CODE="003";
    public static final String ACCESS_FORBIDDEN_CODE="";
    public static final String INVALID_PKUP_LOCN_CD ="019";
    public static final String SUCESS_REQUEST_CD = "000";
    public static final String EXCEPTION_CODE = "020";
    public static final String STATUS_NOT_MATCH_CODE = "021";

    /*ASR ERROR MESSAGES STATUS*/
    public static final int ITEM_NOT_FOUND_STATUS=404;
    public static final int HAVING_HOLD_STATUS=400;
    public static final int CHECKED_IN_STATUS=400;
    public static final int NO_CIRCULATION_STATUS=400;
    public static final int EXCEED_NO_OF_HOLD_STATUS=400;
    public static final int CARD_BLOCKED_STATUS=400;
    public static final int HOLD_PLACED_STATUS=200;
    public static final int PATRON_NOT_FOUND_STATUS=400;
    public static final int OPERATOR_NOT_FOUND_STATUS=400;
    public static final int ITEM_STATUS_NOT_UPDATED_STATUS=400;
    public static final int REQUEST_NOT_FOUND_STATUS=400;
    public static final int REQUEST_CANCELED_STATUS=200;
    public static final int REQUEST_NOT_FOUND_FOR_HOLD_ID_STATUS=404;
    public static final int REQUEST_STATUS_UPDATION_FAILED_STATUS=400;
    public static final int REQUEST_STATUS_UPDATED_STATUS=400;
    public static final int ASR_LIBRARY_NOT_FOUND_STATUS=400;
    public static final int ITEM_DELETED_STATUS=200;
    public static final int NOT_ASR_ITEM_STATUS=204;
    public static final int ITEM_STATUS_UPDATED_STATUS=200;
    public static final int ITEM_NOT_IN_TRANSIT_STATUS=400;
    public static final int LOCATION_MISMATCH_STATUS=400;
    public static final int ACCESS_FORBIDDEN_STATUS=403;
    public static final int INVALID_PKUP_LOCN_STAT =400;
    public static final int SUCCESS_STATUS=200;
    public static final int FAILURE_STATUS=500;
    public static final int EXCEPTION_STATUS=420;
    public static final int STATUS_NOT_MATCH_STATUS=420;

    /*Constants*/
    public static final String ITEM_BARCODE_DISPLAY="ItemBarcode_display";
    public static final String LOCATION_LEVEL_DISPLAY="Location_display";
    public static final String REQUEST_RAISED="Request raised succesfully";
    public static final String HOLD="HOLD";
    public static final String INTRANSIT_FOR_HOLD="INTRANSIT-FOR-HOLD";
    public static final String ON_HOLD="ONHOLD";
    public static final String IN_TRANSIT="INTRANSIT";
    public static final String AVAILABLE="AVAILABLE";
    public static final String ASR_TYP_RQST="ASR_REQUEST_TYPE";
    public static final String ITEM_AVAILABLE="csa";
    public static final String ITEM_MISSING="csn";
    public static final String ITEM_RETRIVED="csp";
    public static final String ASR_ITEM_AVAILABLE="ASR_ITEM_AVAILABLE";
    public static final String ASR_ITEM_MISSING="ASR_ITEM_MISSING";
    public static final String ASR_ITEM_RETRIVED="ASR_ITEM_RETRIVED";
    public static final String ASR_REQUEST_IN_PROCESS="3";
    public static final String ASR_REQUEST_FAILURE="5";
    public static final String ASR_REQUEST_ITEM_STATUS="ASR_REQUEST_ITEM_STATUS";
    public static final String ITEM_STATUS_UPDATED_HOLD="item.status.updated.to.hold";



}
