package org.kuali.ole.sip2.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleItemSearch;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.ncip.bo.*;
import org.kuali.ole.ncip.converter.*;
import org.kuali.ole.ncip.service.OLECirculationService;
import org.kuali.ole.ncip.service.impl.OLECirculationHelperServiceImpl;
import org.kuali.ole.ncip.service.impl.OLECirculationServiceImpl;
import org.kuali.ole.olekrad.filter.OLELoginFilter;
import org.kuali.ole.service.OlePatronService;
import org.kuali.ole.service.OlePatronServiceImpl;
import org.kuali.ole.sip2.common.OLESIP2Util;
import org.kuali.ole.sip2.constants.OLESIP2Constants;
import org.kuali.ole.sip2.requestParser.*;
import org.kuali.ole.sip2.service.OLESIP2HelperService;
import org.kuali.ole.sip2.service.OLESIP2Service;
import org.kuali.ole.sip2.sip2Response.*;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 1/5/15.
 */
public class OLESIP2ServiceImpl implements OLESIP2Service {

    private static final Logger LOG = Logger.getLogger(OLESIP2ServiceImpl.class);

    private DocstoreClientLocator docstoreClientLocator;
    private DocstoreUtil docstoreUtil;
    private int totalRecCount;
    private OlePatronService olePatronService;

    OLESIP2HelperService OLESIP2HelperService = new OLESIP2HelperServiceImpl();
    OLECirculationService oleCirculationService = new OLECirculationServiceImpl();
    private OLECirculationHelperServiceImpl oleCirculationHelperService = new OLECirculationHelperServiceImpl();

    public OlePatronService getOlePatronService() {

        if (olePatronService == null)
            olePatronService = new OlePatronServiceImpl();
        return olePatronService;
    }


    private BusinessObjectService businessObjectService;

