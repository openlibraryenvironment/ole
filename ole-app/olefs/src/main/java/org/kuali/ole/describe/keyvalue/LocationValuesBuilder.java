package org.kuali.ole.describe.keyvalue;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleTemporaryCirculationHistory;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.Location;
import org.kuali.ole.docstore.common.document.content.instance.LocationLevel;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * LocationValuesBuilder used to render the values for LocationValuesBuilder dropdown control.
 */
public class LocationValuesBuilder extends KeyValuesBase {

    private static final Logger LOG = Logger.getLogger(LocationValuesBuilder.class);

    public static List<KeyValue> locationKeyValues = null;

    public static long timeLastRefreshed;


    public static int refreshInterval = 300;     // in seconds
    private CircDeskLocationResolver circDeskLocationResolver;

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public void setCircDeskLocationResolver(CircDeskLocationResolver circDeskLocationResolver) {
        this.circDeskLocationResolver = circDeskLocationResolver;
    }
    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments LevelCode and
     * LocationName.
     *
     * @return List<KeyValue>
     */

    private DocstoreUtil docstoreUtil;
    public DocstoreUtil getDocstoreUtil() {

        if (docstoreUtil == null) {
            docstoreUtil = SpringContext.getBean(DocstoreUtil.class);

        }
        return docstoreUtil;
    }
    @Override
    public List<KeyValue> getKeyValues() {

        List<KeyValue> options = initLocationDetails();

        List<KeyValue> locationList = new ArrayList<KeyValue>();
        Map<String, String> map = new HashMap<String, String>();
        for (KeyValue option : options) {
            map.put(option.getKey(), option.getValue());

        }
        Map<String, Object> locationMap = sortByLocation(map);
        for (Map.Entry<String, Object> entry : locationMap.entrySet()) {
            locationList.add(new ConcreteKeyValue(entry.getKey(), (String) entry.getValue()));
        }
        return locationList;
    }

    public static List<KeyValue> initLocationDetails() {

        List<KeyValue> options = new ArrayList<KeyValue>();
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();

        Map parentCriteria1 = new HashMap();
        parentCriteria1.put("levelCode", "SHELVING");
        List<OleLocationLevel> oleLocationLevel = (List<OleLocationLevel>) businessObjectService.findMatching(OleLocationLevel.class, parentCriteria1);
        String shelvingId = oleLocationLevel.get(0).getLevelId();
        options.add(new ConcreteKeyValue("", ""));
        Map parentCriteria = new HashMap();
        parentCriteria.put("levelId", shelvingId);
        parentCriteria.put("active", true);

        Collection<OleLocation> oleLocationCollection = businessObjectService.findMatching(OleLocation.class, parentCriteria);
        for (OleLocation oleLocation : oleLocationCollection) {
            String locationName = oleLocation.getLocationName();
            String levelId = oleLocation.getLevelId();
            String levelCode = oleLocation.getLocationCode();
            boolean parentId = oleLocation.getParentLocationId() != null ? true : false;
            while (parentId) {
                Map criteriaMap = new HashMap();
                criteriaMap.put("locationId", oleLocation.getParentLocationId());
                OleLocation location = businessObjectService.findByPrimaryKey(OleLocation.class,
                        criteriaMap);
                if (locationName != null) {
                    locationName = location.getLocationName() + "/" + locationName;
                }
                if (levelCode != null) {
                    levelCode = location.getLocationCode() + "/" + levelCode;
                }
                parentId = location.getParentLocationId() != null ? true : false;
                oleLocation = location;
            }
            //String key = levelCode + "|" + locationName;
            options.add(new ConcreteKeyValue(levelCode, levelCode));
        }
        return options;
    }


    public static List<String> retrieveLocationDetailsForSuggest(String locationVal) {
        List<KeyValue> locationKeyValues = retrieveLocationDetails();
        List<String> locationValues = new ArrayList<String>();
        for (KeyValue keyValue : locationKeyValues) {
            locationValues.add(keyValue.getKey());
        }

        Pattern pattern = Pattern.compile("[?$(){}\\[\\]\\^\\\\]");
        Matcher matcher = pattern.matcher(locationVal);
        if (matcher.matches()) {
            return new ArrayList<String>();
        }

        if (!locationVal.equalsIgnoreCase("*")) {
            locationValues = Lists.newArrayList(Collections2.filter(locationValues, Predicates.contains(Pattern.compile(locationVal, Pattern.CASE_INSENSITIVE))));
        }
        Collections.sort(locationValues);
        return locationValues;
    }

