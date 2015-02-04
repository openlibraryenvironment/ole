package org.kuali.ole.ncip.service.impl;

import org.apache.log4j.Logger;
import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.ncip.bo.OLECancelRequest;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.ncip.converter.OLECancelRequestConverter;
import org.kuali.ole.ncip.service.OLECancelRequestItemService;
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
 * Date: 2/19/14
 * Time: 4:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECancelRequestItemServiceImpl implements OLECancelRequestItemService {
    private static final Logger LOG = Logger.getLogger(OLECancelRequestItemServiceImpl.class);
    private OLECirculationService oleCirculationService;
    private OLECirculationHelperServiceImpl oleCirculationHelperService;
    private OLECancelRequestConverter oleCancelRequestConverter = new OLECancelRequestConverter();
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

    public OLECancelRequestConverter getOleCancelRequestConverter() {
        return oleCancelRequestConverter;
    }

    public void setOleCancelRequestConverter(OLECancelRequestConverter oleCancelRequestConverter) {
        this.oleCancelRequestConverter = oleCancelRequestConverter;
    }

    @Override
    public CancelRequestItemResponseData performService(CancelRequestItemInitiationData cancelRequestItemInitiationData, ServiceContext serviceContext, RemoteServiceManager remoteServiceManager) throws ServiceException {
        CancelRequestItemResponseData cancelRequestItemResponseData = new CancelRequestItemResponseData();
        oleCirculationService = getOleCirculationService();
        oleCirculationHelperService = getOleCirculationHelperService();
        List<Problem> problems = new ArrayList<Problem>();
        AgencyId agencyId = null;
        Problem problem = new Problem();
        ProblemType problemType = new ProblemType("");
        if (cancelRequestItemInitiationData.getInitiationHeader() != null && cancelRequestItemInitiationData.getInitiationHeader().getFromAgencyId() != null && cancelRequestItemInitiationData.getInitiationHeader().getFromAgencyId().getAgencyId() != null && cancelRequestItemInitiationData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue() != null && !cancelRequestItemInitiationData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue().trim().isEmpty()) {
            agencyId = new AgencyId(cancelRequestItemInitiationData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue());
        } else if (cancelRequestItemInitiationData.getInitiationHeader() != null && cancelRequestItemInitiationData.getInitiationHeader().getApplicationProfileType() != null && cancelRequestItemInitiationData.getInitiationHeader().getApplicationProfileType().getValue() != null && !cancelRequestItemInitiationData.getInitiationHeader().getApplicationProfileType().getValue().trim().isEmpty()) {
            agencyId = new AgencyId(cancelRequestItemInitiationData.getInitiationHeader().getApplicationProfileType().getValue());
        } else {
            agencyId = new AgencyId(getLoanProcessor().getParameter(OLENCIPConstants.AGENCY_ID_PARAMETER));
        }
        HashMap<String, String> agencyPropertyMap = oleCirculationHelperService.getAgencyPropertyMap(agencyId.getValue());
        if (agencyPropertyMap.size() > 0) {
            String operatorId = agencyPropertyMap.get(OLENCIPConstants.OPERATOR_ID);
            String patronId = cancelRequestItemInitiationData.getUserId().getUserIdentifierValue();
            String itemBarcode = cancelRequestItemInitiationData.getItemId().getItemIdentifierValue();
            LOG.info("Inside Cancel Request . Patron Barcode : " + patronId + " Operator Id : " + operatorId + " Item Barcode : " + itemBarcode);
            String response = oleCirculationService.cancelRequest(operatorId, patronId, itemBarcode);
            LOG.info(response);
            OLECancelRequest oleCancelRequest = (OLECancelRequest) oleCancelRequestConverter.generateCancelRequestObject(response);

            if (oleCancelRequest != null && oleCancelRequest.getMessage() != null && oleCancelRequest.getMessage().contains(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.REQUEST_SUCCESSFULLEY_CANCELLED))) {
                cancelRequestItemResponseData.setUserId(cancelRequestItemInitiationData.getUserId());
                cancelRequestItemResponseData.setItemId(cancelRequestItemInitiationData.getItemId());
                cancelRequestItemResponseData.setRequestId(cancelRequestItemInitiationData.getRequestId());
            } else {
                if (oleCancelRequest != null) {
                    problem.setProblemDetail(oleCancelRequest.getMessage());
                } else {
                    problem.setProblemDetail(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RQST_FAIL));
                }
                problem.setProblemElement(OLENCIPConstants.ITEM);
                problem.setProblemType(problemType);
                problem.setProblemValue(itemBarcode);
                problems.add(problem);
                cancelRequestItemResponseData.setProblems(problems);
            }
        } else {
            problem.setProblemDetail(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.INVALID_AGENCY_ID));
            problem.setProblemElement(OLENCIPConstants.AGENCY_ID);
            problem.setProblemType(problemType);
            problem.setProblemValue(agencyId.getValue());
            problems.add(problem);
            cancelRequestItemResponseData.setProblems(problems);
        }

        return cancelRequestItemResponseData;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
