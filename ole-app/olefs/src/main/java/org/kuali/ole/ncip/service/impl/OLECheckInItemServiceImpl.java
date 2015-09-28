package org.kuali.ole.ncip.service.impl;

import org.apache.log4j.Logger;
import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.bo.OLECheckInItem;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.converter.OLECheckInItemConverter;
import org.kuali.ole.ncip.service.OLECheckInItemService;
import org.kuali.ole.ncip.service.OLECirculationService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 7/31/13
 * Time: 1:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECheckInItemServiceImpl implements OLECheckInItemService {
    private static final Logger LOG = Logger.getLogger(OLECheckInItemServiceImpl.class);
    private OLECirculationService oleCirculationService;
    private OLECheckInItemConverter oleCheckInItemConverter;
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

    public OLECheckInItemConverter getOleCheckInItemConverter() {
        if (null == oleCheckInItemConverter) {
            oleCheckInItemConverter = GlobalResourceLoader.getService(OLENCIPConstants.CHECKIN_ITEM_CONVERTER);
        }
        return oleCheckInItemConverter;
    }

    public void setOleCheckInItemConverter(OLECheckInItemConverter oleCheckInItemConverter) {
        this.oleCheckInItemConverter = oleCheckInItemConverter;
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
    public CheckInItemResponseData performService(CheckInItemInitiationData initData, ServiceContext serviceContext, RemoteServiceManager serviceManager) throws ServiceException {
        CheckInItemResponseData responseData = new CheckInItemResponseData();
        oleCirculationService = getOleCirculationService();
        oleCirculationHelperService = getOleCirculationHelperService();
        oleCheckInItemConverter = getOleCheckInItemConverter();
        OLECheckInItem oleCheckInItem = null;
        Problem problem = new Problem();
        ProblemType problemType = new ProblemType("");
        List<Problem> problems = new ArrayList<Problem>();
        String responseString = null;
        String itemDeleteIndicator = null;
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
                itemDeleteIndicator=getLoanProcessor().getParameter(OLENCIPConstants.TEMP_ITEM_DELETE_INDICATOR);
                LOG.info("Inside Check in Item Service . Operator Id : "+operatorId + " Item Id : " + initData.getItemId().getItemIdentifierValue());
                responseString = oleCirculationService.checkInItem(OLENCIPConstants.PATRON_ID, operatorId, initData.getItemId().getItemIdentifierValue(), itemDeleteIndicator,false);
                oleCheckInItem = (OLECheckInItem) oleCheckInItemConverter.generateCheckInItemObject(responseString);
                if (oleCheckInItem != null && oleCheckInItem.getMessage() != null && oleCheckInItem.getMessage().equals(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.SUCCESSFULLEY_CHECKED_IN))) {
                    ItemId itemId = new ItemId();
                    itemId = setItemId(itemId, initData, oleCheckInItem);
                    responseData.setItemId(itemId);
                    UserId userId = new UserId();
                    userId = setUserId(userId, initData, oleCheckInItem);
                    responseData.setUserId(userId);
                    ItemOptionalFields itemOptionalFields = new ItemOptionalFields();
                    itemOptionalFields = setItemOptionalFields(itemOptionalFields, initData, oleCheckInItem);
                    responseData.setItemOptionalFields(itemOptionalFields);
                } else {
                    if (oleCheckInItem != null) {
                        problem.setProblemDetail(oleCheckInItem.getMessage());
                    } else {
                        problem.setProblemDetail(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.CHECK_IN_FAIL));
                    }
                    problem.setProblemElement(OLENCIPConstants.ITEM);
                    problem.setProblemType(problemType);
                    problem.setProblemValue("Item value:" + initData.getItemId().getItemIdentifierValue());
                    problems.add(problem);
                    responseData.setProblems(problems);
                }
            } else {
                problem.setProblemDetail(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.INVALID_AGENCY_ID));
                problem.setProblemElement(OLENCIPConstants.AGENCY_ID);
                problem.setProblemType(problemType);
                problem.setProblemValue(agencyId.getValue());
                problems.add(problem);
                responseData.setProblems(problems);
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return responseData;
    }

    private ItemId setItemId(ItemId itemId, CheckInItemInitiationData initData, OLECheckInItem oleCheckInItem) throws Exception {
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

    private UserId setUserId(UserId userId, CheckInItemInitiationData initData, OLECheckInItem oleCheckInItem) throws Exception {
        AgencyId agencyId = null;
        if (initData.getInitiationHeader() != null && initData.getInitiationHeader().getFromAgencyId() != null && initData.getInitiationHeader().getFromAgencyId().getAgencyId() != null && initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue() != null && !initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue().trim().isEmpty())
            agencyId = new AgencyId(initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue());
        else if (initData.getInitiationHeader() != null && initData.getInitiationHeader().getApplicationProfileType() != null && initData.getInitiationHeader().getApplicationProfileType().getValue() != null && !initData.getInitiationHeader().getApplicationProfileType().getValue().trim().isEmpty())
            agencyId = new AgencyId(initData.getInitiationHeader().getApplicationProfileType().getValue());
        else
            agencyId = new AgencyId(getLoanProcessor().getParameter(OLENCIPConstants.AGENCY_ID_PARAMETER));
        UserIdentifierType userIdentifierType = new UserIdentifierType(oleCheckInItem.getUserType(), oleCheckInItem.getUserType());
        userId.setUserIdentifierType(userIdentifierType);
        if (oleCheckInItem.getUserId() != null)
            userId.setUserIdentifierValue(oleCheckInItem.getUserId());
        else
            userId.setUserIdentifierValue(" ");
        return userId;
    }

    private ItemOptionalFields setItemOptionalFields(ItemOptionalFields itemOptionalFields, CheckInItemInitiationData initData, OLECheckInItem oleCheckInItem) throws Exception {
        BibliographicDescription bibliographicDescription = new BibliographicDescription();
        bibliographicDescription.setAuthor(oleCheckInItem.getTitle());
        bibliographicDescription.setTitle(oleCheckInItem.getAuthor());
        itemOptionalFields.setBibliographicDescription(bibliographicDescription);
        ItemDescription itemDescription = new ItemDescription();
        itemDescription.setCallNumber(oleCheckInItem.getCallNumber());
        itemOptionalFields.setItemDescription(itemDescription);
        return itemOptionalFields;
    }


}
