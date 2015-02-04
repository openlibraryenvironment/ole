package org.kuali.ole.ncip.service.impl;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.ncip.bo.OLECheckOutItem;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.ncip.converter.OLECheckOutItemConverter;
import org.kuali.ole.ncip.service.OLECheckOutItemService;
import org.kuali.ole.ncip.service.OLECirculationService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 7/31/13
 * Time: 1:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECheckOutItemServiceImpl implements OLECheckOutItemService {
    private static final Logger LOG = Logger.getLogger(OLECheckOutItemServiceImpl.class);
    private OLECirculationService oleCirculationService;
    private OLECheckOutItemConverter oleCheckOutItemConverter;
    private OLECirculationHelperServiceImpl oleCirculationHelperService;
    private LoanProcessor loanProcessor;

    public LoanProcessor getLoanProcessor() {
        if (loanProcessor == null) {
            loanProcessor = SpringContext.getBean(LoanProcessor.class);
        }
        return loanProcessor;
    }

    public OLECirculationService getOleCirculationService() {
        if (null == oleCirculationService) {
            oleCirculationService = GlobalResourceLoader.getService(OLENCIPConstants.CIRCULATION_SERVICE);
        }
        return oleCirculationService;
    }

    public void setOleCirculationService(OLECirculationService oleCirculationService) {
        this.oleCirculationService = oleCirculationService;
    }

    public OLECheckOutItemConverter getOleCheckOutItemConverter() {
        if (null == oleCheckOutItemConverter) {
            oleCheckOutItemConverter = GlobalResourceLoader.getService(OLENCIPConstants.CHECKOUT_ITEM_CONVERTER);
        }
        return oleCheckOutItemConverter;
    }

    public void setOleCheckOutItemConverter(OLECheckOutItemConverter oleCheckOutItemConverter) {
        this.oleCheckOutItemConverter = oleCheckOutItemConverter;
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
    public CheckOutItemResponseData performService(CheckOutItemInitiationData initData, ServiceContext serviceContext, RemoteServiceManager serviceManager) throws ServiceException, ValidationException {
        CheckOutItemResponseData responseData = new CheckOutItemResponseData();
        oleCirculationService = getOleCirculationService();
        oleCirculationHelperService = getOleCirculationHelperService();
        oleCheckOutItemConverter = getOleCheckOutItemConverter();
        OLECheckOutItem oleCheckOutItem = null;
        Problem problem = new Problem();
        ProblemType problemType = new ProblemType("");
        List<Problem> problems = new ArrayList<Problem>();
        String responseString = null;
        AgencyId agencyId = null;
        String operatorId, itemType = "";
        if (initData.getInitiationHeader() != null && initData.getInitiationHeader().getFromAgencyId() != null && initData.getInitiationHeader().getFromAgencyId().getAgencyId() != null && initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue() != null && !initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue().trim().isEmpty())
            agencyId = new AgencyId(initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue());
        else if (initData.getInitiationHeader() != null && initData.getInitiationHeader().getApplicationProfileType() != null && initData.getInitiationHeader().getApplicationProfileType().getValue() != null && !initData.getInitiationHeader().getApplicationProfileType().getValue().trim().isEmpty())
            agencyId = new AgencyId(initData.getInitiationHeader().getApplicationProfileType().getValue());
        else
            agencyId = new AgencyId(getLoanProcessor().getParameter(OLENCIPConstants.AGENCY_ID_PARAMETER));
        HashMap<String, String> agencyPropertyMap = oleCirculationHelperService.getAgencyPropertyMap(agencyId.getValue());
        try {
            if (agencyPropertyMap.size() > 0) {
                itemType = agencyPropertyMap.get(OLENCIPConstants.ITEM_TYPE);
                operatorId = agencyPropertyMap.get(OLENCIPConstants.OPERATOR_ID);
                LOG.info("Inside Check Out Item Service . Patron Barcode : " + initData.getUserId().getUserIdentifierValue() + " Operator Id : "+ operatorId + " Item Barcode : "+ initData.getItemId().getItemIdentifierValue() );
                responseString = oleCirculationService.checkOutItem(initData.getUserId().getUserIdentifierValue(), operatorId, initData.getItemId().getItemIdentifierValue());
                oleCheckOutItem = (OLECheckOutItem) oleCheckOutItemConverter.generateCheckoutItemObject(responseString);
                if (oleCheckOutItem != null && oleCheckOutItem.getMessage() != null && oleCheckOutItem.getMessage().equals(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.SUCCESSFULLEY_LOANED))) {
                    ItemId itemId = new ItemId();
                    itemId = setItemId(itemId, initData, oleCheckOutItem);
                    responseData.setRenewalCount(new BigDecimal(oleCheckOutItem.getRenewalCount()));
                    responseData.setDateDue(oleCirculationHelperService.getGregorianCalendarDate(oleCheckOutItem.getDueDate()));
                    responseData.setItemId(itemId);
                    UserId userId = new UserId();
                    userId = setUserId(userId, initData, oleCheckOutItem);
                    responseData.setUserId(userId);
                } else {
                    if (oleCheckOutItem != null) {
                        problem.setProblemDetail(oleCheckOutItem.getMessage());
                    } else {
                        problem.setProblemDetail(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.CHECK_OUT_FAIL));
                    }
                    problem.setProblemElement(OLENCIPConstants.ITEM);
                    problem.setProblemType(problemType);
                    problem.setProblemValue(initData.getItemId().getItemIdentifierValue());
                    problems.add(problem);
                    responseData.setProblems(problems);
                }
            } else {
                problem.setProblemDetail(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.INVALID_AGENCY_ID));
                problem.setProblemElement(OLENCIPConstants.AGENCY_ID);
                problem.setProblemType(problemType);
                problem.setProblemValue("Agency Id:" + agencyId.getValue());
                problems.add(problem);
                responseData.setProblems(problems);
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return responseData;
    }

    private ItemId setItemId(ItemId itemId, CheckOutItemInitiationData initData, OLECheckOutItem oleCheckOutItem) throws Exception {
        AgencyId agencyId = null;
        if (initData.getInitiationHeader() != null && initData.getInitiationHeader().getFromAgencyId() != null && initData.getInitiationHeader().getFromAgencyId().getAgencyId() != null && initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue() != null && !initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue().trim().isEmpty())
            agencyId = new AgencyId(initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue());
        else if (initData.getInitiationHeader() != null && initData.getInitiationHeader().getApplicationProfileType() != null && initData.getInitiationHeader().getApplicationProfileType().getValue() != null && !initData.getInitiationHeader().getApplicationProfileType().getValue().trim().isEmpty())
            agencyId = new AgencyId(initData.getInitiationHeader().getApplicationProfileType().getValue());
        else
            agencyId = new AgencyId(getLoanProcessor().getParameter(OLENCIPConstants.AGENCY_ID_PARAMETER));
        String identifierType = "";
        HashMap<String, String> agencyPropertyMap = oleCirculationHelperService.getAgencyPropertyMap(agencyId.getValue());
        identifierType = agencyPropertyMap.get(OLENCIPConstants.ITEM_TYPE);
        itemId.setAgencyId(agencyId);
        ItemIdentifierType itemIdentifierType = new ItemIdentifierType(OLENCIPConstants.SCHEME, identifierType);
        itemId.setItemIdentifierType(itemIdentifierType);
        itemId.setItemIdentifierValue(initData.getItemId().getItemIdentifierValue());
        return itemId;
    }

    private UserId setUserId(UserId userId, CheckOutItemInitiationData initData, OLECheckOutItem oleCheckOutItem) throws Exception {
        AgencyId agencyId = null;
        if (initData.getInitiationHeader() != null && initData.getInitiationHeader().getFromAgencyId() != null && initData.getInitiationHeader().getFromAgencyId().getAgencyId() != null && initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue() != null && !initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue().trim().isEmpty())
            agencyId = new AgencyId(initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue());
        else if (initData.getInitiationHeader() != null && initData.getInitiationHeader().getApplicationProfileType() != null && initData.getInitiationHeader().getApplicationProfileType().getValue() != null && !initData.getInitiationHeader().getApplicationProfileType().getValue().trim().isEmpty())
            agencyId = new AgencyId(initData.getInitiationHeader().getApplicationProfileType().getValue());
        else
            agencyId = new AgencyId(getLoanProcessor().getParameter(OLENCIPConstants.AGENCY_ID_PARAMETER));
        userId.setAgencyId(agencyId);
        UserIdentifierType userIdentifierType = new UserIdentifierType(oleCheckOutItem.getUserType(), oleCheckOutItem.getUserType());
        userId.setUserIdentifierValue(initData.getUserId().getUserIdentifierValue());
        userId.setUserIdentifierType(userIdentifierType);
        return userId;
    }


}
