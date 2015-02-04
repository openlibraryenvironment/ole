package org.kuali.ole.ncip.service.impl;

import org.apache.log4j.Logger;
import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.ncip.bo.OLERenewItem;
import org.kuali.ole.ncip.converter.OLERenewItemConverter;
import org.kuali.ole.ncip.service.OLECirculationService;
import org.kuali.ole.ncip.service.OLERenewItemService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 2/19/14
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLERenewItemServiceImpl implements OLERenewItemService {
    private static final Logger LOG = Logger.getLogger(OLERequestItemServiceImpl.class);
    private OLECirculationService oleCirculationService;
    private OLECirculationHelperServiceImpl oleCirculationHelperService;
    private OLERenewItemConverter oleRenewItemConverter = new OLERenewItemConverter();
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

    public OLERenewItemConverter getOleRenewItemConverter() {
        return oleRenewItemConverter;
    }

    public void setOleRenewItemConverter(OLERenewItemConverter oleRenewItemConverter) {
        this.oleRenewItemConverter = oleRenewItemConverter;
    }

    @Override
    public RenewItemResponseData performService(RenewItemInitiationData renewItemInitiationData, ServiceContext serviceContext, RemoteServiceManager remoteServiceManager) throws ServiceException {

        RenewItemResponseData renewItemResponseData = new RenewItemResponseData();
        oleCirculationService = getOleCirculationService();
        oleCirculationHelperService = getOleCirculationHelperService();
        List<Problem> problems = new ArrayList<Problem>();
        String responseString = null;
        AgencyId agencyId = null;
        Problem problem = new Problem();
        ProblemType problemType = new ProblemType("");
        if (renewItemInitiationData.getInitiationHeader() != null && renewItemInitiationData.getInitiationHeader().getFromAgencyId() != null && renewItemInitiationData.getInitiationHeader().getFromAgencyId().getAgencyId() != null && renewItemInitiationData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue() != null && !renewItemInitiationData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue().trim().isEmpty()) {
            agencyId = new AgencyId(renewItemInitiationData.getInitiationHeader().getFromAgencyId().getAgencyId().getValue());
        } else if (renewItemInitiationData.getInitiationHeader() != null && renewItemInitiationData.getInitiationHeader().getApplicationProfileType() != null && renewItemInitiationData.getInitiationHeader().getApplicationProfileType().getValue() != null && !renewItemInitiationData.getInitiationHeader().getApplicationProfileType().getValue().trim().isEmpty()) {
            agencyId = new AgencyId(renewItemInitiationData.getInitiationHeader().getApplicationProfileType().getValue());
        } else {
            agencyId = new AgencyId(getLoanProcessor().getParameter(OLENCIPConstants.AGENCY_ID_PARAMETER));
        }
        HashMap<String, String> agencyPropertyMap = oleCirculationHelperService.getAgencyPropertyMap(agencyId.getValue());
        if (agencyPropertyMap.size() > 0) {
            String operatorId = agencyPropertyMap.get(OLENCIPConstants.OPERATOR_ID);
            String patronId = renewItemInitiationData.getUserId().getUserIdentifierValue();
            String itemBarcode = renewItemInitiationData.getItemId().getItemIdentifierValue();
            LOG.info("Inside Renew Item Service . Patron Barcode :  " + patronId + " Operator Id : " +operatorId + "Item Barcode : " + itemBarcode);
            String response = oleCirculationService.renewItem(patronId, operatorId, itemBarcode);
            LOG.info(response);
            OLERenewItem oleRenewItem = (OLERenewItem) oleRenewItemConverter.generateRenewItemObject(response);
            if (oleRenewItem != null && oleRenewItem.getMessage().contains(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RENEW_SUCCESS))) {
                renewItemResponseData.setItemId(renewItemInitiationData.getItemId());
                renewItemResponseData.setUserId(renewItemInitiationData.getUserId());
                renewItemResponseData.setDateDue(oleCirculationHelperService.getGregorianCalendarDate(oleRenewItem.getPastDueDate()));
                renewItemResponseData.setDateForReturn(oleCirculationHelperService.getGregorianCalendarDate(oleRenewItem.getNewDueDate()));
                renewItemResponseData.setRenewalCount(new BigDecimal(oleRenewItem.getRenewalCount()));
            } else {
                if (oleRenewItem != null) {
                    problem.setProblemDetail(oleRenewItem.getMessage());
                } else {
                    problem.setProblemDetail(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.RENEW_FAIL));
                }
                problem.setProblemElement(OLENCIPConstants.ITEM);
                problem.setProblemType(problemType);
                problem.setProblemValue(itemBarcode);
                problems.add(problem);
                renewItemResponseData.setProblems(problems);
            }
        } else {
            problem.setProblemDetail(ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.INVALID_AGENCY_ID));
            problem.setProblemElement(OLENCIPConstants.AGENCY_ID);
            problem.setProblemType(problemType);
            problem.setProblemValue(agencyId.getValue());
            problems.add(problem);
            renewItemResponseData.setProblems(problems);
        }

        return renewItemResponseData;   //To change body of implemented methods use File | Settings | File Templates.
    }


}
