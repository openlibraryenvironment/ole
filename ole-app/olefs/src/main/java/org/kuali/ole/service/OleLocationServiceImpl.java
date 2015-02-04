package org.kuali.ole.service;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.pojo.OleLocationGroup;
import org.kuali.ole.ingest.pojo.OleLocationIngest;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.rule.event.SaveDocumentEvent;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleLocationServiceImpl generates list of location to perform location operation using xml content.
 */
public class OleLocationServiceImpl implements OleLocationService {

     private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleLocationServiceImpl.class);

    private BusinessObjectService businessObjectService;
    private IdentityService identityService;
    private DocumentService documentService;
    private OleLocationConverterService oleLocationConverterService;
    private List<OleLocationIngest> createLocationList = new ArrayList<OleLocationIngest>();
    private List<OleLocationIngest> updateLocationList = new ArrayList<OleLocationIngest>();

    /**
     * Gets the instance of BusinessObjectService
     * @return businessObjectService(BusinessObjectService)
     */
    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    /**
     *   Gets the instance of IdentityService
     * @param serviceName
     * @param <T>
     * @return <T extends Object>
     */
    protected static <T extends Object> T getService(String serviceName) {
           return GlobalResourceLoader.<T>getService(serviceName);
       }

    /**
     *  Sets the businessObjectService attribute value.
     * @param businessObjectService
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    /**
     * Gets the identityService attribute value.
     * @return  identityService
     */
    public IdentityService getIdentityService() {
        return identityService;
    }

    /**
     *  Sets the identityService attribute value.
     * @param identityService
     */
    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

    /**
     * This method returns documentService
     * @return DocumentService
     */
    public DocumentService getDocumentService() {
        return getService(OLEConstants.DOCUMENT_HEADER_SERVICE);
    }

    /**
     *  Sets the documentService attribute value.
     * @param documentService
     */
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     *  This method returns location using locationCode.
     * @param locationCode
     * @return OleLocation
     */
    @Override
    public OleLocation getLocation(String locationCode) {
        LOG.debug("Inside the getLocation method");
        Map<String,Object> criteria = new HashMap<String,Object>(4);
        criteria.put(OLEConstants.PATRON_ENTITY_ACTIVE, Boolean.TRUE);
        return businessObjectService.findByPrimaryKey(OleLocation.class, criteria);
    }

    /**
     *  This method saves location document.
     * @param locationDocument
     * @return  locationDocument.getDocumentNumber
     * @throws org.kuali.rice.kew.api.exception.WorkflowException
     */
    private String saveLocationDocument(MaintenanceDocument locationDocument) throws WorkflowException {
        getDocumentService().saveDocument(locationDocument, SaveDocumentEvent.class);
        return locationDocument.getDocumentNumber();
    }

    /**
     * This method returns location number once location document is routed.
     * @param locationDocument
     * @return   locationDocument.getDocumentNumber
     * @throws org.kuali.rice.kew.api.exception.WorkflowException
     */
    private String routeLocationDocument(MaintenanceDocument locationDocument) throws WorkflowException{
        getDocumentService().routeDocument(locationDocument, null, null);
        return locationDocument.getDocumentNumber();
    }

    /**
     * This method returns boolean after create location.
     * @param oleLocation
     * @return  doc
     */
    public boolean createLocation(OleLocation oleLocation) {
       boolean doc = false;
       OleLocation oleLocation1= getBusinessObjectService().save(oleLocation);
       doc= true;
       return doc;
    }

    /**
     *  This method updates location using oleLocation.
     * @param oleLocation
     * @return   doc
     */
    public boolean updateLocation(OleLocation oleLocation) {
       boolean doc = false;
       OleLocation oleLocation1= getBusinessObjectService().save(oleLocation);
       doc= true;
       return doc;
     }


    /**
     *  This method process the location details using xmlContent.This updates location if already exist in the list otherwise
     *  creates new location list.
     * @param xmlContent
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException
     */
    private void processLocationDetails(String xmlContent) throws IOException, URISyntaxException {

        OleLocationGroup oleLocationGroup =oleLocationConverterService.buildLocationFromFileContent(xmlContent);
        List<OleLocation> existingLocationList = (List<OleLocation>) getBusinessObjectService().findAll(OleLocation.class);
                 String oleLocationCode ;
                  OleLocationIngest oleLocationIngest;

       for(int i = 0;i<oleLocationGroup.getLocationGroup().size();i++){
              oleLocationIngest = oleLocationGroup.getLocationGroup().get(i);
              oleLocationCode = oleLocationIngest.getLocationCode();
             for(int j=0;j<existingLocationList.size();j++){
                 if(oleLocationCode.equals(existingLocationList.get(j).getLocationCode())){
                     updateLocationList.add(oleLocationIngest);
                 }else if(!(oleLocationCode == null) || !(oleLocationCode.equals(""))){
                     createLocationList.add(oleLocationIngest);
                 }
             }
       }
    }
  }

