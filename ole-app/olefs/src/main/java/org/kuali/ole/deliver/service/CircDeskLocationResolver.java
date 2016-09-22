package org.kuali.ole.deliver.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationDeskDetail;
import org.kuali.ole.deliver.bo.OleCirculationDeskLocation;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.docstore.common.document.content.instance.LocationLevel;
import org.kuali.ole.service.OLEEResourceSearchService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created by premkb on 4/8/15.
 */
public class CircDeskLocationResolver {

    private static final Logger LOG = Logger.getLogger(CircDeskLocationResolver.class);

    private BusinessObjectService businessObjectService;
    private List<String> locationLevelIds = new ArrayList<>();

    public void setLocationLevelIds(List<String> locationLevelIds) {
        this.locationLevelIds = locationLevelIds;
    }


    public String getReplyToEmail(String itemLocation) {
        OleCirculationDesk oleCirculationDesk = getCirculationDesk(itemLocation);
        if (oleCirculationDesk != null && StringUtils.isNotBlank(oleCirculationDesk.getReplyToEmail())) {
            return oleCirculationDesk.getReplyToEmail();
        }
        return null;
    }


    public OleCirculationDesk getCirculationDesk(String itemLocation) {
        OleLocation oleLocation = null;
        try {
            if (StringUtils.isNotBlank(itemLocation)) {
                oleLocation = getLocationByLocationCode(itemLocation);
            }
        } catch (Exception e) {
            LOG.error("Exception " + e);
        }
        if (oleLocation != null) {
            OleCirculationDesk oleCirculationDesk = getCirculationDeskByLocationId(oleLocation.getLocationId());
            return oleCirculationDesk;
        }
        return null;
    }



    /**
     * This method returns location using location code.
     *
     * @param locationCode
     * @return
     * @throws Exception
     */
    public OleLocation getLocationByLocationCode(String locationCode) throws Exception {
        LOG.debug("Inside the getLocationByLocationCode method");
        Map barMap = new HashMap();
        barMap.put(OLEConstants.LOC_CD, locationCode);
        List<OleLocation> matchingLocation = (List<OleLocation>) getBusinessObjectService().findMatching(OleLocation.class, barMap);
        return matchingLocation != null && matchingLocation.size() > 0 ? matchingLocation.get(0) : null;
    }

    public String getFullPathLocation(OleLocation oleLocation,List<String> locationLevelNames) throws Exception {
        StringBuilder locationBuilder = new StringBuilder();
        for(String locationLevelName : locationLevelNames){
            String locationValue  = getLocationLevelName(oleLocation, locationLevelName);
            if(StringUtils.isNotBlank(locationValue))
                locationBuilder.append( locationValue + OLEConstants.SLASH);
        }
        String fullLocation = locationBuilder.toString();
        fullLocation = fullLocation.substring(0, fullLocation.length() - 1);
        return fullLocation;
    }

    private String getLocationLevelName(OleLocation oleLocation, String level) {
        if (oleLocation == null || org.apache.commons.lang.StringUtils.isEmpty(oleLocation.getLevelId()))
            return null;
        if (oleLocation.getLevelId().equalsIgnoreCase(level))
            return oleLocation.getLocationCode();
        return getLocationLevelName(oleLocation.getOleLocation(), level);
    }

    public String getFullPathLocationByName(OleLocation oleLocation) throws Exception {
        String levelFullCode = "";
        if (oleLocation != null) {
            levelFullCode = oleLocation.getLocationName();
            boolean parentId = oleLocation.getParentLocationId() != null ? true : false;
            while (parentId) {
                OleLocation location = getLocationByParentLocationId(oleLocation.getParentLocationId());
                if (levelFullCode != null) {
                    levelFullCode = location.getLocationName() + "-" + levelFullCode;
                }
                parentId = location.getParentLocationId() != null ? true : false;
                oleLocation = location;
            }
        }
        return levelFullCode;
    }

    private OleLocation getLocationByParentLocationId(String locationId){
        Map criteriaMap = new HashMap();
        criteriaMap.put(OLEConstants.LOCATION_ID, locationId);
        OleLocation location = businessObjectService.findByPrimaryKey(OleLocation.class,
                criteriaMap);
        return location;
    }

