package org.kuali.ole.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ntp.TimeStamp;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.FileUtil;
import org.kuali.ole.ingest.OleLocationObjectGeneratorFromXML;
import org.kuali.ole.ingest.OleLocationXMLSchemaValidator;
import org.kuali.ole.ingest.pojo.OleLocationGroup;
import org.kuali.ole.ingest.pojo.OleLocationIngest;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationIngestSummaryRecord;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleLocationConverterService generates list of location to perform ole location operation.
 */
public class OleLocationConverterService {

    private BusinessObjectService businessObjectService;
    private OleLocationObjectGeneratorFromXML oleLocationObjectGeneratorFromXML;
    private OleLocationService oleLocationService;

    private List<OleLocationIngest> createLocationList = new ArrayList<OleLocationIngest>(0);
    private List<OleLocationIngest> updateLocationList = new ArrayList<OleLocationIngest>(0);
    private List<OleLocationIngest> rejectLocationList = new ArrayList<OleLocationIngest>(0);
    private String ingestFailureRecord;
    private int successCount;
    private int failureCount;
    /**
     * Sets the businessObjectService attribute value.
     * @param businessObjectService
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    /**
     * Gets the oleLocationService attribute value.
     * @return oleLocationService.
     */
    public OleLocationService getOleLocationService() {
        return oleLocationService;
    }

    /**
     * Sets the oleLocationService attribute value.
     * @param oleLocationService
     */
    public void setOleLocationService(OleLocationService oleLocationService) {
        this.oleLocationService = oleLocationService;
    }

    /**
     * Gets the createLocationList attribute value.
     * @return  createLocationList
     */
    public List<OleLocationIngest> getCreateLocationList() {
        return createLocationList;
    }

    /**
     *  Sets the createLocationList attribute value.
     * @param createLocationList
     */
    public void setCreateLocationList(List<OleLocationIngest> createLocationList) {
        this.createLocationList = createLocationList;
    }

    /**
     * Gets the updateLocationList attribute value.
     * @return updateLocationList
     */
    public List<OleLocationIngest> getUpdateLocationList() {
        return updateLocationList;
    }

    /**
     *  Sets the updateLocationList attribute value.
     * @param updateLocationList
     */
    public void setUpdateLocationList(List<OleLocationIngest> updateLocationList) {
        this.updateLocationList = updateLocationList;
    }

    public List<OleLocationIngest> getRejectLocationList() {
        return rejectLocationList;
    }

    public void setRejectLocationList(List<OleLocationIngest> rejectLocationList) {
        this.rejectLocationList = rejectLocationList;
    }

    public String getIngestFailureRecord() {
        return ingestFailureRecord;
    }

