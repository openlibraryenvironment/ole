package org.kuali.ole.ncip.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.asr.handler.CheckinItemResponseHandler;
import org.kuali.asr.handler.ResponseHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.drools.CheckedInItem;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.OLEForm;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.bo.OLECheckInItem;
import org.kuali.ole.deliver.controller.checkin.CheckInAPIController;
import org.kuali.ole.ncip.service.CheckinItemService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.util.Map;

/**
 * Created by chenchulakshmig on 8/27/15.
 */
public abstract class CheckinItemServiceImpl implements CheckinItemService{

    private static final Logger LOG = Logger.getLogger(CheckinItemServiceImpl.class);

    protected String responseFormatType;
    protected String response;

    private ResponseHandler responseHandler;
    private CircDeskLocationResolver circDeskLocationResolver;
    private DocstoreClientLocator docstoreClientLocator;

    private OLECheckInItem oleCheckInItem;
    private OleCirculationDesk oleCirculationDesk;

    public ResponseHandler getResponseHandler() {
        if (null == responseHandler) {
            responseHandler = new CheckinItemResponseHandler();
        }
        return responseHandler;
    }

    public void setResponseHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
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

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = (DocstoreClientLocator) SpringContext.getService("docstoreClientLocator");
        }
        return docstoreClientLocator;
    }

    public void setDocstoreClientLocator(DocstoreClientLocator docstoreClientLocator) {
        this.docstoreClientLocator = docstoreClientLocator;
    }

    public OLECheckInItem getOleCheckInItem() {
        return oleCheckInItem;
    }

    public void setOleCheckInItem(OLECheckInItem oleCheckInItem) {
        this.oleCheckInItem = oleCheckInItem;
    }

    public OleCirculationDesk getOleCirculationDesk() {
        return oleCirculationDesk;
    }

    public void setOleCirculationDesk(OleCirculationDesk oleCirculationDesk) {
        this.oleCirculationDesk = oleCirculationDesk;
    }

    public String checkinItem(Map checkinParameters) {

        CheckInAPIController checkInAPIController = new CheckInAPIController();

        setOleCheckInItem(new OLECheckInItem());
        setOleCirculationDesk(null);

        String operatorId = getOperatorId((String) checkinParameters.get("operatorId"));
        String itemBarcode = (String) checkinParameters.get("itemBarcode");
        String deleteIndicator = (String) checkinParameters.get("deleteIndicator");
        setResponseFormatType(checkinParameters);

        boolean isValid = validate(operatorId, itemBarcode);
        if (!isValid){
            return prepareResponse();
        }

        DroolsExchange droolsExchange = new DroolsExchange();
        droolsExchange.addToContext("itemBarcode", itemBarcode);
        droolsExchange.addToContext("selectedCirculationDesk", getOleCirculationDesk().getCirculationDeskId());
        droolsExchange.addToContext("operatorId", operatorId);
        OLEForm oleAPIForm = new OLEForm();
        oleAPIForm.setDroolsExchange(droolsExchange);

        try {
            DroolsResponse droolsResponse = checkInAPIController.checkin(oleAPIForm);
            if (droolsResponse != null && StringUtils.isNotBlank(droolsResponse.getErrorMessage().getErrorMessage())) {
                String checkinErrorMessage = droolsResponse.getErrorMessage().getErrorMessage();
                String responseMessage = null;
                if (droolsResponse.retriveErrorCode() != null) {
                    if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_LOST)) {
                        responseMessage = getLostItemResponseMessage();
                    } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_CLAIMS_RETURNED)) {
                        responseMessage = "Item is Claims Returned";
                    } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_MISSING_PIECE)) {
                        responseMessage = "Item has missing pieces";
                    } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_DAMAGED)) {
                        responseMessage = "Item is Damaged.";
                    } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.CHECKIN_REQUEST_EXITS_FOR_THIS_ITEM)) {
                        responseMessage = "Requests exists for this item";
                    }
                }
                if (StringUtils.isNotBlank(responseMessage)) {
                    getOleCheckInItem().setCode("500");
                    getOleCheckInItem().setMessage(responseMessage);
                    return prepareResponse();
                } else if (checkinErrorMessage.equalsIgnoreCase("Invalid item barcode!")) {
                    getOleCheckInItem().setCode("014");
                    getOleCheckInItem().setMessage(checkinErrorMessage);
                    return prepareResponse();
                }
                getOleCheckInItem().setCode("500");
                getOleCheckInItem().setMessage(checkinErrorMessage);
                return prepareResponse();
            } else {
                CheckedInItem checkedInItem = (CheckedInItem) droolsExchange.getFromContext("checkedInItem");
                if (checkedInItem != null) {
                    getOleCheckInItem().setAuthor(checkedInItem.getAuthor());
                    getOleCheckInItem().setTitle(checkedInItem.getTitle());
                    getOleCheckInItem().setCallNumber(checkedInItem.getCallNumber());
                    getOleCheckInItem().setBarcode(checkedInItem.getItemBarcode());
                    getOleCheckInItem().setUserId(checkedInItem.getPatronId());
                    getOleCheckInItem().setUserType(checkedInItem.getBorrowerType());
                    getOleCheckInItem().setItemType(checkedInItem.getItemType());
                    getOleCheckInItem().setCode("024");
                    getOleCheckInItem().setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.SUCCESSFULLEY_CHECKED_IN));

                    if (StringUtils.isNotBlank(deleteIndicator) && deleteIndicator.equalsIgnoreCase("y")) {
                        try {
                            org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(checkedInItem.getItemUuid());
                            String bibId = item.getHolding().getBib().getId();
                            getDocstoreClientLocator().getDocstoreClient().deleteBib(bibId);
                        } catch (Exception e) {
                            LOG.error("Exception while deleting bib" + e);
                            getOleCheckInItem().setMessage("Item " + ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.SUCCESSFULLEY_CHECKED_IN) + " but Deletion is failed");
                            return prepareResponse();
                        }
                    }
                    return prepareResponse();
                } else {
                    getOleCheckInItem().setCode("025");
                    getOleCheckInItem().setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.CHECK_IN_FAILED));
                    return prepareResponse();
                }
            }
        } catch (Exception e) {
            LOG.error("Exception " + e);
            getOleCheckInItem().setCode("025");
            getOleCheckInItem().setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.CHECK_IN_FAILED));
            return prepareResponse();
        }
    }

    private void setResponseFormatType(Map checkinParameters) {
        responseFormatType = (String) checkinParameters.get("responseFormatType");
        if (responseFormatType == null) {
            responseFormatType = "xml";
        }
        responseFormatType = responseFormatType.toUpperCase();
    }

    private boolean validate(String operatorId, String itemBarcode){
        OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getCircDeskForOpertorId(operatorId);
        setOleCirculationDesk(oleCirculationDesk);
        if (null == oleCirculationDesk) {
            getOleCheckInItem().setCode("026");
            getOleCheckInItem().setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.CIRCULATION_DESK_NOT_MAPPED_OPERATOR));
            return false;
        }

        if (StringUtils.isBlank(itemBarcode)) {
            getOleCheckInItem().setCode("900");
            getOleCheckInItem().setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_BARCODE_REQUIRED));
            return false;
        }
        return true;
    }

    public abstract String prepareResponse();

    public abstract String getOperatorId(String operatorId);

    public abstract String getLostItemResponseMessage();
}
