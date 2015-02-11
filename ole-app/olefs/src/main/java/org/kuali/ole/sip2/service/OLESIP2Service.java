package org.kuali.ole.sip2.service;

import org.kuali.ole.sip2.requestParser.*;

/**
 * Created by sheiksalahudeenm on 1/5/15.
 */
public interface OLESIP2Service {


    public String feePaidService(OLESIP2FeePaidRequestParser sip2FeePaidRequestParser, String service, String operatorId);

    public String processRequest(String requestData, String service, String operatorId);

    public String loginService(OLESIP2LoginRequestParser loginRequestParser, String service, String operatorId);

    public String scStatusService(OLESIP2SCStatusRequestParser sip2SCStatusRequestParser, String service, String operatorId);

    public String checkOutService(OLESIP2CheckOutRequestParser sip2CheckOutRequestParser, String service, String operatorId);

    public String checkInService(OLESIP2CheckInRequestParser sip2CheckInRequestParser, String service, String operatorId);

    public String patronStatusService(OLESIP2PatronStatusRequestParser sip2PatronStatusRequestParser, String service, String operatorId);

    public String patronInformationService(OLESIP2PatronInformationRequestParser sip2PatronInformationRequestParser, String service, String operatorId);

    public String itemInformationService(OLESIP2ItemInformationRequestParser sip2ItemInformationRequestParser, String service, String operatorId);

    public String blockPatronService(OLESIP2BlockPatronRequestParser sip2BlockPatronRequestParser, String service, String operatorId);

    public String patronEnableService(OLESIP2PatronEnableRequestParser sip2PatronEnableRequestParser, String service, String operatorId);

    public String renewService(OLESIP2RenewRequestParser sip2RenewRequestParser, String service, String operatorId);

    public String holdService(OLESIP2HoldRequestParser sip2HoldRequestParser, String service, String operatorId);

    public String getCirculationErrorMessage(String service, String message, String code, String requiredParameters, String outputFormat);

    public String renewAllService(OLESIP2RenewAllRequestParser sip2RenewAllRequestParser, String service, String operatorId);

    public String itemStatusUpdateService(String requestData, OLESIP2ItemStatusUpdateRequestParser oleSIP2ItemStatusUpdateRequestParser, String service, String operatorId);
}