    public OleLocation getLocationByParentIdAndLocationCode(String parentLocationId,String locationCode) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("parentLocationId", parentLocationId);
        map.put("locationCode", locationCode);
        List<OleLocation> matchingLocation = (List<OleLocation>) getBusinessObjectService().findMatching(OleLocation.class, map);
        return matchingLocation != null && matchingLocation.size() > 0 ? matchingLocation.get(0) : null;
    }

    public String circulationDeskLocations(OleCirculationDesk oleCirculationDesk) throws Exception {
        String operatorsCirculationLocation = "";
        if (oleCirculationDesk != null) {
            if (oleCirculationDesk.getOleCirculationDeskLocations() != null) {
                StringBuffer location = new StringBuffer();
                for (OleCirculationDeskLocation oleCirculationDeskLocation : oleCirculationDesk.getOleCirculationDeskLocations()) {
                    if(oleCirculationDeskLocation.getCirculationPickUpDeskLocation()==null || (oleCirculationDeskLocation.getCirculationPickUpDeskLocation()!=null && oleCirculationDeskLocation.getCirculationPickUpDeskLocation().trim().isEmpty())){
                        location.append(getFullPathLocation(oleCirculationDeskLocation.getLocation(), getLocationLevelIds())).append(OLEConstants.DELIMITER_HASH);
                    }
                }
                operatorsCirculationLocation = location.toString();
                operatorsCirculationLocation = operatorsCirculationLocation.replaceAll(OLEConstants.SLASH + OLEConstants.DELIMITER_HASH, OLEConstants.DELIMITER_HASH);
            } else {
                throw new Exception(OLEConstants.NO_LOC_CIR_DESK);
            }
        }
        return operatorsCirculationLocation;
    }

    public OleCirculationDesk getCirculationDeskByLocationId(String locationId) {
        OleCirculationDeskLocation oleCirculationDeskLocation = getOleCirculationDeskLocationByLocationId(locationId);
        if (oleCirculationDeskLocation != null) {
            return getOleCirculationDesk(oleCirculationDeskLocation.getCirculationDeskId());
           /* Map<String, String> userMap = new HashMap<String, String>();
            userMap.put("circulationDeskId", oleCirculationDeskLocation.getCirculationDeskId());
            List<OleCirculationDesk> oleCirculationDesks = (List<OleCirculationDesk>) getBusinessObjectService().findMatching(OleCirculationDesk.class, userMap);
            return oleCirculationDesks != null && oleCirculationDesks.size() > 0 ? oleCirculationDesks.get(0) : null;*/
        }
        return null;
    }

    public OleCirculationDesk getOleCirculationDesk(String id) {
        LOG.debug("Inside the getOleCirculationDesk method");
        OleCirculationDesk oleCirculationDesk = getBusinessObjectService().findBySinglePrimaryKey(OleCirculationDesk.class, id);
        return oleCirculationDesk;
    }

    public OleCirculationDeskLocation getOleCirculationDeskLocationByLocationId(String locationId) {
        Map<String, String> locationMap = new HashMap<String, String>();
        locationMap.put("circulationDeskLocation", locationId);
        List<OleCirculationDeskLocation> oleCirculationDeskLocations = (List<OleCirculationDeskLocation>) getBusinessObjectService().findMatching(OleCirculationDeskLocation.class, locationMap);
        return oleCirculationDeskLocations != null && oleCirculationDeskLocations.size() > 0 ? oleCirculationDeskLocations.get(0) : null;
    }

    public OleCirculationDesk getCirculationDeskByLocationCode(String locationCode) {
        Map<String, String> userMap = new HashMap<String, String>();
        userMap.put(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_CD, locationCode);
        List<OleCirculationDesk> oleCirculationDesks = (List<OleCirculationDesk>) getBusinessObjectService().findMatching(OleCirculationDesk.class, userMap);
        return oleCirculationDesks != null && oleCirculationDesks.size() > 0 ? oleCirculationDesks.get(0) : null;
    }


    /**
     * * This method returns location level using location name.
     *
     * @param levelName
     * @return List
     * @throws Exception
     */
    public OleLocationLevel getLocationLevelByName(String levelName) throws Exception {
        LOG.debug("Inside the getLocationLevelByName method");
        Map barMap = new HashMap();
        barMap.put(OLEConstants.LEVEL_NAME, levelName);
        List<OleLocationLevel> matchingLocationLevel = (List<OleLocationLevel>) getBusinessObjectService().findMatching(OleLocationLevel.class, barMap);
        return matchingLocationLevel.get(0);
    }

    public List<String> getLocationLevelIds() {
        if(CollectionUtils.isEmpty(this.locationLevelIds)){
            List<OleLocationLevel> locationLevel = (List<OleLocationLevel>) KRADServiceLocator.getBusinessObjectService().findAllOrderBy(OleLocationLevel.class, OLEConstants.LEVEL_ID, true);
            for (OleLocationLevel oleLocationLevel : locationLevel) {
                this.locationLevelIds.add(oleLocationLevel.getLevelId());
            }
        }
        return this.locationLevelIds;
    }

    public Map<String, String> getLocationMap(String itemLocation){
        Map<String, String> locationMap = new HashMap<String, String>();
        if(StringUtils.isNotBlank(itemLocation)) {
            String[] locationArray = itemLocation.split("['/']");
            List<String> locationList = Arrays.asList(locationArray);
            for (String value : locationList) {
                Map<String, String> requestMap = new HashMap<>();
            /*requestMap.put(OLEConstants.LOCATION_CODE, value);
            List<OleLocation> oleLocations = (List<OleLocation>) getBusinessObjectService().findMatching(OleLocation.class, requestMap);*/
                OleLocation oleLocations = null;
                try {
                    oleLocations = getLocationByLocationCode(value);
                } catch (Exception location) {
                    LOG.error("Location Code does not exist." + location.getMessage());
                }
                if (oleLocations != null) {
                    String locationLevelId = oleLocations.getLevelId();
                    // requestMap.clear();
                    requestMap.put(OLEConstants.LEVEL_ID, locationLevelId);
                    List<OleLocationLevel> oleLocationLevels = (List<OleLocationLevel>) getBusinessObjectService().findMatching(OleLocationLevel.class, requestMap);
                    if (oleLocationLevels != null && oleLocationLevels.size() > 0) {
                        OleLocationLevel oleLocationLevel = new OleLocationLevel();
                        oleLocationLevel = oleLocationLevels.get(0);
                        if (oleLocationLevel.getLevelCode().equals(OLEConstants.OLEBatchProcess.LOCATION_LEVEL_CAMPUS)) {
                            locationMap.put(OLEConstants.ITEM_CAMPUS, value);
                        } else if (oleLocationLevel.getLevelCode().equals(OLEConstants.OLEBatchProcess.LOCATION_LEVEL_INSTITUTION)) {
                            locationMap.put(OLEConstants.ITEM_INSTITUTION, value);
                        } else if (oleLocationLevel.getLevelCode().equals(OLEConstants.OLEBatchProcess.LOCATION_LEVEL_COLLECTION)) {
                            locationMap.put(OLEConstants.ITEM_COLLECTION, value);
                        } else if (oleLocationLevel.getLevelCode().equals(OLEConstants.OLEBatchProcess.LOCATION_LEVEL_LIBRARY)) {
                            locationMap.put(OLEConstants.ITEM_LIBRARY, value);
                        } else if (oleLocationLevel.getLevelCode().equals(OLEConstants.OLEBatchProcess.LOCATION_LEVEL_SHELVING)) {
                            locationMap.put(OLEConstants.ITEM_SHELVING, value);
                        }
                    }
                }
            }
        }
        return locationMap;
    }

    public LocationLevel createLocationLevel(String locationName, LocationLevel locationLevel) {
        LOG.debug("Inside the createLocationLevel method");
        if (locationName != null && !locationName.equalsIgnoreCase("")) {
            BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
            String[] names = locationName.split("/");
            Map parentCriteria = new HashMap();
            parentCriteria.put(OLEConstants.LOC_CD, names[0]);
            OleLocation oleLocationCollection = businessObjectService.findByPrimaryKey(OleLocation.class, parentCriteria);
            String locationCode = oleLocationCollection.getLocationCode();
            String levelCode = oleLocationCollection.getOleLocationLevel().getLevelName();
            locationLevel.setName(locationCode);
            locationLevel.setLevel(levelCode);
            String locName = "";
            if (locationName.contains(OLEConstants.SLASH))
                locName = locationName.replace(names[0] + OLEConstants.SLASH, "");
            else
                locName = locationName.replace(names[0], "");
            if (locName != null && !locName.equals("")) {
                LocationLevel newLocationLevel = new LocationLevel();
                locationLevel.setLocationLevel(createLocationLevel(locName, newLocationLevel));
            }
        }
        return locationLevel;
    }

    public OleCirculationDesk getCircDeskForOpertorId(String principalId) {
        OleCirculationDesk circkDesk = null;
        if (StringUtils.isNotBlank(principalId)) {
            Map<String, String> userMap = new HashMap<String, String>();
            userMap.put("operatorId", principalId);
            Collection<OleCirculationDeskDetail> oleCirculationDeskDetails = KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDeskDetail.class, userMap);

            for (OleCirculationDeskDetail oleCirculationDeskDetail : oleCirculationDeskDetails) {
                if (oleCirculationDeskDetail.isDefaultLocation() && oleCirculationDeskDetail.getOleCirculationDesk().isActive()) {
                    circkDesk = (oleCirculationDeskDetail.getOleCirculationDesk());
                    break;
                }
            }
        }
        return circkDesk;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }
}
