package org.kuali.ole.ncip.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.drools.CheckedInItem;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.drools.LoanPeriodUtil;
import org.kuali.ole.deliver.form.OLEForm;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.deliver.controller.checkin.CheckInAPIController;
import org.kuali.ole.ncip.service.NCIPCheckInItemResponseBuilder;
import org.kuali.ole.ncip.service.OLECheckInItemService;
import org.kuali.ole.ncip.util.OLENCIPUtil;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.core.api.config.property.ConfigContext;

/**
 * Created by chenchulakshmig on 8/21/15.
 */
public class OLENCIPCheckInItemServiceImpl extends OLENCIPUtil implements OLECheckInItemService {

    private static final Logger LOG = Logger.getLogger(OLENCIPCheckInItemServiceImpl.class);

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = (DocstoreClientLocator) SpringContext.getService("docstoreClientLocator");
        }
        return docstoreClientLocator;
    }

    public void setDocstoreClientLocator(DocstoreClientLocator docstoreClientLocator) {
        this.docstoreClientLocator = docstoreClientLocator;
    }

    @Override
    public CheckInItemResponseData performService(CheckInItemInitiationData checkInItemInitiationData, ServiceContext serviceContext, RemoteServiceManager remoteServiceManager) throws ServiceException {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();

        NCIPCheckInItemResponseBuilder ncipCheckInItemResponseBuilder = new NCIPCheckInItemResponseBuilder();
        CheckInItemResponseData checkInItemResponseData = new CheckInItemResponseData();

        AgencyId agencyId = validateAgency(checkInItemInitiationData.getInitiationHeader(), checkInItemResponseData);
        if (null == agencyId) return checkInItemResponseData;

        String operatorId = agencyPropertyMap.get(OLENCIPConstants.OPERATOR_ID);
        OleCirculationDesk oleCirculationDesk = validOperator(checkInItemResponseData, operatorId);
        if (null == oleCirculationDesk) return checkInItemResponseData;

        String itemBarcode = checkInItemInitiationData.getItemId().getItemIdentifierValue();

        if (StringUtils.isBlank(itemBarcode)) {
            processProblems(checkInItemResponseData, "", ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_BARCODE_REQUIRED), OLENCIPConstants.ITEM);
            return checkInItemResponseData;
        }

        CheckInAPIController checkInAPIController = new CheckInAPIController();
        DroolsExchange droolsExchange = new DroolsExchange();
        droolsExchange.addToContext("itemBarcode", itemBarcode);
        droolsExchange.addToContext("selectedCirculationDesk", oleCirculationDesk.getCirculationDeskId());
        OLEForm oleAPIForm = new OLEForm();
        oleAPIForm.setDroolsExchange(droolsExchange);

        try {
            DroolsResponse droolsResponse = checkInAPIController.checkin(oleAPIForm);
            if (droolsResponse != null && StringUtils.isNotBlank(droolsResponse.getErrorMessage().getErrorMessage())) {
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
                    processProblems(checkInItemResponseData, itemBarcode, responseMessage, OLENCIPConstants.ITEM);
                    return checkInItemResponseData;
                } else {
                    processProblems(checkInItemResponseData, itemBarcode, droolsResponse.getErrorMessage().getErrorMessage(), OLENCIPConstants.ITEM);
                    return checkInItemResponseData;
                }
            } else {
                CheckedInItem checkedInItem = (CheckedInItem) droolsExchange.getFromContext("checkedInItem");
                if (checkedInItem != null) {
                    String itemDeleteIndicator = new LoanPeriodUtil().getParameter(OLENCIPConstants.TEMP_ITEM_DELETE_INDICATOR);
                    if (StringUtils.isNotBlank(itemDeleteIndicator) && itemDeleteIndicator.equalsIgnoreCase("y")) {
                        try {
                            org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(checkedInItem.getItemUuid());
                            String bibId = item.getHolding().getBib().getId();
                            getDocstoreClientLocator().getDocstoreClient().deleteBib(bibId);
                        } catch (Exception e) {
                            LOG.error("Exception while deleting bib" + e);
                            processProblems(checkInItemResponseData, itemBarcode, "Item SuccessFully Checked-in but Deletion is failed", OLENCIPConstants.ITEM);
                            return checkInItemResponseData;
                        }
                    }
                    ncipCheckInItemResponseBuilder.setItemId(checkInItemResponseData, itemBarcode, agencyId, checkedInItem.getItemType());
                    ncipCheckInItemResponseBuilder.setUserId(checkInItemResponseData, agencyId, checkedInItem);
                    ncipCheckInItemResponseBuilder.setItemOptionalFields(checkInItemResponseData, checkedInItem);
                } else {
                    processProblems(checkInItemResponseData, itemBarcode, ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.CHECK_IN_FAIL), OLENCIPConstants.ITEM);
                    return checkInItemResponseData;
                }
            }

        } catch (Exception e) {
            LOG.error("Exception " + e);
            processProblems(checkInItemResponseData, itemBarcode, ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.CHECK_IN_FAIL), OLENCIPConstants.ITEM);
            return checkInItemResponseData;
        }
        oleStopWatch.end();
        LOG.info("Time taken to perform Checkin item service : " + oleStopWatch.getTotalTime());
        return checkInItemResponseData;
    }

}
