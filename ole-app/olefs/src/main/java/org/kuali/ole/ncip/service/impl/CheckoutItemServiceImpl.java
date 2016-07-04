package org.kuali.ole.ncip.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.asr.handler.CheckoutItemResponseHandler;
import org.kuali.asr.handler.ResponseHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.bo.OLECheckOutItem;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.controller.checkout.CheckoutAPIController;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.OLEForm;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.deliver.util.OlePatronRecordUtil;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.ncip.service.CheckoutItemService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.util.Map;

/**
 * Created by chenchulakshmig on 8/24/15.
 */
public abstract class CheckoutItemServiceImpl implements CheckoutItemService {

    private static final Logger LOG = Logger.getLogger(CheckoutItemServiceImpl.class);

    protected String responseFormatType;
    protected String response;

    private ResponseHandler responseHandler;
    private CircDeskLocationResolver circDeskLocationResolver;
    private OlePatronRecordUtil olePatronRecordUtil;

    private OLECheckOutItem oleCheckOutItem;
    private OlePatronDocument olePatronDocument;
    private OleCirculationDesk oleCirculationDesk;

    public CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public void setCircDeskLocationResolver(CircDeskLocationResolver circDeskLocationResolver) {
        this.circDeskLocationResolver = circDeskLocationResolver;
    }

    public OlePatronRecordUtil getOlePatronRecordUtil() {
        if (null == olePatronRecordUtil) {
            olePatronRecordUtil = (OlePatronRecordUtil) SpringContext.getBean("olePatronRecordUtil");
        }
        return olePatronRecordUtil;
    }

    public void setOlePatronRecordUtil(OlePatronRecordUtil olePatronRecordUtil) {
        this.olePatronRecordUtil = olePatronRecordUtil;
    }

    public ResponseHandler getResponseHandler() {
        if (null == responseHandler) {
            responseHandler = new CheckoutItemResponseHandler();
        }
        return responseHandler;
    }

    public void setResponseHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    public OLECheckOutItem getOleCheckOutItem() {
        return oleCheckOutItem;
    }

    public void setOleCheckOutItem(OLECheckOutItem oleCheckOutItem) {
        this.oleCheckOutItem = oleCheckOutItem;
    }

    public OlePatronDocument getOlePatronDocument() {
        return olePatronDocument;
    }

    public void setOlePatronDocument(OlePatronDocument olePatronDocument) {
        this.olePatronDocument = olePatronDocument;
    }

    public OleCirculationDesk getOleCirculationDesk() {
        return oleCirculationDesk;
    }

    public void setOleCirculationDesk(OleCirculationDesk oleCirculationDesk) {
        this.oleCirculationDesk = oleCirculationDesk;
    }

    public String checkoutItem(Map checkoutParameters) {
        setOleCheckOutItem(new OLECheckOutItem());
        setOlePatronDocument(null);
        setOleCirculationDesk(null);

        String patronBarcode = (String) checkoutParameters.get("patronBarcode");
        String operatorId = getOperatorId((String) checkoutParameters.get("operatorId"));
        String itemBarcode = (String) checkoutParameters.get("itemBarcode");
        setResponseFormatType(checkoutParameters);

        if (!validatePatronBarcode(patronBarcode)) {
            return prepareResponse();
        }

        String response = preProcess(checkoutParameters);
        if (response != null) {
            return response;
        }

        if (!validateOperator(operatorId)){
            return prepareResponse();
        }

        if(!isItemBarcodeNotBlank(itemBarcode)){
            return prepareResponse();
        }

        if (!processRules()){
            return prepareResponse();
        }

        DroolsExchange droolsExchange = new DroolsExchange();
        droolsExchange.addToContext("itemBarcode", itemBarcode);
        droolsExchange.addToContext("currentBorrower", getOlePatronDocument());
        droolsExchange.addToContext("selectedCirculationDesk", getOleCirculationDesk());
        droolsExchange.addToContext("operatorId", operatorId);

        return process(droolsExchange);
    }

    protected abstract String preProcess(Map checkoutParameters);

