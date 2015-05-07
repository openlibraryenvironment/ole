package org.kuali.ole.service.impl;

import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.service.OleLocationWebService;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/21/13
 * Time: 10:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleLocationWebServiceImpl implements OleLocationWebService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleLocationWebServiceImpl.class);
    @Override
    public List<String> getItemLocation() {
        LOG.debug(" Inside get Item Location Method of OleLocationWebServiceImpl ");
        List<String> locationList = new ArrayList<String>();
        List<KeyValue> options = new ArrayList<KeyValue>();
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Map parentCriteria1 = new HashMap();
        parentCriteria1.put("levelCode", "SHELVING");
        List<OleLocationLevel> oleLocationLevel = (List<OleLocationLevel>) businessObjectService.findMatching(OleLocationLevel.class, parentCriteria1);
        if (oleLocationLevel != null && oleLocationLevel.size() > 0) {
            String shelvingId = oleLocationLevel.get(0).getLevelId();
            options.add(new ConcreteKeyValue("", ""));
            Map parentCriteria = new HashMap();
            parentCriteria.put("levelId", shelvingId);
            Collection<OleLocation> oleLocationCollection = businessObjectService.findMatching(OleLocation.class, parentCriteria);
            for (OleLocation oleLocation : oleLocationCollection) {
                options.add(new ConcreteKeyValue(oleLocation.getFullLocationPath(), oleLocation.getFullLocationPath()));
            }
        }
        Map<String, String> map = new HashMap<String, String>();
        for (KeyValue option : options) {
            map.put(option.getKey(), option.getValue());

        }
        Map<String, Object> map1 = sortByLocation(map);

        // List<KeyValue> options1 = new ArrayList<KeyValue>();
        for (Map.Entry<String, Object> entry : map1.entrySet()) {
            locationList.add((String) entry.getValue());
        }
        return locationList;
    }

    @Override
    public List<String> getItemType() {
        LOG.debug(" Inside get Item Type Method of OleLocationWebServiceImpl ");
        List<String> keyValues = new ArrayList<String>();
        Collection<OleInstanceItemType> oleInstanceItemTypes = KRADServiceLocator.getBusinessObjectService().findAll(OleInstanceItemType.class);
        for (OleInstanceItemType oleInstanceItemType : oleInstanceItemTypes) {
            String itemTypeCode = oleInstanceItemType.getInstanceItemTypeCode();
            String itemTypeName = oleInstanceItemType.getInstanceItemTypeName();
            String itemType = itemTypeCode + "," + itemTypeName;
            keyValues.add(itemType);

        }
        return keyValues;
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
