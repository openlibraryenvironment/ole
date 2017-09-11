package org.kuali.ole.ncip.service.impl;

import org.apache.log4j.Logger;
import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.ncip.bo.OLEPlaceRequest;
import org.kuali.ole.ncip.converter.OLEPlaceRequestConverter;
import org.kuali.ole.ncip.service.OLECirculationService;
import org.kuali.ole.ncip.service.OLERequestItemService;
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
 * Date: 2/19/14
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLERequestItemServiceImpl implements OLERequestItemService {
    private static final Logger LOG = Logger.getLogger(OLERequestItemServiceImpl.class);
    private OLECirculationService oleCirculationService;
    private OLECirculationHelperServiceImpl oleCirculationHelperService;
    private OLEPlaceRequestConverter olePlaceRequestConverter = new OLEPlaceRequestConverter();
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

    public OLEPlaceRequestConverter getOlePlaceRequestConverter() {
        return olePlaceRequestConverter;
    }

    public void setOlePlaceRequestConverter(OLEPlaceRequestConverter olePlaceRequestConverter) {
        this.olePlaceRequestConverter = olePlaceRequestConverter;
    }

    @Override
    public RequestItemResponseData performService(RequestItemInitiationData requestItemInitiationData, ServiceContext serviceContext, RemoteServiceManager remoteServiceManager) throws ServiceException {
        RequestItemResponseData requestItemResponseData = new RequestItemResponseData();

        oleCirculationService = getOleCirculationService();
        oleCirculationHelperService = getOleCirculationHelperService();
        List<Problem> problems = new ArrayList<Problem>();
        String responseString = null;
        AgencyId agencyId = null;
        RequestId requestId = new RequestId();
        String operatorId, itemType, itemLocation = "";
        Problem problem = new Problem();
        ProblemType problemType = new ProblemType("");
        if (requestItemInitiationData.getInitiationHeader() != null && requestItemInitiationData.getInitiationHeader().getFromAgencyId() != null && requestItemInitiationData.getInitiationHeader().getFromAgencyId().getAgencyId() != null && requestItemInitiationData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue() != null && !requestItemInitiationData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue().trim().isEmpty()) {
            agencyId = new AgencyId(requestItemInitiationData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue());
        } else if (requestItemInitiationData.getInitiationHeader() != null && requestItemInitiationData.getInitiationHeader().getApplicationProfileType() != null && requestItemInitiationData.getInitiationHeader().getApplicationProfileType().getValue() != null && !requestItemInitiationData.getInitiationHeader().getApplicationProfileType().getValue().trim().isEmpty()) {
            agencyId = new AgencyId(requestItemInitiationData.getInitiationHeader().getApplicationProfileType().getValue());
        } else {
            agencyId = new AgencyId(getLoanProcessor().getParameter(OLENCIPConstants.AGENCY_ID_PARAMETER));
        }
        String patronId = requestItemInitiationData.getUserId().getUserIdentifierValue();
        ItemId itemId = requestItemInitiationData.getItemId(0);
        String itemBarcode = itemId.getItemIdentifierValue();
        String requestType = requestItemInitiationData.getRequestType().getValue();
        String location = null;
        if (requestItemInitiationData.getPickupLocation() != null) {
            if (requestItemInitiationData.getPickupLocation().getValue() != null && !requestItemInitiationData.getPickupLocation().getValue().isEmpty()) {
                location = requestItemInitiationData.getPickupLocation().getValue();
            }
        }
        HashMap<String, String> agencyPropertyMap = oleCirculationHelperService.getAgencyPropertyMap(agencyId.getValue());
        if (agencyPropertyMap.size() > 0) {
            operatorId = agencyPropertyMap.get(OLENCIPConstants.OPERATOR_ID);
            LOG.info("Inside Request Item Service . Patron Barcode : "+patronId + " Operator Id : " + operatorId + "Item Barcode : " + itemBarcode + " Request Type : " + requestType + " Pick-up Location  : "+ location);
            String response = oleCirculationService.placeRequest(patronId, operatorId, itemBarcode,null, requestType, location, null,null, OLEConstants.ITEM_LEVEL,null, "");
            OLEPlaceRequest olePlaceRequest = (OLEPlaceRequest) olePlaceRequestConverter.generatePlaceRequestObject(response);
            if (olePlaceRequest != null && olePlaceRequest.getMessage() != null && olePlaceRequest.getMessage().contains(OLEConstants.RQST_SUCCESS)) {
                String reqId = olePlaceRequest.getRequestId();
                requestId.setRequestIdentifierValue(reqId);
                requestItemResponseData.setRequestId(requestId);
                requestItemResponseData.setItemId(itemId);
                requestItemResponseData.setHoldQueuePosition(new BigDecimal(olePlaceRequest.getQueuePosition()));
                requestItemResponseData.setDateAvailable(oleCirculationHelperService.getGregorianCalendarDate(olePlaceRequest.getAvailableDate()));
                requestItemResponseData.setUserId(requestItemInitiationData.getUserId());
                requestItemResponseData.setRequestType(requestItemInitiationData.getRequestType());
                requestItemResponseData.setRequestScopeType(requestItemInitiationData.getRequestScopeType());
            } else {
                if (olePlaceRequest != null) {
                    problem.setProblemDetail(olePlaceRequest.getMessage());
                } else {
                    problem.setProblemDetail(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.REQUEST_FAIL));
                }
                problem.setProblemElement(OLEConstants.ITEM);
                problem.setProblemType(problemType);
                problem.setProblemValue(itemBarcode);
                problems.add(problem);
                requestItemResponseData.setProblems(problems);
            }
        } else {
            problem.setProblemDetail(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.INVALID_AGENCY_ID));
            problem.setProblemElement(OLENCIPConstants.AGENCY_ID);
            problem.setProblemType(problemType);
            problem.setProblemValue(agencyId.getValue());
            problems.add(problem);
            requestItemResponseData.setProblems(problems);
        }

        return requestItemResponseData;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