    private String process(DroolsExchange droolsExchange) {
        CheckoutAPIController checkoutAPIController = new CheckoutAPIController();
        OLEForm oleAPIForm = new OLEForm();
        oleAPIForm.setDroolsExchange(droolsExchange);

        try {
            DroolsResponse droolsResponse = checkoutAPIController.lookupItemAndSaveLoan(oleAPIForm);
            if (droolsResponse != null && StringUtils.isNotBlank(droolsResponse.getErrorMessage().getErrorMessage())) {
                String checkoutErrorMessage = droolsResponse.getErrorMessage().getErrorMessage();
                String responseMessage = null;
                if (StringUtils.isNotBlank(droolsResponse.retriveErrorCode())) {
                    if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.GENERAL_INFO)) {
                        if (checkoutErrorMessage.equalsIgnoreCase(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVAL_LOC))) {
                            getOleCheckOutItem().setCode("028");
                            getOleCheckOutItem().setMessage(checkoutErrorMessage);
                            return prepareResponse();
                        } else if (checkoutErrorMessage.contains("Invalid item barcode")) {
                            getOleCheckOutItem().setCode("014");
                            getOleCheckOutItem().setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_BARCODE_DOESNOT_EXISTS));
                            return prepareResponse();
                        }
                    } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.CUSTOM_DUE_DATE_REQUIRED_FLAG)) {
                        responseMessage = "No Circulation Policy found";
                    } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.LOANED_BY_DIFFERENT_PATRON) ||
                            droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.CHECKED_OUT_BY_SAME_PATRON)) {
                        getOleCheckOutItem().setCode("100");
                        getOleCheckOutItem().setMessage(checkoutErrorMessage);
                        return prepareResponse();
                    } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_CLAIMS_RETURNED)) {
                        responseMessage = "Item is Claims Returned";
                    } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_MISSING_PIECE)) {
                        responseMessage = "Item has missing pieces";
                    } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_DAMAGED)) {
                        responseMessage = "Item is Damaged.";
                    }
                }
                if (StringUtils.isNotBlank(responseMessage)) {
                    getOleCheckOutItem().setCode("500");
                    getOleCheckOutItem().setMessage(responseMessage);
                    return prepareResponse();
                }
                getOleCheckOutItem().setCode("500");
                getOleCheckOutItem().setMessage(checkoutErrorMessage);
                return prepareResponse();
            } else {
                OleLoanDocument oleLoanDocument = (OleLoanDocument) droolsExchange.getFromContext("oleLoanDocument");
                if (oleLoanDocument != null) {
                    getOleCheckOutItem().setDueDate(oleLoanDocument.getLoanDueDate() != null ? oleLoanDocument.getLoanDueDate().toString() : "");
                    getOleCheckOutItem().setRenewalCount(oleLoanDocument.getNumberOfRenewals());
                    getOleCheckOutItem().setUserType(getOlePatronDocument().getBorrowerTypeName());
                    getOleCheckOutItem().setBarcode(oleLoanDocument.getItemId());
                    getOleCheckOutItem().setTitleIdentifier(oleLoanDocument.getTitle());
                    getOleCheckOutItem().setPatronId(getOlePatronDocument().getOlePatronId());
                    getOleCheckOutItem().setPatronBarcode(getOlePatronDocument().getBarcode());
                    if (oleLoanDocument.getOleItem() != null && oleLoanDocument.getOleItem().getItemType() != null) {
                        getOleCheckOutItem().setItemType(oleLoanDocument.getOleItem().getItemType().getCodeValue());
                    }
                    getOleCheckOutItem().setCode("030");
                    getOleCheckOutItem().setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.SUCCESSFULLEY_LOANED));
                    getOleCheckOutItem().setItemProperties("Author : " + oleLoanDocument.getAuthor() + " , Status : " + oleLoanDocument.getItemStatus());
                    getOleCheckOutItem().setItemType(oleLoanDocument.getItemType());
                    return prepareResponse();
                } else {
                    getOleCheckOutItem().setCode("500");
                    getOleCheckOutItem().setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.CHECK_OUT_FAIL));
                    return prepareResponse();
                }
            }
        } catch (Exception e) {
            LOG.error("Exception " + e);
            getOleCheckOutItem().setCode("500");
            getOleCheckOutItem().setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.CHECK_OUT_FAIL));
            return prepareResponse();
        }
    }

    private void setResponseFormatType(Map checkoutParameters) {
        responseFormatType = (String) checkoutParameters.get("responseFormatType");
        if (responseFormatType == null) {
            responseFormatType = "xml";
        }
        responseFormatType = responseFormatType.toUpperCase();
    }

    private boolean isItemBarcodeNotBlank(String itemBarcode){
        if (StringUtils.isBlank(itemBarcode)) {
            getOleCheckOutItem().setCode("900");
            getOleCheckOutItem().setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_BARCODE_REQUIRED));
            return false;
        }
        return true;
    }

    private boolean validateOperator(String operatorId){
        OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getCircDeskForOpertorId(operatorId);
        setOleCirculationDesk(oleCirculationDesk);
        if (null == oleCirculationDesk) {
            getOleCheckOutItem().setCode("026");
            getOleCheckOutItem().setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.CIRCULATION_DESK_NOT_MAPPED_OPERATOR));
            return false;
        }
        return true;
    }

    private boolean processRules(){
        String patronErrorMessage = fireRules();
        if (StringUtils.isNotBlank(patronErrorMessage)) {
            getOleCheckOutItem().setCode("002");
            getOleCheckOutItem().setMessage(patronErrorMessage);
            return false;
        }
        return true;
    }

    private boolean validatePatronBarcode(String patronBarcode) {
        try {
            OlePatronDocument patronDocument = getOlePatronRecordUtil().getPatronRecordByBarcode(patronBarcode);
            setOlePatronDocument(patronDocument);
        } catch (Exception e) {
            LOG.error("Exception " + e);
        }
        if (null == getOlePatronDocument()) {
            getOleCheckOutItem().setCode("002");
            getOleCheckOutItem().setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO));
            return false;
        }
        return true;
    }

    protected abstract String fireRules();

    public abstract String prepareResponse();

    public abstract String getOperatorId(String operatorId);
}
