package org.kuali.ole.ncip.service.impl;

import org.kuali.asr.handler.LookupUserResponseHandler;
import org.kuali.asr.handler.ResponseHandler;
import org.kuali.ole.deliver.OleLoanDocumentsFromSolrBuilder;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.util.OlePatronRecordUtil;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

/**
 * Created by pvsubrah on 9/24/15.
 */
public class LookupUserServiceUtil {
    private ResponseHandler responseHandler;
    private CircDeskLocationResolver circDeskLocationResolver;
    private OlePatronRecordUtil olePatronRecordUtil;
    private OLECirculationHelperServiceImpl oleCirculationHelperService;
    private OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder;
    private BusinessObjectService businessObjectService;
    private CircUtilController circUtilController;
    private OLECirculationServiceImpl oleCirculationService;

    public ResponseHandler getResponseHandler() {
        if (null == responseHandler) {
            responseHandler = new LookupUserResponseHandler();
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

    public OlePatronRecordUtil getOlePatronRecordUtil() {
        if (null == olePatronRecordUtil) {
            olePatronRecordUtil = (OlePatronRecordUtil) SpringContext.getBean("olePatronRecordUtil");
        }
        return olePatronRecordUtil;
    }

    public void setOlePatronRecordUtil(OlePatronRecordUtil olePatronRecordUtil) {
        this.olePatronRecordUtil = olePatronRecordUtil;
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

    public OleLoanDocumentsFromSolrBuilder getOleLoanDocumentsFromSolrBuilder() {
        if (null == oleLoanDocumentsFromSolrBuilder) {
            oleLoanDocumentsFromSolrBuilder = new OleLoanDocumentsFromSolrBuilder();
        }
        return oleLoanDocumentsFromSolrBuilder;
    }

    public void setOleLoanDocumentsFromSolrBuilder(OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder) {
        this.oleLoanDocumentsFromSolrBuilder = oleLoanDocumentsFromSolrBuilder;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public CircUtilController getCircUtilController() {
        if (null == circUtilController) {
            circUtilController = new CircUtilController();
        }
        return circUtilController;
    }

    public void setCircUtilController(CircUtilController circUtilController) {
        this.circUtilController = circUtilController;
    }

    public OLECirculationServiceImpl getOleCirculationService() {
        if (null == oleCirculationService) {
            oleCirculationService = new OLECirculationServiceImpl();
        }
        return oleCirculationService;
    }

    public void setOleCirculationService(OLECirculationServiceImpl oleCirculationService) {
        this.oleCirculationService = oleCirculationService;
    }

}