    private static List<KeyValue> retrieveLocationDetails() {
        long currentTime = System.currentTimeMillis() / 1000;
        if (locationKeyValues == null) {
            locationKeyValues = initLocationDetails();
            timeLastRefreshed = currentTime;
        } else {
            if (currentTime - timeLastRefreshed > refreshInterval) {
                locationKeyValues = initLocationDetails();
                timeLastRefreshed = currentTime;
            }
        }
        return locationKeyValues;
    }

    private Map<String, Object> sortByLocation(Map<String, String> parentCriteria) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        List<String> keyList = new ArrayList<String>(parentCriteria.keySet());
        List<String> valueList = new ArrayList<String>(parentCriteria.values());
        Set<String> sortedSet = new TreeSet<String>(valueList);
        Object[] sortedArray = sortedSet.toArray();
        int size = sortedArray.length;
        for (int i = 0; i < size; i++) {
            map.put(keyList.get(valueList.indexOf(sortedArray[i])), sortedArray[i]);
        }
        return map;
    }


    public String getLocation(LocationLevel oleLocationLevel) {
        String location = null;
        if (OLEConstants.LOCATION_LEVEL_SHELVING.equalsIgnoreCase(oleLocationLevel.getLevel()))
            location = oleLocationLevel.getName();
        else
            location = getLocation(oleLocationLevel.getLocationLevel());
        if ("".equals(location) || location == null)
            return null;
        return location;
    }


    public String getShelvingLocation(Location oleLocation) throws Exception {
        if (oleLocation == null) {
            return "";
        }
        LocationLevel locationLevel = oleLocation.getLocationLevel();
        if (locationLevel != null) {
            while (locationLevel != null && locationLevel.getLocationLevel() != null && !locationLevel.getLevel().equalsIgnoreCase(OLEConstants.OleDeliverRequest.SHELVING)) {
                locationLevel = locationLevel.getLocationLevel();
            }
            return locationLevel.getName();
        }
        return null;
    }


    /**
     * This method returns location details.
     *
     * @param oleItem
     * @param object
     * @throws Exception
     */
    public void getLocation(Item oleItem, Object object, String instanceUUID) throws Exception {
        LOG.debug("Inside the getLocation method");
        try {
            Location physicalLocation = oleItem.getLocation();
            LocationLevel locationLevel = null;
            locationLevel = physicalLocation.getLocationLevel();
            getOleLocationLevel(object, locationLevel);
        } catch (Exception itemException) {
            LOG.error("--------------Invalid Item location data.---------------" + itemException);
            try {

                OleHoldings oleHoldings =getDocstoreUtil().getOleHoldings(instanceUUID);
                if (oleHoldings != null) {
                    Location physicalLocation = oleHoldings.getLocation();
                    LocationLevel locationLevel = null;
                    locationLevel = physicalLocation.getLocationLevel();
                    getOleLocationLevel(object, locationLevel);
                }
            } catch (Exception holdingException) {
                LOG.error("--------------Invalid Holding location data.---------------" + itemException);
                throw new Exception(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVAL_LOC));
            }
        }
    }


    /**
     * Populate location levels.
     *
     * @param object
     * @param locationLevel
     * @throws Exception
     */
    public void getOleLocationLevel(Object object, LocationLevel locationLevel) throws Exception {
        LOG.debug("Inside the getOleLocationLevel method");
        StringBuffer location = new StringBuffer();
        while (locationLevel.getLocationLevel() != null) {
            OleLocationLevel oleLocationLevel = getCircDeskLocationResolver().getLocationLevelByName(locationLevel.getLevel());
            OleLocation oleLocation = new OleLocation();
            if (!"".equals(locationLevel.getName())) {
                oleLocation = getLocationByLocationCode(locationLevel.getName());
            }
            setLocation(location, oleLocationLevel.getLevelName(), oleLocation.getLocationCode(), oleLocation.getLocationName(), object);
            locationLevel = locationLevel.getLocationLevel();
        }
        if (object instanceof OleDeliverRequestBo) {
            OleDeliverRequestBo oleDeliverRequestBo = (OleDeliverRequestBo) object;
            oleDeliverRequestBo.setShelvingLocation(locationLevel.getName());
        }
        if(object instanceof OleTemporaryCirculationHistory){
              OleTemporaryCirculationHistory oleTemporaryCirculationHistory=(OleTemporaryCirculationHistory)object;
            oleTemporaryCirculationHistory.setShelvingLocation(locationLevel.getName());

        }
    }

    /**
     * This method returns location using location code.
     *
     * @param locationCode
     * @return
     * @throws Exception
     */
    private OleLocation getLocationByLocationCode(String locationCode) throws Exception {
        LOG.debug("Inside the getLocationByLocationCode method");
        Map barMap = new HashMap();
        barMap.put(OLEConstants.LOC_CD, locationCode);
        List<OleLocation> matchingLocation = (List<OleLocation>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLocation.class, barMap);
        return matchingLocation.get(0);
    }

    /**
     * sets the value for location levels in Loan Document.
     *
     * @param locationLevelName
     * @param locationCode
     * @param object
     * @return OleLoanDocument
     * @throws Exception
     */
    private void setLocation(StringBuffer location, String locationLevelName, String locationCode, String locationName, Object object) throws Exception {
        LOG.debug("Inside the setLocation method");
        OleLoanDocument oleLoanDocument = null;
        OleDeliverRequestBo oleDeliverRequestBo = null;
        if (object instanceof OleLoanDocument) {
            oleLoanDocument = (OleLoanDocument) object;
        } else if (object instanceof OleDeliverRequestBo) {
            oleDeliverRequestBo = (OleDeliverRequestBo) object;
        }
        if (locationCode != null) {
            if (oleLoanDocument != null) {
                if (locationLevelName.equalsIgnoreCase(OLEConstants.LOCATION_LEVEL_SHELVING)) {
                    location.append(locationName);
                    oleLoanDocument.setItemLocation(locationCode);
                } else if (locationLevelName.equalsIgnoreCase(OLEConstants.LOCATION_LEVEL_COLLECTION)) {
                    location.append(locationName + "-");
                    oleLoanDocument.setItemCollection(locationCode);
                } else if (locationLevelName.equalsIgnoreCase(OLEConstants.LOCATION_LEVEL_LIBRARY)) {
                    location.append(locationName + "-");
                    oleLoanDocument.setItemLibrary(locationCode);
                } else if (locationLevelName.equalsIgnoreCase(OLEConstants.LOCATION_LEVEL_INSTITUTION)) {
                    location.append(locationName + "-");
                    oleLoanDocument.setItemInstitution(locationCode);
                } else if (locationLevelName.equalsIgnoreCase(OLEConstants.LOCATION_LEVEL_CAMPUS)) {
                    location.append(locationName + "-");
                    oleLoanDocument.setItemCampus(locationCode);
                }
            } else if (oleDeliverRequestBo != null) {
                if (locationLevelName.equalsIgnoreCase(OLEConstants.LOCATION_LEVEL_SHELVING)) {
                    oleDeliverRequestBo.setShelvingLocation(locationCode);
                } else if (locationLevelName.equalsIgnoreCase(OLEConstants.LOCATION_LEVEL_COLLECTION)) {
                    oleDeliverRequestBo.setItemCollection(locationCode);
                } else if (locationLevelName.equalsIgnoreCase(OLEConstants.LOCATION_LEVEL_LIBRARY)) {
                    oleDeliverRequestBo.setItemLibrary(locationCode);
                } else if (locationLevelName.equalsIgnoreCase(OLEConstants.LOCATION_LEVEL_INSTITUTION)) {
                    oleDeliverRequestBo.setItemInstitution(locationCode);
                } else if (locationLevelName.equalsIgnoreCase(OLEConstants.LOCATION_LEVEL_CAMPUS)) {
                    oleDeliverRequestBo.setItemCampus(locationCode);
                }
            }

        }
    }

}
