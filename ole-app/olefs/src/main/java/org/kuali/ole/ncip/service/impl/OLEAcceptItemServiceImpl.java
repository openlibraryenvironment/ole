package org.kuali.ole.ncip.service.impl;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.ncip.bo.OLEAcceptItem;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.ncip.converter.OLEAcceptItemConverter;
import org.kuali.ole.ncip.service.OLEAcceptItemService;
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
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEAcceptItemServiceImpl implements OLEAcceptItemService {
    private static final Logger LOG = Logger.getLogger(OLEAcceptItemServiceImpl.class);
    private OLECirculationService oleCirculationService;
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
    public AcceptItemResponseData performService(AcceptItemInitiationData initData, ServiceContext serviceContext, RemoteServiceManager serviceManager) throws ServiceException {
        AcceptItemResponseData responseData = new AcceptItemResponseData();
        oleCirculationService = getOleCirculationService();
        oleCirculationHelperService = getOleCirculationHelperService();
        OLEAcceptItemConverter oleAcceptItemConverter = new OLEAcceptItemConverter();
        List<Problem> problems = new ArrayList<Problem>();
        String responseString = null;
        AgencyId agencyId = null;
        String operatorId, itemType, requestType, itemLocation;
        Problem problem = new Problem();
        ProblemType problemType = new ProblemType("");
        if (initData.getInitiationHeader() != null && initData.getInitiationHeader().getFromAgencyId() != null && initData.getInitiationHeader().getFromAgencyId().getAgencyId() != null && initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue() != null && !initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue().trim().isEmpty()) {
            agencyId = new AgencyId(initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue());
        } else if (initData.getInitiationHeader() != null && initData.getInitiationHeader().getApplicationProfileType() != null && initData.getInitiationHeader().getApplicationProfileType().getValue() != null && !initData.getInitiationHeader().getApplicationProfileType().getValue().trim().isEmpty()) {
            agencyId = new AgencyId(initData.getInitiationHeader().getApplicationProfileType().getValue());
        } else {
            agencyId = new AgencyId(getLoanProcessor().getParameter(OLENCIPConstants.AGENCY_ID_PARAMETER));
        }

        HashMap<String, String> agencyPropertyMap = oleCirculationHelperService.getAgencyPropertyMap(agencyId.getValue());
        try {
            if (agencyPropertyMap.size() > 0) {
                itemType = agencyPropertyMap.get(OLENCIPConstants.ITEM_TYPE);
                operatorId = agencyPropertyMap.get(OLENCIPConstants.OPERATOR_ID);
                requestType = agencyPropertyMap.get(OLENCIPConstants.REQUEST_TYPE);
                itemLocation = agencyPropertyMap.get(OLENCIPConstants.ITEM_LOCATION);
                String expiryDate = initData.getPickupExpiryDate() != null ? initData.getPickupExpiryDate().toString() : "";
                String title = initData.getItemOptionalFields() != null && initData.getItemOptionalFields().getBibliographicDescription() != null
                        && initData.getItemOptionalFields().getBibliographicDescription().getTitle() != null ? initData.getItemOptionalFields().getBibliographicDescription().getTitle() : "";
                String author = initData.getItemOptionalFields() != null && initData.getItemOptionalFields().getBibliographicDescription() != null
                        && initData.getItemOptionalFields().getBibliographicDescription().getAuthor() != null ? initData.getItemOptionalFields().getBibliographicDescription().getAuthor() : "";
                String pickUpLocation = initData.getPickupLocation() != null && initData.getPickupLocation().getValue() != null ? initData.getPickupLocation().getValue() : "";
                String callNumber = initData.getItemOptionalFields() != null && initData.getItemOptionalFields().getItemDescription() != null && initData.getItemOptionalFields().getItemDescription().getCallNumber() != null ? initData.getItemOptionalFields().getItemDescription().getCallNumber() : "";
                LOG.info("Inside Accept Item . Patron Barcode " + initData.getUserId().getUserIdentifierValue() + "Operator Id : " +operatorId + "Item barcode :" +  initData.getItemId().getItemIdentifierValue() + " Call Number : "+callNumber + "Title : "+title + " Author : " +author + "Item Type : "+ itemType + "Item Location : "+itemLocation + "Request Type :" + requestType + "Pick up Location : " + pickUpLocation + " Agency Id : " +agencyId.getValue());
                responseString = oleCirculationService.acceptItem((initData.getUserId().getUserIdentifierValue()),
                        operatorId, initData.getItemId().getItemIdentifierValue(), callNumber,
                        title, author, itemType, itemLocation, expiryDate, requestType, pickUpLocation);
                OLEAcceptItem oleAcceptItem = (OLEAcceptItem) oleAcceptItemConverter.generateAcceptItemObject(responseString);
                if (oleAcceptItem != null && oleAcceptItem.getMessage() != null && oleAcceptItem.getMessage().contains(OLEConstants.RQST_SUCCESS)) {
                    RequestId requestId = new RequestId();
                    requestId = setRequestId(requestId, initData);
                    responseData.setRequestId(requestId);
                    ItemId itemId = new ItemId();
                    itemId = setItemId(itemId, initData, initData.getItemId().getItemIdentifierValue());
                    responseData.setItemId(itemId);
                } else {
                    if (oleAcceptItem != null) {
                        problem.setProblemDetail(oleAcceptItem.getMessage());
                    } else {
                        problem.setProblemDetail(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.ACCEPT_ITEM_FAIL));
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
                problem.setProblemValue(agencyId.getValue());
                problems.add(problem);
                responseData.setProblems(problems);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return responseData;
    }

    private RequestId setRequestId(RequestId requestId, AcceptItemInitiationData initData) throws Exception {
        AgencyId agencyId = null;
        if (initData.getInitiationHeader() != null && initData.getInitiationHeader().getFromAgencyId() != null && initData.getInitiationHeader().getFromAgencyId().getAgencyId() != null && initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue() != null && !initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue().trim().isEmpty()) {
            agencyId = new AgencyId(initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue());
        } else if (initData.getInitiationHeader() != null && initData.getInitiationHeader().getApplicationProfileType() != null && initData.getInitiationHeader().getApplicationProfileType().getValue() != null && !initData.getInitiationHeader().getApplicationProfileType().getValue().trim().isEmpty()) {
            agencyId = new AgencyId(initData.getInitiationHeader().getApplicationProfileType().getValue());
        } else {
            agencyId = new AgencyId(getLoanProcessor().getParameter(OLENCIPConstants.AGENCY_ID_PARAMETER));
        }
        requestId.setAgencyId(agencyId);
        RequestIdentifierType requestIdentifierType = new RequestIdentifierType(OLENCIPConstants.SCHEME, OLENCIPConstants.REQUEST_IDS);
        requestId.setRequestIdentifierType(requestIdentifierType);
        requestId.setRequestIdentifierValue(initData.getRequestId().getRequestIdentifierValue());
        return requestId;
    }

    private ItemId setItemId(ItemId itemId, AcceptItemInitiationData initData, String itemIdentifierValue) throws Exception {
        AgencyId agencyId = null;
        if (initData.getInitiationHeader() != null && initData.getInitiationHeader().getFromAgencyId() != null && initData.getInitiationHeader().getFromAgencyId().getAgencyId() != null && initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue() != null && !initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue().trim().isEmpty()) {
            agencyId = new AgencyId(initData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue());
        } else if (initData.getInitiationHeader() != null && initData.getInitiationHeader().getApplicationProfileType() != null && initData.getInitiationHeader().getApplicationProfileType().getValue() != null && !initData.getInitiationHeader().getApplicationProfileType().getValue().trim().isEmpty()) {
            agencyId = new AgencyId(initData.getInitiationHeader().getApplicationProfileType().getValue());
        } else {
            agencyId = new AgencyId(getLoanProcessor().getParameter(OLENCIPConstants.AGENCY_ID_PARAMETER));
        }
        String identifierType;
        HashMap<String, String> agencyPropertyMap = oleCirculationHelperService.getAgencyPropertyMap(agencyId.getValue());
        identifierType = agencyPropertyMap.get(OLENCIPConstants.ITEM_TYPE);
        itemId.setAgencyId(agencyId);
        ItemIdentifierType itemIdentifierType = new ItemIdentifierType(OLENCIPConstants.SCHEME, OLENCIPConstants.ITEM_BARCODES);
        itemId.setItemIdentifierType(itemIdentifierType);
        itemId.setItemIdentifierValue(itemIdentifierValue);
        return itemId;
    }


}