    public void setIngestFailureRecord(String ingestFailureRecord) {
        this.ingestFailureRecord = ingestFailureRecord;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

    /**
     * This method persist business object using xml file.
     * @param fileContent
     * @param fileName
     * @return String
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException
     */
    public String persistLocationFromFileContent(String fileContent, String fileName) throws IOException, URISyntaxException {
       Timestamp st = new Timestamp(System.currentTimeMillis());
       createLocationList = new ArrayList<OleLocationIngest>(0);
       updateLocationList = new ArrayList<OleLocationIngest>(0);
       rejectLocationList = new ArrayList<OleLocationIngest>(0);
        OleLocationIngestSummaryRecord oleLocationIngestSummaryRecord =  new OleLocationIngestSummaryRecord();
        OleLocationGroup location = getOleLocationObjectGeneratorFromXML().buildLocationFromFileContent(fileContent);
        int locationTotCount = location.getLocationGroup().size();
        for(int i = 0;i<location.getLocationGroup().size();i++){
             OleLocationIngest oleLocationIngest=location.getLocationGroup().get(i);
            if(isLocationLevelCodeExist(oleLocationIngest))    {
                  rejectLocationList.add(oleLocationIngest);
                }else if(isParentLocationExist(oleLocationIngest)) {
                  rejectLocationList.add(oleLocationIngest);
            }
            else {
                if(isLocationExist(oleLocationIngest)){
                    updateLocationList.add(oleLocationIngest);
                    updateOleLocation(oleLocationIngest);
                  } else{
                    createLocationList.add(oleLocationIngest);
                    createOleLocation(oleLocationIngest);
                }
            }
        }

        if (createLocationList.size() > 0) {
            oleLocationIngestSummaryRecord.setOleLocationCreateCount(createLocationList.size());
        }
        if (updateLocationList.size() > 0) {
            oleLocationIngestSummaryRecord.setOleLocationUpdateCount(updateLocationList.size());
        }
        if(rejectLocationList.size()>0) {
            oleLocationIngestSummaryRecord.setOleLocationFailedCount(rejectLocationList.size());
        }
        oleLocationIngestSummaryRecord.setOleLocationTotCount(locationTotCount);
        oleLocationIngestSummaryRecord.setFileName(fileName);
        String principalName="";
       if(GlobalVariables.getUserSession()!=null && GlobalVariables.getUserSession().getPrincipalName()!=null ){
         principalName = GlobalVariables.getUserSession().getPrincipalName();
       }
        oleLocationIngestSummaryRecord.setOlePrincipalName(principalName);
        oleLocationIngestSummaryRecord.setDate(new Timestamp(System.currentTimeMillis()));
        getBusinessObjectService().save(oleLocationIngestSummaryRecord);

        if(rejectLocationList.size()>0) {
            String patronXML = getOleLocationObjectGeneratorFromXML().toXML(rejectLocationList);
            setIngestFailureRecord(patronXML);
            //saveFailureRecordsForAttachment(patronXML,oleLocationIngestSummaryRecord.getOleLocationSummaryId(),fileName);
        }

        int successCount=updateLocationList.size()+createLocationList.size();
        setSuccessCount(successCount);
        setFailureCount(rejectLocationList.size());
        int totalCount=successCount+rejectLocationList.size();
       return "with Total:"+totalCount+" ,Sucess:"+successCount+" , Failure :"+rejectLocationList.size();
    }

    /**
     *  This method moves failed xml file in to specified failed location.
     * @param locationXML
     * @param locationReportId
     */
    private void saveFailureRecordsForAttachment(String locationXML,String locationReportId){
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            String directory =ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.LOCATION_ERROR_FILE_PATH);
            String homeDirectory = System.getProperty(OLEConstants.USER_HOME_DIRECTORY);
            int reportId = Integer.parseInt(locationReportId);
            File file = new File(directory);
            if(file.isDirectory()){
                file = new File(directory+reportId+OLEConstants.FAILED_LOCATION_RECORD_NAME);
                file.createNewFile();
                FileUtils.writeStringToFile(file, locationXML);
            } else{
                file.mkdirs();
                file = new File(directory+reportId+OLEConstants.FAILED_LOCATION_RECORD_NAME);
                file.createNewFile();
                FileUtils.writeStringToFile(file, locationXML);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *   This method checks LocationLevelCode using oleLocationIngest.
     * @param oleLocationIngest
     * @return  boolean
     */
     public boolean isLocationLevelCodeExist( OleLocationIngest oleLocationIngest){
          String locationLevelCode =  oleLocationIngest.getLocationLevelCode();
              Map locationLevelCodeMap = new HashMap();
              locationLevelCodeMap.put("levelCode",locationLevelCode);
              List<OleLocationLevel> locationLevel = (List<OleLocationLevel>)getBusinessObjectService().findMatching(OleLocationLevel.class,locationLevelCodeMap);
              if(locationLevel.size()>0)
               return false;
              else
               return true;
     }

    /**
     * This method checks existence of ParentLocation using oleLocationIngest.
     * @param oleLocationIngest
     * @return  boolean
     */
     public boolean isParentLocationExist(OleLocationIngest oleLocationIngest){
        String parentLocationCode = oleLocationIngest.getParentLocationCode();
         if(! "".equals(parentLocationCode) && parentLocationCode != null ){
         Map  parentLocationCodeMap = new HashMap();
         parentLocationCodeMap.put("locationCode",parentLocationCode);
         List<OleLocation> locationLevel = (List<OleLocation>)getBusinessObjectService().findMatching(OleLocation.class,parentLocationCodeMap);
         if(locationLevel.size()>0)
               return false;
              else
               return true;
         }
         return false;
     }

    /**
     *   This method checks Location using oleLocationIngest.
     * @param oleLocationIngest
     * @return  boolean
     */
     public boolean isLocationExist( OleLocationIngest oleLocationIngest){
         String locationCode=oleLocationIngest.getLocationCode();
         Map localtionCodeMap = new HashMap();
         localtionCodeMap.put("locationCode",locationCode);
         List<OleLocation> location = (List<OleLocation>)getBusinessObjectService().findMatching(OleLocation.class,localtionCodeMap);
         if(location.size()>0){
             oleLocationIngest.setLocationId(location.get(0).getLocationId());
              return true;
         }
         return false;
    }

    /**
     *  This method creates location using oleLocation.
     * @param oleLocation
     */
    public void createOleLocation(OleLocationIngest oleLocation){
        OleLocation oleLocationpojo = new OleLocation();
        oleLocationpojo.setLocationCode(oleLocation.getLocationCode());
        oleLocationpojo.setLocationName(oleLocation.getLocationName());
        String locationLevelCode = oleLocation.getLocationLevelCode();
        OleLocationLevel oleLocationLevel;
        Map localtionLevelCd = new HashMap();
        localtionLevelCd.put("levelCode",locationLevelCode);
        List<OleLocationLevel> levelList = (List<OleLocationLevel>)getBusinessObjectService().findMatching(OleLocationLevel.class,localtionLevelCd);
        if(levelList.size()>0){
            oleLocationLevel =  levelList.get(0);
            oleLocationpojo.setLevelId(oleLocationLevel.getLevelId());
        } else{
            throw new RuntimeException();
        }
        String  parentLocationCode =  oleLocation.getParentLocationCode();
        OleLocation oleLocations;
        Map parentLevelcd = new HashMap();
        parentLevelcd.put("locationCode",parentLocationCode);
        List<OleLocation> parentList = (List<OleLocation>)getBusinessObjectService().findMatching(OleLocation.class,parentLevelcd);
            if(parentList.size()>0){
                oleLocationpojo.setParentLocationId(parentList.get(0).getLocationId());
                persistOleLocation(oleLocation, oleLocationpojo);
            }else if("".equals(oleLocation.getParentLocationCode()) || oleLocation.getParentLocationCode() == null ){
            oleLocationpojo.setParentLocationId(null);
        }
        getOleLocationService().createLocation(oleLocationpojo);
    }

    /**
     *    This method updates location using oleLocation.
     * @param oleLocation
     */
    public void updateOleLocation(OleLocationIngest oleLocation){
        Map locationIdMap = new HashMap();
        locationIdMap.put("locationId",oleLocation.getLocationId());
        List<OleLocation> oleLocationList = (List<OleLocation>)getBusinessObjectService().findMatching(OleLocation.class,locationIdMap);
        OleLocation oleLocationpojo = oleLocationList.get(0);
        oleLocationpojo.setLocationCode(oleLocation.getLocationCode());
        oleLocationpojo.setLocationName(oleLocation.getLocationName());
        String locationLevelCode = oleLocation.getLocationLevelCode();
        OleLocationLevel oleLocationLevel;
        Map localtionLevelCd = new HashMap();
        localtionLevelCd.put("levelCode",locationLevelCode);
        List<OleLocationLevel> levelList = (List<OleLocationLevel>)getBusinessObjectService().findMatching(OleLocationLevel.class,localtionLevelCd);
        if(levelList.size()>0){
            oleLocationLevel =  levelList.get(0);
            oleLocationpojo.setLevelId(oleLocationLevel.getLevelId());
        } else{
            throw new RuntimeException();
        }
        String  parentLocationCode =  oleLocation.getParentLocationCode();
        OleLocation oleLocations;
        Map parentLevelcd = new HashMap();
        parentLevelcd.put("locationCode",parentLocationCode);
        List<OleLocation> parentList = (List<OleLocation>)getBusinessObjectService().findMatching(OleLocation.class, parentLevelcd);
        if(parentList.size()>0){
             oleLocationpojo.setParentLocationId(parentList.get(0).getLocationId());
             persistOleLocation(oleLocation, oleLocationpojo);
         }else if("".equals(oleLocation.getParentLocationCode()) || oleLocation.getParentLocationCode() == null ){
            oleLocationpojo.setParentLocationId(null);
         }
         getOleLocationService().updateLocation(oleLocationpojo);
    }

    /**
     *
     * @param fileContent
     * @return  OleLocationGroup
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     */
    public OleLocationGroup buildLocationFromFileContent(String fileContent) throws URISyntaxException, IOException {
           return getOleLocationObjectGeneratorFromXML().buildLocationFromFileContent(fileContent);
       }

    /**
     *
     * @param fileName
     * @return  OleLocationGroup/null
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */
    public OleLocationGroup buildLocation(String fileName) throws URISyntaxException, IOException, SAXException {
        URL resource = getClass().getResource(fileName);
        File file = new File(resource.toURI());
        String fileContent = new FileUtil().readFile(file);
        if (validLocationXML(fileContent)) {
            return getOleLocationObjectGeneratorFromXML().buildLocationFromFileContent(fileContent);
        }
        return null;
    }

    /**
     * This method validate the fileContent .
     * @param fileContent
     * @return boolean
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */
    private boolean validLocationXML(String fileContent) throws IOException, SAXException {
        return new OleLocationXMLSchemaValidator().validateContentsAgainstSchema(null);
    }

    /**
     *  Persists oleLocation using locationIngest and oleLocation.
     * @param locationIngest
     * @param oleLocation
     */
    private void persistOleLocation(OleLocationIngest locationIngest, OleLocation oleLocation) {

            Map criteria = new HashMap<String,String>();
            criteria.put("levelCode",locationIngest.getLocationLevelCode());
            List<OleLocationLevel> oleLocationLevelList = (List<OleLocationLevel>) getBusinessObjectService().findMatching(OleLocationLevel.class,criteria);
            oleLocation.setOleLocationLevel(oleLocationLevelList.get(0));
    }

    /**
     * Gets the businessObjectService attribute value.
     * @return businessObjectService.
     */
    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    /**
     * Gets the oleLocationObjectGeneratorFromXML attribute value.
     * @return  oleLocationObjectGeneratorFromXML.
     */
    public OleLocationObjectGeneratorFromXML getOleLocationObjectGeneratorFromXML() {
        if (null == oleLocationObjectGeneratorFromXML) {
            oleLocationObjectGeneratorFromXML = new OleLocationObjectGeneratorFromXML();
        }
        return oleLocationObjectGeneratorFromXML;
    }

    /**
     * Sets the oleLocationObjectGeneratorFromXML attribute value.
     * @param oleLocationObjectGeneratorFromXML
     */
    public void setOleLocationObjectGeneratorFromXML(OleLocationObjectGeneratorFromXML oleLocationObjectGeneratorFromXML) {
        this.oleLocationObjectGeneratorFromXML = oleLocationObjectGeneratorFromXML;
    }

}
