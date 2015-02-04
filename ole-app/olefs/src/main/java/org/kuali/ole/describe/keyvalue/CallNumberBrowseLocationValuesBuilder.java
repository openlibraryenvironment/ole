package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * LocationValuesBuilder used to render the values for LocationValuesBuilder dropdown control.
 */
public class CallNumberBrowseLocationValuesBuilder extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = retrieveLocationDetails();
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

    public static List<KeyValue> retrieveLocationDetails() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Collection<OleLocation> oleLocationCollection = businessObjectService.findAll(OleLocation.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleLocation oleLocation : oleLocationCollection) {
//            String locationName = oleLocation.getLocationName();
//            String levelId = oleLocation.getLevelId();
            String locationCode = oleLocation.getLocationCode();
            String levelCode = oleLocation.getOleLocationLevel().getLevelCode();
            if ("LIBRARY".equals(levelCode)) {
                options.add(new ConcreteKeyValue(locationCode, locationCode));
            }
        }
        return options;
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

}

