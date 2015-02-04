package org.kuali.ole.service.impl;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleEventLogBo;
import org.kuali.ole.select.bo.OleLicenseRequestBo;
import org.kuali.ole.select.bo.OleLicenseRequestItemTitle;
import org.kuali.ole.select.bo.OleLicenseRequestStatus;
import org.kuali.ole.service.OleLicenseRequestService;
import org.kuali.ole.service.OleLicenseRequestWebService;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.criteria.CriteriaLookupService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.*;

/**
 * OleLicenseRequestWebServiceImpl creates license request for maintenanceDocument.
 */
public class OleLicenseRequestWebServiceImpl implements OleLicenseRequestWebService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleLicenseRequestWebServiceImpl.class);
    private BusinessObjectService businessObjectService;
    private IdentityService identityService;
    private CriteriaLookupService criteriaLookupService;
    private OleLicenseRequestService oleLicenseRequestService;

    /**
     *   Returns the businessObjectService instance.
     *   If it is not null  returns existing businessObjectService else creates new
     * @return  businessObjectService
     */
    protected BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    /**
     *  Returns identityService instance .
     *  If it is not null returns existing instance else creates new instance.
     * @return  identityService
     */
    protected IdentityService getIdentityService() {
        if (identityService == null) {
            identityService = KimApiServiceLocator.getIdentityService();
        }
        return identityService;
    }

    /**
     *  Returns criteriaLookupService instance.
     *  If it is not null returns existing instance else creates new instance.
     * @return criteriaLookupService
     */
    protected CriteriaLookupService getCriteriaLookupService() {
        if(criteriaLookupService == null) {
            criteriaLookupService = GlobalResourceLoader.getService("criteriaLookupService");
        }
        return criteriaLookupService;
    }

    /**
     * This method returns the object of OleLicesneRequestService
     * @return  oleLicenseRequestService
     */
    public OleLicenseRequestService getOleLicenseRequestservice() {
        if(oleLicenseRequestService == null ) {
            oleLicenseRequestService = GlobalResourceLoader.getService("oleLicenseRequestService");
        }
        return oleLicenseRequestService;
    }
    /**                                                                                                                N
     *  This method creates license request for maintenanceDocument using  document number and  licenseRequest.
     * @param documentnumber
     * @param itemUUIDs
     */
    @Override
    public OleLicenseRequestBo createLicenseRequest(String documentnumber, String itemUUIDs) {
        try{
            String user = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OleLicenseRequest.LICENSE_DOCUMENT_INITIATOR);
            GlobalVariables.setUserSession(new UserSession(user));
            DocumentService documentService= GlobalResourceLoader.getService(OLEConstants.DOCUMENT_HEADER_SERVICE);

            MaintenanceDocument licenseDoc = (MaintenanceDocument) documentService.getNewDocument(OLEConstants.OleLicenseRequest.LICENSE_REQUEST_DOC_TYPE);
            OleLicenseRequestBo oleLicenseRequestBo = (OleLicenseRequestBo) licenseDoc.getDocumentDataObject();
            oleLicenseRequestBo.setLocationId(OLEConstants.OleLicenseRequest.LICENSE_INITIAL_LOCATON);
            oleLicenseRequestBo.setLicenseRequestWorkflowTypeCode(OLEConstants.OleLicenseRequest.LICENSE_INITIAL_WORKFLOW);
            OleEventLogBo eventLog = new OleEventLogBo();
            eventLog.setEventType("system");
            eventLog.setCreatedBy(user);
            eventLog.setEventDescription(OLEConstants.OleLicenseRequest.LICENSE_REQ_INTIAL_EVENT_LOG);
            eventLog.setCurrentTimeStamp();
            oleLicenseRequestBo.getEventLogs().add(eventLog);
            oleLicenseRequestBo.seteResourceDocNumber(documentnumber);
            oleLicenseRequestBo.setLicenseRequestStatusCode(
                    ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OleLicenseRequest.LICENSE_REQ_DOCUMENT_STATUS));
            if(licenseDoc.getDocumentNumber() != null) {
                oleLicenseRequestBo.setDocumentNumber(licenseDoc.getDocumentNumber());
                oleLicenseRequestBo.setLicenseDocumentNumber(licenseDoc.getDocumentNumber());
            }

            List<OleLicenseRequestItemTitle> oleLicenseRequestItemTitles= new ArrayList<OleLicenseRequestItemTitle>();
            OleLicenseRequestItemTitle oleLicenseRequestItemTitle;
            String[] bibUUIDs;
            if (itemUUIDs != null ) {
                bibUUIDs=itemUUIDs.split(",");
                for(int i=0;i<bibUUIDs.length;i++){
                    oleLicenseRequestItemTitle=new OleLicenseRequestItemTitle();
                    oleLicenseRequestItemTitle.setItemUUID(bibUUIDs[i]);
                    oleLicenseRequestItemTitles.add(oleLicenseRequestItemTitle);
                }
            }

            oleLicenseRequestBo.setOleLicenseRequestItemTitles(oleLicenseRequestItemTitles);
            Date now = CoreApiServiceLocator.getDateTimeService().getCurrentSqlDate();
            Map criteria = new HashMap();
            criteria.put("code",
                    ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OleLicenseRequest.LICENSE_REQ_DOCUMENT_STATUS));
            OleLicenseRequestStatus licenseRequestStatus = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleLicenseRequestStatus.class,
                    criteria);
            licenseDoc.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus(licenseRequestStatus.getName());

            licenseDoc.getDocumentHeader().setDocumentDescription(OLEConstants.OleLicenseRequest.LICENSE_DESC+"[date:"+now+"]");
            licenseDoc.getNewMaintainableObject().setDataObject(oleLicenseRequestBo);
            MaintenanceDocument createdLicenseRequest = (MaintenanceDocument)documentService.saveDocument(licenseDoc);
            DocumentRouteHeaderValue documentHeader= DocumentRouteHeaderValue.from(licenseDoc.getDocumentHeader().getWorkflowDocument().getDocument());
            oleLicenseRequestBo.setDocumentRouteHeaderValue(documentHeader);
            return oleLicenseRequestBo;

        }catch(Exception e){
            LOG.error("Exception while creating license request"+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * This method returns the licenseRequestDocument number based on the requisitionDocument number
     * @param reqDocNum
     * @return  licenseRequestDocNum
     */
    /*@Override
    public String getLicenseRequestDocNumber(String reqDocNum) {
        String licenseRequestDocNum = "";
        if(reqDocNum != null && !reqDocNum.isEmpty()) {
            licenseRequestDocNum = getOleLicenseRequestservice().getLicenseRequestByRequisitionDocNum(reqDocNum);
        }
        return licenseRequestDocNum;
    }
*/
    /**
     *  This method returns URL based on the property  oleRequisitionWebService.url
     * @return   url
     */
    public String getURL() {
        String url = ConfigContext.getCurrentContextConfig().getProperty("oleRequisitionWebService.url");
        return url;
    }


}