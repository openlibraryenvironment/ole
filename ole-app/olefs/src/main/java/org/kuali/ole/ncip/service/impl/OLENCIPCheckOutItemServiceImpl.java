package org.kuali.ole.ncip.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.bo.OLECheckOutItem;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.converter.OLECheckOutItemConverter;
import org.kuali.ole.ncip.service.NCIPCheckOutItemResponseBuilder;
import org.kuali.ole.ncip.service.OLECheckOutItemService;
import org.kuali.ole.ncip.util.OLENCIPUtil;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenchulakshmig on 8/17/15.
 */
public class OLENCIPCheckOutItemServiceImpl extends NonSip2CheckoutItemServiceImpl implements OLECheckOutItemService {

    private static final Logger LOG = Logger.getLogger(OLENCIPCheckOutItemServiceImpl.class);

    private OLECirculationHelperServiceImpl oleCirculationHelperService;

    public NCIPCheckOutItemResponseBuilder getNCIPCheckOutItemResponseBuilder() {
        return new NCIPCheckOutItemResponseBuilder();
    }

    public OLECirculationHelperServiceImpl getOleCirculationHelperService() {
        if (null == oleCirculationHelperService) {
            oleCirculationHelperService = GlobalResourceLoader.getService(OLENCIPConstants.CIRCULATION_HELPER_SERVICE);
        }
        return oleCirculationHelperService;
    }

    public void setOleCirculationHelperService(OLECirculationHelperServiceImpl oleCirculationHelperService) {
        this.oleCirculationHelperService = oleCirculationHelperService;
    }

    @Override
    public CheckOutItemResponseData performService(CheckOutItemInitiationData checkOutItemInitiationData, ServiceContext serviceContext, RemoteServiceManager remoteServiceManager) throws ServiceException, ValidationException {
        LOG.info("Calling performService in OLENCIPCheckOutItemServiceImpl >>>");
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();

        OLENCIPUtil olencipUtil = new OLENCIPUtil();
        NCIPCheckOutItemResponseBuilder ncipCheckOutItemResponseBuilder = getNCIPCheckOutItemResponseBuilder();
        CheckOutItemResponseData checkOutItemResponseData = new CheckOutItemResponseData();

        AgencyId agencyId = olencipUtil.validateAgency(checkOutItemInitiationData.getInitiationHeader(), checkOutItemResponseData);
        if (null == agencyId) return checkOutItemResponseData;

        boolean userValid = olencipUtil.validateUser(checkOutItemInitiationData.getUserId(), checkOutItemResponseData);
        if (!userValid) return checkOutItemResponseData;

        String itemBarcode = checkOutItemInitiationData.getItemId().getItemIdentifierValue();
        String patronBarcode = checkOutItemInitiationData.getUserId().getUserIdentifierValue();
        String operatorId = olencipUtil.agencyPropertyMap.get(OLENCIPConstants.OPERATOR_ID);

        LOG.info("itemBarcode >>>" + itemBarcode);
        LOG.info("patronBarcode >>>" + patronBarcode);
        LOG.info("operatorId >>>" + operatorId);
        Map checkoutParameters = new HashMap();
        checkoutParameters.put("patronBarcode", patronBarcode);
        checkoutParameters.put("operatorId", operatorId);
        checkoutParameters.put("itemBarcode", itemBarcode);
        checkoutParameters.put("responseFormatType", "XML");

        String responseString = checkoutItem(checkoutParameters);
        OLECheckOutItem oleCheckOutItem = (OLECheckOutItem) new OLECheckOutItemConverter().generateCheckoutItemObject(responseString);

        if (oleCheckOutItem != null && StringUtils.isNotBlank(oleCheckOutItem.getMessage())) {
            if (oleCheckOutItem.getMessage().equals(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.SUCCESSFULLEY_LOANED))) {
                if (oleCheckOutItem.getDueDate() != null) {
                    ncipCheckOutItemResponseBuilder.setDateDue(checkOutItemResponseData, getOleCirculationHelperService().getGregorianCalendarDate(oleCheckOutItem.getDueDate().toString()));
                } else {
                    ncipCheckOutItemResponseBuilder.setDateDue(checkOutItemResponseData, getOleCirculationHelperService().getGregorianCalendarDate((new java.sql.Timestamp(new Date(2025, 1, 1).getTime()).toString())));
                }
                ncipCheckOutItemResponseBuilder.setRenewalCount(checkOutItemResponseData, new BigDecimal(oleCheckOutItem.getRenewalCount()));
                ncipCheckOutItemResponseBuilder.setItemId(checkOutItemResponseData, checkOutItemInitiationData, agencyId, olencipUtil.agencyPropertyMap.get(OLENCIPConstants.ITEM_TYPE));
                ncipCheckOutItemResponseBuilder.setUserId(checkOutItemResponseData, checkOutItemInitiationData, agencyId, oleCheckOutItem.getUserType());
            } else {
                String problemElement = "";
                String problemValue = "";
                if( getOleCheckOutItem().getCode().equals("014")) {
                    problemElement = OLENCIPConstants.ITEM;
                    problemValue = itemBarcode;
                }
                else if (oleCheckOutItem.getCode().equals("002")) {
                    problemElement = OLENCIPConstants.USER;
                    problemValue = patronBarcode;
                } else if (oleCheckOutItem.getCode().equals("026")) {
                    problemValue = operatorId;
                }
                else if(oleCheckOutItem.getCode().equals("500")) {
                    problemValue = "CheckOut Failed";
                }
                olencipUtil.processProblems(checkOutItemResponseData, problemValue, oleCheckOutItem.getMessage(), problemElement);
            }
        } else {
            olencipUtil.processProblems(checkOutItemResponseData, itemBarcode, ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.CHECK_OUT_FAIL), OLENCIPConstants.ITEM);
        }
        oleStopWatch.end();
        LOG.info("Time taken to perform checkout item service : " + oleStopWatch.getTotalTime());
        return checkOutItemResponseData;
    }

    @Override
    protected String fireRules() {
        return new OLENCIPUtil().fireLookupUserRules(getOlePatronDocument());
    }

}