    protected BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public DocstoreUtil getDocstoreUtil() {
        if (docstoreUtil == null) {
            docstoreUtil = SpringContext.getBean(DocstoreUtil.class);
        }
        return docstoreUtil;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    @Override
    public String feePaidService(OLESIP2FeePaidRequestParser sip2FeePaidRequestParser, String service, String operatorId) {

        String responseString = "";
        String message = "";
        KualiDecimal feeAmount = new KualiDecimal(Double.parseDouble(sip2FeePaidRequestParser.getFeeAmount()));
        if (sip2FeePaidRequestParser.getCurrencyType().equals(OLESIP2Util.getDefaultCurrency().getCurrencyCode())) {
            message = OLESIP2HelperService.feePaid(sip2FeePaidRequestParser.getPatronIdentifier(), sip2FeePaidRequestParser.getFeeType(),
                    sip2FeePaidRequestParser.getFeeIdentifier(), sip2FeePaidRequestParser.getPaymentType(), feeAmount,
                    sip2FeePaidRequestParser.getTransactionId(), operatorId);
        } else {
            message = "Please Enter Valid Currency";
        }


        OLESIP2FeePaidResponse sip2FeePaidResponse = new OLESIP2FeePaidResponse();
        responseString = sip2FeePaidResponse.getFeePaidResponse(message, sip2FeePaidRequestParser);

        if (responseString == null || responseString.equalsIgnoreCase("")) {
            OLENCIPErrorResponse olencipErrorResponse = new OLENCIPErrorResponse();
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE, sip2FeePaidRequestParser.getPatronIdentifier());
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID, operatorId);
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE, OLENCIPConstants.INVALID_INPUT);
            responseString = olencipErrorResponse.getErrorXml(service);
        }


        return responseString;
    }


    @Override
    public String processRequest(String requestData, String service, String operatorId) {
        String responseString = "";

        if (requestData.startsWith("99")) {

            LOG.info("Request Type :  SC Status Request");
            OLESIP2SCStatusRequestParser sip2SCStatusRequestParser = new OLESIP2SCStatusRequestParser(requestData);
            responseString = this.scStatusService(sip2SCStatusRequestParser, service, operatorId);

        } else if (requestData.startsWith("93")) {
            LOG.info("Request Type :  Login Request");
            OLESIP2LoginRequestParser loginRequestParser = new OLESIP2LoginRequestParser(requestData);
            responseString = this.loginService(loginRequestParser, service, operatorId);


        } else if (requestData.startsWith("11")) {
            LOG.info("Request Type :  Checkout Request");
            OLESIP2CheckOutRequestParser sip2CheckOutRequestParser = new OLESIP2CheckOutRequestParser(requestData);
            responseString = this.checkOutService(sip2CheckOutRequestParser, service, operatorId);

        } else if (requestData.startsWith("09")) {
            LOG.info("Request Type :  Check-in Request");
            OLESIP2CheckInRequestParser sip2CheckInRequestParser = new OLESIP2CheckInRequestParser(requestData);
            responseString = this.checkInService(sip2CheckInRequestParser, service, operatorId);


        } else if (requestData.startsWith("17")) {
            LOG.info("Request Type :  Item Information");
            OLESIP2ItemInformationRequestParser sip2ItemInformationRequestParser = new OLESIP2ItemInformationRequestParser(requestData);
            responseString = this.itemInformationService(sip2ItemInformationRequestParser, service, operatorId);

        } else if (requestData.startsWith("23")) {
            LOG.info("Request Type :  Patron Status Request");
            OLESIP2PatronStatusRequestParser sip2PatronStatusRequestParser = new OLESIP2PatronStatusRequestParser(requestData);
            responseString = this.patronStatusService(sip2PatronStatusRequestParser, service, operatorId);

        } else if (requestData.startsWith("63")) {
            LOG.info("Request Type :  Patron Information");
            OLESIP2PatronInformationRequestParser sip2PatronInformationRequestParser = new OLESIP2PatronInformationRequestParser(requestData);
            responseString = this.patronInformationService(sip2PatronInformationRequestParser, service, operatorId);

        } else if (requestData.startsWith("01")) {
            LOG.info("Request Type :  Patron Block");

            OLESIP2BlockPatronRequestParser sip2BlockPatronRequestParser = new OLESIP2BlockPatronRequestParser(requestData);
            responseString = this.blockPatronService(sip2BlockPatronRequestParser, service, operatorId);

        } else if (requestData.startsWith("25")) {
            LOG.info("Request Type :  Patron Enable");
            OLESIP2PatronEnableRequestParser sip2PatronEnableRequestParser = new OLESIP2PatronEnableRequestParser(requestData);
            responseString = this.patronEnableService(sip2PatronEnableRequestParser, service, operatorId);


        } else if (requestData.startsWith("35")) {
            LOG.info("Request Type :  End Patron Session");
            OLESIP2EndPatronSessionRequestParser sip2EndPatronSessionRequestParser = new OLESIP2EndPatronSessionRequestParser(requestData);
            OLESIP2EndPatronSessionResponse olesip2EndPatronSessionResponse = new OLESIP2EndPatronSessionResponse();
            responseString = olesip2EndPatronSessionResponse.getEndPatronSession(sip2EndPatronSessionRequestParser);


        } else if (requestData.startsWith("29")) {
            LOG.info("Request Type :  Renew");
            System.out.println(requestData);
            OLESIP2RenewRequestParser sip2RenewRequestParser = new OLESIP2RenewRequestParser(requestData);
            responseString = this.renewService(sip2RenewRequestParser, service, operatorId);

        } else if (requestData.startsWith("15")) {
            LOG.info("Request Type :  Hold");
            OLESIP2HoldRequestParser sip2HoldRequestParser = new OLESIP2HoldRequestParser(requestData);
            responseString = this.holdService(sip2HoldRequestParser, service, operatorId);

        } else if (requestData.startsWith("37")) {
            LOG.info("Request Type :  Fee Paid Message");
            OLESIP2FeePaidRequestParser sip2FeePaidRequestParser = new OLESIP2FeePaidRequestParser(requestData);
            responseString = this.feePaidService(sip2FeePaidRequestParser, service, operatorId);
        } else if (requestData.startsWith("65")) {
            LOG.info("Request Type :  Renew All");
            OLESIP2RenewAllRequestParser sip2RenewAllRequestParser = new OLESIP2RenewAllRequestParser(requestData);
            responseString = this.renewAllService(sip2RenewAllRequestParser, service, operatorId);

        } else if (requestData.startsWith("19")) {
            LOG.info("Request Type :  Item Status Update");
            OLESIP2ItemStatusUpdateRequestParser sip2ItemStatusUpdateRequestParser = new OLESIP2ItemStatusUpdateRequestParser(requestData);
            //TODO This service is not currently implemented in ole
            responseString = this.itemStatusUpdateService(requestData, sip2ItemStatusUpdateRequestParser, service, operatorId);
        } else {
            LOG.info("Request Type :  *****Not a valid request");
            responseString = "Not a valid SIP2 request";
            new Throwable("Not a valid SIP2 request");

        }
        LOG.info("Exit SOASNettyServerHandler.analysisRequestType(String requestData)");
        return responseString;
    }


    @Override
    public String loginService(OLESIP2LoginRequestParser loginRequestParser, String service, String operatorId) {
        OLELoginFilter oleLoginFilter = new OLELoginFilter();
        String responseString = "";
        Boolean validateUser = oleLoginFilter.checkValidUserNameAndPassword(loginRequestParser.getLoginUserId(),
                loginRequestParser.getLoginPassword());

        OLESIP2LoginResponse sip2LoginResponseParser = new OLESIP2LoginResponse();
        responseString = sip2LoginResponseParser.getSIP2LoginResponse(validateUser, loginRequestParser);
        if (responseString == null) {
            OLENCIPErrorResponse olencipErrorResponse = new OLENCIPErrorResponse();
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE, loginRequestParser.getLoginUserId());
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID, loginRequestParser.getLoginPassword());
            responseString = olencipErrorResponse.getErrorXml(service);
        }
        return responseString;
    }

    @Override
    public String scStatusService(OLESIP2SCStatusRequestParser sip2SCStatusRequestParser, String service, String operatorId) {
        String responseString = "";
        OLESIP2ACSStatusResponse sip2ACSStatusResponseParser = new OLESIP2ACSStatusResponse();

        responseString = sip2ACSStatusResponseParser.getSIP2ACSStatusResponse(sip2SCStatusRequestParser);

        if (responseString == null) {
            OLENCIPErrorResponse olencipErrorResponse = new OLENCIPErrorResponse();
            responseString = olencipErrorResponse.getErrorXml(service);
        }

        return responseString;

    }

    @Override
    public String checkOutService(OLESIP2CheckOutRequestParser sip2CheckOutRequestParser, String service, String operatorId) {
        String responseString = null;
        OLECirculationService oleCirculationService = new OLECirculationServiceImpl();


        Map patronMap = new HashMap();
        patronMap.put("barcode", sip2CheckOutRequestParser.getPatronIdentifier());
        List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) getBusinessObjectService()
                .findMatching(OlePatronDocument.class, patronMap);

        if (olePatronDocumentList != null && olePatronDocumentList.size() > 0) {
            Map barMap = new HashMap();
            barMap.put("itemId", sip2CheckOutRequestParser.getItemIdentifier());
            barMap.put("patronId", olePatronDocumentList.get(0).getOlePatronId());
            List<OleLoanDocument> oleLoanDocuments = (List<OleLoanDocument>) getBusinessObjectService().findMatchingOrderBy(OleLoanDocument.class, barMap, "loanId", true);
            if (oleLoanDocuments != null && oleLoanDocuments.size() > 0) {

                responseString = oleCirculationService.renewItem(sip2CheckOutRequestParser.getPatronIdentifier(),
                        operatorId,
                        sip2CheckOutRequestParser.getItemIdentifier(), true);

                if (responseString != null && !responseString.equalsIgnoreCase("")) {
                    OLERenewItem oleRenewItem = (OLERenewItem) new OLERenewItemConverter().generateRenewItemObject(responseString);
                    OLESIP2CheckOutResponse sip2CheckOutResponseParser = new OLESIP2CheckOutResponse();
                    responseString = sip2CheckOutResponseParser.getSIP2CheckOutResponse(oleRenewItem, sip2CheckOutRequestParser);
                }
            } else {
                responseString = oleCirculationService.checkOutItem(sip2CheckOutRequestParser.getPatronIdentifier(),
                        operatorId,
                        sip2CheckOutRequestParser.getItemIdentifier(), true);

                if (responseString != null && !responseString.equalsIgnoreCase("")) {
                    OLECheckOutItem oleCheckOutItem = (OLECheckOutItem) new OLECheckOutItemConverter().generateCheckoutItemObject(responseString);
                    OLESIP2CheckOutResponse sip2CheckOutResponseParser = new OLESIP2CheckOutResponse();
                    responseString = sip2CheckOutResponseParser.getSIP2CheckOutResponse(oleCheckOutItem, sip2CheckOutRequestParser);
                }

            }

        }
        if (responseString == null || responseString.equalsIgnoreCase("")) {
            OLENCIPErrorResponse olencipErrorResponse = new OLENCIPErrorResponse();
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE, sip2CheckOutRequestParser.getPatronIdentifier());
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID, operatorId);
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE, OLENCIPConstants.INVALID_INPUT);
            responseString = olencipErrorResponse.getErrorXml(service);
        }


        return responseString;

    }

    @Override
    public String checkInService(OLESIP2CheckInRequestParser sip2CheckInRequestParser, String service, String operatorId) {
        String responseString = "";
        OLECirculationService oleCirculationService = new OLECirculationServiceImpl();
        OLECheckInItem oleCheckInItem = new OLECheckInItem();


        LoanProcessor loanProcessor = new LoanProcessor();

        OleLoanDocument oleLoanDocument = loanProcessor.getOleLoanDocumentUsingItemBarcode(sip2CheckInRequestParser.getItemIdentifier());

        if(oleLoanDocument != null){
            responseString = oleCirculationService.checkInItem(oleLoanDocument.getPatronBarcode(),
                    operatorId,
                    sip2CheckInRequestParser.getItemIdentifier(),
                    "N", true);
            if (responseString != null && !responseString.equalsIgnoreCase("")) {
                oleCheckInItem = (OLECheckInItem) new OLECheckInItemConverter().generateCheckInItemObject(responseString);
                OLESIP2CheckInResponse sip2CheckInResponseParser = new OLESIP2CheckInResponse();
                responseString = sip2CheckInResponseParser.getSIP2CheckInResponse(oleCheckInItem, sip2CheckInRequestParser);
            }
        }else{
            oleCheckInItem.setCode("500");
            oleCheckInItem.setMessage("Item is not currently checked out.");
            OLESIP2CheckInResponse sip2CheckInResponseParser = new OLESIP2CheckInResponse();
            responseString = sip2CheckInResponseParser.getSIP2CheckInResponse(oleCheckInItem, sip2CheckInRequestParser);
        }



        if (responseString == null) {
            OLENCIPErrorResponse olencipErrorResponse = new OLENCIPErrorResponse();
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE, oleLoanDocument.getPatronBarcode());
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID, operatorId);
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE, OLENCIPConstants.INVALID_INPUT);
            responseString = olencipErrorResponse.getErrorXml(service);
        }

        return responseString;

    }


    @Override
    public String patronStatusService(OLESIP2PatronStatusRequestParser sip2PatronStatusRequestParser, String service, String operatorId) {
        String responseString = "";
        OLECirculationService oleCirculationService = new OLECirculationServiceImpl();

        responseString = oleCirculationService.lookupUser(sip2PatronStatusRequestParser.getPatronIdentifier(),
                operatorId, null, true);

        if (responseString != null && !responseString.equalsIgnoreCase("")) {
            OLELookupUser oleLookupUser = (OLELookupUser) new OLELookupUserConverter().getLookupUser(responseString);
            OLESIP2PatronStatusResponse sip2PatronStatusResponse = new OLESIP2PatronStatusResponse();

            responseString = sip2PatronStatusResponse.getSIP2PatronStatusResponse(oleLookupUser,
                    sip2PatronStatusRequestParser);

        }
        if (responseString == null) {
            OLENCIPErrorResponse olencipErrorResponse = new OLENCIPErrorResponse();
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE, sip2PatronStatusRequestParser.getPatronIdentifier());
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID, operatorId);
            responseString = olencipErrorResponse.getErrorXml(service);
        }


        return responseString;
    }

    @Override
    public String patronInformationService(OLESIP2PatronInformationRequestParser sip2PatronInformationRequestParser, String service, String operatorId) {
        String responseString = "";

        responseString = oleCirculationService.lookupUser(sip2PatronInformationRequestParser.getPatronIdentifier(),
                operatorId, null, true);

        if (responseString != null && !responseString.equalsIgnoreCase("")) {
            OLELookupUser oleLookupUser = (OLELookupUser) new OLELookupUserConverter().getLookupUser(responseString);
            OLESIP2PatronInformationResponse sip2PatronInformationResponse = new OLESIP2PatronInformationResponse();

            responseString = sip2PatronInformationResponse.getSIP2PatronInfoResponse(oleLookupUser,
                    sip2PatronInformationRequestParser);

        }
        if (responseString == null) {
            OLENCIPErrorResponse olencipErrorResponse = new OLENCIPErrorResponse();
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE, sip2PatronInformationRequestParser.getPatronIdentifier());
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID, operatorId);
            responseString = olencipErrorResponse.getErrorXml(service);
        }


        return responseString;

    }

    @Override
    public String itemInformationService(OLESIP2ItemInformationRequestParser sip2ItemInformationRequestParser, String service, String operatorId) {
        String responseString = "";

        OleItemSearch oleItemSearch = new OleItemSearch();
        List<OleItemSearch> oleItemSearches = new ArrayList<>();
        SearchParams item_search_Params = new SearchParams();
        item_search_Params.getSearchConditions().add(item_search_Params.buildSearchCondition("phrase", item_search_Params.buildSearchField(DocType.ITEM.getCode(), Item.ITEM_BARCODE, sip2ItemInformationRequestParser.getItemIdentifier()), "AND"));
        getDocstoreUtil().getSearchResultFields(item_search_Params);
        SearchResponse searchResponse = null;


        try {
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(item_search_Params);
            this.totalRecCount = searchResponse.getTotalRecordCount();
            oleItemSearches = getDocstoreUtil().getSearchResults(searchResponse);
            if (CollectionUtils.isNotEmpty(oleItemSearches))
                oleItemSearch = oleItemSearches.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (oleItemSearch != null) {

            OLESIP2ItemInformationResponse sip2ItemInfoParser = new OLESIP2ItemInformationResponse();
            responseString = sip2ItemInfoParser.getSIP2ItemInfoResponse(oleItemSearch, sip2ItemInformationRequestParser);

        }
        if (responseString == null) {
            OLENCIPErrorResponse olencipErrorResponse = new OLENCIPErrorResponse();
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.ITEM_BARCODE, sip2ItemInformationRequestParser.getItemIdentifier());
            responseString = olencipErrorResponse.getErrorXml(service);
        }


        return responseString;

    }

    @Override
    public String blockPatronService(OLESIP2BlockPatronRequestParser sip2BlockPatronRequestParser, String service, String operatorId) {
        String responseString = "";

        Map<String, String> patronMap = new HashMap<String, String>();
        patronMap.put(OLEConstants.OlePatron.BARCODE, sip2BlockPatronRequestParser.getPatronIdentifier());
        OlePatronDocument patronDocument = (OlePatronDocument) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, patronMap);
        patronDocument.setGeneralBlock(true);
        patronDocument.setGeneralBlockNotes(sip2BlockPatronRequestParser.getBlockedCardMessage());

        KRADServiceLocator.getBusinessObjectService().save(patronDocument);
        String lookupUserResponse = oleCirculationService.lookupUser(sip2BlockPatronRequestParser.getPatronIdentifier(),
                OLESIP2Constants.OPERATOR_ID, null, true);
        OLELookupUser oleLookupUser = new OLELookupUser();
        if (lookupUserResponse != null && !lookupUserResponse.equalsIgnoreCase("")) {
            oleLookupUser = (OLELookupUser) new OLELookupUserConverter().getLookupUser(lookupUserResponse);
        }
        OLESIP2PatronStatusResponse sip2PatronStatusResponse = new OLESIP2PatronStatusResponse();

        responseString = sip2PatronStatusResponse.getSIP2PatronStatusResponse(oleLookupUser, sip2BlockPatronRequestParser);


        if (responseString == null) {
            OLENCIPErrorResponse olencipErrorResponse = new OLENCIPErrorResponse();
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE, sip2BlockPatronRequestParser.getPatronIdentifier());

            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE, responseString);
            responseString = olencipErrorResponse.getErrorXml(service);
        }

        return responseString;

    }

    @Override
    public String patronEnableService(OLESIP2PatronEnableRequestParser sip2PatronEnableRequestParser, String service, String operatorId) {
        String responseString = "";


        Map<String, String> patronMap = new HashMap<String, String>();
        patronMap.put(OLEConstants.OlePatron.BARCODE, sip2PatronEnableRequestParser.getPatronIdentifier());
        OlePatronDocument patronDocument = (OlePatronDocument) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, patronMap);
        patronDocument.setGeneralBlock(false);


        KRADServiceLocator.getBusinessObjectService().save(patronDocument);
        OLESIP2PatronEnableResponse sip2PatronEnableResponse = new OLESIP2PatronEnableResponse();
        boolean isValidPatron = false;
        if (!(patronDocument.isGeneralBlock() || oleCirculationHelperService.isPatronExpired(patronDocument) || !patronDocument.isActiveIndicator() || oleCirculationHelperService.isPatronActivated(patronDocument))) {
            isValidPatron = true;
        }
        responseString = sip2PatronEnableResponse.getSIP2PatronEnableResponse(patronDocument, sip2PatronEnableRequestParser, isValidPatron);
        if (responseString == null) {
            OLENCIPErrorResponse olencipErrorResponse = new OLENCIPErrorResponse();
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE, sip2PatronEnableRequestParser.getPatronIdentifier());

            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE, responseString);
            responseString = olencipErrorResponse.getErrorXml(service);
        }


        return responseString;

    }

    @Override
    public String renewService(OLESIP2RenewRequestParser sip2RenewRequestParser, String service, String operatorId) {
        String responseString = "";
        OLECirculationService oleCirculationService = new OLECirculationServiceImpl();
        responseString = oleCirculationService.renewItem(sip2RenewRequestParser.getPatronIdentifier(),
                operatorId,
                sip2RenewRequestParser.getItemIdentifier(), true);

        if (responseString != null && !responseString.equalsIgnoreCase("")) {
            OLERenewItem oleRenewItem = (OLERenewItem) new OLERenewItemConverter().generateRenewItemObject(responseString);
            OLESIP2RenewResponse sip2RenewResponse = new OLESIP2RenewResponse();
            responseString = sip2RenewResponse.getSIP2RenewResponse(oleRenewItem, sip2RenewRequestParser);
        }

        if (responseString == null) {
            OLENCIPErrorResponse olencipErrorResponse = new OLENCIPErrorResponse();
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE, sip2RenewRequestParser.getPatronIdentifier());
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID, operatorId);
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE, responseString);
            responseString = olencipErrorResponse.getErrorXml(service);
        }


        return responseString;

    }

    @Override
    public String itemStatusUpdateService(String requestData, OLESIP2ItemStatusUpdateRequestParser oleSIP2ItemStatusUpdateRequestParser, String service, String operatorId) {
        String responseString = "";
        OLESIP2ItemStatusUpdateResponse olesip2ItemStatusUpdateResponse = new OLESIP2ItemStatusUpdateResponse();
        responseString = olesip2ItemStatusUpdateResponse.getOLESIP2ItemStatusUpdateResponse(requestData, oleSIP2ItemStatusUpdateRequestParser);

        if (responseString == null) {
            OLENCIPErrorResponse olencipErrorResponse = new OLENCIPErrorResponse();
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE, oleSIP2ItemStatusUpdateRequestParser.getPatronIdentifier());
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID, operatorId);
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE, responseString);
            responseString = olencipErrorResponse.getErrorXml(service);
        }
        return responseString;
    }

    @Override
    public String holdService(OLESIP2HoldRequestParser sip2HoldRequestParser, String service, String operatorId) {
        String responseString = "";
        OLESIP2HoldResponse sip2HoldResponse = new OLESIP2HoldResponse();
        String pickupLocation = null;
        OLECirculationService oleCirculationService = new OLECirculationServiceImpl();
        if (sip2HoldRequestParser.getHoldType().equalsIgnoreCase("add")) {
            responseString = oleCirculationService.placeRequest(sip2HoldRequestParser.getPatronIdentifier(),
                    operatorId,
                    sip2HoldRequestParser.getItemIdentifier(),
                    OLESIP2Constants.REQUEST_TYPE,
                    (sip2HoldRequestParser.getPickupLocation() != null
                            && !sip2HoldRequestParser.getPickupLocation().equalsIgnoreCase("")) ?
                            sip2HoldRequestParser.getPickupLocation() : "",null,null,
                    null,null);
            if (responseString != null && !responseString.equalsIgnoreCase("")) {
                OLEPlaceRequest olePlaceRequest = (OLEPlaceRequest) new OLEPlaceRequestConverter().generatePlaceRequestObject(responseString);

                responseString = sip2HoldResponse.getSIP2PlaceHoldRequestService(olePlaceRequest, sip2HoldRequestParser);

            }
            if (responseString == null) {
                OLENCIPErrorResponse olencipErrorResponse = new OLENCIPErrorResponse();
                olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE, sip2HoldRequestParser.getPatronIdentifier());
                olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID, operatorId);
                olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE, OLENCIPConstants.INVALID_INPUT);
                responseString = olencipErrorResponse.getErrorXml(service);
            }
        } else if (sip2HoldRequestParser.getHoldType().equals("delete")) {//12.)b.Cancel Hold

            responseString = oleCirculationService.cancelRequest(operatorId,
                    sip2HoldRequestParser.getPatronIdentifier(),
                    sip2HoldRequestParser.getItemIdentifier());

            if (responseString != null && !responseString.equalsIgnoreCase("")) {
                OLECancelRequest oleCancelRequest = (OLECancelRequest) new OLECancelRequestConverter().generateCancelRequestObject(responseString);
                responseString = sip2HoldResponse.getSIP2CancelHoldRequestService(oleCancelRequest, sip2HoldRequestParser);

            }
            if (responseString == null) {
                OLENCIPErrorResponse olencipErrorResponse = new OLENCIPErrorResponse();
                olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID, operatorId);
                olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_ID, sip2HoldRequestParser.getPatronIdentifier());
                olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE, OLENCIPConstants.INVALID_INPUT);
                responseString = olencipErrorResponse.getErrorXml(service);
            }

        } else if (sip2HoldRequestParser.getHoldType().equals("updateHold")) {//12.)c.Update Hold
            responseString = sip2HoldResponse.getSIP2UpdateHoldRequestService(sip2HoldRequestParser);

        }
        return responseString;

    }

    @Override
    public String getCirculationErrorMessage(String service, String message, String code, String requiredParameters, String outputFormat) {
        OLECirculationErrorMessage oleCirculationErrorMessage = new OLECirculationErrorMessage();
        OLECirculationErrorMessageConverter oleCirculationErrorMessageConverter = new OLECirculationErrorMessageConverter();
        oleCirculationErrorMessage.setMessage(message);
        oleCirculationErrorMessage.setCode(code);
        oleCirculationErrorMessage.setService(service);
        oleCirculationErrorMessage.setRequiredParameters(requiredParameters);
        String errorMessage = "";
        errorMessage = oleCirculationErrorMessageConverter.generateCirculationErrorXml(oleCirculationErrorMessage);
        if (outputFormat.equalsIgnoreCase(OLENCIPConstants.JSON_FORMAT)) {
            errorMessage = oleCirculationErrorMessageConverter.generateLookupUserJson(errorMessage);
        }
        return errorMessage;
    }


    @Override
    public String renewAllService(OLESIP2RenewAllRequestParser sip2RenewAllRequestParser, String service, String operatorId) {
        String responseString = "";
        String itemBarcodes = "";
        int count = 1;
        OLECirculationService oleCirculationService = new OLECirculationServiceImpl();

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("barcode", sip2RenewAllRequestParser.getPatronIdentifier());
        List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class, parameterMap);
        if (olePatronDocumentList != null && olePatronDocumentList.size() > 0) {
            OlePatronDocument olePatronDocument = olePatronDocumentList.get(0);
            for (OleLoanDocument oleLoanDocument : olePatronDocument.getOleLoanDocuments()) {
                if (count == 1) {
                    itemBarcodes = oleLoanDocument.getItemId();
                    count = count + 1;
                } else {
                    itemBarcodes = itemBarcodes + "," + oleLoanDocument.getItemId();
                }

            }
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(itemBarcodes)) {
            responseString = oleCirculationService.renewItemList(sip2RenewAllRequestParser.getPatronIdentifier(),
                    operatorId,
                    itemBarcodes, true);
        } else {
            responseString = "Patron don't have loaned item to renew";
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(responseString)) {
            OLESIP2RenewAllResponse sip2RenewAllResponse = new OLESIP2RenewAllResponse();
            if (responseString.equalsIgnoreCase("Patron don't have loaned item to renew")) {
                responseString = sip2RenewAllResponse.getSIP2RenewAllResponse(responseString, sip2RenewAllRequestParser);
            } else {
                OLERenewItemList oleRenewItemList = (OLERenewItemList) new OLERenewItemConverter().generateRenewItemListObjectForSip2(responseString);
                responseString = sip2RenewAllResponse.getSIP2RenewAllResponse(oleRenewItemList, sip2RenewAllRequestParser);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(responseString)) {
            OLENCIPErrorResponse olencipErrorResponse = new OLENCIPErrorResponse();
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE, sip2RenewAllRequestParser.getPatronIdentifier());
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID, operatorId);
            olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE, OLENCIPConstants.INVALID_INPUT);
            responseString = olencipErrorResponse.getErrorXml(service);
        }
        return responseString;

    }

}
