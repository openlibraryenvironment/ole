package org.kuali.ole.describe.keyvalue;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ItemType used to render the values for ItemType dropdown control.
 */
public class ItemType extends KeyValuesBase {
    public static List<KeyValue> itemTypeKeyValues = null;
    public static long timeLastRefreshed;
    public static int refreshInterval = 300;     // in seconds
    /**
     * This method returns the List of ConcreteKeyValue,,
     * ConcreteKeyValue has two arguments instanceItemTypeCode and
     * instanceItemTypeName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        Map<String, String> map = new HashMap<String, String>();
        Collection<OleInstanceItemType> oleInstanceItemTypes =
                KRADServiceLocator.getBusinessObjectService().findAllOrderBy(OleInstanceItemType.class,"instanceItemTypeName",true);
        List<KeyValue> itemTypeList = new ArrayList<KeyValue>();
        for (OleInstanceItemType type : oleInstanceItemTypes) {
            if (type.isActive()) {
                map.put(type.getInstanceItemTypeCode(), type.getInstanceItemTypeName());
            }
        }
        Map<String, Object> itemTypeMap = sortByItemType(map);
        for (Map.Entry<String, Object> entry : itemTypeMap.entrySet()) {
            itemTypeList.add(new ConcreteKeyValue(entry.getKey(), (String) entry.getValue()));
        }
        itemTypeList.add(new ConcreteKeyValue("",""));
        return itemTypeList;
    }

    public static List<KeyValue> initItemTypeDetails() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Collection<OleInstanceItemType> oleInstanceItemTypes =
                KRADServiceLocator.getBusinessObjectService().findAllOrderBy(OleInstanceItemType.class,"instanceItemTypeName",true);
        for (OleInstanceItemType type : oleInstanceItemTypes) {
            if (type.isActive()) {
                options.add(new ConcreteKeyValue(type.getInstanceItemTypeCode(), type.getInstanceItemTypeName()));
            }
        }
        return options;
    }

    public static List<String> retrieveItemTypeDetailsForSuggest(String itemTypeVal) {
        List<KeyValue> itemTypeKeyValues = retrieveItemTypeDetails();
        List<String> itemTypeValues = new ArrayList<String>();
        for (KeyValue keyValue : itemTypeKeyValues) {
            itemTypeValues.add(keyValue.getKey());
        }
        Pattern pattern = Pattern.compile("[?$(){}\\[\\]\\^\\\\]");
        Matcher matcher = pattern.matcher(itemTypeVal);
        if (matcher.matches()) {
            return new ArrayList<String>();
        }
        if (!itemTypeVal.equalsIgnoreCase("*")) {
            itemTypeValues = Lists.newArrayList(Collections2.filter(itemTypeValues, Predicates.contains(Pattern.compile(itemTypeVal, Pattern.CASE_INSENSITIVE))));
        }
        Collections.sort(itemTypeValues);
        return itemTypeValues;
    }

    private static List<KeyValue> retrieveItemTypeDetails() {
        long currentTime = System.currentTimeMillis() / 1000;
        if (itemTypeKeyValues == null) {
            itemTypeKeyValues = initItemTypeDetails();
            timeLastRefreshed = currentTime;
        } else {
            if (currentTime - timeLastRefreshed > refreshInterval) {
                itemTypeKeyValues = initItemTypeDetails();
                timeLastRefreshed = currentTime;
            }
        }
        return itemTypeKeyValues;
    }

    private Map<String, Object> sortByItemType(Map<String, String> parentCriteria) {
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
