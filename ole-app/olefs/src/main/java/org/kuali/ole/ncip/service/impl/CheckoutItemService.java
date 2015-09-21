package org.kuali.ole.ncip.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.asr.handler.CheckoutItemResponseHandler;
import org.kuali.asr.handler.ResponseHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.controller.PatronLookupCircAPIController;
import org.kuali.ole.deliver.controller.checkout.CheckoutAPIController;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.OLEForm;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.deliver.util.OlePatronRecordUtil;
import org.kuali.ole.bo.OLECheckOutItem;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.util.Map;

/**
 * Created by chenchulakshmig on 8/24/15.
 */
public abstract class CheckoutItemService {

    private static final Logger LOG = Logger.getLogger(CheckoutItemService.class);

    protected String responseFormatType;
    protected String response;

    private ResponseHandler responseHandler;
    private OlePatronRecordUtil olePatronRecordUtil;
    private CircDeskLocationResolver circDeskLocationResolver;

    public OlePatronRecordUtil getOlePatronRecordUtil() {
        if (null == olePatronRecordUtil) {
            olePatronRecordUtil = (OlePatronRecordUtil) SpringContext.getBean("olePatronRecordUtil");
        }
        return olePatronRecordUtil;
    }

    public void setOlePatronRecordUtil(OlePatronRecordUtil olePatronRecordUtil) {
        this.olePatronRecordUtil = olePatronRecordUtil;
    }

    public CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public void setCircDeskLocationResolver(CircDeskLocationResolver circDeskLocationResolver) {
        this.circDeskLocationResolver = circDeskLocationResolver;
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

    public String checkoutItem(Map checkoutParameters) {

        CheckoutAPIController checkoutAPIController = new CheckoutAPIController();

        OLECheckOutItem oleCheckOutItem = new OLECheckOutItem();

        String patronBarcode = (String) checkoutParameters.get("patronBarcode");
        String operatorId = getOperatorId((String) checkoutParameters.get("operatorId"));
        String itemBarcode = (String) checkoutParameters.get("itemBarcode");
        responseFormatType = (String) checkoutParameters.get("responseFormatType");
        if (responseFormatType == null) {
            responseFormatType = "xml";
        }
        responseFormatType = responseFormatType.toUpperCase();

        DroolsExchange patronDroolsExchange = new DroolsExchange();
        patronDroolsExchange.addToContext("patronBarcode", patronBarcode);
        PatronLookupCircAPIController patronLookupAPIController = new PatronLookupCircAPIController();
        DroolsResponse patronDroolsResponse = patronLookupAPIController.searchPatron(patronDroolsExchange);
        String patronErrorMessage = patronDroolsResponse.getErrorMessage().getErrorMessage();
        if (StringUtils.isNotBlank(patronErrorMessage)) {
            if (patronErrorMessage.contains(OLEConstants.PTRN_BARCD_NOT_EXT)) {
                oleCheckOutItem.setCode("002");
                oleCheckOutItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO));
                return prepareResponse(oleCheckOutItem);
            } else {
                oleCheckOutItem.setCode("002");
                oleCheckOutItem.setMessage(patronErrorMessage);
                return prepareResponse(oleCheckOutItem);
            }
        }
        OlePatronDocument olePatronDocument = (OlePatronDocument) patronDroolsExchange.getFromContext("olePatronDocument");

        OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getCircDeskForOpertorId(operatorId);
        if (null == oleCirculationDesk) {
            oleCheckOutItem.setCode("026");
            oleCheckOutItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.CIRCULATION_DESK_NOT_MAPPED_OPERATOR));
            return prepareResponse(oleCheckOutItem);
        }
        if (StringUtils.isBlank(itemBarcode)) {
            oleCheckOutItem.setCode("900");
            oleCheckOutItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_BARCODE_REQUIRED));
            return prepareResponse(oleCheckOutItem);
        }

        DroolsExchange droolsExchange = new DroolsExchange();
        droolsExchange.addToContext("itemBarcode", itemBarcode);
        droolsExchange.addToContext("currentBorrower", olePatronDocument);
        droolsExchange.addToContext("selectedCirculationDesk", oleCirculationDesk);
        droolsExchange.addToContext("operatorId", operatorId);

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
                            oleCheckOutItem.setCode("028");
                            oleCheckOutItem.setMessage(checkoutErrorMessage);
                            return prepareResponse(oleCheckOutItem);
                        } else if (checkoutErrorMessage.contains("Invalid item barcode")) {
                            oleCheckOutItem.setCode("014");
                            oleCheckOutItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_BARCODE_DOESNOT_EXISTS));
                            return prepareResponse(oleCheckOutItem);
                        }
                    } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.CUSTOM_DUE_DATE_REQUIRED_FLAG)) {
                        responseMessage = "No Circulation Policy found";
                    } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.LOANED_BY_DIFFERENT_PATRON) ||
                            droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.CHECKED_OUT_BY_SAME_PATRON)) {
                        oleCheckOutItem.setCode("100");
                        oleCheckOutItem.setMessage(checkoutErrorMessage);
                        return prepareResponse(oleCheckOutItem);
                    } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_CLAIMS_RETURNED)) {
                        responseMessage = "Item is Claims Returned";
                    } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_MISSING_PIECE)) {
                        responseMessage = "Item has missing pieces";
                    } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_DAMAGED)) {
                        responseMessage = "Item is Damaged.";
                    }
                }
                if (StringUtils.isNotBlank(responseMessage)) {
                    oleCheckOutItem.setCode("500");
                    oleCheckOutItem.setMessage(responseMessage);
                    return prepareResponse(oleCheckOutItem);
                }
                oleCheckOutItem.setCode("500");
                oleCheckOutItem.setMessage(checkoutErrorMessage);
                return prepareResponse(oleCheckOutItem);
            } else {
                OleLoanDocument oleLoanDocument = (OleLoanDocument) droolsExchange.getFromContext("oleLoanDocument");
                if (oleLoanDocument != null) {
                    oleCheckOutItem.setDueDate(oleLoanDocument.getLoanDueDate() != null ? oleLoanDocument.getLoanDueDate().toString() : "");
                    oleCheckOutItem.setRenewalCount(oleLoanDocument.getNumberOfRenewals());
                    oleCheckOutItem.setUserType(olePatronDocument.getBorrowerTypeName());
                    oleCheckOutItem.setBarcode(oleLoanDocument.getItemId());
                    oleCheckOutItem.setPatronId(olePatronDocument.getOlePatronId());
                    oleCheckOutItem.setPatronBarcode(olePatronDocument.getBarcode());
                    if (oleLoanDocument.getOleItem() != null && oleLoanDocument.getOleItem().getItemType() != null) {
                        oleCheckOutItem.setItemType(oleLoanDocument.getOleItem().getItemType().getCodeValue());
                    }
                    oleCheckOutItem.setCode("030");
                    oleCheckOutItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.SUCCESSFULLEY_LOANED));
                    oleCheckOutItem.setItemProperties("Author : " + oleLoanDocument.getAuthor() + " , Status : " + oleLoanDocument.getItemStatus());
                    oleCheckOutItem.setItemType(oleLoanDocument.getItemType());
                    return prepareResponse(oleCheckOutItem);
                } else {
                    oleCheckOutItem.setCode("500");
                    oleCheckOutItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.CHECK_OUT_FAIL));
                    return prepareResponse(oleCheckOutItem);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception " + e);
            oleCheckOutItem.setCode("500");
            oleCheckOutItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.CHECK_OUT_FAIL));
            return prepareResponse(oleCheckOutItem);
        }
    }

    public abstract String prepareResponse(OLECheckOutItem oleCheckOutItem);

    public abstract String getOperatorId(String operatorId);
}
