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
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.util.Map;

/**
 * Created by chenchulakshmig on 8/27/15.
 */
public abstract class CheckinItemService {

    private static final Logger LOG = Logger.getLogger(CheckinItemService.class);

    protected String responseFormatType;
    protected String response;

    private ResponseHandler responseHandler;
    private CircDeskLocationResolver circDeskLocationResolver;
    private DocstoreClientLocator docstoreClientLocator;

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

    public String checkinItem(Map checkinParameters) {

        CheckInAPIController checkInAPIController = new CheckInAPIController();

        OLECheckInItem oleCheckInItem = new OLECheckInItem();

        String operatorId = getOperatorId((String) checkinParameters.get("operatorId"));
        String itemBarcode = (String) checkinParameters.get("itemBarcode");
        responseFormatType = (String) checkinParameters.get("responseFormatType");
        String deleteIndicator = (String) checkinParameters.get("deleteIndicator");
        if (responseFormatType == null) {
            responseFormatType = "xml";
        }
        responseFormatType = responseFormatType.toUpperCase();

        OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getCircDeskForOpertorId(operatorId);
        if (null == oleCirculationDesk) {
            oleCheckInItem.setCode("026");
            oleCheckInItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.CIRCULATION_DESK_NOT_MAPPED_OPERATOR));
            return prepareResponse(oleCheckInItem);
        }

        if (StringUtils.isBlank(itemBarcode)) {
            oleCheckInItem.setCode("900");
            oleCheckInItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_BARCODE_REQUIRED));
            return prepareResponse(oleCheckInItem);
        }

        DroolsExchange droolsExchange = new DroolsExchange();
        droolsExchange.addToContext("itemBarcode", itemBarcode);
        droolsExchange.addToContext("selectedCirculationDesk", oleCirculationDesk.getCirculationDeskId());
        droolsExchange.addToContext("operatorId", operatorId);
        OLEForm oleAPIForm = new OLEForm();
        oleAPIForm.setDroolsExchange(droolsExchange);

        try {
            DroolsResponse droolsResponse = checkInAPIController.checkin(oleAPIForm);
            if (droolsResponse != null && StringUtils.isNotBlank(droolsResponse.getErrorMessage().getErrorMessage())) {
                String checkinErrorMessage = droolsResponse.getErrorMessage().getErrorMessage();
                String responseMessage = null;
                if (droolsResponse.retriveErrorCode() != null) {
                    if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_CLAIMS_RETURNED)) {
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
                    oleCheckInItem.setCode("500");
                    oleCheckInItem.setMessage(responseMessage);
                    return prepareResponse(oleCheckInItem);
                } else if (checkinErrorMessage.equalsIgnoreCase("Invalid item barcode!")) {
                    oleCheckInItem.setCode("014");
                    oleCheckInItem.setMessage(checkinErrorMessage);
                    return prepareResponse(oleCheckInItem);
                }
                oleCheckInItem.setCode("500");
                oleCheckInItem.setMessage(checkinErrorMessage);
                return prepareResponse(oleCheckInItem);
            } else {
                CheckedInItem checkedInItem = (CheckedInItem) droolsExchange.getFromContext("checkedInItem");
                if (checkedInItem != null) {
                    oleCheckInItem.setAuthor(checkedInItem.getAuthor());
                    oleCheckInItem.setTitle(checkedInItem.getTitle());
                    oleCheckInItem.setCallNumber(checkedInItem.getCallNumber());
                    oleCheckInItem.setBarcode(checkedInItem.getItemBarcode());
                    oleCheckInItem.setUserId(checkedInItem.getPatronId());
                    oleCheckInItem.setUserType(checkedInItem.getBorrowerType());
                    oleCheckInItem.setItemType(checkedInItem.getItemType());
                    oleCheckInItem.setCode("024");
                    oleCheckInItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.SUCCESSFULLEY_CHECKED_IN));

                    if (StringUtils.isNotBlank(deleteIndicator) && deleteIndicator.equalsIgnoreCase("y")) {
                        try {
                            org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(checkedInItem.getItemUuid());
                            String bibId = item.getHolding().getBib().getId();
                            getDocstoreClientLocator().getDocstoreClient().deleteBib(bibId);
                        } catch (Exception e) {
                            LOG.error("Exception while deleting bib" + e);
                            oleCheckInItem.setMessage("Item " + ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.SUCCESSFULLEY_CHECKED_IN) + " but Deletion is failed");
                            return prepareResponse(oleCheckInItem);
                        }
                    }
                    return prepareResponse(oleCheckInItem);
                } else {
                    oleCheckInItem.setCode("025");
                    oleCheckInItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.CHECK_IN_FAILED));
                    return prepareResponse(oleCheckInItem);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception " + e);
            oleCheckInItem.setCode("025");
            oleCheckInItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.CHECK_IN_FAILED));
            return prepareResponse(oleCheckInItem);
        }
    }

    public abstract String prepareResponse(OLECheckInItem oleCheckInItem);

    public abstract String getOperatorId(String operatorId);
}
